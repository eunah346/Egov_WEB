<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript">
	
	var count = 0;
	
	//$(document).ready(function(){	
	$(function(){
				//{다음부터는 html이 로드된 후 실행되는  javascript쪽
		
		$('#myButton').click(function()
		{
			
			var formselect = $("#myForm");
			var formdata = formselect.serialize();
			
			//웹페이지 새로고침이 필요없는 요청
			 $.ajax({
			        url : "<%=request.getContextPath() %>/testjson.do",
			        type: "POST",
			        async: false,
					data: formdata,
				    timeout: 10000,
			        success : function(data){
			            if(data.Name == "abc123")
			            {
				            //동적으로 div태그 달아주기.
				            $(".myList").append("<div>"+data.Name+"/"+data.Text+"</div>")
			            }
			            
			            //JQUERY를 좀더 추가하였습니다.
			            count = count+1;
			            if(count == 5)
			            {
				            $(".myList").empty();
				            count=0;
			            }
			            
						            
						//게시판 조회,게시판 재조회,댓글 등등 페이지 이동 없이 처리가능.
						
					},
					error: function() {
						alert("실패");
					}
		   
		        });
			//여기에 추가적인 코드가있을 경우
			//async: false,async: true(기본값)이면 ajax호출후  바로실행
		});
	
	});
</script>
</head>
<body>
	<form name="myForm" id="myForm">
		내용:<input type="text" name="mytext">
		<br/>
		<input type="button" id="myButton" value="추가">
	</form>
	
	<div class="myList"></div>
</body>
</html>


