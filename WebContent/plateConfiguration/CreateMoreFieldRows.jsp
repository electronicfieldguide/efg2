<%@page import="java.util.Iterator,
project.efg.server.utils.TemplateProducer,
project.efg.util.interfaces.EFGQueueObjectInterface,

java.util.List
" %>

<%
	boolean isImagesExists=false;
	List mediaResourceFields = (List)session.getAttribute("mediaResourceFields");
	List table =(List)session.getAttribute("listFields");
	TemplateProducer tp =(TemplateProducer)session.getAttribute("templateProducer");
	if((mediaResourceFields != null) && (mediaResourceFields.size() > 0)){
		isImagesExists = true;	
	}

	String indexCapStr = request.getParameter(PDFGUIConstants.INDEXCAP);
	int indexCaps = 1;
	if(indexCapStr != null && !indexCapStr.trim().equals("")){
		try{
	indexCaps = Integer.parseInt(indexCapStr);
	indexCaps = indexCaps + 1;
		}
		catch(Exception ee){
	indexCaps = 1;
		}
	}
	
	
	String aboveorbelow="below";
	if(request.getParameter(PDFGUIConstants.ABOVE_BELOW) != null && 
		!request.getParameter(PDFGUIConstants.ABOVE_BELOW).trim().equals("")){
		 aboveorbelow = request.getParameter(PDFGUIConstants.ABOVE_BELOW);
	}
	String fieldName = "";
	String fieldValue ="";
	String name = tp.getCharacter(PDFGUIConstants.isNew,PDFGUIConstants.isNew);
	String groupLabel= tp.getCurrentGroupLabel(name);
	String groupLabelValue =PDFGUIConstants.CAPTIONS_TEXT + aboveorbelow + indexCaps;

	String characterLabel= tp.getCurrentCharacterLabel(name);
	String characterLabelValue =PDFGUIConstants.CAPTION_TEXT+ indexCaps;
%>

<%@page import="project.efg.util.pdf.interfaces.PDFGUIConstants"%>
<tr>
	<td class="captionrow">
	<input type="hidden"    name="<%=groupLabel%>" value="<%=groupLabelValue%>"/>	
	
		<span class="configitemfield">Field:</span> 
		<select name="<%=name%>"  onChange="JavaScript:checkSelected(this);">
		<%
				int ii = 0;
				Iterator it = table.iterator();
				while (it.hasNext()) {
			EFGQueueObjectInterface queueObject = (EFGQueueObjectInterface)it.next();
				 	if(isImagesExists) {
				if(mediaResourceFields.contains(queueObject)){
					continue;
				}
			}	
			fieldName =queueObject.getObject(1);
			if(ii==0){
		%>
			<option></option>
			<%
			}
			%>	
			<option>                                                                 <%=fieldName%>
			</option>
		<%
				ii++;
				}
		%>						
		</select>
		<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
		 <%
		 	name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
		 	fieldValue ="";
		 	characterLabel= tp.getCurrentCharacterLabel(name);
		 	characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCaps + PDFGUIConstants.FONT;
		 %>
		<p class="captionline">
			<span class="configitem">Font:</span> 
			<select name="<%=name%>"><!-- Show built in fonts? -->
			<%
					for(int j = 0; j < PDFGUIConstants.FONTS_NAME.length; j++){
					fieldName = PDFGUIConstants.FONTS_NAME[j];
			%>
				<option value="<%=fieldName%>">                                                                  <%=fieldName%>
				</option>
			
			<%
						}
						%>
			</select>
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			 <%
			 		name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
			 		fieldValue ="8";	
			 		characterLabel= tp.getCurrentCharacterLabel(name);
			 		characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCaps + PDFGUIConstants.SIZE;
			 %>							
			<span class="configitem">Size</span> 
			<span class="note">(6 to 100 pt):</span> 
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			<input type="text" name="<%=name%>" size="2" maxlength="3" value="<%=fieldValue%>"  onKeyDown="resetBackgroundColor(this,'');" onMouseDown="resetBackgroundColor(this,'');" onBlur="checkNumeric(this,6,100,'');"/> 
			 <%
 			 		name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
 			 		fieldValue ="";
 			 		characterLabel= tp.getCurrentCharacterLabel(name);
 			 		characterLabelValue =PDFGUIConstants.CAPTION_TEXT + 
 			 		indexCaps + PDFGUIConstants.BOLD;
 			 %>		
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			<input type="checkbox" name="<%=name%>"  value="<%=PDFGUIConstants.BOLD%>" title="bold"/>
		
			<span class="boldcheck" title="bold">B</span> 
			 <%
 			 		name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
 			 		fieldValue ="";
 			 		
 			 		characterLabel= tp.getCurrentCharacterLabel(name);		
 			 		characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCaps + PDFGUIConstants.ITALICS;
 			 %>
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			<input type="checkbox" name="<%=name%>" value="<%=PDFGUIConstants.ITALICS%>" title="italics"/>
											 							
			<span class="italicscheck" title="italics">i</span> 
			 <%
 			 		name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
 			 		fieldValue ="";
 			 		characterLabel= tp.getCurrentCharacterLabel(name);
 			 		characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCaps + PDFGUIConstants.UNDER_LINE;
 			 %>
			<input type="hidden"    name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			<input type="checkbox" name="<%=name%>" value="<%=PDFGUIConstants.UNDER_LINE%>" title="underline"/>								
			<span class="underlinecheck" title="underline">u</span>
			<span class="configitem">Align:</span> 
			<%
 					name =tp.getCharacter(PDFGUIConstants.isOld,PDFGUIConstants.isOld);
 					fieldValue ="";
 					characterLabel= tp.getCurrentCharacterLabel(name);
 					characterLabelValue =PDFGUIConstants.CAPTION_TEXT + indexCaps + PDFGUIConstants.ALIGN;
 			%>
			<input type="hidden"  name="<%=characterLabel%>" value="<%=characterLabelValue%>"/>
			<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_LEFT%>" CHECKED />
			<span class="align"><%=PDFGUIConstants.ALIGN_LEFT%></span>
			<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_CENTER%>"/>
			<span class="align"><%=PDFGUIConstants.ALIGN_CENTER%></span> 
			<input type="radio" name="<%=name%>" value="<%=PDFGUIConstants.ALIGN_RIGHT%>"/>	
			<span class="align"><%=PDFGUIConstants.ALIGN_RIGHT%></span>			
		</p>
	</td>
</tr>

					
