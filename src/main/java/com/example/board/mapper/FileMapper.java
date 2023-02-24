package com.example.board.mapper;

import com.example.board.web.model.AttachFile;

import java.util.List;

public interface FileMapper {

    /**
     * DB에 첨부 파일 정보 저장
     * @param files 파일 정보 리스트
     */
    void saveFile(List<AttachFile> files);

    /**
     * 파일 정보 단건 조회
     * @param id 파일 id
     * @return 파일 정보
     */
    AttachFile findFileById(int id);

    /**
     * 해당 게시글에 첨부된 파일 정보 조회
     * @param postId
     * @return 파일 정보 리스트
     */
    List<AttachFile> findFiles(int postId);

}
