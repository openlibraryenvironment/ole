package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderRestClient;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.*;
import org.kuali.ole.loaders.describe.service.OLELocationLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLELocationLoaderService;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 2/4/15.
 */
public class OLELocationLoaderServiceImpl implements OLELocationLoaderService {

    private static final Logger LOG = Logger.getLogger(OLELocationLoaderServiceImpl.class);

    OLELocationLoaderHelperService oleLocationLoaderHelperService = new OLELocationLoaderHelperServiceImpl();
    private OLELoaderService oleLoaderService;
    
    public OLELoaderService getOleLoaderService() {
        if(oleLoaderService == null ){
            oleLoaderService = new OLELoaderServiceImpl();
        }
        return oleLoaderService;
    }

    public void setOleLoaderService(OLELoaderService oleLoaderService) {
        this.oleLoaderService = oleLoaderService;
    }

    public OLELocationLoaderHelperService getOleLocationLoaderHelperService() {
        return oleLocationLoaderHelperService;
    }

    public void setOleLocationLoaderHelperService(OLELocationLoaderHelperService oleLocationLoaderHelperService) {
        this.oleLocationLoaderHelperService = oleLocationLoaderHelperService;
    }

    @Override
    public Object importLocations(String bodyContent, HttpContext context) {
        LOG.info("Inside importLocations method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        List<Integer> rejectLocationList = new ArrayList<Integer>();
        List<JSONObject> createdLocationObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray locationJsonArray = getJsonArrayFromString(items);
                    for (int index = 0; index < locationJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OLELocationBo oleLocationBo = new OLELocationBo();
                        try {
                            jsonObject = (JSONObject)locationJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        oleLocationBo.setLocationName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        oleLocationBo.setLocationCode(code);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("level")){
                                    JSONObject levelJsonObject = getJsonObjectFromString(getStringValueFromJsonObject(jsonObject,"level"));
                                    if(levelJsonObject != null){
                                        String levelUrl = getStringValueFromJsonObject(levelJsonObject, "@id");
                                        if(StringUtils.isNotBlank(levelUrl)){
                                            Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(levelUrl);
                                            if((Integer)restResponseMap.get("status")== 200){
                                                String levelResponseContent = (String)restResponseMap.get("content");
                                                JSONObject levelResponseObject = getJsonObjectFromString(levelResponseContent);
                                                if(levelResponseObject != null){
                                                    String urlId = getStringValueFromJsonObject(levelResponseObject, "@id");
                                                    if(StringUtils.isNotBlank(urlId)){
                                                        String levelId = urlId.substring(urlId.indexOf("/api/locationLevel/")+19);
                                                        oleLocationBo.setLocationLevelId(levelId);
                                                        validObject = true;
                                                    }
                                                }else{
                                                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_LEVEL_NOT_EXIST,
                                                            OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST,
                                                            OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
                                                }
                                            }else{
                                                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_LEVEL_NOT_EXIST,
                                                        OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST,
                                                        OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
                                            }



                                        }
                                    }

                                }
                                if(jsonObject.has("parent")){
                                    JSONObject parentJsonObject = getJsonObjectFromString(getStringValueFromJsonObject(jsonObject,"parent"));
                                    if(parentJsonObject != null){
                                        String parentUrl = getStringValueFromJsonObject(parentJsonObject,"@id");
                                        if(StringUtils.isNotBlank(parentUrl)){
                                            Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(parentUrl);
                                            if((Integer)restResponseMap.get("status")== 200){
                                                String parentResponseContent = (String)restResponseMap.get("content");
                                                JSONObject parentResponseObject = getJsonObjectFromString(parentResponseContent);
                                                if(parentResponseObject != null){
                                                    String urlId = getStringValueFromJsonObject(parentResponseObject,"@id");
                                                    if(StringUtils.isNotBlank(urlId)){
                                                        String parentId = urlId.substring(urlId.indexOf("/api/location/")+14);
                                                        oleLocationBo.setParentLocationId(parentId);
                                                        validObject = true;
                                                    }
                                                }else{
                                                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.PARENT_LOCATION_NOT_EXIST,
                                                            OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST,
                                                            OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
                                                }
                                            }else{
                                                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.PARENT_LOCATION_NOT_EXIST,
                                                        OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST,
                                                        OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
                                            }


                                        }
                                    }
                                }
                            }
                            if(oleLocationBo != null && validObject){
                                if (!oleLocationLoaderHelperService.isLocationExistByCode(oleLocationBo.getLocationCode())) {
                                    if (oleLocationLoaderHelperService.isLocationLevelExistById(oleLocationBo.getLocationLevelId())) {
                                        rejectLocationList.add(index+1);
                                    } else if (oleLocationLoaderHelperService.isParentLocationExist(oleLocationBo.getParentLocationId())) {
                                        rejectLocationList.add(index + 1);
                                    } else {
                                        try {
                                            OleLocation oleLocation = oleLocationLoaderHelperService.createOleLocation(oleLocationBo);
                                            createdLocationObject.add((JSONObject)oleLocationLoaderHelperService.formLocationExportResponse(oleLocation, OLELoaderConstants.OLELoaderContext.LOCATION,
                                                    context.getRequest().getRequestUri().toASCIIString()));
                                        } catch (Exception e) {
                                            rejectLocationList.add(index+1);
                                        }
                                    }
                                } else {
                                    rejectLocationList.add(index+1);
                                }

                            }else{
                                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.BAD_REQUEST,OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectLocationList.add(index);
                        }
                    }
                }else{
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.BAD_REQUEST,OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
                }

            }
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.BAD_REQUEST,OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
        oleLoaderImportResponseBo.setOleRejectedBos(rejectLocationList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdLocationObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateLocationById(String locationId, String bodyContent,HttpContext context) {
        LOG.info("Inside updateLocationById method.");
        OLELocationBo oleLocationBo = new OLELocationBo();
        JSONObject jsonObject = getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(jsonObject != null){
            if(jsonObject.has("name")){
                String name = getStringValueFromJsonObject(jsonObject,"name");
                if(StringUtils.isNotBlank(name)){
                    oleLocationBo.setLocationName(name);
                    validObject = true;
                }
            }
            if(jsonObject.has("code")){
                String code = getStringValueFromJsonObject(jsonObject,"code");
                if(StringUtils.isNotBlank(code)){
                    oleLocationBo.setLocationCode(code);
                    validObject = true;
                }
            }
            if(jsonObject.has("level")){
                JSONObject levelJsonObject = getJsonObjectFromString(getStringValueFromJsonObject(jsonObject,"level"));
                if(levelJsonObject != null){
                    String levelUrl = getStringValueFromJsonObject(levelJsonObject, "@id");
                    if(StringUtils.isNotBlank(levelUrl)){
                        Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(levelUrl);
                        if((Integer)restResponseMap.get("status")== 200){
                            String levelResponseContent = (String)restResponseMap.get("content");
                            JSONObject levelResponseObject = getJsonObjectFromString(levelResponseContent);
                            if(levelResponseObject != null){
                                String urlId = getStringValueFromJsonObject(levelResponseObject, "@id");
                                if(StringUtils.isNotBlank(urlId)){
                                    String levelId = urlId.substring(urlId.indexOf("/api/locationLevel/")+19);
                                    oleLocationBo.setLocationLevelId(levelId);
                                    validObject = true;
                                }
                            }else{
                                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_LEVEL_NOT_EXIST,
                                        OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST,
                                        OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
                            }
                        }else{
                            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_LEVEL_NOT_EXIST,
                                    OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST,
                                    OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
                        }



                    }
                }

            }
            if(jsonObject.has("parent")){
                JSONObject parentJsonObject = getJsonObjectFromString(getStringValueFromJsonObject(jsonObject,"parent"));
                if(parentJsonObject != null){
                    String parentUrl = getStringValueFromJsonObject(parentJsonObject,"@id");
                    if(StringUtils.isNotBlank(parentUrl)){
                        Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(parentUrl);
                        if((Integer)restResponseMap.get("status")== 200){
                            String parentResponseContent = (String)restResponseMap.get("content");
                            JSONObject parentResponseObject = getJsonObjectFromString(parentResponseContent);
                            if(parentResponseObject != null){
                                String urlId = getStringValueFromJsonObject(parentResponseObject,"@id");
                                if(StringUtils.isNotBlank(urlId)){
                                    String parentId = urlId.substring(urlId.indexOf("/api/location/")+14);
                                    oleLocationBo.setParentLocationId(parentId);
                                    validObject = true;
                                }
                            }else{
                                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.PARENT_LOCATION_NOT_EXIST,
                                        OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST,
                                        OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
                            }
                        }else{
                            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.PARENT_LOCATION_NOT_EXIST,
                                    OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST,
                                    OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
                        }


                    }
                }
            }
        }
        if(oleLocationBo != null && validObject){
            if(StringUtils.isNotBlank(oleLocationBo.getLocationLevelId()) && oleLocationLoaderHelperService.isLocationLevelExistById(oleLocationBo.getLocationLevelId()))    {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_LEVEL_NOT_EXIST,
                        OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST,
                        OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
            }else if(StringUtils.isNotBlank(oleLocationBo.getParentLocationId()) && oleLocationLoaderHelperService.isParentLocationExist(oleLocationBo.getParentLocationId())) {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.PARENT_LOCATION_NOT_EXIST,
                        OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST,
                        OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
            }else{
                OleLocation oleLocation = oleLocationLoaderHelperService.getLocationById(locationId);
                if(oleLocation != null){
                    return oleLocationLoaderHelperService.updateOleLocation(oleLocation, oleLocationBo,context);
                }else{
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_NOT_EXIST,OLELoaderConstants.OLEloaderMessage.LOCATION_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_NOT_EXIST);
                }
            }

        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.BAD_REQUEST,OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object exportLocationById(String locationId) {
        LOG.info("Inside exportLocationById method.");
        OleLocation oleLocation = oleLocationLoaderHelperService.getLocationById(locationId);
        if(oleLocation != null){
            return oleLocation;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_NOT_EXIST,OLELoaderConstants.OLEloaderMessage.LOCATION_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_NOT_EXIST);
        }
    }

    @Override
    public Object exportLocationByCode(String locationCode) {
        LOG.info("Inside exportLocationByCode method.");
        OleLocation oleLocation = oleLocationLoaderHelperService.getLocationByCode(locationCode);
        if(oleLocation != null){
            return oleLocation;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_NOT_EXIST,OLELoaderConstants.OLEloaderMessage.LOCATION_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_NOT_EXIST);
        }
    }

    @Override
    public List<OleLocation> exportAllLocations(HttpContext context) {
        LOG.info("Inside exportAllLocation method.");
        List<OleLocation> oleLocationList = oleLocationLoaderHelperService.getAllLocation();
        return oleLocationList;
    }

    @Override
    public boolean validateLocationBo(OLELocationBo oleLocationBo) {
        boolean valid = true;
        if(StringUtils.isBlank(oleLocationBo.getLocationId()) && StringUtils.isBlank(oleLocationBo.getLocationCode()) &&
                StringUtils.isBlank(oleLocationBo.getLocationLevelId()) && StringUtils.isBlank(oleLocationBo.getLocationName()) &&
                StringUtils.isBlank(oleLocationBo.getParentLocationId())){
            valid = false;
        }
        return valid;
    }

    @Override
    public Object exportLocationLevelById(String locationLevelId) {
        LOG.info("Inside exportLocationLevelById method.");
        OleLocationLevel oleLocationLevel = oleLocationLoaderHelperService.getLocationLevelById(locationLevelId);
        if(oleLocationLevel != null){
            return oleLocationLevel;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_LEVEL_NOT_EXIST,OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
        }
    }

    @Override
    public Object exportLocationLevelByCode(String locationLevelCode) {
        LOG.info("Inside exportLocationLevelByCdde method.");
        OleLocationLevel oleLocationLevel = oleLocationLoaderHelperService.getLocationLevelByCode(locationLevelCode);
        if(oleLocationLevel != null){
            return oleLocationLevel;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderCode.LOCATION_LEVEL_NOT_EXIST,OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
        }
    }

    public JSONObject getJsonObjectFromString(String body){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray getJsonArrayFromString(String body){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key){
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  returnValue;
    }
}
