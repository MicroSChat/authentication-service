package com.microschat.authenticationservice;

import com.microschat.commonlibrary.connectivity.ConnectivityConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthenticationserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationserviceApplication.class, args);
    }

}
