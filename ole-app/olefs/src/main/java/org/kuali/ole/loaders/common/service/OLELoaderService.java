package org.kuali.ole.loaders.common.service;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import javax.ws.rs.core.Response;

/**
 * Created by sheiksalahudeenm on 2/16/15.
 */
public interface OLELoaderService {

    public OLELoaderResponseBo generateResponse(String message, int statusCode);

    public OLELoaderResponseBo generateResponse(String message, int statusCode,String details);

    public Response returnLoaderAPIResponse(String context, Object object);

    public JSONObject getJsonObjectFromString(String body);

    public JSONArray getJsonArrayFromString(String body);

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key);

    public Object getLoaderService(String serviceName);

    public Object getLoaderHelperService(String serviceName);

    public Response formResponseForImport(Object object, String context);

}
