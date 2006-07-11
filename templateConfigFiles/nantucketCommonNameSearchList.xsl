<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<!-- Make it generic allow authors to specify different template names-->
	<xsl:param name="css" select="'nantucketstyle.css'"/>
	<xsl:variable name="hrefCommon" select="concat($serverbase,'/Redirect.jsp?displayFormat=html&amp;dataSourceName=',$dataSourceName,'&amp;uniqueID=')"/>
	<xsl:variable name="xslPage" select="document($templateConfigFile)//TaxonPageTemplate[@datasourceName=$dataSourceName]/XSLFileNames/xslListPages/xslPage[@fileName=$xslName]"/>
<xsl:variable name="imagetitle" select="concat($template_images_home,$xslPage/groups/group[@id='1']/@text)"/>	
	<xsl:variable name="title" select="$xslPage/groups/group[@id='2']/@text"/>
		<xsl:variable name="headLineText" select="$xslPage/groups/group[@id='3']/@text"/>

	<xsl:variable name="firstColumn">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'6'"/>
			<xsl:with-param name="groupRank" select="'6'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="secondColumn">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'6'"/>
			<xsl:with-param name="groupRank" select="'7'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="thirdColumn">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'6'"/>
			<xsl:with-param name="groupRank" select="'8'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="secondColNew">
		<xsl:choose>
			<xsl:when test="string($secondColumn)=''">
				<xsl:value-of select="string($thirdColumn)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="string($secondColumn)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:template match="/">
		<html>
			<head>
				<link rel="stylesheet">
					<xsl:attribute name="href"><xsl:value-of select="concat($css_home,$css)"/></xsl:attribute>
				</link>
				<title>
					<xsl:value-of select="$title"/>
				</title>
			</head>
			<body text="#000000" bgcolor="#ddeeff" link="#6699FF" vlink="#660033" alink="#660033">
				<table class="header">
					<xsl:call-template name="outputCommonRows"/>
					<xsl:choose>
						<xsl:when test="$dataSourceName and $templateConfigFile">
							<tr>
								<td colspan="2">
									<table class="specieslist">
										<xsl:call-template name="outputFirstTableRow">
											<xsl:with-param name="groups" select="$xslPage/groups"/>
										</xsl:call-template>
										<xsl:call-template name="outputSpacer"/>
										<xsl:apply-templates select="//TaxonEntry">
											<xsl:sort select="Items[@name=string($firstColumn)]"/>
											<xsl:sort select="StatisticalMesures[@name=string($firstColumn)]"/>
											<xsl:sort select="MediaResources[@name=string($firstColumn)]"/>
											<xsl:sort select="EFGLists[@name=string($firstColumn)]"/>
										</xsl:apply-templates>
									</table>
								</td>
							</tr>
						</xsl:when>
						<xsl:otherwise>
							<h2 align="center">
								<xsl:text>Datasource must be specified</xsl:text>
							</h2>
						</xsl:otherwise>
					</xsl:choose>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntry">
		<xsl:variable name="uniqueID" select="@recordID"/>
		<xsl:variable name="items" select="Items"/>
		<xsl:variable name="firstColumnVar">
			<xsl:call-template name="findColumnVariables">
				<xsl:with-param name="items" select="$items"/>
				<xsl:with-param name="stats" select="StatisticalMeasures"/>
				<xsl:with-param name="meds" select="MediaResources"/>
				<xsl:with-param name="lists" select="EFGLists"/>
				<xsl:with-param name="columnName" select="$firstColumn"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="secondColumnVar">
		<xsl:call-template name="findColumnVariables">
				<xsl:with-param name="items" select="$items"/>
				<xsl:with-param name="stats" select="StatisticalMeasures"/>
				<xsl:with-param name="meds" select="MediaResources"/>
				<xsl:with-param name="lists" select="EFGLists"/>
				<xsl:with-param name="columnName" select="$secondColNew"/>
			</xsl:call-template>
		
		</xsl:variable>
		<xsl:variable name="thirdColumnVar">
		<xsl:call-template name="findColumnVariables">
				<xsl:with-param name="items" select="$items"/>
				<xsl:with-param name="stats" select="StatisticalMeasures"/>
				<xsl:with-param name="meds" select="MediaResources"/>
				<xsl:with-param name="lists" select="EFGLists"/>
				<xsl:with-param name="columnName" select="$thirdColumn"/>
			</xsl:call-template>
		
		</xsl:variable>
		<xsl:call-template name="outputRow">
			<xsl:with-param name="uniqueID" select="$uniqueID"/>
			<xsl:with-param name="firstColumnVar" select="$firstColumnVar"/>
			<xsl:with-param name="secondColumnVar" select="$secondColumnVar"/>
			<xsl:with-param name="thirdColumnVar" select="$thirdColumnVar"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="outputRow">
		<xsl:param name="uniqueID"/>
		<xsl:param name="firstColumnVar"/>
		<xsl:param name="secondColumnVar"/>
		<xsl:param name="thirdColumnVar"/>
		<tr>
			<xsl:call-template name="outputFirstCol">
				<xsl:with-param name="uniqueID" select="$uniqueID"/>
				<xsl:with-param name="fieldValue" select="$firstColumnVar"/>
			</xsl:call-template>
			<xsl:call-template name="outputSecondCol">
				<xsl:with-param name="uniqueID" select="$uniqueID"/>
				<xsl:with-param name="fieldValue1" select="$secondColumnVar"/>
				<xsl:with-param name="fieldValue2" select="$thirdColumnVar"/>
			</xsl:call-template>
		</tr>
	</xsl:template>
	<xsl:template name="outputSpacer">
		<tr>
			<td class="spacerheight"/>
			<td class="spacerheight"/>
		</tr>
	</xsl:template>
	<xsl:template name="outputSecondCol">
		<xsl:param name="uniqueID"/>
		<xsl:param name="fieldValue1"/>
		<xsl:param name="fieldValue2"/>
		<xsl:variable name="hrefCurrent" select="concat($hrefCommon,$uniqueID)"/>
		<td class="sciname">
			<a class="sciname2nd" href="{$hrefCurrent}">
				<xsl:value-of select="concat($fieldValue1,'   ',$fieldValue2)"/>
			</a>
		</td>
	</xsl:template>
	<xsl:template name="outputFirstCol">
		<xsl:param name="uniqueID"/>
		<xsl:param name="fieldValue"/>
		<xsl:variable name="hrefCurrent" select="concat($hrefCommon,$uniqueID)"/>
		<td class="commonname">
			<a class="commonname1st" href="{$hrefCurrent}">
				<xsl:value-of select="$fieldValue"/>
			</a>
		</td>
	</xsl:template>
	<xsl:template name="findColumnVariables">
		<xsl:param name="items"/>
		<xsl:param name="stats"/>
		<xsl:param name="meds"/>
		<xsl:param name="lists"/>
		<xsl:param name="columnName"/>
		<xsl:variable name="firstItem">
			<xsl:call-template name="searchItems">
				<xsl:with-param name="items" select="$items"/>
				<xsl:with-param name="fieldName" select="$columnName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string($firstItem)=''">
				<!-- No item exist with the same @name as firstColumn -->
				<xsl:variable name="firstStats">
					<xsl:call-template name="searchStatsMeasure">
						<xsl:with-param name="stats" select="$stats"/>
						<xsl:with-param name="fieldName" select="$columnName"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string($firstStats)=''">
						<!-- No StatisticalMeasure exist with the same @name as firstColumn -->
						<xsl:variable name="firstLists">
							<xsl:call-template name="searchEFGLists">
								<xsl:with-param name="efglists" select="$lists"/>
								<xsl:with-param name="fieldName" select="$columnName"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="string($firstLists)=''">
								<!-- No EFGLists exist with the same @name as firstColumn. Check and output a media resource -->
								<xsl:variable name="firstMedia">
									<xsl:call-template name="searchMediaResources">
										<xsl:with-param name="mediaresources" select="$meds"/>
										<xsl:with-param name="fieldName" select="$columnName"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:value-of select="$firstMedia"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$firstLists"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$firstStats"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$firstItem"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="searchItems">
		<xsl:param name="items"/>
		<xsl:param name="fieldName"/>
		<xsl:for-each select="$items">
			<xsl:if test="@name=$fieldName">
				<xsl:call-template name="getItem">
					<xsl:with-param name="item" select="Item"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="searchEFGLists">
		<xsl:param name="efglists"/>
		<xsl:param name="fieldName"/>
		<xsl:for-each select="$efglists">
			<xsl:if test="@name=$fieldName">
				<xsl:call-template name="getItem">
					<xsl:with-param name="item" select="EFGList"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="searchMediaResources">
		<xsl:param name="mediaresources"/>
		<xsl:param name="fieldName"/>
		<xsl:for-each select="$mediaresources">
			<xsl:if test="@name=$fieldName">
				<xsl:call-template name="getItem">
					<xsl:with-param name="item" select="MediaResource"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="getItem">
		<xsl:param name="item"/>
		<xsl:for-each select="$item">
			<xsl:if test="position() > 1">
				<xsl:value-of select="','"/>
			</xsl:if>
			<xsl:value-of select="."/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="searchStatsMeasure">
		<xsl:param name="stats"/>
		<xsl:param name="fieldName"/>
		<xsl:for-each select="$stats">
			<xsl:if test="@name=$fieldName">
				<xsl:call-template name="getStats">
					<xsl:with-param name="stat" select="StatisticalMeasure"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="getStats">
		<xsl:param name="stat"/>
		<xsl:for-each select="$stat">
			<xsl:if test="position() > 1">
				<xsl:value-of select="','"/>
			</xsl:if>
			<xsl:value-of select="@min"/>
			<xsl:value-of select="' - '"/>
			<xsl:value-of select="@max"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="outputFirstTableRow">
		<xsl:param name="groups"/>
		<tr>
			<xsl:variable name="header1" select="$groups/group[@id='2']/@text"/>
			<xsl:variable name="header2" select="$groups/group[@id='3']/@text"/>
			<td class="commonheader">
				<xsl:value-of select="$header1"/>
			</td>
			<td class="sciheader">
				<xsl:value-of select="$header2"/>
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="getVariable">
		<xsl:param name="groups"/>
		<xsl:param name="groupID"/>
		<xsl:param name="groupRank"/>
		<xsl:param name="characterRank"/>
		<xsl:choose>
			<xsl:when test="$groups/group[@id=$groupID and @rank=$groupRank]/characterValue">
				<xsl:value-of select="$groups/group[@id=$groupID and @rank=$groupRank]/characterValue[@rank=$characterRank]/@value"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="''"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="outputCommonRows">
		<tr>
			<td colspan="2" class="title">
				<img align="left" alt="{$title}" title="{$title}" border="0" height="83" width="467" src="{$imagetitle}"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="descrip">
					<xsl:value-of select="$headLineText"/>
				</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
