package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.userDTOs.UserDTO;
import com.alex.eshop.dto.userDTOs.UserRegisterDTO;
import com.alex.eshop.service.UserService;
import com.alex.eshop.utils.CookieExtractorAndCreator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class.getName());

    int ACCESS_TOKEN_EXPIRATION_TIME = 60*60*24*5; // 5 days
    int REFRESH_TOKEN_EXPIRATION_TIME = 60*60*24*30; // 30 days

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @GetMapping("/users/{Uuid}")
    public UserDTO getUser(@PathVariable String Uuid) {
        return userService.getUserByUuid(Uuid);
    }

    @PostMapping("/users/register")
    public void createUser(@RequestBody UserRegisterDTO userRegisterDTO, boolean rememberMe, HttpServletResponse httpServletResponse) {
        AccessTokenResponse accessTokenResponse = userService.createUserAndAuthenticate(userRegisterDTO);
        if (rememberMe) {
            httpServletResponse.addCookie(CookieExtractorAndCreator.createRefreshTokenCookie(accessTokenResponse.getRefreshToken(), REFRESH_TOKEN_EXPIRATION_TIME));
        }
        httpServletResponse.addCookie(CookieExtractorAndCreator.createAccessTokenCookie(accessTokenResponse.getToken(), ACCESS_TOKEN_EXPIRATION_TIME));
    }

    @PostMapping(value = "/users/token", consumes = "application/x-www-form-urlencoded")
    public void getToken(String email, String password, boolean rememberMe, HttpServletResponse httpServletResponse) {
        AccessTokenResponse accessTokenResponse = userService.getAccessToken(email, password);

        if (rememberMe) {
            httpServletResponse.addCookie(CookieExtractorAndCreator.createRefreshTokenCookie(accessTokenResponse.getRefreshToken(), REFRESH_TOKEN_EXPIRATION_TIME));
        }

        httpServletResponse.addCookie(CookieExtractorAndCreator.createAccessTokenCookie(accessTokenResponse.getToken(), ACCESS_TOKEN_EXPIRATION_TIME));
    }

    @PostMapping("/users/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        AccessTokenResponse accessTokenResponse = userService.refreshAccessToken(CookieExtractorAndCreator.extractCookie(request.getCookies(), "refresh_token"));

        httpServletResponse.addCookie(CookieExtractorAndCreator.createAccessTokenCookie(accessTokenResponse.getToken(), ACCESS_TOKEN_EXPIRATION_TIME));

        httpServletResponse.addCookie(CookieExtractorAndCreator.createRefreshTokenCookie(accessTokenResponse.getRefreshToken(), REFRESH_TOKEN_EXPIRATION_TIME));
    }

    @PostMapping("/users/logout")
    public void logout(HttpServletResponse response) {
        response.addCookie(CookieExtractorAndCreator.createAccessTokenCookie(null, 0));
        response.addCookie(CookieExtractorAndCreator.createRefreshTokenCookie(null, 0));

        userService.logout();
    }

    @GetMapping("/users/current")
    public UserDTO getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PostMapping("/users/send-email-verification")
    public void sendEmailVerification() {
        userService.sendEmailVerification();
    }

    @PostMapping("/users/verify-email")
    public void verifyEmail(String code) {
        logger.info("verifying email");
        userService.verifyEmail(code);
    }

    @PostMapping("/users/send-reset-password-email")
    public void sendEmailForPasswordReset(String email) {
        userService.sendEmailForPasswordReset(email);
    }

    @PostMapping("/users/reset-password")
    public void verifyPasswordReset(String email, String code, String password, String confirmPassword) {
        userService.verifyPasswordReset(email, code, password, confirmPassword);
        logger.info("reseting password");
    }
}