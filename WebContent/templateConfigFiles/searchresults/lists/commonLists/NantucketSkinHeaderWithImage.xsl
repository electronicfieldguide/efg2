<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:sorter="project.efg.server.utils.SortedStringArray" xmlns:imageList="project.efg.server.utils.ImageDisplayList" xmlns:displayList="project.efg.server.utils.EFGPageDisplayList" extension-element-prefixes="sorter imageList displayList" exclude-result-prefixes="sorter imageList displayList">
	<xsl:template name="outputCommonRows">
		<tr>
			<td colspan="2" class="title">
				<img align="left" alt="{$title}" title="{$title}" border="0" src="{$imagetitle}"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="descrip">
				<xsl:value-of select="$headLineText"/>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
