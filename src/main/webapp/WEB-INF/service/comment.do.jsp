<%@ page import="com.example.board.dao.CommentDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = (String)request.getAttribute("id");
    String comment = (String)request.getAttribute("comment");
    if(!id.isBlank() && !comment.isBlank()){
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
