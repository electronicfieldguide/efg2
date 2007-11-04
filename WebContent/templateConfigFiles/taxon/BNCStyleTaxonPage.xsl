<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:imageList="project.efg.server.utils.ImageDisplayList" extension-element-prefixes="imageList" exclude-result-prefixes="imageList">
	<xalan:component prefix="imageList" functions="addImageDisplay getImageName  getImageCaption getSize">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.ImageDisplayList"/>
	</xalan:component>
	<xsl:import  href="NantucketSkinForNavNecTaxonPage.xsl"/>

	</xsl:stylesheet>
