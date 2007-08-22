var JSP_ROOT= "/efg2/templateJSP/";

var myGlobalHandlers = {
	onCreate: function(){
	 	disableAllSubmitButtons();
	},

	onComplete: function() {
		if(Ajax.activeRequestCount == 0){
			enableAllSubmitButtons();
		}
	}
};

Ajax.Responders.register(myGlobalHandlers);


function AddRows(currenturl,nameValueID,
						insertBeforeID
						){
						
	var currentU = JSP_ROOT + currenturl;
	var nameValueObject = document.getElementById(nameValueID);
	var nameValueName = nameValueObject.name;
	var nameValue = nameValueObject.value;
	//alert("nameValue before: " + nameValue);
	var params = "";
	if(nameValue == ''){
		params = nameValueName + '=' +  nameValue;
	}
	else{
		var lastIndex = nameValue.lastIndexOf(':');
		if(lastIndex > -1){
			
			
			lastIndex = lastIndex + 1;
			var firstString = nameValue.substring(0,lastIndex);
			//alert("firstString: " + firstString);
			var lastString = nameValue.substring(lastIndex);
			//alert("lastString: " + lastString);
			var newNum =Number(lastString) + Number(1);
			nameValueObject.value = firstString + newNum;
		}
		params = nameValueName + '=' +  nameValueObject.value;
	}
	//alert("nameValue after: " + nameValueObject.value);
	doPostsUpdater(currentU,params,doNothingHandler,insertBeforeID,Insertion.Before);	
	enableAllSubmitButtons();	
}
function AddLink(currenturl,nameValueID,
						insertBeforeID
						){
						
	var currentU = JSP_ROOT + currenturl;
	var nameValueObject = document.getElementById(nameValueID);
	var nameValueName = nameValueObject.name;
	var nameValue = nameValueObject.value;
	var params = "";
	if(nameValue == ''){
		params = nameValueName + '=' +  nameValue;
	}
	else{
		params = nameValueName + '=' +  nameValue;
		nameValueObject.value = '';
	}
	
	doPostsUpdater(currentU,params,doNothingHandler,insertBeforeID,Insertion.Before);	
	enableAllSubmitButtons();	
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
function reportError(request)
{
	enableAllSubmitButtons();
  	alert("Error " + request.responseText);
 }
function enableAllSubmitButtons(){
var form = $('efg_form');
var buttons = form.getInputs('button', 'efg_btn');

buttons.invoke('enable');

}
function disableAllSubmitButtons(){
  var buttons = form.getInputs('button', 'efg_btn');
	buttons.invoke('disable');
}
function doNothingHandler(loader,obj) 
{ 	
	init();
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

addEvent(window, 'load', init, false);

function init() {
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

function validate(){
	var allNodes = document.getElementsByClassName('efg_select_marker');
	
	var isValid= false;
	if(allNodes != null){
		if(allNodes.length > 0){
			for(i = 0; i < allNodes.length; i++) {
			   if(allNodes[i].selectedIndex  > 0){
					isValid = true;	
		            break;	
				}
			}
		 	if(!isValid){
		  		alert("Please Select a Character");
		 		return false;
			}
			else{
				return true;
			}
		}
		else{
   		 	return true;
    	}
    }
    else{
    	return true;
    }
}
function removeConstants(){
//remove these constants before sending to server
	if(validate()== false){
		return false;
	}
var constantsHash = $H({
 "URL" : "URL",
 "button text" : "button text",
 "Group Label goes here" : "Group Label goes here",
 "Field Label goes here" : "Field Label goes here"
});
var allNodes = document.getElementsByClassName('cleardefault');



for(i = 0; i < allNodes.length; i++) {
   var currentVal = allNodes[i];
   if(constantsHash[currentVal.value]){
   		
    	currentVal.value = '';
   }
}
 return true;
}