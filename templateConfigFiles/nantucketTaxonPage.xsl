<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="outputMediaResources">
		<xsl:param name="MediaResources"/>
		<table align="center" cellspacing="5" class="images">
			<tr>
				<td class="id_text">
					<a href="/efg/EFGImages/cbeaton1/IMG_scan28.jpg">
						<img src="/efg/EFGImages/cbeaton1/IMG_scan28.thumb.jpg#cbeaton1/IMG_scan28.jpg" alt="Plant"/>
					</a>
					<br/>Plant
				</td>
				<td class="id_text">
					<a href="/efg/EFGImages/cbeaton1/IMG_scan27.jpg">
						<img src="/efg/EFGImages/cbeaton1/IMG_scan27.thumb.jpg#cbeaton1/IMG_scan27.jpg" alt="Macro"/>
					</a>
					<br/>Macro
				</td>
				<td class="id_text">
					<a href="/efg/EFGImages/cbeaton1/IMG_scan29.jpg">
						<img src="/efg/EFGImages/cbeaton1/IMG_scan29.thumb.jpg#cbeaton1/IMG_scan29.jpg" alt="Macro"/>
					</a>
					<br/>Macro
				</td>
			</tr>
			<tr>
				<td class="id_text">
					<a href="/efg/EFGImages/cbeaton1/IMG_5138_1.jpg">
						<img src="/efg/EFGImages/cbeaton1/IMG_5138_1.thumb.jpg#cbeaton1/IMG_5138_1.jpg" alt="Fruit"/>
					</a>
					<br/>Fruit
				</td>
				<td class="id_text">
					<a href="/efg/EFGImages/cbeaton1/IMG_5214_1.jpg">
						<img src="/efg/EFGImages/cbeaton1/IMG_5214_1.thumb.jpg#cbeaton1/IMG_5214_1.jpg" alt="Leaf"/>
					</a>
					<br/>Leaf
				</td>
			</tr>
			<tr>
				<td class="id_text">
					<a href="/efg/EFGImages/cbeaton1/IMG_5223_1.jpg">
						<img src="/efg/EFGImages/cbeaton1/IMG_5223_1.thumb.jpg#cbeaton1/IMG_5223_1.jpg" alt="Additional Photos"/>
					</a>
					<br/>Additional Photos
				</td>
			</tr>
		</table>
		<div class="photocred">Photos (c) Cheryl Comeau Beaton</div>
	</xsl:template>
	<xsl:template name="fillerOne">
		<table/>
		<br/>
		<hr/>
		<br/>
	</xsl:template>
	<xsl:template name="outputBullets">
	<xsl:param name="title"/>
		<p class="detail_text">
			<strong><xsl:value-of select="concat($title,':')"/> </strong>
			<xsl:call-template name="ul">
				<xsl:with-param name="lists" select=""/>
			</xsl:call-template>
			<!--
			<ul>
				<li>herbaceous perennial</li>
				<li> 12-36 in. tall</li>
				<li> erect</li>
				<li> unbranched or stiffly branched at top</li>
			</ul>
			-->
		</p>
	</xsl:template>
	<xsl:template name="ul">
	<xsl:param name="lists"/>
	<ul>
		<xsl:for-each select="$lists">
			<xsl:call-template name="list">
				<xsl:with-param name="name" select=""/>
			</xsl:call-template>
		</xsl:for-each>
	</ul>
	</xsl:template>
	<xsl:template name="list">
		<xsl:param name="name"/>
		<li><xsl:value-of select="$name"/></li>
	</xsl:template>
</xsl:stylesheet>
