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

import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sreekanth
 */
public class DeleteManager {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteManager.class);
    private static final String DELETE_WITH_LINKED_DOCS = "deleteWithLinkedDocs";
    private static final String BIBLIOGRAPHIC = "bibliographic";
    private static final String INSTANCE_IDENTIFIER = "instanceIdentifier";
    private static final String SUCCESS = "Success";
    private static final String FAILURE = "Failure";

    public Response deleteDocs(Request request) throws OleException {
        Session session = null;
        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        List<String> uuidsList = new ArrayList<String>();
        String status = null;
        String category = null;
        Response response = new Response();
        session = RepositoryManager.getRepositoryManager().getSession(request.getUser(), request.getOperation());
        try {
            List<String> respositoryUuidList = new ArrayList<String>();
            for (Iterator<RequestDocument> iterator = requestDocuments.iterator(); iterator.hasNext(); ) {
                RequestDocument document = iterator.next();
                category = document.getCategory();
                String uuid = document.getUuid();
                /* if (uuid == null)
              uuid = document.getId();*/
                String operation = request.getOperation();
                LOG.debug("operation-->" + operation);
                respositoryUuidList = getLinkedDocsFromRepository(uuid, session, respositoryUuidList, operation);
            }
            LOG.debug("respository UuidList size-->" + respositoryUuidList.size());
            deleteFromRepository(respositoryUuidList, session);
            String statusValue = ServiceLocator.getIndexerService().deleteDocuments(category, respositoryUuidList);
            if (statusValue.equalsIgnoreCase(IndexerService.SUCCESS)) {
                status = SUCCESS;
                response = getResponse(request, requestDocuments, status, "Documents Deleted Successfully");
                session.save();
            } else {
                status = FAILURE + " - Invalid uuid";
                response = getResponse(request, requestDocuments, FAILURE, status);
            }
            LOG.debug("status" + status);

        } catch (Exception e) {
            String failOverMessage = e.getMessage();
            if (e instanceof ItemNotFoundException) {
                failOverMessage = "Document Not Found for uuid : " + failOverMessage;
            }
            response = getResponse(request, requestDocuments, FAILURE, "Delete Failed, Cause: " + failOverMessage);
            LOG.error("Delete Failed, Cause : " + failOverMessage, e);
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }
        return response;
    }

    private List<String> getLinkedDocsFromRepository(String uuid, Session session, List<String> respositoryUuidList,
                                                     String operation) throws OleException, RepositoryException {

        Node node = session.getNodeByIdentifier(uuid);
        if (operation.equalsIgnoreCase(DELETE_WITH_LINKED_DOCS)) {
            if (node.getPath().contains(BIBLIOGRAPHIC)) {
                try {
                    String instanceId = node.getProperty(INSTANCE_IDENTIFIER).getString();
                    respositoryUuidList.add(instanceId);
                } catch (RepositoryException e) {
                    LOG.error("Instance property not found ", e);
                }
            }
        }
        respositoryUuidList.add(uuid);
        return respositoryUuidList;
    }


    public Response getResponse(Request req, List<RequestDocument> requestDocuments, String status,
                                String statusMessage) {
        Response response = new Response();
        response.setUser(req.getUser());
        response.setOperation(req.getOperation());
        response.setStatus(status);
        response.setStatusMessage(statusMessage);
        ArrayList<ResponseDocument> responseDocumentList = new ArrayList<ResponseDocument>();
        for (int i = 0; i < requestDocuments.size(); i++) {
            ResponseDocument responseDocument = new ResponseDocument();
            responseDocument.setId(requestDocuments.get(i).getId());
            responseDocument.setCategory(requestDocuments.get(i).getCategory());
            responseDocument.setType(requestDocuments.get(i).getType());
            responseDocument.setFormat(requestDocuments.get(i).getFormat());
            responseDocument.setUuid(requestDocuments.get(i).getUuid());
            responseDocumentList.add(responseDocument);
        }
        response.setDocuments(responseDocumentList);
        return response;
    }


    private void deleteFromRepository(List<String> uuidsList, Session session) throws Exception {
        if (uuidsList != null && uuidsList.size() > 0) {
            for (int i = 0; i < uuidsList.size(); i++) {
                Node deleteNode = new NodeHandler().getNodeByUUID(session, uuidsList.get(i));
                LOG.debug("deleteNodes.........." + deleteNode);
                if (deleteNode != null) {
                    LOG.debug("deleteNodes from docstore.........." + deleteNode);
                    deleteNode.remove();
                }
            }
        }
    }

    public void cleanUpDocStoreData() throws OleException, RepositoryException {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("admin", "cleanUpDocStoreData");
        Node rootNode = session.getRootNode();
        for (Iterator<Node> iterator = rootNode.getNodes(); iterator.hasNext(); ) {
            Node catNode = iterator.next();
            if (catNode != null && !catNode.getName().equals("jcr:system")) {
                catNode.remove();
            }
        }
        session.save();
    }


}
