package com.unina.springnatour.repository;

import com.unina.springnatour.model.chat.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChat_id(Long chatId);

    List<Message> findByChat_id(Long chatId, Sort sorting);
}
