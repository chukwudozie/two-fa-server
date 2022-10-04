package dev.saha.otpauthserverweb.service;


import dev.saha.otpauthserverweb.payload.*;

public interface TwoFAService {


    AuthServerValidationResponse validateTokenForWeb(AuthServerValidationRequest request);
    String generateSerialNumber(String input);

    ApiResponse sendTokenToUserForWebClient(String userName);

}
