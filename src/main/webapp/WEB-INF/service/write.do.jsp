<%@ page import="com.example.board.web.service.PostDao" %>
<%@ page import="com.example.board.web.model.PostSaveDto" %>
<%@ page import="com.example.board.web.util.FileStore" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.board.web.model.AttachFile" %>
<%@ page import="com.example.board.web.service.FileDao" %>
<%@ page import="com.example.board.web.validation.PostValidator" %>
<%@ page import="java.sql.Timestamp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  String categoryId = (String)request.getAttribute("category");
  String writer = (String)request.getAttribute("writer");
  String password = (String)request.getAttribute("password");
  String password2 = (String)request.getAttribute("password2");
  String title = (String)request.getAttribute("title");
  String content = (String)request.getAttribute("content");

  PostValidator validator = PostValidator.builder()
          .id(categoryId)
          .writer(writer)
          .password(password)
          .password2(password2)
          .title(title)
          .content(content)
          .build();

  if(validator.createValidation()){
    //POST insert
    PostSaveDto dto = PostSaveDto.builder()
            .categoryId(Integer.parseInt(categoryId))
            .writer(writer)
            .password(password)
            .title(title)
            .content(content)
            .build();
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
      List<AttachFile> attachFiles = FileStore.uploadFiles(id, parts);
      //파일 정보 저장 (database)
      FileDao.saveFile(attachFiles);
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
