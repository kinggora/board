<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

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

    let regPass = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{4,16}$/;
    if(!regPass.test(form.password.value)){
      alert("비밀번호는 영문/숫자/특수문자를 포함하여 4글자 이상, 16글자 미만으로 입력해주세요.");
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
    let div = obj.closest("div");
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
  <form name="modify" method="post" action="/board/free/modify.do" enctype="multipart/form-data" onsubmit="return false;">
    <table>
      <input type="hidden" name="id" value='${post.postId}'/>
      <tr>
        <td>카테고리</td>
        <td>${post.categoryName}</td>
      </tr>
      <tr>
        <td>등록 일시</td>
        <td>${post.regDateToString()}</td>
      </tr>
        <td>수정 일시</td>
        <td>${post.modDateToString()       }</td>
      <tr>
        <td>작성자</td>
        <td><input type="text" name="writer" value="${post.writer}"/></td>
      </tr>
      <tr>
        <td>비밀번호</td>
        <td><input type="password" name="password"/></td>
      </tr>
      <tr>
        <td>제목</td>
        <td><input type="text" name="title" value="${post.title}"/></td>
      </tr>
      <tr>
        <td>내용</td>
        <td><textarea name="content" cols="50" rows="10" >${post.content}</textarea></td>
      </tr>
      <tr>
        <td>파일 첨부</td>
        <td>
          <c:forEach var="file" items="${fileList}">
            <div>
              <form method="post" id="${file.storeName}" action="/board/free/download.do">
                <input type="hidden" name="storeName" value="${file.storeName}"/>
                <input type="hidden" name="origName" value="${file.origName}"/>
                <input type="hidden" name="storeDir" value="${file.storeDir}"/>
                  ${file.origName}&nbsp;<input type="submit" value="Download" onclick="downloadPopup(${file.storeName});"/>
              </form>
              <input type="button" name="remove" value="X" onclick="deleteFile(this)"/><br>
            </div>
          </c:forEach>
          <input type="file" name="files"/><br>
        </td>
      </tr>
    </table>
    <input type="button" value="취소" onclick="location.href='/boards/free/view/'+${post.postId}"/>
    <input type="button" value="저장" onclick="checkForm()"/>
  </form>
</div>
</body>
</html>