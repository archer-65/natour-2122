package com.unina.springnatour.repository;

import com.unina.springnatour.model.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c " +
            "FROM Chat c " +
            "WHERE (c.user1.id = ?1 AND c.user2.id = ?2) " +
            "OR (c.user1.id = ?2 AND c.user2.id = ?1)")
    Chat getChatByUsers(Long senderId, Long recipientId);

}
