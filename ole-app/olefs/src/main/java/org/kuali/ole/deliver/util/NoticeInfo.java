package org.kuali.ole.deliver.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pvsubrah on 6/8/15.
 */
public class NoticeInfo {
    private String noticeType;
    private Map<String, Map<String,Object>> noticeInfoForTypeMap;
    private String intervalType;

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

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(String intervalType) {
        this.intervalType = intervalType;
    }
}
