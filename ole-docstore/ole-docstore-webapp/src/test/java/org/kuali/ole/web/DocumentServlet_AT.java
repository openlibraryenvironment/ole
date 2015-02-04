/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.web;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.utility.CompressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to DocumentServlet_UT.
 * 
 * @author Rajesh Chowdary K
 * @created May 25, 2012
 */
@Ignore
public class DocumentServlet_AT {

    private Logger         logger         = LoggerFactory.getLogger(DocumentServlet_AT.class);
    private String         url            = "http://localhost:9080/oledocstore/document";
    private File           inputDir       = null;
    private Response       response       = null;
    private RequestHandler requestHandler = new RequestHandler();

    /**
     * Method to setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Method to tearDown
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        //fail("Not yet implemented");
    }

    /**
     * Method creates the bag from the test case folder then sends to the document store application and captures the response
     */
    @Test
    public void testMultiPartIngestForWorkLicense() {
        try {
            inputDir = new File(this.getClass().getResource("license/ingest").toURI());
            HttpPost httpPost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            CompressUtils compressUtils = new CompressUtils();
            File zipFile = compressUtils.createZippedBagFile(inputDir);
            FileBody uploadFilePart = new FileBody(zipFile);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("upload-file", uploadFilePart);
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httpPost);
            zipFile.delete();
            logger.info("STATUS : " + response.getStatusLine());
            HttpEntity respEntity = response.getEntity();
            InputStream outcome = respEntity.getContent();
            File respFile = File.createTempFile("DocStore Ingest-", "-Response File.zip");
            IOUtils.copy(outcome, new FileOutputStream(respFile));
            List<File> resp = compressUtils.getAllFilesList(compressUtils.extractZippedBagFile(respFile.getAbsolutePath(), null));
            for (File file : resp) {
                if (file.getName().equalsIgnoreCase("response.xml")) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    logger.info("RESPONSE: " + file.getName());
                    String line = null;
                    while ((line = br.readLine()) != null)
                        logger.info(line);
                    this.response = new ResponseHandler().toObject(FileUtils.readFileToString(file));
                } else
                    logger.info("File : " + file.getName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //fail("Failed Test: ");

        }
    }

    @Test
    public void testMultiPartCheckOutForWorkLicense() {
        try {
            if (response == null)
                this.testMultiPartIngestForWorkLicense();
            Request request = new Request();
            request.setUser(response.getUser());
            request.setOperation("checkOut");
            for (ResponseDocument doc : response.getDocuments()) {
                RequestDocument rd = new RequestDocument();
                rd.setId(doc.getId());
                rd.setCategory(doc.getCategory());
                rd.setType(doc.getType());
                rd.setFormat(doc.getFormat());
                rd.setUuid(doc.getUuid());
                request.getRequestDocuments().add(rd);
            }
            String xml = requestHandler.toXML(request);

            File folder = File.createTempFile("ds-", "-inp");
            folder.delete();
            folder.mkdirs();
            FileOutputStream fos = new FileOutputStream(new File(folder.getAbsolutePath() + File.separator + "request.xml"));
            IOUtils.copy(IOUtils.toInputStream(xml), fos);
            fos.close();
            HttpPost httpPost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            CompressUtils compressUtils = new CompressUtils();
            File zipFile = compressUtils.createZippedBagFile(folder);
            FileBody uploadFilePart = new FileBody(zipFile);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("upload-file", uploadFilePart);
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httpPost);
            zipFile.delete();
            logger.info("STATUS : " + response.getStatusLine());
            HttpEntity respEntity = response.getEntity();
            InputStream outcome = respEntity.getContent();
            File respFile = File.createTempFile("DocStore CheckOut-", "-Response File.zip");
            IOUtils.copy(outcome, new FileOutputStream(respFile));
            List<File> resp = compressUtils.getAllFilesList(compressUtils.extractZippedBagFile(respFile.getAbsolutePath(), null));
            for (File file : resp) {
                if (file.getName().equalsIgnoreCase("response.xml")) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    logger.info("RESPONSE: " + file.getName());
                    String line = null;
                    while ((line = br.readLine()) != null)
                        logger.info(line);
                } else
                    logger.info("File : " + file.getName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail("Failed Test: ");

        }
    }

    @Test
    public void testMultiPartStaticCheckOutForWorkLicense() {
        try {
            inputDir = new File(this.getClass().getResource("license/checkout").toURI());
            HttpPost httpPost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            CompressUtils compressUtils = new CompressUtils();
            File zipFile = compressUtils.createZippedBagFile(inputDir);
            FileBody uploadFilePart = new FileBody(zipFile);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("upload-file", uploadFilePart);
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httpPost);
            zipFile.delete();
            logger.info("STATUS : " + response.getStatusLine());
            HttpEntity respEntity = response.getEntity();
            InputStream outcome = respEntity.getContent();
            File respFile = File.createTempFile("DocStore CheckOut-", "-Response File.zip");
            logger.info("Response is at loc.: " + respFile.getAbsolutePath());
            IOUtils.copy(outcome, new FileOutputStream(respFile));
            List<File> resp = compressUtils.getAllFilesList(compressUtils.extractZippedBagFile(respFile.getAbsolutePath(), null));
            for (File file : resp) {
                if (file.getName().equalsIgnoreCase("response.xml")) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    logger.info("RESPONSE: " + file.getName());
                    String line = null;
                    while ((line = br.readLine()) != null)
                        logger.info(line);
                } else
                    logger.info("File : " + file.getName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail("Failed Test: ");

        }
    }

    @Test
    public void testMultiPartCheckInForWorkLicense() {
        try {
            if (response == null)
                this.testMultiPartIngestForWorkLicense();
            inputDir = new File(this.getClass().getResource("license/checkin").toURI());
            File reqFile = new File(inputDir.getAbsolutePath() + File.separator + "request.xml");
            Request checkinReq = requestHandler.toObject(FileUtils.readFileToString(reqFile));
            for (RequestDocument rd : checkinReq.getRequestDocuments())
                for (ResponseDocument resDoc : response.getDocuments())
                    if (rd.getCategory().equalsIgnoreCase(resDoc.getCategory()) && rd.getType().equalsIgnoreCase(resDoc.getType())
                            && rd.getFormat().equalsIgnoreCase(resDoc.getFormat())) {
                        rd.setUuid(resDoc.getUuid());
                        break;
                    }
            reqFile.delete();
            reqFile.createNewFile();
            FileUtils.write(reqFile, requestHandler.toXML(checkinReq));
            HttpPost httpPost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            CompressUtils compressUtils = new CompressUtils();
            File zipFile = compressUtils.createZippedBagFile(inputDir);
            FileBody uploadFilePart = new FileBody(zipFile);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("upload-file", uploadFilePart);
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httpPost);
            zipFile.delete();
            logger.info("STATUS : " + response.getStatusLine());
            HttpEntity respEntity = response.getEntity();
            InputStream outcome = respEntity.getContent();
            File respFile = File.createTempFile("DocStore CheckIn-", "-Response File.zip");
            logger.info("Response is at loc.: " + respFile.getAbsolutePath());
            IOUtils.copy(outcome, new FileOutputStream(respFile));
            List<File> resp = compressUtils.getAllFilesList(compressUtils.extractZippedBagFile(respFile.getAbsolutePath(), null));
            for (File file : resp) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                logger.info("RESPONSE: " + file.getName());
                String line = null;
                while ((line = br.readLine()) != null) {
                    logger.info(line);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail("Failed Test: ");

        }
    }

    @Test
    public void testMultiPartDeleteForWorkLicense() {
        try {
            if (response == null)
                this.testMultiPartIngestForWorkLicense();
            inputDir = new File(this.getClass().getResource("license/delete").toURI());
            File reqFile = new File(inputDir.getAbsolutePath() + File.separator + "request.xml");
            Request checkinReq = requestHandler.toObject(FileUtils.readFileToString(reqFile));
            for (RequestDocument rd : checkinReq.getRequestDocuments())
                for (ResponseDocument resDoc : response.getDocuments())
                    if (rd.getCategory().equalsIgnoreCase(resDoc.getCategory()) && rd.getType().equalsIgnoreCase(resDoc.getType())
                            && rd.getFormat().equalsIgnoreCase(resDoc.getFormat())) {
                        rd.setUuid(resDoc.getUuid());
                        break;
                    }
            reqFile.delete();
            reqFile.createNewFile();
            FileUtils.write(reqFile, requestHandler.toXML(checkinReq));

            HttpPost httpPost = new HttpPost(url);
            HttpClient httpclient = new DefaultHttpClient();
            CompressUtils compressUtils = new CompressUtils();
            File zipFile = compressUtils.createZippedBagFile(inputDir);
            FileBody uploadFilePart = new FileBody(zipFile);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("upload-file", uploadFilePart);
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httpPost);
            zipFile.delete();
            logger.info("STATUS : " + response.getStatusLine());
            HttpEntity respEntity = response.getEntity();
            InputStream outcome = respEntity.getContent();
            File respFile = File.createTempFile("DocStore Delete-", "-Response File.zip");
            logger.info("Response is at loc.: " + respFile.getAbsolutePath());
            IOUtils.copy(outcome, new FileOutputStream(respFile));
            List<File> resp = compressUtils.getAllFilesList(compressUtils.extractZippedBagFile(respFile.getAbsolutePath(), null));
            for (File file : resp) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                logger.info("RESPONSE: " + file.getName());
                String line = null;
                while ((line = br.readLine()) != null) {
                    logger.info(line);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail("Failed Test: ");

        }
    }

    public static void main(String args[]) {
        DocumentServlet_AT dsClient = new DocumentServlet_AT();
        try {
            dsClient.testMultiPartIngestForWorkLicense();
        } catch (Exception e) {

        }
    }
}
