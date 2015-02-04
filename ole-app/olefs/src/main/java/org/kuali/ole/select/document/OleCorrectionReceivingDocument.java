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

import org.apache.commons.lang.StringEscapeUtils;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.module.purap.document.CorrectionReceivingDocument;
import org.kuali.ole.module.purap.document.LineItemReceivingDocument;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService;
import org.kuali.ole.select.document.service.impl.OleLineItemReceivingServiceImpl;
import org.kuali.ole.select.document.service.impl.OleReceivingServiceImpl;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoWrapperServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants.COMPONENT;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants.NAMESPACE;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.lang.reflect.Method;
import java.util.*;

@NAMESPACE(namespace = OleParameterConstants.PURCHASING_NAMESPACE)
@COMPONENT(component = "CorrectionReceivingDocument")
public class OleCorrectionReceivingDocument extends CorrectionReceivingDocument {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCorrectionReceivingDocument.class);
    private static transient BusinessObjectService businessObjectService;
    private static transient OleLineItemReceivingService oleCorrectionItemReceivingService;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }
    public static OleLineItemReceivingService getOleCorrectionItemReceivingService() {
        if (oleCorrectionItemReceivingService == null) {
            oleCorrectionItemReceivingService = SpringContext.getBean(OleLineItemReceivingService.class);
        }
        return oleCorrectionItemReceivingService;
    }

    public static void setOleCorrectionItemReceivingService(OleLineItemReceivingService oleLineItemReceivingService) {
        OleCorrectionReceivingDocument.oleCorrectionItemReceivingService = oleLineItemReceivingService;
    }

    @Override
    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * This method sets the intial values for the Receiving document
     */
    public void initiateDocument() {
        super.initiateDocument();
        getOleCorrectionItemReceivingService().getInitialCollapseSections(this);

    }

    /**
     * This method is overridden to populate Ole Correction Receiving Item from Receiving Line Item
     *
     * @see org.kuali.ole.module.purap.document.CorrectionReceivingDocument#populateCorrectionReceivingFromReceivingLine(org.kuali.ole.module.purap.document.LineItemReceivingDocument)
     */
    @Override
    public void populateCorrectionReceivingFromReceivingLine(LineItemReceivingDocument rlDoc) {
        LOG.debug("Inside populateCorrectionReceivingFromReceivingLine of OleCorrectionReceivingDocument");
        //populate receiving line document from purchase order
        this.setPurchaseOrderIdentifier(rlDoc.getPurchaseOrderIdentifier());
        this.getDocumentHeader().setDocumentDescription(rlDoc.getDocumentHeader().getDocumentDescription());
        this.getDocumentHeader().setOrganizationDocumentNumber(rlDoc.getDocumentHeader().getOrganizationDocumentNumber());
        this.setAccountsPayablePurchasingDocumentLinkIdentifier(rlDoc.getAccountsPayablePurchasingDocumentLinkIdentifier());
        this.setLineItemReceivingDocumentNumber(rlDoc.getDocumentNumber());

        //copy receiving line items
        for (OleLineItemReceivingItem rli : (List<OleLineItemReceivingItem>) rlDoc.getItems()) {
            OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
            OleLineItemReceivingDoc oleLineItemReceivingDoc = oleLineItemReceivingService.getOleLineItemReceivingDoc(rli.getReceivingItemIdentifier());
            rli.setItemTitleId(oleLineItemReceivingDoc.getItemTitleId());
            this.getItems().add(new OleCorrectionReceivingItem(rli, this));
        }
        LOG.debug("Leaving populateCorrectionReceivingFromReceivingLine of OleCorrectionReceivingDocument");
    }

    /**
     * This method is overridden to set special processing notes and other types of receipt notes
     * in two different lists to be displayed under Special Processing Notes Tab and Receipt Notes tab respectively.
     */
    @Override
    public void processAfterRetrieve() {
        try {
            LOG.debug("Inside processAfterRetrieve of OleCorrectionReceivingDocument");
            super.processAfterRetrieve();
            BibInfoWrapperService docStore = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
            FileProcessingService fileProcessingService = SpringContext.getBean(FileProcessingService.class);
            String itemDescription = "";
            List<OleCorrectionReceivingItem> items = new ArrayList<OleCorrectionReceivingItem>();
            items = this.getItems();
            Iterator<OleCorrectionReceivingItem> iterator = items.iterator();
            String titleId = null;
            int itemCount = 0;
            boolean isBibFileExist = false;
            boolean isBibEdit = false;
            List<OleCorrectionReceivingItemReceiptNotes> receiptNoteList;
            List<OleCorrectionReceivingItemReceiptNotes> specialHandlingNoteList;
            for (OleCorrectionReceivingItem item : items) {
                receiptNoteList = new ArrayList<OleCorrectionReceivingItemReceiptNotes>();
                specialHandlingNoteList = new ArrayList<OleCorrectionReceivingItemReceiptNotes>();
                List<OleCorrectionReceivingItemReceiptNotes> notes = new ArrayList<OleCorrectionReceivingItemReceiptNotes>();
                notes = item.getCorrectionNoteList();
                for (OleCorrectionReceivingItemReceiptNotes note : notes) {
                    String noteType = note.getNoteType().getNoteType();
                    if (noteType.equalsIgnoreCase(OLEConstants.SPECIAL_PROCESSING_INSTRUCTION_NOTE)) {
                        specialHandlingNoteList.add(note);
                    } else {
                        receiptNoteList.add(note);
                    }
                    item.setCorrectionSpecialHandlingNoteList(specialHandlingNoteList);
                    item.setCorrectionReceiptNoteList(receiptNoteList);
                    item.setCorrectionReceiptNoteListSize(receiptNoteList.size());
                }
                while (iterator.hasNext()) {
                    LOG.debug("###########inside processAfterRetrieve item loop###########");
                    Object object = iterator.next();
                    if (object instanceof OleCorrectionReceivingItem) {
                        LOG.debug("###########inside processAfterRetrieve ole requisition item###########");
                        OleCorrectionReceivingItem singleItem = (OleCorrectionReceivingItem) object;
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Title id while retriving ------>" + singleItem.getItemTitleId());
                        }
                        if (singleItem.getItemTitleId() == null) {
                            singleItem.setItemTitleId(getOleCorrectionItemReceivingService().getCorrectionItemDocItemTitleId(singleItem));
                        }

                        if (singleItem.getItemTitleId() != null) {
                            Bib bib =  new BibMarc();
                            if(singleItem.getItemTitleId()!=null && singleItem.getItemTitleId()!=""){
                                bib= getDocstoreClientLocator().getDocstoreClient().retrieveBib(singleItem.getItemTitleId());
                                singleItem.setBibUUID(bib.getId());
                            }
                            itemDescription = (bib.getTitle() != null && !bib.getTitle().isEmpty() ? bib.getTitle() : "") + (bib.getAuthor() != null &&  !bib.getAuthor().isEmpty()? "," + bib.getAuthor() : "") + (bib.getPublisher() != null && !bib.getPublisher().isEmpty()? "," + bib.getPublisher() : "") + (bib.getIsbn() != null && !bib.getIsbn().isEmpty()? "," + bib.getIsbn() : "");
                            if (singleItem.getItemTitleId() != null) {
                                singleItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(singleItem.getItemTitleId()));
                            }
                            StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
                            itemDescription = stringEscapeUtils.unescapeXml(itemDescription);
                            singleItem.setItemDescription(itemDescription);
                        }
                        OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
                        OlePurchaseOrderItem olePurchaseOrderItem = null;
                        if (singleItem.getPurchaseOrderIdentifier() != null) {
                            olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(singleItem.getPurchaseOrderIdentifier());
                        } else {
                            olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(getPurchaseOrderIdentifier());
                        }
                        if (olePurchaseOrderItem != null && olePurchaseOrderItem.getItemTitleId() != null) {
                            singleItem.setItemTitleId(olePurchaseOrderItem.getItemTitleId());
                        } else {
                            singleItem.setItemTitleId(getOleCorrectionItemReceivingService()
                                    .getCorrectionItemDocItemTitleId(singleItem));
                        }

                        Integer rcvnitemIdentifier = item.getReceivingItemIdentifier();
                        Map<String, Integer> recItemIdentifer = new HashMap<String, Integer>();
                        recItemIdentifer.put(OLEConstants.RCV_LN_ITM_IDN, rcvnitemIdentifier);
                        OleCorrectionReceivingItem oleCorrectionReceivingItems = new OleCorrectionReceivingItem();
                        List<OleLineItemCorrectionReceivingDoc> oleLineItemCorrectionReceivingDocs = (List<OleLineItemCorrectionReceivingDoc>) getBusinessObjectService().findMatching(OleLineItemCorrectionReceivingDoc.class, recItemIdentifer);
                        if (oleLineItemCorrectionReceivingDocs != null && oleLineItemCorrectionReceivingDocs.size() > 0) {
                            for (int i = 0; i < oleLineItemCorrectionReceivingDocs.size(); i++) {
                                Integer rcvnItemIdentifier = oleLineItemCorrectionReceivingDocs.get(i).getReceivingLineItemIdentifier();
                                if (!rcvnItemIdentifier.equals(item.getReceivingItemIdentifier())) {
                                    OleLineItemCorrectionReceivingDoc corr = new OleLineItemCorrectionReceivingDoc();
                                    corr.setReceivingLineItemIdentifier(item.getReceivingItemIdentifier());
                                    corr.setItemTitleId(item.getItemTitleId());
                                    oleLineItemReceivingService.saveOleLineItemReceivingCorrection(corr);
                                }
                            }
                        } else {
                            OleLineItemCorrectionReceivingDoc corr = new OleLineItemCorrectionReceivingDoc();
                            corr.setReceivingLineItemIdentifier(item.getReceivingItemIdentifier());
                            corr.setItemTitleId(item.getItemTitleId());
                            oleLineItemReceivingService.saveOleLineItemReceivingCorrection(corr);
                        }
                        Map cpySection = new HashMap();
                        cpySection.put("itemIdentifier", singleItem.getPurchaseOrderIdentifier());

                        List<OlePurchaseOrderItem> olePurchaseOrderItems = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, cpySection);
                        if (olePurchaseOrderItems.size() > 0) {
                            for (OlePurchaseOrderItem purItem : olePurchaseOrderItems) {
                                singleItem.setCopyList(purItem.getCopyList());
                            }
                        }
                    }
                }
                LOG.debug("Leaving processAfterRetrieve of OleCorrectionReceivingDocument");
            }
        } catch (Exception e) {
            LOG.error("Exception in processAfterRetrieve of OleCorrectionReceivingDocument"+e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to get the bibedtior creat url from propertie file
     *
     * @return Bibeditor creat url string
     */
    public String getBibeditorCreateURL() {
        String bibeditorCreateURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.BIBEDITOR_CREATE_URL_KEY);
        return bibeditorCreateURL;
    }

    public String getBibSearchURL() {
        String bibSearchURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.BIBEDITOR_SEARCH_URL_KEY);
        return bibSearchURL;
    }

    /**
     * This method is used to get the bibedtior edit url from propertie file
     *
     * @return Bibeditor edit url string
     */
    public String getBibeditorEditURL() {
        String bibeditorEditURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.BIBEDITOR_URL_KEY);
        return bibeditorEditURL;
    }

    /**
     * This method is used to get the bibedtior view url from propertie file
     *
     * @return Bibeditor view url string
     */
    public String getBibeditorViewURL() {
        String bibeditorViewURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSTORE_APP_URL_KEY);
        return bibeditorViewURL;
    }

    /**
     * This method is used to get the dublinedtior edit url from propertie file
     *
     * @return Dublineditor edit url string
     */
    public String getDublinEditorEditURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorEditURL();
    }

    /**
     * This method is used to get the dublinedtior view url from propertie file
     *
     * @return dublineditor view url string
     */
    public String getDublinEditorViewURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorViewURL();
    }

    /**
     * This method is used to get the directory path where the marc xml files need to be created
     *
     * @return Directory path string
     */
    public String getMarcXMLFileDirLocation() throws Exception {
        FileProcessingService fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        String externaleDirectory = fileProcessingService.getMarcXMLFileDirLocation();
        return externaleDirectory;
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

    @Override
    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {

        if (this.getFinancialSystemDocumentHeader().getWorkflowDocument().isProcessed()) {
            OleReceivingServiceImpl service = new OleReceivingServiceImpl();
            service.completeCorrectionReceivingDocument(this);
        }
    }

    /**
     * This method takes List of UUids as parameter and creates a LinkedHashMap with instance as key and id as value. and calls
     * Docstore's QueryServiceImpl class getWorkBibRecords method and return workBibDocument for passed instance Id.
     *
     * @param instanceIdsList
     * @return List<WorkBibDocument>
     */
//    private List<WorkBibDocument> getWorkBibDocuments(List<String> instanceIdsList) {
//        List<LinkedHashMap<String, String>> instanceIdMapList = new ArrayList<LinkedHashMap<String, String>>();
//        for (String instanceId : instanceIdsList) {
//            LinkedHashMap<String, String> instanceIdMap = new LinkedHashMap<String, String>();
//            instanceIdMap.put(DocType.INSTANCE.getDescription(), instanceId);
//            instanceIdMapList.add(instanceIdMap);
//        }
//
//        QueryService queryService = QueryServiceImpl.getInstance();
//        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
//        try {
//            workBibDocuments = queryService.getWorkBibRecords(instanceIdMapList);
//        } catch (Exception ex) {
//            // TODO Auto-generated catch block
//            ex.printStackTrace();
//        }
//        return workBibDocuments;
//    }

    // Added for Jira OLE-1900 Ends

    // Added for Jira OLE-1900 Starts

    /**
     * This method will set copies into list of copies for LineItem.
     *
     * @param singleItem
     * @param workBibDocument
     * @return
     */
    public List<OleCopies> setCopiesToLineItem(OleCorrectionReceivingItem singleItem, WorkBibDocument workBibDocument) {
        List<WorkInstanceDocument> instanceDocuments = workBibDocument.getWorkInstanceDocumentList();
        List<OleCopies> copies = new ArrayList<OleCopies>();
        for (WorkInstanceDocument workInstanceDocument : instanceDocuments) {
            List<WorkItemDocument> itemDocuments = workInstanceDocument.getItemDocumentList();
            StringBuffer enumeration = new StringBuffer();
            for (int itemDocs = 0; itemDocs < itemDocuments.size(); itemDocs++) {
                if (itemDocs + 1 == itemDocuments.size()) {
                    enumeration = enumeration.append(itemDocuments.get(itemDocs).getEnumeration());
                } else {
                    enumeration = enumeration.append(itemDocuments.get(itemDocs).getEnumeration() + ",");
                }

            }
            int startingCopy = 0;
            if (singleItem.getItemReceivedTotalParts().intValue() != 0 && null != enumeration) {
                String enumerationSplit = enumeration.substring(1, 2);
                boolean isint = checkIsEnumerationSplitIsIntegerOrNot(enumerationSplit);
                if (isint) {
                    startingCopy = Integer.parseInt(enumerationSplit);
                }
            }
            int parts = 1;
            if (null != singleItem.getPurchaseOrderIdentifier()) {
                parts = getPONoOfPartsOrdered(singleItem.getPurchaseOrderIdentifier());
            } else {
                parts = singleItem.getItemReceivedTotalParts().intValue();
            }
            /*
             * if (singleItem.getItemReceivedTotalQuantity().isGreaterThan(new KualiDecimal(1)) ||
             * singleItem.getItemReceivedTotalParts().isGreaterThan(new KualiDecimal(1))) {
             */
            int noOfCopies = workInstanceDocument.getItemDocumentList().size()
                    / parts;
            OleRequisitionCopies copy = new OleRequisitionCopies();
            copy.setParts(new KualiInteger(parts));
            copy.setLocationCopies(workInstanceDocument.getHoldingsDocument().getLocationName());
            copy.setItemCopies(new KualiDecimal(noOfCopies));
            copy.setPartEnumeration(enumeration.toString());
            copy.setStartingCopyNumber(new KualiInteger(startingCopy));
            copies.add(copy);
            // }
        }
        return copies;
    }

    public int getPONoOfPartsOrdered(int poId) {
        OleLineItemReceivingService oleLineItemReceivingService = SpringContext
                .getBean(OleLineItemReceivingServiceImpl.class);
        OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(poId);
        return olePurchaseOrderItem.getItemNoOfParts().intValue();
    }

    public boolean checkIsEnumerationSplitIsIntegerOrNot(String enumerationSplit) {
        try {
            int startingCopy = Integer.parseInt(enumerationSplit);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean getIsATypeOfRCVGDoc() {
        return false;
    }

    public boolean getIsATypeOfCORRDoc() {
        return true;
    }

    public boolean getIsFinalReqs() {
        if (this.getDocumentHeader().getWorkflowDocument().isFinal()) {
            return true;
        }
        return false;
    }

}