package org.kuali.ole.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.utility.CompressUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MultiPartBagRequestClient to run all requests in a given folder for Handling Multi Part Zip Bag contents.
 *
 * @author Rajesh Chowdary K
 * @created Jun 6, 2012
 */
public class MultiPartBagRequestClient {
    private Logger logger = LoggerFactory.getLogger(MultiPartBagRequestClient.class);
    private static String dsDocumentURL = "http://localhost:9080/oledocstore/document";
    private ResponseHandler rh = new ResponseHandler();

    static {
        dsDocumentURL = ConfigContext.getCurrentContextConfig().getProperty("docstore.document.url");
    }

    /**
     * Method to run MultiPart Requests @ Location 'requestsRootFolder'.
     *
     * @param requestsRootFolder
     * @return
     * @throws OleException
     */
    public List<Response> runMultiPartRequestsAtLocation(String requestsRootFolder, String restUrl) throws OleException, IOException {
        boolean requestFound = false;
        String operation = null;
        List<Response> responses = new ArrayList<Response>();
        File folder = new File(requestsRootFolder);
        if (!folder.exists() || !folder.isDirectory())
            throw new OleException("Given Path '" + requestsRootFolder + "' does not Exists or may not be a directory path");
        File[] subFolders = folder.listFiles();
        for (File file : subFolders)
            if (file.isFile() && file.getName().equalsIgnoreCase("request.xml")) {
                operation = getOperation(file);
                responses.add(processRequest(file.getParentFile(), restUrl, operation));
                requestFound = true;
            } else if (file.isDirectory() && new File(file.getAbsolutePath() + File.separator + "request.xml").exists()) {
                operation = getOperation(file);
                responses.add(processRequest(file, restUrl, operation));
                requestFound = true;
            }
        if (!requestFound)
            throw new OleException("Given Folder '" + requestsRootFolder + "' don't Contains any Multi Part Requests");

        return responses;
    }

    private String getOperation(File file) throws IOException {
        String operation = null;
        String fileContent = FileUtils.readFileToString(file);
        logger.debug("file content in string -->" + fileContent);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(fileContent);
        operation = request.getOperation();
        logger.debug("operation--->" + operation);
        return operation;

    }

    private Response processRequest(File folder, String restUrl, String operation) {
        logger.info("processing request @ " + folder.getAbsolutePath());
        Response dsResponse = null;
        try {
            CompressUtils compressUtils = new CompressUtils();
            HttpPost httpPost = null;
            HttpResponse response;
            File reqFile = compressUtils.createZippedBagFile(folder);
            if (!StringUtils.isBlank(restUrl)) {
                httpPost = new HttpPost(restUrl);
            } else {
                httpPost = new HttpPost(dsDocumentURL);
            }
            HttpClient httpclient = new DefaultHttpClient();
            FileBody uploadFilePart = new FileBody(reqFile);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("upload-file", uploadFilePart);
            httpPost.setEntity(reqEntity);
            response = httpclient.execute(httpPost);
            reqFile.delete();
            HttpEntity respEntity = response.getEntity();
            InputStream outcome = respEntity.getContent();
            File respZipFile = File.createTempFile("ds-", "-Resp.zip");
            FileOutputStream fos = new FileOutputStream(respZipFile);
            IOUtils.copy(outcome, fos);
            fos.close();
            File responseFolder = new File(folder.getAbsolutePath() + File.separator + "response-" + System.currentTimeMillis());
            responseFolder.mkdirs();
            compressUtils.extractZippedBagFile(respZipFile.getAbsolutePath(), responseFolder.getAbsolutePath());
            respZipFile.delete();
            dsResponse = rh.toObject(FileUtils.readFileToString(new File(responseFolder.getAbsolutePath() + File.separator + "response.xml")));
            dsResponse.setStatusMessage("Processed Request @" + folder.getAbsolutePath() + " , It's Response is @" + responseFolder.getAbsolutePath()
                    + " & The Result is: " + dsResponse.getStatusMessage());
        } catch (Exception e) {
            dsResponse = new Response();
            dsResponse.setStatus("Failure");
            dsResponse.setStatusMessage("Operation @ " + folder.getAbsolutePath() + " Failed, Cause: " + e.getMessage()
                    + "\n check Logs for futher information");
            logger.error("Operation for Processing Request @ " + folder.getAbsolutePath() + " Failed, Cause: " + e.getMessage(), e);
        }

        return dsResponse;
    }
}
