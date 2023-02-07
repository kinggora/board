<%@ page import="com.example.board.dao.PostDao" %>
<%@ page import="com.example.board.dto.PostViewDto" %>
<%@ page import="com.example.board.dao.CategoryDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String pathInfo = request.getPathInfo();
    String[] pathParts = pathInfo.split("/");
    int id = Integer.parseInt(pathParts[1]); //{seq}

    PostViewDto post = PostDao.findPost(id);
    String category = CategoryDao.getCategoryName(post.getCategoryId());
    pageContext.setAttribute("post", post);
    pageContext.setAttribute("category", category);
%>
<script type="text/javascript">
    function checkForm(){
        let form = document.commentForm;
        if(form.comment.value == ""){
            alert("댓글을 입력해주세요.");
            form.comment.select();
            return;
        }
        return form.submit();
    }
</script>
<html>
<head>
    <title>게시</title>
</head>
<body>
<h1>게시판 - 보기</h1>
<dv>
    ${post.writer} 등록일시 ${post.regDate} 수정일시 ${post.modDate}<br>
</dv>
<dv>
    [${category}] ${post.title} 조회수: ${post.hit}<br>
</dv>
<dv>
    ${post.content}<br>
</dv>
<dv>
    첨부파일<br>
</dv>
<dv>
    댓글 있으면 출력<br>
    <form name="commentForm" method="post">
        <input type="text" name="comment"/>
        <input type="button" value="등록" onclick="checkForm()"><br>
    </form>

</dv>
<dv>
    <input type="button" value="목록">
    <input type="button" value="수정">
    <input type="button" value="삭제">
</dv>
</body>
</html>
