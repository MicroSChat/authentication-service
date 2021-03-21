package com.microschat.authenticationservice.authentication;

import com.microschat.commonlibrary.UserInformationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody UserInformationMessage userInformationMessage){
        return ResponseEntity.ok(authenticationService.authenticateUser(userInformationMessage));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token){
        return ResponseEntity.ok(authenticationService.validateToken(token));
    }
}
