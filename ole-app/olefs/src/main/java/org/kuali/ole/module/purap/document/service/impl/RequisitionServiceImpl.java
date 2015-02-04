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
package org.kuali.ole.module.purap.document.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.integration.cab.CapitalAssetBuilderModuleService;
import org.kuali.ole.integration.purap.CapitalAssetSystem;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.PurapRuleConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.PurchasingDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.dataaccess.RequisitionDao;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.RequisitionService;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.PostalCodeValidationService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.businessobject.VendorCommodityCode;
import org.kuali.ole.vnd.businessobject.VendorContract;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


/**
 * Implementation of RequisitionService
 */
@Transactional
public class RequisitionServiceImpl implements RequisitionService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RequisitionServiceImpl.class);

    private BusinessObjectService businessObjectService;
    private CapitalAssetBuilderModuleService capitalAssetBuilderModuleService;
    private DateTimeService dateTimeService;
    private DocumentService documentService;
    private KualiRuleService ruleService;
    private ConfigurationService kualiConfigurationService;
    private ParameterService parameterService;
    private PersonService personService;
    private PostalCodeValidationService postalCodeValidationService;
    private PurapService purapService;
    private RequisitionDao requisitionDao;
    private UniversityDateService universityDateService;
    private VendorService vendorService;

    @Override
    public PurchasingCapitalAssetItem createCamsItem(PurchasingDocument purDoc, PurApItem purapItem) {
        PurchasingCapitalAssetItem camsItem = new RequisitionCapitalAssetItem();
        camsItem.setItemIdentifier(purapItem.getItemIdentifier());
        // If the system type is INDIVIDUAL then for each of the capital asset items, we need a system attached to it.
        if (purDoc.getCapitalAssetSystemTypeCode().equals(PurapConstants.CapitalAssetTabStrings.INDIVIDUAL_ASSETS)) {
            CapitalAssetSystem resultSystem = new RequisitionCapitalAssetSystem();
            camsItem.setPurchasingCapitalAssetSystem(resultSystem);
        }
        camsItem.setPurchasingDocument(purDoc);

        return camsItem;
    }

    @Override
    public CapitalAssetSystem createCapitalAssetSystem() {
        CapitalAssetSystem resultSystem = new RequisitionCapitalAssetSystem();
        return resultSystem;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.RequisitionService#getRequisitionById(java.lang.Integer)
     */
    @Override
    public RequisitionDocument getRequisitionById(Integer id) {
        String documentNumber = requisitionDao.getDocumentNumberForRequisitionId(id);
        if (ObjectUtils.isNotNull(documentNumber)) {
            try {
                RequisitionDocument doc = (RequisitionDocument) documentService.getByDocumentHeaderId(documentNumber);

                return doc;
            } catch (WorkflowException e) {
                String errorMessage = "Error getting requisition document from document service";
                LOG.error("getRequisitionById() " + errorMessage, e);
                throw new RuntimeException(errorMessage, e);
            }
        }

        return null;
    }

    private boolean isReqCreatedByPo(String routeHeaderId) {
        List<ActionTakenValue> result = (List<ActionTakenValue>) KEWServiceLocator.getActionTakenService().getActionsTaken(routeHeaderId);
        if (result != null && result.size() > 0) {
            for (ActionTakenValue val : result) {
                if (val.getAnnotation() != null &&
                        val.getAnnotation().trim().equals("Automatic Approval From PO")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.RequisitionService#isAutomaticPurchaseOrderAllowed(org.kuali.ole.module.purap.document.RequisitionDocument)
     */
    @Override
    public boolean isAutomaticPurchaseOrderAllowed(RequisitionDocument requisition) {
        LOG.debug("isAutomaticPurchaseOrderAllowed() started");

        /*
         * The private checkAutomaticPurchaseOrderRules method contains rules to check if a requisition can become an APO (Automatic
         * Purchase Order). The method returns a string containing the reason why this method should return false. Save the reason
         * as a note on the requisition.
         */
        String note = checkAutomaticPurchaseOrderRules(requisition);
        if (StringUtils.isNotEmpty(note)) {
            note = PurapConstants.REQ_REASON_NOT_APO + note;
            try {
                Note apoNote = documentService.createNoteFromDocument(requisition, note);
                requisition.addNote(apoNote);
                documentService.saveDocumentNotes(requisition);
            } catch (Exception e) {
                throw new RuntimeException(PurapConstants.REQ_UNABLE_TO_CREATE_NOTE + " " + e);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("isAutomaticPurchaseOrderAllowed() return false; " + note);
            }
            return false;
        }

        LOG.debug("isAutomaticPurchaseOrderAllowed() You made it!  Your REQ can become an APO; return true.");
        return true;
    }

    public Collection<String> getAPORules() {
        return CoreFrameworkServiceLocator.getParameterService().getParameterValuesAsString(
                PurchaseOrderDocument.class, "APORules");
    }

    /**
     * Checks the rule for Automatic Purchase Order eligibility of the requisition and return a String containing the reason why the
     * requisition was not eligible to become an APO if it was not eligible, or return an empty String if the requisition is
     * eligible to become an APO
     *
     * @param requisition the requisition document to be checked for APO eligibility.
     * @return String containing the reason why the requisition was not eligible to become an APO if it was not eligible, or an
     *         empty String if the requisition is eligible to become an APO.
     */
    protected String checkAutomaticPurchaseOrderRules(RequisitionDocument requisition) {
        Collection<String> APORules = getAPORules();
        String requisitionSource = requisition.getRequisitionSourceCode();
        KualiDecimal reqTotal = requisition.getTotalDollarAmount();
        KualiDecimal apoLimit = purapService.getApoLimit(requisition.getVendorContractGeneratedIdentifier(), requisition.getChartOfAccountsCode(), requisition.getOrganizationCode());
        requisition.setOrganizationAutomaticPurchaseOrderLimit(apoLimit);

        if (LOG.isDebugEnabled()) {
            LOG.debug("isAPO() reqId = " + requisition.getPurapDocumentIdentifier() + "; apoLimit = " + apoLimit + "; reqTotal = " + reqTotal);
        }
        if (apoLimit == null) {
            return "APO limit is empty.";
        } else {
            if (APORules.contains(PurapConstants.APO_LIMIT)) {
                if (reqTotal.compareTo(apoLimit) == 1) {
                    return "Requisition total is greater than the APO limit.";
                }
            }
        }
       /* if (APORules.contains(PurapConstants.REQ_TOT)) {
            if (reqTotal.compareTo(KualiDecimal.ZERO) <= 0) {
                return "Requisition total is not greater than zero.";
            }
        }*/

        if (this.isReqCreatedByPo(requisition.getDocumentNumber())) {
            return "Requsiusion is created by PO";
        }

        LOG.debug("isAPO() vendor #" + requisition.getVendorHeaderGeneratedIdentifier() + "-" + requisition.getVendorDetailAssignedIdentifier());
        if (APORules.contains(PurapConstants.VEN_NOT_SEL)) {
            if (requisition.getVendorHeaderGeneratedIdentifier() == null || requisition.getVendorDetailAssignedIdentifier() == null) {
                return "Vendor was not selected from the vendor database.";
            }
        } else {
            VendorDetail vendorDetail = vendorService.getVendorDetail(requisition.getVendorHeaderGeneratedIdentifier(), requisition.getVendorDetailAssignedIdentifier());
            if (APORules.contains(PurapConstants.ERR_RTV_VENDOR)) {
                if (vendorDetail == null) {
                    return "Error retrieving vendor from the database.";
                }
            }
            if (StringUtils.isBlank(requisition.getVendorLine1Address()) ||
                    StringUtils.isBlank(requisition.getVendorCityName()) ||
                    StringUtils.isBlank(requisition.getVendorCountryCode())) {
                return "Requisition does not have all of the vendor address fields that are required for Purchase Order.";
            }
            requisition.setVendorRestrictedIndicator(vendorDetail.getVendorRestrictedIndicator());
            if (APORules.contains(PurapConstants.VENDOR_RESTRICTED)) {
                if (requisition.getVendorRestrictedIndicator() != null && requisition.getVendorRestrictedIndicator()) {
                    return "Selected vendor is marked as restricted.";
                }
                if (vendorDetail.isVendorDebarred()) {
                    return "Selected vendor is marked as a debarred vendor";
                }
                requisition.setVendorDetail(vendorDetail);

                if ((!PurapConstants.RequisitionSources.B2B.equals(requisitionSource)) && ObjectUtils.isNull(requisition.getVendorContractGeneratedIdentifier())) {
                    Person initiator = getPersonService().getPerson(requisition.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId());
                    VendorContract b2bContract = vendorService.getVendorB2BContract(vendorDetail, initiator.getCampusCode());
                    if (APORules.contains(PurapConstants.REQ_WITH_NO_CONTRACT)) {
                        if (b2bContract != null) {
                            return "Standard requisition with no contract selected but a B2B contract exists for the selected vendor.";
                        }
                    }
                }
            }

            //if vendor address isn't complete, no APO
            if (StringUtils.isBlank(requisition.getVendorLine1Address()) ||
                    StringUtils.isBlank(requisition.getVendorCityName()) ||
                    StringUtils.isBlank(requisition.getVendorCountryCode()) ||
                    !postalCodeValidationService.validateAddress(requisition.getVendorCountryCode(), requisition.getVendorStateCode(), requisition.getVendorPostalCode(), "", "")) {
                return "Requistion does not contain a complete vendor address";
            }

            // These are needed for commodity codes. They are put in here so that
            // we don't have to loop through items too many times.
            String purchaseOrderRequiresCommodityCode = parameterService.getParameterValueAsString(PurchaseOrderDocument.class, PurapRuleConstants.ITEMS_REQUIRE_COMMODITY_CODE_IND);
            boolean commodityCodeRequired = purchaseOrderRequiresCommodityCode.equals("Y");

            for (Iterator iter = requisition.getItems().iterator(); iter.hasNext(); ) {
                RequisitionItem item = (RequisitionItem) iter.next();
                if (APORules.contains(PurapConstants.ITM_RESTRICTED)) {
                    if (item.isItemRestrictedIndicator()) {
                        return "Requisition contains an item that is marked as restricted.";
                    }
                }

                //We only need to check the commodity codes if this is an above the line item.
                if (item.getItemType().isLineItemIndicator()) {
                    String commodityCodesReason = "";
                    List<VendorCommodityCode> vendorCommodityCodes = commodityCodeRequired ? requisition.getVendorDetail().getVendorCommodities() : null;
                    commodityCodesReason = checkAPORulesPerItemForCommodityCodes(item, vendorCommodityCodes, commodityCodeRequired);
                    if (StringUtils.isNotBlank(commodityCodesReason)) {
                        return commodityCodesReason;
                    }
                }

                if (PurapConstants.ItemTypeCodes.ITEM_TYPE_ORDER_DISCOUNT_CODE.equals(item.getItemType().getItemTypeCode())
                        || PurapConstants.ItemTypeCodes.ITEM_TYPE_TRADE_IN_CODE.equals(item.getItemType().getItemTypeCode())) {
                    if (APORules.contains(PurapConstants.REQ_CON_TRADEIN)) {
                        if ((item.getItemUnitPrice() != null) && ((BigDecimal.ZERO.compareTo(item.getItemUnitPrice())) != 0)) {
                            // discount or trade-in item has unit price that is not empty or zero
                            return "Requisition contains a " + item.getItemType().getItemTypeDescription() + " item, so it does not qualify as an APO.";
                        }
                    }
                }
                if (APORules.contains(PurapConstants.REQ_CON_ACC_LINE)) {
                    if (!PurapConstants.RequisitionSources.B2B.equals(requisitionSource)) {
                        for (PurApAccountingLine accountingLine : item.getSourceAccountingLines()) {
                            if (capitalAssetBuilderModuleService.doesAccountingLineFailAutomaticPurchaseOrderRules(accountingLine)) {
                                return "Requisition contains accounting line with capital object level";
                            }
                        }
                    }
                }
            }

        }// endfor items
        if (APORules.contains(PurapConstants.RECUR_REQ_TYPE)) {
            if (capitalAssetBuilderModuleService.doesDocumentFailAutomaticPurchaseOrderRules(requisition)) {
                return "Requisition contains capital asset items.";
            }
        }
        if (APORules.contains(PurapConstants.RECUR_PAY_TYPE)) {
            if (StringUtils.isNotEmpty(requisition.getRecurringPaymentTypeCode())) {
                return "Payment type is marked as recurring.";
            }
        }
        if (APORules.contains(PurapConstants.PO_TOT_NOT_EXCEED)) {
            if ((requisition.getPurchaseOrderTotalLimit() != null) && (KualiDecimal.ZERO.compareTo(requisition.getPurchaseOrderTotalLimit()) != 0)) {
                LOG.debug("isAPO() po total limit is not null and not equal to zero; return false.");
                return "The 'PO not to exceed' amount has been entered.";
            }
        }
        if (APORules.contains(new String("ALT_VEN_NAM"))) {
            if (StringUtils.isNotEmpty(requisition.getAlternate1VendorName()) || StringUtils.isNotEmpty(requisition.getAlternate2VendorName()) || StringUtils.isNotEmpty(requisition.getAlternate3VendorName()) || StringUtils.isNotEmpty(requisition.getAlternate4VendorName()) || StringUtils.isNotEmpty(requisition.getAlternate5VendorName())) {
                LOG.debug("isAPO() alternate vendor name exists; return false.");
                return "Requisition contains additional suggested vendor names.";
            }
        }
        if (APORules.contains(PurapConstants.ALT_VEN_NAME_EXISTS)) {
            if (requisition.isPostingYearNext() && !purapService.isTodayWithinApoAllowedRange()) {
                return "Requisition is set to encumber next fiscal year and approval is not within APO allowed date range.";
            }
        }

        return "";
    }

    /**
     * Checks the APO rules for Commodity Codes.
     * The rules are as follow:
     * 1. If an institution does not require a commodity code on a requisition but
     * does require a commodity code on a purchase order:
     * a. If the requisition qualifies for an APO and the commodity code is blank
     * on any line item then the system should use the default commodity code
     * for the vendor.
     * b. If there is not a default commodity code for the vendor then the
     * requisition is not eligible to become an APO.
     * 2. The commodity codes where the restricted indicator is Y should disallow
     * the requisition from becoming an APO.
     * 3. If the commodity code is Inactive when the requisition is finally approved
     * do not allow the requisition to become an APO.
     *
     * @param purItem
     * @param vendorCommodityCodes
     * @param commodityCodeRequired
     * @return
     */
    protected String checkAPORulesPerItemForCommodityCodes(RequisitionItem purItem, List<VendorCommodityCode> vendorCommodityCodes, boolean commodityCodeRequired) {
        // If the commodity code is blank on any line item and a commodity code is required,
        // then the system should use the default commodity code for the vendor
        if (purItem.getCommodityCode() == null && commodityCodeRequired) {
            for (VendorCommodityCode vcc : vendorCommodityCodes) {
                if (vcc.isCommodityDefaultIndicator()) {
                    purItem.setCommodityCode(vcc.getCommodityCode());
                    purItem.setPurchasingCommodityCode(vcc.getPurchasingCommodityCode());
                }
            }
        }
        if (purItem.getCommodityCode() == null) {
            // If there is not a default commodity code for the vendor then the requisition is not eligible to become an APO.
            if (commodityCodeRequired) {
                return "There are missing commodity code(s).";
            }
        } else if (!purItem.getCommodityCode().isActive()) {
            return "Requisition contains inactive commodity codes.";
        } else if (purItem.getCommodityCode().isRestrictedItemsIndicator()) {
            return "Requisition contains an item with a restricted commodity code.";
        }
        return "";
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.RequisitionService#getRequisitionsAwaitingContractManagerAssignment()
     */
    @Override
    public List<RequisitionDocument> getRequisitionsAwaitingContractManagerAssignment() {
        List<RequisitionDocument> unassignedRequisitions = new ArrayList<RequisitionDocument>();
        List<RequisitionDocument> requisitions = new ArrayList<RequisitionDocument>();

        List<String> documentsNumbersAwaitingContractManagerAssignment = getDocumentsNumbersAwaitingContractManagerAssignment();

        for (String documentNumber : documentsNumbersAwaitingContractManagerAssignment) {
            Map fieldValues = new HashMap();
            fieldValues.put(PurapPropertyConstants.DOCUMENT_NUMBER, documentNumber);

            requisitions = (List<RequisitionDocument>) businessObjectService.findMatching(RequisitionDocument.class, fieldValues);

            for (RequisitionDocument req : requisitions) {
                unassignedRequisitions.add(req);
            }
        }

        return unassignedRequisitions;
    }

    /**
     * Gets a list of strings of document numbers from workflow documents where
     * document status = 'P', 'F'  and document type = 'REQS'.  If appDocStatus status
     * of 'Awaiting Contract Manager Assignment' then the document number is added to the list
     * <p/>
     * NOTE: simplify using DocSearch lookup with AppDocStatus
     *
     * @return list of documentNumbers to retrieve requisitions.
     */
    protected List<String> getDocumentsNumbersAwaitingContractManagerAssignment() {
        List<String> requisitionDocumentNumbers = new ArrayList<String>();

        DocumentSearchCriteria.Builder documentSearchCriteriaDTO = DocumentSearchCriteria.Builder.create();
        //Search for status of P and F and "Awaiting Contract Manager Assignment" application document status
        documentSearchCriteriaDTO.setDocumentStatuses(Arrays.asList(DocumentStatus.PROCESSED, DocumentStatus.FINAL));
        documentSearchCriteriaDTO.setDocumentTypeName(PurapConstants.REQUISITION_DOCUMENT_TYPE);
        documentSearchCriteriaDTO.setApplicationDocumentStatus(PurapConstants.RequisitionStatuses.APPDOC_AWAIT_CONTRACT_MANAGER_ASSGN);
        documentSearchCriteriaDTO.setSaveName(null);

        DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(null, documentSearchCriteriaDTO.build());

        String documentHeaderId = null;

        for (DocumentSearchResult result : results.getSearchResults()) {
            requisitionDocumentNumbers.add(result.getDocument().getDocumentId());
        }

        return requisitionDocumentNumbers;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.RequisitionService#getCountOfRequisitionsAwaitingContractManagerAssignment()
     */
    @Override
    public int getCountOfRequisitionsAwaitingContractManagerAssignment() {
        List<RequisitionDocument> unassignedRequisitions = getRequisitionsAwaitingContractManagerAssignment();
        if (ObjectUtils.isNotNull(unassignedRequisitions)) {
            return unassignedRequisitions.size();
        } else {
            return 0;
        }
    }

    /**
     * Create Purchase Order and populate with data from Purchase Order and other default data
     *
     * @param reqDocument The requisition document from which we create the purchase order document.
     * @return The purchase order document created by this method.
     * @throws WorkflowException
     */
    @Override
    public OleRequisitionDocument generateRequisitionFromPurchaseOrder(PurchaseOrderDocument purDocument) throws WorkflowException {

        OleRequisitionDocument rdoc = null;
        rdoc = (OleRequisitionDocument) documentService.getNewDocument("OLE_REQS");
        rdoc = populateRequisitionFromPurchaseOrder(rdoc, purDocument);

//        rdoc.setStatusCode(PurchaseOrderStatuses.IN_PROCESS);
        if (ObjectUtils.isNotNull(purDocument.getVendorContract())) {
            rdoc.setVendorPaymentTermsCode(purDocument.getVendorContract().getVendorPaymentTermsCode());
            rdoc.setVendorShippingPaymentTermsCode(purDocument.getVendorContract().getVendorShippingPaymentTermsCode());
            rdoc.setVendorShippingTitleCode(purDocument.getVendorContract().getVendorShippingTitleCode());
        } else {
            VendorDetail vendor = vendorService.getVendorDetail(purDocument.getVendorHeaderGeneratedIdentifier(), purDocument.getVendorDetailAssignedIdentifier());
            if (ObjectUtils.isNotNull(vendor)) {
                rdoc.setVendorPaymentTermsCode(vendor.getVendorPaymentTermsCode());
                rdoc.setVendorShippingPaymentTermsCode(vendor.getVendorShippingPaymentTermsCode());
                rdoc.setVendorShippingTitleCode(vendor.getVendorShippingTitleCode());
            }
        }

        if (!PurapConstants.RequisitionSources.B2B.equals(purDocument.getRequisitionSourceCode())) {
            purapService.addBelowLineItems(rdoc);
        }
        // rdoc.fixItemReferences();

        return rdoc;
    }


    public OleRequisitionDocument populateRequisitionFromPurchaseOrder(OleRequisitionDocument requisitionDocument, PurchaseOrderDocument pdoc) {

        requisitionDocument.getDocumentHeader().setOrganizationDocumentNumber(pdoc.getDocumentHeader().getOrganizationDocumentNumber());
        requisitionDocument.getDocumentHeader().setDocumentDescription(pdoc.getDocumentHeader().getDocumentDescription());
        requisitionDocument.getDocumentHeader().setExplanation(pdoc.getDocumentHeader().getExplanation());

        requisitionDocument.setBillingName(pdoc.getBillingName());
        requisitionDocument.setBillingLine1Address(pdoc.getBillingLine1Address());
        requisitionDocument.setBillingLine2Address(pdoc.getBillingLine2Address());
        requisitionDocument.setBillingCityName(pdoc.getBillingCityName());
        requisitionDocument.setBillingStateCode(pdoc.getBillingStateCode());
        requisitionDocument.setBillingPostalCode(pdoc.getBillingPostalCode());
        requisitionDocument.setBillingCountryCode(pdoc.getBillingCountryCode());
        requisitionDocument.setBillingPhoneNumber(pdoc.getBillingPhoneNumber());

        requisitionDocument.setReceivingName(pdoc.getReceivingName());
        requisitionDocument.setReceivingCityName(pdoc.getReceivingCityName());
        requisitionDocument.setReceivingLine1Address(pdoc.getReceivingLine1Address());
        requisitionDocument.setReceivingLine2Address(pdoc.getReceivingLine2Address());
        requisitionDocument.setReceivingStateCode(pdoc.getReceivingStateCode());
        requisitionDocument.setReceivingPostalCode(pdoc.getReceivingPostalCode());
        requisitionDocument.setReceivingCountryCode(pdoc.getReceivingCountryCode());
        requisitionDocument.setAddressToVendorIndicator(pdoc.getAddressToVendorIndicator());

        requisitionDocument.setDeliveryBuildingCode(pdoc.getDeliveryBuildingCode());
        requisitionDocument.setDeliveryBuildingRoomNumber(pdoc.getDeliveryBuildingRoomNumber());
        requisitionDocument.setDeliveryBuildingName(pdoc.getDeliveryBuildingName());
        requisitionDocument.setDeliveryCampusCode(pdoc.getDeliveryCampusCode());
        requisitionDocument.setDeliveryCityName(pdoc.getDeliveryCityName());
        requisitionDocument.setDeliveryCountryCode(pdoc.getDeliveryCountryCode());
        requisitionDocument.setDeliveryInstructionText(pdoc.getDeliveryInstructionText());
        requisitionDocument.setDeliveryBuildingLine1Address(pdoc.getDeliveryBuildingLine1Address());
        requisitionDocument.setDeliveryBuildingLine2Address(pdoc.getDeliveryBuildingLine2Address());
        requisitionDocument.setDeliveryPostalCode(pdoc.getDeliveryPostalCode());
        requisitionDocument.setDeliveryRequiredDate(pdoc.getDeliveryRequiredDate());
        requisitionDocument.setDeliveryRequiredDateReasonCode(pdoc.getDeliveryRequiredDateReasonCode());
        requisitionDocument.setDeliveryStateCode(pdoc.getDeliveryStateCode());
        requisitionDocument.setDeliveryToEmailAddress(pdoc.getDeliveryToEmailAddress());
        requisitionDocument.setDeliveryToName(pdoc.getDeliveryToName());
        requisitionDocument.setDeliveryToPhoneNumber(pdoc.getDeliveryToPhoneNumber());
        requisitionDocument.setDeliveryBuildingOtherIndicator(pdoc.isDeliveryBuildingOtherIndicator());

        requisitionDocument.setPurchaseOrderBeginDate(pdoc.getPurchaseOrderBeginDate());
        requisitionDocument.setPurchaseOrderCostSourceCode(pdoc.getPurchaseOrderCostSourceCode());
        requisitionDocument.setPostingYear(pdoc.getPostingYear());
        requisitionDocument.setPurchaseOrderEndDate(pdoc.getPurchaseOrderEndDate());
        requisitionDocument.setChartOfAccountsCode(pdoc.getChartOfAccountsCode());
        requisitionDocument.setDocumentFundingSourceCode(pdoc.getDocumentFundingSourceCode());
        requisitionDocument.setInstitutionContactEmailAddress(pdoc.getInstitutionContactEmailAddress());
        requisitionDocument.setInstitutionContactName(pdoc.getInstitutionContactName());
        requisitionDocument.setInstitutionContactPhoneNumber(pdoc.getInstitutionContactPhoneNumber());
        requisitionDocument.setNonInstitutionFundAccountNumber(pdoc.getNonInstitutionFundAccountNumber());
        requisitionDocument.setNonInstitutionFundChartOfAccountsCode(pdoc.getNonInstitutionFundChartOfAccountsCode());
        requisitionDocument.setNonInstitutionFundOrgChartOfAccountsCode(pdoc.getNonInstitutionFundOrgChartOfAccountsCode());
        requisitionDocument.setNonInstitutionFundOrganizationCode(pdoc.getNonInstitutionFundOrganizationCode());
        requisitionDocument.setOrganizationCode(pdoc.getOrganizationCode());
        requisitionDocument.setRecurringPaymentTypeCode(pdoc.getRecurringPaymentTypeCode());
        requisitionDocument.setRequestorPersonEmailAddress(pdoc.getRequestorPersonEmailAddress());
        requisitionDocument.setRequestorPersonName(pdoc.getRequestorPersonName());
        requisitionDocument.setRequestorPersonPhoneNumber(pdoc.getRequestorPersonPhoneNumber());
//        requisitionDocument.setPurapDocumentIdentifier(pdoc.getRequisitionIdentifier());
        requisitionDocument.setPurchaseOrderTotalLimit(pdoc.getPurchaseOrderTotalLimit());
        requisitionDocument.setPurchaseOrderTransmissionMethodCode(pdoc.getPurchaseOrderTransmissionMethodCode());
        requisitionDocument.setUseTaxIndicator(pdoc.isUseTaxIndicator());
        requisitionDocument.setPurchaseOrderTypeId(pdoc.getPurchaseOrderTypeId());
        /*requisitionDocument.setLicensingRequirementIndicator(pdoc.isLicensingRequirementIndicator());*/
        /*requisitionDocument.setLicensingRequirementCode(pdoc.getLicensingRequirementCode());*/
        requisitionDocument.setVendorCityName(pdoc.getVendorCityName());
        requisitionDocument.setVendorContractGeneratedIdentifier(pdoc.getVendorContractGeneratedIdentifier());
        requisitionDocument.setVendorCountryCode(pdoc.getVendorCountryCode());
        requisitionDocument.setVendorCustomerNumber(pdoc.getVendorCustomerNumber());
        requisitionDocument.setVendorAttentionName(pdoc.getVendorAttentionName());
        requisitionDocument.setVendorDetailAssignedIdentifier(pdoc.getVendorDetailAssignedIdentifier());
        requisitionDocument.setVendorFaxNumber(pdoc.getVendorFaxNumber());
        requisitionDocument.setVendorHeaderGeneratedIdentifier(pdoc.getVendorHeaderGeneratedIdentifier());
        requisitionDocument.setVendorLine1Address(pdoc.getVendorLine1Address());
        requisitionDocument.setVendorLine2Address(pdoc.getVendorLine2Address());
        requisitionDocument.setVendorAddressInternationalProvinceName(pdoc.getVendorAddressInternationalProvinceName());
        requisitionDocument.setVendorName(pdoc.getVendorName());
        requisitionDocument.setVendorNoteText(pdoc.getVendorNoteText());
        requisitionDocument.setVendorPhoneNumber(pdoc.getVendorPhoneNumber());
        requisitionDocument.setVendorPostalCode(pdoc.getVendorPostalCode());
        requisitionDocument.setVendorStateCode(pdoc.getVendorStateCode());
        requisitionDocument.setVendorRestrictedIndicator(pdoc.getVendorRestrictedIndicator());

        requisitionDocument.setExternalOrganizationB2bSupplierIdentifier(pdoc.getExternalOrganizationB2bSupplierIdentifier());
        requisitionDocument.setRequisitionSourceCode(pdoc.getRequisitionSourceCode());
        requisitionDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(pdoc.getAccountsPayablePurchasingDocumentLinkIdentifier());
        requisitionDocument.setReceivingDocumentRequiredIndicator(pdoc.isReceivingDocumentRequiredIndicator());
        requisitionDocument.setPaymentRequestPositiveApprovalIndicator(pdoc.isPaymentRequestPositiveApprovalIndicator());
        /*requisitionDocument.setLicensingRequirementIndicator(pdoc.isLicensingRequirementIndicator());*/
        /*requisitionDocument.setLicensingRequirementCode(pdoc.getLicensingRequirementCode());*/

//        requisitionDocument.setStatusCode(PurapConstants.PurchaseOrderStatuses.IN_PROCESS);

        // Copy items from requisition (which will copy the item's accounts and capital assets)
        List<OleRequisitionItem> items = new ArrayList();
        for (OlePurchaseOrderItem reqItem : (List<OlePurchaseOrderItem>) pdoc.getItems()) {
            PurchasingCapitalAssetItem reqCamsItem = null;
            OleRequisitionItem ritem = getRequisitionItem(reqItem, requisitionDocument, reqCamsItem);

            reqCamsItem = pdoc.
                    getPurchasingCapitalAssetItemByItemIdentifier(reqItem.getItemIdentifier().intValue());
            ritem = populateCapitalAssetItem(ritem, requisitionDocument, reqCamsItem);
            items.add(ritem);
        }
        requisitionDocument.setItems(items);

        // Copy capital asset information that is directly off the document.
        requisitionDocument.setCapitalAssetSystemTypeCode(pdoc.getCapitalAssetSystemTypeCode());
        requisitionDocument.setCapitalAssetSystemStateCode(pdoc.getCapitalAssetSystemStateCode());
        requisitionDocument.setStatusCode(PurapConstants.RequisitionStatuses.APPDOC_CLOSED);


        for (CapitalAssetSystem capitalAssetSystem : pdoc.getPurchasingCapitalAssetSystems()) {
            RequisitionCapitalAssetSystem item = new RequisitionCapitalAssetSystem(capitalAssetSystem);
            item.setPurapDocumentIdentifier(requisitionDocument.getPurapDocumentIdentifier());
            requisitionDocument.getPurchasingCapitalAssetSystems().add(new RequisitionCapitalAssetSystem(capitalAssetSystem));
        }

//        requisitionDocument.fixItemReferences();
        return requisitionDocument;
    }

    private OleRequisitionItem getRequisitionItem(OlePurchaseOrderItem pitem, RequisitionDocument requisitionDocument, PurchasingCapitalAssetItem reqCamsItem) {
        OleRequisitionItem ritem = new OleRequisitionItem();
        ritem.setRequisition(requisitionDocument);
        SequenceAccessorService sas = SpringContext.getBean(SequenceAccessorService.class);
        Integer itemIdentifier = sas.getNextAvailableSequenceNumber("REQS_ITM_ID", RequisitionDocument.class).intValue();
        ritem.setItemIdentifier(itemIdentifier);
        ritem.setItemLineNumber(pitem.getItemLineNumber());
        ritem.setItemUnitOfMeasureCode(pitem.getItemUnitOfMeasureCode());
        ritem.setItemQuantity(pitem.getItemQuantity());
        ritem.setItemCatalogNumber(pitem.getItemCatalogNumber());
        ritem.setItemDescription(pitem.getItemDescription());
        ritem.setItemUnitPrice(pitem.getItemUnitPrice());
        ritem.setItemAuxiliaryPartIdentifier(pitem.getItemAuxiliaryPartIdentifier());
        ritem.setItemAssignedToTradeInIndicator(pitem.getItemAssignedToTradeInIndicator());
        ritem.setItemTaxAmount(pitem.getItemTaxAmount());
        ritem.setItemTitleId(pitem.getItemTitleId());

        for (PurApItemUseTax useTaxItem : pitem.getUseTaxItems()) {
            useTaxItem.setItemIdentifier(itemIdentifier);
            ritem.getUseTaxItems().add(useTaxItem);
        }

        ritem.setExternalOrganizationB2bProductReferenceNumber(pitem.getExternalOrganizationB2bProductReferenceNumber());
        ritem.setExternalOrganizationB2bProductTypeName(pitem.getExternalOrganizationB2bProductTypeName());
        ritem.setItemTypeCode(pitem.getItemTypeCode());

        if (pitem.getSourceAccountingLines() != null && pitem.getSourceAccountingLines().size() > 0 &&
                !StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ORDER_DISCOUNT_CODE, pitem.getItemType().getItemTypeCode())) {
            List accounts = new ArrayList();
            for (PurApAccountingLine account : pitem.getSourceAccountingLines()) {
                PurApAccountingLineBase poAccount = (PurApAccountingLineBase) account;
                RequisitionAccount acc = convertAccount(poAccount, ritem);
                accounts.add(acc);
            }
            ritem.setSourceAccountingLines(accounts);
        }
        ritem.setPurchasingCommodityCode(pitem.getPurchasingCommodityCode());
        ritem.setCommodityCode(pitem.getCommodityCode());

        if (ObjectUtils.isNotNull(reqCamsItem)) {
            RequisitionCapitalAssetItem item = new RequisitionCapitalAssetItem(reqCamsItem, ritem.getItemIdentifier());
            requisitionDocument.getPurchasingCapitalAssetItems().add(item);
        }
        return ritem;
    }

    private OleRequisitionItem populateCapitalAssetItem(OleRequisitionItem ritem, RequisitionDocument requisitionDocument, PurchasingCapitalAssetItem reqCamsItem) {
        if (ObjectUtils.isNotNull(reqCamsItem)) {
            RequisitionCapitalAssetItem item = new RequisitionCapitalAssetItem(reqCamsItem, ritem.getItemIdentifier());
            requisitionDocument.getPurchasingCapitalAssetItems().add(item);
        }
        return ritem;
    }

    private RequisitionAccount convertAccount(PurApAccountingLine account, RequisitionItem requisitionItem) {
        RequisitionAccount result = new RequisitionAccount();
        result.setAccountLinePercent(account.getAccountLinePercent());
        result.setAccountNumber(account.getAccountNumber());
        result.setChartOfAccountsCode(account.getChartOfAccountsCode());
        result.setFinancialObjectCode(account.getFinancialObjectCode());
        result.setFinancialSubObjectCode(account.getFinancialSubObjectCode());
        result.setOrganizationReferenceId(account.getOrganizationReferenceId());
        result.setProjectCode(account.getProjectCode());
        result.setSubAccountNumber(account.getSubAccountNumber());
        result.setRequisitionItem(requisitionItem);
        return result;
    }

    private RequisitionCapitalAssetItem convert(PurchaseOrderCapitalAssetItem pitem) {
        RequisitionCapitalAssetItem result = new RequisitionCapitalAssetItem();
        result.setCapitalAssetItemIdentifier(pitem.getCapitalAssetItemIdentifier());
        result.setCapitalAssetSystemIdentifier(pitem.getCapitalAssetSystemIdentifier());
        result.setCapitalAssetTransactionTypeCode(pitem.getCapitalAssetTransactionTypeCode());
        result.setItemIdentifier(pitem.getItemIdentifier());
        result.setPurapDocumentIdentifier(pitem.getPurchasingDocument().getPurapDocumentIdentifier());
        return result;
    }


    public void setBusinessObjectService(BusinessObjectService boService) {
        this.businessObjectService = boService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public void setRequisitionDao(RequisitionDao requisitionDao) {
        this.requisitionDao = requisitionDao;
    }

    public void setPurapService(PurapService purapService) {
        this.purapService = purapService;
    }

    public KualiRuleService getRuleService() {
        return ruleService;
    }

    public void setRuleService(KualiRuleService ruleService) {
        this.ruleService = ruleService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public void setUniversityDateService(UniversityDateService universityDateService) {
        this.universityDateService = universityDateService;
    }

    public void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    public void setCapitalAssetBuilderModuleService(CapitalAssetBuilderModuleService capitalAssetBuilderModuleService) {
        this.capitalAssetBuilderModuleService = capitalAssetBuilderModuleService;
    }

    public void setPostalCodeValidationService(PostalCodeValidationService postalCodeValidationService) {
        this.postalCodeValidationService = postalCodeValidationService;
    }

    /**
     * @return Returns the personService.
     */
    protected PersonService getPersonService() {
        if (personService == null) {
            personService = SpringContext.getBean(PersonService.class);
        }
        return personService;
    }

}
