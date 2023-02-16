package com.example.board.dao;

import com.example.board.DBConnector;
import com.example.board.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private CategoryDao() {
    }

    public static List<Category> getCategories(){
        String sql = "SELECT * FROM category";

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
            DBConnector.close(con, st, rs);
        }
        return categories;
    }
}
