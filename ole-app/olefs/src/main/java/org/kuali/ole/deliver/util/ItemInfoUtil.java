package org.kuali.ole.deliver.util;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 6/5/15.
 */
public class ItemInfoUtil {
    private static ItemInfoUtil itemInfoUtil;
    private BusinessObjectService businessObjectService;


    private enum LEVEL_CODES {
        INSTITUTION(1), CAMPUS(2), LIBRARY(3), COLLECTION(4), SHELVING(5);
        private int id;

        LEVEL_CODES(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    };


    private ItemInfoUtil() {
    }

    public static ItemInfoUtil getInstance() {
        if (null == itemInfoUtil) {
            itemInfoUtil = new ItemInfoUtil();
        }
        return itemInfoUtil;
    }

    public OleItemRecordForCirc getOleItemRecordForCirc(ItemRecord itemRecord) {
        OleItemRecordForCirc oleItemRecordForCirc = new OleItemRecordForCirc();
        ItemTypeRecord itemTempTypeRecord = itemRecord.getItemTypeRecord();
        String itemType = itemTempTypeRecord.getCode();
        if (itemType == null) {
           itemType = itemRecord.getItemTypeRecord().getCode();
        }
        oleItemRecordForCirc.setItemType(itemType);
        oleItemRecordForCirc.setItemStatusRecord(itemRecord.getItemStatusRecord());
        oleItemRecordForCirc.setItemUUID(itemRecord.getItemId());

        String location = null;
        location = itemRecord.getLocation();

        if (StringUtils.isBlank(location)) {
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            Map<String, String> criteriaMap = new HashMap();
            criteriaMap.put("holdingsId", itemRecord.getHoldingsId());
            List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) businessObjectService.findMatching(HoldingsRecord
                            .class,
                    criteriaMap);

            HoldingsRecord holdingsRecord = holdingsRecords.get(0);
            location = holdingsRecord.getLocation();
        }
        populateItemLocation(oleItemRecordForCirc,location);

        return oleItemRecordForCirc;
    }

    private void populateItemLocation(OleItemRecordForCirc oleItemRecordForCirc,String location) {
        StringTokenizer stringTokenizer = new StringTokenizer(location, "/");

        while (stringTokenizer.hasMoreTokens()) {
            String locationCode = stringTokenizer.nextToken();
            int levelIdForLocationCode = getLevelIdForLocationCode(locationCode);
            if (levelIdForLocationCode == LEVEL_CODES.INSTITUTION.getId()) {
               oleItemRecordForCirc.setInstitutionLocation(locationCode);
            } else if (levelIdForLocationCode == LEVEL_CODES.CAMPUS.getId()) {
                oleItemRecordForCirc.setCampusLocation(locationCode);
            }
            if (levelIdForLocationCode == LEVEL_CODES.LIBRARY.getId()) {
               oleItemRecordForCirc.setItemLibraryLocation(locationCode);
            }
            if (levelIdForLocationCode == LEVEL_CODES.COLLECTION.getId()) {
               oleItemRecordForCirc.setCollectionLocation(locationCode);
            }
            if (levelIdForLocationCode == LEVEL_CODES.SHELVING.getId()) {
               oleItemRecordForCirc.setItemLocation(locationCode);
            }
        }
    }

    private int getLevelIdForLocationCode(String locationCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("locationCode", locationCode);
        List<OleLocation> locations = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, map);
        if (locations.size() > 0) {
            String locationId = locations.get(0).getOleLocationLevel().getLevelId();
            return Integer.valueOf(locationId);
        }
        return 0;

    }


    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
