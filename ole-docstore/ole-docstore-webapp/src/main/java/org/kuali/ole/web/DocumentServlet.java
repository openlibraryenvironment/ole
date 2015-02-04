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
package org.kuali.ole.web;

import gov.loc.repository.bagit.utilities.FormatHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.discovery.service.OleDocstoreDataRetrieveService;
import org.kuali.ole.docstore.discovery.service.OleDocstoreDumpService;
import org.kuali.ole.docstore.discovery.service.ServiceLocator;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.service.BeanLocator;
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
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class DocumentServlet
        extends HttpServlet {

    private static final long serialVersionUID = -3717561557966540651L;
    private static final Logger LOG = LoggerFactory
            .getLogger(DocumentServlet.class);
    private CompressUtils compressUtils = new CompressUtils();
    /**
     * Singleton instance of DocumentStoreManager used by any servlet request.
     */
    private DocumentStoreManager documentStoreManager = BeanLocator.getDocumentStoreManager();
    /**
     * Singleton instance of IngestNIndexHandlerService used by any servlet request.
     */
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator
            .getIngestNIndexHandlerService();


    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String docAction = null;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        //        String updatesEnabled = getServletConfig().getInitParameter("updatesEnabled");
        //        boolean isUpdateEnabled = false;
        //        if ("false".equalsIgnoreCase(updatesEnabled)) {
        //            isUpdateEnabled = false;
        //        } else {
        //            isUpdateEnabled = true;
        //        }
        boolean isUpdateEnabled = ProcessParameters.IS_UPDATE_ENABLED;
        LOG.debug("updateEnabled = " + isUpdateEnabled);
        if (isMultipart) {
            LOG.debug("Processing in Multipart : START");
            try {
                ArrayList<File> files = extractBagFilesFromRequest(request, response);
                RequestHandler rh = new RequestHandler();
                Request dsRequest = null;
                for (File file : files)
                    if (file.getName().equalsIgnoreCase("request.xml")) {
                        String rqStr = FileUtils.readFileToString(file);
                        dsRequest = rh.toObject(rqStr);
                        for (RequestDocument rd : dsRequest.getRequestDocuments())
                            if (rd.getDocumentName() != null)
                                for (File fl : files)
                                    if (fl.getName().equals(rd.getDocumentName())) {
                                        rd.setDocumentName(fl.getAbsolutePath());
                                        break;
                                    }
                        if ("ingest".equalsIgnoreCase(dsRequest.getOperation())) {
                            if (isUpdateEnabled) {
                                Response docStoreResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(dsRequest);
                                sendResponseBag(response, docStoreResponse);
                            } else {
                                sendUnavailableResponseString(response);
                            }
                        } else if ("checkOut".equalsIgnoreCase(dsRequest.getOperation())) {
                            File output = documentStoreManager.checkOutMultiPart(dsRequest);
                            sendResponseAsFile(response, output);
                            output.delete();
                        } else if ("checkIn".equalsIgnoreCase(dsRequest.getOperation())) {
                            if (isUpdateEnabled) {
                                Response dsResponse = new Response();
                                checkIn(dsRequest, dsResponse);
                                sendResponseBag(response, dsResponse);
                            } else {
                                sendUnavailableResponseString(response);
                            }
                        } else if ("delete".equalsIgnoreCase(dsRequest.getOperation())) {
                            if (isUpdateEnabled) {
                                Response dsResponse = processDeleteRequest(dsRequest);
                                sendResponseBag(response, dsResponse);
                            } else {
                                sendUnavailableResponseString(response);
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request Operation: " + dsRequest.getOperation());
                        }
                        break;
                    }
                files.add(files.get(files.size() - 1).getParentFile());
                compressUtils.deleteFiles(files);
                LOG.debug("Got Files in here : " + files);
            } catch (Exception e) {
                LOG.error("Invalid Request : " + e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request : " + e.getMessage());
            }
            LOG.info("Processing in Multipart : END");
        } else {
            LOG.debug("isMultipart-->" + isMultipart);
            docAction = request.getParameter("docAction");
            LOG.debug("docAction-->" + docAction);
            String uuid = request.getParameter("uuid");
            String docFormat = request.getParameter("docFormat");
            LOG.debug("docFormat=" + docFormat);
            if (null != uuid && uuid.length() > 0 && (null == docAction || "".equalsIgnoreCase(docAction))) {
                checkOut(request, response);
            } else if ("checkOut".equalsIgnoreCase(docAction)) {
                checkOut(request, response);
            } else if ("checkIn".equalsIgnoreCase(docAction)) {
                if (isUpdateEnabled) {
                    Request dsRequest = new RequestHandler().toObject(request.getParameter("stringContent"));
                    String checkInResponse = checkIn(dsRequest, null);
                    response.setContentType("text/xml");
                    response.getWriter().println(checkInResponse);
                } else {
                    sendUnavailableResponseString(response);
                }
            } else if ("link".equalsIgnoreCase(docAction)) {
                if (isUpdateEnabled) {
                    link(request, response);
                } else {
                    sendUnavailableResponseString(response);
                }
            } else if ("ingestContent".equalsIgnoreCase(docAction)) {
                if (isUpdateEnabled) {
                    ingestStringContent(request, response);
                } else {
                    sendUnavailableResponseString(response);
                }
            } else if (docAction.contains("delete")) {
                Response dsResponse = null;
                if (isUpdateEnabled) {
                    Request dsRequest = null;
                    String identifierType = request.getParameter("identifierType");
                    LOG.info("identifierType->" + identifierType);
                    String ids = request.getParameter("requestContent");
                    if ((ids != null) && (!StringUtils.isBlank(ids))) {
                        LOG.info("requestContent-->" + ids);
                        dsRequest = buildRequest(ids, identifierType, docAction);
                        //New Refactored code starts here
                        try {
                            dsRequest = buildRequestForDelete(ids, identifierType, docAction);
                            dsResponse = BeanLocator.getDocstoreFactory().getDocumentService().process(dsRequest);
                        } catch (Exception e) {
                            LOG.error("", e);
                            Response response1 = new Response();
                            response1.setMessage(e.getMessage());
                            response1.setOperation("delete");
                            response1.setStatus("Failed");
                            response.setContentType("text/xml");
                            response.getWriter().println(new ResponseHandler().toXML(response1));
                        }
                        //New Refactored code ends here
                    }
//                    dsResponse = processDeleteRequest(dsRequest);
//                    response.setContentType("text/xml");
                    response.getWriter().println(new ResponseHandler().toXML(dsResponse));
                } else {
                    sendUnavailableResponseString(response);
                }
            } else if (docAction.contains("cleanRepository")) {
                if (isUpdateEnabled) {
                    cleanRepository(request, response);
                } else {
                    sendUnavailableResponseString(response);
                }
            } else if (docAction.contains("transferInstances")) {
                LOG.debug("In if transferInstances");
                String operation = request.getParameter("docAction");
                String requestXML = request.getParameter("stringContent");
                LOG.debug("In if transferInstances requestXML " + requestXML);
                Request dsRequest = new RequestHandler().toObject(requestXML);
                try {
                    BeanLocator.getDocstoreFactory().getDocumentService().process(dsRequest);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            } else if (docAction.contains("transferItems")) {
                LOG.debug("In if transferItems");
                String operation = request.getParameter("docAction");
                String requestXML = request.getParameter("stringContent");
                LOG.debug("In if transferItems requestXML " + requestXML);
                Request dsRequest = new RequestHandler().toObject(requestXML);
                try {
                    Response transferItemResponse = BeanLocator.getDocstoreFactory().getDocumentService().process(dsRequest);
                    LOG.debug("Document Servlet transferItemResponse " + new ResponseHandler().toXML(transferItemResponse));
                    response.getWriter().println(new ResponseHandler().toXML(transferItemResponse));

                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            } else if (docAction.equals("instanceDetails")) {
                String bibUUIDs = request.getParameter("bibIds");
                String format = request.getParameter("format");
                List<String> bibUUIDList = getBibIdList(bibUUIDs);
                OleDocstoreDataRetrieveService oleDocstoreDataRetrieveService = new OleDocstoreDataRetrieveService();
                String instanceResponse = oleDocstoreDataRetrieveService.getInstanceDetails(bibUUIDList, format);

                if (instanceResponse == null || instanceResponse.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    if (bibUUIDList.size() == 1)
                        response.getWriter().println("<html><body><b>No record found for the given id </b></body></html>" + bibUUIDList.get(0));
                    else if (bibUUIDList.size() > 1)
                        response.getWriter().println("<html><body><b>No record found for the given id's </b></body></html>");
                } else {
                    if (null == format) {
                        response.setContentType("text/xml");
                    } else if (format.equalsIgnoreCase("xml")) {
                        response.setContentType("text/xml");
                    } else if (format.equalsIgnoreCase("json")) {
                        response.setContentType("application/json");
                    }
                    response.getWriter().println(instanceResponse);
                }
            } else if (docAction.equals("docstoreDBDump")) {
                String requestXML = request.getParameter("requestContent");
                OleDocstoreDumpService oleDocstoreDumpService = new OleDocstoreDumpService();
                try {
                    String instanceResponse = oleDocstoreDumpService.exportDocstoreData(requestXML);
                    response.setContentType("text/xml");
                    response.getWriter().println(instanceResponse);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid docAction.");
            }
        }


    }

//    public void buildRequestForTransfer(String requestXML,String operation){
//       Request dsRequest = new RequestHandler().toObject(requestXML);
//    }


    /**
     * Prepare Request Pojo Object from the given uuids.
     *
     * @param ids
     * @param identifierType
     * @param operation
     * @return
     */
    private Request buildRequestForDelete(String ids, String identifierType, String operation) throws Exception {
        String[] Id = ids.split(",");
        List<String> idList = new ArrayList<String>();
        List<String> uuidList = new ArrayList<String>();
        Request request = null;
        for (int i = 0; i < Id.length; i++) {
            idList.add(Id[i]);
            LOG.debug("adding -->" + idList);
        }
        if ((!StringUtils.isBlank(identifierType)) && (identifierType.equalsIgnoreCase("SCN") || identifierType
                .equalsIgnoreCase("ISBN"))) {
            uuidList = ServiceLocator.getQueryService().getUUIDList(idList, identifierType);
        } else {
            uuidList = idList;
        }
        request = identifyDeleteableDocuments(uuidList, operation);
        return request;
    }

    /**
     * Identifies which uuids exists in the OLE from the given uuids. uuids which exists in OLE are not deleted.
     *
     * @param uuidsList
     * @param operation
     * @return
     * @throws Exception
     */
    public Request identifyDeleteableDocuments(List<String> uuidsList, String operation) throws Exception {
        Request dsRequest = new Request();
        dsRequest.setUser("ole-khuntley");
        dsRequest.setOperation(operation);

        List<RequestDocument> requestDocuments = null;
        Response response = null;
        String uuidsNotInOle = null;
        String serviceURL = null;
        StringBuilder uuidsSB = null;
        // Build a csv of UUIDs of the documents to be deleted.
        uuidsSB = new StringBuilder();
        for (String uuid : uuidsList) {
            uuidsSB.append(uuid).append(",");
        }
        serviceURL = ConfigContext.getCurrentContextConfig().getProperty("uuidCheckServiceURL");
        LOG.info(" uuidCheckServiceURL --------> " + serviceURL);
        //        uuidsNotInOle = uuidsSB.substring(0, uuidsSB.length() - 1);

        OleWebServiceProvider oleWebServiceProvider = new OleWebServiceProviderImpl();

        OleUuidCheckWebService oleUuidCheckWebService = (OleUuidCheckWebService) oleWebServiceProvider
                .getService("org.kuali.ole.service.OleUuidCheckWebService", "oleUuidCheckWebService", serviceURL);

        uuidsNotInOle = oleUuidCheckWebService.checkUuidExsistence(uuidsSB.substring(0, uuidsSB.length() - 1));
        LOG.info("response uuids from OLE " + uuidsNotInOle);
        // If the UUIDs do not exist in OLE, delete them from docstore.
        if ((uuidsNotInOle != null) && (uuidsNotInOle.length() > 0)) {
            String[] uuids = StringUtils.split(uuidsNotInOle, ",");
            requestDocuments = new ArrayList<RequestDocument>();
            for (String id : uuids) {
                RequestDocument requestDocument = new RequestDocument();
                requestDocument.setCategory(DocCategory.WORK.getCode());
                requestDocument.setFormat(DocFormat.MARC.getCode());
                requestDocument.setType(DocType.BIB.getDescription());
                requestDocument.setUuid(id);
                requestDocument.setOperation(dsRequest.getOperation());
                requestDocuments.add(requestDocument);
            }
            dsRequest.setRequestDocuments(requestDocuments);
        }
        return dsRequest;
    }

    /**
     * @param ids
     * @param identifierType
     * @param operation
     * @return
     */
    private Request buildRequest(String ids, String identifierType, String operation) {
        String[] Id = ids.split(",");
        List<String> idList = new ArrayList<String>();
        List<String> uuidList = new ArrayList<String>();
        Request request = new Request();
        for (int i = 0; i < Id.length; i++) {
            idList.add(Id[i]);
            LOG.debug("adding -->" + idList);
        }
        if ((!StringUtils.isBlank(identifierType)) && (identifierType.equalsIgnoreCase("SCN") || identifierType.equalsIgnoreCase("ISBN"))) {
            uuidList = ServiceLocator.getQueryService().getUUIDList(idList, identifierType);
        } else {
            uuidList = idList;
        }

        request = buildRequest(uuidList, operation);
        return request;
    }

    /**
     * @param uuidList
     * @param operation
     * @return
     */

    private Request buildRequest(List<String> uuidList, String operation) {
        Request request = new Request();
        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
        request.setUser("ole-khuntley");
        request.setOperation(operation);
        for (int i = 0; i < uuidList.size(); i++) {
            RequestDocument requestDocument = new RequestDocument();
            //requestDocument.setId(uuidList.get(i));
            requestDocument.setUuid(uuidList.get(i));
            requestDocumentList.add(requestDocument);
        }
        request.setRequestDocuments(requestDocumentList);
        return request;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // checkOut(request, response);
        doPost(request, response);

    }

    private void cleanRepository(HttpServletRequest request, HttpServletResponse response) {
        DeleteManager deleteManager = new DeleteManager();
        try {

            deleteManager.cleanUpDocStoreData();
        } catch (Exception e) {
            LOG.error("Error while cleaning Repository process", e);
        }

    }

    protected void checkOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuid = request.getParameter("uuid");
        LOG.info("check out request uuid-->" + uuid);
        String action = request.getParameter("docAction");
        String requestType = request.getParameter("dataType");
        String docFormat = request.getParameter("docFormat");
        LOG.debug("docFormat-->" + docFormat);
        String userId = getUserId(request);
        PrintWriter out = null;
        String checkoutResponse = null;
        String setContentType = null;
        Request req = null;
        try {
            if (!DocumentUniqueIDPrefix.hasPrefix(uuid)) {
                uuid = "wbm-" + uuid;
            }
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
                req = getRequest(uuid, userId, action, docFormat);
            }
            if (docFormat == null || "".equalsIgnoreCase(docFormat)) {
                if (req != null && req.getRequestDocuments() != null && req.getRequestDocuments().size() > 0) {
                    docFormat = req.getRequestDocuments().get(0).getFormat();
                }
            }

            if (docFormat != null && docFormat.equalsIgnoreCase(DocFormat.PDF.getCode())) {
                setContentType = "application/pdf";
                checkoutBinary(response, setContentType, req);
                //                checkoutBinary(response, uuid, action, docFormat, userId, checkoutResponse, setContentType);
            } else if (docFormat != null && docFormat.equalsIgnoreCase(DocFormat.DOC.getCode())) {
                setContentType = "application/msword";
                checkoutBinary(response, setContentType, req);
                //                checkoutBinary(response, uuid, action, docFormat, userId, checkoutResponse, setContentType);
            } else {

                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
//                checkoutResponse = documentStoreManager.checkOut(uuid, userId, action);
                Response dsResponse = BeanLocator.getDocstoreFactory().getDocumentService().process(req);
                if (dsResponse != null) {
                    for (ResponseDocument responseDocument : dsResponse.getDocuments()) {
                        if (responseDocument != null && responseDocument.getContent() != null) {
                            checkoutResponse = responseDocument.getContent().getContent();
                        }
                        checkoutResponse = checkoutResponse + "\n";
                    }
                }

                //                if (((null == requestType || !requestType.equals("raw")) && checkoutResponse != null)) {
                //                    checkoutResponse = checkoutResponse.replaceAll("&", "&amp;");
                //                    checkoutResponse = checkoutResponse.replaceAll("< ", "&lt; ");
                //                    checkoutResponse = checkoutResponse.replace("&apos;", "\'");
                //                }

                out.println(new ResponseHandler().toXML(dsResponse));
            }
        } catch (Exception ex) {
            LOG.error("Error while performing checkout process", ex);
            checkoutResponse = printResponse(null, "Check-out", "Failure", "Checkout failed",
                    "Error while performing checkout process");
            out.println(checkoutResponse);
        }

    }

    private void checkoutBinary(HttpServletResponse response, String uuid, String action, String docFormat,
                                String userId, String checkOutResponse, String setContentType) throws IOException {
        HashMap<String, String> checkOutMap = null;
        checkOutMap = new HashMap<String, String>();
        checkOutMap.put("uuid", uuid);
        checkOutResponse = documentStoreManager.checkOutBinary(uuid, userId, action, docFormat);
        LOG.info("checkOutResponse-->" + checkOutResponse);
        File file = new File(checkOutResponse);
        String fileName = checkOutResponse
                .substring(checkOutResponse.lastIndexOf(File.separator), checkOutResponse.length());
        LOG.info("fileName-->" + fileName);
        response.setContentType(setContentType);
        response.addHeader("Content-Disposition", "inline; filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream responseOutputStream = response.getOutputStream();
        int bytes;
        while ((bytes = fileInputStream.read()) != -1) {
            responseOutputStream.write(bytes);
        }
    }

    private Request getRequest(String uuid, String userId, String action, String docFormat)
            throws OleDocStoreException, OleException {

        Node nodeByUUID = null;
        Session session = null;
        Request req = new Request();
        if (action == null || "".equalsIgnoreCase(action)) {
            action = "checkOut";
        }
        if (userId == null || "".equalsIgnoreCase(action)) {
            userId = "checkOutUser";
        }
        try {
            session = RepositoryManager.getRepositoryManager().getSession(userId, action);
        } catch (Exception e) {
            LOG.error("Excaption while creating a session" + e.getMessage(), e);
            throw new OleDocStoreException(e.getMessage(), e);
        }
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
            if (docFormat == null || "".equalsIgnoreCase(docFormat)) {
                docFormat = format;
            }
            req.setUser(userId);
            req.setOperation(action);
            List<RequestDocument> reqDocList = new ArrayList<RequestDocument>();
            RequestDocument reqDoc = buildRequestDocument(cat, type, format, uuid);
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

    private RequestDocument buildRequestDocument(String cat, String type, String format, String uuid) {
        RequestDocument reqDoc = new RequestDocument();
        reqDoc.setCategory(cat);
        reqDoc.setType(type);
        reqDoc.setFormat(format);
        reqDoc.setUuid(uuid);
        return reqDoc;

    }

    private void checkoutBinary(HttpServletResponse response, String setContentType, Request req)
            throws IOException, OleDocStoreException, RepositoryException, OleException {
        Response dsResponse = BeanLocator.getDocstoreFactory().getDocumentService().process(req);

        if (dsResponse != null) {
            for (ResponseDocument responseDocument : dsResponse.getDocuments()) {
                if (responseDocument != null && responseDocument.getContent() != null) {
                    String responseContent = responseDocument.getContent().getContent();
                    //TODO  multiple response documents content.
                    if (responseContent != null) {
                        LOG.info("checkOutResponse-->" + responseContent);
                        File file = new File(responseContent);
                        String fileName = responseContent
                                .substring(responseContent.lastIndexOf(File.separator), responseContent.length());
                        LOG.info("fileName-->" + fileName);
                        response.setContentType(setContentType);
                        response.addHeader("Content-Disposition", "inline; filename=" + fileName);
                        response.setHeader("Pragma", "No-cache");
                        FileInputStream fileInputStream = new FileInputStream(file);
                        OutputStream responseOutputStream = response.getOutputStream();
                        int bytes;
                        while ((bytes = fileInputStream.read()) != -1) {
                            responseOutputStream.write(bytes);
                        }
                    }
                }
            }
        }
    }

    protected String checkIn(Request dsRequest, Response dsResponse) {
        if (dsResponse == null) {
            dsResponse = new Response();
        }
        String checkInResponse = null;
        List<String> latestVersion = new ArrayList<String>();
        /*try {
            dsResponse.setOperation(dsRequest.getOperation());
            dsResponse.setUser(dsRequest.getUser());
            for (int i = 0; i < dsRequest.getRequestDocuments().size(); i++) {
                RequestDocument requestDocument = dsRequest.getRequestDocuments().get(i);
                requestDocument.setOperation(dsRequest.getOperation());
                latestVersion.add(documentStoreManager.updateRecord(requestDocument));
                ResponseDocument responseDocument = new ResponseDocument();
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setVersion(latestVersion.get(i));
                responseDocument.setId(requestDocument.getId());
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                dsResponse.getDocuments().add(responseDocument);
            }
            Response response = ingestNIndexHandlerService.buildResponse(dsRequest);
            checkInResponse = printResponse(response, dsRequest.getOperation(), "Success", "Documents checked in",
                    "Successfully checked in ");
        }*/
        try {
            dsResponse = BeanLocator.getDocstoreFactory().getDocumentService().process(dsRequest);
            checkInResponse = (new ResponseHandler().toXML(dsResponse));
        } catch (Exception ex) {
            Response response = ingestNIndexHandlerService.buildResponse(dsRequest);
            String failOverMessage = ex.getMessage();
            failOverMessage = failOverMessage.replace("javax.jcr.ItemNotFoundException", "Document Not Found for uuid");
            checkInResponse = printResponse(response, dsRequest.getOperation(), "Failure", "Checkin failed",
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

    protected void link(HttpServletRequest request, HttpServletResponse response) {
        String uuidFile1 = request.getParameter("uuid1");
        String uuidFile2 = request.getParameter("uuid2");
        String userId = getUserId(request);
        String action = request.getParameter("docAction");
        String result = null;
        PrintWriter out = null;
        HashMap<String, String> linkMap = null;
        String linkResponse = null;
        try {
            response.setContentType("text/xml");
            out = response.getWriter();
            linkMap = new HashMap<String, String>();
            linkMap.put("uuid1", uuidFile1);
            linkMap.put("uuid2", uuidFile2);
            documentStoreManager.addReference(uuidFile1, uuidFile2, userId, action);

            result = uuidFile2 + " has been successfully linked to  " + uuidFile1;
            //            linkResponse = printResponse("Link", linkMap, responseListName, responseListData, result, "Success");
            linkResponse = printResponse(null, "Link", "Success", "Linked successfully", result);
            out.println(linkResponse);
        } catch (Exception e) {
            LOG.error("addReference( " + uuidFile1 + ", " + uuidFile2 + ") failed", e);
            result = "addReference( " + uuidFile1 + ", " + uuidFile2 + ") failed";
            //            linkResponse = printResponse("Link", linkMap, responseListName, responseListData, result, "Failure");
            linkResponse = printResponse(null, "Link", "Failure", "Linking failed", result);
            out.println(linkResponse);
        }
    }


    protected void ingestStringContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringContent = request.getParameter("stringContent");
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        RequestHandler requestHandler = new RequestHandler();
        Response dsResponse = new Response();
        //Request dsRequest = requestHandler.toObject(stringContent);

        try {
            Request dsRequest = requestHandler.toObject(stringContent);
//            String xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(stringContent);
//            out.print(xmlResponse);
            dsResponse = BeanLocator.getDocstoreFactory().getDocumentService().process(dsRequest);
            out.println(new ResponseHandler().toXML(dsResponse));
        } catch (Exception e) {
            dsResponse.setStatus("Failure");
            dsResponse.setMessage("Ingest string content failed.");
            dsResponse.setStatusMessage("Ingest string content failed due to invalid input file." + e.getMessage());
            out.println(new ResponseHandler().toXML(dsResponse));
            LOG.error("Ingest string content failed", e);
            //response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            //        "Ingest string content failed : " + e.getMessage());
        } finally {
            out.close();
        }
    }

    /**
     * Deletes documents from docstore and discovery.
     * Verifies the existence of the documents in OLE by calling a web service.
     * If the documents exist in OLE, this operation fails.
     * Else, the documents are deleted from docstore and discovery.
     *
     * @param dsRequest
     * @return
     * @throws IOException
     */
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
                    //uuidsSB.append(requestDocument.getId()).append(",");
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
                            //requestDocument.setId(id);
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

    /*public String printResponse(String action, HashMap<String, String> paramMap, String responseListName, List<String> responseListData,
            String resMessage, String statusMessage) {
        String xmlString = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document xmlDocument = docBuilder.newDocument();
            Element root = xmlDocument.createElement("OLEDocstore-call");
            xmlDocument.appendChild(root);

            Element requestRoot = xmlDocument.createElement("request");
            root.appendChild(requestRoot);
            Element command = xmlDocument.createElement("command");
            requestRoot.appendChild(command);
            Text text = xmlDocument.createTextNode(action);
            command.appendChild(text);
            Element params = xmlDocument.createElement("params");
            requestRoot.appendChild(params);
            if (paramMap != null) {
                Iterator<String> iterator = paramMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = paramMap.get(key);
                    Element categoryParam = xmlDocument.createElement("param");
                    params.appendChild(categoryParam);
                    categoryParam.setAttribute("name", key);
                    categoryParam.setAttribute("value", value);
                }
            }

            Element responseRoot = xmlDocument.createElement("response");
            root.appendChild(responseRoot);
            Element status = xmlDocument.createElement("status");
            responseRoot.appendChild(status);
            Text statusText = xmlDocument.createTextNode(statusMessage);
            status.appendChild(statusText);
            Element message = xmlDocument.createElement("message");
            responseRoot.appendChild(message);
            Text messageText = xmlDocument.createTextNode(resMessage);
            message.appendChild(messageText);
            if (statusMessage.equalsIgnoreCase("Success")) {
                if (responseListName != null) {
                    Element uuidRoot = xmlDocument.createElement("list");
                    responseRoot.appendChild(uuidRoot);
                    uuidRoot.setAttribute("name", responseListName);
                    if (responseListData != null && responseListData.size() > 0) {
                        for (int i = 0; i < responseListData.size(); i++) {
                            Element uuid = xmlDocument.createElement("item");
                            uuidRoot.appendChild(uuid);
                            Text uuidVal = xmlDocument.createTextNode(responseListData.get(i));
                            uuid.appendChild(uuidVal);
                        }
                    }
                }
            }
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(xmlDocument);
            transformer.transform(source, result);
            xmlString = sw.toString();
        } catch (Exception ex) {
            LOG.error("printResponse() failed:", ex);
            StringBuffer exceptionMsg = new StringBuffer();
            String DOUBLE_QUOTE = "\"";
            exceptionMsg.append("<?xml version=" + DOUBLE_QUOTE + "1.0" + DOUBLE_QUOTE + " encoding=" + DOUBLE_QUOTE + "UTF-8" + DOUBLE_QUOTE + "?>");
            exceptionMsg.append("<OLEDocstore-call>");
            exceptionMsg.append("<response>");
            exceptionMsg.append("<status>");
            exceptionMsg.append(statusMessage);
            exceptionMsg.append("</status>");
            exceptionMsg.append("<message>");
            exceptionMsg.append(resMessage);
            exceptionMsg.append("</message>");
            exceptionMsg.append("</response>");
            exceptionMsg.append("</OLEDocstore-call>");
            return exceptionMsg.toString();
        }
        return xmlString;
    }*/

    /**
     * @param request
     * @param response
     * @return docAction (add or delete)
     * @throws ServletException
     */
    protected List<FileItem> getMultiPartFileItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        List<FileItem> items = null;
        try {
            if (isMultipart) {
                DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
                ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
                items = uploadHandler.parseRequest(request);
            }
        } catch (Exception ex) {
            LOG.error("Error in getting the Reading the File:", ex);
        }
        return items;
    }

    /**
     * @param reqParam
     * @param items
     * @param request
     * @return docAction (delete)
     */
    protected String getParameter(String reqParam, List<FileItem> items, HttpServletRequest request) {
        String paramValue = null;
        if (items != null && items.size() > 0) {
            Iterator<FileItem> itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString();
                    if (reqParam.equals(name)) {
                        paramValue = value;
                    }
                }
            }
        } else {
            paramValue = request.getParameter(reqParam);
        }
        return paramValue;
    }

    protected String getUserId(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        if (userId == null) {
            userId = "defaultUser";
        }
        return userId;
    }

    private ArrayList<File> extractBagFilesFromRequest(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        File targetDir = null;
        try {
            File file = null;
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
            Iterator items = new ServletFileUpload(fileItemFactory).parseRequest(req).iterator();
            while (items.hasNext()) {
                FileItem item = (FileItem) items.next();
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
    private void sendResponseBag(HttpServletResponse res, String message, String status) {
        Response response = new Response();
        response.setMessage(message);
        response.setStatus(status);
        sendResponseBag(res, response);
    }

    private void sendResponseBag(HttpServletResponse res, Response docStoreResponse) {
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

    private List<String> getBibIdList(String bibUUIDs) {
        List<String> bibIdsList = new ArrayList<String>();
        String[] queueArray = bibUUIDs.split(",");
        for (int i = 0; i < queueArray.length; i++) {
            bibIdsList.add(queueArray[i]);
        }
        return bibIdsList;
    }

}
