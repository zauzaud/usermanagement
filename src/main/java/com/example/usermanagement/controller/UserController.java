package com.example.usermanagement.controller;

import com.example.usermanagement.dto.ChangePasswordRequest;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        // Chama para criar um novo usuário
        User createdUser = userService.createUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/")
    public ResponseEntity<User> loginUser(@Valid @RequestBody User user) {
        // Chama para realizar o login
        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(loggedInUser);
    }

    @PutMapping("/changepassword")
    public ResponseEntity<User> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // Chama para trocar a senha
        User updatedUser = userService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<User>> getBlockedUsers() {
        // Chama a lista de usuários bloqueados
        List<User> blockedUsers = userService.getBlockedUsers();
        return ResponseEntity.ok(blockedUsers);
    }

    @PutMapping("/desbloquear/{username}")
    @PreAuthorize("ADMIN")
    public ResponseEntity<User> desbloquearUsuario(@PathVariable String username) {
        // Chama para desbloquear o usuário
        User user = userService.desbloquearUsuario(username);
        return ResponseEntity.ok(user);
    }
}
