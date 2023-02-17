package com.example.board.mapper;

import com.example.board.web.model.PostSaveDto;
import com.example.board.web.model.PostUpdateDto;
import com.example.board.web.model.PostViewDto;
import org.apache.ibatis.annotations.Param;

public interface PostMapper {
    void savePost(PostSaveDto dto);

    PostViewDto findPostById(int id);

    void hitUp(int id);

    void updatePost(PostUpdateDto dto);

    void deletePost(int id);

    int checkPassword(@Param("id") int id, @Param("password") String password);

}
