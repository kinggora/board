package com.example.board.model;

public class PostSaveDto {

    private int categoryId;
    private String writer;
    private String password;
    private String title;
    private String content;

    public PostSaveDto(int categoryId, String writer, String password, String title, String content) {
        this.categoryId = categoryId;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getWriter() {
        return writer;
    }

    public String getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }


}
