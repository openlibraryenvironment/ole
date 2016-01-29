package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.profile.model.*;
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
        if(requestObject.has(OleNGConstants.PROFILE_NAME)){
           profileName =  getStringValueFromJsonObject(requestObject, OleNGConstants.PROFILE_NAME);
        }
        JSONArray jsonArray = new JSONArray();
        List<BatchProcessProfile> matching = getBatchProcessProfiles(profileName);
        if(CollectionUtils.isNotEmpty(matching)){
            for (Iterator<BatchProcessProfile> iterator = matching.iterator(); iterator.hasNext(); ) {
                BatchProcessProfile batchProcessProfile = iterator.next();
                JSONObject profile = new JSONObject();
                profile.put(OleNGConstants.PROFILE_NAME,batchProcessProfile.getBatchProcessProfileName());
                profile.put(OleNGConstants.PROFILE_ID,batchProcessProfile.getBatchProcessProfileId());
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
