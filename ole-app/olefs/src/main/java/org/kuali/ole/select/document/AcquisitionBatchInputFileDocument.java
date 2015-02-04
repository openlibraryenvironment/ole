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
package org.kuali.ole.select.document;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.select.batch.service.RequisitionLoadTransactionsService;
import org.kuali.ole.select.batch.service.impl.RequisitionLoadTransactionsServiceImpl;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.businessobject.OleLoadFailureRecords;
import org.kuali.ole.select.businessobject.OleLoadSumRecords;
import org.kuali.ole.select.service.BatchLoadService;
import org.kuali.ole.select.service.OleBatchIngestService;
import org.kuali.ole.select.service.impl.BatchLoadServiceImpl;
import org.kuali.ole.select.service.impl.OleBatchIngestServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.AcquisitionBatchUpload;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.FinancialSystemTransactionalDocumentBase;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.service.DocumentHeaderService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class AcquisitionBatchInputFileDocument extends FinancialSystemTransactionalDocumentBase implements Inactivatable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AcquisitionBatchInputFileDocument.class);
    private static final String dateTimeFormat = "MMddHHmm";
    private static final String dateFormat = "MMddyy";
    protected boolean active;
    protected BatchLoadService batchLoadService;
    protected OlePurapService olePurapService;

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean prepareForLoadSummary(InputStream fileContents, List<OleLoadFailureRecords> oleLoadFailureRecordsList, OleLoadSumRecords oleLoadSumRecords, FormFile uploadedFile, AcquisitionBatchUpload acquisitionBatchUpload) {

        LOG.debug("start --- prepareForLoadSummary method of AcquisitionBatchInputFileDocument");

        BatchLoadService batchLoadService = getBatchLoadService();
        ;
        List bibIsbnList = new ArrayList(0);
        List bibVendorPoNumberList = new ArrayList(0);
        List<BibInfoBean> reqFailureList = new ArrayList<BibInfoBean>(0);
        List<BibInfoBean> titleFailureList = new ArrayList<BibInfoBean>(0);
        List<BibInfoBean> isbnDupFailureList = new ArrayList<BibInfoBean>(0);
        List<BibInfoBean> vendorPoNumberDupFailureList = new ArrayList<BibInfoBean>(0);
        if (fileContents != null) {
            try {

                // get bibinfobean list values from xml
                InputStream fileContent = fileContents;
                OleBatchIngestService oleBatchIngestService = SpringContext.getBean(OleBatchIngestServiceImpl.class);
                String xml = oleBatchIngestService.transformRawDataToXml(fileContent);
                List<BibInfoBean> bibInfoBeanList = batchLoadService.getBibInfoBeanList(xml);
                if (bibInfoBeanList.size() <= 0) {
                    return false;
                }
                batchLoadService.foundAllDuplicateRecords(bibInfoBeanList, reqFailureList, titleFailureList, isbnDupFailureList, vendorPoNumberDupFailureList, bibIsbnList);
                List docIsbnList = batchLoadService.getDocIsbnList(bibIsbnList);
                int dupRecords = batchLoadService.getNoOfDupIsbnRecords(docIsbnList, bibIsbnList);
                List<BibInfoBean> isbnFailureList = batchLoadService.getIsbnFailureRecordsList(docIsbnList, bibInfoBeanList);
                isbnFailureList.addAll(isbnDupFailureList);
                List vendorPoNumberList = batchLoadService.getVendorPoNumberList(bibInfoBeanList);
                dupRecords += vendorPoNumberList.size();
                List<BibInfoBean> vendorPoNumberFailureList = batchLoadService.getVendorPoNumberFailureRecordsList(vendorPoNumberList, bibInfoBeanList);
                int sucRecords = bibInfoBeanList.size();
                List reqList = new ArrayList(0);
                RequisitionLoadTransactionsService requisitionLoadTransactionsService = SpringContext.getBean(RequisitionLoadTransactionsServiceImpl.class);
                reqList = requisitionLoadTransactionsService.saveRequisitionDocument(bibInfoBeanList);
                if (reqFailureList.size() != 0) {
                    reqFailureList.addAll(batchLoadService.getRequisitionFailureRecords(reqList, bibInfoBeanList));
                } else {
                    reqFailureList = batchLoadService.getRequisitionFailureRecords(reqList, bibInfoBeanList);
                }
                batchLoadService.getDupRecordsCount(reqFailureList, titleFailureList, isbnDupFailureList, vendorPoNumberDupFailureList);
                List<BibInfoBean> bibFailureRecordsList = batchLoadService.getBibFailureRecordsList(isbnFailureList, vendorPoNumberFailureList, reqFailureList, titleFailureList, vendorPoNumberDupFailureList);
                String failureRawData = oleBatchIngestService.getRawXml(uploadedFile.getInputStream(), bibFailureRecordsList);
                String destinationPath = batchLoadService.getDestinationPath();

                // saving the successful records
                batchLoadService.saveSuccessRecord(oleLoadSumRecords, dupRecords, bibInfoBeanList.size(), sucRecords, acquisitionBatchUpload.getBatchDescription(), new BigDecimal(acquisitionBatchUpload.getBatchLoadProfile()), uploadedFile.getFileName());

                if (bibFailureRecordsList.size() > 0) {
                    batchLoadService.createErrorMrkFile(failureRawData, oleLoadSumRecords.getAcqLoadSumId());
                }
                if (reqList.size() > 0) {
                    batchLoadService.createReqIdTextFile(reqList, oleLoadSumRecords.getAcqLoadSumId());
                }
                batchLoadService.saveAllFailureRecords(isbnFailureList, vendorPoNumberFailureList, reqFailureList, titleFailureList, vendorPoNumberDupFailureList, oleLoadSumRecords.getAcqLoadSumId());
                // oleLoadFailureRecordsList.addAll(getLoadFailureRecordsList(oleLoadSumRecords.getAcqLoadSumId(),batchLoadService));

                oleLoadSumRecords = getLoadSummaryAndFailureRecords(oleLoadSumRecords.getAcqLoadSumId(), oleLoadFailureRecordsList);
                olePurapService = getOlePurapService();
                String operatorInitials = olePurapService.getOperatorInitials();
                this.getDocumentHeader().setDocumentDescription(OLEConstants.MANUAL_INGEST_DOCUMENT_DESCRIPTION + (StringUtils.isEmpty(operatorInitials) ? "" : "_" + operatorInitials) + "_" + getCurrentDate());
            } catch (Exception ex) {
                LOG.error("Exception while loading summary " + ex);
                throw new RuntimeException(ex);
            }

        }

        LOG.debug("end --- prepareForLoadSummary method of AcquisitionBatchInputFileDocument");

        return true;
    }


    public OleLoadSumRecords getLoadSummaryAndFailureRecords(int acqLoadSumId, List<OleLoadFailureRecords> oleLoadFailureRecordsList) {
        OleLoadSumRecords oleLoadSumRecords = null;
        try {

            LOG.debug("start --- getLoadSummaryAndFailureRecords method of AcquisitionBatchInputFileDocument");

            BatchLoadService batchLoadService = getBatchLoadService();
            ;
            Map loadRecordsMap = new HashMap();
            loadRecordsMap.put("acqLoadSumId", acqLoadSumId);
            oleLoadSumRecords = batchLoadService.getOleLoadSumRecords(loadRecordsMap);
            oleLoadFailureRecordsList.addAll(getLoadFailureRecordsList(acqLoadSumId, batchLoadService));

            LOG.debug("end --- getLoadSummaryAndFailureRecords method of AcquisitionBatchInputFileDocument");


        } catch (Exception ex) {
            LOG.error("Exception while loading summary & failure records" + ex);
            throw new RuntimeException(ex);
        }
        return oleLoadSumRecords;
    }

    public OleLoadSumRecords getLoadSummaryAndFailureRecordsByLoadSummaryId(int acqLoadSumId, List<OleLoadFailureRecords> oleLoadFailureRecordsList) {
        OleLoadSumRecords oleLoadSumRecords = null;
        try {
            oleLoadSumRecords = getLoadSummaryAndFailureRecords(acqLoadSumId, oleLoadFailureRecordsList);
            setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(oleLoadSumRecords.getDocumentNumber()));
            setDocumentNumber(oleLoadSumRecords.getDocumentNumber());
            Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
            WorkflowDocument doc;
            getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(getDocumentNumber(), principalPerson));
            int totalPOCount = oleLoadSumRecords.getPoToalCount(oleLoadSumRecords.getAcqLoadPoTotCount());
            if (totalPOCount > 0) {
                oleLoadSumRecords.setAcqLoadPoTotCount(totalPOCount);
            }
        } catch (Exception ex) {
            LOG.error("Exception while loading summary & failure records by load summary id ", ex);
            throw new RuntimeException(ex);
        }
        return oleLoadSumRecords;
    }

    public OleLoadSumRecords getLoadSummaryAndFailureRecordsByDocId(String docId, List<OleLoadFailureRecords> oleLoadFailureRecordsList) {
        OleLoadSumRecords oleLoadSumRecords = null;
        try {

            LOG.debug("start --- getLoadSummaryAndFailureRecords method of AcquisitionBatchInputFileDocument");
            BatchLoadService batchLoadService = getBatchLoadService();
            ;
            /*Map loadRecordsMap = new HashMap();
            loadRecordsMap.put("documentNumber", docId);
            oleLoadSumRecords = batchLoadService.getOleLoadSumRecords(loadRecordsMap);*/
            oleLoadSumRecords = getOleLoadSumRecords(docId, batchLoadService);
            oleLoadFailureRecordsList.addAll(getLoadFailureRecordsList(oleLoadSumRecords.getAcqLoadSumId(), batchLoadService));
            LOG.debug("end --- getLoadSummaryAndFailureRecords method of AcquisitionBatchInputFileDocument");

            int totalPOCount = oleLoadSumRecords.getPoToalCount(oleLoadSumRecords.getAcqLoadPoTotCount());
            if (totalPOCount >= -1) {
                //DataCarrierService dataCarrierService = GlobalResourceLoader.getService(org.kuali.ole.OLEConstants.DATA_CARRIER_SERVICE);
                //OLEBatchProcessProfileBo oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) dataCarrierService.getData(org.kuali.ole.OLEConstants.BATCH_PROFILE_BO);
                OLEBatchProcessProfileBo oleBatchProcessProfileBo = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OLEBatchProcessProfileBo.class, oleLoadSumRecords.getBatchProcessProfileId());
                if(oleBatchProcessProfileBo != null && oleBatchProcessProfileBo.getRequisitionsforTitle().equalsIgnoreCase("One Requisition With All Titles"))  {
                    oleLoadSumRecords.setAcqLoadTotCount(1);
                    if(totalPOCount == 0){
                        oleLoadSumRecords.setAcqLoadTotBibCount(0);
                        oleLoadSumRecords.setAcqLoadFailCount(1);
                        oleLoadSumRecords.setAcqLoadSuccCount(0);
                    }
                    else if(totalPOCount == 1){
                        oleLoadSumRecords.setAcqLoadSuccCount(1);
                        oleLoadSumRecords.setAcqLoadFailCount(0);
                    }
                }
                oleLoadSumRecords.setAcqLoadPoTotCount(totalPOCount);
            }
        } catch (Exception ex) {
            LOG.error("Exception while loading summary & failure records by docid " + ex);
            throw new RuntimeException(ex);
        }
        return oleLoadSumRecords;
    }

    public List<OleLoadFailureRecords> getLoadFailureRecordsList(int acqLoadSumId, BatchLoadService batchLoadService) {
        Map loadRecordsMap = new HashMap();
        loadRecordsMap.put("acqLoadSumId", acqLoadSumId);
        return batchLoadService.getOleFailureRecordsList(loadRecordsMap);
    }

    /**
     * Returns current Date in MMddyy(100311) format to be
     * appended in the document description
     *
     * @return current DateTime in String
     */
    public String getCurrentDate() {
        LOG.debug("Inside getCurrentDate()");
        //Modified as per review comments OLE-24
        DateTimeService dateTimeService = SpringContext.getBean(DateTimeService.class);
        Date date = dateTimeService.getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String currentDate = sdf.format(date);
        LOG.debug("End of getCurrentDate()");
        return currentDate;
    }

    public boolean isFailureRecords() {
        String docId = this.getDocumentNumber();
        List<OleLoadFailureRecords> oleLoadFailureRecordsList = new ArrayList<OleLoadFailureRecords>(0);
        BatchLoadService batchLoadService = getBatchLoadService();
        ;
        OleLoadSumRecords oleLoadSumRecords = getOleLoadSumRecords(docId, batchLoadService);
        oleLoadFailureRecordsList = getLoadFailureRecordsList(oleLoadSumRecords.getAcqLoadSumId(), batchLoadService);
        if (oleLoadFailureRecordsList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public OleLoadSumRecords getOleLoadSumRecords(String docId, BatchLoadService batchLoadService) {

        Map loadRecordsMap = new HashMap();
        loadRecordsMap.put("documentNumber", docId);
        OleLoadSumRecords oleLoadSumRecords = batchLoadService.getOleLoadSumRecords(loadRecordsMap);
        return oleLoadSumRecords;
    }


    public BatchLoadService getBatchLoadService() {
        if (batchLoadService == null) {
            batchLoadService = SpringContext.getBean(BatchLoadServiceImpl.class);
        }
        return batchLoadService;
    }

    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(OLEConstants.IS_FAILURE_RECORDS)) {
            return isFailureRecords();
        }
        return super.answerSplitNodeQuestion(nodeName);
    }

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

}
