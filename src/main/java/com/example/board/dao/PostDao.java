package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.dto.AttachFile;
import com.example.board.dto.PostSaveDto;
import com.example.board.dto.PostUpdateDto;
import com.example.board.dto.PostViewDto;
import com.example.board.model.PageInfo;
import com.example.board.model.PostSearch;
import com.example.board.util.FileStore;
import com.example.board.validation.PasswordEncryptor;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDao {

    private static final int PAGE_SIZE = 10;

    public static final String SQL_UPDATE_POST_INC_HIT = "UPDATE POST SET hit=hit+1 WHERE post_id=?";
    public static final String SQL_SELECT_POST_VIEW = "SELECT * FROM POST p JOIN CATEGORY c ON p.category_id=c.category_id WHERE p.post_id=?";

    private PostDao() {}

    public static int savePost(PostSaveDto dto){

        PasswordEncryptor encryptor = new PasswordEncryptor();
        Map<String, String> encrypt = encryptor.encrypt(dto.getPassword());

        String sql = "INSERT INTO POST(post_id, category_id, writer, title, content, reg_date, salt, hash) " +
                "VALUES(NULL, ?,?,?,?,?,?,?)";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, dto.getCategoryId());
            ps.setString(2, dto.getWriter());
            ps.setString(3, dto.getTitle());
            ps.setString(4, dto.getContent());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setString(6, encrypt.get("salt"));
            ps.setString(7, encrypt.get("hash"));

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null) rs.close();
                if(ps != null) ps.close();
                if(con != null) con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static PostViewDto findPostById(String id, boolean hitUp){
        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(hitUp){
                ps = con.prepareStatement(SQL_UPDATE_POST_INC_HIT);
                ps.setString(1, id);
                ps.executeQuery();
                ps.close();
            }

            ps = con.prepareStatement(SQL_SELECT_POST_VIEW);
            ps.setString(1, id);

            rs = ps.executeQuery();
            rs.next();

            PostViewDto dto = new PostViewDto(
                    rs.getInt("p.post_id"),
                    rs.getString("c.name"),
                    rs.getString("p.writer"),
                    rs.getString("p.title"),
                    rs.getString("p.content"),
                    timestampToString(rs.getTimestamp("p.reg_date")),
                    timestampToString(rs.getTimestamp("p.mod_date")),
                    rs.getInt("p.hit")
            );
            return dto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null) rs.close();
                if(ps != null) ps.close();
                if(con != null) con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static boolean updatePost(PostUpdateDto dto){

        String sql = "SELECT salt, hash " +
                "FROM POST " +
                "WHERE post_id=?";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getPostId());

            rs = ps.executeQuery();
            rs.next();

            PasswordEncryptor encryptor = new PasswordEncryptor();
            if(!encryptor.check(dto.getPassword(), rs.getString("salt"), rs.getString("hash"))){
                return false;
            } else {
                ps.close();
                String updateSql = "UPDATE POST " +
                        "SET writer=?, title=?, content=?, mod_date=? " +
                        "WHERE post_id=?";
                ps = con.prepareStatement(updateSql);
                ps.setString(1, dto.getWriter());
                ps.setString(2, dto.getTitle());
                ps.setString(3, dto.getContent());
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                ps.setString(5, dto.getPostId());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null) rs.close();
                if(ps != null) ps.close();
                if(con != null) con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static boolean deletePost(String postId, String password){

        String sql = "SELECT salt, hash " +
                "FROM POST " +
                "WHERE post_id=?";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(postId));

            rs = ps.executeQuery();
            rs.next();

            PasswordEncryptor encryptor = new PasswordEncryptor();
            if(!encryptor.check(password, rs.getString("salt"), rs.getString("hash"))){
                return false;
            } else {
                //Storage 의 FILE 삭제
                List<AttachFile> files = FileDao.findFile(postId);
                FileStore fileStore = new FileStore();
                fileStore.deleteFiles(files);

                //POST DELETE (FILE, COMMENT ON DELETE CASCADE)
                String deleteSql = "DELETE FROM POST WHERE post_id=?";
                ps = con.prepareStatement(deleteSql);
                ps.setInt(1, Integer.parseInt(postId));
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null) rs.close();
                if(ps != null) ps.close();
                if(con != null) con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Object> findPosts(PostSearch ps){
        String searchSql = getSearchSql(ps);
        String countSql = getCountSql(ps);

        Connection con = DBConnector.getConnection();
        Statement st = null;
        ResultSet rs = null;

        List<PostViewDto> posts = new ArrayList<>();
        try {
            st = con.createStatement();
            rs = st.executeQuery(searchSql);

            while (rs.next()){
                PostViewDto dto = new PostViewDto(
                        rs.getInt("p.post_id"),
                        rs.getString("c.name"),
                        rs.getString("p.writer"),
                        rs.getString("p.title"),
                        rs.getString("p.content"),
                        timestampToString(rs.getTimestamp("p.reg_date")),
                        timestampToString(rs.getTimestamp("p.mod_date")),
                        rs.getInt("p.hit")
                );
                posts.add(dto);
            }

            st.close();
            st = con.createStatement();
            rs = st.executeQuery(countSql);

            int totalCount = 0;
            if(rs.next()){
                totalCount = rs.getInt(1);
            }
            int pageCount = totalCount / PAGE_SIZE + 1;
            PageInfo pageInfo = new PageInfo(totalCount, pageCount);

            Map<String, Object> result = new HashMap<>();
            result.put("postList", posts);
            result.put("pageInfo", pageInfo);

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null) rs.close();
                if(ps != null) st.close();
                if(con != null) con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private static String getCountSql(PostSearch ps) {
        String categoryId = ps.getCategoryId();
        String searchWord = ps.getSearchWord();

        String sql = "SELECT COUNT(*) FROM POST";

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

    private static String getSearchSql(PostSearch ps){
        int pageNumber = ps.getPageNumber();
        String categoryId = ps.getCategoryId();
        String searchWord = ps.getSearchWord();

        int startRow = (pageNumber-1) * PAGE_SIZE;

        String sql = "SELECT * FROM POST p JOIN CATEGORY c " +
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
        sb.append(" ORDER BY p.post_id DESC");
        sb.append(" LIMIT ").append(startRow).append(",").append(PAGE_SIZE);

        return sb.toString();
    }


    private static String timestampToString(Timestamp timestamp){
        if(timestamp == null){
            return "-";
        }
        return new SimpleDateFormat("yyyy.MM.dd HH:mm").format(timestamp);
    }
}
