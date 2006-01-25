<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:key name="b" match="Items/@name" use="Item"/>
	<xsl:template match="/">
		<html>
			<body>
				<table border="1" cellspacing="0">
					<!-- Generate the row of table header cells -->
					<!-- Generate a row for each salesman -->
					<xsl:for-each select="//TaxonEntry/Items">
						<!-- Sort by salesman name -->
						<xsl:sort select="@name"/>
						<!-- Keep the current salesman in a variable for later -->
						<xsl:variable name="cur" select="."/>
						<tr>
							<!-- First cell has the salesman's name -->
							<td bgcolor="yellow">
								<xsl:value-of select="@name"/>
							</td>
							<!-- Generate a cell for each unique region -->
							<xsl:for-each select="//Item[generate-id(.)=generate-id(key('b',.)[1])]">
								<td>
									<!-- If no accts for current salesman in current region, 
                   do &nbsp; -->
									<xsl:if test="not($cur[@name=current()])/Item">
                        &#160;</xsl:if>
									<!-- List matching accounts for current salesman in current region -->
									<xsl:for-each select="$cur/[@name=current()]/Item">
										<xsl:value-of select="Item"/>
										<xsl:if test="position() != last()">
											<br/>
										</xsl:if>
									</xsl:for-each>
								</td>
							</xsl:for-each>
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
