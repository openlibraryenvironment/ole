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

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.gl.service.SufficientFundsService;
import org.kuali.ole.integration.purap.CapitalAssetSystem;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.PurapConstants.RequisitionSources;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapWorkflowConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.RequisitionService;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.SufficientFundsItem;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import java.math.BigDecimal;
import java.util.*;

public class OlePurchaseOrderDocument extends PurchaseOrderDocument {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurchaseOrderDocument.class);
    /*    public void populatePurchaseOrderFromRequisition(RequisitionDocument requisitionDocument) {
            super.populatePurchaseOrderFromRequisition(requisitionDocument);
        }*/
    private String vendorPoNumber;

    private static transient ConfigurationService kualiConfigurationService;
    private static transient BibInfoWrapperService bibInfoWrapperService;
    private static transient DateTimeService dateTimeService;
    private static transient FileProcessingService fileProcessingService;
    private static transient BusinessObjectService businessObjectService;
    private static transient ParameterService parameterService;
    private static transient RequisitionService RequisitionService;
    private static transient OlePurchaseOrderDocumentHelperService olePurchaseOrderDocumentHelperService;
    private static transient DocumentService documentService;
    protected List<SourceAccountingLine> accountsForRouting = new ArrayList<SourceAccountingLine>();
    private static transient BibInfoService bibInfoService;
    private static transient OlePatronRecordHandler olePatronRecordHandler;
    private static transient OlePurapService olePurapService;
    private static transient OleCopyHelperService oleCopyHelperService;
    protected List<OlePurchaseOrderLineForInvoice> olePurchaseOrderLineForInvoiceList = new ArrayList<OlePurchaseOrderLineForInvoice>();
    protected List<OlePurchaseOrderTotal> purchaseOrderTotalList = new ArrayList<OlePurchaseOrderTotal>();
    private Date poEndDate;
    private boolean closePO;
    private String poNotes;
    private String poItemLink;
    private DocstoreClientLocator docstoreClientLocator;
    private OleSelectDocumentService oleSelectDocumentService;
    private List<OlePurchaseOrderItem> olePurchaseOrderItemList;

    protected static DocumentService getDocumentService() {
        if (documentService == null) {
            documentService = SpringContext.getBean(DocumentService.class);
        }
        return documentService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public static OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public static OlePurchaseOrderDocumentHelperService getOlePurchaseOrderDocumentHelperService() {
        if (olePurchaseOrderDocumentHelperService == null) {
            olePurchaseOrderDocumentHelperService = SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class);
        }
        return olePurchaseOrderDocumentHelperService;
    }

    public static void setOlePurchaseOrderDocumentHelperService(OlePurchaseOrderDocumentHelperService olePurchaseOrderDocumentHelperService) {
        OlePurchaseOrderDocument.olePurchaseOrderDocumentHelperService = olePurchaseOrderDocumentHelperService;
    }

    @Override
    public ParameterService getParameterService() {
        if (parameterService == null) {
            parameterService = SpringContext.getBean(ParameterService.class);
        }
        return parameterService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    public static RequisitionService getRequisitionService() {
        if (RequisitionService == null) {
            RequisitionService = SpringContext.getBean(RequisitionService.class);
        }
        return RequisitionService;
    }

    public static void setRequisitionService(RequisitionService RequisitionService) {
        OlePurchaseOrderDocument.RequisitionService = RequisitionService;
    }

    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public static void setConfigurationService(ConfigurationService kualiConfigurationService) {
        OlePurchaseOrderDocument.kualiConfigurationService = kualiConfigurationService;
    }

    public static BibInfoWrapperService getBibInfoWrapperService() {
        if (bibInfoWrapperService == null) {
            bibInfoWrapperService = SpringContext.getBean(BibInfoWrapperService.class);
        }
        return bibInfoWrapperService;
    }

    public static void setBibInfoWrapperService(BibInfoWrapperService bibInfoWrapperService) {
        OlePurchaseOrderDocument.bibInfoWrapperService = bibInfoWrapperService;
    }

    public static FileProcessingService getFileProcessingService() {
        if (fileProcessingService == null) {
            fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        }
        return fileProcessingService;
    }

    public static void setFileProcessingService(FileProcessingService fileProcessingService) {
        OlePurchaseOrderDocument.fileProcessingService = fileProcessingService;
    }

    public static DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }

    public static void setDateTimeService(DateTimeService dateTimeService) {
        OlePurchaseOrderDocument.dateTimeService = dateTimeService;
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

    public static BibInfoService getBibInfoService() {
        if (bibInfoService == null) {
            bibInfoService = SpringContext.getBean(BibInfoServiceImpl.class);
        }
        return bibInfoService;
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

    public static OleCopyHelperService getOleCopyHelperService() {
        if(oleCopyHelperService  == null){
            oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
        }
        return oleCopyHelperService;
    }

    public static void setOleCopyHelperService(OleCopyHelperService oleCopyHelperService) {
        OlePurchaseOrderDocument.oleCopyHelperService = oleCopyHelperService;
    }

    public List<OlePurchaseOrderItem> getOlePurchaseOrderItemList() {
        return olePurchaseOrderItemList;
    }

    public void setOlePurchaseOrderItemList(List<OlePurchaseOrderItem> olePurchaseOrderItemList) {
        this.olePurchaseOrderItemList = olePurchaseOrderItemList;
    }

    @Override
    public List<OlePurchaseOrderItem> getItemsActiveOnly() {
        List<OlePurchaseOrderItem> returnList = new ArrayList<OlePurchaseOrderItem>();
        for (Iterator iter = getItems().iterator(); iter.hasNext(); ) {
            OlePurchaseOrderItem item = (OlePurchaseOrderItem) iter.next();
            if (item.isItemActiveIndicator()) {
                returnList.add(item);
            }
        }
        return returnList;
    }

    /**
     * Gets the active items in this Purchase Order, and sets up the alternate amount for GL entry creation.
     *
     * @return the list of all active items in this Purchase Order.
     */
    public List changeItemsActiveOnlySetupAlternateAmount() {
        List returnList = new ArrayList();
        for (Iterator iter = getItems().iterator(); iter.hasNext(); ) {
            OlePurchaseOrderItem item = (OlePurchaseOrderItem) iter.next();
            if (item.isItemActiveIndicator()) {
                for (Object element : item.getSourceAccountingLines()) {
                    PurchaseOrderAccount account = (PurchaseOrderAccount) element;
                    account.setAlternateAmountForGLEntryCreation(account.getItemAccountOutstandingEncumbranceAmount());
                }
                returnList.add(item);
            }
        }
        return returnList;
    }

    @Override
    public boolean getAdditionalChargesExist() {
        List<OlePurchaseOrderItem> items = this.getItems();
        for (OlePurchaseOrderItem item : items) {
            if ((item != null) &&
                    (item.getItemType() != null) &&
                    (item.getItemType().isAdditionalChargeIndicator()) &&
                    (item.getExtendedPrice() != null) &&
                    (!KualiDecimal.ZERO.equals(item.getExtendedPrice()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void populatePurchaseOrderFromRequisition(RequisitionDocument requisitionDoc) {
        OleRequisitionDocument requisitionDocument = (OleRequisitionDocument) requisitionDoc;
        this.setPurchaseOrderCreateTimestamp(getDateTimeService().getCurrentTimestamp());
        this.getDocumentHeader().setOrganizationDocumentNumber(requisitionDocument.getDocumentHeader().getOrganizationDocumentNumber());
        this.getDocumentHeader().setDocumentDescription(requisitionDocument.getDocumentHeader().getDocumentDescription());
        this.getDocumentHeader().setExplanation(requisitionDocument.getDocumentHeader().getExplanation());
        this.setBillingName(requisitionDocument.getBillingName());
        this.setBillingLine1Address(requisitionDocument.getBillingLine1Address());
        this.setBillingLine2Address(requisitionDocument.getBillingLine2Address());
        this.setBillingCityName(requisitionDocument.getBillingCityName());
        this.setBillingStateCode(requisitionDocument.getBillingStateCode());
        this.setBillingPostalCode(requisitionDocument.getBillingPostalCode());
        this.setBillingCountryCode(requisitionDocument.getBillingCountryCode());
        this.setBillingPhoneNumber(requisitionDocument.getBillingPhoneNumber());
        this.setVendorAliasName(requisitionDocument.getVendorAliasName());
        this.setReceivingName(requisitionDocument.getReceivingName());
        this.setReceivingCityName(requisitionDocument.getReceivingCityName());
        this.setReceivingLine1Address(requisitionDocument.getReceivingLine1Address());
        this.setReceivingLine2Address(requisitionDocument.getReceivingLine2Address());
        this.setReceivingStateCode(requisitionDocument.getReceivingStateCode());
        this.setReceivingPostalCode(requisitionDocument.getReceivingPostalCode());
        this.setReceivingCountryCode(requisitionDocument.getReceivingCountryCode());
        this.setAddressToVendorIndicator(requisitionDocument.getAddressToVendorIndicator());
        this.setVendorPoNumber(requisitionDocument.getVendorPoNumber());
        this.setDeliveryBuildingCode(requisitionDocument.getDeliveryBuildingCode());
        this.setDeliveryBuildingRoomNumber(requisitionDocument.getDeliveryBuildingRoomNumber());
        this.setDeliveryBuildingName(requisitionDocument.getDeliveryBuildingName());
        this.setDeliveryCampusCode(requisitionDocument.getDeliveryCampusCode());
        this.setDeliveryCityName(requisitionDocument.getDeliveryCityName());
        this.setDeliveryCountryCode(requisitionDocument.getDeliveryCountryCode());
        this.setDeliveryInstructionText(requisitionDocument.getDeliveryInstructionText());
        this.setDeliveryBuildingLine1Address(requisitionDocument.getDeliveryBuildingLine1Address());
        this.setDeliveryBuildingLine2Address(requisitionDocument.getDeliveryBuildingLine2Address());
        this.setDeliveryPostalCode(requisitionDocument.getDeliveryPostalCode());
        this.setDeliveryRequiredDate(requisitionDocument.getDeliveryRequiredDate());
        this.setDeliveryRequiredDateReasonCode(requisitionDocument.getDeliveryRequiredDateReasonCode());
        this.setDeliveryStateCode(requisitionDocument.getDeliveryStateCode());
        this.setDeliveryToEmailAddress(requisitionDocument.getDeliveryToEmailAddress());
        this.setDeliveryToName(requisitionDocument.getDeliveryToName());
        this.setDeliveryToPhoneNumber(requisitionDocument.getDeliveryToPhoneNumber());
        this.setDeliveryBuildingOtherIndicator(requisitionDocument.isDeliveryBuildingOtherIndicator());
        //this.setPurchaseOrderTypeId(requisitionDocument.getPurchaseOrderTypeId());
        this.setPurchaseOrderBeginDate(requisitionDocument.getPurchaseOrderBeginDate());
        this.setPurchaseOrderCostSourceCode(requisitionDocument.getPurchaseOrderCostSourceCode());
        this.setPostingYear(requisitionDocument.getPostingYear());
        this.setPurchaseOrderEndDate(requisitionDocument.getPurchaseOrderEndDate());
        this.setChartOfAccountsCode(requisitionDocument.getChartOfAccountsCode());
        this.setDocumentFundingSourceCode(requisitionDocument.getDocumentFundingSourceCode());
        this.setInstitutionContactEmailAddress(requisitionDocument.getInstitutionContactEmailAddress());
        this.setInstitutionContactName(requisitionDocument.getInstitutionContactName());
        this.setInstitutionContactPhoneNumber(requisitionDocument.getInstitutionContactPhoneNumber());
        this.setNonInstitutionFundAccountNumber(requisitionDocument.getNonInstitutionFundAccountNumber());
        this.setNonInstitutionFundChartOfAccountsCode(requisitionDocument.getNonInstitutionFundChartOfAccountsCode());
        this.setNonInstitutionFundOrgChartOfAccountsCode(requisitionDocument.getNonInstitutionFundOrgChartOfAccountsCode());
        this.setNonInstitutionFundOrganizationCode(requisitionDocument.getNonInstitutionFundOrganizationCode());
        this.setOrganizationCode(requisitionDocument.getOrganizationCode());
        this.setRecurringPaymentTypeCode(requisitionDocument.getRecurringPaymentTypeCode());
        this.setRequestorPersonEmailAddress(requisitionDocument.getRequestorPersonEmailAddress());
        this.setRequestorPersonName(requisitionDocument.getRequestorPersonName());
        this.setRequestorPersonPhoneNumber(requisitionDocument.getRequestorPersonPhoneNumber());
        this.setRequisitionIdentifier(requisitionDocument.getPurapDocumentIdentifier());
        this.setPurchaseOrderTotalLimit(requisitionDocument.getPurchaseOrderTotalLimit());
        this.setPurchaseOrderTransmissionMethodCode(requisitionDocument.getPurchaseOrderTransmissionMethodCode());
        this.setUseTaxIndicator(requisitionDocument.isUseTaxIndicator());
        this.setPurchaseOrderTypeId(requisitionDocument.getPurchaseOrderTypeId());
        this.setVendorCityName(requisitionDocument.getVendorCityName());
        this.setVendorContractGeneratedIdentifier(requisitionDocument.getVendorContractGeneratedIdentifier());
        this.setVendorCountryCode(requisitionDocument.getVendorCountryCode());
        this.setVendorCustomerNumber(requisitionDocument.getVendorCustomerNumber());
        this.setVendorAttentionName(requisitionDocument.getVendorAttentionName());
        this.setVendorDetailAssignedIdentifier(requisitionDocument.getVendorDetailAssignedIdentifier());
        this.setVendorFaxNumber(requisitionDocument.getVendorFaxNumber());
        this.setVendorHeaderGeneratedIdentifier(requisitionDocument.getVendorHeaderGeneratedIdentifier());
        this.setVendorLine1Address(requisitionDocument.getVendorLine1Address());
        this.setVendorLine2Address(requisitionDocument.getVendorLine2Address());
        this.setVendorAddressInternationalProvinceName(requisitionDocument.getVendorAddressInternationalProvinceName());
        this.setVendorName(requisitionDocument.getVendorName());
        this.setVendorNoteText(requisitionDocument.getVendorNoteText());
        this.setVendorPhoneNumber(requisitionDocument.getVendorPhoneNumber());
        this.setVendorPostalCode(requisitionDocument.getVendorPostalCode());
        this.setVendorStateCode(requisitionDocument.getVendorStateCode());
        this.setVendorRestrictedIndicator(requisitionDocument.getVendorRestrictedIndicator());

        this.setExternalOrganizationB2bSupplierIdentifier(requisitionDocument.getExternalOrganizationB2bSupplierIdentifier());
        this.setRequisitionSourceCode(requisitionDocument.getRequisitionSourceCode());
        this.setAccountsPayablePurchasingDocumentLinkIdentifier(requisitionDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
        this.setReceivingDocumentRequiredIndicator(requisitionDocument.isReceivingDocumentRequiredIndicator());
        this.setPaymentRequestPositiveApprovalIndicator(requisitionDocument.isPaymentRequestPositiveApprovalIndicator());

        this.setStatusCode(PurapConstants.PurchaseOrderStatuses.APPDOC_IN_PROCESS);

        // Copy items from requisition (which will copy the item's accounts and capital assets)
        List<OlePurchaseOrderItem> items = new ArrayList();
        for (PurApItem reqItem : ((PurchasingAccountsPayableDocument) requisitionDocument).getItems()) {
            RequisitionCapitalAssetItem reqCamsItem = (RequisitionCapitalAssetItem) requisitionDocument.getPurchasingCapitalAssetItemByItemIdentifier(reqItem.getItemIdentifier().intValue());
            //items.add(new OlePurchaseOrderItem((OleRequisitionItem)reqItem, this, reqCamsItem));
            items.add(new OlePurchaseOrderItem((OleRequisitionItem) reqItem, this, reqCamsItem));
        }
        this.setItems(items);

        // Copy capital asset information that is directly off the document.
        this.setCapitalAssetSystemTypeCode(requisitionDocument.getCapitalAssetSystemTypeCode());
        this.setCapitalAssetSystemStateCode(requisitionDocument.getCapitalAssetSystemStateCode());
        for (CapitalAssetSystem capitalAssetSystem : requisitionDocument.getPurchasingCapitalAssetSystems()) {
            this.getPurchasingCapitalAssetSystems().add(new PurchaseOrderCapitalAssetSystem(capitalAssetSystem));
        }

        this.fixItemReferences();
    }

    @Override
    public void prepareForSave(KualiDocumentEvent event) {
        try {
            List<OlePurchaseOrderItem> items = this.getItems();
            for (OlePurchaseOrderItem singleItem : items) {
                if (singleItem instanceof OlePurchaseOrderItem) {
                    if(!this.getRequisitionSourceCode().equalsIgnoreCase(
                            OleSelectConstant.REQUISITON_SRC_TYPE_DIRECTINPUT)  && !this.getRequisitionSourceCode().equalsIgnoreCase(
                            OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST)){
                        singleItem.setVendorItemPoNumber(this.getVendorPoNumber());
                    }
                    if (singleItem.getBibInfoBean() != null) {
                        if (!StringUtils.isEmpty(singleItem.getInternalRequestorId()) && singleItem.getBibInfoBean().getRequestorType() == null) {
                            singleItem.setRequestorTypeId(getOlePurapService().getRequestorTypeId(OleSelectConstant.REQUESTOR_TYPE_STAFF));
                        }
                    }
                }
            }
            if (event instanceof org.kuali.rice.krad.rules.rule.event.RouteDocumentEvent && this.getRequisitionIdentifier() == null) {
                routePO();
            }
        } catch (Exception e) {
            LOG.error("Exception in OlePurchaseOrderDocument:prepareForSave: " + e.getMessage());
            throw new RuntimeException("Error in OlePurchaseOrderDocument:prepareForSave: " + e.getMessage());
        }
        super.prepareForSave(event);
    }

    /**
     *
     */
    private void routePO(){
        String currentUser = GlobalVariables.getUserSession().getPrincipalName();
        try {
            UserSession userSession = GlobalVariables.getUserSession();
            // TODO: need to configure a user/role
            GlobalVariables.setUserSession(new UserSession(getOleSelectDocumentService().getSelectParameterValue(OLEConstants.SYSTEM_USER)));
            RequisitionDocument requisitionDocument = getRequisitionService().generateRequisitionFromPurchaseOrder(this);
            // rdoc.setAccountsPayablePurchasingDocumentLinkIdentifier(this.getDocumentNumber());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requisition Document Status Code---->" + requisitionDocument.getApplicationDocumentStatus());
            }
            requisitionDocument.setRequisitionSourceCode(OLEConstants.REQ_SRC_CD);
            List adHocRouteRecipientList = new ArrayList();
            adHocRouteRecipientList.addAll(this.getAdHocRoutePersons());
            adHocRouteRecipientList.addAll(this.getAdHocRouteWorkgroups());

            getDocumentService().blanketApproveDocument(requisitionDocument, "Automatic Approval From PO", adHocRouteRecipientList);
            GlobalVariables.setUserSession(userSession);
            this.setAccountsPayablePurchasingDocumentLinkIdentifier(requisitionDocument.getPurapDocumentIdentifier());
            this.setRequisitionIdentifier(requisitionDocument.getPurapDocumentIdentifier());
        } catch (WorkflowException e) {
            LOG.error("Error in OlePurchaseOrderDocument:prepareForSave: " + e.getMessage());
            throw new RuntimeException("Error in OlePurchaseOrderDocument:prepareForSave: " + e.getMessage());
        } finally {
            GlobalVariables.setUserSession(new UserSession(currentUser));
        }
        this.setStatusCode(PurchaseOrderStatuses.APPDOC_IN_PROCESS);
    }
    @Override
    public void processAfterRetrieve() {
        try {
            PurchaseOrderType purchaseOrderTypeDoc = getOlePurapService().getPurchaseOrderType(this.getPurchaseOrderTypeId());
            if(purchaseOrderTypeDoc != null){
                this.setOrderType(purchaseOrderTypeDoc);
            }
            if (this.getVendorAliasName() == null) {
                populateVendorAliasName();
            }
            List<OlePurchaseOrderItem> items = this.getItems();
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                try{
                    OlePurchaseOrderItem singleItem = (OlePurchaseOrderItem) iterator.next();
                    // Added for jira OLE-2811 starts
                    //Modified for jiar OLE-6032
                    if(singleItem.getItemTypeCode().equals(org.kuali.ole.OLEConstants.ITM_TYP_CODE)) {
                        setItemDetailWhileProcessAfterRetrive(singleItem);
                    }
                }catch(Exception e){
                    LOG.error("Exception in OlePurchaseOrderDocument:processAfterRetrieve --Exception while processing receiving purchasing record--->"+e);
                    e.printStackTrace();
                }
            }//End of Iteration of Purchase order while loop
        } catch (Exception e) {
            LOG.error("Error in OlePurchaseOrderDocument:processAfterRetrieve for OlePurchaseOrderItem " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to set the item detail while processafterretrive method is executed
     * @param olePurchaseOrderItem
     */
    private void setItemDetailWhileProcessAfterRetrive(OlePurchaseOrderItem olePurchaseOrderItem)throws Exception{
        if(olePurchaseOrderItem.getRequestorId()!=null){
            olePurchaseOrderItem.setRequestorFirstName(getOlePurapService().getPatronName(olePurchaseOrderItem.getRequestorId()));
        }
        if(olePurchaseOrderItem.getItemUnitPrice()!=null){
            olePurchaseOrderItem.setItemUnitPrice(olePurchaseOrderItem.getItemUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (olePurchaseOrderItem.getItemTitleId() != null) {
            olePurchaseOrderItem.setBibInfoBean(new BibInfoBean());
            if (olePurchaseOrderItem.getItemTitleId() != null && olePurchaseOrderItem.getItemTitleId() != "") {
                Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(olePurchaseOrderItem.getItemTitleId());
                olePurchaseOrderItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePurchaseOrderItem.getItemTitleId()));
                if (bib != null) {
                    olePurchaseOrderItem.setItemDescription(getOlePurapService().getItemDescription(bib));
                    //setItemDescription(olePurchaseOrderItem,bib);
                    olePurchaseOrderItem.setBibUUID(bib.getId());
                }
            }
        }
        if (olePurchaseOrderItem.getItemType() != null && olePurchaseOrderItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
            populateCopiesSection(olePurchaseOrderItem);
        }
        if(olePurchaseOrderItem.getClaimDate()==null){
            getOlePurapService().setClaimDateForPO(olePurchaseOrderItem,this.vendorDetail);
        }
        if(olePurchaseOrderItem.getCopyList().size() > 0 && olePurchaseOrderItem.getItemTitleId()!=null && olePurchaseOrderItem.getInvoiceDocuments().size()==0) {
            //getOlePurapService().setInvoiceDocumentsForPO(olePurchaseOrderItem);
            getOlePurapService().setInvoiceDocumentsForPO(this,olePurchaseOrderItem);
        }
    }

    /**
     * This method is used to set the item description on item for the given Bib
     * @param olePurchaseOrderItem
     * @param newBib
     */
/*    private void setItemDescription(OlePurchaseOrderItem olePurchaseOrderItem, Bib newBib){
        LOG.debug("### Inside setItemDescription() of OleRequisitionDocument ###");
        String itemDescription = ((newBib.getTitle() != null && !newBib.getTitle().isEmpty()) ? newBib.getTitle() + "," : "") + ((newBib.getAuthor() != null && !newBib.getAuthor().isEmpty()) ? newBib.getAuthor() + "," : "") + ((newBib.getPublisher() != null && !newBib.getPublisher().isEmpty()) ? newBib.getPublisher() + "," : "") + ((newBib.getIsbn() != null && !newBib.getIsbn().isEmpty()) ? newBib.getIsbn() + "," : "");
        itemDescription = itemDescription.substring(0, itemDescription.lastIndexOf(","));
        if(LOG.isDebugEnabled()){
            LOG.debug("Item Description---------->"+itemDescription);
        }
        StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
        itemDescription = stringEscapeUtils.unescapeHtml(itemDescription);
        olePurchaseOrderItem.setItemDescription(itemDescription);
    }*/

    /**
     * This method is used to populated the copies section details
     * @param singleItem
     */
    private void populateCopiesSection(OlePurchaseOrderItem singleItem) {
        LOG.debug("### Inside populateCopiesSection of OlePurchaseOrderDocument ###");
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
        else if (singleItem.getItemQuantity() != null && singleItem.getItemNoOfParts() != null && !singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                && !singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))&&singleItem.getCopyList().size()>0) {
            singleItem.setSingleCopyNumber(singleItem.getCopyList().get(0).getCopyNumber());
        }
        else if (singleItem.getItemQuantity() != null && singleItem.getItemNoOfParts() != null && (singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                || singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1)))) {
            singleItem.setCopies(getOleCopyHelperService().setCopiesToLineItem(singleItem.getCopyList(), singleItem.getItemNoOfParts(), singleItem.getItemTitleId()));
        }
        /*if(singleItem.getCopyList().size() > 0 && singleItem.getItemTitleId() != null) {
            //getOlePurapService().setInvoiceDocumentsForPO(singleItem);
            getOlePurapService().setInvoiceDocumentsForPO(this,singleItem);
        }*/
        else if (singleItem.getCopyList().size() > 0 && singleItem.getItemTitleId() == null) {
            getOlePurapService().setInvoiceDocumentsForEResourcePO(singleItem);
        }

    }

    /**
     * Sets default values for APO.
     */
    @Override
    public void setDefaultValuesForAPO() {
        this.setPurchaseOrderAutomaticIndicator(Boolean.TRUE);
        if (!RequisitionSources.B2B.equals(this.getRequisitionSourceCode())) {
            String paramName = PurapParameterConstants.DEFAULT_B2B_VENDOR_CHOICE;
            String paramValue = getParameterService().getParameterValueAsString(PurchaseOrderDocument.class, paramName);
            this.setPurchaseOrderVendorChoiceCode(paramValue);
        }
    }

    @Override
    public Class getItemClass() {
        // TODO Auto-generated method stub
        return OlePurchaseOrderItem.class;
    }


    public boolean getIsFinalReqs() {

        if (this.getDocumentHeader().getWorkflowDocument().isFinal()) {
            return true;
        }
        return false;
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
     * This method is used to get the Instanceeditor url from propertie file
     *
     * @return Instanceeditor url string
     */
    public String getInstanceEditorURL() {
        String instanceEditorURL = getConfigurationService().getPropertyValueAsString(
                OLEConstants.INSTANCEEDITOR_URL_KEY);
        return instanceEditorURL;
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
     * This method is used to get the directory path where the marc xml files need to be created
     *
     * @return Directory path string
     */
    public String getMarcXMLFileDirLocation() throws Exception {
        String externaleDirectory = getFileProcessingService().getMarcXMLFileDirLocation();
        return externaleDirectory;
    }

    public boolean getIsSplitPO() {
        LOG.debug("Inside getIsSplitPO of OlePurchaseOrderDocument");
        return getOlePurchaseOrderDocumentHelperService().getIsSplitPO(this);
    }

    public boolean getIsReOpenPO() {
        LOG.debug("Inside getIsReOpenPO of OlePurchaseOrderDocument");
        return getOlePurchaseOrderDocumentHelperService().getIsReOpenPO(this);
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
     * Method overridden for JIRA OLE-2359 Fund Check
     * Makes sure that accounts for routing has been generated, so that other information can be retrieved from that
     */
    @Override
    protected void populateAccountsForRouting() {
        List<SufficientFundsItem> fundsItems = new ArrayList<SufficientFundsItem>();
        String documentId = this.getDocumentNumber();
        try {
            //  String nodeName = SpringContext.getBean(WorkflowDocumentService.class).getCurrentRouteLevelName(getFinancialSystemDocumentHeader().getWorkflowDocument());
            String nodeName = getFinancialSystemDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator()
                    .next();
            if (nodeName != null
                    && (nodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || nodeName
                    .equalsIgnoreCase(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED))) {
                /*if (SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear().compareTo(getPostingYear()) >= 0) {
                    List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
                    *//*for (GeneralLedgerPendingEntry glpe : pendingEntries) {
                        glpe.getChartOfAccountsCode();
                    }*//*
                    SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());
                    fundsItems = SpringContext.getBean(SufficientFundsService.class).checkSufficientFunds(pendingEntries);
                 //   SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(this);
                 //   SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());
                }*/
                SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(this);
                accountsForRouting = (SpringContext.getBean(PurapAccountingService.class).generateSummary(getItems()));
                /*List<String> fundsItemList = new ArrayList<String>();
                for (SufficientFundsItem fundsItem : fundsItems) {
                    fundsItemList.add(fundsItem.getAccount().getChartOfAccountsCode());
                }
                for (Iterator accountsForRoutingIter = accountsForRouting.iterator(); accountsForRoutingIter.hasNext(); ) {
                    if (!(fundsItemList.contains(((SourceAccountingLine) accountsForRoutingIter.next()).getChartOfAccountsCode()))) {
                        accountsForRoutingIter.remove();
                    }
                }*/
                setAccountsForRouting(accountsForRouting);
                // need to refresh to get the references for the searchable attributes (ie status) and for invoking route levels (ie account
                // objects) -hjs
                refreshNonUpdateableReferences();
                for (SourceAccountingLine sourceLine : getAccountsForRouting()) {
                    sourceLine.refreshNonUpdateableReferences();
                }
            } else {
                super.populateAccountsForRouting();
            }
        } catch (Exception e) {
            logAndThrowRuntimeException("Error in populateAccountsForRouting while submitting document with id " + getDocumentNumber(), e);
        }
    }


    @Override
    public void doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) {
        super.doRouteLevelChange(levelChangeEvent);
        try {
            String newNodeName = levelChangeEvent.getNewNodeName();
            String documentId = this.getDocumentNumber();
            LOG.info("********** Purchase order document Id: " + documentId + " ***********************");
            if (!OLEConstants.PO_NOTE_MAP.containsKey(documentId)
                    && newNodeName != null
                    && (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || newNodeName
                    .equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET))) {
                if (LOG.isDebugEnabled()){
                    LOG.debug("********** Attachement List:  " + this.getNotes() + "***********************");
                }
                addPurchaseOrderNote(levelChangeEvent);
                if (LOG.isDebugEnabled()){
                    LOG.debug("********** After Adding Attachement List:  " + this.getNotes() + "***********************");
                }
                OLEConstants.PO_NOTE_MAP.put(documentId, true);// boolean variable is not used, but in future if required for any
                // manipulation this can be used.
            }
        } catch (Exception e) {
            String errorMsg = "Workflow Error found checking actions requests on document with id "
                    + getDocumentNumber() + ". *** WILL NOT UPDATE PURAP STATUS ***";
            LOG.error(errorMsg, e);
        }
    }

    public void addPurchaseOrderNote(DocumentRouteLevelChange levelChangeEvent) {

        String newNodeName = levelChangeEvent.getNewNodeName();
        if (newNodeName != null
                && (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || newNodeName
                .equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET))) {
            String note = "";
            if(newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE)){
                note = OLEConstants.SufficientFundCheck.PO_NOTE;
            }
            if(newNodeName.equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET)){
                note = OLEConstants.SufficientFundCheck.FYI_NOTE;
            }
            DocumentService documentService = SpringContext.getBean(DocumentService.class);
            Note apoNote = documentService.createNoteFromDocument(this, note);
            this.addNote(apoNote);
            documentService.saveDocumentNotes(this);
        }

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

    public boolean getIsATypeOfRCVGDoc() {
        return false;
    }

    public boolean getIsATypeOfCORRDoc() {
        return false;
    }

    public List<OlePurchaseOrderLineForInvoice> getOlePurchaseOrderLineForInvoiceList() {
        return olePurchaseOrderLineForInvoiceList;
    }

    public void setOlePurchaseOrderLineForInvoiceList(List<OlePurchaseOrderLineForInvoice> olePurchaseOrderLineForInvoiceList) {
        this.olePurchaseOrderLineForInvoiceList = olePurchaseOrderLineForInvoiceList;
    }

    public List<OlePurchaseOrderTotal> getPurchaseOrderTotalList() {
        return purchaseOrderTotalList;
    }

    public void setPurchaseOrderTotalList(List<OlePurchaseOrderTotal> purchaseOrderTotalList) {
        this.purchaseOrderTotalList = purchaseOrderTotalList;
    }

    public Date getPoEndDate() {
        return poEndDate;
    }

    public void setPoEndDate(Date poEndDate) {
        this.poEndDate = poEndDate;
    }

    public boolean isClosePO() {
        return closePO;
    }

    public void setClosePO(boolean closePO) {
        this.closePO = closePO;
    }

    public String getPoNotes() {
        return poNotes;
    }

    public void setPoNotes(String poNotes) {
        this.poNotes = poNotes;
    }

    public String getPoItemLink() {
        String documentTypeName = PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT;
        DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentTypeName);
        String docHandlerUrl = docType.getResolvedDocumentHandlerUrl();
        int endSubString = docHandlerUrl.lastIndexOf("/");
        String serverName = docHandlerUrl.substring(0, endSubString);
        String handler = docHandlerUrl.substring(endSubString + 1, docHandlerUrl.lastIndexOf("?"));
        return serverName + "/" + KRADConstants.PORTAL_ACTION + "?channelTitle=" + docType.getName() + "&channelUrl=" +
                handler + "?" + KRADConstants.DISPATCH_REQUEST_PARAMETER + "=" + KRADConstants.DOC_HANDLER_METHOD + "&" +
                KRADConstants.PARAMETER_DOC_ID + "=" + this.getDocumentNumber() + "&" + KRADConstants.PARAMETER_COMMAND + "=" +
                KewApiConstants.DOCSEARCH_COMMAND;
    }

    public void setPoItemLink(String poItemLink) {
        this.poItemLink = poItemLink;
    }

    public OleSelectDocumentService getOleSelectDocumentService() {
        if(oleSelectDocumentService == null){
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public void setOleSelectDocumentService(OleSelectDocumentService oleSelectDocumentService) {
        this.oleSelectDocumentService = oleSelectDocumentService;
    }
}
