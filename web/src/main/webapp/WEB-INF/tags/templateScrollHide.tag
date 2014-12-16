<!DOCTYPE html>
<%@include file="/WEB-INF/views/includes/init.jsp" %>
<jsp:directive.attribute name="head" required="false" fragment="true"/>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8">
		<title>${pageTitle}</title>
		<jsp:include page="/WEB-INF/views/includes/brains.jsp"/>
		<jsp:invoke fragment="head"/>
		<script>
			window.onscroll=function(evt){
			 
				var y = window.pageYOffset;
				if(y == undefined)
					return;
				if(y > 0){
					var items = $(".slow-hide");
					//var showItems = $("#slow-nonhide");
		
					items.hide("slow"); 
				 
					//$("#tableHeader").attr("style", "position: fixed; top: 0px;");
				}else{
					$(".slow-hide").show("slow"); 
				}

			}
			
			
		</script>

 <style>
 .wrapper {
  position: relative;
  padding: 0 5px;
  height: 250px;
  overflow-y: auto;
}</style>

	</head>
	<body  class="env-production">
		<div class="container" id="wrapper">
		<div class="slow-hide">
		<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
		</div>
		<div class="padding-md overflow">
			<jsp:doBody />
		</div>
		</div>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
	</body>
</html>