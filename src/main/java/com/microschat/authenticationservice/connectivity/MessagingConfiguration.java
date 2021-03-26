package com.microschat.authenticationservice.connectivity;

import com.microschat.commonlibrary.connectivity.ConnectivityConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    public final static String REGISTRATION_USER_QUEUE_NAME = "register-user-auth";
    public final static String AUTHENTICATION_USER_QUEUE_NAME = "auth-user-auth";
    public final static String VALIDATION_TOKEN_QUEUE_NAME = "validate-token";

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(ConnectivityConstant.APPLICATION_EXCHANGE);
    }

    @Bean
    Declarables declarables(){
        TopicExchange topicExchange = new TopicExchange(ConnectivityConstant.APPLICATION_EXCHANGE);
        Queue registrationQueue = new Queue(REGISTRATION_USER_QUEUE_NAME, false);
        Queue authenticationQueue = new Queue(AUTHENTICATION_USER_QUEUE_NAME, false);
        Queue validationQueue = new Queue(VALIDATION_TOKEN_QUEUE_NAME, false);

        return new Declarables(topicExchange,
                registrationQueue,
                authenticationQueue,
                validationQueue,
                BindingBuilder.bind(registrationQueue).to(topicExchange).with(ConnectivityConstant.USER_REGISTRATION_ROUTING_KEY),
                BindingBuilder.bind(authenticationQueue).to(topicExchange).with(ConnectivityConstant.USER_AUTHENTICATION_ROUTING_KEY),
                BindingBuilder.bind(validationQueue).to(topicExchange).with(ConnectivityConstant.AUTHENTICATION_TOKEN_VALIDATION_ROUTING_KEY));
    }
}
