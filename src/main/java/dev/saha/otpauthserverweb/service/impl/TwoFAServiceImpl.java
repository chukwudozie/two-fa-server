package dev.saha.otpauthserverweb.service.impl;

import dev.saha.otpauthserverweb.domain.Employee;
import dev.saha.otpauthserverweb.domain.Token;
import dev.saha.otpauthserverweb.exceptions.OTPException;
import dev.saha.otpauthserverweb.exceptions.SignUpException;
import dev.saha.otpauthserverweb.payload.*;
import dev.saha.otpauthserverweb.repository.EmployeeRepo;
import dev.saha.otpauthserverweb.repository.TokenRepository;
import dev.saha.otpauthserverweb.service.TwoFAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
@Slf4j
public class TwoFAServiceImpl implements TwoFAService {


    private final TokenRepository tokenRepo;

    private boolean expired = true;

    private final EmployeeRepo employeeRepo;
    public TwoFAServiceImpl(TokenRepository tokenRepo, EmployeeRepo employeeRepo) {
        this.tokenRepo = tokenRepo;
        this.employeeRepo = employeeRepo;
    }


    private String generateToken() {
        Random random = new SecureRandom();
        int num = 100000 + random.nextInt(899999);
        var token = String.valueOf(num);
        if (token.length() != 6){
            log.warn("Only Six digit token permitted {} ",token);
            throw new OTPException("Must be six digit");
        }
        return token;

    }


    @Override
    public ApiResponse sendTokenToUserForWebClient(String userName) {
        checkLoginStatus(userName);
        if (!employeeRepo.existsByUserName(userName)){
            return new ApiResponse(HttpStatus.NOT_FOUND,"Invalid user");
        }
        Employee authorizedUser = employeeRepo.findEmployeeByUserName(userName).get();
        Token newToken = new Token();
        if (tokenRepo.existsByEmployee_UserName(userName)){
            Token existingToken =  tokenRepo.findTokenByEmployee_UserName(userName).get();
            if(existingToken.getCreatedAt().plusSeconds(30).isAfter(LocalTime.now())){
               log.warn("You still have an active token {}, created at: {}, to expire at: {}",
                existingToken.getToken(),existingToken.getCreatedAt(),existingToken.getCreatedAt().plusSeconds(30));
               return new ApiResponse(HttpStatus.OK,"token not expired yet",existingToken.getToken(),
                       authorizedUser.getFullName(),authorizedUser.getRole());
           } else {
                log.info("Token {} expired at {}, deleting token ....",
                  existingToken.getToken(), existingToken.getCreatedAt().plusSeconds(30));
               tokenRepo.delete(existingToken);
               newToken.setToken(generateToken());
               log.info("New token created for user as {}", newToken.getToken());
               newToken.setEmployee(employeeRepo.findEmployeeByUserName(userName).get());
               newToken.setCreatedAt(LocalTime.now());
               tokenRepo.save(newToken);
               return  new ApiResponse(HttpStatus.OK,"New token generated", newToken.getToken(),
                       authorizedUser.getFullName(),authorizedUser.getRole());
           }
        }
        newToken.setToken(generateToken());
        newToken.setEmployee(employeeRepo.findEmployeeByUserName(userName).get());
        newToken.setCreatedAt(LocalTime.now());
        tokenRepo.save(newToken);
        return  new ApiResponse(HttpStatus.OK,"New token generated", newToken.getToken(),
                authorizedUser.getFullName(),authorizedUser.getRole());
    }





    @Override
    public AuthServerValidationResponse validateTokenForWeb(AuthServerValidationRequest request){

        if(!employeeRepo.existsByUserName(request.getUserName())){
            log.error("User Name {} not registered yet or deleted",request.getUserName());
            throw new OTPException("invalid user name");
        }
        if(!tokenRepo.existsByEmployee_UserName(request.getUserName())){
            log.error("Token not yet created for user {}",request.getUserName());
            return new AuthServerValidationResponse(HttpStatus.NOT_FOUND,
                    "no token created for this user", LocalDateTime.now());
        }
        Token token = tokenRepo.findTokenByEmployee_UserName(request.getUserName()).get();
        if (token.getToken().equals(request.getToken())){
            if(token.getCreatedAt().plusSeconds(30).isBefore(LocalTime.now())) {
                //todo: token expired
                log.error("Token {} has expired",request.getToken());
                return new AuthServerValidationResponse(HttpStatus.EXPECTATION_FAILED,"Token expired",LocalDateTime.now());
            }
            else {
                tokenRepo.delete(token);
                log.info("Token {} validated and deleted",request.getToken());
                return new AuthServerValidationResponse(HttpStatus.OK,"Token validated",LocalDateTime.now());
            }
        } else {
            log.error("Invalid token {}",request.getToken());
            return new AuthServerValidationResponse(HttpStatus.BAD_REQUEST,"Invalid token",LocalDateTime.now());
        }

    }

    @Override
    public String generateSerialNumber(String input){
        StringBuilder hashText;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA256");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32)
                hashText.insert(0, "0");
        }
        catch (NoSuchAlgorithmException e) {
            log.error("Serial Number for "+input+" not generated");
            throw new OTPException(e.getMessage());
        }
        log.info("Serial Number for user with passcode {} generated as {}",input,hashText);
        return hashText.toString();
    }
    public static String encodeToken(String token){
        if (token.length() != 6){
            log.error("INVALID TOKEN "+token);
            throw new OTPException("INVALID TOKEN");
        }
        StringBuilder finalString = new StringBuilder();
        String[] chunks = token.split("(?<=\\G.{" + 2 + "})");
        //todo split the 6 digit token into two; 3 digits each
        for (String chunk : chunks) {
            int num = Integer.parseInt(chunk);
            if (num < 33) {
                num = num + 39;
                finalString.append('t').append((char) num);
            } else {
                finalString.append('f').append((char) num);
            }
        }
        System.out.println(finalString+" is what i need");
        System.out.println(Arrays.toString(chunks));
        return finalString.toString();
    }

    public static String decodeTokensFromStore(String encodedToken) {
        log.info("Encoded string from DB  {}",encodedToken);
        String []arrayOfTokens = encodedToken.trim().split("(?<=\\G.{6})");
        System.out.println(Arrays.toString(arrayOfTokens)+" "+arrayOfTokens.length);
        StringBuilder allTokens = new StringBuilder();
        for (String token : arrayOfTokens) {
            var dfv = decryptSingleToken(token);
            allTokens.append(dfv).append(" ");
        }
        return allTokens.toString().trim();
    }

    public static String decryptSingleToken(String token){
        int chunkSize = 2;
        StringBuilder builder = new StringBuilder();
        int value;
        String[] chunks = token.split("(?<=\\G.{" + chunkSize + "})");
        System.out.println(Arrays.toString(chunks)+" array");
        for (String chunk : chunks) {
            value = (chunk.charAt(0) == 't') ? (int) chunk.charAt(1) - 39 : (int) chunk.charAt(1);
            builder.append(String.format("%02d", value));
        }
        if(builder.toString().length() != 6) {
            throw  new OTPException("Incorrect decryption");
        }
        return builder.toString();
    }

    private void checkLoginStatus(String userName){
        Employee user = employeeRepo.findEmployeeByUserName(userName)
                .orElseThrow(() -> new SignUpException("User with User name: "+userName+" doesn't exist"));
        System.out.println("I passed first check");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(user.getUserName());
        System.out.println(authentication.getName());
        if (!(user.getUserName().equals(authentication.getName()))){
            throw new SignUpException("User Not logged in");
        }
        System.out.println(authentication.getName()+" Authenticated");
    }


}
