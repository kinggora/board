package com.example.board.web.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSaveDto {

    private int postId;
    private int categoryId;
    private String writer;
    private String password;
    private String title;
    private String content;
    private Timestamp regDate;

    @Builder
    public PostSaveDto(int postId, int categoryId, String writer, String password, String title, String content) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
        this.regDate = new Timestamp(System.currentTimeMillis());
    }
}
