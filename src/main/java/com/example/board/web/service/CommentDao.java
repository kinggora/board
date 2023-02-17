package com.example.board.web.service;

import com.example.board.MySqlSessionFactory;
import com.example.board.mapper.CommentMapper;
import com.example.board.web.model.CommentDto;
import org.apache.ibatis.session.SqlSession;

import java.sql.*;
import java.util.List;

public class CommentDao {

    private CommentDao() {
    }

    public static void saveComment(int postId, String content){
        CommentMapper mapper = getMapper();
        CommentDto dto = CommentDto.builder()
                .postId(postId)
                .content(content)
                .regDate(new Timestamp(System.currentTimeMillis()))
                .build();
        mapper.saveComment(dto);
    }

    public static List<CommentDto> findComment(int postId){
        CommentMapper mapper = getMapper();
        return mapper.findComment(postId);
    }

    private static CommentMapper getMapper(){
        SqlSession session = MySqlSessionFactory.getSession();
        return session.getMapper(CommentMapper.class);
    }
}
