<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- $Id$ -->
	<xsl:param name="serverbase"/> 
	<!-- The name of the template configuration for the current datasource. Must be a path relative to the location of this XSL file-->
	<xsl:param name="datasource" select="'SolaData'"/>
	<xsl:param name="search"/>
	<xsl:param name="mediaResourceField"/>
<xsl:param name="fieldName"/>

	<!--
	<xsl:param name="header"/>
 -->
 <xsl:param name="serviceLinkFiller" select="'_EFG_'"/>
 
	<xsl:param name="templateConfigFile" select="concat($datasource,'_TaxonPage.xml')"/>
	<xsl:param name="imagebase" select="'efgimagesthumbs'"/>
	<xsl:param name="imagetype" select="'Image'"/>
	<xsl:param name="audiotype" select="'Audio'"/>
	<xsl:param name="vidoetype" select="'Video'"/>
	<xsl:param name="header"/>
	<xsl:param name="serviceLinkConstant" select="'service'"/>
	<xsl:variable name="colon">:</xsl:variable>
	<xsl:variable name="searchTemplateConfig" select="concat($datasource,$search)"/>
	<xsl:param name="query" select="concat($serverbase,'/search?dataSourceName=',$datasource,'&amp;')"/>

	<!-- 
	<xsl:variable name="header">
		<xsl:text>&lt;header&gt;</xsl:text>
		<xsl:text>&lt;version&gt;0.91</xsl:text>
		<xsl:text>&lt;/version&gt;</xsl:text>
		<xsl:text>&lt;sendTime&gt;1997-07-16T19:20:30.45+01:00</xsl:text>
		<xsl:text>&lt;/sendTime&gt;</xsl:text>
		<xsl:text>&lt;source&gt;128.32.214.123</xsl:text>
		<xsl:text>&lt;/source&gt;</xsl:text>
		<xsl:text>&lt;destination resource="SolaData"&gt;>http://128.32.214.123:80/DiGIRprov/ttuwww/DiGIR.php</xsl:text>
		<xsl:text>&lt;/destination&gt;</xsl:text>
		<xsl:text>&lt;type&gt;search</xsl:text>
		<xsl:text>&lt;/type&gt;</xsl:text>
		<xsl:text>&lt;/header&gt;</xsl:text>
	</xsl:variable>
-->
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
        <xsl:with-param name="outputString" 
             select="substring-after($outputString,$target)"/>
        <xsl:with-param name="target" select="$target"/>
        <xsl:with-param name="replacement" 
             select="$replacement"/>
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
		<a>
			<xsl:attribute name="href">
			<xsl:value-of select="concat(string($serverbase),'/search?displayFormat=html&amp;dataSourceName=',string($datasource),'&amp;digir=',string($digirHeaders))"/>
			<xsl:variable name="counter" select="count($efgLists/EFGList)"/>
			<xsl:for-each select="$efgLists/EFGList">
				<xsl:variable name="serviceLink1">
					<xsl:choose>
						<xsl:when test="@serviceLink">
						   <xsl:value-of select="normalize-space(@serviceLink)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$serviceLinkConstant"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="serviceLink">
					<xsl:call-template name="globalReplace">
						<xsl:with-param name="outputString" select="$serviceLink1"/>
						<xsl:with-param name="target" select="' '"/>
						<xsl:with-param name="replacement" select="$serviceLinkFiller"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="character" select="."/>
				<xsl:if test="$counter &gt; 1">
				<xsl:if test="position()=1">
						<xsl:value-of select="string($startDigirOR)"/>
				</xsl:if>
				</xsl:if>
				<xsl:value-of select="string($startDigirEQUALS)"/>
				<xsl:value-of select="concat('&lt;darwin:',string($serviceLink),'&gt;')"/>
				<xsl:value-of select="string($character)"/>
				<xsl:value-of select="concat('&lt;/darwin:',string($serviceLink),'&gt;')"/>
				<xsl:value-of select="string($endDigirEQUALS)"/>
				<xsl:if test="$counter &gt; 1">
				<xsl:if test="position()=last()">
					<xsl:value-of select="string($endDigirOR)"/>
				</xsl:if>
				</xsl:if>
				<xsl:if test="position() mod 2 = 0">
						<xsl:if test="not(position()=last())">
							<xsl:value-of select="string($endDigirOR)"/>
							<xsl:value-of select="string($startDigirOR)"/>
						</xsl:if>
				</xsl:if>
				</xsl:for-each>
				<xsl:value-of select="string($digirFooters)"/>
			</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$caption">
					<xsl:value-of select="$caption"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$efgLists/@name"/>
				</xsl:otherwise>
			</xsl:choose>
		</a>
		<xsl:text>&#160;&#160;</xsl:text>
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
