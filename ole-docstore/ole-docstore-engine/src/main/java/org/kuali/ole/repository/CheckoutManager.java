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
package org.kuali.ole.repository;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.logger.DocStoreLogger;
import org.kuali.ole.logger.MetricsLogger;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.utility.CompressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/14/11
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheckoutManager {
    MetricsLogger metricsLogger = new MetricsLogger(this.getClass().getName());
    DocStoreLogger docStoreLogger = new DocStoreLogger(this.getClass().getName());
    private static final Logger LOG = LoggerFactory.getLogger(CheckoutManager.class);


    public String checkOut(String uuid, String userName, String action)
            throws OleException, RepositoryException, FileNotFoundException {
        return checkOut(null, uuid, userName, action);
    }

    public String checkOut(Session session, String uuid, String userName, String action)
            throws OleException, RepositoryException, FileNotFoundException {
        Session newSession;
        String content = "";

        if (null == session) {
            newSession = RepositoryManager.getRepositoryManager().getSession(userName, action);
        } else {
            newSession = session;
        }
        getMetricsLogger().startRecording();
        Node nodeByUUID = getNodeByUUID(newSession, uuid);
        if (nodeByUUID != null) {
            if (nodeByUUID.getName().equalsIgnoreCase(ProcessParameters.NODE_INSTANCE)) {
                content = getXMLFORInstanceNode(nodeByUUID);
            } else {
                content = getData(nodeByUUID);
            }
        }

        if (null == session) {
            RepositoryManager.getRepositoryManager().logout(newSession);
        }
        getMetricsLogger().endRecording();
        getMetricsLogger().printTimes("Time taken for checking out: ");
        return content;
    }

    public String getXMLFORInstanceNode(Node instanceNode)
            throws RepositoryException, OleException, FileNotFoundException {
        String instance = "";
        String holdings = "";
        String sourceHolding = "";
        List<String> items = new ArrayList<String>();
        StringBuffer stringBuffer = new StringBuffer();
        NodeIterator nodeIterator = instanceNode.getNodes();
        Node node = null;
        while (nodeIterator.hasNext()) {
            node = nodeIterator.nextNode();
            if (node.getName().equalsIgnoreCase("instanceFile")) {
                instance = getData(node);
            } else if (node.getName().equalsIgnoreCase(ProcessParameters.NODE_HOLDINGS)) {
                NodeIterator nodes = node.getNodes();
                while (nodes.hasNext()) {
                    Node node1 = nodes.nextNode();
                    if (node1.getName().equalsIgnoreCase("holdingsFile")) {
                        holdings = getData(node1);
                    } else if (node1.getName().equalsIgnoreCase("sourceHoldingsFile")) {
                        if (getData(node1) != null && getData(node1).length() > 0) {
                            sourceHolding = getData(node1);
                        }
                    } else if (node1.getName().equalsIgnoreCase("itemFile")) {
                        items.add(getData(node1));
                    }
                }

            }
        }
        stringBuffer
                .append(instance.substring(instance.indexOf("<instanceCollection>"), instance.indexOf("</instance>")));
        if (!holdings.equalsIgnoreCase("<null/>")) {
            stringBuffer.append(holdings);
        }
        if (!sourceHolding.equalsIgnoreCase("<null/>")) {
            stringBuffer.append(sourceHolding);
        }
        stringBuffer.append("<items>");
        for (String str : items) {
            if (!str.equalsIgnoreCase("<null/>")) {
                stringBuffer.append(str);
            }
        }
        stringBuffer.append("</items>");
        stringBuffer.append("</instance>");
        stringBuffer.append("</instanceCollection>");
        return stringBuffer.toString();
    }

    public String getData(Node nodeByUUID) throws OleException, RepositoryException, FileNotFoundException {
        StringBuffer stringBuffer = new StringBuffer();
        if (null != nodeByUUID) {
            Node jcrContent = nodeByUUID.getNode("jcr:content");
            Binary binary = jcrContent.getProperty("jcr:data").getBinary();
            InputStream content = binary.getStream();

            Writer writer;
            try {
                writer = new StringWriter();
                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(content, "UTF-8"));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    stringBuffer.append(writer.toString());
                    content.close();
                }

            } catch (IOException e) {
                getDocStoreLogger().log("failure during checkOut of ", e);
            }

        }

        return stringBuffer.toString();
    }

    public File checkOutMultiPart(Request request) throws Exception {
        String uuid;
        Session session = null;
        Response response = new Response();
        response.setUser(request.getUser());
        response.setOperation(request.getOperation());
        File outputZip = null;
        File folder = null;
        try {
            session = RepositoryManager.getRepositoryManager().getSession(request.getUser(), request.getOperation());
            folder = File.createTempFile("tmp", "fdi");
            folder.delete();
            folder.mkdirs();
            for (RequestDocument doc : request.getRequestDocuments()) {
                uuid = doc.getUuid();
                ResponseDocument responseDoc = new ResponseDocument();
                responseDoc.setUuid(uuid);
                response.getDocuments().add(responseDoc);
                Node node = session.getNodeByIdentifier(uuid);
                Property fileName = null;
                try {
                    fileName = node.getProperty("fileName");
                } catch (PathNotFoundException re) {
                }
                if (fileName != null) {
                    File file = new File(folder.getAbsolutePath() + File.separator + fileName.getString());
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    getNodeData(node, fos);
                    responseDoc.setDocumentName(fileName.getString());
                    Content content = new Content();
                    content.setContent(getData(node));
                    responseDoc.setContent(content);
                } else {
                    Content content = new Content();
                    content.setContent(getData(node));
                    responseDoc.setContent(content);
                }
                responseDoc.setId(doc.getId());
                responseDoc.setCategory(doc.getCategory());
                responseDoc.setFormat(doc.getFormat());
                responseDoc.setType(doc.getType());
            }
            response.setStatus("Success");
            response.setStatusMessage("CheckOut Successful.");

        } catch (Exception e) {
            response.setStatus("Failure");
            String failOverMessage = e.getMessage();
            if (e instanceof ItemNotFoundException) {
                failOverMessage = "Document Not Found for uuid : " + failOverMessage;
            }
            response.setStatusMessage("CheckOut Failed, Cause: " + failOverMessage);
            docStoreLogger.log("CheckOut Failed, Cause: " + failOverMessage, e);
        } finally {
            if (session != null) {
                RepositoryManager.getRepositoryManager().logout(session);
            }
            ResponseHandler responseHandler = new ResponseHandler();
            String responseXml = responseHandler.toXML(response);
            FileOutputStream fOSResp = new FileOutputStream(
                    new File(folder.getAbsolutePath() + File.separator + "response.xml"));
            IOUtils.write(responseXml, fOSResp);
            try {
                fOSResp.close();
            } catch (Exception exd) {
                LOG.error(exd.getMessage() , exd);
            }
            outputZip = new CompressUtils().createZippedBagFile(folder);
            FileUtils.deleteDirectory(folder);
        }
        return outputZip;
    }

    public void getNodeData(Node nodeByUUID, OutputStream output) throws Exception {
        if (null != nodeByUUID) {
            Node jcrContent = nodeByUUID.getNode("jcr:content");
            Binary binary = jcrContent.getProperty("jcr:data").getBinary();
            InputStream content = binary.getStream();
            try {
                IOUtils.copy(content, output);
                content.close();
                output.close();
            } catch (Exception e) {
                LOG.error(e.getMessage() , e);
            }
        }
    }

    private Node getNodeByUUID(Session newSession, String uuid) throws OleException {
        return new NodeHandler().getNodeByUUID(newSession, uuid);
    }

    public MetricsLogger getMetricsLogger() {
        return metricsLogger;
    }

    public DocStoreLogger getDocStoreLogger() {
        return docStoreLogger;
    }

    public String checkOutBinary(String uuid, String userId, String action, String docFormat)
            throws OleException, RepositoryException, IOException {
        return checkOutBinary(null, uuid, userId, action, docFormat);
    }

    private String checkOutBinary(Session session, String uuid, String userId, String action, String docFormat)
            throws OleException, RepositoryException, IOException {
        FileOutputStream fos = null;
        Node nodeByUUID = null;
        Session newSession;
        File output = File.createTempFile("checkout.", ".output");
        FileUtils.deleteQuietly(output);
        output.mkdirs();
        if (null == session) {
            newSession = RepositoryManager.getRepositoryManager().getSession(userId, action);

        } else {
            newSession = session;
        }
        getMetricsLogger().startRecording();
        nodeByUUID = getNodeByUUID(newSession, uuid);
        String fileNameAndExtension = null;
        String outputFilePath = null;
        if (nodeByUUID != null) {
            if (docFormat.equalsIgnoreCase(DocFormat.PDF.getCode())) {
                fileNameAndExtension = nodeByUUID.getIdentifier() + ".pdf";
            }
            if (docFormat.equalsIgnoreCase(DocFormat.DOC.getCode())) {
                fileNameAndExtension = nodeByUUID.getIdentifier() + ".doc";
            }
            outputFilePath = output.getAbsolutePath() + File.separator + fileNameAndExtension;
            byte[] binaryContent = getBinaryData(nodeByUUID);
            fos = new FileOutputStream(outputFilePath);
            fos.write(binaryContent);
            fos.close();
        }
        if (null == session) {
            RepositoryManager.getRepositoryManager().logout(newSession);
        }
        getMetricsLogger().endRecording();
        getMetricsLogger().printTimes("Time taken for checking out: ");
        LOG.info("path-->" + output.getAbsolutePath());
        return outputFilePath;
    }

    private byte[] getBinaryData(Node nodeByUUID) throws RepositoryException, IOException {
        byte[] bytes = null;
        if (null != nodeByUUID) {
            Node jcrContent = nodeByUUID.getNode("jcr:content");
            Binary binary = jcrContent.getProperty("jcr:data").getBinary();
            InputStream inputStream = binary.getStream();
            bytes = getBytesFromInputStream(inputStream);
        }
        return bytes;
    }

    public byte[] getBytesFromInputStream(InputStream is) throws IOException {
        long length = is.available();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file ");
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}
