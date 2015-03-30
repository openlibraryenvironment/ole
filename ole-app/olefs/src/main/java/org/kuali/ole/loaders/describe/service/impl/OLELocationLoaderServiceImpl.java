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
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 2/4/15.
 */
public class OLELocationLoaderServiceImpl implements OLELocationLoaderService {

    private static final Logger LOG = Logger.getLogger(OLELocationLoaderServiceImpl.class);

    OLELocationLoaderHelperService oleLocationLoaderHelperService = new OLELocationLoaderHelperServiceImpl();
    private OLELoaderService oleLoaderService;
    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
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
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray locationJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    for (int index = 0; index < locationJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OLELocationBo oleLocationBo = new OLELocationBo();
                        try {
                            jsonObject = (JSONObject)locationJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        oleLocationBo.setLocationName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        oleLocationBo.setLocationCode(code);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("level")){
                                    JSONObject levelJsonObject = getOleLoaderService().getJsonObjectFromString(getOleLoaderService().getStringValueFromJsonObject(jsonObject,"level"));
                                    if(levelJsonObject != null){
                                        String levelUrl = getOleLoaderService().getStringValueFromJsonObject(levelJsonObject, "@id");
                                        if(StringUtils.isNotBlank(levelUrl)){
                                            Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(levelUrl);
                                            if((Integer)restResponseMap.get("status")== 200){
                                                String levelResponseContent = (String)restResponseMap.get("content");
                                                JSONObject levelResponseObject = getOleLoaderService().getJsonObjectFromString(levelResponseContent);
                                                if(levelResponseObject != null){
                                                    String urlId = getOleLoaderService().getStringValueFromJsonObject(levelResponseObject, "@id");
                                                    if(StringUtils.isNotBlank(urlId)){
                                                        String levelId = urlId.substring(urlId.indexOf("/api/locationLevel/")+19);
                                                        oleLocationBo.setLocationLevelId(levelId);
                                                        validObject = true;
                                                    }
                                                }else{
                                                    rejectLocationList.add(index+1);
                                                    continue;
                                                }
                                            }else{
                                                rejectLocationList.add(index+1);
                                                continue;
                                            }



                                        }
                                    }

                                }
                                if(jsonObject.has("parent")){
                                    JSONObject parentJsonObject = getOleLoaderService().getJsonObjectFromString(getOleLoaderService().getStringValueFromJsonObject(jsonObject,"parent"));
                                    if(parentJsonObject != null){
                                        String parentUrl = getOleLoaderService().getStringValueFromJsonObject(parentJsonObject,"@id");
                                        if(StringUtils.isNotBlank(parentUrl)){
                                            Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(parentUrl);
                                            if((Integer)restResponseMap.get("status")== 200){
                                                String parentResponseContent = (String)restResponseMap.get("content");
                                                JSONObject parentResponseObject = getOleLoaderService().getJsonObjectFromString(parentResponseContent);
                                                if(parentResponseObject != null){
                                                    String urlId = getOleLoaderService().getStringValueFromJsonObject(parentResponseObject,"@id");
                                                    if(StringUtils.isNotBlank(urlId)){
                                                        String parentId = urlId.substring(urlId.indexOf("/api/location/")+14);
                                                        oleLocationBo.setParentLocationId(parentId);
                                                        validObject = true;
                                                    }
                                                }else{
                                                    rejectLocationList.add(index+1);
                                                    continue;
                                                }
                                            }else{
                                                rejectLocationList.add(index+1);
                                                continue;
                                            }


                                        }
                                    }
                                }
                            }
                            if(oleLocationBo != null && validObject){
                                if (!oleLocationLoaderHelperService.isLocationExistByCode(oleLocationBo.getLocationCode())) {
                                    OleLocationLevel oleLocationLevel = oleLocationLoaderHelperService.getLocationLevelById(oleLocationBo.getLocationLevelId());
                                    if (oleLocationLevel == null) {
                                        rejectLocationList.add(index+1);
                                    } else if (oleLocationLoaderHelperService.isParentLocationExist(oleLocationBo.getParentLocationId())) {
                                        rejectLocationList.add(index + 1);
                                    } else {
                                        try {
                                            OleLocation oleLocation = oleLocationLoaderHelperService.createOleLocation(oleLocationBo);
                                            createdLocationObject.add((JSONObject)oleLocationLoaderHelperService.formLocationExportResponse(oleLocation, OLELoaderConstants.OLELoaderContext.LOCATION,
                                                    context.getRequest().getRequestUri().toASCIIString(),false));
                                        } catch (Exception e) {
                                            rejectLocationList.add(index+1);
                                        }
                                    }
                                } else {
                                    rejectLocationList.add(index+1);
                                }

                            }else{
                                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectLocationList.add(index);
                        }
                    }
                }else{
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
                }

            }
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
        oleLoaderImportResponseBo.setOleRejectedBos(rejectLocationList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdLocationObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateLocationById(String locationId, String bodyContent,HttpContext context) {
        LOG.info("Inside updateLocationById method.");
        OLELocationBo oleLocationBo = new OLELocationBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(jsonObject != null){
            if(jsonObject.has("name")){
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                if(StringUtils.isNotBlank(name)){
                    oleLocationBo.setLocationName(name);
                    validObject = true;
                }
            }
            if(jsonObject.has("code")){
                String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                if(StringUtils.isNotBlank(code)){
                    oleLocationBo.setLocationCode(code);
                    validObject = true;
                }
            }
            if(jsonObject.has("level")){
                JSONObject levelJsonObject = getOleLoaderService().getJsonObjectFromString(getOleLoaderService().getStringValueFromJsonObject(jsonObject,"level"));
                if(levelJsonObject != null){
                    String levelUrl = getOleLoaderService().getStringValueFromJsonObject(levelJsonObject, "@id");
                    if(StringUtils.isNotBlank(levelUrl)){
                        Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(levelUrl);
                        if((Integer)restResponseMap.get("status")== 200){
                            String levelResponseContent = (String)restResponseMap.get("content");
                            JSONObject levelResponseObject = getOleLoaderService().getJsonObjectFromString(levelResponseContent);
                            if(levelResponseObject != null){
                                String urlId = getOleLoaderService().getStringValueFromJsonObject(levelResponseObject, "@id");
                                if(StringUtils.isNotBlank(urlId)){
                                    String levelId = urlId.substring(urlId.indexOf("/api/locationLevel/")+19);
                                    oleLocationBo.setLocationLevelId(levelId);
                                    validObject = true;
                                }
                            }else{
                                return getOleLoaderService().generateResponse(
                                        OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST,
                                        OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
                            }
                        }else{
                            return getOleLoaderService().generateResponse(
                                    OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST,
                                    OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
                        }



                    }
                }

            }
            if(jsonObject.has("parent")){
                JSONObject parentJsonObject = getOleLoaderService().getJsonObjectFromString(getOleLoaderService().getStringValueFromJsonObject(jsonObject,"parent"));
                if(parentJsonObject != null){
                    String parentUrl = getOleLoaderService().getStringValueFromJsonObject(parentJsonObject,"@id");
                    if(StringUtils.isNotBlank(parentUrl)){
                        Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(parentUrl);
                        if((Integer)restResponseMap.get("status")== 200){
                            String parentResponseContent = (String)restResponseMap.get("content");
                            JSONObject parentResponseObject = getOleLoaderService().getJsonObjectFromString(parentResponseContent);
                            if(parentResponseObject != null){
                                String urlId = getOleLoaderService().getStringValueFromJsonObject(parentResponseObject,"@id");
                                if(StringUtils.isNotBlank(urlId)){
                                    String parentId = urlId.substring(urlId.indexOf("/api/location/")+14);
                                    oleLocationBo.setParentLocationId(parentId);
                                    validObject = true;
                                }
                            }else{
                                return getOleLoaderService().generateResponse(
                                        OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST,
                                        OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
                            }
                        }else{
                            return getOleLoaderService().generateResponse(
                                    OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST,
                                    OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
                        }


                    }
                }
            }
        }
        if(oleLocationBo != null && validObject){
            if(StringUtils.isNotBlank(oleLocationBo.getLocationLevelId()) && oleLocationLoaderHelperService.isLocationLevelExistById(oleLocationBo.getLocationLevelId()))    {
                return getOleLoaderService().generateResponse(
                        OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST,
                        OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
            }else if(StringUtils.isNotBlank(oleLocationBo.getParentLocationId()) && oleLocationLoaderHelperService.isParentLocationExist(oleLocationBo.getParentLocationId())) {
                return getOleLoaderService().generateResponse(
                        OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_NOT_EXIST,
                        OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_NOT_EXIST);
            }else{
                OleLocation oleLocation = oleLocationLoaderHelperService.getLocationById(locationId);
                if(oleLocation != null){
                    return oleLocationLoaderHelperService.updateOleLocation(oleLocation, oleLocationBo,context);
                }else{
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.LOCATION_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_NOT_EXIST);
                }
            }

        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object exportLocationById(String locationId) {
        LOG.info("Inside exportLocationById method.");
        OleLocation oleLocation = oleLocationLoaderHelperService.getLocationById(locationId);
        if(oleLocation != null){
            return oleLocation;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.LOCATION_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_NOT_EXIST);
        }
    }

    @Override
    public Object exportLocationByCode(String locationCode) {
        LOG.info("Inside exportLocationByCode method.");
        OleLocation oleLocation = oleLocationLoaderHelperService.getLocationByCode(locationCode);
        if(oleLocation != null){
            return oleLocation;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.LOCATION_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_NOT_EXIST);
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
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
        }
    }

    @Override
    public Object exportLocationLevelByCode(String locationLevelCode) {
        LOG.info("Inside exportLocationLevelByCdde method.");
        OleLocationLevel oleLocationLevel = oleLocationLoaderHelperService.getLocationLevelByCode(locationLevelCode);
        if(oleLocationLevel != null){
            return oleLocationLevel;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.LOCATION_LEVEL_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_LEVEL_NOT_EXIST);
        }
    }

    @Override
    public Object importLocationLevels(String bodyContent, HttpContext context) {
        LOG.info("Inside importLocations method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        List<Integer> rejectLocationLevelList = new ArrayList<Integer>();
        List<JSONObject> createdLocationLevelObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray locationJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    for (int index = 0; index < locationJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OleLocationLevel oleLocationLevel = new OleLocationLevel();
                        try {
                            jsonObject = (JSONObject)locationJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        oleLocationLevel.setLevelName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        oleLocationLevel.setLevelCode(code);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("parent")){
                                    JSONObject parentJsonObject = getOleLoaderService().getJsonObjectFromString(getOleLoaderService().getStringValueFromJsonObject(jsonObject,"parent"));
                                    if(parentJsonObject != null){
                                        String parentUrl = getOleLoaderService().getStringValueFromJsonObject(parentJsonObject,"@id");
                                        if(StringUtils.isNotBlank(parentUrl)){
                                            Map<String,Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(parentUrl);
                                            if((Integer)restResponseMap.get("status")== 200){
                                                String parentResponseContent = (String)restResponseMap.get("content");
                                                JSONObject parentResponseObject = getOleLoaderService().getJsonObjectFromString(parentResponseContent);
                                                if(parentResponseObject != null){
                                                    String urlId = getOleLoaderService().getStringValueFromJsonObject(parentResponseObject,"@id");
                                                    if(StringUtils.isNotBlank(urlId)){
                                                        String parentId = urlId.substring(urlId.indexOf("/api/locationLevel/")+19);
                                                        oleLocationLevel.setParentLevelId(parentId);
                                                        validObject = true;
                                                    }
                                                }else{
                                                    rejectLocationLevelList.add(index+1);
                                                    continue;
                                                }
                                            }else{
                                                rejectLocationLevelList.add(index+1);
                                                continue;
                                            }


                                        }
                                    }
                                }
                            }
                            if(oleLocationLevel != null && validObject){
                                if(oleLocationLoaderHelperService.getLocationLevelByCode(oleLocationLevel.getLevelCode()) == null){
                                    OleLocationLevel parentLocationLevel = oleLocationLoaderHelperService.getLocationLevelById(oleLocationLevel.getParentLevelId());
                                    if (parentLocationLevel == null) {
                                        rejectLocationLevelList.add(index+1);
                                        continue;
                                    }else {
                                        try {
                                            oleLocationLevel.setOleLocationLevel(oleLocationLevel);
                                            oleLocationLevel = getBusinessObjectService().save(oleLocationLevel);
                                            createdLocationLevelObject.add((JSONObject)oleLocationLoaderHelperService.formLocationLevelExportResponse(oleLocationLevel, OLELoaderConstants.OLELoaderContext.LOCATION_LEVEL,
                                                    context.getRequest().getRequestUri().toASCIIString(), false));
                                        } catch (Exception e) {
                                            rejectLocationLevelList.add(index+1);
                                            continue;
                                        }
                                    }
                                }else{
                                    rejectLocationLevelList.add(index+1);
                                    continue;
                                }


                            }else{
                                rejectLocationLevelList.add(index+1);
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectLocationLevelList.add(index+1);
                            continue;
                        }
                    }
                }else{
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
                }

            }
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
        oleLoaderImportResponseBo.setOleRejectedBos(rejectLocationLevelList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdLocationLevelObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateLocationLevelById(String locationLevelId, String bodyContent, HttpContext context) {
        LOG.info("Inside updateLocationLevelById method.");
        OLELocationLevelBo oleLocationLevelBo = new OLELocationLevelBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if (jsonObject != null) {
            if (jsonObject.has("name")) {
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "name");
                if (StringUtils.isNotBlank(name)) {
                    oleLocationLevelBo.setLevelName(name);
                    validObject = true;
                }
            }
           /* if (jsonObject.has("code")) {
                String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "code");
                if (StringUtils.isNotBlank(code)) {
                    oleLocationLevelBo.setLevelCode(code);
                    validObject = true;
                }
            }*/
            if (jsonObject.has("parent")) {
                JSONObject levelJsonObject = getOleLoaderService().getJsonObjectFromString(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "parent"));
                if (levelJsonObject != null) {
                    String levelUrl = getOleLoaderService().getStringValueFromJsonObject(levelJsonObject, "@id");
                    if (StringUtils.isNotBlank(levelUrl)) {
                        Map<String, Object> restResponseMap = OLELoaderRestClient.jerseryClientGet(levelUrl);
                        if ((Integer) restResponseMap.get("status") == 200) {
                            String parentLevelResponseContent = (String) restResponseMap.get("content");
                            JSONObject parentLevelResponseObject = getOleLoaderService().getJsonObjectFromString(parentLevelResponseContent);
                            if (parentLevelResponseObject != null) {
                                String urlId = getOleLoaderService().getStringValueFromJsonObject(parentLevelResponseObject, "@id");
                                if (StringUtils.isNotBlank(urlId)) {
                                    String levelId = urlId.substring(urlId.indexOf("/api/locationLevel/") + 19);
                                    oleLocationLevelBo.setParentLevelId(levelId);
                                    validObject = true;
                                }
                            } else {
                                return getOleLoaderService().generateResponse(
                                        OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_LEVEL_NOT_EXIST,
                                        OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_LEVEL_NOT_EXIST);
                            }
                        } else {
                            return getOleLoaderService().generateResponse(
                                    OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_LEVEL_NOT_EXIST,
                                    OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_LEVEL_NOT_EXIST);
                        }
                    }
                }

            }

            if (oleLocationLevelBo != null && validObject) {
                OleLocationLevel oleLocationLevel = oleLocationLoaderHelperService.getLocationLevelById(locationLevelId);
                if (oleLocationLevel != null) {
                    if (StringUtils.isNotBlank(oleLocationLevelBo.getParentLevelId())) {
                        OleLocationLevel parentLocationLevel = oleLocationLoaderHelperService.getLocationLevelById(oleLocationLevelBo.getParentLevelId());
                        if (parentLocationLevel != null) {
                            oleLocationLevel.setOleLocationLevel(parentLocationLevel);
                            oleLocationLevel.setParentLevelId(oleLocationLevelBo.getParentLevelId());
                        } else {
                            return getOleLoaderService().generateResponse(
                                    OLELoaderConstants.OLEloaderMessage.PARENT_LOCATION_LEVEL_NOT_EXIST,
                                    OLELoaderConstants.OLEloaderStatus.PARENT_LOCATION_LEVEL_NOT_EXIST);
                        }
                    }
                    return oleLocationLoaderHelperService.updateOleLocationLevel(oleLocationLevel, oleLocationLevelBo, context);
                } else {
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.LOCATION_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.LOCATION_NOT_EXIST);
                }
            } else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
            }
        }else {
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

}
