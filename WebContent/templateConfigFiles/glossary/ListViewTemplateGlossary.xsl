<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:glossaryMaker="project.efg.server.utils.GlossaryMaker" xmlns:glossaryObject="project.efg.server.utils.GlossaryObject" extension-element-prefixes="glossaryMaker glossaryObject" exclude-result-prefixes="glossaryMaker glossaryObject">
	<xalan:component prefix="glossaryMaker" functions="addGlossaryObject getNumberOfTerms getTerm getNumberOfDefinitions getDefinition getAlphabet getNumberOfAlphabets getNumberOfMediaResources getMediaResource getNumberOfAlsoSees getAlsoSee">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.GlossaryMaker"/>
	</xalan:component>
	<xalan:component prefix="glossaryObject" functions="setTerm addDefinition  addAlsoSee  addMediaresource getTerm getDefinitions getMediaResouces getAlsoSee">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.GlossaryObject"/>
	</xalan:component>
	<xsl:include href="../searchresults/lists/commonLists/xslPageList.xsl"/>
	<xsl:include href="GlossaryView.xsl"/>
</xsl:stylesheet>
