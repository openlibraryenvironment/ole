package org.kuali.ole.docstore.document.rdbms;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.service.QueryServiceImpl;

import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 7/1/13
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class RdbmsWorkItemDocumentManager extends RdbmsWorkInstanceDocumentManager {

    private static RdbmsWorkItemDocumentManager ourInstanceRdbms = null;
    private static final Logger LOG = LoggerFactory
            .getLogger(RdbmsWorkItemDocumentManager.class);
    private BusinessObjectService businessObjectService;

    public static RdbmsWorkItemDocumentManager getInstance() {
        if (null == ourInstanceRdbms) {
            ourInstanceRdbms = new RdbmsWorkItemDocumentManager();
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
        //ItemRecord itemRecord = new ItemRecord();
        String itemId = DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid());
        Map itemMap = new HashMap();
        itemMap.put("itemId", itemId);
        List<ItemRecord> itemRecords = (List<ItemRecord>) businessObjectService.findMatching(ItemRecord.class, itemMap);
        if (itemRecords != null && itemRecords.size() > 0) {
            ItemRecord itemRecord = itemRecords.get(0);
            if (itemRecord.getFormerIdentifierRecords() != null && itemRecord.getFormerIdentifierRecords().size() > 0) {
                List<FormerIdentifierRecord> formerIdentifierRecords = itemRecord.getFormerIdentifierRecords();
                businessObjectService.delete(formerIdentifierRecords);
            }


            if (itemRecord.getItemNoteRecords() != null && itemRecord.getItemNoteRecords().size() > 0) {
                List<ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
                businessObjectService.delete(itemNoteRecords);
            }


            if (itemRecord.getLocationsCheckinCountRecords() != null && itemRecord.getLocationsCheckinCountRecords().size() > 0) {
                List<LocationsCheckinCountRecord> locationsCheckinCountRecords = itemRecord.getLocationsCheckinCountRecords();
                businessObjectService.delete(locationsCheckinCountRecords);
            }
            itemRecord.setItemStatusId(null);
            itemRecord.setItemTypeId(null);
            itemRecord.setTempItemTypeId(null);
            itemRecord.setStatisticalSearchId(null);
            businessObjectService.delete(itemRecord);
            buildResponseDocument(requestDocument, itemRecord, responseDocument);
        }

    }

    @Override
    public ResponseDocument checkoutContent(RequestDocument requestDocument, Object object) {
        ResponseDocument responseDocument = new ResponseDocument();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("itemId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<ItemRecord> itemRecordList = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, parentCriteria1);
        if (itemRecordList != null && itemRecordList.size() > 0) {
            ItemRecord itemRecord = itemRecordList.get(0);
            Item item = buildItemContent(itemRecord);
            String content = new InstanceOlemlRecordProcessor().toXML(item);
            AdditionalAttributes additionalAttributes = new AdditionalAttributes();

            /*if(additionalAttributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG)!=null) {
            String  staffOnlyFlagNew=additionalAttributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG);
            itemRecord.setStaffOnlyFlag(staffOnlyFlagNew);
            }*/

            if (itemRecord.getStaffOnlyFlag() != null) {
                additionalAttributes.setAttribute(AdditionalAttributes.STAFFONLYFLAG, itemRecord.getStaffOnlyFlag().toString());
            }
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
            responseDocument.setStatusMessage("Item does not exist.");
        }
        return responseDocument;

    }


    @Override
    public void checkInContent(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        modifyContent(requestDocument, DocumentUniqueIDPrefix.getDocumentId(requestDocument.getId()));
        AdditionalAttributes attributes = requestDocument.getAdditionalAttributes();
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        String instanceId = "";
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("itemId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getId()));
        ItemRecord itemRecord = getBusinessObjectService().findByPrimaryKey(ItemRecord.class, parentCriteria1);
        Item item = new ItemOlemlRecordProcessor().fromXML(requestDocument.getContent().getContent());
        if (attributes != null) {
            itemRecord.setStaffOnlyFlag(Boolean.valueOf(attributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG)));
        }
        buildItemContentForCheckin(item, itemRecord);
        requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), itemRecord.getItemId()));
        respDoc.setAdditionalAttributes(attributes);
        buildResponseDocument(requestDocument, itemRecord, respDoc);
    }

    @Override
    public Node storeDocument(RequestDocument requestDocument, Object object, ResponseDocument responseDocument)
            throws OleDocStoreException {
        return null;
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        validateItem(requestDocument, businessObjectService, valuesList);
    }

    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {

        //  BusinessObjectService businessObjectService = (BusinessObjectService) object;
        LOG.debug("workItemDocumentManager deleteVerify method");
        //List<String> instanceIdentifierList = new ArrayList<String>();
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
        else {
            responseDocument.setUuid(requestDocument.getUuid());
            responseDocument.setId(requestDocument.getId());
            responseDocument.setCategory(requestDocument.getCategory());
            responseDocument.setType(requestDocument.getType());
            responseDocument.setFormat(requestDocument.getFormat());
            responseDocument.setStatus("success");
            responseDocument.setStatusMessage("success");
        }

        /* int itemSize = 0;
     String instanceIdentifier = "";
     Map itemMap = new HashMap();
     itemMap.put("itemId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
     List<ItemRecord> itemRecords = (List<ItemRecord>) businessObjectService.findMatching(ItemRecord.class, itemMap);
     if (itemRecords != null & itemRecords.size() == 1) {
         instanceIdentifier = itemRecords.get(0).getHoldingsId();
     }

     Map instanceMap = new HashMap();
     instanceMap.put("instanceId", instanceIdentifier);
     itemRecords = (List<ItemRecord>) businessObjectService.findMatching(ItemRecord.class, instanceMap);
     if (itemRecords != null) {
         itemSize = itemRecords.size();
     }

     if (StringUtils.isNotEmpty(instanceIdentifier)) {
         String prefix = DocumentUniqueIDPrefix.getPrefix(DocCategory.WORK.getCode(), DocType.INSTANCE.getCode(), DocFormat.OLEML.getCode());
         requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(prefix, instanceIdentifier));
         requestDocument.setCategory(DocCategory.WORK.getCode());
         requestDocument.setType(DocType.INSTANCE.getCode());
         requestDocument.setFormat(DocFormat.OLEML.getCode());
         responseDocument = RdbmsWorkInstanceDocumentManager.getInstance().deleteVerify(requestDocument, businessObjectService);
     } else {
         responseDocument.setUuid(requestDocument.getUuid());
         responseDocument.setId(requestDocument.getId());
         responseDocument.setCategory(requestDocument.getCategory());
         responseDocument.setType(requestDocument.getType());
         responseDocument.setFormat(requestDocument.getFormat());
         responseDocument.setStatus("success");
         responseDocument.setStatusMessage("success");
     }   */

        return responseDocument;
    }

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument, ItemRecord itemRecord, ResponseDocument responseDocument) {
        responseDocument.setId(itemRecord.getItemId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(itemRecord.getItemId());
       // responseDocument.setId(itemRecord.getInstanceId());
        return responseDocument;
    }

    public void validateNewItem(RequestDocument linkedRequestDocument, BusinessObjectService businessObjectService, List<String> fieldValues, String id) throws OleDocStoreException {
        try {
            String itemContent = linkedRequestDocument.getContent().getContent();
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            Item item = itemOlemlRecordProcessor.fromXML(itemContent);
            itemBarcodeValidation(item, fieldValues, null);

            if (item.getCallNumber() != null) {
                Map instanceMap = new HashMap();
                instanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(id));
                List<InstanceRecord> instanceRecords = (List<InstanceRecord>) getBusinessObjectService().findMatching(InstanceRecord.class, instanceMap);
                HoldingsRecord holdingsRecord = null;
                if (instanceRecords != null && instanceRecords.size() > 0) {
                    List<HoldingsRecord> holdingsRecords = instanceRecords.get(0).getHoldingsRecords();
                    if (holdingsRecords != null && holdingsRecords.size() > 0) {
                        holdingsRecord = holdingsRecords.get(0);
                        getHoldingsContentNValidateItem(holdingsRecord, item, getBusinessObjectService());
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

    public void validateItem(RequestDocument linkedRequestDocument, BusinessObjectService businessObjectService, List<String> fieldValues) throws OleDocStoreException {
        try {
            String itemContent = linkedRequestDocument.getContent().getContent();
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            Item item = itemOlemlRecordProcessor.fromXML(itemContent);
            //TODO:Ignored this validation as it affecting check in operation for an existing barcode
            itemBarcodeValidation(item, fieldValues, linkedRequestDocument.getId());
            if (item.getCallNumber() != null) {
                Map itemMap = new HashMap();
                itemMap.put("itemId", DocumentUniqueIDPrefix.getDocumentId(linkedRequestDocument.getId()));
                List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
                if (itemRecords != null && itemRecords.size() > 0) {
                    Map instanceMap = new HashMap();
                   // instanceMap.put("instanceId", itemRecords.get(0).getInstanceId());
                    List<InstanceRecord> instanceRecords = (List<InstanceRecord>) getBusinessObjectService().findMatching(InstanceRecord.class, instanceMap);
                    HoldingsRecord holdingsRecord = null;
                    if (instanceRecords != null && instanceRecords.size() > 0) {
                        List<HoldingsRecord> holdingsRecords = instanceRecords.get(0).getHoldingsRecords();
                        if (holdingsRecords != null && holdingsRecords.size() > 0) {
                            holdingsRecord = holdingsRecords.get(0);
                            getHoldingsContentNValidateItem(holdingsRecord, item, getBusinessObjectService());
                        }
                    }

                }
            }
        } catch (Exception e) {
            throw new OleDocStoreException(e.getMessage(), e);
        }
    }

    private void getHoldingsContentNValidateItem(HoldingsRecord holdingsRecord, Item item, BusinessObjectService businessObjectService) throws Exception {
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
        ResponseDocument responseDocument = RdbmsWorkHoldingsDocumentManager.getInstance().checkoutContent(requestDocument, businessObjectService);
        String content = responseDocument.getContent().getContent();
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(content);
        CallNumber callNumber = item.getCallNumber();
        validateCallNumber(callNumber, oleHoldings);
    }

    protected void buildItemContentForCheckin(Item item, ItemRecord itemRecord) {

        saveItemRecord(item, itemRecord);
    }

    protected void modifyContent(RequestDocument reqDoc, String itemId)
            throws OleDocStoreException {
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

                        OleHoldings hold = getHoldings(itemId);
                        updateShelvingOrder(newItem, hold);
                        status = false;
                    } else {
                        updateShelvingOrder(newItem, null);
                        status = false;
                    }
                }
                if (status) {
                    //get already existing item content from rdbms.
                    String existingItemXml = null;
                    Map itemMap = new HashMap();
                    itemMap.put("itemId", itemId);
                    List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
                    if (itemRecords != null && itemRecords.size() > 0) {
                        ItemRecord itemRecord = itemRecords.get(0);
                        existingItemXml = itemProcessor.toXML(buildItemContent(itemRecord));
                    }

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
                                OleHoldings hold = getHoldings(itemId);
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

    private OleHoldings getHoldings(String itemId) {
        OleHoldings oleHoldings = null;
        Map itemMap = new HashMap();
        itemMap.put("itemId", itemId);
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, itemMap);
        if (itemRecords != null && itemRecords.size() > 0) {
            ItemRecord itemRecord = itemRecords.get(0);
            String instanceId = null;//itemRecord.getInstanceId();
            Map instanceMap = new HashMap();
            instanceMap.put("instanceId", instanceId);
            List<InstanceRecord> instanceRecords = (List<InstanceRecord>) getBusinessObjectService().findMatching(InstanceRecord.class, instanceMap);
            if (instanceRecords != null && instanceRecords.size() > 0) {
                List<HoldingsRecord> holdingsRecords = instanceRecords.get(0).getHoldingsRecords();
                if (holdingsRecords != null && holdingsRecords.size() > 0) {
                    HoldingsRecord holdingsRecord = holdingsRecords.get(0);
                    oleHoldings = buildHoldingsContent(holdingsRecord);
                }
            }
        }
        return oleHoldings;
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

    protected void updateItemCallNumberFromHoldings(String instanceId, OleHoldings newHold, RequestDocument reqDoc)
            throws OleDocStoreException {
        Map instanceMap = new HashMap();
        instanceMap.put("instanceId", instanceId);
        List<InstanceRecord> instanceRecords = (List<InstanceRecord>) getBusinessObjectService().findMatching(InstanceRecord.class, instanceMap);
        InstanceRecord instanceRecord = null;
        if (instanceRecords != null && instanceRecords.size() > 0) {
            instanceRecord = instanceRecords.get(0);
        }
        if (instanceRecord != null) {
            for (ItemRecord itemRecord : instanceRecord.getItemRecords()) {
                ItemOlemlRecordProcessor itemProcessor = new ItemOlemlRecordProcessor();
                Item item = buildItemContent(itemRecord);
                if (item != null) {
                    String itemId = itemRecord.getItemId();
                    if (item.getCallNumber() == null) {
                        item.setCallNumber(new CallNumber());
                    }
                    item.getCallNumber().setNumber("");
                    if (item.getCallNumber().getShelvingScheme() != null)
                        item.getCallNumber().getShelvingScheme().setCodeValue("");
                    updateShelvingOrder(item, newHold);
                    String newItemXml = itemProcessor.toXML(item);
                    buildRequestDocumentForItem(newItemXml, reqDoc, itemId);
                }

            }
        }
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


}
