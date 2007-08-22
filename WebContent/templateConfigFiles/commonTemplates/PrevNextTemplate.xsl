<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template name="write_total_count">
		<xsl:param name="taxon_count" />
		<xsl:param name="total_count" />
		<xsl:choose>
			<xsl:when test="not(string($taxon_count))=''">
				<xsl:value-of select="$taxon_count" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$total_count" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="prevNext">
		<xsl:param name="prev" />
		<xsl:param name="next" />
		<xsl:param name="currentPageNumber" />
		<xsl:param name="totalNumberOfPages" />
		<xsl:param name="taxon_count" />
		
		
		<xsl:variable name="mainQuery">
			<xsl:choose>
				<xsl:when test="not(string($efgSessionID))=''">
					<xsl:value-of select="concat($serverbase,'/NextPrevPageMgmt.jsp?dataSourceName=',$dataSourceName,'&amp;taxon_count=',$taxon_count,'&amp;efgSessionID=',$efgSessionID)" />				
				</xsl:when>
				<xsl:otherwise>
				<xsl:value-of select="concat($serverbase,'/NextPrevPageMgmt.jsp?dataSourceName=',$dataSourceName,'&amp;taxon_count=',$taxon_count)" />				
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="nextValQuery"
			select="concat($mainQuery,'&amp;nextVal=',$next)" />
		<xsl:variable name="prevValQuery"
			select="concat($mainQuery,'&amp;nextVal=',$prev)" />

		<tr>
			<td colspan="3">
				<table class="pagenum">
					<tr>
					<td class="previous">
								<xsl:if test="not(string($prev))=''">
								
									<a class="prevnext"
										href="{$prevValQuery}">
										&lt; Previous |
									</a>
								
							</xsl:if>
						</td>
						
						<td class="pagenum">
							Page
							<span class="pagenum">
								<xsl:value-of
									select="$currentPageNumber" />
							</span>
							of
							<span class="pagenum">
								<xsl:value-of
									select="$totalNumberOfPages" />
							</span>
						</td>
						<td class="next">
						<xsl:if test="not(string($next))=''">
							<xsl:if
								test="number($currentPageNumber) &lt; number($totalNumberOfPages)">
								
									<a class="prevnext"
										href="{$nextValQuery}">
										| Next &gt;
									</a>
								
							</xsl:if>
						</xsl:if>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
