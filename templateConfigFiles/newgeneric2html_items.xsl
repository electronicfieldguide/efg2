<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes" encoding="ISO-8859-1"/>
	<!--xsl:output method="xml"
           omit-xml-declaration="no" encoding="ISO-8859-1"/-->
	<xsl:include href="html_debug_lib.xsl"/>
	<xsl:include href="tokenizeNan.xsl"/>
	<!-- 	Build the url to the content description xml file and load it 
		into the top level parameter content-description.
-->
	<!-- EOL is added, ekerim -->
	<xsl:variable name="EOL" select="'
'"/>
	<xsl:param name="content-description-url" select="concat($resource-path,$html_content_description)"/>
	<xsl:param name="content-description" select="document($content-description-url)"/>
	<!-- all tokenizers used to tokenize list of similar species or genera etc -->
	<xsl:param name="tokenizers" select="$content-description//tokenizers"/>
	<!-- optional name of the database-->
	<xsl:param name="databaseName" select="$content-description//databaseName"/>
	<!-- Indicates whether a link should be placed on the text 'Similar Species' or 'Similar Taxon' the name of the
thing with speciesref type
-->
	<xsl:param name="similarTaxaLink" select="$content-description//similarTaxaLink/@link"/>
	<!-- NOT YET IMPLEMENTED-->
	<xsl:param name="queryAppend" select="$content-description//queryAttributes/*"/>
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="//TaxonEntries"/>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntries">
	<xsl:apply-templates select="//TaxonEntry"/>
	</xsl:template>
	<xsl:template match="TaxonEntry">
	<xsl:param name="uniqueID" select="@recordID"/>
		<head>
			<title>
				<!-- maybe this ought to be generalized also..Perhaps this is not even needed at all -->
				<xsl:apply-templates select='//Item[@name="Common Name"]' mode="value-only"/>
			</title>
			<script type="text/JavaScript" src="{$resource-path}/efgScript.js"/>
			<link rel="stylesheet" href="{$resource-path}/nantucket.css"/>
		</head>
		<!-- javascript written by Jenn Forman and the line referencing it is written by J. Asiedu-->
		<body onload="javascript:init();">
			<table class="title" width="600">
				<tr>
					<xsl:call-template name="display-page-title-common-name"/>
				</tr>
				<tr>
					<!--
					<xsl:call-template name="display-page-title"/>
-->
					<td class="sciname" align="left">
						<xsl:call-template name="display-page-sciname"/>
					</td>
					<xsl:call-template name="display-page-family"/>
				</tr>
			</table>
			<p/>
			<table valign="top" width="600px" background-color="#ffffff">
				<tr>
					<td align="center">
						<!--					$content-description//identification/*-->
						<xsl:if test="$content-description//images">
							<xsl:if test="$content-description//images">
								<table class="imageframe">
									<tr>
										<td>
											<xsl:call-template name="debug_out">
												<xsl:with-param name="msg">Image Content Items: </xsl:with-param>
												<xsl:with-param name="value" select="$content-description//images/*"/>
											</xsl:call-template>
											<xsl:call-template name="display-images">
												<xsl:with-param name="image_contents" select="$content-description//images/*"/>
											</xsl:call-template>
										</td>
									</tr>
								</table>
							<div class="photocred">Photos (c) Cheryl Comeau Beaton</div>
							<table>
							</table>
							</xsl:if>
						</xsl:if>
						<xsl:call-template name="display-details"/>
						<xsl:call-template name="display-footer"/>
					</td>
				</tr>
			</table>
		</body>
	</xsl:template>
	<xsl:template match="Item">
		<strong>
			<xsl:value-of select="./@name"/>
		</strong>
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="Item" mode="value-only">
		<xsl:if test="position() &gt; 1">
			<xsl:value-of select="', '"/>
		</xsl:if>
		<xsl:value-of select="."/>
	</xsl:template>
	<!-- Make a bulleted list -->
	<xsl:template match="Item" mode="bulleted-lists">
		<xsl:if test="position() &gt; 1">
			<xsl:value-of select="', '"/>
		</xsl:if>
		<ul>
			<xsl:call-template name="tokenize">
				<xsl:with-param name="stringToTokenize" select="."/>
			</xsl:call-template>
		</ul>
	</xsl:template>
	<!-- value-only-italic added, ekerim -->
	<xsl:template match="Item" mode="value-only-italic">
		<i>
			<xsl:value-of select="."/>
		</i>
	</xsl:template>
	<xsl:template name="display-page-title-common-name">
		<xsl:param name="items" select="$content-description//title/*"/>
		<xsl:value-of select="$items"/>
		<xsl:choose>
			<!-- non-recursive base case -->
			<xsl:when test="boolean($items)=false()"/>
			<!-- case for method title -->
			<xsl:when test="$items[1]/@method[.='value-only']">
				<td class="comname" align="left" colspan="2">
					<xsl:apply-templates select="./Items[@name=$items[1]/@name]/Item" mode="value-only"/>
				</td>
				<td align="left" class="famname">
					<xsl:call-template name="disp-content-items">
						<xsl:with-param name="items" select="$items[position()>1]"/>
					</xsl:call-template>
				</td>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="display-page-sciname">
		<xsl:param name="items" select="$content-description//sciname/*"/>
		<xsl:choose>
			<!-- non-recursive base case -->
			<xsl:when test="boolean($items)=false()"/>
			<!-- case for method title -->
			<xsl:when test="$items[1]/@method[.='value-only-italic']">
				<xsl:apply-templates select="./Items[@name=$items[1]/@name]/Item" mode="value-only-italic"/>
				<xsl:text> </xsl:text>
				<xsl:call-template name="display-page-sciname">
					<xsl:with-param name="items" select="$items[position()>1]"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="display-page-title">
		<xsl:param name="title_contents" select="$content-description//title/*"/>
		<xsl:call-template name="disp-content-items">
			<xsl:with-param name="items" select="$title_contents"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="display-page-family">
		<xsl:param name="title_contents" select="$content-description//family/*"/>
		<td class="famname" align="left">
			<xsl:call-template name="disp-content-items">
				<xsl:with-param name="items" select="$title_contents"/>
			</xsl:call-template>
		</td>
	</xsl:template>
	<xsl:template name="display-identification">
		<xsl:param name="id_contents" select="$content-description//identification/*"/>
		<xsl:if test="count($id_contents) > 0">
			<td bgcolor="white" class="identification_td" width="150px" valign="top">
				<xsl:call-template name="disp-content-items">
					<xsl:with-param name="items" select="$id_contents"/>
				</xsl:call-template>
			</td>
		</xsl:if>
	</xsl:template>
	<xsl:template name="display-details">
		<xsl:param name="detail_contents" select="$content-description//details/*"/>
		<br/>
		<hr/>
		<br/>
		<table bgcolor="white" width="100%" class="details">
			<tr>
				<td>
					<xsl:call-template name="disp-content-items">
						<xsl:with-param name="items" select="$detail_contents"/>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="display-footer">
		<xsl:param name="footer_contents" select="$content-description//footer/*"/>
		<br/>
		<hr/>
		<br/>
		<table class="credits">
			<tr>
				<td>
					<xsl:call-template name="disp-content-items">
						<xsl:with-param name="items" select="$footer_contents"/>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="caption" mode="internal">
		<strong>
			<xsl:value-of select="."/>
			<xsl:text>: </xsl:text>
		</strong>
	</xsl:template>
	<xsl:template name="disp-content-items">
		<xsl:param name="items"/>
		<xsl:choose>
			<!-- non-recursive base case -->
			<xsl:when test="boolean($items)=false()"/>
			<!-- case for method title -->
			<xsl:when test="$items[1]/@method[.='value-only']">
				<td align="left" class="famname">
					<xsl:apply-templates select="./Items[@name=$items[1]/@name]/Item" mode="value-only"/>
				</td>
				<td align="left" class="famname">
					<xsl:call-template name="disp-content-items">
						<xsl:with-param name="items" select="$items[position()>1]"/>
					</xsl:call-template>
				</td>
			</xsl:when>
			<!-- case for method title -->
			<xsl:when test="$items[1]/@method[.='value-only-italic']">
				<td align="left" class="famname">
					<xsl:apply-templates select=".//Items[@name=$items[1]/@name]/Item" mode="value-only-italic"/>
				</td>
				<td align="left" class="famname">
					<xsl:call-template name="disp-content-items">
						<xsl:with-param name="items" select="$items[position()>1]"/>
					</xsl:call-template>
				</td>
			</xsl:when>
			<!-- case for method Item -->
			<xsl:when test="$items[1]/@method[.='plaintext']">
				<p>
					<xsl:attribute name="class"><xsl:value-of select="$items[1]/@class"/></xsl:attribute>
					<xsl:apply-templates select="$items[1]/caption" mode="internal"/>
					<xsl:apply-templates select="./Items[@name=$items[1]/@name]/Item" mode="value-only"/>
				</p>
				<xsl:call-template name="disp-content-items">
					<xsl:with-param name="items" select="$items[position()>1]"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$items[1]/@method[.='bulleted-lists']">
				<p>
					<xsl:attribute name="class"><xsl:value-of select="$items[1]/@class"/></xsl:attribute>
					<xsl:apply-templates select="$items[1]/caption" mode="internal"/>
					<xsl:apply-templates select="./Items[@name=$items[1]/@name]/Item" mode="bulleted-lists"/>
				</p>
				<xsl:call-template name="disp-content-items">
					<xsl:with-param name="items" select="$items[position()>1]"/>
				</xsl:call-template>
			</xsl:when>
			<!-- case for method copy-of -->
			<xsl:when test="$items[1]/@method[.='copy-of']">
				<xsl:copy-of select="$items[1]/*"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
