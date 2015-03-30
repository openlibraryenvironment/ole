package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEStatisticalSearchingCodeBo;
import org.kuali.ole.loaders.describe.service.OLEStatisticalSearchingCodeLoaderHelperService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEStatisticalSearchingCodeLoaderHelperServiceImpl implements OLEStatisticalSearchingCodeLoaderHelperService {

    private static final Logger LOG = Logger.getLogger(OLEStatisticalSearchingCodeLoaderHelperServiceImpl.class);
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
    public OleStatisticalSearchingCodes getStatisticalSearchingCodeById(String statisticalSearchingCodeId) {
        Map statisticalSearchingCodeMap = new HashMap();
        statisticalSearchingCodeMap.put("statisticalSearchingCodeId", statisticalSearchingCodeId);
        return getBusinessObjectService().findByPrimaryKey(OleStatisticalSearchingCodes.class, statisticalSearchingCodeMap);
    }

    @Override
    public OleStatisticalSearchingCodes getStatisticalSearchingCodeByCode(String statisticalSearchingCodeCode) {
        Map statisticalSearchingCodeMap = new HashMap();
        statisticalSearchingCodeMap.put("statisticalSearchingCodeCode", statisticalSearchingCodeCode);
        return getBusinessObjectService().findByPrimaryKey(OleStatisticalSearchingCodes.class, statisticalSearchingCodeMap);
    }

    @Override
    public List<OleStatisticalSearchingCodes> getAllStatisticalSearchingCode() {
        return (List<OleStatisticalSearchingCodes>) getBusinessObjectService().findAll(OleStatisticalSearchingCodes.class);
    }

    @Override
    public Object formStatisticalSearchingCodeExportResponse(Object object, String statisticalSearchingCodeContext, String uri, boolean addContext) {
        OleStatisticalSearchingCodes oleStatisticalSearchingCode = (OleStatisticalSearchingCodes) object;
        JSONObject jsonObject = new JSONObject();
        try {
            if(addContext)
                jsonObject.put("@context",statisticalSearchingCodeContext);
            jsonObject.put("@id", OLELoaderConstants.STAT_SEARCH_CODE_URI + OLELoaderConstants.SLASH + oleStatisticalSearchingCode.getStatisticalSearchingCodeId());
            jsonObject.put("code",oleStatisticalSearchingCode.getStatisticalSearchingCode());
            jsonObject.put("name",oleStatisticalSearchingCode.getStatisticalSearchingName());
            jsonObject.put("source",oleStatisticalSearchingCode.getSource());
            jsonObject.put("sourceDate",oleStatisticalSearchingCode.getSourceDate());
            jsonObject.put("active",oleStatisticalSearchingCode.isActive());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public Object formAllStatisticalSearchingCodeExportResponse(HttpContext context, List<OleStatisticalSearchingCodes> oleStatisticalSearchingCodeList, String statisticalSearchingCodeContext, String uri) {
        JSONObject jsonResponseObject = new JSONObject();
        JSONArray paginationArray = new JSONArray();
        JSONObject paginationObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            jsonResponseObject.put("@context",statisticalSearchingCodeContext);
            int startIndex = 0;
            int maxResults = 50;
            boolean validStartIndex = true;
            if(context.getRequest().getQueryParameters().containsKey("start")){
                try{
                    String start = context.getRequest().getQueryParameters().getFirst("start");
                    startIndex = Integer.parseInt(start);
                    if(startIndex < 0)
                        startIndex =0;
                    if(startIndex > oleStatisticalSearchingCodeList.size()){
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
            if(startIndex+maxResults > oleStatisticalSearchingCodeList.size())
                loopIterationEnd = oleStatisticalSearchingCodeList.size();
            else{
                loopIterationEnd = startIndex+maxResults;
            }

            if(validStartIndex){
                if(startIndex != 0){
                    paginationObject.put("rel","prev");
                    paginationObject.put("href",OLELoaderConstants.STAT_SEARCH_CODE_URI + "?start="+((startIndex-1)-maxResults < 0 ? 0 : (startIndex-1)-maxResults)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }
                if(loopIterationEnd != oleStatisticalSearchingCodeList.size()){
                    paginationObject = new JSONObject();
                    paginationObject.put("rel","next");
                    paginationObject.put("href",OLELoaderConstants.STAT_SEARCH_CODE_URI + "?start="+(loopIterationEnd+1)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }

                jsonResponseObject.put("links",paginationArray);
                for(int index = (startIndex == 0 ? 0 : startIndex-1) ; index < loopIterationEnd-1 ; index++){
                    OleStatisticalSearchingCodes oleStatisticalSearchingCode = oleStatisticalSearchingCodeList.get(index);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("@id", OLELoaderConstants.STAT_SEARCH_CODE_URI + OLELoaderConstants.SLASH + oleStatisticalSearchingCode.getStatisticalSearchingCodeId());
                        jsonObject.put("code",oleStatisticalSearchingCode.getStatisticalSearchingCode());
                        jsonObject.put("name",oleStatisticalSearchingCode.getStatisticalSearchingName());
                        jsonObject.put("source",oleStatisticalSearchingCode.getSource());
                        jsonObject.put("sourceDate",oleStatisticalSearchingCode.getSourceDate());
                        jsonObject.put("active",oleStatisticalSearchingCode.isActive());
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
    public OLELoaderResponseBo updateOleStatisticalSearchingCode(OleStatisticalSearchingCodes oleInstanceStatisticalSearchingCode, OLEStatisticalSearchingCodeBo oleStatisticalSearchingCodeBo, HttpContext context) {
        if(StringUtils.isNotBlank(oleStatisticalSearchingCodeBo.getStatisticalSearchingName()))
            oleInstanceStatisticalSearchingCode.setStatisticalSearchingName(oleStatisticalSearchingCodeBo.getStatisticalSearchingName());
        if(StringUtils.isNotBlank(oleStatisticalSearchingCodeBo.getSource()))
            oleInstanceStatisticalSearchingCode.setSource(oleStatisticalSearchingCodeBo.getSource());
        if(oleStatisticalSearchingCodeBo.getSourceDate() != null)
            oleInstanceStatisticalSearchingCode.setSourceDate(new java.sql.Date(oleStatisticalSearchingCodeBo.getSourceDate().getTime()));
        oleInstanceStatisticalSearchingCode.setActive(oleStatisticalSearchingCodeBo.isActive());
        try{
            oleInstanceStatisticalSearchingCode = KRADServiceLocator.getBusinessObjectService().save(oleInstanceStatisticalSearchingCode);
            String details = formStatisticalSearchingCodeExportResponse(oleInstanceStatisticalSearchingCode, OLELoaderConstants.OLELoaderContext.STAT_SEARCH_CODE,
                    context.getRequest().getRequestUri().toASCIIString(), true).toString();
            return getOleLoaderService().generateResponse(
                    OLELoaderConstants.OLEloaderMessage.STAT_SEARCH_CODE_SUCCESS,
                    OLELoaderConstants.OLEloaderStatus.STAT_SEARCH_CODE_SUCCESS,details);
        }catch(Exception e){
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.STAT_SEARCH_CODE_FAILED, OLELoaderConstants.OLEloaderStatus.STAT_SEARCH_CODE_FAILED);
        }

    }
}
