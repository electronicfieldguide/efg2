<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:include href="../../commonTemplates/commonTaxonPageTemplate.xsl"/>
	<xsl:include href="../../commonTemplates/commonFunctionTemplate.xsl"/>
	<xsl:include href="../../commonTemplates/dateTemplate.xsl"/>
	<xsl:include href="commonPlates/xslPagePlate.xsl"/>
	<xsl:variable name="title">
		<xsl:if test="count($xslPage/groups/group[@label='htmltitles']/characterValue/@value) &gt; 0">
			<xsl:value-of select="$xslPage/groups/group[@label='htmltitles']/characterValue[1]/@value"/>
		</xsl:if>
	</xsl:variable>
		<xsl:variable name="title1">
		<xsl:if test="count($xslPage/groups/group[@label='htmltitles']/characterValue/@value) &gt; 0">
			<xsl:value-of select="$xslPage/groups/group[@label='htmltitles']/characterValue[2]/@value"/>
		</xsl:if>
	</xsl:variable>
	<xsl:variable name="imagetitle" select="concat($template_images_home,$xslPage/groups/group[@label='imageheaders']/characterValue[1]/@value)"/>

	<xsl:variable name="address1" select="$xslPage/groups/group[@label='addresses']/characterValue[1]/@value"/>
	<xsl:variable name="address2" select="$xslPage/groups/group[@label='addresses']/characterValue[2]/@value"/>
	<xsl:variable name="address3" select="$xslPage/groups/group[@label='addresses']/characterValue[3]/@value"/>
	
	<xsl:variable name="checkListTitle" select="$xslPage/groups/group[@label='titles']/characterValue[1]/@value"/>
	<xsl:variable name="descriptionTextTitle" select="$xslPage/groups/group[@label='intros']/characterValue[1]/@value"/>
	<xsl:variable name="sciTextTitle" select="$xslPage/groups/group[@label='headers']/characterValue[1]/@value"/>
	<xsl:variable name="image1TextTitle" select="$xslPage/groups/group[@label='headers']/characterValue[2]/@value"/>
	<xsl:variable name="image2TextTitle" select="$xslPage/groups/group[@label='headers']/characterValue[3]/@value"/>
	<xsl:variable name="copyrightText" select="$xslPage/groups/group[@label='credits']/characterValue[1]/@value"/>
	<xsl:variable name="urlText" select="$xslPage/groups/group[@label='credits']/characterValue[2]/@value"/>
		<xsl:variable name="descriptionGroup" select="$xslPage/groups/group[@label='items']"/>
		<xsl:variable name="imagesGroup" select="$xslPage/groups/group[@label='images']"/>

	<xsl:variable name="desc1" select="$descriptionGroup/characterValue[1]/@value"/>
	<xsl:variable name="desc2" select="$descriptionGroup/characterValue[2]/@value"/>
	<xsl:variable name="desc3" select="$descriptionGroup/characterValue[3]/@value"/>
<xsl:variable name="desc4" select="$descriptionGroup/characterValue[4]/@value"/>
<xsl:variable name="desc5" select="$descriptionGroup/characterValue[5]/@value"/>
<xsl:variable name="desc6" select="$descriptionGroup/characterValue[6]/@value"/>
<xsl:variable name="desc7" select="$descriptionGroup/characterValue[7]/@value"/>
<xsl:variable name="desc8" select="$descriptionGroup/characterValue[8]/@value"/>
<xsl:variable name="desc9" select="$descriptionGroup/characterValue[9]/@value"/>
	

	<xsl:variable name="imagea1" select="$imagesGroup/characterValue[1]/@value"/>
	
	<xsl:variable name="imagea2" select="$imagesGroup/characterValue[2]/@value"/>
	
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="content-type" content="text/html;charset=iso-8859-1"/>
				<title>
			<title><xsl:value-of select="concat($title,' ',$title1)"/></title>
				</title>
			</head>
			<body bgcolor="#dcdcdc">
				<hr size="2"/>
				<table border="0" cellspacing="2" cellpadding="0" bgcolor="#f5f5f5">
					<tr>
						<td valign="top">
							<h1>
								<font color="#8b0000">
									<xsl:value-of select="$title"/><br/>
									<xsl:value-of select="$title1"/>
								</font>
							</h1>
						</td>
						<td rowspan="3">
							<img src="{$imagetitle}" alt="" border="0"/>
						</td>
					</tr>
					<tr>
						<td rowspan="2" valign="top">
							<h3>
								<font color="#696969">
									<xsl:value-of select="$address1"/>
									<br/>
									<xsl:value-of select="$address2"/>
									<br/>
									<xsl:value-of select="$address3"/>
									<br/>
								</font>
							</h3>
						</td>
					</tr>
					<tr>
			</tr>
				</table>
				<hr size="2"/>
				<table border="0" cellspacing="2" cellpadding="0" bgcolor="#f5f5f5">
					<tr>
						<td>
							<h3>
								<font color="#8b0000" face="Palatino Linotype">
									<b>
										<xsl:value-of select="$checkListTitle"/>
									</b>
								</font>
							</h3>
						</td>
					</tr>
					<tr>
						<td>
							<font color="black" face="Palatino Linotype">
								<b>
									<xsl:value-of select="$descriptionTextTitle"/>
								</b>
							</font>
						</td>
					</tr>
				</table>
				<hr size="2"/>
				<table width="957" border="0" cellspacing="2" cellpadding="3" bgcolor="white">
					<tr>
						<td valign="top" nowrap="nowrap">
							<h4>
								<u>
									<xsl:value-of select="$sciTextTitle"/>
								</u>
							</h4>
						</td>
						<td align="center" valign="top">
							<h4>
								<u>
									<xsl:value-of select="$image1TextTitle"/>
								</u>
							</h4>
						</td>
						<td align="center" valign="top">
							<h4>
								<u>
									<xsl:value-of select="$image2TextTitle"/>
								</u>
							</h4>
						</td>
					</tr>
					<xsl:apply-templates select="//TaxonEntry">
						<xsl:sort select="Items[@name=$desc4]"/>
						<xsl:sort select="Items[@name=$desc1]"/>
						<xsl:sort select="Items[@name=$desc2]"/>
					</xsl:apply-templates>
				</table>
				<xsl:call-template name="outputFooter"/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntry">
		<xsl:variable name="uniqueID" select="@recordID"/>
		<tr>
			<xsl:variable name="desca1">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc1]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc1]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc1]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc1]"/>
					<xsl:with-param name="columnName" select="$desc1"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="desca2">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc2]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc2]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc2]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc2]"/>
					<xsl:with-param name="columnName" select="$desc2"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="desca3">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc3]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc3]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc3]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc3]"/>
					<xsl:with-param name="columnName" select="$desc3"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="desca4">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc4]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc4]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc4]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc4]"/>
					<xsl:with-param name="columnName" select="$desc4"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="desca5">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc5]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc5]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc5]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc5]"/>
					<xsl:with-param name="columnName" select="$desc5"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="desca6">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc6]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc6]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc6]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc6]"/>
					<xsl:with-param name="columnName" select="$desc6"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="desca7">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc7]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc7]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc7]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc7]"/>
					<xsl:with-param name="columnName" select="$desc7"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="desca8">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc8]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc8]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc8]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc8]"/>
					<xsl:with-param name="columnName" select="$desc8"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="desca9">
				<xsl:call-template name="findColumnVariables">
					<xsl:with-param name="items" select="Items[@name=$desc9]"/>
					<xsl:with-param name="stats" select="StatisticalMeasures[@name=$desc9]"/>
					<xsl:with-param name="meds" select="MediaResources[@name=$desc9]"/>
					<xsl:with-param name="lists" select="EFGLists[@name=$desc9]"/>
					<xsl:with-param name="columnName" select="$desc9"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:call-template name="handleDescription">
				<xsl:with-param name="desca1" select="$desca1"/>
				<xsl:with-param name="desca2" select="$desca2"/>
				<xsl:with-param name="desca3" select="$desca3"/>
				<xsl:with-param name="desca4" select="$desca4"/>
				<xsl:with-param name="desca5" select="$desca5"/>
				<xsl:with-param name="desca6" select="$desca6"/>
				<xsl:with-param name="desca7" select="$desca7"/>
				<xsl:with-param name="desca8" select="$desca8"/>
				<xsl:with-param name="desca9" select="$desca9"/>
				<xsl:with-param name="uniqueID" select="$uniqueID"/>
			</xsl:call-template>
			<xsl:call-template name="handleImages">
				<xsl:with-param name="image1" select="MediaResources[@name=$imagea1]/MediaResource[1]"/>
				<xsl:with-param name="image2" select="MediaResources[@name=$imagea2]/MediaResource[1]"/>
				<xsl:with-param name="caption1" select="MediaResources[@name=$imagea1]/MediaResource[1]/@caption"/>
				<xsl:with-param name="caption2" select="MediaResources[@name=$imagea2]/MediaResource[1]/@caption"/>
			</xsl:call-template>
		</tr>
	</xsl:template>
	<xsl:template name="handleImages">
		<xsl:param name="image1"/>
		<xsl:param name="image2"/>
		<xsl:param name="caption1"/>
		<xsl:param name="caption2"/>
		<td align="center">
			<xsl:if test="not(string($image1))=''">
				<xsl:variable name="href" select="concat($imagebase_large,'/',$image1)"/>
				<a href="{$href}">
					<xsl:variable name="src" select="concat($imagebase_thumbs,'/',string($image1))"/>
					<xsl:variable name="alt">
						<xsl:choose>
							<xsl:when test="string($caption1)=''">
								<xsl:value-of select="$image1"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$caption1"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<img border="0" src="{$src}" alt="{$alt}"/>
				</a>
			</xsl:if>
		</td>
		<td align="center">
			<xsl:if test="not(string($image2))=''">
				<xsl:variable name="href" select="concat($imagebase_large,'/',$image2)"/>
				<a href="{$href}">
					<xsl:variable name="src" select="concat($imagebase_thumbs,'/',string($image2))"/>
					<xsl:variable name="alt">
						<xsl:choose>
							<xsl:when test="string($caption2)=''">
								<xsl:value-of select="$image2"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$caption2"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<img border="0" src="{$src}" alt="{$alt}"/>
				</a>
			</xsl:if>
		</td>
	</xsl:template>
	<xsl:template name="handleDescription">
		<xsl:param name="desca1"/>
		<xsl:param name="desca2"/>
		<xsl:param name="desca3"/>
		<xsl:param name="desca4"/>
		<xsl:param name="desca5"/>
		<xsl:param name="desca6"/>
		<xsl:param name="desca7"/>
		<xsl:param name="desca8"/>
		<xsl:param name="desca9"/>
		<xsl:param name="uniqueID"/>
		
		
				<xsl:variable name="linkURL">
					<xsl:choose>
						<xsl:when test="$datasource=''">
							<xsl:value-of select="concat($hrefCommon,$uniqueID)"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat($hrefCommon,$uniqueID,'&amp;displayName=', $datasource)"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
			
			
		
		<td valign="top">
			<b>			
					<a href="{$linkURL}">
					<i>	
					<xsl:value-of select="concat($desca1,' ',$desca2)"/>
				</i>
				</a>
				
			</b>
			<br/>
			<xsl:value-of select="$desca3"/>
			<xsl:if test="not(string($desca4))=''">
				<xsl:value-of select="concat(' (',$desca4,')')"/>
			</xsl:if>
			<br/>
			<xsl:variable name="descb5">
				<xsl:if test="not(string($desca5))=''">
					<xsl:value-of select="concat($desca5,'.')"/>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="descb6">
				<xsl:if test="not(string($desca6))=''">
					<xsl:value-of select="concat($desca6,'.')"/>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="descb7">
				<xsl:if test="not(string($desca7))=''">
					<xsl:value-of select="concat($desca7,'.')"/>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="descb8">
				<xsl:if test="not(string($desca8))=''">
					<xsl:value-of select="concat($desca8,'.')"/>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="descb9">
				<xsl:if test="not(string($desca9))=''">
					<xsl:value-of select="concat($desca9,'.')"/>
				</xsl:if>
			</xsl:variable>
			<xsl:value-of select="concat($descb5,$descb6,$descb7,$descb8,$descb9)"/>
		</td>
	</xsl:template>
	<xsl:template name="outputFooter">
		<hr/>
		<table border="0" cellspacing="2" cellpadding="4">
			<tr>
				<td>
						<xsl:if test="not(string($copyrightText))=''">
						<xsl:value-of select="concat($copyrightText,', ')"/>
						</xsl:if>
					<font color="#006400" face="Palatino Linotype">
						<a href="{$urlText}">
							<xsl:value-of select="$urlText"/>
						</a>
					</font>
				</td>
			</tr>
		</table>
		<hr/>
		<table width="504" border="0" cellspacing="0" cellpadding="4">
			<tr>
				<td>
					<xsl:call-template name="outputDate"/>
				</td>
			</tr>
		</table>
		<hr/>
		<p/>
	</xsl:template>
</xsl:stylesheet>
