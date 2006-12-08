<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2006 sp2 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- $Id$ -->
	<xsl:param name="serverbase"/>
	<!-- The name of the template configuration for the current datasource. Must be a path relative to the location of this XSL file-->
	<xsl:param name="dataSourceName"/>
	<xsl:param name="ALL_TABLE_NAME"/>
	<xsl:param name="datasource"/>
	<xsl:param name="displayName"/>
	<xsl:param name="search"/>
	<xsl:param name="mediaResourceField"/>
	<xsl:param name="guid"/>
	<xsl:param name="templateConfigFile" select="concat('xml/',$dataSourceName,'.xml')"/>
	<xsl:param name="template_css_dir" select="'/templateCSSDirectory/'"/>
	<xsl:param name="template_images_dir" select="'/templateImagesDirectory/'"/>
	<xsl:param name="css_home" select="concat($serverbase,$template_css_dir)"/>
	<xsl:param name="template_images_home" select="concat($serverbase,$template_images_dir)"/>
	<xsl:param name="fieldName"/>
	<xsl:param name="xslName"/>
	<xsl:param name="isTrue" select="'true'"/>
	<xsl:param name="isFalse" select="'false'"/>
	<xsl:param name="serviceLinkFiller" select="'_EFG_'"/>
	<xsl:param name="imagebase" select="'EFGImages'"/>
	<xsl:param name="imagebase_thumbs" select="'efgimagesthumbs'"/>
	<xsl:param name="imagebase_large" select="'EFGImages'"/>
	<xsl:param name="imagetype" select="'Image'"/>
	<xsl:param name="audiotype" select="'Audio'"/>
	<xsl:param name="vidoetype" select="'Video'"/>
	<xsl:param name="header"/>
	<xsl:variable name="colon">:</xsl:variable>
	<xsl:variable name="searchTemplateConfig" select="concat($dataSourceName,$search)"/>
	<xsl:variable name="searchServlet" select="'/search?dataSourceName='"/>
	<xsl:variable name="alldbname" select="concat('ALL_TABLE_NAME=',$ALL_TABLE_NAME)"/>

	<xsl:param name="query" select="concat($serverbase,$searchServlet,$dataSourceName,'&amp;',$alldbname,'&amp;')"/>
	<xsl:variable name="hrefCommon" select="concat($serverbase,'/Redirect.jsp?displayFormat=html&amp;dataSourceName=',$dataSourceName,'&amp;',$alldbname,'&amp;uniqueID=')"/>
	<xsl:variable name="mapQueryServlet" select="concat('/mapQuery?',$alldbname,'&amp;')"/>
	<xsl:variable name="dsNamePrefix" select="'dataSourceName='"/>
	<xsl:variable name="endDigirRequest">
		<xsl:text>&lt;/request&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="startDigirFilter">
		<xsl:text>&lt;filter&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="endDigirFilter">
		<xsl:text>&lt;/filter&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="startDigirSearch">
		<xsl:text>&lt;search&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="endDigirSearch">
		<xsl:text>&lt;/search&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="startDigirOR">
		<xsl:text>&lt;or&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="endDigirOR">
		<xsl:text>&lt;/or&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="startDigirAND">
		<xsl:text>&lt;and&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="endDigirAND">
		<xsl:text>&lt;/and&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="startDigirEQUALS">
		<xsl:text>&lt;equals&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="endDigirEQUALS">
		<xsl:text>&lt;/equals&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="records">
		<xsl:text>&lt;records</xsl:text>
		<xsl:text/>
		<xsl:text> start=</xsl:text>
		<xsl:text> "0"</xsl:text>
		<xsl:text> limit=</xsl:text>
		<xsl:text> "100"</xsl:text>
		<xsl:text> &gt;</xsl:text>
		<xsl:text> &lt;structure/&gt;</xsl:text>
		<xsl:text>&lt;/records&gt;</xsl:text>
		<xsl:text>&lt;count&gt;</xsl:text>
		<xsl:text>true</xsl:text>
		<xsl:text>&lt;/count&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="startDigirRequest">
		<xsl:text>&lt;request</xsl:text>
		<xsl:text> xmlns="http://digir.net/schema/protocol/2003/1.0"</xsl:text>
		<xsl:text> xmlns:darwin=</xsl:text>
		<xsl:text>"http://digir.net/schema/conceptual/darwin/2003/1.0" </xsl:text>
		<xsl:text> xmlns:xsi=</xsl:text>
		<xsl:text>"http://www.w3.org/2001/XMLSchema-instance"</xsl:text>
		<xsl:text>&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="digirHeaders" select="concat(string($startDigirRequest),string($header),string($startDigirSearch),string($startDigirFilter))"/>
	<xsl:variable name="digirFooters">
		<xsl:value-of select="concat(string($endDigirFilter),string($records),string($endDigirSearch),string($endDigirRequest))"/>
	</xsl:variable>
	<xsl:template name="getTitle2">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="titleText"/>
		<xsl:value-of select="concat('Taxon Page for : ',$taxonEntry/Items[@name=$titleText]/Item[1])"/>
	</xsl:template>
	<!---->
	<!-- Outputs some text -->
	<xsl:template name="outputText">
		<xsl:param name="text"/>
		<xsl:value-of select="$text"/>
	</xsl:template>
	<!-- Retrieve character from Items element -->
	<xsl:template name="displayCharacter">
		<xsl:param name="items"/>
		<xsl:call-template name="outputItems">
			<xsl:with-param name="items" select="$items"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ouput texts inside an html strong element-->
	<xsl:template name="outputStrong">
		<xsl:param name="character"/>
		<xsl:param name="states"/>
		<p class="detail_text">
			<xsl:if test="not(string($character) ='')">
				<strong>
					<xsl:value-of select="$character"/>
					<xsl:value-of select="' : '"/>
				</strong>
			</xsl:if>
			<xsl:value-of select="$states"/>
		</p>
	</xsl:template>
	<!-- Retrieve statistical measures element-->
	<xsl:template name="displayStatsMeasures">
		<xsl:param name="statisticalMeasures"/>
		<xsl:call-template name="outputStatsMeasures">
			<xsl:with-param name="statisticalMeasures" select="$statisticalMeasures"/>
		</xsl:call-template>
	</xsl:template>
	<!-- Called by displayStatsMeasures -->
	<xsl:template name="outputStatsMeasures">
		<xsl:param name="statisticalMeasures"/>
		<xsl:variable name="counter" select="count($statisticalMeasures/StatisticalMeasure)"/>
		<xsl:for-each select="$statisticalMeasures/StatisticalMeasure">
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
	<!-- Called by outputStatsMeasures-->
	<xsl:template name="outputStatMeasure">
		<xsl:param name="min"/>
		<xsl:param name="max"/>
		<xsl:param name="units"/>
		<xsl:value-of select="concat($min,'-',$max,'  ',$units)"/>
	</xsl:template>
	<!-- Called by display Characters -->
	<xsl:template name="outputItems">
		<xsl:param name="items"/>
		<xsl:variable name="counter" select="count($items/Item)"/>
		<xsl:for-each select="$items/Item">
			<xsl:value-of select="."/>
			<xsl:if test="position() &lt; $counter">
				<xsl:value-of select="', '"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="globalReplace">
		<xsl:param name="outputString"/>
		<xsl:param name="target"/>
		<xsl:param name="replacement"/>
		<xsl:choose>
			<xsl:when test="contains($outputString,$target)">
				<xsl:value-of select="concat(substring-before($outputString,$target),$replacement)"/>
				<xsl:call-template name="globalReplace">
					<xsl:with-param name="outputString" select="substring-after($outputString,$target)"/>
					<xsl:with-param name="target" select="$target"/>
					<xsl:with-param name="replacement" select="$replacement"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$outputString"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- -->
	<xsl:template name="efgLists">
		<xsl:param name="caption"/>
		<xsl:param name="efgLists"/>
		<xsl:param name="isLink"/>
		<xsl:choose>
			<xsl:when test="not(string($isLink))=''">
				<xsl:variable name="counter" select="count($efgLists/EFGList)"/>
				<xsl:variable name="hrefCurrent">
					<xsl:for-each select="$efgLists/EFGList">
						<xsl:if test="not(string(@resourceLink))=''">
							<xsl:variable name="serviceLink">
								<xsl:if test="not(string(@resourceLink))=''">
									<xsl:value-of select="normalize-space(@resourceLink)"/>
								</xsl:if>
							</xsl:variable>
							<xsl:variable name="character" select="."/>
							<xsl:value-of select="concat(string($serviceLink),'=',string($character),'&amp;')"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<xsl:variable name="href">
					<xsl:if test="not(string($hrefCurrent))=''">
						<xsl:choose>
							<xsl:when test="contains($hrefCurrent,$dsNamePrefix)">
								<xsl:value-of select="concat(string($serverbase),string($mapQueryServlet),'&amp;',$hrefCurrent,'displayFormat=html')"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="concat(string($serverbase),string($mapQueryServlet),string($dsNamePrefix),string($dataSourceName),'&amp;',$hrefCurrent,'displayFormat=html')"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:variable>
				<xsl:variable name="captionText">
					<xsl:choose>
						<xsl:when test="not(string($caption))=''">
							<xsl:value-of select="$caption"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$efgLists/@name"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="not(string($href))=''">
						<a href="{$href}">
							<strong>
								<xsl:value-of select="concat($captionText, '')"/>
							</strong>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<strong>
							<xsl:value-of select="concat($captionText, '')"/>
						</strong>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<strong>
					<xsl:value-of select="concat($caption, '')"/>
				</strong>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="getMax">
		<xsl:param name="charactervalues"/>
		<xsl:for-each select="$charactervalues">
			<xsl:sort data-type="number" order="ascending" select="@rank"/>
			<xsl:if test="position()=last()">
				<xsl:value-of select="@rank"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
