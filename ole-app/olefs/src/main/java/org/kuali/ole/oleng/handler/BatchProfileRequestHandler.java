package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.oleng.service.BatchProfileService;
import org.kuali.ole.spring.batch.BatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by rajeshbabuk on 12/10/15.
 */
@Service("batchProfileRequestHandler")
public class BatchProfileRequestHandler extends BatchProfileRequestHandlerUtil {


    public BatchProcessProfile convertJsonToProfile(String profileJsonString) throws JSONException, IOException {
        getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProcessProfile batchProcessProfile = getObjectMapper().readValue(profileJsonString, BatchProcessProfile.class);
        return batchProcessProfile;
    }

    public String prepareProfileForSearch(String requestContent) throws JSONException, IOException {
        JSONObject requestObject = new JSONObject(requestContent);
        String profileName = null;
        if(requestObject.has("profileName")){
           profileName =  getStringValueFromJsonObject(requestObject, "profileName");
        }
        JSONArray jsonArray = new JSONArray();
        List<BatchProcessProfile> matching = getBatchProcessProfiles(profileName);
        if(CollectionUtils.isNotEmpty(matching)){
            for (Iterator<BatchProcessProfile> iterator = matching.iterator(); iterator.hasNext(); ) {
                BatchProcessProfile batchProcessProfile = iterator.next();
                JSONObject profile = new JSONObject();
                profile.put("profileName",batchProcessProfile.getBatchProcessProfileName());
                profile.put("profileId",batchProcessProfile.getBatchProcessProfileId());
                profile.put("content", IOUtils.toString(batchProcessProfile.getContent()));
                jsonArray.put(profile);
            }
        }
        return jsonArray.toString();
    }

    public String prepareProfileForEdit(String requestContent) throws JSONException, IOException {
        JSONObject requestObject = new JSONObject(requestContent);
        String profileId = null;
        String action = null;
        if(requestObject.has("profileId")){
           profileId =  getStringValueFromJsonObject(requestObject, "profileId");
        }
        if(requestObject.has("action")){
           action =  getStringValueFromJsonObject(requestObject, "action");
        }
        BatchProcessProfile matching = getBatchProcessProfileById(Long.parseLong(profileId));
        if(null != matching){
            JSONObject jsonObject = new JSONObject(IOUtils.toString(matching.getContent()));
            if(StringUtils.equals(action,"copy")){
                jsonObject.put("profileId",JSONObject.NULL);
                jsonObject.put("profileName",JSONObject.NULL);
                jsonObject.put("description",JSONObject.NULL);
            }else if(StringUtils.equals(action,"edit")){
                jsonObject.put("profileId",profileId);
            }
            return jsonObject.toString();
        }
        return null;
    }
}
