<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="commonTemplates/commonTaxonPageTemplate.xsl"/>
	<xsl:include href="commonTemplates/commonFunctionTemplate.xsl"/>
		<xsl:include href="commonTemplates/PrevNextTemplate.xsl"/>
	
	<xsl:variable name="images-per-row" select="3"/>
	<xsl:template match="/">
		<xsl:variable name="dsname" select="//datasources/datasource[1]/@name"/>
		<xsl:variable name="total_count" select="count(//TaxonEntry)"/>
		
				<xsl:variable name="tt_count">
		<xsl:call-template name="write_total_count">
		<xsl:with-param name="taxon_count" select="$taxon_count"/>
		<xsl:with-param name="total_count" select="$total_count"/>
		</xsl:call-template>
		</xsl:variable>	
		<html>
			<body>
				<h3 align="center">Your search found <xsl:value-of select="$tt_count"/> results</h3>
				<table border="0" width="100%">
					<xsl:apply-templates select="//TaxonEntry">
						<xsl:with-param name="total_count" select="$tt_count"/>
						<xsl:with-param name="dsname" select="$dsname"/>
					</xsl:apply-templates>
					<table align="center">
					<xsl:call-template name="prevNext">
						<xsl:with-param name="prev" select="$prevVal"/>
						<xsl:with-param name="next" select="$nextVal"/>
						<xsl:with-param name="currentPageNumber" select="$currentPageNumber"/>
						<xsl:with-param name="totalNumberOfPages" select="$totalNumberOfPages"/>
						<xsl:with-param name="taxon_count" select="$tt_count"/>
					</xsl:call-template>
					</table>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntry">
		<xsl:param name="total_count"/>
		<xsl:param name="dsname"/>
		<xsl:param name="current_pos" select="position()"/>
		<xsl:if test="$current_pos mod $images-per-row = 1">
			<tr>
				<xsl:call-template name="display-images">
					<xsl:with-param name="current_taxon" select="self::TaxonEntry"/>
					<xsl:with-param name="dsname" select="$dsname"/>
					<xsl:with-param name="uniqueID" select="@recordID"/>
				</xsl:call-template>
				<xsl:if test="not($current_pos  = $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="current_taxon" select="following-sibling::TaxonEntry[1]"/>
						<xsl:with-param name="dsname" select="$dsname"/>
						<xsl:with-param name="uniqueID" select="following-sibling::TaxonEntry[1]/@recordID"/>
					</xsl:call-template>
					<xsl:if test="not($current_pos + 1 = $total_count)">
						<xsl:call-template name="display-images">
							<xsl:with-param name="current_taxon" select="following-sibling::TaxonEntry[2]"/>
							<xsl:with-param name="dsname" select="$dsname"/>
							<xsl:with-param name="uniqueID" select="following-sibling::TaxonEntry[2]/@recordID"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template name="display-images">
		<xsl:param name="current_taxon"/>
		<xsl:param name="dsname"/>
		<xsl:param name="uniqueID"/>
		<td align="center">
			<!-- Fields are aggregated and that needs to be solved also output a generic page if transformation fails-->
			<xsl:variable name="sci_name">
				<xsl:choose>
					<xsl:when test="string($current_taxon/Items[@name=$fieldName]/Item)=''">
						<xsl:value-of select="$current_taxon/Items[@databaseName=$fieldName]/Item"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$current_taxon/Items[@name=$fieldName]/Item"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="imageName">
				<xsl:if test="not($mediaResourceField)=''">
					<xsl:if test="$current_taxon/MediaResources">
						<xsl:choose>
							<xsl:when test="string($current_taxon/MediaResources[@name=$mediaResourceField])=''">
								<xsl:value-of select="$current_taxon/MediaResources[@databaseName=$mediaResourceField]/MediaResource[1]"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$current_taxon/MediaResources[@name=$mediaResourceField]/MediaResource[1]"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="linkURL">
					<xsl:choose>
						<xsl:when test="$datasource=''">
							<xsl:value-of select="concat($hrefCommon,$uniqueID)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat($hrefCommon,$uniqueID,'&amp;displayName=', $datasource)"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
		
			<a href="{$linkURL}">
		
				<xsl:if test="not(string($imageName))=''">
					<xsl:variable name="imageURL">
						<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $imageName)"/>
					</xsl:variable>
					<img src="{$imageURL}"/>
					<br clear="all"/>
				</xsl:if>
				<xsl:value-of select="string($sci_name)"/>
			</a>
		</td>
	</xsl:template>
</xsl:stylesheet>
