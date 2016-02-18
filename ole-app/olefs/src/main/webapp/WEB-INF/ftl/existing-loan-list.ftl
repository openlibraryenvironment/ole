<#macro ole_existing_search items manager container>

    <#if manager.totalLines gt 0>
        <@existing_pagesize manager />
    <table id="existingLoanItemTable" class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <th>Select</th>
            <th>Barcode</a></th>
            <th>Title</th>
            <th>Author</th>
            <th>Location</th>
            <th>Call Number</th>
            <th>Copy Number</th>
            <th>Enumeration</th>
            <th>Chronology</th>
            <th>Proxy Brrower</th>
            <th>No Of Renew</th>
            <th>Item Status</th>
            <th>Due Date</th>
            <th>Claims Return Note</th>
            <th>Claims Return Date</th>
            <th>Missing Piece Note</th>
            <th>Item Damaged Note</th>
            <th>Pending Requests</th>
            <th>Send Notices</th>
        </thead>
        <tbody  onclick="toggleExistingLoanSection()" >
            <#list manager.searchLines as line>
            <tr class="odd">
                <td><@existing_loan_checkbox_control line 'checkNo' container "" /></td>
                <td>
                    <div>
                        <a target="_blank"
                           href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=item&amp;editable=false&amp;docFormat=oleml&amp;docId=${line.row.itemUuid!}&amp;instanceId=${line.row.instanceUuid!}&amp;bibId=${line.row.bibUuid!}">${line.row.itemId!?html}</a>
                    </div>
                </td>
                <td>${line.row.title!}</td>
                <td>${line.row.author!}</td>
                <td>${line.row.location!}</td>
                <td>${line.row.itemCallNumber!}</td>
                <td>${line.row.itemCopyNumber!}</td>
                <td>${line.row.enumeration!}</td>
                <td>${line.row.Chronology!}</td>
                <td>${line.row.realPatronName!}</td>
                <td>${line.row.numberOfRenewals!}</td>
                <td>${line.row.itemStatus!}</td>
                <td><#if line.row.loanDueDate ?? >${line.row.loanDueDate ?string[manager.userInterfaceDateFormat] }<#else>
                    ${line.row.dueDateType!}
                </#if>
                </td>
                <td>${line.row.claimsReturnNote!}</td>
                <td>${line.row.claimsReturnedDate!}</td>
                <td>${line.row.missingPieceNote!}</td>
                <td>${line.row.itemDamagedNote!}</td>
                <td>
                    <div>
                        <a target="_blank"
                           href="lookup?methodToCall=search&amp;itemId=${line.row.itemId!}&amp;flag=true&amp;dataObjectClassName=org.kuali.ole.deliver.bo.OleDeliverRequestBo">${line.row.requestFlag!?html}</a>
                    </div>
                </td>
                <td>
                    <div>
                        <a target="_blank"
                           href=${line.row.sentNoticesUrl}>View Sent Notices in a New Window</a>
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
        <@existing_pagenumber manager container />
    </#if>
</#macro>

<#macro existing_loan_checkbox_control line prop container label>
    <@existing_loan_control_wrapper "${line.lineId}_${prop}" container label>
        <@spring.formCheckbox id="${line.lineId}_${prop}_control" label=""
        attributes='class="uif-checkboxControl loanedItemCBClass" data-role="Control" data-control_for="${line.lineId}_${prop}"'
        path="KualiForm.${line.bindPath}.${prop}" />
    </@existing_loan_control_wrapper>
</#macro>


<#macro existing_loan_control_wrapper id container label>
<div id="${id}" class="uif-inputField" data-parent="${container.id}" data-role="InputField" data-label="${label}">
    <#nested/>
    <span id="${id}_markers"></span>

    <div id="${id}_errors" class="uif-validationMessages" style="display: none;" data-messages_for="${id}"></div>
    <span id="${id}_info_message"></span>
</div>
</#macro>



<#macro existing_pagesize manager>
<div class="dataTables_length" style="float:left; width:400px;position: relative">
    <label>Show
        <select id="existingLoanItemTable_length">
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

<#macro existing_pagenumber manager container>
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





