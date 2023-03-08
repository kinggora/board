package com.example.board.web.model;

import com.example.board.web.util.TimestampFormatter;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment {
    private Integer id;
    private Integer postId;
    private String content;
    private Timestamp regDate = new Timestamp(System.currentTimeMillis());

    public String regDateToString(){
        return TimestampFormatter.timestampToString(regDate);
    }

}
