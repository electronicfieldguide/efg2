<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:include href="commonFunctionTemplate.xsl"/>
	<xsl:include href="xslPageTaxon.xsl"/>
	<xsl:include href="commonMonteverdeTaxonPageTemplate.xsl"/>
	<xsl:variable name="defaultcss" select="'taxonpagetemplate3.css'"/>
	<xsl:variable name="cssFile" select="$xslPage/groups/group[@label='styles']/characterValue/@value"/>
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
	<xsl:variable name="headerGroup" select="$xslPage/groups/group[@label='headers']"/>
	<xsl:variable name="imagesGroup" select="$xslPage/groups/group[@label='images']"/>
	<xsl:variable name="identificationGroup" select="$xslPage/groups/group[@label='identifications']"/>
	<xsl:variable name="otherGroup" select="$xslPage/groups/group[@label='itemsorlists']"/>
	<xsl:variable name="copyrightGroup" select="$xslPage/groups/group[@label='credits']"/>
	<xsl:template name="outputdetailistTemplate4">
		<xsl:param name="efglists"/>
		<xsl:param name="caption"/>
		<xsl:param name="fieldName"/>
		<xsl:param name="isLink"/>
		<xsl:call-template name="outputdetailist">
			<xsl:with-param name="efglists" select="$efglists"/>
			<xsl:with-param name="caption" select="$caption"/>
			<xsl:with-param name="fieldName" select="$fieldName"/>
			<xsl:with-param name="isLink" select="$isLink"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="outputIdent">
		<xsl:param name="efglists"/>
		<xsl:param name="caption"/>
		<xsl:param name="fieldName"/>
		<xsl:variable name="title">
			<xsl:choose>
				<xsl:when test="not(string($caption))=''">
					<xsl:value-of select="$caption"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$fieldName"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<p class="id_text">
			<strong>
				<xsl:value-of select="$title"/>
			</strong>
			<xsl:for-each select="$efglists/EFGList">
				<xsl:call-template name="outputidentlist">
					<xsl:with-param name="val" select="."/>
				</xsl:call-template>
			</xsl:for-each>
		</p>
	</xsl:template>
	<xsl:template name="outputidentlist">
		<xsl:param name="val"/>
		<br/>
		<xsl:value-of select="concat('- ',$val)"/>
	</xsl:template>
	<xsl:template name="headerGroupTemplate4">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="headerGroup"/>
		<table class="title" width="600">
			<tr>
	
		</tr>
			<tr>
				<xsl:variable name="family">
					<xsl:if test="count($headerGroup/characterValue[@rank='1']) &gt; 0">
						<xsl:value-of select="$headerGroup/characterValue[@rank='1']/@value"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="outputname">
					<xsl:with-param name="fieldName" select="$family"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					<xsl:with-param name="class" select="'famname'"/>
				</xsl:call-template>
				<xsl:call-template name="outputemptyfamname"/>
				<xsl:variable name="commname">
					<xsl:if test="count($headerGroup/characterValue[@rank='2']) &gt; 0">
						<xsl:value-of select="$headerGroup/characterValue[@rank='2']/@value"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="outputname">
					<xsl:with-param name="fieldName" select="$commname"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					<xsl:with-param name="class" select="'famname'"/>
				</xsl:call-template>
				<xsl:call-template name="outputemptyfamname"/>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="/">
		<xsl:variable name="taxonEntry" select="//TaxonEntry[1]"/>
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
				<title>
					<xsl:call-template name="getTitle2">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
							<xsl:with-param name="titleText" select="$headerGroup/characterValue[1]/@value"/>
						
						
					</xsl:call-template>
				
				</title>
					<xsl:variable name="linkhref" select="concat($css_home,$css)"/>
				<link rel="stylesheet" href="{$linkhref}"/>
			</head>
			<body>
				<xsl:if test="count($headerGroup/characterValue/@value) &gt; 0">
					<xsl:call-template name="headerGroupTemplate4">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="headerGroup" select="$headerGroup"/>
					</xsl:call-template>
				</xsl:if>
				<p/>
				<table valign="top" width="600">
					<tr>
						<td>
							<table align="center" border="1" cellspacing="15">
								<tr>
									<td>
										<xsl:if test="count($imagesGroup/characterValue/@value) &gt; 0">
											<xsl:call-template name="handleMonteVerdeImages">
												<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
												<xsl:with-param name="imagesGroup" select="$imagesGroup"/>
											</xsl:call-template>
										</xsl:if>
									</td>
									<td class="identification_td" bgcolor="white" valign="top" width="150">
										<xsl:if test="count($identificationGroup/characterValue/@value) &gt; 0">
											<xsl:for-each select="$identificationGroup/characterValue">
												<xsl:sort select="@rank" data-type="number"/>
												<xsl:variable name="fieldName" select="@value"/>
												<xsl:variable name="caption" select="@text"/>
												<xsl:if test="count($taxonEntry/Items[@name=$fieldName]) &gt; 0">
													<xsl:call-template name="outputcharacter">
														<xsl:with-param name="items" select="$taxonEntry/Items[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$caption"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
													</xsl:call-template>
												</xsl:if>
												<xsl:if test="count($taxonEntry/StatisticalMeasures[@name=$fieldName]) &gt; 0">
													<xsl:call-template name="outputstats">
														<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$caption"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
													</xsl:call-template>
												</xsl:if>
												<xsl:if test="count($taxonEntry/EFGLists[@name=$fieldName]) &gt; 0">
													<xsl:call-template name="outputIdent">
														<xsl:with-param name="efglists" select="$taxonEntry/EFGLists[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$caption"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
													</xsl:call-template>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									</td>
								</tr>
							</table>
							<xsl:call-template name="ouputlinebreaks"/>
							<table bgcolor="white" width="100%">
								<tr>
									<td>
										<xsl:if test="count($otherGroup/characterValue/@value) &gt; 0">
											<xsl:for-each select="$otherGroup/characterValue">
												<xsl:sort select="@rank" data-type="number"/>
												<xsl:variable name="fieldName" select="@value"/>
												<xsl:variable name="caption" select="@text"/>
												<xsl:if test="count($taxonEntry/Items[@name=$fieldName]) &gt; 0">
													<xsl:call-template name="outputcharacter">
														<xsl:with-param name="items" select="$taxonEntry/Items[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$caption"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
													</xsl:call-template>
												</xsl:if>
												<xsl:if test="count($taxonEntry/StatisticalMeasures[@name=$fieldName]) &gt; 0">
													<xsl:call-template name="outputstats">
														<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$caption"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
													</xsl:call-template>
												</xsl:if>
												<xsl:if test="count($taxonEntry/EFGLists[@name=$fieldName]) &gt; 0">
													<xsl:call-template name="outputdetailistTemplate4">
														<xsl:with-param name="efglists" select="$taxonEntry/EFGLists[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$caption"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
														<xsl:with-param name="isLink" select="''"/>
													</xsl:call-template>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
									</td>
								</tr>
							</table>
							<br/>
							<xsl:call-template name="ouputlinebreaks"/>
							<table bgcolor="white" width="100%">
								<xsl:call-template name="outputCredits"/>
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
