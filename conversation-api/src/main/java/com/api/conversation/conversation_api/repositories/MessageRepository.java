package com.api.conversation.conversation_api.repositories;

import com.api.conversation.conversation_api.models.PrivateMessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<PrivateMessageModel, UUID> {
}
