package com.microschat.authenticationservice.authentication;

import com.microschat.authenticationservice.common.PasswordHasher;
import com.microschat.authenticationservice.common.UserInformation;
import com.microschat.authenticationservice.common.UserInformationRepository;
import com.microschat.commonlibrary.UserInformationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserInformationRepository userInformationRepository;

    @Autowired
    public AuthenticationService(TokenService tokenService, UserInformationRepository userInformationRepository) {
        this.tokenService = tokenService;
        this.userInformationRepository = userInformationRepository;
    }

    public String authenticateUser(UserInformationMessage userInformationMessage){
        UserInformation userInformation = userInformationRepository
                .findByUsername(userInformationMessage.getUsername())
                .filter(user -> PasswordHasher.matches(userInformationMessage.getPassword(), user.getPassword()))
                .orElseThrow(() -> new AuthenticationException(String.format("User with username %s not found", userInformationMessage.getUsername())));

        return tokenService.createToken(userInformationMessage.getUsername(), userInformation.getRoles());
    }

    public boolean validateToken(String token){
        return tokenService.validateToken(token);
    }
}
