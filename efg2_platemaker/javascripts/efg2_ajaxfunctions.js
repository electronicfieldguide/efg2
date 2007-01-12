var pageids = new Array();
pageids[0] = "platechoosedata";
pageids[1] = "platequerydata";
pageids[2] = "platedesignform";
pageids[3] = "platesaveorprint";


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
var xmlHttp;
var READY_STATE_COMPLETE=4;
var ACTIVE_TEXT_CLASS = "menubartextactive";
var IN_ACTIVE_TEXT_CLASS = "menubartext";

var DATASOURCE_MESSAGE='Select a Data source';
var SEARCH_URL="/efg2/Redirect.jsp";
var CHOOSE_DATA_URL=JSP_ROOT + "platechoosedata.jsp";
var DESIGN_FORM_URL =JSP_ROOT + "platedesignform.jsp";
var PLATE_DATA_LIST_URL = JSP_ROOT + "platedatalist.jsp?";
var PLATE_QUERY_DATA = JSP_ROOT + "platequerydata.jsp?";
var DELETE_TEMPLATE_URL="/efg2/templateJSP/DeleteTemplate.jsp";
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

function deleteTemplate(templateid){
	var templatename = document.getElementById(templateid).value;
	
	var guid= document.getElementById(templatename).value;
	var ds=document.getElementById('datasourceName').value;
	 //DELETE_TEMPLATE_URL + "?" +
	 var url =  CONFIGURE_PAGE_TYPE + "&" +
	  			DATASOURCE_NAME + "=" + ds + "&" + 
	  			 GUID + "=" + guid + "&" + "templateUniqueName=" + templatename;

	xmlHttp=GetXmlHttpObject(THIRD_DIV);
	if (xmlHttp==null)
	{
		alert (BROWSER_MESSAGE);
		return;
	} 
	  			 
	xmlHttp.overrideMimeType('text/html'); 
	
	xmlHttp.onreadystatechange = processDeleteQuery;
	doPosts(xmlHttp,DELETE_TEMPLATE_URL,url);
	
}
function processDeleteQuery(){

	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
	{ 
		popHideHyper();
		var responseDoc = document.getElementById(DELETE_MESSAGE_ID);
		responseDoc.innerHTML = xmlHttp.responseText;
		alert(responseDoc.innerHTML );
		var newSuccessDoc = document.getElementById(SUCCESS_DELETE_ID); 
		if( newSuccessDoc == null){
			var failDoc = document.getElementById(FAILURE_DELETE_ID).innerHTML; 
			alert(failDoc);
			responseDoc.innerHTML="";
		}
		else{
			alert(newSuccessDoc.innerHTML);
		 	//reload
			responseDoc.innerHTML="";
			var sel = document.getElementById('statesDS').options[document.getElementById('statesDS').selectedIndex];
			platechoosedataResponse(sel);
		
		}	
	}
	else{
		showProgressMessage(xmlHttp.id);
	}			
}
function platechoosedataResponse(idSel){

	if(idSel.value == null || idSel.value == DATASOURCE_MESSAGE){
		window.location.href = CHOOSE_DATA_URL;
		return;
	}
	textSet(idSel);
	
	var url=PLATE_DATA_LIST_URL;
	
	url=url+DATASOURCE_NAME+ "="+document.getElementById('datasourceName').value;
	
	ajaxResponse(url,FIRST_DIV,stateChanged);
	
	
}
function platedatalistResponse(nextPageID){


	var oldValue = document.getElementById(TEMPLATE_UNIQUE_NAME).value;
	//if this oldValue is not in the list then continue if it is then go directly to page	
	var templateNameValue = '';
	if(trim(oldValue) == ''){
		templateNameValue =trim(document.getElementById('templateUniqueNamex').value);
	}
	else{
		templateNameValue = oldValue;
	}

	 if(templateNameValue == ''){
		alert(TEMPLATE_ALERT_MESSAGE);
		return;
	}
	document.getElementById(TEMPLATE_UNIQUE_NAME).value=templateNameValue;
//if displayName is not selected alert and return
	var isFound='notFound';
	 for(i = 0 ; i < current_template_list.length;i++){
	 	var curvar = current_template_list[i];
	 	if(curvar == templateNameValue){
	 		isFound = 'found';
	 		break;
	 		//go to pdf page if found
	 	}
	 	
	 }
	 
	 //go to design page
	var url=PLATE_QUERY_DATA;
	url=url+DATASOURCE_NAME+ "="+document.getElementById('datasourceName').value;
	url=url+"&" + DISPLAY_NAME+ "="+document.getElementById(DISPLAY_NAME).value;
	url=url+"&"+document.getElementById(MAIN_TABLE_CONSTANT ).value;
	
	ajaxResponse(url,SECOND_DIV,stateChanged);
	actdeactClasses(nextPageID);
	if(oldValue ==''){
		changeDivIntoText(ZEROTH_DIV,EFG_SELECTED_MESSAGE + " " + document.getElementById(DISPLAY_NAME).value);
		changeDivIntoText(FIRST_DIV,TEMPALTE_SELECTED_MESSAGE + " " + templateNameValue);
	}
				
}
/**
* Change the selected values into text messages
*/
function changeDivIntoText(pageID, textValue){
	document.getElementById(pageID).innerHTML = textValue;
}
/**
* change to next tab
*/
function actdeactClasses(nextPageID){
	for(var i = 0; i < pageids.length; i++){
		var currentID = pageids[i];
		if(currentID == nextPageID){
			document.getElementById(nextPageID).className = ACTIVE_TEXT_CLASS;
		}
		else{
			document.getElementById(currentID).className = IN_ACTIVE_TEXT_CLASS;
		}
	}

}
/**
* Response to an ajax request
*/
function ajaxResponse(url,id,funcname){
	xmlHttp=GetXmlHttpObject(id)
	if (xmlHttp==null)
	{
		alert (BROWSER_MESSAGE);
		return;
	} 
		
	xmlHttp.onreadystatechange=funcname; 
	
	xmlHttp.open("GET",url,true);
	xmlHttp.send(null);
	
	//display page
}
/**
* Write the response data into a div
*/
function setDataHTML(req,id)
{	
	document.getElementById(id).innerHTML = req.responseText;	
}
/**
* Show the progress of a request
*/
function showProgressMessage(id){			
	popUpHyper(id);
}
/*
* Set the value or text for a hidden field
*/
function textSet(idSel)
{
	
	document.getElementById('datasourceName').value = idSel.value;
	

	document.getElementById(DISPLAY_NAME).value  = idSel.text;

}
/**
* Get the request object
*/
function GetXmlHttpObject(id)
{ 

	var objXMLHttp=null
	if (window.XMLHttpRequest)
	{
		objXMLHttp=new XMLHttpRequest()
	}
	else if (window.ActiveXObject)
	{
		objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
	}
	objXMLHttp.id = id;
	return objXMLHttp
}
/**
* What should happen when a state changes for a particulra request
*/
function stateChanged() 
{ 

	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
	{ 
		popHideHyper();
		setDataHTML(xmlHttp,xmlHttp.id);
		if(xmlHttp.id == FIRST_DIV){
			//copy the contents of select box into array
			current_template_list = new Array();
			var selects = document.getElementById('guidx');
			
			for(i = 0; i < selects.options.length; i++){
				current_template_list[i] = selects.options[i]; 
			}
		}
	} 
	else{
		showProgressMessage(xmlHttp.id);
	}
} 
/**
* Collect selected options from a form
*/
function formData2QueryString(myNodes) {

	var strSubmit       = '';
	var formElem;
	var strLastElemName = '';
	
	for (i = 0; i < myNodes.length; i++) {
       	myNode = myNodes[i];
       	var myname = myNode.name
       	switch (myNode.type) {
               // Text, select, hidden                       
               case 'select-one':
               strSubmit += myname + '=' + escape(myNode.options[myNode.selectedIndex].text) + '&';
               break;
               case 'select-multiple':
               
               if (myNode.options!=null) {
				var obj_o = myNode.options;
				// For each selected option in the parent...
				for (var j=0; j<obj_o.length; j++) {
					if (obj_o[j].selected) {
					   strSubmit += myname + '=' + obj_o[j].value + '&';
						}
				}
			}
                break;
               case 'text':
               case 'hidden':
               
                       strSubmit += myname + 
                       '=' + escape(myNode.value) + '&';
                      
               break;
          	}
   	}
   	 strSubmit = strSubmit + strLastElemName;
   	
   	return strSubmit;
   }
  var query2=""; 
   function findInputsRecursively(elementElem){
		
		
		
        switch (elementElem.type) {
             // Text, select, hidden                       
         case 'select-one':
         case 'select-multiple':
         case 'text':
         case 'hidden':
       
         query2 = query2 + formData2QueryString(elementElem.parentNode.childNodes);
       
        break;
        default:
			var chnodes = elementElem.childNodes;
			
			  for(i = 0 ; i < chnodes.length; i++){
			  	findInputsRecursively(chnodes[i]);
			  }	
        	break;
	}
}    
   //call from html 
function handlePost(elementID,nextPageID){
	
  	myTable = document.getElementById(elementID);
  
  	if(myTable == null){
  		
  		return;
  	}
  	
  	findInputsRecursively(myTable);

	var id=SECOND_DIV;
	xmlHttp=GetXmlHttpObject(id)
	if (xmlHttp==null)
	{
		alert (BROWSER_MESSAGE);
		return;
	} 
	xmlHttp.overrideMimeType('text/xml'); 
	setSearchQuery(query2);
	xmlHttp.onreadystatechange = processSearchQuery;
	doPosts(xmlHttp,SEARCH_URL,query2);
	
	actdeactClasses(nextPageID);
	
}
function doPosts(req,currenturl,query){
req.open('POST',escape(currenturl), true);
	req.setRequestHeader('Content-Type', 
 		'application/x-www-form-urlencoded');
		
	req.send(query);	
	
}
function resetGlobalQuery(){
	query2='';
}
function processSearchQuery(){
	var ready = xmlHttp.readyState;
	var xml = null;
	var taxonEntries = null;
	var taxonEntryCount = null;
	var counter = null;
	
	
	
	if(ready == READY_STATE_COMPLETE){
		resetGlobalQuery();
		xml = xmlHttp.responseXML;
		taxonEntries = xmlHttp.responseXML.getElementsByTagName('TaxonEntries')[0];
		taxonEntryCount = taxonEntries.childNodes.length;
		counter = taxonEntries.childNodes.length;
		//number of taxa
		
		if(counter > 0){
			document.getElementById(NUMBER_OF_TAXA).value = counter + "";
			//call design page
			//with right arguments
			popHideHyper();
			//use wait time here..
			var query = designPageQuery();
			alert(query);
			getDesignForm(query);
			
			
		}
		else{
			popHideHyper();
			alert("Query returned zero results");
			return;
		}
	}
	else{
		showProgressMessage(xmlHttp.id);
	}
	}
	function getDesignForm(query){
	
	xmlHttp.overrideMimeType('text/html'); 
	xmlHttp.onreadystatechange = processDesignQuery;
	
	doPosts(xmlHttp,DESIGN_FORM_URL,query);
	
}
function processDesignQuery(){
	var ready = xmlHttp.readyState;

	if(ready == READY_STATE_COMPLETE){
		popHideHyper();
		setDataHTML(xmlHttp,xmlHttp.id);
			makeColorSelectors();
	}
	else{
		showProgressMessage(xmlHttp.id);
	}
}
function designPageQuery(){
	var tempU = document.getElementById(TEMPLATE_UNIQUE_NAME).value;
	
	var url=DATASOURCE_NAME+ "="+document.getElementById('datasourceName').value;
	
	url= url+"&" + DISPLAY_NAME+"="+document.getElementById(DISPLAY_NAME).value;
	url= url+"&" + ALL_TABLE_NAME+ "="+document.getElementById(MAIN_TABLE_CONSTANT ).value;
	url = url + "&" + NUMBER_OF_RESULTS+ "=" + document.getElementById(NUMBER_OF_TAXA).value;
	url = url + "&templateUniqueName=" + tempU;
	
	//FIX ME
	var guidN = document.getElementById(tempU);
	if(guidN != null){
		var guid = guidN.value;
		url = url + "&" + GUID + "=" + guid;
	}
	return url;
}

function setSearchQuery(query){
	document.getElementById('searchquery').value =query;	
}
function savePrintPage(elementID,nextPageID){

  	var myTable = document.getElementById(elementID);
  
  	if(myTable == null){
  		
  		return;
  	}
  	var trs = myTable.getElementsByTagName("input");
  	var query = formData2QueryString(trs);
  	trs = myTable.getElementsByTagName("select");
	query = query + formData2QueryString(trs);

	
	document.write(query);

}
var objPopUp = null;
var POP_UP_MESSAGE = "Please wait while your request is processed..";
function popUpHyper(whichLink) {
	objPopTrig = document.getElementById(whichLink);
	objPopUp = document.getElementById('popUpMessage');

	objPopUp.innerHTML='<b>' + POP_UP_MESSAGE + '</b>';

	xPos = objPopTrig.offsetLeft;
	yPos = (objPopTrig.offsetTop + objPopTrig.offsetHeight);
	if (xPos + objPopUp.offsetWidth >  document.body.clientWidth) xPos = xPos - objPopUp.offsetWidth;
	if (yPos + objPopUp.offsetHeight >  document.body.clientHeight) yPos = yPos - objPopUp.offsetHeight - objPopTrig.offsetHeight;
	objPopUp.style.left = xPos + 'px';
	objPopUp.style.top = yPos + 'px';
	objPopUp.style.visibility = 'visible';
	document.getElementById('popUpMessage').innerHTML=POP_UP_MESSAGE;
	
}
function popHideHyper() {
	objPopUp.style.visibility = 'hidden';
	objPopUp = null;
}		
