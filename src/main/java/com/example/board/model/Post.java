package com.example.board.model;

import com.example.board.dto.AttachFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {

    private int id;
    private String title;
    private String writer;
    private String password;
    private String content;
    private int hit;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private List<AttachFile> files = new ArrayList<>();

}
