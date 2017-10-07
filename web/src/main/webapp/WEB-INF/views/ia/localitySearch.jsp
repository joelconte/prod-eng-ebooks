<%@include file="/WEB-INF/views/includes/init.jsp"%>
<tags:template>
<jsp:body>



<!-- adding this version of jquery for calendar -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
<script>

function doLocalitySearch1(){
	 
	var u = document.URL;
	var i = u.indexOf('/ia/');
	u = u.substring(0, i) + "/ia/localitySearch";//"http://localhost:8180/BookScan-web/ia/localitySearch"
	var request = $.ajax({
	  url: u,
	  type: "POST",
	  data: {"localityNumber" : "1"},
	  dataType: "html"
	});

	request.done(function(data) {
	    //alert(data);
	    
	    var select = document.getElementById("locality1");

	    var parser = new DOMParser();
	    var xmlDoc = parser.parseFromString(data,"text/xml");
//////////
/*
var x = xmlDoc.getElementsByTagName("locality")[0].childNodes[0].nodeValue;
var xattrId = xmlDoc.getElementsByTagName("locality")[0].getAttribute('id');
var xattrVal = xmlDoc.getElementsByTagName("locality")[0].getAttribute('value');
alert(x);*/
///////////
	    var xmlArray = xmlDoc.getElementsByTagName("locality");//.childNodes[0].nodeValue;
	    
	    for(var i = 0; i < xmlArray.length; i++) {
	        var option = document.createElement('option');
	        option.value = xmlArray[i].getAttribute('id');
	        option.text = xmlArray[i].getAttribute('value');
	        select.add(option, i);
	    }
	});

	request.fail(function(jqXHR, textStatus) {
	  alert( "Request failed: " + textStatus );
	});
}


function doLocalitySearchX(localityNumber, selectedValue){
	//alert(selectedValue); 
	$("body").css("cursor", "wait");
	var u = document.URL;
	var i = u.indexOf('/ia/');
	u = u.substring(0, i) + "/ia/localitySearch";//"http://localhost:8180/BookScan-web/ia/localitySearch"
	var request = $.ajax({
	  url: u,
	  type: "POST",
	  data: {"localityNumber" : localityNumber, "id" : selectedValue},
	  dataType: "html"
	});

	request.done(function(data) {
	    //alert(data);
	    
	    var select = document.getElementById(localityNumber);

	    var parser = new DOMParser();
	    var xmlDoc = parser.parseFromString(data,"text/xml");
 
	    
	    
	    //////////////////
	    $(data).find('locality').each(function() {
            var $loc = $(this);
            var id = $loc.attr('id');
            var value = $loc.attr('value');
            var option = document.createElement('option');
	        option.value = id;
	        option.text = value;
	        select.add(option);//, i);
  	    });

	    ///////////////////
	    
	  /*  
	    var xmlArray = xmlDoc.getElementsByTagName("locality");//.childNodes[0].nodeValue;
	    
	    for(var i = 0; i < xmlArray.length; i++) {
	        var option = document.createElement('option');
	        option.value = xmlArray[i].getAttribute('id');
	        option.text = xmlArray[i].getAttribute('value');
	        select.add(option, i);
	    }*/
	    $("body").css("cursor", "default");
	});

	request.fail(function(jqXHR, textStatus) {
		$("body").css("cursor", "default");
	  	alert( "Request failed: " + textStatus );
	});
	
	//clear out dropdowns below this level: selectedValue
	if(localityNumber == "locality2"){
		var searchKeyword = document.getElementById("searchKeyword");
		var e = document.getElementById("locality1");
		var currentValueSelected = e.options[e.selectedIndex].text;
		searchKeyword.value = currentValueSelected;
		
		var mySelect = document.getElementById('locality2');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality3');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality4');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality5');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality6');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality7');
		mySelect.options.length = 0;
		 
	}else if(localityNumber  == "locality3"){
		var searchKeyword = document.getElementById("searchKeyword");
		var e = document.getElementById("locality2");
		var currentValueSelected = e.options[e.selectedIndex].text;
		searchKeyword.value = currentValueSelected;
		
		var mySelect = document.getElementById('locality3');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality4');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality5');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality6');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality7');
		
	}else if(localityNumber == "locality4"){
		var searchKeyword = document.getElementById("searchKeyword");
		var e = document.getElementById("locality3");
		var currentValueSelected = e.options[e.selectedIndex].text;
		searchKeyword.value = currentValueSelected;
		
		var mySelect = document.getElementById('locality4');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality5');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality6');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality7');
		mySelect.options.length = 0;
		
	}else if(localityNumber == "locality5"){
		var searchKeyword = document.getElementById("searchKeyword");
		var e = document.getElementById("locality4");
		var currentValueSelected = e.options[e.selectedIndex].text;
		searchKeyword.value = currentValueSelected;
		
		var mySelect = document.getElementById('locality5');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality6');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality7');
		mySelect.options.length = 0;
		
		
	}else if(localityNumber == "locality6"){
		var searchKeyword = document.getElementById("searchKeyword");
		var e = document.getElementById("locality5");
		var currentValueSelected = e.options[e.selectedIndex].text;
		searchKeyword.value = currentValueSelected;
		
		var mySelect = document.getElementById('locality6');
		mySelect.options.length = 0;
		var mySelect = document.getElementById('locality7');
		mySelect.options.length = 0;
	}else if(localityNumber  == "locality7"){
		var searchKeyword = document.getElementById("searchKeyword");
		var e = document.getElementById("locality6");
		var currentValueSelected = e.options[e.selectedIndex].text;
		searchKeyword.value = currentValueSelected;
		
		var mySelect = document.getElementById('locality7');
		mySelect.options.length = 0;
	}
}

function doIaSearch(){
	rc = confirm("A search of archive.org will now be performed.  \nResults will be shown in step '2- Select Books' for further review. \n\nAny existing books you are working on in '2- Select Books' will be removed in order to process this new search.");
	if(rc != true){
		return;
	}
	var searchKeyword = document.getElementById("searchKeyword").value;

	var url = "iaDoSearch?searchKeyword=" + searchKeyword;//in IaSearchController
	window.location.href=url;
}

</script>


<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">

	  <%@include file="/WEB-INF/views/includes/iaMenu.html"%>
      <h1 class="serif">${pageTitle}</h1>
	    <security:authorize access="hasAnyRole('ia', 'ia_admin', 'admin')">  
		<sf:form id="f1" style="margin: 0 0 60px 0;" class="" name="localitySearch"  onsubmit=""  method="post" action="" >
			<div class="trackingFormTop1">
		 		<button id="startLocalitySearch" name="startLocalitySearch" value="startLocalitySearch" onclick="doLocalitySearch1(); return false;">${messages['ia.startLocalitySearch']}</button>
		 	</div>
		 		
		 	<table>
				<tr>
					<td style="padding-top: 22px; padding-right: 11px">${messages['ia.locality1']}</td>
					<td><select id="locality1" style="width: 600px;" name="locality1" onchange="doLocalitySearchX('locality2', this.value); return false;"  ></select>
					 
					</td>
				</tr>
				<tr>
					<td>${messages['ia.locality2']}</td>
					<td><select id="locality2"  style="width: 600px;"   name="locality2" onchange="doLocalitySearchX('locality3', this.value); return false;"  ></select>
					 
					</td>
				</tr>
				
				<tr>
					<td>${messages['ia.locality3']}</td>
					<td><select id="locality3"  style="width: 600px;"   name="locality3" onchange="doLocalitySearchX('locality4', this.value); return false;"  ></select>
					 
					</td>
				</tr>
				<tr>
					<td>${messages['ia.locality4']}</td>
					<td><select id="locality4"   style="width: 600px;"  name="locality4" onchange="doLocalitySearchX('locality5', this.value); return false;"  ></select>
					 
					</td>
				</tr>
				<tr>
					<td>${messages['ia.locality5']}</td>
					<td><select id="locality5"   style="width: 600px;"  name="locality5" onchange="doLocalitySearchX('locality6', this.value); return false;"  ></select>
					 
					</td>
				</tr>
				<tr>
					<td>${messages['ia.locality6']}</td>
					<td><select id="locality6"  style="width: 600px;"  name="locality6" onchange="doLocalitySearchX('locality7', this.value); return false;"  ></select>
					 
					</td>
				</tr>
				<tr>
					<td>${messages['ia.locality7']}</td>
					<td><select id="locality7"  style="width: 600px;"  name="locality7" onchange="doLocalitySearchX('locality8', this.value); return false;"  ></select>
					 
					</td>
				</tr>
				<tr>
					<td>${messages['ia.searchKeyword']}</td>
					<td><input type="text" id="searchKeyword"  style="width: 600px;"  name="searchKeyword" ></select>
					 
					</td>
				</tr>
				
			</table>	 
			
			
				 
				<div class="trackingFormBottom1" style="padding-top: 22px;">
		 			<button id="startIaSearch" name="startIaSearch" value="startIaSearch" onclick="doIaSearch(); return false;">${messages['ia.startIaSearch']}</button>
		 		</div>
					
		 	
		 
		</sf:form>
	    </security:authorize>
	    <security:authorize access="hasAnyRole('ia', 'ia_admin', 'admin') == false">
 			<p>${messages['ia.notAuthorized']}</p>
 		</security:authorize>	
		      
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>