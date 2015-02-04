package org.kuali.ole.select.bo;

/**
 *
 * This class is used to hold the Package detail from GOKb service for displaying in UI.
 *
 */
public class OLEGOKbPackage {

    private Integer packageId;
    private String packageName;
    private String gokbStatus;
    private String oleStatus;
    private String primaryPlatform;
    private String primaryPlatformProvider;
    private int tiips;
    private String dateCreated;
    private String dateEntered;

    private boolean multiplePlatform = false;

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getGokbStatus() {
        return gokbStatus;
    }

    public void setGokbStatus(String gokbStatus) {
        this.gokbStatus = gokbStatus;
    }

    public String getOleStatus() {
        return oleStatus;
    }

    public void setOleStatus(String oleStatus) {
        this.oleStatus = oleStatus;
    }

    public String getPrimaryPlatform() {
        return primaryPlatform;
    }

    public void setPrimaryPlatform(String primaryPlatform) {
        this.primaryPlatform = primaryPlatform;
    }

    public String getPrimaryPlatformProvider() {
        return primaryPlatformProvider;
    }

    public void setPrimaryPlatformProvider(String primaryPlatformProvider) {
        this.primaryPlatformProvider = primaryPlatformProvider;
    }

    public int getTiips() {
        return tiips;
    }

    public void setTiips(int tiips) {
        this.tiips = tiips;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    public boolean isMultiplePlatform() {
        return multiplePlatform;
    }

    public void setMultiplePlatform(boolean multiplePlatform) {
        this.multiplePlatform = multiplePlatform;
    }
}
