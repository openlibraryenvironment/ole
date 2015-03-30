package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEItemTypeBo;
import org.kuali.ole.loaders.describe.service.OLEItemTypeLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLEItemTypeLoaderHelperService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEItemTypeLoaderHelperServiceImpl implements OLEItemTypeLoaderHelperService {

    private static final Logger LOG = Logger.getLogger(OLEItemTypeLoaderHelperServiceImpl.class);
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
    public OleInstanceItemType getItemTypeById(String itemTypeId) {
        Map itemTypeMap = new HashMap();
        itemTypeMap.put("itemTypeId", itemTypeId);
        return getBusinessObjectService().findByPrimaryKey(OleInstanceItemType.class, itemTypeMap);
    }

    @Override
    public OleInstanceItemType getItemTypeByCode(String itemTypeCode) {
        Map itemTypeMap = new HashMap();
        itemTypeMap.put("itemTypeCode", itemTypeCode);
        return getBusinessObjectService().findByPrimaryKey(OleInstanceItemType.class, itemTypeMap);
    }

    @Override
    public List<OleInstanceItemType> getAllItemTypes() {
        return (List<OleInstanceItemType>) getBusinessObjectService().findAll(OleInstanceItemType.class);
    }

    @Override
    public Object formOleInstanceItemTypeExportResponse(Object object, String itemTypeContext, String uri, boolean addContext) {
        OleInstanceItemType oleItemType = (OleInstanceItemType) object;
        JSONObject jsonObject = new JSONObject();
        try {
            if(addContext)
                jsonObject.put("@context",itemTypeContext);
            jsonObject.put("@id", OLELoaderConstants.ITEM_TYPE_URI + OLELoaderConstants.SLASH + oleItemType.getInstanceItemTypeId());
            jsonObject.put("code",oleItemType.getInstanceItemTypeCode());
            jsonObject.put("name",oleItemType.getInstanceItemTypeName());
            jsonObject.put("description",oleItemType.getInstanceItemTypeDesc());
            jsonObject.put("source",oleItemType.getSource());
            jsonObject.put("sourceDate",oleItemType.getSourceDate());
            jsonObject.put("active",oleItemType.isActive());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public Object formAllItemTypeExportResponse(HttpContext context, List<OleInstanceItemType> oleItemTypeList, String itemTypeContext, String uri) {
        JSONObject jsonResponseObject = new JSONObject();
        JSONArray paginationArray = new JSONArray();
        JSONObject paginationObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            jsonResponseObject.put("@context",itemTypeContext);
            int startIndex = 0;
            int maxResults = 50;
            boolean validStartIndex = true;
            if(context.getRequest().getQueryParameters().containsKey("start")){
                try{
                    String start = context.getRequest().getQueryParameters().getFirst("start");
                    startIndex = Integer.parseInt(start);
                    if(startIndex < 0)
                        startIndex =0;
                    if(startIndex > oleItemTypeList.size()){
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
            if(startIndex+maxResults > oleItemTypeList.size())
                loopIterationEnd = oleItemTypeList.size();
            else{
                loopIterationEnd = startIndex+maxResults;
            }

            if(validStartIndex){
                if(startIndex != 0){
                    paginationObject.put("rel","prev");
                    paginationObject.put("href",OLELoaderConstants.ITEM_TYPE_URI + "?start="+((startIndex-1)-maxResults < 0 ? 0 : (startIndex-1)-maxResults)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }
                if(loopIterationEnd != oleItemTypeList.size()){
                    paginationObject = new JSONObject();
                    paginationObject.put("rel","next");
                    paginationObject.put("href",OLELoaderConstants.ITEM_TYPE_URI + "?start="+(loopIterationEnd+1)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }

                jsonResponseObject.put("links",paginationArray);
                for(int index = (startIndex == 0 ? 0 : startIndex-1) ; index < loopIterationEnd-1 ; index++){
                    OleInstanceItemType oleItemType = oleItemTypeList.get(index);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("@id", OLELoaderConstants.ITEM_TYPE_URI + OLELoaderConstants.SLASH + oleItemType.getInstanceItemTypeId());
                        jsonObject.put("code",oleItemType.getInstanceItemTypeCode());
                        jsonObject.put("name",oleItemType.getInstanceItemTypeName());
                        jsonObject.put("description",oleItemType.getInstanceItemTypeDesc());
                        jsonObject.put("source",oleItemType.getSource());
                        jsonObject.put("sourceDate",oleItemType.getSourceDate());
                        jsonObject.put("active",oleItemType.isActive());
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
    public OLELoaderResponseBo updateOleInstanceItemType(OleInstanceItemType oleInstanceItemType, OLEItemTypeBo oleItemTypeBo, HttpContext context) {
        if(StringUtils.isNotBlank(oleItemTypeBo.getItemTypeName()))
            oleInstanceItemType.setInstanceItemTypeName(oleItemTypeBo.getItemTypeName());
        if(StringUtils.isNotBlank(oleItemTypeBo.getItemTypeDesc()))
            oleInstanceItemType.setInstanceItemTypeDesc(oleItemTypeBo.getItemTypeDesc());
        if(StringUtils.isNotBlank(oleItemTypeBo.getSource()))
            oleInstanceItemType.setSource(oleItemTypeBo.getSource());
        if(StringUtils.isNotBlank(oleItemTypeBo.getItemTypeCode()))
            oleInstanceItemType.setInstanceItemTypeCode(oleItemTypeBo.getItemTypeCode());
        if(oleItemTypeBo.getSourceDate() != null)
            oleInstanceItemType.setSourceDate(new java.sql.Date(oleItemTypeBo.getSourceDate().getTime()));
        oleInstanceItemType.setActive(oleItemTypeBo.isActive());
        try{
            oleInstanceItemType = KRADServiceLocator.getBusinessObjectService().save(oleInstanceItemType);
            String details = formOleInstanceItemTypeExportResponse(oleInstanceItemType, OLELoaderConstants.OLELoaderContext.ITEM_TYPE,
                    context.getRequest().getRequestUri().toASCIIString(), true).toString();
            return getOleLoaderService().generateResponse(
                    OLELoaderConstants.OLEloaderMessage.ITEM_TYPE_SUCCESS,
                    OLELoaderConstants.OLEloaderStatus.ITEM_TYPE_SUCCESS,details);
        }catch(Exception e){
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.ITEM_TYPE_FAILED, OLELoaderConstants.OLEloaderStatus.ITEM_TYPE_FAILED);
        }

    }
}
