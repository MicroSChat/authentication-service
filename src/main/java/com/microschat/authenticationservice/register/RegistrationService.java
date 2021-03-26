package com.microschat.authenticationservice.register;

import com.microschat.authenticationservice.common.PasswordHasher;
import com.microschat.authenticationservice.common.UserInformation;
import com.microschat.authenticationservice.common.UserInformationRepository;
import com.microschat.commonlibrary.UserInformationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.microschat.authenticationservice.connectivity.MessagingConfiguration.REGISTRATION_USER_QUEUE_NAME;

@Service
@Slf4j
public class RegistrationService {

    private final UserInformationRepository userInformationRepository;

    @Autowired
    public RegistrationService(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    @RabbitListener(queues = REGISTRATION_USER_QUEUE_NAME)
    public void receiveRegistrationMessage(UserInformationMessage userInformationMessage){
        log.info("Received registration request on queue {}: {}",
                REGISTRATION_USER_QUEUE_NAME,
                userInformationMessage);

        saveNewUser(userInformationMessage);
    }

    public UserInformation saveNewUser(UserInformationMessage userInformationMessage){
        log.info("Got registration request {}, saving..", userInformationMessage);

        String hash = PasswordHasher.hashPassword(userInformationMessage.getPassword());
        try {
            return userInformationRepository.saveAndFlush(UserInformation.builder()
                    .username(userInformationMessage.getUsername())
                    .hash(hash)
                    .build());
        } catch (RuntimeException e){
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
