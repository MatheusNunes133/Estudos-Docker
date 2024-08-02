package com.api.conversation.conversation_api.services.login;

import com.api.conversation.conversation_api.config.webSecurity.jwt.JWTService;
import com.api.conversation.conversation_api.dto.login.LoginDTO;
import com.api.conversation.conversation_api.exceptions.PasswordNotMatchException;
import com.api.conversation.conversation_api.exceptions.UserNotFoundException;
import com.api.conversation.conversation_api.models.UserModel;
import com.api.conversation.conversation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JWTService jwtService;

    public ResponseEntity<Map<String,String>> login(LoginDTO dto){
        try {
            Optional<UserModel> foundUser = repository.findByEmail(dto.email());

            if(foundUser.isEmpty()) {
                throw new UserNotFoundException("Usuário não encontrado!");
            }

            if(encoder.matches(dto.password(), foundUser.get().getPassword())){
                System.out.println(foundUser.get().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
                String jwt = jwtService.generateToken(foundUser.get());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("token", jwt));
            }else{
               throw new PasswordNotMatchException("Senha incorreta!");
            }


        }catch (Exception e){
            if(e instanceof UserNotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }
            if(e instanceof PasswordNotMatchException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            }
        }
    }
}
