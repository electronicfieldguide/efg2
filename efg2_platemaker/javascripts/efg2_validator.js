  
var inputBoxError;
var constantInputs;
var stack;
/**
 * initialize and populate sets
 */
function init(){	
	this.inputBoxError = new Set();
	
	this.constantInputs = new Set();
	this.stack = new Set();
	this.addConstants();
}
function checkSelected(selectObject){
	if(this.inputBoxError== null){
		init();
	}

	var nameVar = selectObject.name;
	
	var selectedVal = selectObject.options[selectObject.selectedIndex].text;
	if(selectedVal == null || selectedVal ==''){
		if(this.stack.contains(nameVar)){
			this.stack.remove(nameVar);
		}	
	}
	else{
		if(!this.stack.contains(nameVar)){
			this.stack.add(nameVar);
		}	
	}
}
/*
 * Add some constants to the set of constants
 * TODO: make generic
 */
function addConstants(){
	this.constantInputs.add("Credits/Copyright Info Goes Here");
	this.constantInputs.add("Main Title Goes Here");
	this.constantInputs.add("Subtitle Goes Here");	
}
/*
 * Set some constant values to blank before they are submitted to server. 
 */
function clearConstants(){
	if(this.inputBoxError== null){
		init();
	}

	var clearClasses =  this.getElementsByClassName(document, "input", "formInputText");
	for(var i=0; i<clearClasses.length; i++){
		var inputClass = clearClasses[i];
		if(this.constantInputs.contains(inputClass.value)){
			inputClass.value="";
		}
	}
}
/*
 * reset the background color of the current input box
 * 
 * inputObject - the current input box
 * color - the color to set the background to
 */
function resetBackgroundColor(inputObject,color){
	if(this.inputBoxError== null){
		init();
	}
	
	inputObject.style.backgroundColor = color;
}
/**
 * To make sure that the value entered  is a valid number
 * 
 * inputObject - The html input text box
 * minvalue - the minimum value allowed for text box
 * maxvalue - the maximum value allowed for text box
 * errorMessage - The error message to output
 * 
 * return true if inputs are valid
 */
function checkNumeric(inputObject,minval, maxval,errormessage)
{
	if(this.inputBoxError== null){
		init();
	}
	
	if (doChecks(inputObject,minval,maxval,errormessage) == false)
	{
		this.resetBackgroundColor(inputObject,"#FF0000");
		this.inputBoxError.add(inputObject.name);
 		return false;
	}
	this.resetBackgroundColor(inputObject,'');
	//inputObject.style.backgroundColor = "";
	this.inputBoxError.remove(inputObject.name);

	return true;
	
}
/**
 * Validate the contents of the form before you submit to server
 * returns true if contents are valid
 */
function validateForm(){
	if(this.inputBoxError== null){
		init();
	}
	
	if(!inputBoxError.isEmpty()){
	
		alert("Input errors exists in one or more text boxes. " +
				"The background color of such error boxes is red.");
		return false;
	}
	else{
		if(this.stack.size() == 0){
			alert("Select at least one character");
			return false;
		}
	//make sure there is at least one input selection
		//clear constants
		clearConstants();
		return true;
	}
}
/**
 * inputObject - The html input text box
 * minvalue - the minimum value allowed for text box
 * maxvalue - the maximum value allowed for text box
 * errorMessage - The error message to output
 * 
 * return true if inputs are valid
 */
function doChecks(inputObject,minvalue,maxvalue,errorMessage){
 	if(this.inputBoxError== null){
		init();
	}
 	
    if(isNaN(inputObject.value)) 
    { 
      alert(inputObject.name+": Should be a number "); 
      return false; 
    }//if 
    if(eval(inputObject.value) >  eval(maxvalue)) //check for maxval
    { 
      if(!errorMessage || errorMessage.length ==0) 
      { 
        errorMessage = "The value you entered should be less than or equal "+ maxvalue; 
      }//if               
      alert(errorMessage); 
      return false;                 
     }//if             

     if(eval(inputObject.value) <  eval(minvalue)) 
     { 
       if(!errorMessage || errorMessage.length ==0) 
       { 
         errorMessage = "The value you entered should be greater than or equal "+ minvalue; 
       }//if               
       alert(errorMessage); 
       return false;                 
     }//if             
 	return true;
	
}
// ---
/*
    Written by Jonathan Snook, http://www.snook.ca/jonathan
    Add-ons by Robert Nyman, http://www.robertnyman.com
*/
function getElementsByClassName(oElm, strTagName, strClassName){
	if(this.inputBoxError== null){
		init();
	}

	var arrElements = (strTagName == "*" && oElm.all)? oElm.all : oElm.getElementsByTagName(strTagName);
	var arrReturnElements = new Array();
	strClassName = strClassName.replace(/\-/g, "\\-");
	var oRegExp = new RegExp("(^|\\s)" + strClassName + "(\\s|$)");
	var oElement;
	for(var i=0; i<arrElements.length; i++){
		oElement = arrElements[i];		
		if(oRegExp.test(oElement.className)){
			arrReturnElements.push(oElement);
		}	
	}
	return (arrReturnElements)
}
// ---
/*
	Revised to support looking for multiple class names,
	no matter in which order they're applied to the element
*/
function getElementsByClassName(oElm, strTagName, oClassNames){
	if(this.inputBoxError== null){
		init();
	}

	var arrElements = (strTagName == "*" && oElm.all)? oElm.all : oElm.getElementsByTagName(strTagName);
	var arrReturnElements = new Array();
	var arrRegExpClassNames = new Array();
	if(typeof oClassNames == "object"){
		for(var i=0; i<oClassNames.length; i++){
			arrRegExpClassNames.push(new RegExp("(^|\\s)" + oClassNames[i].replace(/\-/g, "\\-") + "(\\s|$)"));
		}
	}
	else{
		arrRegExpClassNames.push(new RegExp("(^|\\s)" + oClassNames.replace(/\-/g, "\\-") + "(\\s|$)"));
	}
	var oElement;
	var bMatchesAll;
	for(var j=0; j<arrElements.length; j++){
		oElement = arrElements[j];
		bMatchesAll = true;
		for(var k=0; k<arrRegExpClassNames.length; k++){
			if(!arrRegExpClassNames[k].test(oElement.className)){
				bMatchesAll = false;
				break;
			}
		}
		if(bMatchesAll){
			arrReturnElements.push(oElement);
		}
	}
	return (arrReturnElements)
}
// ---
// Array support for the push method in IE 5
if(typeof Array.prototype.push != "function"){
	Array.prototype.push = ArrayPush;
	function ArrayPush(value){
		this[this.length] = value;
	}
}
// ---
/*
	Examples of how to call the function:
	
	To get all a elements in the document with a "info-links" class:
    getElementsByClassName(document, "a", "info-links");
    
	To get all div elements within the element named "container", with a "col" and a "left" class:
    getElementsByClassName(document.getElementById("container"), "div", ["col", "left"]);
*/
// ---
window.onload= init;
