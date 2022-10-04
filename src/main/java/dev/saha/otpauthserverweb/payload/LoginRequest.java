package dev.saha.otpauthserverweb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username should not be empty")
    private String userName;
    @NotBlank(message = "Password should not be empty")
    private String password;
}
