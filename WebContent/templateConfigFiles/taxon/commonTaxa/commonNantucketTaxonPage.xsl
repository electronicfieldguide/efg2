<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1" xmlns:xalan="http://xml.apache.org/xalan" xmlns:imageList="project.efg.server.utils.ImageDisplayList" extension-element-prefixes="imageList" exclude-result-prefixes="imageList">
	<xalan:component prefix="imageList" functions="addImageDisplay getImageName  getImageCaption getSize">
		<xalan:script lang="javaclass" src="xalan://project.efg.server.utils.ImageDisplayList"/>
	</xalan:component>
    <xsl:template name="outputList">
        <xsl:param name="fieldValue"/>
        <xsl:param name="resourcelink"/>
        <xsl:param name="annotation"/>
       
        <xsl:variable name="annot">
            <xsl:value-of select="normalize-space($annotation)"/>
        </xsl:variable>
        <xsl:if test="not(string($fieldValue))=''">
            <xsl:choose>
                <xsl:when test="not(string($resourcelink))=''">
                <xsl:variable name="toUpperCase" select="translate($resourcelink,'http://','HTTP://')"/>
                    <xsl:variable name="url">
                        <xsl:choose>
                            <xsl:when test="contains($toUpperCase,'HTTP://')">
                                <xsl:value-of select="concat(string($resourcelink),string($fieldValue))"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="contains($resourcelink,$dsNamePrefix)">
                                        <xsl:value-of select="concat(string($serverbase),$mapQueryServlet,'&amp;',$resourcelink,'=',$fieldValue,'&amp;displayFormat=html')"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="concat(string($serverbase),$mapQueryServlet,string($dsNamePrefix), $dataSourceName,'&amp;',$resourcelink,'=',$fieldValue,'&amp;displayFormat=html')"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                       <li>
                        <a href="{$url}">
                            <xsl:value-of select="$fieldValue"/> 
                        </a>
                        <xsl:if test="not(string($annot))=''">
                           <xsl:value-of select="concat(' ',$annot)"/>
                        </xsl:if>
                  </li>
                </xsl:when>
                <xsl:otherwise>
                     <li><xsl:value-of select="$fieldValue"/>
                    <xsl:if test="not(string($annot))=''">
                   
                        <xsl:value-of select="concat(' ',$annot)"/>
                       
                    </xsl:if>
                     </li>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
	</xsl:stylesheet>
