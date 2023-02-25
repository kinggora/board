package com.example.board.web.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageManager {
    private int totalCount;
    private SearchCriteria criteria;

    public PageManager(int totalCount, SearchCriteria criteria) {
        this.totalCount = totalCount;
        this.criteria = criteria;
    }
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


//    <c:set var="startNum" value="${criteria.page-((criteria.page-1)%10)}"/>
//    <c:if test="${criteria.page != 1}">
//      <a href="?page=1&startDate=${criteria.startDate}&endDate=${criteria.endDate}&categoryId=${criteria.categoryId}&searchWord=${criteria.searchWord}"><<</a>
//    </c:if>
//    <c:if test="${criteria.page == 1}">
//      <a onclick="alert('첫 번째 페이지 입니다.')"><<</a>
//    </c:if>
//            &nbsp; &nbsp;
//    <c:if test="${startNum-10 > 0}">
//      <a href="?page=${startNum-10}&startDate=${criteria.startDate}&endDate=${criteria.endDate}&categoryId=${criteria.categoryId}&searchWord=${criteria.searchWord}"><</a>
//    </c:if>
//    <c:if test="${startNum-10 <= 0}">
//      <a onclick="alert('이전 페이지가 없습니다.')"><</a>
//    </c:if>
//            &nbsp; &nbsp;
//    <c:forEach var="i" begin="0" end="9">
//      <c:if test="${startNum+i <= pageInfo.totalPage}">
//        <a href="?page=${startNum+i}&startDate=${criteria.startDate}&endDate=${criteria.endDate}&categoryId=${criteria.categoryId}&searchWord=${criteria.searchWord}">${startNum+i}</a>
//      </c:if>
//    </c:forEach>
//            &nbsp; &nbsp;
//    <c:if test="${startNum+10 <= pageInfo.totalPage}">
//      <a href="?page=${startNum+10}&startDate=${criteria.startDate}&endDate=${criteria.endDate}&categoryId=${criteria.categoryId}&searchWord=${criteria.searchWord}">></a>
//    </c:if>
//    <c:if test="${startNum+10 > pageInfo.totalPage}">
//      <a onclick="alert('다음 페이지가 없습니다.')">></a>
//    </c:if>
//            &nbsp; &nbsp;
//    <c:if test="${criteria.page != pageInfo.totalPage}">
//      <a href="?page=${pageInfo.totalPage}&startDate=${criteria.startDate}&endDate=${criteria.endDate}&categoryId=${criteria.categoryId}&searchWord=${criteria.searchWord}">>></a>
//    </c:if>
//    <c:if test="${criteria.page == pageInfo.totalPage}">
//      <a onclick="alert('마지막 페이지 입니다.')">>></a>
//    </c:if>
}
