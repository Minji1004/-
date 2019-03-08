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
<title>spring</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

<c:if test="${sessionScope.member.userNum==dto.userNum || sessionScope.member.userRoll=='admin'}">
	<script type="text/javascript">
	function deleteBoard(num) {
		if(confirm("게시물을 삭제 하시겠습니까 ?")) {
			var url="<%=cp%>/qAnda/delete.do?boardNum="+num+"&${query}";
			location.href=url;
		}
	}
	</script>
</c:if>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="qAnda">
    <div class="body-qAnda" style="width: 800px; margin: 0 auto;">
        <div class="body-title">
            <h3 style="text-align:center;margin: 20px auto 0px; font-size:30px; ">Q & A</h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" bgcolor="#29D9C2" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc; color:white; font-weight:bold; font-size:18px;">
			    <td colspan="2" align="center">
				   <c:if test="${dto.depth!=0 }"><img src="<%=cp%>/resource/images/newnew.gif" style="height:20px;"> </c:if>
				   ${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       작성자 : ${dto.id}
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created } | 조회 ${dto.hitCount}
			    </td>
			</tr>
			
			<tr style="border-bottom: 1px solid #cccccc;">
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       이전글 :
                  <c:if test="${not empty preReadDto}">
                         <a href="<%=cp%>/board/article.do?boardNum=${preReadDto.boardNum}&${query}">${preReadDto.subject}</a>
                  </c:if>
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       다음글 :
                  <c:if test="${not empty nextReadDto}">
                         <a href="<%=cp%>/board/article.do?boardNum=${nextReadDto.boardNum}&${query}">${nextReadDto.subject}</a>
                  </c:if>
			    </td>
			</tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="45">
			    <td width="300" align="left">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/board/reply.do?boardNum=${dto.boardNum}&page=${page}';">답변</button>
			          <c:if test="${sessionScope.member.userNum == dto.userNum}">
			              <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/board/update.do?boardNum=${dto.boardNum}&${query}';">수정</button>
			          </c:if>
			          <c:if test="${sessionScope.member.userNum != dto.userNum}">
			              <button type="button" class="btn" disabled="disabled">수정</button>
			          </c:if>
			          <c:if test="${sessionScope.member.userNum==dto.userNum || sessionScope.member.userRoll=='admin'}">
			              <button type="button" class="btn" onclick="deleteBoard('${dto.boardNum}');">삭제</button>
			          </c:if>
			          <c:if test="${sessionScope.member.userNum!=dto.userNum && sessionScope.member.userRoll!='admin'}">
			              <button type="button" class="btn" disabled="disabled">삭제</button>
			          </c:if>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/qAnda/list.do?${query}';">리스트</button>
			    </td>
			</tr>
			</table>
        </div>

    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>