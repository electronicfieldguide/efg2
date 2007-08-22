function toggleDisplayFormat(field,idVar){

   var selectedIndex = field.selectedIndex;
   var other = document.getElementById(idVar);

   other.options[selectedIndex] .selected= true;
}
function toggleMax(field,idVar){
   var text = field.value;
   var other = document.getElementById(idVar);
   other.value = text;
}
 function evalOtherCheckBoxes(field)
{

    if(isFieldChecked(field)){
        field[0].checked = false;
    }
    else{
            field[0].checked = true ;
    }
}
//f some checkBox other than the 'any' checkbox is checked
//then uncheck them and check the 'any' check box
//otherwise just check the 'any' checkbox
//means that if user try's to uncheck the 'any' checkbox if nothing is selected it willnot be deselected
function evalAnyCheckBox(field)
{

 if(isFieldChecked(field)){
           for (i = 1; i < field.length; i++){
            if(field[i].checked){
                field[i].checked = false;
            }
        }
    }
    field[0].checked = true;
}
// Find out that if any check box but the 'any' checkbox is selected

function isFieldChecked(field){
    checked = false;
    for (i = 1; i < field.length; i++){
        if(field[i].checked){
            checked = true;
        }
    }
    return checked;
}
function isFieldSelected(field){
    var selected = false;
    for (i = 1; i < field.length; i++){
	if(field.options[i].selected == true){
		selected = true;
	}
    }
    return selected;
}
function evalAnySelectedValue(field)
{ 
  
    if(isFieldSelected(field)){
 	field.options[0].selected = false;
    }
    else{
	field.options[0].selected  = true;
    }
   }