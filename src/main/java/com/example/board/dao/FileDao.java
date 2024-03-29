package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.model.AttachFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDao {

    private FileDao(){}

    public static void saveFile(int postId, List<AttachFile> files) {
        String sql = "INSERT INTO file(post_id, orig_name, store_name, ext, store_dir) " +
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
            DBConnector.close(con, ps, rs);
        }
    }

    public static AttachFile findFileById(String id){
        String sql = "SELECT * FROM file WHERE file_id=? LIMIT 1";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        AttachFile attachFile = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();
            if(rs.next()){
                attachFile = AttachFile.builder()
                        .postId(rs.getInt("post_id"))
                        .origName(rs.getString("orig_name"))
                        .storeName(rs.getString("store_name"))
                        .ext(rs.getString("ext"))
                        .storeDir(rs.getString("store_dir"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
        return attachFile;
    }

    public static List<AttachFile> findFiles(String id){
        String sql = "SELECT * FROM file WHERE post_id=?";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AttachFile> files = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();
            while(rs.next()){
                AttachFile attachFile = AttachFile.builder()
                        .fileId(rs.getInt("file_id"))
                        .origName(rs.getString("orig_name"))
                        .storeName(rs.getString("store_name"))
                        .ext(rs.getString("ext"))
                        .storeDir(rs.getString("store_dir"))
                        .build();
                files.add(attachFile);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, ps, rs);
        }
        return files;
    }

    public static List<Integer> isAttached(List<String> idList){
        List<Integer> attachedList = new ArrayList<>();

        if(idList.isEmpty()) {
            return attachedList;
        }

        String sql1 = "SELECT post_id FROM post p WHERE EXISTS (SELECT 1 FROM file f WHERE f.post_id IN (";
        String sql2 = ") AND p.post_id = f.post_id)";
        String inPostId = String.join(",", idList);

        StringBuilder sb = new StringBuilder();
        sb.append(sql1);
        sb.append(inPostId);
        sb.append(sql2);

        Connection con = DBConnector.getConnection();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = con.createStatement();
            rs = st.executeQuery(sb.toString());
            while (rs.next()){
                attachedList.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnector.close(con, st, rs);
        }
        return attachedList;
    }

}
