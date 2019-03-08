<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>created</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
function sendNotice() {
    var f = document.noticeForm;

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

	var mode="${mode}";
	if(mode=="created")
		f.action="<%=cp%>/siteNotice/created_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/siteNotice/update_ok.do";

    f.submit();
}
    
<c:if test="${mode=='update'}">
    function deleteFile(noticeNum) {
  	  var url="<%=cp%>/siteNotice/deleteFile.do?noticeNum="+noticeNum+"&page=${page}";
  	  location.href=url;
    }
</c:if>
   
</script>
</head>
<body>

<div class="top">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="notice">
    <div class="body-notice" style="width: 800px; margin: 0 auto;">
        <div class="body-title">
            <h3 style="text-align:center;margin: 20px auto 0px; font-size:30px; ">공지사항</h3>
        </div>
        
        <div>
			<form name="noticeForm" method="post" enctype="multipart/form-data">
			
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
  			  	<tr align="left" height="40" style="border-bottom: 1px solid Gainsboro; border-top:1px solid Gainsboro;"> 
			      <td width="100" bgcolor="#29D9C2" style="text-align: center; color:white;">작성자</td>
			     	<td style="padding-left:10px;"> 
			            admin
			      </td>
			  </tr>
<%-- 			        <input type="text" name="name" maxlength="100" class="boxTF" style="width: 20%;" value="${dto.name}"> --%>
<!-- 			      </td> -->
<!-- 			  </tr> -->
			  
			  <tr align="left" height="40" style="border-top: 1px solid white; border-bottom: 1px solid Gainsboro;"> 
			      <td width="100" bgcolor="#29D9C2" style="text-align: center; color:white;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.subject}">
			      </td>
			  </tr>

			  <tr align="left" height="40" style="border-bottom: 1px solid Gainsboro;"> 
			      <td width="100" bgcolor="#29D9C2" style="text-align: center;color:white;">공지여부</td>
			      <td style="padding-left:10px;"> 
			        <input type="checkbox" name="notice" value="1" ${dto.notice==1 ? "checked='checked' ":"" } > 중요공지
			      </td>
			  </tr>
			

			
			  <tr align="left" style="border-bottom: 1px solid Gainsboro;"> 
			      <td width="100" bgcolor="#29D9C2" style="color:white; text-align: center; padding-top:5px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			        <textarea name="content" rows="12" class="boxTA" style="width: 95%;">${dto.content}</textarea>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style=" border-bottom:1px solid Gainsboro;">
			      <td width="100" bgcolor="#29D9C2" style="text-align: center; color:white;">첨&nbsp;&nbsp;&nbsp;&nbsp;부</td>
			      <td style="padding-left:10px;"> 
			           <input type="file" name="upload" class="boxTF" size="53" style="height: 25px;">
			       </td>
			  </tr> 

			  <c:if test="${mode=='update'}">
				  <tr align="left" height="40" style="border-bottom: 1px solid white;">
				      <td width="100" bgcolor="#29D9C2" style="text-align: center; color:white">첨부된파일</td>
				      <td style="padding-left:10px;"> 
				         <c:if test="${not empty dto.saveFilename}">
				             ${dto.originalFilename}
				             | <a href="javascript:deleteFile('${dto.noticeNum}');">삭제</a>
				         </c:if>     
				       </td>
				  </tr> 
			  </c:if>
			  
			  </table>
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn" onclick="sendNotice();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/siteNotice/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
			         <c:if test="${mode=='update'}">
			         	 <input type="hidden" name="noticeNum" value="${dto.noticeNum}">
			        	 <input type="hidden" name="page" value="${page}">
			        	 <input type="hidden" name="fileSize" value="${dto.fileSize}">
			        	 <input type="hidden" name="saveFilename" value="${dto.saveFilename}">
			        	 <input type="hidden" name="originalFilename" value="${dto.originalFilename}">
			        </c:if>
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

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>