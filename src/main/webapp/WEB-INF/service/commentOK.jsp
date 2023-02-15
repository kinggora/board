<%@ page import="com.example.board.dao.CommentDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = request.getParameter("id");
    String comment = request.getParameter("comment");
    if(id != null && comment != null){
        CommentDao.saveComment(id, request.getParameter("comment"));
    }
    response.sendRedirect("/boards/free/view/" + id);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
