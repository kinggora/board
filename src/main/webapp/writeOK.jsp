<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="com.example.board.dto.PostSaveDto" %>
<%@ page import="com.example.board.validation.PostValidation" %><%--
  Created by IntelliJ IDEA.
  User: HYUNA
  Date: 2023-02-06
  Time: 오후 3:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  int categoryId = Integer.parseInt(request.getParameter("category"));
  String writer = request.getParameter("writer");
  String password = request.getParameter("password");
  String password2 = request.getParameter("password2");
  String title = request.getParameter("title");
  String content = request.getParameter("content");
  //String file = request.getParameter("file");

  //dto 검증 작업 통과시
  //if(PostValidation.validate())
  PostSaveDto dto =  new PostSaveDto(categoryId, writer, password, title, content);
  int id = PostDao.savePost(dto);
  response.sendRedirect("/boards/free/view/" + id);
%>
<html>
<head>
    <title>저장</title>
</head>
<body>
저장 완료
</body>
</html>
