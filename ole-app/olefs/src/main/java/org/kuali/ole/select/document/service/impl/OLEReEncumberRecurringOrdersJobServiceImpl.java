package org.kuali.ole.select.document.service.impl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.businessobject.OffsetDefinition;
import org.kuali.ole.deliver.service.DateFormatHelper;
import org.kuali.ole.gl.Constant;
import org.kuali.ole.gl.GeneralLedgerConstants;
import org.kuali.ole.gl.OJBUtility;
import org.kuali.ole.gl.batch.service.AccountBalanceCalculator;
import org.kuali.ole.gl.batch.service.RunDateService;
import org.kuali.ole.gl.businessobject.AccountBalance;
import org.kuali.ole.gl.businessobject.Entry;
import org.kuali.ole.gl.businessobject.OriginEntryFull;
import org.kuali.ole.gl.businessobject.TransientBalanceInquiryAttributes;
import org.kuali.ole.gl.businessobject.lookup.BusinessObjectFieldConverter;
import org.kuali.ole.gl.service.AccountBalanceService;
import org.kuali.ole.select.businessobject.OleFundLookup;
import org.kuali.ole.select.document.service.OLEReEncumberRecurringOrdersJobService;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SystemOptions;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.ole.sys.service.OptionsService;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.jaxb.KualiDecimalAdapter;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.core.impl.datetime.DateTimeServiceImpl;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kns.service.BusinessObjectDictionaryService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.lookup.CollectionIncomplete;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.BeanPropertyComparator;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by arunag on 12/3/14.
 */
public class OLEReEncumberRecurringOrdersJobServiceImpl extends PlatformAwareDaoBaseJdbc implements OLEReEncumberRecurringOrdersJobService {

    protected DateTimeService dateTimeService;
    protected RunDateService runDateService;
    protected ParameterService parameterService;

    Map<String, String> preqAccountMap;
    Map<String, String> cmAccountMap;
    java.sql.Date date = new java.sql.Date(getDateTimeService().getCurrentTimestamp().getTime());
    List<String> entriesList = new ArrayList<>();
    Integer fiscalYear;
    String fromDate = getParameter(OLEConstants.FROM_DATE);
    String toDate = getParameter(OLEConstants.TO_DATE);


    @Override
    public void retrieveReEncumberRecuringOrders() {

        String paramaterValue = getParameter(OLEConstants.REENCUMBER_RECURRING_ORDERS);
        String[] value = paramaterValue.split(",");
        List<String> entriesList = new ArrayList<>();
        List<Map<String, Object>> poItemData = new ArrayList<Map<String, Object>>();
        if (value.length > 0 && value[0].equalsIgnoreCase(OLEConstants.PO)) {
            poItemData = executeQueryForPoOption();
            processReencumbranceForPoOption(poItemData);

        } else if (value.length > 0 && value[0].equalsIgnoreCase(OLEConstants.INVOICE)) {
            List<Map<String, Object>> invoiceItemData = new ArrayList<Map<String, Object>>();
            invoiceItemData = executeQueryForInvoiceOption();
            processReencumbranceForInvoiceOption(invoiceItemData);
        }

    }

    public List<Map<String, Object>> executeQueryForPoOption() {

        fromDate = getParameter(OLEConstants.FROM_DATE);
        toDate = getParameter(OLEConstants.TO_DATE);
        String dbVendor = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.DB_VENDOR);
        if (dbVendor.equals(OLEConstants.MYSQL) && fromDate != null && toDate != null) {
            String formattedFromDateForMySQL = formatDateForMySQL(fromDate);
            String formattedToDateForMySQL = formatDateForMySQL(toDate);
            String query = "select * from GL_ENTRY_T where FDOC_NBR in (select FDOC_NBR from PUR_PO_T where PO_ID in(select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null  and  PO_CRTE_DT between '" + formattedFromDateForMySQL + "' AND '" + formattedToDateForMySQL + "'and FDOC_NBR in (select DOC_HDR_ID from KREW_DOC_HDR_T where DOC_TYP_ID in (select DOC_TYP_ID from KREW_DOC_TYP_T where DOC_TYP_NM in('OLE_PO','OLE_POA')) and APP_DOC_STAT='Open')))) and FIN_BALANCE_TYP_CD='EX' and FDOC_TYP_CD in('OLE_PO','OLE_POA')";
            //String query = "select * from GL_ENTRY_T where FDOC_NBR in (select DOC_HDR_ID from KREW_DOC_HDR_T where DOC_HDR_ID in (select FDOC_NBR from PUR_PO_T where PO_ID in(select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null and  PO_CRTE_DT between '" + formattedFromDateForMySQL + "' AND '" + formattedToDateForMySQL + "'))) and DOC_TYP_ID in ('3233','3234') and APP_DOC_STAT='Open')";
            return getSimpleJdbcTemplate().queryForList(query);
        } else {
            String formattedFromDateForOracle = formatDateForOracle(fromDate);
            String formattedToDateForOracle = formatDateForOracle(toDate);
            String query = "select * from GL_ENTRY_T where FDOC_NBR in (select FDOC_NBR from PUR_PO_T where PO_ID in(select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null  and  PO_CRTE_DT between '" + formattedFromDateForOracle + "' AND '" + formattedToDateForOracle + "'and FDOC_NBR in (select DOC_HDR_ID from KREW_DOC_HDR_T where DOC_TYP_ID in (select DOC_TYP_ID from KREW_DOC_TYP_T where DOC_TYP_NM in('OLE_PO','OLE_POA')) and APP_DOC_STAT='Open')))) and FIN_BALANCE_TYP_CD='EX' and FDOC_TYP_CD in('OLE_PO','OLE_POA')";
            //String query = "select * from GL_ENTRY_T where FDOC_NBR in (select DOC_HDR_ID from KREW_DOC_HDR_T where DOC_HDR_ID in (select FDOC_NBR from PUR_PO_T where PO_ID in(select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null and  PO_CRTE_DT between '" + formattedFromDateForOracle + "' AND '" + formattedToDateForOracle + "'))) and DOC_TYP_ID in ('3233','3234') and APP_DOC_STAT='Open')";
            return getSimpleJdbcTemplate().queryForList(query);
        }

    }

    public List<Map<String, Object>> executeQueryForInvoiceOption() {

        String dbVendor = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.DB_VENDOR);
        if (dbVendor.equals(OLEConstants.MYSQL) && fromDate != null && toDate != null) {
            String formattedFromDateForMySQL = formatDateForMySQL(fromDate);
            String formattedToDateForMySQL = formatDateForMySQL(toDate);
            String query = "select * from GL_ENTRY_T where FDOC_NBR in (select DOC_HDR_ID from KREW_DOC_HDR_T where DOC_HDR_ID in (select FDOC_NBR from PUR_PO_T where PO_ID in(select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null and  PO_CRTE_DT between '" + formattedFromDateForMySQL + "' AND '" + formattedToDateForMySQL + "'))) and DOC_TYP_ID in (select DOC_TYP_ID from KREW_DOC_TYP_T where DOC_TYP_NM in('OLE_PO','OLE_POA')) and APP_DOC_STAT='Open') Order by FDOC_NBR and FIN_OBJECT_CD";
            return getSimpleJdbcTemplate().queryForList(query);
        } else {
            String formattedFromDateForOracle = formatDateForOracle(fromDate);
            String formattedToDateForOracle = formatDateForOracle(toDate);
            String query = "select * from GL_ENTRY_T where FDOC_NBR in (select DOC_HDR_ID from KREW_DOC_HDR_T where DOC_HDR_ID in (select FDOC_NBR from PUR_PO_T where PO_ID in(select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null and  PO_CRTE_DT between '" + formattedFromDateForOracle + "' AND '" + formattedToDateForOracle + "'))) and DOC_TYP_ID in (select DOC_TYP_ID from KREW_DOC_TYP_T where DOC_TYP_NM in('OLE_PO','OLE_POA')) and APP_DOC_STAT='Open')";
            return getSimpleJdbcTemplate().queryForList(query);
        }
    }


    public void processReencumbranceForPoOption(List<Map<String, Object>> poItemData) {

        if (poItemData.size() > 0) {
            try {
                entriesList.clear();
                for (Map<String, Object> poDataMap : poItemData) {
                    Entry entry = new Entry();
                    fiscalYear = new Integer(getParameterService().getParameterValueAsString(OleParameterConstants.GENERAL_LEDGER_BATCH.class, GeneralLedgerConstants.ANNUAL_CLOSING_FISCAL_YEAR_PARM));
                    entry.setUniversityFiscalYear(fiscalYear + 1);
                    entry.setChartOfAccountsCode(poDataMap.get(OLEConstants.GL_CHART_CD).toString());
                    entry.setAccountNumber(poDataMap.get(OLEConstants.GL_ACCOUNT_NBR).toString());
                    entry.setSubAccountNumber(poDataMap.get(OLEConstants.GL_SUB_ACCT_NBR).toString());
                    entry.setFinancialObjectCode(poDataMap.get(OLEConstants.GL_OBJ_CD).toString());
                    entry.setFinancialSubObjectCode(poDataMap.get(OLEConstants.GL_SUB_OBJ_CD).toString());
                    entry.setFinancialBalanceTypeCode(poDataMap.get(OLEConstants.GL_BAL_TYP_CD).toString());
                    entry.setFinancialObjectTypeCode(poDataMap.get(OLEConstants.GL_OBJ_TYP_CD).toString());
                    entry.setUniversityFiscalPeriodCode(org.kuali.ole.sys.OLEConstants.PERIOD_CODE_BEGINNING_BALANCE);
                    entry.setFinancialDocumentTypeCode(poDataMap.get(OLEConstants.GL_FIN_DOC_TYP_CD).toString());
                    entry.setFinancialSystemOriginationCode(poDataMap.get(OLEConstants.GL_FIN_SYS_ORG_CD).toString());
                    entry.setDocumentNumber(poDataMap.get(OLEConstants.GL_DOC_NBR).toString());
                    entry.setTransactionLedgerEntrySequenceNumber(Integer.parseInt(poDataMap.get(OLEConstants.TRANS_LED_SEQ_NO).toString()));
                    entry.setTransactionLedgerEntryDescription(poDataMap.get(OLEConstants.GL_TRANS_LED_ENTRY_DESC).toString());
                    entry.setProjectCode(poDataMap.get(OLEConstants.PROJECT_CODE).toString());
                    entry.setReferenceFinancialDocumentTypeCode(poDataMap.get(OLEConstants.REF_DOC_TYP_CD).toString());
                    entry.setReferenceFinancialSystemOriginationCode(poDataMap.get(OLEConstants.REF_ORG_CD).toString());
                    entry.setTransactionEncumbranceUpdateCode(OLEConstants.ENCUM_UPDT_CD);
                    entry.setReferenceFinancialDocumentNumber(poDataMap.get(OLEConstants.FDOC_REF_NBR).toString());
                    KualiDecimal transactionLedgerEntryAmount = new KualiDecimal((BigDecimal) poDataMap.get(OLEConstants.GL_TRANS_LED_ENTRY_AMT));
                    transactionLedgerEntryAmount = calculateTransactionAmount(transactionLedgerEntryAmount);
                    entry.setTransactionLedgerEntryAmount(transactionLedgerEntryAmount);
                    entry.setTransactionDebitCreditCode(poDataMap.get(OLEConstants.GL_TRANS_DEB_CRE_CD).toString());
                    entry.setTransactionDate(date);
                    OriginEntryFull originEntryFullentry = new OriginEntryFull(entry);
                    entriesList.add(originEntryFullentry.getLine());
                }
                writeOuputFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void processReencumbranceForInvoiceOption(List<Map<String, Object>> invoiceItemData) {

        if (invoiceItemData.size() > 0) {
            try {
                entriesList.clear();
                KualiDecimal transactionLedgerEntryAmount = KualiDecimal.ZERO;
                for (Map<String, Object> invoiceDataMap : invoiceItemData) {
                    Entry entry = new Entry();
                    fiscalYear = new Integer(getParameterService().getParameterValueAsString(OleParameterConstants.GENERAL_LEDGER_BATCH.class, GeneralLedgerConstants.ANNUAL_CLOSING_FISCAL_YEAR_PARM));
                    entry.setUniversityFiscalYear(fiscalYear + 1);
                    entry.setChartOfAccountsCode(invoiceDataMap.get(OLEConstants.GL_CHART_CD).toString());
                    entry.setAccountNumber(invoiceDataMap.get(OLEConstants.GL_ACCOUNT_NBR).toString());
                    entry.setSubAccountNumber(invoiceDataMap.get(OLEConstants.GL_SUB_ACCT_NBR).toString());
                    entry.setFinancialObjectCode(invoiceDataMap.get(OLEConstants.GL_OBJ_CD).toString());
                    entry.setFinancialSubObjectCode(invoiceDataMap.get(OLEConstants.GL_SUB_OBJ_CD).toString());
                    entry.setFinancialBalanceTypeCode(invoiceDataMap.get(OLEConstants.GL_BAL_TYP_CD).toString());
                    entry.setFinancialObjectTypeCode(invoiceDataMap.get(OLEConstants.GL_OBJ_TYP_CD).toString());
                    entry.setUniversityFiscalPeriodCode(org.kuali.ole.sys.OLEConstants.PERIOD_CODE_BEGINNING_BALANCE);
                    entry.setFinancialDocumentTypeCode(invoiceDataMap.get(OLEConstants.GL_FIN_DOC_TYP_CD).toString());
                    entry.setFinancialSystemOriginationCode(invoiceDataMap.get(OLEConstants.GL_FIN_SYS_ORG_CD).toString());
                    entry.setDocumentNumber(invoiceDataMap.get(OLEConstants.GL_DOC_NBR).toString());
                    entry.setTransactionLedgerEntrySequenceNumber(Integer.parseInt(invoiceDataMap.get(OLEConstants.TRANS_LED_SEQ_NO).toString()));
                    entry.setTransactionLedgerEntryDescription(invoiceDataMap.get(OLEConstants.GL_TRANS_LED_ENTRY_DESC).toString());
                    transactionLedgerEntryAmount = getTotalInvoicedAmount(invoiceDataMap.get(OLEConstants.FDOC_REF_NBR).toString(), invoiceDataMap.get(OLEConstants.GL_CHART_CD).toString(), invoiceDataMap.get(OLEConstants.GL_ACCOUNT_NBR).toString(), invoiceDataMap.get(OLEConstants.GL_OBJ_CD).toString());
                    transactionLedgerEntryAmount = transactionLedgerEntryAmount.subtract(getTotalCreditMemoAmount(invoiceDataMap.get(OLEConstants.FDOC_REF_NBR).toString(),invoiceDataMap.get(OLEConstants.GL_CHART_CD).toString(),invoiceDataMap.get(OLEConstants.GL_ACCOUNT_NBR).toString(),invoiceDataMap.get(OLEConstants.GL_OBJ_CD).toString()));
                    transactionLedgerEntryAmount = calculateTransactionAmount(transactionLedgerEntryAmount);
                    entry.setTransactionLedgerEntryAmount(transactionLedgerEntryAmount);
                    entry.setTransactionDebitCreditCode(invoiceDataMap.get(OLEConstants.GL_TRANS_DEB_CRE_CD).toString());
                    entry.setProjectCode(invoiceDataMap.get(OLEConstants.PROJECT_CODE).toString());
                    entry.setReferenceFinancialDocumentTypeCode(invoiceDataMap.get(OLEConstants.REF_DOC_TYP_CD).toString());
                    entry.setReferenceFinancialSystemOriginationCode(invoiceDataMap.get(OLEConstants.REF_ORG_CD).toString());
                    entry.setTransactionEncumbranceUpdateCode(OLEConstants.ENCUM_UPDT_CD);
                    entry.setReferenceFinancialDocumentNumber(invoiceDataMap.get(OLEConstants.FDOC_REF_NBR).toString());
                    entry.setTransactionDate(date);
                    OriginEntryFull originEntryFullentry = new OriginEntryFull(entry);
                    entriesList.add(originEntryFullentry.getLine());
                }
                writeOuputFile();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public KualiDecimal getTotalInvoicedAmount(String poId,String chartCode,String accountNumber,String objectCode) {
        String query = "SELECT FIN_COA_CD,ACCOUNT_NBR,FIN_OBJECT_CD,sum(ITM_ACCT_TOT_AMT) as AMT  FROM AP_PMT_RQST_ACCT_T where PMT_RQST_ITM_ID in(select PMT_RQST_ITM_ID from AP_PMT_RQST_ITM_T where PMT_RQST_ID in (select PMT_RQST_ID from AP_PMT_RQST_T  where PO_ID =" + poId + ")) and FIN_COA_CD='" + chartCode + "' and ACCOUNT_NBR='" + accountNumber +"' GROUP BY ITM_ACCT_TOT_AMT,FIN_COA_CD,ACCOUNT_NBR,FIN_OBJECT_CD";
        List<Map<String, Object>> preqData = getSimpleJdbcTemplate().queryForList(query);
        KualiDecimal totalInvoicedAmount = KualiDecimal.ZERO;
        if (preqData.size() > 0) {
            for (Map<String, Object> dataMap : preqData) {
                if (dataMap.get(OLEConstants.GL_CHART_CD).equals(chartCode) && dataMap.get(OLEConstants.GL_ACCOUNT_NBR).equals(accountNumber)) {
                    totalInvoicedAmount= totalInvoicedAmount.add(new KualiDecimal(dataMap.get(OLEConstants.GL_TOTAL_INV_AMT).toString()));
                }
            }
            return totalInvoicedAmount;
        }
        return KualiDecimal.ZERO;
    }

    public KualiDecimal getTotalCreditMemoAmount(String poId,String chartCode,String accountNumber,String objectCode) {
        String query = "SELECT FIN_COA_CD,ACCOUNT_NBR,FIN_OBJECT_CD,sum(ITM_ACCT_TOT_AMT) as AMT  FROM AP_CRDT_MEMO_ACCT_T where CRDT_MEMO_ITM_ID in(select CRDT_MEMO_ITM_ID from AP_CRDT_MEMO_ITM_T where CRDT_MEMO_ID in (select CRDT_MEMO_ID from AP_CRDT_MEMO_T   where PO_ID =" + poId + ")) and FIN_COA_CD='" + chartCode + "' and ACCOUNT_NBR='" + accountNumber  +"' GROUP BY ITM_ACCT_TOT_AMT,FIN_COA_CD,ACCOUNT_NBR,FIN_OBJECT_CD";
        List<Map<String, Object>> cmData = getSimpleJdbcTemplate().queryForList(query);
        KualiDecimal totalCreditMemoAmount = KualiDecimal.ZERO;
        if (cmData.size() > 0) {
            for (Map<String, Object> dataMap : cmData) {
                if (dataMap.get(OLEConstants.GL_CHART_CD) != null && dataMap.get(OLEConstants.GL_CHART_CD).equals(chartCode) && dataMap.get(OLEConstants.GL_ACCOUNT_NBR) != null && dataMap.get(OLEConstants.GL_ACCOUNT_NBR).equals(accountNumber)) {
                    totalCreditMemoAmount = totalCreditMemoAmount.add(new KualiDecimal(dataMap.get(OLEConstants.GL_TOTAL_INV_AMT).toString()));
                }
            }
            return totalCreditMemoAmount;
        }
        return KualiDecimal.ZERO;
    }

    public KualiDecimal calculateTransactionAmount(KualiDecimal transactionLedgerEntryAmount) {
        String paramaterValue = getParameter(OLEConstants.REENCUMBER_RECURRING_ORDERS);
        if (paramaterValue.contains("+")) {
            String amount = paramaterValue.replaceAll("[^0-9]", "");
            KualiDecimalAdapter kualiDecimalAdapter = new KualiDecimalAdapter();
            KualiDecimal dollar = null;
            try {
                dollar = kualiDecimalAdapter.unmarshal(amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (paramaterValue.contains("$")) {
                transactionLedgerEntryAmount = transactionLedgerEntryAmount.add(dollar);
            } else {
                transactionLedgerEntryAmount = transactionLedgerEntryAmount.add((transactionLedgerEntryAmount.multiply(dollar)).divide(new KualiDecimal(100)));
            }
        } else {
            String amount = paramaterValue.replaceAll("[^0-9]", "");
            KualiDecimalAdapter kualiDecimalAdapter = new KualiDecimalAdapter();
            KualiDecimal dollar = null;
            try {
                dollar = kualiDecimalAdapter.unmarshal(amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (paramaterValue.contains("$")) {
                transactionLedgerEntryAmount = transactionLedgerEntryAmount.subtract(dollar);
            } else {
                transactionLedgerEntryAmount = transactionLedgerEntryAmount.subtract((transactionLedgerEntryAmount.multiply(dollar)).divide(new KualiDecimal(100)));
            }
        }
        return transactionLedgerEntryAmount;
    }


    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

    public void writeOuputFile() {

        String fileDirectory = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEConstants.STAGING_DIRECTORY) + OLEConstants.REENCUMBER_FILE_DIRECTORY;
        new File(fileDirectory).mkdir();
        Date date = new Date();
        String fileCreationDate = new SimpleDateFormat(OLEConstants.FILE_DATE_FORMAT).format(date);
        String filePath = fileDirectory  + OLEConstants.REENCUMBER_FILE_PATH +"_"+fileCreationDate+ GeneralLedgerConstants.BatchFileSystem.EXTENSION;
        File file = new File(filePath);
        BufferedWriter bwr = null;
        try {
            file.createNewFile();
            bwr = new BufferedWriter(new FileWriter(file));
            if (entriesList != null && entriesList.size() > 0) {
                for (String entry : entriesList) {
                    bwr.write(entry);
                    bwr.write("\n");
                }
            }
            bwr.flush();
            bwr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            return (DateTimeService) SpringContext.getService(OLEConstants.DATE_SERVICE);
        }
        return dateTimeService;
    }


    public RunDateService getRunDateService() {
        if (runDateService == null) {
            return (RunDateService) SpringContext.getService(OLEConstants.RUN_DATE_SERVICE);
        }
        return runDateService;
    }

    public java.sql.Date calculateRunDate(java.util.Date currentDate) {
        return new java.sql.Date(getRunDateService().calculateRunDate(currentDate).getTime());
    }

    public ParameterService getParameterService() {
        if (parameterService == null) {
            parameterService = (ParameterService) SpringContext.getService(OLEConstants.PARAMETER_SERVICE);
        }
        return parameterService;
    }

    private String formatDateForOracle(String date) {
        String forOracle = DateFormatHelper.getInstance().generateDateStringsForOracle(date);
        return forOracle;
    }

    private String formatDateForMySQL(String date) {
        java.util.Date ss1 = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat(OLEConstants.RENCUM_DATE_FORMAT);
        String forMysql = formatter.format(ss1);
        return forMysql;
    }

}

