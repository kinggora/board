<%@ page import="com.example.board.web.service.CommentDao" %>
<%@ page import="com.example.board.web.util.TypeConvertor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = (String)request.getAttribute("id");
    String comment = (String)request.getAttribute("comment");
    if(id != null && comment != null && !id.isBlank() && !comment.isBlank()){
        int postId = TypeConvertor.stringToInt(id);
        CommentDao.saveComment(postId, comment);
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
