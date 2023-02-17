package com.example.board.web.service;

import com.example.board.DBConnector;
import com.example.board.MySqlSessionFactory;
import com.example.board.mapper.PostMapper;
import com.example.board.web.util.FileStore;
import com.example.board.web.util.TimestampFormatter;
import com.example.board.web.model.*;
import org.apache.ibatis.session.SqlSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    private static final int PAGE_SIZE = 10;

    private PostDao() {}

    public static int savePost(PostSaveDto dto){
        PostMapper mapper = getMapper();
        mapper.savePost(dto);
        return dto.getPostId();
    }

    public static PostViewDto findPostById(int id){
        PostMapper mapper = getMapper();
        return mapper.findPostById(id);
    }

    public static void hitUp(int id){
        PostMapper mapper = getMapper();
        mapper.hitUp(id);
    }

    public static boolean updatePost(PostUpdateDto dto){
        if(!checkPassword(dto.getPostId(), dto.getPassword())){
            return false;
        }
        PostMapper mapper = getMapper();
        mapper.updatePost(dto);
        return true;
    }

    // TODO : 삭제 flag 처리
    public static boolean deletePost(int postId, String password){
        if(!checkPassword(postId, password)){
            return false;
        }
        //File Storage
        List<AttachFile> files = FileDao.findFiles(postId);
        FileStore fileStore = new FileStore();
        fileStore.deleteFiles(files);

        //Post Database (Comment, File)
        PostMapper mapper = getMapper();
        mapper.deletePost(postId);
        return true;
    }

    private static boolean checkPassword(int postId, String password) {
        PostMapper mapper = getMapper();
        int count = mapper.checkPassword(postId, password);
        return count == 1;
    }

    public static PostListDto findPosts(PostSearch ps){
        String searchSql = getSearchSql(ps);
        List<PostViewDto> postList = getPostList(searchSql);

        String countSql = getCountSql(ps);
        PageInfo pageInfo = getPageInfo(countSql);

        return new PostListDto(postList, pageInfo);
    }

    private static List<PostViewDto> getPostList(String searchSql) {
        Connection con = DBConnector.getConnection();
        Statement st = null;
        ResultSet rs = null;

        List<PostViewDto> postList = new ArrayList<>();
        try {
            st = con.createStatement();
            rs = st.executeQuery(searchSql);

            while (rs.next()) {
                PostViewDto dto = PostViewDto.builder()
                        .postId(rs.getInt("p.post_id"))
                        .category(rs.getString("c.name"))
                        .writer(rs.getString("p.writer"))
                        .title(rs.getString("p.title"))
                        .content(rs.getString("p.content"))
                        .regDate(rs.getTimestamp("p.reg_date"))
                        .modDate(rs.getTimestamp("p.mod_date"))
                        .hit(rs.getInt("p.hit"))
                        .build();
                postList.add(dto);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            DBConnector.close(con, st, rs);
        }

        return postList;
    }

    private static PageInfo getPageInfo(String countSql) {
        Connection con = DBConnector.getConnection();
        Statement st = null;
        ResultSet rs = null;

        int totalCount = 0;
        try{
            st = con.createStatement();
            rs = st.executeQuery(countSql);

            if(rs.next()){
                totalCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, st, rs);
        }

        int pageCount = totalCount / PAGE_SIZE + 1;
        return new PageInfo(totalCount, pageCount);
    }

    private static String getSearchSql(PostSearch ps){
        int pageNumber = ps.getPageNumber();
        String categoryId = ps.getCategoryId();
        String searchWord = ps.getSearchWord();

        int startRow = (pageNumber-1) * PAGE_SIZE;


        String sql = "SELECT * FROM post p JOIN category c " +
                "WHERE p.category_id=c.category_id";

        StringBuilder sb = new StringBuilder(sql);

        if(categoryId.length() > 0){
            sb.append(" AND p.category_id=").append(categoryId);
        }
        //검색어가 공백문자나 빈 문자열이 아니고 2글자 이상
        if(!searchWord.isBlank() && searchWord.length() >= 2){
            sb.append(" AND (p.title LIKE '%")
                    .append(searchWord)
                    .append("%' OR p.writer LIKE '%")
                    .append(searchWord)
                    .append("%' OR p.content LIKE '%")
                    .append(searchWord)
                    .append("%')");
        }

        sb.append(" ORDER BY p.reg_date DESC");
        sb.append(" LIMIT ").append(startRow).append(",").append(PAGE_SIZE);

        return sb.toString();
    }

    private static String getCountSql(PostSearch ps) {
        String categoryId = ps.getCategoryId();
        String searchWord = ps.getSearchWord();

        String sql = "SELECT COUNT(*) FROM post";

        StringBuilder sb = new StringBuilder(sql);

        if((categoryId.length() > 0) && (!searchWord.isBlank() && searchWord.length() >= 2)){
            sb.append(" WHERE category_id=").append(categoryId);
            sb.append(" AND (title LIKE '%")
                    .append(searchWord)
                    .append("%' OR writer LIKE '%")
                    .append(searchWord)
                    .append("%' OR content LIKE '%")
                    .append(searchWord)
                    .append("%')");
        } else if(categoryId.length() > 0){
            sb.append(" WHERE category_id=").append(categoryId);
        } else if(!searchWord.isBlank() && searchWord.length() >= 2){
            sb.append(" WHERE title LIKE '%")
                    .append(searchWord)
                    .append("%' OR writer LIKE '%")
                    .append(searchWord)
                    .append("%' OR content LIKE '%")
                    .append(searchWord)
                    .append("%'");
        }
        return sb.toString();
    }

    private static PostMapper getMapper(){
        SqlSession session = MySqlSessionFactory.getSession();
        return session.getMapper(PostMapper.class);
    }
}

