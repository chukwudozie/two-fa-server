package dev.saha.otpauthserverweb.repository;


import dev.saha.otpauthserverweb.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeById(Long aLong);
    Optional<Employee> findEmployeeBySerialNumber(String serialNumber);
    Optional<Employee> findEmployeeByUserName(String userName);
    Optional<Employee> findEmployeeByRole(String Role);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

}
