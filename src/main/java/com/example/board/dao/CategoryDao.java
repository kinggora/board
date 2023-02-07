package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public List<Category> getCategories(){
        String sql = "SELECT * FROM CATEGORY";

        Connection con = DBConnector.getConnection();
        Statement st = null;
        ResultSet rs = null;

        List<Category> categories = new ArrayList<>();

        try{
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                Category category = new Category(rs.getInt("CATEGORY_ID"), rs.getString("name"));
                categories.add(category);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try{
                if(rs != null) rs.close();
                if(st != null) st.close();
                if(con != null) con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }

        }
        return categories;
    }

    public static String getCategoryName(int id){
        String sql = "SELECT (name) FROM CATEGORY WHERE category_id=?";

        Connection con = DBConnector.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String categoryName = "";

        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();
            categoryName = rs.getString("name");
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try{
                if(rs != null) rs.close();
                if(ps != null) ps.close();
                if(con != null) con.close();
            } catch (SQLException e){
                e.printStackTrace();
            }

        }
        return categoryName;
    }
}
