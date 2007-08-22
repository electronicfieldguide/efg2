<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:variable name="date" select="Date:new()" xmlns:Date="xalan://java.util.Date"/>
	<xsl:template name="outputDate">
		<xsl:variable name="today">
			<xsl:value-of select="concat(substring($date,9,2), '  ',
						substring($date, 5,3), '  ', 
						substring($date,25, 4)
						)"/>
		</xsl:variable>
		<b>Ceated: </b>
		<xsl:value-of select="$today"/>
		<b>  Updated: </b>
		<xsl:value-of select="$today"/>
	</xsl:template>
</xsl:stylesheet>
