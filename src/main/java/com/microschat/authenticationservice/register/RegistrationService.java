package com.microschat.authenticationservice.register;

import com.microschat.authenticationservice.common.PasswordHasher;
import com.microschat.authenticationservice.common.UserInformation;
import com.microschat.authenticationservice.common.UserInformationRepository;
import com.microschat.commonlibrary.UserInformationMessage;
import com.microschat.commonlibrary.connectivity.ConnectivityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {

    private final UserInformationRepository userInformationRepository;

    @Autowired
    public RegistrationService(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    @RabbitListener(queues = ConnectivityConstant.REGISTRATION_USER_AUTH_QUEUE_NAME)
    public void receiveRegistrationMessage(UserInformationMessage userInformationMessage){
        log.info("Received registration request on queue {}: {}",
                ConnectivityConstant.REGISTRATION_USER_AUTH_QUEUE_NAME,
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
