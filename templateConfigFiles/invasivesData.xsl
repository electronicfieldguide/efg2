<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

<xsl:output method="html" indent="yes" encoding="ISO-8859-1"/>

<!-- 	This xsl stylesheet illustrates, how the generic library generic2html.xsl
		together with the definition of a named stylesheet "display-images" and
		the definition of some top level parameters can be instantiated so
		that a particular type of species pages can be produced.
-->

<!-- 	Specify a top level parameter representing the relative path to
		the pieces of metadata and resources for one particular efg application

		In this directory, there must exist files named
		- html_content_description.xml
			This file specifies the actual content of the abstract sections 
			title, images, identification, detail and copyright.

		- style.css
			A cascading stylesheet to customize the appearance of the generated
			html.

		This parameter has to be customized, that is, passed into the 
		stylesheet on invocation of the processor.
-->

<xsl:param name="resource-path">/efg2/templateConfigFiles</xsl:param>
<xsl:param name="servlet-path">/efg2</xsl:param>

<!--	Include the library image-lib.xsl which exports the named template
		"display-images". The task of this template is to produce a layout
		for the images of the page.
		
		The top level parameter "image-base" provides a base url, which prepended
		to image file names results in the url where the image(s) can actually
		be found
-->

<xsl:param name="image-base">/efg2/EFGImages</xsl:param>
<xsl:param name="imagebase-thumbs">/efg2/efgimagesthumbs</xsl:param>

<xsl:param name="imagebase-large">/efg2/EFGImages</xsl:param>

<xsl:param name="images-per-row">3</xsl:param>
<xsl:param name="html_content_description">/invasives.xml</xsl:param>
<xsl:include href="image-row-lib-nant.xsl" />

<!-- 	Include the generic library generic2html.xsl which provides almost
		all functionality to generate a species page.
-->

<xsl:include href="newgeneric2html_items.xsl" />


</xsl:stylesheet>
