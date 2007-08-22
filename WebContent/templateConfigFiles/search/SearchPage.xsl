<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="../commonTemplates/commonTaxonPageTemplate.xsl"/>
	<xsl:include href="xslPageSearch.xsl" />
	<xsl:variable name="defaultcss" select="'nantucket.css'" />
	<xsl:variable name="cssFile"
		select="$xslPage/groups/group[@label='styles']/characterValue/@value" />
	<xsl:variable name="groups"
		select="$xslPage/groups/group[@label='fieldheader']" />

	<xsl:variable name="css">
		<xsl:choose>
			<xsl:when test="not(string($cssFile))=''">
				<xsl:value-of select="$cssFile" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$defaultcss" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

   <xsl:variable name="searchables" select="//Searchables[@datasourceName=$dataSourceName]"/>

	<xsl:template match="/">
		<html>
			<head>
				<title>EFG Search</title>
			<xsl:variable name="js_loc"
					select="concat($js_home,'searchPage.js')" />			
		<script language="JavaScript" src="{$js_loc}" type="text/javascript"></script>
				
				<xsl:variable name="css_loc"
					select="concat($css_home,$cssFile)" />
				<link rel="stylesheet" href="{$css_loc}" />
			</head>
			<body class="search">
				<p class="searchtitle">Search the EFG</p>

				<p class="instruct">
					Select options from any of the categories below,
					then click "Search" at the bottom of the page.
					<br />
					<br />
					Selecting more than one option in a category will
					search for either characteristic (i.e. either "pink"
					or "white" flowers).
					<br />
					To select more than one option in a scrolling menu,
					hold down the Ctrl key on a PC (Command key on a
					Mac) when you click the mouse button.
				</p>

				<form method="post" action="/efg2/Redirect.jsp"
					name="myform">
					
		       		<input type="hidden" name="ALL_TABLE_NAME" value="{$ALL_TABLE_NAME}"/>
					
					<input type="hidden" name="displayFormat"
						value="HTML" />
					<input type="hidden" name="displayName"
						value="{$displayName}" />
					<input type="hidden" name="dataSourceName"
						value="{$dataSourceName}" />
					<xsl:call-template name="displaySubmitTable">
						<xsl:with-param name="first" select="'first'" />
					</xsl:call-template>

					<table class="searchmodule">
						<xsl:call-template name="outputGroup"/>
					</table>
					<xsl:call-template name="displaySubmitTable">
						<xsl:with-param name="first" select="''" />
					</xsl:call-template>
					
				</form>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="displaySubmitTable">
		<xsl:param name="first" />
		<xsl:choose>
			<xsl:when test="not(string($first))=''">
				<table class="search">
					<tr>
						<td class="submitsearch">
							<span class="searchdisplay">
								Display results as:
								<select name="searchType" size="1"
									id="firstSelect"
									onchange="toggleDisplayFormat(this,'lastSelect');"
									onkeyup="toggleDisplayFormat(this,'lastSelect');">
									<option value="plates">
										Thumbnails
									</option>
									<option value="lists">
										Text List
									</option>
								</select>
							</span>
							<span class="searchmatches">
								
								<input type="hidden" size="4"
									id="firstMatch" name="maxDisplay" value="1000000"
									onchange="toggleMax(this,'lastMatch');"
									onkeyup="toggleMax(this,'lastMatch');" />
							</span>
							<span class="search">
								<input type="submit" value="Search" />
							</span>
							<span class="clear">
								<input type="reset" value="Clear all" />
							</span>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<table class="search">
					<tr>
						<td class="submitsearch">
							<span class="searchdisplay">
								Display results as:
								<select name="searchType" size="1"
									id="lastSelect"
									onchange="toggleDisplayFormat(this,'firstSelect');"
									onkeyup="toggleDisplayFormat(this,'firstSelect');">
									<option value="plates">
										Thumbnails
									</option>
									<option value="lists">
										Text List
									</option>
								</select>
							</span>
							<span class="searchmatches">
								
								<input type="hidden" id="lastMatch"
									size="4" name="maxDisplay" value="1000000"
									onchange="toggleMax(this,'firstMatch');"
									onkeyup="toggleMax(this,'firstMatch');" />
							</span>
							<span class="search">
								<input type="submit" value="Search" />
							</span>
							<span class="clear">
								<input type="reset" value="Clear all" />
							</span>
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="Searchable">
		<xsl:variable name="fieldName" select="@fieldName" />
		<xsl:variable name="dbFieldName" select="@dbFieldName" />

		<xsl:if test="not(string($fieldName))=''">
		
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputCharacterGroups">
		<xsl:param name="groupName"/>
		<xsl:param name="characterValues"/>
		
		<xsl:if test="count($characterValues/@value) &gt; 0">
				
				
		<tr>
			<th class="search">
				<xsl:value-of select="$groupName" />
			</th>
		</tr>
		<xsl:call-template name="outputPadderTop" />
		<xsl:for-each select="$characterValues">
			<xsl:sort select="@rank" data-type="number" />
			<xsl:variable name="tt" select="@text" />
			<xsl:variable name="cval" select="@value" />
			
			<!-- use the text to get the states from the searchables list -->
			<xsl:if test="not(string($cval))=''">
			<xsl:variable name="searchable" select="$searchables/Searchable[@fieldName=$cval]"/>
			<xsl:variable name="selectname" select="$searchable/@dbFieldName"/>
			<tr class="section">
				<td class="sectionleft">
					<xsl:choose>
						<xsl:when test="not(string($tt))=''">
							<xsl:value-of select="concat($tt,':')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat($cval,':')" />
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td class="sectionright">				
					<select multiple="multiple" name="{$selectname}"
						size="5" onchange="evalAnySelectedValue(this);">
						<option selected="selected"
							value="EFG_ANY_OPTION">
							any
						</option>
						<xsl:for-each select="$searchable/SearchableState">
						<xsl:sort select="." data-type="text" />
						<option><xsl:value-of select="."/></option>
						</xsl:for-each>
						
					</select>
				</td>
			</tr>
			</xsl:if>
		</xsl:for-each>
		<xsl:call-template name="outputSpace" />
		
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputGroup">
		
		<xsl:for-each select="$groups">
			<xsl:sort select="@id" data-type="number" />
			<xsl:variable name="groupName" select="@text" />
			<xsl:variable name="characterValues"
				select="characterValue" />
			
			<xsl:call-template name="outputCharacterGroups">
				<xsl:with-param name="groupName" select="$groupName" />
				<xsl:with-param name="characterValues"
					select="$characterValues" />
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="outputPadderTop">
		<tr>
			<td class="paddertopleft"></td>
			<td class="paddertopright"></td>
		</tr>
	</xsl:template>
	<xsl:template name="outputPadder">
		<tr>
			<td class="padderbottomleft"></td>
			<td class="padderbottomright"></td>
		</tr>
	</xsl:template>
	<xsl:template name="outputSpace">
		<xsl:call-template name="outputPadder" />
		<tr>
			<td class="spacer"></td>
		</tr>

	</xsl:template>

</xsl:stylesheet>
