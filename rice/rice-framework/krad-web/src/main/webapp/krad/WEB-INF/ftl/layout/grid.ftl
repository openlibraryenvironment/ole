<#--

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
<#--
    Grid Layout Manager:

      Places each component of the managers field list into a table cell. A new row is created after the
      configured number of columns is rendered.

      The number of horizontal places a field takes up in the grid is determined by the configured colSpan.
      Likewise the number of vertical places a field takes up is determined by the configured rowSpan.

      If the width for the column is not given by the field, it will be calculated by equally dividing the
      space by the number of columns.

      The majority of logic is implemented in grid.tag
 -->

<#macro uif_grid items manager container>

    <#if manager.styleClassesAsString?has_content>
        <#local styleClass="class=\"${manager.styleClassesAsString}\""/>
    </#if>

    <#if manager.style?has_content>
        <#local style="style=\"${manager.style}\""/>
    </#if>

    <table id="${manager.id}" ${style!} ${styleClass!}>

        <@krad.grid items=items numberOfColumns=manager.numberOfColumns
                   applyAlternatingRowStyles=manager.applyAlternatingRowStyles
                   applyDefaultCellWidths=manager.applyDefaultCellWidths
                   renderFirstRowHeader=manager.renderFirstRowHeader
                   renderRowFirstCellHeader=manager.renderRowFirstCellHeader
                   renderAlternatingHeaderColumns=manager.renderAlternatingHeaderColumns
                   rowCssClasses=manager.rowCssClasses/>

    </table>

</#macro>