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
<title>Insert title here</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/commonLayout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/login.css" type="text/css">

<script type="text/javascript">


function sendLogin() {
    var f = document.loginF;

    
    var test = document.getElementById("empty");
	var str = f.id.value;
    if(!str) {
        alert(test);
        document.getElementById("empty").innerHTML("아이디를 입력하세요");
        f.id.focus();
        return;
    }

    str = f.password.value;
    if(!str) {
        alert("패스워드를 입력하세요. ");
        f.password.focus();
        return;
    }

    f.action = "<%=cp%>/member/pwd_ok.do?mode='update'";
    
    f.submit();
}

</script>

</head>
<body>
<div class="header">
    <jsp:include page="/WEB-INF/views/layout/commonHeader.jsp"></jsp:include>
</div>
	
<div class="body">
            <form name ="loginF" class="login" action="" method="POST">
                <h1>비밀번호 재확인</h1>
                <p>
                    <input class="loginForm" type="text" name="id" placeholder="아이디를 입력하세요.(필수)">
                </p>
                <p>
                    <input class="loginForm" type="password" name="password" placeholder="비밀번호를 입력하세요.(필수)">
                </p>
                <p>                
                    <button class= "loginForm" onclick="sendLogin();">로그인</button>
                </p>
                <div id=empty style = "color: red"> </div>
                
                <hr>
                <div class="loginPageNavi">
                <div class="haha">
					<div>개인정보 보호를 위하여 재 로그인 해주세요.</div>
                </div>
            </div>
            </form>
        </div>



<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
</body>
</html>