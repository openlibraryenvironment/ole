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
package org.kuali.ole.module.purap.businessobject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Purap Accounting Line Base Business Object.
 */
public abstract class PurApAccountingLineBase extends SourceAccountingLine implements PurApAccountingLine, Comparable {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PurApAccountingLineBase.class);
    protected Integer accountIdentifier;
    private Integer itemIdentifier;
    private BigDecimal accountLinePercent;
    private String postingPeriodCode;  //stored in DB only for PREQ and CM Account History
    private KualiDecimal alternateAmountForGLEntryCreation; // not stored in DB; needed for disencumbrances and such
    public Integer purApSequenceNumber;

    private PurApItem purapItem;

//    protected static final int BIG_DECIMAL_SCALE = 2;

    /**
     * Default constructor
     */
    public PurApAccountingLineBase() {
        super();
        this.setSequenceNumber(0);
        setAmount(null);
    }

    @Override
    public Integer getAccountIdentifier() {
        return accountIdentifier;
    }

    @Override
    public void setAccountIdentifier(Integer requisitionAccountIdentifier) {
        this.accountIdentifier = requisitionAccountIdentifier;
    }

    @Override
    public Integer getItemIdentifier() {
        return itemIdentifier;
    }

    @Override
    public void setItemIdentifier(Integer requisitionItemIdentifier) {
        this.itemIdentifier = requisitionItemIdentifier;
    }

    @Override
    public BigDecimal getAccountLinePercent() {
        if (accountLinePercent != null) {
            accountLinePercent = accountLinePercent.setScale(2, BigDecimal.ROUND_HALF_UP);
            return accountLinePercent;
        } else {
            return new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    @Override
    public void setAccountLinePercent(BigDecimal accountLinePercent) {
        this.accountLinePercent = accountLinePercent;
    }

    /**
     * @see org.kuali.ole.module.purap.businessobject.PurApAccountingLine#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return !(StringUtils.isNotEmpty(getAccountNumber()) || StringUtils.isNotEmpty(getChartOfAccountsCode()) || StringUtils.isNotEmpty(getFinancialObjectCode()) || StringUtils.isNotEmpty(getFinancialSubObjectCode()) || StringUtils.isNotEmpty(getOrganizationReferenceId()) || StringUtils.isNotEmpty(getProjectCode()) || StringUtils.isNotEmpty(getSubAccountNumber()) || ObjectUtils.isNotNull(getAccountLinePercent()));
    }

    /**
     * @see org.kuali.ole.module.purap.businessobject.PurApAccountingLine#createBlankAmountsCopy()
     */
    @Override
    public PurApAccountingLine createBlankAmountsCopy() {
        PurApAccountingLine newAccount = (PurApAccountingLine) ObjectUtils.deepCopy(this);
        newAccount.setAccountLinePercent(BigDecimal.ZERO);
        newAccount.setAmount(KualiDecimal.ZERO);
        newAccount.setSequenceNumber(0);
//        newAccount.setAccountLinePercent(null);
//        newAccount.setAmount(null);

        return newAccount;
    }

    /**
     * @see org.kuali.ole.module.purap.businessobject.PurApAccountingLine#accountStringsAreEqual(org.kuali.ole.sys.businessobject.SourceAccountingLine)
     */
    @Override
    public boolean accountStringsAreEqual(SourceAccountingLine accountingLine) {
        if (accountingLine == null) {
            return false;
        }
        return new EqualsBuilder().append(getChartOfAccountsCode(), accountingLine.getChartOfAccountsCode()).append(getAccountNumber(), accountingLine.getAccountNumber()).append(getSubAccountNumber(), accountingLine.getSubAccountNumber()).append(getFinancialObjectCode(), accountingLine.getFinancialObjectCode()).append(getFinancialSubObjectCode(), accountingLine.getFinancialSubObjectCode()).append(getProjectCode(), accountingLine.getProjectCode()).append(getOrganizationReferenceId(), accountingLine.getOrganizationReferenceId())
                .isEquals();
    }

    @Override
    public boolean accountStringsAreEqual(PurApAccountingLine accountingLine) {
        return accountStringsAreEqual((SourceAccountingLine) accountingLine);

    }

    /**
     * @see org.kuali.ole.module.purap.businessobject.PurApAccountingLine#generateSourceAccountingLine()
     */
    @Override
    public SourceAccountingLine generateSourceAccountingLine() {
        // the fields here should probably match method 'accountStringsAreEqual' above
        SourceAccountingLine sourceLine = new SourceAccountingLine();
        sourceLine.setChartOfAccountsCode(getChartOfAccountsCode());
        sourceLine.setAccountNumber(getAccountNumber());
        sourceLine.setSubAccountNumber(getSubAccountNumber());
        sourceLine.setFinancialObjectCode(getFinancialObjectCode());
        sourceLine.setFinancialSubObjectCode(getFinancialSubObjectCode());
        sourceLine.setProjectCode(getProjectCode());
        sourceLine.setOrganizationReferenceId(getOrganizationReferenceId());
        sourceLine.setAmount(getAmount());
        sourceLine.setSequenceNumber(getSequenceNumber());
        return sourceLine;
    }

    /**
     * @see org.kuali.ole.sys.businessobject.AccountingLineBase#toStringMapper()
     */

    @Override
    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();

        m.put("chart", getChartOfAccountsCode());
        m.put("account", getAccountNumber());
        m.put("objectCode", getFinancialObjectCode());
        m.put("subAccount", getSubAccountNumber());
        m.put("subObjectCode", getFinancialSubObjectCode());
        m.put("projectCode", getProjectCode());
        m.put("orgRefId", getOrganizationReferenceId());

        return m;
    }

    @Override
    public int compareTo(Object arg0) {
        if (arg0 instanceof PurApAccountingLine) {
            PurApAccountingLine account = (PurApAccountingLine) arg0;
            return this.getString().compareTo(account.getString());
        }
        return -1;
    }

    @Override
    public String getString() {
        return getChartOfAccountsCode() + "~" + getAccountNumber() + "~" + getSubAccountNumber() + "~" + getFinancialObjectCode() + "~" + getFinancialSubObjectCode() + "~" + getProjectCode() + "~" + getOrganizationReferenceId();
    }

    @Override
    public KualiDecimal getAlternateAmountForGLEntryCreation() {
        return alternateAmountForGLEntryCreation;
    }

    @Override
    public void setAlternateAmountForGLEntryCreation(KualiDecimal alternateAmount) {
        this.alternateAmountForGLEntryCreation = alternateAmount;
    }

    /**
     * @see org.kuali.ole.sys.businessobject.AccountingLineBase#getSequenceNumber()
     */
    @Override
    public Integer getSequenceNumber() {
        return this.getAccountIdentifier();
    }

    protected void copyFrom(PurApAccountingLine other) {

        super.copyFrom(other);

        setAccountLinePercent(other.getAccountLinePercent());
        setAmount(other.getAmount());
        setAlternateAmountForGLEntryCreation(other.getAlternateAmountForGLEntryCreation());

    }

   /* @Override
    public void refreshNonUpdateableReferences() {
        //hold onto item reference if there without itemId
        PurApItem item = null;
        PurApItem tempItem = getPurapItem();
        if (tempItem != null &&
                tempItem.getItemIdentifier() != null) {
            item = tempItem;
        }
        super.refreshNonUpdateableReferences();
        if (ObjectUtils.isNotNull(item)) {
            this.setPurapItem(item);
        }
    }*/

    @Override
    public <T extends PurApItem> T getPurapItem() {
        return (T) purapItem;
    }

    /**
     * Sets the requisitionItem attribute.
     *
     * @param item
     * @deprecated
     */
    @Deprecated
    @Override
    public void setPurapItem(PurApItem item) {
        purapItem = item;
    }

    @Override
    public String getPostingPeriodCode() {
        return postingPeriodCode;
    }

    @Override
    public void setPostingPeriodCode(String postingPeriodCode) {
        this.postingPeriodCode = postingPeriodCode;
    }

    /**
     * Overridden to use purap doc identifier, rather than document number
     *
     * @see org.kuali.ole.sys.businessobject.AccountingLineBase#getValuesMap()
     */
    @Override
    public Map getValuesMap() {
        Map valuesMap = super.getValuesMap();
        // remove document number
        valuesMap.remove(OLEPropertyConstants.DOCUMENT_NUMBER);
        return valuesMap;
    }

    /**
     * Gets the purApSequenceNumber attribute.
     *
     * @return Returns the purApSequenceNumber
     */

    @Override
    public Integer getPurApSequenceNumber() {
        return super.getSequenceNumber();
    }

    public void setPurApSequenceNumber(Integer purApSequenceNumber) {
        this.purApSequenceNumber = purApSequenceNumber;
    }
}
