<#macro ole_deliver_bib_search items manager container>

    <#if manager.totalLines gt 0>
        <@deliver_search_pagesize manager />
    <table class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
        <th></th>
        <th>Bib ID</a></th>
        <th><a style="text-decoration:underline;" onclick="sortBy('title');">Title</a></th>
        <th><a style="text-decoration:underline;" onclick="sortBy('author');">Author</a></th>
        <th><a style="text-decoration:underline;" onclick="sortBy('pubYear');">Pub Year</a></th>
        <th>Holdings Location</a></th>
        <th><a style="text-decoration:underline;" onclick="sortBy('ItemLocation');">Item Location</th>
        <th><a style="text-decoration:underline;" onclick="sortBy('callNumber');">Call Number</th>
        <th><a style="text-decoration:underline;" onclick="sortBy('status');">Status</th>
        <th>Item Type</th>
        </thead>
        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <td><a onclick="oleItemRowDetails('${line.lineId}','${container.id}',${(!line.rowDetails??)?string('true','false')})">Details</a></td>
                <td>
                    <div>
                        <a target="_blank"
                           href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=bibliographic&amp;docFormat=marc&amp;docId=${line.deliverRow.id!}&amp;editable=true">${line.deliverRow.id!}</a>
                    </div>
                </td>
                <td>${line.deliverRow.title!}</td>
                <td>${line.deliverRow.author!}</td>
                <td>${line.deliverRow.publicationYear!}</td>
                <td>${line.deliverRow.holdingsLocation!}</td>
                <td>${line.deliverRow.itemLocation!}</td>
                <td>${line.deliverRow.itemCallNumber!}</td>
                <td>${line.deliverRow.itemStatus!}</td>
                <td>${line.deliverRow.itemType!}</td>
            </tr>
                <#if line.rowDetails??>
                <tr>
                    <td  colspan="10">
                        <@krad.template line.rowDetails />
                    </td>
                </tr>
                </#if>
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
        </tr>
        </tfoot>
    </table>
        <@deliver_search_pager manager container />
    </#if>
</#macro>


<#macro deliver_search_pagesize manager>
<div class="dataTables_length">
    <label>Show
        <select onchange="jQuery('#hiddenSearchFieldsItem_h0').val(jQuery(this).val)">
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


<#macro deliver_search_pager manager container>
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