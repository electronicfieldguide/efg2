<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="commonLists/ListLevelUpHeader.xsl"/>
	<xsl:template name="outputnameLevelUp">
		<xsl:param name="hrefCurrent"/>
		<xsl:param name="leftColumn1"/>
		<xsl:param name="leftColumn2"/>
		<xsl:param name="rightColumn1"/>
		<xsl:param name="rightColumn2"/>
		
		<td class="sciname">
			<a class="sciname1st" href="{$hrefCurrent}">
				<xsl:value-of select="concat($leftColumn1,' ',$leftColumn2)"/>
			</a>
		</td>
		<td class="commonname">
			<a class="commonname2nd" href="{$hrefCurrent}">
				<xsl:value-of select="concat($rightColumn1, ' ',$rightColumn2)"/>
			</a>
		</td>
	</xsl:template>
</xsl:stylesheet>
