package org.kuali.ole.batch.rule;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileMatchPoint;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.form.OLEBatchProcessDefinitionForm;
import org.kuali.ole.batch.helper.OLESchedulerHelper;
import org.kuali.ole.batch.util.BatchBibImportUtil;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.datadictionary.validation.fieldlevel.EmailAddressValidationPattern;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/30/13
 * Time: 3:48 PM
 * Class to perform validations on the Batch process screen
 */
public class OLEBatchProcessRule {

    Logger LOG = Logger.getLogger(OLEBatchProcessRule.class);



    public boolean batchValidations(OLEBatchProcessDefinitionForm oleBatchProcessDefinitionForm) {
        OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument = (OLEBatchProcessDefinitionDocument) oleBatchProcessDefinitionForm.getDocument();
        if (validateUser(GlobalVariables.getUserSession().getPrincipalId(), oleBatchProcessDefinitionDocument)) {
            if (StringUtils.isBlank(oleBatchProcessDefinitionDocument.getBatchProcessProfileName())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Profile Name");
            }
            else {
                if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) {
                    validateOrderRecordImport(oleBatchProcessDefinitionDocument);
                }
                else if (!oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT) && !oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.FUND_RECORD_IMPORT) && !oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT) && !oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.CLAIM_REPORT) && (oleBatchProcessDefinitionDocument.getIngestedFile() == null || oleBatchProcessDefinitionDocument.getIngestedFile().isEmpty())) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_FILE);
                }
                else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_BIB_IMPORT)) {
                    if (oleBatchProcessDefinitionDocument.getIngestedFile() != null && !oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".mrc")) {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.BATCH_BIB_IMPORT_INGEST_FILE_FORMAT);
                    }
                } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT) && oleBatchProcessDefinitionDocument.getLoadIdFromFile().equalsIgnoreCase("true")) {
                    if (oleBatchProcessDefinitionDocument.getIngestedFile() == null) {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.BATCH_EXPORT_INGEST_FILE_FORMAT);
                    } else if (oleBatchProcessDefinitionDocument.getIngestedFile() != null && !oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".txt")) {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.BATCH_EXPORT_INGEST_FILE_FORMAT);
                    }
                } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {
                    if (oleBatchProcessDefinitionDocument.getIngestedFile() != null && !(oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".txt")
                            || oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".mrc"))) {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.BATCH_DELETE_INGEST_FILE_FORMAT);
                    }
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("batchProcessProfileId", oleBatchProcessDefinitionDocument.getBatchProcessProfileId());
                    List<OLEBatchProcessProfileBo> oleBatchProcessProfileBos = (List<OLEBatchProcessProfileBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEBatchProcessProfileBo.class, map);
                    if (oleBatchProcessProfileBos != null && oleBatchProcessProfileBos.size() > 0) {
                        OLEBatchProcessProfileBo oleBatchProcessProfileBo = oleBatchProcessProfileBos.get(0);
                        List<OLEBatchProcessProfileMatchPoint> bibMatchPointList = BatchBibImportUtil.buildMatchPointListByDataType(oleBatchProcessProfileBo.getOleBatchProcessProfileMatchPointList(), org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode());
                        if (bibMatchPointList == null) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.BATCH_DELETE_MATCH_POINT);
                        } else if (bibMatchPointList.size() == 0) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.BATCH_DELETE_MATCH_POINT);
                        } else if (StringUtils.isEmpty(bibMatchPointList.get(0).getMatchPoint())) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.BATCH_DELETE_MATCH_POINT);
                        }

                    }
                } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.LOCATION_IMPORT)) {
                    if (oleBatchProcessDefinitionDocument.getIngestedFile() != null && !oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".xml")){
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.LOCATION_IMPORT_INGEST_FILE_FORMAT);
                    }
                } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.PATRON_IMPORT)) {
                    if (oleBatchProcessDefinitionDocument.getIngestedFile() != null && !oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".xml")){
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.PATRON_IMPORT_INGEST_FILE_FORMAT);
                    }
                } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.CLAIM_REPORT)){
                    //TODO validation for Claim Report
                } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT)){
                    if(oleBatchProcessDefinitionDocument.getInputFormat().equalsIgnoreCase("XMl")){
                        if (oleBatchProcessDefinitionDocument.getIngestedFile() != null){
                            if(oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile() == null && oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile() == null && oleBatchProcessDefinitionDocument.getSerialRecordTypeFile() == null){
                                if(!oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".xml")){
                                     clearFile(oleBatchProcessDefinitionDocument);
                                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.SERIAL_RECEIVING_IMPORT_INGEST_FILE_FORMAT);
                                }
                            } else{
                                clearFile(oleBatchProcessDefinitionDocument);
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.SERIAL_XML_CSV_FORMAT);
                            }
                        } else {
                            clearFile(oleBatchProcessDefinitionDocument);
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_FILE);
                        }

                    } else if(oleBatchProcessDefinitionDocument.getInputFormat().equalsIgnoreCase("CSV")){
                         if(oleBatchProcessDefinitionDocument.getIngestedFile() != null){
                             clearFile(oleBatchProcessDefinitionDocument);
                             GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.SERIAL_XML_CSV_FORMAT);
                         } else{
                             if (oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile() == null) {
                                 clearFile(oleBatchProcessDefinitionDocument);
                                 GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.RECORD_UPLOAD_CSV);
                             }
                             if(oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile()!=null){
                                 if(!oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile().getOriginalFilename().contains(".csv")){
                                     clearFile(oleBatchProcessDefinitionDocument);
                                     GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.SERIAL_REC_CSV);
                                 }else if(!oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile().getOriginalFilename().endsWith(getParameter(OLEConstants.OLEBatchProcess.SERIAL_RECORD_NAME)+".csv")){
                                     clearFile(oleBatchProcessDefinitionDocument);
                                     GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.RECORD_NAME_MISMATCH);
                                 }
                             }
                             if(oleBatchProcessDefinitionDocument.getSerialRecordTypeFile()!=null){
                                 if(!oleBatchProcessDefinitionDocument.getSerialRecordTypeFile().getOriginalFilename().contains(".csv")){
                                     clearFile(oleBatchProcessDefinitionDocument);
                                     GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.OLEBatchProcess.SERIAL_TYP_CSV);
                                 }
                                 else if(!oleBatchProcessDefinitionDocument.getSerialRecordTypeFile().getOriginalFilename().endsWith(getParameter(OLEConstants.OLEBatchProcess.SERIAL_TYPE_NAME)+".csv")){
                                     clearFile(oleBatchProcessDefinitionDocument);
                                     GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.RECORD_TYPE_NAME_MISMATCH);
                                 }
                             }
                             if(oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile()!=null){
                                 if(!oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile().getOriginalFilename().contains(".csv")){
                                     clearFile(oleBatchProcessDefinitionDocument);
                                     GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.SERIAL_HIS_CSV);
                                 }
                                 else if(!oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile().getOriginalFilename().endsWith(getParameter(OLEConstants.OLEBatchProcess.SERIAL_HISTORY_NAME)+".csv")){
                                     clearFile(oleBatchProcessDefinitionDocument);
                                     GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.OLEBatchProcess.RECORD_HISTORY_NAME_MISMATCH);
                                 }
                             }

                         }
                    }
/*                    if (oleBatchProcessDefinitionDocument.getIngestedFile() != null){
                        if(oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile() == null && oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile() == null && oleBatchProcessDefinitionDocument.getSerialRecordTypeFile() == null){
                        if(!oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".xml")){
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.SERIAL_RECEIVING_IMPORT_INGEST_FILE_FORMAT);
                        }
                        } else{
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Input file format should be either xml or csv");
                        }
                    }else if(oleBatchProcessDefinitionDocument.getIngestedFile()==null){
                        if(oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile()!=null){
                            if(!oleBatchProcessDefinitionDocument.getSerialRecordDocumentFile().getOriginalFilename().contains(".csv")){
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "it should be an csv file");
                            }
                        }
                        if(oleBatchProcessDefinitionDocument.getSerialRecordTypeFile()!=null){
                            if(!oleBatchProcessDefinitionDocument.getSerialRecordTypeFile().getOriginalFilename().contains(".csv")){
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "it should be an csv file");
                            }
                        }
                        if(oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile()!=null){
                            if(!oleBatchProcessDefinitionDocument.getSerialRecordHistoryFile().getOriginalFilename().contains(".csv")){
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "it should be an csv file");
                            }
                        }

                    }*/

                }
               else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.FUND_RECORD_IMPORT)){

                            if (oleBatchProcessDefinitionDocument.getFundRecordDocumentFile() == null) {
                                clearFile(oleBatchProcessDefinitionDocument);
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.FUND_RECORD_UPLOAD_CSV);
                            }
                            if(oleBatchProcessDefinitionDocument.getFundRecordDocumentFile() !=null){
                                if(!oleBatchProcessDefinitionDocument.getFundRecordDocumentFile().getOriginalFilename().contains(".csv")){
                                    clearFile(oleBatchProcessDefinitionDocument);
                                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.FUND_RECORD_TYPE_CSV);
                                }else if(!oleBatchProcessDefinitionDocument.getFundRecordDocumentFile().getOriginalFilename().endsWith(getParameter(OLEConstants.OLEBatchProcess.FUND_RECORD_NAME)+".csv")){
                                    clearFile(oleBatchProcessDefinitionDocument);
                                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.FUND_RECORD_NAME_MISMATCH);
                                }
                            }
                            if(oleBatchProcessDefinitionDocument.getFundAcctlnDocumentFile()!=null){
                                if(!oleBatchProcessDefinitionDocument.getFundAcctlnDocumentFile().getOriginalFilename().contains(".csv")){
                                    clearFile(oleBatchProcessDefinitionDocument);
                                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.OLEBatchProcess.FUND_ACCOUNT_RECORD_TYPE_CSV);
                                }
                                else if(!oleBatchProcessDefinitionDocument.getFundAcctlnDocumentFile().getOriginalFilename().endsWith(getParameter(OLEConstants.OLEBatchProcess.FUND_ACCOUNTING_LINE_RECORD_NAME)+".csv")){
                                    clearFile(oleBatchProcessDefinitionDocument);
                                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.FUND_ACCOUNT_RECORD_NAME_MISMATCH);
                                }
                            }
                    }
                }
            if(StringUtils.isNotBlank(oleBatchProcessDefinitionDocument.getEmailIds()) && !validateEmailIds(oleBatchProcessDefinitionDocument.getEmailIds())){
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.ERROR_EMAIL_ID);
            }
            if (oleBatchProcessDefinitionDocument.isScheduleFlag()) {
                String cronOrSchedule = oleBatchProcessDefinitionDocument.getCronOrSchedule();
                if (StringUtils.isNotBlank(cronOrSchedule)) {
                    if (oleBatchProcessDefinitionDocument.getCronOrSchedule().equals(OLEConstants.OLEBatchProcess.PROVIDED_CRON)) {
                        String cronExpression = oleBatchProcessDefinitionDocument.getEnteredCronExp();
                        if (StringUtils.isBlank(cronExpression)) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Cron Expression");
                        }
                        boolean validCronExpression = org.quartz.CronExpression.isValidExpression(cronExpression);
                        if (StringUtils.isNotBlank(cronExpression) && !validCronExpression) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.ERROR_CRON_EXPRESSION);
                        }
                    } else {
                        OLEBatchProcessScheduleBo oleBatchProcessScheduleBo = oleBatchProcessDefinitionDocument.getOleBatchProcessScheduleBo();
                        String oneTimeOrRecur = oleBatchProcessDefinitionDocument.getOneTimeOrRecurring();
                        if (StringUtils.isNotBlank(oneTimeOrRecur)) {
                            oleBatchProcessScheduleBo.setOneTimeOrRecurring(oneTimeOrRecur);
                            if (oneTimeOrRecur.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_ONETIME)) {
                                Date oneDate = oleBatchProcessScheduleBo.getOneTimeDate();
                                String oneTime = oleBatchProcessScheduleBo.getOneTimeStartTime();
                                if (oneDate == null) {
                                    GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Date");
                                }
                                if (StringUtils.isBlank(oneTime)) {
                                    GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Time");
                                }
                                if (StringUtils.isNotBlank(oneTime) && !validateTime(oneTime)) {
                                    GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE_TIME);
                                }
                                if(oneDate!=null && StringUtils.isNotEmpty(oneTime) && validateTime(oneTime) && isScheduleInValid(oleBatchProcessScheduleBo)){
                                    GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE_ERR);
                                }
                            }

                            if (oneTimeOrRecur.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_RECURRING)) {
                                String schduleType = oleBatchProcessDefinitionDocument.getScheduleType();
                                if (StringUtils.isNotBlank(schduleType)) {
                                    if (schduleType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_TYPE_DAILY)) {
                                        String startTimeDaily = oleBatchProcessScheduleBo.getStartTime();
                                        if (StringUtils.isBlank(startTimeDaily)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Start Time");
                                        }
                                        if (StringUtils.isNotBlank(startTimeDaily) && !validateTime(startTimeDaily)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE_TIME);
                                        }
                                    }
                                    if (schduleType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_TYPE_WEEKLY)) {
                                        String startTimeWeekly = oleBatchProcessScheduleBo.getStartTime();
                                        String weekDays = oleBatchProcessScheduleBo.getWeekDays();
                                        if (StringUtils.isBlank(startTimeWeekly)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Start Time");
                                        }
                                        if (StringUtils.isNotBlank(startTimeWeekly) && !validateTime(startTimeWeekly)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE_TIME);
                                        }
                                        if (StringUtils.isBlank(weekDays)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Week Day");
                                        }
                                    }
                                    if (schduleType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_TYPE_MONTHLY)) {
                                        String startTimeMonthly = oleBatchProcessScheduleBo.getStartTime();
                                        String dayNumberMonthly = oleBatchProcessScheduleBo.getDayNumber();
                                        String monthNumberMonthly = oleBatchProcessScheduleBo.getMonthNumber();
                                        if (StringUtils.isBlank(startTimeMonthly)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Start Time");
                                        }
                                        if (StringUtils.isNotBlank(startTimeMonthly) && !validateTime(startTimeMonthly)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE_TIME);
                                        }
                                        if (StringUtils.isBlank(dayNumberMonthly)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Day of Month");
                                        }
                                        if (StringUtils.isBlank(monthNumberMonthly)) {
                                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.OLE_BATCH_SCHEDULE, "Month Number");
                                        }
                                    }
                                } else {
                                    GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.DAILY_WEEKLY_MONTHLY);
                                }
                            }
                        } else {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.ONE_TIME_OR_RECUR_ERROR);
                        }
                    }
                } else {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BATCH_PROCESS_SCHEDULE_SECTION_ID, OLEConstants.OLEBatchProcess.CRON_OR_SCHEDULE);
                }
            }
        } else {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.ERROR_AUTHORIZATION);
        }
        int errorCount = GlobalVariables.getMessageMap().getErrorCount();
        if (errorCount > 0) {
            return false;
        } else {
            return true;
        }
    }



    private boolean validateTime(String time) {
        String TIME_24HR_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Matcher match = Pattern.compile(TIME_24HR_PATTERN).matcher(time);
        return match.matches();
    }

    public boolean validateUser(String principalId, OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument) {
        boolean hasPermission = true;
        if (StringUtils.isNotEmpty(oleBatchProcessDefinitionDocument.getBatchProcessType())) {
            if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_BIB_IMPORT)) {
                hasPermission = canPerformBatchImport(principalId);
            } else if(oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT)) {
                hasPermission = canPerformBatchExport(principalId);
            } else if (oleBatchProcessDefinitionDocument.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_DELETE)) {
                hasPermission = canPerformBatchDelete(principalId);
            }
        }
        return hasPermission;
    }

    private boolean validateEmailIds(String emailsIds){
        boolean validEmail=true;
        String[] emailsIdsArray = emailsIds.split(",");
        for(String emailId:emailsIdsArray){
            validEmail = validateEmailAddress(emailId.trim());
            if(!validEmail){
                return false;
            }
        }
        return validEmail;
    }

    /**
     * validate the email Address against the email address pattern
     *
     * @param emailAddress
     * @return true if email Address follows the pattern else return false.
     */
    private boolean validateEmailAddress(String emailAddress) {
        boolean valid = true;

        //perform the validation against email address
        if (StringUtils.isNotBlank(emailAddress)) {
            EmailAddressValidationPattern emailAddressPattern = new EmailAddressValidationPattern();
            if (!emailAddressPattern.matches(emailAddress)) {
                return false;
            }
        }
        return valid;
    }


    public boolean  canPerformBatchImport(String principalId){
        PermissionService service= KimApiServiceLocator.getPermissionService();
        return  service.hasPermission(principalId,OLEConstants.CAT_NAMESPACE,OLEConstants.BATCH_PROCESS_IMPORT);
    }

    public boolean  canPerformBatchExport(String principalId){
        PermissionService service= KimApiServiceLocator.getPermissionService();
        return  service.hasPermission(principalId,OLEConstants.CAT_NAMESPACE,OLEConstants.BATCH_PROCESS_EXPORT);
    }

    public boolean  canPerformBatchDelete(String principalId){
        PermissionService service= KimApiServiceLocator.getPermissionService();
        return  service.hasPermission(principalId,OLEConstants.CAT_NAMESPACE,OLEConstants.BATCH_PROCESS_DELETE);
    }

    private boolean isScheduleInValid(OLEBatchProcessScheduleBo scheduleBo) {
        String cronExp = OLESchedulerHelper.getInstance().getCronExpression(scheduleBo);
        CronExpression exp;
        try {
            exp = new CronExpression(cronExp);
        } catch (ParseException e) {
            LOG.error("Error while parsing the cron expression:: "+cronExp,e);
            return false;
        }
        Date date = exp.getNextValidTimeAfter(new Date());
        return date == null;
    }



    private void validateOrderRecordImport(OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument){
        if(oleBatchProcessDefinitionDocument.getMarcOnly()){
            if(oleBatchProcessDefinitionDocument.getMarcFile()== null){
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_MARC_FILE);
            }
        }
        else {
            if(oleBatchProcessDefinitionDocument.getMarcFile() == null && oleBatchProcessDefinitionDocument.getEdiFile() == null){
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_FILE);
            }
            else if(oleBatchProcessDefinitionDocument.getMarcFile() == null){
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_MARC_FILE);
            }
            else if(oleBatchProcessDefinitionDocument.getEdiFile() == null){
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_EDI_FILE);
            }
        }
        if(oleBatchProcessDefinitionDocument.getEdiFile() != null){
            if (oleBatchProcessDefinitionDocument.getMarcFile()!=null && !(oleBatchProcessDefinitionDocument.getMarcFile().getOriginalFilename().contains(".mrc") ||
                    oleBatchProcessDefinitionDocument.getMarcFile().getOriginalFilename().contains(".xml"))) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_MARC_FILE);
            }
            if (oleBatchProcessDefinitionDocument.getEdiFile()!=null && !(oleBatchProcessDefinitionDocument.getEdiFile().getOriginalFilename().contains(".edi") ||
                    oleBatchProcessDefinitionDocument.getEdiFile().getOriginalFilename().contains(".xml"))) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_EDI_FILE);
            }
        }
        else {
            if (oleBatchProcessDefinitionDocument.getIngestedFile()!=null && !(oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".mrc") ||
                    oleBatchProcessDefinitionDocument.getIngestedFile().getOriginalFilename().contains(".xml"))) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEBatchProcess.UPLOAD_MARC_FILE);
            }
        }
    }

    private String getParameter(String name) {
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", OLEConstants.SYS_NMSPC);
            criteriaMap.put("componentCode", OLEConstants.BATCH_CMPNT);
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>)KRADServiceLocator.getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return parameter;
    }

    private void clearFile(OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument ){
        oleBatchProcessDefinitionDocument.setIngestedFile(null);
        oleBatchProcessDefinitionDocument.setSerialRecordTypeFile(null);
        oleBatchProcessDefinitionDocument.setSerialRecordDocumentFile(null);
        oleBatchProcessDefinitionDocument.setSerialRecordHistoryFile(null);

    }
}

