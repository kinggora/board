package com.example.board.mapper;

import com.example.board.web.model.AttachFile;

import java.util.List;

public interface FileMapper {
    void saveFile(List<AttachFile> files);

    AttachFile findFileById(int id);

    List<AttachFile> findFiles(int postId);

    List<Integer> isAttached(List<Integer> idList);
}
