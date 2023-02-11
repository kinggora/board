<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="com.example.board.model.PostSaveDto" %>
<%@ page import="com.example.board.util.FileStore" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.board.model.AttachFile" %>
<%@ page import="com.example.board.dao.FileDao" %>
<%@ page import="com.example.board.validation.PostValidator" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  String categoryId = request.getParameter("category");
  String writer = request.getParameter("writer");
  String password = request.getParameter("password");
  String password2 = request.getParameter("password2");
  String title = request.getParameter("title");
  String content = request.getParameter("content");


  if(PostValidator.createValidation(categoryId, writer, password, password2, title, content)){
    //POST insert
    PostSaveDto dto = new PostSaveDto(Integer.parseInt(categoryId), writer, password, title, content);
    int id = PostDao.savePost(dto);

    //FILE insert
    List<Part> parts = new ArrayList<>();
    for(Part p : request.getParts()){
      if(p.getName().equals("file") && p.getSize() != 0){
        parts.add(p);
      }
    }
    if(!parts.isEmpty()){
      //파일 저장 (storage)
      List<AttachFile> attachFiles = FileStore.storeFiles(parts);
      //파일 정보 저장 (database)
      FileDao.saveFile(id, attachFiles);
    }
    response.sendRedirect("/boards/free/view/" + id);
  }
%>
<script>
  alert('입력이 올바르지 않습니다.');
  window.location.href='/board/free/write';
</script>
<html>
<head>
    <title>저장</title>
</head>
<body>
</body>
</html>
