<%@ page import="com.example.board.web.service.FileDao" %>
<%@ page import="com.example.board.web.model.AttachFile" %>
<%@ page import="com.example.board.web.util.FileStore" %>
<%@ page import="com.example.board.web.util.TypeConvertor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String id = (String)request.getAttribute("fileId");

    int fileId = TypeConvertor.stringToInt(id);

    AttachFile findFile = FileDao.findFileById(fileId);

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
