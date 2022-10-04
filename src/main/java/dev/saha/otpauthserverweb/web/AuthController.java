package dev.saha.otpauthserverweb.web;

import dev.saha.otpauthserverweb.domain.Employee;
import dev.saha.otpauthserverweb.payload.ApiResponse;
import dev.saha.otpauthserverweb.payload.LoginRequest;
import dev.saha.otpauthserverweb.payload.SignUpRequest;
import dev.saha.otpauthserverweb.payload.UpdatePasswordRequest;
import dev.saha.otpauthserverweb.service.EmployeeService;
import dev.saha.otpauthserverweb.service.impl.ErrorValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ErrorValidationService validationService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("register")
    public ResponseEntity<?> registerNewEmployee(@Valid @RequestBody SignUpRequest request, BindingResult result) {
        ResponseEntity<?> validations = validationService.validate(result);
        if (validations != null){
            return validationService.validateRequest(result,validations);
        }
        ApiResponse response = employeeService.registerEmployee(request);
        return new ResponseEntity<>(response,response.getStatus());
    }



    @PutMapping("/update/{userName}")
    public ResponseEntity<?> updateRegisteredEmployee(@PathVariable("userName") String userName,
                                                      @Valid @RequestBody UpdatePasswordRequest request, BindingResult result) {
        ResponseEntity<?> validations = validationService.validate(result);
        if (validations != null){
            return validationService.validateRequest(result,validations);
        }
        ApiResponse response  = employeeService.updateEmployeePassword(userName,request);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result, HttpServletResponse response){
        ResponseEntity<?> validations = validationService.validate(result);
        if (validations != null){
            return validationService.validateRequest(result,validations);
        }
        ApiResponse loginResponse  = employeeService.login(request,response);
        return new ResponseEntity<>(loginResponse, loginResponse.getStatus());
    }
}
