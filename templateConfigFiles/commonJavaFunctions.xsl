<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:sorter="project.efg.util.SortedStringArray" xmlns:imageList="project.efg.util.ImageDisplayList" extension-element-prefixes="sorter imageList" exclude-result-prefixes="sorter imageList">
	<xalan:component prefix="imageList" functions="addImageDisplay getImageName  getImageCaption getSize">
		<xalan:script lang="javaclass" src="xalan://project.efg.util.ImageDisplayList"/>
	</xalan:component>
	<xalan:component prefix="sorter" functions="sort addName  getName  getArraySize">
		<xalan:script lang="javaclass" src="xalan://project.efg.util.SortedStringArray"/>
	</xalan:component>
	<!-- $Id$ -->
	<xsl:template name="outputCaption">
		<xsl:param name="cap1"/>
		<xsl:param name="cap2"/>
		<xsl:variable name="caption">
			<xsl:choose>
				<xsl:when test="not(string($cap2))=''">
					<xsl:value-of select="concat($cap1,' ',$cap2)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$cap1"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:value-of select="$caption"/>
	</xsl:template>
	<xsl:template name="populateMap">
		<xsl:param name="taxonEntries"/>
		<xsl:param name="mySorter"/>
		<xsl:param name="caption1"/>
		<xsl:param name="caption2"/>
		<xsl:for-each select="$taxonEntries">
			<xsl:variable name="cap1">
				<xsl:value-of select="Items[@name=$caption1]/Item"/>
			</xsl:variable>
			<xsl:variable name="cap2">
				<xsl:value-of select="Items[@name=$caption2]/Item"/>
			</xsl:variable>
			<xsl:variable name="caption">
				<xsl:call-template name="outputCaption">
					<xsl:with-param name="cap1" select="$cap1"/>
					<xsl:with-param name="cap2" select="$cap2"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:if test="not(string($caption))=''">
				<xsl:variable name="new-pop" select="sorter:addName($mySorter, string($caption))"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="populateImageList">
		<xsl:param name="mediaresources"/>
		<xsl:param name="myImageList"/>
		<xsl:param name="caption"/>
		<xsl:variable name="fieldName" select="@name"/>
		<xsl:for-each select="$mediaresources/MediaResource">
			<xsl:variable name="caps">
				<xsl:choose>
					<xsl:when test="not(string(@caption))=''">
						<xsl:value-of select="@caption"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="not(string($caption))=''">
								<xsl:value-of select="$caption"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$fieldName"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="med">
				<xsl:value-of select="."/>
			</xsl:variable>
			<xsl:if test="not(string($med))=''">
				<xsl:variable name="new-pop" select="imageList:addImageDisplay($myImageList, string($med),string($caps))"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
