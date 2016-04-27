package org.kuali.ole.batch.ingest;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.dataaccess.OleFundCodePreparedStatementCachingDao;
import org.kuali.ole.batch.impl.AbstractBatchProcess;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.sys.businessobject.FinancialSystemDocumentHeader;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.document.DocumentBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BatchProcessFundCodeImport extends AbstractBatchProcess {
    private static final Logger LOG = Logger.getLogger(BatchProcessFundCodeImport.class);
    private String principalName = GlobalVariables.getUserSession().getPrincipalName();
    private String fundCodeFileContent = null;
    private String fundAcclnFileContent = null;
    private String fundCodeFileName =null;
    private String fundAcclnFileName =null;
    protected OleFundCodePreparedStatementCachingDao oleFundCodeDao;
    int totalRecordSize = 0;
    int docSuccessCount = 0;
    int docFailureCount = 0;
    List<OleFundCode> docFailureList = new ArrayList<>();
    List<OleFundCodeAccountingLine> fndAcclnFailureList = new ArrayList<>();
    int acctSuccessCount = 0;
    int acctFailureCount = 0;
    List<OleFundCode> oleFundCodesDocumentList = new ArrayList<>();
    Map<String, List<OleFundCodeAccountingLine>> oleFundCodeAccountingLineMap = new HashMap<>();
    List<OleFundCode> oleFundCodeDocumentList = new ArrayList<>();
    List<OleFundCode> existingFundCodeDocumentList = new ArrayList<>();
    List<OleFundCodeAccountingLine> existingFundCodeAccountingLineDocumentList = new ArrayList<>();
    List<String> serialIdList = new ArrayList<>();


    public OleFundCodePreparedStatementCachingDao getOleFundCodeDao() {
        if (oleFundCodeDao == null) {
            oleFundCodeDao = SpringContext.getBean(OleFundCodePreparedStatementCachingDao.class);
            oleFundCodeDao.initialize();
        }
        return oleFundCodeDao;
    }


    @Override
    protected void prepareForRead() throws Exception {

        String[] fileNames = job.getUploadFileName().split(",");
            for(String fileName : fileNames) {
                if (fileName.endsWith(getParameter(OLEConstants.OLEBatchProcess.FUND_RECORD_NAME)+".csv")) {
                    fundCodeFileName = getBatchProcessFilePath(processDef.getBatchProcessType()) + job.getJobId() + FileSystems.getDefault().getSeparator()+ job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + fileName;
                    fundCodeFileContent = getBatchProcessFileContent(fileName);
                } else if (fileName.endsWith(getParameter(OLEConstants.OLEBatchProcess.FUND_ACCOUNTING_LINE_RECORD_NAME)+".csv")) {
                    fundAcclnFileName = getBatchProcessFilePath(processDef.getBatchProcessType()) +job.getJobId()+FileSystems.getDefault().getSeparator() +job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + fileName;
                    fundAcclnFileContent = getBatchProcessFileContent(fileName);
                }

            }
        job.setNoOfSuccessRecords("0");
        job.setNoOfFailureRecords("0");
        job.setNoOfRecordsProcessed("0");
    }


    /**
     * This method is used to get the file name from the system parameter
     * @param name
     * @return  parameter
     */
    private String getParameter(String name) {
        LOG.info("Parameter Name : "+ name);
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", OLEConstants.SYS_NMSPC);
            criteriaMap.put("componentCode", OLEConstants.BATCH_CMPNT);
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>) KRADServiceLocator.getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Parameter Value : " + parameter);
        return parameter;
    }

    @Override
    public void prepareForWrite() throws Exception {
        OLEFundCodeRecordSummary oleFundCodeRecordSummary = null;

        oleFundCodeRecordSummary = createOLEFundDocumentFromCsv(fundCodeFileName, fundAcclnFileName);

        String[] fileNames = job.getUploadFileName().split(",");
        for(String fileName : fileNames){
            deleteBatchFile(fileName);
        }

        int totalCount = docSuccessCount +  docFailureCount;
        int faiCount = docFailureCount;
        job.setTotalNoOfRecords(totalCount + "");
        job.setNoOfRecordsProcessed(totalCount + "");
        job.setNoOfSuccessRecords(totalCount - faiCount + "");
        job.setNoOfFailureRecords(faiCount + "");
        job.setFndAcclnFailureCount(existingFundCodeAccountingLineDocumentList.size() + "");
        job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        job.setStatusDesc(OLEConstants.OLEBatchProcess.FUND_RECORD_SUCCESS);

        if (oleFundCodeRecordSummary.getDocFailureList()!=null && oleFundCodeRecordSummary.getDocFailureList().size()>0){
            OLEFundCodeFailureDocuments oleFundCodeFailureDocuments = createOLEFundCodeFailureDocuments(oleFundCodeRecordSummary.getDocFailureList());
            String content = getFundRecordFailureDocumentsXmlContent(oleFundCodeFailureDocuments);
            createBatchFailureFile(content, StringUtils.substringAfter(fundCodeFileName, getBatchProcessFilePath(processDef.getBatchProcessType()) + job.getJobId() + FileSystems.getDefault().getSeparator() + job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_").replace("csv","xml"));
        }
        if (oleFundCodeRecordSummary.getFndAcclnFailureList() !=null && oleFundCodeRecordSummary.getFndAcclnFailureList().size()>0){
            OLEFundAccountingLineFailureRecords oleFundAccountingLineFailureRecords = createOLEFundCodeAcctLineFailureDocuments(oleFundCodeRecordSummary.getFndAcclnFailureList());
            String content = getFundAcctFailureDocumentsXmlContent(oleFundAccountingLineFailureRecords);
            createBatchFailureFile(content, StringUtils.substringAfter(fundAcclnFileName, getBatchProcessFilePath(processDef.getBatchProcessType()) + job.getJobId() + FileSystems.getDefault().getSeparator() + job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB  + "_").replace("csv", "xml"));
        }

    }


    private OLEFundCodeFailureDocuments createOLEFundCodeFailureDocuments(List<OleFundCode> oleFundCodeDocuments){
        LOG.info("Inside createOLEFundCodeFailureDocuments for creating  OLEFundCodeFailureDocuments for the " +oleFundCodeDocuments.size() + "of OLEFundCodegDocuments");
        OLEFundCodeFailureDocuments oleFundCodeFailureDocuments = new OLEFundCodeFailureDocuments();
        List<OLEFundCodeFailureDocument> oleFundCodeFailureDocumentList = new ArrayList<OLEFundCodeFailureDocument>();
        OLEFundCodeFailureDocument oleFundCodeFailureDocument ;

        for(OleFundCode oleFundCodegDocument : existingFundCodeDocumentList){
            oleFundCodeFailureDocument = new OLEFundCodeFailureDocument();
            oleFundCodeFailureDocument.setOleFundCodeDocument(oleFundCodegDocument);

            oleFundCodeFailureDocument.setErrorMessage("Fund Code ID  " + oleFundCodegDocument.getFundCodeId() + "    already exists");
            oleFundCodeFailureDocumentList.add(oleFundCodeFailureDocument);
        }

        oleFundCodeFailureDocuments.setOleFundCodeFailureDocuments(oleFundCodeFailureDocumentList);
        if(oleFundCodeFailureDocumentList!=null && oleFundCodeFailureDocumentList.size()>0){
            LOG.info(oleFundCodeFailureDocumentList.size()+"number of OLEFundRecordFailureDocument created ");
        } else{
            LOG.info("No FundCodeFailureDocument created ");
        }
        return oleFundCodeFailureDocuments;
    }

    private OLEFundAccountingLineFailureRecords createOLEFundCodeAcctLineFailureDocuments(List<OleFundCodeAccountingLine> oleFundCodeAcctLineDocuments){
        LOG.info("Inside createOLEFundCodeAcctLineFailureDocuments for creating  OLEFundAccountingLineFailureRecords for the " +oleFundCodeAcctLineDocuments.size() + "of OleFundCodeAccountingLine");
        OLEFundAccountingLineFailureRecords oleFundAccountingLineFailureRecords = new OLEFundAccountingLineFailureRecords();
        List<OLEFundAccountingLineFailureRecord> oleFundAccountingLineFailureRecordtList = new ArrayList<OLEFundAccountingLineFailureRecord>();
        OLEFundAccountingLineFailureRecord oleFundAccountingLineFailureRecord ;

        for(OleFundCodeAccountingLine oleFundCodeAccountingLine : fndAcclnFailureList){
            oleFundAccountingLineFailureRecord = new OLEFundAccountingLineFailureRecord();
            oleFundAccountingLineFailureRecord.setOleFundCodeAccountingLine(oleFundCodeAccountingLine);

            oleFundAccountingLineFailureRecord.setErrorMessage("Fund Code ID    " + oleFundCodeAccountingLine.getFundCodeAccountingLineId() + "      already exists");
            oleFundAccountingLineFailureRecordtList.add(oleFundAccountingLineFailureRecord);
        }

        oleFundAccountingLineFailureRecords.setOLEFundAccountingLineFailureRecords(oleFundAccountingLineFailureRecordtList);
        if(oleFundAccountingLineFailureRecordtList!=null && oleFundAccountingLineFailureRecordtList.size()>0){
            LOG.info(oleFundAccountingLineFailureRecordtList.size()+"number of OLEFundRecordFailureDocument created ");
        } else{
            LOG.info("No FundCodeFailureDocument created ");
        }
        return oleFundAccountingLineFailureRecords;
    }

    public String getFundRecordFailureDocumentsXmlContent(OLEFundCodeFailureDocuments oleFundCodeFailureDocuments){
        XStream xStream = new XStream();
        xStream.omitField(FinancialSystemDocumentHeader.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "documentNumber");
        xStream.omitField(DocumentBase.class, "pessimisticLocks");
        xStream.omitField(DocumentBase.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "adHocRoutePersons");
        xStream.omitField(DocumentBase.class, "adHocRouteWorkgroups");
        xStream.omitField(DocumentBase.class, "notes");
        xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
        xStream.alias("FundCodeFailureDocumentRecord", OLEFundCodeFailureDocuments.class);
        xStream.alias("FundCodeFailureDocument",OLEFundCodeFailureDocument.class);
        xStream.alias("FundCodeDocument",OleFundCode.class);
        xStream.aliasField("FundCodeFailureDocuments",OLEFundCodeFailureDocuments.class,"oleFundCodeFailureDocuments");
        String content = xStream.toXML(oleFundCodeFailureDocuments);
        return  content;
    }


    public String getFundAcctFailureDocumentsXmlContent(OLEFundAccountingLineFailureRecords oleFundAccountingLineFailureRecords){
        XStream xStream = new XStream();
        xStream.omitField(FinancialSystemDocumentHeader.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "documentNumber");
        xStream.omitField(DocumentBase.class, "pessimisticLocks");
        xStream.omitField(DocumentBase.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "adHocRoutePersons");
        xStream.omitField(DocumentBase.class, "adHocRouteWorkgroups");
        xStream.omitField(DocumentBase.class, "notes");
        xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
        xStream.alias("FundCodeAcctLineFailureDocuments", OLEFundAccountingLineFailureRecords.class);
        xStream.alias("FundAccountingLineFailureRecord",OLEFundAccountingLineFailureRecords.class);
        xStream.alias("FundCodeAccountingDocument",OleFundCodeAccountingLine.class);
        xStream.aliasField("FundCodeAcctLineFailureDocuments",OLEFundAccountingLineFailureRecords.class,"oleFundCodeAcctLineFailureDocuments");
        String content = xStream.toXML(oleFundAccountingLineFailureRecords);
        return  content;
    }


    @Override
    public void getNextBatch() {

    }

    @Override
    public void processBatch() {

    }

    public OLEFundCodeRecordSummary createOLEFundDocumentFromCsv(String fundCodeDocument,String fundCodeAccountingLine) throws SQLException {
        LOG.debug("Inside createOLEFundDocumentFromCsv ");
        try {
            if (fundCodeDocument != null && !fundCodeDocument.trim().isEmpty()) {
                oleFundCodeDocumentList = processFundCodeDocument(fundCodeDocument);
                if (fundCodeAccountingLine != null && !fundCodeAccountingLine.trim().isEmpty()) {
                    oleFundCodeAccountingLineMap = processFundCodeAccountingLines(fundCodeAccountingLine);
                }

                for (OleFundCode oleFundCodeDocument : oleFundCodeDocumentList) {
                    oleFundCodeDocument.setOleFundCodeAccountingLineList(oleFundCodeAccountingLineMap.get(oleFundCodeDocument.getFundCodeId()));
                    oleFundCodesDocumentList.add(oleFundCodeDocument);

                }
                getOleFundCodeDao();

                for(OleFundCode oleFundCodeDocument : oleFundCodesDocumentList) {
                    if(oleFundCodeDao.getOleFundCode(oleFundCodeDocument) != null) {
                        existingFundCodeDocumentList.add(oleFundCodeDocument);

                    }
                    else if(oleFundCodeDao.getOleFundCodeByObjectId(oleFundCodeDocument) != null) {
                        existingFundCodeDocumentList.add(oleFundCodeDocument);
                    }
                    else {
                        oleFundCodeDao.insertOleFundCode(oleFundCodeDocument);
                    }
                        if(oleFundCodeDocument.getOleFundCodeAccountingLineList() != null && oleFundCodeDocument.getOleFundCodeAccountingLineList().size() > 0) {
                        for(OleFundCodeAccountingLine oleFundCodeAccountingLine : oleFundCodeDocument.getOleFundCodeAccountingLineList()) {
                            if(oleFundCodeDao.getOleFundCodeAccountingLine(oleFundCodeAccountingLine) != null) {
                                existingFundCodeAccountingLineDocumentList.add(oleFundCodeAccountingLine);
                            }
                            else {
                                oleFundCodeDao.insertOleFundCodeAccountingLine(oleFundCodeAccountingLine);
                            }
                        }

                    }
                }
                for(OleFundCode existingFundCodeDocument : existingFundCodeDocumentList) {
                    if(existingFundCodeDocument.getOleFundCodeAccountingLineList() != null && existingFundCodeDocument.getOleFundCodeAccountingLineList().size() > 0) {
                        for(OleFundCodeAccountingLine oleFundCodeAccountingLine : existingFundCodeDocument.getOleFundCodeAccountingLineList()) {
                            if(oleFundCodeDao.getOleFundCodeAccountingLine(oleFundCodeAccountingLine) != null) {
                                existingFundCodeAccountingLineDocumentList.add(oleFundCodeAccountingLine);
                            }
                            else {
                                oleFundCodeDao.insertOleFundCodeAccountingLine(oleFundCodeAccountingLine);
                            }
                        }
                    }
                }


                oleFundCodeDao.destroy();
                oleFundCodeDao = null;
                docSuccessCount = oleFundCodesDocumentList.size() - existingFundCodeDocumentList.size();
                docFailureCount = existingFundCodeDocumentList.size();
                docFailureList = existingFundCodeDocumentList;
                acctFailureCount = existingFundCodeAccountingLineDocumentList.size();
                fndAcclnFailureList = existingFundCodeAccountingLineDocumentList;
            }

        } catch (Exception e) {
            LOG.error(e, e);
        }
        if (LOG.isDebugEnabled()){
            LOG.debug("totalRecordSize : "+totalRecordSize +","+"docSuccessCount : "+docSuccessCount +","+"docFailureCount : "+docFailureCount +","+"acctSuccessCount : "+acctSuccessCount +","+"acctFailureCount : "+acctFailureCount);
        }
        return new OLEFundCodeRecordSummary(totalRecordSize, docSuccessCount, docFailureCount, docFailureList,acctSuccessCount,acctFailureCount,fndAcclnFailureList);
    }

    public String getParameter(String name,String namespaceCode,String componentCode) {
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", OLEConstants.SELECT_NMSPC);
            criteriaMap.put("componentCode", OLEConstants.SELECT_CMPNT);
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>) getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return parameter;
    }

    public List<OleFundCode> processFundCodeDocument(String fundCodeDocumentContent) {
        Log.debug("inside processing Fund Code Document");
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = getParameter(OLEConstants.SERIAL_LOADER_DELIMITER,OLEConstants.SELECT_NMSPC,OLEConstants.SELECT_CMPNT);
        if(cvsSplitBy != null && cvsSplitBy.trim().isEmpty()){
            cvsSplitBy = ",";
        }

        List<OleFundCode> oleFundCodeDocumentList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(fundCodeDocumentContent));
            String[] fundCodeDocuments = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] documents = line.split(cvsSplitBy);
                OleFundCode oleFundCodeDocument = new OleFundCode();
                if (count == 0) {
                    fundCodeDocuments = documents;
                }
                int index = 0;
                if (count > 0) {
                    for (String document : documents) {
                        document=document.replace("\"","");
                        if (fundCodeDocuments.length > index && fundCodeDocuments[index]!=null) {
                            if (fundCodeDocuments!=null && fundCodeDocuments[index].equals("FUND_CD_ID")) {
                                index++;
                                oleFundCodeDocument.setFundCodeId(document);
                            } else if (fundCodeDocuments[index].equals("CD")) {
                                index++;
                                oleFundCodeDocument.setFundCode(document);
                            }  else if (fundCodeDocuments[index].equals("OBJ_ID")) {
                                index++;
                                oleFundCodeDocument.setObjectId(document);
                            }   else if (fundCodeDocuments[index].equals("VER_NBR")) {
                                index++;

                                oleFundCodeDocument.setVersionNumber(Long.valueOf(document));
                            }
                            else if (fundCodeDocuments[index].equals("ROW_ACT_IND")) {
                                index++;
                                if (document.equals("Y")) {
                                oleFundCodeDocument.setActive(Boolean.TRUE);
                                }
                                else {
                                    oleFundCodeDocument.setActive(Boolean.FALSE);
                                }
                            }
                            else {
                                index++;
                            }
                        }
                    }
                    oleFundCodeDocumentList.add(oleFundCodeDocument);
                }
                count++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return oleFundCodeDocumentList;
    }


    public Map<String, List<OleFundCodeAccountingLine>> processFundCodeAccountingLines(String fundAcclnFileContent) {
        Map<String, List<OleFundCodeAccountingLine>> oleFundCodeAccountingLineMap = new HashMap<>();

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = getParameter(OLEConstants.SERIAL_LOADER_DELIMITER,OLEConstants.SELECT_NMSPC,OLEConstants.SELECT_CMPNT);
        if(cvsSplitBy != null && cvsSplitBy.trim().isEmpty()){
            cvsSplitBy = ",";
        }
        try {
            br = new BufferedReader(new FileReader(fundAcclnFileContent));
            String[] accountingLines = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] accountingLineDocuments = line.split(cvsSplitBy);
                OleFundCodeAccountingLine oleFundCodeAccountingLine = new OleFundCodeAccountingLine();
                if (count == 0) {
                    accountingLines = accountingLineDocuments;
                }
                int index = 0;
                if (count > 0) {
                    for (String accountingLine : accountingLineDocuments) {
                        accountingLine=accountingLine.replace("\"","");
                        if (accountingLines!=null && accountingLines.length > index && accountingLines[index]!=null) {
                            if (accountingLines[index].equals("FUND_CD_ACCTG_LN_ID")) {
                                index++;
                                oleFundCodeAccountingLine.setFundCodeAccountingLineId(accountingLine);
                            } else if (accountingLines[index].equals("FUND_CD_ID")) {
                                index++;
                                oleFundCodeAccountingLine.setFundCodeId(accountingLine);
                            } else if (accountingLines[index].equals("CHART_CD")) {
                                index++;
                                oleFundCodeAccountingLine.setChartCode(accountingLine);
                            } else if (accountingLines[index].equals("ACCT_NUM")) {
                                index++;
                                oleFundCodeAccountingLine.setAccountNumber(accountingLine);
                            } else if (accountingLines[index].equals("SUB_ACCT_NUM")) {
                                index++;
                                oleFundCodeAccountingLine.setSubAccount(accountingLine);
                            } else if (accountingLines[index].equals("OBJECT_CD")) {
                                index++;
                                oleFundCodeAccountingLine.setObjectCode(accountingLine);
                            } else if (accountingLines[index].equals("SUB_OBJECT_CD")) {
                                index++;
                                oleFundCodeAccountingLine.setSubObject(accountingLine);
                            } else if (accountingLines[index].equals("PROJECT_CD")) {
                                index++;
                                oleFundCodeAccountingLine.setProject(accountingLine);
                            } else if (accountingLines[index].equals("ORG_REF_ID")) {
                                index++;
                                oleFundCodeAccountingLine.setOrgRefId(accountingLine);
                            } else if (accountingLines[index].equals("PERCENTAGE")) {
                                index++;
                                if(accountingLine != null && accountingLine.trim().length() > 0) {
                                oleFundCodeAccountingLine.setPercentage(BigDecimal.valueOf(Long.valueOf(accountingLine)));
                                }
                            } else if (accountingLines[index].equals("OBJ_ID")) {
                                index++;
                                oleFundCodeAccountingLine.setObjectId(accountingLine);
                            } else if (accountingLines[index].equals("VER_NBR")) {
                                index++;
                                oleFundCodeAccountingLine.setVersionNumber(Long.valueOf(accountingLine));
                            }
                            else {
                                index++;
                            }
                        }
                    }
                    boolean keyFound = false;
                    for (String oleAccountingLineId : oleFundCodeAccountingLineMap.keySet()) {
                        if (oleFundCodeAccountingLine!=null && oleFundCodeAccountingLine.getFundCodeId()!=null && oleFundCodeAccountingLine.getFundCodeId().equals(oleAccountingLineId)) {
                            keyFound = true;
                            List<OleFundCodeAccountingLine> oleFundCodeAccountingLineList = oleFundCodeAccountingLineMap.get(oleAccountingLineId);
                            oleFundCodeAccountingLineList.add(oleFundCodeAccountingLine);
                        }
                    }
                    if (!keyFound) {
                        List<OleFundCodeAccountingLine> oleFundCodeAccountingLineList = new ArrayList<>();
                        oleFundCodeAccountingLineList.add(oleFundCodeAccountingLine);
                        oleFundCodeAccountingLineMap.put(oleFundCodeAccountingLine.getFundCodeId(), oleFundCodeAccountingLineList);
                    }
                }
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return oleFundCodeAccountingLineMap;
    }
}
