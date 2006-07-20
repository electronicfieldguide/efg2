<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:imageList="project.efg.util.ImageDisplayList" extension-element-prefixes="imageList" exclude-result-prefixes="imageList">
	<xalan:component prefix="imageList" functions="addImageDisplay getImageName  getImageCaption getSize">
		<xalan:script lang="javaclass" src="xalan://project.efg.util.ImageDisplayList"/>
	</xalan:component>
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:include href="commonFunctionTemplate.xsl"/>
	<xsl:include href="commonJavaFunctions.xsl"/>
	<xsl:param name="css" select="'nantuckettaxonpage.css'"/>
	<xsl:variable name="xslPage" select="document($templateConfigFile)//TaxonPageTemplate[@datasourceName=$dataSourceName]/XSLFileNames/xslTaxonPages/xslPage[@fileName=$xslName]"/>
	<xsl:variable name="headerGroup" select="$xslPage/groups/group[@id='1']"/>
	<xsl:variable name="imagesGroup" select="$xslPage/groups/group[@id='2']"/>
	<xsl:variable name="photosCreditGroup" select="$xslPage/groups/group[@id='3']"/>
	<xsl:variable name="firstListGroup" select="$xslPage/groups/group[@id='4']"/>
	<xsl:variable name="firstCatNarGroup" select="$xslPage/groups/group[@id='5']"/>
	<xsl:variable name="secondListGroup" select="$xslPage/groups/group[@id='6']"/>
	<xsl:variable name="secondCatNarGroup" select="$xslPage/groups/group[@id='7']"/>
	<xsl:variable name="copyrightText" select="$xslPage/groups/group[@id='8']"/>
	<xsl:template name="headerGroup">
		<xsl:param name="headerGroup"/>
		<xsl:param name="taxonEntry"/>
		<table class="title">
			<tr>
				<xsl:variable name="characterValue">
					<xsl:if test="count($headerGroup/characterValue[@rank='1']) &gt; 0">
						<xsl:value-of select="$headerGroup/characterValue[@rank='1']/@value"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="outputname">
					<xsl:with-param name="fieldName" select="$characterValue"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					<xsl:with-param name="class" select="'comname'"/>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:variable name="characterValue1">
					<xsl:if test="count($headerGroup/characterValue[@rank='2']) &gt; 0">
						<xsl:value-of select="$headerGroup/characterValue[@rank='2']/@value"/>
					</xsl:if>
				</xsl:variable>
				<xsl:variable name="characterValue2">
					<xsl:if test="count($headerGroup/characterValue[@rank='3']) &gt; 0">
						<xsl:value-of select="$headerGroup/characterValue[@rank='3']/@value"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="outputItalics">
					<xsl:with-param name="characterValue1" select="$characterValue1"/>
					<xsl:with-param name="characterValue2" select="$characterValue2"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					<xsl:with-param name="class" select="'sciname'"/>
				</xsl:call-template>
				<xsl:variable name="characterValue3">
					<xsl:if test="count($headerGroup/characterValue[@rank='4']) &gt; 0">
						<xsl:value-of select="$headerGroup/characterValue[@rank='4']/@value"/>
					</xsl:if>
				</xsl:variable>
				<xsl:call-template name="outputname">
					<xsl:with-param name="fieldName" select="$characterValue3"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					<xsl:with-param name="class" select="'famname'"/>
				</xsl:call-template>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="outputname">
		<xsl:param name="fieldName"/>
		<xsl:param name="taxonEntry"/>
		<xsl:param name="class"/>
		<td class="{$class}" colspan="2">
			<xsl:if test="not(string($fieldName))=''">
				<xsl:variable name="commonName">
					<xsl:call-template name="findColumnVariables">
						<xsl:with-param name="items" select="$taxonEntry/Items"/>
						<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures"/>
						<xsl:with-param name="meds" select="$taxonEntry/MediaResources"/>
						<xsl:with-param name="lists" select="$taxonEntry/EFGLists"/>
						<xsl:with-param name="columnName" select="$fieldName"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:value-of select="$commonName"/>
			</xsl:if>
		</td>
	</xsl:template>
	<xsl:template name="outputItalics">
		<xsl:param name="characterValue1"/>
		<xsl:param name="characterValue2"/>
		<xsl:param name="taxonEntry"/>
		<xsl:param name="class"/>
		<td class="{$class}" colspan="2">
			<xsl:if test="not(string($characterValue1)) = ''">
				<xsl:variable name="fieldName1">
					<xsl:call-template name="findColumnVariables">
						<xsl:with-param name="items" select="$taxonEntry/Items"/>
						<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures"/>
						<xsl:with-param name="meds" select="$taxonEntry/MediaResources"/>
						<xsl:with-param name="lists" select="$taxonEntry/EFGLists"/>
						<xsl:with-param name="columnName" select="$characterValue1"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="fieldName2">
					<xsl:call-template name="findColumnVariables">
						<xsl:with-param name="items" select="$taxonEntry/Items"/>
						<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures"/>
						<xsl:with-param name="meds" select="$taxonEntry/MediaResources"/>
						<xsl:with-param name="lists" select="$taxonEntry/EFGLists"/>
						<xsl:with-param name="columnName" select="$characterValue2"/>
					</xsl:call-template>
				</xsl:variable>
				<i>
					<xsl:value-of select="concat($fieldName1,' ')"/>
				</i>
				<i>
					<xsl:value-of select="$fieldName2"/>
				</i>
			</xsl:if>
		</td>
	</xsl:template>
	<xsl:template name="outputPhotocred">
		<xsl:param name="photocred"/>
		<xsl:if test="not(string($photocred))=''">
			<xsl:if test="not(string($photocred/characterValue))=''">
				<xsl:if test="$photocred/characterValue[@rank='1']">
					<div class="photocred">
						<xsl:value-of select="$photocred/characterValue[@rank='1']/@text"/>
					</div>
				</xsl:if>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputImage">
		<xsl:param name="index"/>
		<xsl:param name="myImageList"/>
		<xsl:param name="listsize"/>
		<xsl:if test="$index &lt; $listsize">
			<xsl:variable name="imageName" select="imageList:getImageName($myImageList, number($index))"/>
			<xsl:variable name="caption" select="imageList:getImageCaption($myImageList, number($index))"/>
			<xsl:variable name="src">
				<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $imageName)"/>
			</xsl:variable>
			<xsl:variable name="src">
				<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $imageName)"/>
			</xsl:variable>
			<xsl:variable name="href" select="concat($imagebase_large,'/',$imageName)"/>
			<td class="thumb">
				<div class="thumb">
					<a href="{$href}">
						<img src="{$src}" alt="{$caption}"/>
					</a>
				</div>
			</td>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputCaptionImage">
		<xsl:param name="index"/>
		<xsl:param name="myImageList"/>
		<xsl:param name="listsize"/>
		<xsl:if test="$index &lt; $listsize">
			<xsl:variable name="caption" select="imageList:getImageCaption($myImageList, number($index))"/>
			<td class="id_text">
				<xsl:value-of select="$caption"/>
			</td>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputImages">
		<xsl:param name="index"/>
		<xsl:param name="myImageList"/>
		<xsl:param name="listsize"/>
		<xsl:if test="number($index) &lt; number($listsize)">
			<table class="images">
				<tr>
					<xsl:if test="number($index) &lt; number($listsize)">
						<xsl:call-template name="outputImage">
							<xsl:with-param name="index" select="number($index)"/>
							<xsl:with-param name="myImageList" select="$myImageList"/>
							<xsl:with-param name="listsize" select="number($listsize)"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="($index + 1) &lt; $listsize">
						<xsl:call-template name="outputImage">
							<xsl:with-param name="index" select="$index + 1"/>
							<xsl:with-param name="myImageList" select="$myImageList"/>
							<xsl:with-param name="listsize" select="$listsize"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="($index + 2) &lt; $listsize">
						<xsl:call-template name="outputImage">
							<xsl:with-param name="index" select="$index + 2"/>
							<xsl:with-param name="myImageList" select="$myImageList"/>
							<xsl:with-param name="listsize" select="$listsize"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="($index + 3) &lt; $listsize">
						<xsl:call-template name="outputImage">
							<xsl:with-param name="index" select="$index + 3"/>
							<xsl:with-param name="myImageList" select="$myImageList"/>
							<xsl:with-param name="listsize" select="$listsize"/>
						</xsl:call-template>
					</xsl:if>
				</tr>
				<tr>
					<xsl:if test="number($index) &lt; number($listsize)">
						<xsl:call-template name="outputCaptionImage">
							<xsl:with-param name="index" select="number($index)"/>
							<xsl:with-param name="myImageList" select="$myImageList"/>
							<xsl:with-param name="listsize" select="number($listsize)"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="($index + 1) &lt; $listsize">
						<xsl:call-template name="outputCaptionImage">
							<xsl:with-param name="index" select="$index + 1"/>
							<xsl:with-param name="myImageList" select="$myImageList"/>
							<xsl:with-param name="listsize" select="number($listsize)"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="($index + 2) &lt; $listsize">
						<xsl:call-template name="outputCaptionImage">
							<xsl:with-param name="index" select="$index + 2"/>
							<xsl:with-param name="myImageList" select="$myImageList"/>
							<xsl:with-param name="listsize" select="$listsize"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="($index + 3) &lt; $listsize">
						<xsl:call-template name="outputCaptionImage">
							<xsl:with-param name="index" select="$index + 3"/>
							<xsl:with-param name="myImageList" select="$myImageList"/>
							<xsl:with-param name="listsize" select="$listsize"/>
						</xsl:call-template>
					</xsl:if>
				</tr>
			</table>
		</xsl:if>
		<!-- recursively call template-->
		<xsl:if test="($index + 4) &lt; $listsize">
			<xsl:call-template name="outputImages">
				<xsl:with-param name="index" select="$index + 4"/>
				<xsl:with-param name="myImageList" select="$myImageList"/>
				<xsl:with-param name="listsize" select="$listsize"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="handleImages">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="imagesGroup"/>
		<xsl:if test="count($imagesGroup/characterValue) &gt; 0">
			<xsl:variable name="myImageList" select="imageList:new()"/>
			<xsl:for-each select="$imagesGroup/characterValue">
				<xsl:sort select="@rank"/>
				<xsl:variable name="fieldName" select="@value"/>
				<xsl:call-template name="populateImageList">
					<xsl:with-param name="mediaresources" select="$taxonEntry/MediaResources[@name=$fieldName]"/>
					<xsl:with-param name="myImageList" select="$myImageList"/>
					<xsl:with-param name="caption" select="@text"/>
				</xsl:call-template>
			</xsl:for-each>
			<table class="imageframe">
				<tr>
					<td>
						<xsl:variable name="index" select="number('0')"/>
						<xsl:variable name="listsize" select="imageList:getSize($myImageList)"/>
					
						<xsl:if test="number($index) &lt; number($listsize)">
							<xsl:call-template name="outputImages">
								<xsl:with-param name="index" select="number($index)"/>
								<xsl:with-param name="myImageList" select="$myImageList"/>
								<xsl:with-param name="listsize" select="number($listsize)"/>
							</xsl:call-template>
						</xsl:if>
					</td>
				</tr>
			</table>
			<!-- -->
			<xsl:call-template name="outputPhotocred">
				<xsl:with-param name="photocred" select="$photosCreditGroup"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputCredits">
		<br/>
		<hr/>
		<br/>
		<xsl:if test="count($copyrightText) &gt; 0">
			<xsl:if test="count($copyrightText/characterValue) &gt; 0">
				<xsl:if test="count($copyrightText/characterValue[@rank='1']) &gt; 0">
					<xsl:if test="not(string($copyrightText/characterValue[@rank='1']/@text))=''">
						<table class="credits">
							<tr>
								<td>
									<p class="credits">
										<xsl:call-template name="outputstrong">
											<xsl:with-param name="caption" select="'Credits'"/>
										</xsl:call-template>
										<xsl:call-template name="outputstring">
											<xsl:with-param name="caption" select="$copyrightText/characterValue[@rank='1']/@text"/>
										</xsl:call-template>
									</p>
								</td>
							</tr>
						</table>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputstrong">
		<xsl:param name="caption"/>
		<xsl:if test="not(string($caption))=''">
			<strong>
				<xsl:value-of select="concat($caption,': ')"/>
			</strong>
		</xsl:if>
	</xsl:template>
	<!--
		output a lists
	-->
	<xsl:template name="outputdetailist">
		<xsl:param name="efglists"/>
		<xsl:param name="caption"/>
		<xsl:param name="fieldName"/>
		<p class="detail_text">
			<xsl:variable name="caps">
				<xsl:choose>
					<xsl:when test="not(string($caption))=''">
						<xsl:value-of select="$caption"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="not(string($efglists/EFGList[1]/@caption))=''">
								<xsl:value-of select="$efglists/EFGList[1]/@caption"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$fieldName"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="outputstrong">
				<xsl:with-param name="caption" select="$caps"/>
			</xsl:call-template>
			<xsl:if test="count($efglists) &gt; 0">
				<ul>
					<xsl:for-each select="$efglists[@name=$fieldName]">
						<xsl:for-each select="EFGList">
							<xsl:call-template name="outputList">
								<xsl:with-param name="caption" select="."/>
							</xsl:call-template>
						</xsl:for-each>
					</xsl:for-each>
				</ul>
			</xsl:if>
		</p>
	</xsl:template>
	<xsl:template name="outputList">
		<xsl:param name="caption"/>
		<xsl:if test="not(string($caption))=''">
			<li>
				<xsl:value-of select="$caption"/>
			</li>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputcharacter">
		<xsl:param name="items"/>
		<xsl:param name="caption"/>
		<xsl:param name="fieldName"/>
		<p class="detail_text">
			<xsl:variable name="caps">
				<xsl:choose>
					<xsl:when test="not(string($caption))=''">
						<xsl:value-of select="$caption"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="not(string($items/Item[1]/@caption))=''">
								<xsl:value-of select="$items/Item[1]/@caption"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$fieldName"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="outputstrong">
				<xsl:with-param name="caption" select="$caps"/>
			</xsl:call-template>
			<xsl:for-each select="$items/Item">
				<xsl:if test="position() > 1">
					<xsl:value-of select="', '"/>
				</xsl:if>
				<xsl:call-template name="outputstring">
					<xsl:with-param name="caption" select="."/>
				</xsl:call-template>
			</xsl:for-each>
		</p>
	</xsl:template>
	<xsl:template name="outputstring">
		<xsl:param name="caption"/>
		<xsl:if test="not(string($caption))=''">
			<xsl:value-of select="$caption"/>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputstats">
		<xsl:param name="stats"/>
		<xsl:param name="caption"/>
		<xsl:param name="fieldName"/>
		<p class="detail_text">
			<xsl:variable name="caps">
				<xsl:choose>
					<xsl:when test="not(string($caption))=''">
						<xsl:value-of select="$caption"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="not(string($stats/StatisticalMeasure[1]/@caption))=''">
								<xsl:value-of select="$stats/StatisticalMeasure[1]/@caption"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$fieldName"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:call-template name="outputstrong">
				<xsl:with-param name="caption" select="$caps"/>
			</xsl:call-template>
			<xsl:variable name="defaultunit" select="$stats/@unit"/>
			<xsl:for-each select="$stats/StatisticalMeasure">
				<xsl:variable name="min">
					<xsl:call-template name="outputstring">
						<xsl:with-param name="caption" select="@min"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="max">
					<xsl:call-template name="outputstring">
						<xsl:with-param name="caption" select="@max"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="units">
					<xsl:choose>
						<xsl:when test="not(string(@units))=''">
							<xsl:value-of select="@units"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$defaultunit"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="number($min) = number($max)">
						<xsl:value-of select="concat($min, ' ',$units)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat($min,'-',$max,' ',$units)"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</p>
	</xsl:template>
	<xsl:template match="/">
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
				<title>
					<xsl:value-of select="$datasource"/>
				</title>
				<xsl:variable name="linkhref" select="concat($css_home,$css)"/>
				<link rel="stylesheet" href="{$linkhref}"/>
			</head>
			<body>
				<xsl:variable name="taxonEntry" select="//TaxonEntry[1]"/>
				<xsl:if test="count($headerGroup) &gt; 0">
					<xsl:call-template name="headerGroup">
						<xsl:with-param name="headerGroup" select="$headerGroup"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					</xsl:call-template>
				</xsl:if>
				<table class="outerimageframe">
					<tr>
						<td>
						
							<xsl:if test="count($imagesGroup) &gt; 0">
								<xsl:call-template name="handleImages">
									<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
									<xsl:with-param name="imagesGroup" select="$imagesGroup"/>
								</xsl:call-template>
							</xsl:if>
					
							<table bgcolor="white" width="100%" class="details">
								<tr>
									<td>
										<!-- -->
										<xsl:if test="count($firstListGroup) &gt; 0">
											<xsl:if test="count($firstListGroup/characterValue) &gt; 0">
												<xsl:if test="count($taxonEntry/EFGLists[@name=$fieldName]) &gt; 0">
													<xsl:variable name="fieldName" select="$firstListGroup/characterValue[@rank='1']/@value"/>
													<xsl:call-template name="outputdetailist">
														<xsl:with-param name="efgLists" select="$taxonEntry/EFGLists[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$firstListGroup/characterValue[@rank='1']/@text"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
													</xsl:call-template>
												</xsl:if>
											</xsl:if>
										</xsl:if>
										<xsl:if test="count($firstCatNarGroup) &gt; 0">
											<xsl:if test="count($firstCatNarGroup/characterValue) &gt; 0">
												<xsl:variable name="fieldName" select="$firstCatNarGroup/characterValue/@value"/>
												<xsl:variable name="caption" select="$firstCatNarGroup/characterValue/@text"/>
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
											</xsl:if>
										</xsl:if>
										<xsl:if test="count($secondListGroup) &gt; 0">
											<xsl:for-each select="$secondListGroup/characterValue">
												<xsl:sort select="@rank"/>
												<xsl:variable name="fieldName" select="@value"/>
												<xsl:variable name="caption" select="@text"/>
												<xsl:if test="count($taxonEntry/EFGLists[@name=$fieldName]) &gt; 0">
													<xsl:call-template name="outputdetailist">
														<xsl:with-param name="efgLists" select="$taxonEntry/EFGLists[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$caption"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
													</xsl:call-template>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
										<xsl:if test="count($secondCatNarGroup)&gt; 0">
											<xsl:for-each select="$secondCatNarGroup/characterValue">
												<xsl:sort select="@rank"/>
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
											</xsl:for-each>
										</xsl:if>
									</td>
								</tr>
							</table>
							<xsl:call-template name="outputCredits"/>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
