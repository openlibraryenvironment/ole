package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEStatisticalSearchingCodeBo;
import org.kuali.ole.loaders.describe.service.OLEStatisticalSearchingCodeLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLEStatisticalSearchingCodeLoaderService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEStatisticalSearchingCodeLoaderServiceImpl implements OLEStatisticalSearchingCodeLoaderService {

    private static final Logger LOG = Logger.getLogger(OLEStatisticalSearchingCodeLoaderServiceImpl.class);
    private OLEStatisticalSearchingCodeLoaderHelperService oleStatisticalSearchingCodeLoaderHelperService;
    private OLELoaderService oleLoaderService;
    private BusinessObjectService businessObjectService;

    public OLEStatisticalSearchingCodeLoaderHelperService getOleStatisticalSearchingCodeLoaderHelperService() {
        if(oleStatisticalSearchingCodeLoaderHelperService == null){
            oleStatisticalSearchingCodeLoaderHelperService = new OLEStatisticalSearchingCodeLoaderHelperServiceImpl();
        }
        return oleStatisticalSearchingCodeLoaderHelperService;
    }

    public void setOleStatisticalSearchingCodeLoaderHelperService(OLEStatisticalSearchingCodeLoaderHelperService oleStatisticalSearchingCodeLoaderHelperService) {
        this.oleStatisticalSearchingCodeLoaderHelperService = oleStatisticalSearchingCodeLoaderHelperService;
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
    public Object importStatisticalSearchingCode(String bodyContent, HttpContext context) {
        LOG.info("Inside importStatisticalSearchingCode method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        List<Integer> rejectStatisticalSearchingCodeList = new ArrayList<Integer>();
        List<JSONObject> createdStatisticalSearchingCodeObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray statisticalSearchingCodeJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    for (int index = 0; index < statisticalSearchingCodeJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OleStatisticalSearchingCodes statisticalSearchingCode = new OleStatisticalSearchingCodes();
                        try {
                            jsonObject = (JSONObject)statisticalSearchingCodeJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        statisticalSearchingCode.setStatisticalSearchingName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        statisticalSearchingCode.setStatisticalSearchingCode(code);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("source")){
                                    String source = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"source");
                                    if(StringUtils.isNotBlank(source)){
                                        statisticalSearchingCode.setSource(source);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("active")){
                                    try{
                                        boolean active = Boolean.getBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                                        statisticalSearchingCode.setActive(active);
                                        validObject = true;
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        rejectStatisticalSearchingCodeList.add(index+1);
                                        continue;
                                    }

                                }

                                if(jsonObject.has("sourceDate")){
                                    String sourceDate = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"sourceDate");
                                    if(StringUtils.isNotBlank(sourceDate)){
                                        try{
                                            Date date = OLELoaderConstants.DATE_FORMAT.parse(sourceDate);
                                            statisticalSearchingCode.setSourceDate(new java.sql.Date(date.getTime()));
                                            validObject = true;
                                        }catch(Exception e){
                                            e.printStackTrace();
                                            rejectStatisticalSearchingCodeList.add(index+1);
                                            continue;
                                        }
                                    }
                                }

                            }
                            if(statisticalSearchingCode != null && validObject){
                                if(getOleStatisticalSearchingCodeLoaderHelperService().getStatisticalSearchingCodeByCode(statisticalSearchingCode.getStatisticalSearchingCode()) == null){
                                    try {
                                        statisticalSearchingCode = getBusinessObjectService().save(statisticalSearchingCode);
                                        createdStatisticalSearchingCodeObject.add((JSONObject)getOleStatisticalSearchingCodeLoaderHelperService().formStatisticalSearchingCodeExportResponse(statisticalSearchingCode, OLELoaderConstants.OLELoaderContext.STAT_SEARCH_CODE,
                                                context.getRequest().getRequestUri().toASCIIString(), false));
                                    } catch (Exception e) {
                                        rejectStatisticalSearchingCodeList.add(index+1);
                                        continue;
                                    }
                                }else{
                                    rejectStatisticalSearchingCodeList.add(index+1);
                                    continue;
                                }


                            }else{
                                rejectStatisticalSearchingCodeList.add(index+1);
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectStatisticalSearchingCodeList.add(index+1);
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
        oleLoaderImportResponseBo.setOleRejectedBos(rejectStatisticalSearchingCodeList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdStatisticalSearchingCodeObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateStatisticalSearchingCodeById(String statisticalSearchingCodeId, String bodyContent, HttpContext context) {
        LOG.info("Inside updateStatisticalSearchingCodeById method.");
        OLEStatisticalSearchingCodeBo oleStatisticalSearchingCodeBo = new OLEStatisticalSearchingCodeBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if (jsonObject != null) {
            if (jsonObject.has("name")) {
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "name");
                if (StringUtils.isNotBlank(name)) {
                    oleStatisticalSearchingCodeBo.setStatisticalSearchingName(name);
                    validObject = true;
                }
            }

            if(jsonObject.has("active")){
                try{
                    boolean active = Boolean.getBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                    oleStatisticalSearchingCodeBo.setActive(active);
                    validObject = true;
                }catch(Exception e){
                    e.printStackTrace();
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.INVALID_BOOLEAN, OLELoaderConstants.OLEloaderStatus.INVALID_BOOLEAN);
                }

            }

            if (oleStatisticalSearchingCodeBo != null && validObject) {
                OleStatisticalSearchingCodes oleStatisticalSearchingCode = getOleStatisticalSearchingCodeLoaderHelperService().getStatisticalSearchingCodeById(statisticalSearchingCodeId);
                if (oleStatisticalSearchingCode != null) {
                    return getOleStatisticalSearchingCodeLoaderHelperService().updateOleStatisticalSearchingCode(oleStatisticalSearchingCode, oleStatisticalSearchingCodeBo, context);
                } else {
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.STAT_SEARCH_CODE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.STAT_SEARCH_CODE_NOT_EXIST);
                }
            } else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
            }
        }else {
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object exportStatisticalSearchingCodeById(String statisticalSearchingCodeId) {
        LOG.info("Inside exportStatisticalSearchingCodeById method.");
        OleStatisticalSearchingCodes oleStatisticalSearchingCode = getOleStatisticalSearchingCodeLoaderHelperService().getStatisticalSearchingCodeById(statisticalSearchingCodeId);
        if(oleStatisticalSearchingCode != null){
            return oleStatisticalSearchingCode;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.STAT_SEARCH_CODE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.STAT_SEARCH_CODE_NOT_EXIST);
        }
    }

    @Override
    public Object exportStatisticalSearchingCodeByCode(String statisticalSearchingCodeCode) {
        LOG.info("Inside exportStatisticalSearchingCodeByCode method.");
        OleStatisticalSearchingCodes oleStatisticalSearchingCode = getOleStatisticalSearchingCodeLoaderHelperService().getStatisticalSearchingCodeByCode(statisticalSearchingCodeCode);
        if(oleStatisticalSearchingCode != null){
            return oleStatisticalSearchingCode;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.STAT_SEARCH_CODE_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.STAT_SEARCH_CODE_NOT_EXIST);
        }
    }

    @Override
    public List<OleStatisticalSearchingCodes> exportAllStatisticalSearchingCode() {
        LOG.info("Inside exportAllStatisticalSearchingCode method.");
        List<OleStatisticalSearchingCodes> oleStatisticalSearchingCode = getOleStatisticalSearchingCodeLoaderHelperService().getAllStatisticalSearchingCode();
        return oleStatisticalSearchingCode;
    }
}
