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
package org.kuali.ole.module.purap.document.web.struts;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.PurchasingDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.OleDefaultValueAssignment;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.select.document.service.OleRequisitionDocumentService;
import org.kuali.ole.select.document.validation.event.CopiesRequisitionEvent;
import org.kuali.ole.select.document.validation.event.DiscountRequisitionEvent;
import org.kuali.ole.select.document.validation.event.ForeignCurrencyRequisitionEvent;
import org.kuali.ole.select.document.web.struts.OleRequisitionForm;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLineBase;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AddAccountingLineEvent;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class OleRequisitionAction extends RequisitionAction {
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleRequisitionAction.class);
    private static transient ConfigurationService kualiConfigurationService;
    private boolean sufficientFundChecklag = true;
    private DocstoreClientLocator docstoreClientLocator;
    private static transient OlePurapService olePurapService;
    private OleRequisitionDocumentService oleRequisitionDocumentService;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public static OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public OleRequisitionDocumentService getOleRequisitionDocumentService() {
        if(oleRequisitionDocumentService == null) {
           return  SpringContext.getBean(OleRequisitionDocumentService.class);
        }

        return oleRequisitionDocumentService;
    }

    public static void setOlePurapService(OlePurapService olePurapService) {
        OleRequisitionAction.olePurapService = olePurapService;
    }
    /**
     * Does initialization for a new requisition.
     *
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#createDocument(org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase)
     */
    @Override
    protected void createDocument(KualiDocumentFormBase kualiDocumentFormBase) throws WorkflowException {
        LOG.debug("Inside the OleRequisitionAction class Create Document");
        super.createDocument(kualiDocumentFormBase);
        OleRequisitionForm oleRequisitionForm = (OleRequisitionForm) kualiDocumentFormBase;
        OleRequisitionItem oleRequisitionItem = (OleRequisitionItem) oleRequisitionForm.getNewPurchasingItemLine();
        ((RequisitionDocument) kualiDocumentFormBase.getDocument()).initiateDocument();
        RequisitionDocument requisitionDocument = (RequisitionDocument) kualiDocumentFormBase.getDocument();
        requisitionDocument.setPaymentTypeCode(getOleRequisitionDocumentService().getParameter(OLEConstants.RECURRING_PAY_TYP));
        requisitionDocument.setItemLocationForFixed(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_LOCATION_FIRM_FIXD));
        requisitionDocument.setItemLocationForApproval(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_LOCATION_APPROVAL));
        requisitionDocument.setItemStatusForFixed(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_STATUS_FIRM_FIXD));
        requisitionDocument.setItemStatusForApproval(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_STATUS_APPROVAL));
        requisitionDocument.setCopyNumber(getOleRequisitionDocumentService().getParameter(OLEConstants.COPY_NUMBER));
        requisitionDocument.setOrderTypes(getOleRequisitionDocumentService().getPurchaseOrderTypes());
        new OleDefaultValueAssignment(oleRequisitionForm.getDefaultDocumentTypeName(), oleRequisitionItem, requisitionDocument);
    }

    /**
     * Calculate the whole document.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */

    @Override
    public ActionForward calculate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside the OleRequisitionAction class Calculate");
        PurchasingAccountsPayableFormBase purchasingForm = (PurchasingAccountsPayableFormBase) form;
        List<PurApItem> purApItems = ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItems();
        for(PurApItem purApItem:purApItems){
            List<KualiDecimal> existingAmount=new ArrayList<>();
            for(PurApAccountingLine oldSourceAccountingLine:purApItem.getSourceAccountingLines()) {
                if(oldSourceAccountingLine instanceof OleRequisitionAccount) {
                    if(((OleRequisitionAccount)oldSourceAccountingLine).getExistingAmount()!=null){
                        existingAmount.add(((OleRequisitionAccount)oldSourceAccountingLine).getExistingAmount());
                    }
                }
            }
            int count=0;
            for(PurApAccountingLine account:purApItem.getSourceAccountingLines()){

                if (ObjectUtils.isNotNull(account.getAccountLinePercent()) || ObjectUtils.isNotNull(account.getAmount())) {
                    if (account.getAmount()!=null&&count<existingAmount.size()&&existingAmount.size() != 0 && !existingAmount.get(count).toString().equals(account.getAmount().toString())) {
                        KualiDecimal calculatedPercent = new KualiDecimal(account.getAmount().multiply(new KualiDecimal(100)).divide(purApItem.getTotalAmount()).toString());
                        account.setAccountLinePercent(calculatedPercent.bigDecimalValue().setScale(OLEConstants.BIG_DECIMAL_SCALE,BigDecimal.ROUND_CEILING));
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
                if(oldSourceAccountingLine instanceof OleRequisitionAccount) {
                    ((OleRequisitionAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
                }
            }
        }
        ActionForward forward = super.calculate(mapping, form, request, response);
        purchasingForm = (PurchasingAccountsPayableFormBase) form;
        PurchasingDocument purDoc = (PurchasingDocument) purchasingForm.getDocument();
        PurchasingFormBase formBase = (PurchasingFormBase) form;
        OleRequisitionDocument reqDoc = (OleRequisitionDocument) formBase.getDocument();
        boolean foreignCurrencyIndicator = false;

        List<OleRequisitionItem> item = reqDoc.getItems();
        if (purDoc.getVendorDetail()!=null) {
            foreignCurrencyIndicator = isForeignCurrency(purDoc.getVendorDetail().getCurrencyType());
        }
        if (purDoc.getVendorDetail() == null || (purDoc.getVendorDetail() != null && (!foreignCurrencyIndicator))) {
            for (int i = 0; purDoc.getItems().size() > i; i++) {
                OleRequisitionItem items = (OleRequisitionItem) purDoc.getItem(i);
                if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    boolean rulePassed = getKualiRuleService().applyRules(new DiscountRequisitionEvent(reqDoc, items));
                    if (rulePassed) {
                        items.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(items).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    rulePassed = getKualiRuleService().applyRules(new CopiesRequisitionEvent(reqDoc, items));
                }

            }
        } else {
            LOG.debug("###########Foreign Currency Field Calculation###########");
            for (int i = 0; item.size() > i; i++) {
                OleRequisitionItem items = (OleRequisitionItem) reqDoc.getItem(i);
                if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    boolean rulePassed = getKualiRuleService().applyRules(new ForeignCurrencyRequisitionEvent(reqDoc, items));
                    if (rulePassed) {
                        SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(items);
                        Long id = reqDoc.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                        Map documentNumberMap = new HashMap();
                        documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, id);
                        BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
                        List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                        Iterator iterator = exchangeRateList.iterator();
                        OleExchangeRate tempOleExchangeRate = null;
                        if (iterator.hasNext()) {
                            tempOleExchangeRate = (OleExchangeRate) iterator.next();
                            items.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                        }
                        if (items.getItemExchangeRate() != null && items.getItemForeignUnitCost() != null) {
                            items.setItemUnitCostUSD(new KualiDecimal(items.getItemForeignUnitCost().bigDecimalValue().divide(tempOleExchangeRate.getExchangeRate(), 4, RoundingMode.HALF_UP)));
                            items.setItemUnitPrice(items.getItemUnitCostUSD().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                            items.setItemListPrice(items.getItemUnitCostUSD());
                            items.setTotalAmount(items.getTotalAmount());
                        }
                    }
                }
            }
        }
        purchasingForm = (PurchasingAccountsPayableFormBase) form;
        List<PurApItem> newpurApItems = ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItems();
        for(PurApItem purApItem:newpurApItems){
            for(PurApAccountingLine account:purApItem.getSourceAccountingLines()){
                KualiDecimal calculatedAmount = new KualiDecimal(account.getAccountLinePercent().multiply(purApItem.getTotalAmount().bigDecimalValue()).divide(new BigDecimal(100)).toString());
                account.setAmount(calculatedAmount);
            }
        }
        forward = super.calculate(mapping, form, request, response);
        // formBase.setCalculated(true);

        // Added for jira OLE-964
        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        List<SourceAccountingLine> sourceAccountingLineList = reqDoc.getSourceAccountingLines();
        for (SourceAccountingLine accLine : sourceAccountingLineList) {
            String notificationOption = null;
            boolean sufficientFundCheck;
            Map<String, Object> key = new HashMap<String, Object>();
            String chartCode = accLine.getChartOfAccountsCode();
            String accNo = accLine.getAccountNumber();
            key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
            key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
            OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                    OleSufficientFundCheck.class, key);
            if (account != null) {
                notificationOption = account.getNotificationOption();
            }
            if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.BLOCK_USE)) {
                sufficientFundCheck = oleRequisitionDocumentService.hasSufficientFundsOnRequisition(accLine, notificationOption, SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear());
                if (sufficientFundCheck) {
                    GlobalVariables.getMessageMap().putError(
                            OLEConstants.SufficientFundCheck.ERROR_MSG_FOR_INSUFF_FUND, RiceKeyConstants.ERROR_CUSTOM,
                            OLEConstants.SufficientFundCheck.INSUFF_FUND_REQ + accLine.getAccountNumber());
                }
            }
        }
        // End
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside the OleRequisitionAcrion class Calculate" + formBase.getNewPurchasingItemLine().getItemUnitPrice());
        }
        return forward;
    }

    /**
     * Add a new item to the document.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    @Override
    public ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("###########Inside AddItem in oleRequisitionAction ###########");
        PurchasingFormBase purchasingForm = (PurchasingFormBase) form;
        OleRequisitionItem item = (OleRequisitionItem) purchasingForm.getNewPurchasingItemLine();
        OleRequisitionDocument document = (OleRequisitionDocument) purchasingForm.getDocument();
        OleRequisitionForm oleForm = (OleRequisitionForm) form;
        OleRequisitionDocument doc = (OleRequisitionDocument) oleForm.getDocument();
        Iterator itemIterator = doc.getItems().iterator();
        int itemCounter = 0;

        document.setVendorEnterKeyEvent(false);
        while (itemIterator.hasNext()) {
            OleRequisitionItem tempItem = (OleRequisitionItem) itemIterator.next();
            if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                itemCounter++;
            }
        }
        String itemNo = String.valueOf(itemCounter);
        BibInfoBean xmlBibInfoBean = new BibInfoBean();
        if (item.getBibInfoBean() == null) {
            item.setBibInfoBean(xmlBibInfoBean);
            if (item.getBibInfoBean().getDocStoreOperation() == null) {
                item.getBibInfoBean().setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_STAFF);
            }
        } else {
            if (item.getBibInfoBean().getDocStoreOperation() == null) {
                item.getBibInfoBean().setDocStoreOperation(OleSelectConstant.DOCSTORE_OPERATION_STAFF);
            }
        }
        String fileName = document.getDocumentNumber() + "_" + itemNo;
        // Modified for jira OLE - 2437 starts

        setItemDescription(item, fileName);
        if (document.getVendorDetail().getVendorHeader().getVendorForeignIndicator() != null) {
            if ((document.getVendorDetail() == null) || (document.getVendorDetail().getVendorName() != null && !document.getVendorDetail().getVendorHeader().getVendorForeignIndicator())) {
                purchasingForm.getNewPurchasingItemLine().setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP));
                super.addItem(mapping, purchasingForm, request, response);
                //}
            } else {
                LOG.debug("###########Foreign Currency Field Calculation for requisition###########");
                SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(item);
                Long currencyTypeId = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                Map documentNumberMap = new HashMap();
                documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
                BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
                List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                Iterator iterator = exchangeRateList.iterator();
                OleExchangeRate tempOleExchangeRate = null;
                if (iterator.hasNext()) {
                    tempOleExchangeRate = (OleExchangeRate) iterator.next();
                    item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                }
                if (item.getItemExchangeRate() != null && item.getItemForeignUnitCost() != null) {
                    item.setItemUnitCostUSD(new KualiDecimal(item.getItemForeignUnitCost().bigDecimalValue().divide(tempOleExchangeRate.getExchangeRate(), 4, RoundingMode.HALF_UP)));
                    item.setItemUnitPrice(item.getItemUnitCostUSD().bigDecimalValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                    item.setItemListPrice(item.getItemUnitCostUSD());
                }
                super.addItem(mapping, purchasingForm, request, response);
            }
        } else {
            purchasingForm.getNewPurchasingItemLine().setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(item).setScale(2, BigDecimal.ROUND_HALF_UP));
            super.addItem(mapping, purchasingForm, request, response);
        }
        if(item.getClaimDate()==null){
            getOlePurapService().setClaimDateForReq(item,document.getVendorDetail());
        }
        if (GlobalVariables.getMessageMap().getErrorCount() == 0) {
            String chartCode = SpringContext.getBean(ParameterService.class).getParameterValueAsString(RequisitionDocument.class, "DEFAULT_ACCOUNTINGLINE_CHART_CODE");
            String objectCode = SpringContext.getBean(ParameterService.class).getParameterValueAsString(RequisitionDocument.class, "DEFAULT_ACCOUNTINGLINE_OBJECT_CODE");
            if (null != item.getNewSourceLine()) {
                item.getNewSourceLine().setChartOfAccountsCode(chartCode);
                item.getNewSourceLine().setFinancialObjectCode(objectCode);
            }
            //setDefaultItemStatusAndLocation(document, (OleRequisitionItem) purchasingForm.getNewPurchasingItemLine(), true);
        }

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Takes care of storing the action form in the User session and forwarding to the prlookup action.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward performPRLookup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside performPRLookup Method of OleRequisitionAction");
        ActionForward forward = super.performLookup(mapping, form, request, response);
        String path = forward.getPath();
        if (path.contains("kr/" + KRADConstants.LOOKUP_ACTION)) {
            path = path.replace("kr/" + KRADConstants.LOOKUP_ACTION, OLEConstants.PR_LOOKUP_ACTION);
            // path = path.replace("kr/lookup.do", "ptrnlookup.do");
        } else if (path.contains(KRADConstants.LOOKUP_ACTION)) {
            path = path.replace(KRADConstants.LOOKUP_ACTION, OLEConstants.PR_LOOKUP_ACTION);
            // path = path.replace("lookup.do", "ptrnlookup.do");
        }
        forward.setPath(path);
        LOG.debug("Leaving performPRLookup Method of OleRequisitionAction");
        return forward;
    }


    /**
     * Add a new Note to the selected RequisitionItem.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */

    public ActionForward addNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside addNote Method of OleRequisitionAction");
        OleRequisitionForm purchasingForm = (OleRequisitionForm) form;
        int line = this.getSelectedLine(request);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItem(line);
        OleRequisitionNotes note = new OleRequisitionNotes();
        note.setNoteTypeId(item.getNoteTypeId());
        note.setNote(item.getNote());
        item.getNotes().add(note);
        LOG.debug("Adding Note to the RequisitionItem");
        item.setNoteTypeId(null);
        item.setNote(null);
        LOG.debug("Leaving addNote Method of OleRequisitionAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Remove a note for the selected Item .
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward deleteNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteNote Method of OleRequisitionAction");
        OleRequisitionForm purchasingForm = (OleRequisitionForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int noteIndex = Integer.parseInt(indexes[1]);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItem((itemIndex));
        item.getNotes().remove(noteIndex);
        LOG.debug("Selected Note is Remove");
        LOG.debug("Leaving deleteNote Method of OleRequisitionAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PurchasingFormBase purchasingForm = (PurchasingFormBase) form;
        PurchasingDocument purDoc = (PurchasingDocument) purchasingForm.getDocument();
        PurchasingFormBase formBase = (PurchasingFormBase) form;
        OleRequisitionDocument reqDoc = (OleRequisitionDocument) formBase.getDocument();
        List<OleRequisitionItem> items = reqDoc.getItems();
        // if form is not yet calculated, return and prompt user to calculate
        if (requiresCalculate(purchasingForm)) {
            GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS, PurapKeyConstants.ERROR_PURCHASING_REQUIRES_CALCULATE);
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }
        // call prorateDiscountTradeIn
        SpringContext.getBean(PurapService.class).prorateForTradeInAndFullOrderDiscount(purDoc);
        this.calculate(mapping, purchasingForm, request, response);
        for (OleRequisitionItem item : items) {
            if (null != item.getItemType() && item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                if (item.getItemQuantity() != null && item.getItemNoOfParts() != null && !item.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                        && !item.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
                    OleCopy oleCopy = new OleCopy();
                    oleCopy.setLocation(item.getItemLocation());
                    oleCopy.setBibId(item.getItemTitleId());
                    if (StringUtils.isNotBlank(item.getLinkToOrderOption()) && (item.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || item.getLinkToOrderOption().equals(OLEConstants.EB_PRINT))) {
                        oleCopy.setCopyNumber(item.getSingleCopyNumber() != null && !item.getSingleCopyNumber().isEmpty() ? item.getSingleCopyNumber() : null);
                    }
                    oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                    List<OleCopy> copyList = new ArrayList<>();
                    copyList.add(oleCopy);
                    item.setCopyList(copyList);
                }
            }
        }
        if (sufficientFundChecklag) {
            return super.route(mapping, form, request, response);
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    // changes for jira OLE-2177.

    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiAction#refresh(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("<<<---------Inside OleRequisitionAction Refresh------>>>");
        ActionForward forward = super.refresh(mapping, form, request, response);
        OleRequisitionForm rqForm = (OleRequisitionForm) form;
        OleRequisitionDocument document = (OleRequisitionDocument) rqForm.getDocument();
        OleRequisitionItem item = (OleRequisitionItem) rqForm.getNewPurchasingItemLine();
        // super.refresh() must occur before this line to get the correct APO limit
        document.setOrganizationAutomaticPurchaseOrderLimit(SpringContext.getBean(PurapService.class).getApoLimit(document.getVendorContractGeneratedIdentifier(), document.getChartOfAccountsCode(), document.getOrganizationCode()));
        // To set PurchaseOrderTransmissionMethod depend on vendor transmission format
        if (LOG.isInfoEnabled()) {
            LOG.info("Currency Type on Requisition :" + document.getVendorDetail().getCurrencyType());
        }
        if (document.getVendorDetail() != null) {
            boolean activePreferredFound = false;
            if (document.getVendorDetail().getVendorTransmissionFormat().size() > 0) {
                List<VendorTransmissionFormatDetail> vendorTransmissionFormat = document.getVendorDetail().getVendorTransmissionFormat();
                for (VendorTransmissionFormatDetail iter : vendorTransmissionFormat) {
                    if (iter.isVendorPreferredTransmissionFormat() && iter.isActive()) {
                        if (iter.getVendorTransmissionFormat().getVendorTransmissionFormat() != null) {
                            activePreferredFound = true;
                            if (iter.getVendorTransmissionFormat().getVendorTransmissionFormat().equalsIgnoreCase(OleSelectConstant.VENDOR_TRANSMISSION_FORMAT_EDI)) {
                                document.setPurchaseOrderTransmissionMethodCode(OleSelectConstant.METHOD_OF_PO_TRANSMISSION_NOPR);
                            } else {
                                document.setPurchaseOrderTransmissionMethodCode(SpringContext.getBean(ParameterService.class).getParameterValueAsString(RequisitionDocument.class, PurapParameterConstants.PURAP_DEFAULT_PO_TRANSMISSION_CODE));
                            }
                        }
                    }
                }
            }
            if (!activePreferredFound){
                document.setPurchaseOrderTransmissionMethodCode(SpringContext.getBean(ParameterService.class).getParameterValueAsString(RequisitionDocument.class, PurapParameterConstants.PURAP_DEFAULT_PO_TRANSMISSION_CODE));
            }
            boolean foreignCurrencyIndicator = isForeignCurrency(document.getVendorDetail().getCurrencyType());
          //  if (document.getVendorDetail().getVendorHeader().getVendorForeignIndicator() != null) {
                if ((!foreignCurrencyIndicator) && item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                    if (document.getVendorDetail().getCurrencyType() != null) {
                        Long currencyTypeId = document.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                        Map documentNumberMap = new HashMap();
                        documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
                        BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
                        List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                        Iterator iterator = exchangeRateList.iterator();
                        if (iterator.hasNext()) {
                            OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                            item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                        }
                    }
                }
            //}
        }
        return forward;
    }

    public ActionForward refreshDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("<<<---------Inside OleRequisitionAction refreshDocuments------>>>");
        ActionForward forward = super.reload(mapping, form, request, response);
        return forward;
    }

    private boolean isForeignCurrency(OleCurrencyType oleCurrencyType){
        boolean foreignCurrencyIndicator = false;
        if(oleCurrencyType!=null){
            if(!oleCurrencyType.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                foreignCurrencyIndicator = true;
            }
        }
        return foreignCurrencyIndicator;
    }

    /**
     * @see org.kuali.ole.module.purap.document.web.struts.RequisitionAction#clearVendor(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward clearVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward forward = super.clearVendor(mapping, form, request, response);
        OleRequisitionForm rqForm = (OleRequisitionForm) form;
        OleRequisitionDocument document = (OleRequisitionDocument) rqForm.getDocument();
        document.setPurchaseOrderTransmissionMethodCode(OleSelectConstant.METHOD_OF_PO_TRANSMISSION_NOPR);
        document.setVendorEnterKeyEvent(false);
        return forward;
    }
    // end for jira OLE-2177.
    // added for jira OLE-2112

    /**
     * @see org.kuali.ole.sys.web.struts.KualiAccountingDocumentActionBase#insertSourceLine(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward insertSourceLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // It would be preferable to find a way to genericize the KualiAccountingDocument methods but this will work for now
        PurchasingAccountsPayableFormBase purapForm = (PurchasingAccountsPayableFormBase) form;


        // index of item selected
        int itemIndex = getSelectedLine(request);
        PurApItem item = null;


        // if custom processing of an accounting line is not done then insert a line generically.
        if (processCustomInsertAccountingLine(purapForm, request) == false) {
            String errorPrefix = null;
            PurApAccountingLine line = null;

            boolean rulePassed = false;
            if (itemIndex >= 0) {
                item = ((PurchasingAccountsPayableDocument) purapForm.getDocument()).getItem((itemIndex));
                // calculating the dollar amount for the accounting Line.
                PurApAccountingLine lineItem = item.getNewSourceLine();
                if (item.getTotalAmount() != null && !item.getTotalAmount().equals(KualiDecimal.ZERO)) {
                    if (lineItem.getAccountLinePercent() != null && (lineItem.getAmount() == null || lineItem.getAmount().equals(KualiDecimal.ZERO))) {
                        BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                        lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                    } else if (lineItem.getAmount() != null && lineItem.getAmount().isNonZero() && lineItem.getAccountLinePercent() == null) {
                        KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                        BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()), 0, RoundingMode.FLOOR);
                        lineItem.setAccountLinePercent(dollarToPercent);
                    } else if (lineItem.getAmount() != null && lineItem.getAmount().isZero() && lineItem.getAccountLinePercent() == null) {
                        lineItem.setAccountLinePercent(new BigDecimal(0));
                    }
                    else if(lineItem.getAmount()!=null&& lineItem.getAccountLinePercent().intValue()== 100){
                        KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                        BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()),0,RoundingMode.FLOOR);
                        lineItem.setAccountLinePercent(dollarToPercent);
                    }
                    else if(lineItem.getAmount()!=null&&lineItem.getAccountLinePercent() != null){
                        BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                        lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                    }
                } else {
                    lineItem.setAmount(new KualiDecimal(0));
                }
                line = (PurApAccountingLine) ObjectUtils.deepCopy(lineItem);
                //end
                //SpringContext.getBean(AccountService.class).populateAccountingLineChartIfNeeded(line);
                errorPrefix = OLEPropertyConstants.DOCUMENT + "." + PurapPropertyConstants.ITEM + "[" + Integer.toString(itemIndex) + "]." + OLEConstants.NEW_SOURCE_ACCT_LINE_PROPERTY_NAME;
                rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AddAccountingLineEvent(errorPrefix, purapForm.getDocument(), line));
            } else if (itemIndex == -2) {
                //corrected: itemIndex == -2 is the only case for distribute account
                //This is the case when we're inserting an accounting line for distribute account.
                line = ((PurchasingFormBase) purapForm).getAccountDistributionnewSourceLine();
                //SpringContext.getBean(AccountService.class).populateAccountingLineChartIfNeeded(line);
                errorPrefix = PurapPropertyConstants.ACCOUNT_DISTRIBUTION_NEW_SRC_LINE;
                rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AddAccountingLineEvent(errorPrefix, purapForm.getDocument(), line));
            }
            if (rulePassed) {
                AccountingLineBase accountingLineBase = (AccountingLineBase) item.getNewSourceLine();
                if (accountingLineBase != null) {
                    String accountNumber = accountingLineBase.getAccountNumber();
                    String chartOfAccountsCode = accountingLineBase.getChartOfAccountsCode();
                    Map<String, String> criteria = new HashMap<String, String>();
                    criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
                    criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
                    Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(Account.class,criteria);
                    rulePassed = checkForValidAccount(account);
                }
            }
            if (rulePassed) {
                // add accountingLine
                SpringContext.getBean(PersistenceService.class).retrieveNonKeyFields(line);
                PurApAccountingLine newSourceLine = item.getNewSourceLine();
                List<PurApAccountingLine> existingSourceLine = item.getSourceAccountingLines();

                BigDecimal initialValue = OleSelectConstant.ZERO_PERCENT;

                for (PurApAccountingLine accountLine : existingSourceLine) {
                    initialValue = initialValue.add(accountLine.getAccountLinePercent());

                }
                if (itemIndex >= 0) {

                    if ((newSourceLine.getAccountLinePercent().intValue() <= OleSelectConstant.ACCOUNTINGLINE_PERCENT_HUNDRED && newSourceLine.getAccountLinePercent().intValue() <= OleSelectConstant.MAX_PERCENT.subtract(initialValue).intValue()) && newSourceLine.getAccountLinePercent().intValue() > OleSelectConstant.ZERO) {
                        if (OleSelectConstant.MAX_PERCENT.subtract(initialValue).intValue() != OleSelectConstant.ZERO) {
                            insertAccountingLine(purapForm, item, line);
                        }
                    } else {
                        checkAccountingLinePercent(newSourceLine);

                    }
                    for(PurApAccountingLine oldSourceAccountingLine:item.getSourceAccountingLines()) {
                        if(oldSourceAccountingLine instanceof OleRequisitionAccount) {
                            ((OleRequisitionAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
                        }
                    }
                    List<PurApAccountingLine> existingAccountingLine = item.getSourceAccountingLines();
                    BigDecimal totalPercent = new BigDecimal(100);
                    BigDecimal initialPercent = new BigDecimal(0);
                    for (PurApAccountingLine purApAccountingLine : existingAccountingLine) {
                        initialPercent = initialPercent.add(purApAccountingLine.getAccountLinePercent());

                    }
                    initialPercent = totalPercent.subtract(initialPercent);
                    BigDecimal maxPercent = initialPercent.max(OleSelectConstant.ZERO_PERCENT);
                    if (maxPercent.intValue() == OleSelectConstant.ZERO) {
                        item.resetAccount(OleSelectConstant.ZERO_PERCENT);

                    } else {
                        item.resetAccount(initialPercent);

                    }
                } else if (itemIndex == -2) {
                    //this is the case for distribute account
                    ((PurchasingFormBase) purapForm).addAccountDistributionsourceAccountingLine(line);
                }
            }
        }

        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    private void checkAccountingLinePercent(PurApAccountingLine newSourceLine) {
        if (newSourceLine.getAccountLinePercent().intValue() >= OleSelectConstant.ACCOUNTINGLINE_PERCENT_HUNDRED) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OleSelectPropertyConstants.ERROR_PERCENT_SHOULD_GREATER, OleSelectConstant.PERCENT);
        } else if (newSourceLine.getAccountLinePercent().intValue() == OleSelectConstant.ZERO) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OleSelectPropertyConstants.ERROR_PERCENT_ZERO, OleSelectConstant.PERCENT);
        } else {

        }

    }

    private boolean checkForValidAccount(Account account) {
        boolean result = true;
        if (account != null) {
            String subFundGroupParameter = getParameterService().getParameterValueAsString(Account.class,
                    OleSelectConstant.SUB_FUND_GRP_CD);
            if (account.getSubFundGroupCode().equalsIgnoreCase(subFundGroupParameter)) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OleSelectPropertyConstants.ERROR_ACCOUNT_NUMBER,
                        new String[]{OleSelectConstant.REQUISITION});
                result = false;
            }
        }
        return result;
    }

    private void setItemDescription(OleRequisitionItem item, String fileName) throws Exception{

        if (OleDocstoreResponse.getInstance().getEditorResponse() != null) {
            Map<String, OLEEditorResponse> oleEditorResponses = OleDocstoreResponse.getInstance().getEditorResponse();
            OLEEditorResponse oleEditorResponse = oleEditorResponses.get(fileName);
            Bib bib = oleEditorResponse != null ? oleEditorResponse.getBib() : null;
            if (bib != null) {
                String title = (bib.getTitle() != null&& !bib.getTitle().isEmpty()) ? bib.getTitle() + ", " : "";
                String author = (bib.getAuthor()!=null && !bib.getAuthor().isEmpty()) ? bib.getAuthor() + ", " : "";
                String publisher = (bib.getPublisher()!=null && !bib.getPublisher().isEmpty()) ? bib.getPublisher() + ", " : "";
                String isbn = (bib.getIsbn()!=null && !bib.getIsbn().isEmpty()) ? bib.getIsbn() + ", " : "";
                String description = title + author + publisher + isbn;
                item.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(bib.getId().toString()));
                item.setItemDescription(description.substring(0, (description.lastIndexOf(","))));
            }
            if (bib != null) {
                item.setBibUUID(bib.getId());
                item.setItemTitleId(bib.getId());
                item.setLinkToOrderOption(oleEditorResponse.getLinkToOrderOption());
            }
            OleDocstoreResponse.getInstance().getEditorResponse().remove(oleEditorResponse);
        }

    }

    public ActionForward addCopy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        LOG.debug("Inside addCopy Method of OleRequisitionAction");
        OleRequisitionForm purchasingForm = (OleRequisitionForm) form;
        int line = this.getSelectedLine(request);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) purchasingForm
                .getDocument()).getItem(line);
        OleRequisitionCopies itemCopy = new OleRequisitionCopies();
        OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
        boolean isValid = true;
        List<String> volChar = new ArrayList<>();
        String[] volNumbers = item.getVolumeNumber() != null ? item.getVolumeNumber().split(",") : new String[0];
        for (String volStr : volNumbers) {
            volChar.add(volStr);
        }
        Integer itemCount = volChar.size();
        isValid = oleCopyHelperService.checkCopyEntry(
                item.getItemCopies(), item.getLocationCopies(), itemCount, item.getItemQuantity(), item.getItemNoOfParts(), item.getCopies(), item.getVolumeNumber(), false);
        if (isValid) {
            itemCopy.setItemCopies(item.getItemCopies());
            itemCopy.setLocationCopies(item.getLocationCopies());
            itemCopy.setParts(item.getItemNoOfParts());
            itemCopy.setStartingCopyNumber(item.getStartingCopyNumber());
            itemCopy.setCaption(item.getCaption());
            itemCopy.setVolumeNumber(item.getVolumeNumber());
            item.setOleRequisitionCopy(itemCopy);
            List<OleCopy> copyList = oleCopyHelperService.setCopyValues(item.getOleRequisitionCopy(), item.getItemTitleId(), volChar);
            item.getCopyList().addAll(copyList);
            item.getCopies().add(itemCopy);
            item.setParts(null);
            item.setItemCopies(null);
            item.setPartEnumeration(null);
            item.setLocationCopies(null);
            item.setCaption(null);
            item.setVolumeNumber(null);
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public boolean checkForCopiesAndLocation(OleRequisitionItem item) {
        boolean isValid = true;
        if (null == item.getItemCopies() || null == item.getLocationCopies()) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.ITEM_ITEMCOPIES_OR_LOCATIONCOPIES_SHOULDNOT_BE_NULL, new String[]{});
            isValid = false;
        }
        return isValid;
    }

    public boolean checkForItemCopiesGreaterThanQuantity(OleRequisitionItem item) {
        boolean isValid = true;
        if (item.getItemCopies().isGreaterThan(item.getItemQuantity())) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
            isValid = false;
        }
        return isValid;
    }

    public boolean checkForTotalCopiesGreaterThanQuantity(OleRequisitionItem item) {
        boolean isValid = true;
        int copies = 0;
        if (item.getCopies().size() > 0) {
            for (int itemCopies = 0; itemCopies < item.getCopies().size(); itemCopies++) {
                copies = copies + item.getCopies().get(itemCopies).getItemCopies().intValue();
            }
            if (item.getItemQuantity().isLessThan(item.getItemCopies().add(new KualiDecimal(copies)))) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.TOTAL_OF_ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
                isValid = false;
            }
        }
        return isValid;
    }


    /**
     * This method takes RequisitionItem as parameter, it will calculate and set partEnumerations and startingCopyNumber for each
     * lineItem
     *
     * @param item
     * @return OleRequisitionCopies
     */
    public OleRequisitionCopies setCopyValues(OleRequisitionItem item) {
        OleRequisitionCopies itemCopy = new OleRequisitionCopies();
        itemCopy.setParts(item.getItemNoOfParts());
        itemCopy.setItemCopies(item.getItemCopies());
        StringBuffer enumeration = new StringBuffer();
        if (item.getStartingCopyNumber() != null && item.getStartingCopyNumber().isNonZero()) {
            itemCopy.setStartingCopyNumber(item.getStartingCopyNumber());
        } else {
            int startingCopies = 1;
            for (int copy = 0; copy < item.getCopies().size(); copy++) {
                startingCopies = startingCopies + item.getCopies().get(copy).getItemCopies().intValue();
            }
            itemCopy.setStartingCopyNumber(new KualiInteger(startingCopies));
        }

        String partEnumerationCopy = getConfigurationService().getPropertyValueAsString(
                OLEConstants.PART_ENUMERATION_COPY);
        String partEnumerationVolume = getConfigurationService().getPropertyValueAsString(
                OLEConstants.PART_ENUMERATION_VOLUME);
        int startingCopyNumber = itemCopy.getStartingCopyNumber().intValue();
        for (int noOfCopies = 0; noOfCopies < item.getItemCopies().intValue(); noOfCopies++) {
            for (int noOfParts = 0; noOfParts < item.getItemNoOfParts().intValue(); noOfParts++) {
                int newNoOfCopies = startingCopyNumber + noOfCopies;
                int newNoOfParts = noOfParts + 1;
                if (noOfCopies + 1 == item.getItemCopies().intValue()
                        && newNoOfParts == item.getItemNoOfParts().intValue()) {
                    enumeration = enumeration.append(
                            partEnumerationCopy + newNoOfCopies + OLEConstants.DOT_TO_SEPARATE_COPIES_PARTS).append(
                            partEnumerationVolume + newNoOfParts);
                } else {
                    enumeration = enumeration.append(
                            partEnumerationCopy + newNoOfCopies + OLEConstants.DOT_TO_SEPARATE_COPIES_PARTS).append(
                            partEnumerationVolume + newNoOfParts + OLEConstants.COMMA_TO_SEPARATE_ENUMERATION);
                }
            }
        }
        itemCopy.setPartEnumeration(enumeration.toString());
        itemCopy.setLocationCopies(item.getLocationCopies());
        return itemCopy;
    }

    /**
     * Remove a Copy for the selected Item .
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward deleteCopy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteCopy Method of OleRequisitionAction");
        OleRequisitionForm purchasingForm = (OleRequisitionForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int copyIndex = Integer.parseInt(indexes[1]);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) purchasingForm
                .getDocument()).getItem((itemIndex));
        List<OleCopy> copyList = new ArrayList<>();
        for(int i=0;i<item.getCopyList().size();i++){
            OleCopy oleCopy = item.getCopyList().get(i);
            if(item.getCopies().get(copyIndex).getLocationCopies().equalsIgnoreCase(oleCopy.getLocation())){
                copyList.add(oleCopy);
            }
        }
        for(OleCopy copy : copyList){
            item.getCopyList().remove(copy);
            item.getDeletedCopiesList().add(copy);
        }
        item.getCopies().remove(copyIndex);
        LOG.debug("Selected Copy is Remove");
        LOG.debug("Leaving deleteCopy Method of OleRequisitionAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Add a Payment History for selected Item.
     *
     * @param mapping  An ActionMapping
     * @param form     An ActionForm
     * @param request  The HttpServletRequest
     * @param response The HttpServletResponse
     * @return An ActionForward
     * @throws Exception
     */
    public ActionForward addPaymentHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        OleRequisitionForm purchasingForm = (OleRequisitionForm) form;
        int line = this.getSelectedLine(request);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) purchasingForm
                .getDocument()).getItem(line);
        OleRequisitionPaymentHistory paymentHistory = new OleRequisitionPaymentHistory();
        paymentHistory.setPaymentHistory("");
        item.getRequisitionPaymentHistory().add(paymentHistory);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /**
     * Remove a Payment History for selected Item
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deletePaymentHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {

        OleRequisitionForm purchasingForm = (OleRequisitionForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int copyIndex = Integer.parseInt(indexes[1]);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) purchasingForm
                .getDocument()).getItem((itemIndex));
        item.getRequisitionPaymentHistory().remove(copyIndex);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    @Override
    public ActionForward copy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.copy(mapping, form, request, response);

        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        PurchasingAccountsPayableDocument purapDocument = (PurchasingAccountsPayableDocument) kualiDocumentFormBase
                .getDocument();
        if(purapDocument.getIsReqsDoc()) {
            OleRequisitionDocument oleRequisitionDocument = (OleRequisitionDocument)purapDocument;
            oleRequisitionDocument.setOrderTypes(getOleRequisitionDocumentService().getPurchaseOrderTypes());
            oleRequisitionDocument.setPaymentTypeCode(getOleRequisitionDocumentService().getParameter(OLEConstants.RECURRING_PAY_TYP));
            oleRequisitionDocument.setItemLocationForFixed(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_LOCATION_FIRM_FIXD));
            oleRequisitionDocument.setItemLocationForApproval(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_LOCATION_APPROVAL));
            oleRequisitionDocument.setItemStatusForFixed(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_STATUS_FIRM_FIXD));
            oleRequisitionDocument.setItemStatusForApproval(getOleRequisitionDocumentService().getParameter(OLEConstants.ITEM_STATUS_APPROVAL));
            oleRequisitionDocument.setCopyNumber(getOleRequisitionDocumentService().getParameter(OLEConstants.COPY_NUMBER));
            if(oleRequisitionDocument.getRequisitionSource().getRequisitionSourceCode().equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST)) {
                ((OleRequisitionDocument) purapDocument).setRequisitionSourceCode(null);
            }
        }

        // refresh accounts in each item....
        List<PurApItem> items = purapDocument.getItems();

        for (PurApItem item : items) {
            OleRequisitionItem oleRequisitionItem = (OleRequisitionItem) item;
            oleRequisitionItem.setInvoiceDocuments(new ArrayList<OleInvoiceDocument>());
            oleRequisitionItem.setNoOfCopiesReceived("");
            oleRequisitionItem.setNoOfPartsReceived("");
            oleRequisitionItem.setReceiptStatusId(null);
            for (OleCopy oleCopy : ((OleRequisitionItem) item).getCopyList()) {
                oleCopy.setCopyId(null);
                oleCopy.setReqItemId(null);
                oleCopy.setPoDocNum(null);
                oleCopy.setPoItemId(null);
                oleCopy.setReceivingItemId(null);
                oleCopy.setCorrectionItemId(null);
                oleCopy.setSerialReceivingIdentifier(null);
                oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
            }
            for (OLELinkPurapDonor oleLinkPurapDonor : ((OleRequisitionItem) item).getOleDonors()) {
                oleLinkPurapDonor.setLinkPurapDonorId(null);
                oleLinkPurapDonor.setCorrectionItemId(null);
                oleLinkPurapDonor.setReceivingItemId(null);
                oleLinkPurapDonor.setReqItemId(null);
                oleLinkPurapDonor.setPoDocNum(null);
                oleLinkPurapDonor.setPoItemId(null);
            }
            if(oleRequisitionItem.getItemQuantity().equals(new KualiDecimal("1")) && oleRequisitionItem.getItemNoOfParts().equals(new KualiInteger("1"))){
                oleRequisitionItem.setCopyList(new ArrayList<OleCopy>());
                oleRequisitionItem.setCopies(new ArrayList<OleCopies>());
            }
        }

        return actionForward;
    }

    @Override
    public ActionForward blanketApprove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        OleRequisitionDocument document = (OleRequisitionDocument) ((PurchasingFormBase) form).getDocument();
        boolean budgetReviewRequired = isBudgetReviewRequired(document);
        if(budgetReviewRequired){
            route(mapping, form, request, response);
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }
        Iterator itemIterator = document.getItems().iterator();
        PurchasingFormBase purchasingForm = (PurchasingFormBase) form;
        OleRequisitionDocument reqDoc = (OleRequisitionDocument) purchasingForm.getDocument();
        List<OleRequisitionItem> items = reqDoc.getItems();
        boolean rulePassed = true;
        while (itemIterator.hasNext()) {
            OleRequisitionItem tempItem = (OleRequisitionItem) itemIterator.next();
            if (tempItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                List<PurApAccountingLine> accountingLineBase = tempItem.getSourceAccountingLines();
                if (accountingLineBase != null) {
                    for (int accountingLine = 0; accountingLine < accountingLineBase.size(); accountingLine++) {
                        String accountNumber = accountingLineBase.get(accountingLine).getAccountNumber();
                        String chartOfAccountsCode = accountingLineBase.get(accountingLine).getChartOfAccountsCode();
                        Map<String, String> criteria = new HashMap<String, String>();
                        criteria.put(OleSelectConstant.ACCOUNT_NUMBER, accountNumber);
                        criteria.put(OleSelectConstant.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
                        Account account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                                Account.class, criteria);
                        rulePassed = checkForValidAccount(account);
                        if (!rulePassed) {
                            return mapping.findForward(OLEConstants.MAPPING_BASIC);
                        }
                    }
                }
            }
        }
        if (requiresCalculate(purchasingForm)) {
            GlobalVariables.getMessageMap().putError(OLEConstants.DOCUMENT_ERRORS,
                    PurapKeyConstants.ERROR_PURCHASING_REQUIRES_CALCULATE);
            return mapping.findForward(OLEConstants.MAPPING_BASIC);
        }
        // call prorateDiscountTradeIn
        SpringContext.getBean(PurapService.class).prorateForTradeInAndFullOrderDiscount(document);
        this.calculate(mapping, purchasingForm, request, response);
        for (OleRequisitionItem item : items) {
            if (null != item.getItemType() && item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                if (item.getItemQuantity() != null && item.getItemNoOfParts() != null && !item.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                        && !item.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
                    OleCopy oleCopy = new OleCopy();
                    oleCopy.setLocation(item.getItemLocation());
                    oleCopy.setBibId(item.getItemTitleId());
                    if (StringUtils.isNotBlank(item.getLinkToOrderOption()) && (item.getLinkToOrderOption().equals(OLEConstants.NB_PRINT) || item.getLinkToOrderOption().equals(OLEConstants.EB_PRINT))) {
                        oleCopy.setCopyNumber(item.getSingleCopyNumber() != null && !item.getSingleCopyNumber().isEmpty() ? item.getSingleCopyNumber() : OLEConstants.ONE.toString());
                    }
                    oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                    List<OleCopy> copyList = new ArrayList<>();
                    copyList.add(oleCopy);
                    item.setCopyList(copyList);
                }
            }
        }
        if (sufficientFundChecklag) {
            super.blanketApprove(mapping, form, request, response);
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public ActionForward selectVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleRequisitionForm rqForm = (OleRequisitionForm) form;
        OleRequisitionDocument document = (OleRequisitionDocument) rqForm.getDocument();
        if (document.getVendorAliasName() != null && document.getVendorAliasName().length() > 0) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, document.getVendorAliasName());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0) {
                Map vendorDetailMap = new HashMap();
                vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                VendorDetail vendorDetail = businessObject.findByPrimaryKey(VendorDetail.class, vendorDetailMap);
                document.setVendorDetail(vendorDetail);
                document.setVendorHeaderGeneratedIdentifier(vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                document.setVendorDetailAssignedIdentifier(vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                refreshVendor(mapping, form, request, response);
            } else {
                GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
            }
        }
        if (GlobalVariables.getMessageMap().getErrorCount() == 0) {
            document.setVendorEnterKeyEvent(true);
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }


    public ActionForward refreshVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PurchasingAccountsPayableFormBase baseForm = (PurchasingAccountsPayableFormBase) form;

        PurchasingDocument document = (PurchasingDocument) baseForm.getDocument();
        if (StringUtils.equals(OLEConstants.REFRESH_VENDOR_CALLER, VendorConstants.VENDOR_LOOKUPABLE_IMPL) && document.getVendorDetailAssignedIdentifier() != null && document.getVendorHeaderGeneratedIdentifier() != null) {
            document.setVendorContractGeneratedIdentifier(null);
            document.refreshReferenceObject(OLEConstants.VENDOR_CONTRACT);

            // retrieve vendor based on selection from vendor lookup
            document.refreshReferenceObject(OLEConstants.VENDOR_DETAILS);
            document.templateVendorDetail(document.getVendorDetail());

            // populate default address based on selected vendor
            VendorAddress defaultAddress = null;
            if(document.getVendorDetail()!=null && document.getVendorDetail().getVendorAddresses()!=null && document.getVendorDetail().getVendorHeader()!=null && document.getVendorDetail().getVendorHeader().getVendorType()!=null && document.getVendorDetail().getVendorHeader().getVendorType().getAddressType()!=null && document.getVendorDetail().getVendorHeader().getVendorType().getAddressType().getVendorAddressTypeCode()!=null){
                defaultAddress = SpringContext.getBean(VendorService.class).getVendorDefaultAddress(document.getVendorDetail().getVendorAddresses(), document.getVendorDetail().getVendorHeader().getVendorType().getAddressType().getVendorAddressTypeCode(), document.getDeliveryCampusCode());
            }
            document.templateVendorAddress(defaultAddress);
        }
        return refresh(mapping, form, request, response);
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }

    public boolean isBudgetReviewRequired(OleRequisitionDocument document) {
        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        List<SourceAccountingLine> sourceAccountingLineList = document.getSourceAccountingLines();
        boolean sufficientFundCheck = false;
        for (SourceAccountingLine accLine : sourceAccountingLineList) {
            String notificationOption = null;
            Map<String, Object> key = new HashMap<String, Object>();
            String chartCode = accLine.getChartOfAccountsCode();
            String accNo = accLine.getAccountNumber();
            key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
            key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
            OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                    OleSufficientFundCheck.class, key);
            if (account != null) {
                notificationOption = account.getNotificationOption();
            }
            if (notificationOption != null
                    && (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW))) {
                sufficientFundCheck = oleRequisitionDocumentService.hasSufficientFundsOnBlanketApproveRequisition(accLine,notificationOption,SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear());
                if (sufficientFundCheck) {
                    return sufficientFundCheck;
                }

            }
        }
        return sufficientFundCheck;
    }
    public ActionForward addDonor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = true;
        OleRequisitionForm purchasingForm = (OleRequisitionForm) form;
        int line = this.getSelectedLine(request);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItem(line);
        Map map = new HashMap();
        if (item.getDonorCode() != null) {
            map.put(OLEConstants.DONOR_CODE, item.getDonorCode());
            List<OLEDonor> oleDonorList = (List<OLEDonor>) getLookupService().findCollectionBySearch(OLEDonor.class, map);
            if (oleDonorList != null && oleDonorList.size() > 0) {
                OLEDonor oleDonor = oleDonorList.get(0);
                if (oleDonor != null) {
                    for (OLELinkPurapDonor oleLinkPurapDonor : item.getOleDonors()) {
                        if (oleLinkPurapDonor.getDonorCode().equalsIgnoreCase(item.getDonorCode())) {
                            flag = false;
                            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                                    OLEConstants.DONOR_CODE_EXISTS, new String[]{});
                            return mapping.findForward(OLEConstants.MAPPING_BASIC);
                        }
                    }
                    if (flag) {
                        OLELinkPurapDonor donor = new OLELinkPurapDonor();
                        donor.setDonorId(oleDonor.getDonorId());
                        donor.setDonorCode(oleDonor.getDonorCode());
                        item.getOleDonors().add(donor);
                        item.setDonorCode(null);
                    }
                }
            } else {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.ERROR_DONOR_CODE, new String[]{});
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public ActionForward deleteDonor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        LOG.debug("Inside deleteDonor Method of OleRequisitionAction");
        OleRequisitionForm purchasingForm = (OleRequisitionForm) form;
        String[] indexes = getSelectedLineForAccounts(request);
        int itemIndex = Integer.parseInt(indexes[0]);
        int donorIndex = Integer.parseInt(indexes[1]);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) purchasingForm.getDocument()).getItem((itemIndex));
        item.getOleDonors().remove(donorIndex);
        LOG.debug("Selected Donor is Remove");
        LOG.debug("Leaving deleteDonor Method of OleRequisitionAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public ActionForward populateAccountingLines(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {

        OleRequisitionForm oleRequisitionForm = (OleRequisitionForm) form;
        int line = this.getSelectedLine(request);
        OleRequisitionItem item = (OleRequisitionItem) ((PurchasingAccountsPayableDocument) oleRequisitionForm.getDocument()).getItem(line);
        OleRequisitionDocument oleRequisitionDocument = (OleRequisitionDocument) oleRequisitionForm.getDocument();
        Map fundMap = new HashMap();
        if (item.getFundCode() != null && !item.getFundCode().trim().isEmpty()) {
            fundMap.put(org.kuali.ole.OLEConstants.OLEEResourceRecord.FUND_CODE, item.getFundCode());
            List<OleFundCode> oleFundCodes = (List<OleFundCode>) getLookupService().findCollectionBySearch(OleFundCode.class, fundMap);
            if (oleFundCodes != null && oleFundCodes.size() > 0) {
                OleFundCode oleFundCode = oleFundCodes.get(0);
                if (oleFundCode != null) {
                    if (oleFundCode.getOleFundCodeAccountingLineList() != null) {
                        List<PurApAccountingLine> accountingLines = new ArrayList<PurApAccountingLine>();
                        for (OleFundCodeAccountingLine accountingLine : oleFundCode.getOleFundCodeAccountingLineList()) {
                            OleRequisitionAccount account = new OleRequisitionAccount();
                            if (accountingLine.getPercentage() != null) {
                                account.setAccountLinePercent(accountingLine.getPercentage());
                            }
                            account.setChartOfAccountsCode(accountingLine.getChartCode());
                            account.setAccountNumber(accountingLine.getAccountNumber());
                            account.setSubAccountNumber(accountingLine.getSubAccount());
                            account.setFinancialObjectCode(accountingLine.getObjectCode());
                            account.setFinancialSubObjectCode(accountingLine.getSubObject());
                            account.setProjectCode(accountingLine.getProject());
                            account.setOrganizationReferenceId(accountingLine.getOrgRefId());
                            accountingLines.add((PurApAccountingLine) account);
                        }
                        item.getSourceAccountingLines().addAll(accountingLines);
                    }
                }
            } else {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.ERROR_FUND_CODE, new String[]{});
            }
        }else{
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.EMPTY_FUND_CODE, new String[]{});
        }
        item.setFundCode(null);
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    /*@Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PurchasingFormBase formBase = (PurchasingFormBase) form;
        OleRequisitionDocument reqDoc = (OleRequisitionDocument) formBase.getDocument();
        OleDocstoreHelperService oleDocstoreHelperService = SpringContext
                .getBean(OleDocstoreHelperService.class);
        Object question = request.getParameter(KRADConstants.QUESTION_INST_ATTRIBUTE_NAME);
        String reason = request.getParameter(OLEConstants.QUESTION_REASON_ATTRIBUTE_NAME);
        String noteOne = "";
        String noteTwo = "";
        ConfigurationService kualiConfiguration = SpringContext.getBean(ConfigurationService.class);
        String[] reasons = null;
        // this should probably be moved into a private instance variable
        // logic for cancel question
        if (question == null) {
            // ask question if not already asked
            return this.performQuestionWithInput(mapping, form, request, response, KRADConstants.DOCUMENT_CANCEL_QUESTION, getKualiConfigurationService().getPropertyValueAsString(
                    OLEConstants.CANCEL_TEXT), KRADConstants.CONFIRMATION_QUESTION, KRADConstants.MAPPING_CANCEL, "");
        } else {
            Object buttonClicked = request.getParameter(KRADConstants.QUESTION_CLICKED_BUTTON);
            if ((KRADConstants.DOCUMENT_CANCEL_QUESTION.equals(question)) && ConfirmationQuestion.NO.equals(buttonClicked)) {
                // if no button clicked just reload the doc
                return mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
            else {
                // Have to check length on value entered.
                String introNoteMessage = OLEConstants.REQUISITION_CANCEL_NOTE_PREFIX + OLEConstants.BLANK_SPACE;
                int noteTextLength = 0;

                // Build out full message.
                if (StringUtils.isNotBlank(reason)) {
                    reasons = reason.split("/");
                    noteOne = introNoteMessage + reasons[0];
                    if (!reasons[1].equalsIgnoreCase(null)) {
                        noteTwo = introNoteMessage + reasons[1];
                        noteTextLength = noteTwo.length();
                    }

                }

                // Get note text max length from DD.
                int noteTextMaxLength = SpringContext.getBean(org.kuali.rice.krad.service.DataDictionaryService.class).getAttributeMaxLength(Note.class, OLEConstants.NOTE_TEXT_PROPERTY_NAME).intValue();
                String message = getKualiConfigurationService().getPropertyValueAsString(OLEConstants.CANCEL_TEXT);

                if (reasons == null || reasons[0].trim().equalsIgnoreCase(OLEConstants.NULL)) {
                    reason = "";
                    return this.performQuestionWithInputAgainBecauseOfErrors(mapping, form, request, response, KRADConstants.DOCUMENT_CANCEL_QUESTION, message, OLEConstants.CONFIRMATION_QUESTION, KRADConstants.DOCUMENT_CANCEL_QUESTION, "", reason, OLEConstants.ERROR_CANCELLATION_REASON_REQUIRED, OLEConstants.QUESTION_REASON_ATTRIBUTE_NAME, "");
                } else if (!reasons[1].equalsIgnoreCase(null) && (noteTextLength > noteTextMaxLength)) {
                    reason = "";
                    return this.performQuestionWithInputAgainBecauseOfErrors(mapping, form, request, response, KRADConstants.DOCUMENT_CANCEL_QUESTION, message, OLEConstants.CONFIRMATION_QUESTION, KRADConstants.DOCUMENT_CANCEL_QUESTION, "", reason, OLEConstants.ERROR_REASON, OLEConstants.QUESTION_REASON_ATTRIBUTE_NAME, new Integer(noteTextMaxLength - noteTextLength).toString());
                }
                // else go to cancel logic below
            }
        }
        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        doProcessingAfterPost(kualiDocumentFormBase, request);
        // KULRICE-4447 Call cancelDocument() only if the document exists
        if (getDocumentService().documentExists(kualiDocumentFormBase.getDocId())) {
            getDocumentService().cancelDocument(kualiDocumentFormBase.getDocument(), kualiDocumentFormBase.getAnnotation());
        }

        DocumentService documentService = SpringContext.getBean(DocumentService.class);
        Note apoNoteOne = documentService.createNoteFromDocument(kualiDocumentFormBase.getDocument(), noteOne);
        kualiDocumentFormBase.getDocument().addNote(apoNoteOne);
        if (!reasons[1].trim().equalsIgnoreCase(OLEConstants.NULL)) {
            Note apoNoteTwo = documentService.createNoteFromDocument(kualiDocumentFormBase.getDocument(), noteTwo);
            kualiDocumentFormBase.getDocument().addNote(apoNoteTwo);
        }
        documentService.saveDocumentNotes(kualiDocumentFormBase.getDocument());
        List<OleRequisitionItem> item = reqDoc.getItems();

        for (int i = 0; item.size() > i; i++) {
            OleRequisitionItem items = (OleRequisitionItem) reqDoc.getItem(i);
            if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                oleDocstoreHelperService.updateItemNote(items,reasons[0]);
            }
        }
        return returnToSender(request, mapping, kualiDocumentFormBase);
    }

    @Override
    protected ActionForward performQuestionWithInput(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String questionId, String questionText, String questionType, String caller, String context) throws Exception {
        return performQuestion(mapping, form, request, response, questionId, questionText, questionType, caller, context, true, "", "", "", "");
    }

    private ActionForward performQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String questionId, String questionText, String questionType, String caller, String context, boolean showReasonField, String reason, String errorKey, String errorPropertyName, String errorParameter) throws Exception {
        Properties parameters = new Properties();

        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, "start");
        parameters.put(KRADConstants.DOC_FORM_KEY, GlobalVariables.getUserSession().addObjectWithGeneratedKey(form));
        parameters.put(KRADConstants.CALLING_METHOD, caller);
        parameters.put(KRADConstants.QUESTION_INST_ATTRIBUTE_NAME, questionId);
        parameters.put(KRADConstants.QUESTION_IMPL_ATTRIBUTE_NAME, questionType);
        //parameters.put(KRADConstants.QUESTION_TEXT_ATTRIBUTE_NAME, questionText);
        parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation(request, mapping));
        parameters.put(KRADConstants.QUESTION_CONTEXT, context);
        parameters.put(KRADConstants.QUESTION_SHOW_REASON_FIELD, Boolean.toString(showReasonField));
        parameters.put(KRADConstants.QUESTION_REASON_ATTRIBUTE_NAME, reason);
        parameters.put(KRADConstants.QUESTION_ERROR_KEY, errorKey);
        parameters.put(KRADConstants.QUESTION_ERROR_PROPERTY_NAME, errorPropertyName);
        parameters.put(KRADConstants.QUESTION_ERROR_PARAMETER, errorParameter);
        parameters.put(KRADConstants.QUESTION_ANCHOR, form instanceof KualiForm ? org.apache.commons.lang.ObjectUtils.toString(((KualiForm) form).getAnchor()) : "");
        Object methodToCallAttribute = request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (methodToCallAttribute != null) {
            parameters.put(KRADConstants.METHOD_TO_CALL_PATH, methodToCallAttribute);
            ((PojoForm) form).registerEditableProperty(String.valueOf(methodToCallAttribute));
        }

        if (form instanceof KualiDocumentFormBase) {
            String docNum = ((KualiDocumentFormBase) form).getDocument().getDocumentNumber();
            if(docNum != null){
                parameters.put(KRADConstants.DOC_NUM, ((KualiDocumentFormBase) form)
                        .getDocument().getDocumentNumber());
            }
        }

        // KULRICE-8077: PO Quote Limitation of Only 9 Vendors
        String questionTextAttributeName = KRADConstants.QUESTION_TEXT_ATTRIBUTE_NAME + questionId;
        GlobalVariables.getUserSession().addObject(questionTextAttributeName, (Object)questionText);
        String questionUrl;
        if (questionId.equalsIgnoreCase(KRADConstants.DOCUMENT_CANCEL_QUESTION)) {
            questionUrl = UrlFactory.parameterizeUrl(getApplicationBaseUrl() + OLEConstants.QUESTION_ACTION, parameters);
        } else {
            questionUrl = UrlFactory.parameterizeUrl(getApplicationBaseUrl() + "/kr/" + KRADConstants.QUESTION_ACTION, parameters);
        }

        return new ActionForward(questionUrl, true);
    }

    @Override
    protected ActionForward performQuestionWithInputAgainBecauseOfErrors(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String questionId, String questionText, String questionType, String caller, String context, String reason, String errorKey, String errorPropertyName, String errorParameter) throws Exception {
        return performQuestion(mapping, form, request, response, questionId, questionText, questionType, caller, context, true, reason, errorKey, errorPropertyName, errorParameter);
    }*/

}
