package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        Map map = new HashMap();
        if(requestObject.has(OleNGConstants.PROFILE_NAME)){
            String profileName =  getStringValueFromJsonObject(requestObject, OleNGConstants.PROFILE_NAME);
            if (StringUtils.isNotBlank(profileName)) {
                map.put(OleNGConstants.BATCH_PROCESS_PROFILE_NAME,profileName);
            }
        }
        if(requestObject.has(OleNGConstants.PROFILE_TYPE)){
            String profileType =  getStringValueFromJsonObject(requestObject, OleNGConstants.PROFILE_TYPE);
            if (StringUtils.isNotBlank(profileType)) {
                map.put(OleNGConstants.BATCH_PROCESS_PROFILE_TYPE,profileType);
            }
        }
        JSONArray jsonArray = new JSONArray();
        List<BatchProcessProfile> matching = getBatchProcessProfiles(map);
        if(CollectionUtils.isNotEmpty(matching)){
            for (Iterator<BatchProcessProfile> iterator = matching.iterator(); iterator.hasNext(); ) {
                BatchProcessProfile batchProcessProfile = iterator.next();
                JSONObject profile = new JSONObject();
                profile.put(OleNGConstants.PROFILE_NAME,batchProcessProfile.getBatchProcessProfileName());
                profile.put(OleNGConstants.PROFILE_ID,batchProcessProfile.getBatchProcessProfileId());
                profile.put(OleNGConstants.PROFILE_TYPE,batchProcessProfile.getBatchProcessType());
                byte[] content = batchProcessProfile.getContent();
                profile.put(OleNGConstants.CONTENT, (null != content ? IOUtils.toString(content) : "{}"));
                jsonArray.put(profile);
            }
        }
        return jsonArray.toString();
    }

    public String prepareProfileForEdit(String requestContent) throws JSONException, IOException {
        JSONObject requestObject = new JSONObject(requestContent);
        String profileId = null;
        String action = null;
        if(requestObject.has(OleNGConstants.PROFILE_ID)){
           profileId =  getStringValueFromJsonObject(requestObject, OleNGConstants.PROFILE_ID);
        }
        if(requestObject.has(OleNGConstants.ACTION)){
           action =  getStringValueFromJsonObject(requestObject, OleNGConstants.ACTION);
        }
        BatchProcessProfile matching = getBatchProcessProfileById(Long.parseLong(profileId));
        if(null != matching){
            byte[] content = matching.getContent();
            JSONObject jsonObject = new JSONObject((null != content ? IOUtils.toString(content) : "{}"));
            if(StringUtils.equals(action,OleNGConstants.COPY)){
                jsonObject.put(OleNGConstants.PROFILE_ID,JSONObject.NULL);
                jsonObject.put(OleNGConstants.PROFILE_NAME,JSONObject.NULL);
                jsonObject.put(OleNGConstants.DESCRIPTION,JSONObject.NULL);
            }else if(StringUtils.equals(action,OleNGConstants.EDIT)){
                jsonObject.put(OleNGConstants.PROFILE_ID,profileId);
            }
            return jsonObject.toString();
        }
        return null;
    }

    public String deleteProfile(String requestContent) throws JSONException, IOException {
        JSONObject requestObject = new JSONObject(requestContent);
        String profileId = null;
        String action = null;
        if(requestObject.has(OleNGConstants.PROFILE_ID)){
            profileId =  getStringValueFromJsonObject(requestObject, OleNGConstants.PROFILE_ID);
        }
        getBatchProfileService().deleteProfileById(Long.parseLong(profileId));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(OleNGConstants.STATUS,"Successfully Deleted Profile");
        return jsonObject.toString();
    }

}
