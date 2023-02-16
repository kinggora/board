package com.example.board.validation;

import lombok.Builder;

import java.util.regex.Pattern;

@Builder
public class PostValidator {

    private String id;
    private String writer;
    private String password;
    private String password2;
    private String title;
    private String content;

    public boolean createValidation(){

        if(!password.equals(password2)){
            return false;
        }

        if(!modifyValidation()){
            return false;
        }

        return true;
    }

    public boolean modifyValidation(){

        //null 또는 빈 문자열 또는 공백문자
        if(id.isBlank() || writer.isBlank() || password.isBlank() || title.isBlank() || content.isBlank()){
            return false;
        }

        //int 타입으로 변환 불가
        if(!isInteger(id)){
            return false;
        }

        String regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{4,16}$";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(password).matches()){
            return false;
        }

        //공백문자 포함
        if(writer.contains(" ")){
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
