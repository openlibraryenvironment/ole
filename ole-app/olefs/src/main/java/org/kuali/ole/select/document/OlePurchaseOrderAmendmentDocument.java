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
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.client.*;
import org.kuali.ole.docstore.common.client.impl.DocstoreClientLocatorServiceImpl;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.gl.service.SufficientFundsService;
import org.kuali.ole.module.purap.PurapWorkflowConstants;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.PurchaseOrderAmendmentDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.*;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.SufficientFundsItem;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.action.RoutingReportCriteria;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;

import java.math.BigDecimal;
import java.util.*;

public class OlePurchaseOrderAmendmentDocument extends PurchaseOrderAmendmentDocument {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurchaseOrderAmendmentDocument.class);

    private String vendorPoNumber;
    private static transient OlePatronRecordHandler olePatronRecordHandler;
    private static transient OlePatronDocumentList olePatronDocumentList;
    private static transient OleSelectDocumentService oleSelectDocumentService;
    private static transient OlePurapService olePurapService;
    private static transient OleCopyHelperService oleCopyHelperService;
    private String vendorAliasName;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public static OleSelectDocumentService getOleSelectDocumentService() {
        if (oleSelectDocumentService == null) {
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public static void setOleSelectDocumentService(OleSelectDocumentService oleSelectDocumentService) {
        OlePurchaseOrderAmendmentDocument.oleSelectDocumentService = oleSelectDocumentService;
    }

    public static OlePatronDocumentList getOlePatronDocumentList() {
        if (olePatronDocumentList == null) {
            olePatronDocumentList = SpringContext.getBean(OlePatronDocumentList.class);
        }
        return olePatronDocumentList;
    }

    public static void setOlePatronDocumentList(OlePatronDocumentList olePatronDocumentList) {
        OlePurchaseOrderAmendmentDocument.olePatronDocumentList = olePatronDocumentList;
    }


    public OlePatronRecordHandler getOlePatronRecordHandler() {
        if (null == olePatronRecordHandler) {
            olePatronRecordHandler = new OlePatronRecordHandler();
        }
        return olePatronRecordHandler;
    }

    public void setOlePatronRecordHandler(OlePatronRecordHandler olePatronRecordHandler) {
        this.olePatronRecordHandler = olePatronRecordHandler;
    }

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public static OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public static void setOlePurapService(OlePurapService olePurapService) {
        OlePurchaseOrderAmendmentDocument.olePurapService = olePurapService;
    }

    public static OleCopyHelperService getOleCopyHelperService() {
        if(oleCopyHelperService  == null){
            oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
        }
        return oleCopyHelperService;
    }

    public static void setOleCopyHelperService(OleCopyHelperService oleCopyHelperService) {
        OlePurchaseOrderAmendmentDocument.oleCopyHelperService = oleCopyHelperService;
    }

    @Override
    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {
        super.doRouteStatusChange(statusChangeEvent);
        if (getDocumentHeader().getWorkflowDocument().isProcessed()) {
            SpringContext.getBean(OlePurchaseOrderService.class).setStatusCompletePurchaseOrderAmendment(this);
        }
    }

    /**
     * This method is overridden to populate newly added ole fields from requisition into Ole Purchase Order Amendment Document.
     *
     * @see org.kuali.ole.module.purap.document.PurchaseOrderDocument#populatePurchaseOrderFromRequisition(org.kuali.ole.module.purap.document.RequisitionDocument)
     */
    @Override
    public void populatePurchaseOrderFromRequisition(RequisitionDocument requisitionDocument) {

        LOG.debug("Inside populatePurchaseOrderFromRequisition of OlePurchaseOrderAmendmentDocument");
        SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).populatePurchaseOrderFromRequisition(this, requisitionDocument);


    }

    /**
     * This method is overriden to populate bib info in Ole Purchase Order Amendment Document
     *
     * @see org.kuali.ole.module.purap.document.PurchaseOrderDocument#prepareForSave(org.kuali.rice.krad.rule.event.KualiDocumentEvent)
     */
    @Override
    public void prepareForSave(KualiDocumentEvent event) {

        LOG.debug("Inside prepareForSave of OlePurchaseOrderAmendmentDocument");
        //For single copy, if the itemLocation is changed, based on the user input from Location Change prompt, the itemLocation is updated in docstore's item record.
        OleDocstoreHelperService oleDocstoreHelperService = SpringContext
                .getBean(OleDocstoreHelperService.class);
        List<OlePurchaseOrderItem> purItem = this.getItems();
        for (int i = 0; purItem.size() > i; i++) {
            OlePurchaseOrderItem item = (OlePurchaseOrderItem) this.getItem(i);
            if (item.isItemLocationChangeFlag()) {
                oleDocstoreHelperService.updateItemLocation(this, item);
            }
        }
        SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).prepareForSave(this, event);
        super.prepareForSave(event);
    }

    /**
     * This method is overriden to populate bib info in Ole Purchase Order Amendment Document
     *
     * @see org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocumentBase#processAfterRetrieve()
     */
    @Override
    public void processAfterRetrieve() {
        LOG.debug("Inside processAfterRetrieve of OlePurchaseOrderAmendmentDocument");
        if (this.getVendorAliasName() == null) {
            populateVendorAliasName();
        }
        try {
            PurchaseOrderType purchaseOrderTypeDoc = getOlePurapService().getPurchaseOrderType(this.getPurchaseOrderTypeId());
            if(purchaseOrderTypeDoc != null){
                this.setOrderType(purchaseOrderTypeDoc);
            }
        } catch (Exception e) {
            LOG.error("Error in OlePurchaseOrderDocument:processAfterRetrieve for OleRequisitionItem " + e.getMessage());
            throw new RuntimeException(e);
        }
        try {
            List<OlePurchaseOrderItem> items = this.getItems();
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (object instanceof OlePurchaseOrderItem) {
                    OlePurchaseOrderItem singleItem = (OlePurchaseOrderItem) object;
                    if(singleItem.getItemTypeCode().equals(org.kuali.ole.OLEConstants.ITM_TYP_CODE)) {
                        setItemDetailWhileProcessAfterRetrive(singleItem);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in OlePurchaseOrderDocument:processAfterRetrieve for OlePurchaseOrderItem " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to set the item detail while processafterretrive method is executed
     * @param olePurchaseOrderItem
     */
    private void setItemDetailWhileProcessAfterRetrive(OlePurchaseOrderItem olePurchaseOrderItem)throws Exception{
        if (olePurchaseOrderItem.getRequestorId() != null) {
            olePurchaseOrderItem.setRequestorFirstName(getOlePurapService().getPatronName(olePurchaseOrderItem.getRequestorId()));
        }
        olePurchaseOrderItem.setPreviousItemQuantity(olePurchaseOrderItem.getItemQuantity());
        olePurchaseOrderItem.setPreviousItemNoOfParts(olePurchaseOrderItem.getItemNoOfParts());
        // Added for jira OLE-2811 ends
        if (olePurchaseOrderItem.getBibInfoBean() == null) {
            olePurchaseOrderItem.setBibInfoBean(new BibInfoBean());
        }
        if ((!StringUtils.isEmpty(olePurchaseOrderItem.getInternalRequestorId()) || !StringUtils.isEmpty(olePurchaseOrderItem
                .getRequestorId())) && olePurchaseOrderItem.getBibInfoBean().getRequestorType() == null) {
            String requestorType = OleSelectConstant.REQUESTOR_TYPE_STAFF;
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(OleSelectConstant.REQUESTOR_TYPE, requestorType);
            Collection<OleRequestorType> requestorTypeList = SpringContext.getBean(BusinessObjectService.class).findMatching(OleRequestorType.class, criteria);
            LOG.debug("###########inside prepareForSave getting requestor type###########");
            olePurchaseOrderItem.setRequestorTypeId(requestorTypeList.iterator().next().getRequestorTypeId());
        }
        if(olePurchaseOrderItem.getItemUnitPrice()!=null){
            olePurchaseOrderItem.setItemUnitPrice(olePurchaseOrderItem.getItemUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (olePurchaseOrderItem.getItemTitleId() != null) {
            LOG.debug("###########inside processAfterRetrieve OlePurchaseOrderAmendmentDocument ###########");
            Bib bib = new BibMarc();
            if (olePurchaseOrderItem.getItemTitleId() != null && olePurchaseOrderItem.getItemTitleId() != "") {
                bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(olePurchaseOrderItem.getItemTitleId());
                olePurchaseOrderItem.setBibUUID(bib.getId());
            }
            olePurchaseOrderItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePurchaseOrderItem.getItemTitleId()));
            olePurchaseOrderItem.setBibInfoBean(new BibInfoBean());
            olePurchaseOrderItem.setItemDescription(getOlePurapService().getItemDescription(bib));
        }
        if (olePurchaseOrderItem.getItemType() != null && olePurchaseOrderItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
            populateCopiesSection(olePurchaseOrderItem);
        }
        if(olePurchaseOrderItem.getCopyList().size() > 0 && olePurchaseOrderItem.getItemTitleId()!=null) {
            //getOlePurapService().setInvoiceDocumentsForPO(olePurchaseOrderItem);
            getOlePurapService().setInvoiceDocumentsForPO(this,olePurchaseOrderItem);
        }
    }

    /**
     * This method is used to populated the copies section details
     * @param singleItem
     */
    private void populateCopiesSection(OlePurchaseOrderItem singleItem) {
        if (singleItem.getCopies().size() > 0) {
            List<OleCopy> copyList = getOleCopyHelperService().setCopyValuesForList(singleItem.getCopies(), singleItem.getItemTitleId(), null, null);
            if (copyList.size() >= singleItem.getCopyList().size()) {
                int copyCount = 0;
                for (OleCopy oleCopy : singleItem.getCopyList()) {
                    OleCopy copy = copyList.get(copyCount);
                    oleCopy.setLocation(copy.getLocation());
                    //oleCopy.setEnumeration(copy.getEnumeration());
                    //oleCopy.setCopyNumber(copy.getCopyNumber());
                    oleCopy.setPartNumber(copy.getPartNumber());
                    copyCount++;
                }
                for(int i = copyCount ; i<copyList.size() ; i++){
                    singleItem.getCopyList().add(copyList.get(copyCount));
                    copyCount++;
                }
            }
        }
        else {
            if (singleItem.getItemQuantity() != null && singleItem.getItemNoOfParts() != null && (singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                    || singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1)))) {
                singleItem.setCopies(getOleCopyHelperService().setCopiesToLineItem(singleItem.getCopyList(), singleItem.getItemNoOfParts(), singleItem.getItemTitleId()));
            }
        }
        if (singleItem.getItemQuantity() != null && singleItem.getItemNoOfParts() != null && !singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                && !singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))&&singleItem.getCopyList().size()>0) {
            singleItem.setSingleCopyNumber(singleItem.getCopyList().get(0).getCopyNumber());
        }
    }

    // Added for Jira OLE-1900 Starts

    /**
     * This method will set copies into list of copies for LineItem.
     *
     * @param singleItem
     * @param workBibDocument
     * @return
     */
    public List<OleCopies> setCopiesToLineItem(OlePurchaseOrderItem singleItem, WorkBibDocument workBibDocument) {
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
            if (singleItem.getItemNoOfParts().intValue() != 0 && null != enumeration) {
                String enumerationSplit = enumeration.substring(1, 2);
                boolean isint = checkIsEnumerationSplitIsIntegerOrNot(enumerationSplit);
                if (isint) {
                    startingCopy = Integer.parseInt(enumerationSplit);
                }
            }
            if (singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                    || singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
                boolean isValid = checkForEnumeration(enumeration);
                if (isValid) {
                    int noOfCopies = workInstanceDocument.getItemDocumentList().size()
                            / singleItem.getItemNoOfParts().intValue();
                    OleRequisitionCopies copy = new OleRequisitionCopies();
                    copy.setParts(singleItem.getItemNoOfParts());
                    copy.setLocationCopies(workInstanceDocument.getHoldingsDocument().getLocationName());
                    copy.setItemCopies(new KualiDecimal(noOfCopies));
                    copy.setPartEnumeration(enumeration.toString());
                    copy.setStartingCopyNumber(new KualiInteger(startingCopy));
                    copies.add(copy);
                }
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

    @Override
    public List getItemsActiveOnly() {
        LOG.debug("Inside getItemsActiveOnly of OlePurchaseOrderReopenDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getItemsActiveOnly(this);
    }

    /**
     * Gets the active items in this Purchase Order, and sets up the alternate amount for GL entry creation.
     *
     * @return the list of all active items in this Purchase Order.
     */
    @Override
    public List getItemsActiveOnlySetupAlternateAmount() {
        LOG.debug("Inside getItemsActiveOnlySetupAlternateAmount of OlePurchaseOrderReopenDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getItemsActiveOnlySetupAlternateAmount(this);
    }

    @Override
    public boolean getAdditionalChargesExist() {
        LOG.debug("Inside getAdditionalChargesExist of OlePurchaseOrderReopenDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getAdditionalChargesExist(this);
    }

    /**
     * This method returns if Purchase Order Document created is in Final Status
     *
     * @return
     */
    public boolean getIsFinalReqs() {
        LOG.debug("Inside getIsFinalReqs of OlePurchaseOrderAmendmentDocument");

        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getIsFinalReqs(this);
    }

    public boolean getIsSplitPO() {
        LOG.debug("Inside getIsSplitPO of OlePurchaseOrderAmendmentDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getIsSplitPO(this);
    }

    public boolean getIsReOpenPO() {
        LOG.debug("Inside getIsReOpenPO of OlePurchaseOrderAmendmentDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getIsReOpenPO(this);
    }

    /**
     * This method is used to get the bibedtior creat url from propertie file
     *
     * @return Bibeditor creat url string
     */
    public String getBibeditorCreateURL() {
        LOG.debug("Inside getBibeditorCreateURL of OlePurchaseOrderAmendmentDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getBibeditorCreateURL();
    }

    public String getBibSearchURL() {
        LOG.debug("Inside getBibSearchURL of OlePurchaseOrderAmendmentDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getBibSearchURL();
    }

    /**
     * This method is used to get the bibedtior edit url from propertie file
     *
     * @return Bibeditor edit url string
     */
    public String getBibeditorEditURL() {
        LOG.debug("Inside getBibeditorEditURL of OlePurchaseOrderAmendmentDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getBibeditorEditURL();
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
        LOG.debug("Inside getMarcXMLFileDirLocation of OlePurchaseOrderAmendmentDocument");
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getMarcXMLFileDirLocation();
    }

    @Override
    public Class getItemClass() {
        return OlePurchaseOrderItem.class;
    }


    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(PurapWorkflowConstants.HAS_NEW_UNORDERED_ITEMS)) {
            return isNewUnorderedItem();
        }
        else if (nodeName.equals(PurapWorkflowConstants.CONTRACT_MANAGEMENT_REVIEW_REQUIRED)) {
            return isContractManagementReviewRequired();
        }
        else if (nodeName.equals(PurapWorkflowConstants.AWARD_REVIEW_REQUIRED)) {
            return isAwardReviewRequired();
        }
        else if (nodeName.equals(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED)) {
            return isBudgetReviewRequired();
        }
        else if (nodeName.equals(PurapWorkflowConstants.VENDOR_IS_EMPLOYEE_OR_NON_RESIDENT_ALIEN)) {
            return isVendorEmployeeOrNonResidentAlien();
        }

        else if (nodeName.equals(PurapWorkflowConstants.NOTIFY_BUDGET_REVIEW)) {
            return isNotificationRequired();
        }
        throw new UnsupportedOperationException("Cannot answer split question for this node you call \"" + nodeName + "\"");
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

    public boolean getIsATypeOfRCVGDoc() {
        return false;
    }

    public boolean getIsATypeOfCORRDoc() {
        return false;
    }


    @Override
    public void doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) {
        super.doRouteLevelChange(levelChangeEvent);
        try {
            String newNodeName = levelChangeEvent.getNewNodeName();
            String documentId = this.getDocumentNumber();
            if (!OLEConstants.PO_NOTE_MAP.containsKey(documentId)
                    && newNodeName != null
                    && (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || newNodeName
                    .equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET))) {
                addPurchaseOrderNote(levelChangeEvent);
                OLEConstants.PO_NOTE_MAP.put(documentId, true);

            }
        } catch (Exception e) {
            String errorMsg = "Workflow Error found checking actions requests on document with id "
                    + getDocumentNumber() + ". *** WILL NOT UPDATE PURAP STATUS ***";
            LOG.error(errorMsg, e);
        }
    }

    public void addPurchaseOrderNote(DocumentRouteLevelChange levelChangeEvent) {

        String newNodeName = levelChangeEvent.getNewNodeName();
        RoutingReportCriteria.Builder reportCriteria = RoutingReportCriteria.Builder
                .createByDocumentIdAndTargetNodeName(getDocumentNumber(), newNodeName);
        if (newNodeName != null
                && (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || newNodeName
                .equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET))) {
            String note = "";
            if(newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE)){
                 note = OLEConstants.SufficientFundCheck.POA_NOTE;
            }
            if(newNodeName.equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET)){
                note = OLEConstants.SufficientFundCheck.FYI_NOTE;
            }
            DocumentService documentService = SpringContext.getBean(DocumentService.class);
            Note apoNote = documentService.createNoteFromDocument(this, note);
            this.addNote(apoNote);
            String documentTypeName = this.getDocumentHeader().getWorkflowDocument().getDocumentTypeName();
            if (documentTypeName != null
                    && documentTypeName.equals(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_AMENDMENT)) {
                documentService.saveDocumentNotes(this);
            }

        }

    }

    /**
     * Method overridden for Fund Check Makes sure that accounts for routing has been generated, so that other information can be
     * retrieved from that
     */
    @Override
    protected void populateAccountsForRouting() {
        List<SufficientFundsItem> fundsItems = new ArrayList<SufficientFundsItem>();
        String documentId = this.getDocumentNumber();
        try {
            String nodeName = getFinancialSystemDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator()
                    .next();
            if (nodeName != null
                    && (nodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || nodeName
                    .equalsIgnoreCase(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED))) {
                if (SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear()
                        .compareTo(getPostingYear()) >= 0) {
                   // List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
                    /*for (GeneralLedgerPendingEntry glpe : pendingEntries) {
                        glpe.getChartOfAccountsCode();
                    }*/
                    /*SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());
                    fundsItems = SpringContext.getBean(SufficientFundsService.class).checkSufficientFunds(
                            pendingEntries);*/
                 //   SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(
                  //          this);
                 //   SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());
                }
                SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(this);
                accountsForRouting = (SpringContext.getBean(PurapAccountingService.class).generateSummary(getItems()));
                /*String documentFiscalYearString = this.getPostingYear().toString();
                List<String> fundsItemList = new ArrayList<String>();
                List<String> accountsList = new ArrayList<String>();
                for (SufficientFundsItem fundsItem : fundsItems) {
                    fundsItemList.add(fundsItem.getAccount().getChartOfAccountsCode());
                }*/
                setAccountsForRouting(accountsForRouting);
                refreshNonUpdateableReferences();
                for (SourceAccountingLine sourceLine : getAccountsForRouting()) {
                    sourceLine.refreshNonUpdateableReferences();
                }
            } else {
                super.populateAccountsForRouting();
            }
        } catch (Exception e) {
            logAndThrowRuntimeException("Error in populateAccountsForRouting while submitting document with id "
                    + getDocumentNumber(), e);
        }
    }


    @Override
    protected boolean isBudgetReviewRequired() {
        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        boolean sufficientFundCheck = false;
        List<SourceAccountingLine> sourceAccountingLineList = this.getSourceAccountingLines();
        for (SourceAccountingLine accLine : sourceAccountingLineList) {
            Map searchMap = new HashMap();
            String notificationOption = null;
            Map<String, Object> key = new HashMap<String, Object>();
            String chartCode = accLine.getChartOfAccountsCode();
            String accNo = accLine.getAccountNumber();
            String objectCd = accLine.getFinancialObjectCode();
            key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
            key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
            OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                    OleSufficientFundCheck.class, key);
            if (account != null) {
                notificationOption = account.getNotificationOption();
            }
            if (notificationOption != null
                    && (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW))) {
                sufficientFundCheck = oleRequisitionDocumentService.hasSufficientFundsOnRequisition(accLine, notificationOption, SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear());
                if (sufficientFundCheck) {
                    return true;
                }
            }
        }
        return sufficientFundCheck;
    }

    private boolean isNotificationRequired() {
        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        List<SourceAccountingLine> sourceAccountingLineList = this.getSourceAccountingLines();
        boolean sufficientFundCheck = false;
        for (SourceAccountingLine accLine : sourceAccountingLineList) {
            Map searchMap = new HashMap();
            String notificationOption = null;
            Map<String, Object> key = new HashMap<String, Object>();
            String chartCode = accLine.getChartOfAccountsCode();
            String accNo = accLine.getAccountNumber();
            String objectCd = accLine.getFinancialObjectCode();
            key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
            key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
            OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                    OleSufficientFundCheck.class, key);
            if (account != null) {
                notificationOption = account.getNotificationOption();
            }
            if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                sufficientFundCheck =  oleRequisitionDocumentService.hasSufficientFundsOnRequisition(accLine, notificationOption, SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear());
                if (sufficientFundCheck) {
                    return true;
                }
            }
        }
        return sufficientFundCheck;
    }

    private void populateVendorAliasName() {
        Map vendorDetailMap = new HashMap();
        vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, this.getVendorHeaderGeneratedIdentifier());
        vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, this.getVendorDetailAssignedIdentifier());
        List<VendorAlias> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorAlias.class, vendorDetailMap);
        if (vendorDetailList != null && vendorDetailList.size() > 0) {
            this.setVendorAliasName(vendorDetailList.get(0).getVendorAliasName());
        }
    }

}
