package com.alex.eshop.service;

import com.alex.eshop.constants.OneTimePasswordType;
import com.alex.eshop.entity.OneTimePassword;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.repository.OneTimePasswordRepository;
import com.alex.eshop.utils.OneTimePasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class OneTimePasswordService {
    private final OneTimePasswordRepository oneTimePasswordRepository;

    public OneTimePassword createOneTimePassword(String email, OneTimePasswordType type) {
        OneTimePassword oneTimePassword = new OneTimePassword();
        oneTimePassword.setUserEmail(email);
        oneTimePassword.setType(type);
        oneTimePassword.setValue(OneTimePasswordGenerator.generateOTP());
        oneTimePassword.setExpireAt(ZonedDateTime.now().plusDays(3));

        return oneTimePasswordRepository.save(oneTimePassword);
    }

    public void validateOneTimePassword(String email, String value, OneTimePasswordType type) {
        OneTimePassword oneTimePassword = oneTimePasswordRepository.findByUserEmailAndValueAndType(email, value, type)
                .orElseThrow(() -> new DataNotFoundException("OTP was not found"));

        if (oneTimePassword.getExpireAt().isBefore(ZonedDateTime.now())) {
            throw new InvalidDataException("OTP is expired");
        }

        oneTimePasswordRepository.delete(oneTimePassword);
    }

    @Scheduled(cron = "0 * * * * *")
    protected void checkExpiredOneTimePasswords() {

    }
}