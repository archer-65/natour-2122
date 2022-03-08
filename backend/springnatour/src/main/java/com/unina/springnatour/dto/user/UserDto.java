package com.unina.springnatour.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("profile_photo")
    private String profilePhoto;
}