package dev.saha.otpauthserverweb.payload;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateEmployeeDetails {
    @NotBlank(message = "User name is required")
    private String userName;
    @NotBlank(message = "Full Name cannot be empty")
    private String fullName;
    @NotBlank(message = "Choose a valid application type")
    private String application;
    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email cannot be empty")
    private String email;
}

