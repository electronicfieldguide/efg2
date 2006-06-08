<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<!--
	<xsl:variable name="groups" select="document($searchTemplateConfig)/TaxonPageTemplates/TaxonPageTemplate[@displayName=$datasource]/groups"/>
-->
	<xsl:variable name="title" select="'Guide to Invasive Plants on Nantucket'"/>
	<xsl:variable name="header1" select="'Common Name'"/>
	<xsl:variable name="header2" select="'Scientific Name'"/>
	<xsl:variable name="genus" select="'Genus'"/>
	<xsl:variable name="species" select="'Species'"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$title"/>
				</title>
				<link rel="stylesheet">
					<xsl:attribute name="href"><xsl:value-of select="concat($serverbase,'/nantucketstyle.css')"/></xsl:attribute>
				</link>
			</head>
			<body text="#000000" bgcolor="#ddeeff" link="#6699FF" vlink="#660033" alink="#660033">
				<table class="header">
					<tr>
						<td class="title" colspan="2">
							<img  width="467" height="83" border="0" alt="The Electronic Field Guide to the Invasive Plants of Nantucket" align="left">
							<xsl:attribute name="src"><xsl:value-of select="concat($serverbase,'/nanttitlenamelist.jpg')"/></xsl:attribute>
							</img>
						</td>
					</tr>
					<tr>
						<td class="descrip" colspan="2">
						Click on a name to view more information about that species.
				</td>
					</tr>
					<tr>
						<td colspan="2">
							<table class="specieslist">
								<tr>
									<td class="sciheader">
										<xsl:value-of select="string($header2)"/>
									</td>
									<td class="commonheader">
										<xsl:value-of select="string($header1)"/>
									</td>
								</tr>
								<tr>
									<td class="spacerheight"/>
									<td class="spacerheight"/>
								</tr>
								<xsl:apply-templates select="//TaxonEntry">
									<xsl:sort select="Items[@name=string($genus)]"/>
									<xsl:sort select="Items[@name=string($species)]"/>
								</xsl:apply-templates>
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntry">
		<xsl:variable name="uniqueID" select="@recordID"/>
		<tr>
			
			<td class="sciname">
				<xsl:variable name="ItemGenus">
					<xsl:if test="not(string($genus))=''">
						<xsl:value-of select="Items[@name=string($genus)]/Item"/>
					</xsl:if>
				</xsl:variable>
				<xsl:variable name="ItemSpecies">
					<xsl:if test="not(string($species))=''">
						<xsl:value-of select="Items[@name=string($species)]/Item"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="outputsciname">
					<xsl:with-param name="ItemGenus" select="$ItemGenus"/>
					<xsl:with-param name="ItemSpecies" select="$ItemSpecies"/>
					<xsl:with-param name="uniqueID" select="$uniqueID"/>
				</xsl:call-template>
			</td>
			<td class="commonname">
				<xsl:variable name="ItemCommon" select="Items[@name=string($header1)]"/>
				<xsl:call-template name="outputcommonname">
					<xsl:with-param name="header" select="$header1"/>
					<xsl:with-param name="commonName" select="$ItemCommon"/>
					<xsl:with-param name="uniqueID" select="$uniqueID"/>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="outputsciname">
		<xsl:param name="ItemGenus"/>
		<xsl:param name="ItemSpecies"/>
		<xsl:param name="uniqueID"/>
		<xsl:variable name="appender1">
			<xsl:value-of select="concat(string($serverbase),'/search?displayFormat=html&amp;displayName=',string($datasource))"/>
		</xsl:variable>
		<xsl:variable name="appender2">
			<xsl:if test="not(string($ItemGenus))=''">
				<xsl:value-of select="concat('&amp;',string($genus),'=',string($ItemGenus))"/>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="appender3">
			<xsl:if test="not(string($ItemSpecies))=''">
				<xsl:value-of select="concat('&amp;',string($species),'=',string($ItemSpecies))"/>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="appender">
			<xsl:choose>
				<xsl:when test="not(string($uniqueID))=''">
					<xsl:value-of select="concat(string($appender1),'&amp;uniqueID=',$uniqueID)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="concat(string($appender1),string($appender2),string($appender3))"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<a class="sciname1st">
			<xsl:attribute name="href"><xsl:value-of select="string($appender)"/></xsl:attribute>
			<xsl:value-of select="string($ItemGenus)"/>
			<xsl:if test="not(string($ItemSpecies))=''">
				<xsl:value-of select="concat('  ',string($ItemSpecies))"/>
			</xsl:if>
		</a>
	</xsl:template>
	<xsl:template name="outputcommonname">
		<xsl:param name="header"/>
		<xsl:param name="commonName"/>
		<xsl:param name="uniqueID"/>
		<xsl:variable name="commonNames">
			<xsl:for-each select="$commonName/Item">
				<xsl:if test="position() > 1">
					<xsl:value-of select="', '"/>
				</xsl:if>
				<xsl:value-of select="."/>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="appender1">
			<xsl:value-of select="concat(string($serverbase),'/search?displayFormat=html&amp;displayName=',string($datasource))"/>
		</xsl:variable>
		
		<xsl:variable name="appender2" >
			<xsl:for-each select="$commonName/Item">
				<xsl:if test="position() > 1">
					<xsl:value-of select="'&amp;'"/>
				</xsl:if>
				<xsl:variable name="tempApp" select="."/>
				<xsl:value-of select="concat('&amp;',string($header),'=',$tempApp)"/>
			</xsl:for-each>
	
		</xsl:variable>
	<!-- If uniqueID exists use it . Otherwise use the common name-->
		<xsl:variable name="appender">

			<xsl:choose>
				<xsl:when test="not(string($uniqueID))=''">
					<xsl:value-of select="concat(string($appender1),'&amp;uniqueID=',$uniqueID)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="concat(string($appender1),string($appender2))"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<a class="commonname2nd">
			<xsl:attribute name="href"><xsl:value-of select="string($appender)"/></xsl:attribute>
			<xsl:value-of select="string($commonNames)"/>
		</a>
	</xsl:template>
</xsl:stylesheet>
