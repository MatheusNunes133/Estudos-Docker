package com.api.conversation.conversation_api.controllers.users;

import com.api.conversation.conversation_api.dto.singup.SignUpDTO;
import com.api.conversation.conversation_api.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody SignUpDTO dto) throws Exception{
        return service.signUp(dto);
    }
}
