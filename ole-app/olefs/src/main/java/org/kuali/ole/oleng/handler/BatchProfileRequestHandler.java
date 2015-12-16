package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.spring.batch.BatchUtil;

import java.io.IOException;
import java.util.*;

/**
 * Created by rajeshbabuk on 12/10/15.
 */
public class BatchProfileRequestHandler extends BatchUtil {

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
                profile.put("content", IOUtils.toString(batchProcessProfile.getContent()));
                jsonArray.put(profile);
            }
        }
        return jsonArray.toString();
    }

    private List<BatchProcessProfile> getBatchProcessProfiles(String profileName) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(profileName)){
            Map map = new HashedMap();
            map.put("batchProcessProfileName",profileName);
            return (List<BatchProcessProfile>) getBusinessObjectService().findMatching(BatchProcessProfile.class, map);
        } else {
            return (List<BatchProcessProfile>) getBusinessObjectService().findAll(BatchProcessProfile.class);
        }
    }
}
