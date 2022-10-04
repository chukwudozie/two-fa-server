package dev.saha.otpauthserverweb.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class OTPExceptionHandler {
    @ExceptionHandler(OTPException.class)
    public ResponseEntity<Object> twoFactorRequestHandler(OTPException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String errorType = e.getClass().getSimpleName().toUpperCase();
        int position =  errorType.indexOf("EXCEPTION");
        errorType =  errorType.substring(0, position) + " " + errorType.substring(position);
        ExceptionPayLoad payLoad = new ExceptionPayLoad(e.getMessage(),e, errorType, badRequest,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(payLoad,badRequest);

    }


    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<Object> loginExceptionHandler(SignUpException e){
        HttpStatus badSignup = HttpStatus.CONFLICT;
        String errorType = e.getClass().getSimpleName().toUpperCase();
        int position =  errorType.indexOf("EXCEPTION");
        errorType =  errorType.substring(0, position) + " " + errorType.substring(position);
        ExceptionPayLoad payLoad = new ExceptionPayLoad(e.getMessage(),e, errorType, badSignup,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(payLoad.getMessage(),badSignup);

    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<Object> emailExceptionHandler(EmailException e){
        HttpStatus emailError = HttpStatus.NOT_IMPLEMENTED;
        String errorType = e.getClass().getSimpleName().toUpperCase();
        int position =  errorType.indexOf("EXCEPTION");
        errorType =  errorType.substring(0, position) + " " + errorType.substring(position);
        ExceptionPayLoad payLoad = new ExceptionPayLoad(e.getMessage(),e, errorType, emailError,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(payLoad.getMessage(),emailError);

    }
}
