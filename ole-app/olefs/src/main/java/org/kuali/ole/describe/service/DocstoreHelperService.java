package org.kuali.ole.describe.service;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.BibliographicRecordHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleItemRecordHandler;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.Items;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.service.QueryService;
import org.kuali.ole.docstore.discovery.service.QueryServiceImpl;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.jaxb.config.DocumentConfigConverter;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;
import org.kuali.ole.pojo.ProfileAttribute;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.pojo.bib.Collection;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.kuali.ole.select.bo.OLEInstanceSearch;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//import org.kuali.ole.pojo.item.OleHolding;
//import org.kuali.ole.pojo.item.OleItem;

/**
 * DocstoreHelperService is the service class to access Docstore services
 */
public class DocstoreHelperService {

    private static final String DOCSTORE_URL = "docstore.url";
    private final String CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING = "docAction=ingestContent&stringContent=";
    private final String CHECKOUT_DOCSTORE_RECORD_QUERY_STRING = "docAction=checkOut&uuid=";
    private final String UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING = "docAction=checkIn&stringContent=";
    private final String ROLLBACK_DATA_FROM_DOCSTORE = "docAction=deleteWithLinkedDocs&requestContent=";
    private final String TRANSFER_INSTANCES_QUERY_STRING = "docAction=transferInstances&stringContent=";
    private final String TRANSFER_ITEMS_QUERY_STRING = "docAction=transferItems&stringContent=";
    private static final String DOCSTORE_BASE_URL = "ole.docstore.url.base";
    private static final String DOC_CONFIG_INFO = "/getDocumentConfigInfo";
    private InstanceOlemlRecordProcessor instanceOlemlRecordProcessor;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;

    private static final Logger LOG = Logger.getLogger(DocstoreHelperService.class);

    private List<ProfileAttribute> attributes = new ArrayList<ProfileAttribute>();

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public InstanceOlemlRecordProcessor getInstanceOlemlRecordProcessor() {
        if (instanceOlemlRecordProcessor == null) {
            instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        }
        return instanceOlemlRecordProcessor;
    }

    public HoldingOlemlRecordProcessor getHoldingOlemlRecordProcessor() {
        if (holdingOlemlRecordProcessor == null) {
            holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        }
        return holdingOlemlRecordProcessor;
    }

    private ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if (itemOlemlRecordProcessor == null) {
            itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        }
        return itemOlemlRecordProcessor;
    }

    public String persistToDocstoreForEditor(String content, String uuid, String format) throws Exception {
        String responseFromDocstore = callToDocstore(null, content, uuid, format);
        return responseFromDocstore;
    }

    public String persistToDocstoreFromUnifiedEditor(String reqContent, String uuid, String format) throws Exception {
        String responseFromDocstore = callToDocstore(reqContent, null, uuid, format);
        return responseFromDocstore;
    }

    private String callToDocstore(String reqContent, String content, String uuid, String format) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = null;
        if (null == uuid) {
            String xmlContent = "";
            if (reqContent != null) {
                xmlContent = reqContent;
            } else {
                xmlContent = buildRequestDocXML(content, format);
            }
            queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xmlContent, "UTF-8");
        } else {
            String xmlContent = buildRequestDocXML(content, uuid, format);
            queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xmlContent, "UTF-8");
        }

        return postData(docstoreURL, queryString);
    }


    private String callToDocstoreWithAdditionalAttributes(String content, AdditionalAttributes additionalAttributes, String uuid, String format) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = null;

        if (null == uuid) {
            String xmlContent = buildRequestDocXMLWithAdditionalAttributes(content, additionalAttributes, format);
            queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xmlContent, "UTF-8");
        } else {
            String xmlContent = buildRequestDocXMLWithAdditionalAttributes(content, additionalAttributes, uuid, format);
            queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xmlContent, "UTF-8");
        }

        return postData(docstoreURL, queryString);
    }

    //Additional Attributes

    public String persistToDocstoreWithAdditionalAttributesForEditor(String content,
                                                                     AdditionalAttributes additionalAttributes,
                                                                     String uuid, String format) throws Exception {
        String responseFromDocstore = callToDocstoreWithAdditionalAttributes(content, additionalAttributes, uuid,
                format);
        return responseFromDocstore;
    }

    public String persistNewToDocstoreForIngest(BibliographicRecord bibliographicRecord, List<ProfileAttributeBo> profileAttributes) throws Exception {
        this.attributes = buildListOfProfileAttributes(profileAttributes);
        BibliographicRecordHandler bibliographicRecordHandler = new BibliographicRecordHandler();
        String bibXMLContent = bibliographicRecordHandler.generateXML(bibliographicRecord);
        String instanceXMLContent = getInstanceXML(bibliographicRecord);
        String requestXML = buildReuestDocXMLForIngest(bibXMLContent, instanceXMLContent);
        String queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(requestXML);
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String responseXML = postData(docstoreURL, queryString + queryString);
        return responseXML;
    }

    public String persistNewToDocstoreForIngest(LineItemOrder lineItemOrder, BibliographicRecord bibliographicRecord, List<ProfileAttributeBo> profileAttributes) throws Exception {
        this.attributes = buildListOfProfileAttributes(profileAttributes);
        BibliographicRecordHandler bibliographicRecordHandler = new BibliographicRecordHandler();
        String bibXMLContent = bibliographicRecordHandler.generateXML(bibliographicRecord);
        String instanceXMLContent = getInstanceXML(lineItemOrder, bibliographicRecord);
        String requestXML = buildReuestDocXMLForIngest(bibXMLContent, instanceXMLContent);
        String queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(requestXML);
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String responseXML = postData(docstoreURL, queryString + queryString);
        return responseXML;
    }

    public String getDocstoreData(String uuid) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = CHECKOUT_DOCSTORE_RECORD_QUERY_STRING + uuid;
        String responseFromDocstore = postData(docstoreURL, queryString);
        Response response = new ResponseHandler().toObject(responseFromDocstore);
        String responseContent = getResponseContent(response);
        return responseContent;
    }

    public String getDocstoreData(String docCategory, String docType, String docFormat, String uuid) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = CHECKOUT_DOCSTORE_RECORD_QUERY_STRING + uuid + "&docCategory=" + docCategory + "&docType=" + docType + "&docFormat=" + docFormat;
        String responseFromDocstore = postData(docstoreURL, queryString);
        Response response = new ResponseHandler().toObject(responseFromDocstore);
        String responseContent = getResponseContent(response);
        return responseContent;
    }

    public ResponseDocument checkOutDocument(String docCategory, String docType, String docFormat, String uuid) throws Exception {
        ResponseDocument responseDocument = null;
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = CHECKOUT_DOCSTORE_RECORD_QUERY_STRING + uuid + "&docCategory=" + docCategory + "&docType=" + docType + "&docFormat=" + docFormat;
        String responseFromDocstore = postData(docstoreURL, queryString);
        Response response = new ResponseHandler().toObject(responseFromDocstore);
        responseDocument = response.getDocuments().get(0);
        return responseDocument;
    }

    public String getResponseContent(Response response) {
        String responseString = null;
        if (response != null) {
            List<ResponseDocument> responseDocumentList = response.getDocuments();
            if (responseDocumentList.size() > 0) {
                ResponseDocument responseDocument = responseDocumentList.get(0);
                if(responseDocument.getStatus() != null && responseDocument.getStatus().contains("Failed")) {
                    return null;
                }
                Content contentObj = responseDocument.getContent();
                if (contentObj != null) {
                    responseString = contentObj.getContent();
                }
            }
        }
        return responseString;
    }


    public static String postData(String target, String content) throws Exception {
        String response = "";
        URL url = new URL(target);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        Writer w = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        w.write(content);
        w.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String temp;
        while ((temp = in.readLine()) != null) {
            response += temp + "\n";
        }
        in.close();
        return response;
    }


    private String buildRequestDocXML(String xmlContent, String format) {
        return buildRequestDocXML(xmlContent, null, format);
    }

    private String buildRequestDocXMLWithAdditionalAttributes(String xmlContent, AdditionalAttributes additionalAttributes, String format) {
        return buildRequestDocXMLWithAdditionalAttributes(xmlContent, additionalAttributes, null, format);
    }

    public String buildReuestDocXMLForIngest(String bibXML, String instanceXML) {
        Request requestObject = new Request();
        requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession().getPrincipalName() : "");
        requestObject.setOperation(OLEConstants.INGEST_OPERATION);
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId("1");
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(OLEConstants.BIB_DOC_TYPE);
        requestDocument.setFormat(OLEConstants.MARC_FORMAT);
        requestDocument.setContent(new Content(bibXML));

        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        additionalAttributes.setDateEntered(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setFastAddFlag("true");
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setAttribute("dateEntered", String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setAttribute("lastUpdated", String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setAttribute("fastAddFlag", "true");
        additionalAttributes.setAttribute("supressFromPublic", "false");
        requestDocument.setAdditionalAttributes(additionalAttributes);

        RequestDocument linkedRequestDocument = new RequestDocument();
        linkedRequestDocument.setId("2");
        linkedRequestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        linkedRequestDocument.setType(OLEConstants.INSTANCE_DOC_TYPE);
        linkedRequestDocument.setContent(new Content(instanceXML));
        linkedRequestDocument.setFormat(OLEConstants.OLEML_FORMAT);

        ArrayList<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
        linkedRequestDocuments.add(linkedRequestDocument);

        requestDocument.setLinkedRequestDocuments(linkedRequestDocuments);

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);
        return xml;
    }


    private String buildRequestDocXML(String xmlContent, String uuid, String format) {
        String INSTANCE_MARC_XML_STRING = "<instanceCollection>\n" +
                "  <instance>\n" +
                "<instanceIdentifier></instanceIdentifier>\n" +
                "    <oleHoldings primary=\"true\">\n" +
                "       <holdingsIdentifier></holdingsIdentifier>\n" +
                "       <receiptStatus></receiptStatus>\n" +
                "       <uri></uri>\n" +
                "       <note type=\"public\"></note>\n" +
                "       <location primary=\"true\" status=\"permanent\">\n" +
                "           <locationLevel>\n" +
                "               <name></name>\n" +
                "               <level></level>\n" +
                "               <locationLevel>\n" +
                "                   <name></name>\n" +
                "                   <level></level>\n" +
                "                   <locationLevel>\n" +
                "                       <name></name>\n" +
                "                       <level></level>\n" +
                "                       <locationLevel>\n" +
                "                           <name></name>\n" +
                "                           <level></level>\n" +
                "                       </locationLevel>\n" +
                "                    </locationLevel>\n" +
                "                </locationLevel>\n" +
                "        </locationLevel>\n" +
                "      </location>\n" +
                "      <extension>\n" +
                "        <additionalAttributes>\n" +
                "          <createdBy></createdBy>\n" +
                "          <dateEntered></dateEntered>\n" +
                "        </additionalAttributes>\n" +
                "      </extension>\n" +
                "      <callNumber>\n" +
                "        <type></type>\n" +
                "        <prefix></prefix>\n" +
                "        <number></number>\n" +
                "        <shelvingScheme>\n" +
                "          <codeValue></codeValue>\n" +
                "        </shelvingScheme>\n" +
                "        <shelvingOrder>\n" +
                "          <codeValue></codeValue>\n" +
                "        </shelvingOrder>\n" +
                "      </callNumber>\n" +
                "    </oleHoldings>\n" +
                "    <items>\n" +
                "      <item>\n" +
                "        <staffOnlyFlag>false</staffOnlyFlag>\n" +
                "        <fastAddFlag>false</fastAddFlag>\n" +
                "        <extension reference=\"../../../oleHoldings/extension\"/>\n" +
                "      </item>\n" +
                "    </items>\n" +
                "  </instance>\n" +
                "</instanceCollection>";
        Request requestObject = new Request();
        RequestDocument requestDocument = new RequestDocument();

        if (null == uuid) {
            requestDocument.setId("1");
            RequestDocument linkedRequestDocument = new RequestDocument();
            linkedRequestDocument.setId("2");
            linkedRequestDocument.setCategory(OLEConstants.WORK_CATEGORY);
            linkedRequestDocument.setType(OLEConstants.INSTANCE_DOC_TYPE);
            linkedRequestDocument.setContent(new Content(INSTANCE_MARC_XML_STRING));
            linkedRequestDocument.setFormat(OLEConstants.OLEML_FORMAT);

            ArrayList<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
            linkedRequestDocuments.add(linkedRequestDocument);

            requestObject.setOperation(OLEConstants.INGEST_OPERATION);
            requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession().getPrincipalName() : "");
            requestDocument.setLinkedRequestDocuments(linkedRequestDocuments);
        } else {
            requestDocument.setId(uuid);
            requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
            requestObject.setUser("editor");
        }
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(OLEConstants.BIB_DOC_TYPE);

        if (format.equalsIgnoreCase("marc")) {
            requestDocument.setFormat(OLEConstants.MARC_FORMAT);
        } else if (format.equalsIgnoreCase("dublinunq")) {
            requestDocument.setFormat(OLEConstants.UNQUALIFIED_DUBLIN_FORMAT);
        }

        requestDocument.setContent(new Content(xmlContent));

        if (format.equalsIgnoreCase("marc")) {
            AdditionalAttributes additionalAttributes = new AdditionalAttributes();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            if (null == uuid) {
                additionalAttributes.setDateEntered(String.valueOf(dateFormat.format(new Date())));
            }
            additionalAttributes.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
            additionalAttributes.setFastAddFlag("true");
            additionalAttributes.setSupressFromPublic("false");
            requestDocument.setAdditionalAttributes(additionalAttributes);
        }

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);

        if (format.equalsIgnoreCase(OLEConstants.UNQUALIFIED_DUBLIN_FORMAT)) {
            xml = modifyXmlContent(xml);
        }
        return xml;
    }

    private String buildRequestDocXMLWithAdditionalAttributes(String xmlContent,
                                                              AdditionalAttributes additionalAttributes, String uuid,
                                                              String format) {
        String user = null;
        user = GlobalVariables.getUserSession().getPrincipalName();
        String INSTANCE_MARC_XML_STRING = "<instanceCollection>\n" +
                "  <instance>\n" +
                "<instanceIdentifier></instanceIdentifier>\n" +
                "    <oleHoldings primary=\"true\">\n" +
                "       <holdingsIdentifier></holdingsIdentifier>\n" +
                "       <!--Zero or more repetitions:-->\n" +
                "       <extentOfOwnership>\n" +
                "          <textualHoldings></textualHoldings>\n" +
                "              <!--Zero or more repetitions:-->\n" +
                "                 <note type=\"string\"></note>\n" +
                "           <type></type>\n" +
                "       </extentOfOwnership>\n" +
                "       <receiptStatus></receiptStatus>\n" +
                "       <uri></uri>\n" +
                "       <note type=\"public\"></note>\n" +
/*
                                          "       <location primary=\"true\" status=\"permanent\">\n" +
                                          "           <locationLevel>\n" +
                                          "               <name>Holdings</name>\n" +
                                          "               <level></level>\n" +
                                          "               <locationLevel>\n" +
                                          "                   <name></name>\n" +
                                          "                   <level></level>\n" +
                                          "                   <locationLevel>\n" +
                                          "                       <name></name>\n" +
                                          "                       <level></level>\n" +
                                          "                       <locationLevel>\n" +
                                          "                           <name></name>\n" +
                                          "                           <level></level>\n" +
                                          "                       </locationLevel>\n" +
                                          "                    </locationLevel>\n" +
                                          "                </locationLevel>\n" +
                                          "        </locationLevel>\n" +
                                          "      </location>\n" +
*/
                "      <extension>\n" +
                "        <additionalAttributes>\n" +
                "          <createdBy>" + user + "</createdBy>\n" +
                "          <dateEntered></dateEntered>\n" +
                "        </additionalAttributes>\n" +
                "      </extension>\n" +
                "      <callNumber>\n" +
                "        <type></type>\n" +
                "        <prefix></prefix>\n" +
                "        <number></number>\n" +
                "        <shelvingScheme>\n" +
                "          <codeValue></codeValue>\n" +
                "        </shelvingScheme>\n" +
                "        <shelvingOrder>\n" +
                "          <codeValue></codeValue>\n" +
                "          <fullValue></fullValue>\n" +
                "        </shelvingOrder>\n" +
                "      </callNumber>\n" +
                "    </oleHoldings>\n" +
                "    <items>\n" +
                "      <item>\n" +
                "          <copyNumber></copyNumber>\n" +
                "           <volumeNumber></volumeNumber>\n" +
                "          <note type=\"public\"></note>" +
                "          <callNumber>\n" +
                "                <number></number>\n" +
                "          </callNumber>\n" +
                "        <staffOnlyFlag>false</staffOnlyFlag>\n" +
                "        <fastAddFlag>false</fastAddFlag>\n" +
                "        <extension reference=\"../../../oleHoldings/extension\"/>\n" +
                "      </item>\n" +
                "    </items>\n" +
                "  </instance>\n" +
                "</instanceCollection>";
        Request requestObject = new Request();
        RequestDocument requestDocument = new RequestDocument();
        if (null == uuid) {
            requestDocument.setId("1");
            RequestDocument linkedRequestDocument = new RequestDocument();
            linkedRequestDocument.setId("2");
            linkedRequestDocument.setCategory(OLEConstants.WORK_CATEGORY);
            linkedRequestDocument.setType(OLEConstants.INSTANCE_DOC_TYPE);
            linkedRequestDocument.setContent(new Content(INSTANCE_MARC_XML_STRING));
            linkedRequestDocument.setFormat(OLEConstants.OLEML_FORMAT);

            ArrayList<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
            linkedRequestDocuments.add(linkedRequestDocument);

            requestObject.setOperation(OLEConstants.INGEST_OPERATION);
            requestObject.setUser(
                    GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession().getPrincipalName()
                            : "");
            requestDocument.setLinkedRequestDocuments(linkedRequestDocuments);
        } else {
            requestDocument.setId(uuid);
            requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
            requestObject.setUser(
                    GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession().getPrincipalName()
                            : "");
            //requestObject.setUser("editor");
        }
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(OLEConstants.BIB_DOC_TYPE);

        if (format.equalsIgnoreCase("marc")) {
            requestDocument.setFormat(OLEConstants.MARC_FORMAT);
        } else if (format.equalsIgnoreCase("dublinunq")) {
            requestDocument.setFormat(OLEConstants.UNQUALIFIED_DUBLIN_FORMAT);
        }

        requestDocument.setContent(new Content(xmlContent));
        if (additionalAttributes != null && requestDocument.getType().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            requestDocument.setAdditionalAttributes(additionalAttributes);
        }

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);
        if (format.equalsIgnoreCase(OLEConstants.UNQUALIFIED_DUBLIN_FORMAT)) {
            xml = modifyXmlContent(xml);
        }
        return xml;
    }

    public String getInstanceXML(BibliographicRecord bibliographicRecord) {
        InstanceOlemlRecordProcessor instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        InstanceCollection instanceCollection = new InstanceCollection();
        Instance oleInstance = new Instance();
        List<Item> oleItemList = new ArrayList<Item>();
        oleItemList.add(getOleItem(bibliographicRecord));
        if (oleInstance.getItems() == null) {
            oleInstance.setItems(new Items());
        }
        oleInstance.getItems().setItem(oleItemList);
        oleInstance.setOleHoldings(getOleHolding(bibliographicRecord));

        Extension extension = new Extension();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        additionalAttributes.setDateEntered(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setFastAddFlag("false");
        additionalAttributes.setHarvestable("true");
        additionalAttributes.setStatus("n"); // new Record
        additionalAttributes.setAttribute("dateEntered", additionalAttributes.getDateEntered());
        additionalAttributes.setAttribute("lastUpdated", additionalAttributes.getLastUpdated());
        additionalAttributes.setAttribute("fastAddFlag", additionalAttributes.getFastAddFlag());
        additionalAttributes.setAttribute("supressFromPublic", additionalAttributes.getSupressFromPublic());
        additionalAttributes.setAttribute("harvestable", additionalAttributes.getHarvestable());
        additionalAttributes.setAttribute("status", additionalAttributes.getStatus());
        extension.getContent().add(additionalAttributes);
        //List<Extension>   extensionList = new ArrayList<Extension>();
        //extensionList.add(extension);
        oleInstance.setExtension(extension);
        oleInstance.getOleHoldings().setPrimary(OLEConstants.TRUE);
        //oleInstance.getOleHoldings().setReceiptStatus("");
        Uri uri = new Uri();
        uri.setValue("");
        uri.setResolvable(null);
        oleInstance.getOleHoldings().getUri().add(uri);
        Note note = new Note();
        note.setType(OLEConstants.NOTE_TYPE);
        oleInstance.getOleHoldings().getNote().add(note);
        CallNumber callNumber = new CallNumber();
        ShelvingScheme shelvingScheme = new ShelvingScheme();
        ShelvingOrder shelvingOrder = new ShelvingOrder();
        callNumber.setType("");
        callNumber.setPrefix("");
        callNumber.setNumber("");
        shelvingScheme.setCodeValue("");
        shelvingOrder.setCodeValue("");
        callNumber.setShelvingScheme(shelvingScheme);
        callNumber.setShelvingOrder(shelvingOrder);
        oleInstance.getOleHoldings().setCallNumber(callNumber);
        List<Instance> oleInstanceList = new ArrayList<Instance>();
        OleHoldings oleHoldings = oleInstance.getOleHoldings();
        if (oleHoldings.getLocation() == null) {
            LocationLevel locationLevel = new LocationLevel();
            Location location = new Location();
            locationLevel.setLevel("");
            locationLevel.setName("");
            location.setPrimary(OLEConstants.TRUE);
            location.setStatus(OLEConstants.PERMANENT);
            location.setLocationLevel(locationLevel);
            oleInstance.getOleHoldings().setLocation(location);
        }
        oleInstanceList.add(oleInstance);
        instanceCollection.setInstance(oleInstanceList);
        //String instanceXML = instanceOlemlRecordProcessor.generateXML(getOleHolding(), getOleItem(bibliographicRecord));
        String instanceXML = instanceOlemlRecordProcessor.toXML(instanceCollection);
        return instanceXML;
    }

    public String getInstanceXML(LineItemOrder lineItemOrder, BibliographicRecord bibliographicRecord) {
        InstanceOlemlRecordProcessor instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        InstanceCollection instanceCollection = new InstanceCollection();
        Instance oleInstance = new Instance();
        List<Item> oleItemList = new ArrayList<Item>();
        oleItemList.add(getOleItem(lineItemOrder, bibliographicRecord));
        if (oleInstance.getItems() == null) {
            oleInstance.setItems(new Items());
        }
        oleInstance.getItems().setItem(oleItemList);
        oleInstance.setOleHoldings(getOleHolding(bibliographicRecord));

        Extension extension = new Extension();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        additionalAttributes.setDateEntered(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setFastAddFlag("false");
        additionalAttributes.setHarvestable("true");
        additionalAttributes.setStatus("n"); // new Record
        additionalAttributes.setAttribute("dateEntered", additionalAttributes.getDateEntered());
        additionalAttributes.setAttribute("lastUpdated", additionalAttributes.getLastUpdated());
        additionalAttributes.setAttribute("fastAddFlag", additionalAttributes.getFastAddFlag());
        additionalAttributes.setAttribute("supressFromPublic", additionalAttributes.getSupressFromPublic());
        additionalAttributes.setAttribute("harvestable", additionalAttributes.getHarvestable());
        additionalAttributes.setAttribute("status", additionalAttributes.getStatus());
        extension.getContent().add(additionalAttributes);
        //List<Extension>   extensionList = new ArrayList<Extension>();
        //extensionList.add(extension);
        oleInstance.setExtension(extension);
        oleInstance.getOleHoldings().setPrimary(OLEConstants.TRUE);
        //oleInstance.getOleHoldings().setReceiptStatus("");
        Uri uri = new Uri();
        uri.setValue("");
        uri.setResolvable(null);
        oleInstance.getOleHoldings().getUri().add(uri);
        Note note = new Note();
        note.setType(OLEConstants.NOTE_TYPE);
        oleInstance.getOleHoldings().getNote().add(note);
        CallNumber callNumber = new CallNumber();
        ShelvingScheme shelvingScheme = new ShelvingScheme();
        ShelvingOrder shelvingOrder = new ShelvingOrder();
        callNumber.setType("");
        callNumber.setPrefix("");
        callNumber.setNumber("");
        shelvingScheme.setCodeValue("");
        shelvingOrder.setCodeValue("");
        callNumber.setShelvingScheme(shelvingScheme);
        callNumber.setShelvingOrder(shelvingOrder);
        oleInstance.getOleHoldings().setCallNumber(callNumber);
        List<Instance> oleInstanceList = new ArrayList<Instance>();
        OleHoldings oleHoldings = oleInstance.getOleHoldings();
        if (oleHoldings.getLocation() == null) {
            LocationLevel locationLevel = new LocationLevel();
            Location location = new Location();
            locationLevel.setLevel("");
            locationLevel.setName("");
            location.setPrimary(OLEConstants.TRUE);
            location.setStatus(OLEConstants.PERMANENT);
            location.setLocationLevel(locationLevel);
            oleInstance.getOleHoldings().setLocation(location);
        }
        oleInstanceList.add(oleInstance);
        instanceCollection.setInstance(oleInstanceList);
        //String instanceXML = instanceOlemlRecordProcessor.generateXML(getOleHolding(), getOleItem(bibliographicRecord));
        String instanceXML = instanceOlemlRecordProcessor.toXML(instanceCollection);
        return instanceXML;
    }

    public OleHoldings getOleHolding(BibliographicRecord bibliographicRecord) {
        //TODO: return ole holdings as in OleHoldingsRecordHandler().getOleHoldings()
        return null; //new OleHoldingsRecordHandler().getOleHoldings(bibliographicRecord, attributes);
    }

    public Item getOleItem(BibliographicRecord bibliographicRecord) {
        for (DataField dataField : bibliographicRecord.getDatafields()) {
            if (dataField.getTag().equalsIgnoreCase(OLEConstants.DATA_FIELD_985)) {
                List<SubField> subFieldList = dataField.getSubFields();
                SubField subField = new SubField();
                subField.setCode(OLEConstants.SUB_FIELD_A);
                subField.setValue(OLEConstants.DEFAULT_LOCATION_LEVEL_INSTITUTION);
                subFieldList.add(subField);
                dataField.setSubFields(subFieldList);
            }
        }
        //TODO: return ole holdings as in OleItemRecordHandler().getOleItem()
        return null; //new OleItemRecordHandler().getOleItem(bibliographicRecord, attributes);
    }

    public Item getOleItem(LineItemOrder lineItemOrder, BibliographicRecord bibliographicRecord) {
        for (DataField dataField : bibliographicRecord.getDatafields()) {
            if (dataField.getTag().equalsIgnoreCase(OLEConstants.DATA_FIELD_985)) {
                List<SubField> subFieldList = dataField.getSubFields();
                SubField subField = new SubField();
                subField.setCode(OLEConstants.SUB_FIELD_A);
                subField.setValue(OLEConstants.DEFAULT_LOCATION_LEVEL_INSTITUTION);
                subFieldList.add(subField);
                dataField.setSubFields(subFieldList);
            }
        }
        //TODO: return ole holdings as in OleItemRecordHandler().getOleItem()
        return null; //new OleItemRecordHandler().getOleItem(lineItemOrder, bibliographicRecord, attributes);
    }

    private List<ProfileAttribute> buildListOfProfileAttributes(List<ProfileAttributeBo> profileAttributes) {
        for (Iterator iterator = profileAttributes.iterator(); iterator.hasNext(); ) {
            ProfileAttributeBo profileAttributeBo = (ProfileAttributeBo) iterator.next();
            ProfileAttribute profileAttribute = new ProfileAttribute();
            profileAttribute.setAgendaName(profileAttributeBo.getAgendaName());
            profileAttribute.setAttributeName(profileAttributeBo.getAttributeName());
            profileAttribute.setAttributeValue(profileAttributeBo.getAttributeValue());
            profileAttribute.setSystemValue(profileAttributeBo.getSystemValue());
            attributes.add(profileAttribute);
        }
        return attributes;
    }

    private String modifyXmlContent(String inputXmlContent) {
        StringBuffer modifiedContent = new StringBuffer(inputXmlContent);
        modifiedContent.replace(modifiedContent.indexOf("<oai_dc:dc"), modifiedContent.indexOf(">", modifiedContent.indexOf("<oai_dc:dc")),
                "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"");
        return modifiedContent.toString();
    }

    public void rollbackData(String xmlForRollback) throws Exception {
        String queryString = ROLLBACK_DATA_FROM_DOCSTORE + URLEncoder.encode(xmlForRollback, "UTF-8");
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        postData(docstoreURL, queryString + queryString);
    }


    public String updateItem(BibliographicRecord bibliographicRecord, String itemUUID) {
        String responseXML = null;
        try {
            Item oleItem = getOleItem(bibliographicRecord);
            OleItemRecordHandler oleItemRecordHandler = new OleItemRecordHandler();
            //TODO : use ole-docstore-common code
            String itemXML = ""; //oleItemRecordHandler.toXML(oleItem);
            String requestXML = buildReuestDocXMLForUpdate(itemXML, itemUUID);
            String queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(requestXML, "UTF-8");
            String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
            responseXML = postData(docstoreURL, queryString);
        } catch (Exception e) {
            //e.printStackTrace();
            LOG.error("updateItem Exception:" + e);
        }
        return responseXML;
    }

    public String buildReuestDocXMLForUpdate(String itemXML, String itemUUID) {
        Request requestObject = new Request();
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId(itemUUID);
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(OLEConstants.ITEM_DOC_TYPE);
        requestDocument.setFormat(OLEConstants.OLEML_FORMAT);
        requestDocument.setContent(new Content(itemXML));

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);
        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);
        return xml;
    }

    /**
     * Method to generate Request XML and ingest Instance record to docstore
     *
     * @param content
     * @param uuid
     * @param format
     * @return Docstore response for Ingesting New Instance Record
     * @throws Exception
     */
    public String instanceRecordCallToDocstore(String content, String uuid, String format) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = null;
        String xmlContent = buildInstanceRequestDocXML(content, uuid, format);
        queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xmlContent, "UTF-8");
        return postData(docstoreURL, queryString);
    }

    /**
     * Method to generate Request xml for Ingesting Instance record
     *
     * @param xmlContent
     * @param uuid
     * @param format
     * @return Request XML content
     */
    private String buildInstanceRequestDocXML(String xmlContent, String uuid, String format) {
        Request requestObject = new Request();
        RequestDocument requestDocument = new RequestDocument();
        if (null == uuid) {
            requestDocument.setId("1");
            requestObject.setOperation(OLEConstants.INGEST_OPERATION);
        } else {
            requestDocument.setId(uuid);
            requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
        }
        requestObject.setUser(GlobalVariables.getUserSession().getPrincipalName());
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(OLEConstants.INSTANCE_DOC_TYPE);
        requestDocument.setFormat(OLEConstants.OLEML_FORMAT);

        requestDocument.setContent(new Content(xmlContent));

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String requestXml = requestHandler.toXML(requestObject);
        return requestXml;
    }

    /**
     * Method to update Holding or Item record of an Instance record
     *
     * @param uuid
     * @param docType
     * @param xmlContent
     * @return Docstore XML response with success/failure status
     * @throws Exception
     */
    public String updateInstanceRecord(String uuid, String docType, String xmlContent) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        Request requestObject = new Request();
        requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession().getPrincipalName() :
                "admin");
        LOG.info("requestObject.getUser()" + requestObject.getUser());
        requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
        RequestDocument requestDocument = new RequestDocument();

        requestDocument.setId(uuid);
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(docType);  // docType should be either holdings or item
        requestDocument.setFormat(OLEConstants.OLEML_FORMAT);
        requestDocument.setContent(new Content(xmlContent));

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);

        String queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xml, "UTF-8");

        return postData(docstoreURL, queryString);
    }

    public String updateInstanceRecord(String uuid, String docType, String xmlContent, AdditionalAttributes additionalAttributes) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        Request requestObject = new Request();
        requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession().getPrincipalName() :
                "admin");
        LOG.info("requestObject.getUser()" + requestObject.getUser());
        requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
        RequestDocument requestDocument = new RequestDocument();

        requestDocument.setId(uuid);
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(docType);  // docType should be either holdings or item
        requestDocument.setFormat(OLEConstants.OLEML_FORMAT);
        requestDocument.setContent(new Content(xmlContent));
        requestDocument.setAdditionalAttributes(additionalAttributes);

        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);

        String queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xml, "UTF-8");

        return postData(docstoreURL, queryString);
    }

    /**
     * This method used to delete a instance record
     *
     * @param instance
     * @return
     * @throws Exception
     */

    public String deleteInstanceRecord(Instance instance) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        //  Request deleteRequest = buildDeleteRequest(instance);
        String uuid = instance.getInstanceIdentifier();
        String queryString = "docAction=delete&requestContent=" + uuid;
        String response = postData(docstoreURL, queryString);
        return response;

    }

    /**
     * This method used to delete a docstore record based on uuid
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    public String deleteDocstoreRecord(String uuid) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = "docAction=delete&requestContent=" + uuid;
        String response = postData(docstoreURL, queryString);
        return response;

    }

    /**
     * Method to add NEW ITEM for existing Instance record
     *
     * @param instanceUuid
     * @param docType
     * @param xmlContent
     * @return Docstore XML response with success/failure status
     * @throws Exception
     */
    public String createItemForInstanceRecord(String instanceUuid, String docType, String xmlContent) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        Request requestObject = new Request();
        requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession().getPrincipalName() : "");
        requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
        RequestDocument requestDocument = new RequestDocument();

        requestDocument.setId(instanceUuid);
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(OLEConstants.INSTANCE_DOC_TYPE);
        requestDocument.setFormat(OLEConstants.OLEML_FORMAT);


        RequestDocument linkedRequestDocument = new RequestDocument();
        linkedRequestDocument.setId(OLEConstants.NEW_ITEM_ID);
        linkedRequestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        linkedRequestDocument.setType(docType);
        linkedRequestDocument.setFormat(OLEConstants.OLEML_FORMAT);
        linkedRequestDocument.setContent(new Content(xmlContent));

        List<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
        linkedRequestDocuments.add(linkedRequestDocument);
        requestDocument.setLinkedRequestDocuments(linkedRequestDocuments);


        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);

        String queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xml, "UTF-8");

        return postData(docstoreURL, queryString);

    }

    public String createItemForInstanceRecord(String instanceUuid, String docType, String xmlContent, AdditionalAttributes additionalAttributes) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        Request requestObject = new Request();
        requestObject.setUser(GlobalVariables.getUserSession() != null ? GlobalVariables.getUserSession().getPrincipalName() : "");
        requestObject.setOperation(OLEConstants.CHECK_IN_OPERATION);
        RequestDocument requestDocument = new RequestDocument();

        requestDocument.setId(instanceUuid);
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(OLEConstants.INSTANCE_DOC_TYPE);
        requestDocument.setFormat(OLEConstants.OLEML_FORMAT);
        requestDocument.setAdditionalAttributes(additionalAttributes);


        RequestDocument linkedRequestDocument = new RequestDocument();
        linkedRequestDocument.setId(OLEConstants.NEW_ITEM_ID);
        linkedRequestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        linkedRequestDocument.setType(docType);
        linkedRequestDocument.setFormat(OLEConstants.OLEML_FORMAT);
        linkedRequestDocument.setContent(new Content(xmlContent));

        List<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
        linkedRequestDocuments.add(linkedRequestDocument);
        requestDocument.setLinkedRequestDocuments(linkedRequestDocuments);


        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        requestObject.setRequestDocuments(requestDocuments);

        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);

        String queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(xml, "UTF-8");

        return postData(docstoreURL, queryString);

    }

    public void transferInstances(String requestXML) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        LOG.debug("transferInstances docstoreURL " + docstoreURL);
        String queryString = TRANSFER_INSTANCES_QUERY_STRING + URLEncoder.encode(requestXML, "UTF-8");
        postData(docstoreURL, queryString);
    }


    public String transferItems(String requestXML) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        LOG.debug("transferItems docstoreURL " + docstoreURL);
        String queryString = TRANSFER_ITEMS_QUERY_STRING + URLEncoder.encode(requestXML, "UTF-8");
        String response = postData(docstoreURL, queryString);
        LOG.debug("response transferItems " + response);
        return response;
    }


    public String deleteItemrecord(String uuid) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
        String queryString = "docAction=delete&requestContent=" + uuid;
        String response = postData(docstoreURL, queryString);
        return response;

    }

    public WorkBibDocument getInfoForBibTree(WorkBibDocument workBibDocument) {
        QueryService queryService = QueryServiceImpl.getInstance();

        try {
            workBibDocument = queryService.queryForBibTree(workBibDocument);
        } catch (Exception e) {
            //e.printStackTrace();
            LOG.error("getInfoForBibTree Exception:" + e);
        }
        return workBibDocument;

    }

    public List<WorkBibDocument> getInfoForBibTree(List<WorkBibDocument> bibDocumentList) {
        List<WorkBibDocument> workBibDocumentList = new ArrayList<WorkBibDocument>();
        for (WorkBibDocument workBibDocument : bibDocumentList) {
            getInfoForBibTree(workBibDocument);
            workBibDocumentList.add(workBibDocument);
        }

        return workBibDocumentList;
    }

    public BibliographicRecord getBibliographicRecord(String bibUUID) throws Exception {
        BibliographicRecord bibliographicRecord = null;
        String responseDocStoreData = getDocstoreData(bibUUID);
/*        ResponseHandler responseHandler = new ResponseHandler();
        Response response = responseHandler.toObject(responseDocStoreData);
        String responseContentXMLString = getResponseContent(response);
        Collection bibliographicRecords = new BibliographicRecordHandler().fromXML(responseContentXMLString);*/
        Collection bibliographicRecords = new BibliographicRecordHandler().fromXML(responseDocStoreData);
        List<BibliographicRecord> bibliographicRecordList = bibliographicRecords.getRecords();
        Iterator<BibliographicRecord> bibliographicRecordListIterator = bibliographicRecordList.iterator();
        if (bibliographicRecordListIterator.hasNext()) {
            bibliographicRecord = bibliographicRecordListIterator.next();
        }
        return bibliographicRecord;
    }

    public InstanceCollection getInstanceCollection(String instanceUUID) throws Exception {
        String responseFromDocstore = getDocstoreData(instanceUUID);
        InstanceCollection instanceCollection = new InstanceOlemlRecordProcessor().fromXML(responseFromDocstore);
        return instanceCollection;
    }

    public String updateInstanceToDocstore(InstanceCollection instanceCollection) throws Exception {
        String instanceXMLString = getInstanceOlemlRecordProcessor().toXML(instanceCollection);
        String instanceUUID = instanceCollection.getInstance().iterator().next().getInstanceIdentifier();
        String response = updateInstanceRecord(instanceUUID, OLEConstants.INSTANCE_DOC_TYPE, instanceXMLString);
        return response;
    }

    public String updateOleHoldingToDocstore(OleHoldings oleHoldings) throws Exception {
        String oleHoldingXMLString = getHoldingOlemlRecordProcessor().toXML(oleHoldings);
        String oleHoldingUUID = oleHoldings.getHoldingsIdentifier();
        String response = updateInstanceRecord(oleHoldingUUID, OLEConstants.HOLDING_DOC_TYPE, oleHoldingXMLString);
        return response;
    }

    public String updateOleItemToDocstore(Item item) throws Exception {
        String itemXMLString = getItemOlemlRecordProcessor().toXML(item);
        String oleItemUUID = item.getItemIdentifier();
        if (LOG.isInfoEnabled()) {
            LOG.info("oleItemUUID---------->" + oleItemUUID);
        }
        String response = updateInstanceRecord(oleItemUUID, OLEConstants.ITEM_DOC_TYPE, itemXMLString);
        return response;
    }

    public String persistNewToDocstoreForIngest(BibliographicRecord bibliographicRecord, Item item, OleHoldings oleHoldings) {

        BibliographicRecordHandler bibliographicRecordHandler = new BibliographicRecordHandler();
        InstanceOlemlRecordProcessor instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        DocstoreHelperService docstoreHelperService = new DocstoreHelperService();

        String responseXML = null;
        Items items = new Items();
        List<Item> itemList = new ArrayList<Item>();
        try {


            String bibXML = bibliographicRecordHandler.generateXML(bibliographicRecord);
            itemList.add(item);
            InstanceCollection instanceCollection = new InstanceCollection();
            Instance oleInstance = new Instance();

            if (oleInstance.getItems() == null) {
                oleInstance.setItems(new Items());
            }
            oleInstance.getItems().setItem(itemList);
            oleInstance.setOleHoldings(oleHoldings);
            //oleInstance.setExtension(extension);
            List<Instance> oleInstanceList = new ArrayList<Instance>();
            oleInstanceList.add(oleInstance);
            instanceCollection.setInstance(oleInstanceList);
            String instanceXML = instanceOlemlRecordProcessor.toXML(instanceCollection);
            String requestXML = docstoreHelperService.buildReuestDocXMLForIngest(bibXML, instanceXML);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Fast Add instance xml : " + instanceXML);
                LOG.debug("Fast Add request xml : " + requestXML);
            }
            String queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(requestXML);
            String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);
            responseXML = docstoreHelperService.postData(docstoreURL, queryString + queryString);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Fast Add response xml : " + responseXML);
            }
        } catch (Exception ex) {
            LOG.info("exception ---> " + ex.getStackTrace());
        }
        return responseXML;
    }


    /**
     * Converts item xml to item pojo.
     *
     * @param itemXml
     * @return
     * @throws Exception
     */
    public Item getItemPojo(String itemXml) throws Exception {
        LOG.debug("Inside the getItemPojo method");
        Item oleItem = null;
        try {
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            oleItem = itemOlemlRecordProcessor.fromXML(itemXml);
        } catch (Exception e) {
            LOG.error(OLEConstants.PAR_EXP);
            throw new Exception(OLEConstants.PAR_EXP);
        }
        return oleItem;
    }


    /**
     * Retrieves item xml using itemuuid.
     *
     * @param itemUUID
     * @return
     * @throws Exception
     */
    public String getItemXML(String itemUUID) throws Exception {
        LOG.debug("Inside the getItemXML method");
        String itemXml = "";
        try {
            itemXml = getDocstoreData(itemUUID);
            LOG.info("item XML ----- > " + itemXml);
        } catch (Exception e) {
            LOG.error(OLEConstants.ITM_BARCD_NT_AVAL_DOC);
            throw new Exception(OLEConstants.ITM_BARCD_NT_AVAL_DOC);
        }
        return itemXml;
    }


    /**
     * Retrieves Holding Object for given instance UUID.
     *
     * @param instanceUUID
     * @return
     * @throws Exception
     */
    public OleHoldings getOleHoldings(String instanceUUID) throws Exception {
        LOG.info("--Inside getOleHoldings---");
        DocstoreHelperService docstoreHelperService = new DocstoreHelperService();
        String responseFromDocstore = docstoreHelperService.getDocstoreData(instanceUUID);
        InstanceCollection instanceCollection = new InstanceOlemlRecordProcessor().fromXML(responseFromDocstore);
        List<Instance> instances = instanceCollection != null ? instanceCollection.getInstance() : new ArrayList<Instance>();
        Instance instance = instances.size() > 0 ? instances.get(0) : null;
        OleHoldings oleHoldings = instance != null ? instance.getOleHoldings() : null;
        return oleHoldings;
    }


    private List<OleItemSearch> getOleItemListByItemInfo(String solrQuery, Map<String, String> searchCriteria) {

        List<OleItemSearch> oleItemSearchList = new ArrayList<OleItemSearch>();
        int rowSize = 10;
        String bibIdentifier = null;
        String itemIdentfier = "";
        String instanceIdentifier = "";
        String holdingsIdentifier = "";
        if (searchCriteria.get("rowSize") != null)
            rowSize = Integer.parseInt(searchCriteria.get("rowSize"));
        try {

            List<HashMap<String, Object>> documentList = QueryServiceImpl.getInstance().retriveResults("(DocType:item) AND (" + solrQuery + ")", rowSize);
            for (int i = 0; i < documentList.size(); i++) {
                OleItemSearch oleItemSearch = null;
                try {
                    if (documentList.get(i).get("ItemIdentifier_display") != null) {
                        itemIdentfier = (String) ((ArrayList) documentList.get(i).get("ItemIdentifier_display")).get(0);
                        instanceIdentifier = (String) ((ArrayList) documentList.get(i).get("instanceIdentifier")).get(0);
                        holdingsIdentifier = (String) ((ArrayList) documentList.get(i).get("holdingsIdentifier")).get(0);
                        bibIdentifier = (String) ((ArrayList) documentList.get(i).get("bibIdentifier")).get(0);
                    }
                    String itemXml = getItemXML(itemIdentfier);
                    Item oleItem = getItemPojo(itemXml);
                    if (oleItem != null) {
                        if (oleItem != null && ((oleItem.getLocation() == null || oleItem.getCallNumber() == null) || (oleItem.getCallNumber()!=null && StringUtils.isEmpty(oleItem.getCallNumber().getNumber())))) {
                            OleHoldings oleHoldings = getOleHoldings(instanceIdentifier);
                            oleItem.setLocation(oleHoldings.getLocation());
                            oleItem.setCallNumber(oleHoldings.getCallNumber());
                        }
                        if (oleItem != null && (oleItem.getLocation() != null)) {
                            if (oleItem != null && oleItem.getLocation().getLocationLevel() == null) {
                                OleHoldings oleHoldings = getOleHoldings(instanceIdentifier);
                                oleItem.setLocation(oleHoldings.getLocation());
                                oleItem.setCallNumber(oleHoldings.getCallNumber());
                            }
                        }
                        LocationValuesBuilder locationValuesBuilder = new LocationValuesBuilder();
                        /*OleHoldings oleHoldings = getOleHoldings(instanceIdentifier);
                        oleItem.setLocation(oleHoldings.getLocation());*/
                        oleItemSearch = new OleItemSearch();
                        oleItemSearch.setItemBarCode(oleItem.getAccessInformation() == null ? "" : oleItem.getAccessInformation().getBarcode());
                        oleItemSearch.setCallNumber(oleItem.getCallNumber() == null ? "" : oleItem.getCallNumber().getNumber());
                        oleItemSearch.setItemType(oleItem.getItemType() == null ? "" : oleItem.getItemType().getCodeValue());
                        oleItemSearch.setItemUUID(oleItem.getItemIdentifier());
                        oleItemSearch.setInstanceUUID(instanceIdentifier);
                        oleItemSearch.setBibUUID(bibIdentifier);
                        oleItemSearch.setCopyNumber(oleItem.getCopyNumber());
                        oleItemSearch.setShelvingLocation(locationValuesBuilder.getShelvingLocation(oleItem.getLocation()));
                        if (oleItem.getItemStatus() != null) {
                            oleItemSearch.setItemStatus(oleItem.getItemStatus().getCodeValue());
                        }
                        oleItemSearch.setHoldingUUID(holdingsIdentifier);
                        if (bibIdentifier != null) {
                            Map<String, String> bibDetails = QueryServiceImpl.getInstance().getBibInformation(bibIdentifier, searchCriteria);
                            if (bibDetails != null) {
                                if (bibDetails.get("Title") != null)
                                    oleItemSearch.setTitle((String) bibDetails.get("Title"));
                                if (bibDetails.get("Author") != null)
                                    oleItemSearch.setAuthor((String) bibDetails.get("Author"));
                                if (bibDetails.get("Publisher") != null)
                                    oleItemSearch.setPublisher((String) bibDetails.get("Publisher"));

                                oleItemSearchList.add(oleItemSearch);
                            }
                        }

                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    LOG.error("getOleItemListByItemInfo Exception:" + e);
                }


            }
        } catch (Exception ex) {
            LOG.info("Exception ------> " + ex);
            ex.printStackTrace();
        }

        return oleItemSearchList;
    }

    private List<OleItemSearch> getOleItemListByBibInfo(String solrQuery, Map<String, String> searchCriteria) {

        int rowSize = 10;
        List<OleItemSearch> oleItemSearchList = new ArrayList<OleItemSearch>();
        if (searchCriteria.get("rowSize") != null)
            rowSize = Integer.parseInt(searchCriteria.get("rowSize"));
        try {

            List<HashMap<String, Object>> documentList = QueryServiceImpl.getInstance().retriveResults("(DocType:bibliographic) AND (" + solrQuery + ")", rowSize);
            for (int i = 0; i < documentList.size(); i++) {
                String instanceIdentifier = null;
                OleItemSearch oleItemSearch = new OleItemSearch();
                Item itemDetails = null;
                try {
                    HashMap<String, Object> itemvalues = documentList.get(i);
                    if (itemvalues.get("Title_display") != null)
                        oleItemSearch.setTitle((String) ((ArrayList) itemvalues.get("Title_display")).get(0));
                    else if (itemvalues.get("Title_search") != null)
                        oleItemSearch.setTitle((String) ((ArrayList) itemvalues.get("Title_search")).get(0));
                    if (itemvalues.get("Author_display") != null)
                        oleItemSearch.setAuthor((String) ((ArrayList) itemvalues.get("Author_display")).get(0));
                    else if (itemvalues.get("Author_search") != null)
                        oleItemSearch.setAuthor((String) ((ArrayList) itemvalues.get("Author_search")).get(0));
                    if (itemvalues.get("Publisher_display") != null)
                        oleItemSearch.setPublisher((String) ((ArrayList) itemvalues.get("Publisher_display")).get(0));
                    else if (itemvalues.get("Publisher_search") != null)
                        oleItemSearch.setPublisher((String) ((ArrayList) itemvalues.get("Publisher_search")).get(0));

                    if (itemvalues.get("uniqueId") != null)
                        oleItemSearch.setBibUUID((String) itemvalues.get("uniqueId"));
                    if (itemvalues.get("instanceIdentifier") != null) {
                        instanceIdentifier = ((String) ((ArrayList) itemvalues.get("instanceIdentifier")).get(0));
                        if (!"".equals(instanceIdentifier) && instanceIdentifier != null) {
                            oleItemSearch.setInstanceUUID(instanceIdentifier);
                            String query = "id:" + instanceIdentifier;
                            List<HashMap<String, Object>> documentList1 = QueryServiceImpl.getInstance().retriveResults(query);
                            if (documentList1.size() > 0) {
                                HashMap<String, Object> itemIDvalues = documentList1.get(0);
                                String itemUUID = (String) ((ArrayList) itemIDvalues.get("itemIdentifier")).get(0);
                                String holdingUUID = (String) ((ArrayList) itemIDvalues.get("holdingsIdentifier")).get(0);
                                oleItemSearch.setItemUUID(itemUUID);
                                oleItemSearch.setHoldingUUID(holdingUUID);
                            }
                        }
                    }

                    if (instanceIdentifier != null) {
                        itemDetails = getItemInformation(instanceIdentifier, searchCriteria, oleItemSearch.getItemUUID());
                        if (itemDetails != null) {
                            LocationValuesBuilder locationValuesBuilder = new LocationValuesBuilder();
                            oleItemSearch.setItemBarCode(itemDetails.getAccessInformation() == null ? "" : itemDetails.getAccessInformation().getBarcode());
                            oleItemSearch.setCallNumber(itemDetails.getCallNumber() == null ? "" : itemDetails.getCallNumber().getNumber());
                            oleItemSearch.setItemType(itemDetails.getItemType() == null ? "" : itemDetails.getItemType().getCodeValue());
                            oleItemSearch.setCopyNumber(itemDetails.getCopyNumber());
                            oleItemSearch.setItemStatus(itemDetails.getItemStatus().getCodeValue());
                            if (itemDetails.getLocation() != null)
                                oleItemSearch.setShelvingLocation(locationValuesBuilder.getShelvingLocation(itemDetails.getLocation()));

                        }

                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    LOG.error("getOleItemListByBibInfo Exception:" + e);
                }
                if (itemDetails != null && !"".equals(oleItemSearch.getTitle()) && oleItemSearch.getTitle() != null)
                    oleItemSearchList.add(oleItemSearch);
            }

        } catch (Exception ex) {
            //LOG.info("Exception ------> " + ex);
            LOG.error("getOleItemListByBibInfo Exception:" + ex);
        }

        return oleItemSearchList;
    }

    private Item getItemInformation(String instanceIdentifier, Map<String, String> searchCriteria, String itemIdentfier) throws Exception {

        String callNumber = (String) searchCriteria.get("callNumber");
        String itemType = (String) searchCriteria.get("itemType");
        String itemBarCode = (String) searchCriteria.get("itemBarCode");

        StringBuffer solrQuery = new StringBuffer();

        if (callNumber != null && !callNumber.equals(""))
            solrQuery.append("(CallNumber_search:" + callNumber.toLowerCase() + "*) AND ");
        if (itemType != null && !itemType.equals(""))
            solrQuery.append("(ItemTypeCodeValue_search:" + itemType.toLowerCase() + ") AND ");
        if (itemBarCode != null && !itemBarCode.equals(""))
            solrQuery.append("(ItemBarcode_display:" + itemBarCode + ") AND ");

        if (!solrQuery.toString().equals("")) {
            solrQuery.append("(id:" + itemIdentfier + ") AND ");
            String query = solrQuery.substring(0, solrQuery.lastIndexOf("AND"));
            List<HashMap<String, Object>> documentList = QueryServiceImpl.getInstance().retriveResults("(DocType:item) AND (" + query + ")");
            if (documentList.size() == 0) {
                return null;
            }
        }
        String itemXml = getItemXML(itemIdentfier);
        Item oleItem = getItemPojo(itemXml);
        if (oleItem != null && ((oleItem.getLocation() == null || oleItem.getCallNumber() == null) || (oleItem.getCallNumber()!=null && StringUtils.isEmpty(oleItem.getCallNumber().getNumber())))) {
            OleHoldings oleHoldings = getOleHoldings(instanceIdentifier);
            oleItem.setLocation(oleHoldings.getLocation());
            oleItem.setCallNumber(oleHoldings.getCallNumber());
            return oleItem;
        }
        if (oleItem != null && (oleItem.getLocation() != null)) {
            if (oleItem != null && oleItem.getLocation().getLocationLevel() == null) {
                OleHoldings oleHoldings = getOleHoldings(instanceIdentifier);
                oleItem.setLocation(oleHoldings.getLocation());
                oleItem.setCallNumber(oleHoldings.getCallNumber());
                return oleItem;
            }
        }
        return oleItem;
    }


    /**
     * Thsi method is to check whether the item is available in doc store or not
     *
     * @param oleDeliverRequestBo
     * @return boolean
     */
    public boolean isItemAvailableInDocStore(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.info("Inside isItemAvailableInDocStore");
        boolean available = false;
        Map<String, String> itemMap = new HashMap<String, String>();
        LocationValuesBuilder locationValuesBuilder = new LocationValuesBuilder();
        try {
            Map<String, String> itemUUIDMap = QueryServiceImpl.getInstance().getItemDetails(oleDeliverRequestBo.getItemId(), null);
            String itemUUID = itemUUIDMap.get(OLEConstants.ITEM_UUID);
            oleDeliverRequestBo.setItemUuid(itemUUID);

            itemMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
            List<OleItemSearch> itemSearchList = getOleItemSearchList(itemMap);
            if (itemSearchList != null && itemSearchList.size() > 0) {
                oleDeliverRequestBo.setTitle(itemSearchList.get(0).getTitle());
                oleDeliverRequestBo.setAuthor(itemSearchList.get(0).getAuthor());
                oleDeliverRequestBo.setCallNumber(itemSearchList.get(0).getCallNumber());
                oleDeliverRequestBo.setItemType(itemSearchList.get(0).getItemType());
            }
            String itemXml = getItemXML(oleDeliverRequestBo.getItemUuid());
            Item oleItem = getItemPojo(itemXml);
            oleDeliverRequestBo.setOleItem(oleItem);
            oleDeliverRequestBo.setCopyNumber(oleItem.getCopyNumber());
            oleDeliverRequestBo.setItemStatus(oleItem.getItemStatus().getCodeValue());
            locationValuesBuilder.getLocation(oleItem, oleDeliverRequestBo, (String) itemUUIDMap.get(OLEConstants.INSTANCE_UUID));
            available = true;
        } catch (Exception e) {
            LOG.error(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC) + e);
        }


        return available;
    }


    public List<OleItemSearch> getOleItemSearchList(Map<String, String> searchCriteria) {

        List<OleItemSearch> oleItemSearchList = new ArrayList<OleItemSearch>();
        StringBuffer query = new StringBuffer("");
        String title = ((String) searchCriteria.get("title"));
        String author = ((String) searchCriteria.get("author"));
        String publisher = ((String) searchCriteria.get("publisher"));
        String callNumber = ((String) searchCriteria.get("callNumber"));
        String itemType = ((String) searchCriteria.get("itemType"));
        String itemBarCode = (String) searchCriteria.get("itemBarCode");
        String itemUuid = (String) searchCriteria.get("itemUuid");


        if (title != null && !title.equals(""))
            query.append("((Title_search:" + title.toLowerCase() + ")OR(Title_search:" + title.toLowerCase() + "*)) AND ");
        if (author != null && !author.equals(""))
            query.append("((Author_search:" + author.toLowerCase() + ")OR(Author_search:" + author.toLowerCase() + "*)) AND ");
        if (publisher != null && !publisher.equals(""))
            query.append("((Publisher_search:" + publisher.toLowerCase() + ")OR(Publisher_search:" + publisher.toLowerCase() + "*)) AND");

        if (!query.toString().equals("")) {
            query = new StringBuffer(query.substring(0, query.lastIndexOf("AND")));
            oleItemSearchList = getOleItemListByBibInfo(query.toString(), searchCriteria);
            return oleItemSearchList;
        }


        if (itemBarCode != null && !itemBarCode.equals(""))
            query.append("(ItemBarcode_display:" + itemBarCode + ") AND ");
        if (callNumber != null && !callNumber.equals(""))
            query.append("((CallNumber_search:" + callNumber.toLowerCase() + ")OR(CallNumber_search:" + callNumber.toLowerCase() + "*)) AND ");
        if (itemType != null && !itemType.equals(""))
            query.append("(ItemTypeCodeValue_search:" + itemType.toLowerCase() + ") AND ");
        if (itemUuid != null && !itemUuid.equals(""))
            query.append("(id:" + itemUuid + ") AND ");

        if (!query.toString().equals("")) {
            query = new StringBuffer(query.substring(0, query.lastIndexOf("AND")));
            oleItemSearchList = getOleItemListByItemInfo(query.toString(), searchCriteria);
            return oleItemSearchList;
        }

        if (query.toString().equals("")) {
            query.append("*:*");
            oleItemSearchList = getOleItemListByBibInfo(query.toString(), searchCriteria);
        }
        return oleItemSearchList;
    }

    public DocumentConfig getDocumentConfigObj() {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_BASE_URL);
        DocumentConfigConverter documentConverter = new DocumentConfigConverter();
        DocumentConfig documentConfig = null;
        try {
            URL url = new URL(docstoreURL + DOC_CONFIG_INFO);
            InputStream in = new BufferedInputStream(url.openStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String docXml = sb.toString();
            documentConfig = documentConverter.unmarshal(docXml);
        } catch (Exception e) {
            LOG.error(e);
        }

        return documentConfig;
    }

    /**
     * @param uuids
     * @param fieldValueList
     * @return
     * @throws SolrServerException
     */
    public boolean checkItemStatus(List<String> uuids, List<String> fieldValueList) throws SolrServerException {
        for (String uuid : uuids) {
            boolean isNotValidItem = QueryServiceImpl.getInstance()
                    .verifyFieldValue(uuid, "ItemStatus_display", fieldValueList);
            if (isNotValidItem) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param instanceIds
     * @param fieldValueList
     * @return
     * @throws SolrServerException
     */
    public boolean checkItemStatusForInstances(List<String> instanceIds, List<String> fieldValueList) throws SolrServerException {

        List<String> itemIds = QueryServiceImpl.getInstance().getItemIdsForInstanceIds(instanceIds);

        return checkItemStatus(itemIds, fieldValueList);
    }


    //OLEINSTANCE SEARCH
    public List<OLEInstanceSearch> getOleInstanceSearchList(Map<String, String> searchCriteria) {

        List<OLEInstanceSearch> oleInstanceSearchList = new ArrayList<OLEInstanceSearch>();
        StringBuffer query = new StringBuffer("");
        String title = ((String) searchCriteria.get("title"));
        String author = ((String) searchCriteria.get("author"));
        String publisher = ((String) searchCriteria.get("publisher"));
        String instanceId = ((String) searchCriteria.get("instanceIdentifier"));

        if (title != null && !title.equals(""))
            query.append("((Title_search:" + title.toLowerCase() + ")OR(Title_search:" + title.toLowerCase() + "*)) AND ");
        if (author != null && !author.equals(""))
            query.append("((Author_search:" + author.toLowerCase() + ")OR(Author_search:" + author.toLowerCase() + "*)) AND ");
        if (publisher != null && !publisher.equals(""))
            query.append("((Publisher_search:" + publisher.toLowerCase() + ")OR(Publisher_search:" + publisher.toLowerCase() + "*)) AND");
        if (instanceId != null && !instanceId.equals(""))
            query.append("((instanceIdentifier:" + instanceId + ")OR(instanceIdentifier:" + instanceId + "*)) AND");

        if (!query.toString().equals("")) {
            query = new StringBuffer(query.substring(0, query.lastIndexOf("AND")));
            oleInstanceSearchList = getOleInstanceListByBibInfo(query.toString(), searchCriteria);
            return oleInstanceSearchList;
        }
        return oleInstanceSearchList;
    }


    private List<OLEInstanceSearch> getOleInstanceListByBibInfo(String solrQuery, Map<String, String> searchCriteria) {

        int rowSize = 10;
        List<OLEInstanceSearch> oleInstanceSearchList = new ArrayList<OLEInstanceSearch>();
        if (searchCriteria.get("rowSize") != null)
            rowSize = Integer.parseInt(searchCriteria.get("rowSize"));
        try {
            List<HashMap<String, Object>> documentList = QueryServiceImpl.getInstance().retriveResults("(DocType:bibliographic) AND " + solrQuery + "", rowSize);

            for (int i = 0; i < documentList.size(); i++) {
                String instanceIdentifier = null;
                String bibIdentifier = null;

                OLEInstanceSearch oleInstanceSearch = new OLEInstanceSearch();
                try {
                    HashMap<String, Object> itemvalues = documentList.get(i);
                    if (itemvalues.get("Title_display") != null)
                        oleInstanceSearch.setTitle((String) ((ArrayList) itemvalues.get("Title_display")).get(0));
                    else if (itemvalues.get("Title_search") != null)
                        oleInstanceSearch.setTitle((String) ((ArrayList) itemvalues.get("Title_search")).get(0));
                    if (itemvalues.get("Author_display") != null)
                        oleInstanceSearch.setAuthor((String) ((ArrayList) itemvalues.get("Author_display")).get(0));
                    else if (itemvalues.get("Author_search") != null)
                        oleInstanceSearch.setAuthor((String) ((ArrayList) itemvalues.get("Author_search")).get(0));
                    if (itemvalues.get("Publisher_display") != null)
                        oleInstanceSearch.setPublisher((String) ((ArrayList) itemvalues.get("Publisher_display")).get(0));
                    else if (itemvalues.get("Publisher_search") != null)
                        oleInstanceSearch.setPublisher((String) ((ArrayList) itemvalues.get("Publisher_search")).get(0));
                    if (itemvalues.get("LocalId_display") != null) {
                        oleInstanceSearch.setLocalId((String) itemvalues.get("LocalId_display"));
                    }
                    if (itemvalues.get("ISSN_display") != null) {
                        oleInstanceSearch.setIssn((String) itemvalues.get("ISSN_display"));
                    }
                    if (itemvalues.get("instanceIdentifier") != null) {
                        ArrayList<String> list = ((ArrayList) itemvalues.get("instanceIdentifier"));
                        oleInstanceSearch.setInstanceIdList(list);
                    }
                    if (itemvalues.get("id") != null) {
                        bibIdentifier = (String) itemvalues.get("id");
                    }
                    oleInstanceSearch.setBibId(bibIdentifier);
                } catch (Exception e) {
                    //e.printStackTrace();
                    LOG.error("getOleInstanceListByBibInfo Exception:" + e);
                }
                oleInstanceSearchList.add(oleInstanceSearch);

            }

        } catch (Exception ex) {
            LOG.info("Exception ------> " + ex);
            LOG.error("getOleInstanceListByBibInfo Exception:" + ex);
        }
        return oleInstanceSearchList;
    }


    public void getInstanceLocation(OLEInstanceSearch instanceSearch) {

        //for getting holdindsidentifier
        String query = "id:" + instanceSearch.getInstanceId();
        List<HashMap<String, Object>> documentList1 = QueryServiceImpl.getInstance().retriveResults(query);
        HashMap<String, Object> itemIDvalues = documentList1.get(0);
        String holdingUUID = itemIDvalues != null && itemIDvalues.get("holdingsIdentifier") != null ? (String) ((ArrayList) itemIDvalues.get("holdingsIdentifier")).get(0) : null;
        String location = "";
        String callNumber = "";
        String copyNumber="";
        //for getting location
        if (holdingUUID != null) {
            String queryForLocation = "id:" + holdingUUID;
            List<HashMap<String, Object>> documentList = QueryServiceImpl.getInstance().retriveResults(queryForLocation);
            HashMap<String, Object> itemlocvalues = documentList != null && documentList.size() > 0 ? documentList.get(0) : null;
            Object locationValue = itemlocvalues.get("Location_display");
            if(locationValue!=null){
                if (locationValue instanceof String) {
                    location= itemlocvalues.get("Location_display").toString();
                } else if (locationValue instanceof List) {
                    location= (String)((ArrayList) itemlocvalues.get("Location_display")).get(0);
                }
            }
            instanceSearch.setLocation(location);
            Object callNumberValue = itemlocvalues.get("CallNumber_display");
            if(callNumberValue!=null){
                if (callNumberValue instanceof String) {
                    callNumber= itemlocvalues.get("CallNumber_display").toString();
                } else if (callNumberValue instanceof List) {
                    callNumber= (String)((ArrayList) itemlocvalues.get("CallNumber_display")).get(0);
                }
            }
            instanceSearch.setCallNumber(callNumber);

            Object value = itemlocvalues.get("CopyNumber_display");
            if(value!=null){
                if (value instanceof String) {
                    copyNumber= itemlocvalues.get("CopyNumber_display").toString();
                } else if (value instanceof List) {
                    copyNumber= (String)((ArrayList) itemlocvalues.get("CopyNumber_display")).get(0);
                }
            }
            instanceSearch.setCopyNumber(copyNumber);
        }
        //   return location;

    }

    public String getContentFromDocStore(String uuid) throws IOException {
        String docStoreResponse = null;
        String restfulUrl = ConfigContext.getCurrentContextConfig().getProperty("docstore.restful.url");
        restfulUrl = restfulUrl.concat("/") + uuid;
        LOG.info("restful url-->" + restfulUrl);
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(restfulUrl);
        client.executeMethod(getMethod);
        InputStream inputStream = getMethod.getResponseBodyAsStream();
        docStoreResponse = IOUtils.toString(inputStream);
        return docStoreResponse;
    }


    /*  public String buildRequestForIngest(String category, String type, String format, String content, AdditionalAttributes additionalAttributes) {

        String reqXml = "";
        Request request = new Request();
        List<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        request.setOperation(Request.Operation.ingest.name());
        request.setUser("editor");
        RequestDocument requestDocument = new RequestDocument();
        Content contentObj = new Content();
        contentObj.setContent(content);
        requestDocument.setContent(contentObj);
        requestDocument.setCategory(category);
        requestDocument.setType(type);
        requestDocument.setFormat(format);
        if (additionalAttributes != null) {
            requestDocument.setAdditionalAttributes(additionalAttributes);
        }
        requestDocuments.add(requestDocument);
        request.setRequestDocuments(requestDocuments);
        reqXml = new RequestHandler().toXML(request);
        return reqXml;
    }

  public String persistDocstore(String reqContent, String uuid) throws Exception {
        String docstoreURL = ConfigContext.getCurrentContextConfig().getProperty(DOCSTORE_URL);

        String queryString = "";
        if (uuid != null) {
            queryString = UPDATE_EXISTING_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(reqContent, "UTF-8");
        } else {
            queryString = CREATE_NEW_DOCSTORE_RECORD_QUERY_STRING + URLEncoder.encode(reqContent, "UTF-8");
        }


        return postData(docstoreURL, queryString);
    }
    public Map getBibIdsForBatchDelete(String searchData, String dataField) {

        String solrQuery = "";
        Map batchDeleteMap = new HashMap();
        List bibIDsList = new ArrayList(0);

        if ("001".equals(dataField)) {
            solrQuery = "id" + ":\"wbm-" + searchData + "\"";
        } else {
            solrQuery = dataField + ":\"" + searchData + "\"";
        }

        LOG.info("solrQuery for batch delete : ---> " + solrQuery);
        try {
            List solrHits = QueryServiceImpl.getInstance().retriveResults(solrQuery);
            if (solrHits != null && solrHits.size() == 0) {
                batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.BIB_REC_NOT_FOUND);
                return batchDeleteMap;
            } else if (solrHits != null && solrHits.size() > 1) {
                batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.MORE_BIB_RECORDS);
                return batchDeleteMap;
            }

            HashMap bibSolrMap = (HashMap) solrHits.get(0);
            if (bibSolrMap.get(OLEConstants.INSTANCE_SEARCH) != null) {
                List instanceIdList = (ArrayList) bibSolrMap.get(OLEConstants.INSTANCE_SEARCH);
                if (instanceIdList != null && instanceIdList.size() > 1) {                //instanceIdList size >1 return null list
                    batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.MORE_INSTANCES);
                    return batchDeleteMap;
                }
                HashMap instanceSolrMap = null;
                List instSolrHits = QueryServiceImpl.getInstance().queryForBibs((String) instanceIdList.get(0));
                if (instSolrHits != null && instSolrHits.size() > 1) {
                    batchDeleteMap.put(OLEConstants.OLEBatchProcess.FAILURE_INFO, OLEConstants.OLEBatchProcess.BIB_BOUNDS_WITH);
                    return batchDeleteMap;
                }
            }
            if (bibSolrMap.get("id") != null) {
                bibIDsList.add(bibSolrMap.get("id"));
                batchDeleteMap.put(OLEConstants.BIB_SEARCH, bibIDsList);
            }


        } catch (Exception e) {
            //e.printStackTrace();
            LOG.error("getBibIdsForBatchDelete Exception:" + e);
        }
        return batchDeleteMap;
    }

    public List<Response> batchDeleteRecords(List bibIdsList) throws Exception {

        List<Response> responseList = new ArrayList<Response>();
        //Request request = buildRequestForDelete(bibIdsList);
        String ids = buildRequestIdsForDelete(bibIdsList);
        Response response = performRestFulOperation(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode(), ids);
        responseList.add(response);
        return responseList;
    } */


    public String buildRequestIdsForDelete(List bibIdsList) {
        StringBuffer ids = new StringBuffer("");
        if (bibIdsList != null && bibIdsList.size() > 0) {
            int i = 0;
            for (; i < bibIdsList.size() - 1; i++) {
                ids.append(bibIdsList.get(i) + ",");
            }
            ids.append(bibIdsList.get(i));

        }
        return ids.toString();
    }

    public Response performRestFulOperation(String docCategory, String docType, String docFormat, String ids) throws Exception {

        String restfulUrl = ConfigContext.getCurrentContextConfig().getProperty("docstore.restful.url");
        restfulUrl = restfulUrl.concat("/") + ids;
        HttpClient httpClient = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(restfulUrl);
        NameValuePair nvp1 = new NameValuePair("identifierType", "UUID");
        NameValuePair nvp2 = new NameValuePair("operation", "delete");
        // NameValuePair nvp3 = new NameValuePair("id", ids);
        NameValuePair category = new NameValuePair("docCategory", docCategory);
        NameValuePair type = new NameValuePair("docType", docType);
        NameValuePair format = new NameValuePair("docFormat", docFormat);
        deleteMethod.setQueryString(new NameValuePair[]{nvp1, nvp2, category, type, format});
        int statusCode = httpClient.executeMethod(deleteMethod);
        LOG.info("statusCode-->" + statusCode);
        InputStream inputStream = deleteMethod.getResponseBodyAsStream();
        String responseXML = IOUtils.toString(inputStream, "UTF-8");
        LOG.info("Response-->" + responseXML);
        return new ResponseHandler().toObject(responseXML);
    }


    public RequestDocument buildRequestDocument(String bibXML, String uuid) {
        RequestDocument requestDocument = new RequestDocument();
        if (StringUtils.isNotEmpty(uuid)) {
            requestDocument.setId(uuid);
            requestDocument.setUuid(uuid);
        } else {
            requestDocument.setId("1");

        }
        requestDocument.setCategory(OLEConstants.WORK_CATEGORY);
        requestDocument.setType(OLEConstants.BIB_DOC_TYPE);
        requestDocument.setFormat(OLEConstants.MARC_FORMAT);
        requestDocument.setContent(new Content(bibXML));

        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        additionalAttributes.setDateEntered(String.valueOf(dateFormat.format(new Date())));
//        additionalAttributes.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
//        additionalAttributes.setFastAddFlag("true");
//        additionalAttributes.setSupressFromPublic("false");
//        additionalAttributes.setAttribute("dateEntered",String.valueOf(dateFormat.format(new Date())));
//        additionalAttributes.setAttribute("lastUpdated",String.valueOf(dateFormat.format(new Date())));
//        additionalAttributes.setAttribute("fastAddFlag","true");
//        additionalAttributes.setAttribute("supressFromPublic","false");
        requestDocument.setAdditionalAttributes(additionalAttributes);

        return requestDocument;
    }

    /* public List getItemIdList(String bibId) throws SolrServerException {

       List itemIdList = QueryServiceImpl.getInstance().queryForItems(bibId);
       return itemIdList;
   }

  public Object getInstanceCollectionData(String instanceUUID) throws Exception {
public List getItemIdsInInstance(String instanceId) throws SolrServerException {
     List<String> instanceIdList = new ArrayList<String>();
     instanceIdList.add(instanceId);
     List itemIdList = QueryServiceImpl.getInstance().getItemIdsForInstanceIds(instanceIdList);
     return itemIdList;
 }

     String responseFromDocstore = getDocstoreData(instanceUUID);
     Object instanceCollection = new InstanceOlemlRecordProcessor().getInstance(responseFromDocstore);
     return instanceCollection;
 }

public String getBibId(String itemId) throws SolrServerException {
     String bibId = QueryServiceImpl.getInstance().queryForBib(itemId);
     return bibId;
 } */
    public Item getPO(String itemXml) throws Exception {

        return getItemPojo(itemXml);
    }

}