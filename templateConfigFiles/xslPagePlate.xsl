<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:variable name="xslPage" select="document($templateConfigFile)//TaxonPageTemplate[@datasourceName=$dataSourceName]/XSLFileNames/xslPlatePages/xslPage[@fileName=$xslName and @guid=$guid]"/>

</xsl:stylesheet>
