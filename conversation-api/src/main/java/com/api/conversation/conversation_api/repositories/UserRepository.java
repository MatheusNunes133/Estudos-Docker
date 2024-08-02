package com.api.conversation.conversation_api.repositories;

import com.api.conversation.conversation_api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    @Query(value = "select * from \"tb-user\" userr where userr.email = :email ",nativeQuery = true)
    Optional<UserModel> findByEmail(@Param("email") String email);
}
