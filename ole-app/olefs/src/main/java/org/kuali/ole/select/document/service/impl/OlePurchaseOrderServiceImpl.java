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


import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PurchaseOrderStatuses;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.PurchaseOrderAmendmentDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.*;
import org.kuali.ole.module.purap.document.service.impl.PurchaseOrderServiceImpl;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OlePurchaseOrderAccount;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OlePurchaseOrderService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.select.document.validation.event.CopiesPurchaseOrderEvent;
import org.kuali.ole.select.document.validation.event.DiscountPurchaseOrderEvent;
import org.kuali.ole.select.document.validation.event.ForeignCurrencyPOEvent;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.DocumentSystemSaveEvent;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeIndexingQueue;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AutoPopulatingList;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

@Transactional
public class OlePurchaseOrderServiceImpl extends PurchaseOrderServiceImpl implements OlePurchaseOrderService {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurchaseOrderServiceImpl.class);
    // private DocumentService documentService;

    private ConfigurationService kualiConfigurationService;
    private PrintService printService;
    private PurApWorkflowIntegrationService purapWorkflowIntegrationService;
    private OleSelectDocumentService oleSelectDocumentService;
    private boolean currencyTypeIndicator = true;
    private KualiRuleService kualiRuleService;

    @Override
    public void setPrintService(PrintService printService) {
        this.printService = printService;
    }

    @Override
    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    @Override
    public void setPurapWorkflowIntegrationService(PurApWorkflowIntegrationService purapWorkflowIntegrationService) {
        this.purapWorkflowIntegrationService = purapWorkflowIntegrationService;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.PurchaseOrderService#createAutomaticPurchaseOrderDocument(org.kuali.ole.module.purap.document.RequisitionDocument)
     */
    @Override
    public void createAutomaticPurchaseOrderDocument(RequisitionDocument reqDocument) {
        String newSessionUserId = getOleSelectDocumentService().getSelectParameterValue(OLEConstants.SYSTEM_USER);
        try {
            LogicContainer logicToRun = new LogicContainer() {
                @Override
                public Object runLogic(Object[] objects) throws Exception {
                    RequisitionDocument doc = (RequisitionDocument) objects[0];
                    // update REQ data
                    doc.setPurchaseOrderAutomaticIndicator(Boolean.TRUE);
                    // create PO and populate with default data
                    PurchaseOrderDocument po = generatePurchaseOrderFromRequisition(doc);
                    po.setDefaultValuesForAPO();
                    po.setContractManagerCode(PurapConstants.APO_CONTRACT_MANAGER);
                    populatePOValuesFromProfileAttributes(doc,po);
                    String purchaseOrderType = "";
                    if (doc.getPurchaseOrderTypeId() != null) {
                        Map purchaseOrderTypeIdMap = new HashMap();
                        purchaseOrderTypeIdMap.put("purchaseOrderTypeId", doc.getPurchaseOrderTypeId());
                        org.kuali.rice.krad.service.BusinessObjectService
                                businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
                        LOG.debug("before calling findMatching");
                        List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) businessObjectService.findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);
                        LOG.debug("after calling findMatching");
                        if (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0) {
                            PurchaseOrderType purchaseOrderTypeDoc = purchaseOrderTypeDocumentList.get(0);
                            purchaseOrderType = purchaseOrderTypeDoc.getPurchaseOrderType();
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("purchaseOrderType >>>>>>>>>>>" + purchaseOrderType);
                            LOG.debug("purchaseOrder DocumentNumber >>>>>>>>>>>" + po.getDocumentNumber());
                        }
                        if (purchaseOrderType != null ) {
                                /*&& !(po.isLicensingRequirementIndicator())) {*/
                            LOG.debug("before calling document service impl");
                            documentService.routeDocument(po, null, null);
                            LOG.debug("after calling document service impl ");
                        } else {
                            documentService.saveDocument(po, DocumentSystemSaveEvent.class);
                        }
                    }
                    final DocumentAttributeIndexingQueue documentAttributeIndexingQueue = KewApiServiceLocator.getDocumentAttributeIndexingQueue();
                    documentAttributeIndexingQueue.indexDocument(po.getDocumentNumber());
                    return null;
                }
            };
            purapService.performLogicWithFakedUserSession(newSessionUserId, logicToRun, new Object[]{reqDocument});
        } catch (WorkflowException e) {
            String errorMsg = "Workflow Exception caught: " + e.getLocalizedMessage();
            LOG.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void populatePOValuesFromProfileAttributes(RequisitionDocument doc,PurchaseOrderDocument po){
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(org.kuali.ole.OLEConstants.DATA_CARRIER_SERVICE);
        OleTxRecord oleTxRecord = (OleTxRecord)dataCarrierService.getData(org.kuali.ole.OLEConstants.OLE_TX_RECORD);
        if(oleTxRecord != null && doc.getRequisitionSource() != null && doc.getRequisitionSource().getRequisitionSourceCode().equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST)){
            po.setPurchaseOrderConfirmedIndicator(oleTxRecord.isPurchaseOrderConfirmationIndicator());
            if(oleTxRecord.getVendorChoice() != null){
                po.setPurchaseOrderVendorChoiceCode(oleTxRecord.getVendorChoice());
            }
            if(oleTxRecord.getAssignToUser() != null){
                po.setAssignedUserPrincipalName(oleTxRecord.getAssignToUser());
            }
        }
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.PurchaseOrderService#performPurchaseOrderFirstTransmitViaPrinting(java.lang.String,
     *      java.io.ByteArrayOutputStream)
     */
    @Override
    public void performPurchaseOrderFirstTransmitViaPrinting(String documentNumber, ByteArrayOutputStream baosPDF) {
        PurchaseOrderDocument po = getPurchaseOrderByDocumentNumber(documentNumber);
        String environment = kualiConfigurationService.getPropertyValueAsString(OLEConstants.ENVIRONMENT_KEY);
        Collection<String> generatePDFErrors = printService.generatePurchaseOrderPdf(po, baosPDF, environment, null);
        if (!generatePDFErrors.isEmpty()) {
            addStringErrorMessagesToMessageMap(PurapKeyConstants.ERROR_PURCHASE_ORDER_PDF, generatePDFErrors);
            throw new ValidationException("printing purchase order for first transmission failed");
        }
        Timestamp currentDate = dateTimeService.getCurrentTimestamp();
        po.setPurchaseOrderFirstTransmissionTimestamp(currentDate);
        po.setPurchaseOrderLastTransmitTimestamp(currentDate);
        po.setOverrideWorkflowButtons(Boolean.FALSE);
        boolean performedAction = purapWorkflowIntegrationService.takeAllActionsForGivenCriteria(po, "Action taken automatically as part of document initial print transmission", PurapConstants.PurchaseOrderStatuses.NODE_DOCUMENT_TRANSMISSION, GlobalVariables.getUserSession().getPerson(), null);
        if(!performedAction) {
            purchaseOrderDao.deleteFromWorkflowItem(documentNumber);
        }
        performedAction=true;
        po.setApplicationDocumentStatus(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN);
        if (!performedAction) {
            Person systemUserPerson = getPersonService().getPersonByPrincipalName(getOleSelectDocumentService().getSelectParameterValue(OLEConstants.SYSTEM_USER));
            purapWorkflowIntegrationService.takeAllActionsForGivenCriteria(po, "Action taken automatically as part of document initial print transmission by user " + GlobalVariables.getUserSession().getPerson().getName(), PurapConstants.PurchaseOrderStatuses.NODE_DOCUMENT_TRANSMISSION, systemUserPerson, getOleSelectDocumentService().getSelectParameterValue(OLEConstants.SYSTEM_USER));
        }
        po.setOverrideWorkflowButtons(Boolean.TRUE);
        if (!po.getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN)) {
            attemptSetupOfInitialOpenOfDocument(po);
        }

        purapService.saveDocumentNoValidation(po);
    }

    @Override
    public void purchaseOrderFirstTransmitViaPrinting(String documentNumber, ByteArrayOutputStream baosPDF) {
        PurchaseOrderDocument po = getPurchaseOrderByDocumentNumber(documentNumber);
        String environment = kualiConfigurationService.getPropertyValueAsString(OLEConstants.ENVIRONMENT_KEY);
        Collection<String> generatePDFErrors = printService.generatePurchaseOrderPdf(po, baosPDF, environment, null);
        LOG.error("PDF Errors" + generatePDFErrors);
        if (!generatePDFErrors.isEmpty()) {
            addStringErrorMessagesToMessageMap(PurapKeyConstants.ERROR_PURCHASE_ORDER_PDF, generatePDFErrors);
            throw new ValidationException("printing purchase order for first transmission failed");
        }
    }


    /**
     * If the status of the purchase order is not OPEN and the initial open date is null, sets the initial open date to current date
     * and update the status to OPEN, then save the purchase order.
     *
     * @param po The purchase order document whose initial open date and status we want to update.
     */
    @Override
    protected void attemptSetupOfInitialOpenOfDocument(PurchaseOrderDocument po) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("attemptSetupOfInitialOpenOfDocument() started using document with doc id " + po.getDocumentNumber());
        }

        if (!PurchaseOrderStatuses.APPDOC_OPEN.equals(po.getApplicationDocumentStatus())) {
            if (OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_AMENDMENT.equalsIgnoreCase(po.getDocumentHeader().getWorkflowDocument().getDocumentTypeName())) {
                po.setPurchaseOrderInitialOpenTimestamp(null);
            }
            if (ObjectUtils.isNull(po.getPurchaseOrderInitialOpenTimestamp())) {
                LOG.debug("attemptSetupOfInitialOpenOfDocument() setting initial open date on document");
                po.setPurchaseOrderInitialOpenTimestamp(dateTimeService.getCurrentTimestamp());
            } else {
                throw new RuntimeException("Document does not have status code '" + PurchaseOrderStatuses.APPDOC_OPEN + "' on it but value of initial open date is " + po.getPurchaseOrderInitialOpenTimestamp());
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("attemptSetupOfInitialOpenOfDocument() Setting po document id " + po.getDocumentNumber() + " status from '" + po.getApplicationDocumentStatus() + "' to '" + PurchaseOrderStatuses.APPDOC_OPEN + "'");
            }
            po.setApplicationDocumentStatus(PurchaseOrderStatuses.APPDOC_OPEN);
            // no need to save here because calling class should handle the save if needed
        } /*else {
            LOG.error("attemptSetupOfInitialOpenOfDocument() Found document already in '" + PurchaseOrderStatuses.APPDOC_OPEN + "' status for PO#" + po.getPurapDocumentIdentifier() + "; will not change or update");
        }*/
    }

    //setting print button for PO amendment document
    @Override
    public void setStatusCompletePurchaseOrderAmendment(PurchaseOrderDocument poa) {

        LOG.debug("setStatusCompletePurchaseOrderAmendment() started");
        setupDocumentForPendingFirstTransmission(poa);
        if (!PurchaseOrderStatuses.STATUSES_BY_TRANSMISSION_TYPE.values().contains(poa.getApplicationDocumentStatus())) {
            attemptSetupOfInitialOpenOfDocument(poa);
        } else if (PurchaseOrderStatuses.APPDOC_PENDING_PRINT.equals(poa.getApplicationDocumentStatus())) {
            // default to using user that routed PO
            String userToRouteFyi = poa.getDocumentHeader().getWorkflowDocument().getRoutedByPrincipalId();
            // Below code commented to fix issue in POA Screen when changing from NO PRINT to PRINT by Suresh Subramanian
        /*if (poa.getPurchaseOrderAutomaticIndicator()) {
            // if APO, use the user that initiated the requisition
            RequisitionDocument req = SpringContext.getBean(RequisitionService.class).getRequisitionById(poa.getRequisitionIdentifier());
           userToRouteFyi = req.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId();
        }
*/
            // send FYI to user for printing
            poa.getDocumentHeader().getWorkflowDocument().adHocToPrincipal(ActionRequestType.FYI, poa.getDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator().next(), "This POA is ready for printing and distribution.", userToRouteFyi, "", true, "PRINT");
        }
    }

    @Override
    public void completePurchaseOrderAmendment(PurchaseOrderDocument poa) {
        LOG.debug("completePurchaseOrderAmendment() started");

        setCurrentAndPendingIndicatorsForApprovedPODocuments(poa);

        if (SpringContext.getBean(PaymentRequestService.class).hasActivePaymentRequestsForPurchaseOrder(poa.getPurapDocumentIdentifier())) {
            poa.setPaymentRequestPositiveApprovalIndicator(true);
            poa.setReceivingDocumentRequiredIndicator(false);
        }
        // check thresholds to see if receiving is required for purchase order amendment
        else if (!poa.isReceivingDocumentRequiredIndicator()) {
            setReceivingRequiredIndicatorForPurchaseOrder(poa);
        }

        // if unordered items have been added to the PO then send an FYI to all fiscal officers
       /* if (hasNewUnorderedItem(poa)) {
            sendFyiForNewUnorderedItems(poa);
        }*/
        DocumentRouteHeaderValue routeHeader = SpringContext.getBean(RouteHeaderService.class).getRouteHeader(poa.getDocumentNumber());
        String status = routeHeader.getDocRouteStatus();
        if (status.equals(KewApiConstants.ROUTE_HEADER_PROCESSED_CD)) {
            List<PurApItem> items = poa.getItems();
            for (PurApItem item : items) {
                initiateTransmission(poa, item);
            }
            //initiateTransmission(poa);
        }

    }
    /**
     * Gets the documentService attribute.
     * @return Returns the documentService.
     */
    // public DocumentService getDocumentService() {
    //     return documentService;
    // }


    /**
     * Sets the documentService attribute value.
     * @param documentService The documentService to set.
     */
    //  public void setDocumentService(DocumentService documentService) {
    //      this.documentService = documentService;
    //  }


    /**
     * @see org.kuali.ole.module.purap.document.service.PurchaseOrderService#createPurchaseOrderDocument(org.kuali.ole.module.purap.document.RequisitionDocument,
     *      java.lang.String, java.lang.Integer)
     */
    @Override
    public PurchaseOrderDocument createPurchaseOrderDocument(RequisitionDocument reqDocument, String newSessionUserId, Integer contractManagerCode) {
        try {
            LogicContainer logicToRun = new LogicContainer() {
                @Override
                public Object runLogic(Object[] objects) throws Exception {
                    RequisitionDocument doc = (RequisitionDocument) objects[0];
                    PurchaseOrderDocument po = generatePurchaseOrderFromRequisition(doc);
                    Integer cmCode = (Integer) objects[1];
                    po.setContractManagerCode(cmCode);
                    String paramName = PurapParameterConstants.DEFAULT_B2B_VENDOR_CHOICE;
                    String paramValue = SpringContext.getBean(ParameterService.class).getParameterValueAsString(PurchaseOrderDocument.class, paramName);
                    po.setPurchaseOrderVendorChoiceCode(paramValue);
                    purapService.saveDocumentNoValidation(po);
                    return po;
                }
            };
            return (PurchaseOrderDocument) purapService.performLogicWithFakedUserSession(newSessionUserId, logicToRun, new Object[]{reqDocument, contractManagerCode});
        } catch (WorkflowException e) {
            String errorMsg = "Workflow Exception caught: " + e.getLocalizedMessage();
            LOG.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createPurchaseOrderAmendmentDocument(OlePurchaseOrderDocument olePurchaseOrderDocument,String docNumber) {
        String errorString = null;
        String newStatus = PurapConstants.PurchaseOrderStatuses.APPDOC_AMENDMENT;
        String annotation = "Amended in batch by PO document "+olePurchaseOrderDocument.getDocumentNumber()+" and POBA document "+docNumber;
        String documentType = "OLE_POA";
        //PurchaseOrderDocument document = getPurchaseOrderByDocumentNumber(olePurchaseOrderDocument.getDocumentNumber());
        calculate(olePurchaseOrderDocument);
        createNoteForAutoCloseOrders(olePurchaseOrderDocument, annotation);
        try {
            synchronized (this) {
                createAndRoutePotentialChangeDocument(olePurchaseOrderDocument, documentType, annotation, null, newStatus);
            }

        }  catch (Exception e) {
            Map<String, AutoPopulatingList<ErrorMessage>> errorMessages = GlobalVariables.getMessageMap().getErrorMessages();
            for (Iterator<String> errorMessageIterator = errorMessages.keySet().iterator(); errorMessageIterator.hasNext(); ) {
                String errorKey = errorMessageIterator.next();
                AutoPopulatingList<ErrorMessage> errorList = errorMessages.get(errorKey);
                for (ErrorMessage errorMessage : errorList) {
                    if(StringUtils.isNotEmpty(errorString)){
                        errorString += MessageFormat.format(kualiConfigurationService.getPropertyValueAsString(errorMessage.getErrorKey()), errorMessage.getMessageParameters());
                        errorString += "\n";
                   }else{
                        errorString = MessageFormat.format(kualiConfigurationService.getPropertyValueAsString(errorMessage.getErrorKey()), errorMessage.getMessageParameters());
                        errorString += "\n";
                    }
                }

            }
            GlobalVariables.getMessageMap().clearErrorMessages();
        }
        return errorString;

    }

    public boolean getIsATypeOfRCVGDoc() {
        return false;
    }

    public boolean getIsATypeOfCORRDoc() {
        return false;
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



    public void calculate(OlePurchaseOrderDocument olePurchaseOrderDocument) {

        List<PurApItem> purApItems = olePurchaseOrderDocument.getItems();
        for(PurApItem purApItem:purApItems){
            List<KualiDecimal> existingAmount=new ArrayList<>();
            for(PurApAccountingLine oldSourceAccountingLine:purApItem.getSourceAccountingLines()) {
                if(oldSourceAccountingLine instanceof OlePurchaseOrderAccount) {
                    if(((OlePurchaseOrderAccount)oldSourceAccountingLine).getExistingAmount()!=null){
                        existingAmount.add(((OlePurchaseOrderAccount)oldSourceAccountingLine).getExistingAmount());
                    }
                }
            }
            int count=0;
            for(PurApAccountingLine account:purApItem.getSourceAccountingLines()){

                if (ObjectUtils.isNotNull(account.getAccountLinePercent()) || ObjectUtils.isNotNull(account.getAmount())) {
                    if (account.getAmount()!=null&&count<existingAmount.size()&&existingAmount.size() != 0 && !existingAmount.get(count).toString().equals(account.getAmount().toString())) {
                        KualiDecimal calculatedPercent = new KualiDecimal(account.getAmount().multiply(new KualiDecimal(100)).divide(purApItem.getTotalAmount()).toString());
                        account.setAccountLinePercent(calculatedPercent.bigDecimalValue().setScale(OLEConstants.BIG_DECIMAL_SCALE, BigDecimal.ROUND_CEILING));
                    }
                    else {
                        if(account.getAccountLinePercent().intValue()==100&&(account.getAmount()==null||account.getAccount()!=null)){
                            KualiDecimal calculatedAmount = new KualiDecimal(account.getAccountLinePercent().multiply(purApItem.getTotalAmount().bigDecimalValue()).divide(new BigDecimal(100)).toString());
                            account.setAmount(calculatedAmount);
                        }
                        else{
                            KualiDecimal calculatedAmount = new KualiDecimal(account.getAccountLinePercent().multiply(purApItem.getTotalAmount().bigDecimalValue()).divide(new BigDecimal(100)).toString());
                            account.setAmount(calculatedAmount);
                        }
                    }
                }
                count++;
            }
            for(PurApAccountingLine oldSourceAccountingLine:purApItem.getSourceAccountingLines()) {
                if(oldSourceAccountingLine instanceof OlePurchaseOrderAccount) {
                    ((OlePurchaseOrderAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
                }
            }
        }


        PurchaseOrderDocument purchaseDoc = olePurchaseOrderDocument;
        List<OlePurchaseOrderItem> purItem = purchaseDoc.getItems();
        if (purchaseDoc.getVendorDetail().getCurrencyType()!=null){
            if(purchaseDoc.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }
        PurchaseOrderDocument purDoc = olePurchaseOrderDocument;
        if (purDoc.getVendorDetail() == null || (purDoc.getVendorDetail() != null && currencyTypeIndicator)) {
            for (int i = 0; purDoc.getItems().size() > i; i++) {
                OlePurchaseOrderItem item = (OlePurchaseOrderItem) purDoc.getItem(i);
                if ((item.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    boolean rulePassed = getKualiRuleService().applyRules(new DiscountPurchaseOrderEvent(purchaseDoc, item));
                    if (rulePassed) {
                        item.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    rulePassed = getKualiRuleService().applyRules(new CopiesPurchaseOrderEvent(purDoc, item));
                }

            }
        } else {
            LOG.debug("###########Foreign Currency Field Calculation in olepurchaseOrder action###########");
            BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
            for (int i = 0; purItem.size() > i; i++) {
                OlePurchaseOrderItem items = (OlePurchaseOrderItem) purchaseDoc.getItem(i);
                if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    boolean rulePassed = getKualiRuleService().applyRules(new ForeignCurrencyPOEvent(purchaseDoc, items));
                    if (rulePassed) {
                        SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(items);
                        Long id = purchaseDoc.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                        Map currencyTypeMap = new HashMap();
                        currencyTypeMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                        List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, currencyTypeMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                        Iterator iterator = exchangeRateList.iterator();
                        if (iterator.hasNext()) {
                            OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                            String documentNumber = purchaseDoc.getDocumentNumber();
                            Map documentNumberMap = new HashMap();
                            documentNumberMap.put(OLEPropertyConstants.DOCUMENT_NUMBER, documentNumber);
                            List<OlePurchaseOrderItem> currenctExchangeRateList = (List) businessObjectService.findMatching(OlePurchaseOrderItem.class, documentNumberMap);
                            Iterator iterate = currenctExchangeRateList.iterator();
                            if (iterate.hasNext()) {
                                OlePurchaseOrderItem tempCurrentExchangeRate = (OlePurchaseOrderItem) iterate.next();
                                String poCurrencyType = null;
                                if (tempCurrentExchangeRate.getPurchaseOrder().getVendorDetail().getCurrencyType() != null) {
                                    poCurrencyType = tempCurrentExchangeRate.getPurchaseOrder().getVendorDetail().getCurrencyType().getCurrencyType();
                                }
                                String poaCurrencyType = purchaseDoc.getVendorDetail().getCurrencyType().getCurrencyType();
                                if (poCurrencyType != null && (poCurrencyType.equalsIgnoreCase(poaCurrencyType)) && !items.isLatestExchangeRate() && !purchaseDoc.getIsPODoc() && ((purchaseDoc instanceof PurchaseOrderAmendmentDocument) )) {
                                    items.setItemExchangeRate(tempCurrentExchangeRate.getItemExchangeRate());
                                } else {

                                    items.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                                }
                            }
                            if (items.getItemExchangeRate() != null && items.getItemForeignUnitCost() != null) {
                                items.setItemUnitCostUSD(new KualiDecimal(items.getItemForeignUnitCost().bigDecimalValue().divide(items.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                                items.setItemUnitPrice(items.getItemUnitCostUSD().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                                items.setItemListPrice(items.getItemUnitCostUSD());
                            }
                        }
                    }
                }
            }
        }

        List<PurApItem> newpurApItems = olePurchaseOrderDocument.getItems();
        for(PurApItem purApItem:newpurApItems){
            for(PurApAccountingLine account:purApItem.getSourceAccountingLines()){
                KualiDecimal calculatedAmount = new KualiDecimal(account.getAccountLinePercent().multiply(purApItem.getTotalAmount().bigDecimalValue()).divide(new BigDecimal(100)).toString());
                account.setAmount(calculatedAmount);
            }
        }



    }

    protected KualiRuleService getKualiRuleService() {
        if (kualiRuleService == null) {
            kualiRuleService = KRADServiceLocatorWeb.getKualiRuleService();
        }
        return this.kualiRuleService;
    }

}
