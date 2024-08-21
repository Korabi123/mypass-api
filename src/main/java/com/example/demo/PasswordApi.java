package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class PasswordApi {

    private List<PasswordModel> passwords = new ArrayList<>();

    @GetMapping("/api/passwords/get")
    public List<PasswordModel> getPasswords(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "userId", required = false) String userId,
            @RequestParam(name = "url", required = false) String url,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "createdAtMonth", required = false) String createdAtMonth
    ) {
        return passwords.stream()
        .filter(password -> !Boolean.TRUE.equals(password.getIsHidden()))

                .filter(password -> (title == null || password.getTitle().contains(title))
                && (email == null || password.getEmail().contains(email))
                && (userId == null || password.getUserId().equals(userId))
                && (date == null || password.getCreatedAt().toString().contains(date))
                && (createdAtMonth == null || password.getCreatedAtMonth().contains(createdAtMonth))
                && (url == null || password.getUrl().contains(url)))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/passwords/create")
    public List<PasswordModel> createPassword(@RequestBody List<PasswordModel> payload) {
        for (PasswordModel passwordModel : payload) {
            passwords.add(passwordModel);
        }
        return passwords;
    }

    @DeleteMapping("/api/passwords/delete")
    public List<PasswordModel> deletePassword(
            @RequestParam("passwordId") String passwordId
    ) {
        passwords.removeIf(password -> password.getId().equals(passwordId));
        return passwords;
    }

    @PatchMapping("/api/passwords/update")
    public void updatePasswords(
            @RequestParam(name = "passwordId", required = true) String passwordId,
            @RequestBody PasswordModel passwordPayload
    ) {
        passwords.stream()
                .filter(password -> password.getId().equals(passwordId))
                .findFirst()
                .ifPresent(password -> {
                    password.setPassword(passwordPayload.getPassword());
                    password.setTitle(passwordPayload.getTitle());
                    password.setCreatedAt(passwordPayload.getCreatedAt());
                    password.setEmail(passwordPayload.getEmail());
                    password.setUserId(passwordPayload.getUserId());
                    password.setUrl(passwordPayload.getUrl());
                    password.setIsHidden(passwordPayload.getIsHidden());
                });
    }
}
