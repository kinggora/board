<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String storeName = request.getParameter("storeName");
    String storeDir = request.getParameter("storeDir");
    String origName = request.getParameter("origName");

    File file = new File(storeDir, storeName);
    FileInputStream fis = new FileInputStream(file);
    origName = new String(origName.getBytes("utf-8"), "8859_1");

    response.setContentType("application/octet-stream");
    response.setHeader("Cache-Control", "no-cache");
    response.addHeader("Content-disposition", "attachment; fileName=" + origName);

    OutputStream os = response.getOutputStream();

    byte[] buffer = new byte[1024 * 8];
    while(true){
        int count = fis.read(buffer);
        if(count == -1){
            break;
        }
        os.write(buffer, 0, count);
    }
    os.flush();

    os.close();
    fis.close();
%>
<html>
<head>
    <title>download</title>
</head>
<body>

</body>
</html>
