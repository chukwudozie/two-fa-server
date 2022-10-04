package dev.saha.otpauthserverweb.web;

import dev.saha.otpauthserverweb.domain.Employee;
import dev.saha.otpauthserverweb.repository.EmployeeRepo;
import dev.saha.otpauthserverweb.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final EmployeeService employeeService;
    private final EmployeeRepo employeeRepo;

    public AdminController(EmployeeService employeeService, EmployeeRepo employeeRepo) {
        this.employeeService = employeeService;
        this.employeeRepo = employeeRepo;
    }

    @GetMapping("get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> getAll(){
        return ResponseEntity.ok(employeeService.getAll());
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long userId){
         employeeRepo.deleteById(userId);
    }
}

