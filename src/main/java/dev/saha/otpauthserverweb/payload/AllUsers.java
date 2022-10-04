package dev.saha.otpauthserverweb.payload;

import dev.saha.otpauthserverweb.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AllUsers {

    private List<Employee> employeeList;
    public AllUsers() {
        this.employeeList = new ArrayList<>();
    }
}
