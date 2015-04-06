package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEItemAvailableStatusBo;
import org.kuali.ole.loaders.describe.service.OLEItemAvailableStatusLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLEItemAvailableStatusLoaderService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEItemAvailableStatusLoaderServiceImpl implements OLEItemAvailableStatusLoaderService {

    private static final Logger LOG = Logger.getLogger(OLEItemAvailableStatusLoaderServiceImpl.class);
    private OLEItemAvailableStatusLoaderHelperService oleItemAvailableStatusLoaderHelperService;
    private OLELoaderService oleLoaderService;
    private BusinessObjectService businessObjectService;

    public OLEItemAvailableStatusLoaderHelperService getOleItemAvailableStatusLoaderHelperService() {
        if(oleItemAvailableStatusLoaderHelperService == null){
            oleItemAvailableStatusLoaderHelperService = new OLEItemAvailableStatusLoaderHelperServiceImpl();
        }
        return oleItemAvailableStatusLoaderHelperService;
    }

    public void setOleItemAvailableStatusLoaderHelperService(OLEItemAvailableStatusLoaderHelperService oleItemAvailableStatusLoaderHelperService) {
        this.oleItemAvailableStatusLoaderHelperService = oleItemAvailableStatusLoaderHelperService;
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

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    @Override
    public Object importItemAvailableStatus(String bodyContent, HttpContext context) {
        LOG.info("Inside importItemAvailableStatus method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        List<Integer> rejectItemAvailableStatusList = new ArrayList<Integer>();
        List<JSONObject> createdItemAvailableStatusObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray itemAvailableStatusJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    for (int index = 0; index < itemAvailableStatusJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OleItemAvailableStatus itemAvailableStatus = new OleItemAvailableStatus();
                        try {
                            jsonObject = (JSONObject)itemAvailableStatusJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        itemAvailableStatus.setItemAvailableStatusName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        itemAvailableStatus.setItemAvailableStatusCode(code);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("active")){
                                    try{
                                        boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                                        itemAvailableStatus.setActive(active);
                                        validObject = true;
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        rejectItemAvailableStatusList.add(index+1);
                                        continue;
                                    }

                                }

                            }
                            if(itemAvailableStatus != null && validObject){
                                if(getOleItemAvailableStatusLoaderHelperService().getItemAvailableStatusByCode(itemAvailableStatus.getItemAvailableStatusCode()) == null){
                                    try {
                                        itemAvailableStatus = getBusinessObjectService().save(itemAvailableStatus);
                                        createdItemAvailableStatusObject.add((JSONObject)getOleItemAvailableStatusLoaderHelperService().formItemAvailableStatusExportResponse(itemAvailableStatus, OLELoaderConstants.OLELoaderContext.ITEM_TYPE,
                                                context.getRequest().getRequestUri().toASCIIString(), false));
                                    } catch (Exception e) {
                                        rejectItemAvailableStatusList.add(index+1);
                                        continue;
                                    }
                                }else{
                                    rejectItemAvailableStatusList.add(index+1);
                                    continue;
                                }


                            }else{
                                rejectItemAvailableStatusList.add(index+1);
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectItemAvailableStatusList.add(index+1);
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
        oleLoaderImportResponseBo.setOleRejectedBos(rejectItemAvailableStatusList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdItemAvailableStatusObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateItemAvailableStatusById(String itemAvailableStatusId, String bodyContent, HttpContext context) {
        LOG.info("Inside updateItemAvailableStatusById method.");
        OLEItemAvailableStatusBo oleItemAvailableStatusBo = new OLEItemAvailableStatusBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if (jsonObject != null) {
            if (jsonObject.has("name")) {
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "name");
                if (StringUtils.isNotBlank(name)) {
                    oleItemAvailableStatusBo.setItemAvailableStatusName(name);
                    validObject = true;
                }
            }

            if(jsonObject.has("active")){
                try{
                    boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                    oleItemAvailableStatusBo.setActive(active);
                    validObject = true;
                }catch(Exception e){
                    e.printStackTrace();
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.INVALID_BOOLEAN, OLELoaderConstants.OLEloaderStatus.INVALID_BOOLEAN);
                }

            }

            if (oleItemAvailableStatusBo != null && validObject) {
                OleItemAvailableStatus oleItemAvailableStatus = getOleItemAvailableStatusLoaderHelperService().getItemAvailableStatusById(itemAvailableStatusId);
                if (oleItemAvailableStatus != null) {
                    return getOleItemAvailableStatusLoaderHelperService().updateOleItemAvailableStatus(oleItemAvailableStatus,oleItemAvailableStatusBo,context);
                } else {
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.ITEM_STATUS_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.ITEM_STATUS_NOT_EXIST);
                }
            } else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
            }
        }else {
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object exportItemAvailableStatusById(String itemAvailableStatusId) {
        LOG.info("Inside exportItemAvailableStatusById method.");
        OleItemAvailableStatus oleItemAvailableStatus = getOleItemAvailableStatusLoaderHelperService().getItemAvailableStatusById(itemAvailableStatusId);
        if(oleItemAvailableStatus != null){
            return oleItemAvailableStatus;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.ITEM_STATUS_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.ITEM_STATUS_NOT_EXIST);
        }
    }

    @Override
    public Object exportItemAvailableStatusByCode(String itemAvailableStatusCode) {
        LOG.info("Inside exportItemAvailableStatusByCode method.");
        OleItemAvailableStatus oleItemAvailableStatus = getOleItemAvailableStatusLoaderHelperService().getItemAvailableStatusByCode(itemAvailableStatusCode);
        if(oleItemAvailableStatus != null){
            return oleItemAvailableStatus;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.ITEM_STATUS_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.ITEM_STATUS_NOT_EXIST);
        }
    }

    @Override
    public List<OleItemAvailableStatus> exportAllItemAvailableStatus() {
        LOG.info("Inside exportAllItemAvailableStatus method.");
        List<OleItemAvailableStatus> oleItemAvailableStatus = getOleItemAvailableStatusLoaderHelperService().getAllItemAvailableStatus();
        return oleItemAvailableStatus;
    }
}
