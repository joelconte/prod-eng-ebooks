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
		width: 500px;
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
	
	#chart_divEmpty{ margin-top: 200px; }
</style>
<script> 
  
$(function() { 
    /////////////////////Chart.js
 	
    	/* removed
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
     
       
       
    	//three large line graphs
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
		*/
		
		
		$("#start_date").datepicker();
		$("#end_date").datepicker();

	});
</script>


<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
//combo chart using google charts bar chart for monthly numbers - https://developers.google.com/chart/
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawVisualization);


      function drawVisualization() {
        // Some raw data (not necessarily accurate)
        
        var horizontalLineDataOneSite = <c:out value="${horizontalLineDataOneSite}" escapeXml="false" />;//these will be 2-dim array string, or 'none'
        var horizontalLineDataFHL = <c:out value="${horizontalLineDataFHL}" escapeXml="false" />;
        var horizontalLineDataPartnerLibraries = <c:out value="${horizontalLineDataPartnerLibraries}" escapeXml="false" />;
        var horizontalLineDataInternetArchive = <c:out value="${horizontalLineDataInternetArchive}" escapeXml="false" />;
       
       /* horizontalLineData = [
         ['xMonth', 'xScan',   'xPublish', 'xGoal'],
         ['J',  165, 450, 214.6],
         ['F',  135, 288, 214.6],
         ['M',  157, 397, 214.6],
         ['A',  139, 215, 214.6],
         ['M',  136, 366, 214.6]
     	 ];*/
     	
     	var dataOneSite = google.visualization.arrayToDataTable(horizontalLineDataOneSite);
        var dataFHL = google.visualization.arrayToDataTable(horizontalLineDataFHL);
     	var dataHorizontalLineDataPartnerLibraries = google.visualization.arrayToDataTable(horizontalLineDataPartnerLibraries);
     	var dataHorizontalLineDataInternetArchive = google.visualization.arrayToDataTable(horizontalLineDataInternetArchive);


    var options0 = {
       lxegend: {position: 'none'},
       orientation: 'vertical',
       title : '<c:out value="${site}" escapeXml="false" />',
       seriesType: 'bars',
       series: {0: {color: 'blue'}, 1: {color: 'green'}, 2: {type: 'line'}}
    };
    var options1 = {
      xlegend: {position: 'none'},
      orientation: 'vertical',
      title : 'FHCs and FHLs',
      seriesType: 'bars',
      series: {0: {color: 'blue'}, 1: {color: 'green'}, 2: {type: 'line'}}
    };

    var options2 = {
      xlegend: {position: 'none'},
      orientation: 'vertical',
      title : 'Partner Libraries',
      seriesType: 'bars',
      series: {0: {color: 'blue'}, 1: {color: 'green'}, 2: {type: 'line'}}
    };

    var options3 = {
      xlegend: {position: 'none'},
      orientation: 'vertical',
      title : 'Internet Archive',
      seriesType: 'bars',
      series: {0: {color: 'blue'}, 1: {color: 'green'}, 2: {type: 'line'}}
    };
    
    
    //horizontal line charts - left side
    if(horizontalLineDataOneSite!=null && horizontalLineDataOneSite!=''){
    	var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div1'));
        chart.draw(dataOneSite, options0);
        document.getElementById('google_combo_div2').style.display = "none";
        document.getElementById('google_combo_div3').style.display = "none";
    }else{
    	var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div1'));
        chart.draw(dataFHL, options1);
        var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div2'));
        chart.draw(dataHorizontalLineDataPartnerLibraries, options2);
        var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div3'));
        chart.draw(dataHorizontalLineDataInternetArchive, options3);
    }
   
  

  }


</script>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	  //PIE charts
      // Load the Visualization API and the piechart package.
      google.load('visualization', '1.0', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {

        // Create the scan data table.
        /*var goal = <c:out value="${goal}"/>;
        var goalStr = "";
        if(goal == 0){
        	goalStr = " - No Goal Exists for Site"
        }else{
        	goalStr = ""
        }*/
        
        
        //SCAN YTD 2nd column piecharts
        
        var siteCount = <c:out value="${ytdPiesCount}"   escapeXml="false" />;
        //need to convert java list<list> into some sort of js datastructure
        var ytdPies = <c:out value="${twoDimArrayStr}"  escapeXml="false" />;//js 2-dim array
        
        for(x=0;x<siteCount;x++){
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Books');
            data.addColumn('number', 'Count');
            data.addRows([
              ['Goal Remaining',  ytdPies[x][3]],
              ['Complete', ytdPies[x][2]]
            ]);

            //   Set chart options
            var title = '';
            if(ytdPies[x][1] > 0){
            	title = ytdPies[x][0] + ' [' + ytdPies[x][2] + ' / ' + ytdPies[x][1] + ']'; //[complete/goal]
            	var options = {'fontSize': 12,
            			'title': title,
                        'width':400,
                        'height':150,
                        colors: ['red', 'green'],
                        chartArea: {left:16}};

         		// Instantiate and draw our chart, passing in some options.
        		var chartX = new google.visualization.PieChart(document.getElementById('ytd_scan_div'+x));
        		chartX.draw(data, options);
            }else{
            	//no pie chart, just site name
	            title = ytdPies[x][0] + ' [' + ytdPies[x][2] + ' / ' + ytdPies[x][1] + ']'; //[complete/goal]
	            var elem = document.getElementById('ytd_scan_div'+x);
	            elem.innerHTML = '<h6 style="padding-left: 15px; text-align: left;">'+title+'</h6>';
	            
            }
            
            
        }



        //PUBLISHED
        //  3rd column - piecharts
        
        siteCount = <c:out value="${ytdPiesCount}"   escapeXml="false" />;
        //need to convert java list<list> into some sort of js datastructure
        ytdPies = <c:out value="${twoDimArrayStr}"  escapeXml="false" />;//js 2-dim array
        
        for(x=0;x<siteCount;x++){
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Books');
            data.addColumn('number', 'Count');
            data.addRows([
              ['Goal Remaining',  ytdPies[x][5]],
              ['Complete', ytdPies[x][4]]
            ]);

            //   Set chart options
            var title = '';
            if(ytdPies[x][1] > 0){
            	title = ytdPies[x][0] + ' [' + ytdPies[x][4] + ' / ' + ytdPies[x][1] + ']'; //[complete/goal]
            	var options = {'fontSize': 12,
            			'title': title,
                        'width':400,
                        'height':150,
                        colors: ['red', 'green'],
                        chartArea: {left:16}};

         		// Instantiate and draw our chart, passing in some options.
        		var chartX = new google.visualization.PieChart(document.getElementById('ytd_publish_div'+x));
        		chartX.draw(data, options);
            }else{
            	//no pie chart, just site name
	            title = ytdPies[x][0] + ' [' + ytdPies[x][4] + ' / ' + ytdPies[x][1] + ']'; //[complete/goal]
	            var elem = document.getElementById('ytd_publish_div'+x);
	            elem.innerHTML = '<h6 style="padding-left: 15px; text-align: left;">'+title+'</h6>';
	            
            }
            
            
        }
       
  };
  
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
			        <c:if test="${year == '2019'}"> <option selected="true">2019</option></c:if>
			        <c:if test="${year != '2019'}"> <option>2019</option></c:if>
			        <c:if test="${year == '2020'}"> <option selected="true">2020</option></c:if>
			        <c:if test="${year != '2020'}"> <option>2020</option></c:if>
			        <c:if test="${year == '2021'}"> <option selected="true">2021</option></c:if>
			        <c:if test="${year != '2021'}"> <option>2021</option></c:if>
			      
				</select>
				</th>
				<!-- <th>${messages['month']}</th>
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
				</th> -->
				<th>&nbsp;&nbsp;
		 		<select   id="siteDropdown" name="site" >
				 
				 
				<c:forEach var="i" items="${allLocations}">
    				<c:if test="${i==site}"><option selected>${i}</option> </c:if>
					<c:if test="${i!=site}"><option>${i}</option> </c:if>
				</c:forEach>
				</select>
			</th>
			<th>
			 <button value="load" name="load" id="load" style="margin-bottom: 7px;">Refresh</button>
			</th>
			</tr>		
		</table>
		
		
		<table> <tr><td style="padding-bottom: 340px;">
			<table  class="mainRowHeader" >
				<tr>
			 		<td>
			 			<h4 style="text-align:center; margin-bottom: 12px;">Scan and Publish counts</h4>
			 		</td>
	            </tr>
		 	</table>
		 	<div  style="padding-left: 20px;">
		 	 
		 	<div style="padding-top: 11px; padding-left: 50px;">
		 	MTD Scan (books, images): &nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${aboveHorizontalLineTotalMTDScan}"/> <br>
		 	</div>
		 	<div style="padding-top: 11px; padding-left: 50px;">
			MTD Publish (books, images): <c:out value="${aboveHorizontalLineTotalMTDPublish}"/> <br> 
			</div>
			<div style="padding-top: 11px; padding-left: 50px;">
			YTD Scan (books, images): &nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${aboveHorizontalLineTotalYTDScan}"/> <br>
			</div>
			<div style="padding-top: 11px; padding-left: 50px;">
			YTD Publish (books, images): <c:out value="${aboveHorizontalLineTotalYTDPublish}"/><br> 
			</div>
		 	 
		 	</div>
			<div id="google_combo_div1" style="width: 450px; height: 300px;"></div>
			<div id="google_combo_div2" style="width: 450px; height: 300px;"></div>
			<div id="google_combo_div3" style="width: 450px; height: 300px;"></div>
			 
			  
			
		    <!-- Open Issues -->
			<table  class="mainRowHeader" > 
				<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Open Issues</h4></td></tr>
		 	</table>
			<table>
			<tr class="secondaryRowHeader">
				<td align="center"  width='360' class="secondaryTd">Owner</td>
				<td align="center"  width='140' class="secondaryTd">Number Open</td>
			</tr>
			
			<tr>
		 
			<c:forEach items="${openIssues}" var="row">
				<tr>
			    <td class="bodyTd"  width='177'>${row.get(0)}</td>
			    <td class="bodyTd" align="center" width='80'>${row.get(1)} </td>
			    </tr>
            </c:forEach>

			</tr>	 
			</table>
			 
			 
			 
			<!-- big graph  - removed
			<table  class="mainRowHeader" >
				<tr>
			 		<td>
			 			<h4 style="text-align:center; margin-bottom: 12px;">Image/page Counts from Beginning of Year Through Selected Month</h4>
			 		</td>
	            </tr>
		 	</table>
		 	 
		 	
		 	  
		    <canvas id="canvas1" height="200" width="820"></canvas>
			<canvas id="canvasLegend"  height="50"></canvas>   
			<canvas id="canvas2" height="200" width="820"></canvas>
			<canvas id="canvasLegend2" height="50"></canvas>   
			<canvas id="canvas3" height="200" width="820"></canvas>
			<canvas id="canvasLegend3" height="50"></canvas>   
 
			 	   -->
			 
		</td>
		
		
		<!-- YTD scan and publish pie charts here -->
		<td style="padding-left: 6px;">
			<!-- /Goal Actual Pie Charts -->
			<table  class="mainRightRowHeader" > 
				<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">YTD Scan / Goal</h4></td></tr>
		 	</table>
			<table>
			<tr>
				<td align="center" >
					<!--Extra dummy Divs that will hold the pie chart-->
					<c:forEach var="i" begin="0" end="${ytdPiesCount}">
						<div id="ytd_scan_div${i}"></div>
					</c:forEach>
  				
				</td>
			</tr>
			</table>
			
			
			
			<!-- future charts here -->
			
			
		</td>
				
		<!-- percent completed of scanned piecharts here -->
		<td style="padding-left: 6px;">
			<!-- /Goal Actual Pie Charts -->
			<table  class="mainRightRowHeader" > 
				<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">YTD Publish / Goal</h4></td></tr>
		 	</table>
			<table>
			<tr>
				<td align="center" >
					<!--Div that will hold the pie chart-->
					<c:forEach var="i" begin="0" end="${ytdPiesCount}">
						<div id="ytd_publish_div${i}"></div>
					</c:forEach>
  					 
  				
				</td>
			</tr>
			</table>
			
		 
			 
			
			<!-- future charts here -->
			
		</td>
		
		</tr></table>
		
		</form>
    </div>
 
  </div>
</div>
</div>
			  

		

</jsp:body>
</tags:template>