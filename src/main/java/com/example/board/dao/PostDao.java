package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.dto.PostSaveDto;
import com.example.board.dto.PostUpdateDto;
import com.example.board.dto.PostViewDto;
import com.example.board.validation.PasswordEncryptor;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Map;

public class PostDao {

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

    public static PostViewDto findPost(String id){
        String sql =
                "SELECT * " +
                "FROM POST p " +
                    "JOIN CATEGORY c " +
                    "ON p.category_id = c.category_id " +
                "WHERE p.post_id=?";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
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

    private static String timestampToString(Timestamp timestamp){
        if(timestamp == null){
            return "-";
        }
        return new SimpleDateFormat("yyyy.MM.dd HH:mm").format(timestamp);
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
}
