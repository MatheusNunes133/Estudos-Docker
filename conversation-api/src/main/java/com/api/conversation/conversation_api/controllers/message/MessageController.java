package com.api.conversation.conversation_api.controllers.message;

import com.api.conversation.conversation_api.dto.message.MessageGlobalDTO;
import com.api.conversation.conversation_api.dto.message.MessagePrivateDTO;
import com.api.conversation.conversation_api.exceptions.UserNotFoundException;
import com.api.conversation.conversation_api.models.UserModel;
import com.api.conversation.conversation_api.services.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class MessageController {

    @Autowired
    private MessageService service;

    @Autowired
    private SimpMessagingTemplate privateMessage;

    @Autowired
    private SimpUserRegistry userRegistry;

    //Chat Global
    @MessageMapping("/chat")
    @SendTo("/global/messages")
    public Map<String, String> sendMessage(@Payload MessageGlobalDTO message) throws UserNotFoundException {
        return service.sendGlobalMessage(message);
    }

    //Chat private
    @MessageMapping("/privateChat")
    //@SendToUser("/restrict/messages")
    public void sendPrivateMessage(@Payload MessagePrivateDTO message) throws UserNotFoundException{
        Optional<UserModel> foundRecipientId = service.findUserByEmail(message.recipient());
        if(foundRecipientId.isPresent()){
            //Aparentemente, só envia a mensagem para o canal privado se mandar o ID do usuário "foundRecipientId"
            //O frontend tem que se registrar com o ID do usuário também, estou passando pelo payload do token JWT
            privateMessage.convertAndSendToUser(foundRecipientId.get().getId().toString(), "/restrict/messages", service.sendPrivateMessage(message));

        }
    }

    @GetMapping("/findRecipient/{recipientEmail}")
    public ResponseEntity<Map<String, Boolean>> recipientExists(@PathVariable String recipientEmail){
        Optional<UserModel> foundRecipient = service.findUserByEmail(recipientEmail);
        if(foundRecipient.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("recipientExists", true));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("recipientExists", false));
        }
    }
}
