package org.kuali.ole.deliver.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by pvsubrah on 6/5/15.
 */
public class ItemInfoUtil extends OLEUtil {
    private static ItemInfoUtil itemInfoUtil;

    private OleLoanDocumentDaoOjb loanDaoOjb;

    public enum LEVEL_CODES {
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

    public OleItemRecordForCirc getOleItemRecordForCirc(ItemRecord itemRecord, OleCirculationDesk oleCirculationDesk) {
        OleItemRecordForCirc oleItemRecordForCirc = new OleItemRecordForCirc();
        oleItemRecordForCirc.setOperatorCircLocation(oleCirculationDesk);
        oleItemRecordForCirc.setItemRecord(itemRecord);

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
        populateItemLocation(oleItemRecordForCirc, location);

        OleDeliverRequestBo requestBO = getRequestBO(itemRecord.getBarCode());
        oleItemRecordForCirc.setOleDeliverRequestBo(requestBO);

        determineOperatorCircLocAndPickupLocationIsTheSame(oleItemRecordForCirc);
        determineOperatorCircLocAndCheckingIsTheSame(oleItemRecordForCirc);
        determineifLocationMappedToCircDesk(oleItemRecordForCirc);

        if(null != requestBO && StringUtils.isNotBlank(requestBO.getPickUpLocationCode())){
            oleItemRecordForCirc.setRouteToLocation(requestBO.getPickUpLocationCode());
        }

        return oleItemRecordForCirc;
    }

    private void determineOperatorCircLocAndPickupLocationIsTheSame(OleItemRecordForCirc oleItemRecordForCirc) {
        oleItemRecordForCirc.setIsPickupLocationSameAsOperatorCircLocation(false);
        OleCirculationDesk olePickUpLocation = oleItemRecordForCirc.getPickupLocation();
        OleCirculationDesk operatorCircLocation = oleItemRecordForCirc.getOperatorCircLocation();
        if (null != olePickUpLocation && null != operatorCircLocation) {
            if (olePickUpLocation.getCirculationDeskId().equals(operatorCircLocation.getCirculationDeskId())) {
                oleItemRecordForCirc.setIsPickupLocationSameAsOperatorCircLocation(true);
            } else {
                oleItemRecordForCirc.setRouteToLocation(olePickUpLocation.getCirculationDeskCode());
            }
        }
    }

    private void determineifLocationMappedToCircDesk(OleItemRecordForCirc oleItemRecordForCirc) {
        oleItemRecordForCirc.setIsLocationMappedToCircDesk(false);
        String itemLocation = oleItemRecordForCirc.getItemFullPathLocation();
        List<OleCirculationDeskLocation> oleCirculationDeskLocations = (List<OleCirculationDeskLocation>) getBusinessObjectService().findAll(OleCirculationDeskLocation.class);
        if (CollectionUtils.isNotEmpty(oleCirculationDeskLocations)) {
            List<OleLocation> oleLocationList = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findAll(OleLocation.class);
            for (Iterator<OleCirculationDeskLocation> oleCirculationDeskLocationIterator = oleCirculationDeskLocations.iterator(); oleCirculationDeskLocationIterator.hasNext(); ) {
                OleCirculationDeskLocation oleCirculationDeskLocation = oleCirculationDeskLocationIterator.next();
                if (StringUtils.isBlank(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()) && getCirculationFullLocationCode(oleCirculationDeskLocation, oleLocationList).equalsIgnoreCase(itemLocation)) {
                    oleItemRecordForCirc.setRouteToLocation(oleCirculationDeskLocation.getOleCirculationDesk().getCirculationDeskCode());
                    oleItemRecordForCirc.setIsLocationMappedToCircDesk(true);
                    break;
                }
            }
        }
    }

    private String getCirculationFullLocationCode(OleCirculationDeskLocation oleCirculationDeskLocation, List<OleLocation> oleLocationList) {
        String fullLocationCode = oleCirculationDeskLocation.getCirculationLocationCode();
        if (oleLocationList.size() > 0) {
            for (OleLocation deskLocation : oleLocationList){
                if (deskLocation.getLocationId().equals(oleCirculationDeskLocation.getCirculationDeskLocation())){
                    if (deskLocation.getParentLocationId() != null) {
                        deskLocation = deskLocation.getOleLocation();
                    } else {
                        return fullLocationCode;
                    }
                    while (deskLocation != null) {
                        fullLocationCode = deskLocation.getLocationCode() + "/" + fullLocationCode;
                        if (deskLocation.getParentLocationId() != null) {
                            deskLocation = deskLocation.getOleLocation();
                        } else
                            deskLocation = null;
                    }
                }
            }
            return fullLocationCode;
        }
        return fullLocationCode;
    }

    private void determineOperatorCircLocAndCheckingIsTheSame(OleItemRecordForCirc oleItemRecordForCirc) {
        oleItemRecordForCirc.setIsCheckinLocationSameAsHomeLocation(false);
        String itemFullPathLocation = oleItemRecordForCirc.getItemFullPathLocation();
        OleCirculationDesk operatorCircLocation = oleItemRecordForCirc.getOperatorCircLocation();
        if (null != itemFullPathLocation && null != operatorCircLocation) {
            List<OleCirculationDeskLocation> oleCirculationDeskLocations = filterCirculationDeskLocationsForOmitPickupLocation(operatorCircLocation);
            for (Iterator<OleCirculationDeskLocation> iterator = oleCirculationDeskLocations.iterator(); iterator.hasNext(); ) {
                OleCirculationDeskLocation oleCirculationDeskLocation = iterator.next();
                if (oleCirculationDeskLocation.getLocation().getFullLocationPath().equals(itemFullPathLocation)) {
                    oleItemRecordForCirc.setIsCheckinLocationSameAsHomeLocation(true);
                    break;
                }
            }
        }
    }

    private List<OleCirculationDeskLocation> filterCirculationDeskLocationsForOmitPickupLocation(OleCirculationDesk operatorCircLocation) {
        List<OleCirculationDeskLocation> filteredCirculationDeskLocations = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(operatorCircLocation.getOleCirculationDeskLocations())) {
            for (Iterator<OleCirculationDeskLocation> iterator = operatorCircLocation.getOleCirculationDeskLocations().iterator(); iterator.hasNext(); ) {
                OleCirculationDeskLocation oleCirculationDeskLocation = iterator.next();
                if (StringUtils.isBlank(oleCirculationDeskLocation.getCirculationPickUpDeskLocation())) {
                    filteredCirculationDeskLocations.add(oleCirculationDeskLocation);
                }
            }
        }
        return filteredCirculationDeskLocations;
    }

    public void populateItemLocation(OleItemRecordForCirc oleItemRecordForCirc,String location) {
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

    public OleDeliverRequestBo getRequestBO(String itemBarcode) {
        OleDeliverRequestBo oleDeliverRequestBo = getLoanDaoOjb().getPrioritizedRequest(itemBarcode);
        if(null != oleDeliverRequestBo){
            return oleDeliverRequestBo;
        }
        return null;
    }

    public OleDeliverRequestBo getRequestByPatronId(String patronId, String itemBarcode) {
        Map requestMap = new HashMap();
        requestMap.put("borrowerId", patronId);
        requestMap.put("itemId", itemBarcode);
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        return oleDeliverRequestBos != null && oleDeliverRequestBos.size() > 0 ? oleDeliverRequestBos.get(0) : null;
    }

    public ItemRecord getItemRecordByBarcode(String itemBarcode) {
        ItemRecord itemRecord = null;
        HashMap<String, String> criteriaMap = new HashMap<>();
        criteriaMap.put("barCode", itemBarcode);
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class,
                criteriaMap);
        if (null != itemRecords && !itemRecords.isEmpty()) {
            itemRecord = itemRecords.get(0);
        }

        return itemRecord;
    }
    public OleLoanDocumentDaoOjb getLoanDaoOjb() {
        if (null == loanDaoOjb) {
            loanDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return loanDaoOjb;
    }

    public void setLoanDaoOjb(OleLoanDocumentDaoOjb loanDaoOjb) {
        this.loanDaoOjb = loanDaoOjb;
    }
}
