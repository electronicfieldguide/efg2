/**
*
*Requires Prototype.js 1.5 and above from the ajax prototype group
*/
var READY_STATE_COMPLETE=4;
var pageids = new Array();
pageids[0] = "platechoosedata";
pageids[1] = "platequerydata";
pageids[2] = "platedesignform";
pageids[3] = "platesaveorprint";
var FORM_SEARCH_ID='form_search_id';

var DATASOURCE_NAME='dataSourceName';
var SEARCH_QUERY_XML_RESULTS='searchqueryresultsxml';
var SEARCH_QUERY_XML_RESULTS_1='searchqueryresultsxml1';
var SEARCH_QUERY='searchquery';
var colorids = new Array();
colorids[0] = "imageframecolor1id";
colorids[1] = "boundingboxcolor1id";
colorids[2] = "boundingboxcolor2id";

var JSP_ROOT= "/efg2/plateConfiguration/";
var CONFIGURE_PAGE_TYPE="searchType=pdfs";
var DELETE_TEMPLATE= JSP_ROOT + "DeleteTemplate.jsp?";	
		
var PROGRESS_MESSAGE = "Please wait while your requests is processed....";
var NO_TEMPLATE_MESSAGE = "Please select a template or new if configuring a new plate..Fix Me";
var current_template_list;


var ACTIVE_TEXT_CLASS = "menubartextactive";
var IN_ACTIVE_TEXT_CLASS = "menubartext";

var DATASOURCE_MESSAGE='Select a Data source';
var SEARCH_URL="/efg2/search";
var CHOOSE_DATA_URL=JSP_ROOT + "platechoosedata.jsp";
var DESIGN_FORM_URL =JSP_ROOT + "platedesignform.jsp";
var PLATE_DATA_LIST_URL = JSP_ROOT + "platedatalist.jsp";
var PLATE_QUERY_DATA = JSP_ROOT + "platequerydata.jsp";
var DELETE_TEMPLATE_URL="/efg2/templateJSP/DeleteTemplate.jsp";
var PDF_SERVER_URL="/efg2/efg2pdfconfig";
var CONFIGURE_PAGE = "/efg2/configTaxonPage";
var DATASOURCE_NAME="dataSourceName";
var DISPLAY_NAME = "displayName";
var TEMPLATE_ALERT_MESSAGE="Please select a template or enter the name for a new one..[Fix Me]";
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
var POP_UP_MESSAGE = "Please wait while your request is processed..";
/*
* Quote from prototype engine docs.
*"The $() function is a handy shortcut to the all-too-frequent 
*document.getElementById() function of the DOM.."
*/
/**
*
*/
function AddCaptionRow(currenturl,
						spanid,
						captionid,
						numberofcaptionsid){
						
	var currentU = JSP_ROOT + currenturl;
	var caption =  $(captionid);
	var params = caption.name + "=" + caption.value;
 	caption =  $(numberofcaptionsid);
	params = params + "&" + caption.name + "=" + caption.value;
	
	doPostsUpdater(currentU,params,doNothingHandler,spanid,Insertion.Before);		
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
		var guid= $(templatename).value;
		//var guid= $F(templatename);
		var ds=$(DATASOURCE_NAME).value;
		//var ds=$F(DATASOURCE_NAME);
		 //DELETE_TEMPLATE_URL + "?" +
		 var url =  CONFIGURE_PAGE_TYPE + "&" +
		  			DATASOURCE_NAME + "=" + ds + "&" + 
		  			 GUID + "=" + guid + "&" + "templateUniqueName=" + templatename;
		doPosts(DELETE_TEMPLATE_URL,url,processDeleteQuery);
	}
	
}
function processDeleteQuery(loader,obj){

		var responseDoc = $(DELETE_MESSAGE_ID);
		responseDoc.innerHTML = loader.responseText;
		alert(responseDoc.innerHTML );
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
			platechoosedataResponse(sel);
		}		
}
function platechoosedataResponse(idSel){
	//FIXME
	if(idSel.value == null || idSel.value == DATASOURCE_MESSAGE){
		
		window.location.href = CHOOSE_DATA_URL;
		return;
	}
	setDatasourceDisplayName(idSel);
	
	//var url=PLATE_DATA_LIST_URL;
	
	//var url=DATASOURCE_NAME+ "="+$(DATASOURCE_NAME).value;
	var url=DATASOURCE_NAME+ "="+$F(DATASOURCE_NAME);
	
	message_id = FIRST_DIV;
	
	doPostsUpdater(PLATE_DATA_LIST_URL,url,stateChangedID,FIRST_DIV); 
}
/**
* Handle after user clicks 'Go' button on datalist page
* nextPageID - The id of the next page
* guidxid - the id of the select box
*/
function platedatalistResponse(nextPageID,guidxid){

	
	var selectObject =  $(guidxid);
	
	if(selectObject != null){
		var selectedVal = selectObject.options[selectObject.selectedIndex].value;
		$('templateUniqueNamex').value =selectedVal;
		$(TEMPLATE_UNIQUE_NAME).value=selectedVal;
	}
	
	 //go to design page
	var url = DATASOURCE_NAME+ "="+$(DATASOURCE_NAME).value;
		
	url=url+"&" + DISPLAY_NAME+ "="+$(DISPLAY_NAME).value;
	url=url+"&"+$(MAIN_TABLE_CONSTANT ).value;

	message_id =SECOND_DIV;
	doPostsUpdater(PLATE_QUERY_DATA,url,doNothingHandler,SECOND_DIV); 
	actdeactClasses(nextPageID);	
	$('hide').innerHTML ="";			
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
		showProgressMessage(message_id);
	},

	onComplete: function() {
		if(Ajax.activeRequestCount == 0){
			popHideHyper();	
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
	message_id=SECOND_DIV;
	setSearchQuery(SEARCH_QUERY,Form.serialize($(formID)));
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
		return;
	}
	taxonEntryCount = taxonEntries.childNodes.length;
	if(taxonEntryCount == 0){
		alert("Query returned zero results");
		popHideHyper();
		return;
	}
	
	counter = taxonEntries.childNodes.length;
	
	//number of taxa
	
	if(counter > 0){
		if(counter > TEMP_MAX_NUMBER){
			alert("Your query returned more than a 100 results. \n" + 
			"Please increase your search criteria and try again");
			return;
		}
		else{
			$(NUMBER_OF_TAXA).value = counter + "";
			
			setSearchQuery(SEARCH_QUERY_XML_RESULTS_1,response);
			
			var query = designPageQuery();
		}
	}
	else{
		
		alert("Query returned zero results");
		return;
	}
	
}
function getDesignForm(query){
	message_id = SECOND_DIV;
	
	doPostsUpdater(DESIGN_FORM_URL,query,
	processDesignQuery,SECOND_DIV); 
	
}

function processDesignQuery(loader, obj){
	
	makeColorSelectors();
	var el = $('pdfFillFormID');
	el.scrollTop = 0;
	
}
function saveConfig(myTableId){

	alert("Work in Progress");
	return;
	var tempU = $(TEMPLATE_UNIQUE_NAME).value;
	var promptAnswer = callPrompt(tempU);
	if(promptAnswer == null){		
		return;
	}
	else if(promptAnswer == ''){
		return;
	}
	
	$(TEMPLATE_UNIQUE_NAME).value = promptAnswer;

	var queryToSave = requestPDFSavePage(myTableId);	
	var id=THIRD_DIV;
	message_id = THIRD_DIV;
	doPosts(PDF_SERVER_URL,queryToSave,processSave);
}
function processSave(loader,obj){
	popHideHyper();
	alert(loader.responseText);
}
function requestPDFSavePage(myTableID){

	var parameters = Form.serialize($(myTableID));
 	//parameters = parameters + "&" + constructQuery(false);
 	var paramurl = NUMBER_OF_RESULTS+ "=" + $(NUMBER_OF_TAXA).value;

	var tempU = $(TEMPLATE_UNIQUE_NAME).value;
	
	paramurl = paramurl + "&templateUniqueName=" + tempU;

	var guidN = $(tempU);
	if(guidN != null){
		var guid = guidN.value;
		paramurl = paramurl + "&" + GUID + "=" + guid;
		
	}
	
	paramurl=  paramurl+ "&" + $(MAIN_TABLE_CONSTANT).value;
 	parameters=parameters+
 	"&xslName=xslName.xsl&jsp=defaultJSP.jsp" + 
 	"&isDefault=true&searchType=pdfs&search=search" + 
 	"&savepdf=savepdf" + paramurl;
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
function callPrompt(defaultValueToUse){
	var promptAnswer = prompt('Enter The Template Name',defaultValueToUse);
	while(promptAnswer != null && trim(promptAnswer) == ''){
		promptAnswer = prompt('Enter The Template Name',defaultValueToUse);
	}
	return promptAnswer;	
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
