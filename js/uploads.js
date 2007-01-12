
/* 
 * requires trimfunctions.jsso make sure to import it
 */

 var EFG_SUBMIT_BUTTON = 'submitButton'; //id for a submit button on a page that gets 
 											//disabled when a user submits a request
 											//and enabled when the results gets back
 											
 var EFG_STATUS = 'status'; 	//The id of an element where ajax error messages are written										
 
 var fileExtensionHandler = null;
 
 /**
  * Load all the file extensions handled by callers of this script
  * TODO: probably give such elements a class name, read documenr for 
  * them and load them with this method
  */
  function loadFileExtensionHandlers(){
  	fileExtensionHandler = new Array();
  	fileExtensionHandler['templateJavascriptDirectory'] = new Array(".js") ;
	fileExtensionHandler['templateCSSDirectory'] = new Array(".css");
	fileExtensionHandler['templateImagesDirectory'] = new Array(".gif", ".jpg", ".png");
	fileExtensionHandler['importFile'] = new Array(".zip");
  }
  /**
   * Generic error message if a use tries to submit files with the 
   * wrong extensions.
   */
  function extensionMessage(currentExtension){
 	return( "Only upload files that end in types:  " 
	+ (currentExtension.join("  ")) + ".\nSelect a new "
	+ "file to upload and submit again");
  }
  /**
   * fieldName - The value of the name attribute of the html component
   * fileName - The file about to be sublitted to server
   * return true if the file extension is fieldName is in fileExtensionHandler array.
   */
  function checkFileExtension(fieldName,fileName){
  	
  
  	var localallowSubmit = false;
  	if(fileExtensionHandler == null){
  		loadFileExtensionHandlers();
  	}
  	var currentFileExtension = fileExtensionHandler[fieldName];
  
  	if(currentFileExtension == null){
  		printMessage("Programmmer error.\n" + 
		"Could not find component with name: '" + 
		fieldName + "'");
		return false;
	}

	var fileName_ext = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
	
	for (var i = 0; i < currentFileExtension.length; i++) {
		
		if (currentFileExtension[i] == fileName_ext.toLowerCase()) {//we have found at least one file
		 	 	localallowSubmit = true;
		  	 break; 
		}
	}
	
	if(localallowSubmit){
		return true;
	}
	var getExtMessage = extensionMessage(currentFileExtension)
	printMessage(getExtMessage);		
		
	return false;
  }
  /**
   * htmlFileElement - The html element with type 'file'
   * return true if the htmlFileElement contains the right file
   * extension. File extensions are loaded by extensionFileHandler array.
   */
  function validateFormFieldsForFileType(htmlFileElement){
  	if(htmlFileElement == null){
  		return false;
  	}
  	try{
	if (htmlFileElement.type == 'file') {
		var fileName = htmlFileElement.value;
		var fieldName = htmlFileElement.name;
		
		if (trim(fileName).toString() == '') {//blank field
			return false;
		}
		else{
			return checkFileExtension(fieldName,fileName);
		}
	 }
  	}
  	catch(err){
  		
  	}
	 return false; //wrong file type
 }
 /**
  * Report and error that occurs during ajax processing
  * There must be an element with id 'status' to receive the response
  * There must be a button with id 'submitButton' to be enabled
  */
 function reportAjaxError(request)
 {
    $('submitButton').disabled = false;

	$('status').innerHTML = '<div class="error"><b>Error communicating with server. Please try again.</b></div>';
  }
  /**
   * Prints a message with alert
   */
	function printMessage(message){
		
		
		alert(message);
	}
  /**
   * Dummy method to handle errors
   */
 function trapError(msg, URI, ln) {
 	
 }
 window.onerror=trapError 






