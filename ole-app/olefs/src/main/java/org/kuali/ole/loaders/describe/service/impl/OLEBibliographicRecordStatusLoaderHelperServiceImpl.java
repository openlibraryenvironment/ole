package org.kuali.ole.loaders.describe.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.OLEBibliographicRecordStatusBo;
import org.kuali.ole.loaders.describe.service.OLEBibliographicRecordStatusLoaderHelperService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEBibliographicRecordStatusLoaderHelperServiceImpl implements OLEBibliographicRecordStatusLoaderHelperService {

    private static final Logger LOG = Logger.getLogger(OLEBibliographicRecordStatusLoaderHelperServiceImpl.class);
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
    public OleBibliographicRecordStatus getBibliographicRecordStatusById(String bibliographicRecordStatusId) {
        Map bibliographicRecordStatusMap = new HashMap();
        bibliographicRecordStatusMap.put("bibliographicRecordStatusId", bibliographicRecordStatusId);
        return getBusinessObjectService().findByPrimaryKey(OleBibliographicRecordStatus.class, bibliographicRecordStatusMap);
    }

    @Override
    public OleBibliographicRecordStatus getBibliographicRecordStatusByCode(String bibliographicRecordStatusCode) {
        Map bibliographicRecordStatusMap = new HashMap();
        bibliographicRecordStatusMap.put("bibliographicRecordStatusCode", bibliographicRecordStatusCode);
        return getBusinessObjectService().findByPrimaryKey(OleBibliographicRecordStatus.class, bibliographicRecordStatusMap);
    }

    @Override
    public List<OleBibliographicRecordStatus> getAllBibliographicRecordStatus() {
        return (List<OleBibliographicRecordStatus>) getBusinessObjectService().findAll(OleBibliographicRecordStatus.class);
    }

    @Override
    public Object formBibliographicRecordStatusExportResponse(Object object, String bibliographicRecordStatusContext, String uri, boolean addContext) {
        OleBibliographicRecordStatus oleBibliographicRecordStatus = (OleBibliographicRecordStatus) object;
        JSONObject jsonObject = new JSONObject();
        try {
            if(addContext)
                jsonObject.put("@context",bibliographicRecordStatusContext);
            jsonObject.put("@id", OLELoaderConstants.STAT_SEARCH_CODE_URI + OLELoaderConstants.SLASH + oleBibliographicRecordStatus.getBibliographicRecordStatusId());
            jsonObject.put("code",oleBibliographicRecordStatus.getBibliographicRecordStatusCode());
            jsonObject.put("name",oleBibliographicRecordStatus.getBibliographicRecordStatusName());
            jsonObject.put("source",oleBibliographicRecordStatus.getSource());
            jsonObject.put("sourceDate",oleBibliographicRecordStatus.getSourceDate());
            jsonObject.put("active",oleBibliographicRecordStatus.isActive());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public Object formAllBibliographicRecordStatusExportResponse(HttpContext context, List<OleBibliographicRecordStatus> oleBibliographicRecordStatusList, String bibliographicRecordStatusContext, String uri) {
        JSONObject jsonResponseObject = new JSONObject();
        JSONArray paginationArray = new JSONArray();
        JSONObject paginationObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            jsonResponseObject.put("@context",bibliographicRecordStatusContext);
            int startIndex = 0;
            int maxResults = 50;
            boolean validStartIndex = true;
            if(context.getRequest().getQueryParameters().containsKey("start")){
                try{
                    String start = context.getRequest().getQueryParameters().getFirst("start");
                    startIndex = Integer.parseInt(start);
                    if(startIndex < 0)
                        startIndex =0;
                    if(startIndex > oleBibliographicRecordStatusList.size()){
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
            if(startIndex+maxResults > oleBibliographicRecordStatusList.size())
                loopIterationEnd = oleBibliographicRecordStatusList.size();
            else{
                loopIterationEnd = startIndex+maxResults;
            }

            if(validStartIndex){
                if(startIndex != 0){
                    paginationObject.put("rel","prev");
                    paginationObject.put("href",OLELoaderConstants.STAT_SEARCH_CODE_URI + "?start="+((startIndex-1)-maxResults < 0 ? 0 : (startIndex-1)-maxResults)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }
                if(loopIterationEnd != oleBibliographicRecordStatusList.size()){
                    paginationObject = new JSONObject();
                    paginationObject.put("rel","next");
                    paginationObject.put("href",OLELoaderConstants.STAT_SEARCH_CODE_URI + "?start="+(loopIterationEnd+1)+"&maxResults="+maxResults);
                    paginationArray.put(paginationObject);
                }

                jsonResponseObject.put("links",paginationArray);
                for(int index = (startIndex == 0 ? 0 : startIndex-1) ; index < loopIterationEnd-1 ; index++){
                    OleBibliographicRecordStatus oleBibliographicRecordStatus = oleBibliographicRecordStatusList.get(index);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("@id", OLELoaderConstants.STAT_SEARCH_CODE_URI + OLELoaderConstants.SLASH + oleBibliographicRecordStatus.getBibliographicRecordStatusId());
                        jsonObject.put("code",oleBibliographicRecordStatus.getBibliographicRecordStatusCode());
                        jsonObject.put("name",oleBibliographicRecordStatus.getBibliographicRecordStatusName());
                        jsonObject.put("source",oleBibliographicRecordStatus.getSource());
                        jsonObject.put("sourceDate",oleBibliographicRecordStatus.getSourceDate());
                        jsonObject.put("active",oleBibliographicRecordStatus.isActive());
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
    public OLELoaderResponseBo updateOleBibliographicRecordStatus(OleBibliographicRecordStatus oleInstanceBibliographicRecordStatus, OLEBibliographicRecordStatusBo oleBibliographicRecordStatusBo, HttpContext context) {
        if(StringUtils.isNotBlank(oleBibliographicRecordStatusBo.getBibliographicRecordStatusName()))
            oleInstanceBibliographicRecordStatus.setBibliographicRecordStatusName(oleBibliographicRecordStatusBo.getBibliographicRecordStatusName());
        if(StringUtils.isNotBlank(oleBibliographicRecordStatusBo.getSource()))
            oleInstanceBibliographicRecordStatus.setSource(oleBibliographicRecordStatusBo.getSource());
        if(oleBibliographicRecordStatusBo.getSourceDate() != null)
            oleInstanceBibliographicRecordStatus.setSourceDate(new java.sql.Date(oleBibliographicRecordStatusBo.getSourceDate().getTime()));
        oleInstanceBibliographicRecordStatus.setActive(oleBibliographicRecordStatusBo.isActive());
        try{
            oleInstanceBibliographicRecordStatus = KRADServiceLocator.getBusinessObjectService().save(oleInstanceBibliographicRecordStatus);
            String details = formBibliographicRecordStatusExportResponse(oleInstanceBibliographicRecordStatus, OLELoaderConstants.OLELoaderContext.STAT_SEARCH_CODE,
                    context.getRequest().getRequestUri().toASCIIString(), true).toString();
            return getOleLoaderService().generateResponse(
                    OLELoaderConstants.OLEloaderMessage.STAT_SEARCH_CODE_SUCCESS,
                    OLELoaderConstants.OLEloaderStatus.STAT_SEARCH_CODE_SUCCESS,details);
        }catch(Exception e){
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.STAT_SEARCH_CODE_FAILED, OLELoaderConstants.OLEloaderStatus.STAT_SEARCH_CODE_FAILED);
        }

    }
}
