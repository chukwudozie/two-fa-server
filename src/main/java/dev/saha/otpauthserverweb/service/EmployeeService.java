package dev.saha.otpauthserverweb.service;


import dev.saha.otpauthserverweb.domain.Employee;
import dev.saha.otpauthserverweb.payload.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface EmployeeService {
    ApiResponse registerEmployee(SignUpRequest request);

    ApiResponse updateEmployeePassword(String userName, UpdatePasswordRequest request);

    AllUsers getAllEmployees();

    List<Employee> getAll();
    ApiResponse login(LoginRequest loginRequest, HttpServletResponse response);
    ApiResponse updateEmployeeDetails(UpdateEmployeeDetails employeeDetails, Long userId);
}
