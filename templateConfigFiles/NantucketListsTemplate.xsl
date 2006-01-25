<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<!--
	<xsl:variable name="groups" select="document($searchTemplateConfig)/TaxonPageTemplates/TaxonPageTemplate[@datasourceName=$datasource]/groups"/>
-->
	<xsl:variable name="groups" select="document(string($searchTemplateConfig))/TaxonPageTemplates/TaxonPageTemplate[@datasourceName=string($datasource)]/groups"/>
	<xsl:variable name="title">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=1]/characterValue">
				<xsl:value-of select="$groups/group[@id=1]/characterValue/@label"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat('Search results page for ',$datasource)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="header1">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=4]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=4]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="header2">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=5]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=5]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="header3">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=6]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=6]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="headings1">
	<xsl:choose>
			<xsl:when test="$groups/group[@id=2]/characterValue">
				<xsl:value-of select="$groups/group[@id=2]/characterValue/@label"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$header1"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="headings2">
	<xsl:choose>
			<xsl:when test="$groups/group[@id=3]/characterValue">
				<xsl:value-of select="$groups/group[@id=3]/characterValue/@label"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat($header2,'  ',$header3)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="header1">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=4]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=4]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="header2">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=5]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=5]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="header3">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=6]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=6]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:template match="/">
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
				<link rel="stylesheet">
					<xsl:attribute name="href"><xsl:value-of select="concat($serverbase,'/nantucketstyle.css')"/></xsl:attribute>
				</link>
				<title>
					<xsl:value-of select="$title"/>
				</title>
			</head>
			<body>
				<table>
					<tr>
					</tr>
					<tr>
						<td class="descrip">
							<xsl:value-of select="$title"/>
							<br/>
					Click on a name to view more information about that species.
				</td>
					</tr>
					<tr>
						<td>
							<table class="specieslist">
								<tr>
									<td class="commonheader">
										<xsl:value-of select="string($headings1)"/>
									</td>
									<td class="sciheader">
										<xsl:value-of select="string($headings2)"/>
									</td>
								</tr>
								<tr>
									<td class="spacerheight"/>
									<td class="spacerheight"/>
								</tr>
								<xsl:apply-templates select="//TaxonEntry">
									<xsl:sort select="Items[@name=string($header1)]"/>
								</xsl:apply-templates>
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntry">
		<tr>
			<td class="commonname">
				<xsl:variable name="Item1" select="Items[@name=string($header1)]/Item"/>
				<xsl:call-template name="outputcommonname">
					<xsl:with-param name="header" select="$header1"/>
					<xsl:with-param name="Item" select="$Item1"/>
				</xsl:call-template>
				<br/>
			</td>
			<td class="sciname">
				<xsl:variable name="Item3">
					<xsl:if test="not(string($header3))=''">
						<xsl:value-of select="Items[@name=string($header3)]/Item"/>
					</xsl:if>
				</xsl:variable>
				<xsl:variable name="Item2">
					<xsl:if test="not(string($header2))=''">
						<xsl:value-of select="Items[@name=string($header2)]/Item"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="outputsciname">
					<xsl:with-param name="header2" select="$header2"/>
					<xsl:with-param name="header3" select="$header3"/>
					<xsl:with-param name="Item2" select="$Item2"/>
					<xsl:with-param name="Item3" select="$Item3"/>
				</xsl:call-template>
				<br/>
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="outputsciname">
		<xsl:param name="header2"/>
		<xsl:param name="header3"/>
		<xsl:param name="Item2"/>
		<xsl:param name="Item3"/>
		
		
		<xsl:variable name="appender1">
			<xsl:value-of select="concat(string($serverbase),'/search?displayFormat=html&amp;dataSourceName=',string($datasource))"/>
		</xsl:variable>
			<xsl:variable name="appender2">
				<xsl:if test="not(string($Item2))=''">
					<xsl:value-of select="concat('&amp;',string($header2),'=',string($Item2))"/>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="appender3">
				<xsl:if test="not(string($Item3))=''">
					<xsl:value-of select="concat('&amp;',string($header3),'=',string($Item3))"/>
				</xsl:if>
			</xsl:variable>
		<a class="sciname">
			<xsl:attribute name="href">
			<xsl:value-of select="concat(string($appender1),string($appender2),string($appender3))"/>
			</xsl:attribute>
			<xsl:value-of select="string($Item2)"/>
			<xsl:if test="not(string($Item3))=''">
				<xsl:value-of select="concat('  ',string($Item3))"/>
			</xsl:if>
		</a>
	</xsl:template>
	<xsl:template name="outputcommonname">
		<xsl:param name="header"/>
		<xsl:param name="Item"/>
		<a class="commonname">
			<xsl:attribute name="href"><xsl:value-of select="concat(string($serverbase),'/search?displayFormat=html&amp;dataSourceName=',string($datasource),'&amp;',string($header),'=',string($Item))"/></xsl:attribute>
			<xsl:value-of select="string($Item)"/>
		</a>
	</xsl:template>
</xsl:stylesheet>
