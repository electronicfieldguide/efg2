<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:variable name="xslPage" select="//TaxonPageTemplate[@datasourceName=$dataSourceName][1]/XSLFileNames/xslTaxonPages/xslPage[@fileName=$xslName and @guid=$guid]"/>
	<xsl:variable name="bottomLinks" select="$xslPage/groups/group[@label='bottomLinks']"/>
	<xsl:template name="outputbottomLinks">
		<xsl:param name="links"/>
		<xsl:if test="count($links/characterValue) &gt; 0">
			<hr/>
			<p class="link">
			<xsl:for-each select="$links/characterValue">
		    <xsl:variable name="linkhref" select="@value"/>
						
			<a class="link" href="{$linkhref}" target="target1">
				<xsl:value-of select="@label"/>
			</a>
			</xsl:for-each>
			</p>
		</xsl:if>		
	</xsl:template>
	
</xsl:stylesheet>
