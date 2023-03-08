package com.example.board.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorField {

    private String object;
    private String field;
    private Object value;
    private String code;
    private String message;

    public static List<ErrorField> of(BindingResult bindingResult){
        List<ErrorField> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            ErrorField error = ErrorField.builder()
                    .object(fieldError.getObjectName())
                    .field(fieldError.getField())
                    .value(fieldError.getRejectedValue())
                    .code(fieldError.getCode())
                    .message(fieldError.getDefaultMessage())
                    .build();
            errors.add(error);
        });
        return errors;
    }

}
