package com.microschat.authenticationservice.common;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
    Optional<UserInformation> findByUsername(String username);
}
