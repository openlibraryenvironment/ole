package org.kuali.ole.docstore.discovery.service;

import com.thoughtworks.xstream.XStream;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.discovery.circulation.XmlContentHandler;
import org.kuali.ole.docstore.discovery.circulation.json.CircInstance;
import org.kuali.ole.docstore.discovery.circulation.json.InstanceRecord;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EInstance;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.repository.CheckoutManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/7/13
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDocstoreDataRetrieveService {
    private static final Logger LOG = LoggerFactory.getLogger(OleDocstoreDataRetrieveService.class);
    DocstoreService docstoreService = BeanLocator.getDocstoreService();
    ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
    HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    private CheckoutManager checkoutManager;
    private SolrServer solrServer;

    /**
     * @param bibUUIDs
     * @param contentType
     * @return the instance xml for the list of bibuuids
     * @throws Exception
     */
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

            for (int i = 0; i < bibUUIDs.size(); i++) {
                try {
                  bibTree =  docstoreService.retrieveBibTree("wbm-"+bibUUIDs.get(i));
                    holdingsTree = bibTree.getHoldingsTrees();
                    for(HoldingsTree holdingsTree1 : holdingsTree){
                        hasData=true;
                        Holdings holdings = holdingsTree1.getHoldings();
                        Instance instance = new Instance();
                        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                        instance.setOleHoldings(oleHoldings);
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
                } else if (contentType.equalsIgnoreCase("xml")) {
                    output.append(instanceXml);
                } else if (contentType.equalsIgnoreCase("json")) {
                    output.append(generateJsonInstanceContent(instanceCollection));
                }
            }
            LOG.info("Instance Output :" + output.toString());


        return output.toString();
    }

    private CheckoutManager getCheckoutManager() {
        if (null == checkoutManager) {
            checkoutManager = new CheckoutManager();
        }
        return checkoutManager;
    }

    private SolrServer getSolrServer() throws SolrServerException {
        if (null == solrServer) {
            solrServer = SolrServerManager.getInstance().getSolrServer();
        }
        return solrServer;
    }

    public void setCheckoutManager(CheckoutManager checkoutManager) {
        this.checkoutManager = checkoutManager;
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


    /**
     * @param instanceCollection
     * @return the json format of  the instance collection object
     */
    public String generateJsonInstanceContent(InstanceCollection instanceCollection) {
        XmlContentHandler xmlContentHandler = new XmlContentHandler();
        CircInstance circInstance = new CircInstance();
        List<Instance> instanceList = (instanceCollection.getInstance());
        ArrayList<org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection> instanceCollectionArrayList = new ArrayList<org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection>();
        for (Iterator<Instance> iterator = instanceList.iterator(); iterator.hasNext(); ) {
            Instance instance = iterator.next();
            org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection col1 = new org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection();
            InstanceRecord instanceRecord = new InstanceRecord();
            instanceRecord.setExtension(instance.getExtension());
            instanceRecord.setResourceIdentifier(instance.getResourceIdentifier());
            instanceRecord.setFormerResourceIdentifier(instance.getFormerResourceIdentifier());
            instanceRecord.setInstanceIdentifier(instance.getInstanceIdentifier());
            instanceRecord.setItems(instance.getItems().getItem());
            instanceRecord.setOleHoldings(instance.getOleHoldings());
            instanceRecord.setSourceHoldings(instance.getSourceHoldings());
            col1.setInstance(instanceRecord);
            instanceCollectionArrayList.add(col1);
        }  /* for(EInstance eInstance : eInstanceCollection.getEInstance()){
            org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection col2 = new org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection();
            col2.seteInstance(eInstance);
            instanceCollectionArrayList.add(col2);
        }*/
        circInstance.setInstanceCollection(instanceCollectionArrayList);
        String jsonContent = null;
        try{
       jsonContent = xmlContentHandler.marshalToJSON(circInstance);
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
        OleInstanceXmlConverterService oleInstanceXmlConverterService = new OleInstanceXmlConverterService();
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
        xStream.alias("eInstanceCollection",org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class);
        xStream.alias("eInstance",org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EInstance.class);
        xStream.aliasField("eInstances",org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class,"eInstance");
        return xStream.toXML(eInstanceCollection);
    }

    public  org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection getEInstanceCollection(String instanceData) {
        XStream xs = new XStream();
        xs.autodetectAnnotations(true);
        xs.processAnnotations(org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class);
        xs.alias("eInstance",org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EInstance.class);
        xs.aliasField("eInstances",org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection.class,"eInstance");
        org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection instanceCollection = ( org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection) xs.fromXML(instanceData);
        return instanceCollection;
    }


}
