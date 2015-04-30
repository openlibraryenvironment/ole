package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to hold the Platform detail from GOKb service for displaying in UI.
 */
public class OLEGOKbPlatform extends PersistableBusinessObjectBase {

    private List<OLEGOKbTIPP> goKbTIPPList = new ArrayList<>();
    private String platformName;
    private String label;
    private String platformProvider;
    private Integer platformProviderId;
    private Integer noOfTiips;
    private String softwarePlatform;
    private Integer platformId;
    private String status;

    private boolean select;

    private List<OLEGOKbTIPP> selectedGokbTippList;

    public List<OLEGOKbTIPP> getSelectedGokbTippList() {
        return selectedGokbTippList;
    }

    public void setSelectedGokbTippList(List<OLEGOKbTIPP> selectedGokbTippList) {
        this.selectedGokbTippList = selectedGokbTippList;
    }

    public List<OLEGOKbTIPP> getGoKbTIPPList() {
        return goKbTIPPList;
    }

    public void setGoKbTIPPList(List<OLEGOKbTIPP> goKbTIPPList) {
        this.goKbTIPPList = goKbTIPPList;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
        this.label = "select All Tipps from " + platformName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }


    public String getPlatformProvider() {
        return platformProvider;
    }

    public void setPlatformProvider(String platformProvider) {
        this.platformProvider = platformProvider;
    }

    public Integer getPlatformProviderId() {
        return platformProviderId;
    }

    public void setPlatformProviderId(Integer platformProviderId) {
        this.platformProviderId = platformProviderId;
    }

    public Integer getNoOfTiips() {
        return noOfTiips;
    }

    public void setNoOfTiips(Integer noOfTiips) {
        this.noOfTiips = noOfTiips;
    }

    public String getSoftwarePlatform() {
        return softwarePlatform;
    }

    public void setSoftwarePlatform(String softwarePlatform) {
        this.softwarePlatform = softwarePlatform;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
