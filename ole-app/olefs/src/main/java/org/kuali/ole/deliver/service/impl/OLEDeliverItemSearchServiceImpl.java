package org.kuali.ole.deliver.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OLEDeliverItemSearchService;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
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
                singleItemResultDisplayRow.setPatronExpDate(olePatronDocument.getExpirationDate().toString());
                if (olePatronDocument.getOlePatronEntityViewBo() != null) {
                    singleItemResultDisplayRow.setPatronLastName(olePatronDocument.getOlePatronEntityViewBo().getLastName());
                    singleItemResultDisplayRow.setPatronFirstName(olePatronDocument.getOlePatronEntityViewBo().getFirstName());
                    singleItemResultDisplayRow.setPatronMiddleName(olePatronDocument.getOlePatronEntityViewBo().getMiddleName());
                }
                if (StringUtils.isBlank(singleItemResultDisplayRow.getDueDate())) {
                    singleItemResultDisplayRow.setDueDate(OLEConstants.INDEFINITE);
                }
            }
            if (StringUtils.isNotBlank(singleItemResultDisplayRow.getProxyBorrowerId())) {
                singleItemResultDisplayRow.setProxyPatronURL(getLoanProcessor().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), singleItemResultDisplayRow.getProxyBorrowerId()));
                olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class, singleItemResultDisplayRow.getProxyBorrowerId());
                singleItemResultDisplayRow.setProxyPatronBarcode(olePatronDocument.getBarcode());
                if (olePatronDocument.getOlePatronEntityViewBo() != null) {
                    singleItemResultDisplayRow.setProxyPatronName(olePatronDocument.getOlePatronEntityViewBo().getName());
                }
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
                singleItemResultDisplayRow.setFinalStatus("Filed");
                singleItemResultDisplayRow.setFinalStatusDate(deliverRequestHistoryRecordList.get(0).getArchiveDate());
            }
        }
    }
}
