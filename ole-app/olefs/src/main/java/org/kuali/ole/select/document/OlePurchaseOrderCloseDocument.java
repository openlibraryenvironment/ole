/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document;

import org.kuali.ole.module.purap.document.PurchaseOrderCloseDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is the document class for Ole Purchase Order Close Document
 */
public class OlePurchaseOrderCloseDocument extends PurchaseOrderCloseDocument {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurchaseOrderCloseDocument.class);
    private String vendorPoNumber;
    private static transient OlePurapService olePurapService;
    /**
     * Default constructor.
     */
    public OlePurchaseOrderCloseDocument() {
        super();
    }

    /**
     * This method is overridden to populate newly added ole fields from requisition into Ole Purchase Order Close Document.
     *
     * @see org.kuali.ole.module.purap.document.PurchaseOrderDocument#populatePurchaseOrderFromRequisition(org.kuali.ole.module.purap.document.RequisitionDocument)
     */
    @Override
    public void populatePurchaseOrderFromRequisition(RequisitionDocument requisitionDocument) {
        SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).populatePurchaseOrderFromRequisition(this, requisitionDocument);
    }

    /**
     * This method is overriden to populate bib info in Ole Purchase Order Close Document.
     *
     * @see org.kuali.ole.module.purap.document.PurchaseOrderCloseDocument#prepareForSave(org.kuali.rice.krad.rule.event.KualiDocumentEvent)
     */
    @Override
    public void prepareForSave(KualiDocumentEvent event) {
        SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).prepareForSave(this, event);
        super.prepareForSave(event);
    }

    /**
     * This method is overriden to populate bib info in Ole Purchase Order Close Document.
     *
     * @see org.kuali.ole.module.purap.document.PurchaseOrderCloseDocument#prepareForSave(org.kuali.rice.krad.rule.event.KualiDocumentEvent)
     */
    @Override
    public void processAfterRetrieve() {
        if (this.getVendorAliasName() == null) {
            populateVendorAliasName();
        }
        SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).processAfterRetrieve(this);
    }

    @Override
    public List getItemsActiveOnly() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getItemsActiveOnly(this);
    }

    /**
     * Gets the active items in this Purchase Order, and sets up the alternate amount for GL entry creation.
     *
     * @return the list of all active items in this Purchase Order.
     */
    @Override
    public List getItemsActiveOnlySetupAlternateAmount() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getItemsActiveOnlySetupAlternateAmount(this);
    }

    @Override
    public boolean getAdditionalChargesExist() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getAdditionalChargesExist(this);
    }

    /**
     * This method returns if Purchase Order Document created is in Final Status
     *
     * @return
     */
    public boolean getIsFinalReqs() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getIsFinalReqs(this);
    }

    public boolean getIsSplitPO() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getIsSplitPO(this);
    }

    public boolean getIsReOpenPO() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getIsReOpenPO(this);
    }

    /**
     * This method is used to get the bibedtior creat url from propertie file
     *
     * @return Bibeditor creat url string
     */
    public String getBibeditorCreateURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getBibeditorCreateURL();
    }

    public String getBibSearchURL() {
        LOG.debug("Inside getBibSearchURL of OlePurchaseOrderCloseDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getBibSearchURL();
    }

    /**
     * This method is used to get the bibedtior edit url from propertie file
     *
     * @return Bibeditor edit url string
     */
    public String getBibeditorEditURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getBibeditorEditURL();
    }

    /**
     * This method is used to get the Instanceeditor url from propertie file
     *
     * @return Instanceeditor url string
     */
    public String getInstanceEditorURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getInstanceEditorURL();
    }

    /**
     * This method is used to get the bibedtior view url from propertie file
     *
     * @return Bibeditor view url string
     */
    public String getBibeditorViewURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getBibeditorViewURL();
    }

    /**
     * This method is used to get the directory path where the marc xml files need to be created
     *
     * @return Directory path string
     */
    public String getMarcXMLFileDirLocation() throws Exception {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getMarcXMLFileDirLocation();
    }

    public String getVendorPoNumber() {
        return vendorPoNumber;
    }

    public void setVendorPoNumber(String vendorPoNumber) {
        this.vendorPoNumber = vendorPoNumber;
    }

    /**
     * This method is used to check the status of the document for displaying view and edit buttons in line item
     *
     * @return boolean
     */
    public boolean getIsSaved() {
        if (this.getDocumentHeader().getWorkflowDocument().isSaved() || this.getDocumentHeader().getWorkflowDocument().isInitiated()) {
            return true;
        }
        return false;
    }

    /**
     * This method is used to get the dublin editor edit url from property file
     *
     * @return Dublineditor edit url string
     */
    public String getDublinEditorEditURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorEditURL();
    }

    /**
     * This method is used to get the dublin editor view url from property file
     *
     * @return dublineditor view url string
     */
    public String getDublinEditorViewURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorViewURL();
    }

    public boolean getIsATypeOfRCVGDoc() {
        return false;
    }

    public boolean getIsATypeOfCORRDoc() {
        return false;
    }

    private void populateVendorAliasName() {
        Map vendorDetailMap = new HashMap();
        vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, this.getVendorHeaderGeneratedIdentifier());
        vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, this.getVendorDetailAssignedIdentifier());
        List<VendorAlias> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorAlias.class, vendorDetailMap);
        if (vendorDetailList != null && vendorDetailList.size() > 0) {
            this.setVendorAliasName(vendorDetailList.get(0).getVendorAliasName());
        }
    }

    public static OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }
}
