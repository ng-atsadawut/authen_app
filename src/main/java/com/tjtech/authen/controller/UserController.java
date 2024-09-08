package com.tjtech.authen.controller;

import com.tjtech.authen.entity.User;
import com.tjtech.authen.security.JwtTokenProvider;
import com.tjtech.authen.service.UserService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User newUser = userService.register(user);
//        return ResponseEntity.ok(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam @NotEmpty(message = "Username is required") String username,
                                                     @RequestParam @NotEmpty(message = "Password is required") String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByUsername(username);

        // Generate JWT token with roles included
        String jwt = jwtTokenProvider.generateToken(authentication, user.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("user", user);

//        return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateData(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateData(id, updatedUser);
        if (user != null) {
//            return ResponseEntity.ok(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        if (userService.changePassword(id, oldPassword, newPassword)) {
//            return ResponseEntity.ok().build();
            return ResponseEntity.status(HttpStatus.OK).body("Password has been changed success!");
        } else {
//            return ResponseEntity.status(400).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
//            return ResponseEntity.ok().build();
            return ResponseEntity.status(HttpStatus.OK).body("User has been successfully deleted.");
        } else {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
    }
}
