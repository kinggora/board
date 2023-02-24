package com.example.board.web.model;

import com.example.board.web.util.TimestampFormatter;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    private int postId;
    private String writer;
    private String password;
    private String title;
    private String content;
    private Timestamp regDate;
    private Timestamp modDate;
    private int hit;
    private int categoryId;
    private String categoryName;
    private boolean isFileExists;

    public String regDateToString() {
        return TimestampFormatter.timestampToString(regDate);
    }

    public String modDateToString() {
        return TimestampFormatter.timestampToString(modDate);
    }

    public void modifyPost(String writer, String title, String content){
        if(writer != null){
            this.writer = writer;
        }
        if(title != null){
            this.title = title;
        }
        if(content != null){
            this.content = content;
        }
    }
}
