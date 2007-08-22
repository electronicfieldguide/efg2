function deleteConfirmMsg(template) {
	var msg = "Are you sure you want to delete '" + template + "'";
	document.delete_returnValue = confirm(msg);
	return document.delete_returnValue;	
}