package org.kuali.ole.spring.batch.rest.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.drools.core.time.impl.CronExpression;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.batch.process.model.BatchScheduleJob;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.rest.controller.OleNgControllerBase;
import org.kuali.ole.oleng.scheduler.OleNGBatchJobScheduler;
import org.kuali.ole.oleng.util.BatchExcelReportUtil;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.spring.batch.processor.*;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
@Controller
@RequestMapping("/batch")
public class BatchRestController extends OleNgControllerBase {

    @Autowired
    private BatchBibFileProcessor batchBibFileProcessor;

    @Autowired
    private BatchOrderImportProcessor batchOrderImportProcessor;

    @Autowired
    private BatchInvoiceImportProcessor batchInvoiceImportProcessor;

    @Autowired
    private BatchExportFileProcessor batchExportFileProcessor;

    @Autowired
    private BatchDeleteFileProcessor batchDeleteFileProcessor;
    
    @Autowired
    private OleNGBatchJobScheduler oleNGBatchJobScheduler;

    private BatchExcelReportUtil batchExcelReportUtil;

    private BatchUtil batchUtil;

    @Autowired
    private DescribeDAO describeDAO;

    public DescribeDAO getDescribeDAO() {
        return describeDAO;
    }

    public void setDescribeDAO(DescribeDAO describeDAO) {
        this.describeDAO = describeDAO;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String UploadFile(@RequestParam("file") MultipartFile file, @RequestParam("profileName") String profileName,
                             @RequestParam("batchType") String batchType, HttpServletRequest request) throws Exception {
        try {
            if (StringUtils.isNotBlank(profileName) && StringUtils.isNotBlank(batchType)) {
                File uploadedDirectory = storeUploadedFileToFileSystem(file, null);
                if (null != uploadedDirectory) {
                    String originalFilename = null;
                    String extension = null;
                    if (null != file) {
                        originalFilename = file.getOriginalFilename();
                        extension = FilenameUtils.getExtension(originalFilename);
                    }
                    BatchJobDetails batchJobDetails = new BatchJobDetails();
                    batchJobDetails.setProfileName(profileName);
                    batchJobDetails.setFileName(originalFilename);
                    batchJobDetails.setStartTime(new Timestamp(new Date().getTime()));
                    batchJobDetails.setStatus(OleNGConstants.RUNNING);                    
                    JSONObject response = processBatch(uploadedDirectory, batchType, profileName, extension, batchJobDetails);
                    return response.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    private JSONObject processBatch(File uploadDirectory, String batchType, String profileId, String fileExtension, BatchJobDetails batchJobDetails) throws Exception {
        BatchFileProcessor batchProcessor = getBatchProcessor(batchType);

        long jobDetailsId = batchJobDetails.getJobDetailId();
        String reportDirectory = (jobDetailsId != 0) ? String.valueOf(jobDetailsId) : OleNGConstants.QUICK_LAUNCH + OleNGConstants.DATE_FORMAT.format(new Date());

        String statusFilePath = getBatchUtil().writeBatchRunningStatusToFile(uploadDirectory.getAbsolutePath(), OleNGConstants.RUNNING, null);

        JSONObject response = batchProcessor.processBatch(uploadDirectory, fileExtension, profileId,reportDirectory, batchJobDetails);
        if (batchJobDetails.getJobId() != 0 && batchJobDetails.getJobDetailId() != 0) {
            getBusinessObjectService().save(batchJobDetails);
        }
        response.put("filePathName", getStatusFilePathName(statusFilePath));
        return response;
    }

    private String getStatusFilePathName(String statusFilePath) {
        String batchUploadLocation = getBatchUploadLocation();
        File file = new File(batchUploadLocation);
        return statusFilePath.replace(file.getAbsolutePath() + File.separator,  "");
    }

    @RequestMapping(value = "/submit/api", method = RequestMethod.POST, produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String submitApi(@RequestBody String requestBody) throws Exception {
        JSONObject requestJson = new JSONObject(requestBody);
        String rawContent = getStringValueFromJsonObject(requestJson, "marcContent");
        String batchType = getStringValueFromJsonObject(requestJson, "batchType");
        String profileId = getStringValueFromJsonObject(requestJson, "profileId");
        BatchJobDetails batchJobDetails = new BatchJobDetails();
        batchJobDetails.setProfileName(profileId);
        String originalFilename = "quicklanch.mrc";
        batchJobDetails.setFileName(originalFilename);
        File uploadedDirectory = storeUploadedFileToFileSystem(rawContent, originalFilename);
        if(null != uploadedDirectory) {
            String extension = FilenameUtils.getExtension(originalFilename);
            boolean jobRunning = isJobRunning(batchJobDetails.getJobId());
            if(!jobRunning) {
                JSONObject response = processBatch(uploadedDirectory, batchType, profileId, extension, batchJobDetails);
                return response.toString();
            }
        }
        return null;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/job/create", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String createBatchJobDetailsEntry(@RequestBody String requestBody) {
        String response = "";
        BatchProcessJob batchProcessJob;
        try {
            batchProcessJob = convertJsonToProcessJob(requestBody);
            String loginUser = GlobalVariables.getUserSession().getPrincipalName();
            batchProcessJob.setCreatedBy(loginUser);
            batchProcessJob.setCreatedOn(new Timestamp(new Date().getTime()));
            batchProcessJob.setJobType(OleNGConstants.ADHOC);
            getBusinessObjectService().save(batchProcessJob);
            response = getObjectMapper().writeValueAsString(batchProcessJob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/job/quickLaunch", produces = {MediaType.APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String quickLaunchJob(@RequestParam("jobId") String jobId, @RequestParam("numOfRecordsInFile") String numOfRecordsInFile, @RequestParam("extension") String extension, @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        BatchJobDetails batchJobDetails = null;
        try {
            BatchProcessJob matchedBatchJob = getBatchUtil().getBatchProcessJobById(Long.valueOf(jobId));
            matchedBatchJob.setNumOfRecordsInFile(Integer.parseInt(numOfRecordsInFile));
            matchedBatchJob.setOutputFileFormat(extension);
            String originalFilename = null;

            File uploadedDirectory = storeUploadedFileToFileSystem(file, matchedBatchJob.getJobId());
            if (null != file && null != uploadedDirectory) {
                originalFilename = file.getOriginalFilename();
                extension = FilenameUtils.getExtension(originalFilename);

            }
            boolean jobRunning = isJobRunning(matchedBatchJob.getJobId());
            if(!jobRunning) {
                batchJobDetails = getBatchUtil().createBatchJobDetailsEntry(matchedBatchJob, originalFilename);
                getBusinessObjectService().save(batchJobDetails);
                JSONObject response = processBatch(uploadedDirectory, batchJobDetails.getProfileType(),
                        String.valueOf(matchedBatchJob.getBatchProfileId()), extension, batchJobDetails);
            }

        } catch (Exception e) {
            e.printStackTrace();
            batchJobDetails.setStatus(OleNGConstants.FAILED);
        }
        if (null != batchJobDetails) {
            getBusinessObjectService().save(batchJobDetails);
        }
        return "";
    }

    private File storeUploadedFileToFileSystem(MultipartFile file, Long jobId) throws IOException {
        String batchUploadLocation = getBatchUploadLocation();
        if(StringUtils.isNotBlank(batchUploadLocation)) {
            if (null != jobId && jobId != 0 ) {
                batchUploadLocation = batchUploadLocation + File.separator + jobId + "_" + OleNGConstants.DATE_FORMAT.format(new Date()) + System.currentTimeMillis();
            } else {
                batchUploadLocation = batchUploadLocation + File.separator+ OleNGConstants.DATE_FORMAT_MILLISECONDS.format(new Date());
            }
            File uploadDirectory = new File(batchUploadLocation);
            if (!uploadDirectory.exists() || !uploadDirectory.isDirectory()) {
                uploadDirectory.mkdirs();
            }
            if (null != file) {
                File uploadFile = new File(uploadDirectory, file.getOriginalFilename());
                file.transferTo(uploadFile);
            }
            return uploadDirectory;
        }
        return null;
    }

    private String getBatchUploadLocation() {
        return ConfigContext.getCurrentContextConfig().getProperty("batchUploadLocation");
    }

    private File storeUploadedFileToFileSystem(String rawContent, String fileName) throws IOException {
        String batchUploadLocation = getBatchUploadLocation();
        if(StringUtils.isNotBlank(batchUploadLocation)) {
            batchUploadLocation = batchUploadLocation + File.separator + OleNGConstants.DATE_FORMAT.format(new Date());
            File uploadDirectory = new File(batchUploadLocation);
            if (!uploadDirectory.exists() || !uploadDirectory.isDirectory()) {
                uploadDirectory.mkdirs();
            }
            File uploadFile = new File(uploadDirectory, fileName);
            FileUtils.writeStringToFile(uploadFile, rawContent);
            return uploadDirectory;
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/job/schedule", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String scheduleJob(@RequestParam("jobId") String jobId, @RequestParam("numOfRecordsInFile") String numOfRecordsInFile,
                              @RequestParam("extension") String extension, @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam("scheduleJob") String scheduleJobString, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            BatchScheduleJob batchScheduleJob = convertJsonToScheduleJob(scheduleJobString);
            BatchProcessJob matchedBatchJob = getBatchUtil().getBatchProcessJobById(Long.valueOf(jobId));
            boolean jobRunning = isJobRunning((new Long(jobId)).longValue());
            if (!jobRunning) {
                if (matchedBatchJob != null) {
                    matchedBatchJob.setNumOfRecordsInFile(Integer.parseInt(numOfRecordsInFile));
                    matchedBatchJob.setOutputFileFormat(extension);
                    String cronExpression = oleNGBatchJobScheduler.getCronExpression(batchScheduleJob);
                    if (StringUtils.isNotBlank(cronExpression) && CronExpression.isValidExpression(cronExpression)) {
                        if (null != file) {
                            saveUploadedFile(matchedBatchJob, file);
                        }
                        matchedBatchJob.setCronExpression(cronExpression);
                        CronExpression cron = new CronExpression(cronExpression);
                        Date date = cron.getNextValidTimeAfter(new Date());
                        matchedBatchJob.setNextRunTime(null != date ? new Timestamp(date.getTime()) : null);
                        matchedBatchJob.setJobType(OleNGConstants.SCHEDULED);
                        getBusinessObjectService().save(matchedBatchJob);
                        String principalName = "";
                        UserSession userSession = GlobalVariables.getUserSession();
                        if(userSession != null) {
                            principalName = userSession.getPrincipalName();
                        }
                        oleNGBatchJobScheduler.scheduleOrRescheduleJob(Long.valueOf(jobId), matchedBatchJob.getBatchProfileId(), matchedBatchJob.getProfileType(), matchedBatchJob.getCronExpression(), principalName);
                        jsonObject.put(OleNGConstants.JOB_ID, matchedBatchJob.getJobId());
                        jsonObject.put(OleNGConstants.JOB_TYPE, matchedBatchJob.getJobType());
                        jsonObject.put(OleNGConstants.CRON_EXPRESSION, matchedBatchJob.getCronExpression());
                        jsonObject.put(OleNGConstants.NEXT_RUN_TIME, matchedBatchJob.getNextRunTime());
                    }
                }
            }
        }catch(Exception e){
                e.printStackTrace();
            }
            return jsonObject.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job/pauseJob", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String pauseJob(@RequestParam("jobId") long jobId) {
        JSONObject jsonObject = new JSONObject();
        try {
            BatchProcessJob matchedBatchJob = getBatchUtil().getBatchProcessJobById(jobId);
            if (null != matchedBatchJob) {
                oleNGBatchJobScheduler.pauseJob(String.valueOf(jobId));
                matchedBatchJob.setJobType(OleNGConstants.PAUSED);
                getBusinessObjectService().save(matchedBatchJob);
                jsonObject.put(OleNGConstants.JOB_ID, matchedBatchJob.getJobId());
                jsonObject.put(OleNGConstants.JOB_TYPE, matchedBatchJob.getJobType());
                jsonObject.put(OleNGConstants.CRON_EXPRESSION, matchedBatchJob.getCronExpression());
                jsonObject.put(OleNGConstants.NEXT_RUN_TIME, matchedBatchJob.getNextRunTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job/resumeJob", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String resumeJob(@RequestParam("jobId") long jobId) {
        JSONObject jsonObject = new JSONObject();
        try {
            BatchProcessJob matchedBatchJob = getBatchUtil().getBatchProcessJobById(jobId);
            if (null != matchedBatchJob) {
                CronExpression cron = new CronExpression(matchedBatchJob.getCronExpression());
                Date date = cron.getNextValidTimeAfter(new Date());
                if (date != null) {
                    matchedBatchJob.setNextRunTime(new Timestamp(date.getTime()));
                    oleNGBatchJobScheduler.resumeJob(String.valueOf(jobId));
                    matchedBatchJob.setJobType(OleNGConstants.SCHEDULED);
                } else {
                    matchedBatchJob.setNextRunTime(null);
                    matchedBatchJob.setCronExpression(null);
                    matchedBatchJob.setJobType(OleNGConstants.ADHOC);
                }
                getBusinessObjectService().save(matchedBatchJob);
                jsonObject.put(OleNGConstants.JOB_ID, matchedBatchJob.getJobId());
                jsonObject.put(OleNGConstants.JOB_TYPE, matchedBatchJob.getJobType());
                jsonObject.put(OleNGConstants.CRON_EXPRESSION, matchedBatchJob.getCronExpression());
                jsonObject.put(OleNGConstants.NEXT_RUN_TIME, matchedBatchJob.getNextRunTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job/unShedule", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String unShedule(@RequestParam("jobId") long jobId) {
        JSONObject jsonObject = new JSONObject();
        try {
            BatchProcessJob matchedBatchJob = getBatchUtil().getBatchProcessJobById(jobId);
            if (null != matchedBatchJob) {
                oleNGBatchJobScheduler.unScheduleJob(String.valueOf(jobId), true);
                matchedBatchJob.setJobType(OleNGConstants.ADHOC);
                matchedBatchJob.setCronExpression(null);
                matchedBatchJob.setNextRunTime(null);
                getBusinessObjectService().save(matchedBatchJob);
                jsonObject.put(OleNGConstants.JOB_ID, matchedBatchJob.getJobId());
                jsonObject.put(OleNGConstants.JOB_TYPE, matchedBatchJob.getJobType());
                jsonObject.put(OleNGConstants.CRON_EXPRESSION, matchedBatchJob.getCronExpression());
                jsonObject.put(OleNGConstants.NEXT_RUN_TIME, matchedBatchJob.getNextRunTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/job/destroy", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String destroyJob(@RequestParam("jobId") long jobId) {
        try {
            BatchProcessJob matchedBatchJob = getBatchUtil().getBatchProcessJobById(jobId);
            if (null != matchedBatchJob) {
                getBusinessObjectService().delete(matchedBatchJob);
                oleNGBatchJobScheduler.unScheduleJob(String.valueOf(jobId), true);
                String schedulerUploadLocation = ConfigContext.getCurrentContextConfig().getProperty("schedulerUploadLocation");
                File schedulerFileUploadLocation = new File(schedulerUploadLocation, String.valueOf(jobId));
                try {
                    FileUtils.deleteDirectory(schedulerFileUploadLocation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(jobId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "job/getReportsFiles", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String getReportsFiles() {
        JSONArray response = new JSONArray();
        try {
            String reportLocation = ConfigContext.getCurrentContextConfig().getProperty("project.home") + "/reports";
            File reportDirectory = new File(reportLocation);
            response = getFileListResponse(reportDirectory);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }



    @RequestMapping(method = RequestMethod.GET, value = "job/getSpecificReportsFiles", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String getSpecificReportsFiles(@RequestParam("jobDetailsId") long jobDetailsId) {
        JSONArray response = new JSONArray();
        try {
            String reportLocation = ConfigContext.getCurrentContextConfig().getProperty("project.home") + "/reports";
            File reportDirectory = new File(reportLocation);
            File jobReportDirectory = new File(reportDirectory,String.valueOf(jobDetailsId));
            if(jobReportDirectory.exists() && jobReportDirectory.isDirectory()) {
                reportDirectory = jobReportDirectory;
            }
            response = getFileListResponse(reportDirectory);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    private JSONArray getFileListResponse(File reportDirectory) throws JSONException {
        JSONArray response = new JSONArray();
        if(reportDirectory.exists() && reportDirectory.isDirectory()) {
            File[] fileLists = reportDirectory.listFiles();
            for(File file : fileLists) {
                if(file.isFile()) {
                    JSONObject fileObject = new JSONObject();
                    fileObject.put("id",file.getName());
                    fileObject.put("name",file.getName());
                    fileObject.put("parent",file.getParentFile().getName());
                    response.put(fileObject);
                } else if(file.isDirectory()) {
                    JSONArray fileListResponse = getFileListResponse(file);
                    for(int index = 0;  index < fileListResponse.length(); index++) {
                        JSONObject jsonObject = fileListResponse.getJSONObject(index);
                        response.put(jsonObject);
                    }
                }
            }
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "job/getFileContent", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String getFileContent(@RequestParam("fileName") String fileName, @RequestParam("parent") String parent) {
        JSONObject response = new JSONObject();
        try {
            String reportLocation = ConfigContext.getCurrentContextConfig().getProperty("project.home") + "/reports";
            File reportDirectory = new File(reportLocation);
            if(reportDirectory.exists() && reportDirectory.isDirectory()) {
                File file = null;
                if(!reportDirectory.getName().equals(parent)) {
                    file = new File(reportDirectory + File.separator + parent + File.separator + fileName);
                } else {
                    file = new File(reportDirectory + File.separator + fileName);
                }
                if(file.exists() && file.isFile()) {
                    String fileContent = FileUtils.readFileToString(file);
                    response.put("fileContent",fileContent);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "job/downloadReportFile")
    @ResponseBody
    public byte[] downloadReportFile(@RequestParam("fileName") String fileName, @RequestParam("parent") String parent, HttpServletResponse response) throws IOException {

        byte[] fileContentBytes = null;

        String fileContent = "";

        try {
            String reportLocation = ConfigContext.getCurrentContextConfig().getProperty("report.directory");
            File reportDirectory = new File(reportLocation);
            if(reportDirectory.exists() && reportDirectory.isDirectory()) {
                File file = null;
                if(!reportDirectory.getName().equals(parent)) {
                    file = new File(reportDirectory + File.separator + parent + File.separator + fileName);
                } else {
                    file = new File(reportDirectory + File.separator + fileName);
                }
                if(file.exists() && file.isFile()) {
                    fileContent = FileUtils.readFileToString(file);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String extension = FilenameUtils.getExtension(fileName);
        String fileNameWithoutExtension = FilenameUtils.getBaseName(fileName);
        if(extension.contains(OleNGConstants.MARC) || extension.contains(OleNGConstants.XML)) {
            fileContentBytes = fileContent.getBytes();
        } else {
            if(fileName.contains("FailureMessages") || fileName.contains("_DeletedBibIds")) {
                fileContentBytes = fileContent.getBytes();
            } else if(fileName.contains("BibImport")) {
                fileContentBytes = getBatchExcelReportUtil().getExcelSheetForBibImport(fileContent);
                extension = "xlsx";
            } else if(fileName.contains("OrderImport")) {
                fileContentBytes = getBatchExcelReportUtil().getExcelSheetForOrderImport(fileContent);
                extension = "xlsx";
            } else if(fileName.contains("InvoiceImport")) {
                String fileContent1 ="";
                String fileContent2 ="";
                if(fileContent.contains(",\n  \"recordsMap\"")) {
                    fileContent1 = fileContent.substring(0, fileContent.indexOf(",\n  \"recordsMap\""));
                    fileContent2 = fileContent.substring(fileContent.indexOf(",\n  \"unmatchedCount\""));
                }
                if(StringUtils.isNotBlank(fileContent1)&&StringUtils.isNotBlank(fileContent2)){
                    fileContentBytes = getBatchExcelReportUtil().getExcelSheetForInvoiceImport(fileContent1+fileContent2);
                } else{
                    fileContentBytes = getBatchExcelReportUtil().getExcelSheetForInvoiceImport(fileContent);
                }
                extension = "xlsx";
            } else if(fileName.contains("BatchExport")) {
                fileContentBytes = getBatchExcelReportUtil().getExcelSheetForBatchExport(fileContent);
                extension = "xlsx";
            } else if(fileName.contains("BatchDelete")) {
                fileContentBytes = getBatchExcelReportUtil().getExcelSheetForBatchDelete(fileContent);
                extension = "xlsx";
            }
        }

        String fileNameWithExtension = fileNameWithoutExtension + "." + extension;

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameWithExtension + "\"");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentLength(fileContentBytes.length);

        return fileContentBytes;

    }

    @RequestMapping(method = RequestMethod.GET, value = "job/getBatchStatus", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String getBatchStatus(@RequestParam("filePathName") String filePathName) {
        JSONObject response = new JSONObject();
        try {
            String batchUploadLocation = getBatchUploadLocation();
            File statusFile = new File(batchUploadLocation, filePathName);
            if(statusFile.exists() && statusFile.isFile()) {
                String statusContent = FileUtils.readFileToString(statusFile);
                if(StringUtils.isNotBlank(statusContent)) {
                    JSONObject statusJson = new JSONObject(statusContent);
                    String status = getStringValueFromJsonObject(statusJson, OleNGConstants.STATUS);
                    String timeSpent = getStringValueFromJsonObject(statusJson, OleNGConstants.TIME_SPENT);
                    response.put(OleNGConstants.STATUS, status);
                    response.put(OleNGConstants.TIME_SPENT, timeSpent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "job/deleteJobExecution", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String deleteJobExecution(@RequestParam("jobDetailsId") String jobDetailsId) {
        JSONObject response = new JSONObject();
        try {
            getBatchUtil().deleteJobDetailsById(Long.valueOf(jobDetailsId));
            String reportDirectory = ConfigContext.getCurrentContextConfig().getProperty("report.directory");
            File file = new File(reportDirectory, jobDetailsId);
            if(file.exists() && file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "job/stopJobExecution", produces = {MediaType. APPLICATION_JSON + OleNGConstants.CHARSET_UTF_8})
    @ResponseBody
    public String stopJobExecution(@RequestParam("jobDetailsId") String jobDetailsId) {
        JSONObject response = new JSONObject();
        try {
            BatchJobDetails batchJobDetails = getBatchUtil().getJobDetailsById(Long.valueOf(jobDetailsId));
            if(null != batchJobDetails) {
                getBatchUtil().BATCH_JOB_EXECUTION_DETAILS_MAP.remove(batchJobDetails.getJobId() + "_" + batchJobDetails.getJobDetailId());
                batchJobDetails.setStatus(OleNGConstants.STOPPED);
                getBusinessObjectService().save(batchJobDetails);
                response.put(OleNGConstants.STATUS, batchJobDetails.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public BatchProcessJob convertJsonToProcessJob(String processJsonString) throws JSONException, IOException {
        getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProcessJob batchProcessJob = getObjectMapper().readValue(processJsonString, BatchProcessJob.class);
        return batchProcessJob;
    }

    public BatchScheduleJob convertJsonToScheduleJob(String scheduleJobJsonString) throws JSONException, IOException {
        getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchScheduleJob batchScheduleJob = getObjectMapper().readValue(scheduleJobJsonString, BatchScheduleJob.class);
        return batchScheduleJob;
    }

    public BatchFileProcessor getBatchProcessor(String batchType) {
        if(batchType.equalsIgnoreCase("Order Record Import")){
            return batchOrderImportProcessor;
        } else if(batchType.equalsIgnoreCase("Bib Import")) {
            return batchBibFileProcessor;
        } else if(batchType.equalsIgnoreCase("Invoice Import")) {
            return batchInvoiceImportProcessor;
        } else if(batchType.equalsIgnoreCase("Batch Delete")) {
            return batchDeleteFileProcessor;
        } else if(batchType.equalsIgnoreCase("Batch Export")) {
            return batchExportFileProcessor;
        }
        return null;
    }

    private void saveUploadedFile(BatchProcessJob matchedBatchJob, MultipartFile multipartFile) throws IOException {
        File schedulerFileUploadLocation = getDirectoryPath(matchedBatchJob.getJobId());
        File file = new File(schedulerFileUploadLocation , multipartFile.getOriginalFilename());
        FileUtils.writeStringToFile(file, IOUtils.toString(multipartFile.getBytes()));
    }

    private File getDirectoryPath(long jobId) {
        String schedulerUploadLocation = ConfigContext.getCurrentContextConfig().getProperty("schedulerUploadLocation");
        File schedulerFileUploadLocation = new File(schedulerUploadLocation, String.valueOf(jobId));
        try {
            FileUtils.deleteDirectory(schedulerFileUploadLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        schedulerFileUploadLocation.mkdirs();
        return schedulerFileUploadLocation;
    }

    public BatchExcelReportUtil getBatchExcelReportUtil() {
        if(null == batchExcelReportUtil) {
            batchExcelReportUtil = new BatchExcelReportUtil();
        }
        return batchExcelReportUtil;
    }

    public void setBatchExcelReportUtil(BatchExcelReportUtil batchExcelReportUtil) {
        this.batchExcelReportUtil = batchExcelReportUtil;
    }

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }

    public void setBatchUtil(BatchUtil batchUtil) {
        this.batchUtil = batchUtil;
    }

    // Method added as fix for OLE-9027 to disallow Second Instance of VUFIND Job to run at same time
    private boolean isJobRunning(long jobId) {
        BatchProcessJob jobRunning = getBatchUtil().getBatchProcessJobById(Long.valueOf(jobId));
        if(jobRunning.getJobName().toUpperCase().contains("VU")) {
            List<BatchProcessJob> batchProcessJobs = getDescribeDAO().fetchAllBatchProcessJobs();
            for (BatchProcessJob batchProcessJob : batchProcessJobs) {
                batchProcessJob.getStatus();
                if (jobId == batchProcessJob.getJobId()) {
                    List<BatchJobDetails> batchJobDetailsList = batchProcessJob.getBatchJobDetailsList();
                    for (BatchJobDetails existingBatchJobDetails : batchJobDetailsList) {
                        if (existingBatchJobDetails.getStatus().equals(OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}