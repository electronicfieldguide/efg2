/* Beginning of Cookie Code Based on code by Bill Dortch
   of hidaho designs who has generously placed it in the public domain.*/
   
function SetCookie(name,value,expires,path,domain,secure){
  var temp = name + "=" + escape(value);
  if (expires){
    temp += "; expires=" + expires.toGMTString();
  }
  if (path){
    temp += "; path=" + path;
  }
  if (domain){
    temp += "; domain=" + domain;
  }
  if (secure){
    temp += "; secure";
  }
  document.cookie = temp;
}


function GetCookie(name){	
  var arg = name + "=";
  var alen = arg.length;
  var clen = document.cookie.length;

  var i = 0;
  while (i < clen) {
    var j = i + alen;
    if (document.cookie.substring(i,j) == arg){
      return getCookieVal(j);
    }
    i = document.cookie.indexOf(" ", i) + 1;
    if (i == 0){break;}
  }

  return null;
}
function getCookieVal(offset){
  var endstr = document.cookie.indexOf(";", offset);
  if (endstr == -1){
    endstr = document.cookie.length;
  }
  return unescape(document.cookie.substring(offset,endstr));
}
function DeleteCookie (name,path,domain) {
  if (GetCookie(name)) {
    var temp = name + "=";
    temp += ((path) ? "; path=" + path : "");
    temp += ((domain) ? "; domain=" + domain : "");
    temp += "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    document.cookie = temp;
  }
}
function showCookies(){
 	if (GetCookie("datasourceName")){
 		
		if(GetCookie("datasourceIndex")){
			
			var select = document.getElementsByName(GetCookie("datasourceName"));
			if(select != null){
	  			select.item(0).selectedIndex = GetCookie("datasourceIndex");
			}
		}
  	}
	
  	if (GetCookie("rendererIndex")){
		
    		var theRadioButtons = document.redirect.renderer;
    		var cook = GetCookie("rendererIndex");
    		theRadioButtons[cook].checked = true;
  	}
}
function makeCookies(){
 
	var selectedDataSourceIndex = -1;
	var selectedDataSourceName = getSelectedDatasourceName();

	
	if(selectedDataSourceName == null){	
		return;
	}
	

	selectedDataSourceIndex = getSelectedDatasourceIndex(selectedDataSourceName);
	

  	var selectedRendererIndex =  getRadioIndex();
	
	if((selectedDataSourceIndex != -1) && 
	   (selectedDataSourceName != null) && 
           (selectedRendererIndex != -1)
        ){
  		SetCookie("datasourceIndex",selectedDataSourceIndex);
  		SetCookie("rendererIndex",selectedRendererIndex);
                SetCookie("datasourceName",selectedDataSourceName); 
	}
}

function getSelectedDatasourceIndex(name){
	var select = document.getElementsByName(name);
	if(select != null){
		return select.item(0).selectedIndex;
	}
	return -1;
	
}
function getSelectedDatasourceName(){
	var select= document.getElementsByTagName("SELECT");

   	for(var i = 0; i < select.length; i++) {
		if(select[i].selectedIndex  > -1){
			if(select[i].id){
 				return select[i].name;
			}
		}
	}
	return null;
}

function validate(){
 
	var isValid= false;
   	var select= document.getElementsByTagName("SELECT");
        var dsName;
   	for(var i = 0; i < select.length; i++) {
		if(select[i].selectedIndex  > -1){
			if(select[i].id){
				isValid = true;
 				dsName = select[i].value;
                		break;	
			}	
		}
	}
	if(!isValid){
  		alert("Please select a Key");
 		return false;
	}
        document.redirect.dataSource.value=dsName;
    	makeCookies();
    	return true;
}
function getRadioIndex(){
  var theRadioButtons = document.redirect.renderer;
  for(i=0; i < theRadioButtons.length;i++){
    if(theRadioButtons[i].checked){
      return i;
    }
  }
}

function deselect(vall){
	var pairs = vall.split(",");
    	for(var i = 0; i < pairs.length; i++) {
		var select = document.getElementById(pairs[i]);
		select.selectedIndex = -1;
	}
}

