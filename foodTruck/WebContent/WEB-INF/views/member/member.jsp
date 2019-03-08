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
<link rel="stylesheet" href="<%=cp%>/resource/css/register.css"
	type="text/css">


<link rel="stylesheet"
	href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css"
	type="text/css">

<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>

<script type="text/javascript">
function memberOk() {
	var f = document.memberForm;
	var str;
	
	str = f.userId.value;
	str = str.trim();
	if(!str) {
		alert("아이디를 입력하세요. ");
		f.userId.focus();
		return;
	}
	if(!/^[a-z][a-z0-9_]{4,9}$/i.test(str)) { 
		alert("아이디는 5~10자이며 첫글자는 영문자이어야 합니다.");
		f.userId.focus();
		return;
	}
	f.userId.value = str;

	str = f.userPwd.value;
	str = str.trim();
	if(!str) {
		alert("패스워드를 입력하세요. ");
		f.userPwd.focus();
		return;
	}
	if(!/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i.test(str)) { 
		alert("패스워드는 5~10자이며 하나 이상의 숫자나 특수문자가 포함되어야 합니다.");
		f.userPwd.focus();
		return;
	}
	f.userPwd.value = str;

	if(str!= f.userPwdCheck.value) {
        alert("패스워드가 일치하지 않습니다. ");
        f.userPwdCheck.focus();
        return;
	}
    
    str = f.tel1.value;
	str = str.trim();
    if(!str) {
        alert("전화번호를 입력하세요. ");
        f.tel1.focus();
        return;
    }

    str = f.tel2.value;
	str = str.trim();
    if(!str) {
        alert("전화번호를 입력하세요. ");
        f.tel2.focus();
        return;
    }
    if(!/^(\d+)$/.test(str)) {
        alert("숫자만 가능합니다. ");
        f.tel2.focus();
        return;
    }

    str = f.tel3.value;
	str = str.trim();
    if(!str) {
        alert("전화번호를 입력하세요. ");
        f.tel3.focus();
        return;
    }
    if(!/^(\d+)$/.test(str)) {
        alert("숫자만 가능합니다. ");
        f.tel3.focus();
        return;
    }

    var mode="${mode}";
    if(mode=="created") {
    	f.action = "<%=cp%>/member/member_ok.do";

    	
    } else if(mode=="update") {
    	f.action = "<%=cp%>/member/update_ok.do";
		}

		f.submit();
	}

	function changeEmail() {
		var f = document.memberForm;

		var str = f.selectEmail.value;
		if (str != "direct") {
			f.email2.value = str;
			f.email2.readOnly = true;
			f.email1.focus();
		} else {
			f.email2.value = "";
			f.email2.readOnly = false;
			f.email1.focus();
		}
	}

	function userIdCheck() {
		// 아이디 중복 검사

	}
</script>
</head>
<body>

	<div class="container">

		<div class="header">
			<jsp:include page="/WEB-INF/views/layout/commonHeader.jsp"></jsp:include>
		</div>

		<div class="body">
		<c:if test="${mode=='created'}">
			<h3>회원가입</h3>
		</c:if>
		<c:if test="${mode=='update'}">
			<h3>회원정보수정</h3>
		</c:if>
			<hr>
			<p class="notice">
				<span class="star">★</span>는 필수입력입니다.
			</p>
			<form name="memberForm" method="post">
				<table class="memberTable">
					<tr>
						<th><span class="star">★</span> 아이디</th>
						<td>
							<p>
								<input type="text" name="userId" id="userId"
									value="${dto.userId}"
									${mode=="update" ? "readonly='readonly' ":""} placeholder="아이디">
							</p>
							<p class="help-block">아이디는 5~10자 이내이며, 첫글자는 영문자로 시작해야 합니다.</p>
						</td>
					</tr>
					<tr>
						<th><span class="star">★</span>${mode=="update"?"변경할":""} 비밀번호</th>
						<td>
							<p>
								<input type="password" name="userPwd" placeholder="비밀번호">
							</p>
							<p class="help-block">비밀번호는 5~10자 이내이며, 하나 이상의 숫자나 특수문자가
								포함되어야 합니다.</p>

						</td>
					</tr>
					<tr>
						<th><span class="star">★</span>${mode=="update"?"변경할":""} 비밀번호 확인</th>
						<td>
							<p>
								<input type="password" name="userPwdCheck" placeholder="패스워드 확인">

							</p>
							<p class="help-block">패스워드를 한번 더 입력해주세요.</p>
						</td>
					</tr>
					<tr>
						<th><span class="star">&nbsp;&nbsp;&nbsp;</span> 전화번호</th>
						<td>
							<p class="telInput">
								<select class="selectField" id="tel1" name="tel1">
									<option value="">선 택</option>
									<option value="010"
										${dto.tel1=="010" ? "selected='selected'" : ""}>010</option>
									<option value="011"
										${dto.tel1=="011" ? "selected='selected'" : ""}>011</option>
									<option value="016"
										${dto.tel1=="016" ? "selected='selected'" : ""}>016</option>
									<option value="017"
										${dto.tel1=="017" ? "selected='selected'" : ""}>017</option>
									<option value="018"
										${dto.tel1=="018" ? "selected='selected'" : ""}>018</option>
									<option value="019"
										${dto.tel1=="019" ? "selected='selected'" : ""}>019</option>
								</select> - <input type="text" name="tel2" value="${dto.tel2}"
									class="boxTF" maxlength="4"> - <input type="text"
									name="tel3" value="${dto.tel3}" class="boxTF" maxlength="4">
							</p>
						</td>
					</tr>
					<c:if test="${mode=='created'}">
						<tr>
							<th><span class="star">★</span> 사용자 유형</th>
							<td>
								<p class="userType">
									<label><input type="radio" name="usercode" value="user"
										checked="checked">손님</label> <label><input
										type="radio" name="usercode" value="manager">점주</label>
								</p>
							</td>
						</tr>
					</c:if>
				</table>

				<c:if test="${mode=='created'}">
					<div class="permit">
						<h4>이용약관</h4>
						<textarea class="permitText" readonly="readonly">천자만홍이 두기 하였으며, 무엇을 것은 대중을 생생하며, 것이다. 간에 청춘의 생명을 심장은 때문이다. 따뜻한 따뜻한 영락과 봄바람을 살았으며, 인생에 위하여서. 온갖 얼마나 때에, 구하기 것이다. 것이다.보라, 앞이 창공에 없으면 기쁘며, 방지하는 이상 피어나기 청춘이 힘있다. 광야에서 대중을 힘차게 가지에 것이다. 같이, 청춘의 돋고, 봄날의 가지에 작고 뿐이다. 굳세게 길지 대고, 거친 남는 풀이 동력은 목숨이 피다. 공자는 그들은 그들의 사막이다. 그들은 인생에 피가 하였으며, 듣는다.

                        인간이 같지 것은 긴지라 청춘이 얼마나 힘있다. 그들의 이상을 청춘을 스며들어 관현악이며, 봄바람이다. 싹이 대한 너의 투명하되 별과 것이다. 청춘에서만 소담스러운 가치를 용기가 황금시대다. 뼈 열매를 이 기쁘며, 군영과 위하여서. 과실이 속잎나고, 길을 뿐이다. 그들의 노년에게서 보배를 찾아 지혜는 장식하는 무엇을 그리하였는가? 능히 되려니와, 꽃이 크고 어디 구할 인생의 않는 것이다. 인생의 이상이 이상을 타오르고 피어나는 없는 아니더면, 하여도 밝은 약동하다.
                        
                        안고, 것은 위하여, 그와 피어나기 찾아다녀도, 바이며, 보배를 인간의 말이다. 앞이 그들에게 무엇을 보배를 것이다. 이성은 고동을 가치를 것은 뿐이다. 붙잡아 크고 별과 이것이야말로 노래하며 풀이 구하지 커다란 목숨이 힘있다. 피에 이것이야말로 피가 봄바람이다. 광야에서 품으며, 군영과 것이다. 이상 그림자는 없으면, 꾸며 굳세게 예수는 이것이다. 눈에 풀이 너의 바이며, 두기 이는 하는 생의 찾아다녀도, 때문이다. 뼈 동력은 이 그들은 같지 청춘에서만 바로 뜨고, 것이다. 속에서 위하여, 끝까지 피어나기 바이며, 뭇 사랑의 부패를 피다. 맺어, 날카로우나 생명을 얼음 얼마나 힘있다.
                </textarea>
						<p>
							<label><input id="agree" name="agree" type="checkbox"
								checked="checked" onchange="form.sendButton.disabled = !checked">
								이용약관에 동의하십니까?</label>
						</p>
					</div>
				</c:if>

				<div class="buttons">
					<button type="button" name="sendButton" class="btn"
						onclick="memberOk();">${mode=="created"?"회원가입":"정보수정"}</button>
					<button type="reset" class="btn">다시입력</button>
					<button type="button" class="btn"
						onclick="javascript:location.href='<%=cp%>/';">${mode=="created"?"가입취소":"수정취소"}</button>
				</div>
			</form>
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