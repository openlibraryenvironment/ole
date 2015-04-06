package org.kuali.ole.loaders.deliver.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.deliver.bo.OLEBorrowerTypeBo;
import org.kuali.ole.loaders.deliver.service.OLEBorrowerTypeLoaderHelperService;
import org.kuali.ole.loaders.deliver.service.OLEBorrowerTypeLoaderService;
import org.kuali.ole.loaders.deliver.service.impl.OLEBorrowerTypeLoaderHelperServiceImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEBorrowerTypeLoaderServiceImpl implements OLEBorrowerTypeLoaderService {

    private static final Logger LOG = Logger.getLogger(OLEBorrowerTypeLoaderServiceImpl.class);
    private OLEBorrowerTypeLoaderHelperService oleBorrowerTypeLoaderHelperService;
    private OLELoaderService oleLoaderService;
    private BusinessObjectService businessObjectService;

    public OLEBorrowerTypeLoaderHelperService getOleBorrowerTypeLoaderHelperService() {
        if(oleBorrowerTypeLoaderHelperService == null){
            oleBorrowerTypeLoaderHelperService = new OLEBorrowerTypeLoaderHelperServiceImpl();
        }
        return oleBorrowerTypeLoaderHelperService;
    }

    public void setOleBorrowerTypeLoaderHelperService(OLEBorrowerTypeLoaderHelperService oleBorrowerTypeLoaderHelperService) {
        this.oleBorrowerTypeLoaderHelperService = oleBorrowerTypeLoaderHelperService;
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
    public Object importBorrowerTypes(String bodyContent, HttpContext context) {
        LOG.info("Inside importBorrowerTypes method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        List<Integer> rejectBorrowerTypeList = new ArrayList<Integer>();
        List<JSONObject> createdBorrowerTypeObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray borrowerTypeJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    for (int index = 0; index < borrowerTypeJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OleBorrowerType borrowerType = new OleBorrowerType();
                        try {
                            jsonObject = (JSONObject)borrowerTypeJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        borrowerType.setBorrowerTypeName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        borrowerType.setBorrowerTypeCode(code);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("description")){
                                    String description = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"description");
                                    if(StringUtils.isNotBlank(description)){
                                        borrowerType.setBorrowerTypeDescription(description);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("active")){
                                    try{
                                        boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                                        borrowerType.setActive(active);
                                        validObject = true;
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        rejectBorrowerTypeList.add(index+1);
                                        continue;
                                    }

                                }


                            }
                            if(borrowerType != null && validObject){
                                if(getOleBorrowerTypeLoaderHelperService().getBorrowerTypeByCode(borrowerType.getBorrowerTypeCode()) == null){
                                    try {
                                        borrowerType = getBusinessObjectService().save(borrowerType);
                                        createdBorrowerTypeObject.add((JSONObject)getOleBorrowerTypeLoaderHelperService().formOleBorrowerTypeExportResponse(borrowerType, OLELoaderConstants.OLELoaderContext.BORROWER_TYPE,
                                                context.getRequest().getRequestUri().toASCIIString(), false));
                                    } catch (Exception e) {
                                        rejectBorrowerTypeList.add(index+1);
                                        continue;
                                    }
                                }else{
                                    rejectBorrowerTypeList.add(index+1);
                                    continue;
                                }


                            }else{
                                rejectBorrowerTypeList.add(index+1);
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectBorrowerTypeList.add(index+1);
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
        oleLoaderImportResponseBo.setOleRejectedBos(rejectBorrowerTypeList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdBorrowerTypeObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateBorrowerTypeById(String borrowerTypeId, String bodyContent, HttpContext context) {
        LOG.info("Inside updateBorrowerTypeById method.");
        OLEBorrowerTypeBo oleBorrowerTypeBo = new OLEBorrowerTypeBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if (jsonObject != null) {
            if (jsonObject.has("name")) {
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "name");
                if (StringUtils.isNotBlank(name)) {
                    oleBorrowerTypeBo.setBorrowerTypeName(name);
                    validObject = true;
                }
            }

            if(jsonObject.has("description")){
                String description = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"description");
                if(StringUtils.isNotBlank(description)){
                    oleBorrowerTypeBo.setBorrowerTypeDescription(description);
                    validObject = true;
                }
            }

            if(jsonObject.has("active")){
                try{
                    boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                    oleBorrowerTypeBo.setActive(active);
                    validObject = true;
                }catch(Exception e){
                    e.printStackTrace();
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.INVALID_BOOLEAN, OLELoaderConstants.OLEloaderStatus.INVALID_BOOLEAN);
                }

            }

            if (oleBorrowerTypeBo != null && validObject) {
                OleBorrowerType oleBorrowerType = getOleBorrowerTypeLoaderHelperService().getBorrowerTypeById(borrowerTypeId);
                if (oleBorrowerType != null) {
                    return getOleBorrowerTypeLoaderHelperService().updateOleBorrowerType(oleBorrowerType,oleBorrowerTypeBo,context);
                } else {
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BORROWER_TYPE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.BORROWER_TYPE_NOT_EXIST);
                }
            } else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
            }
        }else {
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object exportBorrowerTypeById(String borrowerTypeId) {
        LOG.info("Inside exportBorrowerTypeById method.");
        OleBorrowerType oleBorrowerType = getOleBorrowerTypeLoaderHelperService().getBorrowerTypeById(borrowerTypeId);
        if(oleBorrowerType != null){
            return oleBorrowerType;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BORROWER_TYPE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.BORROWER_TYPE_NOT_EXIST);
        }
    }

    @Override
    public Object exportBorrowerTypeByCode(String borrowerTypeCode) {
        LOG.info("Inside exportBorrowerTypeByCode method.");
        OleBorrowerType oleBorrowerType = getOleBorrowerTypeLoaderHelperService().getBorrowerTypeByCode(borrowerTypeCode);
        if(oleBorrowerType != null){
            return oleBorrowerType;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BORROWER_TYPE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.BORROWER_TYPE_NOT_EXIST);
        }
    }

    @Override
    public List<OleBorrowerType> exportAllBorrowerTypes() {
        LOG.info("Inside exportAllBorrowerTypes method.");
        List<OleBorrowerType> oleBorrowerTypes = getOleBorrowerTypeLoaderHelperService().getAllBorrowerTypes();
        return oleBorrowerTypes;
    }
}
