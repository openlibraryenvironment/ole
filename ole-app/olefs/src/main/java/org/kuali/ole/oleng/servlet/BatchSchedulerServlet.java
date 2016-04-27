package org.kuali.ole.oleng.servlet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.utility.OleHttpRestGet;
import org.kuali.ole.utility.OleHttpRestGetImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 4/19/2016.
 */
public class BatchSchedulerServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(BatchSchedulerServlet.class);

    @Autowired
    private DescribeDAO describeDAO;


    @Override
    public void init() throws ServletException {
        super.init();
        // invoke in separate thread to complete the deployment in parallel
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadDefaultProfileAndJobs();
                OleHttpRestGet oleHttpRestGet = new OleHttpRestGetImpl();
                String controllerurl = OleNGConstants.SCHEDULE_ALL_JOBS;
                try {
                    String response = oleHttpRestGet.rest(controllerurl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println();
            }
        }).start();
    }

    private void loadDefaultProfileAndJobs() {
        String[] springConfig = {"org/kuali/ole/oleng/scheduler/jobs/*.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

        Map<String, Long> existingProfiles = loadDefaultProfile(context);

        loadDefaultJobs(context, existingProfiles);

    }

    private Map<String, Long> loadDefaultProfile(ApplicationContext context) {

        Map<String, BatchProcessProfile> profiles = context.getBeansOfType(BatchProcessProfile.class);

        Map<String, Long> existingProfiles = fetchAllProfiles();

        Map<String, Long> newProfiles = new HashMap<>();

        for (Iterator<BatchProcessProfile> iterator = profiles.values().iterator(); iterator.hasNext(); ) {
            BatchProcessProfile processProfile = iterator.next();
            String batchProcessProfileName = processProfile.getBatchProcessProfileName();
            if(!existingProfiles.containsKey(batchProcessProfileName)) {
                BatchProcessProfile batchProcessProfile = profiles.get(batchProcessProfileName);
                KRADServiceLocator.getBusinessObjectService().save(batchProcessProfile);
                newProfiles.put(batchProcessProfileName, batchProcessProfile.getBatchProcessProfileId());
            }
        }

        LOG.info("New Batch profile loaded: " + newProfiles.size());
        existingProfiles.putAll(newProfiles);

        return existingProfiles;
    }

    private void loadDefaultJobs(ApplicationContext context, Map<String, Long> existingProfiles) {
        Map<String, BatchProcessJob> jobs = context.getBeansOfType(BatchProcessJob.class);
        Map<String, BatchProcessJob> existingJobs = getExistingJobs();
        int newJobCount = 0;
        for (BatchProcessJob job : jobs.values()) {
            if(!existingJobs.containsKey(job.getJobName())) {
                String profileName = job.getBatchProfileName();
                if(StringUtils.isNotBlank(profileName) && existingProfiles.get(profileName) != null) {
                    Long profileId = existingProfiles.get(profileName);
                    job.setBatchProfileId(profileId);
                    KRADServiceLocator.getBusinessObjectService().save(job);
                    newJobCount++;

                }
            }
        }
        LOG.info("New BatchProcessJobs loaded: " + newJobCount);
    }

    public Map<String, BatchProcessJob> getExistingJobs() {
        Map<String, BatchProcessJob> existingJobs = new HashMap<>();
        List<BatchProcessJob> batchProcessJobs = fetchAllBatchProcessJobs();
        if(CollectionUtils.isNotEmpty(batchProcessJobs)) {
            for (Iterator<BatchProcessJob> iterator = batchProcessJobs.iterator(); iterator.hasNext(); ) {
                BatchProcessJob batchProcessJob=  iterator.next();
                String jobName = batchProcessJob.getJobName();
                existingJobs.put(jobName, batchProcessJob);
            }
        }
        return existingJobs;

    }

    public List<BatchProcessJob> fetchAllBatchProcessJobs() {
        return (List<BatchProcessJob>) KRADServiceLocator.getBusinessObjectService().findAll(BatchProcessJob.class);
    }

    public Map<String, Long> fetchAllProfiles() {
        Map<String, Long> profileMap = new HashMap<>();
        List<BatchProcessProfile> batchProcessProfiles = (List<BatchProcessProfile>) KRADServiceLocator.getBusinessObjectService().findAll(BatchProcessProfile.class);
        if(CollectionUtils.isNotEmpty(batchProcessProfiles)) {
            for (Iterator<BatchProcessProfile> iterator = batchProcessProfiles.iterator(); iterator.hasNext(); ) {
                BatchProcessProfile batchProcessProfile = iterator.next();
                String batchProcessProfileName = batchProcessProfile.getBatchProcessProfileName();
                profileMap.put(batchProcessProfileName, batchProcessProfile.getBatchProcessProfileId());
            }
        }
        return profileMap;
    }
}
