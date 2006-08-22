<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:imageList="project.efg.util.ImageDisplayList" extension-element-prefixes="imageList" exclude-result-prefixes="imageList">
	<xalan:component prefix="imageList" functions="addImageDisplay getImageName  getImageCaption getSize">
		<xalan:script lang="javaclass" src="xalan://project.efg.util.ImageDisplayList"/>
	</xalan:component>
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:include href="commonFunctionTemplate.xsl"/>
	<xsl:include href="commonJavaFunctions.xsl"/>
	<xsl:include href="xslPageTaxon.xsl"/>
	<xsl:variable name="defaultcss" select="'nantuckettaxonpage.css'"/>
	<xsl:variable name="cssFile" select="$xslPage/groups/group[@label='styles']/characterValue[@value]"/>
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
	<xsl:variable name="photosCreditGroup" select="$xslPage/groups/group[@label='photocred']"/>
	<xsl:variable name="listsGroup" select="$xslPage/groups/group[@label='lists']"/>
	<xsl:variable name="itemsGroup" select="$xslPage/groups/group[@label='items']"/>
	<xsl:variable name="copyrightText" select="$xslPage/groups/group[@label='credits']"/>
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
					<xsl:call-template name="findColumnVariables1">
						<xsl:with-param name="item" select="$taxonEntry/Items[@name=$fieldName]/Item[1]"/>
						<xsl:with-param name="stat" select="$taxonEntry/StatisticalMeasures[@name=$fieldName]/StatisticalMeasure[1]"/>
						<xsl:with-param name="med" select="$taxonEntry/MediaResources[@name=$fieldName]/MediaResource[1]"/>
						<xsl:with-param name="list" select="$taxonEntry/EFGLists[@name=$fieldName]/EFGList[1]"/>
						<xsl:with-param name="columnName" select="$fieldName"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:value-of select="$commonName"/>
			</xsl:if>
		</td>
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
	<xsl:template name="outputItalics">
		<xsl:param name="characterValue1"/>
		<xsl:param name="characterValue2"/>
		<xsl:param name="taxonEntry"/>
		<xsl:param name="class"/>
		<td class="{$class}" colspan="2">
			<xsl:if test="not(string($characterValue1)) = ''">
				<xsl:variable name="fieldName1">
					<xsl:call-template name="findColumnVariables1">
						<xsl:with-param name="item" select="$taxonEntry/Items[@name=$characterValue1]/Item[1]"/>
						<xsl:with-param name="stat" select="$taxonEntry/StatisticalMeasures[@name=$characterValue1]/StatisticalMeasures[1]"/>
						<xsl:with-param name="med" select="$taxonEntry/MediaResources[@name=$characterValue1]/MediaResource[1]"/>
						<xsl:with-param name="list" select="$taxonEntry/EFGLists[@name=$characterValue1]/EFGList[1]"/>
						<xsl:with-param name="columnName" select="$characterValue1"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="fieldName2">
					<xsl:call-template name="findColumnVariables1">
						<xsl:with-param name="item" select="$taxonEntry/Items[@name=$characterValue2]/Item[1]"/>
						<xsl:with-param name="stat" select="$taxonEntry/StatisticalMeasures[@name=$characterValue2]/StatisticalMeasure[1]"/>
						<xsl:with-param name="med" select="$taxonEntry/MediaResources[@name=$characterValue2]/MeddiaResource[1]"/>
						<xsl:with-param name="list" select="$taxonEntry/EFGLists[@name=$characterValue2]/EFGlist[1]"/>
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
		<xsl:if test="count($photocred/characterValue) &gt; 0">
			<div class="photocred">
				<xsl:value-of select="$photocred/characterValue[@rank='1']/@text"/>
			</div>
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
				<xsl:sort select="@rank" data-type="number"/>
				<xsl:variable name="fieldName" select="@value"/>
				<xsl:variable name="mediaresources" select="$taxonEntry/MediaResources[@name=$fieldName]"/>
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
				<xsl:call-template name="efgLists">
					<xsl:with-param name="caption" select="$caption"/>
					<xsl:with-param name="efgLists" select="$efglists"/>
					<xsl:with-param name="isLink" select="'true'"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:call-template name="outputstrong">
				<xsl:with-param name="caption" select="$caps"/>
			</xsl:call-template>
			<xsl:if test="count($efglists/EFGList) &gt; 0">
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
										<xsl:if test="count($listsGroup) &gt; 0">
											<xsl:for-each select="$listsGroup/characterValue">
												<xsl:sort select="@rank" data-type="number"/>
												<xsl:variable name="fieldName" select="@value"/>
												<xsl:variable name="caption" select="@text"/>
												<xsl:if test="count($taxonEntry/EFGLists[@name=$fieldName]) &gt; 0">
													<xsl:call-template name="outputdetailist">
														<xsl:with-param name="efglists" select="$taxonEntry/EFGLists[@name=$fieldName]"/>
														<xsl:with-param name="caption" select="$caption"/>
														<xsl:with-param name="fieldName" select="$fieldName"/>
													</xsl:call-template>
												</xsl:if>
											</xsl:for-each>
										</xsl:if>
										<xsl:if test="count($itemsGroup)&gt; 0">
											<xsl:for-each select="$itemsGroup/characterValue">
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
