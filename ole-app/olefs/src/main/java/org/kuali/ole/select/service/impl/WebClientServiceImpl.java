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

import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.service.WebClientService;
import org.kuali.ole.select.service.impl.exception.DocStoreConnectionException;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class WebClientServiceImpl implements WebClientService {

    private URL url;
    private URLConnection conn;
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(WebClientServiceImpl.class);


    public String sendRequest(String urlString, String contentType, String data) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("inside sendRequest");
            LOG.debug("urlString----------------->" + urlString);
        }
        String response = "";
        boolean success = true;
        // Modified by Aditya sai prasad for jira OLE-2411 starts.
        try {
            // OleUrlPing oleUrlPing = new OleUrlPing();
            // String msg = oleUrlPing.urlPing(urlString);
            createConnection(urlString, contentType);
            response = postData(data);
        } catch (DocStoreConnectionException dsce) {
            success = false;
            GlobalVariables.getMessageMap().putError("error.requisition.docstore.connectionError", RiceKeyConstants.ERROR_CUSTOM, "Docstore Server Unavailable.");
        }
        
       /* if(success){
            createConnection(urlString, contentType);
            response = postData(data);
        }*/

        // Modified by Aditya sai prasad for jira OLE-2411 ends.

        return response;
    }

    private void createConnection(String urlString, String contentType) throws Exception {
        LOG.debug("inside createConnection");
        url = new URL(urlString);
        conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", contentType);

    }

    private String postData(String data) {
        LOG.debug("inside postData");
        String response = "";
        try {
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            // Read response from the input stream.
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String temp;
            while ((temp = in.readLine()) != null) {
                response += temp + "\n";
            }
            temp = null;
            in.close();
            if (LOG.isDebugEnabled()) {
                LOG.debug("response from docstore---------->" + response);
            }
        } catch (Exception e) {
            LOG.error("Exception while connecting to document storage server.", e);
            throw new DocStoreConnectionException("Error while connecting to document storage server, contact system administrator.", e);
        }
        return response;
    }
    
/*    public String getDataFromResponseXMLForDocStore(String response) throws Exception {
        String data = null;
        if(response!=null){
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inStream = new InputSource();
            inStream.setCharacterStream(new StringReader(response));
            Document doc = db.parse(inStream);
            NodeList nodeList = doc.getElementsByTagName("response");
            for (int index = 0; index < nodeList.getLength(); index++) {
                Node node = nodeList.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    NodeList dataNode = element.getElementsByTagName("item");
                    for (int iIndex = 0; iIndex < dataNode.getLength(); iIndex++) {
                        if (dataNode.item(iIndex).getNodeType() == Node.ELEMENT_NODE) {
                            Element dataElement = (Element) dataNode.item(iIndex);
                            if ( LOG.isDebugEnabled() ) {
                                LOG.info("response data vale = " + dataElement.getFirstChild().getNodeValue().trim());
                            }
                            data = dataElement.getFirstChild().getNodeValue().trim();
                        }
                    }
                }
            }
        }
        return data;
    }*/

    public String getDataFromResponseXMLForDocStore(String response, HashMap<String, String> dataMap) throws Exception {
        StringBuilder uuid = new StringBuilder();
        //ResponseHandler docStoreResponseHandlerObject = new ResponseHandler();
        ResponseHandler docStoreResponseHandlerObject = SpringContext.getBean(ResponseHandler.class);
        Response docStoreResponseObject = docStoreResponseHandlerObject.toObject(response);
        List<ResponseDocument> requestDocuments = docStoreResponseObject.getDocuments();
        Iterator docStoreDocumentIterator = requestDocuments.iterator();
        String instanceType;
        String uuidType = "";
        if (dataMap.containsKey(OleSelectConstant.DOC_CATEGORY_TYPE))
            uuidType = dataMap.get(OleSelectConstant.DOC_CATEGORY_TYPE);
        while (docStoreDocumentIterator.hasNext()) {
            ResponseDocument docStoreDocument = (ResponseDocument) docStoreDocumentIterator.next();
            if (docStoreDocument.getUuid() != null) {
                uuid.append(docStoreDocument.getUuid());
            }
          /*  List<ResponseDocument> linkedInstanceDocuments = docStoreDocument.getLinkedDocuments();
            Iterator linkedInstanceDocumentIterator = linkedInstanceDocuments.iterator();
            while(linkedInstanceDocumentIterator.hasNext()) {
                ResponseDocument linkedInstanceDocument = (ResponseDocument)linkedInstanceDocumentIterator.next();
                //uuid.append(getUUIDFromLinkedResponseDocument(linkedInstanceDocument, uuidType)); 
                //uuid.append(":" + linkedInstanceDocument.getUuid());
                String type = linkedInstanceDocument.getType();
                String format = linkedInstanceDocument.getFormat();
                if(uuidType != null) {
                    if((uuidType.equalsIgnoreCase(OleSelectConstant.DOC_CATEGORY_TYPE_ITEM)) &&
                            (type.equalsIgnoreCase(OleSelectConstant.DOCSTORE_TYPE_INSTANCE)) &&
                            (format.equalsIgnoreCase(OleSelectConstant.DOCSTORE_FORMAT_OLEML))) {
                        uuid.append(linkedInstanceDocument.getUuid());
                    }
                }
            }*/

            if (LOG.isDebugEnabled())
                LOG.debug(docStoreDocument.getType() + " uuid---------->" + docStoreDocument.getUuid());
        }
        return uuid.toString();
    }

    private String getUUIDFromLinkedResponseDocument(ResponseDocument linkedInstanceDocument, String uuidType) {
        String instanceType = linkedInstanceDocument.getType();
        List<ResponseDocument> linkedDocuments = linkedInstanceDocument.getLinkedDocuments();
        Iterator linkedDocumentIterator = linkedDocuments.iterator();
        String type = "";
        String format = "";
        String uuid = "";
        while (linkedDocumentIterator.hasNext()) {
            ResponseDocument linkedDocument = (ResponseDocument) linkedDocumentIterator.next();
            type = linkedDocument.getType();
            format = linkedDocument.getFormat();
            if (uuidType != null) {
                if ((uuidType.equalsIgnoreCase(OleSelectConstant.DOC_CATEGORY_TYPE_ITEM)) &&
                        (type.equalsIgnoreCase(OleSelectConstant.DOC_CATEGORY_TYPE_ITEM)) &&
                        (format.equalsIgnoreCase(OleSelectConstant.DOCSTORE_FORMAT_OLEML))) {
                    uuid = linkedDocument.getUuid();
                }
            }
        }
        return uuid;
    }

    public List<BibInfoBean> getUUIDFromResponseXMLForDocStore(String response, HashMap<String, String> dataMap, List<BibInfoBean> bibInfoBeanList) throws Exception {
        String uuid = null;
        //ResponseHandler docStoreResponseHandlerObject = new ResponseHandler();
        ResponseHandler docStoreResponseHandlerObject = SpringContext.getBean(ResponseHandler.class);
        Response docStoreResponseObject = docStoreResponseHandlerObject.toObject(response);
        List<ResponseDocument> requestDocuments = docStoreResponseObject.getDocuments();
        Iterator docStoreDocumentIterator = requestDocuments.iterator();
        Iterator bibInfoBeanIterator = bibInfoBeanList.iterator();
        String type;
        String uuidType = "";
        if (dataMap.containsKey(OleSelectConstant.DOC_CATEGORY_TYPE))
            uuidType = dataMap.get(OleSelectConstant.DOC_CATEGORY_TYPE);
        while (docStoreDocumentIterator.hasNext()) {
            ResponseDocument docStoreDocument = (ResponseDocument) docStoreDocumentIterator.next();
            List<ResponseDocument> linkedInstanceDocuments = docStoreDocument.getLinkedDocuments();
            Iterator linkedInstanceDocumentIterator = linkedInstanceDocuments.iterator();
            while (linkedInstanceDocumentIterator.hasNext()) {
                ResponseDocument linkedInstanceDocument = (ResponseDocument) linkedInstanceDocumentIterator.next();
                //uuid = getUUIDFromLinkedResponseDocument(linkedInstanceDocument, uuidType);
                String format = linkedInstanceDocument.getFormat();
                type = linkedInstanceDocument.getType();
                ;
                if ((uuidType.equalsIgnoreCase(OleSelectConstant.DOC_CATEGORY_TYPE_ITEM)) &&
                        (type.equalsIgnoreCase(OleSelectConstant.DOCSTORE_TYPE_INSTANCE)) &&
                        (format.equalsIgnoreCase(OleSelectConstant.DOCSTORE_FORMAT_OLEML))) {
                    uuid = linkedInstanceDocument.getUuid();
                }
            }
            if (uuid != null && bibInfoBeanIterator.hasNext()) {
                BibInfoBean bibInfoBean = (BibInfoBean) bibInfoBeanIterator.next();
                if (bibInfoBean.getDocStoreIngestionId().equals(docStoreDocument.getId())) {
                    bibInfoBean.setTitleId(uuid);
                }
                uuid = null;
            }
            if (LOG.isDebugEnabled())
                LOG.debug(docStoreDocument.getType() + " uuid---------->" + docStoreDocument.getUuid());
        }
        return bibInfoBeanList;
    }
}
