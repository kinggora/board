package com.example.board.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostViewDto {

    private int postId;
    private String category;
    private String writer;
    private String title;
    private String content;
    private String regDate;
    private String modDate;
    private int hit;

    public void modifyDto(String writer, String title, String content){
        if(writer != null) {
            this.writer = writer;
        }
        if(title != null) {
            this.title = title;
        }
        if(content != null) {
            this.content = content;
        }
    }
}
