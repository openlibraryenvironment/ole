package org.kuali.ole.loaders.common.service.impl;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.service.OLELoaderService;

import javax.ws.rs.core.Response;
import java.util.Iterator;

/**
 * Created by sheiksalahudeenm on 2/16/15.
 */
public class OLELoaderServiceImpl implements OLELoaderService {

    private static final Logger LOG = Logger.getLogger(OLELoaderServiceImpl.class);

    @Override
    public OLELoaderResponseBo generateResponse(String code, String message, int statusCode) {
        OLELoaderResponseBo oleLoaderResponseBo=new OLELoaderResponseBo();
        oleLoaderResponseBo.setCode(code);
        oleLoaderResponseBo.setMessage(message);
        oleLoaderResponseBo.setStatusCode(statusCode);
        return oleLoaderResponseBo;
    }

    @Override
    public OLELoaderResponseBo generateResponse(String code, String message, int statusCode,String details) {
        OLELoaderResponseBo oleLoaderResponseBo=new OLELoaderResponseBo();
        oleLoaderResponseBo.setCode(code);
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
}
