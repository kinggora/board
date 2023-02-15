<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="java.io.PrintWriter" %><%--
  Created by IntelliJ IDEA.
  User: HYUNA
  Date: 2023-02-10
  Time: 오후 2:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String id = request.getParameter("id");
    String password = request.getParameter("password");

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
