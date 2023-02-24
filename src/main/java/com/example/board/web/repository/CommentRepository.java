package com.example.board.web.repository;

import com.example.board.SqlSessionFactoryWrapper;
import com.example.board.mapper.CommentMapper;
import com.example.board.web.model.Comment;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class CommentRepository {

    /**
     * 코멘트 저장
     * @param postId 게시글 id
     * @param content 코멘트 내용
     */
    public void saveComment(int postId, String content){
        CommentMapper mapper = getMapper();
        Comment dto = Comment.builder()
                .postId(postId)
                .content(content)
                .regDate(new Timestamp(System.currentTimeMillis()))
                .build();
        mapper.saveComment(dto);
    }

    /**
     * 해당 게시글에 작성된 코멘트 조회
     * @param postId 게시글 id
     * @return 코멘트 리스트
     */
    public List<Comment> findComment(int postId){
        CommentMapper mapper = getMapper();
        return mapper.findComment(postId);
    }

    private CommentMapper getMapper(){
        SqlSession session = SqlSessionFactoryWrapper.getSession();
        return session.getMapper(CommentMapper.class);
    }
}