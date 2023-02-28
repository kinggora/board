package com.example.board.web.repository;

import com.example.board.SqlSessionFactoryWrapper;
import com.example.board.mapper.CommentMapper;
import com.example.board.web.model.Comment;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentMapper mapper;

    /**
     * 코멘트 저장
     * @param comment
     */
    public void saveComment(Comment comment){
        mapper.saveComment(comment);
    }

    /**
     * 해당 게시글에 작성된 코멘트 조회
     * @param postId 게시글 id
     * @return 코멘트 리스트
     */
    public List<Comment> findComment(int postId){
        return mapper.findComment(postId);
    }

    private CommentMapper getMapper(){
        SqlSession session = SqlSessionFactoryWrapper.getSession();
        return session.getMapper(CommentMapper.class);
    }
}
