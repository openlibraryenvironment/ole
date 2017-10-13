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
    Template for providing JSON table data via ajax to the DataTables plugin.
-->

<#if KualiForm.updateComponentId??>

    <#assign tableId=KualiForm.updateComponentId/>
    <#assign manager=DataTablesPagingHelper.tableLayoutManager/>
    <#assign filteredCollectionSize=DataTablesPagingHelper.filteredCollectionSize/>
    <#assign totalCollectionSize=DataTablesPagingHelper.totalCollectionSize/>

    <#-- Make the custom directive for escaping quotes available in our template -->
    <#assign jsonEscape = "org.kuali.rice.krad.uif.freemarker.JsonStringEscapeDirective"?new()>

    <#-- define a macro to allow the use of locals -->
    <#macro uif_table_json manager filteredCollectionSize totalCollectionSize>
        <#compress>
            <#local allRowFields=manager.allRowFields/>
{
    <#-- see http://datatables.net/usage/server-side for documentation on these top-level properties -->
    "sEcho"  :  "${request.getParameter("sEcho")}",
    "iTotalDisplayRecords" : ${filteredCollectionSize},
    "iTotalRecords" : ${totalCollectionSize},
    "aaData" :  [
            <#-- iterate over each item (we'll use math to determine row boundaries) and convert them -->
            <#local row=""/>
            <#local rowCount=0/>
            <#local colIndex=0/>
            <#local firstIteration=true/>

            <#list allRowFields as item>

                <#-- take care of commas between rows -->
                <#if colIndex == 0 && !firstIteration>
        ,
                </#if>

                <#-- set the flag that is just used to avoid having a comma before the first row -->
                <#if firstIteration>
                    <#local firstIteration=false/>
                </#if>

                <#-- add open brace, row classes for row beginning -->
                <#if colIndex == 0>
        {

            <#-- add metadata used to set custom classes on table rows -->
            "DT_RowClass" : "${manager.rowCssClasses[rowCount]}",
                </#if>

            "c${colIndex}": {

                <#-- value used for client side sorting.  Do we need this? -->
                "val":${manager.richTable.getCellValue(KualiForm, item)?replace("@quot@","\"")},

                <#-- visible content -->
                "render": "<@jsonEscape><@krad.template component=item/></@jsonEscape>"
            }

                <#local colIndex=colIndex+1/>

                <#-- when we've finished the row, reset the column index to 0 -->
                <#if colIndex == manager.numberOfColumns>
                    <#local colIndex=0/>
                    <#local rowCount=rowCount+1/>
        }
                <#else>
                <#-- otherwise, we need a comma between columns -->
            ,
                </#if>
            </#list>
    ]
}
        </#compress>
    </#macro>

    <#-- call our macro to render the JSON data -->
    <@uif_table_json manager filteredCollectionSize totalCollectionSize/>

<#else>
{ "aaData" :  [] }
</#if>