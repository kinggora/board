package com.example.board.web.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostDto {
    private Integer id;
    private Integer categoryId;
    private String writer;
    private String password;
    private String password2;
    private String title;
    private String content;
    private List<MultipartFile> newFiles = new ArrayList<>();
    private List<Integer> existingFiles = new ArrayList<>();
}

