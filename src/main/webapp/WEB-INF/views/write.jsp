<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<script type="text/javascript">
    function checkForm(){
        let form = document.write;
        let regPass = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{4,16}$/;
        if(form.category.value == ""){
            alert("카테고리를 선택해주세요.");
            form.category.select();
        } else if(form.writer.value == ""){
            alert("작성자를 입력해주세요.");
            form.writer.select();
        } else if(form.writer.value.length < 3 || form.writer.value.length >= 5){
            alert("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.");
            form.writer.select();
        } else if(form.password.value == ""){
            alert("비밀번호를 입력해주세요.");
            form.password.select();
        } else if(!regPass.test(form.password.value)){
            alert("비밀번호는 영문/숫자/특수문자를 포함하여 4글자 이상, 16글자 미만으로 입력해주세요.");
            form.password.select();
        } else if(form.title.value.length < 4){
            alert("제목은 4글자 이상 입력해주세요.");
            form.title.select();
        } else if(form.title.value.length >= 100){
            alert("제목으로 가능한 글자수를 초과했습니다.");
            form.title.select();
        } else if(form.content.value.length < 4){
            alert("내용은 4글자 이상 입력해주세요.");
            form.content.select();
        } else if(form.content.value.length > 2000){
            alert("내용으로 가능한 글자수를 초과했습니다.");
            form.content.select();
        } else if(form.password.value != form.password2.value){
            alert("비밀번호가 일치하지 않습니다.");
            form.password2.select();
        } else {
            form.submit();
        }
    }
</script>
<html>
<head>
    <title>게시판 등록</title>
</head>
<body>
<h1>게시판 - 등록</h1>
<div>
<form name="write" method="post" enctype="multipart/form-data">
    <table>
        <tr>
            <td>카테고리</td>
            <td><select name="categoryId">
                <option value="0">카테고리 선택</option>
                <c:forEach var="c" items="${categories}">
                    <option value="${c.id}">${c.name}</option>
                </c:forEach>
            </select></td>
        </tr>
        <tr>
            <td>작성자</td>
            <td><input type="text" name="writer"/></td>
        </tr>
        <tr>
            <td>비밀번호</td>
            <td><input type="password" name="password" placeholder="비밀번호"/>
                <input type="password" name="password2" placeholder="비밀번호 확인"/></td>
        </tr>
        <tr>
            <td>제목</td>
            <td><input type="text" name="title"/></td>
        </tr>
        <tr>
            <td>내용</td>
            <td><textarea name="content" cols="50" rows="10"></textarea></td>
        </tr>
        <tr>
            <td>파일 첨부</td>
            <td>
                <input type="file" name="files" /><br>
                <input type="file" name="files" /><br>
                <input type="file" name="files" />
            </td>
        </tr>
    </table>
    <input type="button" value="취소">
    <input type="submit" value="저장" onclick="checkForm()">
</form>
</div>
</body>
</html>
