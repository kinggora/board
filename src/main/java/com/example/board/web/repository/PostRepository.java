package com.example.board.web.repository;

import com.example.board.SqlSessionFactoryWrapper;
import com.example.board.mapper.PostMapper;
import com.example.board.web.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final PostMapper mapper;

    /**
     * 게시글 저장
     * @param dto 저장할 게시글 정보
     * @return 저장된 레코드의 primary key 값
     */
    public int savePost(Post dto){
        mapper.savePost(dto);
        return dto.getPostId();
    }

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 조회한 게시글 정보
     */
    public Post findPostById(int id){
        return mapper.findPostById(id);
    }

    /**
     * 검색 조건에 해당하는 게시글 조회
     * @param criteria 검색 조건
     * @return 게시글 정보 리스트
     */
    public List<Post> findPosts(SearchCriteria criteria) {
        return mapper.findPosts(criteria);
    }

    /**
     * 검색 조건에 해당하는 게시글 페이징 정보 조회
     * @param criteria 검색 조건
     * @return 페이징 정보 (총 게시글 수, 총 페이지 수)
     */
    public int getPostCount(SearchCriteria criteria) {
        return mapper.getPostCount(criteria);
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(int id){
        mapper.hitUp(id);
    }

    /**
     * 게시글 수정
     * @param dto 수정할 게시글 정보
     */
    public void updatePost(Post dto){
        mapper.updatePost(dto);
    }

    /**
     * 게시글 삭제
     * @param postId 삭제할 게시글 id
     * @return 삭제 요청 결과 (true:정상처리, false:비밀번호 검증 실패)
     */
    // TODO : 삭제 flag 처리
    public boolean deletePost(int postId){
        //Post Database (Comment, File)
        mapper.deletePost(postId);
        return true;
    }

    /**
     * 게시글의 비밀번호가 일치하는지 확인
     * @param postId 게시글 id
     * @param password 비밀번호
     * @return 비밀번호 일치 여부 (true:일치, false:불일치)
     */
    public boolean checkPassword(int postId, String password) {
        int count = mapper.checkPassword(postId, password);
        return count == 1;
    }

    private PostMapper getMapper(){
        SqlSession session = SqlSessionFactoryWrapper.getSession();
        return session.getMapper(PostMapper.class);
    }
}

