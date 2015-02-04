package org.kuali.ole.docstore.document.jcr;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.SourceHoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.repository.WorkBibNodeManager;
import org.kuali.ole.docstore.repository.WorkInstanceNodeManager;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.kuali.ole.docstore.util.ItemExistsException;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.ole.utility.callnumber.CallNumberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.FileNotFoundException;
import java.util.*;

import static org.kuali.ole.docstore.process.ProcessParameters.FILE;

/**
 * Implements the DocumentManager interface for [Work-Instance-*] documents.
 *
 * @version %I%, %G%
 * @author: tirumalesh.b
 * Date: 31/8/12 Time: 7:04 PM
 */

public class JcrWorkInstanceDocumentManager
        extends JcrAbstractDocumentManager {

    private static JcrWorkInstanceDocumentManager ourInstanceJcr = null;
    private InstanceOlemlRecordProcessor olemlProcessor = new InstanceOlemlRecordProcessor();
    private static final Logger LOG = LoggerFactory
            .getLogger(JcrWorkInstanceDocumentManager.class);

    public static JcrWorkInstanceDocumentManager getInstance() {
        if (null == ourInstanceJcr) {
            ourInstanceJcr = new JcrWorkInstanceDocumentManager();
        }
        return ourInstanceJcr;
    }

    protected JcrWorkInstanceDocumentManager() {
        super();
        this.nodeManager = WorkInstanceNodeManager.getInstance();
    }

    @Override
    public void modifyDocumentContent(RequestDocument document, String nodeIdentifier, String parentNodeIdentifier) {


        if (document.getContent().getContentObject() instanceof OleHoldings) { // holdings
            ((OleHoldings) document.getContent().getContentObject()).setHoldingsIdentifier(nodeIdentifier);
            document.getContent()
                    .setContent(olemlProcessor.toXML((OleHoldings) document.getContent().getContentObject()));
        } else if (document.getContent().getContentObject() instanceof SourceHoldings) { // source holdings.
            ((SourceHoldings) document.getContent().getContentObject()).setHoldingsIdentifier(nodeIdentifier);
            document.getContent()
                    .setContent(olemlProcessor.toXML((SourceHoldings) document.getContent().getContentObject()));
        } else if (document.getContent().getContentObject() instanceof Item) { // item
            ((Item) document.getContent().getContentObject()).setItemIdentifier(nodeIdentifier);
            document.getContent().setContent(olemlProcessor.toXML((Item) document.getContent().getContentObject()));
        } else if (document.getContent().getContentObject() instanceof Instance) { // instance
            Instance inst = ((Instance) document.getContent().getContentObject());
            inst.setInstanceIdentifier(parentNodeIdentifier);
            InstanceCollection instanceCollection = new InstanceCollection();
            ArrayList<Instance> oleinsts = new ArrayList<Instance>();
            oleinsts.add(inst);
            instanceCollection.setInstance((oleinsts));
            document.getContent().setContent(olemlProcessor.toXML(instanceCollection));
        }
    }

    @Override
    public Node storeDocument(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException {
        Session session = (Session) object;
        Node fileNode = null;
        String validateMsg = null;
        try {
            AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
            modifyAdditionalAttributes(requestDocument, null);

            // get the parent node
            Node parentNode = nodeManager.getParentNode(requestDocument, session);
            String fileName = requestDocument.getFormat() + FILE;

            // create the file node
            fileNode = nodeManager.createFileNode(requestDocument, fileName, parentNode, session);

            if (isVersioningEnabled()) {
                nodeManager.enableVersioning(fileNode);
            }
            if (requestDocument.getContent().getContentObject() != null) {
                InstanceCollection instColl = (InstanceCollection) requestDocument.getContent().getContentObject();
                for (Instance inst : instColl.getInstance()) {
                    List<String> resIdList = new ArrayList<String>();
                    resIdList.addAll(inst.getResourceIdentifier());
                    for (String resId : inst.getResourceIdentifier()) {
                        if (resId != null && resId.length() > 0) {
                            try {
                                Node bibNode = nodeManager.getNodeByUUID(session, resId);
                                WorkBibNodeManager bibNodeManager = WorkBibNodeManager.getInstance();
                                bibNodeManager.linkNodes(bibNode, fileNode, session);
                            } catch (Exception e) {
                                resIdList.remove(resId);
                            }
                        }
                    }
                    inst.setResourceIdentifier(resIdList);
                }
            }
            buildResponseDocument(requestDocument, session, responseDocument);
            if (validateMsg != null) {
                LOG.debug("in if validation message not null-->" + validateMsg);
                responseDocument.setStatusMessage(validateMsg);
            } else {
                //LOG.info("in else if validation message null");
                responseDocument.setStatusMessage("Document ingested successfully.");
            }
        } catch (Exception e) {
            throw new OleDocStoreException(e);
        }
        return fileNode;
    }

    @Override
    protected void modifyContent(RequestDocument reqDoc, Session session, Node nodeByUUID) throws RepositoryException, FileNotFoundException, OleDocStoreException {
        setIdentifierValueInContent(reqDoc);
        RequestDocument linkReqInfo = new RequestDocument();
        if (reqDoc.getLinkedRequestDocuments() != null && reqDoc.getLinkedRequestDocuments().size() > 0) {
            for (Iterator<RequestDocument> linkIterator = reqDoc.getLinkedRequestDocuments().iterator(); linkIterator
                    .hasNext(); ) {
                linkReqInfo = linkIterator.next();
                if (linkReqInfo != null && linkReqInfo.getId() != null && linkReqInfo.getType().equalsIgnoreCase(
                        DocType.INSTANCE.getCode())) {
                    if (nodeByUUID.hasProperty("instanceIdentifier")) {
                        String instanceIds = nodeByUUID.getProperty("instanceIdentifier").getString() + "," + linkReqInfo.getId();
                        nodeByUUID.setProperty("instanceIdentifier", instanceIds);
                    } else {
                        nodeByUUID.setProperty("instanceIdentifier", linkReqInfo.getId());
                    }
                }
            }
        }
    }

    @Override
    protected void addNewRecordsToDocStore(RequestDocument requestDocument, Session session)
            throws OleDocStoreException {
        if (requestDocument.getContent().getContent() == null && requestDocument.getId() != null) {
            for (RequestDocument linkedItemDocument : requestDocument.getLinkedRequestDocuments()) {
                if (DocType.ITEM.getDescription().equalsIgnoreCase(linkedItemDocument.getType())
                        && linkedItemDocument.getContent().getContent() != null) {
                    nodeManager.ingestItemRecForInstance(linkedItemDocument, requestDocument.getId(), session);
                }
            }
        }
    }

/*    @Override
    protected String updateIndex(RequestDocument requestDocument) throws OleDocStoreException {
        String result = null;
        if (DocType.SOURCEHOLDINGS.getCode().equalsIgnoreCase(requestDocument.getType())) {
            return "success";
        }
        else {
            result = updateRecord(requestDocument);
        }
        return result;
    }*/

    @Override
    public ResponseDocument bind(RequestDocument requestDocument, Object object, String operation) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        Session session = (Session) object;
        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
        String bindFail = "binding failed";
        try {

            // Store data in docstore.
            updatePropertiesOfInstanceNode(requestDocument, session, operation);
            updatePropertiesOfBibNodes(requestDocument, session, operation);
            getContent(requestDocument, session);
/*
            requestDocumentList.add(requestDocument);
            Request request = new Request();
            request.setRequestDocuments(requestDocumentList);
*/
            updateContentXmlOfInstance(requestDocument, session);
/*
            Node nodeByUUID = session.getNodeByIdentifier(requestDocument.getUuid());
            Property instanceNode = nodeByUUID.getProperty("bibIdentifier");
*/

        } catch (Exception e) {
            LOG.info(
                    "Document was updated in indexer but not in docStore, trying to rollback the changes from indexer",
                    e);

            LOG.info(bindFail, e);
            throw new OleDocStoreException(bindFail, e);
        }
        return buildResponseForBind(requestDocument);
    }


    @Override
    public ResponseDocument unbind(RequestDocument requestDocument, Object object, String operation) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        Session session = (Session) object;
        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
        String bindFail = "unbinding failed";
        try {

            // Store data in docstore.
            updatePropertiesOfInstanceNode(requestDocument, session, operation);
            updatePropertiesOfBibNodes(requestDocument, session, operation);
            getContent(requestDocument, session);
/*
            requestDocumentList.add(requestDocument);
            Request request = new Request();
            request.setRequestDocuments(requestDocumentList);
*/
            updateContentXmlOfInstance(requestDocument, session);
/*
            Node nodeByUUID = session.getNodeByIdentifier(requestDocument.getUuid());
            Property instanceNode = nodeByUUID.getProperty("bibIdentifier");
*/

        } catch (Exception e) {
            LOG.info(
                    "Document was updated in indexer but not in docStore, trying to rollback the changes from indexer",
                    e);

            LOG.info(bindFail, e);
            throw new OleDocStoreException(bindFail, e);
        }
        return buildResponseForBind(requestDocument);
    }

    @Override
    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocument, Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        // List<String> bibIdentifierList = new ArrayList<String>();
        Session session = (Session) object;
        List<String> instanceIdentifierList = new ArrayList<String>();
        ResponseDocument responseDocument = new ResponseDocument();
        Node instanceNode = session.getNodeByIdentifier(requestDocument.getUuid());
        String bibIdentifier = instanceNode.getProperty("bibIdentifier").getString();
        String[] bibIds = bibIdentifier.split(",");
        if (bibIds.length > 1) {
            responseDocument.setCategory(requestDocument.getCategory());
            responseDocument.setType(requestDocument.getType());
            responseDocument.setFormat(requestDocument.getFormat());
            responseDocument.setUuid(requestDocument.getUuid());
            responseDocument.setStatus("failure'");
            responseDocument.setStatusMessage("Instance is bound with more than one bib. So deletion cannot be done");
            return responseDocument;
        }
        boolean exists = checkInstancesOrItemsExistsInOLE(requestDocument.getUuid(), session);
        if (exists) {
            responseDocument.setId(requestDocument.getId());
            responseDocument.setCategory(requestDocument.getCategory());
            responseDocument.setType(requestDocument.getType());
            responseDocument.setFormat(requestDocument.getFormat());
            responseDocument.setUuid(requestDocument.getUuid());
            responseDocument.setStatus("failure'");
            responseDocument.setStatusMessage("Instances or Items in use. So deletion cannot be done");
            return responseDocument;
        }
/*        String instanceIdentifier = "";
        String bibIdentifierValue=bibIds[0]; // getting the first value from the bibIds array expecting that it has only one bib identifier otherwise it is bound with.
        Node bibUUID = session.getNodeByIdentifier(bibIdentifierValue);
            instanceIdentifier = bibUUID.getProperty("instanceIdentifier").getString();
            String[] instanceIds = instanceIdentifier.split(",");
            for (String instanceId : instanceIds) {
                instanceIdentifierList.add(instanceId);
            }*/
      /* if(instanceIdentifierList.size()==1){
                responseDocument.setUuid(bibIdentifierValue);
                responseDocument.setCategory(DocCategory.WORK.getCode());
                responseDocument.setType(DocType.BIB.getDescription());
                responseDocument.setFormat(DocFormat.MARC.getCode());
                responseDocument.setStatus("success");
                responseDocument.setStatusMessage("success");
               // responseDocument = RdbmsWorkBibDocumentManager.getInstance().deleteVerify(requestDocument, session);
            }
        else{*/
        responseDocument.setUuid(requestDocument.getUuid());
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setStatus("success");
        responseDocument.setStatusMessage("success");
        // }
        return responseDocument;
    }

    @Override
    public ResponseDocument delete(RequestDocument requestDocument, Object object) throws Exception {
        Session session = (Session) object;
        DocumentManager documentManager = null;
        ResponseDocument responseDocument = new ResponseDocument();
        ResponseDocument responseDocumentFromDeleteVerify = deleteVerify(requestDocument, session);

        String status = responseDocumentFromDeleteVerify.getStatus();
        if (status.equalsIgnoreCase("success")) {
            if (responseDocumentFromDeleteVerify.getCategory().equalsIgnoreCase("work")
                    && responseDocumentFromDeleteVerify.getFormat().equalsIgnoreCase("marc") && responseDocumentFromDeleteVerify.getType()
                    .equalsIgnoreCase(
                            "bibliographic")) {
                RequestDocument requestDocumentForBib = prepareRequestDocument(responseDocumentFromDeleteVerify);
                documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocumentForBib.getCategory(), requestDocumentForBib.getType(), requestDocumentForBib.getFormat());
                responseDocument = documentManager.delete(requestDocumentForBib, session);
            } else if (responseDocumentFromDeleteVerify.getCategory().equalsIgnoreCase("work")
                    && responseDocumentFromDeleteVerify.getFormat().equalsIgnoreCase("oleml") && responseDocumentFromDeleteVerify.getType()
                    .equalsIgnoreCase(
                            "instance")) {
                //                   documentManager = BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);
                //                   responseDocument= documentManager.delete(requestDocument, session);
                List<String> instanceIdentifierList = new ArrayList<String>();
                instanceIdentifierList.add(requestDocument.getUuid());
                deLinkInstanceFromBib(requestDocument.getUuid(), session);
                deleteFromRepository(instanceIdentifierList, session);
            }
        }

        return responseDocument;
    }

    private void deLinkInstanceFromBib(String instanceIdentifier, Session session) throws Exception {
        List<String> instanceIdentifierList = new ArrayList<String>();
        Node instanceNode = session.getNodeByIdentifier(instanceIdentifier);
        String bibIdentifier = instanceNode.getProperty("bibIdentifier").getString();
        Node bibNode = session.getNodeByIdentifier(bibIdentifier);
        String instanceIdentifiers = bibNode.getProperty("instanceIdentifier").getString();
        String[] instanceIds = instanceIdentifiers.split(",");
        StringBuffer instaceIdentifiersStringBuffer = new StringBuffer();
        if (instanceIds.length > 1) {
            for (String instanceId : instanceIds) {
                if (!instanceId.equalsIgnoreCase(instanceIdentifier)) {
                    instaceIdentifiersStringBuffer.append(instanceId);
                    instaceIdentifiersStringBuffer.append(",");
                }
            }
        }
        String modifierInstanceIdentifiers = instaceIdentifiersStringBuffer.toString();
        // String modified= modifierInstanceIdentifiers.substring(0, modifierInstanceIdentifiers.length() - 1);
        //  bibNode.setProperty("instanceIdentifier",modified);
        //str.substring(0,str.length()-1)
        //modifierInstanceIdentifiers.

    }

    /**
     * This method builds the response document after the bound-with process.
     *
     * @param requestDocument
     * @return
     */

    private ResponseDocument buildResponseForBind(RequestDocument requestDocument) {
        ResponseDocument responseDocument = new ResponseDocument();
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(requestDocument.getUuid());
        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
        List<ResponseDocument> linkedResponseDocumentsList = new ArrayList<ResponseDocument>();
        for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {

            ResponseDocument linkedResponseDocument = new ResponseDocument();
            linkedResponseDocument.setCategory(linkedRequestDocument.getCategory());
            linkedResponseDocument.setType(linkedRequestDocument.getType());
            linkedResponseDocument.setFormat(linkedRequestDocument.getFormat());
            linkedResponseDocument.setId(linkedRequestDocument.getId());
            linkedRequestDocument.setUser(linkedRequestDocument.getUuid());
            linkedResponseDocumentsList.add(linkedResponseDocument);

        }
        responseDocument.setLinkedDocuments(linkedResponseDocumentsList);


        return responseDocument;
    }

    /**
     * @param requestDocument
     * @param session
     * @param operation
     * @throws RepositoryException
     * @throws OleDocStoreException
     * @throws OleException
     */
    private void updatePropertiesOfInstanceNode(RequestDocument requestDocument, Session session, String operation) throws RepositoryException, OleDocStoreException, OleException {
        if (operation.equalsIgnoreCase("bind")) {
            LOG.debug("requestDocument.getUuid()-->" + requestDocument.getUuid());
            Node nodeByUUID = session.getNodeByIdentifier(requestDocument.getUuid());
            StringBuilder bibIdentifierList = new StringBuilder();
            LOG.info("bib id-->" + nodeByUUID.getProperty("bibIdentifier").getString());
            String bibIdentifier = nodeByUUID.getProperty("bibIdentifier").getString();
            bibIdentifierList.append(bibIdentifier);
            List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
            for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {

                bibIdentifierList.append(",");
                bibIdentifierList.append(linkedRequestDocument.getUuid());
            }
            LOG.info("bibIdentifierList-->" + bibIdentifierList.toString());
            nodeByUUID.setProperty("bibIdentifier", bibIdentifierList.toString());

            //session.save();
        } else if (operation.equalsIgnoreCase("unbind")) {
            Node nodeByUUID = session.getNodeByIdentifier(requestDocument.getUuid());
            List<String> bibIdentifierList = new ArrayList<String>();
            String bibIdentifier = nodeByUUID.getProperty("bibIdentifier").getString();
            String[] bibIdentifierSplitter = bibIdentifier.split(",");
            for (String bibId : bibIdentifierSplitter) {
                bibIdentifierList.add(bibId);

            }
            List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
            for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
                if (bibIdentifierList.contains(linkedRequestDocument.getUuid())) {
                    bibIdentifierList.remove(linkedRequestDocument.getUuid());
                }

            }
            LOG.info("bibIdentifierList after removing the bibids-->" + bibIdentifierList.toString());
            nodeByUUID.setProperty("bibIdentifier", bibIdentifierList.toString());


        }

    }

    /**
     * @param requestDocument
     * @param session
     * @throws OleDocStoreException
     */
    private void getContent(RequestDocument requestDocument, Session session) throws OleDocStoreException {
        DocumentManager documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
        ResponseDocument responseDocument = documentManager.checkout(requestDocument, session);
        requestDocument.setContent(responseDocument.getContent());
        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();

        for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
            DocumentManager documentManagerForLink = BeanLocator.getDocstoreFactory().getDocumentManager(linkedRequestDocument.getCategory(), linkedRequestDocument.getType(), linkedRequestDocument.getFormat());
            linkedRequestDocument.setContent(documentManagerForLink.checkout(linkedRequestDocument, session).getContent());

        }
    }

    /**
     * @param requestDocument
     * @param object
     * @param operation
     * @throws RepositoryException
     * @throws OleException
     */
    private void updatePropertiesOfBibNodes(RequestDocument requestDocument, Object object, String operation) throws RepositoryException, OleException {
        Session session = (Session) object;
        List<RequestDocument> linkedReqDocs = requestDocument.getLinkedRequestDocuments();
        for (RequestDocument linkedReqDoc : linkedReqDocs) {
            if (operation.equalsIgnoreCase("bind")) {

                Node nodeByUUID = null;
                StringBuilder instanceIdentifierList = new StringBuilder();
                nodeByUUID = session.getNodeByIdentifier(linkedReqDoc.getUuid());
                String instanceIdentifier = nodeByUUID.getProperty("instanceIdentifier").getString();
                LOG.debug("instanceIdentifier id-->" + instanceIdentifier);
                instanceIdentifierList.append(instanceIdentifier);
                instanceIdentifierList.append(",");
                instanceIdentifierList.append(requestDocument.getUuid());
                LOG.info("instanceIdentifierList-->" + instanceIdentifierList.toString());
                nodeByUUID.setProperty("instanceIdentifier", instanceIdentifierList.toString());
            } else if (operation.equalsIgnoreCase("unbind")) {
                Node nodeByUUID = null;
                List<String> instanceIdentifierList = new ArrayList<String>();
                nodeByUUID = session.getNodeByIdentifier(linkedReqDoc.getUuid());
                String instanceIdentifier = nodeByUUID.getProperty("instanceIdentifier").getString();
                LOG.debug("instanceIdentifier id-->" + instanceIdentifier);
                String[] instanceIdentifierSplitter = instanceIdentifier.split(",");
                for (String instanceId : instanceIdentifierSplitter) {
                    instanceIdentifierList.add(instanceId);
                }
                if (instanceIdentifierList.contains(requestDocument.getUuid())) {
                    instanceIdentifierList.remove(requestDocument.getUuid());
                }

                LOG.info("instanceIdentifierList after remove-->" + instanceIdentifierList.toString());
                nodeByUUID.setProperty("instanceIdentifier", instanceIdentifierList.toString());

            }
        }
    }

    /**
     * @param requestDocument
     * @param session
     * @throws RepositoryException
     * @throws OleException
     * @throws FileNotFoundException
     * @throws OleDocStoreException
     */
    private void updateContentXmlOfInstance(RequestDocument requestDocument, Session session) throws RepositoryException, OleException, FileNotFoundException, OleDocStoreException {
        Node nodeByUUID = session.getNodeByIdentifier(requestDocument.getUuid());
        String content = checkOutContent(nodeByUUID, DocType.INSTANCE.getCode(), "chuntley");
        nodeByUUID.setProperty("jcr:data", content);
        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
        for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
            Node linkNodeByUUID = session.getNodeByIdentifier(linkedRequestDocument.getUuid());
            LOG.info("link getIdentifier-->" + linkNodeByUUID.getIdentifier());
            String linkContent = checkOutContent(linkNodeByUUID, DocType.BIB.getDescription(), "chuntley");
            linkNodeByUUID.getNode("jcr:content").setProperty("jcr:data", linkContent);
        }
    }

    @Override
    protected String checkOutContent(Node nodeByUUID, String format, String user)
            throws RepositoryException, OleDocStoreException, FileNotFoundException {
        String content = null;
        if (nodeByUUID.getName().equalsIgnoreCase(ProcessParameters.NODE_INSTANCE)) {
            content = nodeManager.getInstanceData(nodeByUUID);
        } else {
            content = nodeManager.getData(nodeByUUID);
        }
        return content;
    }


    /**
     * Method to get Parsed Holdings & Item Documents.
     *
     * @param instanceDoc  - Instance document in format OleML
     * @param linkedBibIds TODO
     * @return
     */
    public List<RequestDocument> getParsedHoldingsNItemDocuments(RequestDocument instanceDoc,
                                                                 List<String> linkedBibIds) throws OleDocStoreException {
        List<RequestDocument> parsedItemNHoldingsDocuments = new ArrayList<RequestDocument>();
        if (instanceDoc != null && DocCategory.WORK.isEqualTo(instanceDoc.getCategory()) && DocType.INSTANCE.isEqualTo(
                instanceDoc.getType()) && DocFormat.OLEML.isEqualTo(instanceDoc.getFormat())) {
            String docContent = instanceDoc.getContent().getContent();
            InstanceCollection instanceCollection = olemlProcessor.fromXML(docContent);
            instanceDoc.getContent().setContentObject(instanceCollection);
            // XML conversion
            if (instanceCollection.getInstance() != null && instanceCollection.getInstance().size() > 0) {
                Instance instance = instanceCollection.getInstance().get(0);
                resolveLinkingWithBib(instance);

                if (instance.getResourceIdentifier().size() == 1) {
                    if (instance.getResourceIdentifier().get(0) == null || ""
                            .equals(instance.getResourceIdentifier().get(0))) {
                        instance.getResourceIdentifier().remove(0);
                    }
                }
                if (linkedBibIds != null && linkedBibIds.size() > 0) {
                    for (String likedBibId : linkedBibIds) {
                        instance.getResourceIdentifier().add(likedBibId);
                    }
                }
                parsedItemNHoldingsDocuments.add(generateInstanceDocument(instance));

                OleHoldings oleHolding = instance.getOleHoldings();
                processCallNumber(oleHolding);
                RequestDocument rdHol = (RequestDocument) instanceDoc.clone();
                Content content = new Content();
                content.setContent(olemlProcessor.toXML(oleHolding));
                content.setContentObject(oleHolding);
                rdHol.setContent(content);
                if (oleHolding != null && oleHolding.getExtension() != null) {
                    rdHol.setAdditionalAttributes(getFirstAdditionalAttributes(oleHolding.getExtension()));
                }
                parsedItemNHoldingsDocuments.add(rdHol);

                SourceHoldings sourceHoldings = instance.getSourceHoldings();
                RequestDocument rdSrcHol = (RequestDocument) instanceDoc.clone();
                Content sourceHolContent = new Content();
                sourceHolContent.setContent(olemlProcessor.toXML(sourceHoldings));
                sourceHolContent.setContentObject(sourceHoldings);
                rdSrcHol.setContent(sourceHolContent);
                if (sourceHoldings != null && sourceHoldings.getExtension() != null) {
                    rdSrcHol.setAdditionalAttributes(getFirstAdditionalAttributes(sourceHoldings.getExtension()));
                }
                parsedItemNHoldingsDocuments.add(rdSrcHol);

                if (instance.getItems() == null) {
                    instance.setItems(new Items());
                }
                if (instance.getItems().getItem() == null) {
                    instance.getItems().getItem().add(new Item());
                }
                if (instance.getItems().getItem() != null && instance.getItems().getItem().size() == 0) {
                    instance.getItems().getItem().add(new Item());
                }
                for (Item oleItem : instance.getItems().getItem()) {
                    RequestDocument rdItm = (RequestDocument) instanceDoc.clone();
                    if (oleItem.getCallNumber() != null) {
                        computeCallNumberType(oleItem.getCallNumber());
                    }
                    updateShelvingOrder(oleItem, oleHolding);
                    Content itemContent = new Content();
                    itemContent.setContent(olemlProcessor.toXML(oleItem));
                    itemContent.setContentObject(oleItem);
                    rdItm.setContent(itemContent);
                    if (oleItem != null && oleItem.getExtension() != null) {
                        rdItm.setAdditionalAttributes(getFirstAdditionalAttributes(oleItem.getExtension()));
                    }
                    parsedItemNHoldingsDocuments.add(rdItm);
                }
            }
        }
        return parsedItemNHoldingsDocuments;
    }

    protected void processCallNumber(OleHoldings oleHolding) throws OleDocStoreException {
        if (oleHolding != null && oleHolding.getCallNumber() != null) {
            //validateCallNumber(oleHolding.getCallNumber());
            CallNumber cNum = oleHolding.getCallNumber();
            computeCallNumberType(cNum);
            if (cNum.getNumber() != null && cNum.getNumber().trim().length() > 0) {
                //Build sortable key if a valid call number exists
                boolean isValid = validateCallNumber(cNum.getNumber(), cNum.getShelvingScheme().getCodeValue());
                String value = "";
                if (isValid) {
                    value = buildSortableCallNumber(cNum.getNumber(), cNum.getShelvingScheme().getCodeValue());
                } else {
                    value = cNum.getNumber();
                }
                if (cNum.getShelvingOrder() == null) {
                    cNum.setShelvingOrder(new ShelvingOrder());
                }
                cNum.getShelvingOrder().setFullValue(value);
            }
        }
    }

    public void validateCallNumber(CallNumber cNum) throws OleDocStoreException {
        validateCNumNCNumType(cNum);
        validateShelvingOrderNCNum(cNum);
    }

    private void validateShelvingOrderNCNum(CallNumber cNum) throws OleDocStoreException {
        if (cNum.getShelvingOrder() != null && cNum.getShelvingOrder().getFullValue() != null &&
                cNum.getShelvingOrder().getFullValue().trim().length() > 0) {
            if (!(cNum.getNumber() != null && cNum.getNumber().length() > 0)) {
                throw new OleDocStoreException("Shelving order value is available, so please enter call number information");
            }
        }
    }


    /**
     * Verifies that callNumberType is valid when callNumber is present.
     * Else throws exception.
     */
    private void validateCNumNCNumType(CallNumber cNum) throws OleDocStoreException {
        String callNumber = "";
        String callNumberType = "";
        // Get callNumber and callNumberType
        if (cNum != null) {
            callNumber = cNum.getNumber();
            if (cNum.getShelvingScheme() != null) {
                callNumberType = cNum.getShelvingScheme().getCodeValue();
            }
        }
        // Check if CallNumber is present
        if (StringUtils.isNotEmpty(callNumber)) {
            // Check if callNumberType is empty or #
            if ((callNumberType == null) || (callNumberType.length() == 0) || (callNumberType.equals("#"))) {
                throw new OleDocStoreException("Please enter valid call number type value in call number information ");
            }
        }
    }

    public void validateCallNumber(CallNumber itemCNum, OleHoldings holdings) throws OleDocStoreException {
        // item call number and type verification
        if ((itemCNum.getNumber() != null && itemCNum.getNumber().length() > 0)) {
            validateCNumNCNumType(itemCNum);
            validateShelvingOrderNCNum(itemCNum);
        }
        // if item call number is null consider holdings call number
        else if (holdings != null) {
            if (holdings.getCallNumber() != null) {
                CallNumber holCNum = holdings.getCallNumber();
                validateCNumNCNumType(holCNum);
                // consider item shelving order and holdings call number information.
                if (itemCNum.getShelvingOrder() != null && itemCNum.getShelvingOrder().getFullValue() != null &&
                        itemCNum.getShelvingOrder().getFullValue().trim().length() > 0) {
                    if (!(holCNum.getNumber() != null && holCNum.getNumber().length() > 0)) {
                        throw new OleDocStoreException("Shelving order value is available, Please enter call number information");
                    }
                }
            }
            // item shelving order is not null and holdings call number is null
            else if (itemCNum.getShelvingOrder() != null && itemCNum.getShelvingOrder().getFullValue() != null &&
                    itemCNum.getShelvingOrder().getFullValue().trim().length() > 0) {
                throw new OleDocStoreException("Shelving order value is available, Please enter call number information");
            }

        }
    }

    public void updateShelvingOrder(Item item, OleHoldings oleHolding) throws OleDocStoreException {
        String callNumber = null;
        String shelvingScheme = null;
        if (item != null) {
            if (item.getCallNumber() == null) {
                item.setCallNumber(new CallNumber());
            }
            // validating item if call number is available, shelving scheme should be available
            // validateCallNumber(item.getCallNumber(), null);
            //if call number is null or empty
            if (!(item.getCallNumber().getNumber() != null && item.getCallNumber().getNumber().trim().length() > 0)) {
                // if holding call number and shelving scheme is not empty
                if (oleHolding != null && oleHolding.getCallNumber() != null && oleHolding.getCallNumber().getNumber() != null &&
                        oleHolding.getCallNumber().getShelvingScheme() != null && oleHolding.getCallNumber().getShelvingScheme().getCodeValue() != null) {
                    callNumber = oleHolding.getCallNumber().getNumber();
                    shelvingScheme = oleHolding.getCallNumber().getShelvingScheme().getCodeValue();
                }
            } else {
                // call number is not empty
                //TODO strip off item info from call number
                callNumber = item.getCallNumber().getNumber();
                //                item.getCallNumber().setNumber(appendItemInfoToCalNumber(item, callNumber));
                if (item.getCallNumber().getShelvingScheme() != null) {
                    shelvingScheme = item.getCallNumber().getShelvingScheme().getCodeValue();
                }
            }
            String shelvingOrd = "";
            if (callNumber != null && callNumber.trim().length() > 0 && shelvingScheme != null && shelvingScheme.trim().length() > 0) {
                callNumber = appendItemInfoToCalNumber(item, callNumber);
                //Build sortable key if a valid call number exists
                boolean isValid = validateCallNumber(callNumber, shelvingScheme);
                if (isValid) {
                    shelvingOrd = buildSortableCallNumber(callNumber, shelvingScheme);
                } else {
                    shelvingOrd = callNumber;
                }
                if (item.getCallNumber().getShelvingOrder() == null) {
                    item.getCallNumber().setShelvingOrder(new ShelvingOrder());
                }
                item.getCallNumber().getShelvingOrder().setFullValue(shelvingOrd);
            }
        }
    }

    private String appendItemInfoToCalNumber(Item item, String callNumber) {
        if (item.getEnumeration() != null && item.getEnumeration().trim().length() > 0) {
            callNumber = callNumber + " " + item.getEnumeration().trim();
        }
        if (item.getChronology() != null && item.getChronology().trim().length() > 0) {
            callNumber = callNumber + " " + item.getChronology().trim();

        }
        if (item.getCopyNumber() != null && item.getCopyNumber().trim().length() > 0) {
            callNumber = callNumber + " " + item.getCopyNumber().trim();
        }
        return callNumber;
    }

    protected boolean validateCallNumber(String callNumber, String codeValue) throws OleDocStoreException {
        boolean isValid = false;
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.kuali.ole.utility.callnumber.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                isValid = callNumberObj.isValid(callNumber);
            }
        }
        return isValid;
    }

    protected String buildSortableCallNumber(String callNumber, String codeValue) throws OleDocStoreException {
        String shelvingOrder = "";
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.kuali.ole.utility.callnumber.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                shelvingOrder = callNumberObj.getSortableKey(callNumber);
                //shelvingOrder = shelvingOrder.replaceAll(" ", "_");
            }
        }
        return shelvingOrder;
    }

    private RequestDocument generateInstanceDocument(Instance instance) {
        InstanceCollection instanceCollection = new InstanceCollection();
        List<Instance> instances = new ArrayList<Instance>();
        instanceCollection.setInstance(instances);
        Instance inst = new Instance();
        instances.add(inst);
        inst.setInstanceIdentifier(instance.getInstanceIdentifier());
        inst.setResourceIdentifier(instance.getResourceIdentifier());
        inst.setFormerResourceIdentifier(instance.getFormerResourceIdentifier());
        inst.setExtension(instance.getExtension());
        String cont = olemlProcessor.toXML(instanceCollection);
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setContent(new Content());
        requestDocument.getContent().setContent(cont);
        requestDocument.getContent().setContentObject(inst);
        return requestDocument;
    }

    private void resolveLinkingWithBib(Instance instance) {
        if (ProcessParameters.BULK_INGEST_IS_LINKING_ENABLED) {
            //            instance.getResourceIdentifier().clear();
            for (FormerIdentifier frids : instance.getFormerResourceIdentifier()) {
                Identifier identifier = frids.getIdentifier();
                try {
                    if (identifier.getIdentifierValue() != null
                            && identifier.getIdentifierValue().trim().length() != 0) {
                        List<SolrDocument> solrDocs = ServiceLocator.getIndexerService()
                                .getSolrDocument("SystemControlNumber",
                                        "\"" + identifier
                                                .getIdentifierValue()
                                                + "\"");
                        if (solrDocs != null && solrDocs.size() > 0) {
                            for (SolrDocument solrDoc : solrDocs) {
                                if (checkApplicability(identifier.getIdentifierValue(),
                                        solrDoc.getFieldValue("SystemControlNumber"))) {
                                    instance.getResourceIdentifier().add(solrDoc.getFieldValue("id").toString());
                                }
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private boolean checkApplicability(Object value, Object fieldValue) {
        if (fieldValue instanceof Collection) {
            for (Object object : (Collection) fieldValue) {
                if (object.equals(value)) {
                    return true;
                }
            }
            return false;
        } else {
            return value.equals(fieldValue);
        }
    }

    /**
     * Method to get Additional Attributes
     *
     * @param extension
     * @return
     */
    private AdditionalAttributes getFirstAdditionalAttributes(Extension extension) {
        if (extension != null && extension.getContent() != null) {
            for (Object obj : extension.getContent()) {
                if (obj instanceof AdditionalAttributes) {
                    return (AdditionalAttributes) obj;
                }
            }
        }
        return null;
    }

    private void setIdentifierValueInContent(RequestDocument reqDoc) {
        if (reqDoc.getType().equalsIgnoreCase(DocType.ITEM.getDescription())) {
            ItemOlemlRecordProcessor recordProcessor = new ItemOlemlRecordProcessor();
            Item item = recordProcessor.fromXML(reqDoc.getContent().getContent());
            item.setItemIdentifier(reqDoc.getId());
            reqDoc.getContent().setContent(recordProcessor.toXML(item));
        }
        if (reqDoc.getType().equalsIgnoreCase(DocType.HOLDINGS.getDescription())) {
            HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
            OleHoldings holdings = recordProcessor.fromXML(reqDoc.getContent().getContent());
            holdings.setHoldingsIdentifier(reqDoc.getId());
            reqDoc.getContent().setContent(recordProcessor.toXML(holdings));
        }
        if (reqDoc.getType().equalsIgnoreCase(DocType.SOURCEHOLDINGS.getDescription())) {
            SourceHoldingOlemlRecordProcessor recordProcessor = new SourceHoldingOlemlRecordProcessor();
            SourceHoldings sourceHoldings = recordProcessor.fromXML(reqDoc.getContent().getContent());
            sourceHoldings.setHoldingsIdentifier(reqDoc.getId());
            reqDoc.getContent().setContent(recordProcessor.toXML(sourceHoldings));
        }
    }

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument) {
        ResponseDocument responseDocument = new ResponseDocument();
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(requestDocument.getUuid());
        if ((requestDocument.getLinkedRequestDocuments() != null) && (requestDocument.getLinkedRequestDocuments().size()
                > 0) || requestDocument.getType()
                .equals(DocType.INSTANCE
                        .getCode())) {
            buildInstanceComponentResponseDocuments(requestDocument, responseDocument);
        }
        buildLinkedResponseDocuments(requestDocument, responseDocument);    // in the case of adding an item
        return responseDocument;
    }

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument, Session session) {
        ResponseDocument responseDocument = new ResponseDocument();
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(requestDocument.getUuid());
        String category = requestDocument.getCategory();
        String type = requestDocument.getType();
        Node node = null;
        try {
            node = session.getNodeByIdentifier(requestDocument.getUuid());
        } catch (RepositoryException e) {
            LOG.info("Failed to get node:" + e.getMessage(), e);
        }

        if (node != null) {
            try {
                AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
                if (additionalAttributes != null && category.equals(DocCategory.WORK.getDescription()) && type
                        .equals(DocType.BIB.getDescription())) {
                    Collection<String> attributeNames = additionalAttributes.getAttributeNames();
                    if (attributeNames != null && attributeNames.size() > 0) {
                        for (Iterator<String> iterator = attributeNames.iterator(); iterator.hasNext(); ) {
                            String attributeName = iterator.next();
                            if (node.hasProperty(attributeName)) {
                                additionalAttributes
                                        .setAttribute(attributeName, node.getProperty(attributeName).getString());
                            }
                        }
                    }
                    responseDocument.setAdditionalAttributes(additionalAttributes);
                }
            } catch (RepositoryException e) {
                LOG.info("Failed to get node property:" + e.getMessage(), e);
            }
        }

        buildLinkedResponseDocuments(requestDocument, responseDocument);
        return responseDocument;
    }

    private void buildInstanceComponentResponseDocuments(RequestDocument requestDocument,
                                                         ResponseDocument responseDocument) {
        if (requestDocument.getContent().getContent() == null) {    // in the case of adding an item
            return;
        }
        InstanceCollection instanceCollection = (InstanceCollection) requestDocument.getContent().getContentObject();
        ResponseDocument linkedInstanceDocument = null;
        ResponseDocument linkedInstanceItemDocument = null;
        ResponseDocument linkedInstanceSrHoldingDoc = null;
        requestDocument.getContent().setContent("");
        List<ResponseDocument> linkInstanceDocs = new ArrayList<ResponseDocument>();
        for (Instance oleInstance : instanceCollection.getInstance()) {
            // holding from instance
            linkedInstanceDocument = new ResponseDocument();
            setResponseParameters(linkedInstanceDocument, requestDocument);
            linkedInstanceDocument.setUuid(oleInstance.getOleHoldings().getHoldingsIdentifier());
            linkedInstanceDocument.setType("holdings");
            linkInstanceDocs.add(linkedInstanceDocument);

            //SourceHolding from Instance
            linkedInstanceSrHoldingDoc = new ResponseDocument();
            setResponseParameters(linkedInstanceSrHoldingDoc, requestDocument);
            if (oleInstance.getSourceHoldings() != null
                    && oleInstance.getSourceHoldings().getHoldingsIdentifier() != null) {
                linkedInstanceSrHoldingDoc.setUuid(oleInstance.getSourceHoldings().getHoldingsIdentifier());
                linkedInstanceSrHoldingDoc.setType("sourceHoldings");
                linkInstanceDocs.add(linkedInstanceSrHoldingDoc);
            }

            // item from instance
            for (Iterator<Item> itemIterator = oleInstance.getItems().getItem().iterator(); itemIterator.hasNext(); ) {
                Item oleItem = itemIterator.next();
                linkedInstanceItemDocument = new ResponseDocument();
                setResponseParameters(linkedInstanceItemDocument, requestDocument);
                linkedInstanceItemDocument.setUuid(oleItem.getItemIdentifier());
                linkedInstanceItemDocument.setType("item");
                linkInstanceDocs.add(linkedInstanceItemDocument);
            }
        }
        responseDocument.setLinkedDocuments(linkInstanceDocs);
    }

    /**
     * Used in the case of adding an item.
     *
     * @param requestDocument
     * @param responseDocument
     */
    protected void buildLinkedResponseDocuments(RequestDocument requestDocument, ResponseDocument responseDocument) {
        if ((null == requestDocument.getLinkedRequestDocuments()) || (requestDocument.getLinkedRequestDocuments().size()
                == 0)) {
            buildInstanceComponentResponseDocuments(requestDocument, responseDocument);
            return;
        }
        for (RequestDocument linkedItemDocument : requestDocument.getLinkedRequestDocuments()) {
            if (DocType.ITEM.getDescription().equalsIgnoreCase(linkedItemDocument.getType())
                    && linkedItemDocument.getContent().getContent() != null) {
                ResponseDocument linkedItemResponseDocument = new ResponseDocument();
                setResponseParameters(linkedItemResponseDocument, requestDocument);
                linkedItemResponseDocument.setUuid(linkedItemDocument.getUuid());
                linkedItemResponseDocument.setType("item");
                linkedItemResponseDocument.setContent(new Content(""));
                responseDocument.getLinkedDocuments().add(linkedItemResponseDocument);
            }
        }
    }

    public void transferItems(List<RequestDocument> requestDocuments, Session session) throws Exception {
        LOG.debug("in JcrWorkInstanceDocumentManager transferItems");
        List<String> itemIdentifierList = new ArrayList<String>();
        String destInstanceIdentifier = requestDocuments.get(requestDocuments.size() - 1).getUuid();
        String destPath = "";
        for (int i = 0; i < requestDocuments.size() - 1; i++) {
            itemIdentifierList.add(requestDocuments.get(i).getUuid());
        }
        LOG.debug("JcrWorkInstanceDocumentManager transferItems itemIdentifierList " + itemIdentifierList);
        // Below line is commented to disable item exists check in ole as per jira 4130.
        //boolean isExists=checkInstancesOrItemsExistsInOLE(itemIdentifierList);
        boolean isExists = false;
        LOG.debug("isExists " + isExists);
        if (isExists) {
            LOG.debug("in isExists");
            throw new ItemExistsException("One of the Items to be Transfered is Loaned or In use in OLE");
        }
        LOG.debug("after isExists");
        Node instanceNode = session.getNodeByIdentifier(destInstanceIdentifier);
        NodeIterator instanceNodeIterator = instanceNode.getNodes();

        while (instanceNodeIterator.hasNext()) {
            Node node = instanceNodeIterator.nextNode();
            if (node.getName().equalsIgnoreCase("holdingsNode")) {
                destPath = node.getPath() + "/itemFile";
                break;
            }
        }
        for (String itemIdentifier : itemIdentifierList) {
            String sourcePath = session.getNodeByIdentifier(itemIdentifier).getPath();
            session.move(sourcePath, destPath);
        }

    }

    public void transferInstances(List<RequestDocument> requestDocuments, Session session) throws Exception {
        List<String> instanceIdentifiersList = new ArrayList<String>();
        for (int i = 0; i < requestDocuments.size() - 1; i++) {
            instanceIdentifiersList.add(requestDocuments.get(i).getUuid());
        }
        WorkInstanceNodeManager workInstanceNodeManager = WorkInstanceNodeManager.getInstance();
        InstanceCollection instanceCollection = null;
        String desBibIdentifier = requestDocuments.get(requestDocuments.size() - 1).getUuid();
        for (String instanceIdentifier : instanceIdentifiersList) {
            Node instanceNode = nodeManager.getNodeByUUID(session, instanceIdentifier);
            String instanceXMLContent = workInstanceNodeManager.getXMLOnlyForInstanceType(instanceNode);
            instanceCollection = olemlProcessor.fromXML(instanceXMLContent);
            List<Instance> instanceList = instanceCollection.getInstance();
            List<String> instanceIdentifiers = new ArrayList<String>();
            instanceIdentifiers.add(desBibIdentifier);
            instanceList.get(0).setResourceIdentifier(instanceIdentifiers);
            String backToXML = olemlProcessor.toXML(instanceCollection);
            byte[] bytes = convertContentToBytes(backToXML);
            NodeIterator nodeIterator = instanceNode.getNodes();
            Node node = null;

            while (nodeIterator.hasNext()) {
                node = nodeIterator.nextNode();
                if (node.getName().equalsIgnoreCase("instanceFile")) {
                    updateContentToNode(new RequestDocument(), session, bytes, node);
                }
            }
        }
    }

    private byte[] convertContentToBytes(String xmlContent) throws OleDocStoreException {
        String charset = "UTF-8";
        byte[] documentBytes = null;
        try {
            if (xmlContent != null) {
                documentBytes = xmlContent.getBytes(charset);
            }
        } catch (Exception e) {
            //  logger.info("Failed to convert input string to byte[] with charset " + charset, e);
            throw new OleDocStoreException(e.getMessage());
        }
        return documentBytes;
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> fieldValues) throws OleDocStoreException {
        Session session = (Session) object;
        String content = requestDocument.getContent().getContent();
        if (content == null) {
            if (requestDocument.getLinkedRequestDocuments().size() > 0) {
                List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
                for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
                    if (linkedRequestDocument.getType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                        JcrWorkItemDocumentManager.getInstance().validateNewItem(linkedRequestDocument, session, fieldValues, requestDocument.getId());
                    }
                }
            }
        } else {
            InstanceOlemlRecordProcessor instProcessor = new InstanceOlemlRecordProcessor();
            InstanceCollection instanceCollection = instProcessor.fromXML(content);
            List<Instance> instanceList = instanceCollection.getInstance();
            for (Instance instance : instanceList) {
                if (instance.getOleHoldings() != null) {
                    OleHoldings oleHoldings = instance.getOleHoldings();
                    JcrJcrWorkHoldingsDocumentManager.getInstance().validateHoldings(oleHoldings);
                }
                if (instance.getItems() != null) {
                    Items items = instance.getItems();
                    List<Item> itemList = items.getItem();
                    if (itemList.size() > 0) {
                        for (Item item : itemList) {
                            JcrWorkItemDocumentManager.getInstance().itemBarcodeValidation(item, fieldValues, null);
                            if (item.getCallNumber() != null) {
                                CallNumber callNumber = item.getCallNumber();
                                if (instance.getOleHoldings() != null) {
                                    OleHoldings oleHoldings = instance.getOleHoldings();
                                    validateCallNumber(callNumber, oleHoldings);
                                } else {
                                    validateCallNumber(callNumber, null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Compute 'call number type name' if a valid 'call number type code' is available
     *
     * @param callNumber
     */
    public void computeCallNumberType(CallNumber callNumber) {
        Set<String> validCallNumberTypeSet = CallNumberType.validCallNumberTypeCodeSet;
        if (callNumber != null) {
            if (callNumber.getShelvingScheme() != null) {
                String callNumberTypeCode = callNumber.getShelvingScheme().getCodeValue();
                String callNumberTypeName = "";
                //If call number type code is valid
                if ((StringUtils.isNotEmpty(callNumberTypeCode)) && (validCallNumberTypeSet
                        .contains(callNumberTypeCode))) {
                    callNumberTypeName = CallNumberType.valueOf(callNumberTypeCode).getDescription();
                    callNumber.getShelvingScheme().setFullValue(callNumberTypeName);
                }
            }
        }
    }
}
