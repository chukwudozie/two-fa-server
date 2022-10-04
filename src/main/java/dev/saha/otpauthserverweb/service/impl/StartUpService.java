package dev.saha.otpauthserverweb.service.impl;

import dev.saha.otpauthserverweb.domain.Employee;
import dev.saha.otpauthserverweb.repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StartUpService {


    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder encoder;

    public void createAdmin(){
        Optional<Employee> existingAdmin = employeeRepo.findEmployeeByRole("ADMIN");
        if(existingAdmin.isEmpty()){
            Employee newAdmin = new Employee();
            newAdmin.setEmail("admin@seaico.com");
            newAdmin.setApplication("Calypso");
            newAdmin.setUserName("admin");
            newAdmin.setRole("ADMIN");
            newAdmin.setFullName("ADMIN");
            newAdmin.setCreatedDate(LocalDate.now());
            newAdmin.setFirstTimeLogin(false);
            newAdmin.setPassword(encoder.encode("Admin@1234"));
            employeeRepo.save(newAdmin);
        }

    }


}