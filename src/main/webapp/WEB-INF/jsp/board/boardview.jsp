<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 상세페이지</title>
</head>
<body>

	<div>작성자:${userid}</div><br/>
	<div>제목:${title}</div><br/>
	<div>내용:<textarea readonly>${boardcontents}</textarea></div><br/>
	<a href=""><div>목록보기</div></a>
</body>
</html>