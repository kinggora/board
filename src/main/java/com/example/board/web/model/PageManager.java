package com.example.board.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PageManager {
    private int totalCount;
    private SearchCriteria criteria;

    public int getTotalPage(){
        return totalCount / criteria.getPageSize() + 1;
    }

    public int getStartNum(){
        return criteria.getPage() - (criteria.getPage() - 1) % 10;
    }

    public boolean getPrev(){
        return getStartNum() - 10 > 0;
    }

    public boolean getNext(){
        return getStartNum() + 10 <= getTotalPage();
    }

    public boolean isFirstPage(){
        return criteria.getPage() == 1;
    }
    public boolean isLastPage(){
        return criteria.getPage() == getTotalPage();
    }

    public List<Integer> generatePageSequence(){
        List<Integer> sequence = new ArrayList<>();
        int startNum = getStartNum();
        for(int i = 0; i < 10; i++){
            if(startNum + i > getTotalPage()){
                break;
            }
            sequence.add(startNum + i);
        }
        return sequence;
    }

    public String generateSearchQueryString(){
        return criteria.generateSearchQueryString();
    }

    public String generateSearchQueryString(int page){
        return criteria.generateSearchQueryString(page);
    }

    public String generateFirstPageQueryString(){
        return criteria.generateSearchQueryString(1);
    }

    public String generateLastPageQueryString(){
        return criteria.generateSearchQueryString(getTotalPage());
    }

    public String generatePrevPageQueryString(){
        return criteria.generateSearchQueryString(getStartNum() - 10);
    }

    public String generateNextPageQueryString(){
        return criteria.generateSearchQueryString(getStartNum() + 10);
    }

}
