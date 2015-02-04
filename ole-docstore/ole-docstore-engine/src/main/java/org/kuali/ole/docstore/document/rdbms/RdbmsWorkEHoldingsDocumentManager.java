package org.kuali.ole.docstore.document.rdbms;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.rdbms.bo.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.CallNumber;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.CallNumberType;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EHoldings;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.ShelvingOrder;
import org.kuali.ole.docstore.model.xstream.work.oleml.WorkEHoldingOlemlRecordProcessor;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 7/24/13
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsWorkEHoldingsDocumentManager extends RdbmsWorkEInstanceDocumentManager {

    private static RdbmsWorkEHoldingsDocumentManager ourInstanceRdbms = null;
    private static final Logger LOG = LoggerFactory.getLogger(RdbmsWorkEHoldingsDocumentManager.class);
    private BusinessObjectService businessObjectService;

    public static RdbmsWorkEHoldingsDocumentManager getInstance() {
        if (null == ourInstanceRdbms) {
            ourInstanceRdbms = new RdbmsWorkEHoldingsDocumentManager();
        }
        return ourInstanceRdbms;
    }

    @Override
    public ResponseDocument checkoutContent(RequestDocument requestDocument, Object object) {
        ResponseDocument respDoc = new ResponseDocument();
        Map eInstanceMap = new HashMap();
        eInstanceMap.put("eHoldingsIdentifier", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        EHoldingsRecord eHoldingsRecord = getBusinessObjectService().findByPrimaryKey(EHoldingsRecord.class, eInstanceMap);
        EHoldings eHoldings = buildEHoldingsContent(eHoldingsRecord);
        String content = new WorkEHoldingOlemlRecordProcessor().toXML(eHoldings);
        Content contentObj = new Content();
        contentObj.setContent(content);
        respDoc.setUuid(requestDocument.getUuid());
        respDoc.setCategory(requestDocument.getCategory());
        respDoc.setType(requestDocument.getType());
        respDoc.setFormat(requestDocument.getFormat());
        respDoc.setContent(contentObj);
        return respDoc;
    }

    @Override
    public void deleteDocs(RequestDocument requestDocument, Object object) {
        ResponseDocument responseDocument = new ResponseDocument();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        EInstanceRecord eInstanceRecord = new EInstanceRecord();
        EHoldingsRecord eHoldingsRecord = new EHoldingsRecord();
        String eHoldingsId = DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid());
        Map itemMap = new HashMap();
        itemMap.put("eHoldingsIdentifier", eHoldingsId);
        eHoldingsRecord.seteHoldingsIdentifier(eHoldingsId);
        eInstanceRecord.seteInstanceIdentifier(eHoldingsId);
        deleteEHoldingNChildRecords(eHoldingsRecord);
        getBusinessObjectService().delete(eInstanceRecord);
        buildResponseDocument(requestDocument, eHoldingsRecord, responseDocument);
    }

    @Override
    public void checkInContent(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException {
        AdditionalAttributes attributes = requestDocument.getAdditionalAttributes();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        if (requestDocument.getContent().getContent() != null) {
            modifyContent(requestDocument, businessObjectService, DocumentUniqueIDPrefix.getDocumentId(requestDocument.getId()));
            String content = requestDocument.getContent().getContent();
            WorkEHoldingOlemlRecordProcessor workEHoldingOlemlRecordProcessor = new WorkEHoldingOlemlRecordProcessor();
            EHoldings eHoldings = workEHoldingOlemlRecordProcessor.fromXML(content);
            Map parentCriteria1 = new HashMap();
            parentCriteria1.put("eHoldingsIdentifier", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
            EHoldingsRecord eHoldingsRecord = getBusinessObjectService().findByPrimaryKey(EHoldingsRecord.class, parentCriteria1);
            buildHoldingRecordForCheckIn(eHoldings, eHoldingsRecord);
            // getBusinessObjectService().save(holdingsRecord);
            requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(eHoldingsRecord.getUniqueIdPrefix(), eHoldingsRecord.geteHoldingsIdentifier()));
            responseDocument.setAdditionalAttributes(attributes);
            buildResponseDocument(requestDocument, eHoldingsRecord, responseDocument);
        }
    }

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument, EHoldingsRecord eHoldingsRecord,
                                                  ResponseDocument responseDocument) {
        responseDocument.setId(eHoldingsRecord.geteHoldingsIdentifier());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(eHoldingsRecord.getUniqueIdPrefix(), eHoldingsRecord.geteHoldingsIdentifier()));
        responseDocument.setId(eHoldingsRecord.geteHoldingsIdentifier());
        return responseDocument;
    }

    private void buildHoldingRecordForCheckIn(EHoldings eHoldings, EHoldingsRecord eHoldingsRecord) {
        saveEHoldingsRecord(eHoldings, eHoldingsRecord);
    }

    protected void modifyContent(RequestDocument reqDoc, BusinessObjectService businessObjectService, String holdingId) throws OleDocStoreException {
        WorkEHoldingOlemlRecordProcessor holdProcessor = new WorkEHoldingOlemlRecordProcessor();
        String instanceId = "";
        if (reqDoc != null && reqDoc.getContent() != null) {
            // getting holdings from request document
            EHoldings newHold = holdProcessor.fromXML(reqDoc.getContent().getContent());
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
                    Map holdingsMap = new HashMap();
                    holdingsMap.put("eHoldingsIdentifier", holdingId);
                    EHoldings existHol = null;
                    List<EHoldingsRecord> eHoldingsRecords = (List<EHoldingsRecord>) businessObjectService.findMatching(EHoldingsRecord.class, holdingsMap);
                    if (eHoldingsRecords != null && eHoldingsRecords.size() > 0) {
                        EHoldingsRecord holdingsRecord = eHoldingsRecords.get(0);
                        existHol = buildEHoldingsContent(holdingsRecord);
                        instanceId = holdingsRecord.geteInstanceIdentifier();
                    }
                    if (existHol != null) {
                        setHoldValuesFromNullToEmpty(existHol.getCallNumber());
                        setHoldValuesFromNullToEmpty(newHold.getCallNumber());
                        if (existHol.getCallNumber() != null && existHol.getCallNumber().getNumber() != null && !(existHol.getCallNumber().getNumber().equalsIgnoreCase(cNum.getNumber()) &&
                                existHol.getCallNumber().getCallNumberType().getCodeValue().equalsIgnoreCase(cNum.getCallNumberType().getCodeValue()))) {
                            processCallNumber(newHold);
                            String newHolXml = holdProcessor.toXML(newHold);
                            reqDoc.getContent().setContent(newHolXml);
                            status = false;
                        }
                    }
                    if (status && !(cNum.getShelvingOrder().getFullValue() != null &&
                            cNum.getShelvingOrder().getFullValue().trim().length() > 0)) {
                        processCallNumber(newHold);
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
            if (callNumber.getCallNumberType() == null) {
                callNumber.setCallNumberType(new CallNumberType());
            }
            if (callNumber.getCallNumberType().getCodeValue() == null) {
                callNumber.getCallNumberType().setCodeValue("");
            }
        }
    }

    protected void processCallNumber(EHoldings eHolding) throws OleDocStoreException {
        if (eHolding != null && eHolding.getCallNumber() != null) {
            //validateCallNumber(oleHolding.getCallNumber());
            CallNumber cNum = eHolding.getCallNumber();
            computeCallNumberType(cNum);
            if (cNum.getNumber() != null && cNum.getNumber().trim().length() > 0) {
                //Build sortable key if a valid call number exists
                boolean isValid = validateCallNumber(cNum.getNumber(), cNum.getCallNumberType().getCodeValue());
                String value = "";
                if (isValid) {
                    value = buildSortableCallNumber(cNum.getNumber(), cNum.getCallNumberType().getCodeValue());
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

    /**
     * Compute 'call number type name' if a valid 'call number type code' is available
     *
     * @param callNumber
     */
    public void computeCallNumberType(CallNumber callNumber) {
        Set<String> validCallNumberTypeSet = org.kuali.ole.utility.callnumber.CallNumberType.validCallNumberTypeCodeSet;
        if (callNumber != null) {
            if (callNumber.getCallNumberType() != null) {
                String callNumberTypeCode = callNumber.getCallNumberType().getCodeValue();
                String callNumberTypeName = "";
                //If call number type code is valid
                if ((StringUtils.isNotEmpty(callNumberTypeCode)) && (validCallNumberTypeSet
                        .contains(callNumberTypeCode))) {
                    callNumberTypeName = org.kuali.ole.utility.callnumber.CallNumberType.valueOf(callNumberTypeCode).getDescription();
                    callNumber.getCallNumberType().setFullValue(callNumberTypeName);
                }
            }
        }
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
}