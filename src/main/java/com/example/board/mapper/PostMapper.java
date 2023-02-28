package com.example.board.mapper;

import com.example.board.web.model.Post;
import com.example.board.web.model.SearchCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    /**
     * 게시글 저장
     * @param dto 게시글 정보
     */
    void savePost(Post dto);

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 게시글 정보
     */
    Post findPostById(int id);

    /**
     * 검색 조건에 해당하는 게시글 조회
     * @param sc 검색 조건
     * @return 게시글 정보 리스트
     */
    List<Post> findPosts(SearchCriteria sc);

    /**
     * 검색 조건에 해당하는 게시글 개수 조회
     * @param sc 검색 조건
     * @return 게시글 개수
     */
    int getPostCount(SearchCriteria sc);

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    void hitUp(int id);

    /**
     * 게시글 수정
     * @param dto 수정할 데이터
     */
    void updatePost(Post dto);

    /**
     * 게시글 삭제
     * @param id 게시글 id
     */
    void deletePost(int id);

    /**
     * 게시글의 비밀번호가 일치하는지 확인
     * @param id 게시글 id
     * @param password 비밀번호
     * @return id와 password가 모두 일치하는 레코드 수
     */
    int checkPassword(@Param("id") int id, @Param("password") String password);

}
