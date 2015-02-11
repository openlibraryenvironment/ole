package org.kuali.ole.select.document.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.gl.GeneralLedgerConstants;
import org.kuali.ole.gl.batch.service.RunDateService;
import org.kuali.ole.gl.businessobject.Entry;
import org.kuali.ole.gl.businessobject.OriginEntryFull;
import org.kuali.ole.select.document.service.OLEReEncumberRecurringOrdersJobService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.jaxb.KualiDecimalAdapter;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

    @Override
    public void retrieveReEncumberRecuringOrders() {

        String paramaterValue = getParameter(OLEConstants.REENCUMBER_RECURRING_ORDERS);
        String[] value = paramaterValue.split(",");
        List<String> entriesList = new ArrayList<>();
        List<Map<String, Object>> poItemData = new ArrayList<Map<String, Object>>();
        if (value.length > 0 && value[0].equalsIgnoreCase(OLEConstants.PO)) {
            //String query = "select * from GL_ENTRY_T where  FIN_BALANCE_TYP_CD='EX' and FDOC_NBR in (select FDOC_NBR from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null))";
            String query = "select * from GL_ENTRY_T where  FIN_BALANCE_TYP_CD='EX' and FDOC_NBR in (select FDOC_NBR from PUR_PO_T where PO_ID in (select PO_ID from AP_PMT_RQST_T where PO_ID in (select PO_ID from PUR_PO_T where RECUR_PMT_TYP_CD is not null)))";
            poItemData = getSimpleJdbcTemplate().queryForList(query);
            if (poItemData.size() > 0) {
                try {
                    entriesList.clear();
                    for (Map<String, Object> poDataMap : poItemData) {

                        Entry entry = new Entry();
                        entry.setUniversityFiscalYear(Integer.parseInt(poDataMap.get(OLEConstants.GL_UNIV_FIS_YR).toString()));
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
                        entry.setTransactionDate(new java.sql.Date(((Timestamp) poDataMap.get(OLEConstants.GL_TRANS_DT)).getTime()));
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
            String query = "select ge1.UNIV_FISCAL_YR,ge1.FIN_COA_CD,ge1.ACCOUNT_NBR,ge1.SUB_ACCT_NBR,ge1.FIN_OBJECT_CD,ge1.FIN_SUB_OBJ_CD,ge1.FIN_BALANCE_TYP_CD,ge1.FIN_OBJ_TYP_CD,ge1.UNIV_FISCAL_PRD_CD,ge1.FDOC_TYP_CD,ge1.FS_ORIGIN_CD,ge1.FDOC_NBR,ge1.TRN_ENTR_SEQ_NBR,ge1.TRN_LDGR_ENTR_DESC,ge1.TRN_DEBIT_CRDT_CD,ge1.TRANSACTION_DT,ge2.amt from GL_ENTRY_T ge1,(select (select fdoc_nbr from pur_po_t pot where pot.po_id=ge.fdoc_ref_nbr) as gefdoc_ref_nbr, sum(trn_ldgr_entr_amt) amt from GL_ENTRY_T GE,AP_PMT_RQST_T PREQ,PUR_PO_T PO WHERE GE.FIN_BALANCE_TYP_CD='AC' and GE.TRN_DEBIT_CRDT_CD='D' and GE.FDOC_NBR=PREQ.FDOC_NBR AND PO.PO_ID=PREQ.PO_ID AND PO.RECUR_PMT_TYP_CD is not null group by PREQ.PO_ID,GE.account_nbr) ge2 where ge1.fdoc_nbr=ge2.gefdoc_ref_nbr group by ge1.fdoc_nbr,ge1.account_nbr,ge1.fin_object_cd;";


            invoiceItemData = getSimpleJdbcTemplate().queryForList(query);
            if (invoiceItemData.size() > 0) {
                try {
                    entriesList.clear();
                    for (Map<String, Object> invoiceDataMap : invoiceItemData) {

                        Entry entry = new Entry();
                        entry.setUniversityFiscalYear(Integer.parseInt(invoiceDataMap.get(OLEConstants.GL_UNIV_FIS_YR).toString()));
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
                        entry.setTransactionDate(new java.sql.Date(((Timestamp) invoiceDataMap.get(OLEConstants.GL_TRANS_DT)).getTime()));
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
}

