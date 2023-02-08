<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="com.example.board.dto.PostViewDto" %>
<%@ page import="com.example.board.dao.CommentDao" %>
<%@ page import="com.example.board.dto.CommentDto" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String pathInfo = request.getPathInfo();
    String[] pathParts = pathInfo.split("/");
    String id = pathParts[1]; //{seq}

    if(request.getParameter("comment") != null){
        CommentDao.saveComment(id, request.getParameter("comment"));
    }

    PostViewDto post = PostDao.findPost(id);
    pageContext.setAttribute("p", post);

    List<CommentDto> commentList = CommentDao.findComment(id);
    pageContext.setAttribute("list", commentList);
%>
<script type="text/javascript">
    function checkForm(){
        let form = document.commentForm;
        if(form.comment.value == ""){
            alert("댓글을 입력해주세요.");
            form.comment.select();
            return;
        }
        return form.submit();
    }
</script>
<html>
<head>
    <title>게시</title>
    <style>
        tr{
            background-color: #e9e9e9;
            padding: 10px 5px;
        }
    </style>
</head>
<body>
<h1>게시판 - 보기</h1>
<dv>
    ${p.writer} 등록일시 ${p.regDate} 수정일시 ${p.modDate}<br>
</dv>
<dv>
    [${p.category}] ${p.title} 조회수: ${p.hit}<br>
    ------------------------------------------------------------------------------------<br >
</dv>
<dv>
    ${p.content}<br><br >
</dv>
<dv>
    첨부파일<br><br >
</dv>
<dv>
    <table>
        <c:forEach var="c" items="${list}">
        <tr>
        ${c.regDate}<br >
        ${c.content}<br >
        ------------------------------------------------------------------------------------<br >
        </tr>
        </c:forEach>
        <tr>
            <form name="commentForm" method="post">
                <input type="text" name="comment"/>
                <input type="button" value="등록" onclick="checkForm()"><br>
            </form>
        </tr>
    </table>
    ------------------------------------------------------------------------------------<br >
</dv>
<dv>
    <input type="button" value="목록">
    <input type="button" value="수정">
    <input type="button" value="삭제">
</dv>
</body>
</html>
