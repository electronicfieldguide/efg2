<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:sorter="project.efg.server.utils.SortedStringArray" xmlns:displayList="project.efg.server.utils.EFGPageDisplayList" extension-element-prefixes="sorter displayList" exclude-result-prefixes="sorter displayList">
	<xalan:component prefix="sorter" functions="sort addName  getName  getArraySize">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.SortedStringArray"/>
	</xalan:component>
	<xalan:component prefix="displayList" functions="sort addDisplay  getItemName getImageCaption getImageName getSize getUniqueID">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.EFGPageDisplayList"/>
	</xalan:component>
	<xsl:variable name="bodyclass" select="'searchresults'"/>
	<xsl:include href="commonPlates/SearchResultsCommonSkin.xsl"/>
</xsl:stylesheet>
