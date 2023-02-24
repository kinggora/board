package com.example.board.web.repository;

import com.example.board.SqlSessionFactoryWrapper;
import com.example.board.mapper.FileMapper;
import com.example.board.web.model.AttachFile;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FileRepository {

    /**
     * DB에 첨부 파일 정보 저장
     * @param files 파일 정보 리스트
     */
    public void saveFile(List<AttachFile> files) {
        FileMapper mapper = getMapper();
        mapper.saveFile(files);
    }

    /**
     * 파일 정보 단건 조회
     * @param id 파일 id
     * @return 파일 정보
     */
    public AttachFile findFileById(int id){
        FileMapper mapper = getMapper();
        return mapper.findFileById(id);
    }

    /**
     * 해당 게시글에 첨부된 파일 정보 조회
     * @param postId
     * @return 파일 정보 리스트
     */
    public List<AttachFile> findFiles(int postId){
        FileMapper mapper = getMapper();
        return mapper.findFiles(postId);
    }

    private FileMapper getMapper(){
        SqlSession session = SqlSessionFactoryWrapper.getSession();
        return session.getMapper(FileMapper.class);
    }

}
