package com.example.board.model;

public class PostSearch {
    private int pageNumber = 1;
    private String categoryId = "";
    private String searchWord = "";

    public PostSearch(String pageNumber, String categoryId, String searchWord) {
        if(pageNumber != null) this.pageNumber = Integer.parseInt(pageNumber);
        if(categoryId != null) this.categoryId = categoryId;
        if(searchWord != null) this.searchWord = searchWord;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getSearchWord() {
        return searchWord;
    }
}
