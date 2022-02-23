package com.unina.springnatour.service;

import com.unina.springnatour.dto.chat.ChatDto;
import com.unina.springnatour.dto.chat.ChatMapper;
import com.unina.springnatour.dto.chat.MessageDto;
import com.unina.springnatour.dto.user.UserMapper;
import com.unina.springnatour.model.User;
import com.unina.springnatour.model.chat.Chat;
import com.unina.springnatour.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * Tricky method, contains business logic to get OR create and get a chat
     * @param senderId sender of the request
     * @param recipientId other member of the chat
     * @return ChatDTO Object, the requested chat
     */
    public ChatDto getChatByUsers(Long senderId, Long recipientId) {

        // First, get the chat
        Chat chat = chatRepository.getChatByUsers(senderId, recipientId);

        // If the chat doesn't exist
        if (chat == null) {

            // First, new Chat using no-args constructor
            chat = new Chat();

//            chat.setUser1(User.builder().id(senderId).build());
//            chat.setUser2(User.builder().id(recipientId).build());

            // Relies on UserService and UserMapper to get additional user info
            chat.setUser1(userMapper.toEntity(
                    userService.getUserById(senderId)
            ));
            chat.setUser2(userMapper.toEntity(
                    userService.getUserById(recipientId)
            ));

            chat.setCreationDate(LocalDateTime.now());

            // Persist
            chat = chatRepository.save(chat);
        }

        return chatMapper.toDto(chat);
    }

    public List<ChatDto> getAllChatsByUserId(Long userId) {
        return chatMapper.toDto(chatRepository.getAllChatsByUser(userId)
                .stream()
                .toList());
    }
}
