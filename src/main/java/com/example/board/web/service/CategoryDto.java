package com.example.board.web.service;

import com.example.board.MySqlSessionFactory;
import com.example.board.mapper.CategoryMapper;
import com.example.board.web.model.Category;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

public class CategoryDto {

    private CategoryDto(){}

    public static ArrayList<Category> getCategories(){
        CategoryMapper mapper = getMapper();
        return mapper.getCategories();
    }

    private static CategoryMapper getMapper(){
        SqlSession session = MySqlSessionFactory.getSession();
        return session.getMapper(CategoryMapper.class);
    }
}
