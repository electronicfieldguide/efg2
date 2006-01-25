<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:param name="css" select="'bogstyle.css'"/>
	<xsl:variable name="number-per-row" select="3"/>
	<xsl:template match="/">
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
				<title/>
				<link rel="stylesheet">
				<xsl:attribute name="href">
						<xsl:value-of select="concat(serverbase,$css)"/>
				</xsl:attribute> 
				</link>
			</head>
			<body>
				<xsl:choose>
					<xsl:when test="$datasource and $templateConfigFile" >
							<xsl:call-template name="start">
					<xsl:with-param name="taxonEntry" select="//TaxonEntry"/>
					<xsl:with-param name="groups" select="document($templateConfigFile)/TaxonPageTemplates/TaxonPageTemplate[@datasourceName=$datasource]/groups"/>
				</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<h2 align="center">
						<xsl:text>Datasource must be specified</xsl:text>
						</h2>
					</xsl:otherwise>
				</xsl:choose>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="handleGroup1">
		<xsl:param name="group1"/>
		<xsl:for-each select="$group1/characterValue">
			<xsl:sort data-type="number" order="ascending" select="@rank"/>
			<xsl:if test="string(@rank)='1'">
				<span class="title">
					<xsl:call-template name="outputText">
						<xsl:with-param name="text" select="@text"/>
					</xsl:call-template>
				</span>
			</xsl:if>
			<xsl:if test="string(@rank)='2'">
				<span class="pageauthor">
					<xsl:call-template name="outputText">
						<xsl:with-param name="text" select="@text"/>
					</xsl:call-template>
				</span>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="start">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="groups"/>
		<xsl:if test="$groups/group[@id='1']">
			<xsl:variable name="group1" select="$groups/group[@id=1]"/>
			<xsl:call-template name="handleGroup1">
				<xsl:with-param name="group1" select="$group1"/>
			</xsl:call-template>
		</xsl:if>
		<table>
			<!-- -->
			<xsl:if test="$groups/group[@id=2 or @id=3 or @id=4]">
				<xsl:variable name="groups234" select="$groups/group[@id=2 or @id=3 or @id=4]"/>
				<xsl:call-template name="handleGroups234">
					<xsl:with-param name="group" select="$groups234"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$groups/group[@id=5]">
				<xsl:variable name="group5" select="$groups/group[@id=5]"/>
				<xsl:call-template name="handleGroup5">
					<xsl:with-param name="group" select="$group5"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				</xsl:call-template>
			</xsl:if>
		</table>
		<xsl:if test="$groups/group[@id=6]">
			<xsl:variable name="group6" select="$groups/group[@id=6]"/>
			<xsl:call-template name="handleGroup6">
				<xsl:with-param name="group" select="$group6"/>
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="handleGroup5">
		<xsl:param name="group"/>
		<xsl:param name="taxonEntry"/>
		<xsl:for-each select="$group">
			<xsl:sort data-type="number" order="ascending" select="@rank"/>
			<tr>
				<xsl:for-each select="characterValue">
					<xsl:sort data-type="number" order="ascending" select="@rank"/>
					<xsl:variable name="character" select="@value"/>
					<xsl:variable name="label" select="@label"/>
					<xsl:if test="position()=1">
						<td class="heading">
							<xsl:value-of select="string($label)"/>
						</td>
					</xsl:if>
					<xsl:if test="$taxonEntry/StatisticalMeasures[@name=string($character)]">
						<td class="data">
							<xsl:call-template name="displayStatsMeasures">
								<xsl:with-param name="statisticalMeasures" select="$taxonEntry/StatisticalMeasures[@name=string($character)]"/>
							</xsl:call-template>
						</td>
					</xsl:if>
					<xsl:if test="$taxonEntry/Items[@name=string($character)]">
						<td class="data">
							<xsl:call-template name="displayCharacter">
								<xsl:with-param name="items" select="$taxonEntry/Items[@name=string($character)]"/>
							</xsl:call-template>
						</td>
					</xsl:if>
					<xsl:if test="$taxonEntry/EFGLists[@name=string($character)]">
																
					</xsl:if>
					<xsl:choose>
						<xsl:when test="$taxonEntry/MediaResources[@name=string($character)]">
							<!--
							<xsl:variable name="label1">
								<xsl:if test="$taxonEntry/Items[@name=string($label)]">
									<xsl:value-of select="$taxonEntry/Items[@name=string($label)]/Item"/>
								</xsl:if>
							</xsl:variable>
							-->
							<td class="images">
					
								<xsl:call-template name="displayMediaResources">
									<xsl:with-param name="mediaresources" select="$taxonEntry/MediaResources[@name=string($character)]"/>
								</xsl:call-template>
								<xsl:call-template name="displayImageCaption">
									<xsl:with-param name="label" select="$label"/>
								</xsl:call-template>
							</td>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="position()=last()">
								<td class="images"/>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="handleGroup6">
		<xsl:param name="group"/>
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="copyRight">
			<xsl:for-each select="$group[@id='6']/characterValue">
				<xsl:choose>
					<xsl:when test="@text">
						<xsl:value-of select="@text"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="outputItems">
							<xsl:with-param name="items" select="$taxonEntry/Items[@name=@value]"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:variable>
		<span class="copyright">
			<xsl:value-of select="$copyRight"/>
		</span>
	</xsl:template>
	<!-- handle inner groups -->
	<xsl:template name="handleGroups34">
		<xsl:param name="group"/>
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="group3" select="$group[@id=3]"/>
		<xsl:variable name="group4" select="$group[@id=4]"/>
		<xsl:variable name="var3" select="$group[@id=3 and @rank=3]"/>
		<xsl:variable name="var4" select="''"/>
		<xsl:variable name="var5" select="''"/>
		<xsl:variable name="character3" select="$var3/characterValue/@value"/>
		<xsl:variable name="character4" select="$var4"/>
		<xsl:variable name="character5" select="$var5"/>
		<xsl:variable name="var6" select="$group[@id=3 and @rank=6]"/>
		<xsl:variable name="var7" select="$group[@id=3 and @rank=7]"/>
		<xsl:variable name="var8" select="$group[@id=3 and @rank=8]"/>
		<xsl:variable name="character6" select="$var6/characterValue/@value"/>
		<xsl:variable name="character7" select="$var7/characterValue/@value"/>
		<xsl:variable name="character8" select="$var8/characterValue/@value"/>
		<xsl:variable name="var9" select="$group[@id=3 and @rank=9]"/>
		<xsl:variable name="var10" select="$group[@id=3 and @rank=10]"/>
		<xsl:variable name="character9" select="$var9/characterValue/@value"/>
		<xsl:variable name="character10" select="$var10/characterValue/@value"/>
		<td class="datatop">
			<div class="comname">
				<xsl:call-template name="displayInnerGroup">
					<xsl:with-param name="items" select="$taxonEntry/Items[@name=$character3]"/>
				</xsl:call-template>
				<xsl:call-template name="displayInnerGroup">
					<xsl:with-param name="items" select="$taxonEntry/Items[@name=$character4]"/>
				</xsl:call-template>
				<xsl:call-template name="displayInnerGroup">
					<xsl:with-param name="items" select="$taxonEntry/Items[@name=$character5]"/>
				</xsl:call-template>
			</div>
			<div class="sciname">
				<xsl:call-template name="displayInnerGroup">
					<xsl:with-param name="items" select="$taxonEntry/Items[@name=$character6]"/>
				</xsl:call-template>
				<xsl:call-template name="displayInnerGroup">
					<xsl:with-param name="items" select="$taxonEntry/Items[@name=$character7]"/>
				</xsl:call-template>
				<span class="scinameauth">
					<xsl:call-template name="displayInnerGroup">
						<xsl:with-param name="items" select="$taxonEntry/Items[@name=$character8]"/>
					</xsl:call-template>
				</span>
			</div>
			<div class="family">
				<xsl:call-template name="displayInnerGroup">
					<xsl:with-param name="items" select="$taxonEntry/Items[@name=$character9]"/>
				</xsl:call-template>
				<xsl:value-of select="'('"/>
				<xsl:call-template name="displayInnerGroup">
					<xsl:with-param name="items" select="$taxonEntry/Items[@name=$character10]"/>
				</xsl:call-template>
				<xsl:value-of select="')'"/>
			</div>
		</td>
		<td class="imagestop">
			<xsl:for-each select="$group4/characterValue">
				<xsl:sort data-type="number" order="ascending" select="@rank"/>
				<xsl:variable name="mediaresourceField" select="@value"/>
				<xsl:variable name="label" select="@label"/>
				<!--
				<xsl:variable name="label1">
					<xsl:if test="$taxonEntry/Items[@name=string($label)]">
						<xsl:value-of select="$taxonEntry/Items[@name=string($label)]/Item"/>
					</xsl:if>
				</xsl:variable>
				-->
				<xsl:if test="$taxonEntry/MediaResources[@name=$mediaresourceField]">
					<xsl:call-template name="displayMediaResources">
						<xsl:with-param name="mediaresources" select="$taxonEntry/MediaResources[@name=$mediaresourceField]"/>
					</xsl:call-template>
					<xsl:call-template name="displayImageCaption">
						<xsl:with-param name="label" select="$label"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
		</td>
	</xsl:template>
	<xsl:template name="handleGroup2">
		<xsl:param name="group2"/>
		<xsl:param name="groups34"/>
		<xsl:param name="taxonEntry"/>
		<xsl:if test="$group2[@id=2]">
			<tr>
				<td class="headingtop">
					<xsl:value-of select="$group2/@label"/>
				</td>
				<xsl:call-template name="handleGroups34">
					<xsl:with-param name="group" select="$groups34"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				</xsl:call-template>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template name="handleGroups234">
		<xsl:param name="group"/>
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="groups34" select="$group[@id=3 or @id=4]"/>
		<xsl:variable name="group2" select="$group[@id=2]"/>
		<xsl:call-template name="handleGroup2">
			<xsl:with-param name="group2" select="$group2"/>
			<xsl:with-param name="groups34" select="$groups34"/>
			<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
		</xsl:call-template>
	</xsl:template>
	<!-- Display Mediaresources for the current Mediaresource -->
	<xsl:template name="displayMediaResources">
		<xsl:param name="mediaresources"/>
		<xsl:param name="label"/>
		<xsl:call-template name="outputMediaResources">
			<xsl:with-param name="mediaresources" select="$mediaresources"/>
		</xsl:call-template>
	</xsl:template>
	<!-- Add captions -->
	<xsl:template name="outputMediaResources">
		<xsl:param name="mediaresources"/>
		<xsl:param name="label"/>
		<xsl:for-each select="$mediaresources/MediaResource">
			<xsl:variable name="mr" select="."/>
			<xsl:if test="@type=$imagetype">
				<xsl:variable name="href" select="concat($serverbase,'/',$imagebase,'/',string($mr))"/>
				<a>
					<xsl:attribute name="href"><xsl:value-of select="$href"/></xsl:attribute>
					<img border="0">
						<xsl:attribute name="src"><xsl:value-of select="$href"/></xsl:attribute>
					</img>
				</a>
				<xsl:if test="not(string($label)='')">
					<br/>
					<span class="imagecaption">
						<xsl:value-of select="string($label)"/>
					</span>
				</xsl:if>
			</xsl:if>
			<!-- handle audio and video here -->
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="displayImageCaption">
		<xsl:param name="label"/>
		<xsl:if test="not(string($label)='')">
			<br/>
			<span class="imagecaption">
				<xsl:value-of select="string($label)"/>
			</span>
		</xsl:if>
	</xsl:template>
	<!-- Display inner group for this style sheet-->
	<xsl:template name="displayInnerGroup">
		<xsl:param name="items"/>
		<xsl:variable name="states">
			<xsl:call-template name="outputItems">
				<xsl:with-param name="items" select="$items"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:if test="$states">
			<xsl:value-of select="$states"/>
			<xsl:value-of select="'  '"/>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
