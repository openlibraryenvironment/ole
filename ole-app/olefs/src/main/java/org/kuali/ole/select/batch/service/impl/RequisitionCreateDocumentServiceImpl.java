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
package org.kuali.ole.select.batch.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.batch.service.RequisitionCreateDocumentService;
import org.kuali.ole.select.bo.OLEEResourceOrderRecord;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleRequisitionDocumentService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.DocumentSystemSaveEvent;
import org.kuali.rice.kew.framework.postprocessor.IDocumentEvent;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequisitionCreateDocumentServiceImpl implements RequisitionCreateDocumentService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RequisitionCreateDocumentServiceImpl.class);

    protected DocumentService documentService;
    private OleRequisitionDocumentService oleRequisitionDocumentService;

    /**
     * Gets the documentService attribute.
     *
     * @return Returns the documentService.
     */
    public DocumentService getDocumentService() {
        return documentService;
    }


    /**
     * Sets the documentService attribute value.
     *
     * @param documentService The documentService to set.
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
/*    public boolean saveRequisitionDocuments(RequisitionDocument reqDocument) {
        try {
            documentService.saveDocument(reqDocument, DocumentSystemSaveEvent.class);
            //documentService.prepareWorkflowDocument(reqDocument);
            if ( LOG.isInfoEnabled() ) {
                LOG.info("Saved Requisition document. Document Number: "+reqDocument.getDocumentNumber());
            }
        }
        catch (Exception e) {
            LOG.error("Error persisting document # " + reqDocument.getDocumentHeader().getDocumentNumber() + " " + e.getMessage(), e);
            throw new RuntimeException("Error persisting document # " + reqDocument.getDocumentHeader().getDocumentNumber() + " " + e.getMessage(), e);
        }
        return true;
    }*/

    public String saveRequisitionDocuments(RequisitionDocument reqDocument) {
        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("Calling saveRequisitionDocuments in RequisitionCreateDocumentServiceImpl >>>>" + reqDocument.getDocumentNumber());
            }
            try {
                documentService.saveDocument(reqDocument, DocumentSystemSaveEvent.class);
            } catch (Exception e) {
                LOG.error("Exection while saving requisition document" + e);
                e.printStackTrace();
            }
            reqDocument.populateDocumentForRouting();

            Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
            // reqDocument.getDocumentHeader().setWorkflowDocument(KNSServiceLocator.getWorkflowDocumentService().createWorkflowDocument(new Long(reqDocument.getDocumentNumber()), principalPerson));

            String purchaseOrderType = "";
            if (reqDocument.getPurchaseOrderTypeId() != null) {

                Map purchaseOrderTypeIdMap = new HashMap();
                purchaseOrderTypeIdMap.put("purchaseOrderTypeId", reqDocument.getPurchaseOrderTypeId());
                org.kuali.rice.krad.service.BusinessObjectService
                        businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
                List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) businessObject.findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);
                if (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0) {
                    PurchaseOrderType purchaseOrderTypeDoc = (PurchaseOrderType) purchaseOrderTypeDocumentList.get(0);
                    purchaseOrderType = purchaseOrderTypeDoc.getPurchaseOrderType();
                }
                if (LOG.isDebugEnabled())
                    LOG.debug("purchaseOrderType >>>>>>>>>>>" + purchaseOrderType);
                //Modified for jira OLE-7278
                //if (purchaseOrderType.equalsIgnoreCase(PurapConstants.ORDER_TYPE_FIRM)) {
                    getDocumentService().routeDocument(reqDocument, null, null);
                //}
                LOG.debug("After Calling createWorkflowDocument >>>>>>>>>>>");
            }
            //  System.out.println("After Calling createWorkflowDocument >>>>>>>>>>>" + reqDocument.getDocumentHeader().getWorkflowDocument().getStatus());
            LOG.debug(IDocumentEvent.BEFORE_PROCESS);
            if (LOG.isInfoEnabled()) {
                LOG.info("Saved Requisition document. Document Number: " + reqDocument.getDocumentNumber());
            }
        } catch (Exception e) {
            LOG.error("Error persisting document # " + reqDocument.getDocumentHeader().getDocumentNumber() + " " + e.getMessage(), e);
            throw new RuntimeException("Error persisting document # " + reqDocument.getDocumentHeader().getDocumentNumber() + " " + e.getMessage(), e);
        }
        return reqDocument.getDocumentNumber();
    }

    public OleRequisitionDocument updateParamaterValue(OleRequisitionDocument requisitionDocument,List<PurchaseOrderType> purchaseOrderTypeDocumentList,OLEEResourceOrderRecord oleEResourceOrderRecord) {

        if (purchaseOrderTypeDocumentList.get(0).getPurchaseOrderType().equals(OLEConstants.ORD_TYPE_FIRM_FIX)) {
            for (OleRequisitionItem oleRequisitionItem : (List<OleRequisitionItem>) requisitionDocument.getItems()) {
                if (oleRequisitionItem.getItemTypeCode().equals(OLEConstants.ITEM)) {
                    oleRequisitionItem.setItemLocation(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_LOCATION_FIRM_FIXD));
                    oleRequisitionItem.setSingleCopyNumber(getOleRequisitionDocumentService().getParameter(OLEConstants.COPY_NUMBER));
                    oleRequisitionItem.setItemStatus(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_STATUS_FIRM_FIXD));
                }
            }

        } else if (purchaseOrderTypeDocumentList.get(0).getPurchaseOrderType().equals(OLEConstants.APPROVAL)) {
            for (OleRequisitionItem oleRequisitionItem : (List<OleRequisitionItem>) requisitionDocument.getItems()) {
                if (oleRequisitionItem.getItemTypeCode().equals(OLEConstants.ITEM)) {
                    oleRequisitionItem.setItemLocation(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_LOCATION_APPROVAL));
                    oleRequisitionItem.setSingleCopyNumber(getOleRequisitionDocumentService().getParameter(OLEConstants.COPY_NUMBER));
                    oleRequisitionItem.setItemStatus(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_STATUS_APPROVAL));
                }
            }
        } else {
            try {
                for (OleRequisitionItem oleRequisitionItem : (List<OleRequisitionItem>) requisitionDocument.getItems()) {
                    if (oleRequisitionItem.getItemTypeCode().equals(OLEConstants.ITEM)) {
                        oleRequisitionItem.setSingleCopyNumber(getOleRequisitionDocumentService().getParameter(OLEConstants.COPY_NUMBER));
                        oleRequisitionItem.setItemLocation(oleEResourceOrderRecord.getOleEResourceTxnRecord().getDefaultLocation());
                        oleRequisitionItem.setItemStatus("");
                    }
                }
                requisitionDocument.setRecurringPaymentTypeCode(getOleRequisitionDocumentService().getParameter(OLEConstants.RECURRING_PAY_TYP));
                SimpleDateFormat format = new SimpleDateFormat(OLEConstants.DATE_FORM_PO_BEGN_DT);
                java.util.Date utilDate = (java.util.Date) format.parse(OLEConstants.DATE_FORM_PO_END_DT);
                java.sql.Date poEndDate = new java.sql.Date(utilDate.getTime());
                java.util.Date date = new java.util.Date();
                Date poCreateDate = new Date(date.getTime());
                requisitionDocument.setPurchaseOrderBeginDate(poCreateDate);
                requisitionDocument.setPurchaseOrderEndDate(poEndDate);

            } catch (Exception e) {
                    LOG.error("Error Occured hwile setting the system parameter value to requisition document :"+e.getMessage());
            }
        }
        return requisitionDocument;
    }

    public void updateCopyNumber() {

    }


    public OleRequisitionDocumentService getOleRequisitionDocumentService() {
        if(oleRequisitionDocumentService == null) {
            return  SpringContext.getBean(OleRequisitionDocumentService.class);
        }

        return oleRequisitionDocumentService;
    }


}
