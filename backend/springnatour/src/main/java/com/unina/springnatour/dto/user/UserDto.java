package com.unina.springnatour.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String photoUrl;
}