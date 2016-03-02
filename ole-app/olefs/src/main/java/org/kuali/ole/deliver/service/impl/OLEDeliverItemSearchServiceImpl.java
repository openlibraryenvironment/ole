package org.kuali.ole.deliver.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OLEDeliverItemSearchService;
import org.kuali.ole.deliver.service.OLEDeliverService;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 1/28/15.
 */
public class OLEDeliverItemSearchServiceImpl implements OLEDeliverItemSearchService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEDeliverItemSearchServiceImpl.class);

    private LoanProcessor loanProcessor;
    private DocstoreClientLocator docstoreClientLocator;

    private LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = SpringContext.getBean(LoanProcessor.class);
        }
        return loanProcessor;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public void setDeliverRequestInfo(Map itemIdMap, OLESingleItemResultDisplayRow singleItemResultDisplayRow) {
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemIdMap);
        if (CollectionUtils.isNotEmpty(oleDeliverRequestBos)) {
            List<OLEDeliverRequestResultDisplayRow> deliverRequestResultDisplayRowList = new ArrayList<>();
            for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos) {
                OLEDeliverRequestResultDisplayRow deliverRequestResultDisplayRow = new OLEDeliverRequestResultDisplayRow();
                deliverRequestResultDisplayRow.setBorrowerQueuePosition(oleDeliverRequestBo.getBorrowerQueuePosition());
                deliverRequestResultDisplayRow.setRequestId(oleDeliverRequestBo.getRequestId());
                deliverRequestResultDisplayRow.setRequestTypeCode(oleDeliverRequestBo.getRequestTypeCode());
                deliverRequestResultDisplayRow.setBorrowerFirstName(oleDeliverRequestBo.getFirstName());
                deliverRequestResultDisplayRow.setBorrowerLastName(oleDeliverRequestBo.getLastName());
                deliverRequestResultDisplayRow.setBorrowerBarcode(oleDeliverRequestBo.getBorrowerBarcode());
                deliverRequestResultDisplayRow.setCreateDate(oleDeliverRequestBo.getCreateDate());
                deliverRequestResultDisplayRow.setExpiryDate(oleDeliverRequestBo.getRequestExpiryDate());
                deliverRequestResultDisplayRow.setOnHoldExpirationDate(oleDeliverRequestBo.getHoldExpirationDate());
                deliverRequestResultDisplayRow.setPickUpLocation(oleDeliverRequestBo.getOlePickUpLocation()!=null ? oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskCode() : "");
                deliverRequestResultDisplayRowList.add(deliverRequestResultDisplayRow);
            }
            singleItemResultDisplayRow.setOleDeliverRequestResultDisplayRowList(deliverRequestResultDisplayRowList);
        }
    }

    public void setOutstandingFineInfo(Map itemIdMap, OLESingleItemResultDisplayRow singleItemResultDisplayRow) {
        List<FeeType> feeTypes = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, itemIdMap);
        if (CollectionUtils.isNotEmpty(feeTypes)) {
            PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
            List<OLEFeeTypeResultDisplayRow> feeTypeResultDisplayRowList = new ArrayList<>();
            for (FeeType feeType : feeTypes) {
                OLEFeeTypeResultDisplayRow feeTypeResultDisplayRow = new OLEFeeTypeResultDisplayRow();
                feeTypeResultDisplayRow.setFeeType(feeType.getFeeTypeName());
                feeTypeResultDisplayRow.setFeeAmount(feeType.getFeeAmount());
                feeTypeResultDisplayRow.setBorrowerId(feeType.getPatronBillPayment().getPatronId());
                OlePatronDocument olePatronDocument = patronBillHelperService.getPatronDetails(feeType.getPatronBillPayment().getPatronId());
                if (olePatronDocument != null) {
                    feeTypeResultDisplayRow.setBorrowerBarcode(olePatronDocument.getBarcode());
                    feeTypeResultDisplayRow.setBorrowerFirstName(olePatronDocument.getFirstName());
                    feeTypeResultDisplayRow.setBorrowerLastName(olePatronDocument.getLastName());
                }
                feeTypeResultDisplayRowList.add(feeTypeResultDisplayRow);
            }
            singleItemResultDisplayRow.setOleFeeTypeResultDisplayRowList(feeTypeResultDisplayRowList);
        }
    }

    public void setNoteInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow) {
        if (singleItemResultDisplayRow.getItemNoteType() != null) {
            List<OLEItemNoteResultDisplayRow> itemNoteResultDisplayRowList = new ArrayList<>();
            String[] itemNoteTypes = singleItemResultDisplayRow.getItemNoteType().split(",");
            String[] itemNoteValues = singleItemResultDisplayRow.getItemNoteValue() != null ? singleItemResultDisplayRow.getItemNoteValue().split(",") : new String[itemNoteTypes.length];
            for (int noteCount = 0; noteCount < itemNoteTypes.length; noteCount++) {
                OLEItemNoteResultDisplayRow itemNoteResultDisplayRow = new OLEItemNoteResultDisplayRow();
                itemNoteResultDisplayRow.setNoteType(itemNoteTypes[noteCount]);
                itemNoteResultDisplayRow.setNoteValue(itemNoteValues[noteCount]);
                itemNoteResultDisplayRowList.add(itemNoteResultDisplayRow);
            }
            singleItemResultDisplayRow.setOleItemNoteResultDisplayRowList(itemNoteResultDisplayRowList);
        }
    }

    public void setBorrowerInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow) {
        if (StringUtils.isNotBlank(singleItemResultDisplayRow.getCurrentBorrowerId())) {
            OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class, singleItemResultDisplayRow.getCurrentBorrowerId());
            if (olePatronDocument != null) {
                singleItemResultDisplayRow.setPatronURL(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), singleItemResultDisplayRow.getCurrentBorrowerId()));
                singleItemResultDisplayRow.setPatronBarcode(olePatronDocument.getBarcode());
                if (StringUtils.isNotBlank(olePatronDocument.getBorrowerType())) {
                    OleBorrowerType oleBorrowerType = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleBorrowerType.class, olePatronDocument.getBorrowerType());
                    if (oleBorrowerType != null) {
                        singleItemResultDisplayRow.setPatronType(oleBorrowerType.getBorrowerTypeName());
                    }
                }
                if(olePatronDocument.getExpirationDate()!=null){
                    singleItemResultDisplayRow.setPatronExpDate(olePatronDocument.getExpirationDate().toString());
                }
                olePatronDocument = OLEDeliverService.populatePatronName(olePatronDocument);
                singleItemResultDisplayRow.setPatronLastName(olePatronDocument.getLastName());
                singleItemResultDisplayRow.setPatronFirstName(olePatronDocument.getFirstName());
                singleItemResultDisplayRow.setPatronMiddleName(olePatronDocument.getMiddleName());
                if (StringUtils.isBlank(singleItemResultDisplayRow.getDueDate())) {
                    singleItemResultDisplayRow.setDueDate(OLEConstants.INDEFINITE);
                }
            }
            if (StringUtils.isNotBlank(singleItemResultDisplayRow.getProxyBorrowerId())) {
                singleItemResultDisplayRow.setProxyPatronURL(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), singleItemResultDisplayRow.getProxyBorrowerId()));
                olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class, singleItemResultDisplayRow.getProxyBorrowerId());
                singleItemResultDisplayRow.setProxyPatronBarcode(olePatronDocument.getBarcode());
                olePatronDocument = OLEDeliverService.populatePatronName(olePatronDocument);
                singleItemResultDisplayRow.setProxyPatronName(olePatronDocument.getLastName() + ", "+ olePatronDocument.getFirstName());
            }
        }
    }

    public void setAdditionalCopiesInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow) {
        List<OLEHoldingsSearchResultDisplayRow> oleHoldingsSearchResultDisplayRowList = new ArrayList<>();
        if (StringUtils.isNotBlank(singleItemResultDisplayRow.getBibIdentifier())) {
            try {
                BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(singleItemResultDisplayRow.getBibIdentifier());
                for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                    OLEHoldingsSearchResultDisplayRow holdingsSearchResultDisplayRow = new OLEHoldingsSearchResultDisplayRow();
                    holdingsSearchResultDisplayRow.setId(holdingsTree.getHoldings().getId());
                    holdingsSearchResultDisplayRow.setBibIdentifier(bibTree.getBib().getId());
                    holdingsSearchResultDisplayRow.setLocation(holdingsTree.getHoldings().getLocationName());
                    if (holdingsTree.getHoldings().getContentObject() != null) {
                        if (holdingsTree.getHoldings().getContentObject().getCallNumber() != null)
                            holdingsSearchResultDisplayRow.setCallNumber(holdingsTree.getHoldings().getContentObject().getCallNumber().getNumber());
                    }
                    if (holdingsTree.getItems().size() > 0) {
                        List<OLEItemSearchResultDisplayRow> itemSearchResultDisplayRowList = new ArrayList<>();
                        for (Item item : holdingsTree.getItems()) {
                            if (!item.getId().equals(singleItemResultDisplayRow.getId())) {
                                OLEItemSearchResultDisplayRow oleItemSearchResultDisplayRow = new OLEItemSearchResultDisplayRow();
                                oleItemSearchResultDisplayRow.setId(item.getId());
                                oleItemSearchResultDisplayRow.setHoldingsIdentifier(holdingsTree.getHoldings().getId());
                                oleItemSearchResultDisplayRow.setBibIdentifier(bibTree.getBib().getId());
                                oleItemSearchResultDisplayRow.setLocation(item.getLocation());
                                Object itemContent = item.getContentObject();
                                if (itemContent != null) {
                                    org.kuali.ole.docstore.common.document.content.instance.Item itemPojo = (org.kuali.ole.docstore.common.document.content.instance.Item) itemContent;
                                    oleItemSearchResultDisplayRow.setEnumeration(itemPojo.getEnumeration());
                                    oleItemSearchResultDisplayRow.setChronology(itemPojo.getChronology());
                                    if (itemPojo.getItemType() != null)
                                        oleItemSearchResultDisplayRow.setItemType(itemPojo.getItemType().getCodeValue());
                                    if (itemPojo.getItemStatus() != null)
                                        oleItemSearchResultDisplayRow.setItemStatus(itemPojo.getItemStatus().getCodeValue());
                                    if (itemPojo.getAccessInformation() != null)
                                        oleItemSearchResultDisplayRow.setItemBarCode(itemPojo.getAccessInformation().getBarcode());
                                    Map itemMap = new HashMap();
                                    itemMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, oleItemSearchResultDisplayRow.getItemBarCode());
                                    List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemMap);
                                    if (CollectionUtils.isNotEmpty(deliverRequestBos)) {
                                        oleItemSearchResultDisplayRow.setRequestExists(true);
                                    }
                                    oleItemSearchResultDisplayRow.setPlaceRequest(validateItemStatusForPlaceRequest(oleItemSearchResultDisplayRow.getItemStatus()));
                                }
                                itemSearchResultDisplayRowList.add(oleItemSearchResultDisplayRow);
                            }
                        }
                        if (itemSearchResultDisplayRowList.size() > 0) {
                            if (itemSearchResultDisplayRowList.size() == 1) {
                                holdingsSearchResultDisplayRow.setItemLocation(itemSearchResultDisplayRowList.get(0).getLocation());
                            } else {
                                holdingsSearchResultDisplayRow.setItemLocation(OLEConstants.MULTIPLE);
                            }
                            holdingsSearchResultDisplayRow.setOleItemSearchResultDisplayRowList(itemSearchResultDisplayRowList);
                            oleHoldingsSearchResultDisplayRowList.add(holdingsSearchResultDisplayRow);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception " + e);
            }
        }
        singleItemResultDisplayRow.setOleHoldingsSearchResultDisplayRowList(oleHoldingsSearchResultDisplayRowList);
    }

    public void setRequestHistoryInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow) {
        if (StringUtils.isNotBlank(singleItemResultDisplayRow.getItemBarCode())) {
            List<OleDeliverRequestHistoryRecord> deliverRequestHistoryRecordList = (List<OleDeliverRequestHistoryRecord>) ((OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao")).getDeliverRequestHistoryRecords(singleItemResultDisplayRow.getItemBarCode());
            if (deliverRequestHistoryRecordList != null && deliverRequestHistoryRecordList.size() > 0) {
                singleItemResultDisplayRow.setRequestTypeCode(deliverRequestHistoryRecordList.get(0).getDeliverRequestTypeCode());
                singleItemResultDisplayRow.setPickUpLocationCode(deliverRequestHistoryRecordList.get(0).getPickUpLocationCode());
                singleItemResultDisplayRow.setFinalStatus("Filled");
                singleItemResultDisplayRow.setFinalStatusDate(deliverRequestHistoryRecordList.get(0).getArchiveDate());
            }
        }
    }

    public void setInTransitHistoryInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow) {
        if (StringUtils.isNotBlank(singleItemResultDisplayRow.getId())) {
            Map map = new HashMap();
            map.put(OLEConstants.OVERLAY_ITEMUUID, singleItemResultDisplayRow.getId());
            List<OLEReturnHistoryRecord> oleReturnHistoryRecords = (List<OLEReturnHistoryRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEReturnHistoryRecord.class, map);
            if (CollectionUtils.isNotEmpty(oleReturnHistoryRecords)) {
                singleItemResultDisplayRow.setOleReturnHistoryRecords(oleReturnHistoryRecords);
            }
        }
    }

    public void setClaimsReturnedInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow) {
        if(StringUtils.isNotBlank(singleItemResultDisplayRow.getId())) {
            Map map = new HashMap();
            map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(singleItemResultDisplayRow.getId()));
            List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords = (List<ItemClaimsReturnedRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(ItemClaimsReturnedRecord.class,map);
            if(CollectionUtils.isNotEmpty(itemClaimsReturnedRecords)) {
                for(int index=0 ; index < itemClaimsReturnedRecords.size() ; index++){
                    if(StringUtils.isNotBlank(itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronId()) && StringUtils.isNotBlank(itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronBarcode())){
                        itemClaimsReturnedRecords.get(index).setClaimsReturnedPatronUrl(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronId()));
                    } else if(itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronId() != null && !itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronId().isEmpty()){
                        OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class, itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronId());
                        if(olePatronDocument != null){
                            itemClaimsReturnedRecords.get(index).setClaimsReturnedPatronBarcode(olePatronDocument.getBarcode());
                            itemClaimsReturnedRecords.get(index).setClaimsReturnedPatronUrl(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronId()));
                        }
                    } else if(itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronBarcode() != null && !itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronBarcode().isEmpty()) {
                        Map criteria = new HashMap();
                        criteria.put("barcode",itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronBarcode());
                        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,criteria);
                        if(CollectionUtils.isNotEmpty(olePatronDocumentList)) {
                            for(OlePatronDocument olePatronDocument : olePatronDocumentList) {
                                itemClaimsReturnedRecords.get(index).setClaimsReturnedPatronUrl(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), olePatronDocument.getOlePatronId()));
                            }
                        } else {
                            criteria = new HashMap();
                            criteria.put("invalidOrLostBarcodeNumber", itemClaimsReturnedRecords.get(index).getClaimsReturnedPatronBarcode());
                            List<OlePatronLostBarcode> olePatronLostBarcodeList = (List<OlePatronLostBarcode>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class, criteria);
                            if(CollectionUtils.isNotEmpty(olePatronLostBarcodeList)){
                                for(OlePatronLostBarcode olePatronLostBarcode : olePatronLostBarcodeList) {
                                    itemClaimsReturnedRecords.get(index).setClaimsReturnedPatronUrl(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), olePatronLostBarcode.getOlePatronId()));
                                }
                            }
                        }
                    }
                }
                singleItemResultDisplayRow.setItemClaimsReturnedRecords(itemClaimsReturnedRecords);
            }
        }
    }

    public void setDamagedInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow){
        if(StringUtils.isNotBlank(singleItemResultDisplayRow.getId())) {
            Map map = new HashMap();
            map.put("itemId",DocumentUniqueIDPrefix.getDocumentId(singleItemResultDisplayRow.getId()));
            List<ItemDamagedRecord> itemDamagedRecords = (List<ItemDamagedRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(ItemDamagedRecord.class,map);
            if(CollectionUtils.isNotEmpty(itemDamagedRecords)){
                for(int index=0 ; index < itemDamagedRecords.size() ; index++){
                    if(StringUtils.isNotBlank(itemDamagedRecords.get(index).getDamagedPatronId()) && StringUtils.isNotBlank(itemDamagedRecords.get(index).getPatronBarcode())){
                        itemDamagedRecords.get(index).setDamagedPatronUrl(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), itemDamagedRecords.get(index).getDamagedPatronId()));
                    } else if(itemDamagedRecords.get(index).getDamagedPatronId() != null && !itemDamagedRecords.get(index).getDamagedPatronId().isEmpty()){
                        OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class,itemDamagedRecords.get(index).getDamagedPatronId());
                        if(olePatronDocument != null){
                            itemDamagedRecords.get(index).setPatronBarcode(olePatronDocument.getBarcode());
                            itemDamagedRecords.get(index).setDamagedPatronUrl(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), itemDamagedRecords.get(index).getDamagedPatronId()));
                        }
                    } else if(itemDamagedRecords.get(index).getPatronBarcode() != null && !itemDamagedRecords.get(index).getPatronBarcode().isEmpty()) {
                        Map criteria = new HashMap();
                        criteria.put("barcode",itemDamagedRecords.get(index).getPatronBarcode());
                        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, criteria);
                        if(olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
                            for(OlePatronDocument olePatronDocument : olePatronDocumentList) {
                                itemDamagedRecords.get(index).setDamagedPatronUrl(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), olePatronDocument.getOlePatronId()));
                            }
                        } else {
                            criteria = new HashMap();
                            criteria.put("invalidOrLostBarcodeNumber", itemDamagedRecords.get(index).getPatronBarcode());
                            List<OlePatronLostBarcode> olePatronLostBarcodeList = (List<OlePatronLostBarcode>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class, criteria);
                            if(CollectionUtils.isNotEmpty(olePatronLostBarcodeList)){
                                for(OlePatronLostBarcode olePatronLostBarcode : olePatronLostBarcodeList) {
                                    itemDamagedRecords.get(index).setDamagedPatronUrl(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), olePatronLostBarcode.getOlePatronId()));
                                }
                            }
                        }
                    }
                }
                singleItemResultDisplayRow.setItemDamagedRecords(itemDamagedRecords);
            }
        }
    }


    public void setMissingPieceItemInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow){
        if(StringUtils.isNotBlank(singleItemResultDisplayRow.getId())){
            Map map = new HashMap();
            map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(singleItemResultDisplayRow.getId()));
            List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordHistories = (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord>)KRADServiceLocator.getBusinessObjectService().findMatching(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord.class, map);
            if(CollectionUtils.isNotEmpty(missingPieceItemRecordHistories)){
                for(int index=0 ; index < missingPieceItemRecordHistories.size() ; index++){
                    if(StringUtils.isNotBlank(missingPieceItemRecordHistories.get(index).getPatronId()) && StringUtils.isNotBlank(missingPieceItemRecordHistories.get(index).getPatronBarcode())) {
                        missingPieceItemRecordHistories.get(index).setPatronURL(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), missingPieceItemRecordHistories.get(index).getPatronId()));
                    } else if(missingPieceItemRecordHistories.get(index).getPatronId() != null && !missingPieceItemRecordHistories.get(index).getPatronId().isEmpty()){
                        OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class,missingPieceItemRecordHistories.get(index).getPatronId());
                        if(olePatronDocument != null){
                            missingPieceItemRecordHistories.get(index).setPatronBarcode(olePatronDocument.getBarcode());
                            missingPieceItemRecordHistories.get(index).setPatronURL(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), missingPieceItemRecordHistories.get(index).getPatronId()));
                        }
                    }else if(missingPieceItemRecordHistories.get(index).getPatronBarcode() != null && !missingPieceItemRecordHistories.get(index).getPatronBarcode().isEmpty()){
                        Map criteria = new HashMap();
                        criteria.put("barcode" , missingPieceItemRecordHistories.get(index).getPatronBarcode());
                        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class ,criteria );
                        if(olePatronDocumentList != null && olePatronDocumentList.size()>0){
                            for(OlePatronDocument olePatronDocument : olePatronDocumentList){
                                missingPieceItemRecordHistories.get(index).setPatronURL(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), olePatronDocument.getOlePatronId()));
                            }
                        }else{
                            Map criteria1 = new HashMap();
                            criteria1.put("invalidOrLostBarcodeNumber" ,missingPieceItemRecordHistories.get(index).getPatronBarcode() );
                            List<OlePatronLostBarcode> olePatronLostBarcodeList = (List<OlePatronLostBarcode>)KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class , criteria1);
                            if(olePatronLostBarcodeList != null && olePatronLostBarcodeList.size()>0){
                                for(OlePatronLostBarcode olePatronLostBarcode : olePatronLostBarcodeList){
                                    missingPieceItemRecordHistories.get(index).setPatronURL(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), missingPieceItemRecordHistories.get(index).getPatronId()));
                                }
                            }
                        }
                    }
                }
                singleItemResultDisplayRow.setMissingPieceItemRecordList(missingPieceItemRecordHistories);
            }

        }
    }

    public boolean validateItemStatusForPlaceRequest(String oleItemStatus) {
        boolean valid = false;
        if (StringUtils.isNotBlank(oleItemStatus)){
            String copyRequestItemStatus = getLoanProcessor().getParameter(OLEConstants.COPY_REQUEST_ITEM_STATUS);
            if (org.apache.commons.lang.StringUtils.isNotBlank(copyRequestItemStatus)) {
                String[] itemStatusArray = copyRequestItemStatus.split(";");
                for(String itemStatus : itemStatusArray){
                    if(itemStatus.trim().equalsIgnoreCase(oleItemStatus)){
                        valid = true;
                        return valid;
                    }
                }
            }

            String holdDeliveryItemStatus = getLoanProcessor().getParameter(OLEConstants.HOLD_DELIVERY_ITEM_STATUS);
            if (org.apache.commons.lang.StringUtils.isNotBlank(holdDeliveryItemStatus)) {
                String[] itemStatusArray = holdDeliveryItemStatus.split(";");
                for(String itemStatus : itemStatusArray){
                    if(itemStatus.trim().equalsIgnoreCase(oleItemStatus)){
                        valid = true;
                        return valid;
                    }
                }
            }
            String holdHoldItemStatus = getLoanProcessor().getParameter(OLEConstants.HOLD_HOLD_ITEM_STATUS);
            if (org.apache.commons.lang.StringUtils.isNotBlank(holdHoldItemStatus)) {
                String[] itemStatusArray = holdHoldItemStatus.split(";");
                for(String itemStatus : itemStatusArray){
                    if(itemStatus.trim().equalsIgnoreCase(oleItemStatus)){
                        valid = true;
                        return valid;
                    }
                }
            }
            String pageDeliveryItemStatus = getLoanProcessor().getParameter(OLEConstants.PAGE_DELIVERY_ITEM_STATUS);
            if (org.apache.commons.lang.StringUtils.isNotBlank(pageDeliveryItemStatus)) {
                String[] itemStatusArray = pageDeliveryItemStatus.split(";");
                for(String itemStatus : itemStatusArray){
                    if(itemStatus.trim().equalsIgnoreCase(oleItemStatus)){
                        valid = true;
                        return valid;
                    }
                }
            }
            String pageHoldItemStatus = getLoanProcessor().getParameter(OLEConstants.PAGE_HOLD_ITEM_STATUS);
            if (org.apache.commons.lang.StringUtils.isNotBlank(pageHoldItemStatus)) {
                String[] itemStatusArray = pageHoldItemStatus.split(";");
                for(String itemStatus : itemStatusArray){
                    if(itemStatus.trim().equalsIgnoreCase(oleItemStatus)){
                        valid = true;
                        return valid;
                    }
                }
            }
            String recallDeliveryItemStatus = getLoanProcessor().getParameter(OLEConstants.RECALL_DELIVERY_ITEM_STATUS);
            if (org.apache.commons.lang.StringUtils.isNotBlank(recallDeliveryItemStatus)) {
                String[] itemStatusArray = recallDeliveryItemStatus.split(";");
                for(String itemStatus : itemStatusArray){
                    if(itemStatus.trim().equalsIgnoreCase(oleItemStatus)){
                        valid = true;
                        return valid;
                    }
                }
            }
            String recallHoldItemStatus = getLoanProcessor().getParameter(OLEConstants.RECALL_HOLD_ITEM_STATUS);
            if (org.apache.commons.lang.StringUtils.isNotBlank(recallHoldItemStatus)) {
                String[] itemStatusArray = recallHoldItemStatus.split(";");
                for(String itemStatus : itemStatusArray){
                    if(itemStatus.trim().equalsIgnoreCase(oleItemStatus)){
                        valid = true;
                        return valid;
                    }
                }
            }
        }
        return valid;
    }
}
