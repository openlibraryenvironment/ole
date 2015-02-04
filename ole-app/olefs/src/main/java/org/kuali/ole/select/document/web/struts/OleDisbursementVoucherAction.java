/*
 * Copyright 2013 The Kuali Foundation.
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
package org.kuali.ole.select.document.web.struts;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.fp.document.DisbursementVoucherConstants;
import org.kuali.ole.fp.document.DisbursementVoucherDocument;
import org.kuali.ole.fp.document.web.struts.DisbursementVoucherAction;
import org.kuali.ole.fp.document.web.struts.DisbursementVoucherForm;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.select.businessobject.OleDisbursementVoucherAccountingLine;
import org.kuali.ole.select.document.OleDisbursementVoucherDocument;
import org.kuali.ole.select.document.validation.event.OleDisbursementAccountPercentEvent;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.web.struts.KualiAccountingDocumentFormBase;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleDisbursementVoucherAction extends DisbursementVoucherAction {

    private PurapAccountingService purapAccountingService;

    /**
     * This action executes an insert of a SourceAccountingLine into a document only after validating the accounting line and
     * checking any appropriate business rules.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    @Override
    public ActionForward insertSourceLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleDisbursementVoucherForm disbursementVoucherForm = (OleDisbursementVoucherForm) form;
        OleDisbursementVoucherDocument disbursementDocument = disbursementVoucherForm.getDisbursementVoucherDocument();
        OleDisbursementVoucherAccountingLine line = (OleDisbursementVoucherAccountingLine) disbursementVoucherForm.getNewSourceLine();
        KualiDecimal totalAmount = disbursementDocument.getDisbVchrCheckTotalAmount();
        if ((totalAmount != null) && KualiDecimal.ZERO.compareTo(totalAmount) != 0) {
            if (ObjectUtils.isNotNull(line.getAccountLinePercent())) {
                BigDecimal pct = new BigDecimal(line.getAccountLinePercent().toString()).divide(new BigDecimal(100));
                line.setAmount(new KualiDecimal(pct.multiply(new BigDecimal(totalAmount.toString())).setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
            } else if (ObjectUtils.isNotNull(line.getAmount()) && ObjectUtils.isNull(line.getAccountLinePercent())) {
                KualiDecimal dollar = line.getAmount().multiply(new KualiDecimal(100));
                BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((totalAmount.bigDecimalValue()), 0, RoundingMode.FLOOR);
                line.setAccountLinePercent(dollarToPercent);
            }
        }
        return super.insertSourceLine(mapping, disbursementVoucherForm, request, response);
    }

    /**
     * This method routes the Disbursement Document.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     * @see org.kuali.ole.sys.web.struts.KualiAccountingDocumentActionBase#route(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleDisbursementVoucherForm disbursementVoucherForm = (OleDisbursementVoucherForm) form;
        OleDisbursementVoucherDocument disbursementDocument = disbursementVoucherForm.getDisbursementVoucherDocument();
        boolean isValid = true;
        ActionForward forward = mapping.findForward(OLEConstants.MAPPING_BASIC);
        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new OleDisbursementAccountPercentEvent(OLEConstants.ACCOUNT_NEW_SRC_LINE,
                disbursementDocument, disbursementDocument.getSourceAccountingLines()));
        if(disbursementDocument.getSourceAccountingLines().size() > 0) {
            KualiDecimal totalAmount = KualiDecimal.ZERO;
            for (SourceAccountingLine accLine : disbursementDocument.getSourceAccountingLines()) {
                totalAmount = totalAmount.add(accLine.getAmount());
                if (!(totalAmount.equals(disbursementDocument.getDisbVchrCheckTotalAmount()))) {
                    GlobalVariables.getMessageMap().putError(OLEPropertyConstants.DISB_VCHR_CHECK_TOTAL_AMOUNT, OLEKeyConstants.ERROR_ACC_LINE_TOTAL);
                    isValid = false;
                }
            }
        }
        if (rulePassed) {
            if (!disbursementDocument.getDisbVchrCheckTotalAmount().isPositive()) {
                GlobalVariables.getMessageMap().putError(OLEPropertyConstants.DISB_VCHR_CHECK_TOTAL_AMOUNT, OLEKeyConstants.ERROR_NEGATIVE_OR_ZERO_CHECK_TOTAL);
                isValid = false;
            }
            if (isValid) {
                updateAccountAmountsWithTotal(disbursementDocument.getSourceAccountingLines(),
                        disbursementDocument.getDisbVchrCheckTotalAmount());
                forward = super.route(mapping, disbursementVoucherForm, request, response);
            }
        }


        return forward;
    }

    private void updateAccountAmountsWithTotal(List<OleDisbursementVoucherAccountingLine> sourceAccountingLines, KualiDecimal totalAmount) {
        if ((totalAmount != null) && KualiDecimal.ZERO.compareTo(totalAmount) != 0) {

            KualiDecimal accountTotal = KualiDecimal.ZERO;
            OleDisbursementVoucherAccountingLine lastAccount = null;

            for (OleDisbursementVoucherAccountingLine account : sourceAccountingLines) {
                if (ObjectUtils.isNotNull(account.getAccountLinePercent())) {
                    BigDecimal pct = new BigDecimal(account.getAccountLinePercent().toString()).divide(new BigDecimal(100));
                    account.setAmount(new KualiDecimal(pct.multiply(new BigDecimal(totalAmount.toString())).setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
                } else if (ObjectUtils.isNotNull(account.getAmount()) && ObjectUtils.isNull(account.getAccountLinePercent())) {
                    KualiDecimal dollar = account.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((totalAmount.bigDecimalValue()), 0, RoundingMode.FLOOR);
                    account.setAccountLinePercent(dollarToPercent);
                }
                accountTotal = accountTotal.add(account.getAmount());
                lastAccount = account;
            }
        }
    }

    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        KualiAccountingDocumentFormBase tmpForm = (KualiAccountingDocumentFormBase) form;
        saveVendor(mapping, form, request, response);
        ActionForward forward = super.save(mapping, form, request, response);
        // need to check on sales tax for all the accounting lines
        checkSalesTaxRequiredAllLines(tmpForm, tmpForm.getFinancialDocument().getSourceAccountingLines());
        checkSalesTaxRequiredAllLines(tmpForm, tmpForm.getFinancialDocument().getTargetAccountingLines());
        return forward;
    }

    public ActionForward saveVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleDisbursementVoucherForm disbursementVoucherForm = (OleDisbursementVoucherForm) form;
        OleDisbursementVoucherDocument document = disbursementVoucherForm.getDisbursementVoucherDocument();
        if (document.getDvPayeeDetail().getVendorAliasName() != null && document.getDvPayeeDetail().getVendorAliasName().length() > 0) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, document.getDvPayeeDetail().getVendorAliasName());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0 && vendorAliasList.get(0) != null) {  /* if there matching vendor found for the given vendor alias name */
                if (document.getDvPayeeDetail().getDisbVchrVendorHeaderIdNumber() == null && document.getDvPayeeDetail().getDisbVchrVendorDetailAssignedIdNumber() == null) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_SELECT);
                } else if (document.getDvPayeeDetail().getDisbVchrVendorHeaderIdNumber() != null && document.getDvPayeeDetail().getDisbVchrVendorDetailAssignedIdNumber() != null) {
                    if (!document.getDvPayeeDetail().getDisbVchrVendorHeaderIdNumber().equals(vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier().toString()) ||
                            !document.getDvPayeeDetail().getDisbVchrVendorDetailAssignedIdNumber().equals(vendorAliasList.get(0).getVendorDetailAssignedIdentifier().toString())) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_SAME);
                    }
                }
            } else {     /* If there is no matching vendor found*/

                GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }

    public ActionForward selectVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleDisbursementVoucherForm disbursementVoucherForm = (OleDisbursementVoucherForm) form;
        OleDisbursementVoucherDocument document = disbursementVoucherForm.getDisbursementVoucherDocument();
        if (document.getDvPayeeDetail().getVendorAliasName() != null && document.getDvPayeeDetail().getVendorAliasName().length() > 0) { /* Checks Vendor name is not equal to null  */
            /* Getting matching vendor for the given vendor alias name */
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, document.getDvPayeeDetail().getVendorAliasName());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (vendorAliasList != null && vendorAliasList.size() > 0 && vendorAliasList.get(0) != null) {  /* if there matching vendor found for the given vendor alias name */
                String vendorDetailAssignedIdentifier = vendorAliasList.get(0).getVendorDetailAssignedIdentifier().toString();
                String vendorHeaderGeneratedIdentifier = vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier().toString();
                document.getDvPayeeDetail().setDisbVchrPayeeIdNumber(vendorHeaderGeneratedIdentifier + "-" + vendorDetailAssignedIdentifier);
                document.getDvPayeeDetail().setDisbVchrPaymentReasonCode(DisbursementVoucherConstants.DV_PAYEE_TYPE_EMPLOYEE);
                refreshAfterAliasNameSelection(mapping, disbursementVoucherForm, request);
                refresh(mapping, form, request, response);
            } else {     /* If there is no matching vendor found*/

                GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
            }
        }
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }


    protected ActionForward refreshAfterAliasNameSelection(ActionMapping mapping, DisbursementVoucherForm dvForm, HttpServletRequest request) {
        String refreshCaller = "";
        refreshCaller = OLEConstants.REFRESH_DV_VENDOR_CALLER;

        DisbursementVoucherDocument document = (DisbursementVoucherDocument) dvForm.getDocument();

        boolean isPayeeLookupable = OLEConstants.KUALI_DISBURSEMENT_PAYEE_LOOKUPABLE_IMPL.equals(refreshCaller);
        boolean isAddressLookupable = OLEConstants.KUALI_VENDOR_ADDRESS_LOOKUPABLE_IMPL.equals(refreshCaller);

        // if a cancel occurred on address lookup we need to reset the payee id and type, rest of fields will still have correct information
        if (refreshCaller == null && hasFullEdit(document)) {
            dvForm.setPayeeIdNumber(dvForm.getTempPayeeIdNumber());
            dvForm.setHasMultipleAddresses(false);
            document.getDvPayeeDetail().setDisbVchrPayeeIdNumber(dvForm.getTempPayeeIdNumber());
            document.getDvPayeeDetail().setDisbursementVoucherPayeeTypeCode(dvForm.getOldPayeeType());

            return null;
        }

        // do not execute the further refreshing logic if the refresh caller is not a lookupable
        if (!isPayeeLookupable && !isAddressLookupable) {
            return null;
        }

        // do not execute the further refreshing logic if a payee is not selected
        String payeeIdNumber = document.getDvPayeeDetail().getDisbVchrPayeeIdNumber();
        if (payeeIdNumber == null) {
            return null;
        }

        dvForm.setPayeeIdNumber(payeeIdNumber);
        dvForm.setHasMultipleAddresses(false);

        // determine whether the selected vendor has multiple addresses. If so, redirect to the address selection screen
        if (isPayeeLookupable) {
            VendorDetail refreshVendorDetail = new VendorDetail();
            refreshVendorDetail.setVendorNumber(payeeIdNumber);
            refreshVendorDetail = (VendorDetail) SpringContext.getBean(BusinessObjectService.class).retrieve(refreshVendorDetail);

            VendorAddress defaultVendorAddress = null;
            if (refreshVendorDetail != null) {
                List<VendorAddress> vendorAddresses = refreshVendorDetail.getVendorAddresses();
                boolean hasMultipleAddresses = vendorAddresses != null && vendorAddresses.size() > 1;
                dvForm.setHasMultipleAddresses(hasMultipleAddresses);

                if (vendorAddresses != null) {
                    defaultVendorAddress = vendorAddresses.get(0);
                    setupPayeeAsVendor(dvForm, payeeIdNumber, defaultVendorAddress.getVendorAddressGeneratedIdentifier().toString());
                }
            }
            return null;
        }

        if (isPayeeLookupable && dvForm.isEmployee()) {
            this.setupPayeeAsEmployee(dvForm, payeeIdNumber);
        }

        String payeeAddressIdentifier = request.getParameter(OLEPropertyConstants.VENDOR_ADDRESS_GENERATED_ID);
        if (isAddressLookupable && StringUtils.isNotBlank(payeeAddressIdentifier)) {
            setupPayeeAsVendor(dvForm, payeeIdNumber, payeeAddressIdentifier);
        }

        String paymentReasonCode = document.getDvPayeeDetail().getDisbVchrPaymentReasonCode();
        //Commented for the jira issue OLE-3415
        //addPaymentCodeWarningMessage(dvForm, paymentReasonCode);

        return null;
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }


}
