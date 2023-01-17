package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.UserDTO;
import com.alex.eshop.dto.UserRegisterDTO;
import com.alex.eshop.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")// TODO: add roles
    @GetMapping("/users/{Uuid}")
    public UserDTO getUser(@PathVariable String Uuid) {
        return userService.getUserByUuid(Uuid);
    }

    @PostMapping("/users/register")
    public UserDTO createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.createUser(userRegisterDTO);
    }
}
