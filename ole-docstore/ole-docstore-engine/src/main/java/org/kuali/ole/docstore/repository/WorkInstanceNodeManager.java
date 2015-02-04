package org.kuali.ole.docstore.repository;

import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.document.jcr.JcrWorkInstanceDocumentManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.repository.NodeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.kuali.ole.docstore.process.ProcessParameters.*;

/**
 * User: tirumalesh.b
 * Date: 4/9/12 Time: 1:00 PM
 */
public class WorkInstanceNodeManager
        extends CustomNodeManager
        implements NodeManager {
    private static WorkInstanceNodeManager ourInstance = new WorkInstanceNodeManager();
    private static Logger LOG = LoggerFactory.getLogger(WorkInstanceNodeManager.class);

    public static WorkInstanceNodeManager getInstance() {
        return ourInstance;
    }

    private WorkInstanceNodeManager() {
        super();
        numLevels = 2;
    }

    public String getParentNodePath() {
        return "/work/instance";
    }

    @Override
    public Node createFileNode(RequestDocument document, String name, Node parentNode, Session session)
            throws OleDocStoreException {
        Node instanceNode = null;
        JcrWorkInstanceDocumentManager resolver = JcrWorkInstanceDocumentManager.getInstance();
        List<String> docUUIDs = new ArrayList<String>();
        List<RequestDocument> resolvedDocs = resolver.getParsedHoldingsNItemDocuments(document, docUUIDs);
        CustomNodeManager nodeManager = CustomNodeManager.getInstance();
        try {
            instanceNode = initLevelNode(NODE_INSTANCE, parentNode, false, session);
            Node holdingsNode = initNonStaticNode(NODE_HOLDINGS, instanceNode);
            document.setUuid(instanceNode.getIdentifier());

            Node fileNode = nodeManager.createFileNode(resolvedDocs.get(0), FILE_INSTANCE, instanceNode, session);
            resolver.modifyDocumentContent(resolvedDocs.get(0), fileNode.getIdentifier(), instanceNode.getIdentifier());
            createContentNode(fileNode, resolvedDocs.get(0), parentNode, session);

            fileNode = nodeManager.createFileNode(resolvedDocs.get(1), FILE_HOLDINGS, holdingsNode, session);
            resolver.modifyDocumentContent(resolvedDocs.get(1), fileNode.getIdentifier(), holdingsNode.getIdentifier());
            createContentNode(fileNode, resolvedDocs.get(1), parentNode, session);

            fileNode = nodeManager.createFileNode(resolvedDocs.get(2), FILE_SOURCE_HOLDINGS, holdingsNode, session);
            resolver.modifyDocumentContent(resolvedDocs.get(2), fileNode.getIdentifier(), holdingsNode.getIdentifier());
            createContentNode(fileNode, resolvedDocs.get(2), parentNode, session);


            for (int i = 3; i < resolvedDocs.size(); i++) {
                fileNode = nodeManager.createFileNode(resolvedDocs.get(i), FILE_ITEM, holdingsNode, session);
                resolver.modifyDocumentContent(resolvedDocs.get(i), fileNode.getIdentifier(),
                        holdingsNode.getIdentifier());
                createContentNode(fileNode, resolvedDocs.get(i), parentNode, session);
            }

            ((InstanceCollection) document.getContent().getContentObject()).getInstance().get(0).setInstanceIdentifier(
                    instanceNode.getIdentifier());
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        return instanceNode;
    }

    public void ingestItemRecForInstance(RequestDocument linkedItemDocument, String id, Session session) throws OleDocStoreException {
        try {

            Node nodeByUUID = getNodeByUUID(session, id);
            Node holdingsNode = nodeByUUID.getNode(ProcessParameters.NODE_HOLDINGS);
            NodeHandler nodeHandler = new NodeHandler();
            ItemOlemlRecordProcessor recordProcessor = new ItemOlemlRecordProcessor();
            Item item = recordProcessor.fromXML(linkedItemDocument.getContent().getContent());
            buildShelvingOrderForItem(item, holdingsNode);
            linkedItemDocument.getContent().setContentObject(item);
            String uuid = (nodeHandler.initFileNode(linkedItemDocument, FILE_ITEM, holdingsNode, session))
                    .getIdentifier();
            linkedItemDocument.setId(uuid);
        } catch (Exception e) {
            LOG.info(e.getMessage(),e);
            throw new OleDocStoreException(e.getMessage(), e);
        }
    }

    public Node getNodeByUUID(Session newSession, String uuid) throws OleDocStoreException {
        return CustomNodeManager.getInstance().getNodeByUUID(newSession, uuid);
    }

    public String getInstanceData(Node instanceNode)
            throws RepositoryException, OleDocStoreException, FileNotFoundException {
        String instance = "";
        String holdings = "";
        String sourceHolding = "";
        List<String> items = new ArrayList<String>();
        StringBuffer stringBuffer = new StringBuffer();
        NodeIterator nodeIterator = instanceNode.getNodes();
        Node node = null;
        while (nodeIterator.hasNext()) {
            node = nodeIterator.nextNode();
            if (node.getName().equalsIgnoreCase(ProcessParameters.FILE_INSTANCE)) {
                instance = getData(node);
            } else if (node.getName().equalsIgnoreCase(ProcessParameters.NODE_HOLDINGS)) {
                NodeIterator nodes = node.getNodes();
                while (nodes.hasNext()) {
                    Node node1 = nodes.nextNode();
                    if (node1.getName().equalsIgnoreCase(ProcessParameters.FILE_HOLDINGS)) {
                        holdings = getData(node1);
                    } else if (node1.getName().equalsIgnoreCase(ProcessParameters.FILE_SOURCE_HOLDINGS)) {
                        if (getData(node1) != null && getData(node1).length() > 0) {
                            sourceHolding = getData(node1);
                        }
                    } else if (node1.getName().equalsIgnoreCase(ProcessParameters.FILE_ITEM)) {
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

    public String getXMLOnlyForInstanceType(Node instanceNode)
            throws RepositoryException, OleDocStoreException, FileNotFoundException {
        NodeIterator nodeIterator = instanceNode.getNodes();
        Node node = null;
        String instance = "";
        while (nodeIterator.hasNext()) {
            node = nodeIterator.nextNode();
            if (node.getName().equalsIgnoreCase("instanceFile")) {
                instance = getData(node);
            }
        }
        return instance;
    }

    private void buildShelvingOrderForItem(Item item, Node holdingsNode) throws Exception {
        if (item != null) {
            if (item.getCallNumber() == null) {
                item.setCallNumber(new CallNumber());
            }
            JcrWorkInstanceDocumentManager instanceManagerJcr = JcrWorkInstanceDocumentManager.getInstance();
            Node holdingsFileNode = holdingsNode.getNode(FILE_HOLDINGS);
            String holdingXml = getData(holdingsFileNode);
            if (holdingXml != null) {
                HoldingOlemlRecordProcessor holdProcessor = new HoldingOlemlRecordProcessor();
                OleHoldings holdings = holdProcessor.fromXML(holdingXml);
                instanceManagerJcr.updateShelvingOrder(item, holdings);
            }
        }
    }

}
