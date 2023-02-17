package com.example.board.web.model;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile {
    private int fileId;
    private int postId;
    private String origName;
    private String storeName;
    private String ext;
    private String storeDir;

}
