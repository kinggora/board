package com.example.board.web.model;

import lombok.Data;

@Data
public class SearchCriteria extends Criteria{
    private int page = 1;
    private int categoryId = 0;
    private String searchWord = "";
    private String startDate = "";
    private String endDate = "";

}
