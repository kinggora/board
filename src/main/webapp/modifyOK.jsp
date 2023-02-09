<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="com.example.board.dto.PostUpdateDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String postId = request.getParameter("id");
    String writer = request.getParameter("writer");
    String password = request.getParameter("password");
    String title = request.getParameter("title");
    String content = request.getParameter("content");

    PostUpdateDto dto = new PostUpdateDto(postId, writer, password, title, content);
    boolean updateResult = PostDao.updatePost(dto);
    if(updateResult){
        response.sendRedirect("/boards/free/view/" + postId);
    } else {
        pageContext.setAttribute("dto", dto);
    }
%>
<html>
<body>
<form name="modData" method="post" action="/board/free/modify">
    <input type="hidden" name="id" value="${dto.postId}"/>
    <input type="hidden" name="writer" value="${dto.writer}"/>
    <input type="hidden" name="title" value="${dto.title}"/>
    <input type="hidden" name="content" value="${dto.content}"/>
</form>
<script>
    alert("게시물 등록 시 입력한 비밀번호와 다릅니다.");
    document.modData.submit();
</script>
</body>
</html>