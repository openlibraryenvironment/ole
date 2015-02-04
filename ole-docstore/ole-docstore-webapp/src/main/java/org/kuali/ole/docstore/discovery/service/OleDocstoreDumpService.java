package org.kuali.ole.docstore.discovery.service;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.kuali.ole.BibliographicRecordHandler;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.discovery.bo.OleDiscoveryMarcExportProfile;
import org.kuali.ole.docstore.discovery.bo.OleDiscoveryMarcMappingField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.pojo.bib.Collection;
import org.kuali.ole.repository.CheckoutManager;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/19/13
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDocstoreDumpService {
    private static final Logger LOG = LoggerFactory.getLogger(OleDocstoreDumpService.class);

    public String exportDocstoreData(String requestXML) throws Exception {
        OleDiscoveryMarcExportProfile oleDiscMarcExpProfile = getMarcRequest(requestXML);
        if (browseRepositoryContentForDataExport("work", "bibliographic", "marc", oleDiscMarcExpProfile)) {
            return "Success";
        } else {
            return "Failed";
        }
    }

    private boolean browseRepositoryContentForDataExport(String category, String type,
                                                         String format, OleDiscoveryMarcExportProfile oleDiscMarcExpProfile) throws RepositoryException, OleException {
        try {
            int fileCount = 0;
            List<String> bibMarcRecordList = new ArrayList<String>();
            List<String> recordList = new ArrayList<String>();
            BibliographicRecordHandler bibliographicRecordHandler = new BibliographicRecordHandler();
            RepositoryManager oleRepositoryManager = RepositoryManager.getRepositoryManager();
            Node rootNode = oleRepositoryManager.getSession("repositoryBrowser", "getbibUuids").getRootNode();
            Node categoryNode = rootNode.getNode(category);
            Node typeNode = categoryNode.getNode(type);
            Node formatNode = typeNode.getNode(format);
            Iterator<Node> formatIterator = formatNode.getNodes();
            while (formatIterator.hasNext()) {
                Node levelNode = formatIterator.next();
                Iterator<Node> levelNodeIterator = levelNode.getNodes();
                while (levelNodeIterator.hasNext()) {
                    Node fileNode = levelNodeIterator.next();
                    if (fileNode.getName().equalsIgnoreCase("marcFile")) {
                        fileNode.getIdentifier();           // this its self should give you all the uuids
                        String bibId = fileNode.getProperty("jcr:uuid").getValue().getString(); // this should give you uuid values
                        NodeIterator nodes = fileNode.getNodes();
                        while (nodes.hasNext()) {
                            Node contentNode = nodes.nextNode();
                            if (contentNode.hasProperty("jcr:data")) {
                                System.out.println("uuid: -----> " + contentNode.getIdentifier());
                                String bibContent = contentNode.getProperty("jcr:data").getString(); // other way
                                Collection collection = bibliographicRecordHandler.fromXML(bibContent);
                                BibliographicRecord bibliographicRecord = new BibliographicRecord();
                                bibliographicRecord = collection.getRecords().get(0);
                                bibliographicRecord = getInstanceDetails(bibId, bibliographicRecord, oleDiscMarcExpProfile);
                                String bibMarcRecord = bibliographicRecordHandler.generateXML(bibliographicRecord);
                                bibMarcRecordList.add(bibMarcRecord);
                            }
                        }
                    } else {
                        levelNodeIterator = fileNode.getNodes();
                    }
                }
            }
            int totalBibMarcRecords = bibMarcRecordList.size();
            int noOfRecords = oleDiscMarcExpProfile.getNoOfRecords();
            int i = 0;
            if (noOfRecords > 0) {
                while (i < totalBibMarcRecords) {
                    List<String> records = new ArrayList<String>();
                    for (int j = 0; j < noOfRecords; j++) {
                        records.add(bibMarcRecordList.get(i));
                        if (i == totalBibMarcRecords - 1) {
                            i++;
                            break;
                        }
                        i++;
                    }
                    StringBuffer recordContent = new StringBuffer();
                    recordList.clear();
                    for (int k = 0; k < records.size(); k++) {
                        recordList.add(records.get(k));
                        recordContent.append(recordList.get(k) + "\n");
                    }
                    fileCount++;
                    String xmlVersion = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
                    if (oleDiscMarcExpProfile.getExportFormat().equalsIgnoreCase("MARC XML"))
                        writeFileToLocation(xmlVersion + recordContent.toString(), oleDiscMarcExpProfile.getExportTo(), fileCount);
                    else if (oleDiscMarcExpProfile.getExportFormat().equalsIgnoreCase("MARC21"))
                        generateMarcFromXml(xmlVersion + recordContent.toString(), oleDiscMarcExpProfile.getExportTo(), fileCount);
                    else
                        return false;
                    records.clear();
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private OleDiscoveryMarcExportProfile getMarcRequest(String marcXml) {
        XStream xstream = new XStream();
        xstream.alias("request", OleDiscoveryMarcExportProfile.class);
        xstream.alias("exportProfiles", OleDiscoveryMarcExportProfile.class);
        xstream.alias("exportProfile", OleDiscoveryMarcExportProfile.class);
        xstream.aliasField("exportMappingFields", OleDiscoveryMarcExportProfile.class, "oleDiscoveryMarcMappingFields");
        xstream.alias("exportMappingField", OleDiscoveryMarcMappingField.class);

        OleDiscoveryMarcExportProfile oleDiscMarcExpProfile = (OleDiscoveryMarcExportProfile) xstream.fromXML(marcXml);
        return oleDiscMarcExpProfile;
    }


    public BibliographicRecord getInstanceDetails(String bibUUID, BibliographicRecord bibliographicRecord, OleDiscoveryMarcExportProfile oleDiscMarcExpProfile) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery("id:" + bibUUID);
            solrQuery.setRows(500);
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            QueryResponse response = server.query(solrQuery);
            List<SolrDocument> solrDocumentList = response.getResults();
            for (SolrDocument solrDocument : solrDocumentList) {
                if (solrDocument.getFieldValue("instanceIdentifier") instanceof List) {
                    List<String> instanceIdentifierList = (List<String>) solrDocument.getFieldValue("instanceIdentifier");
                    for (String instanceIdentifier : instanceIdentifierList) {
                        LOG.info("Instant Identifier Value is :" + instanceIdentifier);
                        CheckoutManager checkoutManager = new CheckoutManager();
                        String instanceOutput = checkoutManager.checkOut(instanceIdentifier, null, "checkout");
                        OleDocstoreDataRetrieveService oleDocstoreDataRetrieveService = new OleDocstoreDataRetrieveService();
                        InstanceCollection instanceCollection = oleDocstoreDataRetrieveService.getInstanceCollection(instanceOutput);
                        OleInstanceToMarcConvertor oleInstanceToMarcConvertor = new OleInstanceToMarcConvertor();
                        List<DataField> dataFields = bibliographicRecord.getDatafields();
                        List<DataField> instanceItemDataField = oleInstanceToMarcConvertor.generateDataField(instanceCollection, oleDiscMarcExpProfile);
                        dataFields.addAll(instanceItemDataField);
                    }
                } else {
                    String instanceIdentifier = (String) solrDocument.getFieldValue("instanceIdentifier");
                    LOG.info("Instant Identifier Value is :" + instanceIdentifier);
                    CheckoutManager checkoutManager = new CheckoutManager();
                    String instanceOutput = checkoutManager.checkOut(instanceIdentifier, null, "checkout");
                    OleDocstoreDataRetrieveService oleDocstoreDataRetrieveService = new OleDocstoreDataRetrieveService();
                    InstanceCollection instanceCollection = oleDocstoreDataRetrieveService.getInstanceCollection(instanceOutput);
                    OleInstanceToMarcConvertor oleInstanceToMarcConvertor = new OleInstanceToMarcConvertor();
                    List<DataField> dataFields = bibliographicRecord.getDatafields();
                    List<DataField> instanceItemDataField = oleInstanceToMarcConvertor.generateDataField(instanceCollection, oleDiscMarcExpProfile);
                    dataFields.addAll(instanceItemDataField);
                }
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return bibliographicRecord;
    }

    private void writeFileToLocation(String bibContent, String location, int fileName) throws Exception {
        LOG.info("Docstore dump location : " + location);
        File dumpFile = new File(location + "/" + fileName + ".xml");
        FileUtils.writeStringToFile(dumpFile, bibContent, "UTF-8");
    }

    private void generateMarcFromXml(String bibContent, String location, int fileName) throws Exception {
        LOG.info("Docstore dump location : " + location);
        InputStream input = new ByteArrayInputStream(bibContent.getBytes());
        File file = new File(location + "/" + fileName + ".mrk");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        MarcXmlReader marcXmlReader = new MarcXmlReader(input);
        MarcXmlWriter marcXmlWriter = new MarcXmlWriter(fileOutputStream, true);

        while (marcXmlReader.hasNext()) {
            Record record = marcXmlReader.next();
            marcXmlWriter.write(record);
        }
        marcXmlWriter.close();
    }


}
