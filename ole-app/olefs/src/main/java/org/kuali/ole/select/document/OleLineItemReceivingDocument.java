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
import org.kuali.ole.docstore.common.client.*;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.module.purap.document.LineItemReceivingDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoServiceImpl;
import org.kuali.ole.select.service.impl.exception.DocStoreConnectionException;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants.COMPONENT;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants.NAMESPACE;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import java.util.*;

/**
 * This class is the document class for Ole Line Item Receiving
 */
@NAMESPACE(namespace = OleParameterConstants.PURCHASING_NAMESPACE)
@COMPONENT(component = "LineItemReceivingDocument")
public class OleLineItemReceivingDocument extends LineItemReceivingDocument {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLineItemReceivingDocument.class);

    private static transient ConfigurationService kualiConfigurationService;
    private static transient BibInfoWrapperService bibInfoWrapperService;
    private static transient FileProcessingService fileProcessingService;
    private static transient OleLineItemReceivingService oleLineItemReceivingService;
    //private static transient OleLineItemReceivingDocumentHelperService oleLineItemReceivingDocumentHelperService;
    private static transient BibInfoService bibInfoService;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public static BibInfoService getBibInfoService() {
        if (bibInfoService == null) {
            bibInfoService = SpringContext.getBean(BibInfoServiceImpl.class);
        }
        return bibInfoService;
    }

    public static OleLineItemReceivingService getOleLineItemReceivingService() {
        if (oleLineItemReceivingService == null) {
            oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingService.class);
        }
        return oleLineItemReceivingService;
    }

    public static void setOleLineItemReceivingService(OleLineItemReceivingService oleLineItemReceivingService) {
        OleLineItemReceivingDocument.oleLineItemReceivingService = oleLineItemReceivingService;
    }


    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public static void setConfigurationService(ConfigurationService kualiConfigurationService) {
        OleLineItemReceivingDocument.kualiConfigurationService = kualiConfigurationService;
    }

    public static BibInfoWrapperService getBibInfoWrapperService() {
        if (bibInfoWrapperService == null) {
            bibInfoWrapperService = SpringContext.getBean(BibInfoWrapperService.class);
        }
        return bibInfoWrapperService;
    }

    public static void setBibInfoWrapperService(BibInfoWrapperService bibInfoWrapperService) {
        OleLineItemReceivingDocument.bibInfoWrapperService = bibInfoWrapperService;
    }

    public static FileProcessingService getFileProcessingService() {
        if (fileProcessingService == null) {
            fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        }
        return fileProcessingService;
    }

    public static void setFileProcessingService(FileProcessingService fileProcessingService) {
        OleLineItemReceivingDocument.fileProcessingService = fileProcessingService;
    }

    public boolean getIsFinalReqs() {
        if (this.getDocumentHeader().getWorkflowDocument().isFinal()) {
            return true;
        }
        return false;
    }

    /**
     * This method sets the intial values for the document
     */
    public void initiateDocument() {
        super.initiateDocument();
        getOleLineItemReceivingService().getInitialCollapseSections(this);
    }

    /**
     * This method is overridden to populate OleLineItemReceivingItem from PurchaseOrderDocument.
     *
     * @param PurchaseOrderDocument
     */
    @Override
    public void populateReceivingLineFromPurchaseOrder(PurchaseOrderDocument po) {
        LOG.debug("Inside populateReceivingLineFromPurchaseOrder of OleLineItemReceivingDocument");
        // populate receiving line document from purchase order
        this.setPurchaseOrderIdentifier(po.getPurapDocumentIdentifier());
        this.getDocumentHeader().setOrganizationDocumentNumber(po.getDocumentHeader().getOrganizationDocumentNumber());
        this.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());

        // copy vendor
        this.setVendorHeaderGeneratedIdentifier(po.getVendorHeaderGeneratedIdentifier());
        this.setVendorDetailAssignedIdentifier(po.getVendorDetailAssignedIdentifier());
        this.setVendorName(po.getVendorName());
        this.setVendorNumber(po.getVendorNumber());
        this.setVendorAddressGeneratedIdentifier(po.getVendorAddressGeneratedIdentifier());
        this.setVendorLine1Address(po.getVendorLine1Address());
        this.setVendorLine2Address(po.getVendorLine2Address());
        this.setVendorCityName(po.getVendorCityName());
        this.setVendorStateCode(po.getVendorStateCode());
        this.setVendorPostalCode(po.getVendorPostalCode());
        this.setVendorCountryCode(po.getVendorCountryCode());

        // copy alternate vendor
        this.setAlternateVendorName(po.getAlternateVendorName());
        this.setAlternateVendorNumber(po.getAlternateVendorNumber());
        this.setAlternateVendorDetailAssignedIdentifier(po.getAlternateVendorDetailAssignedIdentifier());
        this.setAlternateVendorHeaderGeneratedIdentifier(po.getAlternateVendorHeaderGeneratedIdentifier());

        // copy delivery
        this.setDeliveryBuildingCode(po.getDeliveryBuildingCode());
        this.setDeliveryBuildingLine1Address(po.getDeliveryBuildingLine1Address());
        this.setDeliveryBuildingLine2Address(po.getDeliveryBuildingLine2Address());
        this.setDeliveryBuildingName(po.getDeliveryBuildingName());
        this.setDeliveryBuildingRoomNumber(po.getDeliveryBuildingRoomNumber());
        this.setDeliveryCampusCode(po.getDeliveryCampusCode());
        this.setDeliveryCityName(po.getDeliveryCityName());
        this.setDeliveryCountryCode(po.getDeliveryCountryCode());
        this.setDeliveryInstructionText(po.getDeliveryInstructionText());
        this.setDeliveryPostalCode(po.getDeliveryPostalCode());
        this.setDeliveryRequiredDate(po.getDeliveryRequiredDate());
        this.setDeliveryRequiredDateReasonCode(po.getDeliveryRequiredDateReasonCode());
        this.setDeliveryStateCode(po.getDeliveryStateCode());
        this.setDeliveryToEmailAddress(po.getDeliveryToEmailAddress());
        this.setDeliveryToName(po.getDeliveryToName());
        this.setDeliveryToPhoneNumber(po.getDeliveryToPhoneNumber());

        // copy purchase order items
        for (OlePurchaseOrderItem poi : (List<OlePurchaseOrderItem>) po.getItems()) {
            // TODO: Refactor this check into a service call. route FYI during submit
            if (poi.isItemActiveIndicator() && poi.getItemType().isQuantityBasedGeneralLedgerIndicator() && poi.getItemType().isLineItemIndicator()) {
                this.getItems().add(new OleLineItemReceivingItem(poi, this));
            }
        }

        populateDocumentDescription(po);
        LOG.debug("Leaving populateReceivingLineFromPurchaseOrder of OleLineItemReceivingDocument");
    }

    @Override
    public void prepareForSave() {
        try {
            String titleId = null;
            List<OleLineItemReceivingItem> items = new ArrayList<OleLineItemReceivingItem>();
            items = this.getItems();

            Iterator iterator = items.iterator();
            HashMap dataMap = new HashMap();
            int itemCount = 0;
            while (iterator.hasNext()) {
                LOG.debug("###########inside prepareForSave item loop###########");
                Object object = iterator.next();
                if (object instanceof OleLineItemReceivingItem) {
                    LOG.debug("###########inside prepareForSave ole requisition item###########");
                    OleLineItemReceivingItem singleItem = (OleLineItemReceivingItem) object;
                    if (singleItem.getItemTitleId() != null) {
                        titleId = singleItem.getItemTitleId();
                    } else if (singleItem.getBibInfoBean() != null) {
                        titleId = singleItem.getBibInfoBean().getTitleId();
                    }
                    if (ObjectUtils.isNull(singleItem.getItemReceivedTotalParts())) {
                        singleItem.setItemReceivedTotalParts(KualiDecimal.ZERO);
                    } else if (ObjectUtils.isNull(singleItem.getItemReceivedTotalQuantity())) {
                        singleItem.setItemReceivedTotalQuantity(KualiDecimal.ZERO);
                    } else if (ObjectUtils.isNull(singleItem.getItemReturnedTotalParts())) {
                        singleItem.setItemReturnedTotalParts(KualiDecimal.ZERO);
                    } else if (ObjectUtils.isNull(singleItem.getItemReturnedTotalQuantity())) {
                        singleItem.setItemReturnedTotalQuantity(KualiDecimal.ZERO);
                    } else if (ObjectUtils.isNull(singleItem.getItemDamagedTotalParts())) {
                        singleItem.setItemDamagedTotalParts(KualiDecimal.ZERO);
                    } else if (ObjectUtils.isNull(singleItem.getItemDamagedTotalQuantity())) {
                        singleItem.setItemDamagedTotalQuantity(KualiDecimal.ZERO);
                    }
                }
            }
            super.prepareForSave();
        } catch (DocStoreConnectionException dsce) {
            GlobalVariables.getMessageMap().putError("error.requisition.docstore.connectionError", RiceKeyConstants.ERROR_CUSTOM, "Error while connecting to document storage server, contact system administrator.");
        } catch (Exception e) {
            LOG.error("Exception during prepareForSave() in OleLineItemReceivingDocument"+e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is overridden to set special processing notes and other types of receipt notes in two different lists to be
     * displayed under Special Processing Notes Tab and Receipt Notes tab respectively.
     */
    @Override
    public void processAfterRetrieve() {
        try {
            LOG.debug("Inside processAfterRetrieve of OleLineItemReceivingDocument");

            String itemDescription = "";
            List<OleLineItemReceivingItem> items = new ArrayList<OleLineItemReceivingItem>();
            items = this.getItems();
            Iterator iterator = items.iterator();
            String titleId = null;
            int itemCount = 0;
            boolean isBibFileExist = false;
            boolean isBibEdit = false;
            List receiptNoteList;
            List specialHandlingNoteList;
            for (OleLineItemReceivingItem item : items) {
                receiptNoteList = new ArrayList();
                specialHandlingNoteList = new ArrayList();
                List<OleLineItemReceivingReceiptNotes> notes = new ArrayList<OleLineItemReceivingReceiptNotes>();
                notes = item.getNoteList();
                for (OleLineItemReceivingReceiptNotes note : notes) {
                    String noteType = note.getNoteType().getNoteType();
                    if (noteType.equalsIgnoreCase(OLEConstants.SPECIAL_PROCESSING_INSTRUCTION_NOTE)) {
                        specialHandlingNoteList.add(note);
                    } else {
                        receiptNoteList.add(note);
                    }
                    item.setSpecialHandlingNoteList(specialHandlingNoteList);
                    item.setReceiptNoteList(receiptNoteList);
                    item.setReceiptNoteListSize(receiptNoteList.size());
                }
            }
            while (iterator.hasNext()) {
                LOG.debug("###########inside processAfterRetrieve item loop###########");
                Object object = iterator.next();
                if (object instanceof OleLineItemReceivingItem) {
                    LOG.debug("###########inside processAfterRetrieve ole requisition item###########");
                    OleLineItemReceivingItem singleItem = (OleLineItemReceivingItem) object;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Title id while retriving ------>" + singleItem.getItemTitleId());
                    }
                    BibInfoBean xmlBibInfoBean = new BibInfoBean();
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    // Modified for Jira OLE-2515
/*                    if (!StringUtils.equalsIgnoreCase(singleItem.getItemTypeCode(), PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
                        OlePurchaseOrderItem olePurchaseOrderItem = getOleLineItemReceivingService().getOlePurchaseOrderItem(singleItem.getPurchaseOrderIdentifier());
                        singleItem.setItemTitleId(olePurchaseOrderItem.getItemTitleId());
                    }*/
                    if (singleItem.getItemTitleId() == null) {
                        singleItem.setItemTitleId(getOleLineItemReceivingService().getLineItemDocItemTitleId(singleItem));
                    }

                    if (singleItem.getItemTitleId() != null) {
                        Bib bib = new BibMarc();
                        if (singleItem.getItemTitleId() != null && singleItem.getItemTitleId() != "") {
                            bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(singleItem.getItemTitleId());
                            singleItem.setBibUUID(bib.getId());
                        }
                        singleItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(singleItem.getItemTitleId()));
                        itemDescription = ((bib.getTitle() != null && !bib.getTitle().isEmpty()) ? bib.getTitle().trim() + ", " : "") +
                                ((bib.getAuthor() != null && !bib.getAuthor().isEmpty()) ? bib.getAuthor().trim() + ", " : "") +
                                ((bib.getPublisher() != null && !bib.getPublisher().isEmpty()) ? bib.getPublisher().trim() + ", " : "") +
                                ((bib.getIsbn() != null && !bib.getIsbn().isEmpty()) ? bib.getIsbn().trim() + ", " : "");
                        if (itemDescription != null && !(itemDescription.equals(""))) {
                            itemDescription = itemDescription.lastIndexOf(",") < 0 ? itemDescription :
                                    itemDescription.substring(0, itemDescription.lastIndexOf(","));
                            StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
                            itemDescription = stringEscapeUtils.unescapeXml(itemDescription);
                            singleItem.setItemDescription(itemDescription);
                        }
                    }
                    Map cpySection = new HashMap();
                    cpySection.put("itemIdentifier", singleItem.getPurchaseOrderIdentifier());

                    List<OlePurchaseOrderItem> olePurchaseOrderItems = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, cpySection);
                    if (olePurchaseOrderItems.size() > 0) {
                        for (OlePurchaseOrderItem item : olePurchaseOrderItems) {
                            singleItem.setCopyList(item.getCopyList());
                        }
                    }
                }
            }
            LOG.debug("Leaving processAfterRetrieve of OleLineItemReceivingDocument");
        } catch (Exception e) {
            LOG.error("Exception during processAfterRetrieve() in OleLineItemReceivingDocument"+e);
            throw new RuntimeException(e);
        }
    }

    // Added for Jira OLE-1900 Starts


    public void setEnumerationToCopies(OleLineItemReceivingItem singleItem) {
        String partEnumerationCopy = getConfigurationService().getPropertyValueAsString(
                OLEConstants.PART_ENUMERATION_COPY);
        String partEnumerationVolume = getConfigurationService().getPropertyValueAsString(
                OLEConstants.PART_ENUMERATION_VOLUME);
        List<OleCopies> itemCopies = singleItem.getCopies();
        List<OleCopies> copiesList = new ArrayList<OleCopies>();
        for (int copies = 0; copies < itemCopies.size(); copies++) {
            itemCopies.get(copies).setParts(new KualiInteger(singleItem.getItemReceivedTotalParts().intValue()));
            int startingCopyNumber = itemCopies.get(copies).getStartingCopyNumber().intValue();
            StringBuffer enumeration = new StringBuffer();
            for (int noOfCopies = 0; noOfCopies < singleItem.getItemCopies().intValue(); noOfCopies++) {
                for (int noOfParts = 0; noOfParts < singleItem.getItemReceivedTotalParts().intValue(); noOfParts++) {
                    int newNoOfCopies = startingCopyNumber + noOfCopies;
                    int newNoOfParts = noOfParts + 1;
                    if (noOfCopies + 1 == singleItem.getItemCopies().intValue()
                            && newNoOfParts == singleItem.getItemReceivedTotalParts().intValue()) {
                        enumeration = enumeration.append(
                                partEnumerationCopy + newNoOfCopies + OLEConstants.DOT_TO_SEPARATE_COPIES_PARTS)
                                .append(
                                        partEnumerationVolume + newNoOfParts);
                    } else {
                        enumeration = enumeration.append(
                                partEnumerationCopy + newNoOfCopies + OLEConstants.DOT_TO_SEPARATE_COPIES_PARTS)
                                .append(partEnumerationVolume + newNoOfParts
                                        + OLEConstants.COMMA_TO_SEPARATE_ENUMERATION);
                    }
                }
            }
            itemCopies.get(copies).setPartEnumeration(enumeration.toString());
        }
    }

    /**
     * This method will set copies into list of copies for LineItem.
     *
     * @param singleItem
     * @param workBibDocument
     * @return
     */
    public List<OleCopies> setCopiesToLineItem(OleLineItemReceivingItem singleItem, WorkBibDocument workBibDocument) {
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

            /*
             * if (singleItem.getItemReceivedTotalQuantity().isGreaterThan(new KualiDecimal(1)) ||
             * singleItem.getItemReceivedTotalParts().isGreaterThan(new KualiDecimal(1))) {
             */
            boolean isValid = checkForEnumeration(enumeration);
            if (isValid) {
                OleRequisitionCopies copy = new OleRequisitionCopies();
                int noOfCopies = 0;
                if (null != singleItem.getItemOrderedQuantity() && null != singleItem.getItemOrderedParts()) {
                    noOfCopies = workInstanceDocument.getItemDocumentList().size()
                            / singleItem.getItemOrderedParts().intValue();
                    copy.setParts(new KualiInteger(singleItem.getItemOrderedParts().intValue()));
                } else {
                    noOfCopies = workInstanceDocument.getItemDocumentList().size()
                            / singleItem.getItemReceivedTotalParts().intValue();
                    copy.setParts(new KualiInteger(singleItem.getItemReceivedTotalParts().intValue()));
                }

                copy.setLocationCopies(workInstanceDocument.getHoldingsDocument().getLocationName());
                copy.setItemCopies(new KualiDecimal(noOfCopies));
                copy.setPartEnumeration(enumeration.toString());
                copy.setStartingCopyNumber(new KualiInteger(startingCopy));
                copies.add(copy);
                // }
            }
        }
        return copies;
    }

    public boolean checkForEnumeration(StringBuffer enumeration) {
        if (null != enumeration && !(enumeration.equals("")) && !(enumeration.toString().equals("null"))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIsEnumerationSplitIsIntegerOrNot(String enumerationSplit) {
        try {
            int startingCopy = Integer.parseInt(enumerationSplit);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
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
//            instanceIdMap.put(DocType.BIB.getDescription(), instanceId);
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


    /**
     * This method is used to get the bibedtior creat url from propertie file
     *
     * @return Bibeditor creat url string
     */
    public String getBibeditorCreateURL() {
        String bibeditorCreateURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_CREATE_URL_KEY);
        return bibeditorCreateURL;
    }

    public String getBibSearchURL() {
        String bibSearchURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_SEARCH_URL_KEY);
        return bibSearchURL;
    }

    /**
     * This method is used to get the bibedtior edit url from propertie file
     *
     * @return Bibeditor edit url string
     */
    public String getBibeditorEditURL() {
        String bibeditorEditURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_URL_KEY);
        return bibeditorEditURL;
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
     * This method is used to get the bibedtior view url from propertie file
     *
     * @return Bibeditor view url string
     */
    public String getBibeditorViewURL() {
        String bibeditorViewURL = getConfigurationService().getPropertyValueAsString(OLEConstants.DOCSTORE_APP_URL_KEY);
        return bibeditorViewURL;
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
        String externaleDirectory = getFileProcessingService().getMarcXMLFileDirLocation();
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

    private void setItemDescription(OleLineItemReceivingItem singleItem) throws Exception {
        if (singleItem.getOleOrderRecord() != null) {
            Map<String, ?> bibAssociatedFieldValuesMap = singleItem.getOleOrderRecord().getOleBibRecord().getBibAssociatedFieldsValueMap();
            List titleList = (List) bibAssociatedFieldValuesMap.get("Title_search");
            String title = titleList != null && !titleList.isEmpty() ? (String) (titleList).get(0) : null;
            List authorList = (List) bibAssociatedFieldValuesMap.get("Author_search");
            String author = authorList != null && !authorList.isEmpty() ? (String) (authorList).get(0) : null;
            List publisherList = (List) bibAssociatedFieldValuesMap.get("Publisher_search");
            String publisher = publisherList != null && !publisherList.isEmpty() ? (String) (publisherList).get(0) : null;
            List isbnList = (List) bibAssociatedFieldValuesMap.get("020a");
            String isbn = isbnList != null && !isbnList.isEmpty() ? (String) (isbnList).get(0) : null;

            singleItem.setBibUUID(singleItem.getOleOrderRecord().getOleBibRecord().getBibUUID());
            singleItem.setItemDescription(title + "," + author + "," + publisher + "," + isbn);
        }
    }

    public boolean getIsATypeOfRCVGDoc() {
        return true;
    }

    public boolean getIsATypeOfCORRDoc() {
        return false;
    }

}
