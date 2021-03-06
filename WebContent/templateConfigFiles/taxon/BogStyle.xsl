<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">
	<xsl:include href="../commonTemplates/commonTaxonPageTemplate.xsl"/>
	<xsl:include href="../commonTemplates/commonFunctionTemplate.xsl"/>
	<xsl:include href="commonTaxa/xslPageTaxon.xsl"/>
	<xsl:key name="xID" match="@value" use="generate-id()"/>
	<xsl:key name="xID1" match="@text" use="generate-id()"/>
	<xsl:variable name="groupSize" select="2"/>
	<xsl:variable name="defaultcss" select="'bogstyle.css'"/>
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
	<xsl:variable name="authorGroup" select="$xslPage/groups/group[@label='title']"/>
	<xsl:variable name="headerGroup" select="$xslPage/groups/group[@label='header']"/>
	<xsl:variable name="titleText" select="$headerGroup/characterValue[@rank='1']/@value"/>
	<xsl:variable name="itemsGroup" select="$xslPage/groups/group[@label='items']"/>
	<xsl:variable name="listsGroup" select="$xslPage/groups/group[@label='lists']"/>
	<xsl:variable name="copyright" select="$xslPage/groups/group[@label='copyright']/@text"/>
	<xsl:template name="copyrightGroup">
		<xsl:if test="not(string($copyright)) =''">
			<span class="copyright">
				<xsl:value-of select="$copyright"/>
			</span>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputAuthorInfo">
		<span class="title">
			<xsl:value-of select="$authorGroup/characterValue[@rank='1']/@text"/>
		</span>
		<span class="pageauthor">
			<xsl:value-of select="$authorGroup/characterValue[@rank='2']/@text"/>
		</span>
	</xsl:template>
	<xsl:template name="headerGroup">
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="imageName" select="$headerGroup/characterValue[@label='image']/@value"/>
		<xsl:variable name="imageCaption" select="$headerGroup/characterValue[@label='image']/@text"/>
		<tr>
			<td class="headingtop">
				<xsl:value-of select="$headerGroup/@text"/>
			</td>
			<td class="datatop">
				<div class="comname">
					<xsl:call-template name="outputdiv">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="fieldName" select="$headerGroup/characterValue[@rank='1']/@value"/>
					</xsl:call-template>
				</div>
				<xsl:variable name="sciName1">
					<xsl:call-template name="outputdiv">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="fieldName" select="$headerGroup/characterValue[@rank='2']/@value"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="sciName2">
					<xsl:call-template name="outputdiv">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="fieldName" select="$headerGroup/characterValue[@rank='3']/@value"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="sciName3">
					<xsl:call-template name="outputdiv">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="fieldName" select="$headerGroup/characterValue[@rank='4']/@value"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="combine12">
					<xsl:value-of select="concat($sciName1,' ',$sciName2)"/>
				</xsl:variable>
				<div class="sciname">
					<xsl:value-of select="$combine12"/>
					<span class="scinameauth">
						<xsl:value-of select="$sciName3"/>
					</span>
				</div>
				<xsl:variable name="famName">
					<xsl:call-template name="outputdiv">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="fieldName" select="$headerGroup/characterValue[@rank='5']/@value"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="famNameCommon">
					<xsl:call-template name="outputdiv">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="fieldName" select="$headerGroup/characterValue[@rank='6']/@value"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="famNameCommonExists">
					<xsl:choose>
						<xsl:when test="not(string($famNameCommon))=''">
							<xsl:value-of select="concat(' (',$famNameCommon,')')"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="' '"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="combine56">
					<xsl:value-of select="concat($famName,$famNameCommonExists)"/>
				</xsl:variable>
				<div class="family">
					<xsl:value-of select="$combine56"/>
				</div>
			</td>
			<!-- Fix me ..select by image name and then if one exists process it-->
			<td class="imagestop">
				<xsl:if test="not(string($imageName))=''">
					<xsl:call-template name="outputImage">
						<xsl:with-param name="mediaresource" select="$taxonEntry/MediaResources[@name=$imageName]"/>
						<xsl:with-param name="caption" select="$imageCaption"/>
					</xsl:call-template>
				</xsl:if>
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="outputImage">
		<xsl:param name="mediaresource"/>
		<xsl:param name="caption"/>
		<xsl:for-each select="$mediaresource/MediaResource">
			<xsl:variable name="imageName" select="."/>
			<xsl:variable name="captionName" select="@caption"/>
			<xsl:variable name="src">
				<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $imageName)"/>
			</xsl:variable>
			<xsl:variable name="href" select="concat($imagebase_large,'/',$imageName)"/>
			<a href="{$href}">
				<img src="{$src}" border="0"/>
			</a>
			<xsl:variable name="cap">
				<xsl:choose>
					<xsl:when test="not(string($captionName))=''">
						<xsl:value-of select="$captionName"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="not(string($caption))=''">
							<xsl:value-of select="$caption"/>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:if test="not(string($cap))=''">
				<br/>
				<span class="imagecaption">
					<xsl:value-of select="$cap"/>
				</span>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="outputdiv">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="fieldName"/>
		<xsl:value-of select="$taxonEntry/Items[@name=$fieldName]/Item[1]"/>
	</xsl:template>
	<xsl:template name="itemsGroup">
		<xsl:param name="taxonEntry"/>
		<xsl:for-each select="$itemsGroup">
			<xsl:sort select="@id" data-type="number"/>
			<xsl:variable name="fieldName" select="characterValue[@rank='1']/@value"/>
			<xsl:variable name="fieldLabel" select="@text"/>
			<xsl:variable name="mediaresourceValue" select="characterValue[@label='image']/@value"/>
			<xsl:if test="count(characterValue/@value) &gt; 0 or count(characterValue/@text)  &gt; 0 or count(@text) &gt; 0 ">
				<xsl:if test="count($taxonEntry/Items[@name=$fieldName]) &gt; 0 or count($taxonEntry/MediaResources[@name=$mediaresourceValue]) &gt; 0">
					<tr>
						<td class="heading">
							<xsl:call-template name="outputRowHeader">
								<xsl:with-param name="fieldName" select="$fieldName"/>
								<xsl:with-param name="fieldLabel" select="$fieldLabel"/>
							</xsl:call-template>
						</td>
						<td class="data">
							<xsl:call-template name="outputRowData">
								<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
								<xsl:with-param name="fieldName" select="$fieldName"/>
								<xsl:with-param name="fieldLabel" select="$fieldLabel"/>
							</xsl:call-template>
						</td>
						<td class="images">
							<xsl:if test="not(string($mediaresourceValue))=''">
								<xsl:call-template name="outputImage">
									<xsl:with-param name="mediaresource" select="$taxonEntry/MediaResources[@name=$mediaresourceValue]"/>
									<xsl:with-param name="caption" select="characterValue[@label='image']/@text"/>
								</xsl:call-template>
							</xsl:if>
						</td>
					</tr>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="isExistsInLists">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="list"/>
		<xsl:param name="image"/>
		<xsl:variable name="doesExists">
			<xsl:for-each select="$list">
				<xsl:variable name="fname" select="@value"/>
				<xsl:if test="count($taxonEntry/EFGLists[@name=$fname]) &gt; 0">
					<xsl:value-of select="'found'"/>
				</xsl:if>
			</xsl:for-each>
			<xsl:for-each select="$image">
				<xsl:variable name="fname" select="@value"/>
				<xsl:if test="count($taxonEntry/MediaResources[@name=$fname]) &gt; 0">
					<xsl:value-of select="'found'"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="not(string($doesExists))=''">
				<xsl:value-of select="'true'"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="''"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="listsGroup">
		<xsl:param name="taxonEntry"/>
		<xsl:for-each select="$listsGroup">
			<xsl:sort select="@id" data-type="number"/>
			<xsl:variable name="list" select="characterValue[@label='list']"/>
			<xsl:variable name="image" select="characterValue[@label='image']/@value"/>
			<xsl:variable name="fieldName" select="''"/>
			<xsl:variable name="fieldLabel" select="@text"/>
			<xsl:if test="count(characterValue/@value) &gt; 0 or count(characterValue/@text)  &gt; 0 or count(@text) &gt; 0 ">
				<!-- for each list find if it exists in current-->
				<xsl:variable name="isExists">
					<xsl:call-template name="isExistsInLists">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="list" select="$list"/>
						<xsl:with-param name="image" select="$image"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:if test="not(string($isExists))=''">
					<tr>
						<td class="heading">
							<xsl:call-template name="outputRowHeader">
								<xsl:with-param name="fieldName" select="$fieldName"/>
								<xsl:with-param name="fieldLabel" select="$fieldLabel"/>
							</xsl:call-template>
						</td>
						<td class="data">
							<table class="simspp">
								<xsl:for-each select="$list">
									<xsl:sort select="@id" data-type="number"/>
									<xsl:variable name="caption" select="@text"/>
									<xsl:if test="count(@value) &gt; 0">
										<xsl:variable name="isital">
											<xsl:choose>
												<xsl:when test="not(string($caption))=''">
													<xsl:value-of select="'true'"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="''"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:variable>
										<xsl:variable name="fname" select="@value"/>
										<xsl:if test="count($taxonEntry/EFGLists[@name=$fname]) &gt; 0">
											<tr>
												<td class="assochead">
													<xsl:if test="not(string($caption))=''">
														<xsl:value-of select="concat($caption,': ')"/>
													</xsl:if>
												</td>
												<td class="simspptext">
													<xsl:call-template name="outputresourcelist">
														<xsl:with-param name="efglists" select="$taxonEntry/EFGLists[@name=$fname]"/>
														<xsl:with-param name="isital" select="$isital"/>
													</xsl:call-template>
												</td>
											</tr>
										</xsl:if>
									</xsl:if>
								</xsl:for-each>
							</table>
						</td>
						<td class="image_capsule">
							<table class="images">
								<xsl:if test="count($image) &gt; 0">
									<!-- Start recursion here -->
									<!--call image group gere -->
									<xsl:call-template name="group">
										<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
										<xsl:with-param name="idList1">
											<xsl:for-each select="characterValue[@label='image']/@value">
												<xsl:sort select="@rank" data-type="number" order="descending"/>
												<xsl:value-of select="generate-id()"/>
												<xsl:text>-</xsl:text>
												<xsl:if test="position() mod $groupSize=0 or position()=last()">
													<xsl:text>|</xsl:text>
												</xsl:if>
											</xsl:for-each>
										</xsl:with-param>
										<xsl:with-param name="idList2">
											<xsl:for-each select="characterValue[@label='image']/@text">
												<xsl:sort select="@rank" data-type="number" order="descending"/>
												<xsl:value-of select="generate-id()"/>
												<xsl:text>-</xsl:text>
												<xsl:if test="position() mod $groupSize=0 or position()=last()">
													<xsl:text>|</xsl:text>
												</xsl:if>
											</xsl:for-each>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</table>
						</td>
					</tr>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="group">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="idList1"/>
		<xsl:param name="idList2"/>
		<xsl:variable name="PartIdlist1" select="substring-before($idList1,'|')"/>
		<xsl:if test="$PartIdlist1">
			<tr>
				<xsl:call-template name="animate">
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					<xsl:with-param name="idList" select="$PartIdlist1"/>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:variable name="PartIdlist2" select="substring-before($idList2,'|')"/>
				<xsl:call-template name="animateText">
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					<xsl:with-param name="idList" select="$PartIdlist2"/>
				</xsl:call-template>
			</tr>
			<xsl:call-template name="group">
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				<xsl:with-param name="idList1" select="substring-after($idList1,'|')"/>
				<xsl:with-param name="idList2" select="substring-after($idList2,'|')"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="animate">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="idList"/>
		<xsl:variable name="id" select="substring-before($idList,'-')"/>
		<xsl:if test="$id">
			<td class="images_new">
				<xsl:apply-templates select="key('xID',$id)">
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				</xsl:apply-templates>
			</td>
			<xsl:call-template name="animate">
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				<xsl:with-param name="idList" select="substring-after($idList,'-')"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="animateText">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="idList"/>
		<xsl:variable name="id" select="substring-before($idList,'-')"/>
		<xsl:if test="$id">
			<td class="imagecaption">
				<xsl:apply-templates select="key('xID1',$id)">
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				</xsl:apply-templates>
			</td>
			<xsl:call-template name="animateText">
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				<xsl:with-param name="idList" select="substring-after($idList,'-')"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template match="@value">
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="imageName" select="."/>
		<xsl:variable name="caps" select="$taxonEntry/MediaResources[@name=$imageName]/MediaResource[1]"/>
		<xsl:if test="not(string($caps))=''">
			<xsl:variable name="src">
				<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $caps)"/>
			</xsl:variable>
			<xsl:variable name="href" select="concat($imagebase_large,'/',$caps)"/>
			<a href="{$href}">
				<img src="{$src}" border="0"/>
			</a>
		</xsl:if>
	</xsl:template>
	<xsl:template match="@text">
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="imageName" select="parent/@value"/>
		<xsl:variable name="imageText" select="."/>
		<xsl:variable name="caps" select="$taxonEntry/MediaResources[@name=$imageName]/MediaResource[1]/@caption"/>
		<xsl:choose>
			<xsl:when test="not(string($caps))=''">
				<xsl:value-of select="$caps"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$imageText"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="outputresourcelist">
		<xsl:param name="efglists"/>
		<xsl:param name="isital"/>
		<xsl:for-each select="$efglists/EFGList">
			<xsl:variable name="resourcelink">
				<xsl:if test="not(string(@resourceLink))=''">
					<xsl:value-of select="@resourceLink"/>
				</xsl:if>
			</xsl:variable>
			<xsl:call-template name="outputList">
				<xsl:with-param name="fieldValue" select="."/>
				<xsl:with-param name="resourcelink" select="$resourcelink"/>
				<xsl:with-param name="annotation" select="@annotation"/>
				<xsl:with-param name="isital" select="$isital"/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="outputList">
		<xsl:param name="fieldValue"/>
		<xsl:param name="resourcelink"/>
		<xsl:param name="annotation"/>
		<xsl:param name="isital"/>
		<xsl:variable name="annot">
			<xsl:value-of select="normalize-space($annotation)"/>
		</xsl:variable>
		<xsl:if test="not(string($fieldValue))=''">
			<xsl:choose>
				<xsl:when test="not(string($resourcelink))=''">
				<xsl:variable name="toUpperCase" select="translate($resourcelink,'http://','HTTP://')"/>
					<xsl:variable name="url">
						<xsl:choose>
							<xsl:when test="contains($toUpperCase,'HTTP://')">
								<xsl:value-of select="concat(string($resourcelink),string($fieldValue))"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="contains($resourcelink,$dsNamePrefix)">
										<xsl:value-of select="concat(string($serverbase),$mapQueryServlet,'&amp;',$resourcelink,'=',$fieldValue,'&amp;displayFormat=html')"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="concat(string($serverbase),$mapQueryServlet,string($dsNamePrefix), $dataSourceName,'&amp;',$resourcelink,'=',$fieldValue,'&amp;displayFormat=html')"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<div class="itemlist">
						<a href="{$url}" class="simspp">
							<xsl:value-of select="$fieldValue"/>
						</a>
						<xsl:if test="not(string($annot))=''">
							<xsl:choose>
								<xsl:when test="not(string($isital))=''">
									<span class="scinameintext">
										<xsl:value-of select="concat(' ',$annot)"/>
									</span>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat(' ',$annot)"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$fieldValue"/>
					<xsl:if test="not(string($annot))=''">
						<xsl:value-of select="$annot"/>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputcharacter">
		<xsl:param name="items"/>
		<xsl:for-each select="$items/Item">
			<xsl:if test="position() > 1">
				<xsl:value-of select="', '"/>
			</xsl:if>
			<xsl:call-template name="outputstring">
				<xsl:with-param name="caption" select="."/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="outputRowData">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="fieldName"/>
		<xsl:param name="fieldLabel"/>
		<xsl:if test="count($taxonEntry/EFGLists[@name=$fieldName]) &gt; 0">
			<xsl:call-template name="outputresourcelist">
				<xsl:with-param name="efglists" select="$taxonEntry/EFGLists[@name=$fieldName]"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="count($taxonEntry/Items[@name=$fieldName]) &gt; 0">
			<xsl:call-template name="outputcharacter">
				<xsl:with-param name="items" select="$taxonEntry/Items[@name=$fieldName]"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="count($taxonEntry/StatisticalMeasures[@name=$fieldName]) &gt; 0">
			<xsl:call-template name="outputstats">
				<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures[@name=$fieldName]"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputstring">
		<xsl:param name="caption"/>
		<xsl:if test="not(string($caption))=''">
			<xsl:value-of select="$caption"/>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputstats">
		<xsl:param name="stats"/>
		<xsl:variable name="defaultunit" select="$stats/@unit"/>
		<xsl:for-each select="$stats/StatisticalMeasure">
			<xsl:if test="position() > 1">
				<xsl:value-of select="', '"/>
			</xsl:if>
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
	</xsl:template>
	<xsl:template name="outputRowHeader">
		<xsl:param name="fieldName"/>
		<xsl:param name="fieldLabel"/>
		<xsl:choose>
			<xsl:when test="not(string($fieldLabel))=''">
				<xsl:value-of select="$fieldLabel"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$fieldName"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="/">
		<xsl:variable name="taxonEntry" select="//TaxonEntry[1]"/>
		<html>
			<head>
			<title>
				<xsl:call-template name="getTitle2">
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					<xsl:with-param name="titleText" select="$titleText"/>
				</xsl:call-template>
			
				</title>
					<xsl:variable name="linkhref" select="concat($css_home,$css)"/>
				<link rel="stylesheet" href="{$linkhref}"/>
			</head>
			<body>
				<xsl:call-template name="outputAuthorInfo"/>
				<table>
					<xsl:call-template name="headerGroup">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					</xsl:call-template>
					<xsl:call-template name="itemsGroup">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					</xsl:call-template>
					<xsl:call-template name="listsGroup">
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					</xsl:call-template>
				</table>
				<xsl:call-template name="outputbottomLinks">
					<xsl:with-param name="links" select="$bottomLinks"/>
				</xsl:call-template>
				<xsl:call-template name="copyrightGroup"/>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
