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
package org.kuali.ole.docstore.xstream.ingest;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/13/11
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponseHandler_UT extends BaseTestCase {
    // private static final Logger LOG = Logger.getLogger(ResponseHandler_UT.class);
    private static final Logger LOG = LoggerFactory.getLogger(ResponseHandler_UT.class);

    @Test
    public void convertResponseXMLToReqObject() throws Exception {
        ResponseHandler responseHandler = new ResponseHandler();
        URL resource = getClass().getResource("/org/kuali/ole/response.xml");
        File file = new File(resource.toURI());
        Response response = responseHandler.toObject(readFile(file));
        assertNotNull(response);
        LOG.info(response.getUser());
        List<ResponseDocument> responseDocuments = response.getDocuments();
        Assert.assertNotNull(responseDocuments);
        LOG.info("responseDocuments size" + responseDocuments.size());
        for (Iterator<ResponseDocument> iterator = responseDocuments.iterator(); iterator.hasNext(); ) {
            ResponseDocument document = iterator.next();
            Assert.assertNotNull(document);
            LOG.info("document" + document);
            String status = document.getStatus();
            LOG.info("status" + status);
            String statusMessage = document.getStatusMessage();
            LOG.info("statusMessage" + statusMessage);
            String id = document.getId();
            LOG.info("id" + id);
            String category = document.getCategory();
            String documentMimeType = document.getDocumentMimeType();
            LOG.info("documentMimeType" + documentMimeType);
            String documentName = document.getDocumentName();
            LOG.info("documentName" + documentName);
            String documentTitle = document.getDocumentTitle();
            LOG.info("documentTitle" + documentTitle);
            LOG.info("category" + category);
            Assert.assertNotNull(category);
            String type = document.getType();
            Assert.assertNotNull(type);
            String format = document.getFormat();
            Assert.assertNotNull(format);
            LOG.info("\n");
            List<ResponseDocument> linkedDocuments = document.getLinkedDocuments();
            for (Iterator<ResponseDocument> iterator1 = linkedDocuments.iterator(); iterator1.hasNext(); ) {
                ResponseDocument linkdocument = iterator1.next();
                Assert.assertNotNull(linkdocument);
                LOG.info("linked document" + linkdocument);
                String linkId = linkdocument.getId();
                LOG.info("linked id" + id);
                Assert.assertNotNull(linkId);
                String linkCategory = linkdocument.getCategory();
                LOG.info("linked category" + category);
                Assert.assertNotNull(linkCategory);
                String linkType = linkdocument.getType();
                LOG.info("linked Type" + linkType);
                Assert.assertNotNull(linkType);
                String linkFormat = linkdocument.getFormat();
                LOG.info("linked formate" + linkFormat);
                Assert.assertNotNull(linkFormat);

            }
        }
    }

    @Test
    public void generateResponse() throws Exception {
        ResponseHandler responseHandler = new ResponseHandler();
        Response response = new Response();
        response.setUser("mockUser");
        response.setOperation("mockOperation");
        response.setStatus("Success/Failure");
        response.setStatusMessage("Document ingested. | Document could not be ingested. Reason:...");
        response.setMessage("info required");
        assertNotNull(response.getStatus());
        assertNotNull(response.getMessage());
        assertNotNull(response.getOperation());
        assertNotNull(response.getStatusMessage());
        ResponseDocument responseDocument1 = new ResponseDocument();
        responseDocument1.setId("1");
        responseDocument1.setCategory("work");
        responseDocument1.setType("Biblographic");
        responseDocument1.setFormat("oleml");
        responseDocument1.setUuid("uuid-1");
        responseDocument1.setVersion("0.8");
        Content content = new Content();
        content.setContent("oleml content");
        responseDocument1.setContent(content);

        ResponseDocument linkedresponseDoc1 = new ResponseDocument();
        linkedresponseDoc1.setId("linkId3");
        linkedresponseDoc1.setCategory("work");
        linkedresponseDoc1.setType("instance");
        linkedresponseDoc1.setFormat("oleml");
        Content linkedContent1 = new Content();
        linkedContent1.setContent("some Instance content");
        linkedresponseDoc1.setContent(linkedContent1);
        linkedresponseDoc1.setUuid("instance uuid-3");
        responseDocument1.addLinkedDocument(linkedresponseDoc1);

        ResponseDocument linkedHoldingResponseDoc = new ResponseDocument();
        linkedHoldingResponseDoc.setId("linkId Holding 3");
        linkedHoldingResponseDoc.setCategory("work");
        linkedHoldingResponseDoc.setType("holding");
        linkedHoldingResponseDoc.setFormat("oleml");
        Content linkedHoldingContent = new Content();
        linkedHoldingContent.setContent("some holding content");
        linkedHoldingResponseDoc.setContent(linkedHoldingContent);
        linkedHoldingResponseDoc.setUuid("holding uuid-3");
        responseDocument1.addLinkedInstanseDocument(linkedHoldingResponseDoc);

        ResponseDocument linkedItemResponseDoc = new ResponseDocument();
        linkedItemResponseDoc.setId("linkId Item 3");
        linkedItemResponseDoc.setCategory("work");
        linkedItemResponseDoc.setType("item");
        linkedItemResponseDoc.setFormat("oleml");
        Content linkedItemContent = new Content();
        linkedItemContent.setContent("some Item content");
        linkedItemResponseDoc.setContent(linkedItemContent);
        linkedItemResponseDoc.setUuid("item uuid-3");
        responseDocument1.addLinkedInstanseDocument(linkedItemResponseDoc);
        ResponseDocument responseDocument2 = new ResponseDocument();
        responseDocument2.setId("2");
        responseDocument2.setCategory("work");
        responseDocument2.setType("bibliographic");
        responseDocument2.setFormat("marc");
        responseDocument2.setUuid("uuid-2");
        Content content2 = new Content();
        content2.setContent("content");
        content2.setContentObject(content2);
        assertNotNull(content2.getContentObject());
        responseDocument2.setContent(content2);
        ArrayList<ResponseDocument> responseDocument = new ArrayList<ResponseDocument>();
        responseDocument.add(responseDocument1);
        responseDocument.add(responseDocument2);
        response.setDocuments(responseDocument);
        ResponseDocument linkedresponseDoc2 = new ResponseDocument();
        linkedresponseDoc2.setId("linkId4");
        linkedresponseDoc2.setCategory("work");
        linkedresponseDoc2.setType("instance");
        linkedresponseDoc2.setFormat("oleml");
        Content linkedContent2 = new Content();
        linkedContent2.setContent("somecontent");
        linkedresponseDoc2.setUuid("instance uuid-4");
        linkedresponseDoc2.setContent(linkedContent1);
        responseDocument2.addLinkedDocument(linkedresponseDoc2);


        List<LinkInformation> linkInformationList = new ArrayList<LinkInformation>();
        LinkInformation linkInformation = new LinkInformation();
        Link link = new Link();
        link.setName("admin");
        link.setValue("value");
        assertNotNull(link.getName());
        assertNotNull(link.getValue());
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setFrom("from");
        assertNotNull(linkInfo.getFrom());
        linkInfo.setTo("to");
        assertNotNull(linkInfo.getTo());
        linkInformation.setBibUUID("7f33dc14");
        linkInformation.setHoldingsUUID("6d33ee14");
        linkInformation.setId("8f33dc14");
        linkInformation.setInstanceUUID("4c3311");
        assertNotNull(linkInformation.getBibUUID());
        assertNotNull(linkInformation.getHoldingsUUID());
        assertNotNull(linkInformation.getId());
        assertNotNull(linkInformation.getInstanceUUID());
        List<String> itemUUIDs = new ArrayList<String>();
        itemUUIDs.add("cc33dc14");
        linkInformation.setItemUUIDs(itemUUIDs);
        assertNotNull(linkInformation.getItemUUIDs());
        linkInformationList.add(linkInformation);
        response.setLinkInformation(linkInformationList);
        assertNotNull(response.getLinkInformation());
        linkedresponseDoc2.setLinkInformation(linkInformationList);
        assertNotNull(linkedresponseDoc2.getLinkInformation());

        String xmlResponse = responseHandler.toXML(response);
        assertNotNull(xmlResponse);
        LOG.info(xmlResponse);
    }

    private String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }
}
