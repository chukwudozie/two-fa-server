package dev.saha.otpauthserverweb.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {

    private HttpStatus status;
    private String message;
    private String token;

    private String fullName;

    private String role;


    public ApiResponse(String message){
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
        this.token = "";
    }


    public ApiResponse (HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public ApiResponse(){

    }

    public ApiResponse(HttpStatus status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }
}


