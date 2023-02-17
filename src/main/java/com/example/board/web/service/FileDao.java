package com.example.board.web.service;

import com.example.board.MySqlSessionFactory;
import com.example.board.mapper.FileMapper;
import com.example.board.web.model.AttachFile;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class FileDao {

    private FileDao(){}

    public static void saveFile(List<AttachFile> files) {
        FileMapper mapper = getMapper();
        mapper.saveFile(files);
    }

    public static AttachFile findFileById(int id){
        FileMapper mapper = getMapper();
        return mapper.findFileById(id);
    }

    public static List<AttachFile> findFiles(int id){
        FileMapper mapper = getMapper();
        return mapper.findFiles(id);
    }

    public static List<Integer> isAttached(List<Integer> idList){
        List<Integer> attachedList = new ArrayList<>();

        if(idList.isEmpty()) {
            return attachedList;
        }

        FileMapper mapper = getMapper();
        return mapper.isAttached(idList);
    }

    private static FileMapper getMapper(){
        SqlSession session = MySqlSessionFactory.getSession();
        return session.getMapper(FileMapper.class);
    }

}
