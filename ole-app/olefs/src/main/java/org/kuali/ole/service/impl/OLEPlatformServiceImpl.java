package org.kuali.ole.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.select.bo.OLEPlatformAdminUrl;
import org.kuali.ole.select.bo.OLEPlatformEventLog;
import org.kuali.ole.select.bo.OLESearchCondition;
import org.kuali.ole.select.document.OLEEResourceInstance;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.ole.select.gokb.OleGokbPlatform;
import org.kuali.ole.select.gokb.OleGokbTipp;
import org.kuali.ole.service.OLEPlatformService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.businessobject.VendorEventLog;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenchulakshmig on 9/19/14.
 */
public class OLEPlatformServiceImpl implements OLEPlatformService {

    private static final Logger LOG = Logger.getLogger(OLEPlatformServiceImpl.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.CREATED_DATE_FORMAT);

    private ConfigurationService getKualiConfigurationService() {
        return GlobalResourceLoader.getService("kualiConfigurationService");
    }

    @Override
    public List<OLEPlatformRecordDocument> performSearch(List<OLESearchCondition> oleSearchConditionsList) {
        boolean flag = true;
        Map<String, List<String>> searchCriteriaMap = new HashMap<>();
        List<OLEPlatformRecordDocument> platformRecordDocumentList = new ArrayList<>();
        List<OLEPlatformRecordDocument> platformList = new ArrayList<>();
        DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();
        docSearchCriteria.setDocumentTypeName(OLEConstants.OLE_PLATFORM_DOC);
        for (int searchCriteriaCnt = 0; searchCriteriaCnt < oleSearchConditionsList.size(); searchCriteriaCnt++) {
            if (StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy()) && StringUtils.isBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.SERACH_CRITERIA_REQUIRED);
            } else if (StringUtils.isBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy()) && StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.SERACH_BY_REQUIRED);
            } else if (!GlobalVariables.getMessageMap().hasMessages() && StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy()) && StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria())
                    && (OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator()) || OLEConstants.OLEEResourceRecord.OR.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator()))) {
                flag = false;
                if (searchCriteriaCnt != 0 && OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator())) {
                    if (!searchCriteriaMap.containsKey(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy())) {
                        searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria(), searchCriteriaMap);
                    } else {
                        searchCriteriaMap.clear();
                        break;
                    }
                } else {
                    searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria(), searchCriteriaMap);
                }
                if (searchCriteriaCnt < oleSearchConditionsList.size() - 1) {
                    if (StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy()) && !oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchCriteria().isEmpty()) {
                        if (OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator())) {
                            if (!searchCriteriaMap.containsKey(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy())) {
                                searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchCriteria(), searchCriteriaMap);
                                if (searchCriteriaCnt < oleSearchConditionsList.size() - 2 && oleSearchConditionsList.get(searchCriteriaCnt + 2).getSearchBy() != null && !oleSearchConditionsList.get(searchCriteriaCnt + 2).getSearchCriteria().isEmpty()) {
                                    if (OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt + 1).getOperator())) {
                                        searchCriteriaCnt++;
                                    }
                                } else {
                                    if (searchCriteriaMap.size() > 0) {
                                        platformList = findMatching(searchCriteriaMap, docSearchCriteria);
                                    }
                                    break;
                                }
                            } else {
                                searchCriteriaMap.clear();
                                break;
                            }
                        } else if (OLEConstants.OLEEResourceRecord.OR.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator())) {
                            if (searchCriteriaMap.size() > 0) {
                                platformRecordDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                                platformList.addAll(platformRecordDocumentList);
                                searchCriteriaMap.clear();
                            }
                            if (searchCriteriaCnt == oleSearchConditionsList.size() - 2) {
                                searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchCriteria(), searchCriteriaMap);
                                if (searchCriteriaMap.size() > 0) {
                                    platformRecordDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                                    platformList.addAll(platformRecordDocumentList);
                                    searchCriteriaMap.clear();
                                }
                                break;
                            }
                            if (OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt + 1).getOperator())) {
                                searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchCriteria(), searchCriteriaMap);
                                if (searchCriteriaMap.size() > 0) {
                                    platformRecordDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                                    platformList.addAll(platformRecordDocumentList);
                                }
                            }
                        }
                    } else {
                        if (!searchCriteriaMap.isEmpty()) {
                            if (searchCriteriaMap.size() > 0) {
                                platformRecordDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                                platformList.addAll(platformRecordDocumentList);
                                searchCriteriaMap.clear();
                            }
                        }
                    }
                }
                if (searchCriteriaCnt == oleSearchConditionsList.size() - 1) {
                    if (searchCriteriaMap.size() > 0) {
                        platformRecordDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                        platformList.addAll(platformRecordDocumentList);
                        searchCriteriaMap.clear();
                    }
                }
            }
        }
        if (flag && !GlobalVariables.getMessageMap().hasMessages()) {
            platformList = findMatching(searchCriteriaMap, docSearchCriteria);
        }
        if (platformList.size() > 0) {
            for (int searchCriteriaCnt = 0; searchCriteriaCnt < oleSearchConditionsList.size(); searchCriteriaCnt++) {
                if (oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy() != null && StringUtils.isNotEmpty(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy()) && !oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria().isEmpty() &&
                        (OLEConstants.OLEEResourceRecord.NOT.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator()))) {
                    searchCriteriaMap.clear();
                    searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria(), searchCriteriaMap);
                    if (searchCriteriaMap.size() > 0) {
                        platformRecordDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                    }
                    List<String> list = new ArrayList<String>();
                    for (OLEPlatformRecordDocument olePlatformRecordDocument : platformRecordDocumentList) {
                        int count = 0;
                        for (OLEPlatformRecordDocument platformRecordDocument : platformList) {
                            if (olePlatformRecordDocument.getDocumentNumber().toString().equalsIgnoreCase(platformRecordDocument.getDocumentNumber().toString())) {
                                list.add(count + "");
                            }
                            count++;
                        }
                    }
                    for (String str : list) {
                        platformList.remove(Integer.parseInt(str));
                    }
                }
            }
        }
        return platformList;
    }

    @Override
    public void getNewPlatformDoc(OLEPlatformRecordDocument tempDocument) {
        if (tempDocument != null) {
            if (tempDocument.getGeneralNotes().size() > 0) {
                KRADServiceLocator.getBusinessObjectService().delete(tempDocument.getGeneralNotes());
            }
            if (tempDocument.getVariantTitles().size() > 0) {
                KRADServiceLocator.getBusinessObjectService().delete(tempDocument.getVariantTitles());
            }
            if (tempDocument.getAdminUrls().size() > 0) {
                KRADServiceLocator.getBusinessObjectService().delete(tempDocument.getAdminUrls());
            }
            if (tempDocument.getEventLogs().size() > 0) {
                KRADServiceLocator.getBusinessObjectService().delete(tempDocument.getEventLogs());
            }
        }
    }

    @Override
    public List<OLEPlatformAdminUrl> saveUrls(List<OLEPlatformAdminUrl> olePlatformAdminUrls, OLEPlatformRecordDocument tempDocument) {
        List<OLEPlatformAdminUrl> olePlatformAdminUrlList = new ArrayList<>();
        for (OLEPlatformAdminUrl olePlatformAdminUrl : olePlatformAdminUrls) {
            if (olePlatformAdminUrl.isSaveFlag()) {
                olePlatformAdminUrlList.add(olePlatformAdminUrl);
            } else if (StringUtils.isNotBlank(olePlatformAdminUrl.getPlatformAdminUrlId()) && tempDocument != null) {
                for (OLEPlatformAdminUrl platformAdminUrl : tempDocument.getAdminUrls()) {
                    if (StringUtils.isNotBlank(platformAdminUrl.getPlatformAdminUrlId()) && platformAdminUrl.getPlatformAdminUrlId().equals(olePlatformAdminUrl.getPlatformAdminUrlId())) {
                        olePlatformAdminUrlList.add(platformAdminUrl);
                        break;
                    }
                }
            }
        }
        return olePlatformAdminUrlList;
    }

    @Override
    public List<OLEPlatformEventLog> saveEvents(List<OLEPlatformEventLog> olePlatformEventLogs, OLEPlatformRecordDocument tempDocument) {
        List<OLEPlatformEventLog> olePlatformEventLogList = new ArrayList<>();
        for (OLEPlatformEventLog olePlatformEventLog : olePlatformEventLogs) {
            if (olePlatformEventLog.isSaveFlag()) {
                olePlatformEventLogList.add(olePlatformEventLog);
            } else if (StringUtils.isNotBlank(olePlatformEventLog.getPlatformEventLogId()) && tempDocument != null) {
                for (OLEPlatformEventLog platformEventLog : tempDocument.getEventLogs()) {
                    if (StringUtils.isNotBlank(platformEventLog.getPlatformEventLogId()) && platformEventLog.getPlatformEventLogId().equals(olePlatformEventLog.getPlatformEventLogId())) {
                        olePlatformEventLogList.add(platformEventLog);
                        break;
                    }
                }
            }
        }
        return olePlatformEventLogList;
    }

    private Map<String, List<String>> getSearchCriteriaMap(String searchBy, String searchCriteria, Map<String, List<String>> searchCriteriaMap) {
        List<String> valueList = new ArrayList<>();
        valueList.add(searchCriteria);
        searchCriteriaMap.put(searchBy, valueList);
        return searchCriteriaMap;
    }

    private List<OLEPlatformRecordDocument> findMatching(Map<String, List<String>> map, DocumentSearchCriteria.Builder docSearchCriteria) {
        Map<String, List<String>> attributes = new HashMap<String, List<String>>();
        if (docSearchCriteria != null) {
            if (!map.isEmpty()) {
                for (String propertyField : map.keySet()) {
                    if (map.get(propertyField) != null) {
                        attributes.put(propertyField, map.get(propertyField));
                    }
                }
            }
        }
        docSearchCriteria.setDocumentAttributeValues(attributes);
        Date currentDate = new Date();
        docSearchCriteria.setDateCreatedTo(new DateTime(currentDate));
        DocumentSearchCriteria docSearchCriteriaDTO = docSearchCriteria.build();
        DocumentSearchResults components = null;
        components = KEWServiceLocator.getDocumentSearchService().lookupDocuments(GlobalVariables.getUserSession().getPrincipalId(), docSearchCriteriaDTO);
        List<DocumentSearchResult> docSearchResults = components.getSearchResults();
        OLEPlatformRecordDocument olePlatformRecordDocument;
        List<OLEPlatformRecordDocument> olePlatformRecordDocumentList = new ArrayList<>();
        if (!docSearchResults.isEmpty()) {
            for (DocumentSearchResult searchResult : docSearchResults) {
                olePlatformRecordDocument = new OLEPlatformRecordDocument();
                olePlatformRecordDocument.setResultDetails(searchResult, new ArrayList());
                if (olePlatformRecordDocument != null) {
                    olePlatformRecordDocumentList.add(olePlatformRecordDocument);
                }
            }
        }
        return olePlatformRecordDocumentList;
    }

    @Override
    public List<OLEPlatformEventLog> filterByReportedDate(Date beginDate, Date endDate, List<OLEPlatformEventLog> eventLogs) {
        List<OLEPlatformEventLog> olePlatformEventLogList = new ArrayList<>();
        try {
            String begin = null;
            if (beginDate != null) {
                begin = dateFormat.format(beginDate);
            }
            String end = null;
            if (endDate != null) {
                end = dateFormat.format(endDate);
            }
            boolean isValid = false;
            for (OLEPlatformEventLog eventLog : eventLogs) {
                Date eventReportedDate = eventLog.getEventReportedDate();
                OleLicenseRequestServiceImpl oleLicenseRequestService = GlobalResourceLoader.getService(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_SERVICE);
                isValid = oleLicenseRequestService.validateDate(eventReportedDate, begin, end);
                if (isValid) {
                    olePlatformEventLogList.add(eventLog);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return olePlatformEventLogList;
    }

    @Override
    public List<OLEPlatformEventLog> filterByResolvedDate(Date beginDate, Date endDate, List<OLEPlatformEventLog> eventLogs) {
        List<OLEPlatformEventLog> olePlatformEventLogList = new ArrayList<>();
        try {
            String begin = null;
            if (beginDate != null) {
                begin = dateFormat.format(beginDate);
            }
            String end = null;
            if (endDate != null) {
                end = dateFormat.format(endDate);
            }
            boolean isValid = false;
            for (OLEPlatformEventLog eventLog : eventLogs) {
                Date eventResolvedDate = eventLog.getEventResolvedDate();
                OleLicenseRequestServiceImpl oleLicenseRequestService = GlobalResourceLoader.getService(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_SERVICE);
                isValid = oleLicenseRequestService.validateDate(eventResolvedDate, begin, end);
                if (isValid) {
                    olePlatformEventLogList.add(eventLog);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return olePlatformEventLogList;
    }

    @Override
    public List<OLEPlatformEventLog> filterByStatus(List<OLEPlatformEventLog> eventLogs, String status) {
        List<OLEPlatformEventLog> olePlatformEventLogList = new ArrayList<>();
        for (OLEPlatformEventLog olePlatformEventLog : eventLogs) {
            if (StringUtils.isNotBlank(olePlatformEventLog.getEventStatus()) && olePlatformEventLog.getEventStatus().equals(status)) {
                olePlatformEventLogList.add(olePlatformEventLog);
            }
        }
        return olePlatformEventLogList;
    }

    @Override
    public List<OLEPlatformEventLog> filterByLogType(List<OLEPlatformEventLog> eventLogs, String logType) {
        List<OLEPlatformEventLog> olePlatformEventLogList = new ArrayList<>();
        for (OLEPlatformEventLog olePlatformEventLog : eventLogs) {
            if (StringUtils.isNotBlank(olePlatformEventLog.getLogTypeId()) && olePlatformEventLog.getLogTypeId().equals(logType)) {
                olePlatformEventLogList.add(olePlatformEventLog);
            }
        }
        return olePlatformEventLogList;
    }

    @Override
    public List<OLEPlatformEventLog> filterByEventType(List<OLEPlatformEventLog> eventLogs, String eventType) {
        List<OLEPlatformEventLog> olePlatformEventLogList = new ArrayList<>();
        for (OLEPlatformEventLog olePlatformEventLog : eventLogs) {
            if (olePlatformEventLog.getEventTypeId() != null && olePlatformEventLog.getEventTypeId().equals(eventType)) {
                olePlatformEventLogList.add(olePlatformEventLog);
            }
        }
        return olePlatformEventLogList;
    }

    @Override
    public List<OLEPlatformEventLog> filterByProblemType(List<OLEPlatformEventLog> eventLogs, String problemType) {
        List<OLEPlatformEventLog> olePlatformEventLogList = new ArrayList<>();
        for (OLEPlatformEventLog olePlatformEventLog : eventLogs) {
            if (StringUtils.isNotBlank(olePlatformEventLog.getProblemTypeId()) && olePlatformEventLog.getProblemTypeId().equals(problemType)) {
                olePlatformEventLogList.add(olePlatformEventLog);
            }
        }
        return olePlatformEventLogList;
    }

    @Override
    public boolean getGokbLinkFlag(String olePlatformId) {
        OLEPlatformRecordDocument tempDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OLEPlatformRecordDocument.class, olePlatformId);
        if (tempDocument != null) {
            if (tempDocument.getGokbId() != null) {
                Map map = new HashMap();
                map.put(OLEConstants.GOKB_ID, tempDocument.getGokbId());
                OleGokbPlatform oleGokbPlatform = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleGokbPlatform.class, tempDocument.getGokbId());
                if (oleGokbPlatform != null && oleGokbPlatform.getPlatformProviderId() != 0) {
                    map.clear();
                    map.put(OLEConstants.GOKB_ID, oleGokbPlatform.getPlatformProviderId());
                    List<VendorDetail> vendorDetailList = (List<VendorDetail>) KRADServiceLocator.getBusinessObjectService().findMatching(VendorDetail.class, map);
                    if (vendorDetailList != null && vendorDetailList.size() > 0) {
                        return true;
                    }
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(tempDocument.getOlePlatformId())) {
                    if (oleGokbPlatform != null) {
                        map.clear();
                        map.put("gokbPlatformId", oleGokbPlatform.getGokbPlatformId());
                        List<OleGokbTipp> oleGokbTipps = (List<OleGokbTipp>) KRADServiceLocator.getBusinessObjectService().findMatching(OleGokbTipp.class, map);
                        if (oleGokbTipps != null && oleGokbTipps.size() > 0) {
                            for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
                                map.clear();
                                map.put(OLEConstants.GOKB_ID, oleGokbTipp.getGokbTippId());
                                map.put(OLEConstants.PLATFORM_ID, tempDocument.getOlePlatformId());
                                List<OLEEResourceInstance> oleEResourceInstances = (List<OLEEResourceInstance>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEEResourceInstance.class, map);
                                if (oleEResourceInstances != null && oleEResourceInstances.size() > 0) {
                                    return true;
                                }
                            }
                        }
                    }

                }
            }
        }
        return false;
    }

    public void storeEventAttachments(MultipartFile attachmentFile) throws IOException {
        String location = "";
        location = getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.PLATFORM_EVENT_TMP_LOCATION;
        LOG.info("Event Attachment LOG :" + location);
        File dirLocation = new File(location);
        if (!dirLocation.exists()) {
            boolean success = dirLocation.mkdirs();
            if (!success) {
                LOG.error("Could not generate directory for File at: " + dirLocation.getAbsolutePath());
            }
        }
        location = location + File.separator + attachmentFile.getOriginalFilename();
        InputStream fileContents = attachmentFile.getInputStream();
        File fileOut = new File(location);
        FileOutputStream streamOut = null;
        BufferedOutputStream bufferedStreamOut = null;
        try {
            streamOut = new FileOutputStream(fileOut);
            bufferedStreamOut = new BufferedOutputStream(streamOut);
            int c;
            while ((c = fileContents.read()) != -1) {
                bufferedStreamOut.write(c);
            }
        } finally {
            bufferedStreamOut.close();
            streamOut.close();
        }
    }

    public void processEventAttachments(List<OLEPlatformEventLog> olePlatformEventLogs) {
        LOG.debug("Inside processEventAttachments method");
        List<OLEPlatformEventLog> eventAttachments = new ArrayList<OLEPlatformEventLog>();
        if (olePlatformEventLogs.size() > 0) {
            try {
                String location = "";
                location = getKualiConfigurationService().getPropertyValueAsString(
                        KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.PLATFORM_EVENT_TMP_LOCATION;
                LOG.info("Event Attachment LOG :" + location);
                for (OLEPlatformEventLog olePlatformEventLog : olePlatformEventLogs) {
                    if (StringUtils.isNotBlank(olePlatformEventLog.getAttachmentFileName1())) {
                        File file = new File(location + File.separator + olePlatformEventLog.getAttachmentFileName1());
                        olePlatformEventLog.setAttachmentContent1(FileUtils.readFileToByteArray(file));
                    }
                    if (StringUtils.isNotBlank(olePlatformEventLog.getAttachmentFileName2())) {
                        File file = new File(location + File.separator + olePlatformEventLog.getAttachmentFileName2());
                        olePlatformEventLog.setAttachmentContent2(FileUtils.readFileToByteArray(file));
                    }
                }
                for (OLEPlatformEventLog olePlatformEventLog : olePlatformEventLogs) {
                    if (StringUtils.isNotBlank(olePlatformEventLog.getAttachmentFileName1())) {
                        File file = new File(location + File.separator + olePlatformEventLog.getAttachmentFileName1());
                        file.delete();
                    }
                    if (StringUtils.isNotBlank(olePlatformEventLog.getAttachmentFileName2())) {
                        File file = new File(location + File.separator + olePlatformEventLog.getAttachmentFileName2());
                        file.delete();
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception while saving attachment" + e);
            }

        }
    }

    public boolean addAttachmentFile(OLEPlatformEventLog olePlatformEventLog, String sectionId) {
        boolean validFile = true;
        MultipartFile attachmentFile1 = olePlatformEventLog.getAttachmentFile1();
        if (attachmentFile1 != null && !org.apache.commons.lang.StringUtils.isBlank(attachmentFile1.getOriginalFilename())) {
            if (attachmentFile1.getSize() == 0) {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(sectionId,
                        RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, attachmentFile1.getOriginalFilename());
                validFile = false;
            } else {
                try {
                    olePlatformEventLog.setAttachmentFileName1(attachmentFile1.getOriginalFilename());
                    olePlatformEventLog.setAttachmentMimeType1(attachmentFile1.getContentType());
                    storeEventAttachments(attachmentFile1);
                } catch (Exception e) {
                    LOG.error("Exception while storing the Attachment Document" + e);
                }
            }
        }
        MultipartFile attachmentFile2 = olePlatformEventLog.getAttachmentFile2();
        if (attachmentFile2 != null && !org.apache.commons.lang.StringUtils.isBlank(attachmentFile2.getOriginalFilename())) {
            if (attachmentFile2.getSize() == 0) {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(sectionId,
                        RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, attachmentFile2.getOriginalFilename());
                validFile = false;
            } else {
                try {
                    olePlatformEventLog.setAttachmentFileName2(attachmentFile2.getOriginalFilename());
                    olePlatformEventLog.setAttachmentMimeType2(attachmentFile2.getContentType());
                    storeEventAttachments(attachmentFile2);
                } catch (Exception e) {
                    LOG.error("Exception while storing the Attachment Document" + e);
                }
            }
        }
        return validFile;
    }

    public void downloadAttachment(HttpServletResponse response, String eventLogId, String fileName, byte[] attachmentContent, String attachmentMimeType) throws Exception {
        File file;
        String location = "";
        location = getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.PLATFORM_EVENT_TMP_LOCATION;
        if (eventLogId == null) {
            file = new File(getKualiConfigurationService().getPropertyValueAsString(
                    KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.PLATFORM_EVENT_TMP_LOCATION +
                    File.separator + fileName);
            LOG.info("Uploaded file location : " + file.getAbsolutePath());
        } else {
            file = new File(location + File.separator + fileName);
            FileUtils.writeByteArrayToFile(file, attachmentContent);
        }
        response.setContentType(attachmentMimeType);
        response.setContentLength((int) file.length());
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"");
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(fis, response.getOutputStream());
    }

    @Override
    public StringBuffer validatePlatformRecordDocument(OLEPlatformRecordDocument olePlatformRecordDocument){
        StringBuffer errorMessage = new StringBuffer();
        if (!olePlatformRecordDocument.isActive() && olePlatformRecordDocument.getLinkedEResources().size() > 0) {
            olePlatformRecordDocument.setActive(true);
            errorMessage.append(olePlatformRecordDocument.getLinkedEResources().size() + " " + SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.ERROR_ERESOURCE_LINKED_TO_PLATFORM));
            errorMessage.append(OLEConstants.BREAK);
        }
        if (olePlatformRecordDocument.getGokbId()!=null) {
            Map gokbMap = new HashMap();
            gokbMap.put(OLEConstants.GOKB_ID, olePlatformRecordDocument.getGokbId());
            List<OLEPlatformRecordDocument> tempDocuments = (List<OLEPlatformRecordDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, gokbMap);
            if (tempDocuments != null && tempDocuments.size() > 0) {
                for (OLEPlatformRecordDocument tempDocument : tempDocuments) {
                    if (StringUtils.isBlank(olePlatformRecordDocument.getOlePlatformId()) || (StringUtils.isNotBlank(olePlatformRecordDocument.getOlePlatformId()) && !olePlatformRecordDocument.getOlePlatformId().equals(tempDocument.getOlePlatformId()))) {
                        errorMessage.append(" Platform record " + tempDocument.getOlePlatformId() + " " + SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.ERROR_PLATFORM_LINKED_TO_GOKB_ID));
                        errorMessage.append(OLEConstants.BREAK);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(olePlatformRecordDocument.getName())) {
            Map platformNameMap = new HashMap();
            platformNameMap.put(OLEConstants.PLATFORM_NAME, olePlatformRecordDocument.getName());
            List<OLEPlatformRecordDocument> tempDocuments = (List<OLEPlatformRecordDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, platformNameMap);
            if (tempDocuments != null && tempDocuments.size() > 0) {
                for (OLEPlatformRecordDocument tempDocument : tempDocuments) {
                    if (StringUtils.isBlank(olePlatformRecordDocument.getOlePlatformId()) || (StringUtils.isNotBlank(olePlatformRecordDocument.getOlePlatformId()) && !olePlatformRecordDocument.getOlePlatformId().equals(tempDocument.getOlePlatformId()))) {
                        errorMessage.append(" Platform record " + tempDocument.getOlePlatformId() + " " + SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.ERROR_PLATFORM_SAME_NAME));
                        errorMessage.append(OLEConstants.BREAK);
                    }

                }
            }
        }
        if (StringUtils.isNotBlank(olePlatformRecordDocument.getPlatformProviderName())) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.VENDOR_NAME, olePlatformRecordDocument.getPlatformProviderName());
            List<VendorDetail> vendorDetails = (List<VendorDetail>) KRADServiceLocator.getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
            if (vendorDetails != null && vendorDetails.size() > 0) {
                olePlatformRecordDocument.setVendorId(vendorDetails.get(0).getVendorNumber());
                olePlatformRecordDocument.setActiveVendor(vendorDetails.get(0).isActiveIndicator());
            } else {
                olePlatformRecordDocument.setVendorId(null);
                errorMessage.append("Invalid Platform Provider " + olePlatformRecordDocument.getPlatformProviderName());
                errorMessage.append(OLEConstants.BREAK);
            }
        }else{
            olePlatformRecordDocument.setVendorId(null);
        }
        return errorMessage;
    }

    @Override
    public OLEPlatformEventLog getFilterPlatformEventLog(OLEPlatformEventLog olePlatformEventLog, List<OLEPlatformEventLog> filterEventLogs) {
        if (StringUtils.isNotBlank(olePlatformEventLog.getPlatformEventLogId())) {
            for (OLEPlatformEventLog platformEventLog : filterEventLogs) {
                if (StringUtils.isNotBlank(platformEventLog.getPlatformEventLogId()) && platformEventLog.getPlatformEventLogId().equals(olePlatformEventLog.getPlatformEventLogId())) {
                    return platformEventLog;
                }
            }
        } else if (olePlatformEventLog.getEventTempId() != null) {
            for (OLEPlatformEventLog platformEventLog : filterEventLogs) {
                if (platformEventLog.getEventTempId() != null && platformEventLog.getEventTempId().equals(olePlatformEventLog.getEventTempId())) {
                    return platformEventLog;
                }
            }
        }
        return null;
    }

    @Override
    public void addVendorEventLog(OLEPlatformRecordDocument olePlatformRecordDocument) {
        if (olePlatformRecordDocument.getVendorHeaderGeneratedIdentifier() != null && olePlatformRecordDocument.getVendorDetailAssignedIdentifier() != null) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID, olePlatformRecordDocument.getVendorHeaderGeneratedIdentifier());
            vendorMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID, olePlatformRecordDocument.getVendorDetailAssignedIdentifier());
            VendorDetail vendor = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorMap);
            if (vendor != null) {
                String note = "This vendor is associated with the platform '" + olePlatformRecordDocument.getName() + "'";
                vendorMap.put("note", note);
                List<VendorEventLog> vendorEventLogList = (List<VendorEventLog>) KRADServiceLocator.getBusinessObjectService().findMatching(VendorEventLog.class, vendorMap);
                if (CollectionUtils.isEmpty(vendorEventLogList)) {
                    VendorEventLog vendorEventLog = new VendorEventLog();
                    vendorEventLog.setLogTypeId("3");
                    vendorEventLog.setDate(new Timestamp((new Date()).getTime()));
                    vendorEventLog.setUserId(GlobalVariables.getUserSession().getPrincipalId());
                    vendorEventLog.setNote(note);
                    vendor.getEventLogs().add(vendorEventLog);
                    KRADServiceLocator.getBusinessObjectService().save(vendor);
                }
            }
        }
    }

}
