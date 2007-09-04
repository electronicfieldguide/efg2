/**
*
*Requires Prototype.js 1.5 and above from the ajax prototype group
*/
var READY_STATE_COMPLETE=4;
var pageids = new Array();
pageids[0] = "choosedataid";
pageids[1] = "querydataid";
pageids[2] = "designformid";
pageids[3] = "saveorprintid";

var colorids = new Array();
colorids[0] = "imageframecolor1id";
colorids[1] = "boundingboxcolor1id";
colorids[2] = "boundingboxcolor2id";
var FORM_SEARCH_ID='form_search_id';

var DATASOURCE_NAME='dataSourceName';
var SEARCH_QUERY_XML_RESULTS='searchqueryresultsxml';
var SEARCH_QUERY_XML_RESULTS_1='searchqueryresultsxml1';
var SEARCH_QUERY='searchquery';




var DELETE_TEMPLATE= "/efg2/plateConfiguration/DeleteTemplate.jsp?";	
		
var PROGRESS_MESSAGE = "Please wait while your requests is processed....";
var NO_TEMPLATE_MESSAGE = "Please select a template or new if configuring a new plate";
var current_template_list;


var ACTIVE_TEXT_CLASS = "menubartextactive";
var IN_ACTIVE_TEXT_CLASS = "menubartext";

var DATASOURCE_MESSAGE='Select a Data source';


var DELETE_TEMPLATE_URL="/efg2/templateJSP/DeleteTemplate.jsp";


var DATASOURCE_NAME="dataSourceName";
var DISPLAY_NAME = "displayName";
var TEMPLATE_ALERT_MESSAGE="Please select a template or enter the name for a new one..";
var DEFAULT_DATA_LIST_VALUE = "[Enter or select a template]";
var FIRST_DIV="firstdiv";
var ZEROTH_DIV="zerothdiv";

var TEMPLATE_UNIQUE_NAME="templateuniquename";
var SECOND_DIV = "seconddiv";
var THIRD_DIV = "thirddiv";
var FOURTH_DIV = "fourthdiv";
var BROWSER_MESSAGE="Browser does not support HTTP Request";
var NUMBER_OF_TAXA = "numberoftaxa";
var TEMP_MAX_NUMBER=400;
var SELECTED_TEMPLATE="selectedTemplate";
var GUID = "guid";
var ALL_TABLE_NAME = "ALL_TABLE_NAME";
var MAIN_TABLE_CONSTANT = "mainTableConstant";
var NUMBER_OF_RESULTS="numberOfResults";
var EFG_SELECTED_MESSAGE ="Place Holder EFG selected message: ";
var TEMPALTE_SELECTED_MESSAGE ="Place Holder For Template selected message: ";
var DELETE_MESSAGE_ID = 'deleteMessageID';
var FAILURE_DELETE_ID = 'failureDeleteID';
var SUCCESS_DELETE_ID = 'successDeleteID';
var query2="";
var objPopUp = null;
var NEW_EFG_TAG='NEW_EFG_TAG';
var POP_UP_MESSAGE = "Please wait while your request is processed..";
/*
* Quote from prototype engine docs.
*"The $() function is a handy shortcut to the all-too-frequent 
*document.getElementById() function of the DOM.."
*/

function changeCheckedValue(checkbox){

	if(checkbox.checked){
		checkbox.value="yes";
	}
	else{
		checkbox.value="no";
	}
	
}
function disableAllSubmitButtons(){
 	var e=document.getElementsByName("submit");

	for(var i=0;i<e.length;i++){
		e[i].disabled = true;
	}
	
	e=document.getElementsByName("button");
	for(var i=0;i<e.length;i++){
		e[i].disabled = true;
	}
}
function enableAllSubmitButtons(){
  	var e=document.getElementsByName("submit");

	for(var i=0;i<e.length;i++){
		e[i].disabled = false;
	}
	e=document.getElementsByName("button");
	for(var i=0;i<e.length;i++){
		e[i].disabled = false;
	}
	
}
/**
* use this for pop up messages
*/

function windowOpener(message) {
   msgWindow=window.open("","MessageWindow","menubar=no,scrollbars=no,status=no,width=300,height=300")
   msgWindow.document.write("<html><head><title>Message window<\/title><\/head><body>")
   msgWindow.document.write(message+ '<br>');
   msgWindow.document.write('<\/body><\/html>');     
   msgWindow.moveTo(0,0)       
}

/**
*
*/
function AddCaptionRow(currenturl,
						spanid,
						captionid,
						numberofcaptionsid){
						
	var currentU = currenturl;
	var caption =  $(captionid);
	var params = caption.name + "=" + caption.value;
 	caption =  $(numberofcaptionsid);
	params = params + "&" + caption.name + "=" + caption.value;
	
	doPostsUpdater(currentU,params,doNothingHandler,spanid,Insertion.Before);	
	enableAllSubmitButtons();	
}
/**
*
*/			
function deleteTemplate(templateid){
	var selectObject =  $(templateid);
	
	var templatename = selectObject.options[selectObject.selectedIndex].value;
	if(templatename == 'NEW_EFG_TAG'){
		alert("Cannot delete New");
		return;
	}
	else{	
		var response = deleteConfirmMsg(templatename);
	
		if(response){
			var guid= $(templatename).value;
			var ds=$(DATASOURCE_NAME).value;
		
			 var url = $('configpagetype').value+ "&" +
		  			DATASOURCE_NAME + "=" + ds + "&" + 
		  			 GUID + "=" + guid + "&" + 
		  			 "templateUniqueName=" + templatename + 
		  			 "&ALL_TABLE_NAME=efg_rdb_tables";
					 
			doPosts(DELETE_TEMPLATE_URL,url,processDeleteQuery);
		}
		else{
		
			return;
		}
	}
}
function processDeleteQuery(loader,obj){
		
		var responseDoc = $(DELETE_MESSAGE_ID);
		responseDoc.innerHTML = loader.responseText;
		//alert(responseDoc.innerHTML );
		var newSuccessDoc = $(SUCCESS_DELETE_ID); 
		if( newSuccessDoc == null){
			var failDoc = $(FAILURE_DELETE_ID).innerHTML; 
			alert(failDoc);
			responseDoc.innerHTML="";
		}
		else{
			alert(newSuccessDoc.innerHTML);
		 	//reload
			responseDoc.innerHTML="";
			var sel = $('statesDS').options[$('statesDS').selectedIndex];
			choosedataResponse(sel);
		}		
}
function choosedataResponse(idSel,jspurl){
	if(idSel.value == null || idSel.value == DATASOURCE_MESSAGE){
		
		window.location.href = jspurl;
		return;
	}
	setDatasourceDisplayName(idSel);
	var url=DATASOURCE_NAME+ "="+$F(DATASOURCE_NAME);
	
	message_id = FIRST_DIV;
	var DATA_LIST_URL = $('datalisturl').value; 
	doPostsUpdater(DATA_LIST_URL,url,stateChangedID,FIRST_DIV); 
}
/**
* Handle after user clicks 'Go' button on datalist page
* nextPageID - The id of the next page
* guidxid - the id of the select box
*/
function datalistResponse(nextPageID,guidxid){

	
	var selectObject =  $(guidxid);
	var url = DATASOURCE_NAME+ "="+$(DATASOURCE_NAME).value;
	if(selectObject != null){
		var selectedVal = selectObject.options[selectObject.selectedIndex].value;
		$('templateUniqueNamex').value =selectedVal;
		$(TEMPLATE_UNIQUE_NAME).value=selectedVal;
		var guid = $(selectedVal);
		
		if(guid != null){
			url = url + "&guid=" + guid.value; 
		}
	}
	
	 //go to design page
	
		
	url=url+"&" + DISPLAY_NAME+ "="+$(DISPLAY_NAME).value;
	url=url+"&"+$(MAIN_TABLE_CONSTANT ).value;

	message_id =SECOND_DIV;
	
	var QUERY_DATA = $('querydata').value;
	doPostsUpdater(QUERY_DATA,url,doNothingHandler,SECOND_DIV); 
	
	actdeactClasses(nextPageID);	
	$('hide').innerHTML ="";	
	$('zerothdiv').innerHTML ="";			
}
/**
* Change the selected values into text messages
*/
function changeDivIntoText(pageID, textValue){
	$(pageID).innerHTML = textValue;
}
/**
* change to next tab
*/
function actdeactClasses(nextPageID){
	for(var i = 0; i < pageids.length; i++){
		var currentID = pageids[i];
		if(currentID == nextPageID){
			$(nextPageID).className = ACTIVE_TEXT_CLASS;
		}
		else{
		
			$(currentID).className = IN_ACTIVE_TEXT_CLASS;
		}
	}

}


/**
* Show the progress of a request
*/
function showProgressMessage(id){			
	popUpHyper(id);
}
/*
* Set the datasource and the displayname fields
*/
function setDatasourceDisplayName(idSel)
{	
	if(idSel.type == 'select-one'){
	 	var text =  idSel.options[idSel.selectedIndex].text;
		var val =  idSel.options[idSel.selectedIndex].value;
		$(DATASOURCE_NAME).value =val;
		$(DISPLAY_NAME).value  =text;	
		
	}
}
/**
* What should happen when a state changes for a particular request
*/
function stateChangedID(loader,obj) 
{ 
	

	//copy the contents of select box into array
	current_template_list = new Object();
	var selects = $('guidx');
	
	for(i = 0; i < selects.options.length; i++){
		current_template_list[i] = selects.options[i]; 
	}
}
/**
* What should happen when a state changes for a particular request
*/
function doNothingHandler(loader,obj) 
{ 
	popHideHyper();
} 

 
   //call from html 
   
var message_id = null;

var myGlobalHandlers = {
	onCreate: function(){
	//search for all buttons and disable them
	 	//$(SUBMIT_BUTTON_ID).disabled = true;
	 	disableAllSubmitButtons();
		showProgressMessage(message_id);
	},

	onComplete: function() {
		if(Ajax.activeRequestCount == 0){
			popHideHyper();	
			enableAllSubmitButtons();
			//search for all buttons and enable them
			//$(SUBMIT_BUTTON_ID).disabled = false;
		}
	}
};

Ajax.Responders.register(myGlobalHandlers);

function reportError(request)
{
  alert("Error " + request.responseText);
   popHideHyper();	
   // $(deleteMessageID).innerHTML = '<div class="error"><b>Error communicating with server. Please try again.</b></div>';
}

function doFormPosts(currenturl,formid,handler){
	
	
  	var loader = new Ajax.Request(
	currenturl, 
	{
		method: 'post', 
		contentType: 'application/x-www-form-urlencoded', 
		encoding:	'UTF-8',
		parameters: Form.serialize($(formid)), 
		onComplete: handler, 
		onFailure: reportError
	});	
}

function doPosts(currenturl,query,handler){
	
	
  	var loader = new Ajax.Request(
	currenturl, 
	{
		method: 'post', 
		contentType: 'application/x-www-form-urlencoded', 
		encoding:	'UTF-8',
		parameters: query, 
		onComplete: handler, 
		onFailure: reportError
	});	
}

function doPostsUpdater(currenturl,query,handler,objectid,insertion){
	var loader1 = new Ajax.Updater(
	{success: objectid},
	currenturl,
	{method: 'post', 
	contentType: 'application/x-www-form-urlencoded', 
	encoding: 'UTF-8', 
	parameters: query,
	insertion: insertion,
	onComplete: handler, 
	onFailure: reportError
	}
	);		
}
function resetGlobalQuery(){
	
	query2='';
}

function stripLastAmp(query){

 var lastindex = query.lastIndexOf('&');

 if(lastindex > -1){
 	return query.substring(0,lastindex);
 }
 else{
 	return query;
 }
}
function postSearch(elementID,nextPageID,formID){
	
  	myTable = $(elementID);
  
  	if(myTable == null){	
  		return;
  	}
  	message_id = FIRST_DIV
	
	window.scrollTo(0,0);
	setSearchQuery(SEARCH_QUERY,Form.serialize($(formID)));
	var SEARCH_URL = $('efgsearchurl').value;
	doFormPosts(SEARCH_URL,formID,processSearchQuery);	
	actdeactClasses(nextPageID);
}
/**
*
*
*/
function processSearchQuery(loader,obj){
	
	var xml = null;
	var taxonEntries = null;
	var taxonEntryCount = null;
	var counter = null;
	var response=loader.responseText;

	//resetGlobalQuery();
	xml = loader.responseXML;
	
	
	
	//store in a hidden field and send with query to server
	//also store query so that if response is not present query will be used
	taxonEntries = loader.responseXML.getElementsByTagName('TaxonEntries')[0];
	
	if(taxonEntries == null){
		alert("Query returned zero results");
		popHideHyper();
		enableAllSubmitButtons();
		return;
	}
	taxonEntryCount = taxonEntries.childNodes.length;
	if(taxonEntryCount == 0){
		alert("Query returned zero results");
		popHideHyper();
		enableAllSubmitButtons();
		return;
	}
	
	counter = taxonEntries.childNodes.length;
	
	//number of taxa
	
	if(counter > 0){
		if(counter > TEMP_MAX_NUMBER){
			alert("Your query returned more than a 100 results. \n" + 
			"Please increase your search criteria and try again");
			enableAllSubmitButtons();
			return;
		}
		else{
			$(NUMBER_OF_TAXA).value = counter + "";
			
			setSearchQuery(SEARCH_QUERY_XML_RESULTS_1,response);
			
			var query = designPageQuery();
			popHideHyper();
		}
	}
	else{
		
		alert("Query returned zero results");
		popHideHyper();
		enableAllSubmitButtons();
		return;
	}
	
}
function getDesignForm(query){
	message_id = SECOND_DIV;
	var DESIGN_FORM_URL = $('designform').value;
	doPostsUpdater(DESIGN_FORM_URL,query,
	processDesignQuery,SECOND_DIV); 
	
}

function processDesignQuery(loader, obj){

	initClear();
	//try{
	//	makeColorSelectors();
	//}
	//catch(e){
	//
	//}
	window.scrollTo(0,0);
}
function saveConfig(myTableId,promptMessage){
	var tempU = $(TEMPLATE_UNIQUE_NAME).value;
	var promptAnswer = callPrompt(tempU,promptMessage);
	
	if(promptAnswer == null){
			
		return;
	}
	else if(promptAnswer == ''){
		
		return;
	}
	else{
		var id=FIRST_DIV;
		message_id = id;
		
		$(TEMPLATE_UNIQUE_NAME).value = promptAnswer;
		showProgressMessage(message_id);
	 	window.scrollTo(0,0);
	 	insertQueryResultsXML();
		var queryToSave = requestPDFSavePage(myTableId);	
		
		
		var searchq = $(SEARCH_QUERY).value;
		searchq=searchq.gsub('&','_EFG_AMP_');
		searchq=searchq.gsub('=','_EFG_EQUALS_');
		queryToSave = queryToSave +"&searchQuery=" + searchq;
		
		doPosts(document.pdfmakerform.action,queryToSave,processSave);
	}
}
function processSave(loader,obj){
	popHideHyper();
	enableAllSubmitButtons();
	
	var response = loader.responseText;
	var is_response_ok=response.indexOf('href');
	/**
	 * Forward to page if response os not just a string
	 */
	if (is_response_ok==-1)
	 { 
		 alert(loader.responseText);
	 }
	else{
		$('downloadMessageID').innerHTML = response;
	  	//$('downloadMessageAppearsHere').innerHTML=response;
	}
}
function requestPDFSavePage(myTableID){

	var parameters = Form.serialize($(myTableID));
 	
	var tempU =$(TEMPLATE_UNIQUE_NAME).value;
	
	
	var paramurl ="templateUniqueName=" + tempU;
	
	var guidN = $(tempU);
	if(guidN != null){
		var guid = guidN.value;
		paramurl = paramurl + "&" + GUID + "=" + guid;
		
	}
	
	paramurl=  paramurl+ "&" + $(MAIN_TABLE_CONSTANT).value;
 	parameters=parameters+
 	"&xslName=xslName.xsl&jsp=defaultJSP.jsp" + 
 	"&isDefault=true&searchType=pdfs&search=search" + 
 	"&savepdf=savepdf&" + paramurl;
 	return parameters;
}
function constructQuery(addDatasourceName){	

	var url = NUMBER_OF_RESULTS+ "=" + $(NUMBER_OF_TAXA).value;
	var sq = $(SEARCH_QUERY).value;
	
	if(sq != null && trim(sq) != ''){
	
		url = url + "&" + sq;
	}
	var tempU = $(TEMPLATE_UNIQUE_NAME).value;
	if(tempU != null && trim(tempU) != ''){
		url = url + "&templateUniqueName=" + tempU;	
		var guidN = $(tempU);
		if(guidN != null){
			var guid = guidN.value;
			url = url + "&" + GUID + "=" + guid;
		}
	}
	url=  url+ "&" + $(MAIN_TABLE_CONSTANT).value;
	if(addDatasourceName){
		url= url + "&" + DATASOURCE_NAME+ "="+$(DATASOURCE_NAME).value;	
		url= url+"&" + DISPLAY_NAME+"="+$(DISPLAY_NAME).value;
	}
	
	return url;
}
function designPageQuery(){

	return getDesignForm(constructQuery(false));
}

function setSearchQuery(objectid,query){
	
	$(objectid).value =query;	
}

function insertQueryResultsXML(){
   
 	if(validateForm()){
 	    window.scrollTo(0,0);
	  	$(SEARCH_QUERY_XML_RESULTS).value = 
	  	$(SEARCH_QUERY_XML_RESULTS_1).value;
	  
		return true;
	}
	else{
	
		return false;
	}
}
/**
*defaultValueToUse - to use for prompt. returns when 
*the user enters something that is not the empty 
*string or cancels
*/
function callPrompt(defaultValueToUse,message){

	var promptAnswer = prompt(message,defaultValueToUse);
	while(trim(promptAnswer) == NEW_EFG_TAG){
		promptAnswer = prompt(message,defaultValueToUse);
	}
	if(promptAnswer == defaultValueToUse ){
		var message = 'The file ' + defaultValueToUse + ' already exists. Replace?';
		var x=window.confirm(message);
		if (x){
			return promptAnswer;	
		}
		else{
			return null;
		}
	}
	else{	
		return promptAnswer;	
	}
}
function popUpHyper(whichLink) {
	objPopTrig = $(whichLink);
	objPopUp = $('popUpMessage');

	objPopUp.innerHTML='<b>' + POP_UP_MESSAGE + '</b>';

	xPos = objPopTrig.offsetLeft;
	yPos = (objPopTrig.offsetTop + objPopTrig.offsetHeight);
	if (xPos + objPopUp.offsetWidth >  document.body.clientWidth) xPos = xPos - objPopUp.offsetWidth;
	if (yPos + objPopUp.offsetHeight >  document.body.clientHeight) yPos = yPos - objPopUp.offsetHeight - objPopTrig.offsetHeight;
	objPopUp.style.left = xPos + 'px';
	objPopUp.style.top = yPos + 'px';
	objPopUp.style.visibility = 'visible';
	$('popUpMessage').innerHTML=POP_UP_MESSAGE;
	
}
function popHideHyper() {
	objPopUp.style.visibility = 'hidden';
	objPopUp = null;
}	

/* 
 * Cross-browser event handling, by Scott Andrew
 */
function addEvent(element, eventType, lamdaFunction, useCapture) {
    if (element.addEventListener) {
        element.addEventListener(eventType, lamdaFunction, useCapture);
        return true;
    } else if (element.attachEvent) {
        var r = element.attachEvent('on' + eventType, lamdaFunction);
        return r;
    } else {
        return false;
    }
}

/* 
 * Kills an event's propagation and default action
 */
function knackerEvent(eventObject) {
    if (eventObject && eventObject.stopPropagation) {
        eventObject.stopPropagation();
    }
    if (window.event && window.event.cancelBubble ) {
        window.event.cancelBubble = true;
    }
    
    if (eventObject && eventObject.preventDefault) {
        eventObject.preventDefault();
    }
    if (window.event) {
        window.event.returnValue = false;
    }
}

/* 
 * Safari doesn't support canceling events in the standard way, so we must
 * hard-code a return of false for it to work.
 */
function cancelEventSafari() {
    return false;        
}

/* 
 * Cross-browser style extraction, from the JavaScript & DHTML Cookbook
 * <http://www.oreillynet.com/pub/a/javascript/excerpt/JSDHTMLCkbk_chap5/index5.html>
 */
function getElementStyle(elementID, CssStyleProperty) {
    var element = document.getElementById(elementID);
    if (element.currentStyle) {
        return element.currentStyle[toCamelCase(CssStyleProperty)];
    } else if (window.getComputedStyle) {
        var compStyle = window.getComputedStyle(element, '');
        return compStyle.getPropertyValue(CssStyleProperty);
    } else {
        return '';
    }
}

/* 
 * CamelCases CSS property names. Useful in conjunction with 'getElementStyle()'
 * From <http://dhtmlkitchen.com/learn/js/setstyle/index4.jsp>
 */
function toCamelCase(CssProperty) {
    var stringArray = CssProperty.toLowerCase().split('-');
    if (stringArray.length == 1) {
        return stringArray[0];
    }
    var ret = (CssProperty.indexOf("-") == 0)
              ? stringArray[0].charAt(0).toUpperCase() + stringArray[0].substring(1)
              : stringArray[0];
    for (var i = 1; i < stringArray.length; i++) {
        var s = stringArray[i];
        ret += s.charAt(0).toUpperCase() + s.substring(1);
    }
    return ret;
}

/*
 * Disables all 'test' links, that point to the href '#', by Ross Shannon
 */
function disableTestLinks() {
  var pageLinks = document.getElementsByTagName('a');
  for (var i=0; i<pageLinks.length; i++) {
    if (pageLinks[i].href.match(/[^#]#$/)) {
      addEvent(pageLinks[i], 'click', knackerEvent, false);
    }
  }
}

/* 
 * Cookie functions
 */
function createCookie(name, value, days) {
    var expires = '';
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        var expires = '; expires=' + date.toGMTString();
    }
    document.cookie = name + '=' + value + expires + '; path=/';
}

function readCookie(name) {
    var cookieCrumbs = document.cookie.split(';');
    var nameToFind = name + '=';
    for (var i = 0; i < cookieCrumbs.length; i++) {
        var crumb = cookieCrumbs[i];
        while (crumb.charAt(0) == ' ') {
            crumb = crumb.substring(1, crumb.length); /* delete spaces */
        }
        if (crumb.indexOf(nameToFind) == 0) {
            return crumb.substring(nameToFind.length, crumb.length);
        }
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name, '', -1);
}
/*
 * Clear Default Text: functions for clearing and replacing default text in
 * <input> elements.
 *
 * by Ross Shannon, http://www.yourhtmlsource.com/
 */

addEvent(window, 'load', initClear, false);

function initClear() {
    var formInputs = document.getElementsByTagName('input');
    for (var i = 0; i < formInputs.length; i++) {
        var theInput = formInputs[i];
        
        if (theInput.type == 'text' && theInput.className.match(/\bcleardefault\b/)) {  
            /* Add event handlers */          
            addEvent(theInput, 'focus', clearDefaultText, false);
            addEvent(theInput, 'blur', replaceDefaultText, false);
            
            /* Save the current value */
            if (theInput.value != '') {
                theInput.defaultText = theInput.value;
            }
        }
    }
}

function clearDefaultText(e) {
    var target = window.event ? window.event.srcElement : e ? e.target : null;
    if (!target) return;
    
    if (target.value == target.defaultText) {
        target.value = '';
    }
}

function replaceDefaultText(e) {
    var target = window.event ? window.event.srcElement : e ? e.target : null;
    if (!target) return;
    
    if (target.value == '' && target.defaultText) {
        target.value = target.defaultText;
    }
}	
