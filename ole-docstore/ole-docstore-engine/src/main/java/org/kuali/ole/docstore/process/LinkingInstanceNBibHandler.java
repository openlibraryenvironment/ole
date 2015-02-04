package org.kuali.ole.docstore.process;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcDocBuilder;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.common.document.content.instance.FormerIdentifier;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.service.DocumentIngester;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.repository.CheckoutManager;
import org.kuali.ole.repository.NodeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/4/12
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkingInstanceNBibHandler
        implements Runnable {

    private static LinkingInstanceNBibHandler link = null;
    private boolean isRunning = false;
    private static final Logger logger = LoggerFactory.getLogger(RebuildIndexesHandler.class);

    private String docCategory;
    private String docType;
    private String docFormat;
    private CheckoutManager checkoutManager;

    private LinkingInstanceNBibHandler(String docCategory, String docType, String docFormat) {
        this.docCategory = docCategory;
        this.docType = docType;
        this.docFormat = docFormat;
        checkoutManager = new CheckoutManager();
    }

    public static LinkingInstanceNBibHandler getInstance(String docCategory, String docType, String docFormat) {
        if (link == null) {
            link = new LinkingInstanceNBibHandler(docCategory, docType, docFormat);
        }
        return link;
    }

    /**
     * Method to get running status.
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Method to startProcess
     */
    public void startProcess() {
        if (!isRunning) {
            Thread rebuilderThread = new Thread(this);
            rebuilderThread.start();
        }
    }

    public void run() {
        Session session = null;
        long totalCount = 0;
        isRunning = true;
        List<RequestDocument> docs = new ArrayList<RequestDocument>();
        List<SolrInputDocument> solrInputDocs = new ArrayList<SolrInputDocument>();
        logger.info("Rebuild Indexes Process(" + docCategory + " : " + docType + " : " + docFormat + "): START");
        try {

            session = RepositoryManager.getRepositoryManager().getSession(ProcessParameters.BULK_DEFAULT_USER,
                    ProcessParameters.BULK_DEFUALT_ACTION);
            RequestDocument rd = new RequestDocument();
            rd.setCategory(docCategory);
            rd.setType(docType);
            rd.setFormat(docFormat);
            DocumentIngester docIngester = new DocumentIngester();
            Node nodeFormat = docIngester.getStaticFormatNode(rd, session);
            NodeIterator nodesL1 = nodeFormat.getNodes();
            while (nodesL1.hasNext()) {
                Node nodeL1 = nodesL1.nextNode();
                NodeIterator nodesL2 = nodeL1.getNodes();
                while (nodesL2.hasNext()) {
                    Node nodeL2 = nodesL2.nextNode();

                    NodeIterator nodesFile = nodeL2.getNodes();
                    while (nodesFile.hasNext()) {
                        if (docs.size() == ProcessParameters.BULK_PROCESSOR_SPLIT_SIZE) {
                            indexDocs(solrInputDocs, totalCount);
                        } else {
                            Node fileNode = nodesFile.nextNode();
                            String instanceCon = null;
                            NodeIterator nodeIterator = null;
                            Node instanceNode = null;
                            try {
                                nodeIterator = fileNode.getNodes();
                                while (nodeIterator.hasNext()) {
                                    instanceNode = nodeIterator.nextNode();
                                    if (instanceNode.getName().equalsIgnoreCase("instanceFile")) {
                                        instanceCon = checkoutManager.getData(instanceNode);
                                    }
                                }
                            } catch (RepositoryException e) {
                                logger.error(e.getMessage(), e);
                            }
                            //                            String content = checkoutManager.checkOut(fileNode.getIdentifier(), "mockUser", "checkout");
                            InstanceOlemlRecordProcessor recProcessor = new InstanceOlemlRecordProcessor();
                            InstanceCollection instanceCollection = recProcessor.fromXML(instanceCon);
                            List<String> bibIdList = new ArrayList<String>();
                            if (instanceCollection.getInstance() != null
                                    && instanceCollection.getInstance().size() > 0) {
                                Instance instance = instanceCollection.getInstance().get(0);
                                resolveLinkingWithBib(instance, bibIdList, session, solrInputDocs);
                                if (instanceNode.getName().equalsIgnoreCase("instanceFile")) {
                                    byte[] documentBytes = recProcessor.toXML(instanceCollection).getBytes();
                                    Binary binary = null;
                                    if (documentBytes != null && instanceNode != null) {
                                        binary = session.getValueFactory()
                                                .createBinary(new ByteArrayInputStream(documentBytes));
                                        instanceNode.getNode("jcr:content").setProperty("jcr:data", binary);
                                    }
                                }
                            }
                            totalCount++;
                        }
                    }
                    if ((totalCount % 1000) == 0) {
                        session.save();
                        indexDocs(solrInputDocs, totalCount);
                    } else if ((totalCount % 1000000) == 0) {
                        ServiceLocator.getDiscoveryAdminService().optimize();
                    }
                }
            }
            if (solrInputDocs.size() > 0) {
                session.save();
                indexDocs(solrInputDocs, totalCount);
            }
        } catch (Exception e) {
            logger.error(
                    "Rebuild Indexes Process(" + docCategory + " : " + docType + " : " + docFormat + ") Processed(" + (
                            totalCount - docs.size()) + "), Failed @ batch(" + docs.size() + "): Cause: " + e, e);
        } finally {
            try {
                isRunning = false;
                RepositoryManager.getRepositoryManager().logout(session);
            } catch (OleException e) {
            }
        }
    }

    private void indexDocs(List<SolrInputDocument> solrInputDocs, long records) {

        try {
            ServiceLocator.getIndexerService().indexSolrDocuments(solrInputDocs);
            logger.info(
                    "Linking Bib and Instance Records (" + records + "), Time Taken for Batch(" + solrInputDocs.size()
                            + "): ");
            solrInputDocs.clear();
        } catch (Exception e) {
            logger.error("Linking Bib and Instance Records (" + (records - solrInputDocs.size()) + "), Failed @ batch("
                    + solrInputDocs.size() + "): Cause: " + e + "\n\tContinuous", e);
        }
    }


    private void resolveLinkingWithBib(Instance instance, List<String> bibIdList, Session session,
                                       List<SolrInputDocument> solrInputDocs) {
        instance.getResourceIdentifier().clear();

        for (FormerIdentifier frids : instance.getFormerResourceIdentifier()) {
            try {
                if (frids != null && frids.getIdentifier() != null &&
                        frids.getIdentifier().getIdentifierValue() != null &&
                        frids.getIdentifier().getIdentifierValue().trim().length() != 0) {
                    WorkBibMarcDocBuilder marcDocBuilder = new WorkBibMarcDocBuilder();
                    List<SolrDocument> solrBibDocs = ServiceLocator.getIndexerService()
                            .getSolrDocument("SystemControlNumber",
                                    frids.getIdentifier()
                                            .getIdentifierValue());
                    List<SolrDocument> solrInstanceDocs = ServiceLocator.getIndexerService().getSolrDocument("id",
                            instance.getInstanceIdentifier());
                    SolrInputDocument solrInputDocument = new SolrInputDocument();
                    if (solrBibDocs != null && solrBibDocs.size() > 0) {

                        for (SolrDocument solrbibDoc : solrBibDocs) {
                            String id = compareListRString(solrbibDoc.getFieldValue("id"));
                            instance.getResourceIdentifier().add(id);

                            logger.info("bib id " + id);
                            compareObjNAddValue(instance.getInstanceIdentifier(),
                                    solrbibDoc.getFieldValue("instanceIdentifier"), solrbibDoc,
                                    "instanceIdentifier");

                            solrInputDocument = new SolrInputDocument();
                            marcDocBuilder.buildSolrInputDocFromSolrDoc(solrbibDoc, solrInputDocument);
                            solrInputDocs.add(solrInputDocument);
                            Node bibNode = getNodeByUUID(session, id);
                            bibNode.setProperty("instanceIdentifier", instance.getInstanceIdentifier());
                            Node instanceNode = getNodeByUUID(session, instance.getInstanceIdentifier());
                            instanceNode.setProperty("bibIdentifier", id);

                            for (SolrDocument solrInstDoc : solrInstanceDocs) {
                                if (id != null) {
                                    compareObjNAddValue(id, solrInstDoc.getFieldValue("bibIdentifier"), solrInstDoc,
                                            "bibIdentifier");
                                }
                                solrInputDocument = new SolrInputDocument();
                                marcDocBuilder.buildSolrInputDocFromSolrDoc(solrInstDoc, solrInputDocument);
                                solrInputDocs.add(solrInputDocument);
                            }
                            logger.info("solr input  docs " + solrInputDocs);
                        }
                    }
                }
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
            }
        }
    }

    private String compareListRString(Object id) {
        if (id != null) {
            if (id instanceof List) {
                List<String> idList = (List<String>) id;
                return idList.get(0);
            } else if (id instanceof String) {
                String strId = (String) id;
                return strId;
            }
        }
        return null;
    }


    /* private void compareObjToObj(SolrDocument solrbibDoc, SolrDocument solrInstDoc) {

        if (solrbibDoc.getFieldValue("id") instanceof List) {
            List<String> bibIds = (List<String>) solrbibDoc.getFieldValue("id");
            compareListToObj(bibIds, solrInstDoc.getFirstValue("bibIdentifier"), solrInstDoc,"bibIdentifier" );
        }
        else if (solrbibDoc.getFieldValue("id") instanceof String) {
            String bibId = solrbibDoc.getFieldValue("id").toString();
            compareStringToObj(bibId, solrInstDoc.getFieldValue("bibIdentifier"), solrInstDoc, "bibIdentifier");
        }
    }*/


    private void compareObjNAddValue(String id, Object idObj, SolrDocument solrDoc, String identifier) {
        if (idObj != null) {
            if (idObj instanceof List) {
                List<String> instBibIdList = (List<String>) idObj;
                if (!instBibIdList.contains(id)) {
                    solrDoc.addField(identifier, id);
                }
            } else if (idObj instanceof String) {
                String instBibId = (String) idObj;
                if (!instBibId.equalsIgnoreCase(id)) {
                    solrDoc.addField(identifier, id);
                }
            }
        } else {
            solrDoc.addField(identifier, id);
        }
    }

    /* private void compareListToObj(List<String> idList, Object IdObj, SolrDocument solrDoc, String identifier) {

            for (String bibId : idList) {
                if (IdObj != null) {
                    if (IdObj instanceof List) {
                        List<String> instBibIdList = (List<String>) IdObj;
                        if (!instBibIdList.contains(bibId)) {
                            solrDoc.addField(identifier, bibId);
                        }
                    }
                    else if ((IdObj instanceof String)) {
                        String instBibId = (String) IdObj;
                        if (!instBibId.equalsIgnoreCase(bibId)) {
                            solrDoc.addField(identifier, bibId);
                        }
                    }
                }
                else {
                    solrDoc.addField(identifier, bibId);
                }
            }
        }
    */
    private Node getNodeByUUID(Session newSession, String uuid) throws OleException {
        return new NodeHandler().getNodeByUUID(newSession, uuid);
    }
}
