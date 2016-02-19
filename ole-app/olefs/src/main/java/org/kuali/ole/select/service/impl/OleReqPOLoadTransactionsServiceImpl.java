/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLETranscationalRecordGenerator;
import org.kuali.ole.OleOrderRecords;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OrderImportHelperBo;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcNamedFieldsBean;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.bib.marc.Collection;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.edi.*;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.businessobject.OleLoadFailureRecords;
import org.kuali.ole.select.businessobject.OleLoadProfile;
import org.kuali.ole.select.businessobject.OleLoadSumRecords;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.select.document.AcquisitionBatchInputFileDocument;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.select.service.OleReqPOCreateDocumentService;
import org.kuali.ole.select.service.OleReqPOLoadTransactionsService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEConstants.FinancialDocumentTypeCodes;
import org.kuali.ole.sys.batch.service.BatchInputFileService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.beans.factory.InitializingBean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class OleReqPOLoadTransactionsServiceImpl implements OleReqPOLoadTransactionsService, InitializingBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleReqPOLoadTransactionsServiceImpl.class);
    private static final String dateFormat = "MMddyy";

    protected BatchInputFileService batchInputFileService;
    protected OleReqPOCreateDocumentService oleReqPOCreateDocumentService;
    protected BuildVendorBibInfoBean buildVendorBibInfoBean;
    protected Properties properties = null;
    protected ConfigurationService kualiConfigurationService;
    private OleSelectDocumentService oleSelectDocumentService;

    @Override
    public void afterPropertiesSet() throws Exception {
        // properties = loadPropertiesFromClassPath("org/kuali/ole/select/batch/service/impl/bibinfo.properties");
    }


    public BatchInputFileService getBatchInputFileService() {
        return batchInputFileService;
    }

    public void setBatchInputFileService(BatchInputFileService batchInputFileService) {
        this.batchInputFileService = batchInputFileService;
    }

    public OleReqPOCreateDocumentService getOleReqPOCreateDocumentService() {
        if (oleReqPOCreateDocumentService == null) {
            oleReqPOCreateDocumentService = SpringContext.getBean(OleReqPOCreateDocumentService.class);
        }
        return oleReqPOCreateDocumentService;
    }

    public void setOleReqPOCreateDocumentService(OleReqPOCreateDocumentService oleReqPOCreateDocumentService) {
        this.oleReqPOCreateDocumentService = oleReqPOCreateDocumentService;
    }

    public BuildVendorBibInfoBean getBuildVendorBibInfoBean() {
        return buildVendorBibInfoBean;
    }

    public void setBuildVendorBibInfoBean(BuildVendorBibInfoBean buildVendorBibInfoBean) {
        this.buildVendorBibInfoBean = buildVendorBibInfoBean;
    }

    public ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }


    @Override
    public List saveRequisitionDocument(OleOrderRecords oleOrderRecords, OLEBatchProcessJobDetailsBo job) throws Exception {
         /*boolean vendorRecordMappingFlag = false;
        List reqList = new ArrayList(0);
        String vendorRecordMappingProperty = getConfigurationService().getPropertyValueAsString("vendorRecordToRequisitionMapping");
        if (vendorRecordMappingProperty.equalsIgnoreCase("TRUE")) {
            vendorRecordMappingFlag = true;
        }*/
        oleReqPOCreateDocumentService = getOleReqPOCreateDocumentService();
        oleReqPOCreateDocumentService.saveRequisitionDocument(oleOrderRecords, job);
        return job.getOrderImportHelperBo().getReqList();

    }

    @Override
    public void createAcquisitionDocument(List reqList, OleOrderRecords oleOrderRecords, OLEBatchProcessJobDetailsBo job) {
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        List<OleOrderRecord> oleOrderRecordList = oleOrderRecords.getRecords();
        List<OleOrderRecord> failureOrderRecordList = new ArrayList<OleOrderRecord>();
        List<OleOrderRecord> failureOrderRecordListForBFN = new ArrayList<OleOrderRecord>();
        OleLoadSumRecords oleLoadSumRecords = new OleLoadSumRecords();
        //BatchLoadService batchLoadService=(BatchLoadServiceImpl)SpringContext.getBean(BatchLoadServiceImpl.class);
        //OleFailureRecordService oleFailureRecordService = (OleFailureRecordServiceImpl)SpringContext.getBean(OleFailureRecordServiceImpl.class);

        try {
            /*BigDecimal profileId = createAgendaProfile(oleOrderRecordList.get(0).getAgendaName());*/
            String user = null;
            if (oleOrderRecordList.size() > 0) {
                user = GlobalVariables.getUserSession().getPrincipalName();
                if (user == null) {
                    user = getConfigurationService().getPropertyValueAsString(
                            getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
                }
                GlobalVariables.setUserSession(new UserSession(user));
            }
            AcquisitionBatchInputFileDocument acqBatchDocument = createAcquisitionBatchUploadDocument();
            oleLoadSumRecords.setDocumentNumber(acqBatchDocument.getDocumentNumber());
            for (int i = 0; i < oleOrderRecordList.size(); i++) {
                OleOrderRecord oleOrderRecord = oleOrderRecordList.get(i);
                //String[] isbnDupFlag = (((List) oleOrderRecord.getMessageMap().get("rulesEvaluated")).get(0)).toString().split(":");
                //changes start
                String isValidBFN = ((oleOrderRecord.getMessageMap().get("isValidBFN"))).toString();
                String isValidRecord = ((oleOrderRecord.getMessageMap().get("isValidRecord"))).toString();
                //String isBadControlField = ((oleOrderRecord.getMessageMap().get("isBadControlField"))).toString();
                String isApoRule = ((oleOrderRecord.getMessageMap().get("isApoRule"))).toString();
                if ((isValidBFN.equalsIgnoreCase("false")) || (isValidRecord.equalsIgnoreCase("false")) ||  ("true".equalsIgnoreCase(isApoRule))) {
                    failureOrderRecordList.add(oleOrderRecordList.get(i));
                }
            }
            if(oleOrderRecordList.get(0).getOriginalEDIFileName() !=null) {
                saveBatchSuccessRecord(oleLoadSumRecords, failureOrderRecordList.size(), reqList.size(), oleOrderRecordList.size() - failureOrderRecordList.size(), oleOrderRecordList.get(0).getOleOriginalBibRecordFileName() + " , " + oleOrderRecordList.get(0).getOriginalEDIFileName(), oleOrderRecordList.get(0).getDescription(), orderImportHelperBo.getOleBatchProcessProfileBo().getBatchProcessProfileId());
            }
            else {
                saveBatchSuccessRecord(oleLoadSumRecords, failureOrderRecordList.size(), reqList.size(), oleOrderRecordList.size() - failureOrderRecordList.size(), oleOrderRecordList.get(0).getOleOriginalBibRecordFileName(), oleOrderRecordList.get(0).getDescription(), orderImportHelperBo.getOleBatchProcessProfileBo().getBatchProcessProfileId());
            }
            saveBatchFailureRecords(failureOrderRecordList, oleLoadSumRecords.getAcqLoadSumId());
            if (reqList.size() > 0) {
                createReqIdTextFile(reqList, oleLoadSumRecords.getAcqLoadSumId());
            }

            if (failureOrderRecordList.size() > 0) {
                if(oleOrderRecordList.get(0).getOriginalEDIFileName() != null) {
                    createBibAndEdiFile(failureOrderRecordList, oleLoadSumRecords.getAcqLoadSumId());
                }
                else {
                    createBibFile(failureOrderRecordList, oleLoadSumRecords.getAcqLoadSumId());
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in createAcquisitionDocument----  " + e);
            throw new RuntimeException(e);
        }
    }

    public void createBibAndEdiFile(List<OleOrderRecord> failureOrderRecordList, int acqLoadSumId) {
        try {
            BibMarcRecordProcessor bibMarcRecordProcessor=new BibMarcRecordProcessor();
            OLETranscationalRecordGenerator transcationalRecordGenerator = new OLETranscationalRecordGenerator();
            String bibXML = "";
            String ediXML = "";
            List<BibMarcRecord> bibMarcRecordList = new ArrayList<BibMarcRecord>();
            List<EDIOrder> ediOrderList = new ArrayList<EDIOrder>();
            List<EDIOrder> oleTxRecordList = new ArrayList<EDIOrder>();
            Collection bibliographicRecords = new Collection();
            EDIOrders ediOrders = new EDIOrders();
            OleOrderRecord oleOrderRecord = null;
            EDIOrder ediOrder = null;
            String ybpNumber = "";
            List<LineItemOrder> lineItemOrderList = new ArrayList<LineItemOrder>();
            List<String> ybpNumberList = new ArrayList<String>();
            for (int i = 0; i < failureOrderRecordList.size(); i++) {
                String ediYbpNumber = "";
                oleOrderRecord = failureOrderRecordList.get(i);
                if (oleOrderRecord.getOriginalRecord() != null) {
                    bibMarcRecordList.add(oleOrderRecord.getOriginalRecord());
                }
                ediOrder = oleOrderRecord.getOriginalEdi();
                if (oleOrderRecord.getOleTxRecord() != null) {
                    ybpNumber = oleOrderRecord.getOleTxRecord().getVendorItemIdentifier();
                    ybpNumberList.add(ybpNumber);
                }
            }
            for (int j = 0; j < ybpNumberList.size(); j++) {
                String ybp = ybpNumberList.get(j);
                List<LineItemOrder> ediLineItemOrder = ediOrder.getLineItemOrder();
                for (LineItemOrder lineItemOrder : ediLineItemOrder) {
                    BuyerReferenceInformation buyerReferenceInformation = lineItemOrder.getBuyerReferenceInformation().get(0);
                    BuyerLineItemReference buyerLineItemReference = buyerReferenceInformation.getBuyerLineItemReference().get(0);
                    String orderLineNumber = buyerLineItemReference.getOrderLineNumber();
                    if (ybp.equals(orderLineNumber)) {
                        //  System.out.println("lineItemOrder:::::" + lineItemOrder);
                        lineItemOrderList.add(lineItemOrder);

                    }
                }

            }
            if (lineItemOrderList.size() > 0) {
                ediOrder.getLineItemOrder().clear();
                ediOrder.setLineItemOrder(lineItemOrderList);
                ediOrderList.add(ediOrder);
                ediOrders.setOrders(ediOrderList);
                ediXML = transcationalRecordGenerator.toXml(ediOrders);
            }
            if (bibMarcRecordList.size() > 0) {
                bibliographicRecords.setRecords(bibMarcRecordList);
                bibXML = bibMarcRecordProcessor.generateXML(bibMarcRecordList);
            }


            if (!"".equals(bibXML)) {
                BufferedWriter bibOut = new BufferedWriter(new FileWriter(getDestinationPath() + acqLoadSumId + OLEConstants.BATCH_FAILURE_BIB_FILE_ETN));
                bibOut.write(bibXML);
                bibOut.close();
            }
            if (!"".equals(ediXML)) {
                BufferedWriter ediOut = new BufferedWriter(new FileWriter(getDestinationPath() + acqLoadSumId + OLEConstants.BATCH_FAILURE_EDI_FILE_ETN));
                ediOut.write(ediXML);
                ediOut.close();
            }
        } catch (Exception ex) {
            LOG.error("createBibAndEdiFile() method --  Exception : " + ex);
            throw new RuntimeException(ex);
        }

    }

    public void createBibFile(List<OleOrderRecord> failureOrderRecordList, int acqLoadSumId) {
        try {
            BibMarcRecordProcessor bibMarcRecordProcessor=new BibMarcRecordProcessor();
           // BibliographicRecordHandler bibliographicRecordHandler = new BibliographicRecordHandler();
            OLETranscationalRecordGenerator transcationalRecordGenerator = new OLETranscationalRecordGenerator();
            String bibXML = "";
            // String ediXML = "";
            List<BibMarcRecord> bibliographicRecordList = new ArrayList<BibMarcRecord>();
            List<EDIOrder> ediOrderList = new ArrayList<EDIOrder>();
            List<EDIOrder> oleTxRecordList = new ArrayList<EDIOrder>();
            Collection bibliographicRecords = new Collection();
            // EDIOrders ediOrders = new EDIOrders();
            OleOrderRecord oleOrderRecord = null;
            // EDIOrder ediOrder = null;
            String ybpNumber = "";
            List<LineItemOrder> lineItemOrderList = new ArrayList<LineItemOrder>();
            List<String> ybpNumberList = new ArrayList<String>();
            for (int i = 0; i < failureOrderRecordList.size(); i++) {
                String ediYbpNumber = "";
                oleOrderRecord = failureOrderRecordList.get(i);
                if (oleOrderRecord.getOriginalRecord() != null) {
                    bibliographicRecordList.add(oleOrderRecord.getOriginalRecord());
                }
                // ediOrder = oleOrderRecord.getOriginalEdi();
                if (oleOrderRecord.getOleTxRecord() != null) {
                    ybpNumber = oleOrderRecord.getOleTxRecord().getVendorItemIdentifier();
                    ybpNumberList.add(ybpNumber);
                }
            }

            if (bibliographicRecordList.size() > 0) {
                bibliographicRecords.setRecords(bibliographicRecordList);
                bibXML = bibMarcRecordProcessor.generateXML(bibliographicRecordList);
            }


            if (!"".equals(bibXML)) {
                BufferedWriter bibOut = new BufferedWriter(new FileWriter(getDestinationPath() + acqLoadSumId + OLEConstants.BATCH_FAILURE_BIB_FILE_ETN));
                bibOut.write(bibXML);
                bibOut.close();
            }
            /*if (!"".equals(ediXML)) {
                BufferedWriter ediOut = new BufferedWriter(new FileWriter(getDestinationPath() + acqLoadSumId + OLEConstants.BATCH_FAILURE_EDI_FILE_ETN));
                ediOut.write(ediXML);
                ediOut.close();
            }*/
        } catch (Exception ex) {
            LOG.error("createBibAndEdiFile() method --  Exception : " + ex);
            throw new RuntimeException(ex);
        }

    }

    protected AcquisitionBatchInputFileDocument createAcquisitionBatchUploadDocument() throws WorkflowException {
        String user;
        if (GlobalVariables.getUserSession() != null) {
            user = GlobalVariables.getUserSession().getPrincipalName();
        } else {
            user = getConfigurationService().getPropertyValueAsString(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
        }
        GlobalVariables.setUserSession(new UserSession(user));
        AcquisitionBatchInputFileDocument acqBatchDocument = (AcquisitionBatchInputFileDocument) SpringContext.getBean(DocumentService.class).getNewDocument(FinancialDocumentTypeCodes.ACQ_BATCH_UPLOAD);
        acqBatchDocument.getDocumentHeader().setDocumentDescription(OLEConstants.MANUAL_INGEST_DOCUMENT_DESCRIPTION + (StringUtils.isEmpty(getOperatorInitials()) ? "" : "_" + getOperatorInitials()) + "_" + getCurrentDate());
        SpringContext.getBean(DocumentService.class).saveDocument(acqBatchDocument);
        return acqBatchDocument;
    }


    protected String getCurrentDate() {
        LOG.debug("Inside getCurrentDate()");
        //Modified as per review comments OLE-24
        DateTimeService dateTimeService = SpringContext.getBean(DateTimeService.class);
        Date date = dateTimeService.getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String currentDate = sdf.format(date);
        LOG.debug("End of getCurrentDate()");
        return currentDate;
    }

    protected String getOperatorInitials() {
        LOG.debug("Inside getOperatorInitials()");
        StringBuffer operatorInitials = new StringBuffer();
        Person person = GlobalVariables.getUserSession().getPerson();
        operatorInitials.append(StringUtils.isEmpty(person.getFirstName()) ? "" : person.getFirstName().toLowerCase().charAt(0));
        operatorInitials.append(StringUtils.isEmpty(person.getMiddleName()) ? "" : person.getMiddleName().toLowerCase().charAt(0));
        operatorInitials.append(StringUtils.isEmpty(person.getLastName()) ? "" : person.getLastName().toLowerCase().charAt(0));
        LOG.debug("End of getOperatorInitials()");
        return operatorInitials.toString();
    }

    protected void saveBatchFailureRecords(List<OleOrderRecord> failureOrderRecordList, Integer acqLoadId) {

        for (int i = 0; i < failureOrderRecordList.size(); i++) {
            OleOrderRecord oleOrderRecord = failureOrderRecordList.get(i);
            String isValidRecord = ((oleOrderRecord.getMessageMap().get("isValidRecord"))).toString();
            if ("true".equals(isValidRecord)) {
                OleLoadFailureRecords oleLoadFailureRecords = new OleLoadFailureRecords();
                oleLoadFailureRecords.setAcqLoadSumId(acqLoadId);
                // changes
                String isValidBFN = ((oleOrderRecord.getMessageMap().get("isValidBFN"))).toString();
                String isBadControlField = ((oleOrderRecord.getMessageMap().get("isBadControlField"))).toString();
                String isApoRule = ((oleOrderRecord.getMessageMap().get("isApoRule"))).toString();
                if (isValidBFN.equals("false")) {
                    oleLoadFailureRecords.setErrorId(new BigDecimal("7"));
                } else if ("true".equals(isBadControlField)) {
                    oleLoadFailureRecords.setErrorId(new BigDecimal("8"));
                } else if ("true".equals(isApoRule)) {
                    oleLoadFailureRecords.setErrorId(new BigDecimal("9"));
                } else {
                    oleLoadFailureRecords.setErrorId(new BigDecimal("5"));
                }
                oleLoadFailureRecords.setVendorId("");
                setBibliographicInformation(oleLoadFailureRecords, oleOrderRecord.getOriginalRecord());
                SpringContext.getBean(BusinessObjectService.class).save(oleLoadFailureRecords);
            }
        }
    }

    protected void saveBatchSuccessRecord(OleLoadSumRecords oleLoadSumRecords, int dupRecords, int poSucRecords, int sucRecords, String fileName, String description, String batchProcessProfileId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("dupRecords =====================" + dupRecords);
            LOG.debug("sucRecords =====================" + sucRecords);
            LOG.debug("poSucRecords =====================" + poSucRecords);
        }
        oleLoadSumRecords.setAcqLoadDescription(description);
        oleLoadSumRecords.setFileName(fileName);
        oleLoadSumRecords.setBatchProcessProfileId(batchProcessProfileId);
        oleLoadSumRecords.setAcqLoadFailCount(dupRecords);
        oleLoadSumRecords.setAcqLoadSuccCount(sucRecords);
        oleLoadSumRecords.setAcqLoadTotCount(dupRecords + sucRecords);
        oleLoadSumRecords.setAcqLoadPoTotCount(poSucRecords);
        oleLoadSumRecords.setPrincipalId(GlobalVariables.getUserSession().getPrincipalName());
        Timestamp ts = SpringContext.getBean(DateTimeService.class).getCurrentTimestamp();
        oleLoadSumRecords.setLoadCreatedDate(ts);
        SpringContext.getBean(BusinessObjectService.class).save(oleLoadSumRecords);

    }

    protected void createReqIdTextFile(List reqList, Integer acqSumId) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(getDestinationPath() + acqSumId + OLEConstants.BATCH_REQ_ID_FILE));
            StringBuffer reqIds = new StringBuffer();
            for (int i = 0; i < reqList.size(); i++) {
                if (reqList.get(i) != null || "".equals(reqList.get(i))) {
                    reqIds.append(reqList.get(i).toString() + ",");
                }
            }
            out.write(reqIds.toString());
            out.close();
        } catch (Exception ex) {
        }

    }

    protected BigDecimal createAgendaProfile(String agendaName) {
        Map profileMap = new HashMap();
        profileMap.put("profile", agendaName);
        OleLoadProfile oleLoadProfile = null;
        int count = SpringContext.getBean(BusinessObjectService.class).countMatching(OleLoadProfile.class, profileMap);
        if (count == 0) {
            oleLoadProfile = new OleLoadProfile();
            oleLoadProfile.setProfile(agendaName);
            oleLoadProfile.setActive(true);
            SpringContext.getBean(BusinessObjectService.class).save(oleLoadProfile);
        } else {
            oleLoadProfile = (OleLoadProfile) ((List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleLoadProfile.class, profileMap)).get(0);
        }

        return oleLoadProfile.getProfileId();
    }

    public String getDestinationPath() {
        String destinationPath = getConfigurationService().getPropertyValueAsString(OLEConstants.STAGING_DIRECTORY_KEY) + getParameter(OleSelectPropertyConstants.STAFF_UPLOAD_DESTINATIONPATH);
        File dirCheck = (new File(destinationPath));
        boolean isDir = dirCheck.exists();
        if (LOG.isDebugEnabled()) {
            LOG.debug("dirCheck =====================" + dirCheck);
        }
        if (!isDir) {
            dirCheck.mkdir();
        }
        return destinationPath;
    }

    protected void setBibliographicInformation(OleLoadFailureRecords oleLoadFailureRecord, BibMarcRecord originalRecord) {
        BibMarcNamedFieldsBean bibliographicNamedFieldsBean = new BibMarcNamedFieldsBean();
        bibliographicNamedFieldsBean.setBibliographicRecord(originalRecord);
        oleLoadFailureRecord.setTitle(bibliographicNamedFieldsBean.getTitle());
        oleLoadFailureRecord.setIsbn(bibliographicNamedFieldsBean.getFieldNameFor("020", "a"));
    }

    public String getParameter(String name){
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

    public OleSelectDocumentService getOleSelectDocumentService() {
        if(oleSelectDocumentService == null){
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public void setOleSelectDocumentService(OleSelectDocumentService oleSelectDocumentService) {
        this.oleSelectDocumentService = oleSelectDocumentService;
    }

}
