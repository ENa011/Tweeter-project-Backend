package com.cogent.tweet.app.project.service;

import com.cogent.tweet.app.project.entity.Register;
import com.cogent.tweet.app.project.payload.LoginDto;
import com.cogent.tweet.app.project.payload.PasswordChangeDto;
import com.cogent.tweet.app.project.payload.RegisterDto;

import java.util.List;

public interface RegisterService {
    String createUser(RegisterDto registerDto);
    List<Register> getAllUser();
    Register getUserByUsername(String username);
    String Login(LoginDto loginDto);
    String PasswordChange(String username, PasswordChangeDto passwordChangeDto);
}
