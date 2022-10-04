package dev.saha.otpauthserverweb.web;

import dev.saha.otpauthserverweb.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("test-user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> testUserEndPoint(){
        ApiResponse apiResponse = new ApiResponse("I can only get here with user security");
        return  ResponseEntity.status(404).body(apiResponse);
    }

    @PostMapping("test-admin")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> testAdminEndPoint(){
        ApiResponse apiResponse = new ApiResponse("I can only get here with admin security");
        return  ResponseEntity.status(404).body(apiResponse);
    }
}
