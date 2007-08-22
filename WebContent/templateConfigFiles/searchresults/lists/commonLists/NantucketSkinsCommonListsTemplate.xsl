<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Make it generic allow authors to specify different template names-->
	<xsl:include href="xslPageList.xsl"/>
	
	<xsl:variable name="defaultcss" select="'nantucketstyle.css'"/>
	
	<xsl:variable name="cssFile" select="$xslPage/groups/group[@label='styles']/characterValue/@value"/>
	<xsl:variable name="css">
		<xsl:choose>
			<xsl:when test="not(string($cssFile))=''">
				<xsl:value-of select="$cssFile"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$defaultcss"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hrefCommon" select="concat($serverbase,'/Redirect.jsp?displayFormat=html&amp;dataSourceName=',$dataSourceName,'&amp;uniqueID=')"/>
	<xsl:variable name="imagetitle" select="concat($template_images_home,$xslPage/groups/group[@label='imageheaders']/characterValue[@rank='1']/@value)"/>
	<xsl:variable name="title" select="$xslPage/groups/group[@label='titles']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="headLineText" select="$xslPage/groups/group[@label='headersInfo']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="headersLeft" select="$xslPage/groups/group[@label='headersLeft']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="headersRight" select="$xslPage/groups/group[@label='headersRight']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="leftColumns" select="$xslPage/groups/group[@label='leftColumns']"/>
	<xsl:variable name="rightColumns" select="$xslPage/groups/group[@label='rightColumns']"/>
	<xsl:variable name="leftColumn" select="$leftColumns/characterValue[1]/@value"/>
	<xsl:variable name="rightColumn" select="$rightColumns/characterValue[1]/@value"/>
	
	<!-- This is the combination column name-->
	<xsl:variable name="midColumn">
		<xsl:choose>
			<xsl:when test="count($leftColumns/characterValue/@value) &gt; 1">
				<xsl:value-of select="$leftColumns/characterValue[2]/@value"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="count($rightColumns/characterValue/@value) &gt; 1">
					<xsl:value-of select="$rightColumns/characterValue[2]/@value"/>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:template match="/">
	
		<xsl:variable name="total_count" select="count(//TaxonEntry)"/>
		
		<xsl:variable name="tt_count">
		<xsl:call-template name="write_total_count">
		<xsl:with-param name="taxon_count" select="$taxon_count"/>
		<xsl:with-param name="total_count" select="$total_count"/>
		</xsl:call-template>
		</xsl:variable>	
	
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
				<xsl:variable name="linkhref" select="concat($css_home,$css)"/>
				<link rel="stylesheet" href="{$linkhref}"/>
				<title>
					<xsl:value-of select="$title"/>
				</title>
			</head>
			<body class="about">
				<table class="header">
					<xsl:call-template name="outputCommonRows"/>
					<xsl:choose>
						<xsl:when test="$dataSourceName">
							<tr>
								<td colspan="2">
									<table class="specieslist">
										<xsl:call-template name="outputFirstTableRow"/>
										<xsl:call-template name="outputSpacer"/>
										<xsl:apply-templates select="//TaxonEntry">
											<xsl:sort select="Items[@name=string($leftColumn)]"/>
											<xsl:sort select="Items[@name=string($rightColumn)]"/>
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
					<xsl:call-template name="prevNext">
						<xsl:with-param name="prev" select="$prevVal"/>
						<xsl:with-param name="next" select="$nextVal"/>
						<xsl:with-param name="currentPageNumber" select="$currentPageNumber"/>
						<xsl:with-param name="totalNumberOfPages" select="$totalNumberOfPages"/>
						<xsl:with-param name="taxon_count" select="$tt_count"/>
					</xsl:call-template>
				</table>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="TaxonEntry">
		<xsl:call-template name="outputRow">
			<xsl:with-param name="uniqueID" select="@recordID"/>
			<xsl:with-param name="leftColumn" select="Items[@name=$leftColumn]/Item[1]"/>
			<xsl:with-param name="rightColumn" select="Items[@name=$rightColumn]/Item[1]"/>
			<xsl:with-param name="midColumn" select="Items[@name=$midColumn]/Item[1]"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="outputSpacer">
		<tr>
			<td class="spacerheight"/>
			<td class="spacerheight"/>
		</tr>
	</xsl:template>
	<xsl:template name="outputCol">
		<xsl:param name="uniqueID"/>
		<xsl:param name="fieldValue"/>
		<xsl:param name="tdClassname"/>
		<xsl:param name="aClassname"/>
		<xsl:variable name="hrefCurrent" select="concat($hrefCommon,$uniqueID)"/>
		<td class="{$tdClassname}">
			<a class="{$aClassname}" href="{$hrefCurrent}">
				<xsl:value-of select="$fieldValue"/>
			</a>
		</td>
	</xsl:template>
	<xsl:template name="outputFirstTableRow">
		<xsl:variable name="headersLeft" select="$xslPage/groups/group[@label='headersLeft']/characterValue[@rank='1']/@value"/>
		<xsl:variable name="headersRight" select="$xslPage/groups/group[@label='headersRight']/characterValue[@rank='1']/@value"/>
		<tr>
			<xsl:call-template name="outputFirstTableData">
				<xsl:with-param name="header1" select="$headersLeft"/>
				<xsl:with-param name="header2" select="$headersRight"/>
			</xsl:call-template>
		</tr>
	</xsl:template>
	
</xsl:stylesheet>
