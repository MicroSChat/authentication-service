package com.microschat.authenticationservice.register;

import com.microschat.commonlibrary.connectivity.ConnectivityConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistrationMessagingConfiguration {

    public final static String REGISTRATION_USER_QUEUE_NAME = "register-user-auth";

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(ConnectivityConstant.APPLICATION_EXCHANGE);
    }

    @Bean
    Declarables declarables(){
        TopicExchange topicExchange = new TopicExchange(ConnectivityConstant.APPLICATION_EXCHANGE);
        Queue registrationQueue = new Queue(REGISTRATION_USER_QUEUE_NAME, false);

        return new Declarables(topicExchange,
                registrationQueue,
                BindingBuilder.bind(registrationQueue).to(topicExchange).with(ConnectivityConstant.REGISTRATION_USER_ROUTING_KEY));
    }
}
