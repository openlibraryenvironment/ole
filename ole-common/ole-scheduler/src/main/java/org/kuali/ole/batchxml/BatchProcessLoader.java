package org.kuali.ole.batchxml;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.utility.OleHttpRest;
import org.kuali.ole.utility.OleHttpRestImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BatchProcessLoader {
    private static final Logger LOG = Logger.getLogger(BatchProcessLoader.class);
    private OleHttpRest oleHttpRest = new OleHttpRestImpl();
    /** Number of minutes to try to get the list of processes from the REST API. */
    final int MINUTES = 2;
    /** maps profile name to profile id */
    private Map<String,Long> profileIds = new HashMap<>();
    /** maps job name to job id */
    private Map<String,Long> jobIds = new HashMap<>();

    public void setOleHttpRest(OleHttpRest oleHttpRest) {
        this.oleHttpRest = oleHttpRest;
    }

    public void load() throws IOException, JSONException, InterruptedException {
        String[] springConfig = {"org/kuali/ole/scheduler/jobs/*.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

        loadProfileIds();
        loadJobIds();
        loadProfiles(context);
        loadJobs(context);
    }

    /**
     * Fill profileIds with the data from the REST JSON profile search API.
     * @throws IOException      when REST call fails
     * @throws JSONException    when REST return invalid JSON
     * @throws InterruptedException when waiting is interrupted
     * @throws RuntimeException     if REST API is not available for 20 Minutes
     */
    private void loadProfileIds() throws IOException, JSONException, InterruptedException {
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
    private void loadProfileIds(String profilesJson) throws JSONException {
        profileIds.clear();
        // The list of profiles, can be an empty list.
        JSONArray profiles = new JSONArray(profilesJson);
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            try {
                Long id     = profile.getLong(  OleNGConstants.PROFILE_ID);
                String name = profile.optString(OleNGConstants.PROFILE_NAME);
                profileIds.put(name, id);
            } catch (JSONException e) {
                // ignore bad profile
                error("failing profile: " + profile.toString());
                error(e);
            }
        }
        info("BatchProcessProfile Ids loaded: " + profiles.length());
    }

    private void loadJobIds() throws JSONException, IOException {
        String jobsJson = oleHttpRest.get(OleNGConstants.REST_BATCH_PROCESS_JOBS);
        jobIds.clear();
        // The list of profiles, can be an empty list.
        JSONArray jobs = new JSONArray(jobsJson);
        for (int i = 0; i < jobs.length(); i++) {
            JSONObject job = jobs.getJSONObject(i);
            try {
                Long id     = job.getLong(  OleNGConstants.JOB_ID);
                String name = job.optString(OleNGConstants.JOB_NAME);
                jobIds.put(name, id);
            } catch (JSONException e) {
                // ignore bad job
                error("failing BatchProcessJob: " + job.toString());
                error(e);
            }
        }
        info("BatchProcessJob Ids loaded: " + jobs.length());
    }

    private void loadProfiles(ApplicationContext context) throws IOException, JSONException {
        Map<String, BatchProcessProfile> profiles = context.getBeansOfType(BatchProcessProfile.class);
        for (BatchProcessProfile profile : profiles.values()) {
            Long profileId = profileIds.get(profile.getBatchProcessProfileName());
            if (profileId != null) {
                profile.setBatchProcessProfileId(profileId);
            }

            JSONObject json = new JSONObject();
            json.put(OleNGConstants.PROFILE_NAME,profile.getBatchProcessProfileName());
            json.put(OleNGConstants.PROFILE_ID,  profile.getBatchProcessProfileId());
            json.put(OleNGConstants.PROCESS_TYPE,profile.getBatchProcessType());
            byte[] content = profile.getContent();
            json.put(OleNGConstants.CONTENT, (null != content ? new String(content) : "{}"));
            oleHttpRest.json(OleNGConstants.REST_BATCH_PROFILE_SUBMIT, json.toString());
        }

        info("BatchProcessProfiles loaded: " + profiles.size());
    }

    /**
     * Return the profile id for the profileName.
     * @param profileName   name of the BatchProcessProfile
     * @return  id
     * @throws IllegalArgumentException     when profileName is unknown
     */
    private long getProfileId(String profileName)
            throws IllegalArgumentException, JSONException, IOException {
        JSONObject json = new JSONObject();
        json.put(OleNGConstants.PROFILE_NAME, profileName);
        String profilesJson = oleHttpRest.json(OleNGConstants.REST_BATCH_PROFILE_SEARCH, json.toString());
        JSONArray profiles = new JSONArray(profilesJson);
        if (profiles.length() == 0) {
            throw new IllegalArgumentException("BatchProcessProfile name is unknown: " + profileName);
        }
        JSONObject profile = profiles.getJSONObject(0);
        return profile.getLong(OleNGConstants.PROFILE_ID);
    }

    private void loadJobs(ApplicationContext context) throws IOException, JSONException {
        Map<String, BatchProcessJob> jobs = context.getBeansOfType(BatchProcessJob.class);
        for (BatchProcessJob job : jobs.values()) {
            Long jobId = jobIds.get(job.getJobName());
            if (jobId != null) {
                job.setJobId(jobId);
                oleHttpRest.get(OleNGConstants.REST_BATCH_JOB_DESTROY + "?" +
                        OleNGConstants.JOB_ID + "=" + jobId);
            }

            if (StringUtils.isEmpty(job.getBatchProfileName())) {
                throw new IllegalArgumentException("Job doesn't have a batchProfileName: " + job.getJobName());
            }
            job.setBatchProfileId(getProfileId(job.getBatchProfileName()));

            JSONObject json = new JSONObject();
            json.put(OleNGConstants.JOB_ID,  job.getJobId());
            json.put(OleNGConstants.JOB_NAME,job.getJobName());
            json.put(OleNGConstants.CRON_EXPRESSION, job.getCronExpression());
            json.put(OleNGConstants.PROFILE_ID,   job.getBatchProfileId());
            json.put(OleNGConstants.PROFILE_NAME, job.getBatchProfileName());
            json.put(OleNGConstants.PROFILE_TYPE, job.getProfileType());
            json.put(OleNGConstants.JOB_TYPE,     job.getJobType());
            oleHttpRest.json(OleNGConstants.REST_BATCH_JOB_CREATE, json.toString());
        }

        info("BatchProcessJobs loaded: " + jobs.size());
    }

    /**
     * Log the error message. Use Logger if available, otherwise use System.err.
     * During server startup and deployment Logger may not have been initializes.
     * @param msg   Message to log
     */
    private static void error(String msg) {
        if (LOG == null) {
            System.err.println("ERROR: " + BatchProcessLoader.class.getSimpleName() + " " + msg);
            return;
        }
        LOG.error(msg);
    }

    /**
     * Log the exception. Use Logger if available, otherwise use System.err.
     * During server startup and deployment Logger may not have been initializes.
     * @param e   Exception to log
     */
    private static void error(Exception e) {
        if (LOG == null) {
            e.printStackTrace();
            return;
        }
        LOG.error(e);
    }

    /**
     * Log the info message. Use Logger if available, otherwise use System.err.
     * During server startup and deployment Logger may not have been initializes.
     * @param msg   Message to log
     */
    private static void info(String msg) {
        if (LOG == null) {
            System.err.println("INFO: "  + BatchProcessLoader.class.getSimpleName() + " " + msg);
            return;
        }
        LOG.info(msg);
    }
}
