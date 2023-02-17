package com.example.board.web.util;

public class TypeConvertor {

    public static int stringToInt(String str){
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(str);
    }
}
