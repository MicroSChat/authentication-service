package com.microschat.authenticationservice.authentication;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.security")
@Data
public class SecurityConfigurationProperties {

    private String secretKey;

    private long tokenExpireLength;
}
