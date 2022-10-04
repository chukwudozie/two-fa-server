package dev.saha.otpauthserverweb.exceptions;

public class OTPException extends RuntimeException {
    public OTPException(String message){
        super(message);
    }
}
