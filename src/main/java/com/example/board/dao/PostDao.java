package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.model.*;
import com.example.board.util.FileStore;
import com.example.board.util.TimestampFormatter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    private static final int PAGE_SIZE = 10;

    private PostDao() {}

    public static int savePost(PostSaveDto dto){
        String sql = "INSERT INTO POST(post_id, category_id, writer, password, title, content, reg_date) " +
                "VALUES(NULL, ?,?,SHA2(?,256),?,?,?)";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, dto.getCategoryId());
            ps.setString(2, dto.getWriter());
            ps.setString(3, dto.getPassword());
            ps.setString(4, dto.getTitle());
            ps.setString(5, dto.getContent());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
            rs = ps.getGeneratedKeys(); //Primary Key
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
    }

    public static PostViewDto findPostById(String id){
        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM post p JOIN category c ON p.category_id=c.category_id WHERE p.post_id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();
            rs.next();

            PostViewDto dto = PostViewDto.builder()
                    .postId(rs.getInt("p.post_id"))
                    .category(rs.getString("c.name"))
                    .writer(rs.getString("p.writer"))
                    .title(rs.getString("p.title"))
                    .content(rs.getString("p.content"))
                    .regDate(TimestampFormatter.timestampToString(rs.getTimestamp("p.reg_date")))
                    .modDate(TimestampFormatter.timestampToString(rs.getTimestamp("p.mod_date")))
                    .hit(rs.getInt("p.hit"))
                    .build();

            return dto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
    }

    public static void hitUp(String id){
        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "UPDATE post SET hit=hit+1 WHERE post_id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
    }

    public static boolean updatePost(PostUpdateDto dto){
        if(!checkPassword(dto.getPostId(), dto.getPassword())){
            return false;
        }

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "UPDATE post " +
                    "SET writer=?, title=?, content=?, mod_date=? " +
                    "WHERE post_id=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getWriter());
            ps.setString(2, dto.getTitle());
            ps.setString(3, dto.getContent());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setString(5, dto.getPostId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
        return true;
    }

    public static boolean deletePost(String postId, String password){
        if(!checkPassword(postId, password)){
            return false;
        }

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //Storage 의 FILE 삭제
            List<AttachFile> files = FileDao.findFiles(postId);
            FileStore fileStore = new FileStore();
            fileStore.deleteFiles(files);

            //POST DELETE (FILE, COMMENT ON DELETE CASCADE)
            String deleteSql = "DELETE FROM post WHERE post_id=?";
            ps = con.prepareStatement(deleteSql);
            ps.setInt(1, Integer.parseInt(postId));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
        return true;
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
                        .regDate(TimestampFormatter.timestampToString(rs.getTimestamp("p.reg_date")))
                        .modDate(TimestampFormatter.timestampToString(rs.getTimestamp("p.mod_date")))
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

    private static boolean checkPassword(String postId, String password) {
        String sql = "SELECT COUNT(post_id) AS CNT FROM post WHERE post_id=? AND password=SHA2(?, 256)";
        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        int cnt = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, postId);
            ps.setString(2, password);

            rs = ps.executeQuery();

            if(rs.next()){
                cnt = rs.getInt("CNT");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
        return cnt == 1;
    }
}

