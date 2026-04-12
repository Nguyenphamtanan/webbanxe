package com.example.DoAnJ2EE.security;

import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println("LOAD USER BY = " + usernameOrEmail);

        User user = userRepository.findByEmail(usernameOrEmail)
                .orElseGet(() -> userRepository.findByUsername(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Không tìm thấy user với username/email: " + usernameOrEmail
                        )));

        System.out.println("FOUND USER = " + user.getUsername() + " | ROLE = " + user.getRole());

        return new CustomUserDetails(user);
    }
}