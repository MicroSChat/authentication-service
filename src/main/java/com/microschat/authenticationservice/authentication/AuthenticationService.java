package com.microschat.authenticationservice.authentication;

import com.microschat.authenticationservice.common.PasswordHasher;
import com.microschat.authenticationservice.common.UserInformation;
import com.microschat.authenticationservice.common.UserInformationRepository;
import com.microschat.authenticationservice.connectivity.MessagingConfiguration;
import com.microschat.commonlibrary.UserInformationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserInformationRepository userInformationRepository;

    @Autowired
    public AuthenticationService(TokenService tokenService, UserInformationRepository userInformationRepository) {
        this.tokenService = tokenService;
        this.userInformationRepository = userInformationRepository;
    }

    @RabbitListener(queues = MessagingConfiguration.AUTHENTICATION_USER_QUEUE_NAME)
        public String authenticateUser(UserInformationMessage userInformationMessage){
        log.info("Got authentication request {}", userInformationMessage);
        UserInformation userInformation = userInformationRepository
                .findByUsername(userInformationMessage.getUsername())
                .filter(user -> PasswordHasher.matches(userInformationMessage.getPassword(), user.getPassword()))
                .orElseThrow(() -> new AuthenticationException(String.format("User with username %s not found", userInformationMessage.getUsername())));

        return tokenService.createToken(userInformationMessage.getUsername(), userInformation.getRoles());
    }

    @RabbitListener(queues = MessagingConfiguration.VALIDATION_TOKEN_QUEUE_NAME)
    public boolean validateToken(String token){
        return tokenService.validateToken(token);
    }
}
