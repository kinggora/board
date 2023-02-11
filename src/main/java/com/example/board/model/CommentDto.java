package com.example.board.model;

public class CommentDto {

    private String regDate;
    private String content;

    public CommentDto(String regDate, String content) {
        this.regDate = regDate;
        this.content = content;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getContent() {
        return content;
    }
}
