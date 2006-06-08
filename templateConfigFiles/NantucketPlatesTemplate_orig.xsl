<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:variable name="commonName" select="'Common Name'"/>
	<xsl:variable name="images-per-row" select="3"/>
	<xsl:variable name="mediaResourceField" select="'Plant (IMG)'"/>
	<xsl:template match="/">
		<xsl:variable name="dsname" select="//datasources/datasource[1]/@name"/>
		<xsl:variable name="total_count" select="count(//TaxonEntry)"/>

		<html>
			<head>
				<style>
body {
	background-color: #DDEEFF;
}
#numresults {
	font-family: arial, helvetica, sans;
	color: #00008B;
	padding-bottom: 10px;
	border-bottom: 2px solid #00008B;
	margin-bottom: 10px;
}
.num {
	background-color: #00008B;
	color: #fff;
	padding: 2px;
	font-weight: bold;
}
table.resultsdisplay {
	border: 0px;
	border-collapse: collapse;
	margin: 0px;
}
td.thumbnail {
	vertical-align: bottom;
	padding-right: 10px;
	padding-left: 10px;
	padding-bottom: 0px;
	padding-top: 3px;
	padding-bottom: 2px;
	text-align: center;
	border-left: 16px solid #DDEEFF;
	border-right: 16px solid #DDEEFF;
}
td.caption {
	font-family: arial, helvetica, sans;
	color: #00008B;
	padding-right: 10px;
	padding-left: 10px;
	padding-bottom: 4px;
	text-align: center;
	margin-right: 0px;
	font-size: 12px;
	background-color: #fff;
	border-left: 16px solid #DDEEFF;
	border-right: 16px solid #DDEEFF;
	border-bottom: 8px solid #DDEEFF;
}
img, a.thumbnail {
	border: 0px;
}
a.thumbnail:hover {
	border: 1px;
}
a.caption {
	font-family: arial, helvetica, sans;
	color: #00008B;
	text-decoration: none;
}
a.caption:hover {
	text-decoration: underline;
}
td.rowspacer {
	height: 20px;
}
</style>
			</head>
			<body>
				<div id="numresults">Your search found <span class="num">
						<xsl:value-of select="$total_count"/>
					</span> results: </div>
				<table class="resultsdisplay">
		
					<xsl:apply-templates select="//TaxonEntry">
						<xsl:with-param name="total_count" select="$total_count"/>
						<xsl:with-param name="dsname" select="$dsname"/>
						
					</xsl:apply-templates>

				</table>
			</body>
		</html>
	</xsl:template>
	
	
	<xsl:template match="TaxonEntry">
		<xsl:param name="total_count"/>
		<xsl:param name="dsname"/>
	
		<xsl:param name="current_pos" select="position()"/>
		<xsl:if test="$current_pos mod $images-per-row = 1">
			<!-- Output Images -->
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
			<!-- Output captions-->
			<tr>
				<xsl:call-template name="display-captions">
					<xsl:with-param name="current_taxon" select="self::TaxonEntry"/>
					<xsl:with-param name="dsname" select="$dsname"/>
					<xsl:with-param name="uniqueID" select="@recordID"/>
				</xsl:call-template>
				<xsl:if test="not($current_pos  = $total_count)">
					<xsl:call-template name="display-captions">
						<xsl:with-param name="current_taxon" select="following-sibling::TaxonEntry[1]"/>
						<xsl:with-param name="dsname" select="$dsname"/>
						<xsl:with-param name="uniqueID" select="following-sibling::TaxonEntry[1]/@recordID"/>
					</xsl:call-template>
					<xsl:if test="not($current_pos + 1 = $total_count)">
						<xsl:call-template name="display-captions">
							<xsl:with-param name="current_taxon" select="following-sibling::TaxonEntry[2]"/>
							<xsl:with-param name="dsname" select="$dsname"/>
							<xsl:with-param name="uniqueID" select="following-sibling::TaxonEntry[2]/@recordID"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
			</tr>
			<!-- Output spacer -->
			<tr>
				<xsl:call-template name="display-spacers"/>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template name="display-spacers">
		<td class="rowspacer"/>
		<td class="rowspacer"/>
		<td class="rowspacer"/>
	</xsl:template>
	<xsl:template name="display-captions">
		<xsl:param name="current_taxon"/>
		<xsl:param name="dsname"/>
		<xsl:param name="uniqueID"/>
		<td class="caption">
			<!-- Fields are aggregated and that needs to be solved also output a generic page if transformation fails-->
			<xsl:variable name="common_name">
				<xsl:choose>
					<xsl:when test="string($current_taxon/Items[@name=$commonName]/Item)=''">
						<xsl:value-of select="$current_taxon/Items[@databaseName=$commonName]/Item"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$current_taxon/Items[@name=$commonName]/Item"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="linkURL">
				<xsl:choose>
					<xsl:when test="$datasource=''">
						<xsl:value-of select="concat($serverbase, '/search?uniqueID=',$uniqueID, '&amp;dataSourceName=',$dsname, '&amp;displayFormat=HTML')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat($serverbase, '/search?uniqueID=',$uniqueID,'&amp;displayName=', $datasource, '&amp;dataSourceName=',$dsname,'&amp;displayFormat=HTML')"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<a class="caption">
				<xsl:attribute name="href"><xsl:value-of select="$linkURL"/></xsl:attribute>
				<xsl:value-of select="$common_name"/>
			</a>
		</td>
	</xsl:template>
	<xsl:template name="display-images">
		<xsl:param name="current_taxon"/>
		<xsl:param name="dsname"/>
		<xsl:param name="uniqueID"/>
		<td class="thumbnail">
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
						<xsl:value-of select="concat($serverbase, '/search?uniqueID=',$uniqueID, '&amp;dataSourceName=',$dsname, '&amp;displayFormat=HTML')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat($serverbase, '/search?uniqueID=',$uniqueID,'&amp;displayName=', $datasource, '&amp;dataSourceName=',$dsname,'&amp;displayFormat=HTML')"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<a class="thumbnail">
				<xsl:attribute name="href"><xsl:value-of select="$linkURL"/></xsl:attribute>
				<xsl:if test="not(string($imageName))=''">
					<xsl:variable name="imageURL">
						<xsl:value-of select="concat($serverbase, '/', $imagebase, '/', $imageName)"/>
					</xsl:variable>
					<img>
						<xsl:attribute name="src"><xsl:value-of select="$imageURL"/></xsl:attribute>
					</img>
				</xsl:if>
			</a>
		</td>
	</xsl:template>
</xsl:stylesheet>
