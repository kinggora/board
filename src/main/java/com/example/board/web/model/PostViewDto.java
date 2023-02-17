package com.example.board.web.model;

import com.example.board.web.util.TimestampFormatter;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class PostViewDto {

    private int postId;
    private String category;
    private String writer;
    private String title;
    private String content;
    private Timestamp regDate;
    private Timestamp modDate;
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

    public String regDateToString(){
        return TimestampFormatter.timestampToString(regDate);
    }
    public String modDateToString(){
        return TimestampFormatter.timestampToString(modDate);
    }
}
