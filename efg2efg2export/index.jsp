<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<!--
/*
 * Fabtabulous! Simple tabs using Prototype
 * http://tetlaw.id.au/view/blog/fabtabulous-simple-tabs-using-prototype/
 * Andrew Tetlaw
 * version 1.1 2006-05-06
 * http://creativecommons.org/licenses/by-sa/2.5/
 */
 -->
	<head>
		<%@ include file="../Header.jsp"%>	
		<link href="../css/tabberMain.css" rel="stylesheet" type="text/css" media="screen"/>
		<link href="../css/upload.css" rel="stylesheet" type="text/css" />	
		<link href="../css/popUpHypertext.css" rel="stylesheet" type="text/css" />
		<script src="../js/prototype.js" language="JavaScript" type="text/javascript"></script>
		<script src="../js/popUps.js" language="JavaScript" type="text/javascript"></script>
	  	<script src="../js/uploads.js" language="JavaScript" type="text/javascript"></script>
		<link href="../css/tabber.css" rel="stylesheet"  type="text/css" media="screen"/>

	
		<script language="JavaScript" type="text/javascript">
		<!--	
		var POP_UP_MESSAGE = 'The response to your requests may take a long time.Please be patient..';
		
		var erroMessagesPointer=null;	
		var deleteMessagesPointer = null;
		var initNumber = 0;
		var autoClose=1;
		var oldObject=null;
		var hashtable = null;
		var strParamsDatasources = 'ALL_TABLE_NAME=EFG_RDB_TABLES';
		var strParamsGlossaries = 'ALL_TABLE_NAME=EFG_GLOSSARY_TABLES';
		var strParamsDummy = 'DUMMY=DUMMY';
		var datasourcelisturl = "../Listdatasources.jsp";
		var mediaresourcelisturl ="../ListMediaResources.jsp";
		var otherresourcelisturl ="../ListOtherResources.jsp";
		var deletesurl="DeleteResources.jsp";
		var exportsurl ="../export";
		var exportsloader = null;
		
		var titles = new Array();
		titles[0] = 'Select datasources';
		titles[1] = 'Select glossaries';
		titles[2] = 'Select media resources';
		titles[3] = 'Select other resources(images,css,javascripts)';
		
		var progressMessages = new Array();
		progressMessages[0] = 'Please wait while your requests are processed...';
		progressMessages[1] = 'It may take a while for your response to come back.Please be patient';
		progressMessages[2] = 'The response to your requests may take a long time.Please be patient..';
	    
	    var defaultProgressMessageNumber = 0;
	    
		var myGlobalHandlers = {
		onCreate: function(){
			showProgressMessage();
		},

		onComplete: function() {
			if(Ajax.activeRequestCount == 0){
				popHideHyper();	
				
			}
		}
	};

	Ajax.Responders.register(myGlobalHandlers);
		  function reportError(request)
		  {
		   popHideHyper();	
		    $('errorMessages').innerHTML = '<div class="error"><b>Error communicating with server. Please try again.</b></div>';
		  }
		/**
		 * Include another javascript file here
		 */
		 var isLoaded = false;
		function loadInputs(argsObj){
		    var ind = argsObj.index;
		      
			if(!isLoaded){
			
				isLoaded = true;
				var ajax1 = new Ajax.Updater(
				{success: 'datasources'},
				datasourcelisturl,
				{method: 'post', contentType: 'application/x-www-form-urlencoded', encoding: 'UTF-8', parameters: strParamsDatasources,onComplete: dummyResponse, onFailure: reportError}
				);
				var ajax2 = new Ajax.Updater(
				{success: 'glossaries'},
				datasourcelisturl,
				{method: 'post',contentType: 'application/x-www-form-urlencoded', encoding: 'UTF-8', parameters: strParamsGlossaries,onComplete: dummyResponse, onFailure: reportError}
				);
	
				var ajax3 = new Ajax.Updater(
				{success: 'mediaresources'},
				 mediaresourcelisturl,
				{method: 'post',contentType: 'application/x-www-form-urlencoded' ,encoding: 'UTF-8', parameters: strParamsDummy,  onComplete: dummyResponse, onFailure: reportError}
				);
				
				refreshAjax();
			}
			else{
				if(ind == 2 || ind == 3){
					refreshAjax();
				}
			}	
					
		}
		function refreshAjax(){
			
				var ajax41 = new Ajax.Updater(
				{success: 'otherresources'},
				otherresourcelisturl,
				{method: 'post',contentType: 
				'application/x-www-form-urlencoded',
				 encoding:	'UTF-8', 
				 parameters: strParamsDummy, 
				 onComplete: dummyResponse,
				 onFailure: reportError}
				);
				var params = 'className=deleteresource';
				var ajax51 = new Ajax.Updater(
					{success: 'dotherresources'},
					otherresourcelisturl,
					{method: 'post',
					contentType: 'application/x-www-form-urlencoded', 
					encoding:	'UTF-8', 
					parameters: params, 
					onComplete: dummyResponse,
					onFailure: reportError}
				);
		
		}

		function dummyResponse(){
		}
		function clearErrorMessages(){
			if(erroMessagesPointer != null){
				erroMessagesPointer.innerHTML ="";
			}
		}
		function clearDeleteMessages(){
			
			if(deleteMessagesPointer == null){
				
			}
			else{
				deleteMessagesPointer.innerHTML ="";
			}
			
		}
		
		
		function removeElement(currentDivId,datasourceName) {	
			clearErrorMessages();
			
			 var d = document.getElementById(currentDivId);
			 var olddiv = document.getElementById(datasourceName);
			 d.removeChild(olddiv);
			 return;
		}
		
		function addElement(currentDivId,datasourceName,inputcomponentName) {
		  clearErrorMessages();
		  var ni = document.getElementById(currentDivId);
		  
		  var newdiv = document.createElement('div');
		  newdiv.setAttribute('id',datasourceName);
		  newdiv.innerHTML = '<input value="'+ datasourceName +
		   '" type="hidden" name="' +
		   inputcomponentName + '"/>'
		  ni.appendChild(newdiv);
		  
		}
		function checkClick(checkbox,currentDivId){
			if(checkbox.checked){
				addElement(currentDivId,checkbox.name,checkbox.className);
			}
			else{
				removeElement(currentDivId,checkbox.name);
			}		
		}
		function clearAllExportData(){
			clearErrorMessages();
			var zipsId = document.getElementById('placezipHere');
	   		zipsId.innerHTML='';
			
			var inputs = document.getElementsByTagName("input");
			var obj = new Array();
			var j = 0;
			for (i = 0; i < inputs.length; i++) {
		       	var myCheckBox = inputs[i];	       	
		       	switch (myCheckBox.type) {
		               case 'checkbox':
		               if(myCheckBox.checked){		               		
		               		myCheckBox.checked = false;
		               		obj[j] = myCheckBox;
		               		j = j + 1;
						}
						break;
		        }
	   		}
	   		for (z = 0; z < obj.length; z++) {
	   			var ob = obj[z];
	   			checkClick(ob,'exportData');
	   		}
		}
		function deleteResources(deleteresources){
			$('defg_submit_id_11').disabled = true;
			$('defg_submit_id_12').disabled = true;
			var exports = document.getElementById(deleteresources);
			var inputs = exports.getElementsByTagName("input");
  			var query = formData2QueryString(inputs);
  			
  			var ajaxD = new Ajax.Request(
			deletesurl, 
			{
				method: 'post', 
				contentType: 'application/x-www-form-urlencoded', 
				encoding:	'UTF-8',
				parameters: query, 
				onComplete: handleDeletes, 
				onFailure: handleDeletes}
			);
		}
		
		function submitForm(exportData){
			$('efg_submit_id_11').disabled =true;
			$('efg_submit_id_12').disabled =true;
			var exports = document.getElementById(exportData);
			var inputs = exports.getElementsByTagName("input");
  			var query = formData2QueryString(inputs);
  		
  			
  			var loader = new Ajax.Request(
			exportsurl, 
			{
				method: 'post', 
				contentType: 'application/x-www-form-urlencoded', 
				encoding:	'UTF-8',
				parameters: query, 
				onComplete: handleZipResponse, 
				onFailure: reportError
			});
		}
		function handleDeletes(request){
			
			clearDeleteMessages();
			
			var resp = request.responseText;
			
			deleteMessagesPointer = document.getElementById('derrorMessages');
			if(deleteMessagesPointer == null){
			}
			else{
				deleteMessagesPointer.innerHTML = resp;
			}
			//refresh;
			var params = 'className=deleteresource';
			refreshAjax();
			$('defg_submit_id_11').disabled = false;
			$('defg_submit_id_12').disabled = false;
			alert("done");
		}		
		
	/**
	* Handled when requests for the zip file completes successfully
	*/
	function handleZipResponse(loader,obj){
		
		clearErrorMessages();
	 	var response = loader.responseXML.getElementsByTagName('div')[0]; // grabs first <div>
	 	var forwardurl = response.firstChild.nodeValue; 
	 	var responseId= response.getAttribute('id');
  		
		if(responseId == 'success'){
			forwardRequests(forwardurl);
		}
		else{
			erroMessagesPointer = document.getElementById('errorMessages');
			erroMessagesPointer.innerHTML =forwardurl;
			$('efg_submit_id_11').disabled =false;
			$('efg_submit_id_12').disabled =false;
		}
		//popHideHyper();	
	}
	function forwardRequests(forwardurl){
		popHideHyper();	
		$('efg_submit_id_11').disabled = false;
		$('efg_submit_id_12').disabled = false;
		var divzip = document.getElementById('placezipHere');
		divzip.innerHTML = '<a href="' + forwardurl + '"> Download zip file </a>'; 
		window.location.href = forwardurl;
	}
	/**
	* Show the progress of a request
	*/
	function showProgressMessage(){		
		var divzip = document.getElementById('placezipHere');
		divzip.innerHTML='';
		popUpHyper('popUpMessage','popUpMessageParent',POP_UP_MESSAGE);
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
	               case 'hidden':
	                       strSubmit += myname + 
	                       '=' + escape(myNode.value) + '&';
	               break;
	        }
	   	}
	   	 strSubmit = strSubmit + strLastElemName;
   	
   		return strSubmit;
   }
   		  var updater = null;
		
		  function validateForm(){
		  	var fileToUpload = document.getElementById('importFile');
			return validateFormFieldsForFileType(fileToUpload);		
		  }
		  function startStatusCheck()
		  {
			 $('submitButton').disabled = true;
			if(!validateForm()){
			  $('submitButton').disabled = false;
				return false;
			}
			else{
			  
			
			    updater = new Ajax.PeriodicalUpdater(
			                                'status',
			                                'importEFG',
			                                {asynchronous:true, frequency:1, method: 'get', parameters: 'c=status', onFailure: reportAjaxError});
			
			    return true;
		    }
		  }
		  function killUpdate(message)
		  {
		    $('submitButton').disabled = false;
		
		    updater.stop();
		    if(message != '')
		    {
		      $('status').innerHTML = '<div class="error"><b>Error processing results: ' + message + '</b></div>';
		    }
		    else
		    {
		      new Ajax.Updater('status',
		                     'importEFG',
		                     {asynchronous:true, method: 'get', parameters: 'c=status', onFailure: reportAjaxError});
		    }
		  }
		  
	
		//-->
	</script>
	<script type="text/javascript">
	<!--
/* Optional: Temporarily hide the "tabber" class so it does not "flash"
   on the page as plain HTML. After tabber runs, the class is changed
   to "tabberlive" and it will appear.
*/
document.write('<style type="text/css">.tabber{display:none;}<\/style>');

var tabberOptions = {

  /* Optional: instead of letting tabber run during the onload event,
     we'll start it up manually. This can be useful because the onload
     even runs after all the images have finished loading, and we can
     run tabber at the bottom of our page to start it up faster. See the
     bottom of this page for more info. Note: this variable must be set
     BEFORE you include ../../js/tabber.js.
  */
  'manualStartup':true,

  /* Optional: code to run after each tabber object has initialized */

  'onLoad': function(argsObj) {
 	loadInputs(argsObj);  
 
  },

  /* Optional: code to run when the user clicks a tab. If this
     function returns boolean false then the tab will not be changed
     (the click is canceled). If you do not return a value or return
     something that is not boolean false, */

  'onClick': function(argsObj) {

   // var t = argsObj.tabber; /* Tabber object */
  
    var i = argsObj.index; /* Which tab was clicked (0 is the first tab) */
    var e = argsObj.event; /* Event object */
	loadInputs(argsObj);
  },

  /* Optional: set an ID for each tab navigation link */
  'addLinkId': true

	};
		 	
		 	//-->
	</script>
	<script type="text/javascript" src="../js/tabber.js"></script>
	</head>
	<body>
	<%@ include file="../EFGTableHeader.jsp"%>		
		<div class="tabber">

		 	<div class="tabbertab">
		 	<h2>Exports</h2>
					<h3>Select resources to export</h3>
					<hr/>
					<br/>	
					<br/>
							<div>
						<!-- Check inputs before you submit -->
						<div id="exportData"></div>
						<br/>
						<div id="errorMessages" class="errorMessage"></div>
						<div id="popUpMessageParent">
							<div id="popUpMessage" class="popUp"> Loading.. </div>
						</div>
						
					<input name="submit" id="efg_submit_id_11" type="button" value="Export" onclick="javascript:submitForm('exportData');"/>
					<input name="submit" id="efg_submit_id_12" type="button" value="Clear" onclick="javascript:clearAllExportData();"/>
					</div>
					<div id="placezipHere">Link to zip file appears here.</div>
						
					<br/>	
					<br/>	
					<div id="datasources">
					</div>
					<div id="glossaries">
					</div>
					<div id="mediaresources">
					</div>
					<div id="otherresources">
					</div>
					
						</div>
			<div class="tabbertab">
				<h2>Delete resources</h2>
				<hr/>
					<div>
					<!-- Check inputs before you submit -->
					<div id="deleteresources"></div>
					<br/>
					<div id="derrorMessages" class="errorMessage"></div>
					<div id="dpopUpMessageParent">
						<div id="dpopUpMessage" class="popUp"> Loading.. </div>
					</div>					
					<input name="submit" id="defg_submit_id_11" type="button" 
					value="Delete" onclick="javascript:deleteResources('deleteresources');"/>
					<input name="submit" id="defg_submit_id_12" type="reset" 
					value="Clear"  onclick="javascript:refreshAjax()"/>
					
					
					
				</div>
				<br/><br/>
				<div id="dotherresources">
				</div>	
							
			</div>
						<div class="tabbertab">
			<h2>Help</h2>
				<p>Coming soon</p>
			</div>
		</div>
		
		<script type="text/javascript">
		
		/* Since we specified manualStartup=true, tabber will not run after
		   the onload event. Instead let's run it now, to prevent any delay
		   while images load.
		*/
		
		tabberAutomatic(tabberOptions);
		
		</script>
	</body>
</html>