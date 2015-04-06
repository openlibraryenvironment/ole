package org.kuali.ole.loaders.common.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.deliver.service.OLEBorrowerTypeLoaderHelperService;
import org.kuali.ole.loaders.deliver.service.OLEBorrowerTypeLoaderService;
import org.kuali.ole.loaders.deliver.service.impl.OLEBorrowerTypeLoaderHelperServiceImpl;
import org.kuali.ole.loaders.deliver.service.impl.OLEBorrowerTypeLoaderServiceImpl;
import org.kuali.ole.loaders.describe.service.*;
import org.kuali.ole.loaders.describe.service.impl.*;

import javax.ws.rs.core.Response;
import java.util.Iterator;

/**
 * Created by sheiksalahudeenm on 2/16/15.
 */
public class OLELoaderServiceImpl implements OLELoaderService {

    private static final Logger LOG = Logger.getLogger(OLELoaderServiceImpl.class);

    private OLEShelvingSchemeLoaderService oleShelvingSchemeLoaderService;
    private OLEShelvingSchemeLoaderHelperService oleShelvingSchemeLoaderHelperService;

    private OLELocationLoaderService oleLocationLoaderService;
    private OLELocationLoaderHelperService oleLocationLoaderHelperService;

    private OLEBorrowerTypeLoaderService oleBorrowerTypeLoaderService;
    private OLEBorrowerTypeLoaderHelperService oleBorrowerTypeLoaderHelperService;


    private OLEItemTypeLoaderService oleItemTypeLoaderService;
    private OLEItemTypeLoaderHelperService oleItemTypeLoaderHelperService;

    private OLEStatisticalSearchingCodeLoaderService oleStatisticalSearchingCodeLoaderService;
    private OLEStatisticalSearchingCodeLoaderHelperService oleStatisticalSearchingCodeLoaderHelperService;

    private OLEItemAvailableStatusLoaderService oleItemAvailableStatusLoaderService;
    private OLEItemAvailableStatusLoaderHelperService oleItemAvailableStatusLoaderHelperService;

    private OLEBibliographicRecordStatusLoaderService oleBibliographicRecordStatusLoaderService;
    private OLEBibliographicRecordStatusLoaderHelperService oleBibliographicRecordStatusLoaderHelperService;

    @Override
    public OLELoaderResponseBo generateResponse(String message, int statusCode) {
        OLELoaderResponseBo oleLoaderResponseBo=new OLELoaderResponseBo();
        oleLoaderResponseBo.setMessage(message);
        oleLoaderResponseBo.setStatusCode(statusCode);
        return oleLoaderResponseBo;
    }

    @Override
    public OLELoaderResponseBo generateResponse(String message, int statusCode,String details) {
        OLELoaderResponseBo oleLoaderResponseBo=new OLELoaderResponseBo();
        oleLoaderResponseBo.setMessage(message);
        oleLoaderResponseBo.setStatusCode(statusCode);
        oleLoaderResponseBo.setDetails(details);
        return oleLoaderResponseBo;
    }

    public Response returnLoaderAPIResponse(String context, Object object){
        String response ="";
        JSONObject jsonObject = new JSONObject();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject1 = new JSONObject(mapper.writeValueAsString(object));
            jsonObject.put("@context",context);
            Iterator<String> keys = jsonObject1.keys();
            while(keys.hasNext()){
                String key = keys.next();
                String val = jsonObject1.getString(key);
                jsonObject.put(key, val);
            }
            LOG.info(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(object instanceof  OLELoaderResponseBo){
            OLELoaderResponseBo oleLoaderResponseBo = (OLELoaderResponseBo) object;
            return  Response.status(oleLoaderResponseBo.getStatusCode()).entity(jsonObject.toString()).build();
        }else{
            return  Response.status(200).entity(jsonObject.toString()).build();
        }
    }


    public JSONObject getJsonObjectFromString(String body){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray getJsonArrayFromString(String body){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key){
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  returnValue;
    }

    @Override
    public Object getLoaderService(String serviceName) {
        if(serviceName.equals("location")){
            if(oleLocationLoaderService == null){
                oleLocationLoaderService = new OLELocationLoaderServiceImpl();
            }
            return oleLocationLoaderService;
        }else if(serviceName.equals("shelvingScheme")){
            if(oleShelvingSchemeLoaderService == null){
                oleShelvingSchemeLoaderService = new OLEShelvingSchemeLoaderServiceImpl();
            }
            return oleShelvingSchemeLoaderService;
        }else if(serviceName.equals("borrowerType")){
            if(oleBorrowerTypeLoaderService == null){
                oleBorrowerTypeLoaderService = new OLEBorrowerTypeLoaderServiceImpl();
            }
            return oleBorrowerTypeLoaderService;
        }else if(serviceName.equals("itemType")){
            if(oleItemTypeLoaderService == null){
                oleItemTypeLoaderService = new OLEItemTypeLoaderServiceImpl();
            }
            return oleItemTypeLoaderService;
        }else if(serviceName.equals("statisticalSearchingCode")){
            if(oleStatisticalSearchingCodeLoaderService == null){
                oleStatisticalSearchingCodeLoaderService = new OLEStatisticalSearchingCodeLoaderServiceImpl();
            }
            return oleStatisticalSearchingCodeLoaderService;
        }else if(serviceName.equals("itemAvailableStatus")){
            if(oleItemAvailableStatusLoaderService == null){
                oleItemAvailableStatusLoaderService = new OLEItemAvailableStatusLoaderServiceImpl();
            }
            return oleItemAvailableStatusLoaderService;
        }else if(serviceName.equals("bibliographicRecordStatus")){
            if(oleBibliographicRecordStatusLoaderService == null){
                oleBibliographicRecordStatusLoaderService = new OLEBibliographicRecordStatusLoaderServiceImpl();
            }
            return oleBibliographicRecordStatusLoaderService;
        }
        return null;
    }

    @Override
    public Object getLoaderHelperService(String serviceName) {
        if(serviceName.equals("location")){
            if(oleLocationLoaderHelperService == null){
                oleLocationLoaderHelperService = new OLELocationLoaderHelperServiceImpl();
            }
            return oleLocationLoaderHelperService;
        }else if(serviceName.equals("shelvingScheme")){
            if(oleShelvingSchemeLoaderHelperService == null){
                oleShelvingSchemeLoaderHelperService = new OLEShelvingSchemeLoaderHelperServiceImpl();
            }
            return oleShelvingSchemeLoaderHelperService;
        } else if(serviceName.equals("borrowerType")){
            if(oleBorrowerTypeLoaderHelperService == null){
                oleBorrowerTypeLoaderHelperService = new OLEBorrowerTypeLoaderHelperServiceImpl();
            }
            return oleBorrowerTypeLoaderHelperService;
        }else if(serviceName.equals("itemType")){
            if(oleItemTypeLoaderHelperService == null){
                oleItemTypeLoaderHelperService = new OLEItemTypeLoaderHelperServiceImpl();
            }
            return oleItemTypeLoaderHelperService;
        }else if(serviceName.equals("statisticalSearchingCode")){
            if(oleStatisticalSearchingCodeLoaderHelperService == null){
                oleStatisticalSearchingCodeLoaderHelperService = new OLEStatisticalSearchingCodeLoaderHelperServiceImpl();
            }
            return oleStatisticalSearchingCodeLoaderHelperService;
        }else if(serviceName.equals("itemAvailableStatus")){
            if(oleItemAvailableStatusLoaderHelperService == null){
                oleItemAvailableStatusLoaderHelperService = new OLEItemAvailableStatusLoaderHelperServiceImpl();
            }
            return oleItemAvailableStatusLoaderHelperService;
        }else if(serviceName.equals("bibliographicRecordStatus")){
            if(oleBibliographicRecordStatusLoaderHelperService == null){
                oleBibliographicRecordStatusLoaderHelperService = new OLEBibliographicRecordStatusLoaderHelperServiceImpl();
            }
            return oleBibliographicRecordStatusLoaderHelperService;
        }
        return null;
    }

    @Override
    public Response formResponseForImport(Object object, String context) {
        if(object instanceof OLELoaderImportResponseBo){
            if(CollectionUtils.isNotEmpty(((OLELoaderImportResponseBo) object).getOleRejectedBos())){
                String rejectedBosIndexs = "";
                int count = 1;
                for(Integer index : ((OLELoaderImportResponseBo) object).getOleRejectedBos()){
                    if(count == 1){
                        rejectedBosIndexs = String.valueOf(index);
                        count++;
                    }else{
                        rejectedBosIndexs += ","+String.valueOf(index);
                    }
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("@context", context);
                    jsonObject.put("items",((OLELoaderImportResponseBo) object).getOleCreatedBos());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(CollectionUtils.isNotEmpty(((OLELoaderImportResponseBo) object).getOleRejectedBos()) && CollectionUtils.isEmpty(((OLELoaderImportResponseBo) object).getOleCreatedBos())){
                    return Response.status(400).entity("").build();
                }else{
                    return Response.status(200).entity(jsonObject).header("X-OLE-Rejected", rejectedBosIndexs).build();
                }
            }else{
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("@context",context);
                    jsonObject.put("items",((OLELoaderImportResponseBo) object).getOleCreatedBos());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Response.status(201).entity(jsonObject).build();
            }
        }
        return null;
    }
}
