package com.example.board.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimestampFormatter {

    private TimestampFormatter(){}
    public static String timestampToString(Timestamp timestamp){
        if(timestamp == null){
            return "-";
        }
        return new SimpleDateFormat("yyyy.MM.dd HH:mm").format(timestamp);
    }
}
