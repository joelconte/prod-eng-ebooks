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
    /////////////////////Chart.js
 	//large graph  
 	 
		var lineChartDataScan = {
			labels : <c:out value="${xAxisLabels}"  escapeXml="false" />,
			datasets : [ {
				fillColor : "orange",
				strokeColor : "orange",
				pointColor : "orange",
				pointStrokeColor : "#fff",
				data : <c:out value="${scanActuals}"/>,
			 
			} ]

		}
		var lineChartDataProcess = {
				labels : <c:out value="${xAxisLabels}"  escapeXml="false" />,
				datasets : [  {
					fillColor : "blue",
					strokeColor : "blue",
					pointColor : "blue",
					pointStrokeColor : "#fff",
					data : <c:out value="${processActuals}"/>,
				 
				} ]

			}
		var lineChartDataPublish = {
				labels : <c:out value="${xAxisLabels}"  escapeXml="false" />,
				datasets : [ {	fillColor : "green",
					strokeColor : "green",
					pointColor : "green",
					pointStrokeColor : "#fff",
					data : <c:out value="${publishActuals}"/>,
				 
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
		var myLineScan = new Chart(document.getElementById("canvas1").getContext("2d")).Line(lineChartDataScan, options);
		var myLineProcess = new Chart(document.getElementById("canvas2").getContext("2d")).Line(lineChartDataProcess, options);
		var myLinePublish = new Chart(document.getElementById("canvas3").getContext("2d")).Line(lineChartDataPublish, options);

		//draw legends by hand
		var c = document.getElementById("canvasLegend");
		var ctx = c.getContext("2d");
		ctx.fillStyle="orange";
		ctx.fillRect(20,10,70,2);
		ctx.font = "16px Arial";
		ctx.fillText("Scan",20,30);
		var c = document.getElementById("canvasLegend2");
		var ctx = c.getContext("2d");
		ctx.fillStyle="blue";
		ctx.fillRect(20,10,70,2)
		ctx.font = "16px Arial";
		ctx.fillText("Process",20,30);
		var c = document.getElementById("canvasLegend3");
		var ctx = c.getContext("2d");
		ctx.fillStyle="green";
		ctx.fillRect(20,10,70,2)
		ctx.font = "16px Arial";
		ctx.fillText("Publish",20,30);
		
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
		<!-- <tr> 
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
				 
				 
				<c:forEach var="i" items="${allLocations}">
    				<c:if test="${i==site}"><option selected>${i}</option> </c:if>
					<c:if test="${i!=site}"><option>${i}</option> </c:if>
				</c:forEach>
				</select>
			</th>
			<th>
			 <button value="load" name="load" id="load">Refresh</button>
			</th>
		</tr> -->
		<tr>
				<th>${messages['year']} </th>
			    <th><select id="year" name="year">
			        <c:if test="${year == '2010'}"> <option selected="true">2010</option></c:if>
			        <c:if test="${year != '2010'}"> <option>2010</option></c:if>
			        <c:if test="${year == '2011'}"> <option selected="true">2011</option></c:if>
			        <c:if test="${year != '2011'}"> <option>2011</option></c:if>
			        <c:if test="${year == '2012'}"> <option selected="true">2012</option></c:if>
			        <c:if test="${year != '2012'}"> <option>2012</option></c:if>
			        <c:if test="${year == '2013'}"> <option selected="true">2013</option></c:if>
			        <c:if test="${year != '2013'}"> <option>2013</option></c:if>
			        <c:if test="${year == '2014'}"> <option selected="true">2014</option></c:if>
			        <c:if test="${year != '2014'}"> <option>2014</option></c:if>
			        <c:if test="${year == '2015'}"> <option selected="true">2015</option></c:if>
			        <c:if test="${year != '2015'}"> <option>2015</option></c:if>
			        <c:if test="${year == '2016'}"> <option selected="true">2016</option></c:if>
			        <c:if test="${year != '2016'}"> <option>2016</option></c:if>
			        <c:if test="${year == '2017'}"> <option selected="true">2017</option></c:if>
			        <c:if test="${year != '2017'}"> <option>2017</option></c:if>
			        <c:if test="${year == '2018'}"> <option selected="true">2018</option></c:if>
			        <c:if test="${year != '2018'}"> <option>2018</option></c:if>
			      
				</select>
				</th>
				<th>${messages['month']}</th>
				<th><select id="month" name="month">
			   		 <c:if test="${month == '1'}"><option selected="true">1</option></c:if>
			   		 <c:if test="${month != '1'}"><option>1</option></c:if>
			   		 <c:if test="${month == '2'}"><option selected="true">2</option></c:if>
	 		   		 <c:if test="${month != '2'}"><option>2</option></c:if>
			   		 <c:if test="${month == '3'}"><option selected="true">3</option></c:if>
			   		 <c:if test="${month != '3'}"><option>3</option></c:if>
			   		 <c:if test="${month == '4'}"><option selected="true">4</option></c:if>
			   		 <c:if test="${month != '4'}"><option>4</option></c:if>
			   		 <c:if test="${month == '5'}"><option selected="true">5</option></c:if>
			   		 <c:if test="${month != '5'}"><option>5</option></c:if>
			   		 <c:if test="${month == '6'}"><option selected="true">6</option></c:if>
			   		 <c:if test="${month != '6'}"><option>6</option></c:if>
			   		 <c:if test="${month == '7'}"><option selected="true">7</option></c:if>
			   		 <c:if test="${month != '7'}"><option>7</option></c:if>
			   		 <c:if test="${month == '8'}"><option selected="true">8</option></c:if>
			   		 <c:if test="${month != '8'}"><option>8</option></c:if>
			   		 <c:if test="${month == '9'}"><option selected="true">9</option></c:if>
			   		 <c:if test="${month != '9'}"><option>9</option></c:if>
			   		 <c:if test="${month == '10'}"><option selected="true">10</option></c:if>
			   		 <c:if test="${month != '10'}"><option>10</option></c:if>
			   		 <c:if test="${month == '11'}"><option selected="true">11</option></c:if>
			   		 <c:if test="${month != '11'}"><option>11</option></c:if>
			   		 <c:if test="${month == '12'}"><option selected="true">12</option></c:if>
			   		 <c:if test="${month != '12'}"><option>12</option></c:if>
 
				</select>
				</th>
				<th>&nbsp;&nbsp;
		 		<select style="margin-bottom: 0px;" id="siteDropdown" name="site" >
				 
				 
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
			  
			<!-- big graph -->
			<table  class="mainRowHeader" >
				<tr>
			 		<td>
			 			<h4 style="text-align:center; margin-bottom: 12px;">Book Counts Per Month</h4>
			 		</td>
	            </tr>
		 	</table>
		 	 
		    <canvas id="canvas1" height="300" width="720"></canvas>
			<canvas id="canvasLegend"  height="50"></canvas>   
			<canvas id="canvas2" height="300" width="720"></canvas>
			<canvas id="canvasLegend2" height="50"></canvas>   
			<canvas id="canvas3" height="300" width="720"></canvas>
			<canvas id="canvasLegend3" height="50"></canvas>   
 
			 	  
			 
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
			<td class="arrowTd" ><c:if test="${topa3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${topa3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${topa3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${topb0}</td>
			<td class="bodyTd"  align="center" width='80'>${topb1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopB"></span> </td>
			<td class="arrowTd" ><c:if test="${topb3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${topb3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${topb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
		 	</tr>
			<tr>
			<td class="bodyTd"  width='177'>${topc0}</td>
			<td class="bodyTd"  align="center" width='80'>${topc1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopC"></span> </td>
			<td class="arrowTd" ><c:if test="${topc3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${topc3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${topc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${topd0}</td>
			<td class="bodyTd" align="center" width='80'>${topd1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopD"></span> </td>
			<td class="arrowTd" ><c:if test="${topd3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${topd3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${topd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${tope0}</td>
			<td class="bodyTd" align="center" width='80'>${tope1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTopE"></span> </td>
			<td class="arrowTd" ><c:if test="${tope3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${tope3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${tope3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			 
			</table>
			
			<!-- Aged Metrics -->
			<!-- 

Removing Aged Metrice charts.  True data, but not very useful.
I think the math behind the 46 minutes is showing that the rate of book production is 46 min/book.  (not that it takes 46 minutes for a given book 

to flow through the system one after the other...)
We can change it to something more useful.
...So, we could have 1000 books that go through the system in 30 days, then this would show:
(30*24*60)/1000 = 43.2 min/books...
			
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
			<td class="arrowTd" ><c:if test="${ageda3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${ageda3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${ageda3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${agedb0}</td>
			<td class="bodyTd"  width='80'>${agedb1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedB"></span> </td>
			<td class="arrowTd" ><c:if test="${agedb3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${agedb3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${agedb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
		 	</tr>
			<tr>
			<td class="bodyTd"  width='177'>${agedc0}</td>
			<td class="bodyTd"  width='80'>${agedc1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedC"></span> </td>
			<td class="arrowTd" ><c:if test="${agedc3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${agedc3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${agedc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${agedd0}</td>
			<td class="bodyTd"  width='80'>${agedd1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedD"></span> </td>
			<td class="arrowTd" ><c:if test="${agedd3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${agedd3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${agedd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
				<tr>
			<td class="bodyTd"  width='177'>${agede0}</td>
			<td class="bodyTd"  width='80'>${agede1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassAgedE"></span> </td>
			<td class="arrowTd" ><c:if test="${agede3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${agede3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${agede3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>			
			</table>
			 -->
			
			<!-- Turnaround Time -->
			<table  class="mainRightRowHeader" > 
				<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Turnaround Time</h4></td></tr>
		 	</table>
			<table>
			<tr class="secondaryRowHeader">
				<td></td>
				<td align="center" class="secondaryTd">Avg time (days) in each phase</td>
				<td align="center" class="secondaryTd">Trend</td>
				<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
			</tr>
			
			<tr>
			<td class="bodyTd"  width='177'>${turnarounda0}</td>
			<td class="bodyTd"  width='80'>${turnarounda1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTurnaroundA"></span> </td>
			<td class="arrowTd" ><c:if test="${turnarounda3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${turnarounda3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${turnarounda3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${turnaroundb0}</td>
			<td class="bodyTd"  width='80'>${turnaroundb1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTurnaroundB"></span> </td>
			<td class="arrowTd" ><c:if test="${turnaroundb3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${turnaroundb3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${turnaroundb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
		 	</tr>
			<tr>
			<td class="bodyTd"  width='177'>${turnaroundc0}</td>
			<td class="bodyTd"  width='80'>${turnaroundc1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTurnaroundC"></span> </td>
			<td class="arrowTd" ><c:if test="${turnaroundc3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${turnaroundc3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${turnaroundc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
			<tr>
			<td class="bodyTd"  width='177'>${turnaroundd0}</td>
			<td class="bodyTd"  width='80'>${turnaroundd1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTurnaroundD"></span> </td>
			<td class="arrowTd" ><c:if test="${turnaroundd3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${turnaroundd3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${turnaroundd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
			</td>
			</tr>
				<tr>
			<td class="bodyTd"  width='177'>${turnarounde0}</td>
			<td class="bodyTd"  width='80'>${turnarounde1} </td>
			<td  class="bodyTd" align="center"><span class="fhcSparkClassTurnaroundE"></span> </td>
			<td class="arrowTd" ><c:if test="${turnarounde3=='up'}"><span class="rArrow">&#8593;</span></c:if>
					<c:if test="${turnarounde3=='down'}"><span class="gArrow">&#8595;</span></c:if>
					<c:if test="${turnarounde3=='equal'}"><span class="gDash">&#8669;</span></c:if>
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