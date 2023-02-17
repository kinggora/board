package com.example.board.web.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateDto {

    private int postId;
    private String writer;
    private String password;
    private String title;
    private String content;
    private Timestamp modDate;

    @Builder
    public PostUpdateDto(int postId, String writer, String password, String title, String content) {
        this.postId = postId;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
        this.modDate = new Timestamp(System.currentTimeMillis());
    }
}
