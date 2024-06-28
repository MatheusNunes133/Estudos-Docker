package com.api.conversation.conversation_api.controllers;

import com.api.conversation.conversation_api.dto.login.LoginDTO;
import com.api.conversation.conversation_api.services.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService service;

    @PostMapping()
    public ResponseEntity<Map<String, String>> login(LoginDTO login){
        return service.login(login);
    }
}
