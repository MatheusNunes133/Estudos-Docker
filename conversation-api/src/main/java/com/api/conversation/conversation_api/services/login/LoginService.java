package com.api.conversation.conversation_api.services.login;

import com.api.conversation.conversation_api.dto.login.LoginDTO;
import com.api.conversation.conversation_api.exceptions.UserNotFoundException;
import com.api.conversation.conversation_api.models.UserModel;
import com.api.conversation.conversation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository repository;

    public ResponseEntity<Map<String,String>> login(LoginDTO login){
        try {
            Optional<UserModel> foundUser = repository.findByEmail(login.email());
            if(foundUser.isEmpty()){
                throw new UserNotFoundException("Usuário não encontrado");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(Map.of("sucess", "Usuário encontrado"));
        }catch (Exception e){
            if(e instanceof UserNotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Falha ao buscar usuário"));
            }
        }
    }
}
