package org.kuali.ole.select.rule;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jating on 24/9/14.
 */
public class OleFundCodeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValidFundCode = false;
        boolean isValidAccountingLines = false;
        OleFundCode oleFundCode = (OleFundCode) document.getNewMaintainableObject().getDataObject();
        isValidFundCode = validateFundCode(oleFundCode);
        isValidAccountingLines = validateAccountingLines(oleFundCode);
        if (isValidFundCode || isValidAccountingLines) {
            return true;
        } else {
            return false;
        }
    }

// validations for the Fund Code

    private boolean validateFundCode(OleFundCode oleFundCode) {
        boolean error = false;
        if (oleFundCode.getFundCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OLEEResourceRecord.FUND_CODE, oleFundCode.getFundCode());
            List<OleFundCode> savedFundCodes = (List<OleFundCode>) KRADServiceLocator.getBusinessObjectService().findMatching(OleFundCode.class, criteria);
            if ((savedFundCodes.size() > 0)) {
                for (OleFundCode fundCode : savedFundCodes) {
                    String fundId = fundCode.getFundCode();
                    if (null == oleFundCode.getFundCodeId() || (!oleFundCode.getFundCode().equalsIgnoreCase(fundId))) {//
                        this.putFieldError(OLEConstants.OLEEResourceRecord.FUND_CODE, OLEConstants.OLEEResourceRecord.ERROR_DUPLICATE_FUND_CODE);
                        error = true;
                    }
                }
            }
        }
        if (error) {
            return false;
        }
        return true;
    }

    private boolean validateAccountingLines(OleFundCode oleFundCode) {
        boolean error = false;
        BigDecimal desiredPercent = new BigDecimal("100");
        BigDecimal totalPercentage = BigDecimal.ZERO;
        if (oleFundCode.getOleFundCodeAccountingLineList() != null && oleFundCode.getOleFundCodeAccountingLineList().size() > 0) {
            for (OleFundCodeAccountingLine oleFundCodeAccountingLine : oleFundCode.getOleFundCodeAccountingLineList()) {
                totalPercentage = totalPercentage.add(oleFundCodeAccountingLine.getPercentage());
                //chart code validation
                Chart chart = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Chart.class, oleFundCodeAccountingLine.getChartCode());
                if (chart == null) {
                    this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_CHART_CODE);
                    error = true;
                }
                //Account no validation
                Map accNoMap = new HashMap();
                accNoMap.put(OLEConstants.ACCOUNT_NUMBER, oleFundCodeAccountingLine.getAccountNumber());
                Account account = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(Account.class, accNoMap);
                if (account == null) {
                    this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_ACCOUNT_NUM);
                    error = true;
                } else {
                    accNoMap = new HashMap();
                    accNoMap.put(OLEConstants.ACCOUNT_NUMBER, oleFundCodeAccountingLine.getAccountNumber());
                    accNoMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, oleFundCodeAccountingLine.getChartCode());
                    List<Account> accountList = (List<Account>) KRADServiceLocator.getBusinessObjectService().findMatching(Account.class, accNoMap);
                    if (accountList.size() == 0) {
                        this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_ACCOUNT_NUM);
                        error = true;
                    }
                }
                //Object Code validation
                UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
                Integer fiscalYear = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
                Map objectCodeMap = new HashMap();
                objectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, oleFundCodeAccountingLine.getObjectCode());
                objectCodeMap.put(org.kuali.ole.sys.OLEConstants.FISCAL_YEAR, fiscalYear);
                List<ObjectCode> objectCodeList = (List<ObjectCode>) KRADServiceLocator.getBusinessObjectService().findMatching(ObjectCode.class, objectCodeMap);
                if (objectCodeList.size() == 0) {
                    this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_OBJECT_CODE);
                    error = true;
                }
                //Sub-Account no validation
                String subAccNo = oleFundCodeAccountingLine.getSubAccount();
                if (StringUtils.isNotBlank(subAccNo)) {
                    Map subAccNoMap = new HashMap();
                    subAccNoMap.put(OLEConstants.SUB_ACCOUNT_NUMBER, subAccNo);
                    SubAccount subAccount = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(SubAccount.class, subAccNoMap);
                    if (subAccount == null) {
                        this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_SUB_ACCOUNT_NUM);
                        error = true;
                    } else {
                        subAccNoMap = new HashMap();
                        subAccNoMap.put(OLEConstants.SUB_ACCOUNT_NUMBER, subAccNo);
                        subAccNoMap.put(OLEConstants.ACCOUNT_NUMBER, oleFundCodeAccountingLine.getAccountNumber());
                        subAccNoMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, oleFundCodeAccountingLine.getChartCode());
                        List<SubAccount> subAccountList = (List<SubAccount>) KRADServiceLocator.getBusinessObjectService().findMatching(SubAccount.class, subAccNoMap);
                        if (subAccountList.size() == 0) {
                            this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_SUB_ACCOUNT_NUM);
                            error = true;
                        }
                    }
                }
                //Sub Object Code validation
                String subObjectCode = oleFundCodeAccountingLine.getSubObject();
                if (StringUtils.isNotBlank(subObjectCode)) {
                    Map subObjectCodeMap = new HashMap();
                    subObjectCodeMap.put(OLEConstants.FINANCIAL_SUB_OBJECT_CODE, subObjectCode);
                    SubObjectCode subObject = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(SubObjectCode.class, subObjectCodeMap);
                    if (subObject == null) {
                        this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_SUB_OBJECT_CODE);
                        error = true;
                    } else {
                        subObjectCodeMap = new HashMap();
                        subObjectCodeMap.put(OLEConstants.FINANCIAL_SUB_OBJECT_CODE, subObjectCode);
                        subObjectCodeMap.put(OLEConstants.ACCOUNT_NUMBER, oleFundCodeAccountingLine.getAccountNumber());
                        subObjectCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, oleFundCodeAccountingLine.getChartCode());
                        subObjectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, oleFundCodeAccountingLine.getObjectCode());
                        subObjectCodeMap.put(org.kuali.ole.sys.OLEConstants.FISCAL_YEAR, fiscalYear);
                        List<SubObjectCode> subObjectCodeList = (List<SubObjectCode>) KRADServiceLocator.getBusinessObjectService().findMatching(SubObjectCode.class, subObjectCodeMap);
                        if (subObjectCodeList.size() == 0) {
                            this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_SUB_OBJECT_CODE);
                            error = true;
                        }
                    }
                }
                //Project Code validation
                String projectCode = oleFundCodeAccountingLine.getProject();
                if (StringUtils.isNotBlank(projectCode)) {
                    Map projectCodeMap = new HashMap();
                    projectCodeMap.put(OLEConstants.CODE, projectCode);
                    List<ProjectCode> projectCodeList = (List<ProjectCode>) KRADServiceLocator.getBusinessObjectService().findMatching(ProjectCode.class, projectCodeMap);
                    if (projectCodeList.size() == 0) {
                        this.putFieldError(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_PROJECT_CODE);
                        error = true;
                    }
                }
            }
            if (desiredPercent.compareTo(totalPercentage) != 0) {
                this.putFieldError(OLEConstants.OLEEResourceRecord.FUND_CODE, OLEConstants.OLEEResourceRecord.ERROR_PERCENTAGE_LESS_THAN_HUNDRED);
                error = true;
            }

        } else {
            this.putFieldError(OLEConstants.OLEEResourceRecord.FUND_CODE, OLEConstants.OLEEResourceRecord.ERROR_ATLEAST_ONE_ACCOUNTING_LINE);
            error = true;
        }
        if (error) {
            return false;
        }
        return true;
    }

}



