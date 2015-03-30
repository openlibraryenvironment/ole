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
import org.kuali.ole.loaders.describe.service.OLELocationLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLELocationLoaderService;
import org.kuali.ole.loaders.describe.service.OLEShelvingSchemeLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLEShelvingSchemeLoaderService;
import org.kuali.ole.loaders.describe.service.impl.OLELocationLoaderHelperServiceImpl;
import org.kuali.ole.loaders.describe.service.impl.OLELocationLoaderServiceImpl;
import org.kuali.ole.loaders.describe.service.impl.OLEShelvingSchemeLoaderHelperServiceImpl;
import org.kuali.ole.loaders.describe.service.impl.OLEShelvingSchemeLoaderServiceImpl;

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
        }else if(serviceName.equals("schelvingScheme")){
            if(oleShelvingSchemeLoaderService == null){
                oleShelvingSchemeLoaderService = new OLEShelvingSchemeLoaderServiceImpl();
            }
            return oleShelvingSchemeLoaderService;
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
        }else if(serviceName.equals("schelvingScheme")){
            if(oleShelvingSchemeLoaderHelperService == null){
                oleShelvingSchemeLoaderHelperService = new OLEShelvingSchemeLoaderHelperServiceImpl();
            }
            return oleShelvingSchemeLoaderHelperService;
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
