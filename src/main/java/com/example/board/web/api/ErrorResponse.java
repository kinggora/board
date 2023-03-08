package com.example.board.web.api;

import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private LocalDateTime timestamp = LocalDateTime.now();
    private List<ErrorField> errors;

    public ErrorResponse(List<ErrorField> errors) {
        this.errors = errors;
    }

    public static ErrorResponse of(BindingResult bindingResult){
        return new ErrorResponse(ErrorField.of(bindingResult));
    }


}
