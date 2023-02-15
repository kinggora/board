package com.example.board.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttachFile {

    private String origName;
    private String storeName;
    private String ext;
    private String storeDir;

}
