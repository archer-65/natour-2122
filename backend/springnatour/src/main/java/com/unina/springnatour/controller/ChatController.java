package com.unina.springnatour.controller;

import com.unina.springnatour.dto.chat.ChatDto;
import com.unina.springnatour.dto.chat.MessageDto;
import com.unina.springnatour.dto.chat.MessageMapper;
import com.unina.springnatour.service.ChatService;
import com.unina.springnatour.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageService messageService;

    /**
     * Message Mapping, every message will "pass" here, with the prefix configured (at the moment is /natour).
     * 1. Save the message on DB
     * 2. Convert and send to user on subscription destination: /user/{recipientId}/queue/messages
     *
     * @param messageDto Message Body
     */
    @MessageMapping("/chat")
    public void processMessage(@RequestBody MessageDto messageDto) {

        MessageDto savedMessage = messageService.saveMessage(messageDto);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(messageDto.getRecipientId()),
                "/queue/messages",
                savedMessage
        );
    }

    /**
     * Get a chat based on two users' ids
     *
     * @param senderId    first user, sender of the request
     * @param recipientId second user, the other member of the chat
     * @return ChatDTO Object with HTTP Status OK
     */
    @GetMapping("/chat/search")
    public ResponseEntity<ChatDto> getChatByUsers(@RequestParam(name = "sender_id") Long senderId,
                                                  @RequestParam(name = "recipient_id") Long recipientId) {

        ChatDto chatDto = chatService.getChatByUsers(senderId, recipientId);

        return new ResponseEntity<>(chatDto, HttpStatus.OK);
    }

    /**
     * Gets all the chats for a certain user
     *
     * @param userId the identifier of the user
     * @return a List of ChatDTO Objects after mapping from Entity, or throws Exception
     */
    @GetMapping("/chats/search")
    public ResponseEntity<List<ChatDto>> getAllChatsByUserId(@RequestParam(name = "user_id") Long userId) {

        List<ChatDto> chatDtoList = chatService.getAllChatsByUserId(userId);

        if (!chatDtoList.isEmpty()) {
            return new ResponseEntity<>(chatDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all the chats for a certain user (paged_version)
     *
     * @param userId the identifier of the user
     * @return a List of ChatDTO Objects after mapping from Entity, or throws Exception
     */
    @GetMapping("/chats/search_page")
    public ResponseEntity<List<ChatDto>> getAllChatsByUserId(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        List<ChatDto> chatDtoList = chatService.getAllChatsByUserId(userId, pageNo, pageSize);

        if (!chatDtoList.isEmpty()) {
            return new ResponseEntity<>(chatDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all messages from a certain chat
     *
     * @param id the identifier of the chat
     * @return a List of MessageDto Objects after mapping from Entity, or throws Exception
     */
    @GetMapping("/chats/{id}/messages")
    public ResponseEntity<List<MessageDto>> getAllMessagesByChatId(@PathVariable Long id) {

        List<MessageDto> messageDtoList = messageService.getAllMessagesByChatId(id);

        if (!messageDtoList.isEmpty()) {
            return new ResponseEntity<>(messageDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

