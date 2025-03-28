package com.cogent.tweet.app.project.security;


import com.cogent.tweet.app.project.entity.Register;
import com.cogent.tweet.app.project.repository.RegisterRepository;
import com.cogent.tweet.app.project.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    RegisterRepository registerRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Register register = registerRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(()-> new RuntimeException());

        Set<GrantedAuthority> authorities = register
                .getRoles()
                .stream()
                .map(roles-> new SimpleGrantedAuthority(roles.getRole())).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(
                register.getUsername(),
                register.getPassword(),
                authorities
        );
    }
}
