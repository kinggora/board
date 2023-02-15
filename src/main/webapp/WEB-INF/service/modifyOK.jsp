<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="com.example.board.model.PostUpdateDto" %>
<%@ page import="com.example.board.util.FileStore" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.board.dao.FileDao" %>
<%@ page import="com.example.board.model.AttachFile" %>
<%@ page import="com.example.board.validation.PostValidator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String postId = request.getParameter("id");
    String writer = request.getParameter("writer");
    String password = request.getParameter("password");
    String title = request.getParameter("title");
    String content = request.getParameter("content");

    //Validation
    if(PostValidator.modifyValidation(postId, writer, password, title, content)){
        //POST update
        PostUpdateDto dto = PostUpdateDto
                .builder()
                .postId(postId)
                .writer(writer)
                .password(password)
                .title(title)
                .content(content)
                .build();

        boolean updateResult = PostDao.updatePost(dto);

        //FILE update
        if(updateResult){
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
                FileDao.saveFile(Integer.parseInt(postId), attachFiles);
            }

            response.sendRedirect("/boards/free/view/" + postId);
        } else {
            pageContext.setAttribute("dto", dto);
        }
    } else {
        out.println("<script>alert('입력이 올바르지 않습니다.');" +
                " window.location.href='/boards/free/list';</script>");
        out.flush();
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