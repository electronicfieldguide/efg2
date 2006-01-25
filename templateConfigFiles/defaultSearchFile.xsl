<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:variable name="images-per-row" select="3"/>
	<xsl:template match="/">
		<xsl:variable name="total_count" select="count(//TaxonEntry)"/>
		<html>
			<body>
				<h3>Your search found <xsl:value-of select="$total_count"/> results</h3>
				<table border="0" width="100%">
					<xsl:apply-templates select="//TaxonEntry">
						<xsl:with-param name="total_count" select="$total_count"/>
					</xsl:apply-templates>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntry">
		<xsl:param name="total_count"/>
		<xsl:param name="current_pos" select="position()"/>
		<xsl:if test="$current_pos mod $images-per-row = 1">
			<tr>
				<xsl:call-template name="display-images">
					<xsl:with-param name="current_taxon" select="self::TaxonEntry"/>
				</xsl:call-template>
				<xsl:if test="not($current_pos  = $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="current_taxon" select="following-sibling::TaxonEntry[1]"/>
					</xsl:call-template>
					<xsl:if test="not($current_pos + 1 = $total_count)">
						<xsl:call-template name="display-images">
							<xsl:with-param name="current_taxon" select="following-sibling::TaxonEntry[2]"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template name="display-images">
		<xsl:param name="current_taxon"/>
		<td align="center">
			<!-- Fields are aggregated and that needs to be solved also output a generic page if transformation fails-->
			<xsl:variable name="sci_name">
				<xsl:value-of select="$current_taxon/Items[@name=$fieldName]/Item"/>
			</xsl:variable>
			<xsl:variable name="imageName">
				<xsl:if test="$mediaResourceField">
					<xsl:if test="not($mediaResourceField)=''">
						<xsl:if test="$current_taxon/MediaResources">
							<xsl:if test="$current_taxon/MediaResources[@name=$mediaResourceField]">
								<xsl:value-of select="$current_taxon/MediaResources[@name=$mediaResourceField]/MediaResource"/>
							</xsl:if>
						</xsl:if>
					</xsl:if>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="linkURL">
				<xsl:choose>
					<xsl:when test="$datasource=''">
						<xsl:value-of select="concat($serverbase, '/search?',$fieldName,'=',$sci_name, '&amp;maxDisplay=1&amp;displayFormat=HTML')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat($serverbase, '/search',  '?dataSourceName=', $datasource, '&amp;',  $fieldName, '=', $sci_name, '&amp;maxDisplay=1&amp;displayFormat=HTML')"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<a>
				<xsl:attribute name="href"><xsl:value-of select="$linkURL"/></xsl:attribute>
				<xsl:if test="not(string($imageName))=''">
					<xsl:variable name="imageURL">
						<xsl:value-of select="concat($serverbase, '/', $imagebase, '/', $imageName)"/>
					</xsl:variable>
					<img>
						<xsl:attribute name="src"><xsl:value-of select="$imageURL"/></xsl:attribute>
					</img>
					<br clear="all"/>
				</xsl:if>
				<xsl:value-of select="string($sci_name)"/>
			</a>
		</td>
	</xsl:template>
</xsl:stylesheet>
