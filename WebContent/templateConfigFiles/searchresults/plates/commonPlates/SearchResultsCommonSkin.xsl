<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:sorter="project.efg.server.utils.SortedStringArray" xmlns:displayList="project.efg.server.utils.EFGPageDisplayList" extension-element-prefixes="sorter displayList" exclude-result-prefixes="sorter displayList">
	<xalan:component prefix="sorter" functions="sort addName  getName  getArraySize">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.SortedStringArray"/>
	</xalan:component>
	<xalan:component prefix="displayList" functions="sort addDisplay  getItemName getImageCaption getImageName getSize getUniqueID">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.EFGPageDisplayList"/>
	</xalan:component>
	<xsl:include href="../../../commonTemplates/commonTaxonPageTemplate.xsl"/>
	<xsl:include href="../../../commonTemplates/commonFunctionTemplate.xsl"/>
	<xsl:include href="../../../commonTemplates/commonJavaFunctions.xsl"/>
	<xsl:include href="xslPagePlate.xsl"/>
	<xsl:include href="../../../commonTemplates/PrevNextTemplate.xsl"/>
	
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
	<xsl:variable name="item">
		<xsl:choose>
			<xsl:when test="not(string($caption1))=''">
				<xsl:value-of select="$caption1"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$caption2"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="images-per-row" select="3"/>
	<xsl:variable name="myDisplayList" select="displayList:new()"/>
	<xsl:template match="/">
		<xsl:variable name="dsname" select="$dataSourceName"/>
		<xsl:variable name="total_count" select="count(//TaxonEntry)"/>
		<xsl:variable name="mySorter" select="sorter:new($total_count)"/>
		<xsl:variable name="tt_count">
		<xsl:call-template name="write_total_count">
		<xsl:with-param name="taxon_count" select="$taxon_count"/>
		<xsl:with-param name="total_count" select="$total_count"/>
		</xsl:call-template>
		</xsl:variable>	
		<html>
			<head>
				<xsl:variable name="css_loc" select="concat($css_home,$cssFile)"/>
				<link rel="stylesheet" href="{$css_loc}"/>
			</head>
			<body class="{$bodyclass}">

				<div id="numresults">Your search found <span class="num">
					<xsl:value-of select="$tt_count"/>		
				</span> results: </div>
				<table class="resultsdisplay">
				
					<xsl:apply-templates select="//TaxonEntry">
						<xsl:sort data-type="text" order="ascending" select="Items[@name=$item]/Item[1]"/>
						<xsl:sort data-type="text" order="ascending" select="Items[@name=$caption2]/Item[1]"/>
					</xsl:apply-templates>
					<xsl:variable name="sortDisplayList" select="displayList:sort($myDisplayList)"/>
					<!-- Sort the list -->
					<xsl:variable name="maxCount" select="displayList:getSize($myDisplayList)"/>
					<xsl:variable name="index" select="0"/>
					<xsl:if test="$index &lt; $maxCount">
						<xsl:call-template name="iterateOverSortedArray">
							<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
							<xsl:with-param name="index" select="$index"/>
							<xsl:with-param name="total_count" select="$maxCount"/>
						</xsl:call-template>
					</xsl:if>
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
		<xsl:variable name="imageName" select="MediaResources[@name=$mediaResourceField]/MediaResource[1]"/>
		<xsl:variable name="imageCaption" select="MediaResources[@name=$mediaResourceField]/MediaResource[1]/@caption"/>
		<xsl:variable name="uniqueID" select="@recordID"/>
		<xsl:variable name="cap1" select="Items[@name=$caption1]/Item[1]"/>
		<xsl:variable name="cap2" select="Items[@name=$caption2]/Item[1]"/>
		<xsl:variable name="itemName">
			<xsl:call-template name="outputCaption">
				<xsl:with-param name="cap1" select="$cap1"/>
				<xsl:with-param name="cap2" select="$cap2"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:if test="not(string($itemName))=''">
		<xsl:call-template name="populateDisplayList">
			<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
			<xsl:with-param name="uniqueID" select="$uniqueID"/>
			<xsl:with-param name="itemName" select="string($itemName)"/>
			<xsl:with-param name="imageName" select="string($imageName)"/>
			<xsl:with-param name="imageCaption" select="string($imageCaption)"/>
		</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="iterateOverSortedArray">
		<xsl:param name="myDisplayList"/>
		<xsl:param name="index"/>
		<xsl:param name="total_count"/>
		<xsl:if test="$index mod $images-per-row = 0">
		<!---->
			<tr>
				<xsl:call-template name="display-images">
					<xsl:with-param name="index" select="$index"/>
					<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
				</xsl:call-template>
				<xsl:if test="not($index + 1= $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="index" select="$index + 1"/>
						<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="not($index + 2 = $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="index" select="$index + 2"/>
						<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
					</xsl:call-template>
				</xsl:if>
			</tr>

			<tr>
				<xsl:call-template name="display-captions">
					<xsl:with-param name="index" select="$index"/>
					<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
				</xsl:call-template>
				<xsl:if test="not($index + 1 = $total_count)">
					<xsl:call-template name="display-captions">
						<xsl:with-param name="index" select="$index + 1"/>
						<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="not($index + 2 = $total_count)">
					<xsl:call-template name="display-captions">
						<xsl:with-param name="index" select="$index + 2"/>
						<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
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
				<xsl:with-param name="myDisplayList" select="$myDisplayList"/>
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
		<xsl:param name="myDisplayList"/>
		<xsl:variable name="uniqueID" select="displayList:getUniqueID($myDisplayList,string($index))"/>
		<xsl:variable name="itemname" select="displayList:getItemName($myDisplayList, string($index))"/>
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
		<xsl:if test="not(string($itemname))=''">
		<td class="resultscaption">
		
			<a class="resultscaption" href="{$linkURL}">
				<xsl:value-of select="$itemname"/>
			</a>
		</td>
		</xsl:if>
		<!-- Fields are aggregated and that needs to be solved also output a generic page if transformation fails-->
	</xsl:template>
	<xsl:template name="display-images">
		<xsl:param name="index"/>
		<xsl:param name="myDisplayList"/>
		<xsl:variable name="uniqueID" select="displayList:getUniqueID($myDisplayList, string($index))"/>
		<xsl:variable name="imagename" select="displayList:getImageName($myDisplayList, string($index))"/>
		<xsl:variable name="imagecaption" select="displayList:getImageCaption($myDisplayList,string($index))"/>
		<xsl:variable name="altImage">
			<xsl:choose>
				<xsl:when test="not(string($imagecaption))=''">
					<xsl:value-of select="$imagecaption"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$imagename"/>
				</xsl:otherwise>
			</xsl:choose>
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
		<xsl:if test="not(string($uniqueID))=''">
		
		<td class="resultsthumb">
			<a class="resultsthumb" href="{$linkURL}">
				<xsl:choose>
					<xsl:when test="not(string($imagename))=''">
						<xsl:variable name="imageURL">
							<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $imagename)"/>
						</xsl:variable>
						<img src="{$imageURL}" class="{$bodyclass}"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="not(string($altImage))=''">
							<xsl:variable name="imageURL">
								<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $altImage)"/>
							</xsl:variable>
							<img src="{$imageURL}" class="{$bodyclass}"/>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</a>
		</td>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
