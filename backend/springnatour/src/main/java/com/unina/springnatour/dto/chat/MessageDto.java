package com.unina.springnatour.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MessageDto implements Serializable {
    @JsonProperty("message_id")
    private Long messageId;

    @JsonProperty("message_content")
    private String messageContent;

    @JsonProperty("sent_on")
    private LocalDateTime sentOn;

    @JsonProperty("sender_id")
    private Long senderId;

    @JsonProperty("recipient_id")
    private Long recipientId;

    @JsonProperty("chat_id")
    private Long chatId;
}
