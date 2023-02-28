<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript">

    function modifyPost(){
        let form = document.modifyForm;
        form.action = "/boards/free/modify";
        return form.submit();
    }

    function createCmt(){
        let form = document.commentForm;
        if(form.comment.value == ""){
            alert("댓글을 입력해주세요.");
            form.comment.select();
        }
        return form.submit();
    }

    function checkPassword(){
        let form = document.deleteForm;
        if(form.password.value == ""){
            alert("비밀번호를 입력해주세요.");
            form.password.select();
        }
        return form.submit();
    }

    function openPopup(){
        document.getElementById("popup_layer").style.display="block";
    }

    function closePopup(){
        document.getElementById("popup_layer").style.display="none";
    }
</script>

<html>
<head>
    <title>게시</title>
    <style>
        body { text-align: center; }
        .wrapper {
            width: 97%; margin: 0 auto; text-align: left;
        }
        table {
            width: 100%;
            border-top: 1px solid #444444;
            border-collapse: collapse;
        }
        th, td {
            border-bottom: 1px solid #444444;
            padding: 10px;
        }
        input_wrap {
            display: flex;
            justify-content: flex-end;
            width: 300px;
        }
        .input {
            width: 90%;
            height: 50px;
        }
        .comment_submit_btn {
            border: 1px solid black;
            background-color: white;
            width: 100px;
            height: 50px;
            text-align: center;
        }
        .popup_layer {
            position: absolute;
            top: 50%;
            left: 50%;
            margin-top: -100px;
            margin-left: -150px;
            width: 300px;
            height: 150px;
            padding-bottom: 50px;
            background: #fff;
            z-index: 10;}
        .popup_box{text-align: center;position: relative;top:50%;left:50%; overflow: auto; height: 200px; width:375px;transform:translate(-50%, -50%);z-index:1002;box-sizing:border-box;background:#fff;box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);-webkit-box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);-moz-box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);}
        .popup_box .popup_cont {padding:40px;line-height:1.4rem;font-size:14px; }
        .btn_area{
            padding-left: 20px;
            display: table;
            position: relative;
            table-layout: fixed;
            width: 100%;
            box-sizing: border-box;
        }
        .btn_area > button{
            border: 1px solid;
            padding: 5px 5px;
            color: #fff;
            font-size: 14px;
            background-color: gray;
            box-sizing: border-box;
            display: table-cell;
            width: 35%;
            margin-right: 5%;
            cursor: pointer;
            box-shadow: 0px 0px 8px -5px #000;
        }
    </style>
</head>
<body>
<div class="wrapper">
<h1>게시판 - 보기</h1>
<div style="float: left">${post.writer}</div>
    <div style="float: right;">등록일시 ${post.regDateToString()} &nbsp; &nbsp; &nbsp; &nbsp; 수정일시 ${post.modDateToString()}</div><br><br>

<div style="float: left">[${post.categoryName}] ${post.title}</div><div style="float: right">조회수: ${post.hit}</div><br><br>
<dv class="content">
    <p>${post.content}</p>
</dv>
<dv>
    <c:forEach var="file" items="${fileList}">
        <form method="post" action="/boards/free/download.do">
            <input type="hidden" name="fileId" value="${file.fileId}"/>
            <input type="submit" value="${file.origName}" target="_blank"/><br >
        </form>
    </c:forEach>
    <br >
    <br >
</dv>
<dv>
    <table>
        <c:forEach var="comment" items="${commentList}">
        <tr>
            <td>
            ${comment.regDateToString()}<br>${comment.content}<br>
            </td>
        </tr>
        </c:forEach>
        <tr>
            <td>
            <form name="commentForm" method="post" action="/boards/free/comment.do" onsubmit="return false">
                <input type="hidden" name="id" value="${post.postId}"/>
                <div class="input_wrap">
                    <input class="input" type="text" name="comment" maxlength="499" placeholder="댓글을 입력해 주세요."/>
                    <button class="comment_submit_btn" onclick="createCmt()">등록</button>
                </div>
            </form>
            </td>
        </tr>
    </table>
</dv>
<dv>
    <input type="button" value="목록" onclick="location.href='/boards/free/list?${criteria.generateSearchQueryString()}'">
    <form name="modifyForm" method="post">
        <input type="hidden" name="id" value="${post.postId}"/>
        <input type="hidden" name="page" value="${criteria.page}"/>
        <input type="hidden" name="categoryId" value="${criteria.categoryId}"/>
        <input type="hidden" name="searchWord" value="${criteria.searchWord}"/>
        <input type="hidden" name="startDate" value="${criteria.startDate}"/>
        <input type="hidden" name="endDate" value="${criteria.endDate}"/>
    </form>
    <input type="button" value="수정" onclick="modifyPost()">
    <input type="button" value="삭제" onclick="openPopup()">
</dv>

<div class="popup_layer" id="popup_layer" style="display: none;">
    <!--팝업-->
    <div class="popup_box"> <!--팝업창-->
        <div class="popup_cont"><!--텍스트 영역-->
            <p class="text">
                <form name="deleteForm" method="post" action="/boards/free/delete.do" onsubmit="return false">
                    <input type="hidden" name="id" value="${post.postId}"/>
                    <input type="hidden" name="page" value="${criteria.page}"/>
                    <input type="hidden" name="categoryId" value="${criteria.categoryId}"/>
                    <input type="hidden" name="searchWord" value="${criteria.searchWord}"/>
                    <input type="hidden" name="startDate" value="${criteria.startDate}"/>
                    <input type="hidden" name="endDate" value="${criteria.endDate}"/>
                    비밀번호 <input type="password" name="password" placeholder="비밀번호를 입력해 주세요.">
                </form>
            </p>
        </div>
        <div class="btn_area"><!--버튼 영역-->
            <button type="button" name="cancel" onclick="closePopup()">취소</button>
            <button type="button" name="submit" onclick="checkPassword()">확인</button>
        </div>
    </div>
</div>
</div>
</body>
</html>