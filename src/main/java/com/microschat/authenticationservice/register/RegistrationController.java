package com.microschat.authenticationservice.register;

import com.microschat.authenticationservice.common.UserInformation;
import com.microschat.commonlibrary.UserInformationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    private UserInformation registerNewUser(@RequestBody UserInformationMessage userInformationMessage){
        return registrationService.saveNewUser(userInformationMessage);
    }
}
