package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.bo.*;

import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hemalathas on 1/18/16.
 */
public class ItemBarcodeUpdateHandler {

    Map<String,String> itemBarcodeMap = new HashMap<String,String>();

    public void updateItemBarcode(String oldBarcode, String newBarcode,String itemId){
        updateDeliverRequest(itemId, newBarcode);
        updateCirculationHistory(itemId, newBarcode);
        updateTemporaryCirculationHistory(itemId, newBarcode);
        updateLoanDocument(itemId, newBarcode);
        updateFeeType(itemId, newBarcode);
        updateReturnHistoryRecord(itemId, newBarcode);
        updateRenewalHistoryRecord(itemId, newBarcode);
        updateDeliverRequestHistoryRecord(oldBarcode, newBarcode);
    }

    public void updateDeliverRequest(String itemId, String newBarcode){
        itemBarcodeMap.put("itemUuid", "wio-"+itemId);
        List<OleDeliverRequestBo> oleDeliverRequestBos = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> deliverRequestBoList = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemBarcodeMap);

        if (CollectionUtils.isNotEmpty(deliverRequestBoList)) {
            for (OleDeliverRequestBo oleDeliverRequestBo : deliverRequestBoList) {
                oleDeliverRequestBo.setItemId(newBarcode);
                oleDeliverRequestBos.add(oleDeliverRequestBo);
            }
            List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().save(oleDeliverRequestBos);
        }
    }

    public void updateCirculationHistory(String itemId, String newBarcode){
        itemBarcodeMap.clear();
        itemBarcodeMap.put("itemUuid", "wio-"+itemId);
        List<OleCirculationHistory> oleCirculationHistories = new ArrayList<OleCirculationHistory>();
        List<OleCirculationHistory> oleCirculationHistoryList = (List<OleCirculationHistory>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationHistory.class, itemBarcodeMap);
        if (CollectionUtils.isNotEmpty(oleCirculationHistoryList)) {
            for (OleCirculationHistory circulationHistory : oleCirculationHistoryList) {
                circulationHistory.setItemId(newBarcode);
                oleCirculationHistories.add(circulationHistory);
            }
            List<OleCirculationHistory> circulationHistoryList = (List<OleCirculationHistory>) KRADServiceLocator.getBusinessObjectService().save(oleCirculationHistories);
        }
    }

    public void updateTemporaryCirculationHistory(String itemId, String newBarcode){
        itemBarcodeMap.clear();
        itemBarcodeMap.put("itemUuid", "wio-"+itemId);
        List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryList = new ArrayList<OleTemporaryCirculationHistory>();
        List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistories = (List<OleTemporaryCirculationHistory>) KRADServiceLocator.getBusinessObjectService().findMatching(OleTemporaryCirculationHistory.class, itemBarcodeMap);
        if (CollectionUtils.isNotEmpty(oleTemporaryCirculationHistories)) {
            for (OleTemporaryCirculationHistory temporaryCirculationHistory : oleTemporaryCirculationHistories) {
                temporaryCirculationHistory.setItemId(newBarcode);
                oleTemporaryCirculationHistoryList.add(temporaryCirculationHistory);
            }
            List<OleTemporaryCirculationHistory> temporaryCirculationHistoryList = (List<OleTemporaryCirculationHistory>) KRADServiceLocator.getBusinessObjectService().save(oleTemporaryCirculationHistoryList);
        }
    }

    public void updateLoanDocument(String itemId, String newBarcode){
        itemBarcodeMap.clear();
        itemBarcodeMap.put("itemUuid", "wio-"+itemId);
        List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
        List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, itemBarcodeMap);
        if (CollectionUtils.isNotEmpty(oleLoanDocumentList)) {
            for (OleLoanDocument loanDocument : oleLoanDocumentList) {
                loanDocument.setItemId(newBarcode);
                oleLoanDocuments.add(loanDocument);
            }
            List<OleLoanDocument> loanDocuments = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().save(oleLoanDocuments);
        }
    }


    public void updateFeeType(String itemId, String newBarcode){
        itemBarcodeMap.clear();
        itemBarcodeMap.put("itemUuid","wio-"+itemId);
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        List<FeeType> feeTypeList = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, itemBarcodeMap);
        if (CollectionUtils.isNotEmpty(feeTypeList)) {
            for (FeeType feeType : feeTypeList) {
                feeType.setItemBarcode(newBarcode);
                feeTypes.add(feeType);
            }
            List<FeeType> feeTypes1 = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().save(feeTypes);
        }
    }

    public void updateReturnHistoryRecord(String itemId, String newBarcode){
        itemBarcodeMap.clear();
        itemBarcodeMap.put("itemUUID","wio-"+itemId);
        List<OLEReturnHistoryRecord> oleReturnHistoryRecords = new ArrayList<OLEReturnHistoryRecord>();
        List<OLEReturnHistoryRecord> oleReturnHistoryRecordList = (List<OLEReturnHistoryRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEReturnHistoryRecord.class, itemBarcodeMap);
        if (CollectionUtils.isNotEmpty(oleReturnHistoryRecordList)) {
            for (OLEReturnHistoryRecord returnHistoryRecord : oleReturnHistoryRecordList) {
                returnHistoryRecord.setItemBarcode(newBarcode);
                oleReturnHistoryRecords.add(returnHistoryRecord);
            }
            List<OLEReturnHistoryRecord> returnHistoryRecords1 = (List<OLEReturnHistoryRecord>) KRADServiceLocator.getBusinessObjectService().save(oleReturnHistoryRecords);
        }
    }


    public void updateRenewalHistoryRecord(String itemId, String newBarcode){
        itemBarcodeMap.clear();
        itemBarcodeMap.put("itemId","wio-"+itemId);
        List<OleRenewalHistory> oleRenewalHistories = new ArrayList<OleRenewalHistory>();
        List<OleRenewalHistory> oleRenewalHistoryList = (List<OleRenewalHistory>) KRADServiceLocator.getBusinessObjectService().findMatching(OleRenewalHistory.class, itemBarcodeMap);
        if (CollectionUtils.isNotEmpty(oleRenewalHistoryList)) {
            for (OleRenewalHistory renewalHistory : oleRenewalHistoryList) {
                renewalHistory.setItemBarcode(newBarcode);
                oleRenewalHistories.add(renewalHistory);
            }
            List<OleRenewalHistory> oleRenewalHistories1 = (List<OleRenewalHistory>) KRADServiceLocator.getBusinessObjectService().save(oleRenewalHistories);
        }

    }

    public void updateDeliverRequestHistoryRecord(String oleBarcode, String newBarcode){
        itemBarcodeMap.clear();
        itemBarcodeMap.put("itemBarcode",oleBarcode);
        List<OleDeliverRequestHistoryRecord> oleDeliverRequestHistoryRecords = new ArrayList<OleDeliverRequestHistoryRecord>();
        List<OleDeliverRequestHistoryRecord> oleDeliverRequestHistoryRecordList = (List<OleDeliverRequestHistoryRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestHistoryRecord.class, itemBarcodeMap);
        if (CollectionUtils.isNotEmpty(oleDeliverRequestHistoryRecordList)) {
            for (OleDeliverRequestHistoryRecord deliverRequestHistoryRecord : oleDeliverRequestHistoryRecordList) {
                deliverRequestHistoryRecord.setItemBarcode(newBarcode);
                oleDeliverRequestHistoryRecords.add(deliverRequestHistoryRecord);
            }
            List<OleDeliverRequestHistoryRecord> oleDeliverRequestHistoryRecords1 = (List<OleDeliverRequestHistoryRecord>) KRADServiceLocator.getBusinessObjectService().save(oleDeliverRequestHistoryRecords);
        }
    }



}
