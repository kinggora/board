package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.model.CommentDto;
import com.example.board.util.TimestampFormatter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {

    private CommentDao() {
    }

    public static void saveComment(String postId, String content){

        String sql = "INSERT INTO COMMENT(post_id, content, reg_date) " +
                "VALUES(?, ?, ?)";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(postId));
            ps.setString(2, content);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
    }

    public static List<CommentDto> findComment(String postId){
        String sql =
                "SELECT * FROM comment WHERE post_id=?";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<CommentDto> comments = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, postId);

            rs = ps.executeQuery();

            while (rs.next()){
                String regDate = TimestampFormatter.timestampToString(rs.getTimestamp("reg_date"));
                String content = rs.getString("content");
                CommentDto comment = new CommentDto(regDate, content);
                comments.add(comment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
        return comments;
    }

}
