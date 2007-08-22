<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:key name="rows" match="TaxonEntry" use="Items/Item" />
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />
	<xsl:include
		href="../../../commonTemplates/commonTaxonPageTemplate.xsl" />
	<xsl:include href="xslPageList.xsl" />
	<xsl:include href="../../../commonTemplates/PrevNextTemplate.xsl" />
	<xsl:variable name="defaultcss" select="'nantucket_forest.css'" />
	<xsl:variable name="cssFile"
		select="$xslPage/groups/group[@label='styles']/characterValue/@value" />
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
	<xsl:variable name="hrefCommon"
		select="concat($serverbase,'/mapQuery?displayFormat=html&amp;dataSourceName=',$dataSourceName,'&amp;')" />
	<xsl:variable name="leveltitle1"
		select="$xslPage/groups/group[@label='titles']/characterValue[@rank='1']/@value" />
	<xsl:variable name="leveltitle2"
		select="$xslPage/groups/group[@label='headersInfo']/characterValue[@label='headerInfo']/@value" />
	<xsl:variable name="groupheader"
		select="$xslPage/groups/group[@label='groupheader']/@text" />
	<xsl:variable name="groupheadervalue"
		select="$xslPage/groups/group[@label='groupheader']/characterValue[@rank='1']/@value" />
	<xsl:variable name="leftColumns"
		select="$xslPage/groups/group[@label='leftColumns']" />
	<xsl:variable name="rightColumns"
		select="$xslPage/groups/group[@label='rightColumns']" />
	<xsl:variable name="leftColumn1"
		select="$leftColumns/characterValue[@rank='1']/@value" />
	<xsl:variable name="leftColumn2"
		select="$leftColumns/characterValue[@rank='2']/@value" />
	<xsl:variable name="rightColumn1"
		select="$rightColumns/characterValue[@rank='1']/@value" />
	<xsl:variable name="rightColumn2"
		select="$rightColumns/characterValue[@rank='2']/@value" />
	<xsl:template match="/">
		<xsl:variable name="total_count" select="count(//TaxonEntry)" />

		<xsl:variable name="tt_count">
			<xsl:call-template name="write_total_count">
				<xsl:with-param name="taxon_count"
					select="$taxon_count" />
				<xsl:with-param name="total_count"
					select="$total_count" />
			</xsl:call-template>
		</xsl:variable>
		<html>
			<head>
				<title>
					<xsl:value-of select="$leveltitle1" />
				</title>
				<xsl:variable name="linkhref"
					select="concat($css_home,$css)" />
				<link rel="stylesheet" href="{$linkhref}" />
			</head>
			<body class="about">
				<table class="header">
					<tr>
						<td colspan="2" class="abouttitle">
							<xsl:value-of select="$leveltitle1" />
						</td>
					</tr>
					<tr>
						<td colspan="2" class="descrip">
							<xsl:value-of select="$leveltitle2" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table class="specieslist">
								<xsl:apply-templates
									select="//TaxonEntries" />
							</table>
						</td>
					</tr>
					<xsl:call-template name="prevNext">
						<xsl:with-param name="prev" select="$prevVal" />
						<xsl:with-param name="next" select="$nextVal" />
						<xsl:with-param name="currentPageNumber"
							select="$currentPageNumber" />
						<xsl:with-param name="totalNumberOfPages"
							select="$totalNumberOfPages" />
						<xsl:with-param name="taxon_count"
							select="$tt_count" />
					</xsl:call-template>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntries">
		<xsl:apply-templates
			select="TaxonEntry[generate-id(.) = generate-id(key('rows', Items[@name=$groupheadervalue]/Item)[1])]">
			<xsl:sort select="Items[@name=$groupheadervalue]/Item" />
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="TaxonEntry">


		<tr>
			<td class="famheader" colspan="2">
				<xsl:if test="not(string($groupheader))=''">
					<xsl:value-of select="concat($groupheader,': ' )" />
				</xsl:if>
				<xsl:value-of
					select="Items[@name=$groupheadervalue]/Item" />
			</td>
		</tr>
		<xsl:for-each
			select="key('rows', Items[@name=$groupheadervalue]/Item)">
			<xsl:sort select="Items[@name=$leftColumn1]/Item" />
			<xsl:sort select="Items[@name=$leftColumn2]/Item" />


			<xsl:variable name="leftCol1"
				select="Items[@name=$leftColumn1]/Item" />
			<xsl:variable name="leftCol2"
				select="Items[@name=$leftColumn2]/Item" />
			<xsl:variable name="rightCol1"
				select="Items[@name=$rightColumn1]/Item" />
			<xsl:variable name="rightCol2"
				select="Items[@name=$rightColumn2]/Item" />


			<xsl:variable name="href1">
				<xsl:if
					test="not(string($leftColumn1))='' and not(string($leftCol1))=''">
					<xsl:value-of
						select="concat($leftColumn1,'=',$leftCol1)" />
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="href2">
				<xsl:if
					test="not(string($leftColumn2))='' and not(string($leftCol2))=''">
					<xsl:choose>
						<xsl:when test="not(string($href1))=''">
							<xsl:value-of
								select="concat('&amp;',$leftColumn2,'=',$leftCol2)" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of
								select="concat($leftColumn2,'=',$leftCol2)" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="href3">
				<xsl:if
					test="not(string($rightColumn1))='' and not(string($rightCol1))=''">
					<xsl:choose>
						<xsl:when
							test="not(string($href2))='' or not(string($href1))=''">
							<xsl:value-of
								select="concat('&amp;',$rightColumn1,'=',$rightCol1)" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of
								select="concat($rightColumn1,'=',$rightCol1)" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="href4">
				<xsl:if
					test="not(string($rightColumn2))='' and not(string($rightCol2))=''">

					<xsl:choose>
						<xsl:when
							test="not(string($href2))='' or not(string($href1))='' or not(string($href3))=''">
							<xsl:value-of
								select="concat('&amp;',$rightColumn2,'=',$rightCol2)" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of
								select="concat($rightColumn2,'=',$rightCol2)" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="hrefCurrent" select="concat($hrefCommon,$href1,$href2,$href3,$href4)"/>
			<tr>
				<xsl:call-template name="outputnameLevelUp">
					<xsl:with-param name="hrefCurrent"
						select="$hrefCurrent" />
					<xsl:with-param name="leftColumn1"
						select="$leftCol1" />
					<xsl:with-param name="leftColumn2"
						select="$leftCol2" />
					<xsl:with-param name="rightColumn1"
						select="$rightCol1" />
					<xsl:with-param name="rightColumn2"
						select="$rightCol2" />
				</xsl:call-template>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<!-- Over-riden in subclasses -->
	<xsl:template name="outputnameLevelUp" />

</xsl:stylesheet>
