package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.entity.User;

public interface AuthService {

    User register(User user);

    String login(String username,String password);

}