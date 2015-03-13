<%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>
 

<!-- adding this version of jquery for calendar -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
<script src="${pageContext['request'].contextPath}/scripts/jquery-sparkline212.js" type="text/javascript"></script>

<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="${pageContext['request'].contextPath}/scripts/excanvas.js"></script><![endif]-->
 
 
<script language="javascript" type="text/javascript" src="${pageContext['request'].contextPath}/scripts/chart.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext['request'].contextPath}/scripts/chart.js.legend"></script>
 
 
<style>
	.mainRowHeader{background-color: #87B940;
		border: 2px solid white;
		height: 20px;
		padding-top: 5px;
		width: 800px;
	}
	.secondaryRowHeader{background-color: #DAEBBB;
		border: 2px solid white;
		height: 20px;
		padding-top: 2px;
		vertical-align: middle;
	}
	.mainRightRowHeader{background-color: #87B940;
		border: 2px solid white;
		height: 30px;
		padding-top: 5px;
		width: 390px;
	}
	.secondaryRightRowHeader{background-color: #DAEBBB;
		border: 2px solid white;
		height: 20px;
		padding-top: 2px;
	}
	.secondaryTd{  border: 1px solid white; margin-left:auto; margin-right:auto; vertical-align: middle;}
	.bodyTd{	border: 1px solid white; margin-left:auto; margin-right:auto;  padding-left: 3px;}
	.arrowTd{	border: 1px solid white; margin-left:auto; margin-right:auto;  padding-left: 7px; }
	.arrowTdHeader{ width:30px;	border: 1px solid white; margin-left:auto; margin-right:auto; padding-right: 3px; vertical-align: middle;}
	.rArrow{color: red; border: 2px; padding-left: 3px; padding-top: 5px;  font-weight: bold; font-size: 20px; vertical-align: middle;}
	.gArrow{color: green; border: 2px; padding-left: 3px; padding-top: 5px;  font-weight: bold; font-size: 20px; vertical-align: middle;}
	.gDash{color: green; border: 2px; padding-left: 3px; padding-top: 5px;  font-weight: light; font-size: 20px; vertical-align: middle;}
	td{height: 24px;}
</style>
<script> 
  
$(function() {
	///////////////////////sparklines
 
	//production
	var fhcA =  <c:out value="${a2}"/>;
    $('.fhcSparkClassA').sparkline(fhcA, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcB  =  <c:out value="${b2}"/>;
    $('.fhcSparkClassB').sparkline(fhcB, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcC  =  <c:out value="${c2}"/>;
    $('.fhcSparkClassC').sparkline(fhcC, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcD  =  <c:out value="${d2}"/>;
    $('.fhcSparkClassD').sparkline(fhcD, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcE  =  <c:out value="${e2}"/>;
    $('.fhcSparkClassE').sparkline(fhcE, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcF  =  <c:out value="${f2}"/>;
    $('.fhcSparkClassF').sparkline(fhcF, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcG  =  <c:out value="${g2}"/>;
    $('.fhcSparkClassG').sparkline(fhcG, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcH  =  <c:out value="${h2}"/>;
    $('.fhcSparkClassH').sparkline(fhcH, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcI  =  <c:out value="${i2}"/>;
    $('.fhcSparkClassI').sparkline(fhcI, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcJ  =  <c:out value="${j2}"/>;
    $('.fhcSparkClassJ').sparkline(fhcJ, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcK  =  <c:out value="${k2}"/>;
    $('.fhcSparkClassK').sparkline(fhcK, {type: 'line', width: '100px', fillColor: undefined} ); 
    var fhcL  =  <c:out value="${l2}"/>;
    $('.fhcSparkClassL').sparkline(fhcL, {type: 'line', width: '100px', fillColor: undefined} ); 
    
    //quality
    var qa2  =  <c:out value="${quala2}"/>;
    $('.fhcSparkClassQualA').sparkline(qa2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var qb2  =  <c:out value="${qualb2}"/>;
    $('.fhcSparkClassQualB').sparkline(qb2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var qc2  =  <c:out value="${qualc2}"/>;
    $('.fhcSparkClassQualC').sparkline(qc2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var qd2  =  <c:out value="${quald2}"/>;
    $('.fhcSparkClassQualD').sparkline(qd2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var qe2  =  <c:out value="${quale2}"/>;
    $('.fhcSparkClassQualE').sparkline(qe2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var qf2  =  <c:out value="${qualf2}"/>;
    $('.fhcSparkClassQualF').sparkline(qf2, {type: 'line', width: '100px', fillColor: undefined} ); 



    //Image Auditor Averages
    var aa2  =  <c:out value="${audita2}"/>;
    $('.fhcSparkClassAuditA').sparkline(aa2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var ab2  =  <c:out value="${auditb2}"/>;
    $('.fhcSparkClassAuditB').sparkline(ab2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var ac2  =  <c:out value="${auditc2}"/>;
    $('.fhcSparkClassAuditC').sparkline(ac2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var ad2  =  <c:out value="${auditd2}"/>;
    $('.fhcSparkClassAuditD').sparkline(ad2, {type: 'line', width: '100px', fillColor: undefined} ); 
    


    //Top 5 issues
    var ta2  =  <c:out value="${topa2}"/>;
    $('.fhcSparkClassTopA').sparkline(ta2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var tb2  =  <c:out value="${topb2}"/>;
    $('.fhcSparkClassTopB').sparkline(tb2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var tc2  =  <c:out value="${topc2}"/>;
    $('.fhcSparkClassTopC').sparkline(tc2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var td2  =  <c:out value="${topd2}"/>;
    $('.fhcSparkClassTopD').sparkline(td2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var te2  =  <c:out value="${tope2}"/>;
    $('.fhcSparkClassTopE').sparkline(te2, {type: 'line', width: '100px', fillColor: undefined} ); 

    
    //Aged metrics
    var aa2  =  <c:out value="${ageda2}"/>;
    $('.fhcSparkClassAgedA').sparkline(aa2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var ab2  =  <c:out value="${agedb2}"/>;
    $('.fhcSparkClassAgedB').sparkline(ab2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var ac2  =  <c:out value="${agedc2}"/>;
    $('.fhcSparkClassAgedC').sparkline(ac2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var ad2  =  <c:out value="${agedd2}"/>;
    $('.fhcSparkClassAgedD').sparkline(ad2, {type: 'line', width: '100px', fillColor: undefined} ); 
    var ae2  =  <c:out value="${agede2}"/>;
    $('.fhcSparkClassAgedE').sparkline(ae2, {type: 'line', width: '100px', fillColor: undefined} ); 
   
    
    /////////////////////Chart.js
 	//large graph  
 	 
		var lineChartData = {
			labels : <c:out value="${goalsLabels}"  escapeXml="false" />,
			datasets : [ {
				fillColor : "lightblue",
				strokeColor : "lightblue",
				pointColor : "lightblue",
				pointStrokeColor : "#fff",
				data : <c:out value="${goals}"/>,
			 
			}, {
				fillColor : "blue",
				strokeColor : "blue",
				pointColor : "blue",
				pointStrokeColor : "#fff",
				data : <c:out value="${actuals}"/>,
			 
			} ]

		}
		var options = {
				
				//Boolean - If we show the scale above the chart data			
				scaleOverlay : false,
				
				//Boolean - If we want to override with a hard coded scale
				scaleOverride : false,
				
				//** Required if scaleOverride is true **
				//Number - The number of steps in a hard coded scale
				scaleSteps : null,
				//Number - The value jump in the hard coded scale
				scaleStepWidth : null,
				//Number - The scale starting value
				scaleStartValue : null,

				//String - Colour of the scale line	
				scaleLineColor : "rgba(0,0,0,.1)",
				
				//Number - Pixel width of the scale line	
				scaleLineWidth : 1,

				//Boolean - Whether to show labels on the scale	
				scaleShowLabels : true,
				
				//Interpolated JS string - can access value
			 
				
				//String - Scale label font declaration for the scale label
				scaleFontFamily : "'Arial'",
				
				//Number - Scale label font size in pixels	
				scaleFontSize : 12,
				
				//String - Scale label font weight style	
				scaleFontStyle : "normal",
				
				//String - Scale label font colour	
				scaleFontColor : "#666",	
				
				///Boolean - Whether grid lines are shown across the chart
				scaleShowGridLines : true,
				
				//String - Colour of the grid lines
				scaleGridLineColor : "rgba(0,0,0,.05)",
				
				//Number - Width of the grid lines
				scaleGridLineWidth : 1,	
				
				//Boolean - Whether the line is curved between points
				bezierCurve : false,
				
				//Boolean - Whether to show a dot for each point
				pointDot : true,
				
				//Number - Radius of each point dot in pixels
				pointDotRadius : 3,
				
				//Number - Pixel width of point dot stroke
				pointDotStrokeWidth : 1,
				
				//Boolean - Whether to show a stroke for datasets
				datasetStroke : true,
				
				//Number - Pixel width of dataset stroke
				datasetStrokeWidth : 2,
				
				//Boolean - Whether to fill the dataset with a colour
				datasetFill : false,
				
				//Boolean - Whether to animate the chart
				animation : true,

				//Number - Number of animation steps
				animationSteps : 60,
				
				//String - Animation easing effect
				animationEasing : "easeOutQuart",

				//Function - Fires when the animation is complete
				onAnimationComplete : null
				
			};
		var myLine = new Chart(document.getElementById("canvas").getContext("2d")).Line(lineChartData, options);

		//draw legends by hand
		var c = document.getElementById("canvasLegend");
		var ctx = c.getContext("2d");
		ctx.fillStyle="lightblue";
		ctx.fillRect(150,10,70,2);
		ctx.font = "16px Arial";
		ctx.fillText("Goal",150,30);
		c = document.getElementById("canvasLegend2");
		var ctx = c.getContext("2d");
		ctx.fillStyle="blue";
		ctx.fillRect(20,10,70,2);
		ctx.font = "16px Arial";
		ctx.fillText("Actual",20,30);
		
		
		$("#start_date").datepicker();
		$("#end_date").datepicker();

	});
</script>

<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     <form id="f1"name="f1" method="post" action="" >
	  	<%@include file="/WEB-INF/views/includes/dashboardMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
		<table>
		<tr> 
	   		<th>${messages['startDate']}</th>
			<th><input id="start_date" name="startDate" value="${startDate}"/>
				<button  class="dtUp" onclick="js:currentTimestamp('start_date'); return false;">&larr;&nbsp; ${messages['now']}</button>
			</th>
			<th>&nbsp;&nbsp; ${messages['endDate']}</th>
			<th><input id="end_date" name="endDate"  value="${endDate}"/>
				<button  class="dtUp" onclick="js:currentTimestamp('end_date'); return false;">&larr;&nbsp; ${messages['now']}</button>
			</th>
			<th>&nbsp;&nbsp;
		 		<select style="margin-bottom: 0px;" id="siteDropdown" name="site" >
				 
				<!-- <c:if test="${'All Sites'==site}"><option selected>${"All Sites"}</option> </c:if> -->
				<c:forEach var="i" items="${allLocations}">
    				<c:if test="${i==site}"><option selected>${i}</option> </c:if>
					<c:if test="${i!=site}"><option>${i}</option> </c:if>
				</c:forEach>
				</select>
			</th>
			<th>
			 <button value="load" name="load" id="load">Refresh</button>
			</th>
		</tr>
		</table>
		
		
		<table> <tr><td style="padding-bottom: 340px;">
			
			<table class="mainRowHeader">
		 		<tr>
		 			<td>
		 				<h4 style="text-align:center; margin-bottom: 0px;">Production</h4>
   		          		<h5  style="text-align:center; margin-top: 0px;">Family History Centers &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		 										Partner Institutions</h5>
		 			</td>
              	</tr>
			</table>
			<table>
			<tr class="secondaryRowHeader">
				<td></td>
				<td align="center" class="secondaryTd">Actual</td>
				<td align="center" class="secondaryTd">Trend</td>
				<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
				<td></td>
				<td align="center" class="secondaryTd">Actual</td>
				<td align="center" class="secondaryTd">Trend</td>
				<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595;  &nbsp;</span></td>
			</tr>
			
			<tr>
			<td class="bodyTd"  width='177'>${a0}</td>
			<td class="bodyTd"  width='80'>${a1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassA"></span> </td>
			<td class="arrowTd" ><c:if test="${a3=='up'}"><span class="gArrow">&#8593;</span></c:if>
				<c:if test="${a3=='down'}"><span class="rArrow">&#8595;</span></c:if>
				<c:if test="${a3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			 
			<td class="bodyTd"  width='177'>${g0}</td>
			<td class="bodyTd"  width='80'>${g1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassG"></span> </td>
			<td class="arrowTd" ><c:if test="${g3=='up'}"><span class="gArrow">&#8593;</span></c:if>
				<c:if test="${g3=='down'}"><span class="rArrow">&#8595;</span></c:if>
				<c:if test="${g3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			
			
			<tr>
			<td class="bodyTd" >${b0}</td>
			<td class="bodyTd" >${b1} </td>
			<td class="bodyTd"  align="center"><span class="fhcSparkClassB"></span> </td>
			<td class="arrowTd"  ><c:if test="${b3=='up'}"><span class="gArrow">&#8593;</span></c:if>
				<c:if test="${b3=='down'}"><span class="rArrow">&#8595;</span></c:if>
				<c:if test="${b3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			 
			<td class="bodyTd" >${h0}</td>
			<td class="bodyTd" >${h1} </td>
			<td  class="bodyTd" align="center"> <span class="fhcSparkClassH"></span> </td>
			<td class="arrowTd"  ><c:if test="${h3=='up'}"><span class="gArrow">&#8593;</span></c:if>
				<c:if test="${h3=='down'}"><span class="rArrow">&#8595;</span></c:if>
				<c:if test="${h3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			
			<tr>
			<td class="bodyTd" >${c0} </td>
			<td class="bodyTd" >${c1} </td>
			<td class="bodyTd"  align="center"><span class="fhcSparkClassC"></span></td>
			<td class="arrowTd" ><c:if test="${c3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${c3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${c3=='equal'}"><span class="gDash">&#8669;</span></c:if>
				</td>
			 
			<td class="bodyTd" >${i0} </td>
			<td class="bodyTd" >${i1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassI"></span></td>
			<td class="arrowTd" ><c:if test="${i3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${i3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${i3=='equal'}"><span class="gDash">&#8669;</span></c:if>
				</td>
			</tr>
			
			
			<tr>
			<td class="bodyTd" >${d0}</td>
			<td class="bodyTd" >${d1} </td>
			<td  class="bodyTd" align="center"> <span class="fhcSparkClassD"></span> </td>
			<td class="arrowTd" ><c:if test="${d3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${d3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${d3=='equal'}"><span class="gDash">&#8669;</span></c:if>
				</td>
			 
			<td class="bodyTd" >${j0}</td>
			<td class="bodyTd" >${j1} </td>
			<td  class="bodyTd" align="center"> <span class="fhcSparkClassJ"></span> </td>
			<td class="arrowTd" ><c:if test="${j3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${j3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${j3=='equal'}"><span class="gDash">&#8669;</span></c:if>
				</td>
			</tr>
			
			
			<tr>
			<td class="bodyTd" >${e0}</td>
			<td class="bodyTd" >${e1} </td>
			<td  class="bodyTd" align="center"> <span class="fhcSparkClassE"></span> </td>
			<td class="arrowTd" ><c:if test="${e3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${e3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${e3=='equal'}"><span class="gDash">&#8669;</span></c:if>
				</td>
			 
			<td class="bodyTd" >${k0}</td>
			<td class="bodyTd" >${k1} </td>
			<td class="bodyTd"  align="center"> <span class="fhcSparkClassK"></span> </td>
			<td class="arrowTd" ><c:if test="${k3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${k3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${k3=='equal'}"><span class="gDash">&#8669;</span></c:if>
				</td>
			</tr>
			
			<tr>
			<td class="bodyTd" >${f0}</td>
			<td class="bodyTd" >${f1} </td>
			<td  class="bodyTd" align="center"> <span class="fhcSparkClassF"></span> </td>
			<td class="arrowTd" ><c:if test="${f3=='up'}"><span class="gArrow">&#8593;</span></c:if>
				<c:if test="${f3=='down'}"><span class="rArrow">&#8595;</span></c:if>
				<c:if test="${f3=='equal'}"><span class="gDash">&#8669;</span></c:if>
				</td>
				
				
						
		 
			<td class="bodyTd" >${l0}</td>
			<td class="bodyTd" >${l1} </td>
			<td  class="bodyTd" align="center"> <span class="fhcSparkClassL"></span> </td>
			<td class="arrowTd" ><c:if test="${l3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${l3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${l3=='equal'}"><span class="gDash">&#8669;</span></c:if>
				</td>
			</tr>
			
		 
			     
			
			</table>
			
			<!-- Quality -->
			<table  class="mainRowHeader" >
			<tr>
		 		<td>
		 				<h4 style="text-align:center; margin-bottom: 0px;">Quality</h4>
   		          		<h5  style="text-align:center; margin-top: 0px;">Family History Centers &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		 										Partner Institutions</h5>
		 			</td>
              	</tr>
			 
		 	</table>
			<table>
			<tr class="secondaryRowHeader">
				<td></td>
				<td align="center" class="secondaryTd">Actual</td>
				<td align="center" class="secondaryTd">Trend</td>
				<td align="center" class="arrowTdHeader"><span class="rArrow">&#8593;</span><span class="gArrow">&#8595; &nbsp;</span></td>
				<td></td>
				<td align="center" class="secondaryTd">Actual</td>
				<td align="center" class="secondaryTd">Trend</td>
				<td align="center" class="arrowTdHeader"><span class="rArrow">&#8593;</span><span class="gArrow">&#8595;  &nbsp;</span></td>
			</tr>
			
			<tr>
			<td class="bodyTd"  width='177'>${quala0}</td>
			<td class="bodyTd"  width='80'>${quala1} </td>
			<td class="bodyTd"  align="center"><span class="fhcSparkClassQualA"></span> </td>
			<td class="arrowTd" ><c:if test="${quala3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${quala3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${quala3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			 
			<td class="bodyTd"  width='177'>${quald0}</td>
			<td class="bodyTd"  width='80'>${quald1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassQualD"></span> </td>
			<td class="arrowTd" ><c:if test="${quald3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${quald3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${quald3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
				
			<tr>
			<td class="bodyTd"  width='177'>${qualb0}</td>
			<td class="bodyTd"  width='80'>${qualb1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassQualB"></span> </td>
			<td class="arrowTd" ><c:if test="${qualb3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${qualb3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${qualb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			 
			<td class="bodyTd"  width='177'>${quale0}</td>
			<td class="bodyTd"  width='80'>${quale1} </td>
			<td class="bodyTd" align="center"><span class="fhcSparkClassQualE"></span> </td>
			<td class="arrowTd" ><c:if test="${quale3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${quale3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${quale3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>		
			
			<tr>
			<td class="bodyTd"  width='177'>${qualc0}</td>
			<td class="bodyTd"  width='80'>${qualc1} </td>
			<td class="bodyTd" align="center"><span class="fhcSparkClassQualC"></span> </td>
			<td class="arrowTd" ><c:if test="${qualc3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${qualc3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${qualc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			 
			<td class="bodyTd"  width='177'>${qualf0}</td>
			<td class="bodyTd"  width='80'>${qualf1} </td>
			<td class="bodyTd" align="center"><span class="fhcSparkClassQualF"></span> </td>
			<td class="arrowTd" ><c:if test="${qualf3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${qualf3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${qualf3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			
			
			</table>
			<!-- big graph -->
			<table  class="mainRowHeader" >
				<tr>
			 		<td>
			 			<h4 style="text-align:center; margin-bottom: 12px;">Goal vs. Actual: Images Published</h4>
			 		</td>
	            </tr>
		 	</table>
		 	 
		    <canvas id="canvas" height="300" width="720"></canvas>
			<canvas id="canvasLegend"></canvas>   
			<canvas id="canvasLegend2"></canvas>   
			 	  
			 
		</td>
		
		
		
		<td style="padding-left: 6px;">
			<!-- Image Auditor Averages -->
			<table  class="mainRightRowHeader" > 
				<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Image Auditor Averages</h4></td></tr>
		 	</table>
			<table>
			<tr class="secondaryRowHeader">
				<td align="center" class="secondaryTd"></td>
				<td align="center" class="secondaryTd">Avg/Auditor</td>
				<td align="center" class="secondaryTd">Trend</td>
				<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
			</tr>
			
			<tr>
			<td class="bodyTd"  width='177'>${audita0}</td>
			<td class="bodyTd"  width='80'>${audita1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAuditA"></span> </td>
			<td class="arrowTd" ><c:if test="${audita3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${audita3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${audita3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${auditb0}</td>
			<td class="bodyTd"  width='80'>${auditb1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAuditB"></span> </td>
			<td class="arrowTd" ><c:if test="${auditb3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${auditb3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${auditb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
		 	</tr>
			<tr>
			<td class="bodyTd"  width='177'>${auditc0}</td>
			<td class="bodyTd"  width='80'>${auditc1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAuditC"></span> </td>
			<td class="arrowTd" ><c:if test="${auditc3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${auditc3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${auditc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${auditd0}</td>
			<td class="bodyTd"  width='80'>${auditd1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAuditD"></span> </td>
			<td class="arrowTd" ><c:if test="${auditd3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${auditd3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${auditd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			 
			</table>
			
					<!-- Top 5 Image Quality Issues -->
			<table  class="mainRightRowHeader" > 
				<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Top 5 Image Quality Issues</h4></td></tr>
		 	</table>
			<table>
			<tr class="secondaryRowHeader">
				<td align="center" class="secondaryTd">Issue</td>
				<td align="center" class="secondaryTd">%</td>
				<td align="center" class="secondaryTd">Trend</td>
				<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
			</tr>
			
			<tr>
			<td class="bodyTd"  width='177'>${topa0}</td>
			<td class="bodyTd" align="center" width='80'>${topa1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopA"></span> </td>
			<td class="arrowTd" ><c:if test="${topa3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${topa3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${topa3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${topb0}</td>
			<td class="bodyTd"  align="center" width='80'>${topb1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopB"></span> </td>
			<td class="arrowTd" ><c:if test="${topb3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${topb3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${topb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
		 	</tr>
			<tr>
			<td class="bodyTd"  width='177'>${topc0}</td>
			<td class="bodyTd"  align="center" width='80'>${topc1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopC"></span> </td>
			<td class="arrowTd" ><c:if test="${topc3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${topc3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${topc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${topd0}</td>
			<td class="bodyTd" align="center" width='80'>${topd1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopD"></span> </td>
			<td class="arrowTd" ><c:if test="${topd3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${topd3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${topd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${tope0}</td>
			<td class="bodyTd" align="center" width='80'>${tope1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopE"></span> </td>
			<td class="arrowTd" ><c:if test="${tope3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${tope3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${tope3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			 
			</table>
			
			<!-- Aged Metrics -->
			<table  class="mainRightRowHeader" > 
				<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Aged Metrics</h4></td></tr>
		 	</table>
			<table>
			<tr class="secondaryRowHeader">
				<td></td>
				<td align="center" class="secondaryTd">Avg minutes per Book</td>
				<td align="center" class="secondaryTd">Trend</td>
				<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
			</tr>
			
			<tr>
			<td class="bodyTd"  width='177'>${ageda0}</td>
			<td class="bodyTd"  width='80'>${ageda1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedA"></span> </td>
			<td class="arrowTd" ><c:if test="${ageda3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${ageda3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${ageda3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${agedb0}</td>
			<td class="bodyTd"  width='80'>${agedb1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedB"></span> </td>
			<td class="arrowTd" ><c:if test="${agedb3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${agedb3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${agedb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
		 	</tr>
			<tr>
			<td class="bodyTd"  width='177'>${agedc0}</td>
			<td class="bodyTd"  width='80'>${agedc1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedC"></span> </td>
			<td class="arrowTd" ><c:if test="${agedc3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${agedc3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${agedc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${agedd0}</td>
			<td class="bodyTd"  width='80'>${agedd1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedD"></span> </td>
			<td class="arrowTd" ><c:if test="${agedd3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${agedd3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${agedd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
				<tr>
			<td class="bodyTd"  width='177'>${agede0}</td>
			<td class="bodyTd"  width='80'>${agede1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedE"></span> </td>
			<td class="arrowTd" ><c:if test="${agede3=='up'}"><span class="gArrow">&#8593;</span></c:if>
					<c:if test="${agede3=='down'}"><span class="rArrow">&#8595;</span></c:if>
					<c:if test="${agede3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>			
			</table>
			
		</td>
		
		</tr></table>
		
		</form>
    </div>
 
  </div>
</div>
</div>
			  

		

</jsp:body>
</tags:template>