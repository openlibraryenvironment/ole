package org.kuali.ole.service;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleReceiptStatus;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Bibs;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.service.impl.OLESerialReceivingService;
import org.kuali.ole.service.impl.OLESerialReceivingServiceImpl;
import org.kuali.ole.sys.businessobject.FinancialSystemDocumentHeader;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.document.DocumentBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 2/10/14
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templatses.
 */
/*
This class is used to convert the serial receiving xml content to Serial Receiving Record
 */
public class OLESerialReceivingImportProcessor {

    private static final Logger LOG = Logger.getLogger(OLESerialReceivingImportProcessor.class);
    public static final List<String> recordTypeList =  getSerialRecordTypeList();
    private BusinessObjectService businessObjectService = getBusinessObjectService();
    private OLESerialReceivingService oleSerialReceivingService = getOleSerialReceivingService();
    private DocstoreClientLocator docstoreClientLocator = getDocstoreClientLocator();
    DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public OLESerialReceivingImportProcessor() {
        getBusinessObjectService();
        getOleSerialReceivingService();
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OLESerialReceivingService getOleSerialReceivingService() {
        if (oleSerialReceivingService == null) {
            oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        }
        return oleSerialReceivingService;
    }


    /**
     * This method reads the incoming xml content and convert the xml into  OLESerialReceivingDocuments object
     *
     * @param serialReceivingXMLContent
     * @return oleSerialReceivingDocuments
     * @throws Exception
     */
    public OLESerialReceivingDocuments getOleSerialReceivingRecordList(String serialReceivingXMLContent) throws Exception {
        LOG.debug("Start of getOleSerialReceivingRecordList ");
        XStream xStream = new XStream();
        xStream.omitField(FinancialSystemDocumentHeader.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "documentNumber");
        xStream.omitField(DocumentBase.class, "pessimisticLocks");
        xStream.omitField(DocumentBase.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "adHocRoutePersons");
        xStream.omitField(DocumentBase.class, "adHocRouteWorkgroups");
        xStream.omitField(DocumentBase.class, "notes");
        xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
        xStream.alias("serialReceivingRecord", OLESerialReceivingDocuments.class);
        xStream.aliasField("serialReceivingDocuments", OLESerialReceivingDocuments.class, "oleSerialReceivingDocuments");
        xStream.alias("serialReceivingDocument", OLESerialReceivingDocument.class);
        xStream.aliasField("serialReceivingRecordHistories", OLESerialReceivingDocument.class, "oleSerialReceivingHistoryList");
        xStream.aliasField("serialReceivingTypes", OLESerialReceivingDocument.class, "oleSerialReceivingTypes");
        xStream.alias("serialReceivingRecordHistory", OLESerialReceivingHistory.class);
        xStream.alias("serialReceivingType", OLESerialReceivingType.class);
        OLESerialReceivingDocuments oleSerialReceivingDocuments = (OLESerialReceivingDocuments) xStream.fromXML(serialReceivingXMLContent);
        LOG.debug("End of getOleSerialReceivingRecordList ");
/*
        }*/
        return oleSerialReceivingDocuments;
    }

    /**
     * This method reads the incoming xml content and convert the xml into  OLESerialReceivingDocuments object
     *
     * @param oleSerialReceivingDocuments
     * @return oleSerialReceivingDocuments
     * @throws Exception
     */
    public String getSerialReceivingXMLContent( OLESerialReceivingDocuments oleSerialReceivingDocuments) throws Exception {
        LOG.debug("Start of getSerialReceivingXMLContent ");
        XStream xStream = new XStream();
        xStream.omitField(FinancialSystemDocumentHeader.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "documentNumber");
        xStream.omitField(DocumentBase.class, "pessimisticLocks");
        xStream.omitField(DocumentBase.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "adHocRoutePersons");
        xStream.omitField(DocumentBase.class, "adHocRouteWorkgroups");
        xStream.omitField(DocumentBase.class, "notes");
        xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
        xStream.alias("serialReceivingRecord", OLESerialReceivingDocuments.class);
        xStream.aliasField("serialReceivingDocuments", OLESerialReceivingDocuments.class, "oleSerialReceivingDocuments");
        xStream.alias("serialReceivingDocument", OLESerialReceivingDocument.class);
        xStream.aliasField("serialReceivingRecordHistories", OLESerialReceivingDocument.class, "oleSerialReceivingHistoryList");
        xStream.aliasField("serialReceivingTypes", OLESerialReceivingDocument.class, "oleSerialReceivingTypes");
        xStream.alias("serialReceivingRecordHistory", OLESerialReceivingHistory.class);
        xStream.alias("serialReceivingType", OLESerialReceivingType.class);
        String content= xStream.toXML(oleSerialReceivingDocuments);
        LOG.debug("End of getSerialReceivingXMLContent ");
/*
        }*/
        return content;
    }

    public String getSerialReceivingDocumentXMLContent(OLESerialReceivingDocument oleSerialReceivingDocument) throws Exception {
        LOG.debug("Start of getSerialReceivingDocumentXMLContent ");
        XStream xStream = new XStream();
        xStream.omitField(FinancialSystemDocumentHeader.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "documentNumber");
        xStream.omitField(DocumentBase.class, "pessimisticLocks");
        xStream.omitField(DocumentBase.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "adHocRoutePersons");
        xStream.omitField(DocumentBase.class, "adHocRouteWorkgroups");
        xStream.omitField(DocumentBase.class, "notes");
        xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
        xStream.alias("serialReceivingRecord", OLESerialReceivingDocuments.class);
        xStream.aliasField("serialReceivingDocuments", OLESerialReceivingDocuments.class, "oleSerialReceivingDocuments");
        xStream.alias("serialReceivingDocument", OLESerialReceivingDocument.class);
        xStream.aliasField("serialReceivingRecordHistories", OLESerialReceivingDocument.class, "oleSerialReceivingHistoryList");
        xStream.aliasField("serialReceivingTypes", OLESerialReceivingDocument.class, "oleSerialReceivingTypes");
        xStream.alias("serialReceivingRecordHistory", OLESerialReceivingHistory.class);
        xStream.alias("serialReceivingType", OLESerialReceivingType.class);
        String content = xStream.toXML(oleSerialReceivingDocument);
        LOG.debug("End of getSerialReceivingDocumentXMLContent ");
        return content;
    }

    /**
     *  This msthod is used to generate the  oleSerialReceivingFailureDocuments content
     * @param oleSerialReceivingFailureDocuments
     * @return content
     */
   public String getSerialReceivingFailureDocumentsXmlContent(OLESerialReceivingFailureDocuments oleSerialReceivingFailureDocuments){
       XStream xStream = new XStream();
       xStream.omitField(FinancialSystemDocumentHeader.class, "documentHeader");
       xStream.omitField(DocumentBase.class, "documentNumber");
       xStream.omitField(DocumentBase.class, "pessimisticLocks");
       xStream.omitField(DocumentBase.class, "documentHeader");
       xStream.omitField(DocumentBase.class, "adHocRoutePersons");
       xStream.omitField(DocumentBase.class, "adHocRouteWorkgroups");
       xStream.omitField(DocumentBase.class, "notes");
       xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
       xStream.omitField(OLESerialReceivingDocument.class,"oleSerialReceivingHistoryList");
       xStream.omitField(OLESerialReceivingDocument.class,"oleSerialReceivingTypes");
       xStream.alias("SerialReceivingFailureDocumentRecord", OLESerialReceivingFailureDocuments.class);
       xStream.alias("SerialReceivingFailureDocument",OLESerialReceivingFailureDocument.class);
       xStream.alias("SerialReceivingDocument",OLESerialReceivingDocument.class);
       xStream.aliasField("SerialReceivingFailureDocuments",OLESerialReceivingFailureDocuments.class,"oleSerialReceivingFailureDocuments");
       String content = xStream.toXML(oleSerialReceivingFailureDocuments);
       return  content;
   }

    /**
     *
     * @param oleSerialReceivingFailureTypes
     * @return content
     */
    public String getSerialReceivingFailureTypesXmlContent(OLESerialReceivingFailureTypes oleSerialReceivingFailureTypes){
        XStream xStream = new XStream();
        xStream.omitField(FinancialSystemDocumentHeader.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "documentNumber");
        xStream.omitField(DocumentBase.class, "pessimisticLocks");
        xStream.omitField(DocumentBase.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "adHocRoutePersons");
        xStream.omitField(DocumentBase.class, "adHocRouteWorkgroups");
        xStream.omitField(DocumentBase.class, "notes");
        xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
        xStream.alias("SerialReceivingFailureTypeRecord",OLESerialReceivingFailureTypes.class);
        xStream.alias("SerialReceivingFailureType",OLESerialReceivingFailureType.class);
        xStream.alias("SerialReceivingType",OLESerialReceivingType.class);
        xStream.aliasField("SerialReceivingFailureTypes",OLESerialReceivingFailureTypes.class,"oleSerialReceivingFailureTypes");
        xStream.omitField(OLESerialReceivingType.class,"oleSerialReceivingDocument");
        String content = xStream.toXML(oleSerialReceivingFailureTypes);
        return content;
    }

    public String getSerialReceivingFailureHistoriesXmlContent(OLESerialReceivingFailureHistories oleSerialReceivingFailureHistories){
        XStream xStream = new XStream();
        xStream.omitField(FinancialSystemDocumentHeader.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "documentNumber");
        xStream.omitField(DocumentBase.class, "pessimisticLocks");
        xStream.omitField(DocumentBase.class, "documentHeader");
        xStream.omitField(DocumentBase.class, "adHocRoutePersons");
        xStream.omitField(DocumentBase.class, "adHocRouteWorkgroups");
        xStream.omitField(DocumentBase.class, "notes");
        xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
        xStream.alias("SerialReceivingFailureHistoryRecord",OLESerialReceivingFailureHistories.class);
        xStream.alias("SerialReceivingFailureHistory",OLESerialReceivingFailureHistory.class);
        xStream.alias("SerialReceivingHistory",OLESerialReceivingHistory.class);
        xStream.aliasField("SerialReceivingFailureHistories",OLESerialReceivingFailureHistories.class,"oleSerialReceivingFailureHistories");
        xStream.omitField(OLESerialReceivingHistory.class,"oleSerialReceivingDocument");
        String content = xStream.toXML(oleSerialReceivingFailureHistories);
        return content;
    }

    /**
     * This method is used to check whether the subscription status send is  valid one or not
     *
     * @param subscriptionStatus
     * @return validStatus
     */
    public boolean isValidSubscriptionStatus(String subscriptionStatus) {
        if (LOG.isDebugEnabled()){
            LOG.debug("Start of isValidSubscriptionStatus for the status : "+subscriptionStatus);
        }
        boolean validStatus = false;
        Map<String, String> subscriptionStatusMap = new HashMap<String, String>();
        subscriptionStatusMap.put("receiptStatusCode", subscriptionStatus);
        List<OleReceiptStatus> oleReceiptStatuses = (List<OleReceiptStatus>) businessObjectService.findMatching(OleReceiptStatus.class, subscriptionStatusMap);
        if (oleReceiptStatuses.size() > 0) {
            validStatus = true;
        }
        if (LOG.isDebugEnabled()){
            LOG.debug("End of isValidSubscriptionStatus for the status : "+subscriptionStatus + ":" + validStatus);
        }
        return validStatus;
    }

    /**
     * This method checks the bibId instanceId and Po Id are interlinked
     * @param poId
     * @param bibId
     * @param instanceId
     * @return
     */
    public boolean isValidPO(String poId, String bibId, String instanceId) {
        if (LOG.isDebugEnabled()){
            LOG.debug("Start of isValidPO : "+"poId : "+ poId+";bibId :" +bibId +";instanceId"+instanceId);
        }
        boolean validPo = true;
        boolean validOrder = false;
        if (poId != null && !poId.trim().isEmpty()) {
            Map<String, String> poMap = new HashMap<String, String>();
            poMap.put("itemTitleId", bibId);
            List<OlePurchaseOrderItem> olePurchaseOrderItems = (List<OlePurchaseOrderItem>) businessObjectService.findMatching(OlePurchaseOrderItem.class, poMap);
            if (olePurchaseOrderItems.size() == 0) {
                if (LOG.isDebugEnabled()){
                    LOG.debug("There is no purchase order document for the poId :"+poId);
                }
                validPo = false;
            } else {
                for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
                     if(olePurchaseOrderItem!=null && olePurchaseOrderItem.getPurchaseOrder()!=null && olePurchaseOrderItem.getPurchaseOrder().getPurapDocumentIdentifier()!=null &&  String.valueOf(olePurchaseOrderItem.getPurchaseOrder().getPurapDocumentIdentifier()).equals(poId)){
                            Integer purapItemId = olePurchaseOrderItem.getItemIdentifier();
                         Map<String,String> copyMap = new HashMap<String,String>();
                         copyMap.put("poItemId",String.valueOf(purapItemId));
                         copyMap.put("bibId",bibId);
                         copyMap.put("instanceId",instanceId);
                         Log.info("Finding the entry for the item id :"+purapItemId +" ;bibId : "+bibId +" ; instanceId : "+ instanceId +"in the copy table" );
                         List<OleCopy> oleCopies = (List<OleCopy>)businessObjectService.findMatching(OleCopy.class,copyMap);
                         if(oleCopies.size()>0){
                             validOrder = true;
                         }
                }

                }
            }
        } else{
            validOrder=true;
        }
        return validPo && validOrder;

    }


    public boolean validBibAndInstance(String bibId,String instanceId){
        if (LOG.isDebugEnabled()){
            LOG.debug("Inside validBibAndInstance for the bibId :"+bibId +":Instance Id :"+instanceId);
        }
        boolean validBibInstance = false;
        List<SearchCondition> searchConditions = new ArrayList<>();
        SearchParams searchParams = new SearchParams();
        searchConditions.add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("holdings", "bibIdentifier", bibId), "AND"));
        searchConditions.add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("holdings", "id", instanceId), "AND"));
        SearchResponse search = null;
        searchParams.getSearchConditions().addAll(searchConditions);
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings","itemIdentifier"));
        try {
            search = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (search.getTotalRecordCount() > 0) {
            if (LOG.isDebugEnabled()){
                LOG.debug("There is  matching bibId for this instance id "+"BibId : "+bibId +";InstanceId : "+instanceId);
            }
            validBibInstance = true;
        }
        if (LOG.isDebugEnabled()){
            LOG.debug("isValidBid : " +validBibInstance);
        }
        return validBibInstance;

    }

    /**
     * This method is used to check whether the record type is valid one or not
     * @param recordType
     * @return
     */
    private boolean isValidSerialReceivingRecordType(String recordType){
            boolean valid = false;
        if(recordType!=null && !recordType.trim().isEmpty()){
             if(recordTypeList.contains(recordType)){
                 valid = true;
             }
        }
        return valid;
    }

    /**
     * This method is used to check whether the generated serial receiving document contains the required fields
     * @param oleSerialReceivingDocument
     * @return  validDocument
     */
    public boolean validateSerialReceivingDocument(OLESerialReceivingDocument oleSerialReceivingDocument) {
        if (LOG.isDebugEnabled()){
            LOG.debug("Inside the validateSerialReceivingDocument : documentId : "+oleSerialReceivingDocument.getSerialReceivingRecordId());
        }
        boolean validDocument = false;
        if(!isSerialRecordAlreadyExist(oleSerialReceivingDocument)){
        if (isValidSerialReceivingRecordType(oleSerialReceivingDocument.getReceivingRecordType())) {
            if (oleSerialReceivingDocument.getSerialReceiptLocation() != null) {
                if (isValidSubscriptionStatus(oleSerialReceivingDocument.getSubscriptionStatus())) {
                    if (oleSerialReceivingDocument.getPoId() != null && !oleSerialReceivingDocument.getPoId().trim().isEmpty()) {
                        if (isValidPO(oleSerialReceivingDocument.getPoId(), oleSerialReceivingDocument.getBibId(), oleSerialReceivingDocument.getInstanceId())) {
                            validDocument = true;
                        } else{
                            oleSerialReceivingDocument.setValidPo(false);
                            validDocument = false;
                        }
                    } else if(oleSerialReceivingDocument.getPoId() == null || (oleSerialReceivingDocument.getPoId()!=null && oleSerialReceivingDocument.getPoId().trim().isEmpty())){
                             if(validBibAndInstance(oleSerialReceivingDocument.getBibId(),oleSerialReceivingDocument.getInstanceId())){
                                 validDocument = true;
                             } else{
                                 oleSerialReceivingDocument.setValidBibAndInstance(false);
                                 validDocument=false;
                             }
                    } if(oleSerialReceivingDocument.getOleSerialReceivingTypes()!=null && oleSerialReceivingDocument.getOleSerialReceivingTypes().size()>0){
                        for(OLESerialReceivingType oleSerialReceivingRecordType : oleSerialReceivingDocument.getOleSerialReceivingTypes()){
                            if(!isValidSerialReceivingRecordType(oleSerialReceivingRecordType.getReceivingRecordType())){
                             oleSerialReceivingDocument.setValidChildRecordType(false);
                             oleSerialReceivingRecordType.setValidRecordType(false);
                                validDocument=false;
                            }
                        }
                    } if(oleSerialReceivingDocument.getOleSerialReceivingHistoryList()!=null && oleSerialReceivingDocument.getOleSerialReceivingHistoryList().size()>0){
                        for(OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()){
                            if(!isValidSerialReceivingRecordType(oleSerialReceivingHistory.getReceivingRecordType())){
                                oleSerialReceivingDocument.setValidChildHistoryRecordType(false);
                                oleSerialReceivingHistory.setValidRecordType(false);
                                validDocument = false;
                            }
                        }
                    }
                } else{
                    oleSerialReceivingDocument.setValidSubscriptionStatus(false);
                    validDocument =false;
                }
        }else{
                oleSerialReceivingDocument.setAvailableSerialReceiptLocation(false);
                validDocument = false;
            }
        }
        else{
            oleSerialReceivingDocument.setValidRecordType(false);
            validDocument = false;
        }
        }else{
            oleSerialReceivingDocument.setRecordAlreadyExist(true);
            validDocument = false;
        }
        if (LOG.isDebugEnabled()){
            LOG.debug("Is validateSerialReceivingDocument : "+validDocument);
        }
        return validDocument;
    }


    /**
     *  This method is used to check whether there is a serial receiving document for the given combination of bib and instance
     * @param oleSerialReceivingDocument
     * @return  isSerialRecordAlreadyExist
     */
    public boolean isSerialRecordAlreadyExist(OLESerialReceivingDocument oleSerialReceivingDocument){
        Map<String,String> serialMap  = new HashMap<String,String>();
        serialMap.put("bibId",oleSerialReceivingDocument.getBibId());
        serialMap.put("instanceId",oleSerialReceivingDocument.getInstanceId());
        List<OLESerialReceivingDocument> oleSerialReceivingDocumentList = (List<OLESerialReceivingDocument>)businessObjectService.findMatching(OLESerialReceivingDocument.class,serialMap);
         if(oleSerialReceivingDocumentList.size()>0){
             return true;
         }               else {
             return false;
         }
    }

    /**
     *  This method creates the actual serial receiving document with the given input values
     * @param oleSerialReceivingDocument
     * @return  OLESerialReceivingDocument
     */
    public OLESerialReceivingDocument generateSerialReceivingDocument(OLESerialReceivingDocument oleSerialReceivingDocument) {
        if (LOG.isDebugEnabled()){
            LOG.debug("Inside the generateSerialReceivingDocument : documentId : "+oleSerialReceivingDocument.getSerialReceivingRecordId());
        }
        if(validateSerialReceivingDocument(oleSerialReceivingDocument)){
            try {
                OLESerialReceivingDocument newDocument = (OLESerialReceivingDocument) documentService.getNewDocument("OLE_SER_RECV_REC");
                newDocument.getDocumentHeader().setDocumentDescription(OLEConstants.SERIAL_REC_DESC);
                oleSerialReceivingService.createNewWithExisting(newDocument, oleSerialReceivingDocument);
                return (OLESerialReceivingDocument) documentService.saveDocument(newDocument);
            } catch (Exception e) {
                LOG.error(e, e);
            }
        }
        return null;
    }


    /**
     * This method returns the result of the loading process
     * @param xmlContent
     * @return OLESerialReceivingRecordSummary
     */
    public OLESerialReceivingRecordSummary createOLESerialReceivingDocumentFromXml(String xmlContent) {
        LOG.debug("Inside createOLESerialReceivingDocumentFromXml");
        int totalRecordSize=0;
        int successfulRecordSize=0;
        int failureRecordSize=0;
        List<OLESerialReceivingDocument> successList = new ArrayList<OLESerialReceivingDocument>();
        List<OLESerialReceivingDocument> failureList = new ArrayList<OLESerialReceivingDocument>();
        OLESerialReceivingDocuments oleSerialReceivingDocuments = null;
        try {
            oleSerialReceivingDocuments = getOleSerialReceivingRecordList(xmlContent);
            totalRecordSize = oleSerialReceivingDocuments.getOleSerialReceivingDocuments().size();
            for (OLESerialReceivingDocument oleSerialReceivingDocument : oleSerialReceivingDocuments.getOleSerialReceivingDocuments()) {
                OLESerialReceivingDocument oleSerialReceivingDocument1 = generateSerialReceivingDocument(oleSerialReceivingDocument);
                if (oleSerialReceivingDocument1 != null) {
                    successList.add(oleSerialReceivingDocument1);
                    successfulRecordSize = successfulRecordSize+1;
                } else{
                    failureList.add(oleSerialReceivingDocument);
                    failureRecordSize = failureRecordSize+1;
                }
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        if (LOG.isDebugEnabled()){
            LOG.debug("End of createOLESerialReceivingDocumentFromXml : total Number Of Successful Records = "+successList.size() +": Total number Of Failure Records : "+failureRecordSize);
        }
        return new OLESerialReceivingRecordSummary(totalRecordSize,successfulRecordSize,failureRecordSize,failureList);
    }


    public OLESerialReceivingRecordSummary createOLESerialReceivingDocumentFromCsv(String serialReceivingDocument,String serialReceivingType ,String serialReceivingHistory) {
        LOG.debug("Inside createOLESerialReceivingDocumentFromCsv ");
        int totalRecordSize = 0;
        int docSuccessCount = 0;
        int docFailureCount = 0;
        List<OLESerialReceivingDocument> docFailureList = new ArrayList<>();
        int hstrySucceesCount = 0;
        int hstryFailureCount = 0;
        List<OLESerialReceivingHistory> hstryFailureList = new ArrayList<>();
        int typeSuccessCount = 0;
        int typeFailureCount = 0;
        List<OLESerialReceivingType> typeFailureList = new ArrayList<>();
        List<OLESerialReceivingDocument> oleSerialReceivingDocumentList = new ArrayList<>();
        Map<String, List<OLESerialReceivingType>> oleSerialReceivingTypeMap = new HashMap<>();
        Map<String, List<OLESerialReceivingHistory>> oleSerialReceivingHistoryMap = new HashMap<>();
        List<OLESerialReceivingDocument> serialReceivingDocumentList = new ArrayList<>();
        List<String> serialIdList = new ArrayList<>();
        try {
            if (serialReceivingDocument != null && !serialReceivingDocument.trim().isEmpty()) {
                oleSerialReceivingDocumentList = processSerialReceivingDocument(serialReceivingDocument);
                if (serialReceivingType != null && !serialReceivingType.trim().isEmpty()) {
                    oleSerialReceivingTypeMap = processSerialReceivingType(serialReceivingType);
                }
                if (serialReceivingHistory != null && !serialReceivingHistory.trim().isEmpty()) {
                    oleSerialReceivingHistoryMap = processSerialReceivingHistory(serialReceivingHistory);
                }
                for (OLESerialReceivingDocument oleSerialReceivingDocument : oleSerialReceivingDocumentList) {
                    oleSerialReceivingDocument.setOleSerialReceivingTypes(oleSerialReceivingTypeMap.get(oleSerialReceivingDocument.getSerialReceivingRecordId()));
                    oleSerialReceivingDocument.setOleSerialReceivingHistoryList(oleSerialReceivingHistoryMap.get(oleSerialReceivingDocument.getSerialReceivingRecordId()));
                    serialReceivingDocumentList.add(oleSerialReceivingDocument);
                }
                for (OLESerialReceivingDocument oleSerialReceivingDocument : serialReceivingDocumentList) {
                    OLESerialReceivingDocument oleSerialReceivingDocument1 = generateSerialReceivingDocument(oleSerialReceivingDocument);
                    if (oleSerialReceivingDocument1 != null) {
                        docSuccessCount = docSuccessCount + 1;
                        if (oleSerialReceivingDocument.getOleSerialReceivingHistoryList() != null)
                            hstrySucceesCount = hstrySucceesCount + oleSerialReceivingDocument1.getOleSerialReceivingHistoryList().size();
                        if (oleSerialReceivingDocument.getOleSerialReceivingTypes() != null)
                            typeSuccessCount = typeSuccessCount + oleSerialReceivingDocument1.getOleSerialReceivingTypes().size();
                    } else {
                        if (oleSerialReceivingDocument.getOleSerialReceivingHistoryList() != null && oleSerialReceivingDocument.getOleSerialReceivingHistoryList().size()>0){
                            hstryFailureCount = hstryFailureCount + oleSerialReceivingDocument.getOleSerialReceivingHistoryList().size();
                            for(OLESerialReceivingHistory oleSerialReceivingHistory :oleSerialReceivingDocument.getOleSerialReceivingHistoryList()){
                                oleSerialReceivingHistory.setDocumentExist(true);
                            }
                            hstryFailureList.addAll(oleSerialReceivingDocument.getOleSerialReceivingHistoryList());
                        }
                        if (oleSerialReceivingDocument.getOleSerialReceivingTypes() != null && oleSerialReceivingDocument.getOleSerialReceivingTypes().size()>0) {
                            typeFailureCount = typeFailureCount + oleSerialReceivingDocument.getOleSerialReceivingTypes().size();
                            for(OLESerialReceivingType oleSerialReceivingType : oleSerialReceivingDocument.getOleSerialReceivingTypes()){
                                oleSerialReceivingType.setDocumentExist(true);
                            }
                            typeFailureList.addAll(oleSerialReceivingDocument.getOleSerialReceivingTypes());
                        }
                        oleSerialReceivingDocument.setOleSerialReceivingTypes(null);
                        oleSerialReceivingDocument.setOleSerialReceivingHistoryList(null);
                        docFailureList.add(oleSerialReceivingDocument);
                        docFailureCount = docFailureCount + 1;
                    }
                    serialIdList.add(oleSerialReceivingDocument.getSerialReceivingRecordId());

                }
                for (String serialReceivingRecordId : oleSerialReceivingHistoryMap.keySet()) {
                    if (!serialIdList.contains(serialReceivingRecordId)) {
                        List<OLESerialReceivingHistory> oleSerialReceivingHistoryList = oleSerialReceivingHistoryMap.get(serialReceivingRecordId);
                        if (oleSerialReceivingHistoryList != null) {
                            hstryFailureCount = hstryFailureCount+oleSerialReceivingHistoryList.size();
                            hstryFailureList.addAll(oleSerialReceivingHistoryList);
                        }
                    }
                }
                for (String serialReceivingRecordId : oleSerialReceivingTypeMap.keySet()) {
                    if (!serialIdList.contains(serialReceivingRecordId)) {
                        List<OLESerialReceivingType> oleSerialReceivingTypeList = oleSerialReceivingTypeMap.get(serialReceivingRecordId);
                        if (oleSerialReceivingTypeList != null) {
                            typeFailureCount = typeFailureCount + oleSerialReceivingTypeList.size();
                            typeFailureList.addAll(oleSerialReceivingTypeList);
                        }
                    }
                }
                totalRecordSize = oleSerialReceivingDocumentList.size();
            } else {
                if (serialReceivingHistory != null && !serialReceivingHistory.trim().isEmpty()) {
                    oleSerialReceivingHistoryMap = processSerialReceivingHistory(serialReceivingHistory);
                    for (String serialReceivingRecordId : oleSerialReceivingHistoryMap.keySet()) {
                        List<OLESerialReceivingHistory> oleSerialReceivingHistoryList = oleSerialReceivingHistoryMap.get(serialReceivingRecordId);
                        if (oleSerialReceivingHistoryList != null) {
                            for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingHistoryList) {
                                if (oleSerialReceivingHistory.getSerialReceivingRecordId() != null) {
                                    Map oleSerialReceivingRecordIdMap = new HashMap();
                                    oleSerialReceivingRecordIdMap.put("serialReceivingRecordId", oleSerialReceivingHistory.getSerialReceivingRecordId());
                                    OLESerialReceivingDocument document = businessObjectService.findByPrimaryKey(OLESerialReceivingDocument.class, oleSerialReceivingRecordIdMap);
                                    if (document != null) {
                                        if (document.getOleSerialReceivingHistoryList() != null) {
                                            document.getOleSerialReceivingHistoryList().add(oleSerialReceivingHistory);
                                        } else {
                                            List<OLESerialReceivingHistory> oleSerialReceivingHistories = new ArrayList<>();
                                            oleSerialReceivingHistories.add(oleSerialReceivingHistory);
                                            document.setOleSerialReceivingHistoryList(oleSerialReceivingHistories);
                                        }
                                        hstrySucceesCount = hstrySucceesCount + 1;
                                        getBusinessObjectService().save(document);
                                    } else {
                                        hstryFailureCount = hstryFailureCount + 1;
                                        hstryFailureList.add(oleSerialReceivingHistory);
                                    }
                                }
                            }
                        }
                    }
                }
                if (serialReceivingType != null && !serialReceivingType.trim().isEmpty()) {
                    oleSerialReceivingTypeMap = processSerialReceivingType(serialReceivingType);
                    for (String serialReceivingRecordId : oleSerialReceivingTypeMap.keySet()) {
                        List<OLESerialReceivingType> oleSerialReceivingTypeList = oleSerialReceivingTypeMap.get(serialReceivingRecordId);
                        if (oleSerialReceivingTypeList != null) {
                            for (OLESerialReceivingType oleSerialReceivingType : oleSerialReceivingTypeList) {
                                if (oleSerialReceivingType.getSerialReceivingRecordId() != null) {
                                    Map oleSerialReceivingRecordIdMap = new HashMap();
                                    oleSerialReceivingRecordIdMap.put("serialReceivingRecordId", oleSerialReceivingType.getSerialReceivingRecordId());
                                    OLESerialReceivingDocument document = businessObjectService.findByPrimaryKey(OLESerialReceivingDocument.class, oleSerialReceivingRecordIdMap);
                                    if (document != null) {
                                        if (document.getOleSerialReceivingTypes() != null) {
                                            document.getOleSerialReceivingTypes().add(oleSerialReceivingType);
                                        } else {
                                            List<OLESerialReceivingType> oleSerialReceivingTypes = new ArrayList<>();
                                            oleSerialReceivingTypes.add(oleSerialReceivingType);
                                            document.setOleSerialReceivingTypes(oleSerialReceivingTypes);
                                        }
                                        typeSuccessCount = typeSuccessCount + 1;
                                        getBusinessObjectService().save(document);
                                    } else {
                                        typeFailureCount = typeFailureCount + 1;
                                        typeFailureList.add(oleSerialReceivingType);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOG.error(e, e);
        }
        if (LOG.isDebugEnabled()){
            LOG.debug("totalRecordSize : "+totalRecordSize +","+"docSuccessCount : "+docSuccessCount +","+"docFailureCount : "+docFailureCount +","+"hstrySucceesCount : "+hstrySucceesCount +","+"hstryFailureCount : "+hstryFailureCount +","+"typeSuccessCount : "+typeSuccessCount +","+"typeFailureCount : "+typeFailureCount);
        }
        return new OLESerialReceivingRecordSummary(totalRecordSize, docSuccessCount, docFailureCount, docFailureList,hstrySucceesCount,hstryFailureCount,hstryFailureList,typeSuccessCount,typeFailureCount,typeFailureList);
    }





    public List<OLESerialReceivingDocument> processSerialReceivingDocument(String serialReceivingDocumentContent) {
        Log.debug("inside processing Serial Receiving Document");
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = getParameter(OLEConstants.SERIAL_LOADER_DELIMITER,OLEConstants.SELECT_NMSPC,OLEConstants.SELECT_CMPNT);
        if(cvsSplitBy != null && cvsSplitBy.trim().isEmpty()){
            cvsSplitBy = ",";
        }

        List<OLESerialReceivingDocument> oleSerialReceivingDocumentList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(serialReceivingDocumentContent));
            String[] serialDocuments = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] documents = line.split(cvsSplitBy);
                OLESerialReceivingDocument oleSerialReceivingDocument = new OLESerialReceivingDocument();
                if (count == 0) {
                    serialDocuments = documents;
                }
                int index = 0;
                if (count > 0) {
                    for (String document : documents) {
                        document=document.replace("\"","");
                        if (serialDocuments.length > index && serialDocuments[index]!=null) {
                            if (serialDocuments!=null && serialDocuments[index].equals("\"FDOC_NBR\"")) {
                                index++;
                                oleSerialReceivingDocument.getDocumentHeader().setDocumentNumber(document);
                            } else if (serialDocuments[index].equals("\"SER_RCV_REC_ID\"")) {
                                index++;
                                oleSerialReceivingDocument.setSerialReceivingRecordId(document);
                            } else if (serialDocuments[index].equals("\"BIB_ID\"")) {
                                index++;
                                oleSerialReceivingDocument.setBibId(document);
                            } else if (serialDocuments[index].equals("\"BOUND_LOC\"")) {
                                index++;
                                oleSerialReceivingDocument.setBoundLocation(document);
                            } else if (serialDocuments[index].equals("\"CALL_NUM\"")) {
                                index++;
                                oleSerialReceivingDocument.setCallNumber(document);
                            } else if (serialDocuments[index].equals("\"RCV_REC_TYP\"")) {
                                index++;
                                oleSerialReceivingDocument.setReceivingRecordType(document);
                            } else if (serialDocuments[index].equals("\"CLAIM\"")) {
                                index++;
                                if (document.equals("Y")) {
                                    oleSerialReceivingDocument.setClaim(true);
                                } else {
                                    oleSerialReceivingDocument.setClaim(false);
                                }
                            } else if (serialDocuments[index].equals("\"CLAIM_INTRVL_INFO\"")) {
                                index++;
                                oleSerialReceivingDocument.setClaimIntervalInformation(document);
                            } else if (serialDocuments[index].equals("\"COPY_NUM\"")) {
                                index++;
                                oleSerialReceivingDocument.setCopyNumber(document);
                            } else if (serialDocuments[index].equals("\"CORPORATE_AUTH\"")) {
                                index++;
                                oleSerialReceivingDocument.setCorporateAuthor(document);
                            } else if (serialDocuments[index].equals("\"CREATE_ITEM\"")) {
                                index++;
                                if (document.equals("Y")) {
                                    oleSerialReceivingDocument.setCreateItem(true);
                                } else {
                                    oleSerialReceivingDocument.setCreateItem(false);
                                }
                            } else if (serialDocuments[index].equals("\"GEN_RCV_NOTE\"")) {
                                index++;
                                oleSerialReceivingDocument.setGeneralReceivingNote(document);
                            } else if (serialDocuments[index].equals("\"INSTANCE_ID\"")) {
                                index++;
                                oleSerialReceivingDocument.setInstanceId(document);
                            } else if (serialDocuments[index].equals("\"ISSN\"")) {
                                index++;
                                oleSerialReceivingDocument.setIssn(document);
                            } else if (serialDocuments[index].equals("\"PO_ID\"")) {
                                index++;
                                oleSerialReceivingDocument.setPoId(document);
                            } else if (serialDocuments[index].equals("\"PRINT_LBL\"")) {
                                index++;
                                if (document.equals("Y")) {
                                    oleSerialReceivingDocument.setPrintLabel(true);
                                } else {
                                    oleSerialReceivingDocument.setPrintLabel(false);
                                }
                            } else if (serialDocuments[index].equals("\"PUBLIC_DISPLAY\"")) {
                                index++;
                                if (document.equals("Y")) {
                                    oleSerialReceivingDocument.setPublicDisplay(true);
                                } else {
                                    oleSerialReceivingDocument.setPublicDisplay(false);
                                }
                            } else if (serialDocuments[index].equals("\"PUBLISHER\"")) {
                                index++;
                                oleSerialReceivingDocument.setPublisher(document);
                            } else if (serialDocuments[index].equals("\"SER_RCPT_LOC\"")) {
                                index++;
                                oleSerialReceivingDocument.setSerialReceiptLocation(document);
                            } else if (serialDocuments[index].equals("\"SER_RCV_REC\"")) {
                                index++;
                                oleSerialReceivingDocument.setSerialReceiptLocation(document);
                            } else if (serialDocuments[index].equals("\"SUBSCR_STAT\"")) {
                                index++;
                                oleSerialReceivingDocument.setSubscriptionStatus(document);
                            } else if (serialDocuments[index].equals("\"TREATMENT_INSTR_NOTE\"")) {
                                index++;
                                oleSerialReceivingDocument.setTreatmentInstructionNote(document);
                            } else if (serialDocuments[index].equals("\"UNBOUND_LOC\"")) {
                                index++;
                                oleSerialReceivingDocument.setUnboundLocation(document);
                            } else if (serialDocuments[index].equals("\"URGENT_NOTE\"")) {
                                index++;
                                oleSerialReceivingDocument.setUrgentNote(document);
                            } else if (serialDocuments[index].equals("\"VENDOR\"")) {
                                index++;
                                oleSerialReceivingDocument.setVendorId(document);
                            } else if (serialDocuments[index].equals("\"CREATE_DATE\"")) {
                                index++;
                                //oleSerialReceivingDocument.setCreateDate(Timestamp.valueOf(document));
                            } else if (serialDocuments[index].equals("\"OPTR_ID\"")) {
                                index++;
                                oleSerialReceivingDocument.setOperatorId(document);
                            } else if (serialDocuments[index].equals("\"MACH_ID\"")) {
                                index++;
                                oleSerialReceivingDocument.setMachineId(document);
                            } else if (serialDocuments[index].equals("\"SUBSCR_STAT_DT\"")) {
                                index++;
                                //oleSerialReceivingDocument.setSubscriptionStatusDate(Timestamp.valueOf(document));
                            } else if (serialDocuments[index].equals("\"SR_TITLE\"")) {
                                index++;
                                oleSerialReceivingDocument.setTitle(document);
                            } else if (serialDocuments[index].equals("\"OBJ_ID\"")) {
                                index++;
                                oleSerialReceivingDocument.setObjectId(document);
                            } else if (serialDocuments[index].equals("\"VER_NBR\"")) {
                                index++;
                                //oleSerialReceivingDocument.setVersionNumber(Long.getLong(document));
                            } else if (serialDocuments[index].equals("\"ACTIVE\"")) {
                                index++;
                                if (document.equals("Y")) {
                                    oleSerialReceivingDocument.setActive(true);
                                } else {
                                    oleSerialReceivingDocument.setActive(false);
                                }
                            }
                            else {
                                index++;
                            }
                        }
                    }
                    oleSerialReceivingDocumentList.add(oleSerialReceivingDocument);
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
        return oleSerialReceivingDocumentList;
    }


    public Map<String, List<OLESerialReceivingHistory>> processSerialReceivingHistory(String serialReceivingHistoryContent) {
        Map<String, List<OLESerialReceivingHistory>> oleSerialReceivingHistoryMap = new HashMap<>();

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = getParameter(OLEConstants.SERIAL_LOADER_DELIMITER,OLEConstants.SELECT_NMSPC,OLEConstants.SELECT_CMPNT);
        if(cvsSplitBy != null && cvsSplitBy.trim().isEmpty()){
            cvsSplitBy = ",";
        }
        try {
            br = new BufferedReader(new FileReader(serialReceivingHistoryContent));
            String[] serialHistories = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] histories = line.split(cvsSplitBy);
                OLESerialReceivingHistory oleSerialReceivingHistory = new OLESerialReceivingHistory();
                if (count == 0) {
                    serialHistories = histories;
                }
                int index = 0;
                if (count > 0) {
                    for (String history : histories) {
                        history=history.replace("\"","");
                        if (serialHistories!=null && serialHistories.length > index && serialHistories[index]!=null) {
                            if (serialHistories[index].equals("\"SER_RCPT_HIS_REC_ID\"")) {
                                index++;
                                oleSerialReceivingHistory.setSerialReceivingRecordHistoryId(history);
                            } else if (serialHistories[index].equals("\"SER_RCV_REC_ID\"")) {
                                index++;
                                oleSerialReceivingHistory.setSerialReceivingRecordId(history);
                            } else if (serialHistories[index].equals("\"RCV_REC_TYP\"")) {
                                index++;
                                oleSerialReceivingHistory.setReceivingRecordType(history);
                            } else if (serialHistories[index].equals("\"CHRON_LVL_1\"")) {
                                index++;
                                oleSerialReceivingHistory.setChronologyCaptionLevel1(history);
                            } else if (serialHistories[index].equals("\"CHRON_LVL_2\"")) {
                                index++;
                                oleSerialReceivingHistory.setChronologyCaptionLevel2(history);
                            } else if (serialHistories[index].equals("\"CHRON_LVL_3\"")) {
                                index++;
                                oleSerialReceivingHistory.setChronologyCaptionLevel3(history);
                            } else if (serialHistories[index].equals("\"CHRON_LVL_4\"")) {
                                index++;
                                oleSerialReceivingHistory.setChronologyCaptionLevel4(history);
                            } else if (serialHistories[index].equals("\"CLAIM_COUNT\"")) {
                                index++;
                                oleSerialReceivingHistory.setClaimCount(history);
                            } else if (serialHistories[index].equals("\"CLAIM_DATE\"")) {
                                index++;
                                //oleSerialReceivingHistory.setClaimDate(history);
                            } else if (serialHistories[index].equals("\"CLAIM_NOTE\"")) {
                                index++;
                                oleSerialReceivingHistory.setClaimNote(history);
                            } else if (serialHistories[index].equals("\"CLAIM_TYPE\"")) {
                                index++;
                                oleSerialReceivingHistory.setClaimType(history);
                            } else if (serialHistories[index].equals("\"CLAIM_RESP\"")) {
                                index++;
                                oleSerialReceivingHistory.setClaimResponse(history);
                            } else if (serialHistories[index].equals("\"ENUM_LVL_1\"")) {
                                index++;
                                oleSerialReceivingHistory.setEnumerationCaptionLevel1(history);
                            } else if (serialHistories[index].equals("\"ENUM_LVL_2\"")) {
                                index++;
                                oleSerialReceivingHistory.setEnumerationCaptionLevel2(history);
                            } else if (serialHistories[index].equals("\"ENUM_LVL_3\"")) {
                                index++;
                                oleSerialReceivingHistory.setEnumerationCaptionLevel3(history);
                            } else if (serialHistories[index].equals("\"ENUM_LVL_4\"")) {
                                index++;
                                oleSerialReceivingHistory.setEnumerationCaptionLevel4(history);
                            } else if (serialHistories[index].equals("\"ENUM_LVL_5\"")) {
                                index++;
                                oleSerialReceivingHistory.setEnumerationCaptionLevel5(history);
                            } else if (serialHistories[index].equals("\"ENUM_LVL_6\"")) {
                                index++;
                                oleSerialReceivingHistory.setEnumerationCaptionLevel6(history);
                            } else if (serialHistories[index].equals("\"PUB_DISPLAY\"")) {
                                index++;
                                if (history.equals("Y")) {
                                    oleSerialReceivingHistory.setPublicDisplay(true);
                                } else {
                                    oleSerialReceivingHistory.setPublicDisplay(false);
                                }
                            } else if (serialHistories[index].equals("\"SER_RCPT_NOTE\"")) {
                                index++;
                                oleSerialReceivingHistory.setSerialReceiptNote(history);
                            } else if (serialHistories[index].equals("\"OPTR_ID\"")) {
                                index++;
                                oleSerialReceivingHistory.setOperatorId(history);
                            } else if (serialHistories[index].equals("\"MACH_ID\"")) {
                                index++;
                                oleSerialReceivingHistory.setMachineId(history);
                            } else if (serialHistories[index].equals("\"RCPT_STAT\"")) {
                                index++;
                                oleSerialReceivingHistory.setReceiptStatus(history);
                            } else if (serialHistories[index].equals("\"RCPT_DATE\"")) {
                                index++;
                                //oleSerialReceivingHistory.setReceiptDate(history);
                            } else if (serialHistories[index].equals("\"PUB_RCPT\"")) {
                                index++;
                                oleSerialReceivingHistory.setPublicReceipt(history);
                            } else if (serialHistories[index].equals("\"STAFF_ONLY_RCPT\"")) {
                                index++;
                                oleSerialReceivingHistory.setStaffOnlyReceipt(history);
                            } else if (serialHistories[index].equals("\"OBJ_ID\"")) {
                                index++;
                                oleSerialReceivingHistory.setObjectId(history);
                            } else if (serialHistories[index].equals("\"VER_NBR\"")) {
                                index++;
                                //oleSerialReceivingHistory.setVersionNumber(Long.getLong(history));
                            }
                            else {
                                index++;
                            }
                        }
                    }
                    boolean keyFound = false;
                    for (String serialReceivingRecordId : oleSerialReceivingHistoryMap.keySet()) {
                        if (oleSerialReceivingHistory!=null && oleSerialReceivingHistory.getSerialReceivingRecordId()!=null && oleSerialReceivingHistory.getSerialReceivingRecordId().equals(serialReceivingRecordId)) {
                            keyFound = true;
                            List<OLESerialReceivingHistory> oleSerialReceivingTypeList = oleSerialReceivingHistoryMap.get(serialReceivingRecordId);
                            oleSerialReceivingTypeList.add(oleSerialReceivingHistory);
                        }
                    }
                    if (!keyFound) {
                        List<OLESerialReceivingHistory> oleSerialReceivingTypeList = new ArrayList<>();
                        oleSerialReceivingTypeList.add(oleSerialReceivingHistory);
                        oleSerialReceivingHistoryMap.put(oleSerialReceivingHistory.getSerialReceivingRecordId(), oleSerialReceivingTypeList);
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
        return oleSerialReceivingHistoryMap;
    }


    public Map<String, List<OLESerialReceivingType>> processSerialReceivingType(String serialReceivingTypeContent) {
        Map<String, List<OLESerialReceivingType>> oleSerialReceivingTypeMap = new HashMap<>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = getParameter(OLEConstants.SERIAL_LOADER_DELIMITER,OLEConstants.SELECT_NMSPC,OLEConstants.SELECT_CMPNT);
        if(cvsSplitBy != null && cvsSplitBy.trim().isEmpty()){
            cvsSplitBy = ",";
        }

        try {
            br = new BufferedReader(new FileReader(serialReceivingTypeContent));
            String[] serialTypes = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] types = line.split(cvsSplitBy);
                OLESerialReceivingType oleSerialReceivingType = new OLESerialReceivingType();
                if (count == 0) {
                    serialTypes = types;
                }
                int index = 0;
                if (count > 0) {
                    for (String type : types) {
                        type=type.replace("\"","");
                        if (serialTypes!=null && serialTypes.length > index && serialTypes[index]!=null) {
                            if (serialTypes[index].equals("\"SER_RCV_REC_TYP_ID\"")) {
                                index++;
                                oleSerialReceivingType.setSerialReceivingTypeId(type);
                            } else if (serialTypes[index].equals("\"SER_RCV_REC_ID\"")) {
                                index++;
                                oleSerialReceivingType.setSerialReceivingRecordId(type);
                            } else if (serialTypes[index].equals("\"RCV_REC_TYP\"")) {
                                index++;
                                oleSerialReceivingType.setReceivingRecordType(type);
                            } else if (serialTypes[index].equals("\"ACTN_DATE\"")) {
                                index++;
                                //oleSerialReceivingType.setActionDate(type);
                            } else if (serialTypes[index].equals("\"ACTN_INTRVL\"")) {
                                index++;
                                oleSerialReceivingType.setActionInterval(type);
                            } else if (serialTypes[index].equals("\"CHRON_CAPTN_LVL1\"")) {
                                index++;
                                oleSerialReceivingType.setChronologyCaptionLevel1(type);
                            } else if (serialTypes[index].equals("\"CHRON_CAPTN_LVL2\"")) {
                                index++;
                                oleSerialReceivingType.setChronologyCaptionLevel2(type);
                            } else if (serialTypes[index].equals("\"CHRON_CAPTN_LVL3\"")) {
                                index++;
                                oleSerialReceivingType.setChronologyCaptionLevel3(type);
                            } else if (serialTypes[index].equals("\"CHRON_CAPTN_LVL4\"")) {
                                index++;
                                oleSerialReceivingType.setChronologyCaptionLevel4(type);
                            } else if (serialTypes[index].equals("\"ENUM_CAPTN_LVL1\"")) {
                                index++;
                                oleSerialReceivingType.setEnumerationCaptionLevel1(type);
                            } else if (serialTypes[index].equals("\"ENUM_CAPTN_LVL2\"")) {
                                index++;
                                oleSerialReceivingType.setEnumerationCaptionLevel2(type);
                            } else if (serialTypes[index].equals("\"ENUM_CAPTN_LVL3\"")) {
                                index++;
                                oleSerialReceivingType.setEnumerationCaptionLevel3(type);
                            } else if (serialTypes[index].equals("\"ENUM_CAPTN_LVL4\"")) {
                                index++;
                                oleSerialReceivingType.setEnumerationCaptionLevel4(type);
                            } else if (serialTypes[index].equals("\"ENUM_CAPTN_LVL5\"")) {
                                index++;
                                oleSerialReceivingType.setEnumerationCaptionLevel5(type);
                            } else if (serialTypes[index].equals("\"ENUM_CAPTN_LVL6\"")) {
                                index++;
                                oleSerialReceivingType.setEnumerationCaptionLevel6(type);
                            } else if (serialTypes[index].equals("\"OBJ_ID\"")) {
                                index++;
                                oleSerialReceivingType.setObjectId(type);
                            } else if (serialTypes[index].equals("\"VER_NBR\"")) {
                                index++;
                                //oleSerialReceivingType.setVersionNumber(Long.getLong(type));
                            }
                            else {
                                index++;
                            }
                        }
                    }
                    boolean keyFound = false;
                    for (String serialReceivingRecordId : oleSerialReceivingTypeMap.keySet()) {
                        if (oleSerialReceivingType!=null && oleSerialReceivingType.getSerialReceivingRecordId()!=null && oleSerialReceivingType.getSerialReceivingRecordId().equals(serialReceivingRecordId)) {
                            keyFound = true;
                            List<OLESerialReceivingType> oleSerialReceivingTypeList = oleSerialReceivingTypeMap.get(serialReceivingRecordId);
                            oleSerialReceivingTypeList.add(oleSerialReceivingType);
                        }
                    }
                    if (!keyFound) {
                        List<OLESerialReceivingType> oleSerialReceivingTypeList = new ArrayList<>();
                        oleSerialReceivingTypeList.add(oleSerialReceivingType);
                        oleSerialReceivingTypeMap.put(oleSerialReceivingType.getSerialReceivingRecordId(), oleSerialReceivingTypeList);
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
        return oleSerialReceivingTypeMap;
    }

    public static List<String> getSerialRecordTypeList(){
        List<String> recordList = new ArrayList<String>();
        recordList.add(OLEConstants.RECORD_TYP_MAIN);
        recordList.add(OLEConstants.RECORD_TYP_SUPPLEMENTARY);
        recordList.add(OLEConstants.RECORD_TYP_INDEX);
       return recordList;
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

}
