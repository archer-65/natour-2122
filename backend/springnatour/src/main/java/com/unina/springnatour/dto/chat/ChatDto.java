package com.unina.springnatour.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ChatDto implements Serializable {
    @JsonProperty("chat_id")
    private Long chatId;

    @JsonProperty("chat_creation_date")
    private LocalDate chatCreationDate;

    @JsonProperty("chat_user_1")
    private UserDto chatUser1;

    @JsonProperty("chat_user_2")
    private UserDto chatUser2;
}
