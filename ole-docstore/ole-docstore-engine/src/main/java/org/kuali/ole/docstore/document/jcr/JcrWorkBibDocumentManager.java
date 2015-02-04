package org.kuali.ole.docstore.document.jcr;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.repository.WorkBibNodeManager;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implements the DocumentManager interface for [Work-Bib-*] documents.
 *
 * @version %I%, %G%
 * @author: tirumalesh.b
 * Date: 31/8/12 Time: 7:04 PM
 */

public class JcrWorkBibDocumentManager
        extends JcrAbstractDocumentManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static JcrWorkBibDocumentManager ourInstance = null;

    public static JcrWorkBibDocumentManager getInstance() {
        if (null == ourInstance) {
            ourInstance = new JcrWorkBibDocumentManager();
        }
        return ourInstance;
    }

    protected JcrWorkBibDocumentManager() {
        super();
        this.nodeManager = WorkBibNodeManager.getInstance();
    }

    /**
     * During string ingest there may be linked instance documents in the request.
     * In this case, the linked instance documents will also be ingested
     *
     * @param requestDocument
     * @param responseDocument
     */
    protected void buildLinkedResponseDocuments(RequestDocument requestDocument, ResponseDocument responseDocument) {
        if ((null == requestDocument.getLinkedRequestDocuments()) || (requestDocument.getLinkedRequestDocuments().size() == 0)) {
            return;
        }
        for (RequestDocument linkedRequestDocument : requestDocument.getLinkedRequestDocuments()) {
            DocumentManager documentManager = BeanLocator.getDocstoreFactory()
                    .getDocumentManager(linkedRequestDocument.getCategory(), linkedRequestDocument.getType(), linkedRequestDocument.getFormat());
            ResponseDocument linkedResponseDocument = documentManager.buildResponseDocument(linkedRequestDocument);
            responseDocument.getLinkedDocuments().add(linkedResponseDocument);
        }
    }

    /**
     * Invoked from JcrAbstractDocumentManager.java
     * Updating BibStatus fields based on ingest/checking operation is invoked
     *
     * @param requestDocument
     * @param node
     */
    @Override
    protected void modifyAdditionalAttributes(RequestDocument requestDocument, Node node) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = requestDocument.getUser();
        String statusFromReqDoc = "";
        String statusFromNode = "";
        AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
        if (additionalAttributes == null) {
            additionalAttributes = new AdditionalAttributes();
        }
        if (requestDocument.getOperation() != null) {
            if (requestDocument.getOperation().equalsIgnoreCase(Request.Operation.ingest.toString())) {
                statusFromReqDoc = additionalAttributes.getAttribute(AdditionalAttributes.STATUS);
                additionalAttributes.setAttribute(AdditionalAttributes.DATE_ENTERED, dateStr);
                additionalAttributes.setAttribute(AdditionalAttributes.CREATED_BY, user);
                //Add statusUpdatedBy and statusUpdatedOn if input request is having non empty status field
                if (StringUtils.isNotEmpty(statusFromReqDoc)) {
                    additionalAttributes.setAttribute(AdditionalAttributes.STATUS_UPDATED_BY, user);
                    additionalAttributes.setAttribute(AdditionalAttributes.STATUS_UPDATED_ON, dateStr);
                }
            } else if (requestDocument.getOperation().equalsIgnoreCase(Request.Operation.checkIn.toString())) {
                if (requestDocument.getAdditionalAttributes() != null) {
                    statusFromReqDoc = additionalAttributes.getAttribute(Constants.STATUS);
                }

                try {
                    if (node != null) {
                        if (node.hasProperty(Constants.STATUS)) {
                            statusFromNode = node.getProperty(Constants.STATUS).getString();
                            //compare status parameter of the node and status parameter from reqDoc
                            if (!statusFromNode.equals(statusFromReqDoc)) {
                                additionalAttributes.setAttribute(Constants.STATUS_UPDATED_BY, user);
                                additionalAttributes.setAttribute(Constants.STATUS_UPDATED_ON, dateStr);
                            }
                        }
                    }
                } catch (RepositoryException e) {
                    logger.error("Exception while getting node property, Cause:" + e.getMessage(), e);
                }

                additionalAttributes.setAttribute(Constants.UPDATED_BY, user);
                additionalAttributes.setAttribute(Constants.LAST_UPDATED, dateStr);
                requestDocument.setAdditionalAttributes(additionalAttributes);
            }
        }
    }

    @Override
    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocument, Object object) {
        return null;
    }

    @Override
    public ResponseDocument delete(RequestDocument requestDocument, Object object) throws Exception {
        Session session = (Session) object;
        ResponseDocument responseDocument = deleteVerify(requestDocument, session);
        List<String> identifierListToDelete = new ArrayList<String>();
        if (responseDocument.getStatus().equalsIgnoreCase("success")) {
            Node bibNode = session.getNodeByIdentifier(requestDocument.getUuid());
            if (bibNode.hasProperty("instanceIdentifier")) {
                String instanceIdentifier = bibNode.getProperty("instanceIdentifier").getString();
                String instanceIds[] = instanceIdentifier.split(",");

                //Get the instance ids of Bib to delete
                for (String instanceId : instanceIds) {
                    identifierListToDelete.add(instanceId);
                }
            }
            String bibIdentifier = requestDocument.getUuid();
            identifierListToDelete.add(bibIdentifier);
            deleteFromRepository(identifierListToDelete, session);
            //TODO build response document
        } else {
            //TODO build response document
        }
        return new ResponseDocument();
    }

    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        Response response = new Response();
        Session session = (Session) object;
        ResponseDocument responseDocument = new ResponseDocument();
        List<ResponseDocument> responseDocumentList = new ArrayList<ResponseDocument>();
        List<String> instanceIdentifierList = new ArrayList<String>();
        Node bibNode = session.getNodeByIdentifier(requestDocument.getUuid());

        if (bibNode.hasProperty("instanceIdentifier")) {
            String instanceIdentifier = bibNode.getProperty("instanceIdentifier").getString();
            String instanceIds[] = instanceIdentifier.split(",");
            for (String instanceId : instanceIds) {
                instanceIdentifierList.add(instanceId);
            }
        }
        for (String instanceIdentifierValue : instanceIdentifierList) {
            Node instanceNode = session.getNodeByIdentifier(instanceIdentifierValue);
            String bibIdentifier = instanceNode.getProperty("bibIdentifier").getString();
            String[] bibIds = bibIdentifier.split(",");
            if (bibIds.length > 1) {
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setStatus("failure'");
                responseDocument.setStatusMessage("Instance is bound with more than one bid. So deletion cannot be done");
                return responseDocument;
            }
            boolean exists = checkInstancesOrItemsExistsInOLE(instanceIdentifierValue, session);
            if (exists) {
                responseDocument.setId(requestDocument.getId());
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setStatus("failure");
                responseDocument.setStatusMessage("Instances or Items in use. So deletion cannot be done");
                return responseDocument;
            }
        }
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(requestDocument.getUuid());
        responseDocument.setStatus("success");
        responseDocument.setStatusMessage("success");
        return responseDocument;
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        for (RequestDocument linkDoc : requestDocument.getLinkedRequestDocuments()) {
            Session session = (Session) object;
            JcrWorkInstanceDocumentManager.getInstance().validateInput(linkDoc, session, valuesList);
        }
    }
}
