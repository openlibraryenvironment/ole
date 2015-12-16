package org.kuali.ole.oleng.handler;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.spring.batch.BatchUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeshbabuk on 12/10/15.
 */
public class BatchProfileRequestHandler extends BatchUtil {

    public BatchProcessProfile convertJsonToProfile(String profileJsonString) throws JSONException, IOException {
        getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProcessProfile batchProcessProfile = getObjectMapper().readValue(profileJsonString, BatchProcessProfile.class);
        return batchProcessProfile;
    }
}
