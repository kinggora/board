package com.example.board.web.model;

import com.example.board.web.util.TimestampFormatter;
import lombok.*;

import java.sql.Timestamp;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentDto {
    private int postId;
    private Timestamp regDate;
    private String content;

    public String regDateToString(){
        return TimestampFormatter.timestampToString(regDate);
    }

}
