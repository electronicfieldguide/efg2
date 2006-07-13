<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:variable name="images-per-row" select="2"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>Default Taxon Display</title>
			</head>
			<body>
				<table border="1" width="90%" align="center">
					<tr>
						<th>Field Name</th>
						<th>Field Value</th>
					</tr>
					<xsl:apply-templates select="//TaxonEntry"/>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="outputItem">
		<xsl:param name="Item"/>
		<xsl:variable name="counter" select="count($Item)"/>
		<xsl:for-each select="$Item">
			<xsl:value-of select="."/>
			<xsl:if test="position() &lt; $counter">
				<xsl:value-of select="', '"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="outputStatMeasureCur">
		<xsl:param name="StatisticalMeasure"/>
		<xsl:variable name="counter" select="count($StatisticalMeasure)"/>
		<xsl:for-each select="$StatisticalMeasure">
			<xsl:variable name="min" select="@min"/>
			<xsl:variable name="max" select="@max"/>
			<xsl:variable name="units" select="@units"/>
			<xsl:call-template name="outputStatMeasure">
				<xsl:with-param name="min" select="$min"/>
				<xsl:with-param name="max" select="$max"/>
				<xsl:with-param name="units" select="$units"/>
			</xsl:call-template>
			<xsl:if test="position() &lt; $counter">
				<xsl:value-of select="', '"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="efgListsLocal">
		<xsl:param name="caption"/>
		<xsl:param name="efgLists"/>
		<tr>
			<xsl:for-each select="$efgLists/EFGList">
				<xsl:variable name="character" select="."/>
				<xsl:choose>
					<xsl:when test="not(string(@serviceLink))=''">
						<xsl:variable name="serviceLink" select="@serviceLink"/>
						<xsl:variable name="url">
							<xsl:choose>
								<xsl:when test="contains(translate($serviceLink,'abcdefhijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'http;//')">
									<xsl:value-of select="concat(string($serviceLink),'=',string($character))"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat(string($serverbase),'/search?dataSourceName=', $datasource,'&amp;',$serviceLink,'=',$character)"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<td>
							<a>
								<xsl:attribute name="href"><xsl:value-of select="$url"/></xsl:attribute>
								<xsl:value-of select="$character"/>
							</a>
							<xsl:text>&#160;&#160;</xsl:text>
						</td>
					</xsl:when>
					<xsl:otherwise>
						<td>
							<xsl:value-of select="$character"/>
							<xsl:text>&#160;&#160;</xsl:text>
						</td>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</tr>
	</xsl:template>
	<xsl:template match="TaxonEntry">
		<xsl:if test="count(Items) &gt; 0">
			<xsl:variable name="Items" select="Items"/>
			<xsl:for-each select="Items">
				<xsl:sort select="@name"/>
				<tr>
					<td>
						<xsl:value-of select="@name"/>
					</td>
					<td>
						<xsl:call-template name="outputItem">
							<xsl:with-param name="Item" select="Item"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="count(StatisticalMeasures) &gt; 0">
			<xsl:variable name="StatisticalMeasures" select="StatisticalMeasures"/>
			<xsl:for-each select="$StatisticalMeasures">
				<xsl:sort select="@name"/>
				<tr>
					<td>
						<xsl:value-of select="@name"/>
					</td>
					<td>
						<xsl:call-template name="outputStatMeasureCur">
							<xsl:with-param name="StatisticalMeasure" select="StatisticalMeasure"/>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="count(EFGLists) &gt; 0">
			<xsl:variable name="EFGLists" select="EFGLists"/>
			<xsl:for-each select="$EFGLists">
				<xsl:sort select="@name"/>
				<xsl:variable name="caption" select="@name"/>
				<xsl:variable name="lists" select="."/>
				<tr>
					<td>
						<xsl:call-template name="efgLists">
							<xsl:with-param name="caption" select="$caption"/>
							<xsl:with-param name="efgLists" select="$lists"/>
						</xsl:call-template>
					</td>
					<td>
						<table>
							<xsl:call-template name="efgListsLocal">
								<xsl:with-param name="caption" select="$caption"/>
								<xsl:with-param name="efgLists" select="$lists"/>
							</xsl:call-template>
						</table>
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="count(MediaResources) &gt; 0">
			<xsl:for-each select="MediaResources">
				<xsl:sort select="@name"/>
				<xsl:variable name="fieldName" select="@name"/>
				<tr>
					<td>
						<xsl:value-of select="$fieldName"/>
					</td>
					<td>
						<table>
							<xsl:for-each select="MediaResource">
								<td>
									<xsl:call-template name="outputMediaresource">
										<xsl:with-param name="imageName" select="."/>
										<xsl:with-param name="imageCaption" select="@caption"/>
										<xsl:with-param name="type" select="@type"/>
										<xsl:with-param name="otherCaption" select="$fieldName"/>
									</xsl:call-template>
								</td>
							</xsl:for-each>
						</table>
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputMediaresource">
		<xsl:param name="imageName"/>
		<xsl:param name="imageCaption"/>
		<xsl:param name="type"/>
		<xsl:param name="otherCaption"/>
		<xsl:if test="string($type) = string($imagetype)">
			<xsl:variable name="imageURL_thumb">
				<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', string($imageName))"/>
			</xsl:variable>
			<xsl:variable name="imageURL_large">
				<xsl:value-of select="concat($serverbase, '/', $imagebase_large, '/', string($imageName))"/>
			</xsl:variable>
			<a>
				<xsl:attribute name="href"><xsl:value-of select="$imageURL_large"/></xsl:attribute>
				<img>
					<xsl:attribute name="src"><xsl:value-of select="$imageURL_thumb"/></xsl:attribute>
				</img>
			</a>
			<br clear="all"/>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="not(string($imageCaption))=''">
				<xsl:value-of select="$imageCaption"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$otherCaption"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
