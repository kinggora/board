package com.example.board.model;

public class PageInfo {

    private int totalCount;
    private int pageCount;

    public PageInfo(int totalCount, int pageCount) {
        this.totalCount = totalCount;
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }
}
