package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'VideoRecording'
 */
public class VideoRecording {
    private String specificMaterialDesignation = "u";
    private String undefined = "#";
    private String color = "u";
    private String videoRecordingFormat = "u";
    private String soundOnMedium = "u";
    private String mediumOfSound = "u";
    private String dimensions = "u";
    private String configuration = "u";

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVideoRecordingFormat() {
        return videoRecordingFormat;
    }

    public void setVideoRecordingFormat(String videoRecordingFormat) {
        this.videoRecordingFormat = videoRecordingFormat;
    }

    public String getSoundOnMedium() {
        return soundOnMedium;
    }

    public void setSoundOnMedium(String soundOnMedium) {
        this.soundOnMedium = soundOnMedium;
    }

    public String getMediumOfSound() {
        return mediumOfSound;
    }

    public void setMediumOfSound(String mediumOfSound) {
        this.mediumOfSound = mediumOfSound;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

}
