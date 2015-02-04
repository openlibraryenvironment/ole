package org.kuali.ole.select.gokb;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEPlatformStatus;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by premkumarv on 12/8/14.
 */
public class OleGokbPlatform extends PersistableBusinessObjectBase {

    private Integer gokbPlatformId;
    private String platformName;
    private String status;
    private String statusId;
    private Integer platformProviderId=0;
    private String authentication;
    private String softwarePlatform;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;



    public Integer getGokbPlatformId() {
        return gokbPlatformId;
    }

    public void setGokbPlatformId(Integer gokbPlatformId) {
        this.gokbPlatformId = gokbPlatformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusId() {
        if (StringUtils.isNotBlank(this.getStatus())){
            Map statusMap = new HashMap();
            statusMap.put(OLEConstants.PLATFORM_STATUS_NAME, this.getStatus());
            List<OLEPlatformStatus> platformStatusList = (List<OLEPlatformStatus>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEPlatformStatus.class, statusMap);
            if (platformStatusList != null && platformStatusList.size() > 0) {
                return platformStatusList.get(0).getPlatformStatusId();
            }
        }
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Integer getPlatformProviderId() {
        return platformProviderId;
    }

    public void setPlatformProviderId(Integer platformProviderId) {
        this.platformProviderId = platformProviderId;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getSoftwarePlatform() {
        return softwarePlatform;
    }

    public void setSoftwarePlatform(String softwarePlatform) {
        this.softwarePlatform = softwarePlatform;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
