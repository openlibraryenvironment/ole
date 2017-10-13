<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright 2005-2014 The Kuali Foundation

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
<!DOCTYPE stylesheet [
<!ENTITY tab "&#x9;" ><!-- horizontal tab -->
<!ENTITY n "&#xa;" ><!-- LF -->
]>
<!--
  Default notification email style sheet
  @author Aaron Hamid (arh14 at cornell dot edu)
 -->
<!-- if this stylesheet hurts your eyes, it will hurt even worse if you try to use <xsl:text> -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:strip-space elements="*"/>

    <!-- "Muenchian" method of grouping: http://www.jenitennison.com/xslt/grouping/muenchian.html -->
    <!-- this is a map of document type names to nodesets -->
    <xsl:key name="doctypes-by-name" match="summarizedActionItem" use="docName"/>

    <xsl:template match="immediateReminder">
        <xsl:variable name="docHandlerUrl" select="actionItem/actionItem/docHandlerURL"/>
        <email>
            <subject>Action List Reminder <xsl:value-of select="actionItem/customSubject"/></subject>
            <body>Your Action List has an eDoc(electronic document) that needs your attention: 

Document ID:&tab;<xsl:value-of select="actionItem/actionItem/documentId"/>
Initiator:&tab;&tab;<xsl:value-of select="actionItem/docInitiatorDisplayName"/>
Type:&tab;&tab;Add/Modify <xsl:value-of select="actionItem/documentType/name"/>
Title:&tab;&tab;<xsl:value-of select="actionItem/actionItem/docTitle"/>


To respond to this eDoc: 
&tab;Go to <xsl:value-of select="$docHandlerUrl"/><xsl:choose>
  <xsl:when test="contains($docHandlerUrl, '?')">&amp;</xsl:when>
  <xsl:otherwise>?</xsl:otherwise>
</xsl:choose>docId=<xsl:value-of select="actionItem/actionItem/documentId"/>&amp;command=displayActionListView

&tab;Or you may access the eDoc from your Action List:
              &tab;Go to <xsl:value-of select="@actionListUrl"/>, and then click on the numeric Document ID: <xsl:value-of select="actionItem/actionItem/documentId"/> in the first column of the List.



              To change how these email notifications are sent(daily, weekly or none):
&tab;Go to <xsl:value-of select="@preferencesUrl"/>



For additional help, email <![CDATA[<mailto:]]><xsl:choose><xsl:when test="string(actionItem/documentType/notificationFromAddress)"><xsl:value-of select="actionItem/documentType/notificationFromAddress"/></xsl:when><xsl:otherwise><xsl:value-of select="@applicationEmailAddress"/></xsl:otherwise></xsl:choose><![CDATA[>]]>

<xsl:if test="@env != 'prd'">
Action Item sent to <xsl:value-of select="actionItem/actionItemPrincipalName"/>
<xsl:if test="string(actionItem/actionItem/delegationType)">
 for delegate type <xsl:value-of select="actionItem/actionItem/delegationType"/>
</xsl:if>
</xsl:if>

<xsl:value-of select="actionItem/customBody"/>
            </body>
        </email>
    </xsl:template>

    <xsl:template match="dailyReminder">
        <email>
            <subject>Action List Reminder</subject>
            <body>Your Action List has <xsl:value-of select="count(summarizedActionItem)"/> eDocs(electronic documents) that need your attention: 
<!-- "Muenchian" method of grouping: http://www.jenitennison.com/xslt/grouping/muenchian.html
     this clever little expression ensures that we only match the FIRST node
     for which there is a name-to-nodeset mapping.  More specifically, we want
     to ensure that we only match ONCE, but the FIRST node is the best node
     to match ONCE (or at least it's as good as any other; depends on whether
     we want to preserve relative ordering, etc.) -->
<xsl:for-each select="summarizedActionItem[count(. | key('doctypes-by-name', docName)[1]) = 1]">
    <!-- the xsl:sort modifies the for-each selection order (I think) -->
    <!-- <xsl:sort select="name" /> -->
    <!-- sort by count -->
    <!-- the order of identical values will be arbitrary in the Java map-based implementation;
         switching this to desceding here because 1) it seems more useful for a user and
         2) I want the unit test to pass, and given that it only uses two doc types, reversing
         the order will make it match the literal output of the Java version ;) -->
    <xsl:sort data-type="number" select="count(key('doctypes-by-name', docName))" order="descending"/>
<xsl:text>&tab;</xsl:text><xsl:value-of select="count(key('doctypes-by-name', docName))"/><xsl:text>&tab;</xsl:text><xsl:value-of select="docName"/><xsl:text>&n;</xsl:text>
</xsl:for-each>

To respond to each of these eDocs: 
&tab;Go to <xsl:value-of select="@actionListUrl"/>, and then click on its numeric Document ID in the first column of the List.



To change how these email notifications are sent (immediately, weekly or none): 
&tab;Go to <xsl:value-of select="@preferencesUrl"/>



For additional help, email <![CDATA[<mailto:]]><xsl:value-of select="@applicationEmailAddress"/><![CDATA[>]]>


</body>
        </email>
    </xsl:template>
    
    <xsl:template match="weeklyReminder">
        <email>
            <subject>Action List Reminder</subject>
            <body>Your Action List has <xsl:value-of select="count(summarizedActionItem)"/> eDocs(electronic documents) that need your attention: 
<!-- "Muenchian" method of grouping: http://www.jenitennison.com/xslt/grouping/muenchian.html
     this clever little expression ensures that we only match the FIRST node
     for which there is a name-to-nodeset mapping.  More specifically, we want
     to ensure that we only match ONCE, but the FIRST node is the best node
     to match ONCE (or at least it's as good as any other; depends on whether
     we want to preserve relative ordering, etc.) -->
<xsl:for-each select="summarizedActionItem[count(. | key('doctypes-by-name', docName)[1]) = 1]">
    <!-- the xsl:sort modifies the for-each selection order (I think) -->
    <!-- <xsl:sort select="name" /> -->
    <!-- sort by count -->
    <!-- the order of identical values will be arbitrary in the Java map-based implementation;
         switching this to desceding here because 1) it seems more useful for a user and
         2) I want the unit test to pass, and given that it only uses two doc types, reversing
         the order will make it match the literal output of the Java version ;) -->
    <xsl:sort data-type="number" select="count(key('doctypes-by-name', docName))" order="descending"/>
<xsl:text>&tab;</xsl:text><xsl:value-of select="count(key('doctypes-by-name', docName))"/><xsl:text>&tab;</xsl:text><xsl:value-of select="docName"/><xsl:text>&n;</xsl:text>
</xsl:for-each>

To respond to each of these eDocs: 
&tab;Go to <xsl:value-of select="@actionListUrl"/>, and then click on its numeric Document ID in the first column of the List.



To change how these email notifications are sent (immediately, daily or none): 
&tab;Go to <xsl:value-of select="@preferencesUrl"/>



For additional help, email <![CDATA[<mailto:]]><xsl:value-of select="@applicationEmailAddress"/><![CDATA[>]]>


</body>
        </email>
    </xsl:template>

    <xsl:template match="feedback">
        <email>
            <subject>Feedback from <xsl:value-of select="networkId"/>
              <xsl:variable name="documentId" select="documentId"/>
              <xsl:choose>
                <xsl:when test="string($documentId)"> for document <xsl:value-of select="$documentId"/></xsl:when>
              </xsl:choose>
            </subject>
            <body>
Network ID: <xsl:value-of select="networkId"/>
Name: <xsl:value-of select="userName"/>
Email: <xsl:value-of select="userEmail"/>
Phone: <xsl:value-of select="phone"/>
Time: <xsl:value-of select="timeDate"/>
Environment: <xsl:value-of select="@env"/>

Document type: <xsl:value-of select="documentType"/>
Document id: <xsl:value-of select="documentId"/>

Category: <xsl:value-of select="category"/>
Comments: 
<xsl:value-of select="comments"/>

Exception: 
<xsl:value-of select="exception"/>
            </body>
        </email>
    </xsl:template>
</xsl:stylesheet>
