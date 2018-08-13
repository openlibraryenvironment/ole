package org.kuali.ole.docstore.document.rdbms;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.ItemClaimsReturnedRecord;
import org.kuali.ole.docstore.common.document.content.instance.MissingPieceItemRecord;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.ole.utility.callnumber.CallNumberType;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/28/13
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class RdbmsWorkInstanceDocumentManager extends RdbmsAbstarctDocumentManager {

    private static RdbmsWorkInstanceDocumentManager ourInstanceRdbms = null;
    private static final Logger LOG = LoggerFactory.getLogger(RdbmsWorkInstanceDocumentManager.class);
    private BusinessObjectService businessObjectService;

    public static RdbmsWorkInstanceDocumentManager getInstance() {
        if (null == ourInstanceRdbms) {
            ourInstanceRdbms = new RdbmsWorkInstanceDocumentManager();
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
    public Node storeDocument(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException {
        String bibUUid = null;
        String prefixedUuid = responseDocument.getUuid();
        if ((responseDocument.getUuid() != null) && (!"".equals(requestDocument.getUuid()))) {
            bibUUid = DocumentUniqueIDPrefix.getDocumentId(responseDocument.getUuid());
        }
        try {
            responseDocument.setUuid("");
            InstanceOlemlRecordProcessor instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
            businessObjectService = (BusinessObjectService) object;
            if (requestDocument.getContent() == null || requestDocument.getContent().getContent() == null) {
                return null;
            }
            InstanceCollection instanceCollection = new InstanceOlemlRecordProcessor().fromXML(requestDocument.getContent().getContent());
            if (instanceCollection.getInstance() != null && instanceCollection.getInstance().size() > 0) {
                Instance instance = instanceCollection.getInstance().get(0);
                List<String> resIdList = new ArrayList<String>();
                resIdList.addAll(instance.getResourceIdentifier());
                List<String> resIdList1 = new ArrayList<String>();
                resIdList1.addAll(resIdList);
                for (String resId : instance.getResourceIdentifier()) {
                    Map bibMap = new HashMap();
                    bibMap.put("bibId", DocumentUniqueIDPrefix.getDocumentId(resId));
                    List<BibRecord> bibRecords = (List<BibRecord>) getBusinessObjectService().findMatching(BibRecord.class, bibMap);
                    if (bibRecords != null && bibRecords.size() == 0) {
                        resIdList1.remove(resId);
                        return null;
                    }
                }
                instance.setResourceIdentifier(resIdList1);
//                resolveLinkingWithBib(instance);
                if (instance.getResourceIdentifier() != null && instance.getResourceIdentifier().size() > 0) {
                    bibUUid = DocumentUniqueIDPrefix.getDocumentId(instance.getResourceIdentifier().get(0));
                    //TODO: If input file contains multiple ResourceIdentifiers then store the record in to bib-instance table
                }
//                if (instance.getResourceIdentifier().size() == 1) {
//                    if (instance.getResourceIdentifier().get(0) == null || ""
//                            .equals(instance.getResourceIdentifier().get(0))) {
//                        instance.getResourceIdentifier().remove(0);
//                    }
//                }
                OleHoldings oleHoldings = instance.getOleHoldings();
                processCallNumber(oleHoldings);
                Items items = instance.getItems();
                String uniqueIdPrefixforInstance = DocumentUniqueIDPrefix.getPrefix(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
                InstanceRecord instanceRecord = saveInstancerecord(bibUUid, uniqueIdPrefixforInstance);
                String instanceId = instanceRecord.getInstanceId();
                List<String> resIds = instance.getResourceIdentifier();
                List<String> prefixedResourceId = new ArrayList<>();
                for (String resId : resIds) {
                    String prefixedBibId = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, resId);
                    prefixedResourceId.add(prefixedBibId);
                }
                //saveBibInstanceRecord(resIds, instanceId);

                String uniqueIdPrefixForHoldings = DocumentUniqueIDPrefix.getPrefix(requestDocument.getCategory(), DocType.HOLDINGS.getCode(), requestDocument.getFormat());
                HoldingsRecord holdingsRecord = null;
                if (oleHoldings.getExtension() != null && oleHoldings.getExtension().getContent() != null && oleHoldings.getExtension().getContent().size() > 0) {
                    if (oleHoldings.getExtension().getContent().get(0) != null) {
                        AdditionalAttributes additionalAttributes = (AdditionalAttributes) oleHoldings.getExtension().getContent().get(0);
                        String staffOnlyFlagForHoldingsNew = additionalAttributes.getAttributeMap().get("staffOnlyFlag");
                        if (staffOnlyFlagForHoldingsNew != null) {
                            holdingsRecord = saveHoldingsRecord(oleHoldings, instanceId, uniqueIdPrefixForHoldings, staffOnlyFlagForHoldingsNew, additionalAttributes);
                        } else if (requestDocument.getAdditionalAttributes() != null) {
                            //additionalAttributes = requestDocument.getAdditionalAttributes();
                            String staffOnlyFlagForHoldings = additionalAttributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG);
                            additionalAttributes = requestDocument.getAdditionalAttributes();
                            holdingsRecord = saveHoldingsRecord(oleHoldings, instanceId, uniqueIdPrefixForHoldings, staffOnlyFlagForHoldings, additionalAttributes);
                        } else {
                            additionalAttributes = requestDocument.getAdditionalAttributes();
                            holdingsRecord = saveHoldingsRecord(oleHoldings, instanceId, uniqueIdPrefixForHoldings, "false", additionalAttributes);
                        }
                    }
                } else {
                    AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
                    String staffOnlyFlagForHoldings = null;
                    if (additionalAttributes != null) {
                        staffOnlyFlagForHoldings = additionalAttributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG);
                        additionalAttributes = requestDocument.getAdditionalAttributes();
                    } else {
                        if (additionalAttributes == null) {
                            additionalAttributes = new AdditionalAttributes();
                            String holdingsCreatedDate = null;
                            Format formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                            holdingsCreatedDate = formatter.format(new Date());
                            additionalAttributes.setAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY, "BulkIngest-User");
                            additionalAttributes.setAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED, holdingsCreatedDate);
                        }
                        staffOnlyFlagForHoldings = "false";
                    }
                    holdingsRecord = saveHoldingsRecord(oleHoldings, instanceId, uniqueIdPrefixForHoldings, staffOnlyFlagForHoldings, additionalAttributes);
                }
                //HoldingsRecord holdingsRecord = saveHoldingsRecord(oleHoldings, instanceId, uniqueIdPrefixForHoldings);
                String uniqueIdPrefixForItem = DocumentUniqueIDPrefix.getPrefix(requestDocument.getCategory(), DocType.ITEM.getCode(), requestDocument.getFormat());
                List<ItemRecord> itemRecords = saveItemRecords(items, instanceId, uniqueIdPrefixForItem, oleHoldings);
                instance.setInstanceIdentifier(DocumentUniqueIDPrefix.getPrefixedId(instanceRecord.getUniqueIdPrefix(), instanceRecord.getInstanceId()));
                instance.getOleHoldings().setHoldingsIdentifier(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
                for (int i = 0; i < itemRecords.size(); i++) {
                    instance.getItems().getItem().get(i).setItemIdentifier(DocumentUniqueIDPrefix.getPrefixedId(itemRecords.get(i).getUniqueIdPrefix(), itemRecords.get(i).getItemId()));
                }
                instance.getResourceIdentifier().clear();
                instance.getResourceIdentifier().addAll(prefixedResourceId);
                instanceCollection.getInstance().set(0, instance);
                requestDocument.getContent().setContentObject(instanceCollection);
                requestDocument.getContent().setContent(instanceOlemlRecordProcessor.toXML(instanceCollection));
                requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(instanceRecord.getUniqueIdPrefix(), instanceId));
                buildResponseDocument(responseDocument, holdingsRecord, itemRecords, instanceRecord);
            }
        } catch (Exception e) {
            LOG.error("Exception :" + e);
            Map map = new HashMap();
            map.put("bibId", bibUUid);
            businessObjectService.deleteMatching(BibRecord.class, map);
            buildFailureResponse(requestDocument, responseDocument);
        }

        return null;
    }

    private void buildFailureResponse(RequestDocument requestDocument, ResponseDocument responseDocument) {
        responseDocument.setId(requestDocument.getId());
        responseDocument.setStatus("Ingest Failed");
    }

    private void buildResponseDocument(ResponseDocument responseDocument, HoldingsRecord holdingsRecord, List<ItemRecord> itemRecords, InstanceRecord instanceRecord) {
        responseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(instanceRecord.getUniqueIdPrefix(), instanceRecord.getInstanceId()));
        responseDocument.setCategory(DocCategory.WORK.getCode());
        responseDocument.setType(DocType.INSTANCE.getCode());
        responseDocument.setFormat(DocFormat.OLEML.getCode());
        responseDocument.setStatus("Success");

        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();

        ResponseDocument holdingResponseDocument = new ResponseDocument();
        holdingResponseDocument.setCategory(DocCategory.WORK.getCode());
        holdingResponseDocument.setType(DocType.HOLDINGS.getCode());
        holdingResponseDocument.setFormat(DocFormat.OLEML.getCode());
        holdingResponseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
        responseDocuments.add(holdingResponseDocument);

        for (ItemRecord itemRecord : itemRecords) {
            ResponseDocument itemResponseDocument = new ResponseDocument();
            itemResponseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), itemRecord.getItemId()));
            itemResponseDocument.setCategory(DocCategory.WORK.getCode());
            itemResponseDocument.setType(DocType.ITEM.getCode());
            itemResponseDocument.setFormat(DocFormat.OLEML.getCode());
            responseDocuments.add(itemResponseDocument);
        }
        responseDocument.setLinkedDocuments(responseDocuments);

    }

    private InstanceRecord saveInstancerecord(String bibId, String prefix) {

        InstanceRecord instanceRecord = new InstanceRecord();
        instanceRecord.setBibId(bibId);
        instanceRecord.setUniqueIdPrefix(prefix);
        getBusinessObjectService().save(instanceRecord);
        return instanceRecord;
    }

    /*   private void saveBibInstanceRecord(List<String> resIds, String instanceId) {
            for (String resId : resIds) {
                Map bibMap = new HashMap();
                bibMap.put("bibId", resId);
                List<BibRecord> bibRecords = (List<BibRecord>) getBusinessObjectService().findMatching(BibRecord.class, bibMap);
                if (bibRecords != null && bibRecords.size() > 0) {
                    BibInstanceRecord bibInstanceRecord = new BibInstanceRecord();
                    bibInstanceRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(resId));
                    bibInstanceRecord.setHoldingsId(instanceId);
                    getBusinessObjectService().save(bibInstanceRecord);
                }
            }
        }

       private HoldingsRecord saveHoldingsRecord(OleHoldings oleHoldings, String instanceId, String prefix) {

            HoldingsRecord holdingsRecord = new HoldingsRecord();
            holdingsRecord.setHoldingsId(instanceId);
            holdingsRecord.setUniqueIdPrefix(prefix);
            saveHoldingsRecord(oleHoldings, holdingsRecord);
            return holdingsRecord;
        }

        private HoldingsRecord saveHoldingsRecord(OleHoldings oleHoldings, String instanceId, String prefix, String staffOnlyFlagForHoldings) {

            HoldingsRecord holdingsRecord = new HoldingsRecord();
            holdingsRecord.setHoldingsId(instanceId);
            holdingsRecord.setUniqueIdPrefix(prefix);
            holdingsRecord.setStaffOnlyFlag(Boolean.valueOf(staffOnlyFlagForHoldings));
            saveHoldingsRecord(oleHoldings, holdingsRecord);
            return holdingsRecord;
        }
    */
    private HoldingsRecord saveHoldingsRecord(OleHoldings oleHoldings, String instanceId, String prefix, String staffOnlyFlagForHoldings,AdditionalAttributes additionalAttributes) {

        HoldingsRecord holdingsRecord = new HoldingsRecord();
        //  holdingsRecord.setInstanceId(instanceId);
        holdingsRecord.setUniqueIdPrefix(prefix);
        saveHoldingsRecordAdditonalAttributesForDate(holdingsRecord, additionalAttributes);
        if (staffOnlyFlagForHoldings != null) {
            holdingsRecord.setStaffOnlyFlag(Boolean.valueOf(staffOnlyFlagForHoldings));
        }
        saveHoldingsRecord(oleHoldings, holdingsRecord);
        return holdingsRecord;
    }

    protected void saveHoldingsRecord(OleHoldings oleHoldings, HoldingsRecord holdingsRecord) {

        StringBuffer locationLevel = new StringBuffer("");
        holdingsRecord.setLocation(getLocation(oleHoldings.getLocation(), locationLevel));
        holdingsRecord.setLocationLevel(locationLevel.toString());
        if (oleHoldings.getCallNumber() != null) {
            CallNumber callNumber = oleHoldings.getCallNumber();
            holdingsRecord.setCallNumberPrefix(callNumber.getPrefix());
            holdingsRecord.setCallNumber(callNumber.getNumber());

            if (callNumber.getShelvingOrder() != null) {
                holdingsRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
            }
            CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
            holdingsRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());

        }
        if (oleHoldings.getReceiptStatus() != null) {
            ReceiptStatusRecord receiptStatusRecord = saveReceiptStatusRecord(oleHoldings.getReceiptStatus());
            holdingsRecord.setReceiptStatusId(receiptStatusRecord == null ? null : receiptStatusRecord.getReceiptStatusId());
        }
        holdingsRecord.setContent("mock content");
        holdingsRecord.setExtentOfOwnerShipRecords(null);
        holdingsRecord.setDonorList(null);
      //  holdingsRecord.setAccessUriRecords(null);
        holdingsRecord.setHoldingsNoteRecords(null);
        if (oleHoldings.getCopyNumber() != null) {
            holdingsRecord.setCopyNumber(oleHoldings.getCopyNumber());
        }
        getBusinessObjectService().save(holdingsRecord);
        saveExtentOfOwnerShip(oleHoldings.getExtentOfOwnership(), holdingsRecord.getHoldingsId());
        saveHoldingNoteRecords(oleHoldings.getNote(), holdingsRecord.getHoldingsId());
        saveAccessUriRecord(oleHoldings.getUri(), holdingsRecord.getHoldingsId());
    }


    private List<ItemRecord> saveItemRecords(Items items, String instanceId, String prefix, OleHoldings oleHoldings) throws OleDocStoreException {

        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        for (Item item : items.getItem()) {
            updateShelvingOrder(item, oleHoldings);
            ItemRecord itemRecord = new ItemRecord();
           // itemRecord.setInstanceId(instanceId);
            itemRecord.setUniqueIdPrefix(prefix);
            saveItemRecord(item, itemRecord);
            itemRecords.add(itemRecord);
        }


        return itemRecords;
    }

    protected void saveItemRecord(Item item, ItemRecord itemRecord) {
        itemRecord.setBarCodeArsl(item.getBarcodeARSL());
        if (item.getCallNumber() != null) {

            CallNumber callNumber = item.getCallNumber();
            itemRecord.setCallNumberPrefix(callNumber.getPrefix());
            itemRecord.setCallNumber(callNumber.getNumber());
            if (callNumber.getShelvingOrder() != null) {
                itemRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
            }
            if (callNumber.getShelvingScheme() != null) {
                CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
                itemRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());
            }
        }
        if (item.getAccessInformation() != null) {
            itemRecord.setBarCode(item.getAccessInformation().getBarcode());
        }
        if (item.getItemType() != null) {
            ItemTypeRecord itemTypeRecord = saveItemTypeRecord(item.getItemType());
            itemRecord.setItemTypeId(itemTypeRecord == null ? null : itemTypeRecord.getItemTypeId());
        }
        if (item.getStatisticalSearchingCode() != null) {
            StatisticalSearchRecord statisticalSearchRecord = saveStatisticalSearchRecord(item.getStatisticalSearchingCode());
            itemRecord.setStatisticalSearchId(statisticalSearchRecord == null ? null : statisticalSearchRecord.getStatisticalSearchId());
        }
        if (item.getTemporaryItemType() != null) {
            ItemTypeRecord tempItemTypeRecord = saveItemTypeRecord(item.getTemporaryItemType());
            itemRecord.setTempItemTypeId(tempItemTypeRecord == null ? null : tempItemTypeRecord.getItemTypeId());
        }
        itemRecord.setChronology(item.getChronology());
        itemRecord.setCopyNumber(item.getCopyNumber());
        itemRecord.setEnumeration(item.getEnumeration());

        itemRecord.setNumberOfPieces(item.getNumberOfPieces());
        itemRecord.setDescriptionOfPieces(item.getDescriptionOfPieces());
        itemRecord.setPurchaseOrderItemLineId(item.getPurchaseOrderLineItemIdentifier());
        itemRecord.setVendorLineItemId(item.getVendorLineItemIdentifier());
        itemRecord.setFund(item.getFund());
        itemRecord.setPrice(item.getPrice());
        itemRecord.setCheckInNote(item.getCheckinNote());
        itemRecord.setFastAddFlag(item.isFastAddFlag() ? Boolean.TRUE : Boolean.FALSE);
        itemRecord.setItemDamagedStatus(item.isItemDamagedStatus());
        itemRecord.setMissingPieceFlag(item.isMissingPieceFlag());
        if (item.isMissingPieceFlag() == true) {
            if (item.getMissingPieceEffectiveDate() != null && !item.getMissingPieceEffectiveDate().equalsIgnoreCase("")) {
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date parsedDate = null;
                try {
                    parsedDate = df.parse(item.getMissingPieceEffectiveDate());
                } catch (ParseException e) {
                    LOG.error("Exception : ", e);
                }
                Timestamp timestamp = new Timestamp(parsedDate.getTime());
                itemRecord.setMissingPieceEffectiveDate(timestamp);
            } else {
                Timestamp timestamp = new Timestamp((new Date()).getTime());
                itemRecord.setMissingPieceEffectiveDate(timestamp);
            }
        }
        if(item.getMissingPiecesCount()!=null && !item.getMissingPiecesCount().equalsIgnoreCase("")){
            int missingPieceCount = Integer.parseInt(item.getMissingPiecesCount());
            if (missingPieceCount == 0) {
                item.setMissingPieceFlag(false);
                item.setMissingPiecesCount("");
            } else {
                itemRecord.setMissingPiecesCount(item.getMissingPiecesCount());
            }
        }
        //itemRecord.setEffectiveDate(item.getItemStatusEffectiveDate());


        itemRecord.setClaimsReturnedFlag(item.isClaimsReturnedFlag());

        String claimsReturnDate = item.getClaimsReturnedFlagCreateDate();
        if (claimsReturnDate != null) {
            //itemRecord.setClaimsReturnedFlagCreateDate(new Timestamp(item.getClaimsReturnedFlagCreateDate().toGregorianCalendar().getTimeInMillis()));
            //itemRecord.setClaimsReturnedFlagCreateDate(new Timestamp(item.getClaimsReturnedFlagCreateDate()));
            String[] claimsReturnDateArray = claimsReturnDate.split(" ");
            if (claimsReturnDateArray.length == 1 && claimsReturnDateArray[0] != "") {
                claimsReturnDate = claimsReturnDate + " 00:00:00";
                claimsReturnsCreateDateItem(item, itemRecord, claimsReturnDate);
            } else if (claimsReturnDateArray.length > 1) {
                claimsReturnsCreateDateItem(item, itemRecord, claimsReturnDate);
            } else {
                itemRecord.setClaimsReturnedFlagCreateDate(null);
            }
        } else {
            itemRecord.setClaimsReturnedFlagCreateDate(null);
        }

        String dueDateItem = item.getDueDateTime();
        if (dueDateItem != null) {
            //Timestamp timestamp = new Timestamp(item.getDueDateTime().toGregorianCalendar().getTimeInMillis());
            //itemRecord.setDueDateTime(timestamp);
            String[] dueDateItemArray = dueDateItem.split(" ");
            if (dueDateItemArray.length == 1 && dueDateItemArray[0] != "") {
                dueDateItem = dueDateItem + " 00:00:00";
                dueDateTime(item, itemRecord, dueDateItem);
            } else if (dueDateItemArray.length > 1) {
                dueDateTime(item, itemRecord, dueDateItem);
            } else {
                itemRecord.setDueDateTime(null);
            }
        } else {
            itemRecord.setDueDateTime(null);
        }

        itemRecord.setClaimsReturnedNote(item.getClaimsReturnedNote());
        itemRecord.setProxyBorrower(item.getProxyBorrower());
        itemRecord.setCurrentBorrower(item.getCurrentBorrower());

        itemRecord.setFastAddFlag(item.isFastAddFlag());
        String effectiveDateForItem = item.getItemStatusEffectiveDate();
        if (effectiveDateForItem != null) {
            String[] effectiveDateForItemArray = effectiveDateForItem.split(" ");
            if (effectiveDateForItemArray.length == 1 && effectiveDateForItemArray[0] != "") {
                effectiveDateForItem = effectiveDateForItem + " 00:00:00";
                effectiveDateItem(item, itemRecord, effectiveDateForItem);
            } else if (effectiveDateForItemArray.length > 1) {
                effectiveDateItem(item, itemRecord, effectiveDateForItem);
            } else {
                itemRecord.setEffectiveDate(null);
            }
        } else {
            itemRecord.setEffectiveDate(null);
        }


        // itemRecord.setStaffOnly(item.isStaffOnlyFlag() ? "true" : "false");
        String staffOnlyFlagForItem = null;
        if (item.getExtension() != null && item.getExtension().getContent().size() > 0 && item.getExtension().getContent().get(0) != null) {
            AdditionalAttributes additionalAttributes = (AdditionalAttributes) item.getExtension().getContent().get(0);
            if (additionalAttributes.getAttributeMap() != null) {
                staffOnlyFlagForItem = additionalAttributes.getAttributeMap().get("staffOnlyFlag");
                if (staffOnlyFlagForItem != null) {
                    itemRecord.setStaffOnlyFlag(Boolean.valueOf(staffOnlyFlagForItem));
                }
            }
        } else {
            itemRecord.setStaffOnlyFlag(false);
        }
        if (item.getItemStatus() != null) {
            ItemStatusRecord itemStatusRecord = saveItemStatusRecord(item.getItemStatus().getCodeValue());
            itemRecord.setItemStatusId(itemStatusRecord == null ? null : itemStatusRecord.getItemStatusId());
        }
        StringBuffer locationLevel = new StringBuffer("");
        itemRecord.setLocation(getLocation(item.getLocation(), locationLevel));
        itemRecord.setLocationLevel(locationLevel.toString());
        itemRecord.setFormerIdentifierRecords(null);
        itemRecord.setLocationsCheckinCountRecords(null);
        itemRecord.setItemNoteRecords(null);
        if(item.getDamagedItemNote()!=null){
            itemRecord.setDamagedItemNote(item.getDamagedItemNote());
        }
        if(item.getMissingPieceFlagNote()!=null){
            itemRecord.setMissingPieceFlagNote(item.getMissingPieceFlagNote());
        }
        if (item.getAccessInformation() != null && item.getAccessInformation().getUri() != null) {
            itemRecord.setUri(item.getAccessInformation().getUri().getValue());
        }
        getBusinessObjectService().save(itemRecord);

        if (item.getFormerIdentifier() != null && item.getFormerIdentifier().size() > 0 && item.getFormerIdentifier().get(0).getIdentifier() != null) {
            saveFormerIdentifierRecords(item.getFormerIdentifier(), itemRecord.getItemId());
        }
        if (item.getNote() != null && item.getNote().size() > 0) {
            saveItemNoteRecord(item.getNote(), itemRecord.getItemId());
        }
        if (item.getNumberOfCirculations() != null && item.getNumberOfCirculations().getCheckInLocation() != null && item.getNumberOfCirculations().getCheckInLocation().size() > 0) {
            saveCheckInLocationRecord(item.getNumberOfCirculations().getCheckInLocation(), itemRecord.getItemId());
        }
        if (item.getDonorInfo() != null && item.getDonorInfo().size() >= 0) {
            saveDonorList(item.getDonorInfo(), itemRecord.getItemId());
        }
        if(item.getMissingPieceItemRecordList() != null && item.getMissingPieceItemRecordList().size()>0){
            saveMissingPieceItemList(item.getMissingPieceItemRecordList() , itemRecord.getItemId());
        }


    }


    @Override
    public void deleteDocs(RequestDocument requestDocument, Object object) {
        ResponseDocument responseDocument = new ResponseDocument();
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        InstanceRecord instanceRecord = new InstanceRecord();
        Map instanceMap = new HashMap();
        instanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<InstanceRecord> instanceRecords = (List<InstanceRecord>) businessObjectService.findMatching(InstanceRecord.class, instanceMap);
        if (instanceRecords != null && instanceRecords.size() > 0) {
            instanceRecord = instanceRecords.get(0);
            if (instanceRecord.getHoldingsRecords() != null && instanceRecord.getHoldingsRecords().size() > 0) {
                HoldingsRecord holdingsRecord = instanceRecord.getHoldingsRecords().get(0);

                if (holdingsRecord.getExtentOfOwnerShipRecords() != null && holdingsRecord.getExtentOfOwnerShipRecords().size() > 0) {
                    List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords = holdingsRecord.getExtentOfOwnerShipRecords();
                    for (int i = 0; i < extentOfOwnerShipRecords.size(); i++) {
                        List<ExtentNoteRecord> extentNoteRecords = extentOfOwnerShipRecords.get(i).getExtentNoteRecords();
                        if (extentNoteRecords != null && extentNoteRecords.size() > 0) {
                            businessObjectService.delete(extentNoteRecords);
                        }
                    }
                    businessObjectService.delete(extentOfOwnerShipRecords);
                }


                if (holdingsRecord.getHoldingsNoteRecords() != null && holdingsRecord.getHoldingsNoteRecords().size() > 0) {
                    List<HoldingsNoteRecord> holdingsNoteRecords = holdingsRecord.getHoldingsNoteRecords();
                    businessObjectService.delete(holdingsNoteRecords);
                }

               /* if (holdingsRecord.getAccessUriRecords() != null && holdingsRecord.getAccessUriRecords().size() > 0) {
                    List<AccessUriRecord> accessUriRecords = holdingsRecord.getAccessUriRecords();
                    businessObjectService.delete(accessUriRecords);
                }*/
                holdingsRecord.setCallNumberTypeId(null);
                holdingsRecord.setReceiptStatusId(null);

                businessObjectService.delete(holdingsRecord);
            }

            List<ItemRecord> itemRecords = instanceRecord.getItemRecords();
            for (ItemRecord itemRecord : itemRecords) {

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
                if (itemRecord.getDonorList() != null && itemRecord.getDonorList().size() > 0) {
                    List<OLEItemDonorRecord> donorList = itemRecord.getDonorList();
                    businessObjectService.delete(donorList);
                }
                itemRecord.setItemStatusId(null);
                itemRecord.setItemTypeId(null);
                itemRecord.setTempItemTypeId(null);
                itemRecord.setStatisticalSearchId(null);
                businessObjectService.delete(itemRecord);

            }
            businessObjectService.delete(instanceRecord);

        }
        buildResponseDocument(requestDocument, instanceRecord, responseDocument);

    }

    @Override
    public ResponseDocument checkoutContent(RequestDocument requestDocument, Object object) {
        ResponseDocument respDoc = new ResponseDocument();
        Map instanceMap = new HashMap();
        instanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<InstanceRecord> instanceRecords = (List<InstanceRecord>) getBusinessObjectService().findMatching(InstanceRecord.class, instanceMap);
        if(instanceRecords != null && instanceRecords.size() > 0) {
            InstanceRecord instanceRecord =  instanceRecords.get(0);
            String content = buildInstanceContent(instanceRecord);
            if (content != null && instanceRecord.getHoldingsRecords()!=null && instanceRecord.getHoldingsRecords().size() > 0) {
                HoldingsRecord holdingsRecord = instanceRecord.getHoldingsRecords().get(0);
                AdditionalAttributes additionalAttributes = new AdditionalAttributes();
                additionalAttributes.setAttribute(AdditionalAttributes.STAFFONLYFLAG, holdingsRecord.getStaffOnlyFlag().toString());
                Content contentObj = new Content();
                contentObj.setContent(content);
                respDoc.setUuid(requestDocument.getUuid());
                respDoc.setCategory(requestDocument.getCategory());
                respDoc.setType(requestDocument.getType());
                respDoc.setFormat(requestDocument.getFormat());
                respDoc.setContent(contentObj);
                respDoc.setAdditionalAttributes(additionalAttributes);
            }
        } else {
            respDoc.setStatus("Failed");
            respDoc.setStatusMessage("Instance does not exist.");
        }
        return respDoc;
    }

    @Override
    public void checkInContent(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException {
        //In case of adding a new Item
        String instanceId = requestDocument.getId();
        if (requestDocument.getLinkedRequestDocuments() != null) {
            for (RequestDocument linkedRequestDocument : requestDocument.getLinkedRequestDocuments()) {
                ItemOlemlRecordProcessor recordProcessor = new ItemOlemlRecordProcessor();
                Item item = recordProcessor.fromXML(linkedRequestDocument.getContent().getContent());
                HoldingsRecord holdingsRecord = null;
                Map instanceMap = new HashMap();
                instanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(instanceId));
                List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord.class, instanceMap);
                if (holdingsRecords != null && holdingsRecords.size() > 0) {
                    holdingsRecord = holdingsRecords.get(0);
                }
                OleHoldings oleHoldings = buildHoldingsContent(holdingsRecord);
                buildShelvingOrderForItem(item, oleHoldings);
                ItemRecord itemRecord = new ItemRecord();
              //  itemRecord.setInstanceId(DocumentUniqueIDPrefix.getDocumentId(instanceId));
                itemRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.getPrefix(linkedRequestDocument.getCategory(), linkedRequestDocument.getType(), linkedRequestDocument.getFormat()));
                if (requestDocument.getAdditionalAttributes() != null) {
                    AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
                    String staffOnlyFlagForHoldings = additionalAttributes.getAttribute(AdditionalAttributes.STAFFONLYFLAG);
                    if (staffOnlyFlagForHoldings != null) {
                        itemRecord.setStaffOnlyFlag(Boolean.valueOf(staffOnlyFlagForHoldings));
                    }
                }

                saveItemRecord(item, itemRecord);
                // getBusinessObjectService().save(itemRecord);
                linkedRequestDocument.setId(DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), itemRecord.getItemId()));
                buildResponseWithLinkedDocument(requestDocument, responseDocument);
            }

        }
    }

    private void buildShelvingOrderForItem(Item item, OleHoldings oleHoldings) throws OleDocStoreException {
        if (item != null) {
            if (item.getCallNumber() == null) {
                item.setCallNumber(new CallNumber());
            }
            updateShelvingOrder(item, oleHoldings);
        }
    }

    private String buildInstanceContent(InstanceRecord instanceRecord) {
        String content = null;
        if (instanceRecord != null) {
            Instance instance = new Instance();
            List<Instance> instanceList = new ArrayList<Instance>();
            InstanceCollection instanceCollection = new InstanceCollection();
            Items items = new Items();
            List<Item> itemList = new ArrayList<Item>();
            instance.setInstanceIdentifier(DocumentUniqueIDPrefix.getPrefixedId(instanceRecord.getUniqueIdPrefix(), instanceRecord.getInstanceId()));
            /*Map instanceMap = new HashMap();
            instanceMap.put("instanceId", instanceRecord.getHoldingsId());
            List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord.class, instanceMap);*/
            List<HoldingsRecord> holdingsRecords = instanceRecord.getHoldingsRecords();
            if(holdingsRecords!=null && holdingsRecords.size()>0) {
                OleHoldings oleHoldings = buildHoldingsContent(holdingsRecords.get(0));
                instance.setOleHoldings(oleHoldings);
            }
            //List<ItemRecord> oleItemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, instanceMap);
            List<ItemRecord> oleItemRecords = instanceRecord.getItemRecords();
            instanceRecord.setItemRecords(oleItemRecords);
            for (ItemRecord itemRecord : oleItemRecords) {
                Item item = buildItemContent(itemRecord);
                itemList.add(item);
            }
            items.setItem(itemList);
            instance.setItems(items);
            instanceList.add(instance);
            if(StringUtils.isNotEmpty(instanceRecord.getBibId())) {
                instance.getResourceIdentifier().add(DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, instanceRecord.getBibId()));
            }
            instanceCollection.setInstance(instanceList);
            content = new InstanceOlemlRecordProcessor().toXML(instanceCollection);
        }
        return content;
    }

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument, InstanceRecord instanceRecord, ResponseDocument responseDocument) {
        responseDocument.setId(instanceRecord.getInstanceId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(instanceRecord.getUniqueIdPrefix(),instanceRecord.getInstanceId()));
        responseDocument.setId(instanceRecord.getInstanceId());
        return responseDocument;
    }

    @Override
    public ResponseDocument bind(RequestDocument requestDocument, Object object, String operation)
            throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        BusinessObjectService businessObjectService = (BusinessObjectService) object;
        if (operation.equalsIgnoreCase(Request.Operation.bind.toString())) {
            updateInstanceData(requestDocument, businessObjectService);
        }
        return buildResponseForBind(requestDocument);
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> fieldValues) throws OleDocStoreException {
        businessObjectService = (BusinessObjectService) object;
        String content = requestDocument.getContent().getContent();
        if (content == null) {
            if (requestDocument.getLinkedRequestDocuments().size() > 0) {
                List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
                for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
                    if (linkedRequestDocument.getType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                        RdbmsWorkItemDocumentManager.getInstance().validateNewItem(linkedRequestDocument, businessObjectService, fieldValues, DocumentUniqueIDPrefix.getDocumentId(
                                requestDocument.getId()));
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
                    RdbmsWorkHoldingsDocumentManager.getInstance().validateHoldings(oleHoldings);
                }
                if (instance.getItems() != null) {
                    Items items = instance.getItems();
                    List<Item> itemList = items.getItem();
                    if (itemList.size() > 0) {
                        for (Item item : itemList) {
                            RdbmsWorkItemDocumentManager.getInstance().itemBarcodeValidation(item, fieldValues, null);
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
            if ((callNumberType == null) || (callNumberType.length() == 0) || (callNumberType.equals("NOINFO"))) {
                throw new OleDocStoreException("Please enter valid call number type value in call number information ");
            }
        }
    }

    private void validateShelvingOrderNCNum(CallNumber cNum) throws OleDocStoreException {
        if (cNum.getShelvingOrder() != null && cNum.getShelvingOrder().getFullValue() != null &&
                cNum.getShelvingOrder().getFullValue().trim().length() > 0) {
            if (!(cNum.getNumber() != null && cNum.getNumber().length() > 0)) {
                throw new OleDocStoreException("Shelving order value is available, so please enter call number information");
            }
        }
    }

    public void validateCallNumber(CallNumber cNum) throws OleDocStoreException {
        validateCNumNCNumType(cNum);
        validateShelvingOrderNCNum(cNum);
    }


    private void updateInstanceData(RequestDocument requestDocument, BusinessObjectService businessObjectService) {
        Map instanceMap = new HashMap();
        instanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        InstanceRecord instanceRecord = businessObjectService.findByPrimaryKey(InstanceRecord.class, instanceMap);
        if (instanceRecord != null) {
            BibInstanceRecord bibInstanceRecord = new BibInstanceRecord();
            bibInstanceRecord.setBibId(instanceRecord.getBibId());
            bibInstanceRecord.setInstanceId(instanceRecord.getInstanceId());
            businessObjectService.save(bibInstanceRecord);
        }
        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
        for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
            BibInstanceRecord bibInstanceRecord = new BibInstanceRecord();
            bibInstanceRecord.setInstanceId(DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
            bibInstanceRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(linkedRequestDocument.getUuid()));
            businessObjectService.save(bibInstanceRecord);
        }
    }

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

    private void buildResponseWithLinkedDocument(RequestDocument requestDocument, ResponseDocument responseDocument) {
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(requestDocument.getUuid());
        responseDocument.setStatus("Success");
        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
        List<ResponseDocument> linkedResponseDocumentsList = new ArrayList<ResponseDocument>();
        for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {

            ResponseDocument linkedResponseDocument = new ResponseDocument();
            linkedResponseDocument.setCategory(linkedRequestDocument.getCategory());
            linkedResponseDocument.setType(linkedRequestDocument.getType());
            linkedResponseDocument.setFormat(linkedRequestDocument.getFormat());
            linkedResponseDocument.setId(linkedRequestDocument.getId());
            linkedResponseDocument.setUuid(linkedRequestDocument.getId());
            linkedRequestDocument.setUser(linkedRequestDocument.getUuid());
            linkedResponseDocument.setStatus("Success");
            linkedResponseDocumentsList.add(linkedResponseDocument);

        }
        responseDocument.setLinkedDocuments(linkedResponseDocumentsList);
    }


    public void transferInstances(List<RequestDocument> requestDocuments, BusinessObjectService businessObjectService)
            throws Exception {
        LOG.debug("RdbmsWorkInstanceDocumentManager transferInstances");
        Collection<InstanceRecord> instanceRecords = null;
        String desBibIdentifier = requestDocuments.get(requestDocuments.size() - 1).getUuid();
        LOG.debug("RdbmsWorkInstanceDocumentManager transferInstances desBibIdentifier " + desBibIdentifier);
        Map instanceMap = new HashMap();
        Map bibInstanceMap = new HashMap();
        for (int i = 0; i < requestDocuments.size() - 1; i++) {
            RequestDocument requestDocument = requestDocuments.get(i);
            instanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
            bibInstanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
            List<BibInstanceRecord> bibInstanceRecordList = (List<BibInstanceRecord>) businessObjectService
                    .findMatching(BibInstanceRecord.class, bibInstanceMap);
            if (bibInstanceRecordList.size() > 1) {
                //Instances are associated with multiple bibs means it has bound with with other bib. So we cant transfer. So throw exception
                LOG.error(requestDocument.getUuid() + " is bounded with other bib and cant be transferred");
                throw new Exception(requestDocument.getUuid() + " is bounded with other bib and cant be transferred");
            }
            //else {
            //    BibInstanceRecord bibInstanceRecord = bibInstanceRecordList.get(0);
            //    bibInstanceRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(desBibIdentifier));
            //    businessObjectService.save(bibInstanceRecord);
            //}
            InstanceRecord instanceRecord = businessObjectService.findByPrimaryKey(InstanceRecord.class, instanceMap);
            instanceRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(desBibIdentifier));
            businessObjectService.save(instanceRecord);
        }
    }

    public void transferItems(List<RequestDocument> requestDocuments, BusinessObjectService businessObjectService)
            throws Exception {
        String destInstanceIdentifier = requestDocuments.get(requestDocuments.size() - 1).getUuid();
        Map itemMap = new HashMap();
        for (int i = 0; i < requestDocuments.size() - 1; i++) {
            RequestDocument requestDocument = requestDocuments.get(i);
            itemMap.put("ITEM_ID", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
            ItemRecord itemRecord = businessObjectService.findByPrimaryKey(ItemRecord.class, itemMap);
          //  itemRecord.setInstanceId(DocumentUniqueIDPrefix.getDocumentId(destInstanceIdentifier));
            businessObjectService.save(itemRecord);
        }
    }

    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        // List<String> bibIdentifierList = new ArrayList<String>();
        businessObjectService = (BusinessObjectService) object;
        List<String> bibIdentifierList = new ArrayList<String>();
        ResponseDocument responseDocument = new ResponseDocument();
        Map instanceMap = new HashMap();
        instanceMap.put("instanceId", DocumentUniqueIDPrefix.getDocumentId(requestDocument.getUuid()));
        List<BibInstanceRecord> bibInstanceRecords = (List<BibInstanceRecord>) businessObjectService
                .findMatching(BibInstanceRecord.class, instanceMap);
        if (bibInstanceRecords.size() > 0) {
            for (BibInstanceRecord bibInstanceRecord : bibInstanceRecords) {
                bibIdentifierList.add(bibInstanceRecord.getBibId());
            }


            if (bibIdentifierList.size() > 1) {
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setStatus("failure'");
                responseDocument.setStatusMessage("Instance is bound with more than one bib. So deletion cannot be done");
                return responseDocument;
            }
            boolean exists = checkInstancesOrItemsExistsInOLE(requestDocument.getUuid(), object);
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
            String bibIdentifierValue = bibIdentifierList.get(0);
            Map bibMap = new HashMap();
            bibMap.put("bibId", bibIdentifierValue);
            bibInstanceRecords = (List<BibInstanceRecord>) businessObjectService
                    .findMatching(BibInstanceRecord.class, bibMap);
            List<String> instanceIdentifierList = new ArrayList<>();
            for (BibInstanceRecord bibInstanceRecord : bibInstanceRecords) {
                instanceIdentifierList.add(bibInstanceRecord.getInstanceId());
            }
            if (instanceIdentifierList.size() == 1) {
                String prefix = DocumentUniqueIDPrefix.getPrefix(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode());
                requestDocument.setUuid(DocumentUniqueIDPrefix.getPrefixedId(prefix, bibIdentifierValue));
                requestDocument.setCategory(DocCategory.WORK.getCode());
                requestDocument.setType(DocType.BIB.getDescription());
                requestDocument.setFormat(DocFormat.MARC.getCode());
                responseDocument.setStatus("success");
                responseDocument.setStatusMessage("success");
                responseDocument = RdbmsWorkBibDocumentManager.getInstance().deleteVerify(requestDocument, businessObjectService);
            } else {
                responseDocument.setUuid(requestDocument.getUuid());
                responseDocument.setId(requestDocument.getId());
                responseDocument.setCategory(requestDocument.getCategory());
                responseDocument.setType(requestDocument.getType());
                responseDocument.setFormat(requestDocument.getFormat());
                responseDocument.setStatus("success");
                responseDocument.setStatusMessage("success");
            }
        } else {
            responseDocument.setUuid(requestDocument.getUuid());
            responseDocument.setId(requestDocument.getId());
            responseDocument.setCategory(requestDocument.getCategory());
            responseDocument.setType(requestDocument.getType());
            responseDocument.setFormat(requestDocument.getFormat());
            responseDocument.setStatus("success");
            responseDocument.setStatusMessage("success");
        }

        return responseDocument;

    }


    protected CallNumberTypeRecord saveCallNumberTypeRecord(ShelvingScheme scheme) {

        Map callMap = new HashMap();
        callMap.put("code", scheme.getCodeValue());
        List<CallNumberTypeRecord> callNumberTypeRecords = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, callMap);
        if (callNumberTypeRecords.size() == 0) {
            if (scheme.getCodeValue() != null && !"".equals(scheme.getCodeValue())) {
                CallNumberTypeRecord callNumberTypeRecord = new CallNumberTypeRecord();
                callNumberTypeRecord.setCode(scheme.getCodeValue());
                callNumberTypeRecord.setName(scheme.getFullValue());
                getBusinessObjectService().save(callNumberTypeRecord);
                return callNumberTypeRecord;
            } else
                return null;
        }
        return callNumberTypeRecords.get(0);
    }

    protected String getLocation(Location location, StringBuffer locationLevel) {
        StringBuffer locationName = new StringBuffer("");
        //StringBuffer locationLevel = new StringBuffer("");
        if (location != null && location.getLocationLevel() != null) {
            locationName = locationName.append(location.getLocationLevel().getName());
            locationLevel = locationLevel.append(location.getLocationLevel().getLevel());

            if (location.getLocationLevel().getLocationLevel() != null) {
                locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getName());
                locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLevel());

                if (location.getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                    locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getName());
                    locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                    if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                        locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                        locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                        if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                            locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                            locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());
                        }
                    }
                }
            }
        }
        return locationName.toString();
    }


    protected void saveExtentNoteRecord(String extOfOwnerShipID, List<Note> notes) {
        Map map = new HashMap();
        map.put("extOfOwnerShipID", extOfOwnerShipID);
        List<ExtentNoteRecord> extentNoteRecords = (List<ExtentNoteRecord>) getBusinessObjectService().findMatching(ExtentNoteRecord.class, map);
        if (notes != null && notes.size() > 0) {
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                    ExtentNoteRecord noteRecord = new ExtentNoteRecord();
                    if (i < extentNoteRecords.size()) {
                        noteRecord = extentNoteRecords.get(i);
                    }

                //    noteRecord.setExtOfOwnerShipID(extOfOwnerShipID);
                    noteRecord.setType(note.getType());
                    noteRecord.setNote(note.getValue());
                    getBusinessObjectService().save(noteRecord);
                }
            }

            if (extentNoteRecords.size() > notes.size()) {
                getBusinessObjectService().delete(extentNoteRecords.subList(notes.size() - 1, extentNoteRecords.size()));
            }
        }

    }

    protected void saveExtentOfOwnerShip(List<ExtentOfOwnership> extentOfOwnershipList, String holdingsId) {

        Map map = new HashMap();
        map.put("holdingsId", holdingsId);
        List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords = (List<ExtentOfOwnerShipRecord>) getBusinessObjectService().findMatching(ExtentOfOwnerShipRecord.class, map);
        if (extentOfOwnershipList != null && extentOfOwnershipList.size() > 0) {
            for (int i = 0; i < extentOfOwnershipList.size(); i++) {
                ExtentOfOwnerShipRecord extentOfOwnerShipRecord = new ExtentOfOwnerShipRecord();
                ExtentOfOwnership extentOfOwnership = extentOfOwnershipList.get(i);
                if (i < extentOfOwnerShipRecords.size()) {
                    extentOfOwnerShipRecord = extentOfOwnerShipRecords.get(i);
                }
                ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord = saveExtentOfOwnerShipType(extentOfOwnership.getType());
                extentOfOwnerShipRecord.setExtOfOwnerShipTypeId(extentOfOwnerShipTypeRecord != null ? extentOfOwnerShipTypeRecord.getExtOfOwnerShipTypeId() : null);
                extentOfOwnerShipRecord.setText(extentOfOwnership.getTextualHoldings());
                extentOfOwnerShipRecord.setHoldingsId(holdingsId);
                getBusinessObjectService().save(extentOfOwnerShipRecord);
                List<Note> notes = extentOfOwnership.getNote();
                /*if (notes.size() > 0) {
                    saveExtentNoteRecord(extentOfOwnerShipRecord.getExtOfOwnerShipID(), notes);
                }*/
            }

            if (extentOfOwnershipList.size() < extentOfOwnerShipRecords.size()) {
                getBusinessObjectService().delete(extentOfOwnerShipRecords.subList(extentOfOwnershipList.size() - 1, extentOfOwnerShipRecords.size()));

            }
        }

    }

    protected ExtentOfOwnerShipTypeRecord saveExtentOfOwnerShipType(String type) {
        Map map = new HashMap();
        map.put("code", type);
        List<ExtentOfOwnerShipTypeRecord> extentOfOwnerShipTypeRecords = (List<ExtentOfOwnerShipTypeRecord>) getBusinessObjectService().findMatching(ExtentOfOwnerShipTypeRecord.class, map);
        if (extentOfOwnerShipTypeRecords.size() == 0) {
            if (type != null && !"".equals(type)) {
                ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord = new ExtentOfOwnerShipTypeRecord();
                extentOfOwnerShipTypeRecord.setCode(type);
                extentOfOwnerShipTypeRecord.setName(type);
                getBusinessObjectService().save(extentOfOwnerShipTypeRecord);
                return extentOfOwnerShipTypeRecord;
            } else {
                return null;
            }
        }
        return extentOfOwnerShipTypeRecords.get(0);
    }


/*    protected HoldingsExtendInfoRecord saveExtensionRecord(OleHoldings oleHoldings) {

        HoldingsExtendInfoRecord holdingsExtendInfoRecord = new HoldingsExtendInfoRecord();
        ReceiptStatusRecord receiptStatusRecord = saveReceiptStatusRecord(oleHoldings.getReceiptStatus());
        holdingsExtendInfoRecord.setReceiptStatusId(receiptStatusRecord == null ? null : receiptStatusRecord.getReceiptStatusId());
        getBusinessObjectService().save(holdingsExtendInfoRecord);
        List<Uri> uriList = oleHoldings.getUri();
        if (uriList.size() > 0) {
            List<AccessUriRecord> accessInfoRecords = new ArrayList<AccessUriRecord>();
            for (Uri uri : uriList) {
                AccessUriRecord accessInfoRecord = new AccessUriRecord();
                accessInfoRecord.setText(uri.getValue());
                accessInfoRecord.setHoldingsExtendInfoId(holdingsExtendInfoRecord.getExtendInfoId());
                accessInfoRecords.add(accessInfoRecord);
            }
            if (accessInfoRecords.size() > 0)
                getBusinessObjectService().save(accessInfoRecords);
        }
        return holdingsExtendInfoRecord;
    }*/


    protected void saveAccessUriRecord(List<Uri> uriList, String holdingsId) {

        Map map = new HashMap();
        map.put("holdingsId", holdingsId);
       /* List<AccessUriRecord> accessUriRecordList = (List<AccessUriRecord>) getBusinessObjectService().findMatching(AccessUriRecord.class, map);
        if (uriList.size() > 0) {
            List<AccessUriRecord> accessUriRecords = new ArrayList<AccessUriRecord>();
            for (int i = 0; i < uriList.size(); i++) {
                AccessUriRecord accessUriRecord = new AccessUriRecord();
                if (i < accessUriRecordList.size()) {
                    accessUriRecord = accessUriRecordList.get(i);
                }
                Uri uri = uriList.get(i);
                accessUriRecord.setText(uri.getValue());
                accessUriRecord.setHoldingsId(holdingsId);
                accessUriRecords.add(accessUriRecord);
            }

            if (accessUriRecords.size() > 0)
                getBusinessObjectService().save(accessUriRecords);

            if (uriList.size() < accessUriRecordList.size()) {
                getBusinessObjectService().delete(accessUriRecordList.subList(uriList.size() - 1, accessUriRecordList.size()));
            }
        }*/
    }


    protected ReceiptStatusRecord saveReceiptStatusRecord(String receiptStatus) {
        Map map = new HashMap();
        map.put("code", receiptStatus);
        List<ReceiptStatusRecord> receiptStatusRecords = (List<ReceiptStatusRecord>) getBusinessObjectService().findMatching(ReceiptStatusRecord.class, map);
        if (receiptStatusRecords.size() == 0) {
            if (receiptStatus != null && !"".equals(receiptStatus)) {
                ReceiptStatusRecord receiptStatusRecord = new ReceiptStatusRecord();
                receiptStatusRecord.setCode(receiptStatus);
                receiptStatusRecord.setName(receiptStatus);
                getBusinessObjectService().save(receiptStatusRecord);
                return receiptStatusRecord;
            } else {
                return null;
            }
        }
        return receiptStatusRecords.get(0);
    }

    protected void saveHoldingNoteRecords(List<Note> noteList, String holdingsId) {

        Map map = new HashMap();
        map.put("holdingsId", holdingsId);
        List<HoldingsNoteRecord> holdingsNoteRecordList = (List<HoldingsNoteRecord>) getBusinessObjectService().findMatching(HoldingsNoteRecord.class, map);
        if (noteList.size() > 0) {
            List<HoldingsNoteRecord> holdingsNoteRecords = new ArrayList<HoldingsNoteRecord>();
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                    HoldingsNoteRecord holdingsNoteRecord = new HoldingsNoteRecord();
                    if (i < holdingsNoteRecordList.size()) {
                        holdingsNoteRecord = holdingsNoteRecordList.get(i);
                    }
                    holdingsNoteRecord.setType(note.getType());
                    holdingsNoteRecord.setNote(note.getValue());
                    holdingsNoteRecord.setHoldingsId(holdingsId);
                    holdingsNoteRecords.add(holdingsNoteRecord);
                }
                if (holdingsNoteRecords.size() > 0) {
                    getBusinessObjectService().save(holdingsNoteRecords);
                }
            }

            if (noteList.size() < holdingsNoteRecordList.size()) {
                getBusinessObjectService().delete(holdingsNoteRecordList.subList(noteList.size() - 1, holdingsNoteRecordList.size()));
            }
        }
    }


    protected ItemTypeRecord saveItemTypeRecord(ItemType itemType) {
        Map map = new HashMap();
        map.put("code", itemType.getCodeValue());
        List<ItemTypeRecord> itemTypeRecords = (List<ItemTypeRecord>) getBusinessObjectService().findMatching(ItemTypeRecord.class, map);
        if (itemTypeRecords.size() == 0) {
            if (itemType.getCodeValue() != null && !"".equals(itemType.getCodeValue())) {
                ItemTypeRecord itemTypeRecord = new ItemTypeRecord();
                itemTypeRecord.setCode(itemType.getCodeValue());
                itemTypeRecord.setName(itemType.getFullValue());
                getBusinessObjectService().save(itemTypeRecord);
                return itemTypeRecord;
            } else {
                return null;
            }
        }
        return itemTypeRecords.get(0);

    }

/*
    protected void updateItemTypeRecord(ItemType itemType, ItemTypeRecord itemTypeRecord) {

        itemTypeRecord.setCode(itemType.getCodeValue());
        itemTypeRecord.setName(itemType.getFullValue());
        getBusinessObjectService().save(itemTypeRecord);
    }


    protected void updateStatisticalSearchRecord(List<StatisticalSearchingCode> statisticalSearchingCodes, StatisticalSearchRecord statisticalSearchRecord) {

        statisticalSearchRecord.setCode(statisticalSearchingCodes.get(0).getCodeValue());
        statisticalSearchRecord.setName(statisticalSearchingCodes.get(0).getFullValue());
        getBusinessObjectService().save(statisticalSearchRecord);

    }*/

    protected StatisticalSearchRecord saveStatisticalSearchRecord(List<StatisticalSearchingCode> statisticalSearchingCodes) {

        if (statisticalSearchingCodes.size() > 0) {
            Map map = new HashMap();
            map.put("code", statisticalSearchingCodes.get(0).getCodeValue());
            List<StatisticalSearchRecord> statisticalSearchRecords = (List<StatisticalSearchRecord>) getBusinessObjectService().findMatching(StatisticalSearchRecord.class, map);
            if (statisticalSearchRecords.size() == 0) {
                if (statisticalSearchingCodes.get(0).getCodeValue() != null && !"".equals(statisticalSearchingCodes.get(0).getCodeValue())) {
                    StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchRecord();
                    statisticalSearchRecord.setCode(statisticalSearchingCodes.get(0).getCodeValue());
                    statisticalSearchRecord.setName(statisticalSearchingCodes.get(0).getFullValue());
                    getBusinessObjectService().save(statisticalSearchRecord);
                    return statisticalSearchRecord;
                } else {
                    return null;
                }
            }
            return statisticalSearchRecords.get(0);
        }
        return null;
    }


    /* protected void updateTempItemTypeRecord(ItemType itemType, ItemTypeRecord tempItemTypeRecord) {
      tempItemTypeRecord.setCode(itemType.getCodeValue());
      tempItemTypeRecord.setName(itemType.getFullValue());
      getBusinessObjectService().save(tempItemTypeRecord);
  }


  protected TempItemTypeRecord saveTempItemTypeRecord(ItemType itemType) {
      Map map = new HashMap();
      map.put("code", itemType.getCodeValue());
      List<TempItemTypeRecord> tempItemTypeRecords = (List<TempItemTypeRecord>) getBusinessObjectService().findMatching(TempItemTypeRecord.class, map);
      if (tempItemTypeRecords.size() == 0) {
          if (itemType.getCodeValue() != null && !"".equals(itemType.getCodeValue())) {
              TempItemTypeRecord itemTypeRecord = new TempItemTypeRecord();
              itemTypeRecord.setCode(itemType.getCodeValue());
              itemTypeRecord.setDescription(itemType.getFullValue());
              getBusinessObjectService().save(itemTypeRecord);
              return itemTypeRecord;
          } else {
              return null;
          }
      }
      return tempItemTypeRecords.get(0);
  }


  protected void updateItemStatusRecord(String itemStatus, ItemStatusRecord itemStatusRecord) {

      itemStatusRecord.setCode(itemStatus);
      itemStatusRecord.setName(itemStatus);
      getBusinessObjectService().save(itemStatusRecord);

  }  */


    protected ItemStatusRecord saveItemStatusRecord(String itemStatus) {
        Map map = new HashMap();
        map.put("code", itemStatus);
        List<ItemStatusRecord> itemStatusRecords = (List<ItemStatusRecord>) getBusinessObjectService().findMatching(ItemStatusRecord.class, map);
        if (itemStatusRecords.size() == 0) {
            if (itemStatus != null && !"".equals(itemStatus)) {
                ItemStatusRecord itemStatusRecord = new ItemStatusRecord();
                itemStatusRecord.setCode(itemStatus);
                itemStatusRecord.setName(itemStatus);
                getBusinessObjectService().save(itemStatusRecord);
                return itemStatusRecord;
            } else {
                return null;
            }
        }
        return itemStatusRecords.get(0);
    }

    /*

    protected void updateItemNoteRecords(List<Note> noteList, String extendInfoId, List<ItemNoteRecord> itemNoteRecords) {

        getBusinessObjectService().delete(itemNoteRecords);
        saveItemNoteRecord(noteList, extendInfoId);
    }


     protected void updateCallNumberRecord(CallNumber callNumber, HoldingsRecord holdingsRecord) {

        ShelvingScheme scheme = callNumber.getShelvingScheme();
        ShelvingOrder order = callNumber.getShelvingOrder();
        if (scheme != null && scheme.getCodeValue() != null && !scheme.getCodeValue().isEmpty()) {

            holdingsRecord.setCallNumber(callNumber.getNumber());
            holdingsRecord.setCallNumberPrefix(callNumber.getPrefix());
            holdingsRecord.setShelvingOrder(order.getCodeValue());
            CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
            holdingsRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());
        } else if (order != null && scheme != null && !scheme.getCodeValue().isEmpty() && (!order.getCodeValue().isEmpty() || !callNumber.getPrefix().isEmpty() || !callNumber.getNumber().isEmpty())) {
            holdingsRecord.setCallNumber(callNumber.getNumber());
            holdingsRecord.setCallNumberPrefix(callNumber.getPrefix());
            holdingsRecord.setShelvingOrder(order.getCodeValue());
            holdingsRecord.setCallNumberTypeId(null);
        }
    }*/


    protected void saveFormerIdentifierRecords(List<FormerIdentifier> formerIdentifierList, String itemId) {
        Map map = new HashMap();
        map.put("itemId", itemId);
        List<FormerIdentifierRecord> formerIdentifierRecordList = (List<FormerIdentifierRecord>) getBusinessObjectService().findMatching(FormerIdentifierRecord.class, map);
        if (formerIdentifierList.size() > 0) {
            List<FormerIdentifierRecord> formerIdentifierRecords = new ArrayList<FormerIdentifierRecord>();
            for (int i = 0; i < formerIdentifierList.size(); i++) {
                FormerIdentifier formerIdentifier = formerIdentifierList.get(i);
                if (formerIdentifier.getIdentifier() != null && formerIdentifier.getIdentifier().getIdentifierValue() != null && !"".equals(formerIdentifier.getIdentifier().getIdentifierValue())) {

                    FormerIdentifierRecord formerIdentifierRecord = new FormerIdentifierRecord();
                    if (i < formerIdentifierRecordList.size()) {
                        formerIdentifierRecord = formerIdentifierRecordList.get(i);
                    }
                    formerIdentifierRecord.setType(formerIdentifier.getIdentifierType());
                    if (formerIdentifier.getIdentifier() != null)
                        formerIdentifierRecord.setValue(formerIdentifier.getIdentifier().getIdentifierValue());
                    formerIdentifierRecord.setItemId(itemId);
                    formerIdentifierRecords.add(formerIdentifierRecord);
                }
            }
            if (formerIdentifierRecords.size() > 0) {
                getBusinessObjectService().save(formerIdentifierRecords);
            }

            if (formerIdentifierRecordList.size() > formerIdentifierList.size()) {
                getBusinessObjectService().delete(formerIdentifierRecordList.subList(formerIdentifierList.size() - 1, formerIdentifierRecordList.size()));
            }
        }
    }

    protected void saveItemNoteRecord(List<Note> noteList, String itemId) {

        Map map = new HashMap();
        map.put("itemId", itemId);
        List<ItemNoteRecord> itemNoteRecordList = (List<ItemNoteRecord>) getBusinessObjectService().findMatching(ItemNoteRecord.class, map);


        if (noteList.size() > 0) {
            List<ItemNoteRecord> itemNoteRecords = new ArrayList<ItemNoteRecord>();
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                    ItemNoteRecord itemNoteRecord = new ItemNoteRecord();
                    if (i < itemNoteRecordList.size()) {
                        itemNoteRecord = itemNoteRecordList.get(i);
                    }
                    itemNoteRecord.setType(note.getType());
                    itemNoteRecord.setNote(note.getValue());
                    itemNoteRecord.setItemId(itemId);
                    itemNoteRecords.add(itemNoteRecord);
                }
            }
            if (itemNoteRecords.size() > 0) {
                getBusinessObjectService().save(itemNoteRecords);
            }

            if (noteList.size() < itemNoteRecordList.size()) {
                getBusinessObjectService().delete(itemNoteRecordList.subList(noteList.size() - 1, itemNoteRecordList.size()));
            }

        }
    }

    protected void saveDonorList(List<DonorInfo> donorslist, String itemId) {
        Map map = new HashMap();
        map.put("itemId", itemId);
        List<OLEItemDonorRecord> itemDonorRecordList = (List<OLEItemDonorRecord>) getBusinessObjectService().findMatching(OLEItemDonorRecord.class, map);
        if(itemDonorRecordList!=null && itemDonorRecordList.size() >= 0) {
            getBusinessObjectService().delete(itemDonorRecordList);
        }
        if (donorslist.size() > 0) {
            List<OLEItemDonorRecord> oleItemDonorRecords = new ArrayList<OLEItemDonorRecord>();
            for (int i = 0; i < donorslist.size(); i++) {
                DonorInfo donorinfo = donorslist.get(i);
                if (donorinfo.getDonorCode() != null ) {
                    OLEItemDonorRecord oleItemDonorRecord = new OLEItemDonorRecord();
                    oleItemDonorRecord.setDonorPublicDisplay(donorinfo.getDonorPublicDisplay());
                    oleItemDonorRecord.setDonorCode(donorinfo.getDonorCode());
                    oleItemDonorRecord.setDonorNote(donorinfo.getDonorNote());
                    oleItemDonorRecord.setItemId(itemId);
                    oleItemDonorRecords.add(oleItemDonorRecord);
                }
            }
            if (oleItemDonorRecords.size() > 0) {
                getBusinessObjectService().save(oleItemDonorRecords);
            }
        }
    }

    protected void saveMissingPieceItemList(List<MissingPieceItemRecord> missingPieceItemRecords,String itemId){
        Map map = new HashMap();
        map.put("itemId", itemId);
        List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordList = (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord>) getBusinessObjectService().findMatching(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord.class, map);
        if(missingPieceItemRecordList != null && missingPieceItemRecordList.size()>0){
            getBusinessObjectService().delete(missingPieceItemRecordList);
        }
        if(missingPieceItemRecords.size()>0){
            List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordList1 =new ArrayList<>();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String parsedDate = df.format((new Date()));
            Timestamp dueDateTime1 = null;
            try{
                 dueDateTime1 = new Timestamp(df.parse(parsedDate).getTime());
            }catch(Exception e){
                e.printStackTrace();
            }
            for(int i=0;i<missingPieceItemRecords.size();i++){
                MissingPieceItemRecord missingPieceItemRecord=missingPieceItemRecords.get(i);
                if(missingPieceItemRecord.getMissingPieceItemId() != null){
                    org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord missingPieceItemRecord1 = new org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord();
                    missingPieceItemRecord1.setMissingPieceCount(missingPieceItemRecord.getMissingPieceCount());
                    missingPieceItemRecord1.setMissingPieceFlagNote(missingPieceItemRecord.getMissingPieceFlagNote());
                    missingPieceItemRecord1.setOperatorId(missingPieceItemRecord.getOperatorId());
                    missingPieceItemRecord1.setItemId(missingPieceItemRecord.getItemId());
                    missingPieceItemRecord1.setPatronBarcode(missingPieceItemRecord.getPatronBarcode());
                    missingPieceItemRecord1.setPatronId(missingPieceItemRecord.getPatronId());
                    missingPieceItemRecord1.setMissingPieceDate(dueDateTime1);
                    missingPieceItemRecordList1.add(missingPieceItemRecord1);
                }
            }
            if(missingPieceItemRecordList1.size()>0){
                getBusinessObjectService().save(missingPieceItemRecordList1);
            }
        }
    }


    protected void saveCheckInLocationRecord(List<CheckInLocation> checkInLocationList, String itemId) {

        Map map = new HashMap();
        map.put("itemId", itemId);
        List<LocationsCheckinCountRecord> locationsCheckinCountRecordList = (List<LocationsCheckinCountRecord>) getBusinessObjectService().findMatching(LocationsCheckinCountRecord.class, map);
        if (locationsCheckinCountRecordList != null && locationsCheckinCountRecordList.size() > 0) {
            LocationsCheckinCountRecord locationsCheckinCountRecord = locationsCheckinCountRecordList.get(0);
            CheckInLocation checkInLocation = checkInLocationList.get(0);
            locationsCheckinCountRecord.setLocationCount(checkInLocation.getCount());
            locationsCheckinCountRecord.setLocationName(checkInLocation.getName());
            locationsCheckinCountRecord.setLocationInhouseCount(checkInLocation.getInHouseCount());
            getBusinessObjectService().save(locationsCheckinCountRecord);
        } else {

            CheckInLocation checkInLocation = checkInLocationList.get(0);
            LocationsCheckinCountRecord locationsCheckinCountRecord = new LocationsCheckinCountRecord();
            locationsCheckinCountRecord.setLocationCount(checkInLocation.getCount());
            locationsCheckinCountRecord.setLocationName(checkInLocation.getName());
            locationsCheckinCountRecord.setLocationInhouseCount(checkInLocation.getInHouseCount());
            locationsCheckinCountRecord.setItemId(itemId);
            getBusinessObjectService().save(locationsCheckinCountRecord);

        }

    }

/*    protected void updateExtensionRecord(OleHoldings oleHoldings, HoldingsExtendInfoRecord holdingsExtendInfoRecord) {

        ReceiptStatusRecord receiptStatusRecord = saveReceiptStatusRecord(oleHoldings.getReceiptStatus());
        holdingsExtendInfoRecord.setReceiptStatusId(receiptStatusRecord == null ? null : receiptStatusRecord.getReceiptStatusId());
        getBusinessObjectService().save(holdingsExtendInfoRecord);
        List<Uri> uriList = oleHoldings.getUri();
        if (uriList.size() > 0) {
            List<AccessUriRecord> accessInfoRecords = new ArrayList<AccessUriRecord>();
            for (Uri uri : uriList) {
                AccessUriRecord accessInfoRecord = new AccessUriRecord();
                accessInfoRecord.setText(uri.getValue());
                accessInfoRecord.setHoldingsExtendInfoId(holdingsExtendInfoRecord.getExtendInfoId());
                accessInfoRecords.add(accessInfoRecord);
            }
            if (accessInfoRecords.size() > 0)
                getBusinessObjectService().save(accessInfoRecords);
        }
    }

    protected void saveExtentOfOwnerShips(List<ExtentOfOwnership> extentOfOwnershipList, String holdingsId) {

        for (ExtentOfOwnership extentOfOwnership : extentOfOwnershipList) {
            ExtentOfOwnerShipRecord extentOfOwnerShipRecord = new ExtentOfOwnerShipRecord();
            extentOfOwnerShipRecord.setCode(extentOfOwnership.getType());
            extentOfOwnerShipRecord.setDescription(extentOfOwnership.getType());
            extentOfOwnerShipRecord.setText(extentOfOwnership.getTextualHoldings());
            extentOfOwnerShipRecord.setHoldingsId(holdingsId);
            getBusinessObjectService().save(extentOfOwnerShipRecord);
            List<Note> notes = extentOfOwnership.getNote();
            if (notes.size() > 0) {
                List<ExtentNoteRecord> extentNoteRecords = new ArrayList<>();
                for (Note note : notes) {
                    ExtentNoteRecord noteRecord = new ExtentNoteRecord();
                    noteRecord.setExtentOfOwnerShipId(extentOfOwnerShipRecord.getExtOfOwnerShipID());
                    noteRecord.setType(note.getType());
                    noteRecord.setNote(note.getValue());
                    extentNoteRecords.add(noteRecord);
                }
                if (extentNoteRecords.size() > 0)
                    getBusinessObjectService().save(extentNoteRecords);
            }
        }
    }

    protected void updateExtentOfOwnerShip(List<ExtentOfOwnership> extentOfOwnershipList, String holdingsId, List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords) {

        getBusinessObjectService().delete(extentOfOwnerShipRecords);
        saveExtentOfOwnerShip(extentOfOwnershipList, holdingsId);
    }

    protected void updateHoldingsNoteRecord(List<Note> noteList, String holdingsId, List<HoldingsNoteRecord> holdingsNoteRecords) {
        getBusinessObjectService().delete(holdingsNoteRecords);
        saveHoldingNoteRecord(noteList, holdingsId);
    }  */

/*    protected void saveHoldingNoteRecord(List<Note> noteList, String holdingsId) {

        List<HoldingsNoteRecord> holdingsNoteRecords = new ArrayList<HoldingsNoteRecord>();
        for (Note note : noteList) {
            HoldingsNoteRecord holdingsNoteRecord = new HoldingsNoteRecord();
            holdingsNoteRecord.setType(note.getType());
            holdingsNoteRecord.setNote(note.getValue());
            holdingsNoteRecord.setHoldingsId(holdingsId);
            holdingsNoteRecords.add(holdingsNoteRecord);
        }
        if (holdingsNoteRecords.size() > 0)
            getBusinessObjectService().save(holdingsNoteRecords);
    }*/

    protected Item buildItemContent(ItemRecord itemRecord) {
        Item item = new Item();
        item.setItemIdentifier(DocumentUniqueIDPrefix.getPrefixedId(itemRecord.getUniqueIdPrefix(), itemRecord.getItemId()));
        item.setPurchaseOrderLineItemIdentifier(itemRecord.getPurchaseOrderItemLineId());
        item.setVendorLineItemIdentifier(itemRecord.getVendorLineItemId());
        item.setFund(itemRecord.getFund());
        item.setPrice(itemRecord.getPrice());
        item.setBarcodeARSL(itemRecord.getBarCodeArsl());
        item.setCopyNumber(itemRecord.getCopyNumber());
        item.setEnumeration(itemRecord.getEnumeration());
        item.setChronology(itemRecord.getChronology());
        item.setNumberOfPieces(itemRecord.getNumberOfPieces());
        item.setDescriptionOfPieces(itemRecord.getDescriptionOfPieces());
        item.setCheckinNote(itemRecord.getCheckInNote());
        item.setLocation(getLocationDetails(itemRecord.getLocation(), itemRecord.getLocationLevel()));
        if (itemRecord.getFormerIdentifierRecords() != null) {
            List<FormerIdentifier> formerIdList = new ArrayList<FormerIdentifier>();
            for (FormerIdentifierRecord formerIdentifierRecord : itemRecord.getFormerIdentifierRecords()) {
                FormerIdentifier formerIdentifier = new FormerIdentifier();
                Identifier identifier = new Identifier();
                identifier.setIdentifierValue(formerIdentifierRecord.getValue());
                formerIdentifier.setIdentifier(identifier);
                formerIdList.add(formerIdentifier);
                item.setFormerIdentifier(formerIdList);
            }
        }

        AccessInformation accessInformation = new AccessInformation();
        accessInformation.setBarcode(itemRecord.getBarCode());
        Uri itemuri = new Uri();
        itemuri.setValue(itemRecord.getUri());
        accessInformation.setUri(itemuri);
        item.setAccessInformation(accessInformation);
        CallNumber itemCallNumber = new CallNumber();
        itemCallNumber.setNumber(itemRecord.getCallNumber());
        itemCallNumber.setPrefix(itemRecord.getCallNumberPrefix());
        ShelvingScheme itemShelvingScheme = new ShelvingScheme();
        if (itemRecord.getCallNumberTypeRecord() != null) {
            itemShelvingScheme.setCodeValue(itemRecord.getCallNumberTypeRecord().getCode());
            itemShelvingScheme.setFullValue(itemRecord.getCallNumberTypeRecord().getName());
            itemCallNumber.setShelvingScheme(itemShelvingScheme);
        }
        ShelvingOrder itemShelvingOrder = new ShelvingOrder();
        itemShelvingOrder.setCodeValue(itemRecord.getShelvingOrder());
        itemShelvingOrder.setFullValue(itemRecord.getShelvingOrder());
        itemCallNumber.setShelvingOrder(itemShelvingOrder);
        item.setCallNumber(itemCallNumber);

        List<Note> notes = new ArrayList<Note>();
        if (itemRecord.getItemNoteRecords() != null) {
            List<ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
            for (ItemNoteRecord itemNoteRecord : itemNoteRecords) {
                Note note = new Note();
                note.setType(itemNoteRecord.getType());
                note.setValue(itemNoteRecord.getNote());
                notes.add(note);
            }
            item.setNote(notes);
        }
        List<DonorInfo> donorInfoList = new ArrayList<DonorInfo>();
        if (itemRecord.getDonorList() != null) {
            List<OLEItemDonorRecord> oleItemDonorRecordList = itemRecord.getDonorList();
            for (OLEItemDonorRecord oleItemDonorRecord : oleItemDonorRecordList) {
                DonorInfo donorInfo = new DonorInfo();
                donorInfo.setDonorCode(oleItemDonorRecord.getDonorCode());
                donorInfo.setDonorPublicDisplay(oleItemDonorRecord.getDonorPublicDisplay());
                donorInfo.setDonorNote(oleItemDonorRecord.getDonorNote());
                donorInfoList.add(donorInfo);
            }
            item.setDonorInfo(donorInfoList);
        }
        List<MissingPieceItemRecord> missingPieceItemRecordList = new ArrayList<>();
        if(itemRecord.getMissingPieceItemRecordList() != null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            String parsedDate = simpleDateFormat.format((new Date()));
            List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecords = itemRecord.getMissingPieceItemRecordList();
            for(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord missingPieceItemRecord : missingPieceItemRecords){
                MissingPieceItemRecord missingPieceItemRecord1 = new MissingPieceItemRecord();
                missingPieceItemRecord1.setMissingPieceFlagNote(missingPieceItemRecord.getMissingPieceFlagNote());
                missingPieceItemRecord1.setMissingPieceCount(missingPieceItemRecord.getMissingPieceCount());
                missingPieceItemRecord1.setItemId(missingPieceItemRecord.getItemId());
                missingPieceItemRecord1.setOperatorId(missingPieceItemRecord.getOperatorId());
                missingPieceItemRecord1.setPatronBarcode(missingPieceItemRecord.getPatronBarcode());
                missingPieceItemRecord1.setPatronId(missingPieceItemRecord.getPatronId());
                missingPieceItemRecord1.setMissingPieceItemId(missingPieceItemRecord.getMissingPieceItemId());
                missingPieceItemRecord1.setMissingPieceDate(parsedDate);
                missingPieceItemRecordList.add(missingPieceItemRecord1);
            }
            item.setMissingPieceItemRecordList(missingPieceItemRecordList);
        }
        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecordList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(itemRecord.getItemClaimsReturnedRecords())) {
            List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord> itemClaimsReturnedRecords = itemRecord.getItemClaimsReturnedRecords();
            for(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord itemClaimsReturnedRecord : itemClaimsReturnedRecords){
                ItemClaimsReturnedRecord claimsReturnedRecord = new ItemClaimsReturnedRecord();
                if (itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate() != null) {
                    SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date claimsReturnedDate = null;
                    try {
                        claimsReturnedDate = format2.parse(itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate().toString());
                    } catch (ParseException e) {
                        LOG.error("format string to Date " + e);
                    }
                    claimsReturnedRecord.setClaimsReturnedFlagCreateDate(format1.format(claimsReturnedDate).toString());
                }
                claimsReturnedRecord.setClaimsReturnedOperatorId(itemClaimsReturnedRecord.getClaimsReturnedOperatorId());
                claimsReturnedRecord.setClaimsReturnedPatronBarcode(itemClaimsReturnedRecord.getClaimsReturnedPatronBarcode());
                claimsReturnedRecord.setClaimsReturnedPatronId(itemClaimsReturnedRecord.getClaimsReturnedPatronId());
                claimsReturnedRecord.setClaimsReturnedNote(itemClaimsReturnedRecord.getClaimsReturnedNote());
                claimsReturnedRecord.setItemId(itemClaimsReturnedRecord.getItemId());
                itemClaimsReturnedRecordList.add(claimsReturnedRecord);
            }
            item.setItemClaimsReturnedRecords(itemClaimsReturnedRecordList);
        }
        List<org.kuali.ole.docstore.common.document.content.instance.ItemDamagedRecord> itemDamagedRecordList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(itemRecord.getItemDamagedRecords())){
            List<ItemDamagedRecord> itemDamagedRecords = itemRecord.getItemDamagedRecords();
            for(ItemDamagedRecord itemDamagedRecord : itemDamagedRecords){
                org.kuali.ole.docstore.common.document.content.instance.ItemDamagedRecord damagedRecord = new org.kuali.ole.docstore.common.document.content.instance.ItemDamagedRecord();
                if(itemDamagedRecord.getDamagedItemDate() != null){
                    SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date itemDamagedDate = null;
                    try{
                        itemDamagedDate = format2.parse(itemDamagedRecord.getDamagedItemDate().toString());
                    } catch (ParseException e){
                        LOG.error("Format string to Date " +e);
                    }
                    damagedRecord.setDamagedItemDate(format1.format(itemDamagedDate).toString());
                }
                damagedRecord.setDamagedItemNote(itemDamagedRecord.getDamagedItemNote());
                damagedRecord.setPatronBarcode(itemDamagedRecord.getPatronBarcode());
                damagedRecord.setDamagedPatronId(itemDamagedRecord.getDamagedPatronId());
                damagedRecord.setOperatorId(itemDamagedRecord.getOperatorId());
                damagedRecord.setItemId(itemDamagedRecord.getItemId());
                itemDamagedRecordList.add(damagedRecord);
            }
            item.setItemDamagedRecords(itemDamagedRecordList);
        }
        if (itemRecord.getItemStatusRecord() != null) {
            ItemStatus itemStatus = new ItemStatus();
            itemStatus.setCodeValue(itemRecord.getItemStatusRecord().getCode());
            itemStatus.setFullValue(itemRecord.getItemStatusRecord().getCode());
            item.setItemStatus(itemStatus);
        }
        if (itemRecord.getItemTypeRecord() != null) {
            ItemType itemType = new ItemType();
            itemType.setCodeValue(itemRecord.getItemTypeRecord().getCode());
            itemType.setFullValue(itemRecord.getItemTypeRecord().getName());
            item.setItemType(itemType);
        }
        if (itemRecord.getItemTempTypeRecord() != null) {
            ItemType itemType = new ItemType();
            itemType.setCodeValue(itemRecord.getItemTempTypeRecord().getCode());
            itemType.setFullValue(itemRecord.getItemTempTypeRecord().getName());
            item.setTemporaryItemType(itemType);
        }
        List<StatisticalSearchingCode> statisticalSearchingCodes = new ArrayList<StatisticalSearchingCode>();
        StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
//        if (itemRecord.getStatisticalSearchRecord() != null) {
//            statisticalSearchingCode.setCodeValue(itemRecord.getStatisticalSearchRecord().getCode());
//            statisticalSearchingCode.setFullValue(itemRecord.getStatisticalSearchRecord().getName());
//        }
        statisticalSearchingCodes.add(statisticalSearchingCode);
        item.setStatisticalSearchingCode(statisticalSearchingCodes);
        if (itemRecord.getEffectiveDate() != null) {
            //Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            //String effectiveDate = formatter.format(itemRecord.getEffectiveDate().toString());
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date effectiveDate = null;
            try {
                effectiveDate = format2.parse(itemRecord.getEffectiveDate().toString());
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            item.setItemStatusEffectiveDate(format1.format(effectiveDate).toString());
        }
        if(itemRecord.getStaffOnlyFlag() != null) {
            item.setStaffOnlyFlag(itemRecord.getStaffOnlyFlag());
        }
        if(itemRecord.getFastAddFlag() != null) {
            item.setFastAddFlag(Boolean.valueOf(itemRecord.getFastAddFlag()));
        }
        List<LocationsCheckinCountRecord> locationsCheckinCountRecords = itemRecord.getLocationsCheckinCountRecords();
        if (locationsCheckinCountRecords != null && locationsCheckinCountRecords.size() > 0) {

            NumberOfCirculations numberOfCirculations = new NumberOfCirculations();
            List<CheckInLocation> checkInLocations = new ArrayList<CheckInLocation>();
            for (LocationsCheckinCountRecord locationsCheckinCountRecord : locationsCheckinCountRecords) {

                CheckInLocation checkInLocation = new CheckInLocation();
                checkInLocation.setCount(locationsCheckinCountRecord.getLocationCount());
                checkInLocation.setName(locationsCheckinCountRecord.getLocationName());
                checkInLocation.setInHouseCount(locationsCheckinCountRecord.getLocationInhouseCount());
                checkInLocations.add(checkInLocation);
            }
            numberOfCirculations.setCheckInLocation(checkInLocations);
            item.setNumberOfCirculations(numberOfCirculations);
        }
        if (itemRecord.getClaimsReturnedFlag() != null) {
            item.setClaimsReturnedFlag(Boolean.valueOf(itemRecord.getClaimsReturnedFlag()));
        }

        if (itemRecord.getClaimsReturnedFlagCreateDate() != null) {
            //try {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date claimReturnCreateDate = null;
            try {
                claimReturnCreateDate = format2.parse(itemRecord.getClaimsReturnedFlagCreateDate().toString());
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            item.setClaimsReturnedFlagCreateDate(format1.format(claimReturnCreateDate).toString());
            //item.setClaimsReturnedFlagCreateDate(getGregorianCalendar(itemRecord.getClaimsReturnedFlagCreateDate()));
            /*} catch (DatatypeConfigurationException e) {
                LOG.error(" getGregorianCalendar", e);
            }*/
        }

        if (itemRecord.getDueDateTime() != null) {
            /*try {
                item.setDueDateTime(getGregorianCalendar(itemRecord.getDueDateTime()));
            } catch (DatatypeConfigurationException e) {
                LOG.error(" getGregorianCalendar", e);
            }*/
            //try {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String DATE_FORMAT_HH_MM_SS_REGX = "^(1[0-2]|0[1-9])/(3[0|1]|[1|2][0-9]|0[1-9])/[0-9]{4}(\\s)((([1|0][0-9])|([2][0-4]))):[0-5][0-9]:[0-5][0-9]$";
            Date dueDateTime = null;
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ssa");
            DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            DateFormat displayLoanTime = new SimpleDateFormat("MM/dd/yyyy hh:mma");
            try {
                dueDateTime = format2.parse(itemRecord.getDueDateTime().toString());
                //item.setDueDateTime(format1.format(dueDateTime).toString());
                String dateString = format1.format(dueDateTime).toString();
                if (StringUtils.isNotBlank(dateString) && dateString.matches(DATE_FORMAT_HH_MM_SS_REGX)) {
                    dueDateTime = df1.parse(dateString);
                    item.setDueDateTime(df.format(dueDateTime));
                    item.setLoanDueDate(displayLoanTime.format(dueDateTime));
                }else {
                    item.setDueDateTime(dateString);
                    item.setLoanDueDate(displayLoanTime.format(dueDateTime));
                }
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            //item.setDueDateTime(format1.format(dueDateTime).toString());
            /*item.setClaimsReturnedFlagCreateDate(getGregorianCalendar(itemRecord.getClaimsReturnedFlagCreateDate()));
            } catch (DatatypeConfigurationException e) {
                LOG.error(" getGregorianCalendar", e);
            }*/
        }

        if(itemRecord.getClaimsReturnedNote() != null){
            item.setClaimsReturnedNote(itemRecord.getClaimsReturnedNote());
        }
        item.setProxyBorrower(itemRecord.getProxyBorrower()!=null?itemRecord.getProxyBorrower():"");
        item.setCurrentBorrower(itemRecord.getCurrentBorrower()!=null?itemRecord.getCurrentBorrower():"");
        item.setItemDamagedStatus(itemRecord.isItemDamagedStatus());
        if(itemRecord.getDamagedItemNote()!=null){
            item.setDamagedItemNote(itemRecord.getDamagedItemNote());
        }
        if(itemRecord.isMissingPieceFlag()){
           item.setMissingPieceFlag(itemRecord.isMissingPieceFlag());
        }
        if(itemRecord.getMissingPieceFlagNote()!=null){
            item.setMissingPieceFlagNote(itemRecord.getMissingPieceFlagNote());
        }
        if (itemRecord.getMissingPieceEffectiveDate() != null) {
            //Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            //String effectiveDate = formatter.format(itemRecord.getEffectiveDate().toString());
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date effectiveDate = null;
            try {
                effectiveDate = format2.parse(itemRecord.getMissingPieceEffectiveDate().toString());
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            item.setMissingPieceEffectiveDate(format1.format(effectiveDate).toString());
        } else {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String date=df.format(new Date());
            item.setMissingPieceEffectiveDate(date);
        }
        if(itemRecord.getMissingPiecesCount()!=null){
           item.setMissingPiecesCount(itemRecord.getMissingPiecesCount());
        }


        return item;
    }

    public LocationLevel createLocationLevel(String locationName, String locationLevelName) {
        LocationLevel locationLevel = null;
        if (StringUtils.isNotEmpty(locationName ) && StringUtils.isNotEmpty(locationLevelName)) {
            String[] locations = locationName.split("/");
            String[] locationLevels = locationLevelName.split("/");
            String locName = "";
            String levelName = "";
            if (locations.length > 0 ) {
                locName = locations[0];
                levelName = locationLevels[0];
                if (locationName.contains("/")) {
                    locationName = locationName.replaceFirst(locations[0] + "/", "");
                } else {
                    locationName = locationName.replace(locations[0], "");
                }

                if (locationLevelName.contains("/")) {
                    locationLevelName = locationLevelName.replaceFirst(locationLevels[0] + "/", "");
                } else {
                    locationLevelName = locationLevelName.replace(locationLevels[0], "");
                }
                if (locName != null && locations.length != 0) {
                    locationLevel = new LocationLevel();
                    locationLevel.setLevel(levelName);
                    locationLevel.setName(locName);
                    locationLevel.setLocationLevel(createLocationLevel(locationName, locationLevelName));
                }
            }
        }
        return locationLevel;
    }


    private Location getLocationDetails(String locationName, String locationLevelName) {
        Location location = new Location();
        //LocationLevel locationLevel = new LocationLevel();
        LocationLevel locationLevel = createLocationLevel(locationName, locationLevelName);
        location.setLocationLevel(locationLevel);
        return location;
    }

    protected OleHoldings buildHoldingsContent(HoldingsRecord holdingsRecord) {
        OleHoldings oleHoldings = new OleHoldings();
        // oleHoldings.set


        oleHoldings.setHoldingsIdentifier(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
        if (holdingsRecord.getReceiptStatusRecord() != null) {
            ReceiptStatusRecord receiptStatusRecord = holdingsRecord.getReceiptStatusRecord();
            if (receiptStatusRecord != null) {
                oleHoldings.setReceiptStatus(receiptStatusRecord.getCode());
            }
        }
       /* if (holdingsRecord.getAccessUriRecords() != null && holdingsRecord.getAccessUriRecords().size() > 0) {
            List<Uri> uris = new ArrayList<Uri>();
            List<AccessUriRecord> accessUriRecords = holdingsRecord.getAccessUriRecords();
            for (AccessUriRecord accessUriRecord : accessUriRecords) {
                Uri uri = new Uri();
                uri.setValue(accessUriRecord.getText());
                uris.add(uri);
            }
            oleHoldings.setUri(uris);
        }*/
        if (holdingsRecord.getLocation() != null) {
            Location location = getLocationDetails(holdingsRecord.getLocation(), holdingsRecord.getLocationLevel());
            oleHoldings.setLocation(location);
        }
        CallNumber callNumber = new CallNumber();
        if (holdingsRecord.getCallNumberTypeRecord() != null) {
            callNumber.setNumber(holdingsRecord.getCallNumber());
            ShelvingScheme shelvingScheme = new ShelvingScheme();
            shelvingScheme.setCodeValue(holdingsRecord.getCallNumberTypeRecord().getCode());
            shelvingScheme.setFullValue(holdingsRecord.getCallNumberTypeRecord().getName());
            callNumber.setShelvingScheme(shelvingScheme);
            ShelvingOrder shelvingOrder = new ShelvingOrder();
            shelvingOrder.setCodeValue(holdingsRecord.getShelvingOrder());
            shelvingOrder.setFullValue(holdingsRecord.getShelvingOrder());
            callNumber.setShelvingOrder(shelvingOrder);
            callNumber.setPrefix(holdingsRecord.getCallNumberPrefix());
            //callNumber.setCopyNumber(holdingsRecord.getCopyNumber());
            oleHoldings.setCallNumber(callNumber);
        }
        if (holdingsRecord.getCopyNumber() != null) {
            oleHoldings.setCopyNumber(holdingsRecord.getCopyNumber());

        }
        if (holdingsRecord.getExtentOfOwnerShipRecords() != null && holdingsRecord.getExtentOfOwnerShipRecords().size() > 0) {
            List<ExtentOfOwnership> extentOfOwnerships = new ArrayList<ExtentOfOwnership>();
            for (ExtentOfOwnerShipRecord extentOfOwnerShipRecord : holdingsRecord.getExtentOfOwnerShipRecords()) {
                ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
                if (extentOfOwnerShipRecord.getExtentOfOwnerShipTypeRecord() != null) {
                    extentOfOwnership.setTextualHoldings(extentOfOwnerShipRecord.getText());
                    extentOfOwnership.setType(extentOfOwnerShipRecord.getExtentOfOwnerShipTypeRecord().getCode());
                }

                if (extentOfOwnerShipRecord.getExtentNoteRecords() != null && extentOfOwnerShipRecord.getExtentNoteRecords().size() > 0) {
                    for (ExtentNoteRecord extentNoteRecord : extentOfOwnerShipRecord.getExtentNoteRecords()) {
                        Note note = new Note();
                        note.setType(extentNoteRecord.getType());
                        note.setValue(extentNoteRecord.getNote());
                        extentOfOwnership.getNote().add(note);
                    }
                } else {
                    Note note = new Note();
                    extentOfOwnership.getNote().add(note);
                }

                extentOfOwnerships.add(extentOfOwnership);
            }
            oleHoldings.setExtentOfOwnership(extentOfOwnerships);
        }


        if (holdingsRecord.getHoldingsNoteRecords() != null && holdingsRecord.getHoldingsNoteRecords().size() > 0) {
            List<HoldingsNoteRecord> holdingsNoteRecords = holdingsRecord.getHoldingsNoteRecords();
            if (holdingsNoteRecords != null && holdingsNoteRecords.size() > 0) {
                List<Note> notes = new ArrayList<Note>();
                for (HoldingsNoteRecord holdingsNoteRecord : holdingsNoteRecords) {
                    Note note = new Note();
                    note.setType(holdingsNoteRecord.getType());
                    note.setValue(holdingsNoteRecord.getNote());
                    notes.add(note);
                }
                oleHoldings.setNote(notes);
            }
        }
//        oleHoldings.setCreatedBy(holdingsRecord.getCreatedBy());
//        oleHoldings.setUpdatedBy(holdingsRecord.getUpdatedBy());
//        oleHoldings.setCreatedDate(holdingsRecord.getCreatedDate());
//        oleHoldings.setUpdatedDate(holdingsRecord.getUpdatedDate());
        return oleHoldings;
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

    protected boolean validateCallNumber(String callNumber, String codeValue) throws OleDocStoreException {
        boolean isValid = false;
        /*if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.kuali.ole.utility.callnumber.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                isValid = callNumberObj.isValid(callNumber);
            }
        }*/
        return isValid;
    }

    protected String buildSortableCallNumber(String callNumber, String codeValue) throws OleDocStoreException {
        String shelvingOrder = "";
       /* if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.kuali.ole.utility.callnumber.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                shelvingOrder = callNumberObj.getSortableKey(callNumber);
                //shelvingOrder = shelvingOrder.replaceAll(" ", "_");
            }
        }*/
        return shelvingOrder;
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

    private String resolveLinkingWithBib(Instance instance) {
//        instance.getResourceIdentifier().clear();
        if (ProcessParameters.BULK_INGEST_IS_LINKING_ENABLED) {
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
                                    instance.getResourceIdentifier().clear();
                                    String bibUniqueId = (String) solrDoc.get("uniqueId");
                                    instance.getResourceIdentifier().add(solrDoc.getFieldValue("id").toString());
                                    return bibUniqueId;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Exception (ignored) while linking instance with bib: ", e);
                }
            }
        }
        return null;
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

    private XMLGregorianCalendar getGregorianCalendar(Date date) throws DatatypeConfigurationException {
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(date);
        XMLGregorianCalendar dealCloseDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregory);
        return dealCloseDate;
    }

    private void effectiveDateItem(Item item, ItemRecord itemRecord, String effectiveDateForItem) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Timestamp effectiveDate = null;
        try {
            if (!"".equals(item.getItemStatusEffectiveDate()) && item.getItemStatusEffectiveDate() != null) {
                effectiveDate = new Timestamp(df.parse(effectiveDateForItem).getTime());
                itemRecord.setEffectiveDate(effectiveDate);
            }

        } catch (Exception e) {
            LOG.error("Effective Date for Item" + e);
        }
    }

    private void claimsReturnsCreateDateItem(Item item, ItemRecord itemRecord, String claimReturnCreateDate) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Timestamp claimReturnCreateDate1 = null;
        try {
            if (!"".equals(item.getClaimsReturnedFlagCreateDate()) && item.getClaimsReturnedFlagCreateDate() != null) {
                claimReturnCreateDate1 = new Timestamp(df.parse(claimReturnCreateDate).getTime());
                itemRecord.setClaimsReturnedFlagCreateDate(claimReturnCreateDate1);
            }
        } catch (Exception e) {
            LOG.error("Effective Date for Item" + e);
        }
    }

    private void dueDateTime(Item item, ItemRecord itemRecord, String dueDateTime) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Timestamp dueDateTime1 = null;
        try {
            if (!"".equals(item.getDueDateTime()) && item.getDueDateTime() != null) {
                dueDateTime1 = new Timestamp(df.parse(dueDateTime).getTime());
                itemRecord.setDueDateTime(dueDateTime1);
            }
        } catch (Exception e) {
            LOG.error("Effective Date for Item" + e);
        }
    }

/*    public void saveHoldingsRecordAdditonalAttributes(OleHoldings oleHoldings ,AdditionalAttributes additionalAttributes){
        if(additionalAttributes != null && additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY) != null){
            oleHoldings.setCreatedBy(additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY));
        }
        try {
        if(additionalAttributes != null && additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED) != null){

            DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Timestamp createdDate = null;
        String createdDateForHoldings = additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED);

            createdDate = new Timestamp(df.parse(createdDateForHoldings).getTime());
            oleHoldings.setCreatedDate(createdDate);
        }
        } catch (Exception e) {
            LOG.error("Created Date for Holdings" + e);
        }
    }*/

    public void saveHoldingsRecordAdditonalAttributesForDate(HoldingsRecord holdingsRecord ,AdditionalAttributes additionalAttributes){
        if(additionalAttributes != null && additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY) != null){
            holdingsRecord.setCreatedBy(additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY));
        }
        try {
            if(additionalAttributes != null && additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED) != null){

                DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                Timestamp createdDate = null;
                String createdDateForHoldings = additionalAttributes.getAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED);

                createdDate = new Timestamp(df.parse(createdDateForHoldings).getTime());
                holdingsRecord.setCreatedDate(createdDate);
            }
        } catch (Exception e) {
            LOG.error("Created Date for Holdings" + e);
        }
    }
}