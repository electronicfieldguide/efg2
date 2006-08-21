<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Make it generic allow authors to specify different template names-->
	<xsl:include href="xslPageList.xsl"/>
	<xsl:variable name="defaultcss" select="'nantucketstyle.css'"/>

	<xsl:variable name="cssFile">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'1'"/>
			<xsl:with-param name="groupRank" select="'1'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="css">
		<xsl:choose>
			<xsl:when test="not(string($cssFile))=''">
				<xsl:value-of select="$cssFile"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$defaultcss"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hrefCommon" select="concat($serverbase,'/Redirect.jsp?displayFormat=html&amp;dataSourceName=',$dataSourceName,'&amp;uniqueID=')"/>
	<xsl:variable name="imagetitle" select="concat($template_images_home,$xslPage/groups/group[@id='2']/characterValue[@rank='1']/@value)"/>
	<xsl:variable name="title" select="$xslPage/groups/group[@id='3']/@text"/>
	<xsl:variable name="headLineText" select="$xslPage/groups/group[@id='4']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="firstColumn">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'7'"/>
			<xsl:with-param name="groupRank" select="'7'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="secondColumn">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'7'"/>
			<xsl:with-param name="groupRank" select="'8'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="thirdColumn">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'7'"/>
			<xsl:with-param name="groupRank" select="'9'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:template match="/">
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
				<xsl:variable name="linkhref" select="concat($css_home,$css)"/>
				<link rel="stylesheet" href="{$linkhref}"/>
				<title>
					<xsl:value-of select="$title"/>
				</title>
			</head>
			<body alink="#660033" vlink="#660033" link="#6699FF" bgcolor="#ddeeff" text="#000000">
				<table class="header">
					<xsl:call-template name="outputCommonRows"/>
					<xsl:choose>
						<xsl:when test="$dataSourceName and $templateConfigFile">
							<tr>
								<td colspan="2">
									<table class="specieslist">
										<xsl:call-template name="outputFirstTableRow">
											<xsl:with-param name="groups" select="$xslPage/groups"/>
										</xsl:call-template>
										<xsl:call-template name="outputSpacer"/>
										<xsl:apply-templates select="//TaxonEntry">
											<xsl:sort select="Items[@name=string($firstColumn)]"/>
											<xsl:sort select="StatisticalMesures[@name=string($firstColumn)]"/>
											<xsl:sort select="MediaResources[@name=string($firstColumn)]"/>
											<xsl:sort select="EFGLists[@name=string($firstColumn)]"/>
											<xsl:sort select="Items[@name=string($secondColumn)]"/>
											<xsl:sort select="StatisticalMesures[@name=string($secondColumn)]"/>
											<xsl:sort select="MediaResources[@name=string($secondColumn)]"/>
											<xsl:sort select="EFGLists[@name=string($secondColumn)]"/>
										</xsl:apply-templates>
									</table>
								</td>
							</tr>
						</xsl:when>
						<xsl:otherwise>
							<h2 align="center">
								<xsl:text>Datasource must be specified</xsl:text>
							</h2>
						</xsl:otherwise>
					</xsl:choose>
				</table>
			</body>
		</html>
	</xsl:template>
		<xsl:template name="findColumnVariables1">
		<xsl:param name="item"/>
		<xsl:param name="stat"/>
		<xsl:param name="med"/>
		<xsl:param name="list"/>
		<xsl:param name="columnName"/>
		<xsl:variable name="firstItem">
			<xsl:value-of select="$item"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="string($firstItem)=''">
				<!-- No item exist with the same @name as firstColumn -->
				<xsl:variable name="firstStats">
					<xsl:value-of select="$stat"/>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="string($firstStats)=''">
						<!-- No StatisticalMeasure exist with the same @name as firstColumn -->
						<xsl:variable name="firstLists">
						<xsl:value-of select="$list"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="string($firstLists)=''">
								<!-- No EFGLists exist with the same @name as firstColumn. Check and output a media resource -->
								<xsl:variable name="firstMedia">
								<xsl:value-of select="$med"/>
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
	<xsl:template match="TaxonEntry">
		<xsl:variable name="uniqueID" select="@recordID"/>
		<xsl:variable name="firstColumnVar">
			<xsl:call-template name="findColumnVariables1">
				<xsl:with-param name="item" select="Items[@name=$firstColumn]/Item[1]"/>
				<xsl:with-param name="stat" select="StatisticalMeasures[@name=$firstColumn]/StatisticalMeasure[1]"/>
				<xsl:with-param name="med" select="MediaResources[@name=$firstColumn]/MediaResource[1]"/>
				<xsl:with-param name="list" select="EFGLists[@name=$firstColumn]/EFGList[1]"/>
				<xsl:with-param name="columnName" select="$firstColumn"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="secondColumnVar">
			<xsl:call-template name="findColumnVariables1">
				<xsl:with-param name="item" select="Items[@name=$secondColumn]/Item[1]"/>
				<xsl:with-param name="stat" select="StatisticalMeasures[@name=$secondColumn]/StatisticalMeasure[1]"/>
				<xsl:with-param name="med" select="MediaResources[@name=$secondColumn]/MediaResource[1]"/>
				<xsl:with-param name="list" select="EFGLists[@name=$secondColumn]"/>
				<xsl:with-param name="columnName" select="$secondColumn"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="thirdColumnVar">
			<xsl:call-template name="findColumnVariables1">
				<xsl:with-param name="item" select="Items[@name=$thirdColumn]/Item[1]"/>
				<xsl:with-param name="stat" select="StatisticalMeasures[@name=$thirdColumn]/StatisticalMeasure[1]"/>
				<xsl:with-param name="med" select="MediaResources[@name=$thirdColumn]/MediaResource[1]"/>
				<xsl:with-param name="list" select="EFGLists[@name=$thirdColumn]"/>
				<xsl:with-param name="columnName" select="$thirdColumn"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:call-template name="outputRow">
			<xsl:with-param name="uniqueID" select="$uniqueID"/>
			<xsl:with-param name="firstColumnVar" select="$firstColumnVar"/>
			<xsl:with-param name="secondColumnVar" select="$secondColumnVar"/>
			<xsl:with-param name="thirdColumnVar" select="$thirdColumnVar"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="outputSpacer">
		<tr>
			<td class="spacerheight"/>
			<td class="spacerheight"/>
		</tr>
	</xsl:template>
	<xsl:template name="outputCol">
		<xsl:param name="uniqueID"/>
		<xsl:param name="fieldValue"/>
		<xsl:param name="tdClassname"/>
		<xsl:param name="aClassname"/>
		<xsl:variable name="hrefCurrent" select="concat($hrefCommon,$uniqueID)"/>
		<td class="{$tdClassname}">
			<a class="{$aClassname}" href="{$hrefCurrent}">
				<xsl:value-of select="$fieldValue"/>
			</a>
		</td>
	</xsl:template>
	<xsl:template name="outputFirstTableRow">
		<xsl:param name="groups"/>
		<tr>
			<xsl:variable name="header1" select="$groups/group[@id='5']/characterValue[@rank='1']/@value"/>
			<xsl:variable name="header2" select="$groups/group[@id='6']/characterValue[@rank='1']/@value"/>
			<xsl:call-template name="outputFirstTableData">
				<xsl:with-param name="header1" select="$header1"/>
				<xsl:with-param name="header2" select="$header2"/>
			</xsl:call-template>
		</tr>
	</xsl:template>
	<xsl:template name="outputCommonRows">
		<tr>
			<td colspan="2" class="title">
				<img align="left" alt="{$title}" title="{$title}" border="0" src="{$imagetitle}"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="descrip">
				<xsl:value-of select="$headLineText"/>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
