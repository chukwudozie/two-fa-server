package dev.saha.otpauthserverweb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class LoginResponse {
    private String token;
    private String message;
    private LocalDate timeOfLogin;
    private String fullName;
    private String role;
}
