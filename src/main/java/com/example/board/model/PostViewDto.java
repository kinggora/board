package com.example.board.model;

import java.time.LocalDateTime;

public class PostViewDto {

    private int postId;
    private String category;
    private String writer;
    private String title;
    private String content;
    private String regDate;
    private String modDate;
    private int hit;

    public PostViewDto(int postId, String category, String writer, String title, String content, String regDate, String modDate, int hit) {
        this.postId = postId;
        this.category = category;
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

    public String getCategory() {
        return category;
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

    public void modifyDto(String writer, String title, String content){
        if(writer != null) this.writer = writer;
        if(title != null) this.title = title;
        if(content != null) this.content = content;
    }

}
