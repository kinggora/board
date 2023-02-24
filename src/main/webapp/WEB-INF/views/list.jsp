<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>게시판 목록</title>
</head>
<script>
  function search(){
    let form = document.searchForm;
    if(form.searchWord.value.length < 2) {
        alert("검색어를 2자 이상 입력해 주세요.");
        return;
    }

    let str = form.searchWord.value.replace(/\s/g, "");
    if(str.length == 0){
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
    <select name="categoryId">
      <option value="">전체 카테고리</option>
      <c:forEach var="category" items="${categories}">
        <option value="${category.id}">${category.name}</option>
      </c:forEach>
    </select>
    <input type="text" name="searchWord" style="width: 300px" placeholder="검색어를 입력해 주세요. (제목+작성자+내용)">
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
      <td>${post.categoryName}</td>
      <td style="text-align:left" colspan="5">
          <c:if test="${post.fileExists}">
              <img src="../../resources/img/attach1.png"/>
          </c:if>
        <a href="/boards/free/view/${post.postId}">
          <c:set var="title" value="${post.title}"></c:set>
            ${fn:length(title) > 80 ? (fn:substring(title,0,80) += "...") : title}
        </a></td>
      <td>${post.writer}</td>
      <td>${post.hit}</td>
      <td>${post.regDateToString()}</td>
      <td>${post.modDateToString()}</td>
    </tr>
  </c:forEach>
</table>
<div style="text-align: center;font-size: 20px;">
  <p>
<c:set var="startNum" value="${page-((page-1)%10)}"/>
    <c:if test="${page != 1}">
      <a href="?page=1&categoryId=${categoryId}&searchWord=${searchWord}"><<</a>
    </c:if>
    <c:if test="${page == 1}">
      <a onclick="alert('첫 번째 페이지 입니다.')"><<</a>
    </c:if>
    &nbsp; &nbsp;
    <c:if test="${startNum-10 > 0}">
      <a href="?page=${startNum-10}&categoryId=${categoryId}&searchWord=${searchWord}"><</a>
    </c:if>
    <c:if test="${startNum-10 <= 0}">
      <a onclick="alert('이전 페이지가 없습니다.')"><</a>
    </c:if>
    &nbsp; &nbsp;
    <c:forEach var="i" begin="0" end="9">
      <c:if test="${startNum+i <= pageInfo.totalPage}">
        <a href="?page=${startNum+i}&categoryId=${categoryId}&searchWord=${searchWord}">${startNum+i}</a>
      </c:if>
    </c:forEach>
    &nbsp; &nbsp;
    <c:if test="${startNum+10 <= pageInfo.totalPage}">
      <a href="?page=${startNum+10}&categoryId=${categoryId}&searchWord=${searchWord}">></a>
    </c:if>
    <c:if test="${startNum+10 > pageInfo.totalPage}">
      <a onclick="alert('다음 페이지가 없습니다.')">></a>
    </c:if>
    &nbsp; &nbsp;
    <c:if test="${page != pageInfo.totalPage}">
      <a href="?page=${pageInfo.totalPage}&categoryId=${categoryId}&searchWord=${searchWord}">>></a>
    </c:if>
    <c:if test="${page == pageInfo.totalPage}">
      <a onclick="alert('마지막 페이지 입니다.')">>></a>
    </c:if>
  </p>
</div>
</div>
</body>
</html>
