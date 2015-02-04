package org.kuali.ole.systemintegration.rest.service;

import com.thoughtworks.xstream.XStream;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.Items;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EInstance;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.bo.OLESerialReceivingHistory;
import org.kuali.ole.service.impl.OLESerialReceivingServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.systemintegration.rest.RestConstants;
import org.kuali.ole.systemintegration.rest.bo.*;
import org.kuali.ole.systemintegration.rest.circulation.XmlContentHandler;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/10/14
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreDataRetrieveService {
    private static final Logger LOG = LoggerFactory.getLogger(DocstoreDataRetrieveService.class);
    private DocstoreClientLocator docstoreClientLocator;
    ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
    HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    private SolrServer solrServer;
    private OLESerialReceivingServiceImpl oleSerialReceivingService = new OLESerialReceivingServiceImpl();

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }
    /**
     * @param bibUUIDs
     * @param contentType
     * @return the instance xml for the list of bibuuids
     * @throws Exception
     */
    @Deprecated
    public String getInstanceDetails(List<String> bibUUIDs, String contentType) {
        InstanceCollection instanceCollection = new InstanceCollection();
        org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection eInstanceCollection = new org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection();
        org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection eInstanceCollectionOutput;
        InstanceCollection instanceCollectionOutput = new InstanceCollection();
        List<Instance> instances = new ArrayList<Instance>();
        StringBuffer output = new StringBuffer();
        boolean hasData = false;
        List<HoldingsTree> holdingsTree;
        BibTree bibTree;
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();

            for (int i = 0; i < bibUUIDs.size(); i++) {
                try {
                  bibTree =  getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(RestConstants.BIB_ID_PREFIX+bibUUIDs.get(i));
                    holdingsTree = bibTree.getHoldingsTrees();
                    for(HoldingsTree holdingsTree1 : holdingsTree){
                        hasData=true;
                        Holdings holdings = holdingsTree1.getHoldings();
                        Instance instance = new Instance();
                        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                        instance.setOleHoldings(oleHoldings);
                        HoldingsSerialReceiving oleHoldingsSerialReceiving = new HoldingsSerialReceiving();
                        oleHoldingsSerialReceiving = oleHoldingsSerialReceiving.copyHoldingSerialReceiving(oleHoldings);
                        Map<String,String> requestMap = new HashMap<String, String>();
                        requestMap.put(RestConstants.INSTACE_ID,instance.getOleHoldings().getHoldingsIdentifier());
                        requestMap.put(RestConstants.BIB_ID,RestConstants.BIB_ID_PREFIX+bibUUIDs.get(i));
                        SerialReceiving oleSerialReceiving = new SerialReceiving();
                        List<OLESerialReceivingDocument> oleSerialReceivingDocuments = (List<OLESerialReceivingDocument>)businessObjectService.findMatching(OLESerialReceivingDocument.class,requestMap);
                        if(oleSerialReceivingDocuments!=null && oleSerialReceivingDocuments.size()>0){
                            if(oleSerialReceivingDocuments.get(0).isPublicDisplay()){
                                oleSerialReceiving = populateOleSerialReceiving(oleSerialReceivingDocuments.get(0));
                                HoldingsSerialHistory oleHoldingsSerialHistory = new HoldingsSerialHistory();
                                List<SerialReceivingMain> oleSerialReceivingMainList = new ArrayList<>();
                                List<SerialReceivingIndex> oleSerialReceivingIndexList = new ArrayList<>();
                                List<SerialReceivingSupplement> oleSerialReceivingSupplementList = new ArrayList<>();
                                for(OLESerialReceivingHistory oleSerialReceivingHistory:oleSerialReceivingDocuments.get(0).getOleSerialReceivingHistoryList()){
                                    oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingHistory);
                                if(oleSerialReceivingHistory.getReceivingRecordType().equals(RestConstants.MAIN) && oleSerialReceivingHistory.isPublicDisplay()){
                                        SerialReceivingMain oleSerialReceivingMain = new SerialReceivingMain();
                                        oleSerialReceivingMain.setChronologyCaption(oleSerialReceivingHistory.getChronologyCaption());
                                        oleSerialReceivingMain.setEnumerationCaption(oleSerialReceivingHistory.getEnumerationCaption());
                                    oleSerialReceivingMain.setPublicReceiptNote(oleSerialReceivingHistory.getPublicReceipt());
                                        oleSerialReceivingMainList.add(oleSerialReceivingMain);
                                }else if(oleSerialReceivingHistory.getReceivingRecordType().equals(RestConstants.INDEX) && oleSerialReceivingHistory.isPublicDisplay()){
                                        oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingHistory);
                                        SerialReceivingIndex oleSerialReceivingindex = new SerialReceivingIndex();
                                        oleSerialReceivingindex.setChronologyCaption(oleSerialReceivingHistory.getChronologyCaption());
                                        oleSerialReceivingindex.setEnumerationCaption(oleSerialReceivingHistory.getEnumerationCaption());
                                    oleSerialReceivingindex.setPublicReceiptNote(oleSerialReceivingHistory.getPublicReceipt());
                                        oleSerialReceivingIndexList.add(oleSerialReceivingindex);
                                }else if(oleSerialReceivingHistory.getReceivingRecordType().equals(RestConstants.SUPPLEMENTARY) && oleSerialReceivingHistory.isPublicDisplay()){
                                        oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingHistory);
                                        SerialReceivingSupplement oleSerialReceivingSupplement = new SerialReceivingSupplement();
                                        oleSerialReceivingSupplement.setChronologyCaption(oleSerialReceivingHistory.getChronologyCaption());
                                        oleSerialReceivingSupplement.setEnumerationCaption(oleSerialReceivingHistory.getEnumerationCaption());
                                    oleSerialReceivingSupplement.setPublicReceiptNote(oleSerialReceivingHistory.getPublicReceipt());
                                        oleSerialReceivingSupplementList.add(oleSerialReceivingSupplement);
                                    }
                                }
                                /*Indexs serialReceivingIndexes = new Indexs();
                                serialReceivingIndexes.setOleSerialReceivingIndexList(oleSerialReceivingIndexList);
                                Mains serialReceivingMains = new Mains();
                                serialReceivingMains.setOleSerialReceivingMainList(oleSerialReceivingMainList);
                                Supplementaries serialReceivingSupplements = new Supplementaries();
                                serialReceivingSupplements.setOleSerialReceivingSupplementList(oleSerialReceivingSupplementList);
                                oleHoldingsSerialHistory.setMains(serialReceivingMains);
                                oleHoldingsSerialHistory.setIndexs(serialReceivingIndexes);
                                oleHoldingsSerialHistory.setSupplementaries(serialReceivingSupplements);
                                oleSerialReceiving.setOleHoldingsSerialHistory(oleHoldingsSerialHistory);
                                oleHoldingsSerialReceiving.setOleSerialReceiving(oleSerialReceiving);*/
                            }
                        }
                        instance.setOleHoldings(oleHoldingsSerialReceiving);
                        Items items = new Items();
                        List<Item> itemList = new ArrayList<Item>();
                        for(org.kuali.ole.docstore.common.document.Item item : holdingsTree1.getItems()){
                            Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
                            itemList.add(item1);
                        }
                        items.setItem(itemList);
                        instance.setItems(items);
                        List<String> bibIdList = new ArrayList<String>();
                        bibIdList.add(bibUUIDs.get(i));
                        instance.setResourceIdentifier(bibIdList);
                        instances.add(instance);
                    }
                }
                catch(Exception e){
                    LOG.info("Exception occured while retrieving the instance details for the bibId .");
                    LOG.error(e.getMessage(),e);
                }
            }
            instanceCollection.setInstance(instances);
            if (hasData) {
                String instanceXml = generateXmlInstanceContent(instanceCollection);
                String eInstanceXml = getEInstanceContent(eInstanceCollection);
/*                eInstanceXml=eInstanceXml.replace("<eInstanceCollection>","");
                eInstanceXml=eInstanceXml.replace("</eInstanceCollection>","");
                eInstanceXml=eInstanceXml.replace("<eInstances>","");
                eInstanceXml=eInstanceXml.replace("</eInstances>","");*/
/*                if(instanceXml.equals("<ole:instanceCollection/>")){
                    instanceXml = instanceXml.replace("<ole:instanceCollection/>","<ole:instanceCollection xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                            "  xsi:schemaLocation=\"http://ole.kuali.org/standards/ole-instance instance9.1.1-circulation.xsd\"\n" +
                            "  xmlns:circ=\"http://ole.kuali.org/standards/ole-instance-circulation\"\n" +
                            "  xmlns:ole=\"http://ole.kuali.org/standards/ole-instance\">"+eInstanceXml+"</ole:instanceCollection>");
                }   else{
                    instanceXml =instanceXml.replace("</ole:instanceCollection>",eInstanceXml+"</ole:instanceCollection>");
                }*/
                if (null == contentType) {
                    output.append(instanceXml);
                } else if (contentType.equalsIgnoreCase(RestConstants.XML)) {
                    output.append(instanceXml);
                } else if (contentType.equalsIgnoreCase(RestConstants.JSON)) {
                  //  output.append(generateJsonInstanceContent(instanceCollection));
                }
            }
            LOG.info("Instance Output :" + output.toString());
        return output.toString();
    }

    public String getHoldingsTree(List<String> bibUUIDs, String contentType) {
        StringBuffer output = new StringBuffer();
        boolean hasData = false;
        List<HoldingsTree> holdingsTrees;
        BibTrees bibTrees;
        HoldingsTrees oleHoldingsTrees = new HoldingsTrees();
        List<org.kuali.ole.docstore.common.document.content.instance.HoldingsTree> oleHoldingsTreeList = new ArrayList<>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        StringBuffer instanceXml = new StringBuffer();
        //for (int i = 0; i < bibUUIDs.size(); i++) {
        try {
            bibTrees = (BibTrees) getDocstoreClientLocator().getDocstoreClient().retrieveBibTrees(bibUUIDs);
            for (int i = 0; i < bibTrees.getBibTrees().size(); i++) {
                BibTree bibTree = bibTrees.getBibTrees().get(i);
                holdingsTrees = bibTree.getHoldingsTrees();
                for(HoldingsTree holdingsTree : holdingsTrees){
                    org.kuali.ole.docstore.common.document.content.instance.HoldingsTree oleHoldingsTree = new org.kuali.ole.docstore.common.document.content.instance.HoldingsTree();
                    hasData=true;
                    Holdings holdings = holdingsTree.getHoldings();
                    OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                    oleHoldings = processLocationName(oleHoldings);
                    HoldingsSerialReceiving oleHoldingsSerialReceiving = new HoldingsSerialReceiving();
                    oleHoldingsSerialReceiving = oleHoldingsSerialReceiving.copyHoldingSerialReceiving(oleHoldings);
                    Map<String,Object> requestMap = new HashMap<String, Object>();
                    requestMap.put(RestConstants.INSTACE_ID, oleHoldings.getHoldingsIdentifier());
                    requestMap.put(RestConstants.BIB_ID,RestConstants.BIB_ID_PREFIX+bibUUIDs.get(i));
                    requestMap.put(RestConstants.ACTIVE,Boolean.TRUE);
                    SerialReceiving oleSerialReceiving = new SerialReceiving();
                    List<OLESerialReceivingDocument> oleSerialReceivingDocuments = (List<OLESerialReceivingDocument>)businessObjectService.findMatching(OLESerialReceivingDocument.class,requestMap);
                    if(oleSerialReceivingDocuments!=null && oleSerialReceivingDocuments.size()>0){
                       // if(oleSerialReceivingDocuments.get(0).isPublicDisplay()){
                            oleSerialReceiving = populateOleSerialReceiving(oleSerialReceivingDocuments.get(0));
                            HoldingsSerialHistory oleHoldingsSerialHistory = new HoldingsSerialHistory();
                            List<SerialReceivingMain> oleSerialReceivingMainList = new ArrayList<>();
                            List<SerialReceivingIndex> oleSerialReceivingIndexList = new ArrayList<>();
                            List<SerialReceivingSupplement> oleSerialReceivingSupplementList = new ArrayList<>();
                            for(OLESerialReceivingHistory oleSerialReceivingHistory:oleSerialReceivingDocuments.get(0).getOleSerialReceivingHistoryList()){
                                oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingHistory);
                                if(oleSerialReceivingHistory.getReceivingRecordType().equals(RestConstants.MAIN) && oleSerialReceivingHistory.isPublicDisplay()){
                                    SerialReceivingMain oleSerialReceivingMain = new SerialReceivingMain();
                                    oleSerialReceivingMain.setChronologyCaption(oleSerialReceivingHistory.getChronologyCaption());
                                    oleSerialReceivingMain.setEnumerationCaption(oleSerialReceivingHistory.getEnumerationCaption());
                                    oleSerialReceivingMain.setPublicReceiptNote(oleSerialReceivingHistory.getPublicReceipt());
                                    oleSerialReceivingMainList.add(oleSerialReceivingMain);
                                }else if(oleSerialReceivingHistory.getReceivingRecordType().equals(RestConstants.INDEX) && oleSerialReceivingHistory.isPublicDisplay()){
                                    oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingHistory);
                                    SerialReceivingIndex oleSerialReceivingindex = new SerialReceivingIndex();
                                    oleSerialReceivingindex.setChronologyCaption(oleSerialReceivingHistory.getChronologyCaption());
                                    oleSerialReceivingindex.setEnumerationCaption(oleSerialReceivingHistory.getEnumerationCaption());
                                    oleSerialReceivingindex.setPublicReceiptNote(oleSerialReceivingHistory.getPublicReceipt());
                                    oleSerialReceivingIndexList.add(oleSerialReceivingindex);
                                }else if(oleSerialReceivingHistory.getReceivingRecordType().equals(RestConstants.SUPPLEMENTARY) && oleSerialReceivingHistory.isPublicDisplay()){
                                    oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingHistory);
                                    SerialReceivingSupplement oleSerialReceivingSupplement = new SerialReceivingSupplement();
                                    oleSerialReceivingSupplement.setChronologyCaption(oleSerialReceivingHistory.getChronologyCaption());
                                    oleSerialReceivingSupplement.setEnumerationCaption(oleSerialReceivingHistory.getEnumerationCaption());
                                    oleSerialReceivingSupplement.setPublicReceiptNote(oleSerialReceivingHistory.getPublicReceipt());
                                    oleSerialReceivingSupplementList.add(oleSerialReceivingSupplement);
                                }
                            }
                            Indexes serialReceivingIndexes = new Indexes();
                            serialReceivingIndexes.setIndex(oleSerialReceivingIndexList);
                            Mains serialReceivingMains = new Mains();
                            serialReceivingMains.setMain(oleSerialReceivingMainList);
                            Supplementaries serialReceivingSupplements = new Supplementaries();
                            serialReceivingSupplements.setSupplementary(oleSerialReceivingSupplementList);
                            oleHoldingsSerialHistory.setMains(serialReceivingMains);
                            oleHoldingsSerialHistory.setIndexes(serialReceivingIndexes);
                            oleHoldingsSerialHistory.setSupplementaries(serialReceivingSupplements);
                            oleSerialReceiving.setSerialReceivingHistory(oleHoldingsSerialHistory);
                            oleHoldingsSerialReceiving.setSerialReceiving(oleSerialReceiving);
                       // }
                    }
                    oleHoldingsTree.setOleHoldings(oleHoldingsSerialReceiving);
                    Items items = new Items();
                    List<Item> itemList = new ArrayList<Item>();
                    for(org.kuali.ole.docstore.common.document.Item item : holdingsTree.getItems()){
                        Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
                        itemList.add(item1);
                    }
                    items.getItem().addAll((List)itemList);
                    oleHoldingsTree.setItems(items);
                    List<String> bibIdList = new ArrayList<String>();
                    bibIdList.add(bibUUIDs.get(i));
                    oleHoldingsTreeList.add(oleHoldingsTree);
                }
            }
        }
        catch(Exception e){
            LOG.info("Exception occured while retrieving the instance details for the bibId .");
            LOG.error(e.getMessage(),e);
        }
        //}
        oleHoldingsTrees.setHoldingsTrees(oleHoldingsTreeList);
        if (hasData) {
            if(oleHoldingsTrees.getHoldingsTrees().size()>0){
                instanceXml.append(oleHoldingsTrees.serialize(oleHoldingsTrees));
            }
            if (null == contentType) {
                output.append(instanceXml);
            } else if (contentType.equalsIgnoreCase(RestConstants.XML)) {
                output.append(instanceXml);
            } else if (contentType.equalsIgnoreCase(RestConstants.JSON)) {
                output.append(generateJsonInstanceContent(oleHoldingsTrees));
            }
        }
        LOG.info("Instance Output :" + output.toString());
        return output.toString();
    }

    /**
     * This method is used to set the location name instead of the location code in the Location Object
     * @param oleHoldings
     * @return OleHoldings
     */
    public OleHoldings processLocationName(OleHoldings oleHoldings) {
        if (oleHoldings.getLocation() != null) {
            Location location = oleHoldings.getLocation();
            LocationLevel locationLevel = location.getLocationLevel();
            while (locationLevel != null) {
                Map<String, String> locationMap = new HashMap<String, String>();
                locationMap.put("locationCode", locationLevel.getName());
                List<OleLocation> oleLocationList = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class, locationMap);
                if (oleLocationList.size() > 0) {
                    locationLevel.setCode(locationLevel.getName());
                    locationLevel.setName(oleLocationList.get(0).getLocationName());
                }
                locationLevel = locationLevel.getLocationLevel();
            }
        }
        return oleHoldings;
    }

    private SolrServer getSolrServer() throws SolrServerException {
        if (null == solrServer) {
            solrServer = SolrServerManager.getInstance().getSolrServer();
        }
        return solrServer;
    }

    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    /**
     * @param instanceData
     * @return the instance Collection object from the given xml content
     */
    public InstanceCollection getInstanceCollection(String instanceData) {
        XStream xs = new XStream();
        xs.autodetectAnnotations(true);
        xs.processAnnotations(InstanceCollection.class);
        InstanceCollection instanceCollection = (InstanceCollection) xs.fromXML(instanceData);
        return instanceCollection;
    }


    public String generateJsonInstanceContent(HoldingsTrees holdingsTree) {
        XmlContentHandler xmlContentHandler = new XmlContentHandler();
        String jsonContent = null;
        try{
       jsonContent = xmlContentHandler.marshalToJSON(holdingsTree);
        }catch(Exception e){
            LOG.info(e.getMessage());

        }
        return jsonContent;
    }

    private XmlContentHandler getXmlContentHandler() {
        return new XmlContentHandler();
    }

    /**
     * @param instanceCollection
     * @return the required xml format content.
     */
    public String generateXmlInstanceContent(InstanceCollection instanceCollection) {
        InstanceXmlConverterService oleInstanceXmlConverterService = new InstanceXmlConverterService();
        String instanceXml = oleInstanceXmlConverterService.generateInstanceCollectionsXml(instanceCollection);
        return instanceXml;
    }

    private RequestDocument buildRequestDocument(String cat, String type, String format, String uuid) {
        RequestDocument reqDoc = new RequestDocument();
        reqDoc.setCategory(cat);
        reqDoc.setType(type);
        reqDoc.setFormat(format);
        reqDoc.setUuid(uuid);
        return reqDoc;

    }

    public String getEInstanceContent( org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection eInstanceCollection){
        XStream xStream = new XStream();
        xStream.alias(RestConstants.EINSTANCE_COLLECTION,org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class);
        xStream.alias(RestConstants.EINSTANCE,EInstance.class);
        xStream.aliasField(RestConstants.EINSTANCES,org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class,RestConstants.EINSTANCE);
        return xStream.toXML(eInstanceCollection);
    }

    public  org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection getEInstanceCollection(String instanceData) {
        XStream xs = new XStream();
        xs.autodetectAnnotations(true);
        xs.processAnnotations(org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class);
        xs.alias(RestConstants.EINSTANCE,EInstance.class);
        xs.aliasField(RestConstants.EINSTANCES,org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class,RestConstants.EINSTANCE);
        org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection instanceCollection = ( org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection) xs.fromXML(instanceData);
        return instanceCollection;
    }

    public SerialReceiving populateOleSerialReceiving(OLESerialReceivingDocument oleSerialReceivingDocument){
        SerialReceiving serialReceiving = new SerialReceiving();
        serialReceiving.setCallNumber(oleSerialReceivingDocument.getCallNumber());
        serialReceiving.setUnboundLocation(generateLocation(oleSerialReceivingDocument.getUnboundLocation()));
        /*oleSerialReceiving.setSerialReceivingRecordId(oleSerialReceivingDocument.getSerialReceivingRecordId());
        oleSerialReceiving.setVendorId(oleSerialReceivingDocument.getVendorId());
        if(oleSerialReceivingDocument.getCreateDate()!=null)
            oleSerialReceiving.setCreateDate(oleSerialReceivingDocument.getCreateDate().toString());
        oleSerialReceiving.setOperatorId(oleSerialReceivingDocument.getOperatorId());
        oleSerialReceiving.setMachineId(oleSerialReceivingDocument.getMachineId());
        oleSerialReceiving.setBoundLocation(oleSerialReceivingDocument.getBoundLocation());
        oleSerialReceiving.setReceivingRecordType(oleSerialReceivingDocument.getReceivingRecordType());
        oleSerialReceiving.setClaim(oleSerialReceivingDocument.isClaim());
        oleSerialReceiving.setClaimIntervalInformation(oleSerialReceivingDocument.getClaimIntervalInformation());
        oleSerialReceiving.setCreateItem(oleSerialReceivingDocument.isCreateItem());
        oleSerialReceiving.setGeneralReceivingNote(oleSerialReceivingDocument.getGeneralReceivingNote());
        oleSerialReceiving.setPoId(oleSerialReceivingDocument.getPoId());
        oleSerialReceiving.setPrintLabel(oleSerialReceivingDocument.isPrintLabel());
        oleSerialReceiving.setPublicDisplay(oleSerialReceivingDocument.isPublicDisplay());
        oleSerialReceiving.setSubscriptionStatus(oleSerialReceivingDocument.getSubscriptionStatus());
        oleSerialReceiving.setSerialReceiptLocation(oleSerialReceivingDocument.getSerialReceiptLocation());
        oleSerialReceiving.setSerialReceivingRecord(oleSerialReceivingDocument.getSerialReceivingRecord());
        oleSerialReceiving.setTreatmentInstructionNote(oleSerialReceivingDocument.getTreatmentInstructionNote());
        oleSerialReceiving.setUrgentNote(oleSerialReceivingDocument.getUrgentNote());
        if(oleSerialReceivingDocument.getSubscriptionStatusDate()!=null)
            oleSerialReceiving.setSubscriptionStatusDate(oleSerialReceivingDocument.getSubscriptionStatusDate().toString());*/
        return serialReceiving;

    }

    public UnboundLocation generateLocation(String locationName){
        LocationLevel locationLevel = new LocationLevel();
        locationLevel = createLocationLevel(locationName, locationLevel);
        UnboundLocation unboundLocation = new UnboundLocation();
        unboundLocation.setLocationLevel(locationLevel);
        return unboundLocation;
    }

    public LocationLevel createLocationLevel(String locationName, LocationLevel locationLevel) {
        LOG.debug("Inside the instance details createLocationLevel method");
        if (locationName != null && !locationName.equalsIgnoreCase("")) {
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            String[] names = locationName.split("/");
            Map parentCriteria = new HashMap();
            parentCriteria.put(OLEConstants.LOC_CD, names[0]);
            OleLocation oleLocationCollection = businessObjectService.findByPrimaryKey(OleLocation.class, parentCriteria);
            String levelName = oleLocationCollection.getLocationName();
            String locationCode = oleLocationCollection.getLocationCode();
            String levelCode = oleLocationCollection.getOleLocationLevel().getLevelName();
            locationLevel.setName(levelName);
            locationLevel.setLevel(levelCode);
            locationLevel.setCode(locationCode);
            String locName = "";
            if (locationName.contains(OLEConstants.SLASH))
                locName = locationName.replace(names[0] + OLEConstants.SLASH, "");
            else
                locName = locationName.replace(names[0], "");
            if (locName != null && !locName.equals("")) {
                LocationLevel newLocationLevel = new LocationLevel();
                locationLevel.setLocationLevel(createLocationLevel(locName, newLocationLevel));
            }
        }
        return locationLevel;
    }


}
