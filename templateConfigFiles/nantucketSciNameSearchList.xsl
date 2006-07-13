<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:include href="commonFunctionTemplate.xsl"/>
	<xsl:include href="nantucketCommonSearchTemplate.xsl"/>
	<xsl:template name="outputRow">
		<xsl:param name="uniqueID"/>
		<xsl:param name="firstColumnVar"/>
		<xsl:param name="secondColumnVar"/>
		<xsl:param name="thirdColumnVar"/>
		<xsl:variable name="fieldValue1">
			<xsl:choose>
				<xsl:when test="string($firstColumnVar)=''">
					<xsl:value-of select="string($secondColumnVar)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="concat(string($firstColumnVar),' ',string($secondColumnVar))"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
	
		<tr>
			<xsl:call-template name="outputCol">
				<xsl:with-param name="uniqueID" select="$uniqueID"/>
				<xsl:with-param name="fieldValue" select="$fieldValue1"/>
				<xsl:with-param name="tdClassname" select="'sciname'"/>
				<xsl:with-param name="aClassname" select="'sciname1st'"/>
			</xsl:call-template>
			<xsl:call-template name="outputCol">
				<xsl:with-param name="uniqueID" select="$uniqueID"/>
				<xsl:with-param name="fieldValue" select="$thirdColumnVar"/>
				<xsl:with-param name="tdClassname" select="'commonname'"/>
				<xsl:with-param name="aClassname" select="'commonname2nd'"/>
			</xsl:call-template>
		</tr>
	</xsl:template>
	<xsl:template name="outputFirstTableData">
		<xsl:param name="header1"/>
		<xsl:param name="header2"/>
		<td class="sciheader">
			<xsl:value-of select="$header1"/>
		</td>
		<td class="commonheader">
			<xsl:value-of select="$header2"/>
		</td>
	</xsl:template>
</xsl:stylesheet>
