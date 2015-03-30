package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEItemTypeBo;
import org.kuali.ole.loaders.describe.service.OLEItemTypeLoaderService;
import org.kuali.ole.loaders.describe.service.OLEItemTypeLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLEItemTypeLoaderService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEItemTypeLoaderServiceImpl implements OLEItemTypeLoaderService {

    private static final Logger LOG = Logger.getLogger(OLEItemTypeLoaderServiceImpl.class);
    private OLEItemTypeLoaderHelperService oleItemTypeLoaderHelperService;
    private OLELoaderService oleLoaderService;
    private BusinessObjectService businessObjectService;

    public OLEItemTypeLoaderHelperService getOleInstanceItemTypeLoaderHelperService() {
        if(oleItemTypeLoaderHelperService == null){
            oleItemTypeLoaderHelperService = new OLEItemTypeLoaderHelperServiceImpl();
        }
        return oleItemTypeLoaderHelperService;
    }

    public void setOleInstanceItemTypeLoaderHelperService(OLEItemTypeLoaderHelperService oleItemTypeLoaderHelperService) {
        this.oleItemTypeLoaderHelperService = oleItemTypeLoaderHelperService;
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
    public Object importItemTypes(String bodyContent, HttpContext context) {
        LOG.info("Inside importItemTypes method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        List<Integer> rejectItemTypeList = new ArrayList<Integer>();
        List<JSONObject> createdItemTypeObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray itemTypeJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    for (int index = 0; index < itemTypeJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OleInstanceItemType itemType = new OleInstanceItemType();
                        try {
                            jsonObject = (JSONObject)itemTypeJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        itemType.setInstanceItemTypeName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        itemType.setInstanceItemTypeCode(code);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("source")){
                                    String source = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"source");
                                    if(StringUtils.isNotBlank(source)){
                                        itemType.setSource(source);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("description")){
                                    String description = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"description");
                                    if(StringUtils.isNotBlank(description)){
                                        itemType.setInstanceItemTypeDesc(description);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("active")){
                                    try{
                                        boolean active = Boolean.getBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                                        itemType.setActive(active);
                                        validObject = true;
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        rejectItemTypeList.add(index+1);
                                        continue;
                                    }

                                }

                                if(jsonObject.has("sourceDate")){
                                    String sourceDate = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"sourceDate");
                                    if(StringUtils.isNotBlank(sourceDate)){
                                        try{
                                            Date date = OLELoaderConstants.DATE_FORMAT.parse(sourceDate);
                                            itemType.setSourceDate(new java.sql.Date(date.getTime()));
                                            validObject = true;
                                        }catch(Exception e){
                                            e.printStackTrace();
                                            rejectItemTypeList.add(index+1);
                                            continue;
                                        }
                                    }
                                }

                            }
                            if(itemType != null && validObject){
                                if(getOleInstanceItemTypeLoaderHelperService().getItemTypeByCode(itemType.getInstanceItemTypeCode()) == null){
                                    try {
                                        itemType = getBusinessObjectService().save(itemType);
                                        createdItemTypeObject.add((JSONObject)getOleInstanceItemTypeLoaderHelperService().formOleInstanceItemTypeExportResponse(itemType, OLELoaderConstants.OLELoaderContext.ITEM_TYPE,
                                                context.getRequest().getRequestUri().toASCIIString(), false));
                                    } catch (Exception e) {
                                        rejectItemTypeList.add(index+1);
                                        continue;
                                    }
                                }else{
                                    rejectItemTypeList.add(index+1);
                                    continue;
                                }


                            }else{
                                rejectItemTypeList.add(index+1);
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectItemTypeList.add(index+1);
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
        oleLoaderImportResponseBo.setOleRejectedBos(rejectItemTypeList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdItemTypeObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateItemTypeById(String itemTypeId, String bodyContent, HttpContext context) {
        LOG.info("Inside updateItemTypeById method.");
        OLEItemTypeBo oleItemTypeBo = new OLEItemTypeBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if (jsonObject != null) {
            if (jsonObject.has("name")) {
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "name");
                if (StringUtils.isNotBlank(name)) {
                    oleItemTypeBo.setItemTypeName(name);
                    validObject = true;
                }
            }

            if(jsonObject.has("source")){
                String source = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"source");
                if(StringUtils.isNotBlank(source)){
                    oleItemTypeBo.setSource(source);
                    validObject = true;
                }
            }

            if(jsonObject.has("description")){
                String description = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"description");
                if(StringUtils.isNotBlank(description)){
                    oleItemTypeBo.setItemTypeDesc(description);
                    validObject = true;
                }
            }

            if(jsonObject.has("active")){
                try{
                    boolean active = Boolean.getBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                    oleItemTypeBo.setActive(active);
                    validObject = true;
                }catch(Exception e){
                    e.printStackTrace();
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.INVALID_BOOLEAN, OLELoaderConstants.OLEloaderStatus.INVALID_BOOLEAN);
                }

            }

            if(jsonObject.has("sourceDate")){
                String sourceDate = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"sourceDate");
                if(StringUtils.isNotBlank(sourceDate)){
                    try{
                        Date date = OLELoaderConstants.DATE_FORMAT.parse(sourceDate);
                        oleItemTypeBo.setSourceDate(new java.sql.Date(date.getTime()));
                        validObject = true;
                    }catch(Exception e){
                        e.printStackTrace();
                        return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.INVALID_DATE, OLELoaderConstants.OLEloaderStatus.INVALID_DATE);
                    }
                }
            }


            if (oleItemTypeBo != null && validObject) {
                OleInstanceItemType oleItemType = getOleInstanceItemTypeLoaderHelperService().getItemTypeById(itemTypeId);
                if (oleItemType != null) {
                    return getOleInstanceItemTypeLoaderHelperService().updateOleInstanceItemType(oleItemType,oleItemTypeBo,context);
                } else {
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.ITEM_TYPE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.ITEM_TYPE_NOT_EXIST);
                }
            } else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
            }
        }else {
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object exportItemTypeById(String itemTypeId) {
        LOG.info("Inside exportItemTypeById method.");
        OleInstanceItemType oleItemType = getOleInstanceItemTypeLoaderHelperService().getItemTypeById(itemTypeId);
        if(oleItemType != null){
            return oleItemType;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.ITEM_TYPE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.ITEM_TYPE_NOT_EXIST);
        }
    }

    @Override
    public Object exportItemTypeByCode(String itemTypeCode) {
        LOG.info("Inside exportItemTypeByCode method.");
        OleInstanceItemType oleItemType = getOleInstanceItemTypeLoaderHelperService().getItemTypeByCode(itemTypeCode);
        if(oleItemType != null){
            return oleItemType;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.ITEM_TYPE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.ITEM_TYPE_NOT_EXIST);
        }
    }

    @Override
    public List<OleInstanceItemType> exportAllItemTypes() {
        LOG.info("Inside exportAllItemTypes method.");
        List<OleInstanceItemType> oleItemTypes = getOleInstanceItemTypeLoaderHelperService().getAllItemTypes();
        return oleItemTypes;
    }
}
