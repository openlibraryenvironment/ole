package org.kuali.ole.batchxml;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.batchxml.BatchProcessProfile;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.utility.OleHttpRest;
import org.kuali.ole.utility.OleHttpRestGet;
import org.kuali.ole.utility.OleHttpRestGetImpl;
import org.kuali.ole.utility.OleHttpRestImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BatchProcessProfileLoader {
    private static final Logger LOG = Logger.getLogger(BatchProcessProfileLoader.class);
    private OleHttpRest oleHttpRest = new OleHttpRestImpl();
    /** Number of minutes to try to get the list of processes from the REST API. */
    final int MINUTES = 2;
    /** maps profile name to profile id */
    private Map<String,Integer> profileIds = new HashMap<>();

    public void setOleHttpRest(OleHttpRest oleHttpRest) {
        this.oleHttpRest = oleHttpRest;
    }

    public void load() throws IOException, JSONException, InterruptedException {
        info("load()");

        loadProfileIds();
        loadProfiles();
    }

    /**
     * Fill profileIds with the data from the REST JSON profile search API.
     * @throws IOException      when REST call fails
     * @throws JSONException    when REST return invalid JSON
     * @throws InterruptedException when waiting is interrupted
     * @throws RuntimeException     if REST API is not available for 20 Minutes
     */
    void loadProfileIds() throws IOException, JSONException, InterruptedException {
        // At the start of Tomcat it may some time until the REST api is available.
        // During that time we get a not found status.
        for (int n = 0; n < 60*MINUTES; n++) {
            try {
                String profiles = oleHttpRest.json(OleNGConstants.REST_BATCH_PROFILE_SEARCH, "{}");
                loadProfileIds(profiles);
                return;
            } catch (HttpResponseException e) {
                if (e.getStatusCode() != HttpStatus.SC_NOT_FOUND /* 404 */) {
                    throw e;
                }
            }

            if (n == 1) {
                error("rest not yet available, not fully deployed?");
            }
            TimeUnit.SECONDS.sleep(1);
        }

        throw new RuntimeException("REST api not available: " + OleNGConstants.REST_BATCH_PROFILE_SEARCH);
    }

    /**
     * Fill profileIds with the data from the JSON String.
     * @param profilesJson     JSON with array of profiles, each with profile id and profile name
     */
    void loadProfileIds(String profilesJson) throws JSONException {
        profileIds.clear();
        // The list of profiles, can be an empty list.
        JSONArray profiles = new JSONArray(profilesJson);
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            try {
                Integer id  = profile.getInt(   OleNGConstants.PROFILE_ID);
                String name = profile.optString(OleNGConstants.PROFILE_NAME);
                profileIds.put(name, id);
                info(name + " - " + id);
            } catch (JSONException e) {
                // ignore bad profile
                error("failing profile: " + profile.toString());
                error(e);
            }
        }
    }

    void loadProfiles() throws IOException, JSONException {
        String[] springConfig = {"org/kuali/ole/scheduler/jobs/*.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
        Map<String, BatchProcessProfile> profiles = context.getBeansOfType(BatchProcessProfile.class);
        for (BatchProcessProfile profile : profiles.values()) {
            Integer profileId = profileIds.get(profile.getBatchProcessProfileName());
            if (profileId != null) {
                profile.setBatchProcessProfileId(profileId);
            }

            JSONObject json = new JSONObject();
            json.put(OleNGConstants.PROFILE_NAME,profile.getBatchProcessProfileName());
            json.put(OleNGConstants.PROFILE_ID,  profile.getBatchProcessProfileId());
            json.put(OleNGConstants.PROFILE_TYPE,profile.getBatchProcessType());
            byte[] content = profile.getContent();
            json.put(OleNGConstants.CONTENT, (null != content ? new String(content) : "{}"));
info(json.toString());
            String result = oleHttpRest.json(OleNGConstants.REST_BATCH_PROFILE_SUBMIT, json.toString());
            LOG.info("Submit result: " + result);
        }
    }

    /**
     * Log the error message. Use Logger if available, otherwise use System.err.
     * On server startup Logger may not have been initializes.
     * @param msg   Message to log
     */
    private static void error(String msg) {
        if (LOG == null) {
            System.err.println("ERROR: " + BatchProcessProfileLoader.class.getSimpleName() + " " + msg);
            return;
        }
        LOG.error(msg);
    }

    private static void error(Exception e) {
        if (LOG == null) {
            e.printStackTrace();
            return;
        }
        LOG.error(e);
    }

    /**
     * Log the info message. Use Logger if available, otherwise use System.err.
     * On server startup Logger may not have been initializes.
     * @param msg   Message to log
     */
    private static void info(String msg) {
        if (LOG == null) {
            System.err.println("INFO: "  + BatchProcessProfileLoader.class.getSimpleName() + " " + msg);
            return;
        }
        LOG.info(msg);
    }
}
