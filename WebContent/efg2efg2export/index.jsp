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
	
		<link rel="stylesheet" href="../efg2web.css" type="text/css"/>
		<%@ include file="../Header.jsp"%>	
		<link href="../css/tabberMain.css" rel="stylesheet" type="text/css" media="screen"/>
		<link href="../css/upload.css" rel="stylesheet" type="text/css" />	
		<link href="../css/popUpHypertext.css" rel="stylesheet" type="text/css" />
		<script src="../js/prototype.js" language="JavaScript" type="text/javascript"></script>
		<script src="../js/popUps.js" language="JavaScript" type="text/javascript"></script>
	  	<script src="../js/uploads.js" language="JavaScript" type="text/javascript"></script>
		<link href="../css/tabber.css" rel="stylesheet"  type="text/css" media="screen"/>
	  	<script src="../js/upload_ajax.js" language="JavaScript" type="text/javascript"></script>
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