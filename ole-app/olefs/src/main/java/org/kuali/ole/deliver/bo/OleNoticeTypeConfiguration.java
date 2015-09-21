package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by angelind on 9/9/15.
 */
public class OleNoticeTypeConfiguration extends PersistableBusinessObjectBase {

    private Integer noticeTypeConfigId;
    private String circPolicyId;
    private String noticeType;

    public Integer getNoticeTypeConfigId() {
        return noticeTypeConfigId;
    }

    public void setNoticeTypeConfigId(Integer noticeTypeConfigId) {
        this.noticeTypeConfigId = noticeTypeConfigId;
    }

    public String getCircPolicyId() {
        return circPolicyId;
    }

    public void setCircPolicyId(String circPolicyId) {
        this.circPolicyId = circPolicyId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
}
