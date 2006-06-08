<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:variable name="groups" select="document(string($searchTemplateConfig))//TaxonPageTemplate[@datasourceName=string($datasource)]/groups"/>
	<xsl:variable name="title">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=1]/characterValue">
				<xsl:value-of select="$groups/group[@id=1]/characterValue/@label"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat('Search results page for  ',$datasource)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="imageField">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=2]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=2]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="captionField1">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=3]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=3]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="captionField2">
		<xsl:choose>
			<xsl:when test="$groups/group[@id=4]/characterValue[@rank=1]">
				<xsl:value-of select="$groups/group[@id=4]/characterValue[@rank=1]/@value"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:template match="/">
		<html>
			<head>
			<title><xsl:value-of select="$title"/></title>
				
			</head>
			<body>
				<h2 align="center">
					<xsl:value-of select="string($title)"/>
				</h2>
				<xsl:variable name="total_count" select="count(//TaxonEntry)"/>
				<h3 align="center">Your search found <xsl:value-of select="$total_count"/> results</h3>
				<table border="0" width="100%">
					<!-- Apply to all TaxonEntry elements-->
					<xsl:apply-templates select="//TaxonEntry">
							<xsl:with-param name="total_count" select="$total_count"/>
					</xsl:apply-templates>
				</table>
			</body>
		</html>
	</xsl:template>
	<!-- Handles each taxonEntry element-->
	<xsl:template match="TaxonEntry">
	<xsl:param name="total_count"/>
		<!-- Get the position of the current TaxonEntry-->
		<xsl:param name="current_pos" select="position()"/>
		<!-- Display 3 taxa per row-->
		<xsl:if test="$current_pos mod 3 = 1">
			<tr>
				<!-- displays html images in an html table -->
				<xsl:call-template name="display-images">
					<xsl:with-param name="current_taxon" select="self::TaxonEntry"/>
					<xsl:with-param name="dataSourceName" select="self::TaxonEntry/@dataSourceName"/>
				</xsl:call-template>
				<!-- Check to see that we are not processing the last element. It is handled elsewhere-->
				<xsl:if test="not($current_pos  = $total_count)">
					<xsl:call-template name="display-images">
						<xsl:with-param name="current_taxon" select="following-sibling::TaxonEntry[1]"/>
						<xsl:with-param name="dataSourceName" select="following-sibling::TaxonEntry[1]/@dataSourceName"/>
					</xsl:call-template>
					<!-- Check to see that we are not processing the last but one element. It is handled elsewhere-->
					<xsl:if test="not($current_pos + 1 = $total_count)">
						<xsl:call-template name="display-images">
							<xsl:with-param name="current_taxon" select="following-sibling::TaxonEntry[2]"/>
							<xsl:with-param name="dataSourceName" select="following-sibling::TaxonEntry[2]/@dataSourceName"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- outputs images if it exists -->
	<xsl:template name="display-images">
		<xsl:param name="current_taxon"/>
		<xsl:param name="dataSourceName"/>
		<td align="center">
			<!-- Choose the first name in the list of Item elements or perhaps -->
			<xsl:variable name="sci_name1">
				<xsl:if test="not(string($captionField1)) = ''">
					<xsl:for-each select="$current_taxon/Items[@name=string($captionField1)]/Item">
						<xsl:value-of select="."/>
						<xsl:if test="position() != last() ">
							<xsl:value-of select="', '"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="sci_name2">
				<xsl:if test="not(string($captionField2)) = ''">
					<xsl:for-each select="$current_taxon/Items[@name=string($captionField2)]/Item">
						<xsl:value-of select="."/>
						<xsl:if test="position() != last() ">
							<xsl:value-of select="', '"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
			</xsl:variable>
			<!-- Choose the first name in the list -->
			<xsl:variable name="imageName">
				<xsl:choose>
					<xsl:when test="not(string($imageField))">
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="$current_taxon/MediaResources">
							<xsl:if test="$current_taxon/MediaResources[@name=$imageField]">
								<xsl:if test="$current_taxon/MediaResources[@name=$imageField]/MediaResource">
									<!-- 
										Fix to show all images later ..Perhaps find a way to indicate which image should be shown. 
										For instance if order matters then all images should be shown
								-->
									<xsl:value-of select="$current_taxon/MediaResources[@name=$imageField]/MediaResource[1]"/>
								</xsl:if>
							</xsl:if>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<!-- get Caption to use for image if one exists -->
			<xsl:variable name="imageCaption">
				<xsl:choose>
					<xsl:when test="not(string($imageName))=''">
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="not(string($current_taxon/MediaResources[@name=$imageField]/MediaResource[1]/@caption))">
					</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$current_taxon/MediaResources[@name=$imageField]/MediaResource[1]/@caption"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<!-- Get the link to use for the caption/label -->
			<xsl:variable name="linkURL">
				<xsl:choose>
					<xsl:when test="not(string($sci_name1))='' and not(string($sci_name2))=''">
						<xsl:value-of select="concat(string($query), string($captionField1), '=',string($sci_name1),'&amp;',string($captionField2),'=',string($sci_name2),'&amp;maxDisplay=1')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="not(string($sci_name1))=''">
							<xsl:value-of select="concat($query, $captionField1, '=', $sci_name1,'&amp;maxDisplay=1')"/>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<a>
				<xsl:attribute name="href"><xsl:value-of select="$linkURL"/></xsl:attribute>
				<xsl:choose>
					<xsl:when test="not(string($imageField))">
					
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="imageURL">
							<xsl:value-of select="concat($serverbase, '/', $imagebase, '/', $imageName)"/>
						</xsl:variable>
						<img>
							<xsl:attribute name="src"><xsl:value-of select="$imageURL"/></xsl:attribute>
						</img>
						<br clear="all"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="string($imageCaption)=''">
						<xsl:value-of select="concat($sci_name1,'  ',$sci_name2)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$imageCaption"/>
					</xsl:otherwise>
				</xsl:choose>
			</a>
		</td>
	</xsl:template>
</xsl:stylesheet>
