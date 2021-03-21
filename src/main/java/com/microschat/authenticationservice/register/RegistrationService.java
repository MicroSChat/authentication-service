package com.microschat.authenticationservice.register;

import com.microschat.authenticationservice.common.PasswordHasher;
import com.microschat.authenticationservice.common.UserInformation;
import com.microschat.authenticationservice.common.UserInformationRepository;
import com.microschat.commonlibrary.UserInformationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {

    private final UserInformationRepository userInformationRepository;

    @Autowired
    public RegistrationService(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    public UserInformation saveNewUser(UserInformationMessage userInformationMessage){
        log.info("Got registration request {}, saving..", userInformationMessage);

        String hash = PasswordHasher.hashPassword(userInformationMessage.getPassword());
        return userInformationRepository.saveAndFlush(UserInformation.builder()
                .username(userInformationMessage.getUsername())
                .hash(hash)
                .build());
    }
}
