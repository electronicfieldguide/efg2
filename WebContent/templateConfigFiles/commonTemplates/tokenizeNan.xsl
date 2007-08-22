<?xml version="1.0" encoding="utf-8"?>
<!-- $Id: tokenizeNan.xsl,v 1.1.1.1 2007/08/01 19:11:35 kasiedu Exp $ -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="tokenize2">
		<xsl:param name="toke"/>
		<li><xsl:value-of select="$toke"/></li>
	</xsl:template>
	<!-- Start tokenizing -->
	<xsl:template name="tokenize">
		<xsl:param name="stringToTokenize"/>
		<xsl:choose>
			<xsl:when test="contains($stringToTokenize,';')">
				<xsl:variable name="toke" select="substring-before($stringToTokenize, ';')"/>
				<xsl:variable name="restOfString" select="substring-after($stringToTokenize, ';')"/>
				<xsl:call-template name="tokenize2">
					<xsl:with-param name="toke" select="$toke"/>
				</xsl:call-template>
				<xsl:call-template name="tokenize">
					<xsl:with-param name="stringToTokenize" select="$restOfString"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="tokenize2">
					<xsl:with-param name="toke" select="$stringToTokenize"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
