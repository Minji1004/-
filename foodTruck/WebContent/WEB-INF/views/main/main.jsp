
 <%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/commonLayout.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/mainLayout.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/main.css"
	type="text/css">


<link rel="stylesheet"
	href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css"
	type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<script type="text/javascript">

function korea(){
		
	location.href="<%=cp%>/searchlist/searchum.do?usercode=um1";

}

function china(){
	
	location.href="<%=cp%>/searchlist/searchum.do?usercode=um2";

	
}

function japan(){
	
	location.href="<%=cp%>/searchlist/searchum.do?usercode=um3";
	
}

function yang(){
	
	location.href="<%=cp%>/searchlist/searchum.do?usercode=um4";

	
}

function bun(){
	
	location.href="<%=cp%>/searchlist/searchum.do?usercode=um5";
	
}

function dessert(){
	
	location.href="<%=cp%>/searchlist/searchum.do?usercode=um6";

	
}




</script>

</head>
<body>
	<div class="container">
		<div class="header">
			<jsp:include page="/WEB-INF/views/layout/searchHeader.jsp"></jsp:include>
		</div>

		<div class="body">
		
			<div onclick="korea();">한식</div>
			<div onclick="china();">중식</div>
			<div onclick="japan();">일식</div>
			<div onclick="yang()">양식</div>
			<div onclick="bun()">분식</div>
			<div onclick="dessert()">디저트</div>
		</div>



		<div class="footer">
			<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
		</div>
	</div>

	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>