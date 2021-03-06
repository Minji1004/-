<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>notice</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript">
function searchList() {
	var f=document.searchForm;
	f.submit();
}
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="notice">
    <div class="body-notice" style="width: 800px; margin: 0 auto;">
        <div class="body-title">
            <h3 style="text-align:center;margin: 20px auto 0px; font-size:30px; ">공지사항</h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px 0px 0px 15px; border-spacing: 0px;">
			   <tr height="35">
			      <td align="left" width="50%">
			          	<span style="color:red;">★★★</span>은 중요 공지사항입니다.
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="center" bgcolor="#29D9C2" height="35" style="border-top: 1px solid Gainsboro; border-bottom: 1px solid Gainsboro;"> 
			      <th width="80" style="color: white;">번호</th>
			      <th style="color: white;">제목</th>
			      <th width="100" style="color: white;">작성자</th>
			      <th width="80" style="color: white;">작성일</th>
			      <th width="60" style="color: white;">조회수</th>
			  </tr>

			 <c:forEach var="dto" items="${listNotice}">
			  <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid Gainsboro;"> 
			      <td>
			           <span style="display: inline-block;width: 40px;height:18px;line-height:18px;color: red; ">★★★</span>
			      </td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&noticeNum=${dto.noticeNum}">${dto.subject}</a>
			      </td>
			      <td>admin</td>
			      <td>${dto.created}</td>
			      <td>${dto.hitCount}</td>
			  </tr>
			</c:forEach> 

			 <c:forEach var="dto" items="${list}">
			  <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid Gainsboro;"> 
			      <td>${dto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&noticeNum=${dto.noticeNum}">${dto.subject}</a>
			           <c:if test="${dto.gap<=1}"><img src="<%=cp%>/resource/images/newnew.gif" style="height:20px;"></c:if>
			      </td>
			      <td>admin</td>
			      <td>${dto.created}</td>
			      <td>${dto.hitCount}</td>
			  </tr>
			</c:forEach> 
			</table>
			 
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
			        <c:if test="${dataCount==0 }">
			                등록된 게시물이 없습니다.
			         </c:if>
			        <c:if test="${dataCount!=0 }">
			               ${paging}
			         </c:if>
				</td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/siteNotice/list.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="<%=cp%>/siteNotice/list.do" method="post">
			              <select name="searchKey" class="selectField">
			                  <option value="subject">제목</option>
			                  <option value="content">내용</option>
			            </select>
			            <input type="text" name="searchValue" class="boxTF">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
			      <td align="right" width="100">
			          <c:if test="${sessionScope.member.userRoll=='admin'}"> <!-- admin == 처리 -->
			              <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/siteNotice/created.do';">글올리기</button>
			          </c:if>
			      </td>
			   </tr>
			</table>
        </div>
        
    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>
</html>