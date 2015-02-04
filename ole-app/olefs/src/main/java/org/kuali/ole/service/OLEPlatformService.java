package org.kuali.ole.service;

import org.kuali.ole.select.bo.OLEPlatformAdminUrl;
import org.kuali.ole.select.bo.OLEPlatformEventLog;
import org.kuali.ole.select.bo.OLESearchCondition;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by chenchulakshmig on 9/19/14.
 */
public interface OLEPlatformService {

    public List<OLEPlatformRecordDocument> performSearch(List<OLESearchCondition> oleSearchConditionsList);

    public void getNewPlatformDoc(String olePlatformId);

    public List<OLEPlatformAdminUrl> saveUrls(List<OLEPlatformAdminUrl> olePlatformAdminUrls);

    public List<OLEPlatformEventLog> saveEvents(List<OLEPlatformEventLog> olePlatformEventLogs);

    public List<OLEPlatformEventLog> filterByReportedDate(Date beginDate, Date endDate, List<OLEPlatformEventLog> eventLogs);

    public List<OLEPlatformEventLog> filterByResolvedDate(Date beginDate, Date endDate, List<OLEPlatformEventLog> eventLogs);

    public List<OLEPlatformEventLog> filterByStatus(List<OLEPlatformEventLog> eventLogs, String status);

    public List<OLEPlatformEventLog> filterByLogType(List<OLEPlatformEventLog> eventLogs, String logType);

    public List<OLEPlatformEventLog> filterByEventType(List<OLEPlatformEventLog> eventLogs, String eventType);

    public List<OLEPlatformEventLog> filterByProblemType(List<OLEPlatformEventLog> eventLogs, String problemType);

    public boolean getGokbLinkFlag(String olePlatformId);

    public void storeEventAttachments(MultipartFile attachmentFile) throws IOException;

    public void processEventAttachments(List<OLEPlatformEventLog> olePlatformEventLogs);

    public boolean addAttachmentFile(OLEPlatformEventLog olePlatformEventLog, String sectionId);

    public void downloadAttachment(HttpServletResponse response, String eventLogId, String fileName, byte[] attachmentContent, String attachmentMimeType) throws Exception;

}
