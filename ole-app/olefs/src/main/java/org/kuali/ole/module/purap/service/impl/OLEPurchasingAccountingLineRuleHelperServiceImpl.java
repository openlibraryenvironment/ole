package org.kuali.ole.module.purap.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.businessobject.OriginationCode;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.doctype.bo.DocumentTypeEBO;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 11/25/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPurchasingAccountingLineRuleHelperServiceImpl extends PurchasingAccountingLineRuleHelperServiceImpl{

    @Override
    public boolean validateAccountingLine(AccountingLine accountingLine) {
        if (accountingLine == null) {
            throw new IllegalStateException(SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEKeyConstants.ERROR_DOCUMENT_NULL_ACCOUNTING_LINE));
        }
        DataDictionary dd = getDataDictionaryService().getDataDictionary();
        org.kuali.rice.krad.datadictionary.BusinessObjectEntry accountingLineEntry = dd.getBusinessObjectEntry(SourceAccountingLine.class.getName());
        String accountIdentifyingPropertyName = getAccountIdentifyingPropertyName(accountingLine);
        accountingLine.refreshReferenceObject(OLEConstants.CHART_PROPERTY_NAME);
        Chart chart = accountingLine.getChart();
        accountingLine.refreshReferenceObject(OLEConstants.ACCOUNT_PROPERTY_NAME);
        String accountNumber = accountingLine.getAccountNumber();
        Account account = accountingLine.getAccount();
        accountingLine.refreshReferenceObject(OLEConstants.OrderQueue.ORDQ_OBJ_CODE);
        ObjectCode objectCode = accountingLine.getObjectCode();
        boolean valid = true;
        valid &= isValidChart(accountIdentifyingPropertyName, chart, dd);
        valid &= isValidAccount(accountIdentifyingPropertyName, account, accountNumber, dd);
        if (StringUtils.isNotBlank(accountingLine.getSubAccountNumber())) {
            accountingLine.refreshReferenceObject(OLEConstants.SUB_ACCOUNT_PROPERTY_NAME);
            SubAccount subAccount = accountingLine.getSubAccount();
            valid &= isValidSubAccount(accountIdentifyingPropertyName, subAccount, dd);
        }
        valid &= isValidObjectCode(accountIdentifyingPropertyName, objectCode, dd);
        if (StringUtils.isNotBlank(accountingLine.getFinancialSubObjectCode())) {
            accountingLine.refreshReferenceObject(OLEConstants.SUB_OBJECT_CODE_PROPERTY_NAME);
            SubObjectCode subObjectCode = accountingLine.getSubObjectCode();
            valid &= isValidSubObjectCode(accountIdentifyingPropertyName, subObjectCode, dd);
        }
        if (StringUtils.isNotBlank(accountingLine.getProjectCode())) {
            accountingLine.refreshReferenceObject(OLEConstants.PROJECT_PROPERTY_NAME);
            ProjectCode projectCode = accountingLine.getProject();
            valid &= isValidProjectCode(accountIdentifyingPropertyName, projectCode, dd);
        }
        if (StringUtils.isNotBlank(accountingLine.getReferenceOriginCode())) {
            accountingLine.refreshReferenceObject(OLEConstants.REF_ORIGIN_PROPERTY_NAME);
            OriginationCode referenceOrigin = accountingLine.getReferenceOrigin();
            valid &= isValidReferenceOriginCode(accountIdentifyingPropertyName, referenceOrigin, accountingLineEntry);
        }
        if (StringUtils.isNotBlank(accountingLine.getReferenceTypeCode())) {
            DocumentTypeEBO referenceType = accountingLine.getReferenceFinancialSystemDocumentTypeCode();
            valid &= isValidReferenceTypeCode(accountingLine.getReferenceTypeCode(), referenceType, accountingLineEntry, accountIdentifyingPropertyName);
        }
        valid &= hasRequiredOverrides(accountingLine, accountingLine.getOverrideCode());
        return valid;
    }

    private boolean isValidAccount(String accountIdentifyingPropertyName, Account account, String accountNumber, DataDictionary dataDictionary) {
        return isValidAccount(account, accountNumber, dataDictionary, OLEConstants.ACCOUNT_NUMBER_PROPERTY_NAME, accountIdentifyingPropertyName);
    }

    private boolean isValidAccount(Account account, String accountNumber, DataDictionary dataDictionary, String errorPropertyName, String accountIdentifyingPropertyName) {
        String label = getAccountLabel();
        if (ObjectUtils.isNull(account)) {
            GlobalVariables.getMessageMap().putError(errorPropertyName, OLEKeyConstants.ERROR_EXISTING_WITH_IDENTIFYING_ACCOUNTING_LINE, label, accountNumber);
            return false;
        }
        if (!account.isActive()) {
            GlobalVariables.getMessageMap().putError(errorPropertyName, OLEKeyConstants.ERROR_DOCUMENT_ACCOUNT_CLOSED_WITH_IDENTIFYING_ACCOUNTING_LINE, label);
            return false;
        }
        if (ObjectUtils.isNotNull(account.getAccountRestrictedStatusCode())) {
            if (account.getAccountRestrictedStatusCode().equalsIgnoreCase(OleSelectConstant.ACCOUNT_TEMPORARY_RESTRICTED_CODE)) {
                GlobalVariables.getMessageMap().putError(errorPropertyName, OLEKeyConstants.ERROR_DOCUMENT_ACCOUNT_IS_TEMPORARY_RESTRICTED, label);
                return false;
            } else if (account.getAccountRestrictedStatusCode().equalsIgnoreCase(OleSelectConstant.ACCOUNT_RESTRICTED_CODE)) {
                GlobalVariables.getMessageMap().putError(errorPropertyName, OLEKeyConstants.ERROR_DOCUMENT_ACCOUNT_IS_RESTRICTED, accountNumber);
                return false;
            }
        }
        return true;
    }
}
