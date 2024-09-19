package com.api.conversation.conversation_api.controllers.users;

import com.api.conversation.conversation_api.dto.singup.SignUpDTO;
import com.api.conversation.conversation_api.models.UserModel;
import com.api.conversation.conversation_api.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() throws Exception{
        return service.findAll();
    }
}
