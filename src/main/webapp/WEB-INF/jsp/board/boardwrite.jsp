<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="boardInsert.do" method="post">
		제목<input type="text" name="title"><br/>
		본문<textarea name="mytextarea" style="height:500px;"></textarea><br/>
		<input type="submit" value="제출">
		<a href="boardList.do"><input type="button" value="목록"></a>
	</form>
</body>
</html>