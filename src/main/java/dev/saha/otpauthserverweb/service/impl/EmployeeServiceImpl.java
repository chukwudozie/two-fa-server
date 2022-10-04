package dev.saha.otpauthserverweb.service.impl;

import dev.saha.otpauthserverweb.domain.Employee;
import dev.saha.otpauthserverweb.exceptions.SignUpException;
import dev.saha.otpauthserverweb.payload.*;
import dev.saha.otpauthserverweb.repository.EmployeeRepo;
import dev.saha.otpauthserverweb.security.JwtUtils;
import dev.saha.otpauthserverweb.security.service.PasswordValidator;
import dev.saha.otpauthserverweb.service.EmailService;
import dev.saha.otpauthserverweb.service.EmployeeService;
import dev.saha.otpauthserverweb.service.TwoFAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepo employeeRepo;
    private final TwoFAService twoFAService;
    private final JwtUtils utils;
    private final PasswordValidator passwordValidator;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    private final EmailService emailService;
    private static final String updatePasswordURL = "https://localhost:3000/updatePassword";

    private final UserDetailsService userDetailsService;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, TwoFAService twoFAService,
                               JwtUtils utils, PasswordValidator passwordValidator, AuthenticationManager authenticationManager,
                               PasswordEncoder encoder, EmailService emailService, UserDetailsService userDetailsService) {
        this.employeeRepo = employeeRepo;
        this.twoFAService = twoFAService;
        this.utils = utils;
        this.passwordValidator = passwordValidator;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.emailService = emailService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public ApiResponse registerEmployee(SignUpRequest request) {
        if(employeeRepo.existsByUserName(request.getUserName())){
            log.error("Username {} already exist",request.getUserName());
           return new ApiResponse(HttpStatus.BAD_REQUEST,"User name already exist");
        }
        if (employeeRepo.existsByEmail(request.getEmail())) {
            log.error("Email already exist {}", request.getEmail());
           return new ApiResponse(HttpStatus.BAD_REQUEST,"Email already exist");
        }

        Employee employee = new Employee();
        employee.setUserName(request.getUserName());
        employee.setFullName(request.getFullName());
        var role = request.getAdmin().equalsIgnoreCase("yes") ? "ADMIN":"USER";
        employee.setRole(role);
        employee.setEmail(request.getEmail());
        employee.setApplication(request.getApplication());
        employee.setFirstTimeLogin(true);
        employee.setSerialNumber(twoFAService.generateSerialNumber(request.getUserName()));
        employee.setCreatedDate(LocalDate.now());
        employee.setPassword(generateDefaultPasswordForFirstTimeUser(request.getUserName()));
//        emailService.sendEmailForPasswordUpdateToNewUser(request.getEmail(),request.getUserName(),
//                generateDefaultPasswordForFirstTimeUser(request.getUserName()),updatePasswordURL);
        employeeRepo.save(employee);
        log.info("Successfully saved Employee: {}",employee);
        log.info("Update info successfully sent to newly registered user {}",request.getUserName());
        return new ApiResponse(HttpStatus.OK, "REGISTRATION SUCCESSFUL");
    }

    @Override
    public AllUsers getAllEmployees() {
        AllUsers users = new AllUsers();
       users.setEmployeeList(employeeRepo.findAll());
       log.info("Number of users currently in the DB: {}",users.getEmployeeList().size());
       return users;
    }

    public List<Employee> getAll() {
        log.info("Number of users currently in the DB: {}",employeeRepo.findAll().size());
        return employeeRepo.findAll();
    }

    @Override
    public ApiResponse login(LoginRequest loginRequest, HttpServletResponse response) {

        try {
            var defaultPassword = loginRequest.getUserName().substring(0,1).toUpperCase().concat(loginRequest.getUserName()
                    .substring(1)).concat("@").concat(String.valueOf(LocalDate.now().getYear()));
            if (employeeRepo.existsByUserName(loginRequest.getUserName()) && loginRequest.getPassword().equals(defaultPassword) ){
                log.error("You can't login with your default password, update your password first");
                return new ApiResponse(HttpStatus.NOT_ACCEPTABLE,"You can only login after updating your default password " +
                        "http://localhost:3000/updatePassword/");
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),
                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUserName());
            System.out.println("User details:" +userDetails.toString());
            if (!employeeRepo.existsByUserName(loginRequest.getUserName())){
                log.error("User not found with user name: {}", loginRequest.getUserName());
                return new ApiResponse(HttpStatus.NO_CONTENT,"User name "+loginRequest.getUserName()+" invalid");
            }
            Employee user = employeeRepo.findEmployeeByUserName(loginRequest.getUserName()).get();
            String jwt = utils.generateJwtToken(authentication);
            if (user.isFirstTimeLogin()) {
               return new ApiResponse(HttpStatus.NOT_FOUND,"Kindly update your password before logging in");
            } else {
                Enable2FARequest faRequest = new Enable2FARequest();
                faRequest.setUserName(loginRequest.getUserName());
                response.addHeader("Authorization", "Bearer " + jwt);
                ApiResponse successResponse = new ApiResponse();
                successResponse.setStatus(HttpStatus.OK);
                successResponse.setMessage("LOGIN SUCCESSFUL FOR "+user.getFullName());
                successResponse.setToken(jwt);
                successResponse.setRole(user.getRole());    
                successResponse.setFullName(user.getFullName());
                log.info("Successful login for "+user.getFullName()+": jwt token: "+successResponse.getToken());
                return successResponse;
            }

        }catch (BadCredentialsException | DisabledException e) {
            return new ApiResponse(HttpStatus.UNAUTHORIZED,"Login failed. "+e.getMessage()+" incorrect username or password");
        }

    }

    @Override
    public ApiResponse updateEmployeePassword(String userName, UpdatePasswordRequest request) {
        if (!employeeRepo.existsByUserName(request.getUserName())){
            log.error("Invalid user name provided {}",request.getUserName());
           return new ApiResponse("Invalid user name provided");
        }
        Employee savedEmployee = employeeRepo.findEmployeeByUserName(request.getUserName()).get();

        boolean validatePassword = passwordValidator.validate(request.getPassword());

        if(!request.getUserName().equals(savedEmployee.getUserName())) {
            log.error("User name provided {} doesn't match the user: {}",
                    request.getUserName(), savedEmployee.getUserName());
            return new ApiResponse("User name does not match, you can only update your own password");
        }
        if(!savedEmployee.isFirstTimeLogin()) {
            log.warn("Password already updated for user, login with your new password");
            return new ApiResponse(HttpStatus.ALREADY_REPORTED,
                    "Password already updated for user, login with your new password");
        }

        if (!request.getDefaultPassword().equals(savedEmployee.getPassword())) {
            log.error("Incorrect default password provided {}",
                    request.getDefaultPassword());
            return new ApiResponse("Incorrect default password provided, kindly check your mail for the correct one");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.error("New password provided {} doesn't match the confirm password provided: {}",
                    request.getPassword(), request.getConfirmPassword());
            return new ApiResponse("Password does not match");
        }

        if (!validatePassword) {
            return new ApiResponse(HttpStatus.EXPECTATION_FAILED,
             "Password must be upto 8 characters, at least one capital letter," +
                     " one small letter, one digit and special character!");
        }
        //todo: encrypt this password before persisting
        savedEmployee.setPassword(encoder.encode(request.getPassword()));
        savedEmployee.setFirstTimeLogin(false);
        employeeRepo.save(savedEmployee);
        Enable2FARequest faRequest = new Enable2FARequest();
        faRequest.setUserName(request.getUserName());
        log.info("New Password successfully saved for user as : {}",request.getPassword());
        ApiResponse successResponse = new ApiResponse();
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("Password successfully updated, you can proceed to login");
        return successResponse;

    }


    @Override
    public ApiResponse updateEmployeeDetails(UpdateEmployeeDetails employeeDetails, Long userId) {
        if(employeeRepo.findEmployeeById(userId).isEmpty()) {
            throw new SignUpException("Invalid user id");
        }
        Employee savedEmployee = employeeRepo.findEmployeeById(userId).get();
        if(employeeRepo.existsByUserName(employeeDetails.getUserName())){
            return new ApiResponse(HttpStatus.BAD_REQUEST,"user name already taken");
        }
        savedEmployee.setUserName(employeeDetails.getUserName());
        savedEmployee.setFullName(employeeDetails.getFullName());
        savedEmployee.setEmail(employeeDetails.getEmail());
        savedEmployee.setApplication(employeeDetails.getApplication());
        employeeRepo.save(savedEmployee);
        return new ApiResponse(HttpStatus.OK,"Successfully updated user information");
    }

    private String generateDefaultPasswordForFirstTimeUser(String userName) {
        if(userName.isEmpty())throw new SignUpException("User name cannot be null");
        StringBuilder defaultPassword = new StringBuilder();
        defaultPassword.append(userName.substring(0, 1).toUpperCase()).
        append(userName.substring(1)).append('@').append(LocalDate.now().getYear());
        log.info("{} is your default password",defaultPassword);
        return defaultPassword.toString();
    }



}
