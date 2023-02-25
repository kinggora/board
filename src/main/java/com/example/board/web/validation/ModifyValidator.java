package com.example.board.web.validation;

import com.example.board.web.model.PostDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class ModifyValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PostDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PostDto post = (PostDto) target;

        /**
         * 작성자 검증
         */
        if(!StringUtils.hasText(post.getWriter())){
            errors.rejectValue("writer", "required");
        } else if(post.getWriter().length() < 3 || post.getWriter().length() >= 5 || post.getWriter().contains(" ")){
            errors.rejectValue("writer", "range");
        }

        /**
         * 비밀번호 검증
         */
        String regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{4,16}$";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(post.getPassword()).matches()){
            errors.rejectValue("password", "regex");
        }

        /**
         * 제목 검증
         */
        if(!StringUtils.hasText(post.getTitle()) || post.getTitle().length() < 4 ){
            errors.rejectValue("title", "min");
        } else if(post.getTitle().length() >= 100){
            errors.rejectValue("title", "over");
        }

        /**
         * 내용 검증
         */
        if(!StringUtils.hasText(post.getContent()) || post.getContent().length() < 4){
            errors.rejectValue("content", "min");
        } else if(post.getContent().length() > 2000){
            errors.rejectValue("content", "over");
        }
    }
}