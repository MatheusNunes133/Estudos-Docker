package com.api.conversation.conversation_api.services.user;

import com.api.conversation.conversation_api.dto.singup.SignUpDTO;
import com.api.conversation.conversation_api.enuns.UserRolesEnum;
import com.api.conversation.conversation_api.exceptions.EmailExistsException;
import com.api.conversation.conversation_api.exceptions.NicknameExistsException;
import com.api.conversation.conversation_api.exceptions.UserNotFoundException;
import com.api.conversation.conversation_api.models.UserModel;
import com.api.conversation.conversation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public ResponseEntity<Map<String, String>> signUp(SignUpDTO dto) throws Exception{
        try {
            Optional<UserModel> foundUser = repository.findByEmail(dto.email());
            if(foundUser.isPresent() && foundUser.get().getEmail().equals(dto.email())){
                throw new EmailExistsException("Email já registrado!");
            }

            if(foundUser.isPresent() && foundUser.get().getNickname().equals(dto.nickname())){
                throw new NicknameExistsException("Nome de usuário já registrado!");
            }

            UserModel newUser = new UserModel();
            newUser.setEmail(dto.email());
            newUser.setName(dto.name());
            newUser.setNickname(dto.nickname());
            newUser.setPassword(new BCryptPasswordEncoder().encode(dto.password()));
            newUser.setRoles(List.of(UserRolesEnum.USER));

            repository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", "Usuário Criado!"));
        }catch (Exception e){
            if(e instanceof EmailExistsException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            }
            if(e instanceof NicknameExistsException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Falha ao registrar usuário!"));
        }
    }

    public ResponseEntity<?> findAll() throws Exception {
        try{
            List<UserModel> allUsers = repository.findAll();

            if(allUsers.isEmpty()){
                throw new UserNotFoundException("Nenhum usuário registrado!");
            }
            return ResponseEntity.status(HttpStatus.OK).body(allUsers);
        }catch (Exception error){
            if(error instanceof UserNotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", error.getMessage()));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
            }

        }
    }
}
