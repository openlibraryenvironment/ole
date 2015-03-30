package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEShelvingSchemeBo;
import org.kuali.ole.loaders.describe.service.OLEShelvingSchemeLoaderHelperService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEShelvingSchemeLoaderHelperServiceImpl implements OLEShelvingSchemeLoaderHelperService {

    private static final Logger LOG = Logger.getLogger(OLEShelvingSchemeLoaderHelperServiceImpl.class);
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
    public OleShelvingScheme getShelvingSchemeById(String shelvingSchemeId) {
        Map shelvingSchemeMap = new HashMap();
        shelvingSchemeMap.put("shelvingSchemeId", shelvingSchemeId);
        return getBusinessObjectService().findByPrimaryKey(OleShelvingScheme.class, shelvingSchemeMap);
    }

    @Override
    public OleShelvingScheme getShelvingSchemeByCode(String shelvingSchemeCode) {
        Map shelvingSchemeMap = new HashMap();
        shelvingSchemeMap.put("shelvingSchemeCode", shelvingSchemeCode);
        return getBusinessObjectService().findByPrimaryKey(OleShelvingScheme.class, shelvingSchemeMap);
    }

    @Override
    public List<OleShelvingScheme> getAllShelvingSchemes() {
        return (List<OleShelvingScheme>) getBusinessObjectService().findAll(OleShelvingScheme.class);
    }

    @Override
    public Object formOleShelvingSchemeExportResponse(Object object, String shelvingSchemeContext, String uri, boolean addContext) {
        OleShelvingScheme oleShelvingScheme = (OleShelvingScheme) object;
        JSONObject jsonObject = new JSONObject();
        try {
            if(addContext)
                jsonObject.put("@context",shelvingSchemeContext);
            jsonObject.put("@id", OLELoaderConstants.SHELVING_SCHEME_URI + OLELoaderConstants.SLASH + oleShelvingScheme.getShelvingSchemeId());
            jsonObject.put("code",oleShelvingScheme.getShelvingSchemeCode());
            jsonObject.put("name",oleShelvingScheme.getShelvingSchemeName());
            jsonObject.put("source",oleShelvingScheme.getSource());
            jsonObject.put("sourceDate",oleShelvingScheme.getSourceDate());
            jsonObject.put("active",oleShelvingScheme.isActive());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public Object formAllShelvingSchemeExportResponse(HttpContext context, List<OleShelvingScheme> oleShelvingSchemeList, String shelvingSchemeContext, String uri) {
        JSONObject jsonResponseObject = new JSONObject();
        JSONArray paginationArray = new JSONArray();
        JSONObject paginationObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            jsonResponseObject.put("@context",shelvingSchemeContext);
            int startIndex = 0;
            int maxResults = 50;
            boolean validStartIndex = true;
            if(context.getRequest().getQueryParameters().containsKey("start")){
                try{
                    String start = context.getRequest().getQueryParameters().getFirst("start");
                    startIndex = Integer.parseInt(start);
                    if(startIndex < 0)
                        startIndex =0;
                    if(startIndex > oleShelvingSchemeList.size()){
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
            if(startIndex+maxResults > oleShelvingSchemeList.size())
                loopIterationEnd = oleShelvingSchemeList.size();
            else{
                loopIterationEnd = startIndex+maxResults;
            }

            if(validStartIndex){
                if(startIndex != 0){
                    paginationObject.put("rel","prev");
                    paginationObject.put("href",OLELoaderConstants.SHELVING_SCHEME_URI + "?start="+((startIndex-1)-maxResults < 0 ? 0 : (startIndex-1)-maxResults)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }
                if(loopIterationEnd != oleShelvingSchemeList.size()){
                    paginationObject = new JSONObject();
                    paginationObject.put("rel","next");
                    paginationObject.put("href",OLELoaderConstants.SHELVING_SCHEME_URI + "?start="+(loopIterationEnd+1)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }

                jsonResponseObject.put("links",paginationArray);
                for(int index = (startIndex == 0 ? 0 : startIndex-1) ; index < loopIterationEnd-1 ; index++){
                    OleShelvingScheme oleShelvingScheme = oleShelvingSchemeList.get(index);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("@id", OLELoaderConstants.SHELVING_SCHEME_URI + OLELoaderConstants.SLASH + oleShelvingScheme.getShelvingSchemeId());
                        jsonObject.put("code",oleShelvingScheme.getShelvingSchemeCode());
                        jsonObject.put("name",oleShelvingScheme.getShelvingSchemeName());
                        jsonObject.put("source",oleShelvingScheme.getSource());
                        jsonObject.put("sourceDate",oleShelvingScheme.getSourceDate());
                        jsonObject.put("active",oleShelvingScheme.isActive());
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
    public OLELoaderResponseBo updateOleShelvingScheme(OleShelvingScheme oleShelvingScheme, OLEShelvingSchemeBo oleShelvingSchemeBo, HttpContext context) {
        if(StringUtils.isNotBlank(oleShelvingSchemeBo.getShelvingSchemeCode()))
            oleShelvingScheme.setShelvingSchemeCode(oleShelvingSchemeBo.getShelvingSchemeCode());
        if(StringUtils.isNotBlank(oleShelvingSchemeBo.getShelvingSchemeName()))
            oleShelvingScheme.setShelvingSchemeName(oleShelvingSchemeBo.getShelvingSchemeName());
        if(StringUtils.isNotBlank(oleShelvingSchemeBo.getSource()))
            oleShelvingScheme.setSource(oleShelvingSchemeBo.getSource());
        if(oleShelvingSchemeBo.getSourceDate() != null)
            oleShelvingScheme.setSourceDate(new java.sql.Date(oleShelvingSchemeBo.getSourceDate().getTime()));
        oleShelvingScheme.setActive(oleShelvingSchemeBo.isActive());
        try{
            oleShelvingScheme = KRADServiceLocator.getBusinessObjectService().save(oleShelvingScheme);
            String details = formOleShelvingSchemeExportResponse(oleShelvingScheme, OLELoaderConstants.OLELoaderContext.SHELVING_SCHEME,
                    context.getRequest().getRequestUri().toASCIIString(), true).toString();
            return getOleLoaderService().generateResponse(
                    OLELoaderConstants.OLEloaderMessage.SHELVING_SCHEME_SUCCESS,
                    OLELoaderConstants.OLEloaderStatus.SHELVING_SCHEME_SUCCESS,details);
        }catch(Exception e){
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.SHELVING_SCHEME_FAILED, OLELoaderConstants.OLEloaderStatus.SHELVING_SCHEME_FAILED);
        }
    }
}
