package com.example.board.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearch {
    private int pageNumber = 1;
    private String categoryId = "";
    private String searchWord = "";

    @Builder
    private PostSearch(String pageNumber, String categoryId, String searchWord) {
        if(pageNumber != null) {
            this.pageNumber = Integer.parseInt(pageNumber);
        }
        if(categoryId != null) {
            this.categoryId = categoryId;
        }
        if(searchWord != null) {
            this.searchWord = searchWord;
        }
    }
}
