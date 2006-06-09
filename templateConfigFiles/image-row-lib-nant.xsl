<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="display-images">
		<xsl:param name="image_contents"/>
		<xsl:call-template name="debug_out">
			<xsl:with-param name="msg">Image Content Items: </xsl:with-param>
			<xsl:with-param name="value" select="$image_contents"/>
		</xsl:call-template>
		<table class="images" cellspacing="5" align="center">
			<xsl:call-template name="disp-image-row">
				<xsl:with-param name="items" select="$image_contents"/>
				<xsl:with-param name="hd-length">0</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	<xsl:template name="disp-image-row">
		<xsl:param name="items"/>
		<xsl:param name="hd-length"/>
		<xsl:choose>
			<xsl:when test="boolean($items)=false()">
				<xsl:if test="$hd-length &gt; 0">
					<xsl:call-template name="display-imgs">
						<xsl:with-param name="items" select="$items"/>
						<xsl:with-param name="padding" select="$images-per-row - $hd-length"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$hd-length=$images-per-row">
				<xsl:call-template name="display-imgs">
					<xsl:with-param name="items" select="$items[position() &lt;= $images-per-row]"/>
					<xsl:with-param name="padding">0</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="disp-image-row">
					<xsl:with-param name="items" select="$items[position() &gt; $images-per-row]"/>
					<xsl:with-param name="hd-length">0</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="disp-image-row">
					<xsl:with-param name="items" select="$items"/>
					<xsl:with-param name="hd-length" select="1+$hd-length"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="display-imgs">
		<xsl:param name="items"/>
		<xsl:param name="padding"/>
		<tr>
			<xsl:call-template name="disp-images">
				<xsl:with-param name="items" select="$items"/>
			</xsl:call-template>
			<xsl:call-template name="disp-padding">
				<xsl:with-param name="padding" select="$padding"/>
			</xsl:call-template>
		</tr>
	</xsl:template>
	<xsl:template name="disp-images">
		<xsl:param name="items"/>
		<xsl:choose>
			<xsl:when test="boolean($items)=false()"> 		</xsl:when>
			<xsl:otherwise>
			<xsl:variable name="images">	<xsl:call-template name="constructImage">
			<xsl:with-param name="MediaResources" select="./MediaResources[@name=$items[1]/@name]"/>
			</xsl:call-template></xsl:variable>
		
				<xsl:call-template name="display-image">
					<xsl:with-param name="image"  select="$images"/>
					<xsl:with-param name="item" select="$items[1]"/>
				</xsl:call-template>
				<xsl:call-template name="disp-images">
					<xsl:with-param name="items" select="$items[position()>1]"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="constructImage">
		<xsl:param name="MediaResources"/>
		
		<xsl:for-each select="$MediaResources/MediaResource">
		<xsl:variable name="image">
			<xsl:value-of select="."/>
		</xsl:variable>
		<xsl:if test="not(string($image))=''">
			<xsl:if test="position() > 1">
				<xsl:value-of select="','"/>
			</xsl:if>
			<xsl:value-of select="$image"/>
		</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="disp-padding">
		<xsl:param name="padding"/>
		<xsl:if test="$padding &gt; 0">
			<td/>
			<xsl:call-template name="disp-padding">
				<xsl:with-param name="padding" select="$padding - 1"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template name="display-image">
		<xsl:param name="image"/>
		<xsl:param name="item"/>
		
		<xsl:choose>
			<xsl:when test="contains($image,',')">
				<xsl:call-template name="display-image">
					<xsl:with-param name="image" select="substring-before($image,',')"/>
					<xsl:with-param name="item" select="$item"/>
				</xsl:call-template>
				<xsl:call-template name="display-image">
					<xsl:with-param name="image" select="substring-after($image,',')"/>
					<xsl:with-param name="item" select="$item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="boolean($image)=true()">
				<xsl:if test="not(string($image))=''">
					<td class="id_text">
						<xsl:attribute name="class"><xsl:value-of select="$item/caption/@class"/></xsl:attribute>
						<a>
									<xsl:attribute name="href"><xsl:value-of select="concat($imagebase-large,'/',$image)"/></xsl:attribute>
							<img>
								<xsl:attribute name="src"><xsl:value-of select="concat($imagebase-thumbs,'/',string($image))"/></xsl:attribute>
								<xsl:attribute name="alt"><xsl:value-of select="$item/caption"/></xsl:attribute>
							</img>
						</a>
						<br/>
						<xsl:value-of select="$item/caption"/>
					</td>
					</xsl:if>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp. <metaInformation> <scenarios/> </metaInformation> -->
