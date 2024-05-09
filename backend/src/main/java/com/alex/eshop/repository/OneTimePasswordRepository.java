package com.alex.eshop.repository;

import com.alex.eshop.constants.OneTimePasswordType;
import com.alex.eshop.entity.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {
    Optional<OneTimePassword> findByUserEmailAndValueAndType(String email, String value, OneTimePasswordType type);

    void deleteAllByExpireAtBefore(ZonedDateTime now);
}
