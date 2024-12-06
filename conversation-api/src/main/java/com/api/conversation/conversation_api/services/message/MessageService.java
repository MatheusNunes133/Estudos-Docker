package com.api.conversation.conversation_api.services.message;

import com.api.conversation.conversation_api.dto.message.MessageGlobalDTO;
import com.api.conversation.conversation_api.dto.message.MessagePrivateDTO;
import com.api.conversation.conversation_api.exceptions.UserNotFoundException;
import com.api.conversation.conversation_api.models.PrivateMessageModel;
import com.api.conversation.conversation_api.models.UserModel;
import com.api.conversation.conversation_api.repositories.MessageRepository;
import com.api.conversation.conversation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository repository;

    public Map<String, String> sendGlobalMessage(MessageGlobalDTO dto) throws UserNotFoundException{
        UserModel sender = userRepository.findById(dto.sender())
                .orElseThrow(()-> new UserNotFoundException("Usuário não encontrado!"));

        return Map.of("sender",sender.getNickname(), "message", dto.message(), "dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm")));
    }

    public Map<String, ?> sendPrivateMessage(MessagePrivateDTO dto) throws UserNotFoundException {
            Optional<UserModel> sender = userRepository.findByEmail(dto.sender());
            if(sender.isEmpty()){
                throw new UserNotFoundException("Remetente não encontrado!");
            }

            Optional<UserModel> recipient = userRepository.findByEmail(dto.recipient());
            if(recipient.isEmpty()){
                throw new UserNotFoundException("Destinatário não encontrado!");
            }

            PrivateMessageModel message = new PrivateMessageModel(null, LocalDateTime.now(), dto.message(), sender.get(), recipient.get());

            //repository.save(message);
            return Map.of("sender", sender.get().getNickname(), "senderEmail", sender.get().getEmail(), "recipient", recipient.get().getNickname(), "message", message.getMessage(), "dateTime", message.getDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm")));

    }

    public Optional<UserModel> findUserByEmail(String recipientEmail) {
        return userRepository.findByEmail(recipientEmail);
    }
}
