package org.kuali.ole.dsng.dao;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/3/2015.
 */
@Repository("bibDAO")
@Scope("prototype")
public class BibDAOImpl extends OleDsNGDAOBase implements BibDAO {

    public BibRecord save(BibRecord bibRecord) {
        return getBusinessObjectService().save(bibRecord);
    }

    @Override
    public List<BibRecord> saveAll(List<BibRecord> bibRecords) {
        return (List<BibRecord>) getBusinessObjectService().save(bibRecords);
    }

    public BibRecord retrieveBibById(String id) {
        return getBusinessObjectService().findBySinglePrimaryKey(BibRecord.class, id);
    }

    @Override
    public void deleteBibTreeRecord(String bibId) {
        bibId = DocumentUniqueIDPrefix.getDocumentId(bibId);
        deleteHoldingTrees(bibId);
        deleteBib(bibId);
        deleteBibInfo(bibId);
    }

    private void deleteHoldingTrees(String bibId) {
        Map map = new HashMap();
        map.put(OleNGConstants.BIB_ID, bibId);
        List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord.class, map);
        if (CollectionUtils.isNotEmpty(holdingsRecords)) {
            for (HoldingsRecord holdingsRecord : holdingsRecords) {
                deleteHoldingTree(holdingsRecord);
            }
        }
    }

    private void deleteBib(String bibId) {
        Map map = new HashMap();
        map.put(OleNGConstants.BIB_ID, bibId);
        getBusinessObjectService().deleteMatching(BibRecord.class, map);
    }

    private void deleteBibInfo(String bibId) {
        Map map = new HashMap();
        map.put(OleNGConstants.BIB_ID_STR, bibId);
        getBusinessObjectService().deleteMatching(BibInfoRecord.class, map);
    }

    private void deleteHoldingTree(HoldingsRecord holdingsRecord) {
        deleteItems(holdingsRecord.getHoldingsId());
        deleteHolding(holdingsRecord);
    }

    private void deleteItems(String holdingsId) {
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, getHoldingsMap(holdingsId));
        if (CollectionUtils.isNotEmpty(itemRecords)) {
            for (ItemRecord itemRecord : itemRecords) {
                deleteItem(itemRecord);
            }
        }
    }

    private void deleteItem(ItemRecord itemRecord) {
        deleteItemChildRecords(itemRecord);
        getBusinessObjectService().delete(itemRecord);
    }

    private void deleteHolding(HoldingsRecord holdingsRecord) {
        deleteHoldingChildRecords(holdingsRecord);
        getBusinessObjectService().delete(holdingsRecord);
    }

    private void deleteHoldingChildRecords(HoldingsRecord holdingsRecord) {
        Map holdingsMap = getHoldingsMap(holdingsRecord.getHoldingsId());
        List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords = (List<ExtentOfOwnerShipRecord>) getBusinessObjectService().findMatching(ExtentOfOwnerShipRecord.class, holdingsMap);
        if (CollectionUtils.isNotEmpty(extentOfOwnerShipRecords)) {
            for (int j = 0; j < extentOfOwnerShipRecords.size(); j++) {
                List<ExtentNoteRecord> extentNoteRecords = extentOfOwnerShipRecords.get(j).getExtentNoteRecords();
                if (CollectionUtils.isNotEmpty(extentNoteRecords)) {
                    getBusinessObjectService().delete(extentNoteRecords);
                }
            }
            getBusinessObjectService().delete(extentOfOwnerShipRecords);
        }
        if (CollectionUtils.isNotEmpty(holdingsRecord.getHoldingsNoteRecords())) {
            List<HoldingsNoteRecord> holdingsNoteRecords = holdingsRecord.getHoldingsNoteRecords();
            getBusinessObjectService().delete(holdingsNoteRecords);
        }
        if (CollectionUtils.isNotEmpty(holdingsRecord.getAccessUriRecords())) {
            List<HoldingsUriRecord> accessUriRecords = holdingsRecord.getAccessUriRecords();
            getBusinessObjectService().delete(accessUriRecords);
        }
        if (CollectionUtils.isNotEmpty(holdingsRecord.getHoldingsUriRecords())) {
            List<HoldingsUriRecord> holdingsUriRecords = holdingsRecord.getHoldingsUriRecords();
            getBusinessObjectService().delete(holdingsUriRecords);
        }
        holdingsRecord.setCallNumberTypeId(null);
        holdingsRecord.setReceiptStatusId(null);
        getBusinessObjectService().deleteMatching(EInstanceCoverageRecord.class, holdingsMap);
        getBusinessObjectService().deleteMatching(EInstancePerpetualAccessRecord.class, holdingsMap);
        getBusinessObjectService().deleteMatching(HoldingsAccessLocation.class, holdingsMap);
        getBusinessObjectService().deleteMatching(HoldingsStatisticalSearchRecord.class, holdingsMap);
        getBusinessObjectService().deleteMatching(OLEHoldingsDonorRecord.class, holdingsMap);
    }

    private void deleteItemChildRecords(ItemRecord itemRecord) {
        if (CollectionUtils.isNotEmpty(itemRecord.getFormerIdentifierRecords())) {
            List<FormerIdentifierRecord> formerIdentifierRecords = itemRecord.getFormerIdentifierRecords();
            getBusinessObjectService().delete(formerIdentifierRecords);
        }
        if (CollectionUtils.isNotEmpty(itemRecord.getItemNoteRecords())) {
            List<ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
            getBusinessObjectService().delete(itemNoteRecords);
        }
        if (CollectionUtils.isNotEmpty(itemRecord.getLocationsCheckinCountRecords())) {
            List<LocationsCheckinCountRecord> locationsCheckinCountRecords = itemRecord.getLocationsCheckinCountRecords();
            getBusinessObjectService().delete(locationsCheckinCountRecords);
        }
        if (CollectionUtils.isNotEmpty(itemRecord.getDonorList())) {
            List<OLEItemDonorRecord> itemRecordDonorList = itemRecord.getDonorList();
            getBusinessObjectService().delete(itemRecordDonorList);
        }
        itemRecord.setItemStatusId(null);
        itemRecord.setItemTypeId(null);
        itemRecord.setTempItemTypeId(null);
        itemRecord.setStatisticalSearchId(null);
        itemRecord.setHighDensityStorageId(null);
        Map map = new HashMap();
        map.put(OleNGConstants.ITEM_ID, itemRecord.getItemId());
        getBusinessObjectService().deleteMatching(ItemStatisticalSearchRecord.class, map);
    }

}
