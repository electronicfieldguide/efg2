<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:include href="commonFunctionTemplate.xsl"/>
	<xsl:include href="dateTemplate.xsl"/>
		<xsl:include href="xslPagePlate.xsl"/>

	<xsl:variable name="title1" select="$xslPage/groups/group[@id='1']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="title2" select="$xslPage/groups/group[@id='2']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="imagetitle" select="concat($template_images_home,$xslPage/groups/group[@id='3']/characterValue[@rank='1']/@value)"/>
	<xsl:variable name="address1" select="$xslPage/groups/group[@rank='4']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="address2" select="$xslPage/groups/group[@rank='5']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="address3" select="$xslPage/groups/group[@rank='6']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="address4" select="$xslPage/groups/group[@rank='7']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="checkListTitle" select="$xslPage/groups/group[@id='5']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="descriptionTextTitle" select="$xslPage/groups/group[@id='6']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="sciTextTitle" select="$xslPage/groups/group[@id='7']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="descStatusTextTitle" select="$xslPage/groups/group[@id='8']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="image1TextTitle" select="$xslPage/groups/group[@id='9']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="image2TextTitle" select="$xslPage/groups/group[@id='10']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="copyrightText" select="$xslPage/groups/group[@rank='20']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="urlText" select="$xslPage/groups/group[@rank='21']/characterValue[@rank='1']/@value"/>
	<xsl:variable name="desc1">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'11'"/>
			<xsl:with-param name="groupRank" select="'14'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="desc2">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'11'"/>
			<xsl:with-param name="groupRank" select="'15'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="desc3">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'11'"/>
			<xsl:with-param name="groupRank" select="'16'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="desc4">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'11'"/>
			<xsl:with-param name="groupRank" select="'17'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="imagea1">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'12'"/>
			<xsl:with-param name="groupRank" select="'18'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="imagea2">
		<xsl:call-template name="getVariable">
			<xsl:with-param name="groups" select="$xslPage/groups"/>
			<xsl:with-param name="groupID" select="'12'"/>
			<xsl:with-param name="groupRank" select="'19'"/>
			<xsl:with-param name="characterRank" select="'1'"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="content-type" content="text/html;charset=iso-8859-1"/>
				<title>
					<xsl:value-of select="$checkListTitle"/>
				</title>
			</head>
			<body bgcolor="#dcdcdc">
				<hr size="2"/>
				<table border="0" cellspacing="2" cellpadding="0" bgcolor="#f5f5f5">
					<tr>
						<td valign="top">
							<h1>
								<font color="#8b0000">
									<xsl:value-of select="$title1"/>
									<br/>
									<xsl:value-of select="$title2"/>
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
									<xsl:value-of select="$address4"/>
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
				<table border="0" cellspacing="2" cellpadding="4" bgcolor="#f5f5f5">
					<tr>
						<td>
							<b>
								<xsl:value-of select="$sciTextTitle"/>
							</b>
						</td>
						<td>
							<b>
								<xsl:value-of select="$descStatusTextTitle"/>
							</b>
						</td>
						<td>
							<b>
								<xsl:value-of select="$image1TextTitle"/>
							</b>
						</td>
						<td>
							<b>
								<xsl:value-of select="$image2TextTitle"/>
							</b>
						</td>
					</tr>
					<xsl:apply-templates select="//TaxonEntry">
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
		<tr>
			<xsl:call-template name="handleDescription">
				<xsl:with-param name="desca1" select="$desca1"/>
				<xsl:with-param name="desca2" select="$desca2"/>
				<xsl:with-param name="desca3" select="$desca3"/>
				<xsl:with-param name="desca4" select="$desca4"/>
			</xsl:call-template>
			<xsl:call-template name="handleImages">
				<xsl:with-param name="image1" select="MediaResources[@name=$imagea1]/MediaResource[1]"/>
				<xsl:with-param name="image2" select="MediaResources[@name=$imagea2]/MediaResource[1]"/>
				<xsl:with-param name="caption1" select="MediaResources[@name=$imagea1]/MediaResource[1]/@caption"/>
				<xsl:with-param name="caption2" select="MediaResources[@name=$imagea2]/MediaResource[1]/@caption"/>
				<xsl:with-param name="uniqueID" select="$uniqueID"/>
			</xsl:call-template>
		</tr>
	</xsl:template>
	<xsl:template name="handleImages">
		<xsl:param name="image1"/>
		<xsl:param name="image2"/>
		<xsl:param name="caption1"/>
		<xsl:param name="caption2"/>
		<xsl:param name="uniqueID"/>
		<td>
			<xsl:if test="not(string($image1))=''">
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
				<xsl:variable name="src">
					<xsl:value-of select="concat($imagebase_thumbs,'/',string($image1))"/>
				</xsl:variable>
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
				<a href="{$linkURL}">
					<img  border="0" src="{$src}" alt="{$alt}"/>
				</a>
			</xsl:if>
		</td>
		<td>
			<xsl:if test="not(string($image2))=''">
				<xsl:variable name="src">
					<xsl:value-of select="concat($imagebase_thumbs,'/',string($image2))"/>
				</xsl:variable>
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
				<img  border="0" src="{$src}" alt="{$alt}"/>
			</xsl:if>
		</td>
	</xsl:template>
	<xsl:template name="handleDescription">
		<xsl:param name="desca1"/>
		<xsl:param name="desca2"/>
		<xsl:param name="desca3"/>
		<xsl:param name="desca4"/>
		<td nowrap="nowrap">
			<font size="-1" face="Arial,Helvetica,Geneva,Swiss,SunSans-Regular">
				<b>
					<i>
						<xsl:value-of select="concat($desca1,' ',$desca2)"/>
					</i>
				</b>
			</font>
		</td>
		<td>
			<xsl:variable name="descb3">
				<xsl:if test="not(string($desca3))=''">
					<xsl:value-of select="concat($desca3,'')"/>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="descb4">
				<xsl:if test="not(string($desca4))=''">
					<xsl:value-of select="concat($desca4,'')"/>
				</xsl:if>
			</xsl:variable>
			<font size="-1" face="Arial,Helvetica,Geneva,Swiss,SunSans-Regular">
				<xsl:value-of select="concat($descb3,$descb4)"/>
			</font>
		</td>
	</xsl:template>
	<xsl:template name="outputFooter">
		<hr/>
		<table border="0" cellpadding="4" cellspacing="2">
			<tbody>
				<tr>
					<td>
						<xsl:value-of select="concat($copyrightText,',')"/>
						<font color="#006400" face="Palatino Linotype">
							<a href="{$urlText}">
								<xsl:value-of select="$urlText"/>
							</a>
						</font>
					</td>
				</tr>
			</tbody>
		</table>
		<hr/>
		<table border="0" cellpadding="4" cellspacing="0" width="504">
			<tbody>
				<tr>
					<td>
						<xsl:call-template name="outputDate"/>
					</td>
				</tr>
			</tbody>
		</table>
		<hr/>
		<p/>
	</xsl:template>
</xsl:stylesheet>
