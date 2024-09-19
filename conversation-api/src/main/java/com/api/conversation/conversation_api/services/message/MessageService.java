package com.api.conversation.conversation_api.services.message;

import com.api.conversation.conversation_api.dto.message.MessageGlobalDTO;
import com.api.conversation.conversation_api.dto.message.MessagePrivateDTO;
import com.api.conversation.conversation_api.exceptions.UserNotFoundException;
import com.api.conversation.conversation_api.models.MessageModel;
import com.api.conversation.conversation_api.models.UserModel;
import com.api.conversation.conversation_api.repositories.MessageRepository;
import com.api.conversation.conversation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository repository;

    public Map<String, String> sendGlobalMessage(MessageGlobalDTO message) throws UserNotFoundException{
        UserModel sender = userRepository.findById(message.sender())
                .orElseThrow(()-> new UserNotFoundException("Usuário não encontrado!"));

        return Map.of("user",sender.getNickname(), "message", message.message());
    }

    public Map<String, Object> sendPrivateMessage(MessagePrivateDTO dto) throws UserNotFoundException {
        UserModel sender = userRepository.findByEmail(dto.sender())
                .orElseThrow(()-> new UserNotFoundException("Usuário não encontrado!"));

        UserModel recipient = userRepository.findByEmail(dto.recipient())
                .orElseThrow(()-> new UserNotFoundException("Usuário não encontrado!"));

        System.out.println("Remetente: " + sender.getEmail());
        System.out.println("Destinatário: " + recipient.getEmail());

        MessageModel message = new MessageModel(null, LocalDateTime.now(), dto.message(), sender, recipient);

        //repository.save(message);
        return Map.of("sender", sender.getNickname(), "senderEmail", sender.getEmail(), "recipient", recipient.getNickname(), "message", message.getMessage(), "dateTime", message.getDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm")));
    }
}
