/*
 * Copyright 2006 The Kuali Foundation
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

import org.kuali.ole.integration.purap.CapitalAssetLocation;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.RequisitionStatuses;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.kns.web.ui.HeaderField;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Struts Action Form for Requisition document.
 */
public class RequisitionForm extends PurchasingFormBase {

    protected String shopUrl;

    /**
     * Constructs a RequisitionForm instance and sets up the appropriately casted document.
     */
    public RequisitionForm() {
        super();
    }

    /**
     * Gets the documentType attribute.
     *
     * @return Returns the documentType
     */

    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_REQS";
    }

    public RequisitionDocument getRequisitionDocument() {
        return (RequisitionDocument) getDocument();
    }

    public void setRequisitionDocument(RequisitionDocument requisitionDocument) {
        setDocument(requisitionDocument);
    }

    /**
     * KRAD Conversion: Performs customization of header fields.
     * <p/>
     * Use of data dictionary for bo RequisitionDocument.
     */
    @Override
    public void populateHeaderFields(WorkflowDocument workflowDocument) {
        super.populateHeaderFields(workflowDocument);

        if (ObjectUtils.isNotNull(this.getRequisitionDocument().getPurapDocumentIdentifier())) {
            getDocInfo().add(new HeaderField("DataDictionary.RequisitionDocument.attributes.purapDocumentIdentifier", ((RequisitionDocument) this.getDocument()).getPurapDocumentIdentifier().toString()));
        } else {
            getDocInfo().add(new HeaderField("DataDictionary.RequisitionDocument.attributes.purapDocumentIdentifier", PurapConstants.PURAP_APPLICATION_DOCUMENT_ID_NOT_AVAILABLE));
        }

        String applicationDocumentStatus = PurapConstants.PURAP_APPLICATION_DOCUMENT_STATUS_NOT_AVAILABLE;

        if (ObjectUtils.isNotNull(this.getRequisitionDocument().getApplicationDocumentStatus())) {
            applicationDocumentStatus = workflowDocument.getApplicationDocumentStatus();
        }

        getDocInfo().add(new HeaderField("DataDictionary.RequisitionDocument.attributes.applicationDocumentStatus", applicationDocumentStatus));
    }

    /**
     * @see org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase#shouldMethodToCallParameterBeUsed(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean shouldMethodToCallParameterBeUsed(String methodToCallParameterName, String methodToCallParameterValue, HttpServletRequest request) {
        if (KRADConstants.DISPATCH_REQUEST_PARAMETER.equals(methodToCallParameterName) &&
                ("displayB2BRequisition".equals(methodToCallParameterValue))) {
            return true;
        }
        return super.shouldMethodToCallParameterBeUsed(methodToCallParameterName, methodToCallParameterValue, request);
    }

    @Override
    public Class getCapitalAssetLocationClass() {
        return RequisitionCapitalAssetLocation.class;
    }

    @Override
    public Class getItemCapitalAssetClass() {
        return RequisitionItemCapitalAsset.class;
    }

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.PurchasingFormBase#setupNewPurchasingItemLine()
     */
    @Override
    public PurApItem setupNewPurchasingItemLine() {
        return new RequisitionItem();
    }

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.PurchasingFormBase#setupNewPurchasingAccountingLine()
     */
    @Override
    public RequisitionAccount setupNewPurchasingAccountingLine() {
        return new RequisitionAccount();
    }

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.PurchasingFormBase#setupNewAccountDistributionAccountingLine()
     */
    @Override
    public RequisitionAccount setupNewAccountDistributionAccountingLine() {
        RequisitionAccount account = setupNewPurchasingAccountingLine();
        account.setAccountLinePercent(new BigDecimal(100));
        return account;
    }

    @Override
    public CapitalAssetLocation setupNewPurchasingCapitalAssetLocationLine() {
        return new RequisitionCapitalAssetLocation();
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.PurchasingFormBase#getExtraButtons()
     *      <p/>
     *      KRAD Conversion: Performs customization of extra buttons.
     *      <p/>
     *      No data dictionary is involved.
     */
    @Override
    public List<ExtraButton> getExtraButtons() {
        super.getExtraButtons();
        for (int i = 0; i < extraButtons.size(); i++) {
            ExtraButton extraButton = extraButtons.get(i);
            if ("methodToCall.calculate".equalsIgnoreCase(extraButton.getExtraButtonProperty())) {
                if (canUserCalculate() == false) {
                    extraButtons.remove(i);
                    return extraButtons;
                }
            }
        }
        return extraButtons;
    }

    @Override
    public boolean canUserCalculate() {
        return documentActions != null && documentActions.containsKey(KRADConstants.KUALI_ACTION_CAN_EDIT) &&
                !getRequisitionDocument().isDocumentStoppedInRouteNode(RequisitionStatuses.NODE_ORG_REVIEW);
    }
}
