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

<script type="text/javascript">
    function sendOk() {
        var f = document.qAndaForm;

    	var str = f.subject.value;
        if(!str) {
            alert("제목을 입력하세요. ");
            f.subject.focus();
            return;
        }

    	str = f.content.value;
        if(!str) {
            alert("내용을 입력하세요. ");
            f.content.focus();
            return;
        }

    	f.action="<%=cp%>/qAnda/${mode}_ok.do";

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
            <h3 style="text-align:center;margin: 20px auto 0px; font-size:30px; ">Q & A</h3>
        </div>
        
        <div>
			<form name="qAndaForm" method="post">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="left" height="50" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#29D9C2" style="text-align: center; color:white;">TITLE</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.subject}">
			      </td>
			  </tr>
			
			  <tr align="left" height="50" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#29D9C2" style="text-align: center; color:white;">WRITER</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.userId}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#29D9C2" style="text-align: center; padding-top:5px; color:white;" valign="top">CONTENTS</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			        <textarea name="content" rows="12" class="boxTA" style="width: 95%;">${dto.content}</textarea>
			      </td>
			  </tr>
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			         <c:if test="${mode=='update'}">
			         	 <input type="hidden" name="boardNum" value="${dto.boardNum}">
			        	 <input type="hidden" name="page" value="${page}">
			        	 <input type="hidden" name="searchKey" value="${searchKey}">
			        	 <input type="hidden" name="searchValue" value="${searchValue}">
			        </c:if>			      
			      	<c:if test="${mode=='reply'}">
			      	     <input type="hidden" name="groupNum" value="${dto.groupNum}">
			      	     <input type="hidden" name="orderNo" value="${dto.orderNo}">
			      	     <input type="hidden" name="depth" value="${dto.depth}">
			      	     <input type="hidden" name="parent" value="${dto.boardNum}">
			      	     <input type="hidden" name="page" value="${page}">
			      	</c:if>
			        <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':(mode=='reply'? '답변완료':'OK')}</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/qAnda/list.do';">${mode=='update'?'수정취소':(mode=='reply'? '답변취소':'CANCEL')}</button>
			      </td>
			    </tr>
			  </table>
			</form>
        </div>

    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</html>