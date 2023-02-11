package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.model.AttachFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDao {

    private FileDao(){}

    public static void saveFile(int postId, List<AttachFile> files) {

        String sql = "INSERT INTO FILE(post_id, orig_name, store_name, ext, store_dir) " +
                "VALUES(?,?,?,?,?)";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            for (AttachFile file : files) {
                ps = con.prepareStatement(sql);

                ps.setInt(1, postId);
                ps.setString(2, file.getOrigName());
                ps.setString(3, file.getStoreName());
                ps.setString(4, file.getExt());
                ps.setString(5, file.getStoreDir());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<AttachFile> findFile(String id){
        String sql = "SELECT * FROM File WHERE post_id=?";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AttachFile> files = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();
            while(rs.next()){
                AttachFile attachFile = new AttachFile(
                        rs.getString("orig_name"),
                        rs.getString("store_name"),
                        rs.getString("ext"),
                        rs.getString("store_dir")
                );
                files.add(attachFile);
            }
            return files;
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

    public static boolean[] isAttached(Integer[] idList){
        String sql = "SELECT EXISTS(SELECT * FROM FILE WHERE post_id=? LIMIT 1)";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean[] isAttachedList = new boolean[idList.length];

        try {
            for(int i = 0; i < idList.length; i++){
                ps = con.prepareStatement(sql);
                ps.setInt(1, idList[i]);
                rs = ps.executeQuery();
                while (rs.next()){
                    int result = rs.getInt(1);
                    if(result==0){
                        isAttachedList[i] = false;
                    } else {
                        isAttachedList[i] = true;
                    }
                }
                ps.clearParameters();
                rs.close();
            }
            return isAttachedList;
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
