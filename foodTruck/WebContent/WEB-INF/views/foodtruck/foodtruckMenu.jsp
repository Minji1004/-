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
<title>Insert title here</title>
<link rel="stylesheet" href="<%=cp%>/resource/css/commonLayout.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/truckCommonLayout.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/truckmenu.css"
	type="text/css">

<link rel="stylesheet"
	href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css"
	type="text/css">
	
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
	
<script type="text/javascript">
</script>
	
</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/foodtruck/header.jsp"></jsp:include>
	</div>
	<div class="body">
		<div class="truck">
			<div class="truckBasic">
				<h3>메뉴관리</h3>
				<hr>

				<form name="scoreForm" method="post">
					<table>
						<thead>
							<tr>
								<td>사진</td>
								<td>메뉴명</td>
								<td>설명</td>
								<td>가격</td>
							</tr>
						</thead>

						<tbody id="tbScore">
							<tr id="inputForm">
								<td><img class="img" src="<%=cp%>/resource/images/food1.jpg"></td>
								<td>떡볶이</td>
								<td>맛있습니다.</td>
								<td>5000</td>
							</tr>
						</tbody>

					</table>
					<button type="button" class="btnUpdate">수정하기</button>
				</form>
			</div>

		</div>
	</div>


	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
</body>
</html>