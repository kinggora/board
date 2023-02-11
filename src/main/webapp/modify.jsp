<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="com.example.board.model.PostViewDto" %>
<%@ page import="com.example.board.dao.FileDao" %>
<%@ page import="com.example.board.model.AttachFile" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<%
  String postId = request.getParameter("id");
  PostViewDto post = PostDao.findPostById(postId, false);

  //수정 시도 중 비밀번호를 틀렸을 경우 데이터 유지
  String writer = request.getParameter("writer");
  String title = request.getParameter("title");
  String content = request.getParameter("content");
  post.modifyDto(writer, title, content);

  pageContext.setAttribute("p", post);

  List<AttachFile> fileList = FileDao.findFile(postId);
  pageContext.setAttribute("fl", fileList);
%>
<script type="text/javascript">
  function checkForm(){
    let form = document.modify;

    if(form.writer.value == ""){
      alert("작성자를 입력해주세요.");
      form.writer.select();
      return;
    }

    if(form.writer.value.length < 3 || form.writer.value.length >= 5){
      alert("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.");
      form.writer.select();
      return;
    }

    if(form.password.value == ""){
      alert("등록할 때의 비밀번호를 입력해주세요.");
      form.password.select();
      return;
    }

    if(form.title.value.length < 4){
      alert("제목은 4글자 이상 입력해주세요.");
      form.title.select();
      return;
    }

    if(form.title.value.length >= 100){
      alert("제목으로 가능한 글자수를 초과했습니다.");
      form.title.select();
      return;
    }

    if(form.content.value.length < 4){
      alert("내용은 4글자 이상 입력해주세요.");
      form.content.select();
      return;
    }

    if(form.content.value.length > 2000){
      alert("내용으로 가능한 글자수를 초과했습니다.");
      form.content.select();
      return;
    }

    form.submit();
  }

  function downloadPopup(id){
    let popup = window.open("","myWindow","width=100, height=100");
    let form = document.getElementById(id);
    form.target="myWindow";
    if (popup == null)
      alert('차단된 팝업창을 허용해 주세요');
    else{
      form.submit();
      popup.focus();
    }
    form.submit();
  }

  function deleteFile(obj) {
    let div = $(obj).closest("div");
    div.remove();
  }
</script>
<html>
<head>
  <title>게시판 수정</title>
</head>
<body>
<h1>게시판 - 수정</h1>
<div>
  <form name="modify" method="post" action="/board/free/modifyOK" enctype="multipart/form-data" onsubmit="return false;">
    <table>
      <input type="hidden" name="id" value='${p.postId}'/>
      <tr>
        <td>카테고리</td>
        <td>${p.category}</td>
      </tr>
      <tr>
        <td>등록 일시</td>
        <td>${p.regDate}</td>
      </tr>
        <td>수정 일시</td>
        <td>${p.modDate}</td>
      <tr>
        <td>작성자</td>
        <td><input type="text" name="writer" value="${p.writer}"/></td>
      </tr>
      <tr>
        <td>비밀번호</td>
        <td><input type="password" name="password"/></td>
      </tr>
      <tr>
        <td>제목</td>
        <td><input type="text" name="title" value="${p.title}"/></td>
      </tr>
      <tr>
        <td>내용</td>
        <td><textarea name="content" cols="50" rows="10" >${p.content}</textarea></td>
      </tr>
      <tr>
        <td>파일 첨부</td>
        <td>
          <c:forEach var="f" items="${fl}">
            <div>
              <form method="post" id="${f.storeName}" action="/board/free/download.do">
                <input type="hidden" name="storeName" value="${f.storeName}"/>
                <input type="hidden" name="origName" value="${f.origName}"/>
                <input type="hidden" name="storeDir" value="${f.storeDir}"/>
                  ${f.origName}&nbsp;<input type="submit" value="Download" onclick="downloadPopup(${f.storeName});"/>
              </form>
              <input type="button" name="remove" value="X" onclick="deleteFile(this)" /><br>
            </div>
          </c:forEach>
          <input type="file" name="file"/><br>
        </td>
      </tr>
    </table>
    <input type="button" value="취소" onclick="location.href='/boards/free/list'"/>
    <input type="button" value="저장" onclick="checkForm()"/>
  </form>
</div>
</body>
</html>