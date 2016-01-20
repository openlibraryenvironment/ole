package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleRenewalHistory;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hemalathas on 1/18/16.
 */
public class PatronBarcodeUpdateHandler {

    Map<String,String> requestMap = new HashMap<>();

    public void updatePatronBarcode(OlePatronDocument newPatronDocument , String olePatronBarcode){

            updateDeliverRequest(newPatronDocument);
            updateMissingPieceHistory(newPatronDocument);
            updateClaimsReturnHistory(newPatronDocument , olePatronBarcode);
            updateItemDamagedHistory(newPatronDocument , olePatronBarcode);
            updateRenewalHistory(newPatronDocument , olePatronBarcode);

    }

    public void updateDeliverRequest(OlePatronDocument olePatronDocument){
        List<OleDeliverRequestBo> oleDeliverRequestBos = new ArrayList<OleDeliverRequestBo>();
        requestMap.put("borrowerId", olePatronDocument.getOlePatronId());
        List<OleDeliverRequestBo> deliverRequestBoList = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
        if(CollectionUtils.isNotEmpty(deliverRequestBoList)){
            for(OleDeliverRequestBo oleDeliverRequestBo : deliverRequestBoList){
                oleDeliverRequestBo.setBorrowerBarcode(olePatronDocument.getBarcode());
                oleDeliverRequestBos.add(oleDeliverRequestBo);
            }

        }
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>)KRADServiceLocator.getBusinessObjectService().save(oleDeliverRequestBos);
    }

    public void updateMissingPieceHistory(OlePatronDocument olePatronDocument){
        requestMap.clear();
        requestMap.put("patronId", olePatronDocument.getOlePatronId());
        List<MissingPieceItemRecord> missingPieceItemRecordLists = new ArrayList<MissingPieceItemRecord>();
        List<MissingPieceItemRecord> missingPieceItemRecordList = (List<MissingPieceItemRecord>)KRADServiceLocator.getBusinessObjectService().findMatching(MissingPieceItemRecord.class,requestMap);
        if(CollectionUtils.isNotEmpty(missingPieceItemRecordList)){
            for(MissingPieceItemRecord missingPieceItemRecord : missingPieceItemRecordList){
                missingPieceItemRecord.setPatronBarcode(olePatronDocument.getBarcode());
                missingPieceItemRecordLists.add(missingPieceItemRecord);
            }

        }
        List<MissingPieceItemRecord> missingPieceItemRecords = (List<MissingPieceItemRecord>)KRADServiceLocator.getBusinessObjectService().save(missingPieceItemRecordLists);
    }

    public void updateClaimsReturnHistory (OlePatronDocument olePatronDocument,String olePatronBarcode){
        requestMap.clear();
        requestMap.put("claimsReturnedPatronId", olePatronDocument.getOlePatronId());
        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecordArrayList = new ArrayList<ItemClaimsReturnedRecord>();
        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords = (List<ItemClaimsReturnedRecord>)KRADServiceLocator.getBusinessObjectService().findMatching(ItemClaimsReturnedRecord.class,requestMap);
        if(CollectionUtils.isNotEmpty(itemClaimsReturnedRecords)){
            for(ItemClaimsReturnedRecord itemClaimsReturnedRecord : itemClaimsReturnedRecords){
                itemClaimsReturnedRecord.setClaimsReturnedPatronBarcode(olePatronDocument.getBarcode());
                itemClaimsReturnedRecordArrayList.add(itemClaimsReturnedRecord);
            }
            List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords1 = (List<ItemClaimsReturnedRecord>)KRADServiceLocator.getBusinessObjectService().save(itemClaimsReturnedRecordArrayList);
        }else{
            requestMap.clear();
            requestMap.put("claimsReturnedPatronBarcode",olePatronBarcode);
            List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords1 = (List<ItemClaimsReturnedRecord>)KRADServiceLocator.getBusinessObjectService().findMatching(ItemClaimsReturnedRecord.class,requestMap);
            if(CollectionUtils.isNotEmpty(itemClaimsReturnedRecords1)){
                for(ItemClaimsReturnedRecord itemClaimsReturnedRecord : itemClaimsReturnedRecords1){
                    itemClaimsReturnedRecord.setClaimsReturnedPatronBarcode(olePatronDocument.getBarcode());
                    itemClaimsReturnedRecordArrayList.add(itemClaimsReturnedRecord);
                }

            }

        }
        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecordList = (List<ItemClaimsReturnedRecord>)KRADServiceLocator.getBusinessObjectService().save(itemClaimsReturnedRecordArrayList);
    }


    public void updateItemDamagedHistory(OlePatronDocument olePatronDocument,String olePatronBarcode){
        requestMap.clear();
        requestMap.put("damagedPatronId",olePatronDocument.getOlePatronId());
        List<ItemDamagedRecord> itemDamagedRecords = new ArrayList<ItemDamagedRecord>();
        List<ItemDamagedRecord> itemDamagedRecordList = (List<ItemDamagedRecord>)KRADServiceLocator.getBusinessObjectService().findMatching(ItemDamagedRecord.class,requestMap);
        if(CollectionUtils.isNotEmpty(itemDamagedRecordList)){
            for(ItemDamagedRecord damagedRecord : itemDamagedRecordList){
                damagedRecord.setPatronBarcode(olePatronDocument.getBarcode());
                itemDamagedRecords.add(damagedRecord);
            }
            List<ItemDamagedRecord> itemDamagedRecords1 = (List<ItemDamagedRecord>)KRADServiceLocator.getBusinessObjectService().save(itemDamagedRecords);
        }else{
            requestMap.clear();
            requestMap.put("patronBarcode",olePatronBarcode);
            List<ItemDamagedRecord> itemDamagedRecords1 = (List<ItemDamagedRecord>)KRADServiceLocator.getBusinessObjectService().findMatching(ItemDamagedRecord.class,requestMap);
            if(CollectionUtils.isNotEmpty(itemDamagedRecords1)){
                for(ItemDamagedRecord itemDamagedRecord : itemDamagedRecords1){
                    itemDamagedRecord.setPatronBarcode(olePatronDocument.getBarcode());
                    itemDamagedRecords.add(itemDamagedRecord);
                }

            }
        }
        List<ItemDamagedRecord> itemDamagedRecords1 = (List<ItemDamagedRecord>)KRADServiceLocator.getBusinessObjectService().save(itemDamagedRecords);
    }


    public void updateRenewalHistory(OlePatronDocument olePatronDocument,String olePatronBarcode){
        requestMap.clear();
        requestMap.put("patronBarcode",olePatronBarcode);
        List<OleRenewalHistory> oleRenewalHistorys = new ArrayList<OleRenewalHistory>();
        List<OleRenewalHistory> oleRenewalHistoryList = (List<OleRenewalHistory>)KRADServiceLocator.getBusinessObjectService().findMatching(OleRenewalHistory.class,requestMap);
        if(CollectionUtils.isNotEmpty(oleRenewalHistoryList)){
            for(OleRenewalHistory oleRenewalHistory : oleRenewalHistoryList){
                oleRenewalHistory.setPatronBarcode(olePatronDocument.getBarcode());
                oleRenewalHistorys.add(oleRenewalHistory);
            }

        }
        List<OleRenewalHistory> oleRenewalHistories = (List<OleRenewalHistory>)KRADServiceLocator.getBusinessObjectService().save(oleRenewalHistorys);
    }
}
