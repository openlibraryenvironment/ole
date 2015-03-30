package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEItemAvailableStatusBo;
import org.kuali.ole.loaders.describe.service.OLEItemAvailableStatusLoaderHelperService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEItemAvailableStatusLoaderHelperServiceImpl implements OLEItemAvailableStatusLoaderHelperService {

    private static final Logger LOG = Logger.getLogger(OLEItemAvailableStatusLoaderHelperServiceImpl.class);
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
    public OleItemAvailableStatus getItemAvailableStatusById(String itemAvailableStatusId) {
        Map itemAvailableStatusMap = new HashMap();
        itemAvailableStatusMap.put("itemAvailableStatusId", itemAvailableStatusId);
        return getBusinessObjectService().findByPrimaryKey(OleItemAvailableStatus.class, itemAvailableStatusMap);
    }

    @Override
    public OleItemAvailableStatus getItemAvailableStatusByCode(String itemAvailableStatusCode) {
        Map itemAvailableStatusMap = new HashMap();
        itemAvailableStatusMap.put("itemAvailableStatusCode", itemAvailableStatusCode);
        return getBusinessObjectService().findByPrimaryKey(OleItemAvailableStatus.class, itemAvailableStatusMap);
    }

    @Override
    public List<OleItemAvailableStatus> getAllItemAvailableStatus() {
        return (List<OleItemAvailableStatus>) getBusinessObjectService().findAll(OleItemAvailableStatus.class);
    }

    @Override
    public Object formItemAvailableStatusExportResponse(Object object, String itemAvailableStatusContext, String uri, boolean addContext) {
        OleItemAvailableStatus oleItemAvailableStatus = (OleItemAvailableStatus) object;
        JSONObject jsonObject = new JSONObject();
        try {
            if(addContext)
                jsonObject.put("@context",itemAvailableStatusContext);
            jsonObject.put("@id", OLELoaderConstants.ITEM_STATUS_URI + OLELoaderConstants.SLASH + oleItemAvailableStatus.getItemAvailableStatusId());
            jsonObject.put("code",oleItemAvailableStatus.getItemAvailableStatusCode());
            jsonObject.put("name",oleItemAvailableStatus.getItemAvailableStatusName());
            jsonObject.put("active",oleItemAvailableStatus.isActive());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public Object formAllItemAvailableStatusExportResponse(HttpContext context, List<OleItemAvailableStatus> oleItemAvailableStatusList, String itemAvailableStatusContext, String uri) {
        JSONObject jsonResponseObject = new JSONObject();
        JSONArray paginationArray = new JSONArray();
        JSONObject paginationObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            jsonResponseObject.put("@context",itemAvailableStatusContext);
            int startIndex = 0;
            int maxResults = 50;
            boolean validStartIndex = true;
            if(context.getRequest().getQueryParameters().containsKey("start")){
                try{
                    String start = context.getRequest().getQueryParameters().getFirst("start");
                    startIndex = Integer.parseInt(start);
                    if(startIndex < 0)
                        startIndex =0;
                    if(startIndex > oleItemAvailableStatusList.size()){
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
            if(startIndex+maxResults > oleItemAvailableStatusList.size())
                loopIterationEnd = oleItemAvailableStatusList.size();
            else{
                loopIterationEnd = startIndex+maxResults;
            }

            if(validStartIndex){
                if(startIndex != 0){
                    paginationObject.put("rel","prev");
                    paginationObject.put("href",OLELoaderConstants.ITEM_STATUS_URI + "?start="+((startIndex-1)-maxResults < 0 ? 0 : (startIndex-1)-maxResults)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }
                if(loopIterationEnd != oleItemAvailableStatusList.size()){
                    paginationObject = new JSONObject();
                    paginationObject.put("rel","next");
                    paginationObject.put("href",OLELoaderConstants.ITEM_STATUS_URI + "?start="+(loopIterationEnd+1)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }

                jsonResponseObject.put("links",paginationArray);
                for(int index = (startIndex == 0 ? 0 : startIndex-1) ; index < loopIterationEnd-1 ; index++){
                    OleItemAvailableStatus oleItemAvailableStatus = oleItemAvailableStatusList.get(index);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("@id", OLELoaderConstants.ITEM_STATUS_URI + OLELoaderConstants.SLASH + oleItemAvailableStatus.getItemAvailableStatusId());
                        jsonObject.put("code",oleItemAvailableStatus.getItemAvailableStatusCode());
                        jsonObject.put("name",oleItemAvailableStatus.getItemAvailableStatusName());
                        jsonObject.put("active",oleItemAvailableStatus.isActive());
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
    public OLELoaderResponseBo updateOleItemAvailableStatus(OleItemAvailableStatus oleInstanceItemAvailableStatus, OLEItemAvailableStatusBo oleItemAvailableStatusBo, HttpContext context) {
        if(oleItemAvailableStatusBo.getItemAvailableStatusName() != null)
            oleInstanceItemAvailableStatus.setItemAvailableStatusName(oleItemAvailableStatusBo.getItemAvailableStatusName());
        oleInstanceItemAvailableStatus.setActive(oleItemAvailableStatusBo.isActive());
        try{
            oleInstanceItemAvailableStatus = KRADServiceLocator.getBusinessObjectService().save(oleInstanceItemAvailableStatus);
            String details = formItemAvailableStatusExportResponse(oleInstanceItemAvailableStatus, OLELoaderConstants.OLELoaderContext.ITEM_STATUS,
                    context.getRequest().getRequestUri().toASCIIString(), true).toString();
            return getOleLoaderService().generateResponse(
                    OLELoaderConstants.OLEloaderMessage.ITEM_STATUS_SUCCESS,
                    OLELoaderConstants.OLEloaderStatus.ITEM_STATUS_SUCCESS,details);
        }catch(Exception e){
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.ITEM_STATUS_FAILED, OLELoaderConstants.OLEloaderStatus.ITEM_STATUS_FAILED);
        }

    }
}
