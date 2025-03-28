package com.cogent.tweet.app.project.controller;

import com.cogent.tweet.app.project.entity.Register;
import com.cogent.tweet.app.project.payload.LoginDto;
import com.cogent.tweet.app.project.payload.PasswordChangeDto;
import com.cogent.tweet.app.project.payload.RegisterDto;
import com.cogent.tweet.app.project.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody RegisterDto registerDto){
        String response = registerService.createUser(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody LoginDto loginDto){
        String response = registerService.Login(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<Register>> getAllUsers() {
        var data = registerService.getAllUser();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/users/search/{username}")
    public ResponseEntity<Register> getUserByUsername(@PathVariable("username") String username) {
        var data = registerService.getUserByUsername(username);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("/{username}/forgot")
    public ResponseEntity<String> changePassword(@PathVariable("username") String username,
                                                 @RequestBody PasswordChangeDto passwordChangeDto){
        var data = registerService.PasswordChange(username, passwordChangeDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
//        String token = authorizationHeader.replace("Bearer ", "");
//        return ResponseEntity.ok("Logged out successfully");
//    }
}
