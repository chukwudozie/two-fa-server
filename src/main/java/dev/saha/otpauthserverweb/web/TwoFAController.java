package dev.saha.otpauthserverweb.web;

import dev.saha.otpauthserverweb.payload.AndroidClientResponse;
import dev.saha.otpauthserverweb.payload.AuthServerValidationRequest;
import dev.saha.otpauthserverweb.payload.AuthServerValidationResponse;
import dev.saha.otpauthserverweb.payload.Enable2FARequest;
import dev.saha.otpauthserverweb.service.TwoFAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@CrossOrigin("*")
@RequestMapping("two-factor")
@Slf4j
public class TwoFAController {

    @Autowired
    private TwoFAService twoFAService;



    @PostMapping("/validate-web")
    public ResponseEntity<AuthServerValidationResponse>validateWebToken(@RequestBody AuthServerValidationRequest request) {
        var response = twoFAService.validateTokenForWeb(request);
        return new ResponseEntity<>(response,response.getStatus());
    }


    @PostMapping("/send-token-web/{userName}")
    public ResponseEntity<?> sendOneTokenToLoggedInWebUser(@PathVariable String userName){
        var response = twoFAService.sendTokenToUserForWebClient(userName);
        return  new ResponseEntity<>(response,response.getStatus());
    }

}
