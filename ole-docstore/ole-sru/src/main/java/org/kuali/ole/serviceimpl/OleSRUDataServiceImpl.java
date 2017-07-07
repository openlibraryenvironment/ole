package org.kuali.ole.serviceimpl;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.OLESruItemHandler;
import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;
import org.kuali.ole.bo.serachRetrieve.*;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.bib.dc.BibDcRecord;
import org.kuali.ole.docstore.common.document.content.bib.dc.DCValue;
import org.kuali.ole.docstore.common.document.content.bib.dc.xstream.BibDcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.service.SRUCQLQueryService;
import org.kuali.ole.docstore.discovery.service.SRUCQLQueryServiceImpl;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.docstore.model.bo.OleDocument;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.service.OleWebServiceProvider;
import org.kuali.ole.docstore.service.impl.OleWebServiceProviderImpl;
import org.kuali.ole.handler.OleSRUDublinRecordResponseHandler;
import org.kuali.ole.handler.OleSRUOpacXMLResponseHandler;
import org.kuali.ole.pojo.OLESruItem;
import org.kuali.ole.service.OleDiagnosticsService;
import org.kuali.ole.service.OleLoanDocumentWebService;
import org.kuali.ole.service.OleSRUDataService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 7:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUDataServiceImpl implements OleSRUDataService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    public SRUCQLQueryService srucqlQueryService;
    public OleDiagnosticsService oleDiagnosticsService;
    private DocstoreClient docstoreClient;

    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    private InstanceOlemlRecordProcessor instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
    private OLESruItemHandler oleSruItemHandler;

    public OleSRUDataServiceImpl() {
        srucqlQueryService = SRUCQLQueryServiceImpl.getInstance();
        oleDiagnosticsService = new OleDiagnosticsServiceImpl();
    }

    public OLESruItemHandler getOleSruItemHandler(){
        if(oleSruItemHandler == null){
            oleSruItemHandler = new OLESruItemHandler();
        }
        return oleSruItemHandler;
    }

    /**
     * this method will fetch the bib id list from the docstore
     *
     * @param reqParamMap
     * @param solrQuery
     * @return list of bib id
     */
    public List getBibRecordsIdList(Map reqParamMap, String solrQuery) {
        LOG.info("Inside getBibRecordsIdList method");
        List oleBibIDList = null;
        List<OleDocument> oleDocumentsList = null;
        try {
            oleBibIDList = new ArrayList();
            oleDocumentsList = (ArrayList<OleDocument>) srucqlQueryService.queryForBibDocs(reqParamMap, solrQuery);
            if ((oleDocumentsList != null) && (oleDocumentsList.size() > 0)) {
                oleBibIDList = generateBibIDList(oleDocumentsList);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            if(e instanceof SolrServerException){
                if(solrQuery.contains("LocalId"))
                    oleBibIDList.add("Invalid Local Id");
                else
                    oleBibIDList.add("Exception Occured");

                return  oleBibIDList;
            } else
                return null;
        }
        return oleBibIDList;
    }

    /**
     * this method will fetch the bib details , holding details from the docstore data base , creates an object for bib and holdings , and generates OPAC xml
     *
     * @param oleBibIDList
     * @param reqParamMap
     * @return opac xml or diagnostics or oleSRUResponseDocuments as a string depends upon the record packing
     */
    public String getOPACXMLSearchRetrieveResponse(List oleBibIDList, Map reqParamMap) {
        LOG.info("Inside getOPACXMLSearchRetrieveResponse method");
        OleSRUOpacXMLResponseHandler oleSRUOpacXMLResponseHandler = new OleSRUOpacXMLResponseHandler();
        OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponse = new OleSRUSearchRetrieveResponse();
        oleSRUSearchRetrieveResponse.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
        OleSRUDiagnostics oleSRUDiagnostics = new OleSRUDiagnostics();
        try {
            OleSRUResponseRecords oleSRUResponseRecords = new OleSRUResponseRecords();
            List<OleSRUResponseRecord> oleSRUResponseRecordList = new ArrayList<OleSRUResponseRecord>();
            if (oleBibIDList != null && oleBibIDList.size() > 0) {
                List<OleSRUResponseDocument> oleSRUResponseDocumentList = new ArrayList<OleSRUResponseDocument>();
                String bibRecordInfo = null;
                int startPosition = 0;
                if ((Integer) reqParamMap.get(OleSRUConstants.START_RECORD) != 0) {
                    startPosition = (Integer) reqParamMap.get(OleSRUConstants.START_RECORD);
                }
                for (int i = 0; i < oleBibIDList.size(); i++) {
                    OleSRUResponseRecord oleSRUResponseRecord = new OleSRUResponseRecord();
                    oleSRUResponseRecord.setRecordPacking((String) reqParamMap.get(OleSRUConstants.RECORD_PACKING));
                    if ((Integer) reqParamMap.get(OleSRUConstants.START_RECORD) == 0) {
                        oleSRUResponseRecord.setRecordPosition(i + 1);
                    } else {
                        oleSRUResponseRecord.setRecordPosition(startPosition + 1);
                        startPosition = startPosition + 1;
                    }
                    OleSRUResponseDocument oleSRUResponseDocument = new OleSRUResponseDocument();
                    OleSRUData oleSRUData = (OleSRUData) oleBibIDList.get(i);
                    bibRecordInfo = getBibliographicRecordInfo(oleSRUData.getBibId());
                    BibMarcRecords bibMarcRecords=(BibMarcRecords)(new BibMarcRecordProcessor()).fromXML(bibRecordInfo);
                    OleSRUResponseRecordData oleSRUResponseRecordData = new OleSRUResponseRecordData();
                    String recordSchema= (String)reqParamMap.get(OleSRUConstants.RECORD_SCHEMA);
                    if (recordSchema.equalsIgnoreCase(OleSRUConstants.OPAC_RECORD)) {
                        List<OleSRUInstanceDocument> oleSRUInstanceDocumentList = new ArrayList<OleSRUInstanceDocument>();
                        if (oleSRUData.getInstanceIds() != null && oleSRUData.getInstanceIds().size() > 0) {
                            for (String instance : oleSRUData.getInstanceIds()) {
                                String holdingInfo = getInstanceInfoRecordInfo(instance);
                                OleSRUInstanceDocument oleSRUInstanceDocument = processInstanceCollectionXml(holdingInfo);
                                oleSRUInstanceDocumentList.add(oleSRUInstanceDocument);
                            }
                        }
                        oleSRUResponseRecordData.setHoldings(oleSRUInstanceDocumentList);
                    }

                    if (((String) reqParamMap.get(OleSRUConstants.RECORD_SCHEMA)).equalsIgnoreCase(OleSRUConstants.DUBLIN_RECORD_SCHEMA)) {
                        String dublinRecordInfo = null;
                        BibDcRecordProcessor qualifiedDublinRecordHandler = new BibDcRecordProcessor();
                        BibDcRecord workBibDublinRecord = qualifiedDublinRecordHandler.fromXML(bibRecordInfo);
                        OleSRUDublinRecord oleSRUDublinRecord = new OleSRUDublinRecord();
                        for (DCValue dcValue : workBibDublinRecord.getDcValues()) {
                            oleSRUDublinRecord.put(dcValue.getElement(), dcValue.getValue());
                        }
                        OleSRUDublinRecordResponseHandler oleSRUDublinRecordResponseHandler = new OleSRUDublinRecordResponseHandler();
                        String dublinInfo = oleSRUDublinRecordResponseHandler.toXML(oleSRUDublinRecord);
                        //dublinInfo = dublinInfo.replaceAll("&amp;","&");
                        //bibRecordInfo=StringEscapeUtils.unescapeXml(bibRecordInfo);
                        oleSRUResponseRecordData.setBibliographicRecord(dublinInfo);
                        oleSRUResponseDocument.setOleSRUResponseRecordData(oleSRUResponseRecordData);
                    } else {
                        //bibRecordInfo = bibRecordInfo.replaceAll("&amp;","&");
                        //bibRecordInfo=StringEscapeUtils.unescapeXml(bibRecordInfo);
                        oleSRUResponseRecordData.setBibliographicRecord(bibRecordInfo);
                        oleSRUResponseRecordData.setBibMarcRecords(bibMarcRecords);
                        oleSRUResponseDocument.setOleSRUResponseRecordData(oleSRUResponseRecordData);
                    }
                    oleSRUSearchRetrieveResponse.setNumberOfRecords((Long) (reqParamMap.get(OleSRUConstants.NUMBER_OF_REORDS)));
                    if(recordSchema == null ||(recordSchema!=null && (recordSchema.equalsIgnoreCase(OleSRUConstants.MARC) || recordSchema.equalsIgnoreCase(OleSRUConstants.MARC_SCHEMA)))){
                        oleSRUResponseRecord.setRecordSchema(OleSRUConstants.MARC_RECORD_RESPONSE_SCHEMA);
                    }else if(recordSchema!=null && recordSchema!=null && recordSchema.equalsIgnoreCase(OleSRUConstants.DC_SCHEMA)){
                        oleSRUResponseRecord.setRecordSchema(OleSRUConstants.DC_RECORD_RESPONSE_SCHEMA);
                    }else if(recordSchema!=null && recordSchema.equalsIgnoreCase(OleSRUConstants.OPAC_RECORD)){
                        oleSRUResponseRecord.setRecordSchema(OleSRUConstants.OPAC_RECORD_RESPONSE_SCHEMA);
                    }
                    if (reqParamMap.containsKey(OleSRUConstants.EXTRA_REQ_DATA_KEY))
                        oleSRUResponseRecordData.setExtraRequestData(getExtraReqDataInfo(reqParamMap));
                    oleSRUResponseRecord.setOleSRUResponseDocument(oleSRUResponseDocument);
                    oleSRUResponseRecordList.add(oleSRUResponseRecord);
                }
                oleSRUResponseRecords.setOleSRUResponseRecordList(oleSRUResponseRecordList);
                oleSRUSearchRetrieveResponse.setOleSRUResponseRecords(oleSRUResponseRecords);
                int maxRecords = (Integer) reqParamMap.get(OleSRUConstants.MAXIMUM_RECORDS);
                if (maxRecords == 0) {
                    oleSRUSearchRetrieveResponse = new OleSRUSearchRetrieveResponse();
                    oleSRUSearchRetrieveResponse.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
                    oleSRUSearchRetrieveResponse.setNumberOfRecords((Long) (reqParamMap.get(OleSRUConstants.NUMBER_OF_REORDS)));
                    String xml = oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get(OleSRUConstants.RECORD_SCHEMA));
                    return xml;
                }
                if (OleSRUConstants.RECORD_PACK_XML.equalsIgnoreCase((String) reqParamMap.get(OleSRUConstants.RECORD_PACKING))) {
                    String opacXML = oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get(OleSRUConstants.RECORD_SCHEMA));
                    if (null != opacXML)
                        //opacXML=StringEscapeUtils.unescapeXml(opacXML);
                        //opacXML = replaceStringWithSymbols(opacXML);
                    return opacXML;
                } else if (OleSRUConstants.RECORD_PACK_STRING.equalsIgnoreCase((String) reqParamMap.get(OleSRUConstants.RECORD_PACKING))) {
                    return oleSRUSearchRetrieveResponse.toString();
                }
            }
            Long numberOfRecords = (Long) (reqParamMap.get(OleSRUConstants.NUMBER_OF_REORDS));
            oleSRUSearchRetrieveResponse.setNumberOfRecords(numberOfRecords);
            if (numberOfRecords > 0) {
                oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.START_RECORD_UNMATCH));
            } else {
                oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.NORECORDS_DIAGNOSTIC_MSG));
            }
            int start = (Integer) reqParamMap.get(OleSRUConstants.START_RECORD);
            if(start > numberOfRecords){
                oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.START_RECORD_UNMATCH));
            }
            oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
            return oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get(OleSRUConstants.RECORD_SCHEMA));
        } catch (Exception e) {
            LOG.error(e.getMessage() , e );
        }
        oleSRUDiagnostics = oleDiagnosticsService.getDiagnosticResponse(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.SEARCH_PROCESS_FAILED));
        oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
        return oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get(OleSRUConstants.RECORD_SCHEMA));
    }

    /**
     * this will generate the list of document from docstore
     *
     * @param oleDocumentsList (list of OleDocument)
     * @return bib id list
     */
    public List generateBibIDList(List<OleDocument> oleDocumentsList) {
        LOG.info("Inside generateBibIDList method");
        List oleBibIDList = new ArrayList();
        WorkBibDocument workBibDocument = null;
        List<OleSRUData> oleSRUDataList = new ArrayList<OleSRUData>();
        OleSRUData oleSRUData = null;
        for (int i = 0; i < oleDocumentsList.size(); i++) {
            workBibDocument = (WorkBibDocument) oleDocumentsList.get(i);
            oleSRUData = new OleSRUData();
            oleSRUData.setBibId(workBibDocument.getId());
            List<String> instanceList = new ArrayList<String>();
            if (workBibDocument.getWorkInstanceDocumentList() != null) {
                for (WorkInstanceDocument workInstanceDocument : workBibDocument.getWorkInstanceDocumentList()) {
                    instanceList.add(workInstanceDocument.getInstanceIdentifier());
                }
            }
            oleSRUData.setInstanceIds(instanceList);
            oleSRUDataList.add(oleSRUData);
        }
        return oleSRUDataList;
    }

    /**
     * this method will do checkout operation of docstore which gives the bib info details
     *
     * @param uuid
     * @return bib info xml as a string
     */
    public String getBibliographicRecordInfo(String uuid) {
        LOG.info("Inside getBibliographicRecordInfo method");
        String bibRecordInfo = null;

        Bib bib =  getDocstoreClient().retrieveBib(uuid);
        bibRecordInfo = bib.getContent();
//        CheckoutManager checkoutManager = new CheckoutManager();
      /*  try {
            Request req = new Request();
            if (DocumentUniqueIDPrefix.hasPrefix(uuid)) {
                Map<String, String> categoryTypeFormat = DocumentUniqueIDPrefix.getCategoryTypeFormat(uuid);
                String category = categoryTypeFormat.get("category");
                String type = categoryTypeFormat.get("type");
                String format = categoryTypeFormat.get("format");
                RequestDocument requestDocument = new RequestDocument();
                requestDocument.setCategory(category);
                requestDocument.setType(type);
                requestDocument.setFormat(format);
                requestDocument.setUuid(uuid);
                List<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
                requestDocuments.add(requestDocument);
                req.setOperation("checkOut");
                req.setUser("checkOutUser");
                req.setRequestDocuments(requestDocuments);
                Response response = BeanLocator.getDocstoreFactory().getDocumentService().process(req);
                if(response.getDocuments()!=null && response.getDocuments().size()>0)
                bibRecordInfo = response.getDocuments().get(0).getContent().getContent();
            } else {
                bibRecordInfo = checkoutManager.checkOut(uuid, "defaultUser", "checkOut");
            }
            LOG.info("BibRecordInfo:" + bibRecordInfo);
            if (bibRecordInfo != null) {
                bibRecordInfo = bibRecordInfo.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
                bibRecordInfo = bibRecordInfo.replace("<collection>","");
                bibRecordInfo = bibRecordInfo.replace("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">", "");
                bibRecordInfo = bibRecordInfo.replace("</collection>", "");
                bibRecordInfo = bibRecordInfo.replace("<record>", "<record xmlns=\"http://www.loc.gov/MARC21/slim\">");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() , e );
        }*/
        return bibRecordInfo;
    }


    public String getInstanceInfoRecordInfo(String uuid) {
        String instanceXml = "";
        InstanceCollection instanceCollection = new InstanceCollection();
        HoldingsTree holdingsTree = getDocstoreClient().retrieveHoldingsTree(uuid);
        if(holdingsTree!=null && holdingsTree.getHoldings()!=null && holdingsTree.getHoldings().getContent()!=null){
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
        Items items = new Items();
        if(holdingsTree!=null && holdingsTree.getItems()!=null){
        for(org.kuali.ole.docstore.common.document.Item itemDoc : holdingsTree.getItems()) {
            items.getItem().add(itemOlemlRecordProcessor.fromXML(itemDoc.getContent()));
        }
        }
        Instance instance = new Instance();
        instance.setOleHoldings(oleHoldings);
        instance.setItems(items);
        instanceCollection.getInstance().add(instance);
        instanceXml = instanceOlemlRecordProcessor.toXML(instanceCollection);
    }
        return instanceXml;
    }

    /**
     * to replace the encoded value from the string to original special character to display as an xml
     *
     * @param
     * @return opac xml response
     */
   /* private String replaceStringWithSymbols(String opacXML) {
        LOG.info("Inside replaceStringWithSymbols method");
        opacXML = opacXML.replaceAll("&lt;", "<");
        opacXML = opacXML.replaceAll("&gt;", ">");
        opacXML = opacXML.replaceAll("&quot;", "\"");
        opacXML = opacXML.replaceAll("&apos;", "\'");
        //opacXML =opacXML.replaceAll("&amp;","&");
        //opacXML = opacXML.replaceAll("&","&amp;");
        //opacXML = StringEscapeUtils.unescapeHtml(opacXML).replaceAll("[^\\x20-\\x7e]", "");
        return opacXML;
    }*/

    public List<OleSRUInstanceDocument> getOleSRUInstanceDocument() {
        LOG.info("Inside getOleSRUInstanceDocument method");
        OleSRUInstanceDocument oleSRUInstanceDocument = new OleSRUInstanceDocument();
        List<OleSRUInstanceDocument> oleSRUInstanceDocumentList = new ArrayList<OleSRUInstanceDocument>();
        List<OleSRUCirculationDocument> oleSRUCirculationDocumentList = getOleSRUCirculationDocument();
        List<OleSRUInstanceVolume> oleSRUInstanceVolumeList = getOleSRUInstanceVolumes();
        oleSRUInstanceDocument.setCallNumber("mockCallNumber");
        oleSRUInstanceDocument.setCompleteness("mockCompleteness");
        oleSRUInstanceDocument.setCopyNumber("mockCopyNumber");
        oleSRUInstanceDocument.setDateOfReport("mockDateOfReport");
        oleSRUInstanceDocument.setEncodingLevel("mockEncodingLevel");
        oleSRUInstanceDocument.setEnumAndChron("mockEnumAndChorn");
        oleSRUInstanceDocument.setFormat("mockFormat");
        oleSRUInstanceDocument.setGeneralRetention("mockGeneralRentention");
        oleSRUInstanceDocument.setLocalLocation("mockLocalLocation");
        oleSRUInstanceDocument.setTypeOfRecord("mockTypeOfRecord");
        oleSRUInstanceDocument.setShelvingLocation("mockShelvingLocation");
        oleSRUInstanceDocument.setShelvingData("mockShelvingData");
        oleSRUInstanceDocument.setReproductionNote("mockReproductionNote");
        oleSRUInstanceDocument.setReceiptAcqStatus("mockReceiptAcqStatus");
        oleSRUInstanceDocument.setNucCode("mockNucCode");
        oleSRUInstanceDocument.setPublicNote("mockPublicNote");
        oleSRUInstanceDocument.setCirculations(oleSRUCirculationDocumentList);
        oleSRUInstanceDocument.setVolumes(oleSRUInstanceVolumeList);
        oleSRUInstanceDocumentList.add(oleSRUInstanceDocument);
        return oleSRUInstanceDocumentList;
    }

    public List<OleSRUCirculationDocument> getOleSRUCirculationDocument() {
        LOG.info("Inside getOleSRUCirculationDocument method");
        OleSRUCirculationDocument oleSRUCirculationDocument = new OleSRUCirculationDocument();
        List<OleSRUCirculationDocument> oleSRUCirculationDocumentList = new ArrayList<OleSRUCirculationDocument>();
        oleSRUCirculationDocument.setAvailabilityDate("mockAvailabilityDate");
        oleSRUCirculationDocument.setAvailableNow("mockAvailableNow");
        oleSRUCirculationDocument.setAvailableThru("mockAvailableThru");
        oleSRUCirculationDocument.setEnumAndChron("mockEnumAndChron");
        oleSRUCirculationDocument.setItemId("mockItemId");
        oleSRUCirculationDocument.setMidspine("mockMidspine");
        oleSRUCirculationDocument.setOnHold("mockOnHold");
        //oleSRUCirculationDocument.setRenewable("mockRenewable");
        oleSRUCirculationDocument.setRestrictions("mockRestrictions");
        oleSRUCirculationDocument.setTemporaryLocation("mockTemporaryLocation");
        oleSRUCirculationDocumentList.add(oleSRUCirculationDocument);
        return oleSRUCirculationDocumentList;
    }

    public List<OleSRUInstanceVolume> getOleSRUInstanceVolumes() {
        LOG.info("Inside getOleSRUInstanceVolumes method");
        OleSRUInstanceVolume oleSRUInstanceVolume = new OleSRUInstanceVolume();
        List<OleSRUInstanceVolume> oleSRUInstanceVolumeList = new ArrayList<OleSRUInstanceVolume>();
        oleSRUInstanceVolume.setChronology("mockChronology");
        oleSRUInstanceVolume.setEnumAndChron("mockEnumAndChron");
        oleSRUInstanceVolume.setEnumeration("mockEnumeration");
        oleSRUInstanceVolumeList.add(oleSRUInstanceVolume);
        return oleSRUInstanceVolumeList;
    }

    public String getExtraReqDataInfo(Map reqParamMap) {
        LOG.info("Inside getExtraReqDataInfo method");
        return "<theo:" + reqParamMap.get(OleSRUConstants.EXTRA_REQ_DATA_KEY) + " xmlns:theo=\"" + ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.EXTRA_REQ_DATA_XML_NAMESPACE) + "\">\n" +
                reqParamMap.get(OleSRUConstants.EXTRA_REQ_DATA_VALUE) + "\n" +
                "</theo:" + reqParamMap.get(OleSRUConstants.EXTRA_REQ_DATA_KEY) + ">";
    }

    public OleSRUInstanceDocument processInstanceCollectionXml(String instanceXml) {
        LOG.info("Inside processInstanceCollectionXml method");
        String sruTrueValue = OleSRUConstants.BOOLEAN_FIELD_TRUE_FORMAT;
        String sruFalseValue = OleSRUConstants.BOOLEAN_FIELD_FALSE_FORMAT;
        String availableItemStatus = OleSRUConstants.SRU_AVAILABLE_STATUSES;
        String holdItemStatus = OleSRUConstants.SRU_ON_HOLD_STATUSES;
        List<String> availableItemStatuses = new ArrayList<String>();
        List<String> holdItemStatuses = new ArrayList<String>();

        if(StringUtils.isNotEmpty(availableItemStatus)){
            String[] availableStatus =  availableItemStatus.split(";");
            Collections.addAll(availableItemStatuses, availableStatus);
        }


        if(StringUtils.isNotEmpty(holdItemStatus)){
            String[] holdStatus =  holdItemStatus.split(";");
            Collections.addAll(holdItemStatuses, holdStatus);
        }

        InstanceOlemlRecordProcessor instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        InstanceCollection instanceCollection = instanceOlemlRecordProcessor.fromXML(instanceXml);
        OleSRUInstanceDocument oleSRUInstanceDocument = null;
        OleWebServiceProvider oleWebServiceProvider = new OleWebServiceProviderImpl();
        String url = ConfigContext.getCurrentContextConfig().getProperty("oleLoanWebService.url");
        OleLoanDocumentWebService oleLoanDocumentService = (OleLoanDocumentWebService) oleWebServiceProvider.getService("org.kuali.ole.service.OleLoanDocumentWebService", "oleLoanWebService", url);
        String sruItemContent;
        OLESruItem oleSruItem;
        if (instanceCollection != null) {
            List<Instance> instances = instanceCollection.getInstance();
            if (instances != null && instances.size() > 0) {
                for (Instance instance : instances) {
                    oleSRUInstanceDocument = new OleSRUInstanceDocument();
                    if (instance.getOleHoldings() != null && instance.getOleHoldings().getCallNumber() != null) {
                        oleSRUInstanceDocument.setCallNumber(instance.getOleHoldings().getCallNumber().getNumber());
                        oleSRUInstanceDocument.setShelvingData(instance.getOleHoldings().getCallNumber().getPrefix());
                    }
                    if (instance.getOleHoldings() != null) {
                        oleSRUInstanceDocument.setReceiptAcqStatus(instance.getOleHoldings().getReceiptStatus());
                    }
                    if (instance.getOleHoldings().getLocation() != null) {
                        sruItemContent = oleLoanDocumentService.getItemInformation(null,null,null, getLocations(instance.getOleHoldings().getLocation(), OleSRUConstants.SHELVING_LOCATION), getLocations(instance.getOleHoldings().getLocation(), OleSRUConstants.LOCAL_LOCATION));
                        oleSruItem = (OLESruItem) getOleSruItemHandler().getObjectFromXml(sruItemContent, (Object) new OLESruItem());
                        if (oleSruItem != null) {
                            oleSRUInstanceDocument.setLocalLocation(oleSruItem.getLocalLocation());
                            oleSRUInstanceDocument.setShelvingLocation(oleSruItem.getShelvingLocation());
                        }
                    }
                    Items items = instance.getItems();
                    List<OleSRUInstanceVolume> oleSRUInstanceVolumeList = new ArrayList<OleSRUInstanceVolume>();
                    List<OleSRUCirculationDocument> oleSRUCirculationDocumentList = new ArrayList<OleSRUCirculationDocument>();
                    if (items != null) {
                        List<Item> itemList = items.getItem();

                        for (int i = 0; i < itemList.size(); i++) {
                            Item item = itemList.get(i);
                            if (item != null) {
                                oleSRUInstanceDocument.setCopyNumber(item.getCopyNumber());
                            }
                            OleSRUInstanceVolume oleSRUInstanceVolume = new OleSRUInstanceVolume();
                            oleSRUInstanceVolume.setChronology(item.getChronology());
                            oleSRUInstanceVolume.setEnumeration(item.getEnumeration());
                            if (item.getEnumeration() != null && item.getChronology() != null) {
                                oleSRUInstanceVolume.setEnumAndChron(item.getEnumeration() + "," + item.getChronology());
                            }
                            oleSRUInstanceVolumeList.add(oleSRUInstanceVolume);
                            OleSRUCirculationDocument oleSRUCirculationDocument = new OleSRUCirculationDocument();
                            oleSRUCirculationDocument.setItemId(item.getBarcodeARSL());
                            StringBuffer locationName = new StringBuffer();
                            if (item.getItemStatus() != null && availableItemStatuses.contains(item.getItemStatus().getCodeValue())) {
                                oleSRUCirculationDocument.setAvailableNow(sruTrueValue);
                            } else {
                                oleSRUCirculationDocument.setAvailableNow(sruFalseValue);
                            }
                            if (item.getItemStatus() != null && holdItemStatuses.contains(item.getItemStatus().getCodeValue())) {
                                oleSRUCirculationDocument.setOnHold(sruTrueValue);
                            } else {
                                oleSRUCirculationDocument.setOnHold(sruFalseValue);
                            }
                            String shelvingLocationCode = "";
                            if (item.getLocation() != null) {
                                shelvingLocationCode = getShelvingLocation(item.getLocation());
                            }
                            String itemTypeCode = "";
                            if (item.getItemType() != null) {
                                itemTypeCode = item.getItemType().getCodeValue();
                            }

                            if (item.getLocation() != null) {
                                sruItemContent = oleLoanDocumentService.getItemInformation(item.getItemIdentifier(), itemTypeCode, shelvingLocationCode, getLocations(item.getLocation(), OleSRUConstants.SHELVING_LOCATION), getLocations(item.getLocation(), OleSRUConstants.LOCAL_LOCATION));
                                oleSruItem = (OLESruItem) getOleSruItemHandler().getObjectFromXml(sruItemContent, (Object) new OLESruItem());
                                if (oleSruItem != null) {
                                    String tempLocation = oleSruItem.getShelvingLocation() != null ? oleSruItem.getShelvingLocation() : "";
                                    if (!"".equals(tempLocation)) {
                                        oleSRUCirculationDocument.setTemporaryLocation(tempLocation);
                                    }
                                }
                            }

                            if(item.getDueDateTime()!=null){
                                oleSRUCirculationDocument.setAvailabilityDate(item.getDueDateTime());
                            }
                            if (item.getEnumeration() != null && item.getChronology() != null) {
                                oleSRUCirculationDocument.setEnumAndChron(item.getEnumeration() + "," + item.getChronology());
                            }
                            if (item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
                                oleSRUCirculationDocument.setItemId(item.getAccessInformation().getBarcode());
                            }
                            if(item.getTemporaryItemType() !=null && item.getTemporaryItemType().getFullValue()!=null){
                                oleSRUCirculationDocument.setRestrictions(item.getTemporaryItemType().getFullValue());
                                oleSRUCirculationDocument.setAvailableThru(item.getTemporaryItemType().getFullValue());
                            }else if(item.getItemType() !=null && item.getItemType().getFullValue()!=null){
                                oleSRUCirculationDocument.setRestrictions(item.getItemType().getFullValue());
                                oleSRUCirculationDocument.setAvailableThru(item.getItemType().getFullValue());
                            }
                            oleSRUCirculationDocumentList.add(oleSRUCirculationDocument);
                        }
                    }
                    oleSRUInstanceDocument.setVolumes(oleSRUInstanceVolumeList);
                    oleSRUInstanceDocument.setCirculations(oleSRUCirculationDocumentList);
                }
            }
        }
        return oleSRUInstanceDocument;
    }


    public String getParameter(String name) {
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", "OLE-DESC");
            criteriaMap.put("componentCode", "Describe");
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>) KRADServiceLocator.getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception while getting parameter value", e);
        }
        return parameter;
    }
    private String getShelvingLocation(Location location) {
        String locationName = "";
        if (location.getLocationLevel() != null) {
            locationName = location.getLocationLevel().getName();

            if (location.getLocationLevel().getLocationLevel() != null) {
                locationName = location.getLocationLevel().getLocationLevel().getName();

                if (location.getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                    locationName = location.getLocationLevel().getLocationLevel().getLocationLevel().getName();

                    if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                        locationName = location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName();

                        if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                            locationName = location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName();
                        }
                    }
                }
            }
        }
        return locationName;
    }
    private String getLocations(Location location,String paramName) {
        String locationName = "";
        LocationLevel locationLevel = location.getLocationLevel();
        if(locationLevel != null) {
            if (StringUtils.isNotBlank(paramName)) {
                if (locationLevel.getLevel() != null && locationLevel.getLevel().equalsIgnoreCase(paramName)) {
                    locationName = locationLevel.getName();
                } else if (locationLevel.getLocationLevel() != null && locationLevel.getLocationLevel().getLevel() != null && locationLevel.getLocationLevel().getLevel().equalsIgnoreCase(paramName)) {
                    locationName = locationLevel.getLocationLevel().getName();
                } else if (locationLevel.getLocationLevel().getLocationLevel() != null && locationLevel.getLocationLevel().getLocationLevel().getLevel() != null && locationLevel.getLocationLevel().getLocationLevel().getLevel().equalsIgnoreCase(paramName)) {
                    locationName = locationLevel.getLocationLevel().getLocationLevel().getName();
                }
            }
        }
        return locationName;
    }

    private DocstoreClient getDocstoreClient(){

        if (docstoreClient == null) {
            docstoreClient = new DocstoreLocalClient();
        }
        return docstoreClient;
    }

}
