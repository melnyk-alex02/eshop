package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.userDTOs.UserDTO;
import com.alex.eshop.dto.userDTOs.UserRegisterDTO;
import com.alex.eshop.service.UserService;
import com.alex.eshop.utils.CookieExtractorAndCreator;
import jakarta.servlet.http.Cookie;
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

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @GetMapping("/users/{Uuid}")
    public UserDTO getUser(@PathVariable String Uuid) {
        return userService.getUserByUuid(Uuid);
    }

    @PostMapping("/users/register")
    public UserDTO createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.createUser(userRegisterDTO);
    }

    @PostMapping(value = "/users/token")
    public void getToken(String email, String password, boolean rememberMe, HttpServletResponse httpServletResponse) {
        AccessTokenResponse accessTokenResponse = userService.getAccessToken(email, password);

        if (rememberMe) {
            httpServletResponse.addCookie(CookieExtractorAndCreator.createRefreshTokenCookie(accessTokenResponse.getRefreshToken()));
        }
        httpServletResponse.addCookie(CookieExtractorAndCreator.createAccessTokenCookie(accessTokenResponse.getToken()));
    }

    @PostMapping("/users/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        AccessTokenResponse accessTokenResponse = userService.refreshAccessToken(CookieExtractorAndCreator.extractCookie(request.getCookies(), "refresh_token"));

        httpServletResponse.addCookie(CookieExtractorAndCreator.createAccessTokenCookie(accessTokenResponse.getToken()));

        httpServletResponse.addCookie(CookieExtractorAndCreator.createRefreshTokenCookie(accessTokenResponse.getRefreshToken()));
    }

    @PostMapping("/users/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = "";

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("access_token")) {

                    token = cookie.getValue();
                    logger.info("Authorization" + "Bearer " + cookie.getValue());
                }
            }
        }

        request.getAttribute("Authorization");
        response.setHeader("Authorization", token);

        logger.info(request.getAttribute("Authorization") + " request ");

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
        userService.verifyEmail(code);
    }

    @PostMapping("/users/send-reset-password-email")
    public void sendEmailForPasswordReset(String email) {
        userService.sendEmailForPasswordReset(email);
    }

    @PostMapping("/users/reset-password")
    public void verifyPasswordReset(String email, String code, String password, String confirmPassword) {
        userService.verifyPasswordReset(email, code, password, confirmPassword);
    }
}