package com.cogent.tweet.app.project.service.Impl;

import com.cogent.tweet.app.project.entity.Register;
import com.cogent.tweet.app.project.entity.Roles;
import com.cogent.tweet.app.project.exceptions.MissMatchIdException;
import com.cogent.tweet.app.project.payload.LoginDto;
import com.cogent.tweet.app.project.payload.PasswordChangeDto;
import com.cogent.tweet.app.project.payload.RegisterDto;
import com.cogent.tweet.app.project.repository.RegisterRepository;
import com.cogent.tweet.app.project.repository.RolesRepository;
import com.cogent.tweet.app.project.security.JwtTokenProvider;
import com.cogent.tweet.app.project.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    RegisterRepository registerRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public String createUser(RegisterDto registerDto) {

        if(registerRepository.existsByUsername(registerDto.getUsername())){
            throw new MissMatchIdException("User ID already in use");
        }
        if(registerRepository.existsByEmail(registerDto.getEmail())){
            throw new MissMatchIdException("Email already in use");
        }
        Register register = new Register();
        register.setFirstName(registerDto.getFirstname());
        register.setLastName(registerDto.getLastname());
        register.setContactNumber(registerDto.getContactNumber());
        register.setEmail(registerDto.getEmail());
        register.setUsername(registerDto.getUsername());

        if(registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            register.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        } else throw new MissMatchIdException("Password and confirmed password do not match");;

        Set<Roles> roles = new HashSet<>();
        Roles userRoles = rolesRepository.findByRole("ROLE_USER").get();
        roles.add(userRoles);
        register.setRoles(roles);
        registerRepository.save(register);
        return "User register Successfully";
    }

    @Override
    public List<Register> getAllUser() {
        return registerRepository.findAll();
    }

    @Override
    public Register getUserByUsername(String username) {

        return registerRepository.findByUsername(username);
    }

    @Override
    public String Login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(), loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        System.out.println(token);
        return token;
    }

    @Override
    public String PasswordChange(String username, PasswordChangeDto passwordChangeDto) {
        Register register = registerRepository.findByUsername(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(username.equals(((UserDetails) principal).getUsername())){
                register.setPassword(passwordEncoder.encode(passwordChangeDto.getPassword()));
                registerRepository.save(register);
                return "Password Successfully changed";
            } else throw new MissMatchIdException("Not authorized");
        } else throw new MissMatchIdException("authentication info not found");
    }
}
