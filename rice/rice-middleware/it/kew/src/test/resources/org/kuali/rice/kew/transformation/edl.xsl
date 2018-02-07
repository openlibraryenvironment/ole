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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

        <!-- TODO: import string.xsl so we can uppercase() the @name in absence of @title -->
        <!-- TODO: try to find a solution for 'xmlns' pollution on literal result elements -->

        <xsl:output doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes" method="html" omit-xml-declaration="yes"/>

        <xsl:param name="formTarget" select="'EDocLite'"/>
        <!-- whether the FIELDS can be edited -->
        <xsl:variable name="readOnly" select="/documentContent/documentState/editable != 'true'"/>
        <!-- whether the form can be acted upon -->
        <xsl:variable name="actionable" select="/documentContent/documentState/actionable = 'true'"/>
        <xsl:variable name="docId" select="/documentContent/documentState/docId"/>
        <xsl:variable name="def" select="/documentContent/documentState/definition"/>
        <xsl:variable name="docType" select="/documentContent/documentState/docType"/>
        <xsl:variable name="style" select="/documentContent/documentState/style"/>
        <xsl:variable name="annotatable" select="/documentContent/documentState/annotatable = 'true'"/>

        <xsl:template match="/documentContent">
          <xsl:variable name="docTitle">
            <xsl:choose>
              <xsl:when test="//edlContent/edl/@title">
                <xsl:value-of select="//edlContent/edl/@title"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="//edlContent/edl/@name"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <xsl:variable name="pageTitle">
            <xsl:choose>
              <xsl:when test="$readOnly = 'true'">
                Viewing
              </xsl:when>
              <xsl:otherwise>
                Editing 
              </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="$docTitle"/>
            (<xsl:value-of select="$docType"/>)
          </xsl:variable>

          <html>
            <head>
              <title>
                <xsl:value-of select="$pageTitle"/> document
              </title>
              <link href="css/screen.css" rel="stylesheet" type="text/css"/>
              <link href="css/edoclite.css" rel="stylesheet" type="text/css"/>
              <script src="scripts/edoclite.js" type="text/javascript"/>
              <xsl:comment>
                Meta data block for automation/testing
                [var editable_value=<xsl:value-of select="documentState/editable"/>]
                [var annotatable_value=<xsl:value-of select="documentState/annotatable"/>]
                [var readonly=<xsl:value-of select="$readOnly"/>]
                [var annotatable=<xsl:value-of select="$annotatable"/>]
                [var annotation=<xsl:value-of select="//edlContent/data/version[@current='true']/annotation"/>]
                [transient start]
                [var docid=<xsl:value-of select="$docId"/>]
                [transient end]
                [var doctype=<xsl:value-of select="$docType"/>]
                [var def=<xsl:value-of select="$def"/>]
                [var style=<xsl:value-of select="$style"/>]
              </xsl:comment>

            </head>
            <body onload="onPageLoad()">
              <xsl:call-template name="header"/>

              <xsl:variable name="instructions">
                <xsl:choose>
                  <xsl:when test="edlContent/edl/instructions">
                    <xsl:value-of select="edlContent/edl/instructions"/>
                  </xsl:when>
                  <xsl:otherwise>
                    Review <xsl:value-of select="$docTitle"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

              <xsl:variable name="createInstructions">
                <xsl:choose>
                  <xsl:when test="edlContent/edl/createInstructions">
                    <xsl:value-of select="edlContent/edl/createInstructions"/>
                  </xsl:when>
                  <xsl:otherwise>
                    Fill out new <xsl:value-of select="$docTitle"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

              <xsl:call-template name="instructions">
                <xsl:with-param name="title" select="$pageTitle"/>
                <xsl:with-param name="createInstructions" select="$createInstructions"/>
                <xsl:with-param name="instructions" select="$instructions"/>
              </xsl:call-template>
              <xsl:call-template name="errors"/>
              <xsl:call-template name="main-body"/>
              <xsl:call-template name="footer"/>
            </body>
          </html>
        </xsl:template>


        <xsl:template name="instructions">
          <xsl:param name="title"/>
          <xsl:param name="instructions"/>
          <xsl:param name="createInstructions"/>
          <table align="center" border="0" cellpadding="10" cellspacing="0" width="80%">
            <tr>
              <td>
                <strong>
                  <xsl:value-of select="$title"/>
                </strong>
              </td>
            </tr>
            <tr>
              <td>
                <!-- let's just assume that if create action is present that this is
                     prior to creation and nothing else is present (that wouldn't really
                     make sense). -->
                <xsl:choose>
                  <xsl:when test="documentState/actionsPossible/create">
                    <xsl:value-of select="$createInstructions"/>
				  </xsl:when>
				  <xsl:otherwise>
                    <xsl:value-of select="$instructions"/>
				  </xsl:otherwise>
                </xsl:choose>
              </td>
            </tr>
          </table>
        </xsl:template>


        <xsl:template name="errors">

          <table align="center" border="0" cellpadding="10" cellspacing="0" width="80%">
            <xsl:for-each select="documentState/error">
              <tr>
                <td>
                  <div class="error-message">
                    <xsl:value-of select="."/>
                  </div>
                </td>
              </tr>

            </xsl:for-each>
          </table>

        </xsl:template>


        <xsl:template name="main-body">
          <form action="{$formTarget}" id="edoclite" method="post" onsubmit="return validateOnSubmit()">
            <xsl:call-template name="hidden-params"/>
            <table align="center" border="0" cellpadding="0" cellspacing="0" class="bord-r-t" width="80%">
              <xsl:apply-templates select="edlContent"/>
              <!-- should this really live here? or should it be part of the EDL doc somewhere?
                   message maybe? -->
              <xsl:variable name="annotation" select="//edlContent/data/version[@current='true']/annotation"/>
              <xsl:if test="$annotatable or $annotation">
              <tr>
                <td align="center" class="thnormal" colspan="2">
                    <xsl:if test="$annotation">
                      Current annotation: <xsl:value-of select="$annotation"/>
<br/>
                    </xsl:if>
                    <xsl:if test="$annotatable">
                      Set annotation:<br/>
                      <textarea name="annotation">
                      </textarea>
                    </xsl:if>
                </td>
              </tr>
              </xsl:if>
              <xsl:call-template name="buttons"/>
            </table>
          </form>
        </xsl:template>

        <xsl:template name="buttons">
          <xsl:if test="documentState/actionsPossible/*">
            <tr>
              <td align="center" class="thnormal" colspan="2">
                <xsl:text>
                </xsl:text>
                <xsl:for-each select="documentState/actionsPossible/*[. != 'returnToPrevious']">
                  <xsl:variable name="actionTitle">
                    <xsl:choose>
                      <xsl:when test="@title">
                        <xsl:value-of select="@title"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="local-name()"/>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:variable>
                  <input name="action" title="{$actionTitle}" type="submit" value="{local-name()}">
                    <xsl:if test="not($actionable)">
                      <xsl:attribute name="disabled">disabled</xsl:attribute>
                    </xsl:if>
                  </input>
                  <xsl:text>
                  </xsl:text>
                </xsl:for-each>
                <xsl:if test="documentState/actionsPossible/returnToPrevious">
                  <select name="previousNode">
                    <xsl:if test="not($actionable)">
                      <xsl:attribute name="disabled">disabled</xsl:attribute>
                    </xsl:if>
                    <xsl:for-each select="documentState/previousNodes/*">
                      <option value="{@name}">
<xsl:value-of select="@name"/>
</option>
                    </xsl:for-each>
                  </select>
                  <xsl:text>
                  </xsl:text>
                </xsl:if>
              </td>
            </tr>
          </xsl:if>
        </xsl:template>


        <xsl:template match="edl">
          <xsl:comment>start template: edl</xsl:comment>
          <!-- iterate over field elements
               the reason we do this instead of using a template is so
               that we can perform a common prologue.  we could potentially
               do this in a separate template with match="field" but then
               we have the problem of how to subsequently pass processing
               onto template specialized for field 'type' without
               instigating infinite recursion (e.g. we can't apply-templates to
               '.') -->

          <xsl:for-each select="fieldDef">
            <!-- common logic.  is there a better way to factor this out? -->
            <xsl:variable name="fieldDisplayName">
              <xsl:choose>
                <xsl:when test="@title">
                  <xsl:value-of select="@title"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="@name"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:variable>

            <xsl:variable name="regex" select="validation/regex"/>
            <xsl:variable name="validation_required" select="validation/@required = 'true'"/>
            <xsl:variable name="message">
              <!-- <xsl:if test="//edlContent/data/version[@current='true']/field[@name=current()/@name]"> -->
                <xsl:choose>
                  <xsl:when test="//edlContent/data/version[@current='true']/field[@name=current()/@name]/errorMessage">
                    <xsl:value-of select="//edlContent/data/version[@current='true']/field[@name=current()/@name]/errorMessage"/>
                  </xsl:when>
                  <xsl:when test="validation/message">
                    <xsl:value-of select="validation/message"/>
                  </xsl:when>
                  <xsl:when test="validation/regex">
                    <xsl:value-of select="$fieldDisplayName"/>
                    (<xsl:value-of select="@name"/>)
                    <xsl:text> does not match '</xsl:text>
                    <xsl:value-of select="$regex"/>
                    <xsl:text>'</xsl:text>
                  </xsl:when>
                  <xsl:otherwise/>
                </xsl:choose>
              <!-- </xsl:if> -->
            </xsl:variable>

            <xsl:variable name="custommessage">
              <xsl:choose>
                <xsl:when test="//edlContent/data/version[@current='true']/field[@name=current()/@name]/errorMessage">
                  <xsl:value-of select="//edlContent/data/version[@current='true']/field[@name=current()/@name]/errorMessage"/>
                </xsl:when>
                <xsl:otherwise>NONE</xsl:otherwise>
              </xsl:choose>
            </xsl:variable>
            
            <xsl:comment>
              custom message: <xsl:value-of select="$custommessage"/>
              validation/message: <xsl:value-of select="validation/message"/>
               message: <xsl:value-of select="$message"/>
            </xsl:comment>
            <xsl:variable name="invalid" select="//edlContent/data/version[@current='true']/field[@name=current()/@name]/@invalid"/>

          <!-- determine value to display: use the value specified in the current version
               if it exists, otherwise use the 'default' value defined in the field -->
            <xsl:variable name="userValue" select="//edlContent/data/version[@current='true']/field[@name=current()/@name]/value"/>
            
            <xsl:variable name="value">
              <xsl:choose>
                <xsl:when test="$hasUserValue">
                  <xsl:value-of select="$userValue"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="value"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:variable>

            <!-- message row -->
            <xsl:variable name="hasUserValue" select="boolean($userValue)" /><xsl:variable name="type">
              <xsl:choose>
                <xsl:when test="$invalid and $validation_required">error</xsl:when>
                <xsl:when test="$invalid and not($validation_required)">warning</xsl:when>
                <xsl:otherwise>empty</xsl:otherwise>
              </xsl:choose>
            </xsl:variable>
            <tr class="{$type}_messageRow" id="{@name}_messageRow" xmlns="http://www.w3.org/1999/xhtml">
              <td class="{$type}_messageHeaderCell" id="{@name}_messageHeaderCell">
                <xsl:value-of select="$type"/>
              </td>
              <td class="{$type}_messageDataCell" id="{@name}_messageDataCell">
                <span id="{@name}_message">
                  <xsl:value-of select="$message"/>
                </span>
              </td>
            </tr>

            <tr>
              <td class="thnormal" width="30%">
                <xsl:value-of select="$fieldDisplayName"/>
              </td>

              <td class="datacell">
                <!-- register this field FOR VALIDATION -->
                <!-- took me a long time to find out XHTML (strict at least) does not
                     accept comments in Javascript (it consumes the content, which is
                     the right, strict, thing to do. -->
                <!-- OK: FIRST check whether validation is possible at all.  Validation is possible
                     if there is EITHER a regex specified (and required='false' or omitted) OR
                     a regex is NOT specified but required is present and equals 'true' -->
                <!-- if neither are present then disregard this field entirely and just preserve
                     the existing warning/error if any -->
                <xsl:if test="validation/regex or validation[@required='true']">
                  <script type="text/javascript">
                    // register field
                    register("<xsl:value-of select="@name"/>","<xsl:value-of select="$fieldDisplayName"/>","<xsl:value-of select="$regex"/>","<xsl:value-of select="$message"/>","<xsl:value-of select="$validation_required"/>");
                    // end comment
                  </script>
                </xsl:if>

                <xsl:apply-templates select=".">
                  <xsl:with-param name="hasUserValue">
                    <xsl:value-of select="$hasUserValue"/>
                  </xsl:with-param>
                  <xsl:with-param name="value">
                    <xsl:value-of select="$value"/>
                  </xsl:with-param>
                  <xsl:with-param name="invalid">
                    <xsl:value-of select="$invalid"/>
                  </xsl:with-param>
                  <xsl:with-param name="regex">
                    <xsl:value-of select="$regex"/>
                  </xsl:with-param>
                  <xsl:with-param name="message">
                    <xsl:value-of select="$message"/>
                  </xsl:with-param>
                  <xsl:with-param name="validation_required">
                    <xsl:value-of select="$validation_required"/>
                  </xsl:with-param>
                </xsl:apply-templates>
              </td>
            </tr>
          </xsl:for-each>
          <xsl:comment>end template: edl</xsl:comment>
        </xsl:template>




      <!-- a simple text field -->
        <xsl:template match="fieldDef[display/type='text' or display/type='password']">
          <xsl:param name="value"/>
          <xsl:param name="regex"/>
          <xsl:param name="message"/>
          <xsl:param name="validation_required"/>

          <xsl:comment>start template: fieldDef[display/type='text']</xsl:comment>

        <!-- retrieve size specifier meta data -->
          <xsl:variable name="size" select="display/meta[name='size']/value"/>

          <input name="{@name}" type="{display/type}" value="{$value}">
            <xsl:if test="$readOnly = 'true'">
              <xsl:attribute name="disabled">disabled</xsl:attribute>
            </xsl:if>
            <xsl:if test="$size">
              <xsl:attribute name="size">
                <xsl:value-of select="$size"/>
              </xsl:attribute>
            </xsl:if>

          <!--
          <xsl:if test="$regex">
            <xsl:attribute name="onchange">
              <xsl:text>validate(event,'</xsl:text>
              <xsl:value-of select="$regex"/>
              <xsl:text>','</xsl:text>
              <xsl:value-of select="$message"/>
              <xsl:text>');</xsl:text>
            </xsl:attribute>
          </xsl:if>
          -->
          </input>
          <xsl:comment>end template: fieldDef[display/type='text']</xsl:comment>
        </xsl:template>

      <!-- a text area -->
        <xsl:template match="fieldDef[display/type='textarea']">
          <xsl:param name="value"/>
          <xsl:param name="regex"/>
          <xsl:param name="message"/>
          <xsl:param name="validation_required"/>

          <xsl:comment>start template: field[display/type='textarea']</xsl:comment>

        <!-- retrieve cols and rows specifier meta data -->
          <xsl:variable name="metaCols" select="display/meta[name='cols']/value"/>
          <xsl:variable name="cols">
            <xsl:choose>
              <xsl:when test="$metaCols">
                <xsl:value-of select="$metaCols"/>
              </xsl:when>
              <xsl:otherwise>1</xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="metaRows" select="display/meta[name='rows']/value"/>
          <xsl:variable name="rows">
            <xsl:choose>
              <xsl:when test="$metaRows">
                <xsl:value-of select="$metaRows"/>
              </xsl:when>
              <xsl:otherwise>1</xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <textarea cols="{$cols}" name="{@name}" rows="{$rows}" xmlns="http://www.w3.org/1999/xhtml">
            <xsl:if test="$readOnly = 'true'">
              <xsl:attribute name="disabled">disabled</xsl:attribute>
            </xsl:if>
          <!-- force a space if value is empty, or browsers (firefox)
               set the rest of the literal body content as the value
               if the tag is a short-form close tag (!) -->
            <xsl:choose>
              <xsl:when test="string-length($value) &gt; 0">
                <xsl:value-of select="$value"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:text> </xsl:text>
              </xsl:otherwise>
            </xsl:choose>

          <!--
          <xsl:if test="$regex">
            <xsl:attribute name="onchange">
              <xsl:text>validate(event,'</xsl:text>
              <xsl:value-of select="$regex"/>
              <xsl:text>','</xsl:text>
              <xsl:value-of select="$message"/>
              <xsl:text>');</xsl:text>
            </xsl:attribute>
          </xsl:if>
          -->
          </textarea>

          <xsl:comment>end template: fieldDef[display/type='textarea']</xsl:comment>
        </xsl:template>

      <!-- a set of radio buttons -->
        <xsl:template match="fieldDef[display/type='radio' or display/type='checkbox']">
          <xsl:param name="hasUserValue"/>
          <xsl:param name="value"/>
          <xsl:comment>start template: fieldDef[display/type='radio' or display/type='checkbox']</xsl:comment>

          <xsl:comment>hasUserValue: <xsl:value-of select="$hasUserValue"/>
          </xsl:comment>

          <xsl:for-each select="display/values">
            <xsl:variable name="title">
              <xsl:choose>
                <xsl:when test="@title">
                  <xsl:value-of select="@title"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="@name"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:variable>
            <xsl:variable name="optionName">
              <xsl:value-of select="../../@name"/>
            </xsl:variable>
            <xsl:text>
            </xsl:text>
            <input name="{$optionName}" title="{$title}" type="{../type}" value="{.}" xmlns="http://www.w3.org/1999/xhtml">
              <xsl:if test="$readOnly = 'true'">
                <xsl:attribute name="disabled">disabled</xsl:attribute>
              </xsl:if>
              <xsl:choose>
                <xsl:when test="$hasUserValue">
                  <xsl:if test="//edlContent/data/version[@current='true']/field[@name=current()/../../@name and value=current()]">
                    <xsl:attribute name="checked">checked</xsl:attribute>
                  </xsl:if>
                </xsl:when>
                <xsl:otherwise>
                <!-- use the default if no user values are specified -->
                  <xsl:if test=". = ../../value">
                    <xsl:attribute name="checked">checked</xsl:attribute>
                  </xsl:if>
                </xsl:otherwise>
              </xsl:choose>
            </input>
            <xsl:text>
            </xsl:text>
            <xsl:value-of select="$title"/>
          </xsl:for-each>
          <xsl:text>
          </xsl:text>
          <xsl:comment>end template: fieldDef[display/type='radio' or display/type='checkbox']</xsl:comment>
        </xsl:template>

      <!-- a drop-down selection -->
        <xsl:template match="fieldDef[display/type='select']">
          <xsl:param name="hasUserValue"/>
          <xsl:param name="value"/>
          <xsl:comment>start template: fieldDef[display/type='select']</xsl:comment>

          <xsl:comment>
            INCOMING VALUE: <xsl:value-of select="$value"/>
            FIELD NAME: <xsl:value-of select="current()/@name"/>
            FIELD DATA EXISTS: <xsl:value-of select="boolean(//edlContent/data/version[@current='true']/field[@name=current()/@name])"/>
            RETRIEVED FIELD DATA VALUE: <xsl:value-of select="//edlContent/data/version[@current='true']/field[@name=current()/@name]/value"/>

            <xsl:for-each select="display/values">
              ====================================
              CURRENT (value): <xsl:value-of select="current()"/>
              FIELD DATA MATCHES: <xsl:value-of select="boolean(//edlContent/data/version[@current='true']/field[@name=current()/../../@name and value=current()])"/>
              VALUE EQUALS CURRENT: <xsl:value-of select="$value = current()"/>
              ====================================
            </xsl:for-each>
          </xsl:comment>

          <select name="{@name}" xmlns="http://www.w3.org/1999/xhtml">
          <!-- TODO: decide how to handle optiona/required value selections
               (same goes with checkbox and radio buttons potentially -->
            <xsl:if test="$readOnly = 'true'">
              <xsl:attribute name="disabled">disabled</xsl:attribute>
            </xsl:if>
            <!-- empty selections can be specified in edl config...don't hardcode one here -->
            <!-- <option title="no selection" value=""/> -->

            <xsl:for-each select="display/values">
              <xsl:variable name="title">
                <xsl:choose>
                  <xsl:when test="@title">
                    <xsl:value-of select="@title"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="@name"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:variable>
              <option title="{$title}" value="{.}">
                <xsl:choose>
                  <xsl:when test="$hasUserValue">
                    <xsl:if test="//edlContent/data/version[@current='true']/field[@name=current()/../../@name and value=current()]">
                    <!-- <xsl:if test="$value = current()"> -->
                      <xsl:attribute name="selected">selected</xsl:attribute>
                    </xsl:if>
                  </xsl:when>
                  <xsl:otherwise>
                  <!-- use the default if no user values are specified -->
                    <xsl:if test=". = ../../value">
                      <xsl:attribute name="selected">selected</xsl:attribute>
                    </xsl:if>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:value-of select="$title"/>
              </option>
            </xsl:for-each>
          </select>

          <xsl:comment>end template: fieldDef[display/type='select']</xsl:comment>
        </xsl:template>

        <xsl:template name="hidden-params">
          <xsl:comment>hide this nastiness so we can concentrate on formating above</xsl:comment>
          <div style="display: none">
            <xsl:choose>
              <xsl:when test="$docId">
                <!-- preserve the data for comparison without transient value -->
                <xsl:comment>input name="docId" type="hidden"</xsl:comment>
                <!-- mark the entire input element transient because we can't insert
                     comments in the middle of a tag just to omit a certain attribute -->
                <xsl:comment>[transient start]</xsl:comment>
                <input name="docId" type="hidden" value="{$docId}"/>
                <xsl:comment>[transient end]</xsl:comment>
              </xsl:when>
              <xsl:otherwise>
                <xsl:if test="$docType">
                  <input name="docType" type="hidden" value="{$docType}"/>
                </xsl:if>
                <xsl:if test="$def">
                  <input name="def" type="hidden" value="{$def}"/>
                </xsl:if>
                <xsl:if test="$style">
                  <input name="style" type="hidden" value="{$style}"/>
                </xsl:if>
              </xsl:otherwise>
            </xsl:choose>
          </div>
        </xsl:template>

        <xsl:template name="header">
          <table border="0" cellpadding="0" cellspacing="0" class="headertable" width="100%">
            <tr>
              <td align="left" valign="top" width="10%">
                <img alt="Workflow" height="21" hspace="5" src="images/wf-logo.gif" vspace="5" width="150"/>
              </td>
              <td align="right">
                <table border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td align="right" class="thnormal">Document Type Name:</td>
                    <td class="datacell1">
                      <xsl:value-of select="$docType"/>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="thnormal">Document Status:</td>
                    <td class="datacell1" width="50">
                      <xsl:value-of select="documentState/workflowDocumentState/status"/>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="thnormal">Create Date:</td>
                    <td class="datacell1">
                      <xsl:comment>[transient start]</xsl:comment>
                      <xsl:value-of select="documentState/workflowDocumentState/createDate"/>
                      <xsl:comment>[transient end]</xsl:comment>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="thnormal">Document ID:</td>
                    <td class="datacell1">
                      <nobr>
                        <xsl:comment>[transient start]</xsl:comment>
                        <xsl:value-of select="$docId"/>
                        <xsl:comment>[transient end]</xsl:comment>
                      </nobr>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </xsl:template>

        <xsl:template name="footer">
          <xsl:if test="documentState/userSession/backdoorUser">
            <center>
              User
              <xsl:choose>
                <xsl:when test="documentState/userSession/loggedInUser/displayName">
                  <xsl:value-of select="documentState/userSession/loggedInUser/displayName"/>
                </xsl:when>
                <xsl:when test="documentState/userSession/loggedInUser/networkId">
                  <xsl:value-of select="documentState/userSession/loggedInUser/networkId"/>
                </xsl:when>
                <xsl:otherwise>
                  ??Unknown user??
                </xsl:otherwise>
              </xsl:choose>
              standing in for user
              <xsl:choose>
                <xsl:when test="documentState/userSession/backdoorUser/backdoorDisplayName">
                  <xsl:value-of select="documentState/userSession/backdoorUser/backdoorDisplayName"/>
                </xsl:when>
                <xsl:when test="documentState/userSession/backdoorUser/backdoorNetworkId">
                  <xsl:value-of select="documentState/userSession/backdoorUser/backdoorNetworkId"/>
                </xsl:when>
                <xsl:otherwise>
                  ??Unknown user??
                </xsl:otherwise>
              </xsl:choose>
            </center>
          </xsl:if>
        </xsl:template>

        <!-- quell the built-in templates for these elements
             an alternative way to do this is to specify specific selects
             on all apply-templates elements to reduce the candidate node set,
             so that the built-in templates are not executed on the non-handled
             nodes -->
        <xsl:template match="//edlContent/data/version"/>
        <xsl:template match="//documentState"/>
        <xsl:template match="//do-header"/>
</xsl:stylesheet>
