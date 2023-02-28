package com.example.board.web.repository;

import com.example.board.SqlSessionFactoryWrapper;
import com.example.board.mapper.CategoryMapper;
import com.example.board.web.model.Category;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final CategoryMapper mapper;

    /**
     * 카테고리 조회
     * @return 카테고리 리스트
     */
    public ArrayList<Category> findCategories(){
        return mapper.findCategories();
    }

    private CategoryMapper getMapper(){
        SqlSession session = SqlSessionFactoryWrapper.getSession();
        return session.getMapper(CategoryMapper.class);
    }
}
