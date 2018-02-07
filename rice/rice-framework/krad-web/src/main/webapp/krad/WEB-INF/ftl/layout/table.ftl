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
    Table Layout Manager:

      Works on a collection group to lay out the items as a table.
 -->

<#macro uif_table items manager container>

    <#if manager.styleClassesAsString?has_content>
        <#local styleClass="class=\"${manager.styleClassesAsString}\""/>
    </#if>

    <#if manager.style?has_content>
        <#local style="style=\"${manager.style}\""/>
    </#if>

    <#if manager.separateAddLine>
        <@krad.template component=manager.addLineGroup/>
    </#if>

    <#if manager.allRowFields?? && (manager.allRowFields?size gt 0)>

        <#-- action button for opening and closing all details -->
        <#if manager.showToggleAllDetails>
            <@krad.template component=manager.toggleAllDetailsAction/>
        </#if>

        <table id="${manager.id}" ${style!} ${styleClass!}>

            <#if manager.headerLabels?? && (manager.headerLabels?size gt 0)>
                <thead>
                    <@krad.grid items=manager.headerLabels numberOfColumns=manager.numberOfColumns
                    renderHeaderRow=true renderAlternatingHeaderColumns=false
                    applyDefaultCellWidths=manager.applyDefaultCellWidths/>
                </thead>
            </#if>

            <#if manager.richTable?has_content && manager.richTable.render
                && (manager.richTable.forceLocalJsonData)>

                <#if container.useServerPaging>
                    <#-- empty body because content is being retrieved from the server after render -->
                    <tbody></tbody>
                </#if>

                <#-- iterate over each row (and its items) and convert them to the json array equivalent for later
                retrieval -->
                <#local row=""/>
                <#local colIndex=0/>
                <#local rowIndex=0/>

                <#compress>
                    <#list manager.allRowFields as item>
                        <#-- build custom json data structure using quote placeholders (to be parsed later) -->
                        <#local row>
                            ${row}
                            @quot@c${colIndex}@quot@:{
                                @quot@val@quot@:${manager.richTable.getCellValue(KualiForm, item)},
                                @quot@render@quot@:@quot@<@krad.template component=item/>@quot@
                            },
                        </#local>

                        <#local colIndex=colIndex+1/>
                        <#if colIndex == manager.numberOfColumns>
                            <#-- append row class to data -->
                            <#local row>
                                @quot@DT_RowClass@quot@:@quot@${manager.rowCssClasses[rowIndex]}@quot@,
                                ${row}
                            </#local>

                            <#-- add the row of table data to the internal aaData storage in richTable -->
                            ${manager.richTable.addRowToTableData(row)}

                            <#local row=""/>
                            <#local colIndex=0/>
                            <#local rowIndex=rowIndex+1/>
                        </#if>
                    </#list>
                </#compress>
            <#else>
                <tbody>
                    <@krad.grid items=manager.allRowFields numberOfColumns=manager.numberOfColumns
                    applyAlternatingRowStyles=manager.applyAlternatingRowStyles
                    applyDefaultCellWidths=manager.applyDefaultCellWidths
                    renderAlternatingHeaderColumns=false
                    rowCssClasses=manager.rowCssClasses/>
                </tbody>
            </#if>

            <#if manager.footerCalculationComponents?has_content>
                <tfoot>
                <tr>
                    <#list manager.footerCalculationComponents as component>
                        <th rowspan="1" colspan="1">
                            <#if component??>
                                <@krad.template component=component/>
                            </#if>
                        </th>
                    </#list>
                </tr>
                </tfoot>
            </#if>
        </table>

        <#if (!manager.richTable?has_content || !manager.richTable.render) && manager.pagerWidget?has_content
            && container.useServerPaging>
            <@krad.template component=manager.pagerWidget parent=container/>
        </#if>

        <#-- invoke table tools widget -->
        <@krad.template component=manager.richTable componentId="${manager.id}"/>

    </#if>

</#macro>