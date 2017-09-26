
 /* updates myForm's action to be the same URL as myLink - removed buttons on switchboard, so not used anymore*/
function updateAction(myForm, myLink) {
	var myLinkElem = document.getElementById(myLink);
	var myFormElem = document.getElementById(myForm);

	myFormElem.action = myLinkElem.href;
}

function fetchProblem(selectId, mode, tn){

	var sel= document.getElementById(selectId);
	var pn = sel.options[sel.selectedIndex].text;

	var url = "problemsForm?" + mode + "&tn=" + tn + "&pn=" + pn; //mode read,update,create
	window.location.href=url;
	 
}

function fetchBook(selectId, mode){

	var sel= document.getElementById(selectId);
	var tn = sel.options[sel.selectedIndex].text;
	
	//replace & with %26
	if(tn.indexOf("&") != -1){
		tn = tn.replace("&", "%26");
	}
	
	var url = "trackingForm?" + mode + "&tn=" + tn; //mode read,update,create
	window.location.href=url;
	 
}
function fetchBook2(textId, mode){
 
	var sel= document.getElementById(textId);
	var tn = sel.value;
	
	//replace & with %26
	if(tn.indexOf("&") != -1){
		tn = tn.replace("&", "%26");
	}
	var url = "trackingForm?" + mode+ "&tn=" + tn; //mode read,update,create
	window.location.href=url;
}
    


function fetchAllTns(textId, mode){
	var sel= document.getElementById(textId);
	var tn = sel.value;
	
	//replace & with %26
	if(tn.indexOf("&") != -1){
		tn = tn.replace("&", "%26");
	}
	
	if(tn != "")
		var url = "trackingForm?" + mode + "&" + "fetchAllTns=true&tn=" + tn; //mode read,update,create
	else
		var url = "trackingForm?" + mode + "&" + "fetchAllTns=true"; //mode read,update,create
	window.location.href=url;
}

function fetchAllTnsMetadata(textId, mode){
	var sel= document.getElementById(textId);
	var tn = sel.value;
	
	//replace & with %26
	if(tn.indexOf("&") != -1){
		tn = tn.replace("&", "%26");
	}
	
	if(tn != "")
		var url = "metadataForm?" + mode + "&" + "fetchAllTns=true&tn=" + tn; //mode read,update,create
	else
		var url = "metadataForm?" + mode + "&" + "fetchAllTns=true"; //mode read,update,create
	window.location.href=url;
	
}

function fetchBookMetadata(selectId, mode){

	var sel= document.getElementById(selectId);
	var tn = sel.options[sel.selectedIndex].text;

	//replace & with %26
	if(tn.indexOf("&") != -1){
		tn = tn.replace("&", "%26");
	}
	
	var url = "metadataForm?" + mode + "&titleno=" + tn; //mode read,update,create
	window.location.href=url;
	 
}
function fetchBook2Metadata(textId, mode){

	var sel= document.getElementById(textId);
	var tn = sel.value;
	
	//replace & with %26
	if(tn.indexOf("&") != -1){
		tn = tn.replace("&", "%26");
	}
	
	var url = "metadataForm?" + mode+ "&titleno=" + tn; //mode read,update,create
	window.location.href=url;
}
    
function fetchUser(selectId, mode){

	var sel= document.getElementById(selectId);
	var user = sel.options[sel.selectedIndex].text;

	var url = "userAdmin?" + mode + "&userId=" + user; //mode read,update,create
	window.location.href=url;
}
function fetchSite(selectId, mode){

	var sel= document.getElementById(selectId);
	var site = sel.options[sel.selectedIndex].text;

	var url = "siteAdmin?" + mode + "&siteId=" + site; //mode read,update,create
	window.location.href=url;
}


function fetchSearch(selectId, mode){

	var sel= document.getElementById(selectId);
	var search = sel.options[sel.selectedIndex].text;


	var url = "searchMisc?" + mode + "&searchId=" + search; //mode read,update,create
	window.location.href=url;
}

function hasReturnTo(){
	if( getUrlParm("returnTo")!= null && getUrlParm("returnTo")!= "")
		return true;
	else
		return false;
}


function goBackToMetadata( ){
	//var el = document.getElementById(selectId);
	//var tn = el.options[el.selectedIndex].text;//select box tn
	var f = document.getElementById("f1"); //form
	var url = "metadataNewBooks";
	 
	f.action = url;
}
function goBackToInternetArchiveMetadata( ){
	//var el = document.getElementById(selectId);
	//var tn = el.options[el.selectedIndex].text;//select box tn
	var f = document.getElementById("f1"); //form
	var url = "metadataInternetArchiveNewBooks";
	 
	f.action = url;
}

//update action on form formId with mode and page url - (use to update url on form right before button click submits it) 
function updateUrl2(formId, page, mode){
	 	
	//var el = document.getElementById(selectId);
	//var tn = el.options[el.selectedIndex].text;//select box tn
	var f = document.getElementById(formId); //form
	var url = page + "?" + mode;//tn already in form data    + "&tn=" + tn; //mode read,update,create
	if( getUrlParm("returnTo")!= null &&  getUrlParm("returnTo")!= "")
		url = url + "&returnTo=" + getUrlParm("returnTo");
	f.action = url;
}
  
 
	
	
	
//update action on form formId with mode and page url - (use to update url on form right before button click submits it)  and udate target attr for new/same window
function updateUrlAndTarget(formId, page, mode, target){
	updateUrl2(formId, page, mode);
	var f = document.getElementById(formId); //form
	f.target = target;
}

//update action on form formId with mode and page url - (use to update url on form right before button click submits it) 
function updateUrlForReport(formId, page, button){
	//var el = document.getElementById(selectId);
	//var tn = el.options[el.selectedIndex].text;//select box tn
	var f = document.getElementById(formId); //form
	var url = page + "?" + button;//button used to key on which request method to call
	

	//get dropdown selected values
	var year =  document.getElementById('year'); //form
	var month =  document.getElementById('month'); //form
	var scannedBy =  document.getElementById('scannedBy'); //form
	var yearVal = year.options[year.selectedIndex].text;
	var monthVal = month.options[month.selectedIndex].text;
	var scannedByVal = scannedBy.options[scannedBy.selectedIndex].text;
	
	
	url = url + "&year=" + yearVal  + "&month=" + monthVal  + "&scannedBy=" + scannedByVal;
	f.action = url;
}
 

//update action on form formId with mode and page url - (use to update url on form right before button click submits it)  and udate target attr for new/same window
function updateUrlAndTargetForReport(formId, page, button, target){
	updateUrlForReport(formId, page, button);
	var f = document.getElementById(formId); //form
	f.target = target;
}

function getUrlParm( name ){
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");  
	var regexS = "[\\?&]"+name+"=([^&#]*)";  
	var regex = new RegExp( regexS );  
	var results = regex.exec( window.location.href ); 
	if( results == null )
		return "";  
	else    
		return results[1];
}
 

function currentTimestamp(id){
	var cdStr = getCurrentTimestamp();
	var elem = document.getElementById(id);
	elem.value = cdStr;
	return false;
}

function getCurrentTimestamp(){
	var cd = new Date();
	 
	var mon = cd.getMonth()+1;
	var day = cd.getDate();
	var year = cd.getFullYear();
	var hours = cd.getHours();
	var mins = cd.getMinutes();
	
	if(mins < 10)
		mins = "0" + mins;
	
	var ampm;
	if(hours >= 12) 
		ampm = "pm";
	else  
		ampm = "am";
	
	if(hours == 0)
		hours = "12";
	else if(hours >= 13)
		hours = hours - 12;
	
	var cdStr = mon + "/" + day + "/" + year + " " + hours + ":" + mins + " " + ampm;
	return cdStr;
}
function selectAllCheckboxes(checkAll){
	var t = document.getElementById(checkAll); //checkall box

    var selected = t.checked;
    // Iterate each checkbox
    $(':checkbox').each(function () {    this.checked = selected; });
	
}

function toggleOverlay(){
	var overlay = document.getElementById('overlay1');
	var specialBox = document.getElementById('specialBox1');
	overlay.style.opacity = .5;
	if(overlay.style.display == "block"){
	overlay.style.display = "none";
	specialBox.style.display = "none";
	} else {
	overlay.style.display = "block";
	specialBox.style.display = "block";
	}
}


function toggleOverlay2(actionRequest){
	var f = document.getElementById('fPasteData'); //form in overlay
	f.action = actionRequest;
	
	toggleOverlay();
}


function doubleCheckSkip(action){
	if(action == "doTnListSkipScanAndProcess")
		var conf = "Warning:  This will move books of TNs entered to the complete state.\n(Fields file_sent_to_orem and date_released will be updated.)";
	else if(action == "doTnListSkipScan")
		var conf = "Warning:  This will move books of TNs entered to the Process queue.\n(Field file_sent_to_orem will be updated.)";
	var doIt = alert(conf);

	return true;
}
 


function setValueInDom(id, value){
	var elem = document.getElementById(id);
	//keep old existing datat if not blank
	if(elem.value == null || elem.value == undefined  || elem.value == '')
		elem.value = value;
	//elem.className += " flickerText"; have to add inline to override other css
	elem.style.cssText="color: blue; border-color: red;  xfont-weight:bold; " +
   " box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(82, 168, 236, 0.6);  " +
   " outline: 0 none;   " ;
};
/*
function highlightInDom(id){
	var elem = document.getElementById(id);
	
	//elem.className += " flickerText"; have to add inline to override other css
	elem.style.cssText="color: blue; border-color: red;  xfont-weight:bold; " +
   " box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(82, 168, 236, 0.6);  " +
   " outline: 0 none;   " ;
};*/
 


function processBookState(){
	//get url (state) + user and then auto-populate and highlight text
	var state = getUrlParm("returnTo");
	if(state == null || state == "")
		return;
	
	var user = document.getElementById("loggedInUser").innerHTML;
	user = user.substring(14);
	
	var scannedByHidden = document.getElementById("scannedByHidden");
	var scannedBy = "";
	if(scannedByHidden != null)
		scannedBy = document.getElementById("scannedByHidden").value;

	
	switch (state) {
		case "/scan/scanReady":
			setValueInDom("scanOperator", user);
			//setValueInDom("scan_start_date", getCurrentTimestamp());
			setValueInDom("scan_start_date", "");
			setValueInDom("scanned_by", scannedBy);//Cathy req
			setValueInDom("scan_machine", "");	
			setValueInDom("scan_num_of_pages", "");	
			break;
		case "/scan/scanInProgress":
	 
			//setValueInDom("scan_complete_date", getCurrentTimestamp());
			setValueInDom("scan_complete_date", "");
			break;
		case "/scan/auditReady":

			
			var scanIaStart1 = document.getElementById("scan_ia_start_date").value;
			if(scanIaStart1==null || scanIaStart1==''){
				setValueInDom("scanImageAuditor", user);
				//setValueInDom("scan_ia_start_date", getCurrentTimestamp());
				setValueInDom("scan_ia_start_date", "");
			}else{
				setValueInDom("scan_ia_complete_date", "");
			}
			
			break;
		case "/scan/auditInProgress":
			//setValueInDom("scanImageAuditor", user);
			//setValueInDom("scan_ia_complete_date", getCurrentTimestamp());
			var scanIaStart2 = document.getElementById("scan_ia_start_date2").value;
			if(scanIaStart2==null || scanIaStart2==''){
				setValueInDom("scanImageAuditor2", user);
				setValueInDom("scan_ia_start_date2", "");
			}else{
				setValueInDom("scan_ia_complete_date2", "");
			}
			break;
		case "/scan/scanProblems":
			break;
		case "/scan/processedReadyForOrem":
			//setValueInDom("files_sent_to_orem", getCurrentTimestamp());
			setValueInDom("files_sent_to_orem", "");
			break;
		case "/process/waitingForFiles":
			//setValueInDom("files_received_by_orem", getCurrentTimestamp());
			setValueInDom("files_received_by_orem", "");
			break;
		case "/process/titleCheck":
			setValueInDom("imageAudit", user);
			//setValueInDom("ia_start_date", getCurrentTimestamp());
			setValueInDom("ia_start_date", "");
			break;
		case "/process/titleCheckInProgress":
		 
			//setValueInDom("ia_complete_date", getCurrentTimestamp());
			setValueInDom("ia_complete_date", "");
			break;
		case "/process/ocrReady":
			setValueInDom("ocrBy", user);
			//setValueInDom("imported_date", getCurrentTimestamp());
			setValueInDom("ocr_start_date", "");
			break;
		case "/process/ocrInProgress":
			//setValueInDom("imported_date", getCurrentTimestamp());
			setValueInDom("ocr_complete_date", "");
			break;
		case "/process/pdfDownload":
			setValueInDom("pdfDownloadBy", user);
			//setValueInDom("pdfDownload_date", getCurrentTimestamp());
			setValueInDom("pdfDownload_date", "");
			break;
		case "/process/ocrPdf":
			setValueInDom("pdfreviewBy", user);
			//setValueInDom("kofax_start_date", getCurrentTimestamp());
			setValueInDom("pdfreview_start_date", "");
			break;
		case "/process/ocrPdfInProgress":
			//setValueInDom("pdf_ready", getCurrentTimestamp());
			setValueInDom("pdf_ready", "");
			break;
	}
}


function doubleCheckDelete(){
	var deleteit = confirm("Are you sure you want to DELETE this book?");
	if(deleteit == false)
		return false;
	else
		return true;
}

//check metadata rows for dupes and prompt user to confirm if he wants to update existing dupe books
function checkForDupesSome(){
	//todo add js code to filter out tns not checked
	//get list of TNs that are checkbox selected
	//var sel= document.getElementById("dupeList");
	var sel= document.getElementById("dupeList");//these are from all on list, not just checked rows
	var tns = sel.value;
	if(tns == "")
		return true;
	
	var updateit = confirm("The following book(s) already exist in the Book table for Scan/Process.  \nIf a book you selected is in this list, it will be updated with this metadata.\n" + tns);
	if(updateit == false)
		return false;
	else
		return true;
}
//check metadata rows for dupes and prompt user to confirm if he wants to update existing dupe books
function checkForDupesAll(){
	var sel= document.getElementById("dupeList");
	var tns = sel.value;
	if(tns == "")
		return true;
	
	var updateit = confirm("The following book(s) already exist in the Book table for Scan/Process.  \nThey will be updated with this metadata.\n" + tns);
	if(updateit == false)
		return false;
	else
		return true;
}


function doubleCheckUpdateTitleCheckNull(){
	var updateit = confirm("This will reset Title Check values to null of books whose TN matches W/O Prefix in this list.  \nAre you sure you want continue?");
	if(updateit == false)
		return false;
	else
		return true;
}

function reloadScanListWithSite(formId, dropdownId){
	var f = document.getElementById(formId); //form
	var site =  document.getElementById(dropdownId);
	var siteStr = site.options[site.selectedIndex].text;
	var url =  "?site=" + siteStr;//current page + site
 
	//document.location.href
	window.location.href=url;
}

function reloadProcessListWithSite(formId, dropdownId){
	reloadScanListWithSite(formId, dropdownId);
}




var tableToExcel = (function() {
	  var uri = 'data:application/vnd.ms-excel;base64,'
	    , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
	    , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
	    , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
	  return function(table, name) {
		  var x = $('#' + table + ' a');
		   
		  $('#' + table + ' a').replaceTag('<p>', true); //paul added to remove hyperlinks
		  
	    if (!table.nodeType) table = document.getElementById(table)
	    var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
	    window.location.href = uri + base64(format(template, ctx))
	  }
	})();
	
$.fn.replaceTag = function (newTagObj, keepProps) {
    $this = this;
    var i, len, $result = jQuery([]), $newTagObj = $(newTagObj);
    len = $this.length;
    for (i=0; i<len; i++) {
        $currentElem = $this.eq(i);
        currentElem = $currentElem[0];
        $newTag = $newTagObj.clone();
        if (keepProps) {//{{{
            newTag = $newTag[0];
            newTag.className = currentElem.className;
            $.extend(newTag.classList, currentElem.classList);
            $.extend(newTag.attributes, currentElem.attributes);
        }//}}}
        $newTag.html(currentElem.innerHTML).replaceAll($currentElem);
        $result.pushStack($newTag);
    }

    return this;
};




function validateDateData(event){
	var elem = event.target;
	var val = elem.value;
	var date = val.split("/");
	var y = parseInt(date[2], 10);

	if(typeof y != "number" || y < 1000 ){
		if(event.relatedTarget != null && event.relatedTarget.className.indexOf("ui-state-default") != -1){
			return;//new date selected from date box popup...hack and use name of trackingForm since this works
		}

		event.preventDefault();
	 	alert( "Please use a four digit year" );
	 	
	 
		if(event.relatedTarget != null && event.relatedTarget.localName != null && event.relatedTarget.localName.indexOf("select") != -1){
		  //event.relatedTarget.close();
			//event.relatedTarget.toggle();//try to close dropdown here
		} 	
		event.relatedTarget.blur();
	 	elem.focus();
	 	elem.click();
	 	 
	}
};


function validateFormData(){
	//if scan date is not null then scannedby must not be null - Jeri request
	var scanSite = $('#scanned_by').val();
	var scanStartDate = $('#scan_start_date').val();
	var scanEndDate = $('#scan_complete_date').val();
	
	if(scanStartDate != '' || scanEndDate != ''  ){
		if( scanSite == ''){
			alert("You must enter a 'Scanned by Site' value because Scan Date or Scan Complete Date have a value.");
			return false;
		}
	}
	
	return true;//valid data
};

function doPost(url){
	var form= document.createElement('form');
	form.method= 'post';
    form.action= url;
 	document.body.appendChild(form);
    form.submit();
}
