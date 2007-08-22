var fActiveMenu = false;
var oOverMenu = false;

function mouseSelect(e)
{
	if (fActiveMenu)
	{
		if (oOverMenu == false)
		{
			oOverMenu = false;
			document.getElementById(fActiveMenu).style.display = "none";
			fActiveMenu = false;
			return false;
		}
		return false;
	}
	return true;
}

function menuActivate(idEdit, idMenu, idSel)
{
	if (fActiveMenu) return mouseSelect(0);

	oMenu = document.getElementById(idMenu);
	oEdit = document.getElementById(idEdit);
	nTop = oEdit.offsetTop + oEdit.offsetHeight;
	nLeft = oEdit.offsetLeft;
	while (oEdit.offsetParent != document.body)
	{
		oEdit = oEdit.offsetParent;
		nTop += oEdit.offsetTop;
		nLeft += oEdit.offsetLeft;
	}
	oMenu.style.left = nLeft;
	oMenu.style.top = nTop;
	oMenu.style.display = "";
	fActiveMenu = idMenu;
	document.getElementById(idSel).focus();
	return false;
}

function textSet(idEdit, text)
{
	document.getElementById(idEdit).value = text;
	oOverMenu = false;
	mouseSelect(0);
	document.getElementById(idEdit).focus();
}

function comboKey(idEdit, idSel)
{
	if (window.event.keyCode == 13 || window.event.keyCode == 32)
		textSet(idEdit,idSel.value);
	else if (window.event.keyCode == 27)
	{
		mouseSelect(0);
		document.getElementById(idEdit).focus();
	}
}
function valid(idEdit) {
	txt = document.getElementById(idEdit).value ;
	if(txt == '') {
	  alert('You need to type your choice into the text box  or make a selection from the list');
	  return false;
	}
	return true;
}
document.onmousedown = mouseSelect;
