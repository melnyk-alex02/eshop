package com.alex.eshop.utils;


import org.apache.commons.lang3.RandomStringUtils;

public class OneTimePasswordGenerator {
    public static String generateOTP() {
        return RandomStringUtils.randomAlphanumeric(30, 50);
    }
}