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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + email));

        return new CustomUserDetails(user);
    }
}