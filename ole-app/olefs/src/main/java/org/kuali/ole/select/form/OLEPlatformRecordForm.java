package org.kuali.ole.select.form;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

import java.sql.Timestamp;

/**
 * Created by chenchulakshmig on 9/10/14.
 * OLEPlatformRecordForm is the Form class for Platform Record.
 */
public class OLEPlatformRecordForm extends TransactionalDocumentFormBase {

    private String message;

    private boolean problemFlag;

    private int selectedIndex;

    private Timestamp filterReportedBeginDate;

    private Timestamp filterReportedEndDate;

    private Timestamp filterResolvedBeginDate;

    private Timestamp filterResolvedEndDate;

    private String logType;

    private String eventType;

    private String problemType;

    private String status;

    private Integer tempId;

    private boolean filterEventLog;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return OLEConstants.OLE_PLATFORM_DOC;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public boolean isProblemFlag() {
        return problemFlag;
    }

    public void setProblemFlag(boolean problemFlag) {
        this.problemFlag = problemFlag;
    }

    public Timestamp getFilterReportedBeginDate() {
        return filterReportedBeginDate;
    }

    public void setFilterReportedBeginDate(Timestamp filterReportedBeginDate) {
        this.filterReportedBeginDate = filterReportedBeginDate;
    }

    public Timestamp getFilterReportedEndDate() {
        return filterReportedEndDate;
    }

    public void setFilterReportedEndDate(Timestamp filterReportedEndDate) {
        this.filterReportedEndDate = filterReportedEndDate;
    }

    public Timestamp getFilterResolvedBeginDate() {
        return filterResolvedBeginDate;
    }

    public void setFilterResolvedBeginDate(Timestamp filterResolvedBeginDate) {
        this.filterResolvedBeginDate = filterResolvedBeginDate;
    }

    public Timestamp getFilterResolvedEndDate() {
        return filterResolvedEndDate;
    }

    public void setFilterResolvedEndDate(Timestamp filterResolvedEndDate) {
        this.filterResolvedEndDate = filterResolvedEndDate;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public boolean isFilterEventLog() {
        return filterEventLog;
    }

    public void setFilterEventLog(boolean filterEventLog) {
        this.filterEventLog = filterEventLog;
    }
}
