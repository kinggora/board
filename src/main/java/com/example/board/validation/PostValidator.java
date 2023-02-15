package com.example.board.validation;

import java.util.regex.Pattern;


public class PostValidator {

    public static boolean createValidation(String id, String writer, String password, String password2, String title, String content){

        if(!password.equals(password2)){
            return false;
        }

        if(!modifyValidation(id, writer, password, title, content)){
            return false;
        }

        return true;
    }

    public static boolean modifyValidation(String id, String writer, String password, String title, String content){

        if(id == null || writer == null || password == null || title == null || content == null){
            return false;
        }

        if(!isInteger(id)){
            return false;
        }

        String regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{4,16}$";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(password).matches()){
            return false;
        }

        if(writer.length() < 3 || writer.length() >= 5){
            return false;
        }

        if(title.length() < 4 || title.length() >= 100){
            return false;
        }

        if(content.length() < 4 || content.length() > 2000){
            return false;
        }
        return true;
    }

    private static boolean isInteger(String strValue) {
        try {
            Integer.parseInt(strValue);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
