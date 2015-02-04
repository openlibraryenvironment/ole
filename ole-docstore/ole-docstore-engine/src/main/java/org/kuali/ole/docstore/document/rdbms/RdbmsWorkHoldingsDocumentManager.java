package org.kuali.ole.docstore.document.rdbms;

import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingOrder;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 7/1/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsWorkHoldingsDocumentManager
        extends RdbmsWorkInstanceDocumentManager {

    private static RdbmsWorkHoldingsDocumentManager ourInstanceRdbms = null;
    private static final Logger LOG = LoggerFactory
            .getLogger(RdbmsWorkHoldingsDocumentManager.class);
    private BusinessObjectService businessObjectService;

    public static RdbmsWorkHoldingsDocumentManager getInstance() {
        if (null == ourInstanceRdbms) {
            ourInstanceRdbms = new RdbmsWorkHoldingsDocumentManager();
        }
        return ourInstanceRdbms;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    @Override
    public void deleteDocs(RequestDocument requestDocument, Object object) {
        ResponseDocument responseDocument = new ResponseDocument();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        String holdingsId = DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid());
        Map itemMap = new HashMap();
        itemMap.put("holdingsId", holdingsId);
        holdingsRecord.setHoldingsId(holdingsId);
        getBusinessObjectService().delete(holdingsRecord);
        buildResponseDocument(requestDocument, holdingsRecord, responseDocument);

    }

    @Override
    public ResponseDocument checkoutContent(RequestDocument requestDocument, Object object) {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        ResponseDocument responseDocument = new ResponseDocument();
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("holdingsId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<HoldingsRecord> holdingsRecordList = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord.class, parentCriteria1);
        if(holdingsRecordList != null && holdingsRecordList.size() > 0) {
            HoldingsRecord holdingsRecord = holdingsRecordList.get(0);
            OleHoldings oleHoldings = buildHoldingsContent(holdingsRecord);
            String content = new InstanceOlemlRecordProcessor().toXML(oleHoldings);
            AdditionalAttributes additionalAttributes = new AdditionalAttributes();
            additionalAttributes.setAttribute(AdditionalAttributes.STAFFONLYFLAG, holdingsRecord.getStaffOnlyFlag().toString());
            Map<String, String> mapObject = new HashMap<String, String>();
            mapObject.put("staffOnlyFlag", holdingsRecord.getStaffOnlyFlag().toString());
            additionalAttributes.setAttributeMap(mapObject);
            Content contentObj = new Content();
            contentObj.setContent(content);
            responseDocument.setUuid(requestDocument.getUuid());
            responseDocument.setCategory(requestDocument.getCategory());
            responseDocument.setType(requestDocument.getType());
            responseDocument.setFormat(requestDocument.getFormat());
            responseDocument.setContent(contentObj);
            responseDocument.setAdditionalAttributes(additionalAttributes);
        } else {
            responseDocument.setStatus("Failed");
            responseDocument.setStatusMessage("Holdings does not exist.");
        }
        return responseDocument;
    }


    @Override
    public void checkInContent(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException {
        AdditionalAttributes attributes = requestDocument.getAdditionalAttributes();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        if (requestDocument.getContent().getContent() != null) {
            modifyContent(requestDocument, businessObjectService, DocumentUniqueIDPrefix.getDocumentId(requestDocument.getId()));
            String content = requestDocument.getContent().getContent();
            HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
            OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(content);
            Map parentCriteria1 = new HashMap();
            parentCriteria1.put("holdingsId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
            HoldingsRecord holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, parentCriteria1);
            if (attributes != null) {
                holdingsRecord.setStaffOnlyFlag(Boolean.valueOf(attributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG)));
                holdingsRecord.setCreatedBy(attributes.getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY));
                holdingsRecord.setUpdatedBy(attributes.getAttribute(AdditionalAttributes.HOLDINGS_UPDATED_BY));
                //holdingsRecord.setCreatedDate(attributes.getAttribute(AdditionalAttributes.DATE_ENTERED));
                String createdDateForHoldings = attributes.getAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED);
                createdDateForHoldings(holdingsRecord,requestDocument.getAdditionalAttributes());

            }
            buildHoldingRecordForCheckIn(oleHoldings, holdingsRecord);
            // getBusinessObjectService().save(holdingsRecord);
            requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
            respDoc.setAdditionalAttributes(attributes);
            buildResponseDocument(requestDocument, holdingsRecord, respDoc);
        }
    }

    @Override
    public Node storeDocument(RequestDocument requestDocument, Object object, ResponseDocument responseDocument)
            throws OleDocStoreException {
        return null;
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        if (requestDocument.getContent() != null && requestDocument.getContent().getContent() != null) {
            OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(requestDocument.getContent().getContent());
            validateHoldings(oleHoldings);
        }
    }

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument, HoldingsRecord holdingsRecord,
                                                  ResponseDocument responseDocument) {
        responseDocument.setId(holdingsRecord.getHoldingsId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
        //responseDocument.setId(holdingsRecord.getInstanceId());
        return responseDocument;
    }

    public void validateHoldings(OleHoldings oleHoldings) throws OleDocStoreException {
        if (oleHoldings.getCallNumber() != null) {
            CallNumber callNumber = oleHoldings.getCallNumber();
            validateCallNumber(callNumber);
        }
    }

    private void buildHoldingRecordForCheckIn(OleHoldings oleHoldings, HoldingsRecord holdingsRecord) {
        saveHoldingsRecord(oleHoldings, holdingsRecord);
    }

    protected void modifyContent(RequestDocument reqDoc, BusinessObjectService businessObjectService, String holdingId) throws OleDocStoreException {
        HoldingOlemlRecordProcessor holdProcessor = new HoldingOlemlRecordProcessor();
        String instanceId = "";
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
                    //get existing holdings from rdbms
                    boolean status = true;
                    RdbmsWorkItemDocumentManager rdbmsWorkItemDocumentManager = RdbmsWorkItemDocumentManager.getInstance();
                    Map holdingsMap = new HashMap();
                    holdingsMap.put("holdingsId", holdingId);
                    OleHoldings existHol = null;
                    List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) businessObjectService.findMatching(HoldingsRecord.class, holdingsMap);
                    if (holdingsRecords != null && holdingsRecords.size() > 0) {
                        HoldingsRecord holdingsRecord = holdingsRecords.get(0);
                        existHol = buildHoldingsContent(holdingsRecord);
                        //instanceId = holdingsRecord.getInstanceId();
                    }
                    if (existHol != null) {
                        setHoldValuesFromNullToEmpty(existHol.getCallNumber());
                        setHoldValuesFromNullToEmpty(newHold.getCallNumber());
                        if (existHol.getCallNumber() != null && existHol.getCallNumber().getNumber() != null && !(existHol.getCallNumber().getNumber().equalsIgnoreCase(cNum.getNumber()) &&
                                existHol.getCallNumber().getShelvingScheme().getCodeValue().equalsIgnoreCase(cNum.getShelvingScheme().getCodeValue()))) {
                            processCallNumber(newHold);
                            String newHolXml = holdProcessor.toXML(newHold);
                            reqDoc.getContent().setContent(newHolXml);
                            rdbmsWorkItemDocumentManager.updateItemCallNumberFromHoldings(instanceId, newHold, reqDoc);
                            status = false;
                        }
                    }
                    if (status && !(cNum.getShelvingOrder().getFullValue() != null &&
                            cNum.getShelvingOrder().getFullValue().trim().length() > 0)) {
                        processCallNumber(newHold);
                        String newHolXml = holdProcessor.toXML(newHold);
                        reqDoc.getContent().setContent(newHolXml);
//                        rdbmsWorkItemDocumentManager.updateItemCallNumberFromHoldings(nodeByUUID, newHold, reqDoc);
                    }
                }

            }
        }
    }

    private void setHoldValuesFromNullToEmpty(CallNumber callNumber) {
        if (callNumber != null) {
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
    }


    private void createdDateForHoldings(HoldingsRecord holdingsRecord, AdditionalAttributes additionalAttributes) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Timestamp createdDate = null;
        String createdDateForHoldings = additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED);
        String updateDateForHoldings =   additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_LAST_UPDATED);
        holdingsRecord.setCreatedBy(additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY));
        holdingsRecord.setUpdatedBy(additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_UPDATED_BY));
        try {
            createdDate = new Timestamp(df.parse(createdDateForHoldings).getTime());
            holdingsRecord.setCreatedDate(createdDate);
            holdingsRecord.setUpdatedDate(new Timestamp(df.parse(updateDateForHoldings).getTime()));

        } catch (Exception e) {
            LOG.error("Created Date for Holdings" + e);
        }
    }

    private void updatedDateForHoldings(HoldingsRecord holdingsRecord, String DateForHoldings) {
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Timestamp updatedDate = null;
        try {
            updatedDate = new Timestamp(df.parse(DateForHoldings).getTime());
            holdingsRecord.setUpdatedDate(updatedDate);

        } catch (Exception e) {
            LOG.error("Created Date for Holdings" + e);
        }
    }

}
