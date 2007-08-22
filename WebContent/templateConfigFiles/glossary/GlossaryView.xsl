<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by UMASS Boston CSLabs (UMASS Boston CSLabs) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:glossaryMaker="project.efg.server.utils.GlossaryMaker" xmlns:glossaryObject="project.efg.server.utils.GlossaryObject" extension-element-prefixes="glossaryMaker glossaryObject" exclude-result-prefixes="glossaryMaker glossaryObject">
	<xalan:component prefix="glossaryMaker" functions="addGlossaryObject getNumberOfTerms getTerm getNumberOfDefinitions getDefinition getAlphabet getNumberOfAlphabets getNumberOfMediaResources getMediaResource getNumberOfAlsoSees getAlsoSee">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.GlossaryMaker"/>
	</xalan:component>
	<xalan:component prefix="glossaryObject" functions="setTerm addDefinition  addAlsoSee  addMediaresource getTerm getDefinitions getMediaResouces getAlsoSee">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.GlossaryObject"/>
	</xalan:component>
	<xsl:include href="../../commonTemplates/commonTaxonPageTemplate.xsl"/>
	<xsl:include href="../../commonTemplates/PrevNextTemplate.xsl"/>

	<xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
	<xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
	<xsl:variable name="defaultcss" select="'nantucketGlossary.css'"/>
	<xsl:variable name="cssFile" select="$xslPage/groups/group[@label='styles']/characterValue/@value"/>
	<xsl:variable name="title" select="$xslPage/groups/group[@label='titles']/characterValue[1]/@value"/>
	<xsl:variable name="termChar" select="$xslPage/groups/group[@label='terms']/characterValue[1]/@value"/>
	<xsl:variable name="definitionChar" select="$xslPage/groups/group[@label='definitions']/characterValue[1]/@value"/>
	<xsl:variable name="imageChar" select="$xslPage/groups/group[@label='images']/characterValue[1]/@value"/>

	<xsl:variable name="myGlossaryMaker" select="glossaryMaker:new()"/>
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
	<xsl:variable name="zero" select="'0'"/>
	<xsl:template match="/">
	<xsl:variable name="total_count" select="count(//TaxonEntry)"/>
			<xsl:variable name="tt_count">
			<xsl:call-template name="write_total_count">
				<xsl:with-param name="taxon_count" select="$taxon_count"/>
				<xsl:with-param name="total_count" select="$total_count"/>
			</xsl:call-template>
		</xsl:variable>	
	<xsl:variable name="taxonentry" select="//TaxonEntry"/>
		<html>
			<head>
			<title><xsl:value-of select="$title"/></title>
				<xsl:variable name="css_loc" select="concat($css_home,$cssFile)"/>
				<link rel="stylesheet" href="{$css_loc}"></link>
			</head>
			<body class="main">
				<table class="about">
					<tr>
						<td class="abouttitle">
							<a name="top"/>
							<xsl:value-of select="$title"/>
						</td>
					</tr>
				</table>
				<xsl:apply-templates select="//TaxonEntry"/>
				<!-- Write alphabets-->
				<xsl:variable name="alpha_count" select="glossaryMaker:getNumberOfAlphabets($myGlossaryMaker)"/>
				<xsl:if test="$alpha_count &gt; 0">
					<table class="about">
						<tr>
							<td class="alphastrip">
								<xsl:call-template name="writeAlphabets">
									<xsl:with-param name="index" select="number($zero)"/>
									<xsl:with-param name="alpha_count" select="$alpha_count"/>
								</xsl:call-template>
							</td>
						</tr>
					</table>
					<table class="about">
						<tr>
							<td>
								<xsl:call-template name="writeTerms">
									<xsl:with-param name="taxonentry" select="$taxonentry"/>
								</xsl:call-template>
								<xsl:call-template name="outputGlossaryFooter"/>
							</td>
						</tr>
				
					</table>
				</xsl:if>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="TaxonEntry">
		<xsl:variable name="term" select="Items[@name=$termChar]/Item[1]"/>
		<xsl:variable name="definition" select="Items[@name=$definitionChar]/Item[1]"/>
		<xsl:call-template name="populateGlossaryMap">
			<xsl:with-param name="term" select="$term"/>
			<xsl:with-param name="definition" select="$definition"/>
		</xsl:call-template>
	</xsl:template>
	<!-- populate the glossary map-->
	<xsl:template name="populateGlossaryMap">
		<xsl:param name="term"/>
		<xsl:param name="definition"/>
		<xsl:variable name="myGlossaryObject" select="glossaryObject:new()"/>
		<xsl:variable name="new-pop1" select="glossaryObject:setTerm($myGlossaryObject, string($term))"/>
		<xsl:variable name="new-pop2" select="glossaryObject:addDefinition($myGlossaryObject, string($definition))"/>
		<xsl:variable name="new-pop3" select="glossaryMaker:addGlossaryObject($myGlossaryMaker,$myGlossaryObject)"/>
	</xsl:template>
	<!-- Write the alphabets contained in the current data source-->
	<xsl:template name="writeAlphabets">
		<xsl:param name="index"/>
		<xsl:param name="alpha_count"/>
		<xsl:variable name="alpha" select="glossaryMaker:getAlphabet($myGlossaryMaker, number($index))"/>
		<xsl:variable name="href" select="concat('#',$alpha)"/>
		<a class="glossary" href="{$href}">
			<xsl:value-of select="$alpha"/>
		</a>
		<xsl:if test="not(($index + 1)= $alpha_count)">
			<xsl:value-of select="'|'"/>
			<xsl:call-template name="writeAlphabets">
				<xsl:with-param name="index" select="$index + 1"/>
				<xsl:with-param name="alpha_count" select="$alpha_count"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- Write Terms-->
	<xsl:template name="writeTerms">
	<xsl:param name="taxonentry"/>
		<xsl:variable name="numberofTerms" select="glossaryMaker:getNumberOfTerms($myGlossaryMaker)"/>
		<xsl:variable name="indexVar" select="number($zero)"/>
		<xsl:call-template name="writeTerm">
			<xsl:with-param name="index" select="$indexVar"/>
			<xsl:with-param name="numberofTerms" select="$numberofTerms"/>
			<xsl:with-param name="taxonentry" select="$taxonentry"></xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	<!-- Write a Term-->
	<xsl:template name="writeTerm">
		<xsl:param name="index"/>
		<xsl:param name="numberofTerms"/>
		<xsl:param name="taxonentry"/>
		<xsl:variable name="term" select="glossaryMaker:getTerm($myGlossaryMaker,$index)"/>
		<xsl:variable name="firstLetter" select="translate(substring($term, 1, 1),$lcletters,$ucletters)"/>
		<xsl:if test="not($index=0)">
			<xsl:variable name="prevterm" select="glossaryMaker:getTerm($myGlossaryMaker,number($index)-1)"/>
			<xsl:variable name="firstLetterprev" select="translate(substring($prevterm ,1, 1),$lcletters,$ucletters)"/>
			<xsl:if test="not($firstLetter= $firstLetterprev)">
				<xsl:call-template name="outputHeader">
					<xsl:with-param name="alpha" select="$firstLetter"/>
					<xsl:with-param name="index" select="$index"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
		<xsl:if test="$index=0">
			<xsl:call-template name="outputHeader">
				<xsl:with-param name="alpha" select="$firstLetter"/>
				<xsl:with-param name="index" select="$index"/>
			</xsl:call-template>
		</xsl:if>
		<dl class="glossary">
			<xsl:call-template name="outputGlossaryTerm">
				<xsl:with-param name="term" select="$term"/>
			</xsl:call-template>
			<xsl:call-template name="outputGlossaryDefinitions">
				<xsl:with-param name="term" select="$term"/>
				<xsl:with-param name="taxonentry" select="$taxonentry"></xsl:with-param>
			</xsl:call-template>
			
		</dl>
		<div class="clearboth"></div>
		<xsl:if test="not(($index + 1)= $numberofTerms)">
			<xsl:call-template name="writeTerm">
				<xsl:with-param name="index" select="$index + 1"/>
				<xsl:with-param name="numberofTerms" select="$numberofTerms"/>
				<xsl:with-param name="taxonentry" select="$taxonentry"></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!--	 Output a header -->
	<xsl:template name="outputHeader">
		<xsl:param name="alpha"/>
		<xsl:param name="index"/>
		<!--		 Call this only when index is not zero -->
		<xsl:if test="not($index = 0)">
			<xsl:call-template name="outputGlossaryFooter"/>
		</xsl:if>
		<dl class="alphaheader">
			<dt class="alphaheader">
				<a name="{$alpha}"/>
				<xsl:value-of select="concat('- ',$alpha,' -')"/>
			</dt>
		</dl>
	</xsl:template>
	<!--Footer at the end of each alphabet set-->
	<xsl:template name="outputGlossaryFooter">
		<dl class="topofpage">
			<dd>
				<a class="topofpage" href="#top">Back</a>
			</dd>
		</dl>
		<div style="clear:both;"></div>
	</xsl:template>
	<!-- Output a term-->
	<xsl:template name="outputGlossaryTerm">
		<xsl:param name="term"/>
		<dt class="glossary">
			<xsl:value-of select="$term"/>
		</dt>
	</xsl:template>
	<xsl:template name="outputGlossaryImage">
			<xsl:param name="term"/>
			<xsl:param name="definition"/>
			<xsl:param name="taxonentry"/>
		
	<xsl:variable name="imagename" select="$taxonentry[(Items[@name=$termChar]/Item=$term) and (Items[@name=$definitionChar]/Item=$definition)][1]/MediaResources[@name=$imageChar]/MediaResource"/>
		<xsl:if test="not(string($imagename))=''">
			<xsl:variable name="imageURL">
				<xsl:value-of select="concat($serverbase, '/', $imagebase_thumbs, '/', $imagename)"/>
			</xsl:variable>
		<div class="glossaryicon">
		<img src="{$imageURL}" border="0"></img></div>
		</xsl:if>
	</xsl:template>
	<!--
Output a definition object-->
	<xsl:template name="outputGlossaryDefinition">
		<xsl:param name="term"/>
		<xsl:param name="numberofdefinitions"/>
		<xsl:param name="index"/>
		<xsl:param name="taxonentry"/>
		
		<xsl:variable name="definition" select="glossaryMaker:getDefinition($myGlossaryMaker,$term,$index)"/>
		<xsl:if test="not(string($definition))=''">
		<dd class="glossary">
			<xsl:call-template name="outputGlossaryImage">
			<xsl:with-param name="term" select="$term"/>
			<xsl:with-param name="definition" select="$definition"/>
			<xsl:with-param name="taxonentry" select="$taxonentry"/>
			</xsl:call-template>
			<xsl:value-of select="$definition"/>
		</dd>	
	
		</xsl:if>

		<xsl:if test="not($index+1=$numberofdefinitions)">
			<xsl:call-template name="outputGlossaryDefinition">
				<xsl:with-param name="term" select="$term"/>
				<xsl:with-param name="numberofdefinitions" select="$numberofdefinitions"/>
				<xsl:with-param name="index" select="number($index)+1"/>
				<xsl:with-param name="taxonentry" select="$taxonentry"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- Output Glossary Defintiions-->
	<xsl:template name="outputGlossaryDefinitions">
		<xsl:param name="term"/>
		<xsl:param name="taxonentry"/>
		<xsl:variable name="numberofdefinitions" select="glossaryMaker:getNumberOfDefinitions($myGlossaryMaker,$term)"/>
		<xsl:variable name="index" select="number($zero)"/>
		<xsl:call-template name="outputGlossaryDefinition">
			<xsl:with-param name="term" select="$term"/>
			<xsl:with-param name="numberofdefinitions" select="$numberofdefinitions"/>
			<xsl:with-param name="index" select="$index"/>
			<xsl:with-param name="taxonentry" select="$taxonentry"/>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
