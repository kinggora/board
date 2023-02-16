<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.board.dao.CategoryDao" %>
<%@ page import="com.example.board.dao.FileDao" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="com.example.board.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String currentPage = (String)request.getAttribute("page");
    String searchCategory = (String)request.getAttribute("category");
    String searchWord = (String)request.getAttribute("search_word");

    PostSearch postSearch = PostSearch
            .builder()
            .pageNumber(currentPage)
            .categoryId(searchCategory)
            .searchWord(searchWord)
            .build();

  PostListDto postListDto = PostDao.findPosts(postSearch);
  List<PostViewDto> postList = postListDto.getPostList();
  pageContext.setAttribute("postList", postList);

  PageInfo pageInfo = postListDto.getPageInfo();
  pageContext.setAttribute("pageInfo", pageInfo);

  List<String> idList = postList.stream()
          .map(post -> String.valueOf(post.getPostId()))
          .collect(Collectors.toList());
  List<Integer> attachedList = FileDao.isAttached(idList);
  pageContext.setAttribute("attachedList", attachedList);

  pageContext.setAttribute("page", postSearch.getPageNumber());
  pageContext.setAttribute("category", postSearch.getCategoryId());
  pageContext.setAttribute("searchWord", postSearch.getSearchWord());

  List<Category> categories = CategoryDao.getCategories();
  pageContext.setAttribute("categories", categories);

%>
<html>
<head>
    <title>게시판 목록</title>
</head>
<script>
  function search(){
    let form = document.searchForm;
    if(form.search_word.value.length < 2){
      alert("검색어를 2자 이상 입력해 주세요.");
      return;
    }
    return form.submit();
  }
</script>
<style>
  table{
    width: 100%;
  }
  body { background: #fff; }
  .wrapper {
    width: 97%; margin: 0 auto; text-align: left;
  }
  .blueone {
    border-collapse: collapse;
    text-align:center;
    table-layout: fixed;

  }
  .blueone th {
    padding: 10px;
    color: #168;
    border-bottom: 3px solid #168;
  }
  .blueone td {
    color: #669;
    padding: 10px;
    border-bottom: 1px solid #ddd;
  }
  .blueone tr:hover td {
    color: #004;
  }
  a:link {
     color : #669;
   }
  a:visited {
    color : purple;
  }
  a:hover {
    color : purple;
  }
  a:active {
    color : green
  }

</style>

<body>
<div class="wrapper">
    <a class="title" style="text-decoration-line: none;" href="/boards/free/list"><h1>자유 게시판 - 목록</h1></a>
<div>
  <form name="searchForm" onsubmit="return false">
    <select name="category">
      <option value="">전체 카테고리</option>
      <c:forEach var="c" items="${categories}">
        <option value="${c.id}">${c.name}</option>
      </c:forEach>
    </select>
    <input type="text" name="search_word" style="width: 300px" placeholder="검색어를 입력해 주세요. (제목+작성자+내용)">
    <button class="search_btn" onclick="search()">검색</button>
  </form>
</div>
<p><div style="float: left">총 ${pageInfo.totalCount}건</div>
    <div style="float: right"><button onclick="location.href='/board/free/write'">등록</button> </div>
    </p>
    <br >
    <br >
<table class="blueone">
  <tr>
    <th>카테고리</th>
    <th colspan="5">제목</th>
    <th>작성자</th>
    <th>조회수</th>
    <th>등록 일시</th>
    <th>수정 일시</th>
  </tr>
  <c:forEach var="post" items="${postList}">
    <tr>
      <td>${post.category}</td>
      <td style="text-align:left" colspan="5">
          <c:forEach var="attached" items="${attachedList}">
              <c:if test="${post.postId eq attached}">
                  <img src="../../resources/img/attach1.png"/>
              </c:if>
          </c:forEach>

        <a href="/boards/free/view/${post.postId}">
          <c:set var="title" value="${post.title}"></c:set>
            ${fn:length(title) > 80 ? (fn:substring(title,0,80) += "...") : title}
        </a></td>
      <td>${post.writer}</td>
      <td>${post.hit}</td>
      <td>${post.regDate}</td>
      <td>${post.modDate}</td>
    </tr>
  </c:forEach>
</table>
<div style="text-align: center;font-size: 20px;">
  <p>
<c:set var="startNum" value="${page-((page-1)%10)}"/>
    <c:if test="${page != 1}">
      <a href="?page=1&category=${category}&search_word=${searchWord}"><<</a>
    </c:if>
    <c:if test="${page == 1}">
      <a onclick="alert('첫 번째 페이지 입니다.')"><<</a>
    </c:if>
    &nbsp; &nbsp;
    <c:if test="${startNum-10 > 0}">
      <a href="?page=${startNum-10}&category=${category}&search_word=${searchWord}"><</a>
    </c:if>
    <c:if test="${startNum-10 <= 0}">
      <a onclick="alert('이전 페이지가 없습니다.')"><</a>
    </c:if>
    &nbsp; &nbsp;
    <c:forEach var="i" begin="0" end="9">
      <c:if test="${startNum+i <= pageInfo.pageCount}">
        <a href="?page=${startNum+i}&category=${category}&search_word=${searchWord}">${startNum+i}</a>
      </c:if>
    </c:forEach>
    &nbsp; &nbsp;
    <c:if test="${startNum+10 <= pageInfo.pageCount}">
      <a href="?page=${startNum+10}&category=${category}&search_word=${searchWord}">></a>
    </c:if>
    <c:if test="${startNum+10 > pageInfo.pageCount}">
      <a onclick="alert('다음 페이지가 없습니다.')">></a>
    </c:if>
    &nbsp; &nbsp;
    <c:if test="${page != pageInfo.pageCount}">
      <a href="?page=${pageInfo.pageCount}&category=${category}&search_word=${searchWord}">>></a>
    </c:if>
    <c:if test="${page == pageInfo.pageCount}">
      <a onclick="alert('마지막 페이지 입니다.')">>></a>
    </c:if>
  </p>
</div>
</div>
</body>
</html>
