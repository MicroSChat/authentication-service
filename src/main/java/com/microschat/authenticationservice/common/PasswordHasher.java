package com.microschat.authenticationservice.common;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public final class PasswordHasher {

    private static final SCryptPasswordEncoder passwordEncoder = new SCryptPasswordEncoder();

    private PasswordHasher(){}

    public static String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public static boolean matches(String password, String encodedPassword){
        return passwordEncoder.matches(password, encodedPassword);
    }
}
