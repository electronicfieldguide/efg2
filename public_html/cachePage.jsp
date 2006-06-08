<%@page language="java" %>
<%@page import="java.util.*, java.io.*, project.efg.vkeyutils.*, project.efg.efgServiceRegistry.*,
      project.efg.efgServiceRegistry.types.*" %>
<html><head><title>Electronic Field Guide</title>
		
		<%

      StringBuffer buf = new StringBuffer();
      String scheme = request.getScheme();
      String server = request.getServerName();
      String serverPort = request.getServerPort() + "";
	    
	 

	
        buf.append(scheme + "://" + server);
      
	//if it contains localhost look for an Ipaddress
      //
      	buf.append("/efg/search");
	ArrayList datasourceList = new ArrayList();

     FileReader reader = null;	  
	  EfgDataSource ed = null;
	  String pathToFiles = null;
	  String configFiles = "configurationFiles" +  File.separator + "backEndServiceInfo.xml";  
	  
	  pathToFiles = getServletContext().getRealPath("/WEB-INF"); 
	  //find out if it is already in backend
	  pathToFiles = pathToFiles + File.separator + configFiles;
	  reader = new FileReader(pathToFiles);//Read the file
	  //unmarshall into EfgDataSources Object, the root element
	  EfgDataSources eds = EfgDataSources.unmarshal(reader);
	  //Get all DataSource Elements
	  Enumeration dsEnum = eds.enumerateEfgDataSource();
	
	
	  while(dsEnum.hasMoreElements() ){
	  //Get the current DataSource Element
	  	ed = (EfgDataSource)dsEnum.nextElement();
		if(ed.getDescriptionCount() > 0){
			Description description1 = ed.getDescription(0);
 			if(description1 != null){
				String content1 = description1.getContent();
			
				if((content1 != null) && (!"".equals(content1.trim()))){						
	  				datasourceList.add(ed);
				}
			}
		}
	}

	  if(reader != null){
	  reader.close();
	  }
      %>


	
		<style>
	td {
	  color: #e8c77a;
	  font-family: helvetica, verdana, arial;
	  font-size: 8pt
	   }
	td.numlist {
	  color: #efe022;
	  font-family: helvetica, verdana, arial;
	  font-weight: bold;
	  font-size: 8pt;
	  vertical-align: bottom;
	  padding-left: 3px;
	  padding-right: 3px;
	  padding-bottom: 5px; 
	   }
	td.header {
	  color: #e8c77a;
	  font-family: helvetica, verdana, arial;
	  font-weight: bold;
	  font-size: 8pt;
	  vertical-align: bottom;
	   }
	td.intro {
	  color: #efe022;
	  font-family: helvetica, verdana, arial;
	  font-weight: bold;
	  font-size: 8pt;
	  vertical-align: bottom;
	  padding-bottom: 5px;
	   }	
	a {
	  color: #fffcdc;
	  font-family: helvetica, verdana, arial;
	  font-size: 10pt;
	  text-align: left;
	  text-decoration: none;
	   }
	td.logo {
	   color: #e8c77a;
	   vertical-align: center;
	   padding-bottom: 5px;
	   }
	caption {
    text-align: center;
    vertical-align: bottom;
	color: #efe022;
    font-family: helvetica, verdana, arial;
	font-weight: bold;
	font-size: 10pt;
       }
	table.steps {
	border-top-style: outset;
	border-top-color: pink;
	border-top-width: 3px;
	border-left-style: outset;
	border-left-color: pink;
	border-left-width: 3px;
	border-right-style: outset;
	border-right-color: pink;
	border-right-width: 3px;
	border-bottom-style: outset;
	border-bottom-color: pink;
	border-bottom-width: 3px;
	   }
	</style>
		<script language="JavaScript" type="text/javascript" src="javascripts/index.js"></script></head>





	<body onload="disableCheckBox();" bgcolor="#7c0e0e">

		<form name="redirect" action="Redirect.jsp" onsubmit="return validate();" target="main">
			
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="180">
			<tbody><tr>
				<td class="logo" align="center">
					<img src="EFGIcons/EFGKeysLogo.jpg" alt="EFG Keys Logo" title="EFG Keys Logo" border="0" height="82" width="163">
				</td>
			</tr>
		</tbody></table>
		<table class="steps" align="center" cellpadding="0" cellspacing="0" width="200">
						<caption>ID as Easy as 1, 2, 3!</caption>
						<tbody><tr>
							<td colspan="4" height="8"></td>
						</tr>
						<tr>
							<td class="numlist">1.</td>
							<td colspan="2" style="padding-bottom: 5px;" align="left">
								<select name="dataSource"><option value="default" style="font-size: 10pt;">I Need to Identify...</option>
									<%
									for(int i=0; i < datasourceList.size();i++){
											ed = (EfgDataSource)datasourceList.get(i);
											if(ed != null) {
												String edsn = ed.getDataSourceName();
												Description description = ed.getDescription(0);
 												if(description != null){
													String content = description.getContent();
													if((content != null) && (!"".equals(content.trim()))){
									%>
												  		<option value="<%=edsn%>"><%=content%></option>
													<%}
												}
											}
									}%>
								</select>
							</td>
							<td>&nbsp;</td>				
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td class="numlist">2.</td>
							<td class="intro" colspan="2">Now choose a View:</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td rowspan="2" align="right">
								<img src="EFGIcons/VisualKeyMiniLogo.jpg" width="77" height="99" border="0" align="left" alt="Visual Key Logo" title="Visual Key Logo" /></td>
							<td class="header">Picture Key</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td valign="top">
							<input name="renderer" value="applet" checked="checked" onclick="showCheckBox();" type="radio">Java (Best)
							
								<br/>
								<input name="renderer" value="html" onclick="showCheckBox();" type="radio">HTML
									
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td rowspan="2" align="left">
								<img src="EFGIcons/TextKeyMiniLogo.jpg" alt="Text Key Logo" title="Text Key Logo" align="left" border="0" height="99" width="77">
							</td>
							<td class="header">Text Key</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td valign="top">
								
								<input name="renderer" value="ipodzipped" onclick="showCheckBox();" type="radio">IPOD
								<br/>
								<input name="renderer" value="pdf" onclick="showCheckBox();" type="radio">PDF (Print It!)
									
								<br>
								<input name="renderer" value="xml" onclick="showCheckBox();" type="radio">XML<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (developers)
									
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td rowspan="2" align="left">
								<img src="EFGIcons/BrowseAllIconMini.jpg" alt="Browse All Logo" title="Browse All Logo" align="left" border="0" height="99" width="77">
							</td>
							<td class="header">Browse</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td valign="top">
								<input name="renderer" value="htmlbrowse" onclick="showCheckBox();" type="radio">Show All
								<br>
 								<input name="renderer" value="listbrowse" onclick="showCheckBox();" type="radio">Show List
								<br>	
								<input name="renderer" value="pullDownService" onclick="showCheckBox();" type="radio">Pull Downs<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (synoptic key)		
							</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td colspan="2"><div id="downDiv" style="visibility: hidden;">Download It:<input disabled="disabled" name="download" value="download" type="checkbox"></div>&nbsp;</td>
								<td>&nbsp;<input type="hidden" name="host" value="<%=buf.toString()%>"/></td>
						
						</tr>
						<tr>
							<td class="numlist">3.</td>
							<td colspan="2" style="padding-bottom: 5px;" align="center">
								<input name="submit" value="Start the ID!" type="submit">
							</td>
							<td>&nbsp;</td>
						</tr>
					</tbody></table>
				
				<br>
		<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tbody><tr>
				<td class="link" width="38">
					<img src="EFGIcons//GoldenKeyIcon.gif" align="middle" border="0" height="18" width="36">
				</td>
				<td class="link">
					<a href="html/keys.html" target="main">What is a Key?</a>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="link" width="38">
					<img src="EFGIcons/GoldenKeyIcon.gif" align="middle" border="0" height="18" width="36">
				</td>
				<td class="link">
					<a href="html/KeyAuthorInstructions.html" target="main">Make Your Own Key!</a>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="link" width="38">
					<img src="EFGIcons/GoldenKeyIcon.gif" align="middle" border="0" height="18" width="36">
				</td>
				<td class="link">
					<a href="http://www.cs.umb.edu/efg/" target="main">About the EFG Project</a>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="link" width="38">
					<img src="EFGIcons/GoldenKeyIcon.gif" align="middle" border="0" height="18" width="36">
				</td>
				<td class="link">
					<a href="http://bdei.cs.umb.edu/" target="main">EFG Home</a>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="link" width="38">
					<img src="EFGIcons/GoldenKeyIcon.gif" align="middle" border="0" height="18" width="36">
				</td>
				<td class="link">
					<a href="mailto:robert.stevensonREMOVETHIS@umb.edu">Contact Us</a>
				</td>
			</tr>
		</tbody></table>
		</form>
	</body></html>