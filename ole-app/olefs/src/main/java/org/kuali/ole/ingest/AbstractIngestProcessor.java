package org.kuali.ole.ingest;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.*;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.ingest.BatchProcessBibImport;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.ingest.pojo.IngestRecord;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.edi.*;
import org.kuali.ole.service.OleOrderRecordService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.engine.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * AbstractIngestProcessor which does the pre-processing for Staff Upload process to create Requisition.
 */
public abstract class AbstractIngestProcessor {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AbstractIngestProcessor.class);
    private static final String NAMESPACE_CODE_SELECTOR = "namespaceCode";
    private static final String NAME_SELECTOR = "name";
    private BusinessObjectService businessObjectService;
    private OleOrderRecordService oleOrderRecordService;
    private List<EngineResults> engineResults = new ArrayList<EngineResults>();
    private String user;
    String marcXMLContent;
    String ediXMLContent;
    private IngestRecord ingestRecord;
    private DocstoreClientLocator docstoreClientLocator;
    private List<OleOrderRecord> oleOrderRecordList = new ArrayList<OleOrderRecord>();
    //private DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);

    public List<OleOrderRecord> getOleOrderRecordList() {
        return oleOrderRecordList;
    }

    public void setOleOrderRecordList(List<OleOrderRecord> oleOrderRecordList) {
        this.oleOrderRecordList = oleOrderRecordList;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     *  Gets the engineResults attribute.
     * @return engineResults
     */

    public List<EngineResults> getEngineResults() {
        return engineResults;
    }


    /**
     *   This method takes the initial request when Ingesting a record.
     * @param ingestRecord
     * @param processDef
     * @param job
     */

    public int start(IngestRecord ingestRecord, boolean failure_flag, OLEBatchProcessDefinitionDocument processDef, OLEBatchProcessJobDetailsBo job) {
        this.ingestRecord = ingestRecord;
        this.setUser(ingestRecord.getUser());
        if(ingestRecord.getEdiFileContent() != null) {
            byPassLogicForPreProcess(ingestRecord.getMarcFileContent(), ingestRecord.getEdiFileContent(), ingestRecord.getByPassPreProcessing(), job);
        }
        else {
            byPassLogicForPreProcess(ingestRecord.getMarcFileContent(), ingestRecord.getByPassPreProcessing(), job);
        }
        try {
            if(ingestRecord.getEdiFileContent() != null){
                marcEdiprocess(processDef, job);
            }
            else {
                marcProcess(processDef, job);
            }
            ingestRecord.setUpdate(this.ingestRecord.isUpdate());
        } catch (URISyntaxException e) {
            LOG.info(e.getMessage());
        } catch (IOException e) {
            LOG.info(e.getMessage());
        } catch (SAXException e) {
            LOG.info(e.getMessage());
        } catch (XPathExpressionException e) {
            LOG.info(e.getMessage());
        } catch (ParserConfigurationException e) {
            LOG.info(e.getMessage());
        } catch (InterruptedException e) {
            LOG.info(e.getMessage());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        postProcess(job);
        int recordCount=getOleOrderRecordList().size();
        return recordCount;
    }

    /**
     * This method forwards to preProcess method if preProcessReq is true else sets the marcXmlContent and ediXmlContent.
     * @param rawMarcContent
     * @param rawEdiContent
     * @param preProcessingReq
     */
    private void byPassLogicForPreProcess(String rawMarcContent, String rawEdiContent, boolean preProcessingReq, OLEBatchProcessJobDetailsBo job) {
        if (preProcessingReq) {
            preProcess(rawMarcContent, rawEdiContent, job);
        } else {
            marcXMLContent = rawMarcContent;
            ediXMLContent = rawEdiContent;
        }
    }


    public void byPassLogicForPreProcess(String rawMarcContent, boolean preProcessingReq, OLEBatchProcessJobDetailsBo job) {
        if (preProcessingReq) {
            preProcess(rawMarcContent, job);
        } else {
            marcXMLContent = rawMarcContent;
        }
    }


    private void preProcess(String rawMarcContent, OLEBatchProcessJobDetailsBo job) {
        marcXMLContent = preProcessMarc(rawMarcContent, job);
    }
    /**
     *   This method forwards to preProcessMarc and preProcessEDI method.
     * @param rawMarcContent
     * @param rawEdiContent
     */
    private void preProcess(String rawMarcContent, String rawEdiContent , OLEBatchProcessJobDetailsBo job) {
        marcXMLContent = preProcessMarc(rawMarcContent, job);
        ediXMLContent = preProcessEDI(rawEdiContent);
    }


    /**
     * This method is for building Order Record values based on ingested marc file/data mapping or constant and default values from profile.
     * @throws Exception
     * @param processDef
     * @param job
     */
    public boolean marcProcess(OLEBatchProcessDefinitionDocument processDef, OLEBatchProcessJobDetailsBo job) throws Exception {
        LOG.info("----Inside marcProcess()------------------------------");
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = processDef.getOleBatchProcessProfileBo();
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        orderImportHelperBo.setOrderImportSuccessCount(0);
        orderImportHelperBo.setOrderImportFailureCount(0);
        orderImportHelperBo.setOleBatchProcessProfileBo(oleBatchProcessProfileBo);
        orderImportHelperBo.setFailureReason(new ArrayList<String>());
        oleOrderRecordService = getOleOrderRecordService();
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(marcXMLContent);
        List<BibMarcRecord> records = bibMarcRecords.getRecords();
        OLEBatchProcessProfileBo oleBatchProcessProfileBoForBibImport = getBibImportProfile(oleBatchProcessProfileBo);
        BatchProcessBibImport batchProcessBibImport = new BatchProcessBibImport(processDef, job);
        batchProcessBibImport.setOleBatchProcessProfileBo(oleBatchProcessProfileBoForBibImport);
        int bibMarcRecordsCount = records.size();
        int noOfProcessedOrderRecords = 0;
        List<OleTxRecord> oleTxRecords = oleOrderRecordService.getQuantityItemPartsLocation(records, job);
        if (bibMarcRecordsCount > 0) {
            noOfProcessedOrderRecords = bibMarcRecordsCount - oleTxRecords.size();
        }
        OLEBatchBibImportDataObjects oleBatchBibImportDataObjects = new OLEBatchBibImportDataObjects();
        List<OrderBibMarcRecord> orderBibMarcRecordList = oleBatchBibImportDataObjects.processBibImport(records,batchProcessBibImport);
        if (orderBibMarcRecordList != null && orderBibMarcRecordList.size() > 0) {
            for (int recordCount = 0; recordCount < orderBibMarcRecordList.size(); recordCount++) {
                OrderBibMarcRecord orderBibMarcRecord = orderBibMarcRecordList.get(recordCount);
                if (orderBibMarcRecord != null && orderBibMarcRecord.getBibId() != null && orderBibMarcRecord.getBibId().getId() != null) {
                    String bibId = orderBibMarcRecord.getBibId().getId();
                    OleOrderRecord oleOrderRecord = oleOrderRecordService.fetchOleOrderRecordForMarc(bibId, orderBibMarcRecord.getBibMarcRecord(), recordCount, job);
                    try{
                        setBibValues(oleOrderRecord, orderBibMarcRecord.getBibId());
                        if (oleBatchProcessProfileBoForBibImport.getDataToImport() != null) {
                            if (oleBatchProcessProfileBoForBibImport.getDataToImport().equals(OLEConstants.BIB_DATA_ONLY)
                                    || oleBatchProcessProfileBoForBibImport.getDataToImport().equals(OLEConstants.BIB_INS)) {
                                oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT);
                            } else if (oleBatchProcessProfileBoForBibImport.getDataToImport().equals(OLEConstants.BIB_EINS)) {
                                oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC);
                            } else if (oleBatchProcessProfileBoForBibImport.getDataToImport().equals(OLEConstants.BIB_INS_EINS)) {
                                oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT_ELECTRONIC);
                            }
                        }
                        //getValueForVendorProfileCodeFromProfile(getOleBatchProcessProfileBo());
                        orderImportHelperBo = job.getOrderImportHelperBo();
                        orderImportHelperBo.setBibMarcRecord(orderBibMarcRecord.getBibMarcRecord());
                        orderImportHelperBo.setOleBatchProcessProfileBo(oleBatchProcessProfileBo);
                        orderImportHelperBo.setEdiXMLContent(this.ediXMLContent);
                        orderImportHelperBo.setAgendaName(ingestRecord.getAgendaName());
                        if (oleOrderRecord == null) {
                            ingestRecord.setUpdate(true);
                            oleOrderRecord = new OleOrderRecord();
                            oleOrderRecord.setOriginalRecord(orderBibMarcRecord.getBibMarcRecord());
                            OleTxRecord oleTxRecord = new OleTxRecord();
                            oleOrderRecord.setOleTxRecord(oleTxRecord);
                        }
                        oleOrderRecord.setAgendaName(ingestRecord.getAgendaName());
                        oleOrderRecord.setOleOriginalBibRecordFileName(ingestRecord.getOriginalMarcFileName());
                        oleOrderRecord.setOriginalEDIFileName(ingestRecord.getOriginalEdiFileName());
                        oleOrderRecord.setDescription(ingestRecord.getAgendaDescription());
                        boolean validBFNFlag = (Boolean) (oleOrderRecord.getMessageMap().get(OLEConstants.IS_VALID_BFN) == null ? true : oleOrderRecord.getMessageMap().get(OLEConstants.IS_VALID_BFN));
                        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_RECORD, true);
                        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_BFN, validBFNFlag);
                        oleOrderRecordList.add(oleOrderRecord);
                    }
                    catch (Exception e){
                        setOleOrderRecordAsFailureRecord(job);
                        createErrorLog(job, orderBibMarcRecord.getBibMarcRecord(),OLEConstants.BIB_IMPORT_FAILURE_REASON);
                    }
                } else if (orderBibMarcRecord != null && orderBibMarcRecord.getFailureReason() != null && orderBibMarcRecord.getBibMarcRecord() != null) {
                    setOleOrderRecordAsFailureRecord(job);
                    createErrorLog(job, orderBibMarcRecord.getBibMarcRecord(), orderBibMarcRecord.getFailureReason());
                } else {
                    setOleOrderRecordAsFailureRecord(job);
                    createErrorLog(job, orderBibMarcRecord.getBibMarcRecord());
                }
            }
            orderImportHelperBo.setCreateBibCount(orderBibMarcRecordList.get(orderBibMarcRecordList.size()-1).getCreateBibCount());
            orderImportHelperBo.setUpdateBibCount(orderBibMarcRecordList.get(orderBibMarcRecordList.size() - 1).getUpdateBibCount());
            orderImportHelperBo.setCreateHoldingsCount(orderBibMarcRecordList.get(orderBibMarcRecordList.size() - 1).getCreateHoldingsCount());
            orderImportHelperBo.setUpdateHoldingsCount(orderBibMarcRecordList.get(orderBibMarcRecordList.size()-1).getUpdateHoldingsCount());
        }
        else {
            for (int count = 0; count < records.size(); count++) {
                createErrorLog(job, records.get(count));
                setOleOrderRecordAsFailureRecord(job);
            }
        }
        if (noOfProcessedOrderRecords > 0) {
            for (int i = 0; i < noOfProcessedOrderRecords; i++) {
                setOleOrderRecordAsFailureRecord(job);
            }
        }
        job.setUpdateBibCount(0);
        job.setCreateHoldingsCount(0);
        LOG.info("----End of marcProcess()------------------------------");
        return true;
    }

    private OleOrderRecordService getOleOrderRecordService() {
        if (null == oleOrderRecordService) {
            oleOrderRecordService = SpringContext.getBean(OleOrderRecordService.class);
        }
        return oleOrderRecordService;
    }

    public void setOleOrderRecordService(OleOrderRecordService oleOrderRecordService) {
        this.oleOrderRecordService = oleOrderRecordService;
    }

    private void setOleOrderRecordAsFailureRecord(OLEBatchProcessJobDetailsBo job) {
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        OleOrderRecord oleOrderRecord = new OleOrderRecord();
        //Below fields are set to false when the record is not process to due invalid location/quantity/parts
        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_RECORD, false);
        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_BFN, false);
        oleOrderRecord.addMessageToMap(OLEConstants.IS_APO_RULE, false);
        //dataCarrierService.addData(OLEConstants.ORDER_IMPORT_FAILURE_COUNT, (int) dataCarrierService.getData(OLEConstants.ORDER_IMPORT_FAILURE_COUNT) + 1);
        orderImportHelperBo.setOrderImportFailureCount(orderImportHelperBo.getOrderImportFailureCount() + 1);
        oleOrderRecordList.add(oleOrderRecord);
    }

    /**
     * This method returns Bib Import Profile based on which we can create the bib.
     */
    private OLEBatchProcessProfileBo getBibImportProfile(OLEBatchProcessProfileBo oleBatchProcessProfileBo){
        String bibImportProfileForOrderRecord = oleBatchProcessProfileBo.getBibImportProfileForOrderRecord();
        org.kuali.rice.krad.service.BusinessObjectService
                businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        Map<String,String> bibImportProfileMap = new HashMap<>();
        bibImportProfileMap.put(OLEConstants.BATCH_PROFILE_NM,bibImportProfileForOrderRecord);
        List<OLEBatchProcessProfileBo> oleBatchProcessProfileBoList = (List) businessObjectService.findMatching(OLEBatchProcessProfileBo.class, bibImportProfileMap);
        if(oleBatchProcessProfileBoList != null && oleBatchProcessProfileBoList.size() > 0){
            return oleBatchProcessProfileBoList.get(0);
        }
        return null;
    }

    /**
     * This method populate the value for vendor profile code from profile.
     * @param oleBatchProcessProfileBo
     * @return Vendor Profile Code.
     */
    private String getValueForVendorProfileCodeFromProfile(OLEBatchProcessProfileBo oleBatchProcessProfileBo){
        LOG.info("----Inside getValueForVendorProfileCodeFromProfile()------------------------------");
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileMappingOptionsList();
        for (OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo : oleBatchProcessProfileMappingOptionsBoList) {
            List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
            for (int dataMapCount = 0;dataMapCount<oleBatchProcessProfileDataMappingOptionsBoList.size();dataMapCount++) {
                String sourceField = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getSourceField();
                String sourceFields[] = sourceField.split("\\$");
                if (sourceFields.length == 2) {
                    if (OLEConstants.OLEBatchProcess.VENDOR_PROFILE_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                        return oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationFieldValue();
                    }
                }
            }
        }
        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList();
        for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoList) {
            if (StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getDataType()) && OLEConstants.OLEBatchProcess.ORDER_IMPORT.equalsIgnoreCase(oleBatchProcessProfileConstantsBo.getDataType())
                    && StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getAttributeValue()) && StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                if (OLEConstants.OLEBatchProcess.CONSTANT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {
                    if (OLEConstants.OLEBatchProcess.VENDOR_PROFILE_CODE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        return oleBatchProcessProfileConstantsBo.getAttributeValue();
                    }
                }
                else if (OLEConstants.OLEBatchProcess.DEFAULT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {
                    if (OLEConstants.OLEBatchProcess.VENDOR_PROFILE_CODE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        return oleBatchProcessProfileConstantsBo.getAttributeValue();
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method returns isbn value from the ingested file.
     * @param lineItemOrder
     */
    private String getISBN(LineItemOrder lineItemOrder){
        if(lineItemOrder.getProductFunction()!= null && lineItemOrder.getProductFunction().size() > 0){
            if(lineItemOrder.getProductFunction().get(0).getProductArticleNumber() != null && lineItemOrder.getProductFunction().get(0).getProductArticleNumber().size() > 0){
                if(lineItemOrder.getProductFunction().get(0).getProductArticleNumber().get(0).getProductIsbn() != null){
                    return lineItemOrder.getProductFunction().get(0).getProductArticleNumber().get(0).getProductIsbn();
                }
            }
        }
        return null;
    }

    /**
     * This method takes the value of isbn and returns corresponding bib id value.
     * @param isbn
     * @return
     * @throws Exception
     */
    private String getBibId(String isbn) throws Exception {
        ISBNUtil isbnUtil = new ISBNUtil();
        isbn = isbnUtil.normalizeISBN(isbn);
        SearchParams searchParams = new SearchParams();
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(), Bib.ISBN, isbn), "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), "ID"));
        SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        if (searchResponse.getSearchResults().size() > 0) {
                SearchResult searchResult = searchResponse.getSearchResults().get(searchResponse.getSearchResults().size()-1);
                for(SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if(searchResultField.getFieldName().equalsIgnoreCase("ID") ) {
                        return searchResultField.getFieldValue();
                    }
                }
        }
        return null;
    }

    /**
     * This method sets the value for bib details.
     * @param oleOrderRecord
     * @param bibId
     * @throws Exception
     */
    private void setBibValues(OleOrderRecord oleOrderRecord,String bibId) throws Exception{
        oleOrderRecord.setBibId(bibId);
        OleBibRecord oleBibRecord = new OleBibRecord();
        oleBibRecord.setBibUUID(bibId);
        oleBibRecord.setBib(getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibId));
        oleOrderRecord.setOleBibRecord(oleBibRecord);
    }

    /**
     * This method sets the value for bib details.
     * @param oleOrderRecord
     * @param bibTree
     * @throws Exception
     */
    private void setBibValues(OleOrderRecord oleOrderRecord, BibId bibTree) throws Exception {
        oleOrderRecord.setBibTree(bibTree);
        oleOrderRecord.setBibId(bibTree.getId());
        OleBibRecord oleBibRecord = new OleBibRecord();
        oleBibRecord.setBibUUID(bibTree.getId());
        oleBibRecord.setBib(getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibTree.getId()));
        oleOrderRecord.setOleBibRecord(oleBibRecord);
    }

    /**
     * This method is for building Order Record values based on ingested marc file or Edi files/data mapping or constant and default values from profile.
     * @throws Exception
     */
    public boolean marcEdiprocess(OLEBatchProcessDefinitionDocument processDef, OLEBatchProcessJobDetailsBo job) throws Exception {
        LOG.info("----Inside marcEdiProcess()------------------------------");
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = processDef.getOleBatchProcessProfileBo();
        OLETranscationalRecordGenerator oleTranscationalRecordGenerator = new OLETranscationalRecordGenerator();
        EDIOrders ediOrders = oleTranscationalRecordGenerator.fromXml(ediXMLContent);
        EDIOrder ediOrder = ediOrders.getOrders().get(0);
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(marcXMLContent);
        List<BibMarcRecord> records = bibMarcRecords.getRecords();
        OLEBatchProcessProfileBo oleBatchProcessProfileBoForBibImport = getBibImportProfile(oleBatchProcessProfileBo);
        BatchProcessBibImport batchProcessBibImport = new BatchProcessBibImport(processDef, job);
        batchProcessBibImport.setOleBatchProcessProfileBo(oleBatchProcessProfileBoForBibImport);
        //DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        //dataCarrierService.addData(OLEConstants.FAILURE_REASON, new ArrayList<>());
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        orderImportHelperBo.setFailureReason(new ArrayList<String>());
        /*dataCarrierService.addData(OLEConstants.ORDER_IMPORT_SUCCESS_COUNT, 0);
        dataCarrierService.addData(OLEConstants.ORDER_IMPORT_FAILURE_COUNT,0);*/
        orderImportHelperBo.setOrderImportSuccessCount(0);
        orderImportHelperBo.setOrderImportFailureCount(0);
        orderImportHelperBo.setOleBatchProcessProfileBo(oleBatchProcessProfileBo);
        OLEBatchBibImportDataObjects oleBatchBibImportDataObjects = new OLEBatchBibImportDataObjects();
        List<OrderBibMarcRecord> orderBibMarcRecordList = oleBatchBibImportDataObjects.processBibImport(records, batchProcessBibImport);
        if (orderBibMarcRecordList != null && orderBibMarcRecordList.size() > 0) {
            for (int recordCount = 0; recordCount < orderBibMarcRecordList.size(); recordCount++) {
                OrderBibMarcRecord orderBibMarcRecord = orderBibMarcRecordList.get(recordCount);
                if (orderBibMarcRecord != null && orderBibMarcRecord.getBibId() != null && orderBibMarcRecord.getBibId().getId() != null) {
                    String bibId = orderBibMarcRecord.getBibId().getId();
                    oleOrderRecordService = getOleOrderRecordService();
                    OleOrderRecord oleOrderRecord = oleOrderRecordService.fetchOleOrderRecordForMarcEdi(bibId, ediOrder, records.get(recordCount), recordCount, job);
                    oleOrderRecord.setAgendaName(OLEConstants.PROFILE_AGENDA_NM);
                    if (oleOrderRecord.getMessageMap().get(OLEConstants.IS_VALID_BFN).toString().equalsIgnoreCase(OLEConstants.TRUE)) {
                        try {
                            setBibValues(oleOrderRecord, orderBibMarcRecord.getBibId());
                            if (oleBatchProcessProfileBoForBibImport.getDataToImport() != null) {
                                if (oleBatchProcessProfileBoForBibImport.getDataToImport().equals(OLEConstants.BIB_DATA_ONLY)
                                        || oleBatchProcessProfileBoForBibImport.getDataToImport().equals(OLEConstants.BIB_INS)) {
                                    oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI);
                                } else if (oleBatchProcessProfileBoForBibImport.getDataToImport().equals(OLEConstants.BIB_EINS)) {
                                    oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI_ELECTRONIC);
                                } else if (oleBatchProcessProfileBoForBibImport.getDataToImport().equals(OLEConstants.BIB_INS_EINS)) {
                                    oleOrderRecord.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI_PRINT_ELECTRONIC);
                                }
                            }
                            populateOrderRecordForValidBFN(job, oleOrderRecord, ediOrder, records, recordCount, oleBatchProcessProfileBo);
                            oleOrderRecordList.add(oleOrderRecord);
                        } catch (Exception e) {
                            setOleOrderRecordAsFailureRecord(job);
                            createErrorLog(job, orderBibMarcRecord.getBibMarcRecord(), OLEConstants.BIB_IMPORT_FAILURE_REASON);
                        }
                    } else {
                        oleOrderRecord.getMessageMap().put(OLEConstants.IS_VALID_RECORD, true);
                        oleOrderRecord.getMessageMap().put(OLEConstants.IS_BAD_CTRL_FLD, false);
                        oleOrderRecord.setOleOriginalBibRecordFileName(ingestRecord.getOriginalMarcFileName());
                        oleOrderRecord.setOriginalEDIFileName(ingestRecord.getOriginalEdiFileName());
                        createErrorLog(job, orderBibMarcRecord.getBibMarcRecord(), "Invalid BFN");
                        orderImportHelperBo.setOrderImportFailureCount(orderImportHelperBo.getOrderImportFailureCount() + 1);
                        oleOrderRecordList.add(oleOrderRecord);
                    }
                } else if (orderBibMarcRecord != null && orderBibMarcRecord.getFailureReason() != null && orderBibMarcRecord.getBibMarcRecord() != null) {
                    setOleOrderRecordAsFailureRecord(job);
                    createErrorLog(job, orderBibMarcRecord.getBibMarcRecord(), orderBibMarcRecord.getFailureReason());
                } else {
                    setOleOrderRecordAsFailureRecord(job);
                    createErrorLog(job, orderBibMarcRecord.getBibMarcRecord());
                }
            }
        } else {
            for (int count = 0; count < records.size(); count++) {
                createErrorLog(job, records.get(count));
                setOleOrderRecordAsFailureRecord(job);
            }
        }
        /*dataCarrierService.addData(OLEConstants.UPDATE_BIB_CNT, 0);
        dataCarrierService.addData(OLEConstants.CREATE_HLD_CNT, 0);*/
        orderImportHelperBo.setUpdateBibCount(0);
        orderImportHelperBo.setCreateHoldingsCount(0);
        LOG.info("----End of marcEdiProcess()------------------------------");
        return true;
    }

    /**
     * This method is for populating order record values for valid BFN value.
     * @param job
     * @param oleOrderRecord
     * @param ediOrder
     * @param records
     * @param marcCount
     */
    private void populateOrderRecordForValidBFN(OLEBatchProcessJobDetailsBo job,OleOrderRecord oleOrderRecord,EDIOrder ediOrder,List<BibMarcRecord> records,int marcCount, OLEBatchProcessProfileBo oleBatchProcessProfileBo){
        LOG.info("----Inside populateOrderRecordForValidBFN()------------------------------");
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        //getValueForVendorProfileCodeFromProfile(getOleBatchProcessProfileBo());
        LineItemOrderMatcherForBib lineItemOrderMatcherForBib = new LineItemOrderMatcherForBib();
        String vendorProfileCode = OLEConstants.PROFILE_AGENDA_NM;
        LineItemOrder lineItemOrder = lineItemOrderMatcherForBib.getLineItemOrder(ediOrder.getLineItemOrder(),records.get(marcCount), vendorProfileCode);
        /*dataCarrierService.addData(OLEConstants.REQUEST_BIB_RECORD, records.get(marcCount));
        dataCarrierService.addData(OLEConstants.REQUEST_LINE_ITEM_ORDER_RECORD, lineItemOrder);
        dataCarrierService.addData(OLEConstants.EDI_ORDER, ediOrder);
        dataCarrierService.addData(OLEConstants.EDI_XML_CONTENT, this.ediXMLContent);
        dataCarrierService.addData(OLEConstants.BATCH_PROFILE_BO, getOleBatchProcessProfileBo());*/
        orderImportHelperBo.setBibMarcRecord(records.get(marcCount));
        orderImportHelperBo.setLineItemOrder(lineItemOrder);
        orderImportHelperBo.setEdiOrder(ediOrder);
        orderImportHelperBo.setEdiXMLContent(this.ediXMLContent);
        orderImportHelperBo.setOleBatchProcessProfileBo(oleBatchProcessProfileBo);
        if(oleOrderRecord==null){
            ingestRecord.setUpdate(true);
            oleOrderRecord = new OleOrderRecord();
            oleOrderRecord.setOriginalRecord(records.get(marcCount));
            oleOrderRecord.setOriginalEdi(ediOrder);
            OleTxRecord oleTxRecord = new OleTxRecord();
            oleTxRecord.setVendorItemIdentifier(OleTxRecordBuilder.getInstance().getVendorItemIdentifier(lineItemOrder));
            oleOrderRecord.setOleTxRecord(oleTxRecord);
        }
        oleOrderRecord.setOleOriginalBibRecordFileName(ingestRecord.getOriginalMarcFileName());
        oleOrderRecord.setOriginalEDIFileName(ingestRecord.getOriginalEdiFileName());
        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_RECORD,true);
    }

    /**
     * This method is for populating order record values for Extra Marc.
     * @param records
     * @param marcCount
     * @param ediOrder
     * @return OleOrderRecord values
     */
    private OleOrderRecord populateOrderRecordForExtraMarc(List<BibMarcRecord> records,int marcCount,EDIOrder ediOrder){
        LOG.info("----Inside populateOrderRecordForExtraMarc()------------------------------");
        OleOrderRecord oleOrderRecord=new OleOrderRecord();
        oleOrderRecord.setAgendaName(ingestRecord.getAgendaName());
        oleOrderRecord.setOleOriginalBibRecordFileName(ingestRecord.getOriginalMarcFileName());
        oleOrderRecord.setOriginalEDIFileName(ingestRecord.getOriginalEdiFileName());
        oleOrderRecord.setOriginalRecord(records.get(marcCount));
        oleOrderRecord.setDescription(ingestRecord.getAgendaDescription());
        oleOrderRecord.setOriginalEdi(ediOrder);
        oleOrderRecord.setOleBibRecord(new OleBibRecord());
        oleOrderRecord.setOleTxRecord(null);
        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_RECORD,false);
        oleOrderRecord.addMessageToMap(OLEConstants.IS_BAD_CTRL_FLD,false);
        List rulesEvaluatedList = new ArrayList();
        String ruleEvaluated = "ISBN Validation"+ " : " + OLEConstants.TRUE;
        rulesEvaluatedList.add(ruleEvaluated);
        oleOrderRecord.addMessageToMap(OLEConstants.RULES_EVAL, rulesEvaluatedList);
        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_BFN,OLEConstants.FALSE);
        return oleOrderRecord;
    }

    /**
     * This method is for populating order record values for Extra Edi.
     * @param ediCount
     * @param ediOrder
     * @return OleOrderRecord values.
     */
    private OleOrderRecord populateOrderRecordForExtraEdi(int ediCount,EDIOrder ediOrder){
        LOG.info("----Inside populateOrderRecordForExtraEdi()------------------------------");
        OleOrderRecord oleOrderRecord=new OleOrderRecord();
        oleOrderRecord.setAgendaName(ingestRecord.getAgendaName());
        oleOrderRecord.setOleOriginalBibRecordFileName(ingestRecord.getOriginalMarcFileName());
        oleOrderRecord.setOriginalEDIFileName(ingestRecord.getOriginalEdiFileName());
        oleOrderRecord.setOriginalRecord(null);
        oleOrderRecord.setDescription(ingestRecord.getAgendaDescription());
        oleOrderRecord.setOriginalEdi(ediOrder);
        oleOrderRecord.setOleBibRecord(new OleBibRecord());
        OleTxRecord oleTxRecord = null;
        try {
            oleTxRecord = getOleTxRecord(ediOrder.getLineItemOrder().get(ediCount), ediOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        oleOrderRecord.setOleTxRecord(oleTxRecord);
        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_RECORD,false);
        oleOrderRecord.addMessageToMap(OLEConstants.IS_BAD_CTRL_FLD,false);
        List rulesEvaluatedList = new ArrayList();
        String ruleEvaluated = "ISBN Validation"+ " : " + OLEConstants.TRUE;
        rulesEvaluatedList.add(ruleEvaluated);
        oleOrderRecord.addMessageToMap(OLEConstants.RULES_EVAL, rulesEvaluatedList);
        oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_BFN,OLEConstants.FALSE);
        return oleOrderRecord;
    }

    private void createErrorLog(OLEBatchProcessJobDetailsBo job){
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        //List reasonForFailure = (List) dataCarrierService.getData(OLEConstants.FAILURE_REASON);
        List reasonForFailure = (List) orderImportHelperBo.getFailureReason();
        if(reasonForFailure != null){
            List<String> failureRecords = new ArrayList<>();
            failureRecords.add("Docstore Exception : Cannot create/update bib , Check Bib Import Profile values");
            failureRecords.add("Note : Bib Overlay can be performed only if there is an availability of Bib records in the docstore");
            failureRecords.add("===============================================================================");
            failureRecords.add("");
            reasonForFailure.addAll(failureRecords);
            //dataCarrierService.addData(OLEConstants.FAILURE_REASON, reasonForFailure);
            orderImportHelperBo.setFailureReason(reasonForFailure);
        }
    }

    private void createErrorLog(OLEBatchProcessJobDetailsBo job, BibMarcRecord bibMarcRecord){
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        //List reasonForFailure = (List) dataCarrierService.getData(OLEConstants.FAILURE_REASON);
        List reasonForFailure = (List) orderImportHelperBo.getFailureReason();
        if(reasonForFailure != null){
            List<String> failureRecords = new ArrayList<>();
            String title = null;
            for (DataField dataField : bibMarcRecord.getDataFields()){
                if (dataField.getTag().equals("245")){
                    for (SubField subfield : dataField.getSubFields()){
                        if (subfield.getCode().equals("a")){
                            title = subfield.getValue();
                        }
                    }
                }
            }
            if (title!=null){
                failureRecords.add("For Title : "+title);
            }
            failureRecords.add("Docstore Exception : Cannot create/update bib , Check Bib Import Profile values");
            failureRecords.add("Note : Bib Overlay can be performed only if there is an availability of Bib records in the docstore");
            failureRecords.add("===============================================================================");
            failureRecords.add("");
            reasonForFailure.addAll(failureRecords);
            //dataCarrierService.addData(OLEConstants.FAILURE_REASON, reasonForFailure);
            orderImportHelperBo.setFailureReason(reasonForFailure);
        }
    }
    private void createErrorLog(OLEBatchProcessJobDetailsBo job, BibMarcRecord bibMarcRecord, String failureReason){
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        //List reasonForFailure = (List) dataCarrierService.getData(OLEConstants.FAILURE_REASON);
        List reasonForFailure = (List) orderImportHelperBo.getFailureReason();
        if(reasonForFailure != null){
            List<String> failureRecords = new ArrayList<>();
            String title = null;
            for (DataField dataField : bibMarcRecord.getDataFields()){
                if (dataField.getTag().equals("245")){
                    for (SubField subfield : dataField.getSubFields()){
                        if (subfield.getCode().equals("a")){
                            title = subfield.getValue();
                        }
                    }
                }
            }
            if (title!=null){
                failureRecords.add("For Title : "+title);
            }
            failureRecords.add(failureReason);
            failureRecords.add("===============================================================================");
            reasonForFailure.addAll(failureRecords);
            //dataCarrierService.addData(OLEConstants.FAILURE_REASON, reasonForFailure);
            orderImportHelperBo.setFailureReason(reasonForFailure);
        }
    }

    public String getParameter(String name){
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID,OLEConstants.SELECT_NMSPC,OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

    private OleTxRecord getOleTxRecord(LineItemOrder lineItemOrder,EDIOrder ediOrder) throws Exception {
        OleTxRecord oleTxRecord = OleTxRecordBuilder.getInstance().build(lineItemOrder, ediOrder);
        return oleTxRecord;
    }

    /**
     *  Gets the instance of businessObjectService.
     *  if businessObjectService is null it will create a new instance else it will return existing businessObjectService.
     * @return  businessObjectService
     */
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     *  This method returns selector map contains OLE code  and contextName.
     * @param contextName
     * @return  selector
     */
    protected Map<String, String> getSelectionContext(String contextName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(NAMESPACE_CODE_SELECTOR, "OLE");
        selector.put(NAME_SELECTOR, contextName);
        return selector;
    }

    /**
     *   This method returns selector map contains agendaName
     * @param agendaName
     * @return  selector
     */
    protected Map<String, String> getAgendaContext(String agendaName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(NAME_SELECTOR, agendaName);
        return selector;
    }

    public abstract String preProcessMarc(String marcFileContent, OLEBatchProcessJobDetailsBo job);

    public abstract String preProcessEDI(String ediFileContent);

    public abstract void postProcess(OLEBatchProcessJobDetailsBo job);

    public void setMarcXMLContent(String marcXMLContent) {
        this.marcXMLContent = marcXMLContent;
    }

    public void setIngestRecord(IngestRecord ingestRecord) {
        this.ingestRecord = ingestRecord;
    }
}
