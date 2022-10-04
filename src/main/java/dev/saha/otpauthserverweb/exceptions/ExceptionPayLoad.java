package dev.saha.otpauthserverweb.exceptions;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
//@JsonComponent
public class ExceptionPayLoad {
    private final String errorType;
    private final String message                ;
    private final HttpStatus status;
    private final ZonedDateTime time;


    public ExceptionPayLoad(String message, Throwable t, String errorType, HttpStatus status, ZonedDateTime time){

        this.errorType = errorType;
        this.message = message;
        this.status = status;
        this.time = time;
    }
}
