<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String cp = request.getContextPath();
%>
 <div class="header">
            <div class="header-top">
                <h1 class="title"><a href="<%=cp%>/">지존트럭</a></h1>
                <ul class="loginNavi">
                <c:if test="${empty sessionScope.member}">
                    <li><a href="<%=cp%>/member/member.do">회원가입</a></li>
                    <li><span>|</span></li>
                    <li><a href="<%=cp%>/member/login.do">로그인</a></li>
                </c:if>
                <c:if test="${not empty sessionScope.member}">
                  <li><a href="<%=cp%>/member/logout.do">로그아웃</a></li>
                    <li><span>|</span></li>
                    <li><a href="<%=cp%>/member/pwd.do?mode=update">정보수정</a></li>
           
                </c:if>
                </ul>
            </div>
        </div>