package com.unina.springnatour.dto.chat;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MessageDto implements Serializable {
    private Long id;
    private String content;
    private LocalDateTime sentOn;
    private Long senderId;
    private Long recipientId;
    private Long chatId;
}
