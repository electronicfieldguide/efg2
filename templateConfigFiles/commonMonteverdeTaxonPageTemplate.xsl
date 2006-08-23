<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:imageList="project.efg.util.ImageDisplayList" extension-element-prefixes="imageList" exclude-result-prefixes="imageList">
	<xalan:component prefix="imageList" functions="addImageDisplay getImageName  getImageCaption getSize">
		<xalan:script lang="javaclass" src="xalan://project.efg.util.ImageDisplayList"/>
	</xalan:component>
	<xsl:include href="commonJavaFunctions.xsl"/>
	<xsl:template name="handleMonteVerdeImages">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="imagesGroup"/>
		<xsl:if test="count($imagesGroup/characterValue) &gt; 0">
			<xsl:variable name="myImageList" select="imageList:new()"/>
			<xsl:for-each select="$imagesGroup/characterValue">
				<xsl:sort select="@rank" data-type="number"/>
				<xsl:variable name="fieldName" select="@value"/>
				<xsl:call-template name="populateImageList">
					<xsl:with-param name="mediaresources" select="$taxonEntry/MediaResources[@name=$fieldName]"/>
					<xsl:with-param name="myImageList" select="$myImageList"/>
					<xsl:with-param name="caption" select="@text"/>
				</xsl:call-template>
			</xsl:for-each>
			<table border="0" cellspacing="5">
				<xsl:variable name="index" select="number('0')"/>
				<xsl:variable name="listsize" select="imageList:getSize($myImageList)"/>
				<xsl:if test="number($index) &lt; number($listsize)">
					<xsl:call-template name="outputMonteVerdeImages">
						<xsl:with-param name="index" select="number($index)"/>
						<xsl:with-param name="myImageList" select="$myImageList"/>
						<xsl:with-param name="listsize" select="number($listsize)"/>
					</xsl:call-template>
				</xsl:if>
			</table>
		</xsl:if>
	</xsl:template>
	<xsl:template name="ouputlinebreaks">
		<br/>
		<hr/>
		<br/>
	</xsl:template>
	<xsl:template name="outputMonteVerdeImage">
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
			<td class="id_text">
				<a href="{$href}">
					<img src="{$src}" alt="{$caption}"/>
				</a>
				<br/>
				<xsl:value-of select="$caption"/>
			</td>
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputMonteVerdeImages">
		<xsl:param name="index"/>
		<xsl:param name="myImageList"/>
		<xsl:param name="listsize"/>
		<xsl:if test="number($index) &lt; number($listsize)">
			<tr>
				<xsl:if test="number($index) &lt; number($listsize)">
					<xsl:call-template name="outputMonteVerdeImage">
						<xsl:with-param name="index" select="number($index)"/>
						<xsl:with-param name="myImageList" select="$myImageList"/>
						<xsl:with-param name="listsize" select="number($listsize)"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="($index + 1) &lt; $listsize">
					<xsl:call-template name="outputMonteVerdeImage">
						<xsl:with-param name="index" select="$index + 1"/>
						<xsl:with-param name="myImageList" select="$myImageList"/>
						<xsl:with-param name="listsize" select="$listsize"/>
					</xsl:call-template>
				</xsl:if>
			</tr>
		</xsl:if>
		<!-- recursively call template-->
		<xsl:if test="($index + 2) &lt; $listsize">
			<tr/>
			<xsl:call-template name="outputMonteVerdeImages">
				<xsl:with-param name="index" select="$index + 2"/>
				<xsl:with-param name="myImageList" select="$myImageList"/>
				<xsl:with-param name="listsize" select="$listsize"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:variable name="copyrightGroup" select="$xslPage/groups/group[@id='5']"/>
	<xsl:template name="outputname">
		<xsl:param name="fieldName"/>
		<xsl:param name="taxonEntry"/>
		<xsl:param name="class"/>
		<td class="{$class}" align="left">
			<xsl:if test="not(string($fieldName))=''">
				<xsl:variable name="commonName">
					<xsl:call-template name="findColumnVariables">
						<xsl:with-param name="items" select="$taxonEntry/Items[@name=$fieldName]"/>
						<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures[@name=$fieldName]"/>
						<xsl:with-param name="meds" select="$taxonEntry/MediaResources[@name=$fieldName]"/>
						<xsl:with-param name="lists" select="$taxonEntry/EFGLists[@name=$fieldName]"/>
						<xsl:with-param name="columnName" select="$fieldName"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:value-of select="$commonName"/>
			</xsl:if>
		</td>
	</xsl:template>
	<xsl:template name="outputItalics">
		<xsl:param name="sciname1"/>
		<xsl:param name="sciname2"/>
		<xsl:param name="taxonEntry"/>
		<xsl:param name="class"/>
		<xsl:variable name="fieldName">
			<xsl:if test="not(string($sciname1))=''">
				<xsl:value-of select="$sciname1"/>
			</xsl:if>
			<xsl:if test="not(string($sciname2))=''">
				<xsl:value-of select="$sciname2"/>
			</xsl:if>
		</xsl:variable>
		<td class="{$class}" align="left">
			<xsl:if test="not(string($fieldName)) = ''">
				<xsl:variable name="fieldName1">
					<xsl:call-template name="findColumnVariables">
						<xsl:with-param name="items" select="$taxonEntry/Items[@name=$sciname1]"/>
						<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures[@name=$sciname1]"/>
						<xsl:with-param name="meds" select="$taxonEntry/MediaResources[@name=$sciname1]"/>
						<xsl:with-param name="lists" select="$taxonEntry/EFGLists[@name=$sciname1]"/>
						<xsl:with-param name="columnName" select="$sciname1"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="fieldName2">
					<xsl:call-template name="findColumnVariables">
						<xsl:with-param name="items" select="$taxonEntry/Items[@name=$sciname2]"/>
						<xsl:with-param name="stats" select="$taxonEntry/StatisticalMeasures[@name=$sciname2]"/>
						<xsl:with-param name="meds" select="$taxonEntry/MediaResources[@name=$sciname2]"/>
						<xsl:with-param name="lists" select="$taxonEntry/EFGLists[@name=$sciname2]"/>
						<xsl:with-param name="columnName" select="$sciname2"/>
					</xsl:call-template>
				</xsl:variable>
				<i>
					<xsl:if test="not(string($fieldName1)) =''">
						<xsl:value-of select="concat($fieldName1,' ')"/>
					</xsl:if>
					<xsl:if test="not(string($fieldName2)) =''">
						<xsl:value-of select="$fieldName2"/>
					</xsl:if>
				</i>
			</xsl:if>
		</td>
	</xsl:template>
	<xsl:template name="outputemptyfamname">
		<td class="famname" align="left"/>
	</xsl:template>
	<xsl:template name="outputdetailist">
		<xsl:param name="efglists"/>
		<xsl:param name="caption"/>
		<xsl:param name="fieldName"/>
		<xsl:param name="isLink"/>
		<p class="detail_text">
			<xsl:call-template name="efgLists">
				<xsl:with-param name="caption" select="$caption"/>
				<xsl:with-param name="efgLists" select="$efglists"/>
				<xsl:with-param name="isLink" select="$isLink"/>
			</xsl:call-template>
			<xsl:choose>
				<xsl:when test="not(string($efglists//@resourceLink))=''">
					<table class="simfam" id="Similar species" width="575">
						<xsl:for-each select="$efglists/EFGList">
							<xsl:variable name="trclass">
								<xsl:choose>
									<xsl:when test="position() mod 2 = 0">
										<xsl:value-of select="'odd'"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="'even'"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:variable name="resourcelink">
								<xsl:if test="not(string(@resourceLink))=''">
									<xsl:value-of select="@resourceLink"/>
								</xsl:if>
							</xsl:variable>
							<xsl:call-template name="outputList">
								<xsl:with-param name="caption" select="."/>
								<xsl:with-param name="resourcelink" select="$resourcelink"/>
								<xsl:with-param name="trclass" select="$trclass"/>
								<xsl:with-param name="annotation" select="@annotation"/>
							</xsl:call-template>
						</xsl:for-each>
					</table>
				</xsl:when>
				<xsl:otherwise>
					<br/>
					<xsl:for-each select="$efglists/EFGList">
						<xsl:text>&#160;&#160;</xsl:text>
						<xsl:variable name="annot">
							<xsl:value-of select="normalize-space(@annotation)"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="not(string($annot))=''">
								<xsl:value-of select="concat($annot,': ',.)"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="."/>
							</xsl:otherwise>
						</xsl:choose>
						<br/>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
		</p>
	</xsl:template>
	<xsl:template name="outputList">
		<xsl:param name="caption"/>
		<xsl:param name="resourcelink"/>
		<xsl:param name="trclass"/>
		<xsl:param name="annotation"/>
		<xsl:if test="not(string($caption))=''">
			<tr class="{$trclass}">
				<td class="simfam">
					<xsl:choose>
						<xsl:when test="not(string($resourcelink))=''">
							<xsl:variable name="url">
								<xsl:choose>
									<xsl:when test="contains(translate($resourcelink,'abcdefhijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'http;//')">
										<xsl:value-of select="concat(string($resourcelink),'=',string($caption))"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="concat(string($serverbase),'/search?dataSourceName=', $dataSourceName,'&amp;',$resourcelink,'=',$caption)"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<a href="{$url}">
								<xsl:value-of select="$caption"/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$caption"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<xsl:variable name="annot">
					<xsl:value-of select="normalize-space($annotation)"/>
				</xsl:variable>
				<xsl:if test="not(string($annot))=''">
					<td class="simfam">
						<xsl:value-of select="$annot"/>
					</td>
				</xsl:if>
			</tr>
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
	<xsl:template name="outputCredits">
		<xsl:if test="count($copyrightGroup) &gt; 0">
			<xsl:if test="count($copyrightGroup/characterValue) &gt; 0">
				<xsl:if test="count($copyrightGroup/characterValue[@rank='1']) &gt; 0">
					<xsl:if test="not(string($copyrightGroup/characterValue[@rank='1']/@text))=''">
						<tr>
							<td>
								<p class="credits">
									<xsl:call-template name="outputstrong">
										<xsl:with-param name="caption" select="'Credits'"/>
									</xsl:call-template>
									<xsl:call-template name="outputstring">
										<xsl:with-param name="caption" select="$copyrightGroup/characterValue[@rank='1']/@text"/>
									</xsl:call-template>
								</p>
							</td>
						</tr>
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
	<xsl:template name="outputstring">
		<xsl:param name="caption"/>
		<xsl:value-of select="$caption"/>
	</xsl:template>
	<xsl:template name="headerGroup">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="headerGroup"/>
		<table class="title" width="600">
			<tr>
				<xsl:if test="(count($headerGroup/characterValue[@rank='1']) &gt; 0 ) or (count($headerGroup/characterValue[@rank='2']) &gt; 0 ) or (count($headerGroup/characterValue[@rank='3'])&gt; 0 )">
					<xsl:variable name="sciname1" select="$headerGroup/characterValue[@rank='1']/@value"/>
					<xsl:variable name="sciname2" select="$headerGroup/characterValue[@rank='2']/@value"/>
					<xsl:call-template name="outputItalics">
						<xsl:with-param name="sciname1" select="$sciname1"/>
						<xsl:with-param name="sciname2" select="$sciname2"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="class" select="'famname'"/>
					</xsl:call-template>
					<xsl:call-template name="outputemptyfamname"/>
					<xsl:variable name="author">
						<xsl:if test="count($headerGroup/characterValue[@rank='3']) &gt; 0">
							<xsl:value-of select="$headerGroup/characterValue[@rank='3']/@value"/>
						</xsl:if>
					</xsl:variable>
					<xsl:call-template name="outputname">
						<xsl:with-param name="fieldName" select="$author"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="class" select="'famname'"/>
					</xsl:call-template>
					<xsl:call-template name="outputemptyfamname"/>
				</xsl:if>
			</tr>
			<tr>
				<xsl:if test="(count($headerGroup/characterValue[@rank='4']) &gt; 0 ) or (count($headerGroup/characterValue[@rank='5']))">
					<xsl:variable name="family">
						<xsl:if test="count($headerGroup/characterValue[@rank='4']) &gt; 0">
							<xsl:value-of select="$headerGroup/characterValue[@rank='4']/@value"/>
						</xsl:if>
					</xsl:variable>
					<xsl:call-template name="outputname">
						<xsl:with-param name="fieldName" select="$family"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="class" select="'famname'"/>
					</xsl:call-template>
					<xsl:call-template name="outputemptyfamname"/>
					<xsl:variable name="commname">
						<xsl:if test="count($headerGroup/characterValue[@rank='5']) &gt; 0">
							<xsl:value-of select="$headerGroup/characterValue[@rank='5']/@value"/>
						</xsl:if>
					</xsl:variable>
					<xsl:call-template name="outputname">
						<xsl:with-param name="fieldName" select="$commname"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						<xsl:with-param name="class" select="'famname'"/>
					</xsl:call-template>
					<xsl:call-template name="outputemptyfamname"/>
				</xsl:if>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
