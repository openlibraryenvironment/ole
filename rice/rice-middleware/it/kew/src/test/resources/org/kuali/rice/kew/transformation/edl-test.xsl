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
 
 <!--
     <xsl:call-template name="widget_render">
      <xsl:with-param name="fieldName" select="department" />
      <xsl:with-param name="renderCmd" select="title | all | input"  />
      <xsl:with-param name="align" select="vertical | horizontal"  />
     </xsl:call-template>
 -->   

<xsl:template match="/">
 
  <table>
   <tr>
    <td>
     <xsl:call-template name="widget_render">
      <xsl:with-param name="fieldName" select="'department'" />
      <xsl:with-param name="renderCmd" select="'title'"  />
     </xsl:call-template>
    </td>
    <td>
     <xsl:call-template name="widget_render">
      <xsl:with-param name="fieldName" select="'department'" />
      <xsl:with-param name="renderCmd" select="'input'"  />
     </xsl:call-template>
    </td>
   </tr>
   <tr>
    <td>
     <xsl:call-template name="widget_render">
      <xsl:with-param name="fieldName" select="'courseJustification'" />
      <xsl:with-param name="renderCmd" select="'title'"  />
     </xsl:call-template>
    </td>
    <td>
     <xsl:call-template name="widget_render">
      <xsl:with-param name="fieldName" select="'courseJustification'" />
      <xsl:with-param name="renderCmd" select="'input'"  />
     </xsl:call-template>
    </td>
   </tr>
   <tr>
    <td>
     <xsl:call-template name="widget_render">
      <xsl:with-param name="fieldName" select="'button-test'" />
      <xsl:with-param name="renderCmd" select="'title'"  />
     </xsl:call-template>
    </td>
    <td>
     <xsl:call-template name="widget_render">
      <xsl:with-param name="fieldName" select="'button-test'" />
      <xsl:with-param name="renderCmd" select="'input'"  />
     </xsl:call-template>
    </td>
   </tr>
   <tr>
   	<td>
  	 <xsl:call-template name="widget_render">
       <xsl:with-param name="fieldName" select="'submit-test'" />
       <xsl:with-param name="renderCmd" select="'all'"  />
       <xsl:with-param name="align" select="'vertical'"/>
     </xsl:call-template>
    </td>
   </tr>
   <tr>
   	<td>
   		<xsl:call-template name="widget_render">
       		<xsl:with-param name="fieldName" select="'hiddenfield'" />
     	</xsl:call-template>
   	</td>
   	
   </tr>
  </table>
  
  		<xsl:call-template name="widget_render">
			<xsl:with-param name="fieldName" select="'courseNumber'"/>
			<xsl:with-param name="renderCmd" select="'input'"  /> 
			<xsl:with-param name="align" select="'horizontal'"  /> 
		</xsl:call-template>
		<br/>
		<xsl:call-template name="widget_render">
			<xsl:with-param name="fieldName" select="'password1'"/>
			<xsl:with-param name="renderCmd" select="'all'"  /> 
			<xsl:with-param name="align" select="'horizontal'"  /> 
		</xsl:call-template>
		<br/>
		<xsl:call-template name="widget_render">
			<xsl:with-param name="fieldName" select="'creditType'"/>
			<xsl:with-param name="renderCmd" select="'input'"  /> 
			<xsl:with-param name="align" select="'vertical'"  />  
		</xsl:call-template> 
		<br/>
		<xsl:call-template name="widget_render">
			<xsl:with-param name="fieldName" select="'calculation'"/>
			<xsl:with-param name="renderCmd" select="'input'"  />  
			<xsl:with-param name="align" select="'vertical'"  />  
		</xsl:call-template>

</xsl:template>
 <xsl:include href="widgets.xsl"/>
</xsl:stylesheet>
 
