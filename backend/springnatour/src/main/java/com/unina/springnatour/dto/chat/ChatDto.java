package com.unina.springnatour.dto.chat;

import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ChatDto implements Serializable {
    private Long id;
    private LocalDate creationDate;
    private UserDto user1;
    private UserDto user2;
}
