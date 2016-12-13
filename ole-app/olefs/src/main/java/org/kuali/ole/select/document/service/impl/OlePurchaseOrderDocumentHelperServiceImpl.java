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
package org.kuali.ole.select.document.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.integration.purap.CapitalAssetSystem;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService;
import org.kuali.ole.select.document.service.OleRequestorService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.OleUrlResolver;
import org.kuali.ole.select.service.impl.BibInfoWrapperServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import java.math.BigDecimal;
import java.util.*;

public class OlePurchaseOrderDocumentHelperServiceImpl implements OlePurchaseOrderDocumentHelperService {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurchaseOrderDocumentHelperServiceImpl.class);
    private final String UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING = "docAction=checkIn&stringContent=";
    private final String CHECKOUT_DOCSTORE_RECORD_QUERY_STRING = "docAction=checkOut&uuid=";
    private final String CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING = "docAction=ingestContent&stringContent=";
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor;
    private InstanceOlemlRecordProcessor instanceOlemlRecordProcessor;
    private transient OleUrlResolver oleUrlResolver;
    private static transient OlePatronRecordHandler olePatronRecordHandler;
    private static transient OlePatronDocumentList olePatronDocumentList;
    private static transient OleSelectDocumentService oleSelectDocumentService;
    private static transient OlePurapService olePurapService;
    private DocstoreClientLocator docstoreClientLocator;
    protected ConfigurationService kualiConfigurationService;

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
        OlePurchaseOrderDocumentHelperServiceImpl.oleSelectDocumentService = oleSelectDocumentService;
    }

    public static OlePatronDocumentList getOlePatronDocumentList() {
        if (olePatronDocumentList == null) {
            olePatronDocumentList = SpringContext.getBean(OlePatronDocumentList.class);
        }
        return olePatronDocumentList;
    }

    public static void setOlePatronDocumentList(OlePatronDocumentList olePatronDocumentList) {
        OlePurchaseOrderDocumentHelperServiceImpl.olePatronDocumentList = olePatronDocumentList;
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

    public static OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public static void setOlePurapService(OlePurapService olePurapService) {
        OlePurchaseOrderDocumentHelperServiceImpl.olePurapService = olePurapService;
    }


    /**
     * This method is overridden to populate newly added ole fields from requisition into Purchase Order Document.
     *
     * @see org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService#populatePurchaseOrderFromRequisition(org.kuali.ole.module.purap.document.PurchaseOrderDocument, org.kuali.ole.module.purap.document.RequisitionDocument)
     */
    @Override
    public void populatePurchaseOrderFromRequisition(PurchaseOrderDocument purchaseOrderDoc, RequisitionDocument requisitionDoc) {
        LOG.debug("Inside populatePurchaseOrderFromRequisition of OlePurchaseOrderDocumentHelperServiceImpl");
        OlePurchaseOrderDocument purchaseOrderDocument = (OlePurchaseOrderDocument) purchaseOrderDoc;
        OleRequisitionDocument requisitionDocument = (OleRequisitionDocument) requisitionDoc;
        purchaseOrderDocument.setPurchaseOrderCreateTimestamp(SpringContext.getBean(DateTimeService.class).getCurrentTimestamp());
        purchaseOrderDocument.getDocumentHeader().setOrganizationDocumentNumber(requisitionDocument.getDocumentHeader().getOrganizationDocumentNumber());
        purchaseOrderDocument.getDocumentHeader().setDocumentDescription(requisitionDocument.getDocumentHeader().getDocumentDescription());
        purchaseOrderDocument.getDocumentHeader().setExplanation(requisitionDocument.getDocumentHeader().getExplanation());
        purchaseOrderDocument.setBillingName(requisitionDocument.getBillingName());
        purchaseOrderDocument.setBillingLine1Address(requisitionDocument.getBillingLine1Address());
        purchaseOrderDocument.setBillingLine2Address(requisitionDocument.getBillingLine2Address());
        purchaseOrderDocument.setBillingCityName(requisitionDocument.getBillingCityName());
        purchaseOrderDocument.setBillingStateCode(requisitionDocument.getBillingStateCode());
        purchaseOrderDocument.setBillingPostalCode(requisitionDocument.getBillingPostalCode());
        purchaseOrderDocument.setBillingCountryCode(requisitionDocument.getBillingCountryCode());
        purchaseOrderDocument.setBillingPhoneNumber(requisitionDocument.getBillingPhoneNumber());

        purchaseOrderDocument.setReceivingName(requisitionDocument.getReceivingName());
        purchaseOrderDocument.setReceivingCityName(requisitionDocument.getReceivingCityName());
        purchaseOrderDocument.setReceivingLine1Address(requisitionDocument.getReceivingLine1Address());
        purchaseOrderDocument.setReceivingLine2Address(requisitionDocument.getReceivingLine2Address());
        purchaseOrderDocument.setReceivingStateCode(requisitionDocument.getReceivingStateCode());
        purchaseOrderDocument.setReceivingPostalCode(requisitionDocument.getReceivingPostalCode());
        purchaseOrderDocument.setReceivingCountryCode(requisitionDocument.getReceivingCountryCode());
        purchaseOrderDocument.setAddressToVendorIndicator(requisitionDocument.getAddressToVendorIndicator());

        purchaseOrderDocument.setDeliveryBuildingCode(requisitionDocument.getDeliveryBuildingCode());
        purchaseOrderDocument.setDeliveryBuildingRoomNumber(requisitionDocument.getDeliveryBuildingRoomNumber());
        purchaseOrderDocument.setDeliveryBuildingName(requisitionDocument.getDeliveryBuildingName());
        purchaseOrderDocument.setDeliveryCampusCode(requisitionDocument.getDeliveryCampusCode());
        purchaseOrderDocument.setDeliveryCityName(requisitionDocument.getDeliveryCityName());
        purchaseOrderDocument.setDeliveryCountryCode(requisitionDocument.getDeliveryCountryCode());
        purchaseOrderDocument.setDeliveryInstructionText(requisitionDocument.getDeliveryInstructionText());
        purchaseOrderDocument.setDeliveryBuildingLine1Address(requisitionDocument.getDeliveryBuildingLine1Address());
        purchaseOrderDocument.setDeliveryBuildingLine2Address(requisitionDocument.getDeliveryBuildingLine2Address());
        purchaseOrderDocument.setDeliveryPostalCode(requisitionDocument.getDeliveryPostalCode());
        purchaseOrderDocument.setDeliveryRequiredDate(requisitionDocument.getDeliveryRequiredDate());
        purchaseOrderDocument.setDeliveryRequiredDateReasonCode(requisitionDocument.getDeliveryRequiredDateReasonCode());
        purchaseOrderDocument.setDeliveryStateCode(requisitionDocument.getDeliveryStateCode());
        purchaseOrderDocument.setDeliveryToEmailAddress(requisitionDocument.getDeliveryToEmailAddress());
        purchaseOrderDocument.setDeliveryToName(requisitionDocument.getDeliveryToName());
        purchaseOrderDocument.setDeliveryToPhoneNumber(requisitionDocument.getDeliveryToPhoneNumber());
        purchaseOrderDocument.setDeliveryBuildingOtherIndicator(requisitionDocument.isDeliveryBuildingOtherIndicator());
        purchaseOrderDocument.setPurchaseOrderTypeId(requisitionDocument.getPurchaseOrderTypeId());
        purchaseOrderDocument.setPurchaseOrderBeginDate(requisitionDocument.getPurchaseOrderBeginDate());
        purchaseOrderDocument.setPurchaseOrderCostSourceCode(requisitionDocument.getPurchaseOrderCostSourceCode());
        purchaseOrderDocument.setPostingYear(requisitionDocument.getPostingYear());
        purchaseOrderDocument.setPurchaseOrderEndDate(requisitionDocument.getPurchaseOrderEndDate());
        purchaseOrderDocument.setChartOfAccountsCode(requisitionDocument.getChartOfAccountsCode());
        purchaseOrderDocument.setDocumentFundingSourceCode(requisitionDocument.getDocumentFundingSourceCode());
        purchaseOrderDocument.setInstitutionContactEmailAddress(requisitionDocument.getInstitutionContactEmailAddress());
        purchaseOrderDocument.setInstitutionContactName(requisitionDocument.getInstitutionContactName());
        purchaseOrderDocument.setInstitutionContactPhoneNumber(requisitionDocument.getInstitutionContactPhoneNumber());
        purchaseOrderDocument.setNonInstitutionFundAccountNumber(requisitionDocument.getNonInstitutionFundAccountNumber());
        purchaseOrderDocument.setNonInstitutionFundChartOfAccountsCode(requisitionDocument.getNonInstitutionFundChartOfAccountsCode());
        purchaseOrderDocument.setNonInstitutionFundOrgChartOfAccountsCode(requisitionDocument.getNonInstitutionFundOrgChartOfAccountsCode());
        purchaseOrderDocument.setNonInstitutionFundOrganizationCode(requisitionDocument.getNonInstitutionFundOrganizationCode());
        purchaseOrderDocument.setOrganizationCode(requisitionDocument.getOrganizationCode());
        purchaseOrderDocument.setRecurringPaymentTypeCode(requisitionDocument.getRecurringPaymentTypeCode());
        purchaseOrderDocument.setRequestorPersonEmailAddress(requisitionDocument.getRequestorPersonEmailAddress());
        purchaseOrderDocument.setRequestorPersonName(requisitionDocument.getRequestorPersonName());
        purchaseOrderDocument.setRequestorPersonPhoneNumber(requisitionDocument.getRequestorPersonPhoneNumber());
        purchaseOrderDocument.setRequisitionIdentifier(requisitionDocument.getPurapDocumentIdentifier());
        purchaseOrderDocument.setPurchaseOrderTotalLimit(requisitionDocument.getPurchaseOrderTotalLimit());
        purchaseOrderDocument.setPurchaseOrderTransmissionMethodCode(requisitionDocument.getPurchaseOrderTransmissionMethodCode());
        purchaseOrderDocument.setUseTaxIndicator(requisitionDocument.isUseTaxIndicator());
        purchaseOrderDocument.setPurchaseOrderTypeId(requisitionDocument.getPurchaseOrderTypeId());
        purchaseOrderDocument.setVendorCityName(requisitionDocument.getVendorCityName());
        purchaseOrderDocument.setVendorContractGeneratedIdentifier(requisitionDocument.getVendorContractGeneratedIdentifier());
        purchaseOrderDocument.setVendorCountryCode(requisitionDocument.getVendorCountryCode());
        purchaseOrderDocument.setVendorCustomerNumber(requisitionDocument.getVendorCustomerNumber());
        purchaseOrderDocument.setVendorAttentionName(requisitionDocument.getVendorAttentionName());
        purchaseOrderDocument.setVendorDetailAssignedIdentifier(requisitionDocument.getVendorDetailAssignedIdentifier());
        purchaseOrderDocument.setVendorFaxNumber(requisitionDocument.getVendorFaxNumber());
        purchaseOrderDocument.setVendorHeaderGeneratedIdentifier(requisitionDocument.getVendorHeaderGeneratedIdentifier());
        purchaseOrderDocument.setVendorLine1Address(requisitionDocument.getVendorLine1Address());
        purchaseOrderDocument.setVendorLine2Address(requisitionDocument.getVendorLine2Address());
        purchaseOrderDocument.setVendorAddressInternationalProvinceName(requisitionDocument.getVendorAddressInternationalProvinceName());
        purchaseOrderDocument.setVendorName(requisitionDocument.getVendorName());
        purchaseOrderDocument.setVendorNoteText(requisitionDocument.getVendorNoteText());
        purchaseOrderDocument.setVendorPhoneNumber(requisitionDocument.getVendorPhoneNumber());
        purchaseOrderDocument.setVendorPostalCode(requisitionDocument.getVendorPostalCode());
        purchaseOrderDocument.setVendorStateCode(requisitionDocument.getVendorStateCode());
        purchaseOrderDocument.setVendorRestrictedIndicator(requisitionDocument.getVendorRestrictedIndicator());
        purchaseOrderDocument.setVendorPoNumber(requisitionDocument.getVendorPoNumber());
        purchaseOrderDocument.setExternalOrganizationB2bSupplierIdentifier(requisitionDocument.getExternalOrganizationB2bSupplierIdentifier());
        purchaseOrderDocument.setRequisitionSourceCode(requisitionDocument.getRequisitionSourceCode());
        purchaseOrderDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(requisitionDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
        purchaseOrderDocument.setReceivingDocumentRequiredIndicator(requisitionDocument.isReceivingDocumentRequiredIndicator());
        purchaseOrderDocument.setPaymentRequestPositiveApprovalIndicator(requisitionDocument.isPaymentRequestPositiveApprovalIndicator());
        /*purchaseOrderDocument.setLicensingRequirementIndicator(requisitionDocument.isLicensingRequirementIndicator());*/
       /* purchaseOrderDocument.setLicensingRequirementCode(requisitionDocument.getLicensingRequirementCode());*/

        purchaseOrderDocument.setStatusCode(PurapConstants.PurchaseOrderStatuses.APPDOC_IN_PROCESS);

        // Copy items from requisition (which will copy the item's accounts and capital assets)
        List<OlePurchaseOrderItem> items = new ArrayList();
        for (PurApItem reqItem : ((PurchasingAccountsPayableDocument) requisitionDocument).getItems()) {
            RequisitionCapitalAssetItem reqCamsItem = (RequisitionCapitalAssetItem) requisitionDocument.getPurchasingCapitalAssetItemByItemIdentifier(reqItem.getItemIdentifier().intValue());
            items.add(new OlePurchaseOrderItem((OleRequisitionItem) reqItem, purchaseOrderDocument, reqCamsItem));
        }
        purchaseOrderDocument.setItems(items);

        // Copy capital asset information that is directly off the document.
        purchaseOrderDocument.setCapitalAssetSystemTypeCode(requisitionDocument.getCapitalAssetSystemTypeCode());
        purchaseOrderDocument.setCapitalAssetSystemStateCode(requisitionDocument.getCapitalAssetSystemStateCode());
        for (CapitalAssetSystem capitalAssetSystem : requisitionDocument.getPurchasingCapitalAssetSystems()) {
            purchaseOrderDocument.getPurchasingCapitalAssetSystems().add(new PurchaseOrderCapitalAssetSystem(capitalAssetSystem));
        }

        purchaseOrderDocument.fixItemReferences();
    }

    /**
     * This method is overriden to populate bib info in  Purchase Order Document
     *
     * @see org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService#prepareForSave(org.kuali.ole.module.purap.document.PurchaseOrderDocument, org.kuali.rice.krad.rule.event.KualiDocumentEvent)
     */
    @Override
    public void prepareForSave(PurchaseOrderDocument purchaseOrderDocument, KualiDocumentEvent event) {
        LOG.debug("Inside prepareForSave of OlePurchaseOrderDocumentHelperServiceImpl");
        WorkflowDocument workFlowDocument = purchaseOrderDocument.getDocumentHeader().getWorkflowDocument();
        try {
            List<OlePurchaseOrderItem> items = purchaseOrderDocument.getItems();
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (object instanceof OlePurchaseOrderItem) {
                    OlePurchaseOrderItem singleItem = (OlePurchaseOrderItem) object;
                    if ((!StringUtils.isEmpty(singleItem.getInternalRequestorId()) || !StringUtils.isEmpty(singleItem
                            .getRequestorId())) && singleItem.getRequestorTypeId() == null) {
                        KualiInteger staffRequestorTypeId = getRequestorTypeId(OleSelectConstant.REQUESTOR_TYPE_STAFF);
                        singleItem.setRequestorTypeId(staffRequestorTypeId.intValue());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in OlePurchaseOrderDocumentHelperServiceImpl:prepareForSave: " + e.getMessage());
            throw new RuntimeException(e);
        }
        if (event instanceof org.kuali.rice.krad.rules.rule.event.RouteDocumentEvent &&
                purchaseOrderDocument.getRequisitionIdentifier() == null) {
            try {
                UserSession userSession = GlobalVariables.getUserSession();
                //TODO: need to configure a user/role
                //GlobalVariables.setUserSession(new UserSession("ole-khuntley"));
                String user = null;
                if (GlobalVariables.getUserSession() == null) {
                    kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
                    user = kualiConfigurationService.getPropertyValueAsString(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
                    GlobalVariables.setUserSession(new UserSession(user));
                }
                RequisitionDocument rdoc = SpringContext.getBean(org.kuali.ole.module.purap.document.service.RequisitionService.class)
                        .generateRequisitionFromPurchaseOrder(purchaseOrderDocument);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Requisition Document Status Code---->" + rdoc.getApplicationDocumentStatus());
                }
                rdoc.setRequisitionSourceCode("STAN");
                List adhocRouteRecipientList = new ArrayList();
                adhocRouteRecipientList.addAll(purchaseOrderDocument.getAdHocRoutePersons());
                adhocRouteRecipientList.addAll(purchaseOrderDocument.getAdHocRouteWorkgroups());
                SpringContext.getBean(org.kuali.rice.krad.service.DocumentService.class).blanketApproveDocument(rdoc, "Automatic Approval From PO", adhocRouteRecipientList);
                GlobalVariables.setUserSession(userSession);
                purchaseOrderDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(rdoc.getPurapDocumentIdentifier());
                purchaseOrderDocument.setRequisitionIdentifier(rdoc.getPurapDocumentIdentifier());
            } catch (WorkflowException e) {
                LOG.error("Exception in OlePurchaseOrderDocumentHelperServiceImpl:prepareForSave: " + e.getMessage());
                throw new RuntimeException("Error in OlePurchaseOrderDocumentHelperServiceImpl:prepareForSave: " + e.getMessage());
            }

            purchaseOrderDocument.setStatusCode(PurchaseOrderStatuses.APPDOC_IN_PROCESS);
        }
    }

    private KualiInteger getRequestorTypeId(String requestorType) {
        KualiInteger requestorTypeId = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(OleSelectConstant.REQUESTOR_TYPE, requestorType);
        org.kuali.rice.krad.service.BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        Collection<OleRequestorType> requestorTypeList = businessObjectService.findMatching(OleRequestorType.class, criteria);
        requestorTypeId = new KualiInteger(requestorTypeList.iterator().next().getRequestorTypeId());
        return requestorTypeId;
    }

    protected void setRequestorNameOnItem(OlePurchasingItem singleItem) {
        String requestorFirstName = singleItem.getRequestorFirstName();
        if ((singleItem.getRequestorId() != null) || (singleItem.getInternalRequestorId() != null)) {
            int requestorTypeId = singleItem.getRequestorTypeId();
            Map requestorTypeIdMap = new HashMap();
            requestorTypeIdMap.put("requestorTypeId", requestorTypeId);
            org.kuali.rice.krad.service.BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<OleRequestorType> oleRequestorTypeList = (List) businessObjectService.findMatching(OleRequestorType.class, requestorTypeIdMap);
            for (int i = 0; i < oleRequestorTypeList.size(); i++) {
                if (oleRequestorTypeList.get(i).getRequestorType().equalsIgnoreCase(OleSelectConstant.REQUESTOR_TYPE_STAFF)) {
                    Person personImpl = SpringContext.getBean(PersonService.class).getPerson(singleItem.getInternalRequestorId());
                    if (singleItem.getRequestorId() == null && singleItem.getInternalRequestorId() != null) {
                        if (personImpl != null) {
                            singleItem.setRequestorFirstName(personImpl.getName());
                        }
                    }
                }
                /*
                 * else { Map requestorIdMap = new HashMap(); requestorIdMap.put("requestorId", singleItem.getRequestorId());
                 * List<OleRequestor> oleRequestorList = (List) businessObjectService.findMatching(OleRequestor.class,
                 * requestorIdMap); singleItem.setRequestorFirstName(oleRequestorList.get(0).getRequestorFirstName()); }
                 */
            }
        }
    }


    /**
     * This method is overriden to populate bib info in  Purchase Order Document
     *
     * @see org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService#processAfterRetrieve(org.kuali.ole.module.purap.document.PurchaseOrderDocument)
     */
    @Override
    public void processAfterRetrieve(PurchaseOrderDocument purchaseOrderDocument) {
        // super.processAfterRetrieve();
        LOG.debug("Inside processAfterRetrieve of OlePurchaseOrderDocumentHelperServiceImpl");
         // if(!purchaseOrderDocument.getFinancialDocumentTypeCode().equals("OLE_POC")) {
        try {
            PurchaseOrderType purchaseOrderTypeDoc = getOlePurapService().getPurchaseOrderType(purchaseOrderDocument.getPurchaseOrderTypeId());
            if(purchaseOrderTypeDoc != null){
                purchaseOrderDocument.setOrderType(purchaseOrderTypeDoc);
            }
            List<OlePurchaseOrderItem> items = purchaseOrderDocument.getItems();
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (object instanceof OlePurchaseOrderItem) {
                    OlePurchaseOrderItem singleItem = (OlePurchaseOrderItem) object;
                    setItemDetailWhileProcessAfterRetrive(singleItem);
                    if(singleItem.getCopyList().size() > 0) {
                        getOlePurapService().setInvoiceDocumentsForPO(purchaseOrderDocument,singleItem);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in OlePurchaseOrderDocument:processAfterRetrieve for OlePurchaseOrderItem " + e.getMessage());
            throw new RuntimeException(e);
        }
    // }
    }

    /**
     * This method is used to set the item detail while processafterretrive method is executed
     * @param olePurchaseOrderItem
     */
    private void setItemDetailWhileProcessAfterRetrive(OlePurchaseOrderItem olePurchaseOrderItem)throws Exception{
        if(olePurchaseOrderItem.getRequestorId()!=null){
            olePurchaseOrderItem.setRequestorFirstName(getOlePurapService().getPatronName(olePurchaseOrderItem.getRequestorId()));
        }
        // Added for jira OLE-2811 ends
        setRequestorNameOnItem(olePurchaseOrderItem);
        HashMap<String, String> dataMap = new HashMap<String, String>();
        if(olePurchaseOrderItem.getItemUnitPrice()!=null){
            olePurchaseOrderItem.setItemUnitPrice(olePurchaseOrderItem.getItemUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (olePurchaseOrderItem.getItemTitleId() != null) {
            Bib bib = new BibMarc();
            if (olePurchaseOrderItem.getItemTitleId() != null && olePurchaseOrderItem.getItemTitleId() != "") {
                bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(olePurchaseOrderItem.getItemTitleId());
            }
            olePurchaseOrderItem.setBibUUID(bib.getId());
            dataMap.put(OleSelectConstant.TITLE_ID, olePurchaseOrderItem.getItemTitleId());
            dataMap.put(OleSelectConstant.DOC_CATEGORY_TYPE, OleSelectConstant.DOC_CATEGORY_TYPE_ITEMLINKS);
            olePurchaseOrderItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePurchaseOrderItem.getItemTitleId()));
            olePurchaseOrderItem.setItemDescription(getOlePurapService().getItemDescription(bib));
        }
        BibTree bibTree = new BibTree();
        if(olePurchaseOrderItem.getItemTitleId() != null && olePurchaseOrderItem.getItemTitleId() != ""){
            bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(olePurchaseOrderItem.getItemTitleId());
        }
        if (null != olePurchaseOrderItem.getItemTitleId()) {
            if (olePurchaseOrderItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                    || olePurchaseOrderItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
                List<OleCopies> copies = setCopiesToLineItem(olePurchaseOrderItem, bibTree);
                if(CollectionUtils.isNotEmpty(bibTree.getHoldingsTrees())){
                    if(CollectionUtils.isNotEmpty(bibTree.getHoldingsTrees().get(0).getItems())){
                        olePurchaseOrderItem.setItemTypeDescription(bibTree.getHoldingsTrees().get(0).getItems().get(0).getItemType());
                    }else{
                        olePurchaseOrderItem.setItemTypeDescription("");
                    }
                }else {
                    olePurchaseOrderItem.setItemTypeDescription("");
                }
                olePurchaseOrderItem.setCopies(copies);
            }
        }
        if (olePurchaseOrderItem.getItemQuantity() != null && olePurchaseOrderItem.getItemNoOfParts() != null && !olePurchaseOrderItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                && !olePurchaseOrderItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))&&olePurchaseOrderItem.getCopyList().size()>0) {
            olePurchaseOrderItem.setSingleCopyNumber(olePurchaseOrderItem.getCopyList().get(0).getCopyNumber());
        }
    }

    public List<OleCopies> setCopiesToLineItem(OlePurchaseOrderItem singleItem, BibTree bibTree) {
        List<HoldingsTree> instanceDocuments = bibTree.getHoldingsTrees();
        List<OleCopies> copies = new ArrayList<OleCopies>();
        for (HoldingsTree holdingsTree : instanceDocuments) {
            List<Item> itemDocuments = holdingsTree.getItems();
            StringBuffer enumeration = new StringBuffer();
            for (int itemDocs = 0; itemDocs < itemDocuments.size(); itemDocs++) {
                if (itemDocs + 1 == itemDocuments.size()) {
                    enumeration = enumeration.append(itemDocuments.get(itemDocs).getEnumeration());
                } else {
                    enumeration = enumeration.append(itemDocuments.get(itemDocs).getEnumeration() + ";");
                }

            }
            int startingCopy = 0;
            if (singleItem.getItemNoOfParts().intValue() != 0 && null != enumeration && StringUtils.isNotEmpty(enumeration.toString())) {
                String enumerationSplit = enumeration.substring(1, 2);
                boolean isint = checkIsEnumerationSplitIsIntegerOrNot(enumerationSplit);
                if (isint) {
                    startingCopy = Integer.parseInt(enumerationSplit);
                }
            }
            if (singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                    || singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
                int noOfCopies = holdingsTree.getItems().size()
                        / singleItem.getItemNoOfParts().intValue();
                OleRequisitionCopies copy = new OleRequisitionCopies();
                copy.setParts(singleItem.getItemNoOfParts());
                copy.setLocationCopies(holdingsTree.getHoldings().getLocationName());
                copy.setItemCopies(new KualiDecimal(noOfCopies));
                copy.setPartEnumeration(enumeration.toString());
                copy.setStartingCopyNumber(new KualiInteger(startingCopy));
                copies.add(copy);
            }
        }
        return copies;
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

    @Override
    public void setBibInfoBean(BibInfoBean bibInfoBean, OlePurchaseOrderItem singleItem) {
        bibInfoBean.setTitle(singleItem.getItemTitle());
        bibInfoBean.setAuthor(singleItem.getItemAuthor());
        bibInfoBean.setRequestor(singleItem.getRequestorId() == null ? null : singleItem.getRequestorId().toString());
    }

    @Override
    public void setBibInfoBean(BibInfoBean bibInfoBean, OleRequisitionItem singleItem) {
        bibInfoBean.setTitle(singleItem.getBibInfoBean().getTitle());
        bibInfoBean.setAuthor(singleItem.getBibInfoBean().getAuthor());
        bibInfoBean.setRequestor(singleItem.getRequestorId() == null ? null : singleItem.getRequestorId().toString());
    }

    /**
     * This method returns if Purchase Order Document created is in Final Status
     *
     * @see org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService#getIsFinalReqs(org.kuali.ole.module.purap.document.PurchaseOrderDocument)
     */
    @Override
    public boolean getIsFinalReqs(PurchaseOrderDocument purchaseOrderDocument) {
        LOG.debug("Inside getIsFinalReqs of OlePurchaseOrderDocumentHelperServiceImpl");
        if (purchaseOrderDocument.getDocumentHeader().getWorkflowDocument().isFinal()) {
            return true;
        }
        return false;
    }


    @Override
    public boolean getIsSplitPO(PurchaseOrderDocument purchaseOrderDocument) {
        LOG.debug("Inside getIsSplitPO of OlePurchaseOrderDocumentHelperServiceImpl");
        if (purchaseOrderDocument.getDocumentHeader().getWorkflowDocument().getDocumentTypeName().equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_SPLIT) && purchaseOrderDocument.getDocumentHeader().getWorkflowDocument().isSaved()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean getIsReOpenPO(PurchaseOrderDocument purchaseOrderDocument) {
        LOG.debug("Inside getIsSplitPO of OlePurchaseOrderDocumentHelperServiceImpl");
        if (purchaseOrderDocument.getDocumentHeader().getWorkflowDocument().getDocumentTypeName().equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_REOPEN) && purchaseOrderDocument.getDocumentHeader().getWorkflowDocument().isSaved()) {
            return true;
        }
        return false;
    }

    /**
     * This method is used to get the bibedtior creat url from propertie file
     *
     * @return Bibeditor creat url string
     */
    @Override
    public String getBibeditorCreateURL() {
        String bibeditorCreateURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.BIBEDITOR_CREATE_URL_KEY);
        return bibeditorCreateURL;
    }

    @Override
    public String getBibSearchURL() {
        String bibSearchURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.BIBEDITOR_SEARCH_URL_KEY);
        return bibSearchURL;
    }

    /**
     * This method is used to get the bibedtior edit url from propertie file
     *
     * @return Bibeditor edit url string
     */
    @Override
    public String getBibeditorEditURL() {
        String bibeditorEditURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.BIBEDITOR_URL_KEY);
        return bibeditorEditURL;
    }

    /**
     * This method is used to get the dublinedtior edit url from propertie file
     *
     * @return Dublineditor edit url string
     */
    public String getDublinEditorEditURL() {
        String dublinEditorEditURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DUBLINEDITOR_URL_KEY);
        return dublinEditorEditURL;
    }
    /**
     * This method is used to get the bibedtior view url from propertie file
     *
     * @return Bibeditor view url string
     */
    @Override
    public String getBibeditorViewURL() {
        String bibeditorViewURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSTORE_APP_URL_KEY);
        return bibeditorViewURL;
    }
    /**
     * This method is used to get the dublinedtior view url from propertie file
     *
     * @return dublineditor view url string
     */
    public String getDublinEditorViewURL() {
        String dublinEditorViewURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSTORE_VIEW_URL_KEY);
        return dublinEditorViewURL;
    }
    /**
     * This method is used to get the Instanceeditor url from propertie file
     *
     * @return Instanceeditor url string
     */
    @Override
    public String getInstanceEditorURL() {
        String instanceEditorURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(
                OLEConstants.INSTANCEEDITOR_URL_KEY);
        return instanceEditorURL;
    }

    /**
     * This method is used to get the directory path where the marc xml files need to be created
     *
     * @return Directory path string
     */
    @Override
    public String getMarcXMLFileDirLocation() throws Exception {
        FileProcessingService fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        String externaleDirectory = fileProcessingService.getMarcXMLFileDirLocation();
        return externaleDirectory;
    }


    @Override
    public List getItemsActiveOnly(PurchaseOrderDocument purchaseOrderDocument) {
        List returnList = new ArrayList();
        for (Iterator iter = purchaseOrderDocument.getItems().iterator(); iter.hasNext(); ) {
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
    @Override
    public List getItemsActiveOnlySetupAlternateAmount(PurchaseOrderDocument purchaseOrderDocument) {
        List returnList = new ArrayList();
        for (Iterator iter = purchaseOrderDocument.getItems().iterator(); iter.hasNext(); ) {
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
    public boolean getAdditionalChargesExist(PurchaseOrderDocument purchaseOrderDocument) {
        List<OlePurchaseOrderItem> items = purchaseOrderDocument.getItems();
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
}
