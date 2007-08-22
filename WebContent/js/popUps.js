var objPopUp = null;

function popUpHyper(popUpMessageElementID,popUpMessageParentElementID,message) {
	objPopTrig = document.getElementById(popUpMessageParentElementID);
	objPopUp = document.getElementById(popUpMessageElementID);
	objPopUp.innerHTML='<b>' + message + '</b>';

	xPos = objPopTrig.offsetLeft;
	yPos = (objPopTrig.offsetTop + objPopTrig.offsetHeight);
	if (xPos + objPopUp.offsetWidth >  document.body.clientWidth) xPos = xPos - objPopUp.offsetWidth;
	if (yPos + objPopUp.offsetHeight >  document.body.clientHeight) yPos = yPos - objPopUp.offsetHeight - objPopTrig.offsetHeight;
	objPopUp.style.left = xPos + 'px';
	objPopUp.style.top = yPos + 'px';
	objPopUp.style.visibility = 'visible';
	document.getElementById(popUpMessageElementID).innerHTML=message;
	
}
function popHideHyper() {
	objPopUp.style.visibility = 'hidden';
	objPopUp = null;
}






