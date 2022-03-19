package com.unina.springnatour.service;

import com.unina.springnatour.dto.chat.MessageDto;
import com.unina.springnatour.dto.chat.MessageMapper;
import com.unina.springnatour.model.chat.Message;
import com.unina.springnatour.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    /**
     * Save the message for certain chat
     *
     * @param messageDto MessageDTO containing required fields
     * @return
     */
    public MessageDto saveMessage(MessageDto messageDto) {
        Message savedMessage = messageRepository.save(messageMapper.toEntity(messageDto));
        return messageMapper.toDto(savedMessage);
    }

    /**
     * Get all messages by chat
     *
     * @param chatId the identifier of the chat
     * @return List of MessageDTO
     */
    public List<MessageDto> getAllMessagesByChatId(Long chatId) {
        return messageMapper.toDto(messageRepository.findByChat_id(chatId, Sort.by("sentOn").descending())
                .stream()
                .toList());
    }
}
