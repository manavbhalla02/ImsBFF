package com.ims.bff.authZ.orgDiscovery.provider;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class RandomSixDigitOtpCodeGenerator implements OtpCodeGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public String generateOtp() {
        int value = SECURE_RANDOM.nextInt(900000) + 100000;
        return Integer.toString(value);
    }
}
