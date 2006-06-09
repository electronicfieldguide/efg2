<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:sorter="project.efg.util.SortedStringArray" extension-element-prefixes="sorter" exclude-result-prefixes="sorter">
	<xalan:component prefix="sorter" functions="sort addName  getName  getArraySize">
		<xalan:script lang="javaclass" src="xalan://project.efg.util.SortedStringArray"/>
	</xalan:component>
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:variable name="commonName" select="'Common Name'"/>
	<xsl:variable name="images-per-row" select="3"/>
	<xsl:variable name="mediaResourceField" select="'Plant (IMG)'"/>
	<xsl:template match="/">
		<xsl:variable name="dsname" select="//datasources/datasource[1]/@name"/>
		<xsl:variable name="total_count" select="count(//TaxonEntry)"/>
		<xsl:variable name="mySorter" select="sorter:new($total_count)"/>
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
					<xsl:variable name="taxonEntries" select="//TaxonEntry"/>
					<xsl:call-template name="populateMap">
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
					</xsl:call-template>
					<!-- Sort the list -->
					<xsl:variable name="sortList" select="sorter:sort($mySorter)"/>
					<xsl:variable name="maxCount" select="sorter:getArraySize($mySorter)"/>
					<xsl:variable name="index" select="1"/>
					<xsl:if test="$index &lt; $maxCount + 1">
						<xsl:call-template name="iterateOverSortedArray">
							<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
							<xsl:with-param name="mySorter" select="$mySorter"/>
							<xsl:with-param name="index" select="$index"/>
							<xsl:with-param name="dsname" select="$dsname"/>
							<xsl:with-param name="total_count" select="$maxCount"/>
						</xsl:call-template>
					</xsl:if>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="iterateOverSortedArray">
		<xsl:param name="taxonEntries"/>
		<xsl:param name="mySorter"/>
		<xsl:param name="index"/>
		<xsl:param name="dsname"/>
		<xsl:param name="total_count"/>
		<xsl:if test="$index mod $images-per-row = 1">
			<tr>
				<xsl:call-template name="display-images">
					<xsl:with-param name="index" select="$index "/>
					<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
					<xsl:with-param name="mySorter" select="$mySorter"/>
					<xsl:with-param name="dsname" select="$dsname"/>
				</xsl:call-template>
				<xsl:if test="not($index = $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="index" select="$index + 1"/>
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
						<xsl:with-param name="dsname" select="$dsname"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="not($index + 1 = $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="index" select="$index + 2"/>
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
						<xsl:with-param name="dsname" select="$dsname"/>
					</xsl:call-template>
				</xsl:if>
			</tr>
			<tr>
				<xsl:call-template name="display-captions">
					<xsl:with-param name="index" select="$index"/>
					<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
					<xsl:with-param name="mySorter" select="$mySorter"/>
					<xsl:with-param name="dsname" select="$dsname"/>
				</xsl:call-template>
				<xsl:if test="not($index  = $total_count)">
					<xsl:call-template name="display-captions">
						<xsl:with-param name="index" select="$index + 1"/>
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
						<xsl:with-param name="dsname" select="$dsname"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="not($index + 1 = $total_count)">
					<xsl:call-template name="display-captions">
						<xsl:with-param name="index" select="$index + 2"/>
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
						<xsl:with-param name="dsname" select="$dsname"/>
					</xsl:call-template>
				</xsl:if>
			</tr>
			<tr>
				<xsl:call-template name="display-spacers"/>
			</tr>
		</xsl:if>
		<xsl:variable name="index2" select="number($index) + 3"/>
	
		<xsl:if test="number($index2) &lt; number($total_count) + 1">
		
			<xsl:call-template name="iterateOverSortedArray">
				<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
				<xsl:with-param name="mySorter" select="$mySorter"/>
				<xsl:with-param name="index" select="$index2"/>
				<xsl:with-param name="dsname" select="$dsname"/>
				<xsl:with-param name="total_count" select="$total_count"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="display-spacers">
		<td class="rowspacer"/>
		<td class="rowspacer"/>
		<td class="rowspacer"/>
	</xsl:template>
	<xsl:template name="display-captions">
		<xsl:param name="index"/>
		<xsl:param name="taxonEntries"/>
		<xsl:param name="mySorter"/>
		<xsl:param name="dsname"/>
		<xsl:variable name="pos" select="number($index)-1"/>
		<xsl:variable name="commonname" select="sorter:getName($mySorter, string(number($pos)))"/>
		<xsl:for-each select="$taxonEntries">
			<xsl:if test="string(Items[@name=$commonName]/Item)=$commonname">
				<xsl:variable name="uniqueID">
					<xsl:value-of select="@recordID"/>
				</xsl:variable>
				<xsl:variable name="linkURL">
					<xsl:choose>
						<xsl:when test="$datasource=''">
							<xsl:value-of select="concat($serverbase, '/Redirect.jsp?displayFormat=HTML', '&amp;dataSourceName=',$dsname, '&amp;uniqueID=',$uniqueID)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat($serverbase, '/Redirect.jsp?displayFormat=HTML','&amp;displayName=', $datasource, '&amp;dataSourceName=',$dsname,'&amp;uniqueID=',$uniqueID)"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<td class="caption">
					<a class="caption">
						<xsl:attribute name="href"><xsl:value-of select="$linkURL"/></xsl:attribute>
						<xsl:value-of select="$commonname"/>
					</a>
				</td>
			</xsl:if>
		</xsl:for-each>
		<!-- Fields are aggregated and that needs to be solved also output a generic page if transformation fails-->
	</xsl:template>
	<xsl:template name="display-images">
		<xsl:param name="index"/>
		<xsl:param name="taxonEntries"/>
		<xsl:param name="mySorter"/>
		<xsl:param name="dsname"/>
		<xsl:variable name="pos" select="number($index)-1"/>
		<xsl:variable name="commonname" select="sorter:getName($mySorter, string(number($pos)))"/>
		<xsl:for-each select="$taxonEntries">
			<xsl:if test="string(Items[@name=$commonName]/Item)=$commonname">
				<xsl:variable name="uniqueID">
					<xsl:value-of select="@recordID"/>
				</xsl:variable>
				<xsl:variable name="imageName">
					<xsl:if test="not($mediaResourceField)=''">
						<xsl:if test="MediaResources">
							<xsl:choose>
								<xsl:when test="string(MediaResources[@name=$mediaResourceField])=''">
									<xsl:value-of select="MediaResources[@databaseName=$mediaResourceField]/MediaResource[1]"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="MediaResources[@name=$mediaResourceField]/MediaResource[1]"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</xsl:if>
				</xsl:variable>
				<xsl:variable name="altImage">
						<xsl:if test="MediaResources">
									<xsl:value-of select="MediaResources[1]/MediaResource[1]"/>
						</xsl:if>
				</xsl:variable>
				<xsl:variable name="linkURL">
					<xsl:choose>
						<xsl:when test="$datasource=''">
							<xsl:value-of select="concat($serverbase, '/Redirect.jsp?uniqueID=',$uniqueID, '&amp;dataSourceName=',$dsname, '&amp;displayFormat=HTML')"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat($serverbase, '/Redirect.jsp?uniqueID=',$uniqueID,'&amp;displayName=', $datasource, '&amp;dataSourceName=',$dsname,'&amp;displayFormat=HTML')"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<td class="thumbnail">
					<a class="thumbnail">
						<xsl:attribute name="href"><xsl:value-of select="$linkURL"/></xsl:attribute>
						<xsl:choose>
							<xsl:when test="string($imageName)=''">
								<xsl:if test="not(string($altImage))=''">
							<xsl:variable name="imageURL">
								<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $altImage)"/>
							</xsl:variable>
							<img>
								<xsl:attribute name="src"><xsl:value-of select="$imageURL"/></xsl:attribute>
							</img>
						</xsl:if>
							</xsl:when>
							<xsl:otherwise>
							<xsl:if test="not(string($imageName))=''">
							<xsl:variable name="imageURL">
								<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $imageName)"/>
							</xsl:variable>
							<img>
								<xsl:attribute name="src"><xsl:value-of select="$imageURL"/></xsl:attribute>
							</img>
						</xsl:if>
							</xsl:otherwise>
						</xsl:choose> 
						
					</a>
				</td>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="findAltImage">
		<xsl:param name="mediaresources"/>
	<xsl:for-each select="$mediaresources">
			
	</xsl:for-each>		
		
	</xsl:template>
	
	<!-- Populate array with common names-->
	<xsl:template name="populateMap">
		<xsl:param name="taxonEntries"/>
		<xsl:param name="mySorter"/>
		<xsl:for-each select="$taxonEntries">
			<xsl:variable name="common_name">
				<xsl:choose>
					<xsl:when test="string(Items[@name=$commonName]/Item)=''">
						<xsl:value-of select="Items[@databaseName=$commonName]/Item"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="Items[@name=$commonName]/Item"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="new-pop" select="sorter:addName($mySorter, string($common_name))"/>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
