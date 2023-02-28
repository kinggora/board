package com.example.board.mapper;

import com.example.board.web.model.AttachFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
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

    /**
     * 게시글 수정 - 기존 첨부파일 삭제
     * @param postId 게시글 id
     * @param fileIds 수정 후 존재하는 파일 리스트
     */
    void updateDeleteFile(@Param("postId") int postId, @Param("fileIds") List<Integer> fileIds);
}
