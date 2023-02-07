package com.example.board.dto;

import java.time.LocalDateTime;

public class PostViewDto {

    private int postId;
    private int categoryId;
    private String writer;
    private String title;
    private String content;
    private String regDate;
    private String modDate;
    private int hit;

    public PostViewDto(int postId, int categoryId, String writer, String title, String content, String regDate, String modDate, int hit) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.modDate = modDate;
        this.hit = hit;
    }

    public int getPostId() {
        return postId;
    }

    public int getCategoryId() {
        return categoryId;
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

    public String getRegDate() {
        return regDate;
    }

    public String getModDate() {
        return modDate;
    }

    public int getHit() {
        return hit;
    }
}
