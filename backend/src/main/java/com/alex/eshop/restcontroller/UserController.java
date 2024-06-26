package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.userDTOs.UserDTO;
import com.alex.eshop.dto.userDTOs.UserRegisterDTO;
import com.alex.eshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @GetMapping("/users/{Uuid}")
    public UserDTO getUser(@PathVariable String Uuid) {
        return userService.getUserByUuid(Uuid);
    }

    @PostMapping("/users/register")
    public UserDTO createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.createUser(userRegisterDTO);
    }
}
