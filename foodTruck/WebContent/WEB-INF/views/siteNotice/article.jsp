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
<title>article</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript">

<c:if test="${sessionScope.member.userRoll=='admin'}">
function deleteNotice(noticeNum) {
	if(confirm("진짜루 삭제할거야?")) {
		var url="<%=cp%>/siteNotice/delete.do?noticeNum="+noticeNum;
		url+="&page=${page}";
		location.href=url;
	}
}
</c:if>
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
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center" bgcolor="#29D9C2" style="color:white; font-weight:bold; font-size:18px;">
				   ${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       WRITER : admin
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created} | HITS ${dto.hitCount}
			    </td>
			</tr>
			
			<tr style="border-bottom: 1px solid #cccccc;">
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       첨&nbsp;&nbsp;부 :
		           <c:if test="${not empty dto.saveFilename}">
		                   <a href="<%=cp%>/siteNotice/download.do?noticeNum=${dto.noticeNum}">${dto.originalFilename}</a>
		                    (<fmt:formatNumber value="${dto.fileSize/1024}" pattern="0.00"/> Kbyte)
		           </c:if>
			    </td>
			</tr>

			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       이전글 :
			         <c:if test="${not empty preReadDto}">
			              <a href="<%=cp%>/siteNotice/article.do?${query}&noticeNum=${preReadDto.noticeNum}">${preReadDto.subject}</a>
			        </c:if>
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			    다음글 :
			         <c:if test="${not empty nextReadDto}">
			              <a href="<%=cp%>/siteNotice/article.do?${query}&noticeNum=${nextReadDto.noticeNum}">${nextReadDto.subject}</a>
			        </c:if>
			    </td>
			</tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
			<tr height="45">
			    <td width="300" align="left">
			    	  <c:if test="${sessionScope.member.userRoll=='admin'}">
			          <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/siteNotice/update.do?noticeNum=${dto.noticeNum}&page=${page}';">수정</button>
			          </c:if>
			          <c:if test="${sessionScope.member.userRoll=='admin'}">
			          <button type="button" class="btn" onclick="deleteNotice('${dto.noticeNum}');">삭제</button>
			    	  </c:if>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/siteNotice/list.do?${query}';">리스트</button>
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