<#macro ole_eResource_search items manager container>
    <#if manager.totalLines gt 0>
        <@search_pagesize manager />
    <table id="instanceDetails" class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <th>Title</th>
            <th>EResource Name</th>
            <th>ISBN/ISSN</th>
            <th>Holdings</th>
            <th>URL</th>
            <th>Public Note</th>
            <th>Publisher</th>
            <th>Access Status</th>
            <th>Subscription Status</th>
            <th>Platform</th>
            <th>Remove</th>
        </tr>
        </thead>

        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <td>
                    <#if line.row.instanceTitle??>
                        <#if line.row.instanceFlag == 'true'>
                            <div>
                                <a target="_blank"
                                   href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docFormat=oleml&amp;docType=holdings&amp;docId=${line.row.holdingsId}&amp;instanceId=${line.row.instanceId}&amp;bibId=${line.row.bibId}&amp;editable=false">${line.row.instanceTitle!?html}</a>
                            </div>
                        <#else>
                            <div>
                                <a target="_blank"
                                   href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docFormat=oleml&amp;docType=eHoldings&amp;docId=${line.row.instanceId}&amp;bibId=${line.row.bibId}&amp;editable=false">${line.row.instanceTitle!?html}</a>
                            </div>
                        </#if>
                    </#if>
                </td>

                <td>
                    <div>
                        <a target="_blank"
                           href="oleERSController?viewId=OLEEResourceRecordView&amp;methodToCall=docHandler&amp;docId=${line.row.eResourceDocNum}&amp;command=displayDocSearchView">${line.row.eResourceName!?html}</a>
                    </div>
                </td>
                <td>${line.row.isbn!}</td>
                <td>${line.row.instanceHoldings!}</td>
                <td>${line.row.url!}</td>
                <td>${line.row.publicDisplayNote!}</td>
                <td>${line.row.instancePublisher!}</td>
                 <td>${line.row.status!}</td>
                <td>${line.row.subscriptionStatus!}</td>
                <td>
                    <#if line.row.platformName??>
                        <div>
                            <a target="_blank"
                                href="${line.row.redirectUrl}">${line.row.platformName!?html}</a>
                        </div>
                    </#if>
                </td>
                <td>
                    <div>
                        <a style="text-decoration:underline;" onclick="removeLine(${line.lineNumber});">Remove</a>
                    </div>
                </td>
            </tr>
            </#list>
        </tbody>
        <tfoot>
        <tr>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
        </tfoot>
    </table>
        <@search_pager manager container />
    </#if>

</#macro>


<#macro search_pager manager container>
    <#if manager.pager?? && manager.pager.currentPage gt 0>
    <div class="dataTables_info">
        Showing ${(manager.pager.currentPage - 1) * manager.pageSize + 1}
        to ${(manager.pager.currentPage - 1) * manager.pageSize + manager.displayedLines}
        of ${manager.totalLines} entries
    </div>
    <div style="float : right">
        <@krad.template component=manager.pager parent=container />
    </div>
    </#if>
</#macro>


<#macro search_pagesize manager>
<div class="dataTables_length">
    <label>Show
        <select  id="pageSizeHoldings">
            <#list manager.pageSizeOptions as pso>
                <#if pso.key != '' && manager.pageSize == pso.key?number>
                    <option selected value="${pso.key}">${pso.value}</option>
                <#else>
                    <option value="${pso.key}">${pso.value}</option>
                </#if>
            </#list>
        </select> entries</label>
</div>
</#macro>