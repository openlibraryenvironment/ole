package org.kuali.ole.docstore.document.jcr;

import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingOrder;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pranitha J
 * Date: 27/2/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class JcrJcrWorkHoldingsDocumentManager extends JcrWorkInstanceDocumentManager {

    private static JcrJcrWorkHoldingsDocumentManager ourInstance = null;
    private static Logger LOG = LoggerFactory.getLogger(JcrJcrWorkHoldingsDocumentManager.class);


    public static JcrJcrWorkHoldingsDocumentManager getInstance() {
        if (null == ourInstance) {
            ourInstance = new JcrJcrWorkHoldingsDocumentManager();
        }
        return ourInstance;
    }

    @Override
    protected void modifyContent(RequestDocument reqDoc, Session session, Node nodeByUUID)
            throws RepositoryException, FileNotFoundException, OleDocStoreException {
        HoldingOlemlRecordProcessor holdProcessor = new HoldingOlemlRecordProcessor();
        if (reqDoc != null && reqDoc.getContent() != null) {
            // getting holdings from request document
            OleHoldings newHold = holdProcessor.fromXML(reqDoc.getContent().getContent());
            if (newHold != null && newHold.getCallNumber() != null) {
                CallNumber cNum = newHold.getCallNumber();
                // validate holdings call number
//                validateCallNumber(cNum);
                if (cNum.getNumber() != null && cNum.getNumber().trim().length() > 0) {
                    // add new shelving order if null
                    if (cNum.getShelvingOrder() == null) {
                        cNum.setShelvingOrder(new ShelvingOrder());
                    }
                    //get existing holdings from jackrabbit
                    String holdingsXml = nodeManager.getData(nodeByUUID);
                    JcrWorkItemDocumentManager workItemDocumentManager = JcrWorkItemDocumentManager.getInstance();
                    boolean status = true;
                    if (holdingsXml != null) {
                        OleHoldings existHol = holdProcessor.fromXML(holdingsXml);
                        setHoldValuesFromNullToEmpty(existHol.getCallNumber());
                        setHoldValuesFromNullToEmpty(newHold.getCallNumber());
                        if (!(existHol.getCallNumber().getNumber().equalsIgnoreCase(cNum.getNumber()) &&
                                existHol.getCallNumber().getShelvingScheme().getCodeValue().equalsIgnoreCase(cNum.getShelvingScheme().getCodeValue()))) {
                            processCallNumber(newHold);
                            String newHolXml = holdProcessor.toXML(newHold);
                            reqDoc.getContent().setContent(newHolXml);
                            workItemDocumentManager.updateItemCallNumberFromHoldings(nodeByUUID, newHold, reqDoc);
                            status = false;
                        }
                    }
                    if (status && !(cNum.getShelvingOrder().getFullValue() != null &&
                            cNum.getShelvingOrder().getFullValue().trim().length() > 0)) {
                        processCallNumber(newHold);
//                        workItemDocumentManager.updateItemCallNumberFromHoldings(nodeByUUID, newHold, reqDoc);
                    }
                }

            }
        }
    }

    protected void updateContentToNode(RequestDocument reqDoc, Session session, byte[] documentBytes, Node nodeByUUID)
            throws RepositoryException, OleDocStoreException {
        documentBytes = convertContentToBytes(reqDoc);
        super.updateContentToNode(reqDoc, session, documentBytes, nodeByUUID);
        for (RequestDocument linkReqDoc : reqDoc.getLinkedRequestDocuments()) {
            documentBytes = convertContentToBytes(linkReqDoc);
            Node itemNode = nodeManager.getNodeByUUID(session, linkReqDoc.getId());
            super.updateContentToNode(linkReqDoc, session, documentBytes, itemNode);
        }
    }

    private void setHoldValuesFromNullToEmpty(CallNumber callNumber) {
        if (callNumber.getNumber() == null) {
            callNumber.setNumber("");
        }
        if (callNumber.getShelvingScheme() == null) {
            callNumber.setShelvingScheme(new ShelvingScheme());
        }
        if (callNumber.getShelvingScheme().getCodeValue() == null) {
            callNumber.getShelvingScheme().setCodeValue("");
        }
    }

    @Override
    public void validateInput(RequestDocument reqDoc, Object object, List<String> valuesList) throws OleDocStoreException {
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        if (reqDoc.getContent() != null && reqDoc.getContent().getContent() != null) {
            OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(reqDoc.getContent().getContent());
            validateHoldings(oleHoldings);
        }
    }

    public void validateHoldings(OleHoldings oleHoldings) throws OleDocStoreException {
        if (oleHoldings.getCallNumber() != null) {
            CallNumber callNumber = oleHoldings.getCallNumber();
            validateCallNumber(callNumber);
        }
    }
}

