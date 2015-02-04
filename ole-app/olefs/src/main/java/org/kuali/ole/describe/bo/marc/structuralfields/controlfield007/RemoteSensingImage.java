package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'Remote Sensing Image'
 */
public class RemoteSensingImage {
    private String specificMaterialDesignation = "u";
    private String undefined = "#";
    private String altitudeOfSensor = "u";
    private String attitudeOfSensor = "u";
    private String cloudCover = "u";
    private String constructionType = "u";
    private String category = "u";
    private String sensorType = "u";
    private String dataType ="uu";

    public String getSpecificMaterialDesignation() {
        return specificMaterialDesignation;
    }

    public void setSpecificMaterialDesignation(String specificMaterialDesignation) {
        this.specificMaterialDesignation = specificMaterialDesignation;
    }

    public String getUndefined() {
        return undefined;
    }

    public void setUndefined(String undefined) {
        this.undefined = undefined;
    }

    public String getAltitudeOfSensor() {
        return altitudeOfSensor;
    }

    public void setAltitudeOfSensor(String altitudeOfSensor) {
        this.altitudeOfSensor = altitudeOfSensor;
    }

    public String getAttitudeOfSensor() {
        return attitudeOfSensor;
    }

    public void setAttitudeOfSensor(String attitudeOfSensor) {
        this.attitudeOfSensor = attitudeOfSensor;
    }

    public String getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(String cloudCover) {
        this.cloudCover = cloudCover;
    }

    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
