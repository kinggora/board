package com.example.board.mapper;

import com.example.board.web.model.Category;

import java.util.ArrayList;

public interface CategoryMapper {

    /**
     * 카테고리 조회
     * @return 카테고리 리스트
     */
    ArrayList<Category> findCategories();
}
