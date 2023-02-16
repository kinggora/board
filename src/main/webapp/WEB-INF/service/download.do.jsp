<%@ page import="com.example.board.dao.FileDao" %>
<%@ page import="com.example.board.model.AttachFile" %>
<%@ page import="com.example.board.util.FileStore" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = (String)request.getAttribute("fileId");

    AttachFile findFile = FileDao.findFileById(id);

    if(findFile != null){
        FileStore.downloadFile(findFile, response);
    }
%>
<html>
<head>
    <title>download</title>
</head>
<body>

</body>
</html>
