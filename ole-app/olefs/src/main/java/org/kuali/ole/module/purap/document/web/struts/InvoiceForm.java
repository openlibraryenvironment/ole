/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.module.purap.document.web.struts;

import org.kuali.ole.module.purap.PurapAuthorizationConstants.InvoiceEditMode;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.InvoiceStatuses;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kns.document.authorization.DocumentAuthorizer;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.kns.web.ui.HeaderField;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Struts Action Form for Invoice document.
 */
public class InvoiceForm extends AccountsPayableFormBase {

    /**
     * Indicates whether tax has been calculated based on the tax area data.
     * Tax calculation is enforced before preq can be routed for tax approval.
     */
    protected boolean calculatedTax;

    /**
     * Constructs a InvoiceForm instance and sets up the appropriately casted document.
     */
    public InvoiceForm() {
        super();
        this.setNewPurchasingItemLine(setupNewPurchasingItemLine());
        //on PO, account distribution should be read only
        setReadOnlyAccountDistributionMethod(true);
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_PRQS";
    }

    public boolean isCalculatedTax() {
        return calculatedTax;
    }

    public void setCalculatedTax(boolean calculatedTax) {
        this.calculatedTax = calculatedTax;
    }

    public InvoiceDocument getInvoiceDocument() {
        return (InvoiceDocument) getDocument();
    }

    public void setInvoiceDocument(InvoiceDocument purchaseOrderDocument) {
        setDocument(purchaseOrderDocument);
    }

    @Override
    public void populateHeaderFields(WorkflowDocument workflowDocument) {
        super.populateHeaderFields(workflowDocument);

        if (ObjectUtils.isNotNull(this.getInvoiceDocument().getPurapDocumentIdentifier())) {
            getDocInfo().add(new HeaderField("DataDictionary.InvoiceDocument.attributes.purapDocumentIdentifier", ((InvoiceDocument) this.getDocument()).getPurapDocumentIdentifier().toString()));
        } else {
            getDocInfo().add(new HeaderField("DataDictionary.InvoiceDocument.attributes.purapDocumentIdentifier", "Not Available"));
        }

        String applicationDocumentStatus = "Not Available";

        if (ObjectUtils.isNotNull(this.getInvoiceDocument().getApplicationDocumentStatus())) {
            applicationDocumentStatus = workflowDocument.getApplicationDocumentStatus();
        }

        getDocInfo().add(new HeaderField("DataDictionary.InvoiceDocument.attributes.applicationDocumentStatus", applicationDocumentStatus));
    }

    /**
     * @see PurchasingFormBase#setupNewPurchasingItemLine()
     */
    @Override
    public PurApItem setupNewPurchasingItemLine() {
        return new OlePurchaseOrderItem();
    }

    /**
     * Build additional Invoice specific buttons and set extraButtons list.
     *
     * @return - list of extra buttons to be displayed to the user
     *         <p/>
     *         KRAD Conversion: Performs customization of an extra button.
     *         <p/>
     *         No data dictionary is involved.
     */
    @Override
    public List<ExtraButton> getExtraButtons() {
        extraButtons.clear(); // clear out the extra buttons array
        InvoiceDocument invoiceDocument = this.getInvoiceDocument();
        String externalImageURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.RICE_EXTERNALIZABLE_IMAGES_URL_KEY);
        String appExternalImageURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY);

        if (canContinue()) {
            addExtraButton("methodToCall.continuePRQS", externalImageURL + "buttonsmall_continue.gif", "Continue");
            addExtraButton("methodToCall.clearInitFields", externalImageURL + "buttonsmall_clear.gif", "Clear");
        } else {
            if (getEditingMode().containsKey(InvoiceEditMode.HOLD)) {
                addExtraButton("methodToCall.addHoldOnPayment", appExternalImageURL + "buttonsmall_hold.gif", "Hold");
            }

            if (getEditingMode().containsKey(InvoiceEditMode.REMOVE_HOLD) && invoiceDocument.isHoldIndicator()) {
                addExtraButton("methodToCall.removeHoldFromPayment", appExternalImageURL + "buttonsmall_removehold.gif", "Remove");
            }

            if (getEditingMode().containsKey(InvoiceEditMode.REQUEST_CANCEL)) {
                addExtraButton("methodToCall.requestCancelOnPayment", appExternalImageURL + "buttonsmall_requestcancel.gif", "Cancel");
            }

            if (getEditingMode().containsKey(InvoiceEditMode.REMOVE_REQUEST_CANCEL) && invoiceDocument.isInvoiceCancelIndicator()) {
                addExtraButton("methodToCall.removeCancelRequestFromPayment", appExternalImageURL + "buttonsmall_remreqcanc.gif", "Remove");
            }

            if (canCalculate()) {
                addExtraButton("methodToCall.calculate", appExternalImageURL + "buttonsmall_calculate.gif", "Calculate");
            }

            if (getEditingMode().containsKey(InvoiceEditMode.ACCOUNTS_PAYABLE_PROCESSOR_CANCEL) ||
                    getEditingMode().containsKey(InvoiceEditMode.ACCOUNTS_PAYABLE_MANAGER_CANCEL)) {
                if (PurapConstants.PurchaseOrderStatuses.APPDOC_CLOSED.equals(invoiceDocument.getPurchaseOrderDocument().getApplicationDocumentStatus())) {
                    //if the PO is CLOSED, show the 'open order' button; but only if there are no pending actions on the PO
                    if (!invoiceDocument.getPurchaseOrderDocument().isPendingActionIndicator()) {
                        addExtraButton("methodToCall.reopenPo", appExternalImageURL + "buttonsmall_openorder.gif", "Reopen PO");
                    }
                } else {
                    if (!invoiceDocument.getFinancialSystemDocumentHeader().getWorkflowDocument().isDisapproved()) {
                        addExtraButton("methodToCall.cancel", externalImageURL + "buttonsmall_cancel.gif", "Cancel");
                    }
                }
            }
        }

        return extraButtons;
    }

    /**
     * Determines whether the current user can continue creating or clear fields of the Invoice in initial status. Conditions:
     * - the Invoice must be in the INITIATE state; and
     * - the user must have the authorization to initiate a Invoice.
     *
     * @return True if the current user can continue creating or clear fields of the initiated Invoice.
     */
    public boolean canContinue() {
        // preq must be in initiated status
        boolean can = InvoiceStatuses.APPDOC_INITIATE.equals(getInvoiceDocument().getApplicationDocumentStatus());

        // check user authorization
        if (can) {
            DocumentAuthorizer documentAuthorizer = SpringContext.getBean(DocumentHelperService.class).getDocumentAuthorizer(getInvoiceDocument());
            can = documentAuthorizer.canInitiate(OLEConstants.FinancialDocumentTypeCodes.INVOICE, GlobalVariables.getUserSession().getPerson());
        }

        return can;
    }

    /**
     * Determine whether the current user can calculate the invoice. Conditions:
     * - Invoice is not FullDocumentEntryCompleted, and
     * - current user has the permission to edit the document.
     *
     * @return True if the current user can calculate the Invoice.
     */
    public boolean canCalculate() {
        // preq must not be FullDocumentEntryCompleted
        boolean can = !SpringContext.getBean(PurapService.class).isFullDocumentEntryCompleted(getInvoiceDocument());

        // check user authorization: whoever can edit can calculate
        can = can && documentActions.containsKey(KRADConstants.KUALI_ACTION_CAN_EDIT);

        //FIXME this is temporary so that calculate will show up at tax
        can = can || editingMode.containsKey(InvoiceEditMode.TAX_AREA_EDITABLE);

        return can;
    }

    /**
     * @see org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase#shouldMethodToCallParameterBeUsed(String, String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean shouldMethodToCallParameterBeUsed(String methodToCallParameterName, String methodToCallParameterValue, HttpServletRequest request) {
        if (KRADConstants.DISPATCH_REQUEST_PARAMETER.equals(methodToCallParameterName) &&
                ("changeUseTaxIndicator".equals(methodToCallParameterValue))) {
            return true;
        }
        return super.shouldMethodToCallParameterBeUsed(methodToCallParameterName, methodToCallParameterValue, request);
    }

}

