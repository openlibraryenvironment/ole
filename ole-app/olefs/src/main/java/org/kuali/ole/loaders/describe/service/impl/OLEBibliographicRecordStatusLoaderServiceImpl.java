package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEBibliographicRecordStatusBo;
import org.kuali.ole.loaders.describe.service.OLEBibliographicRecordStatusLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLEBibliographicRecordStatusLoaderService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEBibliographicRecordStatusLoaderServiceImpl implements OLEBibliographicRecordStatusLoaderService {

    private static final Logger LOG = Logger.getLogger(OLEBibliographicRecordStatusLoaderServiceImpl.class);
    private OLEBibliographicRecordStatusLoaderHelperService oleBibliographicRecordStatusLoaderHelperService;
    private OLELoaderService oleLoaderService;
    private BusinessObjectService businessObjectService;

    public OLEBibliographicRecordStatusLoaderHelperService getOleBibliographicRecordStatusLoaderHelperService() {
        if(oleBibliographicRecordStatusLoaderHelperService == null){
            oleBibliographicRecordStatusLoaderHelperService = new OLEBibliographicRecordStatusLoaderHelperServiceImpl();
        }
        return oleBibliographicRecordStatusLoaderHelperService;
    }

    public void setOleBibliographicRecordStatusLoaderHelperService(OLEBibliographicRecordStatusLoaderHelperService oleBibliographicRecordStatusLoaderHelperService) {
        this.oleBibliographicRecordStatusLoaderHelperService = oleBibliographicRecordStatusLoaderHelperService;
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
    public Object importBibliographicRecordStatus(String bodyContent, HttpContext context) {
        LOG.info("Inside importBibliographicRecordStatus method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        List<Integer> rejectBibliographicRecordStatusList = new ArrayList<Integer>();
        List<JSONObject> createdBibliographicRecordStatusObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray bibliographicRecordStatusJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    for (int index = 0; index < bibliographicRecordStatusJsonArray.length(); index ++) {
                        JSONObject jsonObject = null;
                        OleBibliographicRecordStatus bibliographicRecordStatus = new OleBibliographicRecordStatus();
                        try {
                            jsonObject = (JSONObject)bibliographicRecordStatusJsonArray.get(index);
                            if(jsonObject != null){
                                if(jsonObject.has("name")){
                                    String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"name");
                                    if(StringUtils.isNotBlank(name)){
                                        bibliographicRecordStatus.setBibliographicRecordStatusName(name);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("code")){
                                    String code = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"code");
                                    if(StringUtils.isNotBlank(code)){
                                        bibliographicRecordStatus.setBibliographicRecordStatusCode(code);
                                        validObject = true;
                                    }
                                }

                                if(jsonObject.has("source")){
                                    String source = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"source");
                                    if(StringUtils.isNotBlank(source)){
                                        bibliographicRecordStatus.setSource(source);
                                        validObject = true;
                                    }
                                }
                                if(jsonObject.has("active")){
                                    try{
                                        boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                                        bibliographicRecordStatus.setActive(active);
                                        validObject = true;
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        rejectBibliographicRecordStatusList.add(index+1);
                                        continue;
                                    }

                                }

                                if(jsonObject.has("sourceDate")){
                                    String sourceDate = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"sourceDate");
                                    if(StringUtils.isNotBlank(sourceDate)){
                                        try{
                                            Date date = OLELoaderConstants.DATE_FORMAT.parse(sourceDate);
                                            bibliographicRecordStatus.setSourceDate(new java.sql.Date(date.getTime()));
                                            validObject = true;
                                        }catch(Exception e){
                                            e.printStackTrace();
                                            rejectBibliographicRecordStatusList.add(index+1);
                                            continue;
                                        }
                                    }
                                }

                            }
                            if(bibliographicRecordStatus != null && validObject){
                                if(getOleBibliographicRecordStatusLoaderHelperService().getBibliographicRecordStatusByCode(bibliographicRecordStatus.getBibliographicRecordStatusCode()) == null){
                                    try {
                                        bibliographicRecordStatus = getBusinessObjectService().save(bibliographicRecordStatus);
                                        createdBibliographicRecordStatusObject.add((JSONObject)getOleBibliographicRecordStatusLoaderHelperService().formBibliographicRecordStatusExportResponse(bibliographicRecordStatus, OLELoaderConstants.OLELoaderContext.BIB_RECORD_STATUS,
                                                context.getRequest().getRequestUri().toASCIIString(), false));
                                    } catch (Exception e) {
                                        rejectBibliographicRecordStatusList.add(index+1);
                                        continue;
                                    }
                                }else{
                                    rejectBibliographicRecordStatusList.add(index+1);
                                    continue;
                                }


                            }else{
                                rejectBibliographicRecordStatusList.add(index+1);
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rejectBibliographicRecordStatusList.add(index+1);
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
        oleLoaderImportResponseBo.setOleRejectedBos(rejectBibliographicRecordStatusList);
        oleLoaderImportResponseBo.setOleCreatedBos(createdBibliographicRecordStatusObject);
        return oleLoaderImportResponseBo;
    }

    @Override
    public OLELoaderResponseBo updateBibliographicRecordStatusById(String bibliographicRecordStatusId, String bodyContent, HttpContext context) {
        LOG.info("Inside updateBibliographicRecordStatusById method.");
        OLEBibliographicRecordStatusBo oleBibliographicRecordStatusBo = new OLEBibliographicRecordStatusBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if (jsonObject != null) {
            if (jsonObject.has("name")) {
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "name");
                if (StringUtils.isNotBlank(name)) {
                    oleBibliographicRecordStatusBo.setBibliographicRecordStatusName(name);
                    validObject = true;
                }
            }

            if(jsonObject.has("source")){
                String source = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"source");
                if(StringUtils.isNotBlank(source)){
                    oleBibliographicRecordStatusBo.setSource(source);
                    validObject = true;
                }
            }

            if(jsonObject.has("active")){
                try{
                    boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                    oleBibliographicRecordStatusBo.setActive(active);
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
                        oleBibliographicRecordStatusBo.setSourceDate(new java.sql.Date(date.getTime()));
                        validObject = true;
                    }catch(Exception e){
                        e.printStackTrace();
                        return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.INVALID_DATE, OLELoaderConstants.OLEloaderStatus.INVALID_DATE);
                    }
                }
            }

            if (oleBibliographicRecordStatusBo != null && validObject) {
                OleBibliographicRecordStatus oleBibliographicRecordStatus = getOleBibliographicRecordStatusLoaderHelperService().getBibliographicRecordStatusById(bibliographicRecordStatusId);
                if (oleBibliographicRecordStatus != null) {
                    return getOleBibliographicRecordStatusLoaderHelperService().updateOleBibliographicRecordStatus(oleBibliographicRecordStatus, oleBibliographicRecordStatusBo, context);
                } else {
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BIB_RECORD_STATUS_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.BIB_RECORD_STATUS_NOT_EXIST);
                }
            } else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
            }
        }else {
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object exportBibliographicRecordStatusById(String bibliographicRecordStatusId) {
        LOG.info("Inside exportBibliographicRecordStatusById method.");
        OleBibliographicRecordStatus oleBibliographicRecordStatus = getOleBibliographicRecordStatusLoaderHelperService().getBibliographicRecordStatusById(bibliographicRecordStatusId);
        if(oleBibliographicRecordStatus != null){
            return oleBibliographicRecordStatus;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BIB_RECORD_STATUS_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.BIB_RECORD_STATUS_NOT_EXIST);
        }
    }

    @Override
    public Object exportBibliographicRecordStatusByCode(String bibliographicRecordStatusCode) {
        LOG.info("Inside exportBibliographicRecordStatusByCode method.");
        OleBibliographicRecordStatus oleBibliographicRecordStatus = getOleBibliographicRecordStatusLoaderHelperService().getBibliographicRecordStatusByCode(bibliographicRecordStatusCode);
        if(oleBibliographicRecordStatus != null){
            return oleBibliographicRecordStatus;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BIB_RECORD_STATUS_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.BIB_RECORD_STATUS_NOT_EXIST);
        }
    }

    @Override
    public List<OleBibliographicRecordStatus> exportAllBibliographicRecordStatus() {
        LOG.info("Inside exportAllBibliographicRecordStatus method.");
        List<OleBibliographicRecordStatus> oleBibliographicRecordStatus = getOleBibliographicRecordStatusLoaderHelperService().getAllBibliographicRecordStatus();
        return oleBibliographicRecordStatus;
    }
}
