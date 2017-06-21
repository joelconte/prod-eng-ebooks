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
		width: 600px;
	}
	.mainRowHeaderWide{background-color: #87B940;
		border: 2px solid white;
		height: 20px;
		padding-top: 5px;
		width: 810px;
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
	.bodyTd2{	 }
	.arrowTd{	border: 1px solid white; margin-left:auto; margin-right:auto;  padding-left: 7px; }
	.arrowTdHeader{ width:30px;	border: 1px solid white; margin-left:auto; margin-right:auto; padding-right: 3px; vertical-align: middle;}
	.rArrow{color: red; border: 2px; padding-left: 3px; padding-top: 5px;  font-weight: bold; font-size: 20px; vertical-align: middle;}
	.gArrow{color: green; border: 2px; padding-left: 3px; padding-top: 5px;  font-weight: bold; font-size: 20px; vertical-align: middle;}
	.gDash{color: green; border: 2px; padding-left: 3px; padding-top: 5px;  font-weight: light; font-size: 20px; vertical-align: middle;}
	td{height: 24px;}
	
	#chart_divEmpty{ margin-top: 200px; }
</style>
<script type="text/javascript"> 
  
  function checkDashboard2Dates(){
	  var dStart = document.getElementById('start_date').value;
	  var dEnd = document.getElementById('end_date').value;
	  var i = dStart.indexOf('/');
	  i = dStart.indexOf('/', i+1);
	  var y1 = dStart.substring(i+1);
	  i = dEnd.indexOf('/');
	  i = dEnd.indexOf('/', i+1);
	  var y2 = dEnd.substring(i+1);
	  
	  if(y1==y2){
		  document.getElementById('f1').submit();
	  }else{
		  window.alert("Start and End dates must be within the same year.");
		  return false;
	  }
  }
  
$(function() { 
	 
		///////////////////////sparklines
	 
		//production
		var fhcA =  <c:out value="${a2}"/>;
	    $('.fhcSparkClassA').sparkline(fhcA, {type: 'line', width: '130px', fillColor: undefined} ); 
	    var fhcB  =  <c:out value="${b2}"/>;
	    $('.fhcSparkClassB').sparkline(fhcB, {type: 'line', width: '130px', fillColor: undefined} ); 
	    var fhcC  =  <c:out value="${c2}"/>;
	    $('.fhcSparkClassC').sparkline(fhcC, {type: 'line', width: '130px', fillColor: undefined} ); 
	    var fhcD  =  <c:out value="${d2}"/>;
	    $('.fhcSparkClassD').sparkline(fhcD, {type: 'line', width: '130px', fillColor: undefined} ); 
	    var fhcE  =  <c:out value="${e2}"/>;
	    $('.fhcSparkClassE').sparkline(fhcE, {type: 'line', width: '130px', fillColor: undefined} ); 
	    var fhcF  =  <c:out value="${f2}"/>;
	    $('.fhcSparkClassF').sparkline(fhcF, {type: 'line', width: '130px', fillColor: undefined} ); 
	    
	    

	    //quality
	    var qa2  =  <c:out value="${quala2}"/>;
	    $('.fhcSparkClassQualA').sparkline(qa2, {type: 'line', width: '100px', fillColor: undefined} ); 
	    var qb2  =  <c:out value="${qualb2}"/>;
	    $('.fhcSparkClassQualB').sparkline(qb2, {type: 'line', width: '100px', fillColor: undefined} ); 
	    var qc2  =  <c:out value="${qualc2}"/>;
	    $('.fhcSparkClassQualC').sparkline(qc2, {type: 'line', width: '100px', fillColor: undefined} ); 
	    


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

	    
	    //Turnaround Time
	    var tta2  =  <c:out value="${turnarounda2}"/>;
	    $('.fhcSparkClassTurnaroundA').sparkline(tta2, {type: 'line', width: '100px', fillColor: undefined} ); 
	    var ttb2  =  <c:out value="${turnaroundb2}"/>;
	    $('.fhcSparkClassTurnaroundB').sparkline(ttb2, {type: 'line', width: '100px', fillColor: undefined} ); 
	    var ttc2  =  <c:out value="${turnaroundc2}"/>;
	    $('.fhcSparkClassTurnaroundC').sparkline(ttc2, {type: 'line', width: '100px', fillColor: undefined} ); 
	    var ttd2  =  <c:out value="${turnaroundd2}"/>;
	    $('.fhcSparkClassTurnaroundD').sparkline(ttd2, {type: 'line', width: '100px', fillColor: undefined} ); 
	    var tte2  =  <c:out value="${turnarounde2}"/>;
	    $('.fhcSparkClassTurnaroundE').sparkline(tte2, {type: 'line', width: '100px', fillColor: undefined} );   
	    
	    
 	   	/////////////////////Chart.js
 		//large graph  
 	 
		var lineChartData = {
			labels : <c:out value="${goalsLabels}"  escapeXml="false" />,
			datasets : [ {
				fillColor : "orange",
				strokeColor : "orange",
				pointColor : "orange",
				pointStrokeColor : "#fff",
				data : <c:out value="${goals}"/>,
			 
			}, {
				fillColor : "blue",
				strokeColor : "blue",
				pointColor : "blue",
				pointStrokeColor : "#fff",
				data : <c:out value="${scan}"/>,
			 
			}, {
				fillColor : "green",
				strokeColor : "green",
				pointColor : "green",
				pointStrokeColor : "#fff",
				data : <c:out value="${publish}"/>,
			 
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
		ctx.fillStyle="Orange";
		ctx.fillRect(20,10,70,2);
		ctx.font = "16px Arial";
		ctx.fillText("Goal",20,30);
		c = document.getElementById("canvasLegend2");
		var ctx = c.getContext("2d");
		ctx.fillStyle="blue";
		ctx.fillRect(20,10,70,2);
		ctx.font = "16px Arial";
		ctx.fillText("Scan",20,30);
		c = document.getElementById("canvasLegend3");
		var ctx = c.getContext("2d");
		ctx.fillStyle="green";
		ctx.fillRect(20,10,70,2);
		ctx.font = "16px Arial";
		ctx.fillText("Publish",20,30);
		
		
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
        var horizontalLineDataInternetArchiveRt = <c:out value="${horizontalLineDataInternetArchiveRt}" escapeXml="false" />;
        var horizontalLineDataGmrv = <c:out value="${horizontalLineDataGmrv}" escapeXml="false" />;
       
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
     	var dataHorizontalLineDataInternetArchiveRt = google.visualization.arrayToDataTable(horizontalLineDataInternetArchiveRt);
     	var dataHorizontalLineDataGmrv = google.visualization.arrayToDataTable(horizontalLineDataGmrv);


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
      title : 'Internet Archive (w/o RT)',
      seriesType: 'bars',
      series: {0: {color: 'blue'}, 1: {color: 'green'}, 2: {type: 'line'}}
    };
    
    var options4 = {
 	      xlegend: {position: 'none'},
 	      orientation: 'vertical',
 	      title : 'Internet Archive - RT',
 	      seriesType: 'bars',
 	      series: {0: {color: 'blue'}, 1: {color: 'green'}, 2: {type: 'line'}}
    };
    var options5 = {
   	      xlegend: {position: 'none'},
   	      orientation: 'vertical',
   	      title : 'GMRV',
   	      seriesType: 'bars',
   	      series: {0: {color: 'blue'}, 1: {color: 'green'}, 2: {type: 'line'}}
   	};
    
    //horizontal line charts - left side
    if(horizontalLineDataOneSite!=null && horizontalLineDataOneSite!=''){
    	var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div1'));
        chart.draw(dataOneSite, options0);
        document.getElementById('google_combo_div1').style.marginTop = "150px";
        document.getElementById('google_combo_div2').style.display = "none";
        document.getElementById('google_combo_div3').style.display = "none";
        document.getElementById('google_combo_div4').style.display = "none";
        document.getElementById('google_combo_div5').style.display = "none";
    }else{
    	var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div1'));
        chart.draw(dataFHL, options1);
        var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div2'));
        chart.draw(dataHorizontalLineDataPartnerLibraries, options2);
        var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div3'));
        chart.draw(dataHorizontalLineDataInternetArchive, options3);
        var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div4'));
        chart.draw(dataHorizontalLineDataInternetArchiveRt, options4);
        var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div5'));
        chart.draw(dataHorizontalLineDataGmrv, options5);
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
                        'width':420,
                        'height':200,
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
                        'width':420,
                        'height':200,
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
		
		<!-- Date selection table -->
		<table>
		<tr> 
	   		<th>${messages['startDate']}</th>
			<th><input id="start_date" name="startDate" value="${startDate}"/>
			</th>
			<th>&nbsp;&nbsp; ${messages['endDate']}</th>
			<th><input id="end_date" name="endDate"  value="${endDate}"/>
			</th>
		 
		 
			<!--
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
				</th> -->
				<th>&nbsp;&nbsp;
		 		<select   id="siteDropdown" name="site" >
				 
				 
				<c:forEach var="i" items="${allScanLocations}">
    				<c:if test="${i==site}"><option selected>${i}</option> </c:if>
					<c:if test="${i!=site}"><option>${i}</option> </c:if>
				</c:forEach>
				</select>
				</th>
				<th>
				 <button value="load" name="load" id="load" style="margin-bottom: 7px;"  onclick="js:checkDashboard2Dates(); return false;">Refresh</button>
				</th>
			</tr>		
		</table>
		
		<!-- MAIN   table -->
		<table> 
			<tr>
				<!--  left side page td -->
				<td style="padding-bottom: 340px;">
					<table  class="mainRowHeader" >
						<tr>
			 				<td>
			 				<h4 style="text-align:center; margin-bottom: 12px;">Scan and Publish counts</h4>
			 				</td>
	            		</tr>
		 			</table>
		 			<div  style="margin-top: 40px; padding-left: 20px;">
			 	 
			 			<div style="padding-top: 11px; padding-left: 50px;">
			 			(date-range) Scan (books, images): &nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${aboveHorizontalLineTotalDateRangeScan}"/> <br>
			 			</div>
					 	 
					 	<div style="padding-top: 11px; padding-left: 50px;">
						(date-range) Publish (books, images): <c:out value="${aboveHorizontalLineTotalDateRangePublish}"/> <br> 
						</div>
						<div style="padding-top: 22px; padding-left: 10px; font-size: 140%; ">
						YTD Scan (books, images): &nbsp;&nbsp;<c:out value="${aboveHorizontalLineTotalYTDScan}"/> <br>
						</div>
					
						<div style="padding-top: 18px;  padding-bottom: 5px; padding-left: 10px; font-size: 140%; ">
						YTD Publish (books, images): <c:out value="${aboveHorizontalLineTotalYTDPublish}"/><br> 
						</div>
		 	 
		 			</div>
					<div id="google_combo_div1" style="margin-top: 400px; width: 450px; height: 270px;"></div>
					<div id="google_combo_div2" style="margin-top: -22px; width: 450px; height: 270px;"></div>
					<div id="google_combo_div3" style="margin-top: -22px; width: 450px; height: 270px;"></div>
					<div id="google_combo_div4" style="margin-top: -22px; width: 450px; height: 270px;"></div>
					<div id="google_combo_div5" style="margin-top: -22px; width: 450px; height: 270px;"></div>
					 
			  
			
				    <!-- Open Issues -->
					<table  class="mainRowHeader" > 
						<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Open Issues (today)</h4></td></tr>
				 	</table>
					<table>
					<tr class="secondaryRowHeader">
						<td align="center"  width='400' class="secondaryTd">Owner</td>
						<td align="center"  width='200' class="secondaryTd">Number Open</td>
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
					
					<!-- ////////////////PRODUCTION SPARKLINES//////////////////// -->
				
					<table class="mainRowHeader">
				 		<tr>
				 			<td>
				 				<h4 style="text-align:center; margin-bottom: 8px;">Production (date-range)</h4>
				 			</td>
		              	</tr>
					</table>
					<table>
					<tr class="secondaryRowHeader">
						<td style="height: 33px; width: 200px;"></td>
						<td align="center" class="secondaryTd" style="height: 33px; width: 160px;">Actual</td>
						<td align="center" class="secondaryTd"  style="height: 33px; width: 180px;">Trend</td>
						<td align="center" class="arrowTdHeader"  style="height: 33px; width: 50px;"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
					</tr>
					
					<tr>
					<td class="bodyTd"  width='177'>${a0}</td>
					<td class="bodyTd"  width='80'>${a1} </td>
					<td  class="bodyTd" align="center"><span class="fhcSparkClassA"></span> </td>
					<td class="arrowTd" ><c:if test="${a3=='up'}"><span class="gArrow">&#8593;</span></c:if>
						<c:if test="${a3=='down'}"><span class="rArrow">&#8595;</span></c:if>
						<c:if test="${a3=='equal'}"><span class="gDash">&#8669;</span></c:if>
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
					</tr>
					
					<tr>
					<td class="bodyTd" >${c0} </td>
					<td class="bodyTd" >${c1} </td>
					<td class="bodyTd"  align="center"><span class="fhcSparkClassC"></span></td>
					<td class="arrowTd" ><c:if test="${c3=='up'}"><span class="gArrow">&#8593;</span></c:if>
							<c:if test="${c3=='down'}"><span class="rArrow">&#8595;</span></c:if>
							<c:if test="${c3=='equal'}"><span class="gDash">&#8669;</span></c:if>
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
					</tr>
					
					
					<tr>
					<td class="bodyTd" >${e0}</td>
					<td class="bodyTd" >${e1} </td>
					<td  class="bodyTd" align="center"> <span class="fhcSparkClassE"></span> </td>
					<td class="arrowTd" ><c:if test="${e3=='up'}"><span class="gArrow">&#8593;</span></c:if>
							<c:if test="${e3=='down'}"><span class="rArrow">&#8595;</span></c:if>
							<c:if test="${e3=='equal'}"><span class="gDash">&#8669;</span></c:if>
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
					</tr>
					
					</table>
							
				 	
					
						<!-- ////////////////end PRODUCTION SPARKLINES//////////////////// -->
				</td>
		
				<!-- right side of page td -->
				<td>
					<table>

						<!-- big graph  spanning 2 columns-->
						<tr>
						<td colspan="2">
						<table  class="mainRowHeaderWide" >
							<tr>
						 		<td>
						 			<h4 style="text-align:center; margin-bottom: 12px;">YTD Scan / Publish / Goal</h4>
						 		</td>
				            </tr>
					 	</table>
					
					    <canvas id="canvas" height="200" width="830"></canvas>
						<canvas id="canvasLegend"  width="100" height="50" style="padding-left:500px; "></canvas>  
						<canvas id="canvasLegend2" width="100" height="50"></canvas>   
						<canvas id="canvasLegend3" width="100" height="50"></canvas>   
			 			<div style="height:22px;"></div>
						</td>
							
						</tr>
					
					
						<!-- right side 2 skinny columns of pie charts -->
						<tr> 	    

						<!-- YTD scan and publish pie charts here -->
						<td style="padding-left: 6px;">
							<!-- /Goal Actual Pie Charts -->
							<table  class="mainRightRowHeader" style="margin-bottom:-44px;"> 
								<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">YTD Scan / Goal</h4></td></tr>
						 	</table>
							<table>
							<tr>
								<td align="center" >
									<!--Extra dummy Divs that will hold the pie chart-->
									<c:forEach var="i" begin="0" end="${ytdPiesCount}">
										<div id="ytd_scan_div${i}" style="margin-top: 48px; mxargin-right:-30px; "></div>
									</c:forEach>
				  				
								</td>
							</tr>
							</table>
							
							
							
										
							<!-- Quality -->
							<table  class="mainRowHeader" style="width: 387px; margin-top: 20px;" >
							<tr>
						 		<td>
						 				<h4 style="text-align:center; margin-bottom: 8px;">Quality (date-range)</h4>
						 			</td>
				              	</tr>
							 
						 	</table>
							<table>
							<tr class="secondaryRowHeader">
								<td style="width: 158px;"></td>
								<td align="center" class="secondaryTd">Actual</td>
								<td align="center" class="secondaryTd">Trend</td>
								<td align="center" class="arrowTdHeader"><span class="rArrow">&#8593;</span><span class="gArrow">&#8595; &nbsp;</span></td>
							</tr>
							
							<tr>
							<td class="bodyTd"  width='158'>${quala0}</td>
							<td class="bodyTd"  width='80'>${quala1} </td>
							<td class="bodyTd"  align="center"><span class="fhcSparkClassQualA"></span> </td>
							<td class="arrowTd" ><c:if test="${quala3=='up'}"><span class="rArrow">&#8593;</span></c:if>
									<c:if test="${quala3=='down'}"><span class="gArrow">&#8595;</span></c:if>
									<c:if test="${quala3=='equal'}"><span class="gDash">&#8669;</span></c:if>
							</td>
							</tr>
								
							<tr>
							<td class="bodyTd"  width='158'>${qualb0}</td>
							<td class="bodyTd"  width='80'>${qualb1} </td>
							<td  class="bodyTd" align="center"><span class="fhcSparkClassQualB"></span> </td>
							<td class="arrowTd" ><c:if test="${qualb3=='up'}"><span class="rArrow">&#8593;</span></c:if>
									<c:if test="${qualb3=='down'}"><span class="gArrow">&#8595;</span></c:if>
									<c:if test="${qualb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
							</td>
							</tr>		
							
							<tr>
							<td class="bodyTd"  width='158'>${qualc0}</td>
							<td class="bodyTd"  width='80'>${qualc1} </td>
							<td class="bodyTd" align="center"><span class="fhcSparkClassQualC"></span> </td>
							<td class="arrowTd" ><c:if test="${qualc3=='up'}"><span class="rArrow">&#8593;</span></c:if>
									<c:if test="${qualc3=='down'}"><span class="gArrow">&#8595;</span></c:if>
									<c:if test="${qualc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
							</td>
							</tr>
							</table>
			            
			            
			            
			            	<!-- Image Auditor Averages -->
			            	<div style="height: 33px;"></div>
							<table  class="mainRightRowHeader" > 
								<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Image Auditor Averages (date-range)</h4></td></tr>
						 	</table>
							<table>
							<tr class="secondaryRowHeader">
								<td align="center" class="secondaryTd"></td>
								<td align="center" class="secondaryTd">Avg/Auditor</td>
								<td align="center" class="secondaryTd">Trend</td>
								<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
							</tr>
							
							<tr>
							<td class="bodyTd"  width='158'>${audita0}</td>
							<td class="bodyTd"  width='82'>${audita1} </td>
							<td  class="bodyTd" align="center"><span class="fhcSparkClassAuditA"></span> </td>
							<td class="arrowTd" ><c:if test="${audita3=='up'}"><span class="gArrow">&#8593;</span></c:if>
									<c:if test="${audita3=='down'}"><span class="rArrow">&#8595;</span></c:if>
									<c:if test="${audita3=='equal'}"><span class="gDash">&#8669;</span></c:if>
							</td>
							</tr>
							<tr>
							<td class="bodyTd"  width='158'>${auditb0}</td>
							<td class="bodyTd"  width='82'>${auditb1} </td>
							<td  class="bodyTd" align="center"><span class="fhcSparkClassAuditB"></span> </td>
							<td class="arrowTd" ><c:if test="${auditb3=='up'}"><span class="gArrow">&#8593;</span></c:if>
									<c:if test="${auditb3=='down'}"><span class="rArrow">&#8595;</span></c:if>
									<c:if test="${auditb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
							</td>
						 	</tr>
							<tr>
							<td class="bodyTd"  width='158'>${auditc0}</td>
							<td class="bodyTd"  width='82'>${auditc1} </td>
							<td  class="bodyTd" align="center"><span class="fhcSparkClassAuditC"></span> </td>
							<td class="arrowTd" ><c:if test="${auditc3=='up'}"><span class="gArrow">&#8593;</span></c:if>
									<c:if test="${auditc3=='down'}"><span class="rArrow">&#8595;</span></c:if>
									<c:if test="${auditc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
							</td>
							</tr>
							<tr>
							<td class="bodyTd"  width='158'>${auditd0}</td>
							<td class="bodyTd"  width='82'>${auditd1} </td>
							<td  class="bodyTd" align="center"><span class="fhcSparkClassAuditD"></span> </td>
							<td class="arrowTd" ><c:if test="${auditd3=='up'}"><span class="gArrow">&#8593;</span></c:if>
									<c:if test="${auditd3=='down'}"><span class="rArrow">&#8595;</span></c:if>
									<c:if test="${auditd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
							</td>
							</tr>
							</table>
			
			
				
							<!-- future charts here -->
				
						</td>
									
			
					
						<!-- percent completed of scanned piecharts here -->
						<td style="padding-left: 6px;">
						<!-- /Goal Actual Pie Charts -->
						<table  class="mainRightRowHeader"  style="margin-bottom:-44px;">  
							<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">YTD Publish / Goal</h4></td></tr>
					 	</table>
						<table>
						<tr>
							<td align="center" >
								<!--Div that will hold the pie chart-->
								<c:forEach var="i" begin="0" end="${ytdPiesCount}">
									<div id="ytd_publish_div${i}" style="margin-top: 48px; mxargin-right:-30px; "></div>
								</c:forEach>
			  					 
			  				
							</td>
						</tr>
						</table>
					
					
					
						<!-- Top 5 Image Quality Issues -->
						<table  class="mainRightRowHeader"  style="width:400px; margin-top: 20px;" >
							<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Top 5 Image Quality Issues (date-range)</h4></td></tr>
					 	</table>
						<table  style="width:400px;">
						<tr class="secondaryRowHeader">
							<td align="center" class="secondaryTd">Issue</td>
							<td align="center" class="secondaryTd">%</td>
							<td align="center" class="secondaryTd">Trend</td>
							<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
						</tr>
						
						<tr>
						<td class="bodyTd"  width='158'>${topa0}</td>
						<td class="bodyTd" align="center" width='82'>${topa1} </td>
						<td  class="bodyTd" align="center"><span class="fhcSparkClassTopA"></span> </td>
						<td class="arrowTd" ><c:if test="${topa3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${topa3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${topa3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
						</tr>
						<tr>
						<td class="bodyTd"  width='158'>${topb0}</td>
						<td class="bodyTd"  align="center" width='82'>${topb1} </td>
						<td  class="bodyTd" align="center"><span class="fhcSparkClassTopB"></span> </td>
						<td class="arrowTd" ><c:if test="${topb3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${topb3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${topb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
					 	</tr>
						<tr>
						<td class="bodyTd"  width='158'>${topc0}</td>
						<td class="bodyTd"  align="center" width='82'>${topc1} </td>
						<td  class="bodyTd" align="center"><span class="fhcSparkClassTopC"></span> </td>
						<td class="arrowTd" ><c:if test="${topc3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${topc3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${topc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
						</tr>
						<tr>
						<td class="bodyTd"  width='158'>${topd0}</td>
						<td class="bodyTd" align="center" width='82'>${topd1} </td>
						<td  class="bodyTd" align="center"><span class="fhcSparkClassTopD"></span> </td>
						<td class="arrowTd" ><c:if test="${topd3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${topd3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${topd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
						</tr>
						<tr>
						<td class="bodyTd"  width='158'>${tope0}</td>
						<td class="bodyTd" align="center" width='82'>${tope1} </td>
						<td  class="bodyTd" align="center"><span class="fhcSparkClassTopE"></span> </td>
						<td class="arrowTd" ><c:if test="${tope3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${tope3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${tope3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
						</tr>
						 
						</table>
						
			
			
			
						<!-- Turnaround Time -->
						<table  class="mainRightRowHeader" style="width: 400px;"> 
							<tr><td><h4 style="text-align:center; maxrgin-bottom: 0px;">Turnaround Time (date-range)</h4></td></tr>
					 	</table>
						<table style="width:400px;">
						<tr class="secondaryRowHeader">
							<td style="width:100px;"></td>
							<td align="center" class="secondaryTd" style="width:100px;">Avg time (days) in each phase</td>
							<td align="center" class="secondaryTd"  style="width:100px;">Trend</td>
							<td align="center" class="arrowTdHeader"><span class="gArrow">&#8593;</span><span class="rArrow">&#8595; &nbsp;</span></td>
						</tr>
						
						<tr>
						<td class="bodyTd2"  >${turnarounda0}</td>
						<td class="bodyTd2"  >${turnarounda1} </td>
						<td  class="bodyTd2" align="center"><span class="fhcSparkClassTurnaroundA"></span> </td>
						<td class="arrowTd" ><c:if test="${turnarounda3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${turnarounda3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${turnarounda3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
						</tr>
						<tr>
						<td class="bodyTd2"  >${turnaroundb0}</td>
						<td class="bodyTd2"   >${turnaroundb1} </td>
						<td  class="bodyTd2" align="center"><span class="fhcSparkClassTurnaroundB"></span> </td>
						<td class="arrowTd" ><c:if test="${turnaroundb3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${turnaroundb3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${turnaroundb3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
					 	</tr>
						<tr>
						<td class="bodyTd2"   >${turnaroundc0}</td>
						<td class="bodyTd2"  >${turnaroundc1} </td>
						<td  class="bodyTd2" align="center"><span class="fhcSparkClassTurnaroundC"></span> </td>
						<td class="arrowTd" ><c:if test="${turnaroundc3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${turnaroundc3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${turnaroundc3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
						</tr>
						<tr>
						<td class="bodyTd2"   >${turnaroundd0}</td>
						<td class="bodyTd2"  >${turnaroundd1} </td>
						<td  class="bodyTd2" align="center"><span class="fhcSparkClassTurnaroundD"></span> </td>
						<td class="arrowTd" ><c:if test="${turnaroundd3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${turnaroundd3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${turnaroundd3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
						</tr>
							<tr>
						<td class="bodyTd2"  >${turnarounde0}</td>
						<td class="bodyTd2"  >${turnarounde1} </td>
						<td  class="bodyTd2" align="center"><span class="fhcSparkClassTurnaroundE"></span> </td>
						<td class="arrowTd" ><c:if test="${turnarounde3=='up'}"><span class="rArrow">&#8593;</span></c:if>
								<c:if test="${turnarounde3=='down'}"><span class="gArrow">&#8595;</span></c:if>
								<c:if test="${turnarounde3=='equal'}"><span class="gDash">&#8669;</span></c:if>
						</td>
						</tr>			
						</table>
						
						
			
						<!-- future charts here -->
						
						</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
		</form>
    </div>
 
  </div>
</div>
</div>
			  

		

</jsp:body>
</tags:template>