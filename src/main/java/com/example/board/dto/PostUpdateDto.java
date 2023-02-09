package com.example.board.dto;

public class PostUpdateDto {

    private String postId;

    private String writer;
    private String password;
    private String title;
    private String content;

    public PostUpdateDto(String postId, String writer, String password, String title, String content) {
        this.postId = postId;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPassword() {
        return password;
    }


}
