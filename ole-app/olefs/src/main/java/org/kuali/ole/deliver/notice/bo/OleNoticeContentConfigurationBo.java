package org.kuali.ole.deliver.notice.bo;

import org.kuali.ole.alert.document.OlePersistableBusinessObjectBase;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 7/7/15.
 */
public class OleNoticeContentConfigurationBo extends OlePersistableBusinessObjectBase {
    private String oleNoticeContentConfigurationId;
    private String noticeType;
    private String noticeName;
    private String noticeTitle;
    private String noticeBody;
    private String noticeSubjectLine;
    private boolean active;
    private List<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<OleNoticeFieldLabelMapping>();

    public String getOleNoticeContentConfigurationId() {
        return oleNoticeContentConfigurationId;
    }

    public void setOleNoticeContentConfigurationId(String oleNoticeContentConfigurationId) {
        this.oleNoticeContentConfigurationId = oleNoticeContentConfigurationId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeBody() {
        return noticeBody;
    }

    public void setNoticeBody(String noticeBody) {
        this.noticeBody = noticeBody;
    }

    public String getNoticeSubjectLine() {
        return noticeSubjectLine;
    }

    public void setNoticeSubjectLine(String noticeSubjectLine) {
        this.noticeSubjectLine = noticeSubjectLine;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<OleNoticeFieldLabelMapping> getOleNoticeFieldLabelMappings() {
        return oleNoticeFieldLabelMappings;
    }

    public void setOleNoticeFieldLabelMappings(List<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings) {
        this.oleNoticeFieldLabelMappings = oleNoticeFieldLabelMappings;
    }
}
