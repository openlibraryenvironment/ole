package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderRestClient;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEShelvingSchemeBo;
import org.kuali.ole.loaders.describe.service.OLEShelvingSchemeLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLEShelvingSchemeLoaderService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEShelvingSchemeLoaderServiceImpl implements OLEShelvingSchemeLoaderService {

    private static final Logger LOG = Logger.getLogger(OLEShelvingSchemeLoaderServiceImpl.class);
    private OLEShelvingSchemeLoaderHelperService oleShelvingSchemeLoaderHelperService;
    private OLELoaderService oleLoaderService;
    private BusinessObjectService businessObjectService;

    public OLEShelvingSchemeLoaderHelperService getOleShelvingSchemeLoaderHelperService() {
        if(oleShelvingSchemeLoaderHelperService == null){
            oleShelvingSchemeLoaderHelperService = new OLEShelvingSchemeLoaderHelperServiceImpl();
        }
        return oleShelvingSchemeLoaderHelperService;
    }

    public void setOleShelvingSchemeLoaderHelperService(OLEShelvingSchemeLoaderHelperService oleShelvingSchemeLoaderHelperService) {
        this.oleShelvingSchemeLoaderHelperService = oleShelvingSchemeLoaderHelperService;
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
    public Object importShelvingSchemes(String bodyContent, HttpContext context) {
        LOG.info("Inside importShelvingSchemes method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        List<Integer> rejectShelvingSchemeList = new ArrayList<Integer>();
        List<JSONObject> createdShelvingSchemeObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray shelvingSchemeJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    for (int index = 0; index < shelvingSchemeJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OleShelvingScheme shelvingScheme = new OleShelvingScheme();
                        try {
                            jsonObject = (JSONObject)shelvingSchemeJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        shelvingScheme.setShelvingSchemeName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        shelvingScheme.setShelvingSchemeCode(code);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("source")){
                                    String source = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"source");
                                    if(StringUtils.isNotBlank(source)){
                                        shelvingScheme.setSource(source);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("active")){
                                    try{
                                        boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                                        shelvingScheme.setActive(active);
                                        validObject = true;
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        rejectShelvingSchemeList.add(index+1);
                                        continue;
                                    }

                                }

                                if(jsonObject.has("sourceDate")){
                                    String sourceDate = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"sourceDate");
                                    if(StringUtils.isNotBlank(sourceDate)){
                                        try{
                                            Date date = OLELoaderConstants.DATE_FORMAT.parse(sourceDate);
                                            shelvingScheme.setSourceDate(new java.sql.Date(date.getTime()));
                                            validObject = true;
                                        }catch(Exception e){
                                            e.printStackTrace();
                                            rejectShelvingSchemeList.add(index+1);
                                            continue;
                                        }
                                    }
                                }

                            }
                            if(shelvingScheme != null && validObject){
                                if(getOleShelvingSchemeLoaderHelperService().getShelvingSchemeByCode(shelvingScheme.getShelvingSchemeCode()) == null){
                                    try {
                                        shelvingScheme = getBusinessObjectService().save(shelvingScheme);
                                        createdShelvingSchemeObject.add((JSONObject)getOleShelvingSchemeLoaderHelperService().formOleShelvingSchemeExportResponse(shelvingScheme, OLELoaderConstants.OLELoaderContext.SHELVING_SCHEME,
                                                context.getRequest().getRequestUri().toASCIIString(), false));
                                    } catch (Exception e) {
                                        rejectShelvingSchemeList.add(index+1);
                                        continue;
                                    }
                                }else{
                                    rejectShelvingSchemeList.add(index+1);
                                    continue;
                                }


                            }else{
                                rejectShelvingSchemeList.add(index+1);
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectShelvingSchemeList.add(index+1);
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
        oleLoaderImportResponseBo.setOleRejectedBos(rejectShelvingSchemeList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdShelvingSchemeObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateShelvingSchemeById(String shelvingSchemeId, String bodyContent, HttpContext context) {
        LOG.info("Inside updateShelvingSchemeById method.");
        OLEShelvingSchemeBo oleShelvingSchemeBo = new OLEShelvingSchemeBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if (jsonObject != null) {
            if (jsonObject.has("name")) {
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "name");
                if (StringUtils.isNotBlank(name)) {
                    oleShelvingSchemeBo.setShelvingSchemeName(name);
                    validObject = true;
                }
            }

            if(jsonObject.has("source")){
                String source = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"source");
                if(StringUtils.isNotBlank(source)){
                    oleShelvingSchemeBo.setSource(source);
                    validObject = true;
                }
            }

            if(jsonObject.has("active")){
                try{
                    boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                    oleShelvingSchemeBo.setActive(active);
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
                        oleShelvingSchemeBo.setSourceDate(new java.sql.Date(date.getTime()));
                        validObject = true;
                    }catch(Exception e){
                        e.printStackTrace();
                        return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.INVALID_DATE, OLELoaderConstants.OLEloaderStatus.INVALID_DATE);
                    }
                }
            }


            if (oleShelvingSchemeBo != null && validObject) {
                OleShelvingScheme oleShelvingScheme = getOleShelvingSchemeLoaderHelperService().getShelvingSchemeById(shelvingSchemeId);
                if (oleShelvingScheme != null) {
                    return getOleShelvingSchemeLoaderHelperService().updateOleShelvingScheme(oleShelvingScheme,oleShelvingSchemeBo,context);
                } else {
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.SHELVING_SCHEME_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.SHELVING_SCHEME_NOT_EXIST);
                }
            } else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
            }
        }else {
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object exportShelvingSchemeById(String shelvingSchemeId) {
        LOG.info("Inside exportShelvingSchemeById method.");
        OleShelvingScheme oleShelvingScheme = getOleShelvingSchemeLoaderHelperService().getShelvingSchemeById(shelvingSchemeId);
        if(oleShelvingScheme != null){
            return oleShelvingScheme;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.SHELVING_SCHEME_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.SHELVING_SCHEME_NOT_EXIST);
        }
    }

    @Override
    public Object exportShelvingSchemeByCode(String shelvingSchemeCode) {
        LOG.info("Inside exportShelvingSchemeByCode method.");
        OleShelvingScheme oleShelvingScheme = getOleShelvingSchemeLoaderHelperService().getShelvingSchemeByCode(shelvingSchemeCode);
        if(oleShelvingScheme != null){
            return oleShelvingScheme;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.SHELVING_SCHEME_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.SHELVING_SCHEME_NOT_EXIST);
        }
    }

    @Override
    public List<OleShelvingScheme> exportAllShelvingSchemes() {
        LOG.info("Inside exportAllShelvingSchemes method.");
        List<OleShelvingScheme> oleShelvingSchemes = getOleShelvingSchemeLoaderHelperService().getAllShelvingSchemes();
        return oleShelvingSchemes;
    }
}
