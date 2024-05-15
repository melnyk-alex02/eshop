package com.alex.eshop.utils;

import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public class CookieExtractorAndCreator {

    public static String extractCookie(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies == null ? new Cookie[0] : cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public static Cookie createAccessTokenCookie(String cookieValue, int maxAge) {
        Cookie cookie = new Cookie("access_token", cookieValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Cookie createRefreshTokenCookie(String cookieValue, int maxAge) {
        Cookie cookie = new Cookie("refresh_token", cookieValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}