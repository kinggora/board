<%@ page import="com.example.board.dao.CommentDao" %><%--
  Created by IntelliJ IDEA.
  User: HYUNA
  Date: 2023-02-10
  Time: 오후 10:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = request.getParameter("id");
    String comment = request.getParameter("comment");
    if(id != null && comment != null){
        System.out.println(request.getParameter("comment"));
        CommentDao.saveComment(id, request.getParameter("comment"));
    }
    System.out.println("id = " + id);
    System.out.println("comment = " + comment);

    response.sendRedirect("/boards/free/view/" + id);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
