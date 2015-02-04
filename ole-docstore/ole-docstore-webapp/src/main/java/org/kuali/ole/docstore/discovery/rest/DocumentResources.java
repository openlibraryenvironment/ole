package org.kuali.ole.docstore.discovery.rest;

import gov.loc.repository.bagit.utilities.FormatHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.discovery.service.ServiceLocator;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.DocumentService;
import org.kuali.ole.docstore.service.DocumentServiceImpl;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.repository.DeleteManager;
import org.kuali.ole.repository.DocumentStoreManager;
import org.kuali.ole.repository.NodeHandler;
import org.kuali.ole.service.OleUuidCheckWebService;
import org.kuali.ole.service.OleWebServiceProvider;
import org.kuali.ole.service.impl.OleWebServiceProviderImpl;
import org.kuali.ole.utility.CompressUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 8/3/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/documents")
public class DocumentResources {
    private static final Logger LOG = LoggerFactory
            .getLogger(DocumentResources.class);
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator
            .getIngestNIndexHandlerService();
    private DocumentStoreManager documentStoreManager = BeanLocator.getDocumentStoreManager();
    private CompressUtils compressUtils = new CompressUtils();
    private DocumentService documentService;


    /**
     * This method ingest the string content and returns the response
     *
     * @return
     * @throws IOException
     */
    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.TEXT_XML})
    public String create(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        String xmlResponse = null;
        File targetDir = null;
        String fileData = null;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        String updatesEnabled = "true";
        boolean isUpdateEnabled = true;
        if ("false".equalsIgnoreCase(updatesEnabled)) {
            isUpdateEnabled = false;
        } else {
            isUpdateEnabled = true;
        }
        if (isMultipart) {
            LOG.info("Processing in Multipart : START");
            FileItem item = getFileItemFromRequest(request);
            String contentType = item.getContentType();
            LOG.info("content type-->" + contentType);
            if (contentType.equalsIgnoreCase(MediaType.APPLICATION_OCTET_STREAM)) {
                File outputFile = new File(FileUtils.getTempDirectory(), item.getName());
                item.write(outputFile);
                targetDir = compressUtils.extractZippedBagFile(outputFile.getAbsolutePath(), null);
                ArrayList<File> files = compressUtils.getAllFilesList(targetDir);
                try {
                    RequestHandler rh = new RequestHandler();
                    Request dsRequest = null;
                    for (File file : files) {
                        if (file.getName().equalsIgnoreCase("request.xml")) {
                            String rqStr = FileUtils.readFileToString(file);
                            dsRequest = rh.toObject(rqStr);
                            for (RequestDocument rd : dsRequest.getRequestDocuments()) {
                                if (rd.getDocumentName() != null) {
                                    for (File fl : files) {
                                        if (fl.getName().equals(rd.getDocumentName())) {
                                            rd.setDocumentName(fl.getAbsolutePath());
                                            break;
                                        }
                                    }
                                }
                            }
                            if ("ingest".equalsIgnoreCase(dsRequest.getOperation())) {
                                if (isUpdateEnabled) {
//                                    Response docStoreResponse = ingestNIndexHandlerService
//                                            .ingestNIndexRequestDocuments(dsRequest);
                                    Response docStoreResponse = BeanLocator.getDocumentServiceImpl().process(dsRequest);
                                    LOG.info("xmlResponse :" + new ResponseHandler().toXML(docStoreResponse));
                                    sendResponseBag(response, docStoreResponse);
                                } else {
                                    sendUnavailableResponseString(response);
                                }
                            } else if ("checkIn".equalsIgnoreCase(dsRequest.getOperation())) {
                                if (isUpdateEnabled) {
                                    Response dsResponse = new Response();
                                    checkIn(dsRequest, dsResponse);
                                    sendResponseBag(response, dsResponse);
                                } else {
                                    sendUnavailableResponseString(response);
                                }

                            } else {
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                        "Invalid Request Operation: " + dsRequest.getOperation());
                            }
                            break;
                        }
                    }
                    files.add(files.get(files.size() - 1).getParentFile());
                    compressUtils.deleteFiles(files);
                } catch (Exception e) {
                    LOG.error("Invalid Request : " + e.getMessage(), e);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request : " + e.getMessage());
                }
                LOG.info("Processing in Multipart : END");


            } else if (contentType.equalsIgnoreCase(MediaType.TEXT_XML)) {
                File file = new File(FileUtils.getTempDirectory(), item.getName());
                item.write(file);
                fileData = FileUtils.readFileToString(file, "UTF-8");
                file.delete();
//                xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(fileData);
//                LOG.info("response-->" + xmlResponse);
                Request dsRequest = null;
                try{
                    dsRequest = new RequestHandler().toObject(fileData);
                    Response dsResponse = BeanLocator.getDocumentServiceImpl().process(dsRequest);
                    xmlResponse = new ResponseHandler().toXML(dsResponse);
                    LOG.info("xmlResponse-->" + xmlResponse);
                } catch (Exception e){
                    LOG.error("Invalid Request : " + e.getMessage(), e);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request : " +e.getMessage());
                }
            }

        }

        return xmlResponse;

    }

    /**
     * This methods reads the uploaded file content from request and returns the file content
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private String getFileContent(@Context HttpServletRequest request, @Context HttpServletResponse response)
            throws IOException

    {
        int maxFileSize = 100 * 1024;
        int maxMemSize = 4 * 1024;
        File file = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        File output = File.createTempFile("ingest.", ".output");
        FileUtils.deleteQuietly(output);
        factory.setRepository(output);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);
        String fileData = null;
        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {

                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    LOG.debug("item Name = " + fi.getName() + " ; content type = " + fi.getContentType());
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();

                    // Write the file
                    fi.write(output);
                    LOG.debug("Uploaded Filename: " + fileName);
                    LOG.debug("Uploaded output path: " + output.getAbsolutePath());
                }
            }
            fileData = FileUtils.readFileToString(output, "UTF-8");
            output.delete();

        } catch (Exception ex) {
            LOG.error("error while ingesting :" + ex);
        }

        return fileData;
    }


    /**
     * @return
     */
    private FileItem getFileItemFromRequest(@Context HttpServletRequest req) throws Exception, Exception {
        int maxFileSize = 100 * 1024;
        int maxMemSize = 4 * 1024;
        File file = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);
        String fileData = null;

        // Parse the request to get file items.
        List fileItems = upload.parseRequest(req);

        // Process the uploaded file items
        Iterator i = fileItems.iterator();

        FileItem fi = null;
        while (i.hasNext()) {
            fi = (FileItem) i.next();
            if (!fi.isFormField()) {

                // Get the uploaded file parameters
                String fieldName = fi.getFieldName();
                String fileName = fi.getName();
                String contentType = fi.getContentType();
                LOG.debug(
                        "In getFileContent() item Name = " + fi.getName() + " ; content type = " + fi.getContentType());

            }
        }
        return fi;
    }

    /**
     * @param id
     * @return
     */

    @GET
    @Path("{id}")
    @Produces({MediaType.TEXT_XML, MediaType.TEXT_PLAIN})
    public String findById(@PathParam("id") String id) throws Exception {
        LOG.info("id--> " + id);
        String xmlResponse = null;
        Response response = null;
        try {
            response = checkOut(id);
            LOG.debug("checkOut response-->" + xmlResponse);
        } catch (Exception e) {
            LOG.error("Exception while checkout" + e);
        }
        return new ResponseHandler().toXML(response);
    }

    /**
     * @return
     * @throws Exception
     */
    @GET
    @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_OCTET_STREAM})
    public String getCheckOutResponse(@Context HttpServletRequest httpServletRequest,
                                      @QueryParam("category") String category, @QueryParam("type") String type,
                                      @QueryParam("format") String format,
                                      @QueryParam("staringUUID") String staringUUID,
                                      @QueryParam("endingUUID") String numUUIDs) throws Exception {
        String requestXML = null;
        String responseXML = null;
        requestXML = httpServletRequest.getParameter("checkOutRequest");
        LOG.debug("requestXML-->" + requestXML);
        if (!StringUtils.isBlank(requestXML)) {
            LOG.debug("requestXML-->" + requestXML);
            Request dsRequest = new RequestHandler().toObject(requestXML);
            File output = documentStoreManager.checkOutMultiPart(dsRequest);
            File extractDir = compressUtils.extractZippedBagFile(output.getAbsolutePath(), null);
            LOG.debug("extractDir-->" + extractDir.getAbsolutePath());
            String responsePath = extractDir + File.separator + "response.xml";
            File responseFile = new File(responsePath);
            if (extractDir.isDirectory() && responseFile.isFile()) {
                responseXML = FileUtils.readFileToString(responseFile);
                LOG.debug("response-->" + responseXML);
            }

        } else {
            int startIndex = Integer.parseInt(staringUUID);
            LOG.debug("numUUIDs -->" + numUUIDs);
            int endIndex = Integer.parseInt(numUUIDs);
            RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
            List<String> uuidList = repositoryBrowser.getUUIDs(category, type, format, startIndex, endIndex);
            Response response = buildResponse(uuidList, category, type, format);
            responseXML = new ResponseHandler().toXML(response);
            LOG.debug("response XML-->" + responseXML);

        }
        return responseXML;
    }

    /**
     * This method builds the response for the list of UUID's
     *
     * @param uuidList
     * @param category
     * @param type
     * @param format
     * @return
     */
    public Response buildResponse(List<String> uuidList, String category, String type, String format) {
        Response response = new Response();
        response.setOperation("getUUIDs");
        ArrayList<ResponseDocument> responseDocumentList = new ArrayList<ResponseDocument>();
        for (int i = 0; i < uuidList.size(); i++) {
            ResponseDocument responseDocument = new ResponseDocument();
            responseDocument.setId(String.valueOf(i + 1));
            responseDocument.setCategory(category);
            responseDocument.setType(type);
            responseDocument.setFormat(format);
            responseDocument.setUuid(uuidList.get(i));
            responseDocumentList.add(responseDocument);
        }
        response.setDocuments(responseDocumentList);
        return response;
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_XML})
    public String update(@PathVariable("stringContent") String stringContent) {
        LOG.debug("check in content-->" + stringContent);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(stringContent);
        String checkInResponse = checkIn(request, null);
        LOG.debug("checkInResponse-->" + checkInResponse);
        return checkInResponse;
    }

    @PUT
    @Path("{bind}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @Produces({MediaType.TEXT_XML})
    public String bind(@PathParam("bind") String operation, @QueryParam("stringContent") String stringContent) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        LOG.info("request-->" + stringContent);
        Response response = new Response();
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(stringContent);
        documentService = DocumentServiceImpl.getInstance();
        if ((request.getOperation().equalsIgnoreCase("bind")) || (request.getOperation().equalsIgnoreCase("unbind"))) {
            LOG.info("operation-->" + operation);
            response = documentService.process(request);
            LOG.info("response-->" + new ResponseHandler().toXML(response));
        }

        return new ResponseHandler().toXML(response);
    }


    @DELETE
    @Path("{id}")
    @Produces({MediaType.TEXT_XML})
    public String remove(@PathParam("id") String ids, @QueryParam("identifierType") String identifierType,
                         @QueryParam("operation") String operation, @QueryParam("docCategory") String docCategory, @QueryParam("docType") String docType, @QueryParam("docFormat") String docFormat) throws IOException, RepositoryException, OleException, OleDocStoreException {
        LOG.debug("ids-->" + ids);
        LOG.debug("identifier-->" + identifierType);
        LOG.debug("operation-->" + operation);
        Request request = null;
        request = buildRequest(ids, identifierType, operation, docCategory, docType, docFormat);

        documentService = DocumentServiceImpl.getInstance();
        Response response = documentService.process(request);
//        processDeleteRequest(request);
        LOG.debug("response-->" + new ResponseHandler().toXML(response));
        return new ResponseHandler().toXML(response);
    }


    @DELETE
    @Produces({MediaType.TEXT_XML})
    public String removeDocuments(@PathVariable("deleteRequest") String deleteRequest) throws IOException {
        LOG.debug("request xml for delete-->" + deleteRequest);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(deleteRequest);
        Response response = processDeleteRequest(request);
        String deleteResponse = new ResponseHandler().toXML(response);
        LOG.debug("delete response -->" + deleteResponse);

        return deleteResponse;
    }

    private Request buildRequest(String ids, String identifierType, String operation, String category, String type, String format) {
        String[] Id = ids.split(",");
        List<String> idList = new ArrayList<String>();
        List<String> uuidList = new ArrayList<String>();
        Request request = new Request();
        for (int i = 0; i < Id.length; i++) {
            idList.add(Id[i]);
            LOG.debug("adding -->" + idList);
        }
        if ((!StringUtils.isBlank(identifierType)) && identifierType.equalsIgnoreCase("SCN") || identifierType
                .equalsIgnoreCase("ISBN")) {
            uuidList = ServiceLocator.getQueryService().getUUIDList(idList, identifierType);
        } else {
            uuidList = idList;
        }
        request = buildRequest(uuidList, operation, category, type, format);
        return request;


    }

    private Request buildRequest(List<String> uuidList, String operation, String category, String type, String format) {
        Request request = new Request();
        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
        request.setUser("ole-khuntley");
        request.setOperation(operation);
        for (int i = 0; i < uuidList.size(); i++) {
            if (DocumentUniqueIDPrefix.hasPrefix(uuidList.get(i))) {
                Map<String, String> categoryTypeFormat = DocumentUniqueIDPrefix.getCategoryTypeFormat(uuidList.get(i));
                category = categoryTypeFormat.get("category");
                type = categoryTypeFormat.get("type");
                format = categoryTypeFormat.get("format");
            }

            RequestDocument requestDocument = new RequestDocument();
            requestDocument.setCategory(category);
            requestDocument.setType(type);
            requestDocument.setFormat(format);
            requestDocument.setUuid(uuidList.get(i));
            requestDocumentList.add(requestDocument);
        }
        request.setRequestDocuments(requestDocumentList);
        return request;
    }

    protected Response processDeleteRequest(Request dsRequest) throws IOException {
        DeleteManager deleteManager = new DeleteManager();
        List<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
        requestDocuments = dsRequest.getRequestDocuments();
        Response response = null;
        String uuidsNotInOle = null;
        String serviceURL = null;
        StringBuilder uuidsSB = null;
        try {
            if ((requestDocuments == null) || (requestDocuments.size() == 0)) {
                response = deleteManager.getResponse(dsRequest, requestDocuments, "Failed",
                        "Deletion failed: No documents specified for deletion.");
            } else {
                // Build a csv of UUIDs of the documents to be deleted.
                uuidsSB = new StringBuilder();
                for (RequestDocument requestDocument : requestDocuments) {
                    uuidsSB.append(requestDocument.getUuid()).append(",");
                }
                serviceURL = ConfigContext.getCurrentContextConfig().getProperty("uuidCheckServiceURL");
                LOG.info(" uuidCheckServiceURL --------> " + serviceURL);
                boolean verifiedWithOLE = true;
                try {
                    // Use OLE web service to verify the existence of the UUIDs in OLE.
                    OleWebServiceProvider oleWebServiceProvider = new OleWebServiceProviderImpl();

                    OleUuidCheckWebService oleUuidCheckWebService = (OleUuidCheckWebService) oleWebServiceProvider
                            .getService("org.kuali.ole.service.OleUuidCheckWebService", "oleUuidCheckWebService",
                                    serviceURL);

                    uuidsNotInOle = oleUuidCheckWebService
                            .checkUuidExsistence(uuidsSB.substring(0, uuidsSB.length() - 1));
                } catch (Exception e) {
                    verifiedWithOLE = false;
                    LOG.error("Check uuid call to OLE failed:", e);
                    response = deleteManager.getResponse(dsRequest, requestDocuments, "Failure",
                            "Deletion failed: Exception while connecting to OLE service to verify the existence of documents."
                                    + e.getMessage());
                }
                if (verifiedWithOLE) {
                    LOG.info("response uuids from OLE " + uuidsNotInOle);
                    // If the UUIDs do not exist in OLE, delete them from docstore.
                    if ((uuidsNotInOle != null) && (uuidsNotInOle.length() > 0)) {
                        String[] uuids = StringUtils.split(uuidsNotInOle, ",");
                        requestDocuments = new ArrayList<RequestDocument>();
                        for (String id : uuids) {
                            RequestDocument requestDocument = new RequestDocument();
                            requestDocument.setUuid(id);
                            requestDocuments.add(requestDocument);
                        }
                        dsRequest.setRequestDocuments(requestDocuments);
                        try {
                            response = deleteManager.deleteDocs(dsRequest);
                        } catch (Exception e) {
                            response = deleteManager.getResponse(dsRequest, requestDocuments, "Failure",
                                    "Deletion failed: Exception while deleting from docstore. "
                                            + e.getMessage());
                        }
                    } else {
                        response = deleteManager.getResponse(dsRequest, requestDocuments, "Failure",
                                "Deletion failed: Documents exist in OLE database and cannot be deleted.");
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("delete operation failed:", e);
            response = deleteManager.getResponse(dsRequest, requestDocuments, "Failure", "Failed : " + e.getMessage());
        }
        return response;
    }

    public String checkIn(Request dsRequest, Response dsResponse) {
        if (dsResponse == null) {
            dsResponse = new Response();
        }
        String checkInResponse = null;
        List<String> latestVersion = new ArrayList<String>();
        try {
            dsResponse = BeanLocator.getDocumentServiceImpl().process(dsRequest);
            checkInResponse = (new ResponseHandler().toXML(dsResponse));
        } catch (Exception ex) {
//            Response response = ingestNIndexHandlerService.buildResponse(dsRequest);
            String failOverMessage = ex.getMessage();
            failOverMessage = failOverMessage.replace("javax.jcr.ItemNotFoundException", "Document Not Found for uuid");
            checkInResponse = printResponse(dsResponse, dsRequest.getOperation(), "Failure", "Checkin failed",
                    failOverMessage);
        }
        return checkInResponse;
    }

    private String printResponse(Response response, String operation, String status, String message,
                                 String statusMessage) {
        String resp = null;
        if (response == null) {
            response = new Response();
        }
        response.setOperation(operation);
        response.setStatus(status);
        response.setMessage(message);
        response.setStatusMessage(statusMessage);
        resp = new ResponseHandler().toXML(response);
        return resp;
    }

    protected Response checkOut(String uuid) throws Exception {

        String checkoutResponse = null;
        Request req = null;
        Response dsResponse = null;
        try {
            if (DocumentUniqueIDPrefix.hasPrefix(uuid)) {
                Map<String, String> categoryTypeFormat = DocumentUniqueIDPrefix.getCategoryTypeFormat(uuid);
                String category = categoryTypeFormat.get("category");
                String type = categoryTypeFormat.get("type");
                String format = categoryTypeFormat.get("format");
                req = new Request();
                RequestDocument requestDocument = buildRequestDocument(category, type, format, uuid);
                List<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
                requestDocuments.add(requestDocument);
                req.setOperation("checkOut");
                req.setUser("checkOutUser");
                req.setRequestDocuments(requestDocuments);
            }
            if (req == null) {

                req = getRequest(uuid, null, null);
            }
            dsResponse = BeanLocator.getDocumentServiceImpl().process(req);
            if (dsResponse != null) {
                for (ResponseDocument responseDocument : dsResponse.getDocuments()) {
                    if (responseDocument != null && responseDocument.getContent() != null) {
                        checkoutResponse = responseDocument.getContent().getContent();
                    }
                    checkoutResponse = checkoutResponse + "\n";
                }
            }

            LOG.info("xmlResponse-->" + checkoutResponse);
        } catch (Exception ex) {
            LOG.error("Error while performing checkout process", ex);
        }

        return dsResponse;

    }


    private RequestDocument buildRequestDocument(String cat, String type, String format, String uuid) {
        RequestDocument reqDoc = new RequestDocument();
        reqDoc.setCategory(cat);
        reqDoc.setType(type);
        reqDoc.setFormat(format);
        reqDoc.setUuid(uuid);
        return reqDoc;

    }

    private Request getRequest(String uuid, String userId, String action) throws OleDocStoreException, OleException {

        Node nodeByUUID = null;
        Session session = null;
        Request req = new Request();
        if (action == null || "".equalsIgnoreCase(action)) {
            action = "checkOut";
        }
        if (userId == null || "".equalsIgnoreCase(userId)) {
            userId = "RestWebUser";
        }
        session = RepositoryManager.getRepositoryManager().getSession(userId, action);
        String cat = null;
        String type = null;
        String format = null;
        try {
            NodeHandler nodeHandler = new NodeHandler();
            nodeByUUID = nodeHandler.getNodeByUUID(session, uuid);
            String nodePath = nodeByUUID.getPath();
            String[] splitLine = nodePath.split("/");

            if (splitLine != null && splitLine.length >= 4) {
                cat = splitLine[1];
                type = splitLine[2];
                format = splitLine[3];
            } else {
                throw new OleDocStoreException(" This is not a valid UUID ");
            }
            req.setOperation(action);
            req.setUser(userId);
            List<RequestDocument> reqDocList = new ArrayList<RequestDocument>();
            RequestDocument reqDoc = buildRequest(cat, type, format, uuid);
            reqDocList.add(reqDoc);
            req.setRequestDocuments(reqDocList);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            throw new OleDocStoreException(e);
        } finally {
            session.logout();
        }
        return req;
    }

    private RequestDocument buildRequest(String cat, String type, String format, String uuid) {
        RequestDocument reqDoc = new RequestDocument();
        reqDoc.setCategory(cat);
        reqDoc.setType(type);
        reqDoc.setFormat(format);
        reqDoc.setUuid(uuid);
        return reqDoc;

    }

    private ArrayList<File> extractBagFilesFromRequest(@Context HttpServletRequest req,
                                                       @Context HttpServletResponse res) throws Exception {
        File targetDir = null;
        try {
            File file = null;
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
            Iterator items = new ServletFileUpload(fileItemFactory).parseRequest(req).iterator();
            while (items.hasNext()) {
                FileItem item = (FileItem) items.next();
                LOG.info("item Name = " + item.getName() + " ; content type = " + item.getContentType());
                file = new File(FileUtils.getTempDirectory(), item.getName());
                item.write(file);
            }
            targetDir = compressUtils.extractZippedBagFile(file.getAbsolutePath(), null);
            LOG.info("extractedBagFileLocation " + targetDir);
        } catch (IOException e) {
            LOG.error("IOException", e);
            sendResponseBag(res, e.getMessage(), "failure");
        } catch (FormatHelper.UnknownFormatException unknownFormatException) {
            LOG.error("unknownFormatException", unknownFormatException);
            sendResponseBag(res, unknownFormatException.getMessage(), "failure");
        }
        return compressUtils.getAllFilesList(targetDir);
    }

    /**
     * Method to send response for a request in a zipped bag.
     *
     * @param res
     * @param message
     * @param status
     * @return
     */
    private void sendResponseBag(@Context HttpServletResponse res, String message, String status) {
        Response response = new Response();
        response.setMessage(message);
        response.setStatus(status);
        sendResponseBag(res, response);
    }

    private void sendResponseBag(@Context HttpServletResponse res, @Context Response docStoreResponse) {
        String responseXML = new ResponseHandler().toXML(docStoreResponse);
        try {
            File output = File.createTempFile("checkout.", ".output");
            FileUtils.deleteQuietly(output);
            output.mkdirs();
            File file = new File(output + File.separator + "response.xml");
            Writer writer = null;
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(responseXML);
            writer.close();
            File zipFile = compressUtils.createZippedBagFile(output);
            res.setContentType("application/zip");
            sendResponseAsFile(res, zipFile);
            zipFile.delete();
        } catch (IOException e) {
            LOG.error("IOException", e);
        }
    }

    /**
     * Method sends the response generated for the given request
     *
     * @param res
     * @param file
     * @throws IOException
     */
    private void sendResponseAsFile(HttpServletResponse res, File file) throws IOException {
        ServletOutputStream stream = res.getOutputStream();
        FileInputStream fos = new FileInputStream(file.getAbsolutePath());
        BufferedInputStream buf = new BufferedInputStream(fos);
        int readBytes = 0;
        while ((readBytes = buf.read()) != -1) {
            stream.write(readBytes);
        }
        if (stream != null) {
            stream.close();
        }
        if (buf != null) {
            buf.close();
        }
        res.flushBuffer();
    }

    private void sendUnavailableResponseString(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("The Requested Operation is not available");
        } catch (IOException e) {
            LOG.error("IOException", e);
        }
    }

}
