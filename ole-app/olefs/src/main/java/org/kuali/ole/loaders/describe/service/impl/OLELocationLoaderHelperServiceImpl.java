package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLELocationBo;
import org.kuali.ole.loaders.describe.service.OLELocationLoaderHelperService;
import org.kuali.ole.service.OleLocationService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 2/4/15.
 */
public class OLELocationLoaderHelperServiceImpl implements OLELocationLoaderHelperService {

    private static final Logger LOG = Logger.getLogger(OLELocationLoaderHelperServiceImpl.class);
    private BusinessObjectService businessObjectService;
    private OleLocationService oleLocationService;
    private OLELoaderService oleLoaderService;

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OleLocationService getOleLocationService() {
        if(oleLocationService == null){
            oleLocationService = (OleLocationService) SpringContext.getBean("oleLocationService");
        }
        return oleLocationService;
    }

    public void setOleLocationService(OleLocationService oleLocationService) {
        this.oleLocationService = oleLocationService;
    }

    public OLELoaderService getOleLoaderService() {
        if(oleLoaderService == null ){
            oleLoaderService = new OLELoaderServiceImpl();
        }
        return oleLoaderService;
    }

    public void setOleLoaderService(OLELoaderService oleLoaderService) {
        this.oleLoaderService = oleLoaderService;
    }

    @Override
    public boolean isLocationLevelExistById(String locationLevelId) {
        Map locationLevelCodeMap = new HashMap();
        locationLevelCodeMap.put("levelId",locationLevelId);
        List<OleLocationLevel> locationLevel = (List<OleLocationLevel>)getBusinessObjectService().findMatching(OleLocationLevel.class,locationLevelCodeMap);
        if(locationLevel.size()>0)
            return false;
        else
            return true;
    }

    @Override
    public boolean isParentLocationLevelExist(String parentLocationLevelId) {
        if(StringUtils.isNotBlank(parentLocationLevelId)){
            Map  parentLocationLevelMap = new HashMap();
            parentLocationLevelMap.put("levelId",parentLocationLevelId);
            List<OleLocationLevel> locationLevel = (List<OleLocationLevel>)getBusinessObjectService().findMatching(OleLocationLevel.class,parentLocationLevelMap);
            if(locationLevel.size()>0)
                return false;
            else
                return true;
        }
        return false;
    }

    @Override
    public boolean isParentLocationExist(String parentLocationId) {
        if(StringUtils.isNotBlank(parentLocationId)){
            Map  parentLocationCodeMap = new HashMap();
            parentLocationCodeMap.put("locationId",parentLocationId);
            List<OleLocation> locationLevel = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class,parentLocationCodeMap);
            if(locationLevel.size()>0)
                return false;
            else
                return true;
        }
        return false;
    }

    @Override
    public boolean isLocationExistByCode(String locationCode) {
        Map localtionCodeMap = new HashMap();
        localtionCodeMap.put("locationCode", locationCode);
        List<OleLocation> location = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class,localtionCodeMap);
        if(location.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public OleLocation createOleLocation(OLELocationBo oleLocationBo) {
        LOG.info("Inside Create OleLocation. Location Code : " + oleLocationBo.getLocationCode());
        OleLocation oleLocation = new OleLocation();
        oleLocation.setLocationCode(oleLocationBo.getLocationCode());
        oleLocation.setLocationName(oleLocationBo.getLocationName());
        String locationLevelId = oleLocationBo.getLocationLevelId();
        OleLocationLevel oleLocationLevel;
        Map localtionLevelCd = new HashMap();
        localtionLevelCd.put("levelId",locationLevelId);
        List<OleLocationLevel> levelList = (List<OleLocationLevel>)getBusinessObjectService().findMatching(OleLocationLevel.class,localtionLevelCd);
        if(levelList.size()>0){
            oleLocationLevel =  levelList.get(0);
            oleLocation.setLevelId(oleLocationLevel.getLevelId());
        } else{
            throw new RuntimeException();
        }
        String  parentLocationId =  oleLocationBo.getParentLocationId();
        OleLocation oleLocations;
        if(StringUtils.isNotBlank(parentLocationId)){
            Map parentLevelcd = new HashMap();
            parentLevelcd.put("locationId",parentLocationId);
            List<OleLocation> parentList = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class,parentLevelcd);
            if(parentList.size()>0){
                oleLocation.setParentLocationId(parentList.get(0).getLocationId());
                oleLocation.setOleLocation(parentList.get(0));
            }else {
                oleLocation.setParentLocationId(null);
            }
        }else{
            oleLocation.setParentLocationId(null);
        }

        getOleLocationService().createLocation(oleLocation);
        return oleLocation;
    }

    @Override
    public OLELoaderResponseBo updateOleLocation(OleLocation oleLocation, OLELocationBo oleLocationBo,HttpContext context) {
        LOG.info("Inside update OleLocation. Location Code : " + oleLocationBo.getLocationCode());
        if(StringUtils.isNotBlank(oleLocationBo.getLocationCode()))
            oleLocation.setLocationCode(oleLocationBo.getLocationCode());
        if(StringUtils.isNotBlank(oleLocationBo.getLocationName()))
            oleLocation.setLocationName(oleLocationBo.getLocationName());
        String locationLevelId = oleLocationBo.getLocationLevelId();
        if(StringUtils.isNotBlank(locationLevelId)){
            OleLocationLevel oleLocationLevel;
            Map localtionLevelCd = new HashMap();
            localtionLevelCd.put("levelId",locationLevelId);
            List<OleLocationLevel> levelList = (List<OleLocationLevel>)getBusinessObjectService().findMatching(OleLocationLevel.class,localtionLevelCd);
            if(levelList.size()>0){
                oleLocationLevel =  levelList.get(0);
                oleLocation.setLevelId(oleLocationLevel.getLevelId());
                oleLocation.setOleLocationLevel(oleLocationLevel);
            } else{
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_LEVEL_NOT_EXIST,OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
            }
        }

        String  parentLocationId =  oleLocationBo.getLocationLevelId();
        if(StringUtils.isNotBlank(parentLocationId)){
            Map parentLevelcd = new HashMap();
            parentLevelcd.put("locationId",parentLocationId);
            List<OleLocation> parentList = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class, parentLevelcd);
            if(parentList.size()>0){
                oleLocation.setParentLocationId(parentList.get(0).getLocationId());
                oleLocation.setOleLocation(parentList.get(0));
            }else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.PARENT_LOCATION_NOT_EXIST,OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
            }
        }
        try{
            if(getOleLocationService().updateLocation(oleLocation)){
                String details = formLocationExportResponse(oleLocation,OLELoaderConstants.OLELoaderContext.LOCATION,context.getRequest().getRequestUri().toASCIIString()).toString();
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_SUCCESS,OLELoaderConstants.OLEloaderMessage.LOCATION_SUCCESS, OLELoaderConstants.OLEloaderStatus.LOCATION_SUCCESS,details);
            }else{
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_FAILED,OLELoaderConstants.OLEloaderMessage.LOCATION_FAILED, OLELoaderConstants.OLEloaderStatus.LOCATION_FAILED);
            }
        }catch(Exception e){
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_FAILED,OLELoaderConstants.OLEloaderMessage.LOCATION_FAILED, OLELoaderConstants.OLEloaderStatus.LOCATION_FAILED);
        }
    }

    @Override
    public OleLocation getLocationById(String locationId) {
        Map locationMap = new HashMap();
        locationMap.put("locationId", locationId);
        return getBusinessObjectService().findByPrimaryKey(OleLocation.class, locationMap);
    }

    @Override
    public OleLocation getLocationByCode(String locationCode) {
        Map locationMap = new HashMap();
        locationMap.put("locationCode", locationCode);
        List<OleLocation> oleLocationList = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, locationMap);
        return (oleLocationList != null && oleLocationList.size() > 0) ? oleLocationList.get(0) : null;
    }

    @Override
    public List<OleLocation> getAllLocation() {
        return (List<OleLocation>) getBusinessObjectService().findAll(OleLocation.class);
    }

    public Object formLocationExportResponse(Object object, String locationContext, String uri){
        OleLocation oleLocation = (OleLocation) object;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("@context",locationContext);
            jsonObject.put("@id",OLELoaderConstants.LOCATION_URI + OLELoaderConstants.SLASH + oleLocation.getLocationId());
            jsonObject.put("code",oleLocation.getLocationCode());
            jsonObject.put("name",oleLocation.getLocationName());
            JSONObject parentJsonObject = new JSONObject();
            if(oleLocation.getOleLocation() != null){
                parentJsonObject.put("@id",OLELoaderConstants.LOCATION_URI + OLELoaderConstants.SLASH + oleLocation.getOleLocation().getLocationId());
                parentJsonObject.put("name",oleLocation.getOleLocation().getLocationName());
            }
            jsonObject.put("parent",parentJsonObject);
            JSONObject locationLevelJsonObject = new JSONObject();
            if(oleLocation.getOleLocationLevel() != null){
                locationLevelJsonObject.put("@context", OLELoaderConstants.OLELoaderContext.LOCATION_LEVEL);
                locationLevelJsonObject.put("@id",OLELoaderConstants.LOCATION_LEVEL_URI  + OLELoaderConstants.SLASH + oleLocation.getOleLocationLevel().getLevelId());
                locationLevelJsonObject.put("name",oleLocation.getOleLocationLevel().getLevelName());
            }
            jsonObject.put("level",locationLevelJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public Object formAllLocationExportResponse(HttpContext context,List<OleLocation> oleLocationList, String locationContext, String uri) {
        JSONObject jsonResponseObject = new JSONObject();
        JSONArray paginationArray = new JSONArray();
        JSONObject paginationObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            jsonResponseObject.put("@context",locationContext);
            int startIndex = 0;
            int maxResults = 50;
            boolean validStartIndex = true;
            if(context.getRequest().getQueryParameters().containsKey("start")){
                try{
                    String start = context.getRequest().getQueryParameters().getFirst("start");
                    startIndex = Integer.parseInt(start);
                    if(startIndex < 0)
                        startIndex =0;
                    if(startIndex > oleLocationList.size()){
                        validStartIndex = false;
                    }
                }catch (Exception e){
                    LOG.info("Invalid Start Index : " + e.getMessage());
                    startIndex = 0;
                }
            }
            if(context.getRequest().getQueryParameters().containsKey("maxResults")){
                try{
                    String maxCount = context.getRequest().getQueryParameters().getFirst("maxResults");
                    maxResults = Integer.parseInt(maxCount);
                    if(maxResults < 0)
                        maxResults =50;
                }catch (Exception e){
                    LOG.info("Invalid Max Result count : " + e.getMessage());
                    maxResults = 50;
                }
            }
            int loopIterationEnd = 0;
            if(startIndex+maxResults > oleLocationList.size())
                loopIterationEnd = oleLocationList.size();
            else{
                loopIterationEnd = startIndex+maxResults;
            }

            if(validStartIndex){
                if(startIndex != 0){
                    paginationObject.put("rel","prev");
                    paginationObject.put("href",OLELoaderConstants.LOCATION_URI + "?start="+((startIndex-1)-maxResults < 0 ? 0 : (startIndex-1)-maxResults)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }
                if(loopIterationEnd != oleLocationList.size()){
                    paginationObject = new JSONObject();
                    paginationObject.put("rel","next");
                    paginationObject.put("href",OLELoaderConstants.LOCATION_URI + "?start="+(loopIterationEnd+1)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }

                jsonResponseObject.put("links",paginationArray);
                for(int index = (startIndex == 0 ? 0 : startIndex-1) ; index < loopIterationEnd-1 ; index++){
                    OleLocation oleLocation = oleLocationList.get(index);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("@id",OLELoaderConstants.LOCATION_URI + OLELoaderConstants.SLASH + oleLocation.getLocationId());
                        jsonObject.put("code",oleLocation.getLocationCode());
                        jsonObject.put("name",oleLocation.getLocationName());
                        JSONObject parentJsonObject = new JSONObject();
                        if(oleLocation.getOleLocation() != null){
                            parentJsonObject.put("@id",OLELoaderConstants.LOCATION_URI + OLELoaderConstants.SLASH + oleLocation.getOleLocation().getLocationId());
                            parentJsonObject.put("name",oleLocation.getOleLocation().getLocationName());
                        }
                        jsonObject.put("parent",parentJsonObject);
                        JSONObject locationLevelJsonObject = new JSONObject();
                        if(oleLocation.getOleLocationLevel() != null){
                            locationLevelJsonObject.put("@context", OLELoaderConstants.OLELoaderContext.LOCATION_LEVEL);
                            locationLevelJsonObject.put("@id",OLELoaderConstants.LOCATION_LEVEL_URI + OLELoaderConstants.SLASH + oleLocation.getOleLocationLevel().getLevelId());
                            locationLevelJsonObject.put("name",oleLocation.getOleLocationLevel().getLevelName());
                        }
                        jsonObject.put("level",locationLevelJsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                jsonResponseObject.put("items",jsonArray);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonResponseObject;
    }

    @Override
    public OleLocationLevel getLocationLevelById(String locationLevelId) {
        Map locationLevelMap = new HashMap();
        locationLevelMap.put("levelId", locationLevelId);
        return getBusinessObjectService().findByPrimaryKey(OleLocationLevel.class, locationLevelMap);
    }

    @Override
    public OleLocationLevel getLocationLevelByCode(String locationLevelCode) {
        Map locationLevelMap = new HashMap();
        locationLevelMap.put("levelCode", locationLevelCode);
        return getBusinessObjectService().findByPrimaryKey(OleLocationLevel.class, locationLevelMap);
    }

    @Override
    public Object formLocationLevelExportResponse(Object object, String locationLevelContext, String uri) {
        OleLocationLevel oleLocationLevel = (OleLocationLevel) object;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("@context",locationLevelContext);
            jsonObject.put("@id",OLELoaderConstants.LOCATION_LEVEL_URI + OLELoaderConstants.SLASH + oleLocationLevel.getLevelId());
            jsonObject.put("code",oleLocationLevel.getLevelCode());
            jsonObject.put("name",oleLocationLevel.getLevelName());
            JSONObject parentJsonObject = new JSONObject();
            if(oleLocationLevel.getOleLocationLevel() != null){
                parentJsonObject.put("@id",OLELoaderConstants.LOCATION_LEVEL_URI + OLELoaderConstants.SLASH + oleLocationLevel.getOleLocationLevel().getLevelId());
                parentJsonObject.put("name",oleLocationLevel.getOleLocationLevel().getLevelName());
            }
            jsonObject.put("parent",parentJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
