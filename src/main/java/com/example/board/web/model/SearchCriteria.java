package com.example.board.web.model;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class SearchCriteria extends Criteria{
    private int page = 1;
    private int categoryId = 0;
    private String searchWord = "";
    private String startDate = "";
    private String endDate = "";

    public String generateSearchQueryString(){
        return generateSearchQueryString(page);
    }

    public String generateSearchQueryString(int page){
        StringBuilder sb = new StringBuilder();
        sb.append("?page=").append(page);
        if(StringUtils.hasText(startDate)){
            sb.append("&startDate=").append(startDate);
        }
        if(StringUtils.hasText(endDate)){
            sb.append("&endDate=").append(endDate);
        }
        if(categoryId > 0){
            sb.append("&categoryId=").append(categoryId);
        }
        if(StringUtils.hasText(searchWord)){
            sb.append("&searchWord=").append(searchWord);
        }
        return sb.toString();
    }

}
