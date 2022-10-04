package dev.saha.otpauthserverweb.payload;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdatePasswordRequest {

    @NotBlank(message = "User name cannot be empty")
    private String userName;

    @NotBlank(message = "default password cannot be empty")
    private String defaultPassword;

    @NotBlank(message = "New Password cannot be empty")
    private String password;

    @NotBlank(message = "Please fill the new password again")
    private String confirmPassword;
}
