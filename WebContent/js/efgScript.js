//$Id$
//look for all id elements in a table and color them in alternating colors
function init(){

	try{
		var tables = document.getElementsByTagName("table");
	
		for(i = 0; i < tables.length;i++){
			
			var table = tables[i];
			//get the id attribute of the current table
			var test = table.id;
			if(test == ""){//if it is blank continue
			
			}
			else{//get the rows element
 			  var rows = table.getElementsByTagName("tr");  
    			for(j = 0; j < rows.length; j++){ //apply style sheet to it         
     				 //manipulate rows
      				if(j % 2 == 0){
					rows[j].className = "even";
      				}else{
					rows[j].className = "odd";
      				}      
   			 }	
			
			} 
		}
  
	}catch(exceptionObj){  
	}
}

