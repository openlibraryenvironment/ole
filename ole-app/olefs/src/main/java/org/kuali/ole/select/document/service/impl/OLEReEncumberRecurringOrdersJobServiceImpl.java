package org.kuali.ole.select.document.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.DateFormatHelper;
import org.kuali.ole.gl.GeneralLedgerConstants;
import org.kuali.ole.gl.batch.service.RunDateService;
import org.kuali.ole.gl.businessobject.Entry;
import org.kuali.ole.gl.businessobject.OriginEntryFull;
import org.kuali.ole.select.document.service.OLEReEncumberRecurringOrdersJobService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.jaxb.KualiDecimalAdapter;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by arunag on 12/3/14.
 */
public class OLEReEncumberRecurringOrdersJobServiceImpl extends PlatformAwareDaoBaseJdbc implements OLEReEncumberRecurringOrdersJobService {

    protected DateTimeService dateTimeService;
    protected RunDateService runDateService;
    protected ParameterService parameterService;

    @Override
    public void retrieveReEncumberRecuringOrders() {

        String paramaterValue = getParameter(OLEConstants.REENCUMBER_RECURRING_ORDERS);
        String fromDate =  getParameter(OLEConstants.FROM_DATE);
        String toDate =  getParameter(OLEConstants.TO_DATE);
        java.sql.Date today = new java.sql.Date(getDateTimeService().getCurrentTimestamp().getTime());
        String dbVendor = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.DB_VENDOR);
        String[] value = paramaterValue.split(",");
        List<String> entriesList = new ArrayList<>();
        List<Map<String, Object>> poItemData = new ArrayList<Map<String, Object>>();
        Integer fiscalYear =  new Integer(getParameterService().getParameterValueAsString(OleParameterConstants.GENERAL_LEDGER_BATCH.class, GeneralLedgerConstants.ANNUAL_CLOSING_FISCAL_YEAR_PARM));
        if (value.length > 0 && value[0].equalsIgnoreCase(OLEConstants.PO)) {
            if (dbVendor.equals(OLEConstants.MYSQL) && fromDate != null && toDate != null) {
                String formattedFromDateForMySQL = formatDateForMySQL(fromDate);
                String formattedToDateForMySQL = formatDateForMySQL(toDate);
                String query = "select * from GL_ENTRY_T where  FIN_BALANCE_TYP_CD='EX' and FDOC_NBR in (select FDOC_NBR from PUR_PO_T where PO_ID in (select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null and  PO_CRTE_DT between '"+formattedFromDateForMySQL+"' AND '" +formattedToDateForMySQL+"')))";
                poItemData = getSimpleJdbcTemplate().queryForList(query);
            } else {
                String formattedFromDateForOracle = formatDateForOracle(fromDate);
                String formattedToDateForOracle = formatDateForOracle(toDate);
                String query = "select * from GL_ENTRY_T where  FIN_BALANCE_TYP_CD='EX' and FDOC_NBR in (select FDOC_NBR from PUR_PO_T where PO_ID in (select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null and  PO_CRTE_DT between '"+formattedFromDateForOracle+"' AND '" +formattedToDateForOracle+"')))";
                poItemData = getSimpleJdbcTemplate().queryForList(query);
            }

            if (poItemData.size() > 0) {
                try {
                    entriesList.clear();
                    for (Map<String, Object> poDataMap : poItemData) {

                        Entry entry = new Entry();
                        entry.setUniversityFiscalYear(fiscalYear+1);
                        entry.setChartOfAccountsCode(poDataMap.get(OLEConstants.GL_CHART_CD).toString());
                        entry.setAccountNumber(poDataMap.get(OLEConstants.GL_ACCOUNT_NBR).toString());
                        entry.setSubAccountNumber(poDataMap.get(OLEConstants.GL_SUB_ACCT_NBR).toString());
                        entry.setFinancialObjectCode(poDataMap.get(OLEConstants.GL_OBJ_CD).toString());
                        entry.setFinancialSubObjectCode(poDataMap.get(OLEConstants.GL_SUB_OBJ_CD).toString());
                        entry.setFinancialBalanceTypeCode(poDataMap.get(OLEConstants.GL_BAL_TYP_CD).toString());
                        entry.setFinancialObjectTypeCode(poDataMap.get(OLEConstants.GL_OBJ_TYP_CD).toString());
                        entry.setUniversityFiscalPeriodCode(poDataMap.get(OLEConstants.GL_UNIV_FISC_PERIOD_CD).toString());
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
                        if (paramaterValue.contains("+")) {
                            String amount = paramaterValue.replaceAll("[^0-9]", "");
                            KualiDecimalAdapter kualiDecimalAdapter = new KualiDecimalAdapter();
                            KualiDecimal dollar = kualiDecimalAdapter.unmarshal(amount);
                            if (paramaterValue.contains("$")) {
                                transactionLedgerEntryAmount = transactionLedgerEntryAmount.add(dollar);
                            } else {
                                transactionLedgerEntryAmount = transactionLedgerEntryAmount.add((transactionLedgerEntryAmount.multiply(dollar)).divide(new KualiDecimal(100)));
                            }
                        } else {
                            String amount = paramaterValue.replaceAll("[^0-9]", "");
                            KualiDecimalAdapter kualiDecimalAdapter = new KualiDecimalAdapter();
                            KualiDecimal dollar = kualiDecimalAdapter.unmarshal(amount);
                            if (paramaterValue.contains("$")) {
                                transactionLedgerEntryAmount = transactionLedgerEntryAmount.subtract(dollar);
                            } else {
                                transactionLedgerEntryAmount = transactionLedgerEntryAmount.subtract((transactionLedgerEntryAmount.multiply(dollar)).divide(new KualiDecimal(100)));
                            }
                        }
                        entry.setTransactionLedgerEntryAmount(transactionLedgerEntryAmount);
                        entry.setTransactionDebitCreditCode(poDataMap.get(OLEConstants.GL_TRANS_DEB_CRE_CD).toString());
                        entry.setTransactionDate(today);
                        OriginEntryFull originEntryFullentry = new OriginEntryFull(entry);
                        entriesList.add(originEntryFullentry.getLine());
                    }
                    String fileDirectory = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEConstants.STAGING_DIRECTORY) + OLEConstants.REENCUMBER_FILE_DIRECTORY;
                    new File(fileDirectory).mkdir();
                    Date runDate = calculateRunDate(getDateTimeService().getCurrentDate());
                    String filePath = fileDirectory +OLEConstants.REENCUMBER_FILE_PATH+runDate+ GeneralLedgerConstants.BatchFileSystem.EXTENSION;
                    File file = new File(filePath);
                    file.createNewFile();
                    BufferedWriter bwr = new BufferedWriter(new FileWriter(file));
                    if (entriesList != null && entriesList.size() > 0) {
                        for (String entry : entriesList) {
                            bwr.write(entry);
                            bwr.write("\n");
                        }
                    }
                    bwr.flush();
                    bwr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (value.length > 0 && value[0].equalsIgnoreCase(OLEConstants.INVOICE)) {
            List<Map<String, Object>> invoiceItemData = new ArrayList<Map<String, Object>>();
            //The below query bring the the sum transaction amount from the payment request record of gl_entry_t table and rest of the fields from the PO record of the gl_entry_t table
            if (dbVendor.equals(OLEConstants.MYSQL) && fromDate != null && toDate != null) {
                String formattedFromDateForMySQL = formatDateForMySQL(fromDate);
                String formattedToDateForMySQL = formatDateForMySQL(toDate);
            String query = "SELECT \n" +
                    "GE1.FDOC_REF_NBR,GE1.UNIV_FISCAL_YR,GE1.FDOC_NBR,GE1.FIN_COA_CD,GE1.ACCOUNT_NBR,GE1.SUB_ACCT_NBR,GE1.FIN_OBJECT_CD,GE1.FIN_SUB_OBJ_CD,\n" +
                    "GE1.FIN_BALANCE_TYP_CD,GE1.FIN_OBJ_TYP_CD,GE1.UNIV_FISCAL_PRD_CD,GE1.FDOC_TYP_CD,GE1.FS_ORIGIN_CD,GE1.TRN_ENTR_SEQ_NBR,GE1.TRN_LDGR_ENTR_DESC,\n" +
                    "GE1.TRN_DEBIT_CRDT_CD,GE1.PROJECT_CD,GE1.FDOC_REF_TYP_CD,GE1.FS_REF_ORIGIN_CD,GE1.TRN_ENCUM_UPDT_CD,SUM(GE2.AMT) AS AMT \n" +
                    "FROM \n" +
                    "GL_ENTRY_T GE1,\n" +
                    "(SELECT \n" +
                    "(SELECT DISTINCT(FDOC_NBR) FROM GL_ENTRY_T GE3 WHERE GE3.FDOC_REF_NBR=GE.FDOC_REF_NBR AND FDOC_TYP_CD='OLE_PO' AND TRN_DEBIT_CRDT_CD='D') AS GEFDOC_REF_NBR,\n" +
                    "  SUM(TRN_LDGR_ENTR_AMT) AS AMT,GE.ACCOUNT_NBR AS ACCNBR,GE.SUB_ACCT_NBR AS SUBACCNBR,GE.FIN_OBJECT_CD AS OBJCD \n" +
                    "  FROM GL_ENTRY_T GE,AP_PMT_RQST_T PREQ,PUR_PO_T PO\n" +
                    "  WHERE GE.FIN_BALANCE_TYP_CD='AC' AND GE.TRN_DEBIT_CRDT_CD='D' \n" +
                    "  AND GE.FDOC_NBR=PREQ.FDOC_NBR AND PO.PO_ID=PREQ.PO_ID AND PO.RECUR_PMT_TYP_CD IS NOT NULL AND  PO_CRTE_DT BETWEEN '"+formattedFromDateForMySQL+"' AND '" +formattedToDateForMySQL+"'\n" +
                    "  AND PO.FDOC_NBR=(SELECT DISTINCT(FDOC_NBR) FROM GL_ENTRY_T GE4 WHERE PO.PO_ID=GE4.FDOC_REF_NBR AND FDOC_TYP_CD='OLE_PO' AND TRN_DEBIT_CRDT_CD='D')\n" +
                    "  GROUP BY PREQ.PO_ID,GE.FDOC_REF_NBR,GE.ACCOUNT_NBR,GE.SUB_ACCT_NBR,GE.FIN_OBJECT_CD) GE2 \n" +
                    "WHERE \n" +
                    "GE1.FDOC_NBR=GE2.GEFDOC_REF_NBR AND GE1.ACCOUNT_NBR=GE2.ACCNBR \n" +
                    "GROUP BY\n" +
                    "GE1.FDOC_REF_NBR,GE1.UNIV_FISCAL_YR,GE1.FDOC_NBR,GE1.FIN_COA_CD,GE1.ACCOUNT_NBR,GE1.SUB_ACCT_NBR,GE1.FIN_OBJECT_CD,GE1.FIN_SUB_OBJ_CD,GE1.FIN_BALANCE_TYP_CD,\n" +
                    "GE1.FIN_OBJ_TYP_CD,GE1.UNIV_FISCAL_PRD_CD,GE1.FDOC_TYP_CD,GE1.FS_ORIGIN_CD,GE1.TRN_ENTR_SEQ_NBR,GE1.TRN_LDGR_ENTR_DESC,GE1.TRN_DEBIT_CRDT_CD,GE1.PROJECT_CD,GE1.FDOC_REF_TYP_CD,GE1.FS_REF_ORIGIN_CD,GE1.TRN_ENCUM_UPDT_CD \n" +
                    "ORDER BY GE1.FDOC_REF_NBR,GE1.ACCOUNT_NBR,GE1.SUB_ACCT_NBR,GE1.FIN_OBJECT_CD";

                    invoiceItemData = getSimpleJdbcTemplate().queryForList(query);
            }else {
                String formattedFromDateForOracle = formatDateForOracle(fromDate);
                String formattedToDateForOracle = formatDateForOracle(toDate);
                String query = "SELECT \n" +
                        "GE1.FDOC_REF_NBR,GE1.UNIV_FISCAL_YR,GE1.FDOC_NBR,GE1.FIN_COA_CD,GE1.ACCOUNT_NBR,GE1.SUB_ACCT_NBR,GE1.FIN_OBJECT_CD,GE1.FIN_SUB_OBJ_CD,\n" +
                        "GE1.FIN_BALANCE_TYP_CD,GE1.FIN_OBJ_TYP_CD,GE1.UNIV_FISCAL_PRD_CD,GE1.FDOC_TYP_CD,GE1.FS_ORIGIN_CD,GE1.TRN_ENTR_SEQ_NBR,GE1.TRN_LDGR_ENTR_DESC,\n" +
                        "GE1.TRN_DEBIT_CRDT_CD,GE1.PROJECT_CD,GE1.FDOC_REF_TYP_CD,GE1.FS_REF_ORIGIN_CD,GE1.TRN_ENCUM_UPDT_CD,SUM(GE2.AMT) AS AMT \n" +
                        "FROM \n" +
                        "GL_ENTRY_T GE1,\n" +
                        "(SELECT \n" +
                        "(SELECT DISTINCT(FDOC_NBR) FROM GL_ENTRY_T GE3 WHERE GE3.FDOC_REF_NBR=GE.FDOC_REF_NBR AND FDOC_TYP_CD='OLE_PO' AND TRN_DEBIT_CRDT_CD='D') AS GEFDOC_REF_NBR,\n" +
                        "  SUM(TRN_LDGR_ENTR_AMT) AS AMT,GE.ACCOUNT_NBR AS ACCNBR,GE.SUB_ACCT_NBR AS SUBACCNBR,GE.FIN_OBJECT_CD AS OBJCD \n" +
                        "  FROM GL_ENTRY_T GE,AP_PMT_RQST_T PREQ,PUR_PO_T PO\n" +
                        "  WHERE GE.FIN_BALANCE_TYP_CD='AC' AND GE.TRN_DEBIT_CRDT_CD='D' \n" +
                        "  AND GE.FDOC_NBR=PREQ.FDOC_NBR AND PO.PO_ID=PREQ.PO_ID AND PO.RECUR_PMT_TYP_CD IS NOT NULL AND  PO_CRTE_DT BETWEEN '"+formattedFromDateForOracle+"' AND '" +formattedToDateForOracle+"'\n" +
                        "  AND PO.FDOC_NBR=(SELECT DISTINCT(FDOC_NBR) FROM GL_ENTRY_T GE4 WHERE PO.PO_ID=GE4.FDOC_REF_NBR AND FDOC_TYP_CD='OLE_PO' AND TRN_DEBIT_CRDT_CD='D')\n" +
                        "  GROUP BY PREQ.PO_ID,GE.FDOC_REF_NBR,GE.ACCOUNT_NBR,GE.SUB_ACCT_NBR,GE.FIN_OBJECT_CD) GE2 \n" +
                        "WHERE \n" +
                        "GE1.FDOC_NBR=GE2.GEFDOC_REF_NBR AND GE1.ACCOUNT_NBR=GE2.ACCNBR \n" +
                        "GROUP BY\n" +
                        "GE1.FDOC_REF_NBR,GE1.UNIV_FISCAL_YR,GE1.FDOC_NBR,GE1.FIN_COA_CD,GE1.ACCOUNT_NBR,GE1.SUB_ACCT_NBR,GE1.FIN_OBJECT_CD,GE1.FIN_SUB_OBJ_CD,GE1.FIN_BALANCE_TYP_CD,\n" +
                        "GE1.FIN_OBJ_TYP_CD,GE1.UNIV_FISCAL_PRD_CD,GE1.FDOC_TYP_CD,GE1.FS_ORIGIN_CD,GE1.TRN_ENTR_SEQ_NBR,GE1.TRN_LDGR_ENTR_DESC,GE1.TRN_DEBIT_CRDT_CD,GE1.PROJECT_CD,GE1.FDOC_REF_TYP_CD,GE1.FS_REF_ORIGIN_CD,GE1.TRN_ENCUM_UPDT_CD \n" +
                        "ORDER BY GE1.FDOC_REF_NBR,GE1.ACCOUNT_NBR,GE1.SUB_ACCT_NBR,GE1.FIN_OBJECT_CD";

                        invoiceItemData = getSimpleJdbcTemplate().queryForList(query);
            }
            if (invoiceItemData.size() > 0) {
                try {
                    entriesList.clear();
                    for (Map<String, Object> invoiceDataMap : invoiceItemData) {
                        Entry entry = new Entry();
                        entry.setUniversityFiscalYear(fiscalYear+1);
                        entry.setChartOfAccountsCode(invoiceDataMap.get(OLEConstants.GL_CHART_CD).toString());
                        entry.setAccountNumber(invoiceDataMap.get(OLEConstants.GL_ACCOUNT_NBR).toString());
                        entry.setSubAccountNumber(invoiceDataMap.get(OLEConstants.GL_SUB_ACCT_NBR).toString());
                        entry.setFinancialObjectCode(invoiceDataMap.get(OLEConstants.GL_OBJ_CD).toString());
                        entry.setFinancialSubObjectCode(invoiceDataMap.get(OLEConstants.GL_SUB_OBJ_CD).toString());
                        entry.setFinancialBalanceTypeCode(invoiceDataMap.get(OLEConstants.GL_BAL_TYP_CD).toString());
                        entry.setFinancialObjectTypeCode(invoiceDataMap.get(OLEConstants.GL_OBJ_TYP_CD).toString());
                        entry.setUniversityFiscalPeriodCode(invoiceDataMap.get(OLEConstants.GL_UNIV_FISC_PERIOD_CD).toString());
                        entry.setFinancialDocumentTypeCode(invoiceDataMap.get(OLEConstants.GL_FIN_DOC_TYP_CD).toString());
                        entry.setFinancialSystemOriginationCode(invoiceDataMap.get(OLEConstants.GL_FIN_SYS_ORG_CD).toString());
                        entry.setDocumentNumber(invoiceDataMap.get(OLEConstants.GL_DOC_NBR).toString());
                        entry.setTransactionLedgerEntrySequenceNumber(Integer.parseInt(invoiceDataMap.get(OLEConstants.TRANS_LED_SEQ_NO).toString()));
                        entry.setTransactionLedgerEntryDescription(invoiceDataMap.get(OLEConstants.GL_TRANS_LED_ENTRY_DESC).toString());
                        KualiDecimal transactionLedgerEntryAmount = new KualiDecimal((BigDecimal) invoiceDataMap.get(OLEConstants.GL_TOTAL_INV_AMT));
                        if (paramaterValue.contains("+")) {
                            String amount = paramaterValue.replaceAll("[^0-9]", "");
                            KualiDecimalAdapter kualiDecimalAdapter = new KualiDecimalAdapter();
                            KualiDecimal dollar = kualiDecimalAdapter.unmarshal(amount);
                            if (paramaterValue.contains("$")) {
                                transactionLedgerEntryAmount = transactionLedgerEntryAmount.add(dollar);
                            } else {
                                transactionLedgerEntryAmount = transactionLedgerEntryAmount.add((transactionLedgerEntryAmount.multiply(dollar)).divide(new KualiDecimal(100)));
                            }
                        } else {
                            String amount = paramaterValue.replaceAll("[^0-9]", "");
                            KualiDecimalAdapter kualiDecimalAdapter = new KualiDecimalAdapter();
                            KualiDecimal dollar = kualiDecimalAdapter.unmarshal(amount);
                            if (paramaterValue.contains("$")) {
                                transactionLedgerEntryAmount = transactionLedgerEntryAmount.subtract(dollar);
                            } else {
                                transactionLedgerEntryAmount = transactionLedgerEntryAmount.subtract((transactionLedgerEntryAmount.multiply(dollar)).divide(new KualiDecimal(100)));
                            }
                        }

                        entry.setTransactionLedgerEntryAmount(transactionLedgerEntryAmount);
                        entry.setTransactionDebitCreditCode(invoiceDataMap.get(OLEConstants.GL_TRANS_DEB_CRE_CD).toString());
                        entry.setProjectCode(invoiceDataMap.get(OLEConstants.PROJECT_CODE).toString());
                        entry.setReferenceFinancialDocumentTypeCode(invoiceDataMap.get(OLEConstants.REF_DOC_TYP_CD).toString());
                        entry.setReferenceFinancialSystemOriginationCode(invoiceDataMap.get(OLEConstants.REF_ORG_CD).toString());
                        entry.setTransactionEncumbranceUpdateCode(OLEConstants.ENCUM_UPDT_CD);
                        entry.setReferenceFinancialDocumentNumber(invoiceDataMap.get(OLEConstants.FDOC_REF_NBR).toString());
                        entry.setTransactionDate(today);
                       // entry.setTransactionDate(new java.sql.Date(((Timestamp) invoiceDataMap.get(OLEConstants.GL_TRANS_DT)).getTime()));
                        OriginEntryFull originEntryFullentry = new OriginEntryFull(entry);
                        entriesList.add(originEntryFullentry.getLine());
                    }
                    String fileDirectory = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEConstants.STAGING_DIRECTORY) + OLEConstants.REENCUMBER_FILE_DIRECTORY;
                    new File(fileDirectory).mkdir();
                    Date runDate = calculateRunDate(getDateTimeService().getCurrentDate());
                    String filePath = fileDirectory + OLEConstants.REENCUMBER_FILE_PATH + runDate + GeneralLedgerConstants.BatchFileSystem.EXTENSION;
                    File file = new File(filePath);
                    file.createNewFile();
                    BufferedWriter bwr = new BufferedWriter(new FileWriter(file));
                    if (entriesList != null && entriesList.size() > 0) {
                        for (String entry : entriesList) {
                            bwr.write(entry);
                            bwr.write("\n");
                        }
                    }
                    bwr.flush();
                    bwr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }


    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

    public DateTimeService getDateTimeService() {
        if(dateTimeService == null) {
            return SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }


    public RunDateService getRunDateService() {
        if(runDateService == null) {
            return SpringContext.getBean(RunDateService.class);
        }
        return runDateService;
    }

    public java.sql.Date calculateRunDate(java.util.Date currentDate) {
        return new java.sql.Date(getRunDateService().calculateRunDate(currentDate).getTime());
    }

    public ParameterService getParameterService() {
        if(parameterService == null) {
            parameterService = SpringContext.getBean(ParameterService.class);
        }
        return parameterService;
    }

    private String formatDateForOracle(String date) {
        String forOracle = DateFormatHelper.getInstance().generateDateStringsForOracle(date);
        return forOracle;
    }

    private String formatDateForMySQL(String date) {
        java.util.Date  ss1=new Date(date);
        SimpleDateFormat formatter=new SimpleDateFormat(OLEConstants.RENCUM_DATE_FORMAT);
        String forMysql = formatter.format(ss1);
        return forMysql;
    }

}

