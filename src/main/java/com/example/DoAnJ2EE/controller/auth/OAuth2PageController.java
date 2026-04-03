package com.example.DoAnJ2EE.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2PageController {

    @GetMapping("/oauth2-success")
    public String oauth2SuccessPage() {
        return "auth/oauth2-success";
    }
}