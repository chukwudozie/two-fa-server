package dev.saha.otpauthserverweb.security.service;

import dev.saha.otpauthserverweb.domain.Employee;
import dev.saha.otpauthserverweb.exceptions.SignUpException;
import dev.saha.otpauthserverweb.repository.EmployeeRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepo employeeRepo;

    public UserDetailsServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user = employeeRepo.findEmployeeByUserName(username)
                .orElseThrow(() -> new SignUpException("User not found with User name: "+username));
        return UserDetailsImpl.build(user);
    }
}