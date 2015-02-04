/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.module.purap.document.CorrectionReceivingDocument;
import org.kuali.ole.module.purap.document.LineItemReceivingDocument;
import org.kuali.ole.module.purap.document.ReceivingDocument;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleCorrectionReceivingDocument;
import org.kuali.ole.select.document.OleLineItemReceivingDocument;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.*;

public class OleLineItemReceivingServiceImpl implements OleLineItemReceivingService {

    private static final Logger LOG = Logger.getLogger(OleLineItemReceivingServiceImpl.class);

    private BusinessObjectService businessObjectService;

    protected ParameterService parameterService;


    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @Override
    public void saveOleLineItemReceivingItemDoc(OleLineItemReceivingDoc oleLineItemReceivingItemDoc) {
        //oleLineItemReceivingItemDoc.setActive(true);
        businessObjectService.save(oleLineItemReceivingItemDoc);
    }


    @Override
    public void saveOleLineItemReceivingCorrection(OleLineItemCorrectionReceivingDoc oleLineItemReceivingCorrection) {
        //oleLineItemReceivingCorrection.setActive(true);
        businessObjectService.save(oleLineItemReceivingCorrection);
    }

    public OleLineItemReceivingDoc getOleLineItemReceivingDoc(Integer receivingItemIdentifier, Integer lineItemId) {
        Map keys = new HashMap();
        keys.put("receivingItemIdentifier", receivingItemIdentifier);
        keys.put("lineItemId", lineItemId);
        return (OleLineItemReceivingDoc) businessObjectService.findByPrimaryKey(OleLineItemReceivingDoc.class, keys);
    }

    public OlePurchaseOrderItem getOlePurchaseOrderItem(Integer itemIdentifier) {
        Map keys = new HashMap();
        keys.put("itemIdentifier", itemIdentifier);
        return (OlePurchaseOrderItem) businessObjectService.findByPrimaryKey(OlePurchaseOrderItem.class, keys);
    }

    public OleLineItemReceivingDoc getOleLineItemReceivingDoc(Integer receivingItemIdentifier) {
        Map keys = new HashMap();
        keys.put("receivingLineItemIdentifier", receivingItemIdentifier);
        return (OleLineItemReceivingDoc) businessObjectService.findByPrimaryKey(OleLineItemReceivingDoc.class, keys);
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OleLineItemCorrectionReceivingDoc getOleLineItemCorrectionReceivingDoc(Integer receivingItemIdentifier) {
        Map keys = new HashMap();
        keys.put("receivingLineItemIdentifier", receivingItemIdentifier);
        return (OleLineItemCorrectionReceivingDoc) businessObjectService.findByPrimaryKey(OleLineItemCorrectionReceivingDoc.class, keys);

    }

    @Override
    public String getLineItemDocItemTitleId(OleLineItemReceivingItem oleLineItemReceivingItem) throws Exception {
        String itemTitleId = null;
        Map oleLineItemReceivingDocMap = new HashMap();
        oleLineItemReceivingDocMap.put("receivingLineItemIdentifier", oleLineItemReceivingItem.getReceivingItemIdentifier());
        List<OleLineItemReceivingDoc> oleLineItemReceivingItemDocList = (List) businessObjectService.findMatching(org.kuali.ole.select.businessobject.OleLineItemReceivingDoc.class, oleLineItemReceivingDocMap);
        Iterator iterator = oleLineItemReceivingItemDocList.iterator();
        if (iterator.hasNext()) {
            OleLineItemReceivingDoc oleLineItemReceivingDoc = (OleLineItemReceivingDoc) iterator.next();
            itemTitleId = oleLineItemReceivingDoc.getItemTitleId();
        }
        return itemTitleId;
    }

    @Override
    public String getCorrectionItemDocItemTitleId(OleCorrectionReceivingItem oleCorrectionReceivingItem) throws Exception {
        String itemTitleId = null;
        Map oleCorrectionItemReceivingDocMap = new HashMap();
        oleCorrectionItemReceivingDocMap.put(OLEConstants.RCV_LN_ITM_IDN, oleCorrectionReceivingItem.getReceivingItemIdentifier());
        List<OleLineItemCorrectionReceivingDoc> oleLineCorrectionReceivingDoc = (List) businessObjectService.findMatching(org.kuali.ole.select.businessobject.OleLineItemCorrectionReceivingDoc.class, oleCorrectionItemReceivingDocMap);
        Iterator iterator = oleLineCorrectionReceivingDoc.iterator();
        if (iterator.hasNext()) {
            OleLineItemCorrectionReceivingDoc oleLineItemCorrectionReceivingDoc = (OleLineItemCorrectionReceivingDoc) iterator.next();
            itemTitleId = oleLineItemCorrectionReceivingDoc.getItemTitleId();
        }
        return itemTitleId;
    }

    /**
     * This method returns the section Name that are to be collapsed while opening the document
     * @param document
     * @return collapseSections
     */
    public void getInitialCollapseSections(ReceivingDocument document) {
        LOG.debug("Inside getInitialCollapseSections()");
        String[] collapseSections = new String[]{};
        try {
            if (document instanceof OleLineItemReceivingDocument) {
                OleLineItemReceivingDocument rcvlDocument = (OleLineItemReceivingDocument)document;
                collapseSections = parameterService.getParameterValuesAsString(LineItemReceivingDocument.class,
                        OLEConstants.INITIAL_COLLAPSE_SECTIONS).toArray(new String[]{});
                rcvlDocument.setOverviewFlag(canCollapse(OLEConstants.OVERVIEW_SECTION, collapseSections));
                rcvlDocument.setDeliveryFlag(canCollapse(OLEConstants.DELIVERY_SECTION, collapseSections));
                rcvlDocument.setVendorFlag(canCollapse(OLEConstants.VENDOR_SECTION, collapseSections));
                rcvlDocument.setTitlesFlag(canCollapse(OLEConstants.TITLES_SECTION, collapseSections));
                rcvlDocument.setRelatedDocumentsFlag(canCollapse(OLEConstants.RELATED_DOCUMENT_SECTION, collapseSections));
                rcvlDocument.setNotesAndAttachmentFlag(canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION, collapseSections));
                rcvlDocument.setRouteLogFlag(canCollapse(OLEConstants.ROUTE_LOG_SECTION, collapseSections));
            }
            else if (document instanceof OleCorrectionReceivingDocument) {
                OleCorrectionReceivingDocument rcvcDocument = (OleCorrectionReceivingDocument)document;
                collapseSections = parameterService.getParameterValuesAsString(CorrectionReceivingDocument.class,
                        OLEConstants.INITIAL_COLLAPSE_SECTIONS).toArray(new String[]{});
                rcvcDocument.setOverviewFlag(canCollapse(OLEConstants.OVERVIEW_SECTION, collapseSections));
                rcvcDocument.setDeliveryFlag(canCollapse(OLEConstants.DELIVERY_SECTION, collapseSections));
                rcvcDocument.setVendorFlag(canCollapse(OLEConstants.VENDOR_SECTION, collapseSections));
                rcvcDocument.setTitlesFlag(canCollapse(OLEConstants.ITEMS_SECTION, collapseSections));
                rcvcDocument.setRelatedDocumentsFlag(canCollapse(OLEConstants.RELATED_DOCUMENT_SECTION, collapseSections));
                rcvcDocument.setNotesAndAttachmentFlag(canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION, collapseSections));
                rcvcDocument.setRouteLogFlag(canCollapse(OLEConstants.ROUTE_LOG_SECTION,collapseSections));
            }
        }
        catch (Exception e) {
            LOG.error("Exception while getting the default Collapse section on PurchasingAccountsPayable Document"+e);
            throw new RuntimeException(e);
        }
        LOG.debug("Leaving getInitialCollapseSections()");

    }

    private boolean canCollapse(String sectionName, String[] collapseSections) {
        LOG.debug("Inside method canCollapse()");
        List<String> sectionLists = Arrays.asList(collapseSections);
        if (sectionLists.contains(sectionName)) {
            return false;
        }
        return true;
    }

}
