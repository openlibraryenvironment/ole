<#macro proxy_patron_search items manager container>

    <#if manager.totalLines gt 0>
        <@proxy_pagesize manager />
    <table class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <th>Choose Proxy For Patron</th>
            <th>Proxied Patron Id</a></th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Barcode</th>
            <th>Proxied Patron Type</th>
        </tr>
        </thead>
        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <td><@proxy_checkbox_control line 'olePatronDocument.checkoutForSelf' container "" /></td>
                <td>${line.row.olePatronDocument.olePatronId!}</td>
                <td>${line.row.olePatronDocument.entity.names[0].firstName!}</td>
                <td>${line.row.olePatronDocument.entity.names[0].lastName!}</td>
                <td id="${line.lineId}_proxyBarcode_control">${line.row.olePatronDocument.barcode!}</td>
                <td>${line.row.olePatronDocument.oleBorrowerType.borrowerTypeName!}</td>
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
        </tr>
        </tfoot>
    </table>
        <@proxy_pagenumber manager container />
    </#if>
</#macro>

<#macro proxy_checkbox_control line prop container label>
    <@proxy_control_wrapper "${line.lineId}_${prop}" container label>
        <@spring.formCheckbox id="${line.lineId}_control" label=""
        attributes='class="uif-checkboxControl patronCheckBoxListClass" data-role="Control" data-control_for="${line.lineId}_${prop}"'
        path="KualiForm.${line.bindPath}.${prop}" />
    </@proxy_control_wrapper>
</#macro>


<#macro proxy_control_wrapper id container label>
<div id="${id}" class="uif-inputField" data-parent="${container.id}" data-role="InputField" data-label="${label}">
    <#nested/>
    <span id="${id}_markers"></span>

    <div id="${id}_errors" class="uif-validationMessages" style="display: none;" data-messages_for="${id}"></div>
    <span id="${id}_info_message"></span>
</div>
</#macro>



<#macro proxy_pagesize manager>
<div   class="dataTables_length">
    <label>Show
        <select id="pageSizeProxy">
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

<#macro proxy_pagenumber manager container>
    <#if manager.pager.currentPage gt 0>
    <div>
        Showing ${(manager.pager.currentPage - 1) * manager.pageSize + 1}
        to ${(manager.pager.currentPage - 1) * manager.pageSize + manager.displayedLines}
        of ${manager.totalLines}
    </div>
    <div style="float : right">
        <@krad.template component=manager.pager parent=container />
    </div>
    </#if>
</#macro>





