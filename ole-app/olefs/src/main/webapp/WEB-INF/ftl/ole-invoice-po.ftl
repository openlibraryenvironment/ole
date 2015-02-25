<#macro ole_current_items items manager container>

<div>
    <table class="table table-condensed table-bordered uif-tableCollectionLayout">
        <thead>
        <tr>
            <th class="sorting_disabled"></th>
            <th><span class="infoline"><label>Item Line #</label></span></th>
            <th><span class="infoline"><label>Check Subscription Date Overlap</label></span></th>
            <th><span class="infoline"><label>Subscription From Date</label></span></th>
            <th><span class="infoline"><label>To Date</label></span></th>
            <th><span class="infoline"><label>Open Quantity</label></span></th>
            <th><span class="infoline"><label>Title</label></span></th>
            <th><span class="infoline"><label>Donor</label></span></th>
            <th><span class="infoline"><label>Copies Ordered</label></span></th>
            <th><span class="infoline"><label>Parts Ordered</label></span></th>
            <th><span class="infoline"><label>Invoiced Copies</label></span></th>
            <th><span class="infoline"><label>Invoiced Parts</label></span></th>
            <th><span class="infoline"><label>Invoiced Price</label></span></th>
			<#if !manager.foreignCurrency>
                <th><span class="infoline"><label>Discount</label></span></th>
                <th><span class="infoline"><label>Discount type</label></span></th>
			</#if>
            <th><span class="infoline"><label>Unit Cost</label></span></th>
            <th><span class="infoline"><label>Extended Cost</label></span></th>
			<#if manager.foreignCurrency>
                <th><span class="infoline"><label>Invoiced Foreign Price</label></span></th>
                <th><span class="infoline"><label>Foreign Discount</label></span></th>
                <th><span class="infoline"><label>Discount type</label></span></th>
                <th><span class="infoline"><label>Currency Type</label></span></th>
                <th><span class="infoline"><label>Exchange Rate</label></span></th>
                <th><span class="infoline"><label>Foreign Unit Cost</label></span></th>
			</#if>
            <th><span class="infoline"><label>Select Item For Invoice</label></span></th>
        </tr>
        </thead>
        <tbody>
			<#list manager.currentItemLines as line>
            <tr class="odd">
                <td><a onclick="oleInvoiceRowDetails('${line.lineId}','${container.id}',${(!line.rowDetails??)?string('true','false')})">Details</a></td>
                <td>${line.poItem.itemLineNumber}</td>
                <td><#if container.readOnly>${line.poItem.subscriptionOverlap!}<#else>
					<@checkbox_control line 'subscriptionOverlap' container "Check Subscription Date Overlap"/></#if></td>
                <td><#if container.readOnly>${line.poItem.subscriptionFromDate!}<#else>
					<@date_control line 'subscriptionFromDate' container "Subscription From Date"/></#if></td>
                <td><#if container.readOnly>${line.poItem.subscriptionToDate!}<#else>
					<@date_control line 'subscriptionToDate' container "To Date"/></#if></td>
                <td>${line.poItem.openQuantity!}</td>
                <td>
					<#if line.poItem.itemTitleId?? && line.poItem.docFormat??>
                        <a href="editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=${line.poItem.docFormat}&docId=${line.poItem.itemTitleId}&editable=true">${line.poItem.title}</a>
					<#else>
					${line.poItem.title!}
					</#if>
                </td>
                <td>
					<#if line.poItem.donorCode?? && line.poItem.donorId??>
                        <a style="word-break: break-all;word-wrap: break-word" target="_blank" href="inquiry?methodToCall=start&amp;donorId=${line.poItem.donorId}&amp;dataObjectClassName=org.kuali.ole.select.bo.OLEDonor">${line.poItem.donorCode}</a>
					</#if>
                </td>
                <td>${line.poItem.copiesOrdered!}</td>
                <td>${line.poItem.noOfItemParts!}</td>
                <td><#if container.readOnly>${line.poItem.copiesInvoicedNumber!}<#else>
					<@text_control line 'copiesInvoicedNumber' container "Invoiced Copies" false "5" "25" />
				</#if></td>
                <td><#if container.readOnly>${line.poItem.partsInvoicedNumber!}<#else>
					<@text_control line 'partsInvoicedNumber' container "Invoiced Parts" false "5" "25" />
				</#if></td>
                <td><#if container.readOnly || manager.foreignCurrency>${line.poItem.invoiceItemListPrice!}<#else>
					<@text_control line 'invoiceItemListPrice' container "Invoiced Price" false "5" "25" />
				</#if></td>
				<#if !manager.foreignCurrency>
                    <td><#if container.readOnly>${line.poItem.itemDiscount!}<#else>
						<@text_control line 'itemDiscount' container "Discount" false "5" "25" />
					</#if></td>
                    <td><#if container.readOnly>${line.poItem.itemDiscountType!"%"}<#else>
						<@select_control line 'itemDiscountType' container "Discount Type" manager.discountKeyValues />
					</#if></td>
				</#if>
                <td>${line.poItem.itemUnitPrice!}</td>
                <td>${line.poItem.extendedPrice!?string.currency}</td>
				<#if manager.foreignCurrency>
                    <td><#if container.readOnly>${line.poItem.invoiceForeignItemListPrice!}<#else>
						<@text_control line 'invoiceForeignItemListPrice' container "Invoiced Foreign Price" false "5" "25" />
					</#if></td>
                    <td><#if container.readOnly>${line.poItem.invoiceForeignDiscount!}<#else>
						<@text_control line 'invoiceForeignDiscount' container "Foreign Discount" false "5" "25" />
					</#if></td>
                    <td><#if container.readOnly>${line.poItem.invoiceForeignDiscountType!"%"}<#else>
						<@select_control line 'invoiceForeignDiscountType' container "Discount type" manager.discountKeyValues />
					</#if></td>
                    <td>${line.poItem.invoiceForeignCurrency!}</td>
                    <td>${line.poItem.invoiceExchangeRate!}</td>
                    <td>${line.poItem.invoiceForeignUnitCost!}</td>
				</#if>
                <td><#if container.readOnly>${line.poItem.itemForInvoice!}<#else>
					<@checkbox_control line 'itemForInvoice' container "Select Item For Invoice" /></#if></td>
            </tr>
				<#if line.rowDetails??>
                <tr>
                    <td colspan="${manager.foreignCurrency?string('22','18')}" class="uif-rowDetails">
						<@krad.template line.rowDetails />
                    </td>
                </tr>
				</#if>
			</#list>
        </tbody>
        <tfoot><tr><th colspan="${manager.foreignCurrency?string('22','18')}"></th></tr></tfoot>
    </table>
</div>

</#macro>

<#macro ole_accounting_items items manager container>

<div>
    <table class="table table-condensed table-bordered uif-tableCollectionLayout">
        <thead>
        <tr>
            <th><span class="infoline"><label>Chart</label></span></th>
            <th><span class="infoline"><label>* Account Number</label></span></th>
            <th><span class="infoline"><label>Sub Account Number</label></span></th>
            <th><span class="infoline"><label>* Object Code</label></span></th>
            <th><span class="infoline"><label>Sub Object Code</label></span></th>
            <th><span class="infoline"><label>Project</label></span></th>
            <th><span class="infoline"><label>Org Ref Id</label></span></th>
            <th><span class="infoline"><label>Dollar</label></span></th>
            <th><span class="infoline"><label>* Percentage</label></span></th>
			<#if !container.readOnly>
                <th><span class="infoline"><label>Actions</label></span></th>
			</#if>
        </tr>
        </thead>
        <tbody>
			<#list manager.accountingLines as line><tr>
                <td><#if container.readOnly>${line.chartOfAccountsCode!}<#else>
					<@select_control line 'chartOfAccountsCode' container "Chart" manager.chartKeyValues />
				</#if></td>
                <td><#if container.readOnly>${line.accountNumber!}<#else>
					<@text_control line 'accountNumber' container "Account Number" !line.lineId?contains("_add_") />
					<@krad.template component=line.accountNumberQuickfinder componentId="${line.lineId}_accountNumber" />
				</#if></td>
                <td><#if container.readOnly>${line.subAccountNumber!}<#else>
					<@text_control line 'subAccountNumber' container "Sub Account Number" />
					<@krad.template component=line.subAccountNumberQuickfinder componentId="${line.lineId}_subAccountNumber" />
				</#if></td>
                <td><#if container.readOnly>${line.financialObjectCode!}<#else>
					<@text_control line 'financialObjectCode' container "Object Code" !line.lineId?contains("_add_") />
					<@krad.template component=line.financialObjectCodeQuickfinder componentId="${line.lineId}_financialObjectCode" />
				</#if></td>
                <td><#if container.readOnly>${line.financialSubObjectCode!}<#else>
					<@text_control line 'financialSubObjectCode' container "Sub Object Code" />
					<@krad.template component=line.financialSubObjectCodeQuickfinder componentId="${line.lineId}_financialSubObjectCode" />
				</#if></td>
                <td><#if container.readOnly>${line.projectCode!}<#else>
					<@text_control line 'projectCode' container "Project" />
					<@krad.template component=line.projectCodeQuickfinder componentId="${line.lineId}_projectCode" />
				</#if></td>
                <td><#if container.readOnly>${line.organizationReferenceId!}<#else>
					<@text_control line 'organizationReferenceId' container "Org Ref Id" />
				</#if></td>
                <td><#if container.readOnly>${line.amount!}<#else>
					<@text_control line 'amount' container "Dollar" />
				</#if></td>
                <td><#if container.readOnly>${line.accountLinePercent!}<#else>
					<@text_control line 'accountLinePercent' container "Percentage" !line.lineId?contains("_add_") />
				</#if></td>
				<#if !container.readOnly><td>
					<#list line.actions as action>
							<@krad.template action />
						</#list>
                </td></#if>
            </tr></#list>
        </tbody>
        <tfoot><tr><th colspan="${container.readOnly?string("9","10")}"></th></tr></tfoot>
    </table>
</div>

</#macro>

<#macro ole_invoice_items items manager container>

<div>
    <table class="table table-condensed table-bordered uif-tableCollectionLayout">
        <thead>
        <tr>
            <th></th>
            <th></th>
            <th><span class="infoline"><label>PO #</label></span></th>
            <th><span class="infoline"><label>Open Qty</label></span></th>
            <th><span class="infoline"><label>Title</label></span></th>
            <th><span class="infoline"><label>Donor</label></span></th>
            <th><span class="infoline"><label>PO Unt/Ext Price</label></span></th>
            <th><span class="infoline"><label>Copies Invoiced</label></span></th>
            <th><span class="infoline"><label>Invoiced Price</label></span></th>
			<#if !manager.foreignCurrency>
                <th><span class="infoline"><label>Discount</label></span></th>
                <th><span class="infoline"><label>Discount type</label></span></th>
			</#if>
            <th><span class="infoline"><label>Unit Cost</label></span></th>
            <th><span class="infoline"><label>Extended Cost</label></span></th>
			<#if manager.foreignCurrency>
                <th><span class="infoline"><label>Invoiced Foreign Price</label></span></th>
                <th><span class="infoline"><label>Discount</label></span></th>
                <th><span class="infoline"><label>Discount type</label></span></th>
                <th><span class="infoline"><label>Currency Type</label></span></th>
                <th><span class="infoline"><label>Exchange Rate</label></span></th>
                <th><span class="infoline"><label>Foreign Unit Cost</label></span></th>
			</#if>
            <th><span class="infoline"><label>Total Cost</label></span></th>
            <th><span class="infoline"><label>Action</label></span></th>
        </tr>
        </thead>
        <tbody>
			<#list manager.itemLines as line>
            <tr class="odd">
                <td>${line.lineNumber + 1}</td>
                <td><a onclick="oleInvoiceRowDetails('${line.lineId}','${container.id}',${(!line.rowDetails??)?string('true','false')})">Details</a></td>
                <td><#if line.item.tempPurchaseOrderIdentifier?? && line.item.poItemLink??>
                    <a href="${line.item.poItemLink}" target="_blank">${line.item.tempPurchaseOrderIdentifier}</a>
				</#if></td>
                <td>${line.item.oleOpenQuantity!}</td>
                <td>
					<#if line.item.itemTitleId?? && line.item.docFormat?? && line.item.title??>
                        <a href="editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=${line.item.docFormat}&docId=${line.item.itemTitleId}&editable=true">${line.item.itemTitle}</a>
					<#else>
					${line.item.itemTitle!}
					</#if>
                </td>
                <td>
					<#if line.item.donorCode?? && line.item.donorId??>
                        <a style="word-break: break-all;word-wrap: break-word" target="_blank" href="inquiry?methodToCall=start&amp;donorId=${line.item.donorId}&amp;dataObjectClassName=org.kuali.ole.select.bo.OLEDonor">${line.item.donorCode}</a>
					</#if>
                </td>
                <td>${line.item.purchaseOrderItemUnitPrice!}</td>
                <td>
					<#if container.readOnly || manager.foreignCurrency>
								${line.item.oleCopiesOrdered!}
							<#else>
						<@text_control line 'oleCopiesOrdered' container "Copies Invoiced" false "5" "30" />
					</#if>
                </td>
                <td>
					<#if container.readOnly || manager.foreignCurrency>
								${line.item.listPrice!}
							<#else>
						<@text_control line 'listPrice' container "Invoiced Price" false "5" "30" "onChangePriceScript();" />
					</#if>
                </td>
				<#if !manager.foreignCurrency>
                    <td><#if container.readOnly>${line.item.itemDiscount!}<#else>
						<@text_control line 'itemDiscount' container "Discount" false "5" "25" "onChangePriceScript();" />
					</#if></td>
                    <td><#if container.readOnly>${line.item.itemDiscountType!"%"}<#else>
						<@select_control line 'itemDiscountType' container "Discount Type" manager.discountKeyValues />
					</#if></td>
				</#if>
                <td>${line.item.itemUnitPrice!}</td>
                <td>${line.item.extendedPrice!?string.currency}</td>
				<#if manager.foreignCurrency>
                    <td><#if container.readOnly>${line.item.foreignListPrice!}<#else>
						<@text_control line 'foreignListPrice' container "Invoiced Foreign Price" false "10" "25" "onChangePriceScript();" />
					</#if></td>
                    <td><#if container.readOnly>${line.item.foreignDiscount!}<#else>
						<@text_control line 'foreignDiscount' container "Discount" false "10" "25" "onChangePriceScript();" />
					</#if></td>
                    <td><#if container.readOnly>${line.item.itemForeignDiscountType!}<#else>
						<@select_control line 'itemForeignDiscountType' container "Discount Type" manager.discountKeyValues />
					</#if></td>
                    <td>${line.item.invoicedCurrency!}</td>
                    <td>${line.item.exchangeRate!}</td>
                    <td>${line.item.foreignUnitCost!}</td>
				</#if>
                <td>${line.item.totalAmount!?string.currency}</td>
				<#if !container.readOnly><td>
					<#list line.actions as action>
						<@krad.template action />
					</#list>
					<#if line.relinkQuickfinder??>
                        <label for="${line.lineId}_reLinkPO">Relink:</label>
						<@text_control line 'reLinkPO' container "Relink" />
						<@krad.template component=line.relinkQuickfinder componentId="${line.lineId}_reLinkPO" />
                        +							</#if>
                </td></#if>
            </tr>
				<#if line.rowDetails??>
                <tr>
                    <td colspan="${manager.foreignCurrency?string('21','17')}" class="uif-rowDetails">
						<@krad.template line.rowDetails />
                    </td>
                </tr>
				</#if>
			</#list>
        </tbody>
        <tfoot><tr><th colspan="${manager.foreignCurrency?string('21','17')}"></th></tr></tfoot>
    </table>
	<#if manager.pager.currentPage gt 0>
        <div>
            Showing ${(manager.pager.currentPage - 1) * manager.pageSize + 1}
            to ${(manager.pager.currentPage - 1) * manager.pageSize + manager.displayedLines}
            of ${manager.totalLines} entries
            (filtered from ${manager.totalLines + manager.filteredLines} total entries)
        </div>
        <div style="float : right">
			<@krad.template component=manager.pager parent=container />
        </div>
	</#if>
</div>

</#macro>

<#macro control_wrapper id container label>
<div id="${id}" class="uif-inputField" data-parent="${container.id}" data-role="InputField" data-label="${label}">
	<#nested/>
    <span id="${id}_markers"></span>
    <div id="${id}_errors" class="uif-validationMessages" style="display: none;" data-messages_for="${id}"></div>
    <span id="${id}_info_message"></span>
</div>
</#macro>

<#macro checkbox_control line prop container label>
	<@control_wrapper "${line.lineId}_${prop}" container label>
		<@spring.formCheckbox id="${line.lineId}_${prop}_control" label=""
		attributes='class="uif-checkboxControl" data-role="Control" data-control_for="${line.lineId}_${prop}"'
		path="KualiForm.${line.bindPath}.${prop}" />
	</@control_wrapper>
</#macro>

<#macro date_control line prop container label>
	<@control_wrapper "${line.lineId}_${prop}" container label>
		<@spring.formInput id="${line.lineId}_${prop}_control"
		attributes='maxlength="22" size="10" class="uif-dateControl" data-role="Control" data-control_for="${line.lineId}_${prop}"'
		path="KualiForm.${line.bindPath}.${prop}" />
		<@krad.script value="createDatePicker('${line.lineId}_${prop}_control', {showAnim:'slideDown',buttonImageOnly:true,buttonImage:'${request.contextPath}/themes/kboot/images/cal.gif',changeYear:true,showOn:'button',changeMonth:true,disabled:false,showButtonPanel:true});" />
	</@control_wrapper>
</#macro>

<#macro select_control line prop container label options>
	<@control_wrapper "${line.lineId}_${prop}" container label>
		<@spring.formSingleSelect id="${line.lineId}_${prop}_control"
		attributes='class="uif-dropdownControl fixed-size-50-select" data-role="Control" data-control_for="${line.lineId}_${prop}"'
		path="KualiForm.${line.bindPath}.${prop}" options=options />
	</@control_wrapper>
</#macro>

<#macro text_control line prop container label required=false size="7" maxlength="10" onchange="">
	<@control_wrapper "${line.lineId}_${prop}" container label>
		<@spring.formInput id="${line.lineId}_${prop}_control"
		attributes='maxlength="${maxlength}" size="${size}" class="uif-textControl${required?string(" required","")}" data-role="Control" data-control_for="${line.lineId}_${prop}" onchange="${onchange}"'
		path="KualiForm.${line.bindPath}.${prop}" />
	</@control_wrapper>
</#macro>
