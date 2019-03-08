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
<link rel="stylesheet" href="<%=cp%>/resource/css/truckmenuCreated.css"
	type="text/css">

<link rel="stylesheet"
	href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css"
	type="text/css">
	
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
	
<script type="text/javascript">
function isNumber(data){
	var format=/^(\d+)$/;
	return format.test(data);
}

//엔터 처리
$(function(){
	   $("#inputForm input").keypress(function (evt) {
	        var fields = $("#inputForm input:text");
	        var index = fields.index(this);
	        
	        if (evt.keyCode == 13) {
	            if ( index > -1 && index < 3 ) {
	                fields.eq( index + 1 ).focus();
	            } else {
	            	$("#btnAdd").trigger("click");
	            }
	            return false;
	        }
	     });
});

//이미지 파일 등록
$(function(){
	$("body").on("change", "#inputForm input[type='file']", function(e){   
		var $this=$(this);
		var photo =  e.target.files[0];
		// 이미지 파일이 아닌경우
		if(! photo.type.match("image.*")) {
			alert("이미지 파일을 입력하세요. !!!");
			return;
		}
		
		var reader = new FileReader();
		reader.onload = function(e1) {
			$this.closest("div").children("img").attr("src", e1.target.result);			
		}
		reader.readAsDataURL(photo); 
	});
});


//등록하기
$(function(){
	$("#btnAdd").click(function(e){
		var photo = $('#photo').prop('files')[0];
		var name=$("#name").val().trim();
		var info=$("#info").val().trim();
		var price=$("#price").val().trim();

		if(!name || !info || !price) {
			alert("값을 입력 하세요 !!!");
			return;
		}

		if(!isNumber(price)) {
			alert("가격은 숫자만 가능합니다. !!!");
			return;
		}	      
		
		var str="<tr class='rows'></tr>";
		$(str).append("<td><div class='imgUpload'><img></div></td>")
		.append("<td>"+name+"</td>")
	       .append("<td>"+info+"</td>")
	       .append("<td>"+price+"</td>")
		.append("<td><span class='spanUpdate' style='cursor:pointer;'>수정</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class='spanDelete' style='cursor:pointer;'>삭제</span></td>")
		.appendTo("#tbScore");
		
		
		var reader = new FileReader();
		reader.onload = function(e1) {
			$("tr:last-child img").attr("src", e1.target.result);
		}
		reader.readAsDataURL(photo); //파일 객체를 넣는다. 

		$("#photo").val("");
		$("#name").val("");
		$("#info").val("");
		$("#price").val("");
		$("#name").focus();
	}); 
});

//수정
$(function(){
	var arr=[];
	$("body").on("click", ".spanUpdate", function(){
		var tds = $(this).closest("tr").children("td");		
		var size, s;
		
		$(tds).each(function(i){
			if(i==0){		
				arr[i]=$(this).children('img').attr('src');
				$(this).empty();
				$(this).append("<input type='file' value='"+arr[i]+"' class='boxTF'>");
			}else if(i!=tds.length-1) {
				arr[i]=$(this).text();
				$(this).empty();
				$(this).append("<input type='text' value='"+arr[i]+"' class='boxTF'>");
			} else {
				$(this).empty();
				$(this).append("<span class='spanUpdateOk' style='cursor:pointer;'>완료</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class='spanUpdateCancel' style='cursor:pointer;'>취소</span>");
			}
		});
		
		$(tds[1]).find("input").focus();
	});
	
	$("body").on("click", ".spanUpdateOk", function(){
		var price = $(this).closest("tr").children("td:nth-child(4)").children().val().trim();
		
		if(!isNumber(price)) {
			alert("가격은 숫자만 가능합니다. !!!");
			return;
		}	
		
		
		var tds = $(this).closest("tr").children("td");
		var photo = $(this).closest("tr").find("input[type='file']").prop('files')[0];
		
		$(tds).each(function(i){
			var ob=$(this);
			
			if(i==0){
				$(this).empty();
				$(this).append("<td><img class='img'></td>");					
				
			}else if(i!=tds.length-1) {
				$(this).find("input:text").each(function(i) {
					ob.empty();
			        ob.text($(this).val());
			    });
			}
			else {
				$(this).empty();
				$(this).append("<span class='spanUpdate' style='cursor:pointer;'>수정</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class='spanDelete' style='cursor:pointer;'>삭제</span>");
			}
		});
		
		var reader = new FileReader();
		reader.onload = function(e1) {
			$(this).closest("tr").children("td:first-child").children().attr("src", e1.target.result);
		}
		reader.readAsDataURL(photo); //파일 객체를 넣는다. 
	
	});
	
	/* $("body").on("click", ".spanUpdateCancel", function(){
		var tds = $(this).closest("tr").children("td");
		$(tds).each(function(i){
			if(i!=tds.length-1) {
				$(this).empty();
				$(this).text(arr[i]);
			} else {
				$(this).empty();
				$(this).append("<span class='spanUpdate' style='cursor:pointer;'>수정</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class='spanDelete' style='cursor:pointer;'>삭제</span>");
			}
		});
	});  */
	
});
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
								<th>사진</th>
								<th>메뉴명</th>
								<th>설명</th>
								<th>가격</th>
								<th>&nbsp;</th>
							</tr>
						</thead>

						<tbody id="tbScore">
							<tr id="inputForm">
								<td>
								<div class="imgUpload">
								<input type="file" name="" id="photo" class="boxTF"
		      		                       accept="image/*">
		      		             <img class="img" src="<%=cp%>/resource/images/image_add.png">		      		             
		      		             <span class="mask"></span>		
		      		             </div>      		             
		      		     </td>
								<td><input type="text" name="" id="name" class="boxTF"
									size="8"></td>
								<td><input type="text" name="" id="info" class="boxTF"
									size="8"></td>
								<td><input type="text" name="" id="price" class="boxTF"
									size="6"></td>
								<td>
									<button type="button" class="btn" id="btnAdd">등록하기</button>
								</td>
							</tr>
						</tbody>

					</table>
					<button type="button" class="btnUpdate">수정완료</button>
				</form>
			</div>

		</div>
	</div>


	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
</body>
</html>