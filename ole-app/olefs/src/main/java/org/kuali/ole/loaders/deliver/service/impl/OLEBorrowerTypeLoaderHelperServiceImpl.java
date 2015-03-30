package org.kuali.ole.loaders.deliver.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.deliver.bo.OLEBorrowerTypeBo;
import org.kuali.ole.loaders.deliver.service.OLEBorrowerTypeLoaderHelperService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEBorrowerTypeLoaderHelperServiceImpl implements OLEBorrowerTypeLoaderHelperService {

    private static final Logger LOG = Logger.getLogger(OLEBorrowerTypeLoaderHelperServiceImpl.class);
    private BusinessObjectService businessObjectService;
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

    public OLELoaderService getOleLoaderService() {
        if(oleLoaderService == null){
            oleLoaderService = new OLELoaderServiceImpl();
        }
        return oleLoaderService;
    }

    public void setOleLoaderService(OLELoaderService oleLoaderService) {
        this.oleLoaderService = oleLoaderService;
    }

    @Override
    public OleBorrowerType getBorrowerTypeById(String borrowerTypeId) {
        Map borrowerTypeMap = new HashMap();
        borrowerTypeMap.put("borrowerTypeId", borrowerTypeId);
        return getBusinessObjectService().findByPrimaryKey(OleBorrowerType.class, borrowerTypeMap);
    }

    @Override
    public OleBorrowerType getBorrowerTypeByCode(String borrowerTypeCode) {
        Map borrowerTypeMap = new HashMap();
        borrowerTypeMap.put("borrowerTypeCode", borrowerTypeCode);
        return getBusinessObjectService().findByPrimaryKey(OleBorrowerType.class, borrowerTypeMap);
    }

    @Override
    public List<OleBorrowerType> getAllBorrowerTypes() {
        return (List<OleBorrowerType>) getBusinessObjectService().findAll(OleBorrowerType.class);
    }

    @Override
    public Object formOleBorrowerTypeExportResponse(Object object, String borrowerTypeContext, String uri, boolean addContext) {
        OleBorrowerType oleBorrowerType = (OleBorrowerType) object;
        JSONObject jsonObject = new JSONObject();
        try {
            if(addContext)
                jsonObject.put("@context",borrowerTypeContext);
            jsonObject.put("@id", OLELoaderConstants.BORROWER_TYPE_URI + OLELoaderConstants.SLASH + oleBorrowerType.getBorrowerTypeId());
            jsonObject.put("code",oleBorrowerType.getBorrowerTypeCode());
            jsonObject.put("name",oleBorrowerType.getBorrowerTypeName());
            jsonObject.put("description",oleBorrowerType.getBorrowerTypeDescription());
            jsonObject.put("active",oleBorrowerType.isActive());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public Object formAllBorrowerTypeExportResponse(HttpContext context, List<OleBorrowerType> oleBorrowerTypeList, String borrowerTypeContext, String uri) {
        JSONObject jsonResponseObject = new JSONObject();
        JSONArray paginationArray = new JSONArray();
        JSONObject paginationObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            jsonResponseObject.put("@context",borrowerTypeContext);
            int startIndex = 0;
            int maxResults = 50;
            boolean validStartIndex = true;
            if(context.getRequest().getQueryParameters().containsKey("start")){
                try{
                    String start = context.getRequest().getQueryParameters().getFirst("start");
                    startIndex = Integer.parseInt(start);
                    if(startIndex < 0)
                        startIndex =0;
                    if(startIndex > oleBorrowerTypeList.size()){
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
            if(startIndex+maxResults > oleBorrowerTypeList.size())
                loopIterationEnd = oleBorrowerTypeList.size();
            else{
                loopIterationEnd = startIndex+maxResults;
            }

            if(validStartIndex){
                if(startIndex != 0){
                    paginationObject.put("rel","prev");
                    paginationObject.put("href",OLELoaderConstants.BORROWER_TYPE_URI + "?start="+((startIndex-1)-maxResults < 0 ? 0 : (startIndex-1)-maxResults)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }
                if(loopIterationEnd != oleBorrowerTypeList.size()){
                    paginationObject = new JSONObject();
                    paginationObject.put("rel","next");
                    paginationObject.put("href",OLELoaderConstants.BORROWER_TYPE_URI + "?start="+(loopIterationEnd+1)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }

                jsonResponseObject.put("links",paginationArray);
                for(int index = (startIndex == 0 ? 0 : startIndex-1) ; index < loopIterationEnd-1 ; index++){
                    OleBorrowerType oleBorrowerType = oleBorrowerTypeList.get(index);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("@id", OLELoaderConstants.BORROWER_TYPE_URI + OLELoaderConstants.SLASH + oleBorrowerType.getBorrowerTypeId());
                        jsonObject.put("code",oleBorrowerType.getBorrowerTypeCode());
                        jsonObject.put("name",oleBorrowerType.getBorrowerTypeName());
                        jsonObject.put("description",oleBorrowerType.getBorrowerTypeDescription());
                        jsonObject.put("active",oleBorrowerType.isActive());
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
    public OLELoaderResponseBo updateOleBorrowerType(OleBorrowerType oleBorrowerType, OLEBorrowerTypeBo oleBorrowerTypeBo, HttpContext context) {
        if(StringUtils.isNotBlank(oleBorrowerTypeBo.getBorrowerTypeName()))
            oleBorrowerType.setBorrowerTypeName(oleBorrowerTypeBo.getBorrowerTypeName());
        if(StringUtils.isNotBlank(oleBorrowerTypeBo.getBorrowerTypeDescription()))
            oleBorrowerType.setBorrowerTypeDescription(oleBorrowerTypeBo.getBorrowerTypeDescription());
        oleBorrowerType.setActive(oleBorrowerTypeBo.isActive());
        try{
            oleBorrowerType = KRADServiceLocator.getBusinessObjectService().save(oleBorrowerType);
            String details = formOleBorrowerTypeExportResponse(oleBorrowerType, OLELoaderConstants.OLELoaderContext.BORROWER_TYPE,
                    context.getRequest().getRequestUri().toASCIIString(), true).toString();
            return getOleLoaderService().generateResponse(
                    OLELoaderConstants.OLEloaderMessage.BORROWER_TYPE_SUCCESS,
                    OLELoaderConstants.OLEloaderStatus.BORROWER_TYPE_SUCCESS,details);
        }catch(Exception e){
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BORROWER_TYPE_FAILED, OLELoaderConstants.OLEloaderStatus.BORROWER_TYPE_FAILED);
        }

    }
}
