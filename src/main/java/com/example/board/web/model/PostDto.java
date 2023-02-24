package com.example.board.web.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostDto {
    private int id;
    private int categoryId;
    private String writer;
    private String password;
    private String password2;
    private String title;
    private String content;
    private List<MultipartFile> files;
}
