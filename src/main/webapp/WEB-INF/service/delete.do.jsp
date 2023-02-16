<%@ page import="com.example.board.dao.PostDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String id = (String)request.getAttribute("id");
    String password = (String)request.getAttribute("password");

    //삭제 성공 -> 게시판 목록 이동
    boolean deleteResult = PostDao.deletePost(id, password);
    if(deleteResult){
        out.println("<script>alert('삭제되었습니다.'); window.location.href='/boards/free/list';</script>");
        out.flush();
    } else{
        pageContext.setAttribute("id", id);
    }
%>
<script>
    alert('게시물 등록 시 입력한 비밀번호와 다릅니다.');
    window.location.href='/boards/free/view/${id}';
</script>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
