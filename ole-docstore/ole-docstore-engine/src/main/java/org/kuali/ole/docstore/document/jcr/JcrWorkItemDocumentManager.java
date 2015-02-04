package org.kuali.ole.docstore.document.jcr;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.service.QueryServiceImpl;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.service.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.kuali.ole.docstore.process.ProcessParameters.FILE_HOLDINGS;

/**
 * Created with IntelliJ IDEA.
 * User: SG7940
 * Date: 1/23/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class JcrWorkItemDocumentManager extends JcrWorkInstanceDocumentManager {

    private static JcrWorkItemDocumentManager ourInstance = null;
    private static Logger LOG = LoggerFactory.getLogger(JcrWorkItemDocumentManager.class);

    public static JcrWorkItemDocumentManager getInstance() {
        if (null == ourInstance) {
            ourInstance = new JcrWorkItemDocumentManager();
        }
        return ourInstance;
    }


    @Override
    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocument, Object object) {
        return null;
    }

    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        Session session = (Session) object;
        LOG.debug("workItemDocumentManager deleteVerify method");
        List<String> instanceIdentifierList = new ArrayList<String>();
        ResponseDocument responseDocument = new ResponseDocument();
        List<String> itemIdentifierList = new ArrayList<String>();
        itemIdentifierList.add(requestDocument.getUuid());
        boolean exists = checkInstancesOrItemsExistsInOLE(itemIdentifierList);
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
        Node itemNode = session.getNodeByIdentifier(requestDocument.getUuid());

        long itemSize = itemNode.getParent().getNodes().getSize() - 2;
        LOG.debug("JcrWorkItemDocumentManager : itemSize " + itemSize);
        String instanceIdentifier = itemNode.getParent().getParent().getIdentifier();
        LOG.debug("JcrWorkItemDocumentManager : instanceIdentifier " + instanceIdentifier);
/*         if(itemSize==1){
            requestDocument.setUuid(instanceIdentifier);
            requestDocument.setCategory(DocCategory.WORK.getCode());
            requestDocument.setType(DocType.INSTANCE.getCode());
            requestDocument.setFormat(DocFormat.OLEML.getCode());
            responseDocument = JcrWorkInstanceDocumentManager.getInstance().deleteVerify(requestDocument, session);
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
        LOG.debug("in JcrWorkItemDocumentManager :delete");
        DocumentManager documentManager = null;
        ResponseDocument responseDocument = new ResponseDocument();
        ResponseDocument responseDocumentFromDeleteVerify = deleteVerify(requestDocument, session);
        LOG.debug("in JcrWorkItemDocumentManager :delete after verify getUuid " + responseDocumentFromDeleteVerify.getUuid());
        String status = responseDocumentFromDeleteVerify.getStatus();
        LOG.debug("in JcrWorkItemDocumentManager :delete status " + status);

        if (status.equalsIgnoreCase("success")) {
            if (responseDocumentFromDeleteVerify.getCategory().equalsIgnoreCase("work") && responseDocumentFromDeleteVerify.getFormat().equalsIgnoreCase("oleml") && responseDocumentFromDeleteVerify.getType().equalsIgnoreCase("instance")) {
                LOG.debug("in JcrWorkItemDocumentManager :delete if of instance");
                RequestDocument requestDocumentForInstance = prepareRequestDocument(responseDocumentFromDeleteVerify);
                documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocumentForInstance.getCategory(), requestDocumentForInstance.getType(), requestDocumentForInstance.getFormat());
                responseDocument = documentManager.delete(requestDocumentForInstance, session);
            } else if (responseDocumentFromDeleteVerify.getCategory().equalsIgnoreCase("work") && responseDocumentFromDeleteVerify.getFormat().equalsIgnoreCase("marc") && responseDocumentFromDeleteVerify.getType().equalsIgnoreCase("bibliographic")) {
                LOG.debug("in JcrWorkItemDocumentManager :delete if of marc");
                RequestDocument requestDocumentForBib = prepareRequestDocument(responseDocumentFromDeleteVerify);
                documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocumentForBib.getCategory(), requestDocumentForBib.getType(), requestDocument.getFormat());
                responseDocument = documentManager.delete(requestDocumentForBib, session);
            } else if (responseDocumentFromDeleteVerify.getCategory().equalsIgnoreCase("work") && responseDocumentFromDeleteVerify.getFormat().equalsIgnoreCase("oleml") && responseDocumentFromDeleteVerify.getType().equalsIgnoreCase("item")) {
                LOG.debug("in JcrWorkItemDocumentManager :delete if of item");
                List<String> itemIdentifierList = new ArrayList<String>();
                itemIdentifierList.add(requestDocument.getUuid());
                deleteFromRepository(itemIdentifierList, session);

            }
        }
        return responseDocument;
    }

    @Override
    protected void modifyContent(RequestDocument reqDoc, Session session, Node nodeByUUID)
            throws RepositoryException, FileNotFoundException, OleDocStoreException {
        if (reqDoc.getOperation().equalsIgnoreCase("checkIn")) {
            ItemOlemlRecordProcessor itemProcessor = new ItemOlemlRecordProcessor();

            if (reqDoc.getContent() != null && reqDoc.getContent().getContent() != null) {
                // get new item content to check in
                Item newItem = itemProcessor.fromXML(reqDoc.getContent().getContent());
                // verify new item call number and shelving scheme to build sortable call num
                Boolean status = true;
                if (newItem.getCallNumber() == null) {
                    newItem.setCallNumber(new CallNumber());
                }
                CallNumber cNum = newItem.getCallNumber();
                if (!(cNum.getShelvingOrder() != null && cNum.getShelvingOrder().getFullValue() != null &&
                        cNum.getShelvingOrder().getFullValue().trim().length() > 0)) {
                    if (!(cNum.getNumber() != null && cNum.getNumber().trim().length() > 0)) {
                        OleHoldings hold = getHoldings(nodeByUUID);
                        updateShelvingOrder(newItem, hold);
                        status = false;
                    } else {
                        updateShelvingOrder(newItem, null);
                        status = false;
                    }
                }
                if (status) {
                    //get already existing item content from jcr.
                    String existingItemXml = nodeManager.getData(nodeByUUID);
                    // if new item contains shelving scheme and call number
                    if (existingItemXml != null) {
                        Item existItem = itemProcessor.fromXML(existingItemXml);
                        if (existItem.getCallNumber() == null) {
                            existItem.setCallNumber(new CallNumber());
                        }
                        CallNumber existCNum = existItem.getCallNumber();
                        setItemValuesFromNullToEmpty(existItem);
                        setItemValuesFromNullToEmpty(newItem);
                        cNum = newItem.getCallNumber();
                        computeCallNumberType(cNum);
                        if (!(existCNum.getNumber().equalsIgnoreCase((cNum.getNumber()))) |
                                !(existCNum.getShelvingScheme().getCodeValue().equalsIgnoreCase(cNum.getShelvingScheme().getCodeValue())) |
                                !((existItem.getEnumeration()).equalsIgnoreCase(newItem.getEnumeration())) |
                                !((existItem.getChronology()).equalsIgnoreCase(newItem.getChronology())) |
                                !((existItem.getCopyNumber()).equalsIgnoreCase(newItem.getCopyNumber()))) {
                            if (!(cNum.getNumber() != null && cNum.getNumber().trim().length() > 0)) {
                                OleHoldings hold = getHoldings(nodeByUUID);
                                updateShelvingOrder(newItem, hold);
                            } else {
                                updateShelvingOrder(newItem, null);
                            }

                        }
                    }
                }
                reqDoc.getContent().setContent(itemProcessor.toXML(newItem));
            }
        }
    }

    private OleHoldings getHoldings(Node nodeByUUID) throws RepositoryException, FileNotFoundException, OleDocStoreException {
        // if new item call num and shelving scheme are empty get them from holdings.
        Node holdingsNode = nodeByUUID.getParent();
        Node holdingsFileNode = holdingsNode.getNode(FILE_HOLDINGS);
        String holdXml = nodeManager.getData(holdingsFileNode);
        HoldingOlemlRecordProcessor holdProc = new HoldingOlemlRecordProcessor();
        OleHoldings hold = holdProc.fromXML(holdXml);
        return hold;
    }

    protected void updateItemCallNumberFromHoldings(Node nodeByUUID, OleHoldings newHold, RequestDocument reqDoc)
            throws RepositoryException, FileNotFoundException, OleDocStoreException {
        Node holdingsNode = nodeByUUID.getParent();
//        Node itemNode = holdingsNode.getNodes(ProcessParameters.FILE_ITEM);
        for (Iterator<Node> itemIterator = holdingsNode.getNodes(ProcessParameters.FILE_ITEM); itemIterator.hasNext(); ) {
            Node itemNode = itemIterator.next();
            String itemXml = nodeManager.getData(itemNode);
            String uuid = itemNode.getProperty("jcr:uuid").getString();
            ItemOlemlRecordProcessor itemProcessor = new ItemOlemlRecordProcessor();
            Item item = itemProcessor.fromXML(itemXml);
            if (item != null) {
                if (item.getCallNumber() == null) {
                    item.setCallNumber(new CallNumber());
                }
                item.getCallNumber().setNumber("");
                if (item.getCallNumber().getShelvingScheme() != null)
                    item.getCallNumber().getShelvingScheme().setCodeValue("");
                updateShelvingOrder(item, newHold);
                String newItemXml = itemProcessor.toXML(item);
                buildRequestDocumentForItem(newItemXml, reqDoc, uuid);
            }
        }
    }

    protected void updateContentToNode(RequestDocument reqDoc, Session session, byte[] documentBytes, Node nodeByUUID)
            throws RepositoryException, OleDocStoreException {
        documentBytes = convertContentToBytes(reqDoc);
        super.updateContentToNode(reqDoc, session, documentBytes, nodeByUUID);
    }

    private void buildRequestDocumentForItem(String newItemXml, RequestDocument reqDoc, String uuid) {
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setOperation("checkIn");
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.ITEM.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setUuid(uuid);
        requestDocument.setId(uuid);
        Content content = new Content();
        content.setContent(newItemXml);
        requestDocument.setContent(content);
        List<RequestDocument> reqDocList = reqDoc.getLinkedRequestDocuments();
        if (reqDocList == null) {
            reqDoc.setLinkedRequestDocuments(new ArrayList<RequestDocument>());
        }
        reqDocList.add(requestDocument);
    }

    private void setItemValuesFromNullToEmpty(Item item) {
        CallNumber existCNum = item.getCallNumber();
        if (existCNum == null) {
            item.setCallNumber(new CallNumber());
        }
        if (existCNum.getNumber() == null) {
            existCNum.setNumber("");
        }
        ShelvingScheme existSS = existCNum.getShelvingScheme();
        if (existSS == null) {
            existCNum.setShelvingScheme(new ShelvingScheme());
            existSS = existCNum.getShelvingScheme();
        }
        if (existSS != null && !(existSS.getCodeValue() != null && existSS.getCodeValue().trim().length() > 0)) {
            existSS.setCodeValue("");
        }
        if (item.getEnumeration() == null) {
            item.setEnumeration("");
        }
        if (item.getChronology() == null) {
            item.setChronology("");
        }
        if (item.getCopyNumber() == null) {
            item.setCopyNumber("");
        }
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        Session session = (Session) object;
        validateItem(requestDocument, session, valuesList);
    }


    public void validateNewItem(RequestDocument linkedRequestDocument, Session session, List<String> fieldValues, String instanceUuid) throws OleDocStoreException {
        try {
            String itemContent = linkedRequestDocument.getContent().getContent();
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            Item item = itemOlemlRecordProcessor.fromXML(itemContent);
            itemBarcodeValidation(item, fieldValues, null);
            if (item.getCallNumber() != null) {
                Node instanceNode = nodeManager.getNodeByUUID(session, instanceUuid);
                if (instanceNode != null && instanceNode.hasNode(ProcessParameters.NODE_HOLDINGS)) {
                    Node holdingsParentNode = instanceNode.getNode(ProcessParameters.NODE_HOLDINGS);
                    if (holdingsParentNode.hasNode(ProcessParameters.FILE_HOLDINGS)) {
                        Node holdingsNode = holdingsParentNode.getNode(ProcessParameters.FILE_HOLDINGS);
                        if (holdingsNode != null) {
                            getHoldingsContentNValidateItem(holdingsNode, item);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new OleDocStoreException(e.getMessage(), e);
        }
    }


    public void validateItem(RequestDocument linkedRequestDocument, Session session, List<String> fieldValues) throws OleDocStoreException {
        try {
            String itemContent = linkedRequestDocument.getContent().getContent();
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            Item item = itemOlemlRecordProcessor.fromXML(itemContent);
            //TODO:Ignored this validation as it affecting check in operation for an existing barcode
            itemBarcodeValidation(item, fieldValues, linkedRequestDocument.getId());
            if (item.getCallNumber() != null) {
                Node node = nodeManager.getNodeByUUID(session, linkedRequestDocument.getUuid());
                Node parentNode = null;
                parentNode = node.getParent();
                if (parentNode != null) {
                    if (parentNode.hasNode(ProcessParameters.FILE_HOLDINGS)) {
                        Node holdingsNode = parentNode.getNode(ProcessParameters.FILE_HOLDINGS);
                        getHoldingsContentNValidateItem(holdingsNode, item);
                    }
                }
            }
        } catch (Exception e) {
            throw new OleDocStoreException(e.getMessage(), e);
        }
    }

    public void itemBarcodeValidation(Item item, List<String> fieldValues, String id) throws OleDocStoreException {
        if (item.getAccessInformation() != null && StringUtils.isNotEmpty(item.getAccessInformation().getBarcode())) {
            try {
                if (fieldValues.contains(item.getAccessInformation().getBarcode()) ||
                        QueryServiceImpl.getInstance().isFieldValueExists(DocType.ITEM.getCode(), "ItemBarcode_display", item.getAccessInformation().getBarcode(), id)) {
                    throw new OleDocStoreException("Barcode " + item.getAccessInformation().getBarcode() + " already exists ");
                }
                fieldValues.add(item.getAccessInformation().getBarcode());
            } catch (Exception e) {
                throw new OleDocStoreException(e.getMessage(), e);
            }
        }

    }

    private void getHoldingsContentNValidateItem(Node holdingsNode, Item item) throws Exception {
        String content = checkOutContent(holdingsNode, DocFormat.OLEML.getCode(), "ole-khuntley");
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(content);
        CallNumber callNumber = item.getCallNumber();
        validateCallNumber(callNumber, oleHoldings);
    }


}
