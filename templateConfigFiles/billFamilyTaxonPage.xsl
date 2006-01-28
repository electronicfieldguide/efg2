<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:include href="commonTaxonPageTemplate.xsl"/>
	<xsl:variable name="number-per-row" select="2"/>
	<xsl:param name="css" select="'famstyle.css'"/>
	<xsl:param name="jscript" select="'efgScript.js'"/>
	<xsl:template match="/">
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
				<script src="http://alpaca.cs.umb.edu/efg/monteverde/efgScript.js" type="text/JavaScript"/>
				<script type="text/javaScript">
					<xsl:attribute name="href"><xsl:value-of select="concat(serverbase,$jscript)"/></xsl:attribute>
				</script>
				<link rel="stylesheet">
					<xsl:attribute name="href"><xsl:value-of select="concat(serverbase,$css)"/></xsl:attribute>
				</link>
				<title>
					<xsl:value-of select="concat('Taxon Page for  ',$datasource)"/>
				</title>
			</head>
			<body onload="javascript:init();">
				<xsl:choose>
					<xsl:when test="$datasource and $templateConfigFile">
						<xsl:call-template name="start">
							<xsl:with-param name="taxonEntry" select="//TaxonEntry"/>
							<xsl:with-param name="groups" select="document($templateConfigFile)//TaxonPageTemplate[@datasourceName=$datasource]/groups"/>
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
	<!-- Begin processing of data 	-->
	<xsl:template name="start">
		<xsl:param name="taxonEntry"/>
		<xsl:param name="groups"/>
		<xsl:variable name="headerVariables" select="$groups/group[@id=1 or @id=2]"/>
		<xsl:variable name="creditVariables" select="$groups/group[@id=6]"/>
		<xsl:variable name="otherVariables" select="$groups/group[@id=3 or @id=4 or @id=5]"/>
		<table width="600" class="title">
			<xsl:call-template name="handleGroups12">
				<xsl:with-param name="headerVariables" select="$headerVariables"/>
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
			</xsl:call-template>
		</table>
		<p/>
		<!-- -->
		<table valign="top" width="600px">
			<tr>
				<td>
					<xsl:call-template name="handleGroups345">
						<xsl:with-param name="otherVariables" select="$otherVariables"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					</xsl:call-template>
					<xsl:call-template name="handleGroup6">
						<xsl:with-param name="creditVariables" select="$creditVariables"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="handleCharacters">
		<xsl:param name="character"/>
		<xsl:param name="taxonEntry"/>
		<xsl:if test="$taxonEntry/StatisticalMeasures[@name=string($character)]">
			<xsl:call-template name="displayStatsMeasures">
				<xsl:with-param name="statisticalMeasures" select="$taxonEntry/StatisticalMeasures[@name=string($character)]"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="$taxonEntry/Items[@name=string($character)]">
			<xsl:call-template name="displayCharacter">
				<xsl:with-param name="items" select="$taxonEntry/Items[@name=string($character)]"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="$taxonEntry/EFGLists[@name=string($character)]">
		
		</xsl:if>
	</xsl:template>
	<xsl:template name="outputItalics">
		<xsl:param name="var1"/>
		<xsl:param name="var2"/>
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="curVar1">
			<xsl:call-template name="handleCharacters">
				<xsl:with-param name="character" select="$var1"/>
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="curVar2">
			<xsl:call-template name="handleCharacters">
				<xsl:with-param name="character" select="$var2"/>
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
			</xsl:call-template>
		</xsl:variable>
		<td class="famname" align="left">
			<i>
				<xsl:value-of select="concat(string($curVar1),'  ',string($curVar2))"/>
			</i>
		</td>
	</xsl:template>
	<xsl:template name="outputCommon">
		<xsl:param name="var1"/>
		<xsl:param name="var2"/>
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="curVar1">
			<xsl:call-template name="handleCharacters">
				<xsl:with-param name="character" select="$var1"/>
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="curVar2">
			<xsl:call-template name="handleCharacters">
				<xsl:with-param name="character" select="$var2"/>
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
			</xsl:call-template>
		</xsl:variable>
		<tr>
			<td class="famname" align="left">
				<xsl:value-of select="string($curVar1)"/>
			</td>
			<td class="famname" align="left">
				<td class="famname" align="left">
					<xsl:value-of select="string($curVar2)"/>
				</td>
				<td class="famname" align="left"/>
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="outputAuthor">
		<xsl:param name="var3"/>
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="curVar1">
			<xsl:call-template name="handleCharacters">
				<xsl:with-param name="character" select="$var3"/>
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
			</xsl:call-template>
		</xsl:variable>
		<td class="famname" align="left">
			<td class="famname" align="left">
				<xsl:value-of select="string($curVar1)"/>
			</td>
			<td class="famname" align="left"/>
		</td>
		<!--
-->
	</xsl:template>
	<!-- Create Header -->
	<xsl:template name="handleGroups12">
		<xsl:param name="headerVariables"/>
		<xsl:param name="taxonEntry"/>
		<xsl:for-each select="$headerVariables">
			<xsl:sort data-type="number" order="ascending" select="@rank"/>
			<xsl:choose>
				<xsl:when test="@rank=1">
					<tr>
						<xsl:variable name="var1" select="characterValue[@rank=1]/@value"/>
						<xsl:variable name="var2" select="characterValue[@rank=2]/@value"/>
						<xsl:variable name="var3" select="characterValue[@rank=3]/@value"/>
						<xsl:call-template name="outputItalics">
							<xsl:with-param name="var1" select="$var1"/>
							<xsl:with-param name="var2" select="$var2"/>
							<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						</xsl:call-template>
						<xsl:call-template name="outputAuthor">
							<xsl:with-param name="var3" select="$var3"/>
							<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						</xsl:call-template>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="var1" select="characterValue[@rank=1]/@value"/>
					<xsl:variable name="var2" select="characterValue[@rank=2]/@value"/>
					<xsl:call-template name="outputCommon">
						<xsl:with-param name="var1" select="$var1"/>
						<xsl:with-param name="var2" select="$var2"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<!-- Create Images and body -->
	<xsl:template name="handleGroups345">
		<xsl:param name="otherVariables"/>
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="groups34" select="$otherVariables[@id=3 or @id=4]"/>
		<table align="center" cellspacing="15" border="1">
			<tr>
				<xsl:call-template name="handleGroups34">
					<xsl:with-param name="groups34" select="$groups34"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				</xsl:call-template>
			</tr>
		</table>
		<br/>
		<hr/>
		<br/>
		<xsl:variable name="group5" select="$otherVariables[@id=5]"/>
		<xsl:if test="$group5">
			<xsl:call-template name="handleGroup5">
				<xsl:with-param name="group5" select="$group5"/>
				<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="efgListsLocal">
		<xsl:param name="caption"/>
		<xsl:param name="efgLists"/>
		<xsl:for-each select="$efgLists/EFGList">
			<xsl:variable name="character" select="."/>
			<xsl:choose>
				<xsl:when test="@serviceLink">
					<xsl:variable name="serviceLink" select="@serviceLink"/>
					<xsl:variable name="url">
						<xsl:choose>
							<xsl:when test="contains(translate($serviceLink,'abcdefhijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'http;//')">
								<xsl:value-of select="concat(string($serviceLink),'=',string($character))"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="concat(string($serverbase),'/search?dataSourceName=', $datasource,'&amp;',$serviceLink,'=',$character)"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<tr>
						<td class="simfam">
							<a>
								<xsl:attribute name="href"><xsl:value-of select="$url"/></xsl:attribute>
								<xsl:value-of select="$character"/>
							</a>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td class="simfam">
							<xsl:value-of select="$character"/>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="handleGroup5Characters">
		<xsl:param name="character"/>
		<xsl:param name="label"/>
		<xsl:param name="text"/>
		<xsl:param name="taxonEntry"/>
		<xsl:choose>
			<xsl:when test="$character">
				<xsl:choose>
					<xsl:when test="$taxonEntry/EFGLists[@name=string($character)]">
						<p class="detail_text">
							<xsl:call-template name="efgLists">
								<xsl:with-param name="caption" select="$character"/>
								<xsl:with-param name="efgLists" select="$taxonEntry/EFGLists[@name=string($character)]"/>
							</xsl:call-template>
							<table width="575px" class="simfam" id="Larval host plants">
								<xsl:call-template name="efgListsLocal">
									<xsl:with-param name="caption" select="$character"/>
									<xsl:with-param name="efgLists" select="$taxonEntry/EFGLists[@name=string($character)]"/>
								</xsl:call-template>
							</table>
						</p>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="states">
							<xsl:call-template name="handleCharacters">
								<xsl:with-param name="character" select="$character"/>
								<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$taxonEntry/MediaResources[@name=string($character)]">
							
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="outputStrong">
									<xsl:with-param name="character" select="$label"/>
									<xsl:with-param name="states" select="$states"/>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="@text">
					<xsl:call-template name="outputStrong">
						<xsl:with-param name="states" select="@text"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="handleGroup5">
		<xsl:param name="group5"/>
		<xsl:param name="taxonEntry"/>
		<table width="100%" bgcolor="white">
			<tr>
				<td>
					<xsl:for-each select="$group5/characterValue">
						<xsl:sort data-type="number" order="ascending" select="@rank"/>
						<xsl:variable name="label">
							<xsl:choose>
								<xsl:when test="not(string(@label))=''">
									<xsl:value-of select="@label"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="@value"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="character" select="@value"/>
						<xsl:call-template name="handleGroup5Characters">
							<xsl:with-param name="character" select="$character"/>
							<xsl:with-param name="label" select="$label"/>
							<xsl:with-param name="text" select="@text"/>
							<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
						</xsl:call-template>
						<!--
						<xsl:choose>
							<xsl:when test="@value">
								<xsl:variable name="states">
									<xsl:call-template name="handleCharacters">
										<xsl:with-param name="character" select="$character"/>
										<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:call-template name="outputStrong">
									<xsl:with-param name="character" select="$label"/>
									<xsl:with-param name="states" select="$states"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="@text">
									<xsl:call-template name="outputStrong">
										<xsl:with-param name="states" select="@text"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
-->
					</xsl:for-each>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="handleGroups34">
		<xsl:param name="groups34"/>
		<xsl:param name="taxonEntry"/>
		<xsl:if test="$groups34[@rank=3]">
			<td>
				<table cellspacing="5" border="0">
					<xsl:call-template name="handleGroup3">
						<xsl:with-param name="group3" select="$groups34[@rank=3]"/>
						<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
					</xsl:call-template>
				</table>
			</td>
		</xsl:if>
		<xsl:if test="$groups34[@rank=4]">
			<td>
				<xsl:call-template name="handleGroup4">
					<xsl:with-param name="group4" select="$groups34[@rank=4]"/>
					<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
				</xsl:call-template>
			</td>
		</xsl:if>
	</xsl:template>
	<xsl:template name="handleGroup4">
		<xsl:param name="group4"/>
		<xsl:param name="taxonEntry"/>
		<xsl:if test="$group4">
			<xsl:if test="count($group4/characterValue) &gt;  0">
				<td valign="top" width="150px" class="identification_td" bgcolor="white">
					<xsl:for-each select="$group4/characterValue">
						<xsl:sort data-type="number" order="ascending" select="@rank"/>
						<xsl:variable name="label">
							<xsl:choose>
								<xsl:when test="not(string(@label))=''">
									<xsl:value-of select="@label"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="@value"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="character" select="@value"/>
						<xsl:choose>
							<xsl:when test="@value">
								<xsl:variable name="states">
									<xsl:call-template name="handleCharacters">
										<xsl:with-param name="character" select="$character"/>
										<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
									</xsl:call-template>
								</xsl:variable>
								<xsl:call-template name="outputStrong">
									<xsl:with-param name="character" select="$label"/>
									<xsl:with-param name="states" select="$states"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="@text">
									<xsl:call-template name="outputStrong">
										<xsl:with-param name="states" select="@text"/>
									</xsl:call-template>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</td>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<xsl:template name="handleGroup3">
		<xsl:param name="group3"/>
		<xsl:param name="taxonEntry"/>
		<xsl:if test="$group3">
			<xsl:variable name="total_count" select="count($group3/characterValue)"/>
			<xsl:for-each select="$group3/characterValue">
				<xsl:sort data-type="number" order="ascending" select="@rank"/>
				<xsl:variable name="current_rank" select="@rank"/>
				<xsl:if test="$current_rank mod $number-per-row = 1">
					<xsl:variable name="character1" select="@value"/>
					<xsl:variable name="label">
						<xsl:choose>
							<xsl:when test="not(string(@label))=''">
								<xsl:value-of select="@label"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$character1"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<tr>
						<td class="id_text">
							<xsl:call-template name="displayMediaResources">
								<xsl:with-param name="mediaresources" select="$taxonEntry/MediaResources[@name=$character1]"/>
								<xsl:with-param name="label" select="$label"/>
							</xsl:call-template>
						</td>
						<xsl:if test="not(number($current_rank)  = number($total_count))">
							<td class="id_text">
								<xsl:variable name="character2">
									<xsl:choose>
										<xsl:when test="ancestor::group/characterValue[number(@rank)=(number($current_rank) + 1)]">
											<xsl:value-of select="ancestor::group/characterValue[number(@rank)=(number($current_rank) + 1)]/@value"/>
										</xsl:when>
									</xsl:choose>
								</xsl:variable>
								<xsl:variable name="label2">
									<xsl:choose>
										<xsl:when test="ancestor::group/characterValue[number(@rank)=(number($current_rank) + 1)]">
											<xsl:choose>
												<xsl:when test="not(string(ancestor::group/characterValue[number(@rank)=(number($current_rank) + 1)]/@label))=''">
													<xsl:value-of select="ancestor::group/characterValue[number(@rank)=(number($current_rank) + 1)]/@label"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$character2"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
									</xsl:choose>
								</xsl:variable>
								<xsl:call-template name="displayMediaResources">
									<xsl:with-param name="mediaresources" select="$taxonEntry/MediaResources[@name=$character2]"/>
									<xsl:with-param name="label" select="$label2"/>
								</xsl:call-template>
							</td>
						</xsl:if>
					</tr>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	<xsl:template name="displayMediaResources">
		<xsl:param name="mediaresources"/>
		<xsl:param name="label"/>
		<xsl:call-template name="outputMediaResources">
			<xsl:with-param name="mediaresources" select="$mediaresources"/>
			<xsl:with-param name="label" select="$label"/>
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
					<img>
						<xsl:attribute name="src"><xsl:value-of select="$href"/></xsl:attribute>
					</img>
				</a>
				<br/>
				<xsl:choose>
					<xsl:when test="not(string($label)='')">
						<xsl:value-of select="string($label)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="@caption">
							<xsl:value-of select="@caption"/>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			<!-- handle audio and video here -->
		</xsl:for-each>
	</xsl:template>
	<!-- Create Footer  -->
	<xsl:template name="handleGroup6">
		<xsl:param name="creditVariables"/>
		<xsl:param name="taxonEntry"/>
		<xsl:variable name="character">
			<xsl:if test="$creditVariables/characterValue">
				<xsl:value-of select="$creditVariables/characterValue[@rank=1]/@value"/>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="groupLabel">
			<xsl:if test="not(string($creditVariables/@label)) = ''">
				<xsl:value-of select="$creditVariables/@label"/>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="text">
			<xsl:if test="$creditVariables/characterValue">
				<xsl:value-of select="$creditVariables/characterValue[@rank=1]/@text"/>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="label">
			<xsl:if test="$creditVariables/characterValue">
				<xsl:value-of select="$creditVariables/characterValue[@rank=1]/@label"/>
			</xsl:if>
		</xsl:variable>
	
		<br/>
		<hr/>
		<br/>
		<xsl:choose>
			<xsl:when test="string($groupLabel)='' and string($character)='' and string($text)='' and string($label)=''">
			</xsl:when>
			<xsl:otherwise>
			<table width="100%" bgcolor="white">
				<tr>
					<td>
						<xsl:choose>
							<xsl:when test="not(string($groupLabel)='')">
								<xsl:choose>
									<xsl:when test="not(string($text))=''">
										<xsl:call-template name="outputStrong">
											<xsl:with-param name="character" select="$groupLabel"/>
											<xsl:with-param name="states" select="$text"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="states">
											<xsl:call-template name="handleCharacters">
												<xsl:with-param name="character" select="$character"/>
												<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
											</xsl:call-template>
										</xsl:variable>
										<xsl:call-template name="outputStrong">
											<xsl:with-param name="character" select="$groupLabel"/>
											<xsl:with-param name="states" select="$states"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:variable name="var1">
									<xsl:choose>
										<xsl:when test="not(string($label))=''">
											<xsl:call-template name="handleCharacters">
												<xsl:with-param name="character" select="$label"/>
												<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$character"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:choose>
									<xsl:when test="not(string($text))=''">
										<xsl:call-template name="outputStrong">
											<xsl:with-param name="character" select="$var1"/>
											<xsl:with-param name="states" select="$text"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="var2">
											<xsl:call-template name="handleCharacters">
												<xsl:with-param name="character" select="$character"/>
												<xsl:with-param name="taxonEntry" select="$taxonEntry"/>
											</xsl:call-template>
										</xsl:variable>
										<xsl:call-template name="outputStrong">
											<xsl:with-param name="character" select="$var1"/>
											<xsl:with-param name="states" select="$var2"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</table>
			</xsl:otherwise>
		</xsl:choose>
	
	</xsl:template>
</xsl:stylesheet>
