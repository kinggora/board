package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.dto.PostSaveDto;
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

        String sql = "INSERT INTO POST(post_id, category_id, writer, title, content, salt, hash) " +
                "VALUES(NULL, ?,?,?,?,?,?)";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, dto.getCategoryId());
            ps.setString(2, dto.getWriter());
            ps.setString(3, dto.getTitle());
            ps.setString(4, dto.getContent());
            ps.setString(5, encrypt.get("salt"));
            ps.setString(6, encrypt.get("hash"));

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

    public static PostViewDto findPost(int id){
        String sql = "SELECT * FROM POST WHERE post_id=?";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            rs.next();

            PostViewDto dto = new PostViewDto(
                    rs.getInt("post_id"),
                    rs.getInt("category_id"),
                    rs.getString("writer"),
                    rs.getString("title"),
                    rs.getString("content"),
                    timestampToString(rs.getTimestamp("reg_date")),
                    timestampToString(rs.getTimestamp("mod_date")),
                    rs.getInt("hit")
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
}
