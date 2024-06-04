package com.example.usermanagement.service;

import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        
        if (user.isLocked()) {
            throw new IllegalArgumentException("User is locked");
        }

        if (user.getPassword().equals(password)) {
            user.setTotalLogins(user.getTotalLogins() + 1);
            if (user.getTotalLogins() > 10) {
                throw new IllegalArgumentException("Password must be changed");
            }
        } else {
            user.setTotalFailedLogins(user.getTotalFailedLogins() + 1);
            if (user.getTotalFailedLogins() > 5) {
                user.setLocked(true);
            }
            userRepository.save(user);
            throw new IllegalArgumentException("Invalid username or password");
        }

        return userRepository.save(user);
    }

    public User changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        user.setPassword(newPassword);
        user.setTotalLogins(0);
        return userRepository.save(user);
    }

    public List<User> getBlockedUsers() {
        return userRepository.findByLocked(true);
    }
}
