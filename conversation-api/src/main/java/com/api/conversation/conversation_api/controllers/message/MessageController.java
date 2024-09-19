package com.api.conversation.conversation_api.controllers.message;

import com.api.conversation.conversation_api.dto.message.MessageGlobalDTO;
import com.api.conversation.conversation_api.dto.message.MessagePrivateDTO;
import com.api.conversation.conversation_api.exceptions.UserNotFoundException;
import com.api.conversation.conversation_api.services.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
        privateMessage.convertAndSendToUser(message.recipient(), "/restrict/messages", service.sendPrivateMessage(message));
    }
}
