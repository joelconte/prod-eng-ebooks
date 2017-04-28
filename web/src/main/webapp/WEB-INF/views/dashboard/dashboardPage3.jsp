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
		width: 700px;
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
 
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
//combo chart using google charts bar chart for monthly numbers - https://developers.google.com/chart/
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawVisualization);


      function drawVisualization() {
        // Some raw data (not necessarily accurate)
        
        var horizontalLineData1 = '<c:out value="${horizontalLineData1}" escapeXml="false" />';//these will be 2-dim array string, or 'none'
        var horizontalLineData2 = '<c:out value="${horizontalLineData2}" escapeXml="false" />';
      
       
         horizontalLineData1 = [
         ['xMonth', 'Total Views',   'Unique books viewed'],
         ['<c:out value="${past12Months.get(0)}"/>',  <c:out value="${past12MonthsViews.get(0)}"/>, <c:out value="${past12MonthsUnique.get(0)}"/>],
         ['<c:out value="${past12Months.get(1)}"/>',  <c:out value="${past12MonthsViews.get(1)}"/>, <c:out value="${past12MonthsUnique.get(1)}"/>],
         ['<c:out value="${past12Months.get(2)}"/>',  <c:out value="${past12MonthsViews.get(2)}"/>, <c:out value="${past12MonthsUnique.get(2)}"/>],
         ['<c:out value="${past12Months.get(3)}"/>',  <c:out value="${past12MonthsViews.get(3)}"/>, <c:out value="${past12MonthsUnique.get(3)}"/>],
         ['<c:out value="${past12Months.get(4)}"/>',  <c:out value="${past12MonthsViews.get(4)}"/>, <c:out value="${past12MonthsUnique.get(4)}"/>],
         ['<c:out value="${past12Months.get(5)}"/>',  <c:out value="${past12MonthsViews.get(5)}"/>, <c:out value="${past12MonthsUnique.get(5)}"/>],
         ['<c:out value="${past12Months.get(6)}"/>',  <c:out value="${past12MonthsViews.get(6)}"/>, <c:out value="${past12MonthsUnique.get(6)}"/>],
         ['<c:out value="${past12Months.get(7)}"/>',  <c:out value="${past12MonthsViews.get(7)}"/>, <c:out value="${past12MonthsUnique.get(7)}"/>],
         ['<c:out value="${past12Months.get(8)}"/>',  <c:out value="${past12MonthsViews.get(8)}"/>, <c:out value="${past12MonthsUnique.get(8)}"/>],
         ['<c:out value="${past12Months.get(9)}"/>',  <c:out value="${past12MonthsViews.get(9)}"/>, <c:out value="${past12MonthsUnique.get(9)}"/>],  
         ['<c:out value="${past12Months.get(10)}"/>',  <c:out value="${past12MonthsViews.get(10)}"/>, <c:out value="${past12MonthsUnique.get(10)}"/>],
         ['<c:out value="${past12Months.get(11)}"/>',  <c:out value="${past12MonthsViews.get(11)}"/>, <c:out value="${past12MonthsUnique.get(11)}"/>],
         ['<c:out value="${past12Months.get(12)}"/>',  <c:out value="${past12MonthsViews.get(12)}"/>, <c:out value="${past12MonthsUnique.get(12)}"/>]
     	 ]; 
         
         horizontalLineData2 = [
                                ['xYear', '<c:out value="${years.get(0)}"/>', '<c:out value="${years.get(1)}"/>', '<c:out value="${years.get(2)}"/>','<c:out value="${years.get(3)}"/>',   '<c:out value="${years.get(4)}"/>'],
                                ['Jan',  <c:out value="${yearMinus4Views.get(0)}"/>,  <c:out value="${yearMinus3Views.get(0)}"/>,  <c:out value="${yearMinus2Views.get(0)}"/>,  <c:out value="${yearMinus1Views.get(0)}"/>,  <c:out value="${yearMinus0Views.get(0)}"/>],
                                ['Feb',  <c:out value="${yearMinus4Views.get(1)}"/>,  <c:out value="${yearMinus3Views.get(1)}"/>,  <c:out value="${yearMinus2Views.get(1)}"/>,  <c:out value="${yearMinus1Views.get(1)}"/>,  <c:out value="${yearMinus0Views.get(1)}"/>],
                                ['Mar',  <c:out value="${yearMinus4Views.get(2)}"/>,  <c:out value="${yearMinus3Views.get(2)}"/>,  <c:out value="${yearMinus2Views.get(2)}"/>,  <c:out value="${yearMinus1Views.get(2)}"/>,  <c:out value="${yearMinus0Views.get(2)}"/>],
                                ['Apr',  <c:out value="${yearMinus4Views.get(3)}"/>,  <c:out value="${yearMinus3Views.get(3)}"/>,  <c:out value="${yearMinus2Views.get(3)}"/>,  <c:out value="${yearMinus1Views.get(3)}"/>,  <c:out value="${yearMinus0Views.get(3)}"/>],
                                ['May',  <c:out value="${yearMinus4Views.get(4)}"/>,  <c:out value="${yearMinus3Views.get(4)}"/>,  <c:out value="${yearMinus2Views.get(4)}"/>,  <c:out value="${yearMinus1Views.get(4)}"/>,  <c:out value="${yearMinus0Views.get(4)}"/>],
                                ['Jun',  <c:out value="${yearMinus4Views.get(5)}"/>,  <c:out value="${yearMinus3Views.get(5)}"/>,  <c:out value="${yearMinus2Views.get(5)}"/>,  <c:out value="${yearMinus1Views.get(5)}"/>,  <c:out value="${yearMinus0Views.get(5)}"/>],
                                ['Jul',  <c:out value="${yearMinus4Views.get(6)}"/>,  <c:out value="${yearMinus3Views.get(6)}"/>,  <c:out value="${yearMinus2Views.get(6)}"/>,  <c:out value="${yearMinus1Views.get(6)}"/>,  <c:out value="${yearMinus0Views.get(6)}"/>],
                                ['Aug',  <c:out value="${yearMinus4Views.get(7)}"/>,  <c:out value="${yearMinus3Views.get(7)}"/>,  <c:out value="${yearMinus2Views.get(7)}"/>,  <c:out value="${yearMinus1Views.get(7)}"/>,  <c:out value="${yearMinus0Views.get(7)}"/>],
                                ['Sep',  <c:out value="${yearMinus4Views.get(8)}"/>,  <c:out value="${yearMinus3Views.get(8)}"/>,  <c:out value="${yearMinus2Views.get(8)}"/>,  <c:out value="${yearMinus1Views.get(8)}"/>,  <c:out value="${yearMinus0Views.get(8)}"/>],
                                ['Oct',  <c:out value="${yearMinus4Views.get(9)}"/>,  <c:out value="${yearMinus3Views.get(9)}"/>,  <c:out value="${yearMinus2Views.get(9)}"/>,  <c:out value="${yearMinus1Views.get(9)}"/>,  <c:out value="${yearMinus0Views.get(9)}"/>],
                                ['Nov',  <c:out value="${yearMinus4Views.get(10)}"/>,  <c:out value="${yearMinus3Views.get(10)}"/>,  <c:out value="${yearMinus2Views.get(10)}"/>,  <c:out value="${yearMinus1Views.get(10)}"/>,  <c:out value="${yearMinus0Views.get(10)}"/>],
                                ['Dec',  <c:out value="${yearMinus4Views.get(11)}"/>,  <c:out value="${yearMinus3Views.get(11)}"/>, <c:out value="${yearMinus2Views.get(11)}"/>,  <c:out value="${yearMinus1Views.get(11)}"/>,  <c:out value="${yearMinus0Views.get(11)}"/>]
                            	 ]; 
 
 		 
 		
     	var data1 = google.visualization.arrayToDataTable(horizontalLineData1);
        var data2 = google.visualization.arrayToDataTable(horizontalLineData2);
     

    var options1 = {
       lxegend: {position: 'none'},
       orientation: 'horizontal',
       title : 'Total Book Downloads: Past 12 Months',
       series: {0: {color: 'blue', type: 'line'}, 1: {color: 'orange', type: 'line'}}
    };
    var options2 = {
    	      xlegend: {position: 'none'},
    	      orientation: 'horizontal',
    	      title : 'Monthly and Yearly Usage Comparison',
    	      series: {0: {color: 'blue', type: 'line'}, 1: {color: 'green', type: 'line'}, 2: {color: 'red', type: 'line'}, 3: {color: 'orange', type: 'line'}, 4: {color: 'yellow', type: 'line'}}
   };
 
    
    
 
    	var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div1'));
        chart.draw(data1, options1);
        var chart = new google.visualization.ComboChart(document.getElementById('google_combo_div2'));
        chart.draw(data2, options2);
       
  }


</script>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     <form id="f1"name="f1" method="post" action="" >
	  	<%@include file="/WEB-INF/views/includes/dashboardMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
		 
		
		
		<table> <tr><td  >
			<table  class="mainRowHeader" >
				<tr>
			 		<td>
			 			<h4 style="text-align:center; margin-bottom: 12px;">Usage and Downloads</h4>
			 		</td>
	            </tr>
		 	</table>
		 	 
		 	
		 	<style>
		 	
				.yearTable th, .yearTable  tr {
    				border: 1px solid black;  padding: 10px;
    				text-align: left;
    				 
				}
				.yearTable  th { font-weight: bold; padding: 5px; text-align: center;}
				.yearTable  td {
    				border: 1px solid black;  
    				text-align: center;
    				padding: 5px;
				}
				.yearTable {   border-collapse: collapse;  padding: 15px;
   					 text-align: left; }

			 
			</style>
			<div id="yearlyTable_div1" style="width: 350px; height: 300px; padding: 20px;">
				<table  class="yearTable" >
					<tr>
					<th></th>
					<th style="width: 95px;">Total</th>
					<th style="width: 222px;">Monthly Avg</th>
					</tr>
					<tr>
						<td style="font-weight: bold;"><c:out value='${y1}'/></td>
						<td><c:out value='${total1}'/></td>
						<td><c:out value='${monthlyAvg1}'/></td>
					</tr>
					<tr>
						<td style="font-weight: bold;"><c:out value='${y2}'/></td>
						<td><c:out value='${total2}'/></td>
						<td><c:out value='${monthlyAvg2}'/></td>
					</tr>
					<tr>
						<td style="font-weight: bold;"><c:out value='${y3}'/></td>
						<td><c:out value='${total3}'/></td>
						<td><c:out value='${monthlyAvg3}'/></td>
					</tr>
					<tr>
						<td style="font-weight: bold;"><c:out value='${y4}'/></td>
						<td><c:out value='${total4}'/></td>
						<td><c:out value='${monthlyAvg4}'/></td>
					</tr>
					<tr>
						<td style="font-weight: bold;"><c:out value='${y5}'/></td>
						<td><c:out value='${total5}'/></td>
						<td><c:out value='${monthlyAvg5}'/></td>
					</tr>
				</table>
			</div>
			
			
			<div id="google_combo_div1" style="width: 450px; height: 300px;"></div>
		</td>
		
		<!-- large graph on right side-->
		 
		 <td  >
			<table  class="mainRightRowHeader" >
				<tr>
			 		<td>
			 			<h4 style="text-align:center; margin-bottom: 12px;">Usage Comparison</h4>
			 		</td>
	            </tr>
		 	</table>
		 	
		 	
			
		 	<div id="google_combo_div2" style="width: 750px; height: 500px;"></div>
		 	
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