package dev.saha.otpauthserverweb.service.impl;


import dev.saha.otpauthserverweb.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ErrorValidationService {
    public ResponseEntity<?> validate(BindingResult result){
        if (result.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            ApiResponse errors = new ApiResponse();
            for (FieldError error: result.getFieldErrors()){
                errors.setMessage(error.getField());
                errors.setStatus(HttpStatus.BAD_REQUEST);
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<?> validateRequest(BindingResult result, ResponseEntity<?> validations){

        List<String> allErrors = new ArrayList<>();
        ApiResponse apiResponse = new ApiResponse();
        var output = result.getFieldErrors().stream().findFirst().get().getDefaultMessage();
        apiResponse.setMessage(output);
        apiResponse.setStatus(HttpStatus.BAD_REQUEST);
        result.getFieldErrors().forEach(fieldError -> {
            //todo use this list later
            allErrors.add('{'+fieldError.getDefaultMessage()+'}');
        });
        log.error("error: {}",validations);
        allErrors.forEach(System.out::println);
        return  ResponseEntity.badRequest().body(apiResponse);

    }
}
