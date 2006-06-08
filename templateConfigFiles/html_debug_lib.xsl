<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="debug_out">
		<xsl:param name="msg"/>
		<xsl:param name="value"/>
		<xsl:comment>
			<xsl:value-of select="$msg"/>
			<xsl:value-of select="$value"/>
		</xsl:comment>
	</xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp. <metaInformation> <scenarios/> </metaInformation> -->
