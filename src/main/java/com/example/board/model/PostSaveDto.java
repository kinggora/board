package com.example.board.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSaveDto {

    private int categoryId;
    private String writer;
    private String password;
    private String title;
    private String content;

}
