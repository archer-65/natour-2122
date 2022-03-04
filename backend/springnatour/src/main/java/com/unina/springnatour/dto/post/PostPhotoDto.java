package com.unina.springnatour.dto.post;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostPhotoDto implements Serializable {
    private Long id;
    private String photo;
}
