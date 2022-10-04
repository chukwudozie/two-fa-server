package dev.saha.otpauthserverweb.web;

import dev.saha.otpauthserverweb.payload.ApiResponse;
import dev.saha.otpauthserverweb.payload.UpdateEmployeeDetails;
import dev.saha.otpauthserverweb.repository.EmployeeRepo;
import dev.saha.otpauthserverweb.service.EmployeeService;
import dev.saha.otpauthserverweb.service.impl.ErrorValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class EmployeeController {


    private final EmployeeService employeeService;
    private final ErrorValidationService validationService;

    public EmployeeController(EmployeeService employeeService, ErrorValidationService validationService) {
        this.employeeService = employeeService;
        this.validationService = validationService;
    }


    @PostMapping("/{userId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long userId,
                                            @RequestBody @Valid  UpdateEmployeeDetails employeeDetails, BindingResult result) {
        ResponseEntity<?> validations = validationService.validate(result);
        if (validations != null){
            return validationService.validateRequest(result,validations);
        }
        ApiResponse response = employeeService.updateEmployeeDetails(employeeDetails,userId);
        return new ResponseEntity<>(response,response.getStatus());
    }
}
