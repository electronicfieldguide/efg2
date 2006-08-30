<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:sorter="project.efg.util.SortedStringArray" extension-element-prefixes="sorter" exclude-result-prefixes="sorter">
	<xalan:component prefix="sorter" functions="sort addName  getName  getArraySize">
		<xalan:script lang="javaclass" src="xalan://project.efg.util.SortedStringArray"/>
	</xalan:component>
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:include href="commonFunctionTemplate.xsl"/>
	<xsl:include href="commonJavaFunctions.xsl"/>
	<xsl:include href="xslPagePlate.xsl"/>
	<xsl:variable name="defaultcss" select="'nantucketstyleplates.css'"/>
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
	<xsl:variable name="mediaResourceField" select="$xslPage/groups/group[@label='images']/characterValue[1]/@value"/>
	<xsl:variable name="caption1" select="$xslPage/groups/group[@label='captions']/characterValue[1]/@value"/>
	<xsl:variable name="caption2" select="$xslPage/groups/group[@label='captions']/characterValue[2]/@value"/>
	<xsl:variable name="images-per-row" select="3"/>
	<xsl:template match="/">
		<xsl:variable name="dsname" select="$dataSourceName"/>
		<xsl:variable name="total_count" select="count(//TaxonEntry)"/>
		<xsl:variable name="mySorter" select="sorter:new($total_count)"/>
		<html>
			<head>
				<xsl:variable name="css_loc" select="concat($css_home,$cssFile)"/>
				<link rel="stylesheet" href="{$css_loc}"/>
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
						<xsl:with-param name="caption1" select="$caption1"/>
						<xsl:with-param name="caption2" select="$caption2"/>
					</xsl:call-template>
					<!-- Sort the list -->
					<xsl:variable name="sortList" select="sorter:sort($mySorter)"/>
					<xsl:variable name="maxCount" select="sorter:getArraySize($mySorter)"/>
					<xsl:variable name="index" select="0"/>
					<xsl:if test="$index &lt; $maxCount">
						<xsl:call-template name="iterateOverSortedArray">
							<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
							<xsl:with-param name="mySorter" select="$mySorter"/>
							<xsl:with-param name="index" select="$index"/>
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
		<xsl:param name="total_count"/>
		<xsl:if test="$index mod $images-per-row = 0">
			<tr>
				<xsl:call-template name="display-images">
					<xsl:with-param name="index" select="$index"/>
					<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
					<xsl:with-param name="mySorter" select="$mySorter"/>
				</xsl:call-template>
				<xsl:if test="not($index + 1= $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="index" select="$index + 1"/>
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="not($index + 2 = $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="index" select="$index + 2"/>
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
					</xsl:call-template>
				</xsl:if>
			</tr>
			
			<tr>
				<xsl:call-template name="display-captions">
					<xsl:with-param name="index" select="$index"/>
					<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
					<xsl:with-param name="mySorter" select="$mySorter"/>
				</xsl:call-template>
				<xsl:if test="not($index + 1 = $total_count)">
					<xsl:call-template name="display-captions">
						<xsl:with-param name="index" select="$index + 1"/>
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="not($index + 2 = $total_count)">
					<xsl:call-template name="display-captions">
						<xsl:with-param name="index" select="$index + 2"/>
						<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
						<xsl:with-param name="mySorter" select="$mySorter"/>
					</xsl:call-template>
				</xsl:if>
			</tr>
			<tr>
				<xsl:call-template name="display-spacers"/>
			</tr>
		</xsl:if>
		<xsl:variable name="index2" select="number($index) + 3"/>
		<xsl:if test="number($index2) &lt; number($total_count) ">
			<xsl:call-template name="iterateOverSortedArray">
				<xsl:with-param name="taxonEntries" select="$taxonEntries"/>
				<xsl:with-param name="mySorter" select="$mySorter"/>
				<xsl:with-param name="index" select="$index2"/>
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
		<xsl:variable name="pos" select="number($index)"/>
		<xsl:variable name="currentname" select="sorter:getName($mySorter, string(number($pos)))"/>
		<xsl:if test="not(string($currentname))=''">

	
		<xsl:for-each select="$taxonEntries">
			<xsl:variable name="cap1">
				<xsl:value-of select="Items[@name=$caption1]/Item[1]"/>
			</xsl:variable>
			<xsl:variable name="cap2">
				<xsl:value-of select="Items[@name=$caption2]/Item[1]"/>
			</xsl:variable>
			<xsl:variable name="caption">
				<xsl:call-template name="outputCaption">
					<xsl:with-param name="cap1" select="$cap1"/>
					<xsl:with-param name="cap2" select="$cap2"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:if test="string($caption)=string($currentname)">
				<xsl:variable name="uniqueID">
					<xsl:value-of select="@recordID"/>
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
				<td class="caption">
					<a class="caption" href="{$linkURL}">
						<xsl:value-of select="$currentname"/>
					</a>
				</td>
			</xsl:if>
		</xsl:for-each>
			</xsl:if>
		<!-- Fields are aggregated and that needs to be solved also output a generic page if transformation fails-->
	</xsl:template>
	<xsl:template name="display-images">
		<xsl:param name="index"/>
		<xsl:param name="taxonEntries"/>
		<xsl:param name="mySorter"/>
		<xsl:param name="dsname"/>
		<xsl:variable name="pos" select="number($index)"/>
		<xsl:variable name="currentname" select="sorter:getName($mySorter, string(number($pos)))"/>
		<xsl:for-each select="$taxonEntries">
		<!-- -->
			<xsl:variable name="cap1">
				<xsl:value-of select="Items[@name=$caption1]/Item[1]"/>
			</xsl:variable>
			<xsl:variable name="cap2">
				<xsl:value-of select="Items[@name=$caption2]/Item[1]"/>
			</xsl:variable>
			<xsl:variable name="caption">
				<xsl:call-template name="outputCaption">
					<xsl:with-param name="cap1" select="$cap1"/>
					<xsl:with-param name="cap2" select="$cap2"/>
				</xsl:call-template>
			</xsl:variable>
<xsl:if test="not(string($currentname))=''">
<xsl:if test="not(string($caption))=''">
			<xsl:if test="string($caption)=string($currentname)">
				<xsl:variable name="uniqueID">
					<xsl:value-of select="@recordID"/>
				</xsl:variable>
				<xsl:variable name="imageName">
					<xsl:if test="not($mediaResourceField)=''">
						<xsl:if test="count(MediaResources) &gt; 0">
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
							<xsl:value-of select="concat($hrefCommon,$uniqueID)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat($hrefCommon,$uniqueID,'&amp;displayName=', $datasource)"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<td class="thumbnail">
					<a class="thumbnail" href="{$linkURL}">
						<xsl:choose>
							<xsl:when test="string($imageName)=''">
								<xsl:if test="not(string($altImage))=''">
									<xsl:variable name="imageURL">
										<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $altImage)"/>
									</xsl:variable>
									<img src="{$imageURL}"/>
								</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="not(string($imageName))=''">
									<xsl:variable name="imageURL">
										<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $imageName)"/>
									</xsl:variable>
									<img src="{$imageURL}"/>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
					</a>
		
				</td>
			</xsl:if>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
