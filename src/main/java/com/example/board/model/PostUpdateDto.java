package com.example.board.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateDto {

    private String postId;
    private String writer;
    private String password;
    private String title;
    private String content;

}
