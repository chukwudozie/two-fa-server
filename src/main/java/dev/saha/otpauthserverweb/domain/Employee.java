package dev.saha.otpauthserverweb.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String password;// if timeOfFirstLogin is true, password = username@year

    private String serialNumber;
    private String email;
    private String fullName;

    private String application;

    private String role;
    private boolean firstTimeLogin;
    private LocalDate createdDate;



}
