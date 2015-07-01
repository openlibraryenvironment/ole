package org.kuali.ole.deliver.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pvsubrah on 6/8/15.
 */
public class NoticeInfo {
    private String loanType;
    private Map<String, Map<String,Object>> noticeInfoForTypeMap;

    public void addInfo(String noticeType, String noticeParameter, String noticeValue) {
        if (null == noticeInfoForTypeMap) {
            noticeInfoForTypeMap = new HashMap<>();
        }

        Map<String, Object> stringObjectMap;
        if(noticeInfoForTypeMap.containsKey(noticeType)){
            stringObjectMap = noticeInfoForTypeMap.get(noticeType);
        } else {
            stringObjectMap = new HashMap<>();
        }
        stringObjectMap.put(noticeParameter, noticeValue);
        noticeInfoForTypeMap.put(noticeType, stringObjectMap);
    }

    public Map<String, Map<String, Object>> getNoticeInfoForTypeMap() {
        return noticeInfoForTypeMap;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}
