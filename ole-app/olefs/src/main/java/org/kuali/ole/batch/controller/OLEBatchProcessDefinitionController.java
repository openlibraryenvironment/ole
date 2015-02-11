package org.kuali.ole.batch.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.form.OLEBatchProcessDefinitionForm;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
import org.kuali.ole.batch.helper.OLESchedulerHelper;
import org.kuali.ole.batch.keyvalue.*;
import org.kuali.ole.batch.rule.OLEBatchProcessRule;
import org.kuali.ole.batch.service.OLEBatchSchedulerService;
import org.kuali.ole.select.document.OLEInvoiceIngestLoadReport;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.TransactionalDocumentControllerBase;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/12/13
 * Time: 8:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/oleBatchProcessDefinitionController")
public class OLEBatchProcessDefinitionController extends TransactionalDocumentControllerBase {

    @Override
    protected DocumentFormBase createInitialForm(HttpServletRequest request) {
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = new OLEBatchProcessDefinitionForm();
        return oleBatchProcessDefinitionForm;
    }

    private OLEBatchProcessDataHelper oleBatchProcessDataHelper;
    private OLEBatchProcessRule oleBatchProcessRule;

    public OLEBatchProcessRule getOleBatchProcessRule() {
        if (oleBatchProcessRule == null) {
            oleBatchProcessRule = new OLEBatchProcessRule();
        }
        return oleBatchProcessRule;
    }

    private OLEBatchProcessDataHelper getOLEBatchProcessDataHelper() {

        if (oleBatchProcessDataHelper == null) {
            oleBatchProcessDataHelper = OLEBatchProcessDataHelper.getInstance();
        }
        return oleBatchProcessDataHelper;
    }

    @RequestMapping(params = "methodToCall=startBatch")
    public ModelAndView startBatch(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = super.docHandler(form,result,request,response);
        boolean isValidated = getOleBatchProcessRule().canPerformBatchImport(GlobalVariables.getUserSession().getPrincipalId())
                || getOleBatchProcessRule().canPerformBatchExport(GlobalVariables.getUserSession().getPrincipalId())
                || getOleBatchProcessRule().canPerformBatchDelete(GlobalVariables.getUserSession().getPrincipalId());
        if(!isValidated) {
          GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.ERROR_AUTHORIZATION);
          ((OLEBatchProcessDefinitionForm) form).setPermissionFlag(false);
        }
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        oleBatchProcessDefinitionDocument.setChunkSize(10000);
        oleBatchProcessDefinitionDocument.setMaxRecordsInFile(100000);
        oleBatchProcessDefinitionDocument.setMaxNumberOfThreads(10);
        return modelAndView;
    }


    @RequestMapping(params = "methodToCall=runNowRoute")
    public ModelAndView runNowRoute(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        buildProcessDefinitionDocument(oleBatchProcessDefinitionDocument);
        oleBatchProcessDefinitionForm.setDocument(oleBatchProcessDefinitionDocument);


        boolean isValid = getOleBatchProcessRule().canPerformBatchImport(GlobalVariables.getUserSession().getPrincipalId())
                || getOleBatchProcessRule().canPerformBatchExport(GlobalVariables.getUserSession().getPrincipalId())
                || getOleBatchProcessRule().canPerformBatchDelete(GlobalVariables.getUserSession().getPrincipalId());
        if (!isValid) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.ERROR_AUTHORIZATION);
            return getUIFModelAndView(oleBatchProcessDefinitionForm);
        }

        if (oleBatchProcessDefinitionDocument.getBatchProcessId() != null)
            return getUIFModelAndView(oleBatchProcessDefinitionForm);
        boolean isValidated = getOleBatchProcessRule().batchValidations(oleBatchProcessDefinitionForm);
        if (!isValidated) {
            return getUIFModelAndView(oleBatchProcessDefinitionForm);
        }
        if (oleBatchProcessDefinitionDocument.getBatchProcessType() != null && oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_INVOICE) && !oleBatchProcessDefinitionDocument.isContinueImportFlag()) {
            String fileName = oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename();
            List<OLEInvoiceIngestLoadReport> oleInvoiceIngestLoadReportList = (List<OLEInvoiceIngestLoadReport>) getBusinessObjectService().findAll(OLEInvoiceIngestLoadReport.class);
            if (oleInvoiceIngestLoadReportList != null && oleInvoiceIngestLoadReportList.size() > 0) {
                List<String> fileNames = new ArrayList<>();
                for (OLEInvoiceIngestLoadReport oleInvoiceIngestLoadReport : oleInvoiceIngestLoadReportList) {
                    fileNames.add(oleInvoiceIngestLoadReport.getFileName());
                }
                if (fileNames != null && fileNames.size() > 0) {
                    if (fileNames.contains(fileName)) {
                        oleBatchProcessDefinitionDocument.setFileFlag(true);
                        return getUIFModelAndView(oleBatchProcessDefinitionForm);
                    } else {

                    }
                }
            }
        }
        oleBatchProcessDefinitionDocument.setUser(GlobalVariables.getUserSession().getPrincipalName());
        oleBatchProcessDefinitionDocument.setLinkToJob(true);
        oleBatchProcessDefinitionDocument.getDocumentHeader().setDocumentDescription(OLEConstants.OLEBatchProcess.NEW_BATCH_PRCS_DOCUMENT + oleBatchProcessDefinitionDocument.getDocumentNumber());
        saveJob(oleBatchProcessDefinitionForm);
        ModelAndView modelAndView = super.save(oleBatchProcessDefinitionForm, result, request, response);
        String jobName = null;
        if (oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList() != null && oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().size() > 0) {
            OLEBatchProcessJobDetailsBo jobDetailsBo = oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().get(0);
            jobName = jobDetailsBo.getJobId();
            jobDetailsBo.setJobName(oleBatchProcessDefinitionDocument.getBatchProcessName());
            KRADServiceLocator.getBusinessObjectService().save(jobDetailsBo);
        }
        if (!oleBatchProcessDefinitionDocument.getBatchProcessType().equals(OLEConstants.OLEBatchProcess.CLAIM_REPORT) && oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList() != null && oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().size() > 0) {
            // for order import
            if (oleBatchProcessDefinitionDocument.getBatchProcessType().equals(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) {
                if (oleBatchProcessDefinitionDocument.getEdiFile() == null) {
                    createBatchProcessJobFile(oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().get(0), oleBatchProcessDefinitionDocument.getMarcFile(), null, oleBatchProcessDefinitionDocument.getBatchProcessType(), OLEConstants.OLEBatchProcess.PROFILE_JOB);
                } else {
                    createBatchProcessJobFile(oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().get(0), oleBatchProcessDefinitionDocument.getMarcFile(), oleBatchProcessDefinitionDocument.getEdiFile(), oleBatchProcessDefinitionDocument.getBatchProcessType(), OLEConstants.OLEBatchProcess.PROFILE_JOB);
                }
            }
            // for serial record import
            else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equals(OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT)) {
                if (oleBatchProcessDefinitionDocument.getInputFormat().equalsIgnoreCase("xml")) {
                    createBatchProcessJobFile(oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().get(0), oleBatchProcessDefinitionDocument.getIngestedFile(), null, oleBatchProcessDefinitionDocument.getBatchProcessType(), OLEConstants.OLEBatchProcess.PROFILE_JOB);
                } else {
                    createBatchProcessJobFile(oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().get(0), oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile(), oleBatchProcessDefinitionDocument.getSerialRecordTypeFile(), oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile(), oleBatchProcessDefinitionDocument.getBatchProcessType(), OLEConstants.OLEBatchProcess.PROFILE_JOB);
                }
            } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT)) {
                if (oleBatchProcessDefinitionDocument.getLoadIdFromFile().equalsIgnoreCase("true")) {
                    createBatchProcessJobFile(oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().get(0), oleBatchProcessDefinitionDocument.getIngestedFile(), null, oleBatchProcessDefinitionDocument.getBatchProcessType(), OLEConstants.OLEBatchProcess.PROFILE_JOB);
                }
            }
            // for other than   serial record import & Order Import
            else {
                createBatchProcessJobFile(oleBatchProcessDefinitionDocument.getOleBatchProcessJobDetailsBoList().get(0), oleBatchProcessDefinitionDocument.getIngestedFile(), null, oleBatchProcessDefinitionDocument.getBatchProcessType(), OLEConstants.OLEBatchProcess.PROFILE_JOB);
            }

        }
        if (jobName != null) {
            runJobNow(jobName);
        }
        oleBatchProcessDefinitionDocument.setAfterSubmitFlag(true);
        return modelAndView;
    }

    /**
     * This method saves the job
     * @param
     */
    private OLEBatchProcessJobDetailsBo saveJob(DocumentFormBase form) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo = new OLEBatchProcessJobDetailsBo();
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        String profileName = oleBatchProcessDefinitionDocument.getBatchProcessProfileName();
        if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) {
            if(oleBatchProcessDefinitionDocument.getEdiFile() != null) {
                MultipartFile marcFile = oleBatchProcessDefinitionDocument.getMarcFile();
                MultipartFile ediFile = oleBatchProcessDefinitionDocument.getEdiFile();
                oleBatchProcessJobDetailsBo.setUploadFileName(marcFile.getOriginalFilename() + "," + ediFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setUploadFileName(marcFile.getOriginalFilename() + "," + ediFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setMarcFileName(marcFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setEdiFileName(ediFile.getOriginalFilename());
            }
            else{
                MultipartFile ingestFile = oleBatchProcessDefinitionDocument.getMarcFile();
                oleBatchProcessJobDetailsBo.setUploadFileName(ingestFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setUploadFileName(ingestFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setMarcFileName(ingestFile.getOriginalFilename());
            }
        } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT)) {
            if (oleBatchProcessDefinitionDocument.getInputFormat().equalsIgnoreCase("xml")) {
                MultipartFile ingestFile = oleBatchProcessDefinitionDocument.getIngestedFile();
                oleBatchProcessJobDetailsBo.setUploadFileName(ingestFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setUploadFileName(ingestFile.getOriginalFilename());
            } else {
                MultipartFile documentFile = oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile();
                MultipartFile typeFile = oleBatchProcessDefinitionDocument.getSerialRecordTypeFile();
                MultipartFile historyFile = oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile();
                oleBatchProcessJobDetailsBo.setUploadFileName((documentFile != null ? documentFile.getOriginalFilename() + "," : "")
                        + (typeFile != null ? typeFile.getOriginalFilename() + "," : "")
                        + (historyFile != null ? historyFile.getOriginalFilename() : ""));
                oleBatchProcessDefinitionDocument.setSerialRecordDocumentFileName(documentFile != null ? documentFile.getOriginalFilename() : null);
                oleBatchProcessDefinitionDocument.setSerialRecordTypeFileName(typeFile != null ? typeFile.getOriginalFilename() : null);
                oleBatchProcessDefinitionDocument.setSerialRecordHistoryFileName(historyFile != null ? historyFile.getOriginalFilename() : null);
            }
            oleBatchProcessDefinitionDocument.setOutputFormat(oleBatchProcessDefinitionDocument.getInputFormat());
        } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT) && oleBatchProcessDefinitionDocument.getLoadIdFromFile().equalsIgnoreCase("true")) {
            MultipartFile ingestFile = oleBatchProcessDefinitionDocument.getIngestedFile();
            oleBatchProcessJobDetailsBo.setUploadFileName(ingestFile.getOriginalFilename());
            oleBatchProcessDefinitionDocument.setUploadFileName(ingestFile.getOriginalFilename());
        } else if (!oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT)
                && !oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.CLAIM_REPORT)
                && !oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT)) {
            MultipartFile ingestFile = oleBatchProcessDefinitionDocument.getIngestedFile();
            oleBatchProcessJobDetailsBo.setUploadFileName(ingestFile.getOriginalFilename());
            oleBatchProcessDefinitionDocument.setUploadFileName(ingestFile.getOriginalFilename());
        }
        oleBatchProcessJobDetailsBo.setCreateTime(timestamp);
        if (oleBatchProcessDefinitionDocument.isScheduleFlag()) {
            oleBatchProcessJobDetailsBo.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_SCHEDULED);
            //   oleBatchProcessJobDetailsBo.setStatus(profileName + " " + OLEConstants.OLEBatchProcess.JOB_STATUS_SCHEDULED);
        } else {
            oleBatchProcessJobDetailsBo.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING);
            // oleBatchProcessJobDetailsBo.setStatus(profileName + " " + OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING);
        }
        String loginUser = GlobalVariables.getUserSession().getPrincipalName();
        oleBatchProcessJobDetailsBo.setUserName(loginUser);
        oleBatchProcessJobDetailsBo.setBatchProfileName(profileName);
        oleBatchProcessJobDetailsBo.setBatchProcessType(oleBatchProcessDefinitionDocument.getBatchProcessType());
        List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBos = new ArrayList<OLEBatchProcessJobDetailsBo>();
        oleBatchProcessJobDetailsBos.add(oleBatchProcessJobDetailsBo);
        oleBatchProcessDefinitionDocument.setOleBatchProcessJobDetailsBoList(oleBatchProcessJobDetailsBos);
        oleBatchProcessJobDetailsBo.setStartTime(timestamp);
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        dataCarrierService.addData("oleBatchProcessJobDetailsBo",oleBatchProcessJobDetailsBo);
        return oleBatchProcessJobDetailsBo;
    }

    private void saveSchedule(OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm) {
        // OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm)form;
        Timestamp timestamp = new Timestamp(new Date().getTime());
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();

        oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo().setScheduleType(oleBatchProcessDefinitionDocument.getScheduleType());
        oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo().setOneTimeOrRecurring(oleBatchProcessDefinitionDocument.getOneTimeOrRecurring());
        OLEBatchProcessScheduleBo oleBatchProcessScheduleBo = oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo();
        if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) {
            if(oleBatchProcessDefinitionDocument.getEdiFile() != null) {
                MultipartFile marcFile = oleBatchProcessDefinitionDocument.getMarcFile();
                MultipartFile ediFile = oleBatchProcessDefinitionDocument.getEdiFile();
                oleBatchProcessScheduleBo.setUploadFileName(marcFile.getOriginalFilename() + "," + ediFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setUploadFileName(marcFile.getOriginalFilename() + "," + ediFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setMarcFileName(marcFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setEdiFileName(ediFile.getOriginalFilename());
            }
            else{
                MultipartFile ingestFile = oleBatchProcessDefinitionDocument.getMarcFile();
                oleBatchProcessScheduleBo.setUploadFileName(ingestFile.getOriginalFilename());
                oleBatchProcessDefinitionDocument.setMarcFileName(ingestFile.getOriginalFilename());
            }
        } else if ((oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT) && oleBatchProcessDefinitionDocument.getLoadIdFromFile().equalsIgnoreCase(String.valueOf(Boolean.TRUE))) || !oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT)) {
            MultipartFile ingestFile = oleBatchProcessDefinitionDocument.getIngestedFile();
            oleBatchProcessScheduleBo.setUploadFileName(ingestFile.getOriginalFilename());
            oleBatchProcessDefinitionDocument.setUploadFileName(ingestFile.getOriginalFilename());
        }
        oleBatchProcessScheduleBo.setCreateTime(timestamp);
        String loginUser = GlobalVariables.getUserSession().getPrincipalName();
        oleBatchProcessScheduleBo.setUserName(loginUser);
        String cronExpression = null;
        if (!oleBatchProcessDefinitionDocument.getCronOrSchedule().equals(OLEConstants.OLEBatchProcess.PROVIDED_CRON)) {
            OLESchedulerHelper oleSchedulerHelper = OLESchedulerHelper.getInstance();
            cronExpression = oleSchedulerHelper.getCronExpression(oleBatchProcessScheduleBo);
        } else {
            cronExpression = oleBatchProcessDefinitionDocument.getEnteredCronExp();
        }
        boolean validCronExpression = org.quartz.CronExpression.isValidExpression(cronExpression);
        if (!validCronExpression) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.ERROR_CRON_EXPRESSION);
            return;
        }
        oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo().setCronExpression(cronExpression);
        oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo().setWeekDays(oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo().getWeekDays());
        oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo().setBatchProcessType(oleBatchProcessDefinitionDocument.getBatchProcessType());
        List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBos = new ArrayList<OLEBatchProcessScheduleBo>();
        oleBatchProcessScheduleBos.add(oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo());
        oleBatchProcessDefinitionDocument.setOleBatchProcessScheduleBoList(oleBatchProcessScheduleBos);
    }

    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = null;
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        buildProcessDefinitionDocument(oleBatchProcessDefinitionDocument);
        oleBatchProcessDefinitionForm.setDocument(oleBatchProcessDefinitionDocument);
        oleBatchProcessDefinitionDocument.getDocumentHeader().setDocumentDescription(OLEConstants.OLEBatchProcess.NEW_BATCH_PRCS_DOCUMENT + oleBatchProcessDefinitionDocument.getDocumentNumber());
        boolean isValidated = getOleBatchProcessRule().batchValidations(oleBatchProcessDefinitionForm);
        if (!isValidated) {
            return getUIFModelAndView(oleBatchProcessDefinitionForm);
        }
        oleBatchProcessDefinitionDocument.setUser(GlobalVariables.getUserSession().getPrincipalName());
        if (!oleBatchProcessDefinitionDocument.isRunNowFlag()) {
            saveSchedule(oleBatchProcessDefinitionForm);
            boolean updateDocFlag = false;
            if (oleBatchProcessDefinitionDocument.getBatchProcessId() != null) {
                updateDocFlag = true;
                if (oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo() != null) {
                    rescheduleJob(oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo().getScheduleId(), oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo().getCronExpression());
                }
            }

            try {
                modelAndView = super.save(oleBatchProcessDefinitionForm, result, request, response);
                if (oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBoList() != null && oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBoList().size() > 0 && !updateDocFlag) {
                    if (oleBatchProcessDefinitionDocument.getBatchProcessType().equals(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) {
                        createBatchProcessSchedulerFile(oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBoList().get(0), oleBatchProcessDefinitionDocument.getMarcFile(), oleBatchProcessDefinitionDocument.getEdiFile(), oleBatchProcessDefinitionDocument.getBatchProcessType(), OLEConstants.OLEBatchProcess.PROFILE_SCHEDULE);
                    } else if ((oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT) && oleBatchProcessDefinitionDocument.getLoadIdFromFile().equalsIgnoreCase(String.valueOf(Boolean.TRUE))) || !oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT)) {
                        createBatchProcessSchedulerFile(oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBoList().get(0), oleBatchProcessDefinitionDocument.getIngestedFile(), null, oleBatchProcessDefinitionDocument.getBatchProcessType(), OLEConstants.OLEBatchProcess.PROFILE_SCHEDULE);
                    }
                }
            } catch (Exception e) {
                LOG.error("Error when scheduling the job :" + e);
            }
            OLEBatchProcessScheduleBo scheduleBo = ((OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument()).getOleBatchProcessScheduleBo();
            try {
                addToBatchScheduler(scheduleBo.getScheduleId(), scheduleBo.getCronExpression());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            oleBatchProcessDefinitionDocument.setRescheduleFlag(true);
            oleBatchProcessDefinitionDocument.setAfterSubmitFlag(true);
        }
        return modelAndView;
    }

    @Override
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        oleBatchProcessDefinitionDocument.getDocumentHeader().setDocumentDescription(OLEConstants.OLEBatchProcess.NEW_BATCH_PRCS_DOCUMENT + oleBatchProcessDefinitionDocument.getDocumentNumber());
        boolean isValidated = getOleBatchProcessRule().batchValidations(oleBatchProcessDefinitionForm);
        if (!isValidated) {
            return getUIFModelAndView(oleBatchProcessDefinitionForm);
        }
        return super.save(oleBatchProcessDefinitionForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=clear")
    public ModelAndView clear(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        oleBatchProcessDefinitionDocument.setScheduleFlag(false);
        oleBatchProcessDefinitionDocument.setBatchProcessName("");
        oleBatchProcessDefinitionDocument.setBatchProcessProfileName("");
        oleBatchProcessDefinitionDocument.setBatchProcessType("");
        oleBatchProcessDefinitionDocument.setBatchProcessKRMSProfile("");
        oleBatchProcessDefinitionDocument.setChunkSize(0);
        return getUIFModelAndView(oleBatchProcessDefinitionForm);
    }

    /**
     * Ads the scheduled record id as the jobName and sets the trigger for the cronExpression supplied
     * @param jobName
     * @param cronExpression
     */
    private void addToBatchScheduler(String jobName, String cronExpression) throws Exception {
        OLEBatchSchedulerService schedulerService = GlobalResourceLoader.getService("oleSchedulerService");
        schedulerService.initializeJobsForModule(jobName);
        schedulerService.initializeTriggersForModule(jobName, cronExpression);
    }

    /**
     * Runs the process for the profile selected and takes the scheduled record id as the job name
     *
     * @param jobName
     */
    private void runJobNow(String jobName) {
        OLEBatchSchedulerService schedulerService = GlobalResourceLoader.getService("oleSchedulerService");
        try {
            schedulerService.startJob(jobName);
        } catch (Exception e) {
            LOG.error("Error while starting job with job id :: "+jobName);
        }
    }

   /* private String getBatchProcessFilePath(String batchProceesType) {

        String batchProcessLocation = getOLEBatchProcessDataHelper().getBatchProcessFilePath(batchProceesType);
        return batchProcessLocation;
    }*/

    private void createBatchProcessJobFile(OLEBatchProcessJobDetailsBo batchProcessJobDetailsBo, MultipartFile ingestFile1, MultipartFile ingestFile2, String batchProceesType, String jobType) throws Exception {
        if (ingestFile2 == null) {
            String ingestFileName = batchProcessJobDetailsBo.getJobId() + jobType + "_" + ingestFile1.getOriginalFilename();
            getOLEBatchProcessDataHelper().createBatchProcessFile(batchProceesType, ingestFileName, new String(ingestFile1.getBytes()), batchProcessJobDetailsBo.getJobId());
        } else {

            String mrcFileName = batchProcessJobDetailsBo.getJobId() + jobType + "_" + ingestFile1.getOriginalFilename();
            String ediFileName = batchProcessJobDetailsBo.getJobId() + jobType + "_" + ingestFile2.getOriginalFilename();
            if(ediFileName != null){
                getOLEBatchProcessDataHelper().createBatchProcessFile(batchProceesType, mrcFileName, ediFileName, new String(ingestFile1.getBytes()), new String(ingestFile2.getBytes()) ,batchProcessJobDetailsBo.getJobId() );
            }
           /* else {
                getOLEBatchProcessDataHelper().createBatchProcessFile(batchProceesType, mrcFileName, new String(ingestFile1.getBytes()), new String(ingestFile2.getBytes()));
            }*/

        }

    }

    private void createBatchProcessJobFile(OLEBatchProcessJobDetailsBo batchProcessJobDetailsBo, MultipartFile ingestFile1, MultipartFile ingestFile2, MultipartFile ingestFile3, String batchProceesType, String jobType) throws Exception {
        if (ingestFile1 != null || ingestFile2 != null || ingestFile3 != null) {
            String documentFileName = ingestFile1 != null ? batchProcessJobDetailsBo.getJobId() + jobType + "_" + ingestFile1.getOriginalFilename() : null;
            String typeFileName = ingestFile2 != null ? batchProcessJobDetailsBo.getJobId() + jobType + "_" + ingestFile2.getOriginalFilename() : null;
            String historyFileName = ingestFile3 != null ? batchProcessJobDetailsBo.getJobId() + jobType + "_" + ingestFile3.getOriginalFilename() : null;
            getOLEBatchProcessDataHelper().createBatchProcessFile(batchProceesType, documentFileName, typeFileName, historyFileName, ingestFile1 !=null ? new String(ingestFile1.getBytes()) : null, ingestFile2 !=null ? new String(ingestFile2.getBytes()) : null, ingestFile3 !=null ? new String(ingestFile3.getBytes()) : null ,batchProcessJobDetailsBo.getJobId());
        }
    }

    private void createBatchProcessSchedulerFile(OLEBatchProcessScheduleBo oleBatchProcessScheduleBo, MultipartFile ingestFile1, MultipartFile ingestFile2, String batchProceesType, String jobType) throws Exception {
        if (ingestFile2 == null && ingestFile1!=null) {
            String ingestFileName = oleBatchProcessScheduleBo.getScheduleId() + jobType + "_" + ingestFile1.getOriginalFilename();
            getOLEBatchProcessDataHelper().createBatchProcessFile(batchProceesType, ingestFileName, new String(ingestFile1.getBytes()), oleBatchProcessScheduleBo.getScheduleId());
        }
        else  if (ingestFile2 != null && ingestFile1!=null) {
            String mrcFileName = oleBatchProcessScheduleBo.getScheduleId() + jobType + "_" + ingestFile1.getOriginalFilename();
            String ediFileName = oleBatchProcessScheduleBo.getScheduleId() + jobType + "_" + ingestFile2.getOriginalFilename();
            getOLEBatchProcessDataHelper().createBatchProcessFile(batchProceesType, mrcFileName, ediFileName, new String(ingestFile1.getBytes()), new String(ingestFile2.getBytes()) , oleBatchProcessScheduleBo.getScheduleId());

        }

    }

    @RequestMapping(params = "methodToCall=docHandler")
    public ModelAndView docHandler(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        String batchProcessId= request.getParameter("batchProcessId");
        ModelAndView modelAndView = super.docHandler(oleBatchProcessDefinitionForm,result,request,response);
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        batchProcessId= oleBatchProcessDefinitionDocument.getBatchProcessId();
        if(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT.equals(oleBatchProcessDefinitionDocument.getBatchProcessType()) && oleBatchProcessDefinitionDocument.getUploadFileName()!=null) {
            String[] fileNames = oleBatchProcessDefinitionDocument.getUploadFileName().split(",");
            if(fileNames.length == 2) {
                oleBatchProcessDefinitionDocument.setMarcFileName(fileNames[0]);
                oleBatchProcessDefinitionDocument.setEdiFileName(fileNames[1]);
            }
            else {
                oleBatchProcessDefinitionDocument.setMarcFileName(fileNames[0]);
            }
        }

        if(oleBatchProcessDefinitionDocument.getCronOrSchedule()!=null) {
            Map batchProcessMap = new HashMap();
            batchProcessMap.put("batchProcessId", batchProcessId);
            List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList = (List<OLEBatchProcessScheduleBo>) getBusinessObjectService().findMatching(OLEBatchProcessScheduleBo.class, batchProcessMap);
            if(oleBatchProcessScheduleBoList!=null && oleBatchProcessScheduleBoList.size() > 0){
                oleBatchProcessDefinitionDocument.setOleBatchProcessScheduleBo(oleBatchProcessScheduleBoList.get(0));
                oleBatchProcessDefinitionDocument.setScheduleFlag(true);
            }
        }
        if (oleBatchProcessDefinitionDocument != null && oleBatchProcessDefinitionDocument.getDocumentHeader() != null
                && oleBatchProcessDefinitionDocument.getDocumentHeader().getWorkflowDocument() != null
                && oleBatchProcessDefinitionDocument.getDocumentHeader().getWorkflowDocument().isSaved()) {
            oleBatchProcessDefinitionDocument.setAfterSubmitFlag(true);
        }
        return modelAndView;
    }

    @RequestMapping(params = "methodToCall=reschedule")
    public ModelAndView reschedule(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        String batchProcessId = request.getParameter("batchProcessId");
        ModelAndView modelAndView = null;
        if (batchProcessId != null) {
            Map batchProcessMap = new HashMap();
            batchProcessMap.put("batchProcessId", batchProcessId);
            List<OLEBatchProcessDefinitionDocument> oleBatchProcessDefinitionDocuments = (List<OLEBatchProcessDefinitionDocument>) getBusinessObjectService().findMatching(OLEBatchProcessDefinitionDocument.class, batchProcessMap);
            if (oleBatchProcessDefinitionDocuments.size() > 0) {
                oleBatchProcessDefinitionForm.setDocId(oleBatchProcessDefinitionDocuments.get(0).getDocumentNumber());
                modelAndView = super.docHandler(oleBatchProcessDefinitionForm, result, request, response);
                OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
                oleBatchProcessDefinitionDocument.setRescheduleFlag(false);
                if(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT.equals(oleBatchProcessDefinitionDocument.getBatchProcessType()) && oleBatchProcessDefinitionDocument.getUploadFileName()!=null) {
                    String[] fileNames = oleBatchProcessDefinitionDocument.getUploadFileName().split(",");
                    if(fileNames.length == 2) {
                        oleBatchProcessDefinitionDocument.setMarcFileName(fileNames[0]);
                        oleBatchProcessDefinitionDocument.setEdiFileName(fileNames[1]);
                    }
                    else {
                        oleBatchProcessDefinitionDocument.setMarcFileName(fileNames[0]);
                    }
                }
                if (oleBatchProcessDefinitionDocument.getCronOrSchedule() != null) {
                    List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList = (List<OLEBatchProcessScheduleBo>) getBusinessObjectService().findMatching(OLEBatchProcessScheduleBo.class, batchProcessMap);
                    if (oleBatchProcessScheduleBoList != null && oleBatchProcessScheduleBoList.size() > 0) {
                        oleBatchProcessDefinitionDocument.setOleBatchProcessScheduleBo(oleBatchProcessScheduleBoList.get(0));
                        oleBatchProcessDefinitionDocument.setScheduleFlag(true);
                    }
                }
            }
        }

        return modelAndView;
    }

    private void rescheduleJob(String scheduleId, String cronExp) {
        OLEBatchSchedulerService schedulerService = GlobalResourceLoader.getService("oleSchedulerService");
        try {
            schedulerService.rescheduleJob(scheduleId, cronExp);
        } catch (Exception e) {
            LOG.error("Error while removing job with schedule id :: " + scheduleId);
        }
    }

    @RequestMapping(params = "methodToCall=close")
    public ModelAndView close(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        return performRedirect(form, url, props);
    }

    @RequestMapping(params = "methodToCall=populateField")
    public ModelAndView populateField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;

        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        oleBatchProcessDefinitionForm.setBatchProcessType(oleBatchProcessDefinitionDocument.getProcessTypeHidden());
        oleBatchProcessDefinitionDocument.setBatchProcessType(oleBatchProcessDefinitionDocument.getProcessTypeHidden());
        oleBatchProcessDefinitionDocument.setBatchProcessProfileName(oleBatchProcessDefinitionDocument.getProcessNameHidden());

        buildProcessDefinitionDocument(oleBatchProcessDefinitionDocument);
        oleBatchProcessDefinitionForm.setDocument(oleBatchProcessDefinitionDocument);
        return super.refresh(form,result,request,response);
    }

    @RequestMapping(params = "methodToCall=refreshPageView")
    public ModelAndView refreshPageView(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

        return super.refresh(form,result,request,response);
    }


    @RequestMapping(params = "methodToCall=profileId")
    public ModelAndView profileId(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm = (OLEBatchProcessDefinitionForm) form;
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        buildProcessDefinitionDocument(oleBatchProcessDefinitionDocument);
        oleBatchProcessDefinitionForm.setDocument(oleBatchProcessDefinitionDocument);
        return super.refresh(form,result,request,response);
    }

    private void buildProcessDefinitionDocument(OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument) {
        if (OLEConstants.OLEBatchProcess.BATCH_BIB_IMPORT.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessBibImportProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        } else if (OLEConstants.OLEBatchProcess.BATCH_INVOICE.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessInvoiceImportProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        } else if (OLEConstants.OLEBatchProcess.BATCH_ORDER_IMPORT.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessOrderImportProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
            oleBatchProcessDefinitionDocument.setBatchProcessKRMSProfile(OLEBatchProcessOrderImportProfileValueFinder.getKrmsValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
            oleBatchProcessDefinitionDocument.setMarcOnly(OLEBatchProcessOrderImportProfileValueFinder.getMarcValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        } else if (OLEConstants.OLEBatchProcess.PATRON_IMPORT.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessPatronImportProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        } else if (OLEConstants.OLEBatchProcess.CLAIM_REPORT.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessClaimImportProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        } else if (OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessSerialRecordProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        } else if (OLEConstants.OLEBatchProcess.LOCATION_IMPORT.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessLocationImportProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        } else if (OLEConstants.OLEBatchProcess.BATCH_DELETE.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessBatchDeleteProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        } else if (OLEConstants.OLEBatchProcess.BATCH_EXPORT.equalsIgnoreCase(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            oleBatchProcessDefinitionDocument.setBatchProcessProfileId(OLEBatchProcessBatchExportProfileValueFinder.getValue(oleBatchProcessDefinitionDocument.getBatchProcessProfileName()));
        }
    }


}
