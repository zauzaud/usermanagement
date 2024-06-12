package com.example.usermanagement.kafka;

import com.example.usermanagement.model.LoginAttempt;
import com.example.usermanagement.repository.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KafkaConsumer {

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    @KafkaListener(topics = "login_attempts", groupId = "group_id")
    public void consume(String username) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setUsername(username);
        attempt.setAttemptTime(LocalDateTime.now());
        loginAttemptRepository.save(attempt);
    }
}
