/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.web.struts;

import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.util.SummaryAccount;
import org.kuali.ole.pdp.PdpPropertyConstants;
import org.kuali.ole.pdp.businessobject.PurchasingPaymentDetail;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEParameterKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.ole.sys.web.struts.KualiAccountingDocumentFormBase;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Struts Action Form for Purchasing and Accounts Payable documents.
 */
public class PurchasingAccountsPayableFormBase extends KualiAccountingDocumentFormBase {

    protected transient List<SummaryAccount> summaryAccounts;
    protected boolean readOnlyAccountDistributionMethod;

    /**
     * Constructs a PurchasingAccountsPayableFormBase instance and initializes summary accounts.
     */
    public PurchasingAccountsPayableFormBase() {
        super();
        clearSummaryAccounts();
        setupAccountDistributionMethod();
    }

    /**
     * retrieves the system parameter value for account distribution method and determines
     * if the drop-down box on the form should be read only or not.
     */
    protected void setupAccountDistributionMethod() {
        // OLE-3405 : disabling the distribution method choice
        //String defaultDistributionMethod = SpringContext.getBean(ParameterService.class).getParameterValueAsString(PurapConstants.PURAP_NAMESPACE, "Document", PurapParameterConstants.DISTRIBUTION_METHOD_FOR_ACCOUNTING_LINES);

        //if (PurapConstants.AccountDistributionMethodCodes.PROPORTIONAL_CODE.equalsIgnoreCase(defaultDistributionMethod) || PurapConstants.AccountDistributionMethodCodes.SEQUENTIAL_CODE.equalsIgnoreCase(defaultDistributionMethod)) {
        this.setReadOnlyAccountDistributionMethod(true);
        //}
        //else {
        //    this.setReadOnlyAccountDistributionMethod(false);
        //}
    }

    /**
     * Updates the summaryAccounts that are contained in the form. Currently we are only calling this on load and when
     * refreshAccountSummary is called.
     */
    public void refreshAccountSummmary() {
        clearSummaryAccounts();
        PurchasingAccountsPayableDocument purapDocument = (PurchasingAccountsPayableDocument) getDocument();
        summaryAccounts.addAll(SpringContext.getBean(PurapAccountingService.class).generateSummaryAccounts(purapDocument));
    }

    /**
     * Initializes summary accounts.
     */
    public void clearSummaryAccounts() {
        summaryAccounts = new ArrayList<SummaryAccount>();
    }

    /**
     * @see org.kuali.ole.sys.web.struts.KualiAccountingDocumentFormBase#getBaselineSourceAccountingLines()
     */
    public List getBaselineSourceAccountingLines() {
        List<AccountingLine> accounts = new ArrayList<AccountingLine>();
        if (ObjectUtils.isNull(accounts) || accounts.isEmpty()) {
            accounts = new ArrayList<AccountingLine>();
            for (PurApItem item : ((PurchasingAccountsPayableDocument) getDocument()).getItems()) {
                List<PurApAccountingLine> lines = item.getBaselineSourceAccountingLines();
                for (PurApAccountingLine line : lines) {
                    accounts.add(line);
                }

            }
        }
        return accounts;
    }

    @Override
    public void populate(HttpServletRequest request) {
        super.populate(request);
        PurchasingAccountsPayableDocument purapDoc = (PurchasingAccountsPayableDocument) this.getDocument();

        //fix document item/account references if necessary
        purapDoc.fixItemReferences();
    }

    public List<SummaryAccount> getSummaryAccounts() {
        if (summaryAccounts == null) {
            refreshAccountSummmary();
        }
        return summaryAccounts;
    }

    public void setSummaryAccounts(List<SummaryAccount> summaryAccounts) {
        this.summaryAccounts = summaryAccounts;
    }

    /**
     * KRAD Conversion: Performs customization of an extra button.
     * <p/>
     * No data dictionary is involved.
     */
    protected void addExtraButton(String property, String source, String altText) {

        ExtraButton newButton = new ExtraButton();

        newButton.setExtraButtonProperty(property);
        newButton.setExtraButtonSource(source);
        newButton.setExtraButtonAltText(altText);

        extraButtons.add(newButton);
    }

    /**
     * This method builds the url for the disbursement info on the purap documents.
     *
     * @return the disbursement info url
     */
    public String getDisbursementInfoUrl() {
        String basePath = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.APPLICATION_URL_KEY);
        ParameterService parameterService = SpringContext.getBean(ParameterService.class);

        String orgCode = parameterService.getParameterValueAsString(OleParameterConstants.PURCHASING_BATCH.class, OLEParameterKeyConstants.PurapPdpParameterConstants.PURAP_PDP_ORG_CODE);
        String subUnitCode = parameterService.getParameterValueAsString(OleParameterConstants.PURCHASING_BATCH.class, OLEParameterKeyConstants.PurapPdpParameterConstants.PURAP_PDP_SUB_UNIT_CODE);

        Properties parameters = new Properties();
        parameters.put(OLEConstants.DISPATCH_REQUEST_PARAMETER, OLEConstants.SEARCH_METHOD);
        parameters.put(OLEConstants.BACK_LOCATION, basePath + "/" + OLEConstants.MAPPING_PORTAL + ".do");
        parameters.put(KRADConstants.DOC_FORM_KEY, "88888888");
        parameters.put(OLEConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, PurchasingPaymentDetail.class.getName());
        parameters.put(OLEConstants.HIDE_LOOKUP_RETURN_LINK, "true");
        parameters.put(OLEConstants.SUPPRESS_ACTIONS, "false");
        parameters.put(PdpPropertyConstants.PaymentDetail.PAYMENT_UNIT_CODE, orgCode);
        parameters.put(PdpPropertyConstants.PaymentDetail.PAYMENT_SUBUNIT_CODE, subUnitCode);

        String lookupUrl = UrlFactory.parameterizeUrl(basePath + "/" + OLEConstants.LOOKUP_ACTION, parameters);

        return lookupUrl;
    }

    /**
     * overridden to make sure accounting lines on items are repopulated
     *
     * @see org.kuali.ole.sys.web.struts.KualiAccountingDocumentFormBase#populateAccountingLinesForResponse(java.lang.String, java.util.Map)
     */
    @Override
    protected void populateAccountingLinesForResponse(String methodToCall, Map parameterMap) {
        super.populateAccountingLinesForResponse(methodToCall, parameterMap);

        populateItemAccountingLines(parameterMap);
    }

    /**
     * Populates accounting lines for each item on the Purchasing AP document
     *
     * @param parameterMap the map of parameters
     */
    protected void populateItemAccountingLines(Map parameterMap) {
        int itemCount = 0;
        for (PurApItem item : ((PurchasingAccountsPayableDocument) getDocument()).getItems()) {
            populateAccountingLine(item.getNewSourceLine(), OLEPropertyConstants.DOCUMENT + "." + OLEPropertyConstants.ITEM + "[" + itemCount + "]." + OLEPropertyConstants.NEW_SOURCE_LINE, parameterMap);

            int sourceLineCount = 0;
            for (PurApAccountingLine purApLine : item.getSourceAccountingLines()) {
                populateAccountingLine(purApLine, OLEPropertyConstants.DOCUMENT + "." + OLEPropertyConstants.ITEM + "[" + itemCount + "]." + OLEPropertyConstants.SOURCE_ACCOUNTING_LINE + "[" + sourceLineCount + "]", parameterMap);
                sourceLineCount += 1;
            }
        }
    }

    /**
     * Gets the readOnlyAccountDistributionMethod attribute.
     *
     * @return Returns the readOnlyAccountDistributionMethod
     */

    public boolean isReadOnlyAccountDistributionMethod() {
        return readOnlyAccountDistributionMethod;
    }

    /**
     * Sets the readOnlyAccountDistributionMethod attribute.
     *
     * @param readOnlyAccountDistributionMethod
     *         The readOnlyAccountDistributionMethod to set.
     */
    public void setReadOnlyAccountDistributionMethod(boolean readOnlyAccountDistributionMethod) {
        this.readOnlyAccountDistributionMethod = readOnlyAccountDistributionMethod;
    }
}
