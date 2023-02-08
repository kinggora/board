<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="com.example.board.dto.PostSaveDto" %>
<%@ page import="com.example.board.validation.PostValidation" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="com.example.board.util.FileStore" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.board.dto.AttachFile" %>
<%@ page import="com.example.board.dao.FileDao" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  int categoryId = Integer.parseInt(request.getParameter("category"));
  String writer = request.getParameter("writer");
  String password = request.getParameter("password");
  String password2 = request.getParameter("password2");
  String title = request.getParameter("title");
  String content = request.getParameter("content");

  //dto 검증 작업 통과시
  //if(PostValidation.validate())
  PostSaveDto dto =  new PostSaveDto(categoryId, writer, password, title, content);
  int id = PostDao.savePost(dto);

  FileStore fileStore = new FileStore();
  List<Part> parts = new ArrayList<>();
  for(Part p : request.getParts()){
    if(p.getName().equals("file") && p.getSize() != 0){
      parts.add(p);
    }
  }

  if(!parts.isEmpty()){
    List<AttachFile> attachFiles = fileStore.storeFiles(parts);
    FileDao.saveFile(id, attachFiles);
  }

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
