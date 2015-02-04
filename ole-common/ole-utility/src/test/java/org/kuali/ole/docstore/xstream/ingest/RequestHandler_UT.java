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

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.utility.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/7/11
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestHandler_UT extends BaseTestCase {
    //private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RequestHandler_UT.class);
    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler_UT.class);

    @Test
    public void testDublinUnqualifiedXML() throws URISyntaxException, IOException {
        RequestHandler requestSerializer = new RequestHandler();
        URL resource = getClass().getResource("/org/kuali/ole/unqDublin.xml");
        File file = new File(resource.toURI());
        Request request = requestSerializer.toObject(readFile(file));
        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        Assert.assertNotNull(requestDocuments);
        for (Iterator<RequestDocument> iterator = requestDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument ingestDocument = iterator.next();
            Assert.assertNotNull(ingestDocument);
            String category = ingestDocument.getCategory();
            Assert.assertNotNull(category);
            String type = ingestDocument.getType();
            Assert.assertNotNull(type);
            String format = ingestDocument.getFormat();
            Assert.assertNotNull(format);
            String content = ingestDocument.getContent().getContent();
            LOG.info("content-->" + content);
            Assert.assertNotNull(content);
            LOG.info("\n");
        }
        String xml = requestSerializer.toXML(request);
        LOG.info("xml-->" + xml);
        Assert.assertNotNull(request);


    }

    @Test
    public void convertXMLToReqObject() throws Exception {
        RequestHandler requestSerializer = new RequestHandler();
        URL resource = getClass().getResource("/org/kuali/ole/sample-link.xml");
        File file = new File(resource.toURI());
        Request request = requestSerializer.toObject(readFile(file));
        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        Assert.assertNotNull(requestDocuments);
        for (Iterator<RequestDocument> iterator = requestDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument ingestDocument = iterator.next();
            Assert.assertNotNull(ingestDocument);
            String category = ingestDocument.getCategory();
            Assert.assertNotNull(category);
            String type = ingestDocument.getType();
            Assert.assertNotNull(type);
            String format = ingestDocument.getFormat();
            Assert.assertNotNull(format);
            String content = ingestDocument.getContent().getContent();
            Assert.assertNotNull(content);
            LOG.info("\n");
        }
        Assert.assertNotNull(request);
        String xml = requestSerializer.toXML(request);
        LOG.info("request-->" + xml);

    }

    @Test
    public void endToEndTestFromNToXML() throws Exception {
        RequestHandler requestSerializer = new RequestHandler();
        URL resource = getClass().getResource("/org/kuali/ole/docstore/model/xstream/ingest/request-instance.xml");
        File file = new File(resource.toURI());
        Request request = requestSerializer.toObject(readFile(file));
        LOG.info(requestSerializer.toXML(request));
    }

    @Test
    public void convertReqObjectToXML() throws Exception {
        Request requestObject = new Request();

        RequestDocument requestDocument1 = new RequestDocument();
        List<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
        requestDocument1.setId("2");
        requestDocument1.setCategory("work");
        requestDocument1.setType("instance");
        requestDocument1.setFormat("oleml");
        requestDocument1.setUser("user");
        assertNotNull(requestDocument1.getUser());
        requestDocument1.setUuid("1111");
        assertNotNull(requestDocument1.getUuid());
        requestDocument1.setOperation("read");
        assertNotNull(requestDocument1.getOperation());
        Content content = new Content();
        content.setContent("instance content");
        requestDocument1.setContent(content);
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setDateEntered("mockDate");
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setLastUpdated("mockDate");
        additionalAttributes.setFastAddFlag("false");
        requestDocument1.setAdditionalAttributes(additionalAttributes);
        RequestDocument linkedRequestDoc1 = new RequestDocument();
        linkedRequestDoc1.setId("linkId1");
        linkedRequestDoc1.setCategory("work");
        linkedRequestDoc1.setType("instance");
        linkedRequestDoc1.setFormat("oleml");
        linkedRequestDoc1.setAdditionalAttributes(additionalAttributes);
        Content linkedContent1 = new Content();
        linkedContent1.setContent("somecontent");
        linkedRequestDoc1.setContent(linkedContent1);
        requestDocument1.addLinkedRequestDocument(linkedRequestDoc1);
        RequestDocument requestDocument2 = new RequestDocument();

        requestDocument2.setId("1");
        requestDocument2.setCategory("work");
        requestDocument2.setType("bibliographic");
        requestDocument2.setFormat("marc");
        Content content1 = new Content();
        content1.setContent("marc content");
        requestDocument2.setContent(content1);
        AdditionalAttributes additionalAttributes1 = new AdditionalAttributes();
        additionalAttributes1.setDateEntered("mockDate");
        additionalAttributes1.setSupressFromPublic("false");
        additionalAttributes1.setLastUpdated("mockDate");
        additionalAttributes1.setFastAddFlag("false");
        requestDocument2.setAdditionalAttributes(additionalAttributes1);
        RequestDocument linkedRequestDoc2 = new RequestDocument();
        linkedRequestDoc2.setId("linkId2");
        linkedRequestDoc2.setCategory("work");
        linkedRequestDoc2.setType("instance");
        linkedRequestDoc2.setFormat("oleml");
        linkedRequestDoc2.setAdditionalAttributes(additionalAttributes1);
        Content linkedContent = new Content();
        linkedContent.setContent("somecontent");
        linkedRequestDoc2.setContent(linkedContent);
        requestDocument2.addLinkedRequestDocument(linkedRequestDoc2);
        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument1);
        requestDocuments.add(requestDocument2);
        RequestDocument requestDocument3 = (RequestDocument) requestDocument1.clone();
        requestDocument3.setLinkedRequestDocuments(requestDocuments);
        requestObject.setRequestDocuments(requestDocuments);
        RequestHandler requestHandler = new RequestHandler();
        String xml = requestHandler.toXML(requestObject);
        Assert.assertNotNull(xml);
        LOG.info(xml);
    }


    @Test
    public void convertReqObjectWithOLEDataToXML() throws Exception {
        Request requestObject = new Request();
        requestObject.setUser("admin");
        assertNotNull(requestObject.getUser());
        requestObject.setOperation(Request.Operation.bulkIngest.toString());
        assertNotNull(requestObject.getOperation());
        URL resource = null;
        File file = null;
        RequestDocument requestDocument1 = new RequestDocument();
        requestDocument1.setId("2");
        requestDocument1.setCategory("work");
        requestDocument1.setType("instance");
        requestDocument1.setFormat("oleml");
        requestDocument1.setUser("admin");

        Content content = new Content();
        resource = getClass().getResource("/org/kuali/ole/instance.xml");
        file = new File(resource.toURI());
        String instanceContent = FileUtils.readFileToString(file);
        content.setContent(instanceContent);
        requestDocument1.setContent(content);
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setDateEntered("mockDate");
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setLastUpdated("mockDate");
        additionalAttributes.setFastAddFlag("false");
        requestDocument1.setAdditionalAttributes(additionalAttributes);
        RequestDocument requestDocument2 = new RequestDocument();
        requestDocument2.setId("1");
        requestDocument2.setCategory("work");
        requestDocument2.setType("bibliographic");
        requestDocument2.setFormat("marc");
        Content content1 = new Content();
        resource = getClass().getResource("/org/kuali/ole/input-marc.xml");
        file = new File(resource.toURI());
        String marcContent = FileUtils.readFileToString(file);
        content1.setContent(marcContent);
        requestDocument2.setContent(content1);
        AdditionalAttributes additionalAttributes1 = new AdditionalAttributes();
        additionalAttributes1.setDateEntered("mockDate");
        additionalAttributes1.setSupressFromPublic("false");
        additionalAttributes1.setLastUpdated("mockDate");
        additionalAttributes1.setFastAddFlag("false");
        requestDocument2.setAdditionalAttributes(additionalAttributes1);
        ArrayList<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments.add(requestDocument1);
        requestDocuments.add(requestDocument2);
        requestObject.setRequestDocuments(requestDocuments);
        RequestHandler requestHandler = new RequestHandler();
        String xmlString = requestHandler.toXML(requestObject);
        LOG.info(xmlString);
        Assert.assertNotNull(xmlString);

        Request newRequest = requestHandler.toObject(xmlString);
        LOG.info("requestDocument size in req object " + requestObject.getRequestDocuments().size());
        LOG.info("requestDocument size in new req object " + newRequest.getRequestDocuments().size());
        Assert.assertEquals(requestObject.getRequestDocuments().size(), newRequest.getRequestDocuments().size());
        Assert.assertEquals(requestObject.getRequestDocuments().get(0).getCategory(),
                newRequest.getRequestDocuments().get(0).getCategory());

    }

    //
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
