package com.example.board.web.repository;

import com.example.board.SqlSessionFactoryWrapper;
import com.example.board.mapper.PostMapper;
import com.example.board.web.util.FileStore;
import com.example.board.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Repository
public class PostRepository {

    /**
     * 게시글 저장
     * @param dto 저장할 게시글 정보
     * @return 저장된 레코드의 primary key 값
     */
    public int savePost(Post dto){
        PostMapper mapper = getMapper();
        mapper.savePost(dto);
        return dto.getPostId();
    }

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 조회한 게시글 정보
     */
    public Post findPostById(int id){
        PostMapper mapper = getMapper();
        return mapper.findPostById(id);
    }

    /**
     * 검색 조건에 해당하는 게시글 조회
     * @param criteria 검색 조건
     * @return 게시글 정보 리스트
     */
    public List<Post> findPosts(SearchCriteria criteria) {
        int startRow = (criteria.getPage() - 1) * criteria.getPageSize();
        criteria.setStartRow(startRow);
        PostMapper mapper = getMapper();
        return mapper.findPosts(criteria);
    }

    /**
     * 검색 조건에 해당하는 게시글 페이징 정보 조회
     * @param criteria 검색 조건
     * @return 페이징 정보 (총 게시글 수, 총 페이지 수)
     */
    public PageManager getPageManager(SearchCriteria criteria) {
        PostMapper mapper = getMapper();
        int totalCount = mapper.getPostCount(criteria);
        return new PageManager(totalCount, criteria);
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(int id){
        PostMapper mapper = getMapper();
        mapper.hitUp(id);
    }

    /**
     * 게시글 수정
     * @param dto 수정할 게시글 정보
     * @return 수정 요청 결과 (true:정상처리, false:비밀번호 검증 실패)
     */
    public boolean updatePost(Post dto){
        if(!checkPassword(dto.getPostId(), dto.getPassword())){
            return false;
        }
        PostMapper mapper = getMapper();
        mapper.updatePost(dto);
        return true;
    }

    /**
     * 게시글 삭제
     * @param postId 삭제할 게시글 id
     * @param password 삭제할 게시글 password
     * @return 삭제 요청 결과 (true:정상처리, false:비밀번호 검증 실패)
     */
    // TODO : 삭제 flag 처리
    public boolean deletePost(int postId, String password){
        if(!checkPassword(postId, password)){
            return false;
        }
        //File Storage
        FileRepository fileDao = new FileRepository();
        List<AttachFile> files = fileDao.findFiles(postId);
        FileStore fileStore = new FileStore();
        fileStore.deleteFiles(files);

        //Post Database (Comment, File)
        PostMapper mapper = getMapper();
        mapper.deletePost(postId);
        return true;
    }

    /**
     * 게시글의 비밀번호가 일치하는지 확인
     * @param postId 게시글 id
     * @param password 비밀번호
     * @return 비밀번호 일치 여부 (true:일치, false:불일치)
     */
    private boolean checkPassword(int postId, String password) {
        PostMapper mapper = getMapper();
        int count = mapper.checkPassword(postId, password);
        return count == 1;
    }

    private PostMapper getMapper(){
        SqlSession session = SqlSessionFactoryWrapper.getSession();
        return session.getMapper(PostMapper.class);
    }
}

