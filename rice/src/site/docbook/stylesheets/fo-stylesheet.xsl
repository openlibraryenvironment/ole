<?xml version='1.0'?>
<!--

    Copyright 2005-2013 The Kuali Foundation

    Licensed under the Educational Community License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.opensource.org/licenses/ecl2.php

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:import href="http://docbook.sourceforge.net/release/xsl/current/fo/docbook.xsl"/>	
    <xsl:import href="appendix.xsl"/>
    <xsl:import href="xref.xsl"/>
    <xsl:import href="titlepage.xsl"/>
    <xsl:param name="callout.graphics.path">src/site/docbook/images/callouts/</xsl:param>
    <xsl:template match="*[@role = 'keyword']" mode="class.value">
        <xsl:value-of select="'keyword'"/>
    </xsl:template>
    <xsl:template match="emphasis[@role = 'keyword']">
        <fo:inline> 
            <xsl:call-template name="dbfo-attribute"/>
            <xsl:attribute name="background-color">#EEEEEE</xsl:attribute>
            <xsl:attribute name="font-family">monospace</xsl:attribute>
            <xsl:attribute name="font-weight">bold</xsl:attribute>
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="section[@role = 'NotInToc']" mode="toc" />
    <xsl:attribute-set name="monospace.verbatim.properties">
        <xsl:attribute name="wrap-option">wrap</xsl:attribute>
        <xsl:attribute name="font-size">7pt</xsl:attribute>
        <xsl:attribute name="start-indent">0pt</xsl:attribute>
        <xsl:attribute name="end-indent">0pt</xsl:attribute>
        <xsl:attribute name="border">1px solid #e1e1e8</xsl:attribute>
        <xsl:attribute name="background-color">#f5f5f5</xsl:attribute>
        <xsl:attribute name="padding">9.5px</xsl:attribute>
    </xsl:attribute-set>
    <xsl:template name="table.cell.block.properties">
        <xsl:attribute name="font-size">7pt</xsl:attribute>
    </xsl:template>
    <xsl:template name="table.row.properties">
        <xsl:variable name="tabstyle">
            <xsl:call-template name="tabstyle"/>
        </xsl:variable>
        <xsl:variable name="bgcolor">
            <xsl:call-template name="dbfo-attribute">
                <xsl:with-param name="pis" select="processing-instruction('dbfo')"/>
                <xsl:with-param name="attribute" select="'bgcolor'"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="rownum">
            <xsl:number from="tgroup" count="row"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$bgcolor != ''">
                <xsl:attribute name="background-color">
                    <xsl:value-of select="$bgcolor"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:when test="$tabstyle = 'striped'">
                <xsl:if test="$rownum mod 2 = 0">
                    <xsl:attribute name="background-color">#EEEEEE</xsl:attribute>
                </xsl:if>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>