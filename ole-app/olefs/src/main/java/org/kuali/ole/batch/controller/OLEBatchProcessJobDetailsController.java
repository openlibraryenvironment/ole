package org.kuali.ole.batch.controller;

import org.apache.commons.io.IOUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.form.OLEBatchProcessJobDetailsForm;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
import org.kuali.ole.batch.service.OLEBatchSchedulerService;
import org.kuali.ole.sys.batch.BatchFile;
import org.kuali.ole.sys.batch.BatchFileUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.TransactionalDocumentControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.quartz.CronExpression;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: krishnamohanv
 * Date: 7/12/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */


@Controller
@RequestMapping(value = "/oleBatchProcessJobController")
public class OLEBatchProcessJobDetailsController extends TransactionalDocumentControllerBase {

    private OLEBatchProcessDataHelper oleBatchProcessDataHelper;
    private BusinessObjectService businessObjectService;
    private OLEBatchSchedulerService schedulerService;
    private static HashMap<String, String> batchProcessJobStatusMap = new HashMap<String, String>();

    public OLEBatchSchedulerService getSchedulerService() {
        if (schedulerService == null) {
            schedulerService = GlobalResourceLoader.getService("oleSchedulerService");
        }
        return schedulerService;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null)
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        return businessObjectService;
    }

    private OLEBatchProcessDataHelper getOLEBatchProcessDataHelper() {

        if (oleBatchProcessDataHelper == null) {
            oleBatchProcessDataHelper = OLEBatchProcessDataHelper.getInstance();
        }
        return oleBatchProcessDataHelper;
    }

    @Override
    protected OLEBatchProcessJobDetailsForm createInitialForm(HttpServletRequest request) {
        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = new OLEBatchProcessJobDetailsForm();
        GlobalVariables.getUserSession().addObject("formId", oleBatchProcessJobDetailsForm.getFormKey());
        return oleBatchProcessJobDetailsForm;
    }

    @RequestMapping(params = "methodToCall=stopButton")
    public ModelAndView stopButton(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oLEBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        OLEBatchProcessJobDetailsBo jobDetailsBo = getJobBo(oLEBatchProcessJobDetailsForm);
        batchProcessJobStatusMap.put(jobDetailsBo.getJobId(), OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED);
        return getUIFModelAndView(oLEBatchProcessJobDetailsForm);
    }

    @RequestMapping(params = "methodToCall=startButton")
    public ModelAndView restartButton(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oLEBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        OLEBatchProcessJobDetailsBo jobDetailsBo = getJobBo(oLEBatchProcessJobDetailsForm);
        jobDetailsBo.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING);
        getBusinessObjectService().save(jobDetailsBo);
        List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findAllOrderBy(OLEBatchProcessJobDetailsBo.class,"jobId",false);
        oLEBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oleBatchProcessJobDetailsBoList);
        getSchedulerService().startJob(jobDetailsBo.getJobId());
        return getUIFModelAndView(oLEBatchProcessJobDetailsForm);
    }

    @RequestMapping(params = "methodToCall=resumeButton")
    public ModelAndView resumeButton(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oLEBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        OLEBatchProcessJobDetailsBo jobDetailsBo = getJobBo(oLEBatchProcessJobDetailsForm);
        if (jobDetailsBo.getPerCompleted().equalsIgnoreCase(String.format("%.2f", Float.valueOf(100)) + "%")) {
            jobDetailsBo.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        } else {
            jobDetailsBo.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING);
            getSchedulerService().resumeJob(jobDetailsBo.getJobId());
        }
        getBusinessObjectService().save(jobDetailsBo);
       /* List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findAllOrderBy(OLEBatchProcessJobDetailsBo.class,"jobId",false);
        oLEBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oleBatchProcessJobDetailsBoList);*/
        for(OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo:oLEBatchProcessJobDetailsForm.getOleBatchProcessJobDetailsBoList()){
            if(oleBatchProcessJobDetailsBo.getJobId().equals(jobDetailsBo.getJobId())){
                oleBatchProcessJobDetailsBo.setStatus(jobDetailsBo.getStatus());
                break;
            }
        }
        return getUIFModelAndView(oLEBatchProcessJobDetailsForm);
    }

    @RequestMapping(params = "methodToCall=removeButton")
    public ModelAndView removeButton(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oLEBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        OLEBatchProcessJobDetailsBo jobDetailsBo = getJobBo(oLEBatchProcessJobDetailsForm);
        getOLEBatchProcessDataHelper().deleteBatchFailureFile(jobDetailsBo.getBatchProcessType(), jobDetailsBo.getJobId() + "_FailureRecord" + "_" + jobDetailsBo.getUploadFileName());
        jobDetailsBo.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_CANCELLED);
        getBusinessObjectService().save(jobDetailsBo);
        /*List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findAllOrderBy(OLEBatchProcessJobDetailsBo.class,"jobId",false);
        oLEBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oleBatchProcessJobDetailsBoList);*/
        for(OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo:oLEBatchProcessJobDetailsForm.getOleBatchProcessJobDetailsBoList()){
            if(oleBatchProcessJobDetailsBo.getJobId().equals(jobDetailsBo.getJobId())){
                oleBatchProcessJobDetailsBo.setStatus(jobDetailsBo.getStatus());
                break;
            }
        }
        getSchedulerService().deleteJob(jobDetailsBo.getJobId());
        return getUIFModelAndView(oLEBatchProcessJobDetailsForm);
    }

    @RequestMapping(params = "methodToCall=pauseButton")
    public ModelAndView pauseButton(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oLEBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        OLEBatchProcessJobDetailsBo jobDetailsBo = getJobBo(oLEBatchProcessJobDetailsForm);
        if (jobDetailsBo.getPerCompleted().equalsIgnoreCase(String.format("%.2f", Float.valueOf(100)) + "%")) {
            jobDetailsBo.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        } else {
            jobDetailsBo.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_PAUSED);
            getSchedulerService().pauseJob(jobDetailsBo.getJobId());
        }
        getBusinessObjectService().save(jobDetailsBo);
        /*List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findAllOrderBy(OLEBatchProcessJobDetailsBo.class,"jobId",false);
        oLEBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oleBatchProcessJobDetailsBoList);*/
        for(OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo:oLEBatchProcessJobDetailsForm.getOleBatchProcessJobDetailsBoList()){
            if(oleBatchProcessJobDetailsBo.getJobId().equals(jobDetailsBo.getJobId())){
                oleBatchProcessJobDetailsBo.setStatus(jobDetailsBo.getStatus());
                break;
            }
        }
        return getUIFModelAndView(oLEBatchProcessJobDetailsForm);
    }

    @RequestMapping(params = "methodToCall=openViewReport")
    public ModelAndView openViewReport(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        OLEBatchProcessJobDetailsForm oLEBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        OLEBatchProcessJobDetailsBo jobDetailsBo = getJobBo(oLEBatchProcessJobDetailsForm);
        if (jobDetailsBo.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT)) {
            jobDetailsBo.setUploadFileName(getOLEBatchProcessDataHelper().getExportPathUrl(jobDetailsBo));
        } else if (jobDetailsBo.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_BIB_IMPORT)) {
            jobDetailsBo.setBibErrorPath(getOLEBatchProcessDataHelper().getBibPathUrl(jobDetailsBo));
        } else if (jobDetailsBo.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {
            jobDetailsBo.setBatchDeletePath(getOLEBatchProcessDataHelper().getDeletePathUrl(jobDetailsBo));
        } else if (jobDetailsBo.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT)) {
            jobDetailsBo.setSerialCSVErrorPath(getOLEBatchProcessDataHelper().getSerialCSVPathUrl(jobDetailsBo));
        }
        else if (jobDetailsBo.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.FUND_RECORD_IMPORT)) {
            jobDetailsBo.setFundCodeCSVErrorPath(getOLEBatchProcessDataHelper().getFundCodeCSVPathUrl(jobDetailsBo));
        }
        List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findAllOrderBy(OLEBatchProcessJobDetailsBo.class,"jobId",false);
        oLEBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oleBatchProcessJobDetailsBoList);
        oLEBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBo(jobDetailsBo);

        return getUIFModelAndView(oLEBatchProcessJobDetailsForm);
    }

    @RequestMapping(params = "methodToCall=downLoadFile")
    public ModelAndView downLoadFile(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;

        OLEBatchProcessJobDetailsBo jobDetailsBo = getJobBo(oleBatchProcessJobDetailsForm);
        if (jobDetailsBo != null) {
            Map map = new HashMap();
            map.put("batchProcessId", jobDetailsBo.getBatchProcessId());
            List<OLEBatchProcessDefinitionDocument> oleBatchProcessDefinitionDocumentList = (List<OLEBatchProcessDefinitionDocument>) getBusinessObjectService().findMatching(OLEBatchProcessDefinitionDocument.class, map);
            if (oleBatchProcessDefinitionDocumentList != null && oleBatchProcessDefinitionDocumentList.size() > 0) {
                OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = oleBatchProcessDefinitionDocumentList.get(0);
                File file = null;
                if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT)) {
                    file = new File(getBatchProcessFilePath(oleBatchProcessDefinitionDocument.getBatchProcessType() , jobDetailsBo.getJobId()) + jobDetailsBo.getJobId() + OLEConstants.OLEBatchProcess.DELETED_BIB_IDS_FILE_NAME);
                }
                else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.FUND_RECORD_IMPORT)){
                    String[] fileNames=jobDetailsBo.getUploadFileName().split(",");
                            new File(getBatchProcessFilePath(oleBatchProcessDefinitionDocument.getBatchProcessType() , jobDetailsBo.getJobId()) + jobDetailsBo.getJobId() + "_FailureRecord" + "_" + fileNames[0]);
                }
                else {
                    file = new File(getBatchProcessFilePath(oleBatchProcessDefinitionDocument.getBatchProcessType() , jobDetailsBo.getJobId()) + jobDetailsBo.getJobId() + "_FailureRecord" + "_" + jobDetailsBo.getUploadFileName());
                }
                if (!file.exists() || !file.isFile()) {
                    throw new RuntimeException("Error: non-existent file or directory provided");
                }
                File containingDirectory = file.getParentFile();
                if (!BatchFileUtils.isDirectoryAccessible(containingDirectory.getAbsolutePath())) {
                    throw new RuntimeException("Error: inaccessible directory provided");
                }

                BatchFile batchFile = new BatchFile();
                batchFile.setFile(file);
                response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setContentLength((int) file.length());

                InputStream fis = new FileInputStream(file);
                IOUtils.copy(fis, response.getOutputStream());
                response.getOutputStream().flush();
            }
        }
        return null;
    }

    @RequestMapping(params = "methodToCall=downLoadErrorAttachmentFile")
    public ModelAndView downLoadErrorAttachmentFile(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        OLEBatchProcessJobDetailsBo jobDetailsBo = getJobBo(oleBatchProcessJobDetailsForm);
        if (jobDetailsBo != null) {
            Map map = new HashMap();
            map.put("batchProcessId", jobDetailsBo.getBatchProcessId());
            List<OLEBatchProcessDefinitionDocument> oleBatchProcessDefinitionDocumentList = (List<OLEBatchProcessDefinitionDocument>) getBusinessObjectService().findMatching(OLEBatchProcessDefinitionDocument.class, map);
            if (oleBatchProcessDefinitionDocumentList != null && oleBatchProcessDefinitionDocumentList.size() > 0) {
                OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = oleBatchProcessDefinitionDocumentList.get(0);
                String uploadFileName = jobDetailsBo.getUploadFileName();
                String errorFileName = null;
                String[] fileNames = uploadFileName.split(",");
                errorFileName = fileNames.length == 2 ? fileNames[0]:uploadFileName;
                if(errorFileName.endsWith(".mrc")){
                    errorFileName = errorFileName.replace(".mrc",".txt");
                }
                else if(errorFileName.endsWith(".INV")){
                    errorFileName = errorFileName.replace(".INV",".txt");
                }
                else if(errorFileName.endsWith(".edi")){
                    errorFileName = errorFileName.replace(".edi",".txt");
                }
                File file = new File(getBatchProcessFilePath(oleBatchProcessDefinitionDocument.getBatchProcessType() , jobDetailsBo.getJobId()) + jobDetailsBo.getJobId() + "_FailureRecord" + "_" + errorFileName);
                if (!file.exists() || !file.isFile()) {
                    throw new RuntimeException("Error: non-existent file or directory provided");
                }
                File containingDirectory = file.getParentFile();
                if (!BatchFileUtils.isDirectoryAccessible(containingDirectory.getAbsolutePath())) {
                    throw new RuntimeException("Error: inaccessible directory provided");
                }
                BatchFile batchFile = new BatchFile();
                batchFile.setFile(file);
                response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setContentLength((int) file.length());

                InputStream fis = new FileInputStream(file);
                IOUtils.copy(fis, response.getOutputStream());
                response.getOutputStream().flush();
            }
        }
        return null;
    }

    @RequestMapping(params = "methodToCall=jobDocHandler")
    public ModelAndView jobDocHandler(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        ModelAndView modelAndView = super.docHandler(oleBatchProcessJobDetailsForm, result, request, response);
        String documentClass = request.getParameter("documentClass");
        if (documentClass != null && OLEBatchProcessScheduleBo.class.toString().contains(documentClass)) {
            List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList = (List<OLEBatchProcessScheduleBo>) getBusinessObjectService().findAll(OLEBatchProcessScheduleBo.class);
            for (OLEBatchProcessScheduleBo oleBatchProcessScheduleBo : oleBatchProcessScheduleBoList) {
                oleBatchProcessScheduleBo.setBatchProfileName(getBatchProcessDocument(oleBatchProcessScheduleBo.getBatchProcessId()).getBatchProcessProfileName());
            }
            oleBatchProcessJobDetailsForm.setOleBatchProcessScheduleBoList(oleBatchProcessScheduleBoList);
            oneTimeDate(oleBatchProcessScheduleBoList);
        } else {
            List<OLEBatchProcessJobDetailsBo> oLEBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findAllOrderBy(OLEBatchProcessJobDetailsBo.class,"jobId",false);
            oleBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oLEBatchProcessJobDetailsBoList);
        }
        return modelAndView;
    }

    private OLEBatchProcessDefinitionDocument getBatchProcessDocument(String batchProcessId) {
        Map map = new HashMap();
        map.put("batchProcessId", batchProcessId);
        OLEBatchProcessDefinitionDocument batchProcessDefinitionDocument = getBusinessObjectService().findByPrimaryKey(OLEBatchProcessDefinitionDocument.class, map);
        return batchProcessDefinitionDocument;
    }

    private String getBatchProcessFilePath(String batchProceesType , String jobId) {
        String batchProcessLocation = getOLEBatchProcessDataHelper().getBatchProcessFilePath(batchProceesType , jobId);
        return batchProcessLocation;
    }

    /**
     * Runs the process for the profile selected and takes the scheduled record id as the job name
     * @param jobName
     */
    private void runJobNow(String jobName) {
        OLEBatchSchedulerService schedulerService = GlobalResourceLoader.getService("oleSchedulerService");
        try {
            schedulerService.startJob(jobName);
        } catch (Exception e) {
            LOG.error("Error while starting job with job id :: " + jobName);
        }
    }

    private void oneTimeDate(List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList) {

        try {
            for (OLEBatchProcessScheduleBo oleBatchProcessScheduleBo : oleBatchProcessScheduleBoList) {
                CronExpression exp = new CronExpression(oleBatchProcessScheduleBo.getCronExpression());
                Date date = exp.getNextValidTimeAfter(new Date());
                if (date != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Timestamp nextRunTime = new Timestamp(date.getTime());
                    oleBatchProcessScheduleBo.setNextRunTime(nextRunTime);
                }
            }
        } catch (ParseException e) {
            LOG.error("Error while validating cron exp::" + oleBatchProcessScheduleBoList.get(0).getCronExpression(), e);
        }


    }

    @RequestMapping(params = "methodToCall=removeScheduleJob")
    public ModelAndView removeScheduleJob(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        Map map = new HashMap();
        map.put("scheduleId", request.getParameter("scheduleId"));
        OLEBatchProcessScheduleBo scheduleBo = getBusinessObjectService().findByPrimaryKey(OLEBatchProcessScheduleBo.class, map);
        getOLEBatchProcessDataHelper().deleteBatchFailureFile(scheduleBo.getBatchProcessType(), scheduleBo.getScheduleId() + "_Scheduled" + "_" + scheduleBo.getUploadFileName());
        getBusinessObjectService().delete(scheduleBo);
        removeJob(scheduleBo.getScheduleId());
        List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList = (List<OLEBatchProcessScheduleBo>) getBusinessObjectService().findAll(OLEBatchProcessScheduleBo.class);
        oleBatchProcessJobDetailsForm.setOleBatchProcessScheduleBoList(oleBatchProcessScheduleBoList);
        return getUIFModelAndView(oleBatchProcessJobDetailsForm);
    }

    private void removeJob(String jobName) {
        OLEBatchSchedulerService schedulerService = GlobalResourceLoader.getService("oleSchedulerService");
        try {
            schedulerService.deleteJob(jobName);
        } catch (Exception e) {
            LOG.error("Error while removing job with schedule id :: " + jobName);
        }
    }

    private OLEBatchProcessJobDetailsBo getJobBo(OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm) {
        Map map = new HashMap();
        map.put("jobId", oleBatchProcessJobDetailsForm.getJobId());
        OLEBatchProcessJobDetailsBo jobDetailsBo = getBusinessObjectService().findByPrimaryKey(OLEBatchProcessJobDetailsBo.class, map);
        return jobDetailsBo;
    }

    public static HashMap<String, String> getBatchProcessJobStatusMap() {
        return batchProcessJobStatusMap;
    }

    public static void setBatchProcessJobStatusMap(String jobId, String jobStatus) {
        batchProcessJobStatusMap.put(jobId, jobStatus);
    }

    public static void removeStatusFromBatchProcess(String jobId) {
        if (getBatchProcessJobStatusMap() != null)
            batchProcessJobStatusMap.remove(jobId);
    }

    public static String getBatchProcessJobStatus(String jobId) {
        String jobStatus = null;
        if (getBatchProcessJobStatusMap() != null) {
            jobStatus = batchProcessJobStatusMap.get(jobId);
            if (jobStatus != null)
                return jobStatus;
            else {
                return OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING;
            }
        } else {
            return OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING;
        }
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Entering Batch process job details search");
        boolean checkFromDate = false;
        boolean checkToDate = false;
        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        oleBatchProcessJobDetailsForm.setSingleJobView(false);
        if (oleBatchProcessJobDetailsForm.getJobFromDate() != null) {
            checkFromDate = true;
        }
        if (oleBatchProcessJobDetailsForm.getJobToDate() != null) {
            checkToDate = true;
        }
        oleBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(null);
        List<OLEBatchProcessJobDetailsBo> oLEBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findAllOrderBy(OLEBatchProcessJobDetailsBo.class, "jobId", false);
        if (!checkFromDate && !checkToDate) {
            oleBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oLEBatchProcessJobDetailsBoList);
        } else {
            List<OLEBatchProcessJobDetailsBo> resultList = new ArrayList<>();
            for (OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo : oLEBatchProcessJobDetailsBoList) {
                Date fromDate = oleBatchProcessJobDetailsBo.getCreateTime();
                if (checkFromDate && checkToDate) {
                    Date searchFromDate = oleBatchProcessJobDetailsForm.getJobFromDate();
                    int subtract = 1;
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTime(oleBatchProcessJobDetailsForm.getJobToDate());
                    calendar.add(Calendar.DAY_OF_YEAR, subtract);
                    Date searchToDate = calendar.getTime();
                    if (fromDate.after(searchFromDate) && fromDate.before(searchToDate)) {
                        resultList.add(oleBatchProcessJobDetailsBo);
                    }
                } else if (checkFromDate) {
                    Date searchFromDate = oleBatchProcessJobDetailsForm.getJobFromDate();
                    if (fromDate.after(searchFromDate)) {
                        resultList.add(oleBatchProcessJobDetailsBo);
                    }
                } else if (checkToDate) {
                    int subtract = 1;
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTime(oleBatchProcessJobDetailsForm.getJobToDate());
                    calendar.add(Calendar.DAY_OF_YEAR, subtract);
                    Date searchToDate = calendar.getTime();
                    if (fromDate.before(searchToDate)) {
                        resultList.add(oleBatchProcessJobDetailsBo);
                    }
                }
            }
            oleBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(resultList);
        }

        LOG.debug("Leaving Batch process job details search");
        return getUIFModelAndView(oleBatchProcessJobDetailsForm);
    }

    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Entering Batch process job details search");
        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        oleBatchProcessJobDetailsForm.setJobFromDate(null);
        oleBatchProcessJobDetailsForm.setJobToDate(null);
        LOG.debug("Leaving Batch process job details search");
        return getUIFModelAndView(oleBatchProcessJobDetailsForm);
    }

    @RequestMapping(params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (org.apache.commons.lang.StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        return performRedirect(form, url, props);
    }

    @RequestMapping(params = "methodToCall=singleJobDetailView")
    public ModelAndView singleJobDetailView(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        oleBatchProcessJobDetailsForm.setSingleJobView(true);
        ModelAndView modelAndView = super.docHandler(oleBatchProcessJobDetailsForm, result, request, response);
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("batchProcessId",request.getParameter("batchProcessId"));
        List<OLEBatchProcessJobDetailsBo> oLEBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findMatching(OLEBatchProcessJobDetailsBo.class,parameterMap);
        oleBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oLEBatchProcessJobDetailsBoList);
        return modelAndView;
    }

    @RequestMapping(params = "methodToCall=refreshSingleJobDetailView")
    public ModelAndView refreshSingleJobDetailView(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessJobDetailsForm oleBatchProcessJobDetailsForm = (OLEBatchProcessJobDetailsForm) form;
        oleBatchProcessJobDetailsForm.setSingleJobView(true);
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("jobId",oleBatchProcessJobDetailsForm.getJobId());
        List<OLEBatchProcessJobDetailsBo> oLEBatchProcessJobDetailsBoList = (List<OLEBatchProcessJobDetailsBo>) getBusinessObjectService().findMatching(OLEBatchProcessJobDetailsBo.class,parameterMap);
        oleBatchProcessJobDetailsForm.setOleBatchProcessJobDetailsBoList(oLEBatchProcessJobDetailsBoList);
        return getUIFModelAndView(oleBatchProcessJobDetailsForm);
    }
}