package dev.saha.otpauthserverweb.payload;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfo {
    private String userName;
    private String fullName;
    private  String role;
    private AndroidClientResponse clientResponse;
}
