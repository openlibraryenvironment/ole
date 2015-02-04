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
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.model.bo.OleDocument;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.businessobject.DocInfoBean;
import org.kuali.ole.select.service.*;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.kuali.ole.docstore.common.client.DocstoreClient;

public class BibInfoServiceImpl implements BibInfoService {

    //private static BibInfoServiceImpl docStoreInstance;
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BibInfoServiceImpl.class);

    protected ConfigurationService configurationService;
    protected WebClientService webClientService;
    protected BibMarcXMLGenerationService bibMarcXMLGenerationService;
    protected ItemMarcXMLGenerationService itemMarcXMLGenerationService;
    protected FileProcessingService fileProcessingService;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private DocstoreClient docstoreClient;

    protected BibInfoServiceImpl() {

    }

/*    public DocstoreClient getDocstoreClient() {
        if (docstoreClient == null) {
            docstoreClient = SpringContext.getBean(DocstoreLocalClient.class);
        }
        return docstoreClient;
    }*/

/*    public static BibInfoServiceImpl getInstance() {
        if (docStoreInstance == null)
            docStoreInstance = new BibInfoServiceImpl();
        return docStoreInstance;
    }*/

    @Override
    public String save(BibInfoBean bibInfoBean) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("inside BibInfoService save..............");
        }
        BibInfoBeanToBibXML bibInfoBeanToBibXML = new BibInfoBeanToBibXML();
        bibInfoBeanToBibXML.exportToXMLFile(bibInfoBean);
        return bibInfoBean.getTitleId();
    }

/*    public String save(BibInfoBean bibInfoBean,HashMap dataMap) throws Exception {
        String urlString = getWebClientPropertyValue("docstoreurl");
        String contentType = getWebClientPropertyValue("contenttype");
        String xmlString = convertBibInfoBeanToMarcXMLString(bibInfoBean,dataMap);
        String data = getFormData(xmlString);
        String response="";
        String responseData="";
        WebClientService webClient = SpringContext.getBean(WebClientServiceImpl.class);
        response=webClient.sendRequest(urlString, contentType, data);
        responseData=webClient.getDataFromResponseXMLForDocStore(response);
        return responseData;
    }*/

    @Override
    public String save(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception {
        LOG.debug("inside BibInfoService save(BibInfoBean bibInfoBean, HashMap dataMap)");
        String xmlString = generateXMLStringForIngest(bibInfoBean, dataMap);
        String userName;
        //Changes to include userId and userAction in docstore ingest.
        if (GlobalVariables.getUserSession() != null) {
            userName = GlobalVariables.getUserSession().getPrincipalName();
        } else {
            userName = configurationService.getPropertyValueAsString("userName");
        }
        xmlString = xmlString.concat("&userId=" + userName + "&userAction=" + bibInfoBean.getRequestSource());
        String encodedXMLString = URLEncoder.encode(xmlString, "UTF-8");
        dataMap.put(OleSelectConstant.DOCSTORE_REQUEST_XMLSTRING, encodedXMLString);
        if (LOG.isDebugEnabled()) {
            LOG.debug("XMLString ------------->>>>>> " + xmlString);
        }
        String response = getDocStoreResponse(dataMap);
        String responseElement = getDocStoreResponseElement(response, dataMap);
        return responseElement;
    }

    @Override
    public List<BibInfoBean> getUUID(List<BibInfoBean> bibInfoBeanList, HashMap dataMap) throws Exception {
        LOG.debug("inside BibInfoService save(BibInfoBean bibInfoBean, HashMap dataMap)");
        String xmlString = generateXMLStringForIngest(bibInfoBeanList, dataMap);
        String userName;
        if (GlobalVariables.getUserSession() != null) {
            userName = GlobalVariables.getUserSession().getPrincipalName();
        } else {
            userName = configurationService.getPropertyValueAsString("userName");
        }
        xmlString = xmlString.concat("&userId=" + userName + "&userAction=" + OleSelectConstant.REQUEST_SRC_TYPE_BATCHINGEST);
        String encodedXMLString = URLEncoder.encode(xmlString, "UTF-8");
        dataMap.put(OleSelectConstant.DOCSTORE_REQUEST_XMLSTRING, encodedXMLString);
        if (LOG.isDebugEnabled()) {
            LOG.debug("XMLString ------------->>>>>> " + xmlString);
        }
        String response = getDocStoreResponse(dataMap);
        if (LOG.isDebugEnabled()) {
            LOG.debug("responseXMLString ------------->>>>>> " + response);
        }
        bibInfoBeanList = getDocStoreResponseElement(response, dataMap, bibInfoBeanList);
        return bibInfoBeanList;
    }

    @Override
    public String getDocStoreResponse(HashMap<String, String> dataMap) throws Exception {
        String urlString = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_URL_KEY_FOR_POS);
        //String urlString = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_URL_KEY);
        String contentType = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_CONTENT_TYPE_KEY);
        if (LOG.isDebugEnabled()) {
            LOG.debug("docstoreurl-------->" + urlString);
        }
        String data = getFormData(dataMap);
        String response = webClientService.sendRequest(urlString, contentType, data);
        return response;
    }

    private String getDocStoreResponseElement(String response, HashMap<String, String> dataMap) throws Exception {
        String responseElement = webClientService.getDataFromResponseXMLForDocStore(response, dataMap);
        return responseElement;
    }

    private List<BibInfoBean> getDocStoreResponseElement(String response, HashMap<String, String> dataMap, List<BibInfoBean> bibInfoBeanList) throws Exception {
        bibInfoBeanList = webClientService.getUUIDFromResponseXMLForDocStore(response, dataMap, bibInfoBeanList);
        return bibInfoBeanList;
    }

    @Override
    public String getDocSearchResponse(BibInfoBean bibInfoBean) throws Exception {
        String urlString = configurationService.getPropertyValueAsString(OLEConstants.DOCSEARCH_URL_KEY);
        if (LOG.isDebugEnabled()) {
            LOG.debug("docstoreurl-------->" + urlString);
        }
        String contentType = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_CONTENT_TYPE_KEY);
        String query = buildDocSearchQuery(bibInfoBean);
        String response = webClientService.sendRequest(urlString, contentType, query);
        return response;
    }

    @Override
    public String convertBibInfoBeanToMarcXMLString(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception {
        String xmlString = bibMarcXMLGenerationService.getMarcXML(bibInfoBean, dataMap);
        //xmlString = OleSelectConstant.CDATA_START_TAG+xmlString+OleSelectConstant.CDATA_END_TAG;
        return xmlString;
    }

    @Override
    public String generateItemMarcXMLString(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception {
        String xmlString = itemMarcXMLGenerationService.getMarcXML(bibInfoBean, dataMap);
        //xmlString = OleSelectConstant.CDATA_START_TAG+xmlString+OleSelectConstant.CDATA_END_TAG;
        return xmlString;
    }

    public String generateInstanceMarcXMLString(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception {
        String xmlString = OLEConstants.INSTANCE_MARC_XML_STRING;
        return xmlString;
    }

    @Override
    public String generateXMLStringForIngest(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception {
        String bibMarcXMLString;
        String itemMarcXMLString;
        if (!dataMap.containsKey(OleSelectConstant.BIB_MARC_XMLSTRING)) {
            bibMarcXMLString = convertBibInfoBeanToMarcXMLString(bibInfoBean, dataMap);
            dataMap.put(OleSelectConstant.BIB_MARC_XMLSTRING, bibMarcXMLString);
        }
        if (!dataMap.containsKey(OleSelectConstant.ITEM_MARC_XMLSTRING)) {
            itemMarcXMLString = generateItemMarcXMLString(bibInfoBean, dataMap);
            dataMap.put(OleSelectConstant.ITEM_MARC_XMLSTRING, itemMarcXMLString);
        }
        if (!dataMap.containsKey(OleSelectConstant.INSTANCE_MARC_XMLSTRING)) {
            itemMarcXMLString = generateInstanceMarcXMLString(bibInfoBean, dataMap);
            dataMap.put(OleSelectConstant.INSTANCE_MARC_XMLSTRING, itemMarcXMLString);
        }
        String requestXMLString = generateRequestXMLString(bibInfoBean, dataMap);
        //requestXMLString = replaceStringWithSymbols(requestXMLString);
        if (LOG.isDebugEnabled()) {
            LOG.debug("requestXMLString ------------->" + requestXMLString);
        }
        return requestXMLString;
    }

    public String generateXMLStringForIngest(List<BibInfoBean> bibInfoBeanList, HashMap<String, String> dataMap) throws Exception {
        String requestXMLString = generateRequestXMLString(bibInfoBeanList, dataMap);
        //requestXMLString = replaceStringWithSymbols(requestXMLString);
        if (LOG.isDebugEnabled()) {
            LOG.debug("requestXMLString ------------->" + requestXMLString);
        }
        return requestXMLString;
    }

    private String replaceStringWithSymbols(String requestXMLString) throws Exception {
        requestXMLString = requestXMLString.replaceAll("&lt;", "<");
        requestXMLString = requestXMLString.replaceAll("&gt;", ">");
        requestXMLString = requestXMLString.replaceAll("&quot;", "\"");
        requestXMLString = requestXMLString.replaceAll("&apos;", "\'");
        //requestXMLString = requestXMLString.replaceAll("&amp;", "#8805");
        return requestXMLString;
    }

    @Override
    public String generateRequestXMLString(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception {
        Request requestObject = new Request();
        int i = 0;
        String bibId = String.valueOf(i++);
        String itemId = String.valueOf(i++);
        if (GlobalVariables.getUserSession() != null) {
            requestObject.setUser(GlobalVariables.getUserSession().getPrincipalName());
        } else {
            requestObject.setUser(configurationService.getPropertyValueAsString("userName"));
        }
        requestObject.setOperation(OleSelectConstant.DOCSTORE_OPERATION_INGEST);
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId(bibId);
        requestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_WORK);
        requestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_BIB);
        requestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_MARC);
        requestDocument.setContent(new Content(dataMap.get(OleSelectConstant.BIB_MARC_XMLSTRING)));

        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setDateEntered(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setFastAddFlag("true");
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setHarvestable("false");
        additionalAttributes.setStatus("n");
        requestDocument.setAdditionalAttributes(additionalAttributes);

        RequestDocument linkedRequestDocument = new RequestDocument();
        linkedRequestDocument.setId(itemId);
        linkedRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_WORK);
        linkedRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_INSTANCE);
        linkedRequestDocument.setContent(new Content(dataMap.get(OleSelectConstant.INSTANCE_MARC_XMLSTRING)));
        linkedRequestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_OLEML);

        ArrayList<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
        linkedRequestDocuments.add(linkedRequestDocument);

        requestDocument.setLinkedRequestDocuments(linkedRequestDocuments);


        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument);
        removeElements(dataMap);
        requestObject.setRequestDocuments(requestDocuments);
        RequestHandler requestHandler = new RequestHandler();
        String requestXMLString = requestHandler.toXML(requestObject);
        return requestXMLString;
    }

/*    public String generateRequestXMLString(List<BibInfoBean> bibInfoBeanList,HashMap<String,String> dataMap)throws Exception{
        Request requestObject = new Request();
        ArrayList<String> bibMarcXMLStringList = new ArrayList<String>();
        ArrayList<String> itemMarcXMLStringList = new ArrayList<String>();
        ArrayList<DocStoreDocument> requestDocuments = new ArrayList<DocStoreDocument>();
        ArrayList<LinkInfo> links = new ArrayList<LinkInfo>();
        Iterator iterator = bibInfoBeanList.iterator();
        int i = 0;
        String bibId;
        String itemId;
        if(GlobalVariables.getUserSession()!=null){
            requestObject.setUser(GlobalVariables.getUserSession().getPrincipalName());
        }else{
            requestObject.setUser(getPropertyValue("userName"));
        }
        requestObject.setOperation(OleSelectConstant.DOCSTORE_OPERATION_BATCHINGEST);
        while (iterator.hasNext()) {
            BibInfoBean bibInfoBean = (BibInfoBean)iterator.next();
            bibId = String.valueOf(i++);
            itemId = String.valueOf(i++);
            String bibMarcXMLString = convertBibInfoBeanToMarcXMLString(bibInfoBean, dataMap);
            String itemMarcXMLString = generateItemMarcXMLString(bibInfoBean, dataMap);
            DocStoreDocument bibRequestDocument = new DocStoreDocument();
            bibInfoBean.setDocStoreIngestionId(itemId);
            bibRequestDocument.setId(bibId);
            bibRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_BIB);
            bibRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_BIB);
            bibRequestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_MARC);
            bibRequestDocument.setContent(bibMarcXMLString);
            DocStoreDocument itemRequestDocument = new DocStoreDocument();
            itemRequestDocument.setId(itemId);
            itemRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_BIB);
            itemRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_ITEM);
            itemRequestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_MARC);
            itemRequestDocument.setContent(itemMarcXMLString);
            requestDocuments.add(bibRequestDocument);
            requestDocuments.add(itemRequestDocument);
            LinkInfo linkInfo = new LinkInfo();
            linkInfo.setFrom(bibId);
            linkInfo.setTo(itemId);
            links.add(linkInfo);
        }
        requestObject.setRequestDocuments(requestDocuments);
        requestObject.setLinks(links);
        RequestHandler requestHandler = new RequestHandler();
        String requestXMLString = requestHandler.toXML(requestObject);
        return requestXMLString;
    }*/

    public String generateRequestXMLString(List<BibInfoBean> bibInfoBeanList, HashMap<String, String> dataMap) throws Exception {
        Request requestObject = new Request();
        ArrayList<String> bibMarcXMLStringList = new ArrayList<String>();
        ArrayList<String> itemMarcXMLStringList = new ArrayList<String>();
        ArrayList<String> instanceMarcXMLStringList = new ArrayList<String>();
        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        ArrayList<LinkInfo> links = new ArrayList<LinkInfo>();
        Iterator bibInfoBeanListIterator = bibInfoBeanList.iterator();
        RequestDocument docStoreDocument;
        int i = 0;
        String bibId;
        String itemId;
        if (GlobalVariables.getUserSession() != null) {
            requestObject.setUser(GlobalVariables.getUserSession().getPrincipalName());
        } else {
            requestObject.setUser(configurationService.getPropertyValueAsString("userName"));
        }
        requestObject.setOperation(OleSelectConstant.DOCSTORE_OPERATION_BATCHINGEST);
        while (bibInfoBeanListIterator.hasNext()) {
            BibInfoBean bibInfoBean = (BibInfoBean) bibInfoBeanListIterator.next();
            String bibMarcXMLString = convertBibInfoBeanToMarcXMLString(bibInfoBean, dataMap);
            //String itemMarcXMLString = generateItemMarcXMLString(bibInfoBean, dataMap);
            String instanceXMLString = generateInstanceMarcXMLString(bibInfoBean, dataMap);

            bibMarcXMLStringList.add(bibMarcXMLString);
            //itemMarcXMLStringList.add(itemMarcXMLString);
            instanceMarcXMLStringList.add(instanceXMLString);

        }
        Iterator bibMarcXMLStringListIterator = bibMarcXMLStringList.iterator();
        Iterator itemMarcXMLStringListIterator = itemMarcXMLStringList.iterator();
        int idCounter = 0;
        bibInfoBeanListIterator = bibInfoBeanList.iterator();
        while (bibMarcXMLStringListIterator.hasNext()) {
            docStoreDocument = getBibDocStoreDocument(bibMarcXMLStringListIterator.next().toString(), String.valueOf(idCounter));
            requestDocuments.add(docStoreDocument);
            BibInfoBean bibInfoBean = (BibInfoBean) bibInfoBeanListIterator.next();
            bibInfoBean.setDocStoreIngestionId(String.valueOf(idCounter));
            idCounter++;
        }
        /*bibInfoBeanListIterator = bibInfoBeanList.iterator();
        while(itemMarcXMLStringListIterator.hasNext()){
            docStoreDocument = getItemDocStoreDocument(itemMarcXMLStringListIterator.next().toString(),String.valueOf(idCounter));
            requestDocuments.add(docStoreDocument);
            BibInfoBean bibInfoBean = (BibInfoBean)bibInfoBeanListIterator.next();
            bibInfoBean.setDocStoreIngestionId(String.valueOf(idCounter));
            idCounter++;
        }*/
        requestObject.setRequestDocuments(requestDocuments);
        links = getLinkInfo(bibInfoBeanList);
        //requestObject.setLinks(links);
        RequestHandler requestHandler = new RequestHandler();
        String requestXMLString = requestHandler.toXML(requestObject);
        return requestXMLString;
    }


    private RequestDocument getBibDocStoreDocument(String xmlString, String id) throws Exception {
        RequestDocument bibRequestDocument = new RequestDocument();
        int itemId = Integer.valueOf(id);
        bibRequestDocument.setId(id);
        /*bibRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_BIB);
        bibRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_BIB);
        bibRequestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_MARC);
        bibRequestDocument.setContent(new Content(xmlString));*/
        bibRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_WORK);
        bibRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_BIB);
        bibRequestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_MARC);
        bibRequestDocument.setContent(new Content(xmlString));
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setDateEntered(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setFastAddFlag("true");
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setHarvestable("false");
        additionalAttributes.setStatus("n");
        bibRequestDocument.setAdditionalAttributes(additionalAttributes);

        RequestDocument linkedRequestDocument = new RequestDocument();
        linkedRequestDocument.setId(String.valueOf(++itemId));
        linkedRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_WORK);
        linkedRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_INSTANCE);
        linkedRequestDocument.setContent(new Content(OLEConstants.INSTANCE_MARC_XML_STRING));
        linkedRequestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_OLEML);

        ArrayList<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
        linkedRequestDocuments.add(linkedRequestDocument);

        bibRequestDocument.setLinkedRequestDocuments(linkedRequestDocuments);
        return bibRequestDocument;
    }

    private RequestDocument getItemDocStoreDocument(String xmlString, String id) throws Exception {
        RequestDocument bibRequestDocument = new RequestDocument();
        int itemId = Integer.valueOf(id);
        bibRequestDocument.setId(id);
        bibRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_WORK);
        bibRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_BIB);
        bibRequestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_MARC);
        bibRequestDocument.setContent(new Content(xmlString));
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setDateEntered(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
        additionalAttributes.setFastAddFlag("true");
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setHarvestable("false");
        additionalAttributes.setStatus("n");
        bibRequestDocument.setAdditionalAttributes(additionalAttributes);

        RequestDocument linkedRequestDocument = new RequestDocument();
        linkedRequestDocument.setId(String.valueOf(++itemId));
        linkedRequestDocument.setCategory(OleSelectConstant.DOCSTORE_CATEGORY_WORK);
        linkedRequestDocument.setType(OleSelectConstant.DOCSTORE_TYPE_INSTANCE);
        linkedRequestDocument.setContent(new Content(OLEConstants.INSTANCE_MARC_XML_STRING));
        linkedRequestDocument.setFormat(OleSelectConstant.DOCSTORE_FORMAT_OLEML);

        ArrayList<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
        linkedRequestDocuments.add(linkedRequestDocument);
        return bibRequestDocument;
    }

    private ArrayList<LinkInfo> getLinkInfo(List<BibInfoBean> bibInfoBeanList) throws Exception {
        ArrayList<LinkInfo> links = new ArrayList<LinkInfo>();
        for (int i = 0; i < bibInfoBeanList.size(); i++) {
            LinkInfo linkInfo = new LinkInfo();
            linkInfo.setFrom(String.valueOf(i));
            linkInfo.setTo(bibInfoBeanList.get(i).getDocStoreIngestionId());
            links.add(linkInfo);
        }
        return links;
    }

    private void removeElements(HashMap<String, String> dataMap) throws Exception {
        dataMap.remove(OleSelectConstant.BIB_MARC_XMLSTRING);
        dataMap.remove(OleSelectConstant.ITEM_MARC_XMLSTRING);
    }

    private Properties loadPropertiesFromClassPath(String classPath) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource(classPath);
        Properties properties = new Properties();
        try {
            properties.load(classPathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Invalid class path: " + classPath, e);
        }
        return properties;
    }

    public String getFormData(HashMap<String, String> dataMap) throws Exception {
        String postdata = "";
        if (dataMap.containsKey(OleSelectConstant.IS_BIB_EDIT)) {
            if (dataMap.get(OleSelectConstant.IS_BIB_EDIT).equalsIgnoreCase("true")) {
                postdata = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_POST_DATA_EDIT_KEY);
                postdata = postdata + "&uuid=" + dataMap.get(OleSelectConstant.TITLE_ID) + "&fileContent=";
            } else {
                postdata = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_POST_DATA_KEY);
            }
        } else {
            postdata = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_POST_DATA_KEY);
            postdata = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_APP_POST_DATA_KEY);
        }
        postdata = postdata + dataMap.get(OleSelectConstant.DOCSTORE_REQUEST_XMLSTRING);
        return postdata;
    }

    public BibInfoBean retrieve(String titleId) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("inside BibInfoService retrieve-- titleid----------->" + titleId);
        }
        XPathFactory xFactory = XPathFactory.newInstance();
        XPath xpath = xFactory.newXPath();
        XPathExpression expr = xpath.compile("//bibData[titleId='" + titleId + "']");
        Object result = expr.evaluate(parseDocStoreContent(), XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        BibInfoBean bibInfoBean = new BibInfoBean();
        if (nodes.getLength() > 0) {
            Node node = nodes.item(0);
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node tempNode = list.item(i);
                convertToBean(bibInfoBean, tempNode.getNodeName(), tempNode.getTextContent());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("node name--------->" + tempNode.getNodeName());
                    LOG.debug("node text--------->" + tempNode.getTextContent());
                }
            }
        }
        return bibInfoBean;
    }

    //public BibInfoBean retrieveFromDocStore(String titleId) throws Exception {
    @Override
    public BibInfoBean retrieveFromDocStore(HashMap<String, String> dataMap) throws Exception {
        BibInfoBean bibInfoBean = new BibInfoBean();
        bibInfoBean.setTitleId(dataMap.get(OleSelectConstant.TITLE_ID));
        bibInfoBean.setDocCategoryType(dataMap.get(OleSelectConstant.DOC_CATEGORY_TYPE));
        List<BibInfoBean> bibInfoBeanList = searchBibInfo(bibInfoBean);
        if (LOG.isDebugEnabled()) {
            LOG.debug("bibInfoBeanList.size----------->" + bibInfoBeanList.size());
        }
        for (int i = 0; i < bibInfoBeanList.size(); i++) {
            if (bibInfoBeanList.get(i).getTitle() != null) {
                bibInfoBean = bibInfoBeanList.get(i);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("title---------" + i + "->" + bibInfoBeanList.get(i).getTitle());
            }
        }
/*        for (Iterator<BibInfoBean> iterator = bibInfoBeanList.iterator(); iterator.hasNext(); ) {
            BibInfoBean infoBean = iterator.next();
            if (null != infoBean.getTitle()) {
                return bibInfoBean;
            }
        }*/
/*        if(bibInfoBeanList.iterator().hasNext())
            bibInfoBean = bibInfoBeanList.iterator().next();*/
        return bibInfoBean;
    }

    private void convertToBean(BibInfoBean bibInfoBean, String nodeName, String textContent) {
        if ("titleId".equals(nodeName)) {
            bibInfoBean.setTitleId(textContent);
        } else if ("title".equals(nodeName)) {
            bibInfoBean.setTitle(textContent);
        } else if ("author".equals(nodeName)) {
            bibInfoBean.setAuthor(textContent);
        } else if ("edition".equals(nodeName)) {
            bibInfoBean.setEdition(textContent);
        } else if ("standardNumber".equals(nodeName)) {
            bibInfoBean.setStandardNumber(textContent);
        } else if ("publisher".equals(nodeName)) {
            bibInfoBean.setPublisher(textContent);
        } else if ("placeOfPublication".equals(nodeName)) {
            bibInfoBean.setPlaceOfPublication(textContent);
        } else if ("yearOfPublication".equals(nodeName)) {
            bibInfoBean.setYearOfPublication(textContent);
        } else if ("physicalDescription".equals(nodeName)) {
            bibInfoBean.setPhysicalDescription(textContent);
        } else if ("format".equals(nodeName)) {
            bibInfoBean.setFormat(textContent);
        } else if ("series".equals(nodeName)) {
            bibInfoBean.setSeries(textContent);
        } else if ("subjects".equals(nodeName)) {
            bibInfoBean.setSubjects(textContent);
        } else if ("price".equals(nodeName)) {
            bibInfoBean.setPrice(textContent);
        } else if ("requestorContact".equals(nodeName)) {
            bibInfoBean.setRequestorContact(textContent);
        } else if ("requestersNotes".equals(nodeName)) {
            bibInfoBean.setRequestersNotes(textContent);
        } else if ("noOfCopies".equals(nodeName)) {
            bibInfoBean.setNoOfCopies(textContent);
        } else if ("category".equals(nodeName)) {
            bibInfoBean.setCategory(textContent);
        } else if ("requestSource".equals(nodeName)) {
            bibInfoBean.setRequestSource(textContent);
        } else if ("selector".equals(nodeName)) {
            bibInfoBean.setSelector(textContent);
        } else if ("selectorNotes".equals(nodeName)) {
            bibInfoBean.setSelectorNotes(textContent);
        } else if ("startPage".equals(nodeName)) {
            if (textContent != null && !"".equals(textContent)) {
                bibInfoBean.setStartPage(Long.valueOf(textContent));
            }
        } else if ("endPage".equals(nodeName)) {
            if (textContent != null && !"".equals(textContent)) {
                bibInfoBean.setEndPage(Long.valueOf(textContent));
            }
        }
    }

    private List<BibInfoBean> convertToBibInfoBeanList(List<DocInfoBean> docInfoBeanList) throws Exception {
        List<BibInfoBean> bibInfoBeanList = new ArrayList<BibInfoBean>();
        for (DocInfoBean docInfoBean : docInfoBeanList) {
            BibInfoBean bibInfoBean = new BibInfoBean();
            bibInfoBean.setTitleId(manipulateStringValue(docInfoBean.getTitleId()));
            bibInfoBean.setTitle(manipulateStringValue(docInfoBean.getTitle_display()));
            bibInfoBean.setAuthor(manipulateStringValue(docInfoBean.getAuthor_display()));
            bibInfoBean.setPublisher(manipulateStringValue(docInfoBean.getPublisher_display()));
            bibInfoBean.setIsbn(manipulateStringValue(docInfoBean.getIsbn_display()));
            bibInfoBean.setYearOfPublication(manipulateStringValue(docInfoBean.getYearOfPublication()));
            bibInfoBeanList.add(bibInfoBean);
        }
        return bibInfoBeanList;
    }

    private String manipulateStringValue(String value) throws Exception {
        if (value != null) {
            value = value.trim();
            value = value.replace("||", "");
        }
        return value;
    }

    private Document parseDocStoreContent() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        String externalDirectory = configurationService.getPropertyValueAsString(OLEConstants.STAGING_DIRECTORY_KEY);
        String fileName = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_FILE_KEY);
        if (LOG.isInfoEnabled()) {
            LOG.info("parseDocStoreContent externalDirectory------------>" + externalDirectory);
            LOG.info("parseDocStoreContent fileName------------>" + fileName);
        }
        File file = new File(externalDirectory + fileName);
        if (!file.exists()) {
            return null;
        }
        Document doc = builder.parse(file.getAbsolutePath());
        return doc;
    }

    @Override
    public boolean isExists(HashMap map) throws Exception {
        String externalDirectory = configurationService.getPropertyValueAsString(OLEConstants.STAGING_DIRECTORY_KEY);
        String fileName = configurationService.getPropertyValueAsString(OLEConstants.DOCSTORE_FILE_KEY);
        if (LOG.isInfoEnabled()) {
            LOG.info("Doc Store file Path :" + externalDirectory + fileName);
        }
        File file = new File(externalDirectory + fileName);
        if (!file.exists()) {
            return false;
        }
        if (map.size() == 0) {
            return false;
        }
        XPathFactory xFactory = XPathFactory.newInstance();
        XPath xpath = xFactory.newXPath();
        StringBuilder sBuff = new StringBuilder("//bibData");
        Set set = map.keySet();
        Iterator<String> setIt = set.iterator();
        String value = null;
        while (setIt.hasNext()) {
            String key = setIt.next();
            //sBuff.append("[" + key + "='" + map.get(key) + "']");
            value = (String) map.get(key);
            if (value.indexOf("\"") != -1) {
                sBuff.append("[translate(" + key + ",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=translate('" + map.get(key) + "','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')]");
            } else {
                sBuff.append("[translate(" + key + ",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=translate(\"" + map.get(key) + "\",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')]");
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("XPath expr :" + sBuff.toString());
        }
        XPathExpression expr = xpath.compile(sBuff.toString());
        Object result = expr.evaluate(parseDocStoreContent(), XPathConstants.NODESET);
        NodeList nodeList = (NodeList) result;
        if (LOG.isDebugEnabled()) {
            LOG.debug("NodeList length :" + nodeList.getLength());
        }
        if (nodeList.getLength() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isDuplicateRecord(BibInfoBean bibInfoBean) throws Exception {
        List<DocInfoBean> docInfoBeanList = search(bibInfoBean);
        for (DocInfoBean docInfoBean : docInfoBeanList) {
            if (bibInfoBean.getTitle().trim().equalsIgnoreCase(docInfoBean.getTitle_display().trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BibInfoBean> searchBibInfo(BibInfoBean bibInfoBean) throws Exception {
        List<DocInfoBean> docInfoBeanList = search(bibInfoBean);
        List<BibInfoBean> bibInfoBeanList = convertToBibInfoBeanList(docInfoBeanList);
        return bibInfoBeanList;
    }

    public List<DocInfoBean> search(BibInfoBean bibInfoBean) throws Exception {
        List<DocInfoBean> docInfoBeanList;
        String title = bibInfoBean.getTitle();
        if (title != null) {
            title = title.replaceAll(" ", "%20");
        }
        String query = buildDocSearchQuery(bibInfoBean);
        docInfoBeanList = getResponse(query);
        if (LOG.isDebugEnabled()) {
            LOG.debug("docsearch query------>" + query);
        }
        return docInfoBeanList;
    }


    /**
     * This method takes map as the parameter takes values in map and generate docstore query
     *
     * @param map
     * @return
     */
    public String buildDocSearchQuery(Map map) {
        LOG.debug(" BibInfoServiceImpl.buildDocSearchQuery(Map map) method starts ");
        StringBuilder query = new StringBuilder();
        query.append("q=");
        Set set = map.keySet();
        Iterator setIterator = set.iterator();
        String key = null;
        String value = null;
        String operator = null;
        int count = 0;
        while (setIterator.hasNext()) {
            key = (String) setIterator.next();
            //value = (String) map.get(key);
            value = map.get(key).toString().toLowerCase();
            if (OleSelectConstant.DocStoreDetails.DOCSTORE_QUERY_KEYS.containsKey(key)) {
                if (!OleSelectConstant.DocStoreDetails.DOCSTORE_QUERY_KEYS.get(key).equals(OleSelectConstant.DocStoreDetails.ITEMLINKS_VALUE)) {
                    if (value.indexOf("\"") == 0 && value.lastIndexOf("\"") == value.length() - 1) {
                        operator = " AND ";
                    } else {
                        operator = " OR ";
                    }
                    value = value.replaceAll("[^a-zA-Z 0-9*]+", "");
                    value = value.replaceAll("\\s+", " ");
                    value = value.trim().replace(" ", operator);
                }
                if (count == 0) {
                    query.append("(" + OleSelectConstant.DocStoreDetails.DOCSTORE_QUERY_KEYS.get(key) + ":(\"" + value + "\"))");
                } else {
                    query.append("AND(" + OleSelectConstant.DocStoreDetails.DOCSTORE_QUERY_KEYS.get(key) + ":(\"" + value + "\"))");
                }
                count++;
            }
        }
        query.append("&fl=instanceIdentifier,uniqueId,bibIdentifier,Title_display,Author_display,PublicationDate_search,ISBN_display,Publisher_search");
        int noOfRows = Integer.parseInt(SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSEARCH_LIMIT_KEY));
        query.append("&rows=" + noOfRows);
        if (GlobalVariables.getUserSession() != null) {
            query.append("&userId=" + GlobalVariables.getUserSession().getPerson().getPrincipalName());
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("docsearch query1------>" + query.toString());
            LOG.debug(" BibInfoServiceImpl.buildDocSearchQuery(Map map) method ends ");
        }
        return query.toString();
    }

    private String buildDocSearchQuery(BibInfoBean bibInfoBean) throws Exception {
        StringBuilder query = new StringBuilder();
        String id = "instanceIdentifier";
        if (bibInfoBean.getDocCategoryType() != null) {
            id = bibInfoBean.getDocCategoryType();
        }
        query.append("q=");
        if (bibInfoBean.getTitleId() != null) {
            query.append("(" + id + ":" + bibInfoBean.getTitleId() + ")");
        } else {
            query.append("(Title_display:" + bibInfoBean.getTitle() + ")");
            if (bibInfoBean.getAuthor() != null && !StringUtils.isEmpty(bibInfoBean.getAuthor())) {
                query.append("AND(Author_display:" + bibInfoBean.getAuthor() + ")");
            }
            if (bibInfoBean.getTypeOfStandardNumber() != null) {
                if (bibInfoBean.getTypeOfStandardNumber().equalsIgnoreCase("ISBN")) {
                    query.append("AND(ISBN_display:" + bibInfoBean.getStandardNumber() + ")");
                } else if (bibInfoBean.getTypeOfStandardNumber().equalsIgnoreCase("ISSN")) {
                    query.append("AND(ISSN_display:" + bibInfoBean.getTypeOfStandardNumber() + ")");
                }
            }
        }
        query.append("&fl=" + id + ",Title_display,Author_display,YearOfPublication,ISBN_display,Publisher_display");
        int noOfRows = Integer.parseInt(SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSEARCH_LIMIT_KEY));
        query.append("&rows=" + noOfRows);
        // Changes to include userId in docstore URl.
        if (GlobalVariables.getUserSession() != null) {
            query.append("&userId=" + GlobalVariables.getUserSession().getPerson().getPrincipalName());
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("docsearch query------>" + query.toString());
        }
        return query.toString();
    }

    private List<DocInfoBean> getResponse(String query) {
        LOG.debug(" BibInfoServiceImpl.getResponse method starts ");
        List<DocInfoBean> docInfoBeanList = new ArrayList<DocInfoBean>(0);
        BuildDocInfoBean buildVendorDocInfoBean = new BuildDocInfoBean();
        docInfoBeanList = buildVendorDocInfoBean.getDocInfoBeanList(query);
        LOG.debug(" BibInfoServiceImpl.getResponse method ends ");
        return docInfoBeanList;
    }

    /**
     * @see org.kuali.ole.select.service.BibInfoService#search(java.util.Map)
     */
    @Override
    public List search(Map map) throws Exception {
        LOG.debug(" BibInfoServiceImpl.search(Map map) method starts ");
        List<DocInfoBean> docInfoBeanList;
        String query = buildDocSearchQuery(map);
        docInfoBeanList = getResponse(query);
        LOG.debug(" BibInfoServiceImpl.search(Map map) method ends ");
        return docInfoBeanList;
    }

    @Override
    public List search(HashMap map, int noOfRecords) throws Exception {
        List<BibInfoBean> bibInfoBeanList = new ArrayList<BibInfoBean>();
        XPathFactory xFactory = XPathFactory.newInstance();
        XPath xpath = xFactory.newXPath();
        StringBuilder stringExpression = new StringBuilder("//bibData");
        Set set = map.keySet();
        Iterator<String> setIt = set.iterator();
        String value = null;
        while (setIt.hasNext()) {
            String key = setIt.next();
            //sBuff.append("[" + key + "='" + map.get(key) + "']");
            value = (String) map.get(key);
            if (value.indexOf("\"") != -1) {
                stringExpression.append("[translate(" + key + ",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=translate('" + map.get(key) + "','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')]");
            } else {
                stringExpression.append("[translate(" + key + ",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')=translate(\"" + map.get(key) + "\",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')]");
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("XPath expr :" + stringExpression.toString());
        }
        XPathExpression expr = xpath.compile(stringExpression.toString());
        Object result = expr.evaluate(parseDocStoreContent(), XPathConstants.NODESET);
        NodeList nodeList = (NodeList) result;

        BibInfoBean bibInfoBean;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList list = node.getChildNodes();
            bibInfoBean = new BibInfoBean();
            for (int j = 0; j < list.getLength(); j++) {
                Node tempNode = list.item(j);
                convertToBean(bibInfoBean, tempNode.getNodeName(), tempNode.getTextContent());
            }
            bibInfoBeanList.add(bibInfoBean);
            if (i == (noOfRecords - 1)) {
                break;
            }
        }

        return bibInfoBeanList;
    }

    @Override
    public String getTitleIdByMarcXMLFileProcessing(BibInfoBean bibInfoBean, HashMap<String, String> dataMap) throws Exception {
        String titleId = null;
        BibInfoBean xmlBibInfoBean = new BibInfoBean();
        FileProcessingService fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        String bibMarcXmlString = fileProcessingService.getFileContentAndDeleteFile(dataMap).get(OleSelectConstant.XML_FILE_CONTENT);
        if (bibMarcXmlString != null) {
            //bibMarcXmlString = OleSelectConstant.CDATA_START_TAG + bibMarcXmlString + OleSelectConstant.CDATA_END_TAG;
            dataMap.put(OleSelectConstant.BIB_MARC_XMLSTRING, bibMarcXmlString);
            dataMap.put(OleSelectConstant.DOC_CATEGORY_TYPE, OleSelectConstant.DOC_CATEGORY_TYPE_ITEM);
            titleId = save(bibInfoBean, dataMap);
        }
        return titleId;
    }


    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.configurationService = kualiConfigurationService;
    }

    public WebClientService getWebClientService() {
        return webClientService;
    }

    public void setWebClientService(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    public BibMarcXMLGenerationService getBibMarcXMLGenerationService() {
        return bibMarcXMLGenerationService;
    }

    public void setBibMarcXMLGenerationService(BibMarcXMLGenerationService bibMarcXMLGenerationService) {
        this.bibMarcXMLGenerationService = bibMarcXMLGenerationService;
    }

    public ItemMarcXMLGenerationService getItemMarcXMLGenerationService() {
        return itemMarcXMLGenerationService;
    }

    public void setItemMarcXMLGenerationService(ItemMarcXMLGenerationService itemMarcXMLGenerationService) {
        this.itemMarcXMLGenerationService = itemMarcXMLGenerationService;
    }

    public FileProcessingService getFileProcessingService() {
        return fileProcessingService;
    }

    public void setFileProcessingService(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @Override
    public List<DocInfoBean> getResult(List isbnList) throws Exception {

        List<DocInfoBean> docInfoBeanList = new ArrayList<DocInfoBean>(0);
        StringBuilder query = new StringBuilder("q=");
        query.append("(");
        HashMap titleIdMap = new HashMap();
        for (int i = 0; i < isbnList.size(); i++) {
            if (isbnList.get(i) != null && !("".equals(isbnList.get(i)))) {
                query.append("(ISBN_display:" + isbnList.get(i) + ")");
            }
        }
        query.append(")");
        // Changes to include userId in docstore URl.
        if (GlobalVariables.getUserSession() != null) {
            query.append("&userId=" + GlobalVariables.getUserSession().getPerson().getPrincipalName());
        }
        if (isbnList.size() > 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Doc Store Query :" + query.toString());
            }
            docInfoBeanList = getResponse(query.toString());
        }
        if (docInfoBeanList.size() > 0) {
            String maxLimit = docInfoBeanList.get(0).getNoOfRecords();
            query.append("&fl=uniqueId,bibIdentifier,ISBN_display");
            query.append("&rows=" + maxLimit);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Doc Store Query :" + query.toString());
            }
            if (isbnList.size() > 0) {
                docInfoBeanList = getResponse(query.toString());
            }
        }
        return docInfoBeanList;
    }
//    public String getWebClientPropertyValue(String key)throws Exception{
//        if ( LOG.isDebugEnabled() ) {
//            LOG.debug("Webclient property key - "+key);
//        }
//        Properties properties = PropertyUtil.loadPropertiesFromClassPath("org/kuali/ole/select/service/impl/webclient.properties");
//        if ( LOG.isDebugEnabled() ) {
//            LOG.debug("Properties loaded from classpath - "+properties);
//        }
//        if(OLEConstants.WebclientProperties.DOCSEARCH_URL.equals(key) || OLEConstants.WebclientProperties.DOCSTORE_URL.equals(key)){
//            key = OLEConstants.WebclientProperties.WEBCLIENT_PROPERTIES.get(key);
//        }
//        if ( LOG.isDebugEnabled() ) {
//            LOG.debug("Final key for which value has to be retrieved - "+key);
//        }
//        String propertyValue = properties.getProperty(key);
//        if ( LOG.isDebugEnabled() ) {
//            LOG.debug("Value retrieved - "+propertyValue);
//        }
//        return propertyValue;
//    }

    public BibInfoBean retrieveFromSolrQuery(Map map) throws Exception {
        String key = null;
        String value = null;
        OleDocument oleDocument = new WorkBibDocument();
        WorkInstanceDocument workInstance = new WorkInstanceDocument();
        Iterator iterator = map.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        String id = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            value = (String) map.get(key);
            if (map.get("instanceIdentifier") != null) {
                workInstance.setInstanceIdentifier((String) map.get("instanceIdentifier"));
                ((org.kuali.ole.docstore.model.bo.WorkBibDocument) oleDocument).setInstanceDocument(workInstance);
                id = (String) map.get("instanceIdentifier");
            }
        }
        String queryString = "instanceIdentifier:" + id;
        SolrRequestReponseHandler solrResponse = new SolrRequestReponseHandler();
        List<HashMap<String, Object>> bibInfo = solrResponse.retriveResults(queryString);
        BibInfoBean bibInfoList = setBibInfoBean(bibInfo);
        return bibInfoList;
    }

/*    public BibInfoBean retrieveFromSolrQueryNew(Map map) throws Exception {
        org.kuali.ole.docstore.common.document.Bib bib = getDocstoreClient().findBib(map);
        BibInfoBean bibInfoList = setBibInfoBeanNew(bib);
        return bibInfoList;
    }*/


    public BibInfoBean setBibInfoBean(List<HashMap<String, Object>> bibInfo) {
        BibInfoBean bibInfoBean = new BibInfoBean();
        Iterator itr = bibInfo.iterator();
        String author = null;
        String title = null;
        String publisher = null;
        String isbn = null;
        while (itr.hasNext()) {
            HashMap<String, Object> resultMap = (HashMap<String, Object>) itr.next();
            if (resultMap.get("Author_display") != null) {
                author = (String) resultMap.get("Author_display").toString();
                author = author.replace('[', ' ').replace(']', ' ');
            }
            if (resultMap.get("Title_display") != null) {
                title = (String) resultMap.get("Title_display").toString();
                title = title.replace('[', ' ').replace(']', ' ');
            }
            if (resultMap.get("Publisher_display") != null) {
                publisher = (String) resultMap.get("Publisher_display").toString();
                publisher = publisher.replace('[', ' ').replace(']', ' ');
            }
            if (resultMap.get("ISBN_display") != null) {
                isbn = (String) resultMap.get("ISBN_display").toString();
                isbn = isbn.replace('[', ' ').replace(']', ' ');
            }
        }
        bibInfoBean.setAuthor(author);
        bibInfoBean.setTitle(title);
        bibInfoBean.setPublisher(publisher);
        bibInfoBean.setIsbn(isbn);

        return bibInfoBean;
    }
    public BibInfoBean setBibInfoBeanNew(Bib bib) {
        BibInfoBean bibInfoBean = new BibInfoBean();
        bibInfoBean.setTitle(bib.getTitle());
        bibInfoBean.setAuthor(bib.getAuthor());
        bibInfoBean.setIsbn(bib.getIsbn());
        return bibInfoBean;
    }
}
