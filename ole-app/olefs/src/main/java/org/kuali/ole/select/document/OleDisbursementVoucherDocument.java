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
package org.kuali.ole.select.document;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.fp.businessobject.DisbursementVoucherPayeeDetail;
import org.kuali.ole.fp.document.DisbursementVoucherConstants;
import org.kuali.ole.fp.document.DisbursementVoucherDocument;
import org.kuali.ole.fp.document.service.DisbursementVoucherPaymentReasonService;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.businessobject.OleDisbursementVoucherAccountingLine;
import org.kuali.ole.select.businessobject.OleDisbursementVoucherPayeeDetail;
import org.kuali.ole.select.businessobject.OlePaymentMethod;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEConstants.AdHocPaymentIndicator;
import org.kuali.ole.sys.businessobject.Bank;
import org.kuali.ole.sys.businessobject.ChartOrgHolder;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AmountTotaling;
import org.kuali.ole.sys.service.BankService;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.sql.Date;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OleDisbursementVoucherDocument extends DisbursementVoucherDocument {

    private String vendorAliasName;
    private String invoiceNumber;

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    protected static final String HAS_CLEARING_ACCOUNT_TYPE_SPLIT = "HasClearingAccountType";

    protected OleDisbursementVoucherPayeeDetail dvPayeeDetail;

    /**
     * Default no-arg constructor.
     */
    public OleDisbursementVoucherDocument() {
        super();
        dvPayeeDetail = new OleDisbursementVoucherPayeeDetail();
    }

    /**
     * @return Returns the dvPayeeDetail.
     */
    @Override
    public OleDisbursementVoucherPayeeDetail getDvPayeeDetail() {
        return dvPayeeDetail;
    }

    /**
     * @param dvPayeeDetail The dvPayeeDetail to set.
     */
    @Override
    public void setDvPayeeDetail(DisbursementVoucherPayeeDetail dvPayeeDetail) {
        OleDisbursementVoucherPayeeDetail dvDetail = (OleDisbursementVoucherPayeeDetail) dvPayeeDetail;
        this.dvPayeeDetail = dvDetail;
    }

    /**
     * @see org.kuali.rice.kns.document.Document#prepareForSave()
     */
    @Override
    public void prepareForSave() {
        if (this instanceof AmountTotaling) {
            getFinancialSystemDocumentHeader().setFinancialDocumentTotalAmount(
                    ((AmountTotaling) this).getTotalDollarAmount());
        }

        if (dvWireTransfer != null) {
            dvWireTransfer.setDocumentNumber(this.documentNumber);
        }

        if (dvNonResidentAlienTax != null) {
            dvNonResidentAlienTax.setDocumentNumber(this.documentNumber);
        }

        dvPayeeDetail.setDocumentNumber(this.documentNumber);

        if (dvNonEmployeeTravel != null) {
            dvNonEmployeeTravel.setDocumentNumber(this.documentNumber);
            dvNonEmployeeTravel.setTotalTravelAmount(dvNonEmployeeTravel.getTotalTravelAmount());
        }

        if (dvPreConferenceDetail != null) {
            dvPreConferenceDetail.setDocumentNumber(this.documentNumber);
            dvPreConferenceDetail.setDisbVchrConferenceTotalAmt(dvPreConferenceDetail.getDisbVchrConferenceTotalAmt());
        }

        if (shouldClearSpecialHandling()) {
            clearSpecialHandling();
        }
        if (dvPayeeDetail.getVendorAliasName() != null && dvPayeeDetail.getVendorAliasName().length() > 0) {
            checkVendorUsingVendorAliasName();
        }
        if(this.getPaymentMethodId() != null)  {
            OlePaymentMethod olePaymentMethod = new OlePaymentMethod();
                 olePaymentMethod = SpringContext.getBean(BusinessObjectService.class).findBySinglePrimaryKey(
                        OlePaymentMethod.class, this.getPaymentMethodId());
           if(olePaymentMethod != null)  {
               if(olePaymentMethod.getPaymentMethod().equalsIgnoreCase("CHECK")) {
                   this.setDisbVchrPaymentMethodCode(DisbursementVoucherConstants.PAYMENT_METHOD_CHECK);
               }
               else if(olePaymentMethod.getPaymentMethod().equalsIgnoreCase("Wire Transfer")) {
                   this.setDisbVchrPaymentMethodCode(DisbursementVoucherConstants.PAYMENT_METHOD_WIRE);
               }
               else if(olePaymentMethod.getPaymentMethod().equalsIgnoreCase("Foreign Draft")) {
                   this.setDisbVchrPaymentMethodCode(DisbursementVoucherConstants.PAYMENT_METHOD_DRAFT);
               }

           }
        }
    }

    private void checkVendorUsingVendorAliasName() {
        Map vendorAliasMap = new HashMap();
        vendorAliasMap.put(OLEConstants.VENDOR_ALIAS_NAME, dvPayeeDetail.getVendorAliasName());
        org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
        if (vendorAliasList != null && vendorAliasList.size() > 0 && vendorAliasList.get(0) != null) {
            if (this.getDvPayeeDetail().getDisbVchrPayeeIdNumber() == null) {
                GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_SELECT);
            } else {
                String vendorDetailAssignedIdentifier = vendorAliasList.get(0).getVendorDetailAssignedIdentifier().toString();
                String vendorHeaderGeneratedIdentifier = vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier().toString();
                String AliasPayeeId = vendorHeaderGeneratedIdentifier + "-" + vendorDetailAssignedIdentifier;
                if (this.getDvPayeeDetail().getDisbVchrPayeeIdNumber().equalsIgnoreCase(AliasPayeeId)) {
                    this.getDvPayeeDetail().setDisbVchrPaymentReasonCode(DisbursementVoucherConstants.DV_PAYEE_TYPE_EMPLOYEE);
                    LOG.debug("###########vendors are allowed to save###########");
                } else {
                    GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_SAME);
                }
            }
        } else {
            GlobalVariables.getMessageMap().putError(PurapConstants.VENDOR_ERRORS, OLEConstants.VENDOR_NOT_FOUND);
        }
    }

    /**
     * Clears all set special handling fields
     */
    @Override
    protected void clearSpecialHandling() {
        OleDisbursementVoucherPayeeDetail payeeDetail = getDvPayeeDetail();

        if (!StringUtils.isBlank(payeeDetail.getDisbVchrSpecialHandlingPersonName())) {
            payeeDetail.setDisbVchrSpecialHandlingPersonName(null);
        }
        if (!StringUtils.isBlank(payeeDetail.getDisbVchrSpecialHandlingLine1Addr())) {
            payeeDetail.setDisbVchrSpecialHandlingLine1Addr(null);
        }
        if (!StringUtils.isBlank(payeeDetail.getDisbVchrSpecialHandlingLine2Addr())) {
            payeeDetail.setDisbVchrSpecialHandlingLine2Addr(null);
        }
        if (!StringUtils.isBlank(payeeDetail.getDisbVchrSpecialHandlingCityName())) {
            payeeDetail.setDisbVchrSpecialHandlingCityName(null);
        }
        if (!StringUtils.isBlank(payeeDetail.getDisbVchrSpecialHandlingStateCode())) {
            payeeDetail.setDisbVchrSpecialHandlingStateCode(null);
        }
        if (!StringUtils.isBlank(payeeDetail.getDisbVchrSpecialHandlingZipCode())) {
            payeeDetail.setDisbVchrSpecialHandlingZipCode(null);
        }
        if (!StringUtils.isBlank(payeeDetail.getDisbVchrSpecialHandlingCountryCode())) {
            payeeDetail.setDisbVchrSpecialHandlingCountryCode(null);
        }
    }

    /**
     * Clears information that might have been entered for sub tables, but because of changes to the document is longer needed and
     * should not be persisted.
     */
    @Override
    protected void cleanDocumentData() {
        // TODO: warren: this method ain't called!!! maybe this should be called by prepare for save above
        if (!DisbursementVoucherConstants.PAYMENT_METHOD_WIRE.equals(this.getDisbVchrPaymentMethodCode()) && !DisbursementVoucherConstants.PAYMENT_METHOD_DRAFT.equals(this.getDisbVchrPaymentMethodCode())) {
            getBusinessObjectService().delete(dvWireTransfer);
            dvWireTransfer = null;
        }

        if (!dvPayeeDetail.isDisbVchrAlienPaymentCode()) {
            getBusinessObjectService().delete(dvNonResidentAlienTax);
            dvNonResidentAlienTax = null;
        }

        DisbursementVoucherPaymentReasonService paymentReasonService = SpringContext.getBean(DisbursementVoucherPaymentReasonService.class);
        String paymentReasonCode = this.getDvPayeeDetail().getDisbVchrPaymentReasonCode();
        //Commented for the jira issue OLE-3415
        /*if (!paymentReasonService.isNonEmployeeTravelPaymentReason(paymentReasonCode)) {
            getBusinessObjectService().delete(dvNonEmployeeTravel);
            dvNonEmployeeTravel = null;
        }

        if (!paymentReasonService.isPrepaidTravelPaymentReason(paymentReasonCode)) {
            getBusinessObjectService().delete(dvPreConferenceDetail);
            dvPreConferenceDetail = null;
        }*/
    }

    /**
     * build document title based on the properties of current document
     *
     * @param the default document title
     * @return the combine information of the given title and additional payment indicators
     */
    @Override
    protected String buildDocumentTitle(String title) {
        OleDisbursementVoucherPayeeDetail payee = getDvPayeeDetail();
        if (payee == null) {
            return title;
        }

        Object[] indicators = new String[2];
        indicators[0] = payee.isEmployee() ? AdHocPaymentIndicator.EMPLOYEE_PAYEE : AdHocPaymentIndicator.OTHER;
        indicators[1] = payee.isDisbVchrAlienPaymentCode() ? AdHocPaymentIndicator.ALIEN_PAYEE : AdHocPaymentIndicator.OTHER;

        DisbursementVoucherPaymentReasonService paymentReasonService = SpringContext.getBean(DisbursementVoucherPaymentReasonService.class);
        //Commented for the jira OLE-3415
        /*boolean isTaxReviewRequired = paymentReasonService.isTaxReviewRequired(payee.getDisbVchrPaymentReasonCode());
        indicators[2] = isTaxReviewRequired ? AdHocPaymentIndicator.PAYMENT_REASON_REQUIRING_TAX_REVIEW : AdHocPaymentIndicator.OTHER;*/

        for (Object indicator : indicators) {
            if (!AdHocPaymentIndicator.OTHER.equals(indicator)) {
                String titlePattern = title + " [{0}:{1}]";
                return MessageFormat.format(titlePattern, indicators);
            }
        }

        return title;
    }

    /**
     * Convenience method to set dv payee detail fields based on a given vendor.
     *
     * @param vendor
     */
    @Override
    public void templateVendor(VendorDetail vendor, VendorAddress vendorAddress) {
        if (vendor == null) {
            return;
        }
        if (this.getDocumentHeader().getDocumentTemplateNumber() == null) {
            this.setPaymentMethodId(vendor.getPaymentMethodId());
        }
        this.getDvPayeeDetail().setDisbursementVoucherPayeeTypeCode(DisbursementVoucherConstants.DV_PAYEE_TYPE_VENDOR);
        this.getDvPayeeDetail().setDisbVchrPayeeIdNumber(vendor.getVendorNumber());
        this.getDvPayeeDetail().setDisbVchrPayeePersonName(vendor.getVendorName());

        this.getDvPayeeDetail().setDisbVchrAlienPaymentCode(vendor.getVendorHeader().getVendorForeignIndicator());

        if (ObjectUtils.isNull(vendorAddress) || ObjectUtils.isNull(vendorAddress.getVendorAddressGeneratedIdentifier())) {
            for (VendorAddress addr : vendor.getVendorAddresses()) {
                if (addr.isVendorDefaultAddressIndicator()) {
                    vendorAddress = addr;
                    break;
                }
            }
        }

        if (ObjectUtils.isNotNull(vendorAddress) && ObjectUtils.isNotNull(vendorAddress.getVendorAddressGeneratedIdentifier())) {
            this.getDvPayeeDetail().setDisbVchrVendorAddressIdNumber(vendorAddress.getVendorAddressGeneratedIdentifier().toString());
            this.getDvPayeeDetail().setDisbVchrPayeeLine1Addr(vendorAddress.getVendorLine1Address());
            this.getDvPayeeDetail().setDisbVchrPayeeLine2Addr(vendorAddress.getVendorLine2Address());
            this.getDvPayeeDetail().setDisbVchrPayeeCityName(vendorAddress.getVendorCityName());
            this.getDvPayeeDetail().setDisbVchrPayeeStateCode(vendorAddress.getVendorStateCode());
            this.getDvPayeeDetail().setDisbVchrPayeeZipCode(vendorAddress.getVendorZipCode());
            this.getDvPayeeDetail().setDisbVchrPayeeCountryCode(vendorAddress.getVendorCountryCode());
        } else {
            this.getDvPayeeDetail().setDisbVchrVendorAddressIdNumber("");
            this.getDvPayeeDetail().setDisbVchrPayeeLine1Addr("");
            this.getDvPayeeDetail().setDisbVchrPayeeLine2Addr("");
            this.getDvPayeeDetail().setDisbVchrPayeeCityName("");
            this.getDvPayeeDetail().setDisbVchrPayeeStateCode("");
            this.getDvPayeeDetail().setDisbVchrPayeeZipCode("");
            this.getDvPayeeDetail().setDisbVchrPayeeCountryCode("");
        }

//        this.getDvPayeeDetail().setDisbVchrAlienPaymentCode(vendor.getVendorHeader().getVendorForeignIndicator());
        this.getDvPayeeDetail().setDvPayeeSubjectPaymentCode(VendorConstants.VendorTypes.SUBJECT_PAYMENT.equals(vendor.getVendorHeader().getVendorTypeCode()));
        this.getDvPayeeDetail().setDisbVchrEmployeePaidOutsidePayrollCode(getVendorService().isVendorInstitutionEmployee(vendor.getVendorHeaderGeneratedIdentifier()));

        this.getDvPayeeDetail().setHasMultipleVendorAddresses(1 < vendor.getVendorAddresses().size());

        boolean w9AndW8Checked = false;
        if ((ObjectUtils.isNotNull(vendor.getVendorHeader().getVendorW9ReceivedIndicator()) && vendor.getVendorHeader().getVendorW9ReceivedIndicator() == true) ||
                (ObjectUtils.isNotNull(vendor.getVendorHeader().getVendorW8BenReceivedIndicator()) && vendor.getVendorHeader().getVendorW8BenReceivedIndicator() == true)) {

            w9AndW8Checked = true;
        }

        //    this.disbVchrPayeeW9CompleteCode = vendor.getVendorHeader().getVendorW8BenReceivedIndicator()  == null ? false : vendor.getVendorHeader().getVendorW8BenReceivedIndicator();
        this.disbVchrPayeeW9CompleteCode = w9AndW8Checked;

        Date vendorFederalWithholdingTaxBeginDate = vendor.getVendorHeader().getVendorFederalWithholdingTaxBeginningDate();
        Date vendorFederalWithholdingTaxEndDate = vendor.getVendorHeader().getVendorFederalWithholdingTaxEndDate();
        java.util.Date today = getDateTimeService().getCurrentDate();
        if ((vendorFederalWithholdingTaxBeginDate != null && vendorFederalWithholdingTaxBeginDate.before(today)) && (vendorFederalWithholdingTaxEndDate == null || vendorFederalWithholdingTaxEndDate.after(today))) {
            this.disbVchrPayeeTaxControlCode = DisbursementVoucherConstants.TAX_CONTROL_CODE_BEGIN_WITHHOLDING;
        }

        // if vendor is foreign, default alien payment code to true
        if (getVendorService().isVendorForeign(vendor.getVendorHeaderGeneratedIdentifier())) {
            getDvPayeeDetail().setDisbVchrAlienPaymentCode(true);
        }
    }

    /**
     * This method is overridden to populate some local variables that are not persisted to the database. These values need to be
     * computed and saved to the DV Payee Detail BO so they can be serialized to XML for routing. Some of the routing rules rely on
     * these variables.
     *
     * @see org.kuali.rice.kns.document.DocumentBase#populateDocumentForRouting()
     */
    @Override
    public void populateDocumentForRouting() {
        OleDisbursementVoucherPayeeDetail payeeDetail = getDvPayeeDetail();

        if (payeeDetail.isVendor()) {
            payeeDetail.setDisbVchrPayeeEmployeeCode(getVendorService().isVendorInstitutionEmployee(payeeDetail.getDisbVchrVendorHeaderIdNumberAsInteger()));
            payeeDetail.setDvPayeeSubjectPaymentCode(getVendorService().isSubjectPaymentVendor(payeeDetail.getDisbVchrVendorHeaderIdNumberAsInteger()));
        } else if (payeeDetail.isEmployee()) {
            //Commented for the jira issue OLE-3415
            // Determine if employee is a research subject
            /*ParameterEvaluator researchPaymentReasonCodeEvaluator = getParameterService().getParameterEvaluator(DisbursementVoucherDocument.class, DisbursementVoucherConstants.RESEARCH_PAYMENT_REASONS_PARM_NM, payeeDetail.getDisbVchrPaymentReasonCode());
            if (researchPaymentReasonCodeEvaluator.evaluationSucceeds()) {
                if (getParameterService().parameterExists(DisbursementVoucherDocument.class, DisbursementVoucherConstants.RESEARCH_NON_VENDOR_PAY_LIMIT_AMOUNT_PARM_NM)) {
                    String researchPayLimit = getParameterService().getParameterValue(DisbursementVoucherDocument.class, DisbursementVoucherConstants.RESEARCH_NON_VENDOR_PAY_LIMIT_AMOUNT_PARM_NM);
                    if (StringUtils.isNotBlank(researchPayLimit)) {
                        KualiDecimal payLimit = new KualiDecimal(researchPayLimit);

                        if (getDisbVchrCheckTotalAmount().isLessThan(payLimit)) {
                            payeeDetail.setDvPayeeSubjectPaymentCode(true);
                        }
                    }
                }
            }*/
        }
        if (this.getPaymentMethodId() != null) {
            OlePaymentMethod olePaymentMethod = SpringContext.getBean(BusinessObjectService.class).findBySinglePrimaryKey(OlePaymentMethod.class, this.getPaymentMethodId());
            this.setPaymentMethod(olePaymentMethod);
        }
        super.populateDocumentForRouting(); // Call last, serializes to XML
    }

    /**
     * generic, shared logic used to initiate a dv document
     */
    @Override
    public void initiateDocument() {
        Person currentUser = GlobalVariables.getUserSession().getPerson();
        setDisbVchrContactPersonName(currentUser.getName());
        setDisbVchrContactPhoneNumber(currentUser.getPhoneNumber());
        setDisbVchrContactEmailId(currentUser.getEmailAddress());
        ChartOrgHolder chartOrg = SpringContext.getBean(org.kuali.ole.sys.service.FinancialSystemUserService.class).getPrimaryOrganization(currentUser, OLEConstants.ParameterNamespaces.FINANCIAL);

        // Does a valid campus code exist for this person?  If so, simply grab
        // the campus code via the business object service.
        if (chartOrg != null && chartOrg.getOrganization() != null) {
            setCampusCode(chartOrg.getOrganization().getOrganizationPhysicalCampusCode());
        }
        // A valid campus code was not found; therefore, use the default affiliated
        // campus code.
        else {
            String affiliatedCampusCode = currentUser.getCampusCode();
            setCampusCode(affiliatedCampusCode);
        }

        // due date
        Calendar calendar = getDateTimeService().getCurrentCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        setDisbursementVoucherDueDate(new Date(calendar.getTimeInMillis()));

        // default doc location
        if (StringUtils.isBlank(getDisbursementVoucherDocumentationLocationCode())) {
            setDisbursementVoucherDocumentationLocationCode(getParameterService().getParameterValueAsString(
                    DisbursementVoucherDocument.class, DisbursementVoucherConstants.DEFAULT_DOC_LOCATION_PARM_NM));
        }

        // default bank code
        Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(this.getClass());
        if (defaultBank != null) {
            this.disbVchrBankCode = defaultBank.getBankCode();
            this.bank = defaultBank;
        }
        OlePaymentMethod olePaymentMethod = new OlePaymentMethod();
        if (this.getPaymentMethodId() != null) {
            olePaymentMethod = SpringContext.getBean(BusinessObjectService.class).findBySinglePrimaryKey(
                    OlePaymentMethod.class, this.getPaymentMethodId());
        }

        this.setPaymentMethod(olePaymentMethod);
    }

    /**
     * This implementation sets the sequence number appropriately for the passed in source accounting line using the value that has
     * been stored in the nextSourceLineNumber variable, adds the accounting line to the list that is aggregated by this object, and
     * then handles incrementing the nextSourceLineNumber variable for you.
     *
     * @see org.kuali.ole.sys.document.AccountingDocument#addSourceAccountingLine(org.kuali.ole.sys.businessobject.SourceAccountingLine)
     */
    @Override
    public void addSourceAccountingLine(SourceAccountingLine line) {
        OleDisbursementVoucherAccountingLine oleDisbursementVoucherAccountingLine = (OleDisbursementVoucherAccountingLine) line;
        oleDisbursementVoucherAccountingLine.setSequenceNumber(this.getNextSourceLineNumber());
        this.sourceAccountingLines.add(oleDisbursementVoucherAccountingLine);
        this.nextSourceLineNumber = new Integer(this.getNextSourceLineNumber().intValue() + 1);
    }

    /**
     * This implementation is coupled tightly with some underlying issues that the Struts PojoProcessor plugin has with how objects
     * get instantiated within lists. The first three lines are required otherwise when the PojoProcessor tries to automatically
     * inject values into the list, it will get an index out of bounds error if the instance at an index is being called and prior
     * instances at indices before that one are not being instantiated. So changing the code below will cause adding lines to break
     * if you add more than one item to the list.
     *
     * @see org.kuali.ole.sys.document.AccountingDocument#getSourceAccountingLine(int)
     */
    @Override
    public OleDisbursementVoucherAccountingLine getSourceAccountingLine(int index) {
        while (getSourceAccountingLines().size() <= index) {
            try {
                getSourceAccountingLines().add((OleDisbursementVoucherAccountingLine) getSourceAccountingLineClass().newInstance());
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to get class");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to get class");
            }
        }
        return getSourceAccountingLines().get(index);
    }

    /**
     * Returns the default Source accounting line class.Dollar
     *
     * @see org.kuali.ole.sys.document.AccountingDocument#getSourceAccountingLineClass()
     */
    @Override
    public Class getSourceAccountingLineClass() {
        if (sourceAccountingLineClass == null) {
            sourceAccountingLineClass = (getDataDictionaryEntry().getAccountingLineGroups() != null && getDataDictionaryEntry().getAccountingLineGroups().containsKey("source") && getDataDictionaryEntry().getAccountingLineGroups().get("source").getAccountingLineClass() != null) ? getDataDictionaryEntry().getAccountingLineGroups().get("source").getAccountingLineClass() : OleDisbursementVoucherAccountingLine.class;
        }
        return sourceAccountingLineClass;
    }

    /**
     * @see org.kuali.ole.sys.document.AccountingDocument#getSourceAccountingLines()
     */
    @Override
    public List<OleDisbursementVoucherAccountingLine> getSourceAccountingLines() {
        return this.sourceAccountingLines;
    }

    /**
     * @see org.kuali.ole.sys.document.AccountingDocument#setSourceAccountingLines(java.util.List)
     */
    @Override
    public void setSourceAccountingLines(List sourceLines) {
        this.sourceAccountingLines = sourceLines;
    }

    /**
     * Provides answers to the following splits: PayeeIsPurchaseOrderVendor
     * RequiresTaxReview
     * RequiresTravelReview
     * HasClearingAccountType
     * HasPaymentMethod
     *
     * @see org.kuali.ole.sys.document.FinancialSystemTransactionalDocumentBase#answerSplitNodeQuestion(String)
     */
    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(OleDisbursementVoucherDocument.HAS_CLEARING_ACCOUNT_TYPE_SPLIT)) {
            return hasClearingAccountType();
        }
        if (nodeName.equals(DisbursementVoucherDocument.PAYEE_IS_PURCHASE_ORDER_VENDOR_SPLIT)) {
            return isPayeePurchaseOrderVendor();
        }
        if (nodeName.equals(DisbursementVoucherDocument.DOCUMENT_REQUIRES_TAX_REVIEW_SPLIT)) {
            return isTaxReviewRequired();
        }
        if (nodeName.equals(DisbursementVoucherDocument.DOCUMENT_REQUIRES_TRAVEL_REVIEW_SPLIT)) {
            return true;
        }
        if (nodeName.equals(DOCUMENT_REQUIRES_SEPARATION_OF_DUTIES)) {
            return isSeparationOfDutiesReviewRequired();
        }
        throw new UnsupportedOperationException("Cannot answer split question for this node you call \"" + nodeName + "\"");
    }

    private boolean hasClearingAccountType() {
        boolean hasAccountType = false;
        List<OleDisbursementVoucherAccountingLine> sourceAccounts = this.getSourceAccountingLines();
        for (OleDisbursementVoucherAccountingLine sourceAccount : sourceAccounts) {
            if (sourceAccount.getAccount().getSubFundGroupCode().equalsIgnoreCase(OLEConstants.CLEARING_ACCOUNT_CODE)) {
                hasAccountType = true;
            }
        }/*
        if(hasAccountType) {
            List roleIds = new ArrayList<String>();
            String nameSpaceCode = OLEConstants.OleRequisition.REQUISITION_NAMESPACE;
            String roleId = getRoleService().getRoleIdByName(nameSpaceCode,OLEConstants.OLE_PREPAYMENT);
            roleIds.add(roleId);
            hasAccountType = !getRoleService().principalHasRole(this.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId(), roleIds, null);
        }*/
        return hasAccountType;
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }

}
