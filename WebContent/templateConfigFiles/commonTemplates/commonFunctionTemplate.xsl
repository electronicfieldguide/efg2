<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">
	<!-- $Id$ -->
	<xsl:template name="substring-after-last">
		<xsl:param name="string"/>
		<xsl:param name="delimiter"/>
		<xsl:choose>
			<xsl:when test="contains($string, $delimiter)">
				<xsl:call-template name="substring-after-last">
					<xsl:with-param name="string" select="substring-after($string, $delimiter)"/>
					<xsl:with-param name="delimiter" select="$delimiter"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$string"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="findColumnVariables">
		<xsl:param name="items"/>
		<xsl:param name="stats"/>
		<xsl:param name="meds"/>
		<xsl:param name="lists"/>
		<xsl:param name="columnName"/>
		<xsl:variable name="firstItem">
			<xsl:call-template name="searchItems">
				<xsl:with-param name="items" select="$items"/>
				<xsl:with-param name="fieldName" select="$columnName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string($firstItem)=''">
				<!-- No item exist with the same @name as firstColumn -->
				<xsl:variable name="firstStats">
					<xsl:call-template name="searchStatsMeasure">
						<xsl:with-param name="stats" select="$stats"/>
						<xsl:with-param name="fieldName" select="$columnName"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string($firstStats)=''">
						<!-- No StatisticalMeasure exist with the same @name as firstColumn -->
						<xsl:variable name="firstLists">
							<xsl:call-template name="searchEFGLists">
								<xsl:with-param name="efglists" select="$lists"/>
								<xsl:with-param name="fieldName" select="$columnName"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="string($firstLists)=''">
								<!-- No EFGLists exist with the same @name as firstColumn. Check and output a media resource -->
								<xsl:variable name="firstMedia">
									<xsl:call-template name="searchMediaResources">
										<xsl:with-param name="mediaresources" select="$meds"/>
										<xsl:with-param name="fieldName" select="$columnName"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:value-of select="$firstMedia"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$firstLists"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$firstStats"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$firstItem"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="searchItems">
		<xsl:param name="items"/>
		<xsl:param name="fieldName"/>
		<xsl:for-each select="$items/Item">
			<xsl:call-template name="getItem">
				<xsl:with-param name="item" select="."/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="searchEFGLists">
		<xsl:param name="efglists"/>
		<xsl:param name="fieldName"/>
		<xsl:for-each select="$efglists">
			<xsl:if test="@name=$fieldName">
				<xsl:call-template name="getItem">
					<xsl:with-param name="item" select="EFGList"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="searchMediaResources">
		<xsl:param name="mediaresources"/>
		<xsl:param name="fieldName"/>
		<xsl:for-each select="$mediaresources">
			<xsl:if test="@name=$fieldName">
				<xsl:call-template name="getItem">
					<xsl:with-param name="item" select="MediaResource"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="getItem">
		<xsl:param name="item"/>
		<xsl:for-each select="$item">
			<xsl:if test="position() > 1">
				<xsl:value-of select="','"/>
			</xsl:if>
			<xsl:value-of select="."/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="searchStatsMeasure">
		<xsl:param name="stats"/>
		<xsl:param name="fieldName"/>
		<xsl:for-each select="$stats">
			<xsl:if test="@name=$fieldName">
				<xsl:call-template name="getStats">
					<xsl:with-param name="stat" select="StatisticalMeasure"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="getStats">
		<xsl:param name="stat"/>
		<xsl:for-each select="$stat">
			<xsl:if test="position() > 1">
				<xsl:value-of select="','"/>
			</xsl:if>
			<xsl:value-of select="@min"/>
			<xsl:value-of select="' - '"/>
			<xsl:value-of select="@max"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="getVariable">
		<xsl:param name="groups"/>
		<xsl:param name="groupID"/>
		<xsl:param name="groupRank"/>
		<xsl:param name="characterRank"/>
		<xsl:choose>
			<xsl:when test="$groups/group[@id=$groupID and @rank=$groupRank]/characterValue">
				<xsl:value-of select="$groups/group[@id=$groupID and @rank=$groupRank]/characterValue[@rank=$characterRank]/@value"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="''"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
