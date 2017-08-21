package org.kuali.ole.deliver.notice.bo;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.alert.document.OlePersistableBusinessObjectBase;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.Iterator;
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
    private String noticeFooterBody;
    private String noticeSubjectLine;
    private boolean active;
    private List<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings = new ArrayList<OleNoticeFieldLabelMapping>();
    private List<OleNoticeFieldLabelMapping> oleNoticePatronFieldLabelMappings = new ArrayList<OleNoticeFieldLabelMapping>();
    private List<OleNoticeFieldLabelMapping> oleNoticeItemFieldLabelMappings = new ArrayList<OleNoticeFieldLabelMapping>();

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


    //TODO: Add a cache map to keep the labels from being checked everytime.
    public String getFieldLabel(String key) {
        if (CollectionUtils.isNotEmpty(oleNoticeFieldLabelMappings)) {
            for (Iterator<OleNoticeFieldLabelMapping> iterator = oleNoticeFieldLabelMappings.iterator(); iterator.hasNext(); ) {
                OleNoticeFieldLabelMapping oleNoticeFieldLabelMapping = iterator.next();
                if (oleNoticeFieldLabelMapping.getFieldName().equalsIgnoreCase(key)){
                    return oleNoticeFieldLabelMapping.getFieldLabel();
                }
            }
        }
        return null;
    }

    public String getNoticeFooterBody() {
        return noticeFooterBody;
    }

    public void setNoticeFooterBody(String noticeFooterBody) {
        this.noticeFooterBody = noticeFooterBody;
    }

    public List<OleNoticeFieldLabelMapping> getOleNoticePatronFieldLabelMappings() {
        return oleNoticePatronFieldLabelMappings;
    }

    public void setOleNoticePatronFieldLabelMappings(List<OleNoticeFieldLabelMapping> oleNoticePatronFieldLabelMappings) {
        this.oleNoticePatronFieldLabelMappings = oleNoticePatronFieldLabelMappings;
    }

    public List<OleNoticeFieldLabelMapping> getOleNoticeItemFieldLabelMappings() {
        return oleNoticeItemFieldLabelMappings;
    }

    public void setOleNoticeItemFieldLabelMappings(List<OleNoticeFieldLabelMapping> oleNoticeItemFieldLabelMappings) {
        this.oleNoticeItemFieldLabelMappings = oleNoticeItemFieldLabelMappings;
    }
}
