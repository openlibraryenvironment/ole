package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'Sound Recording'
 */

public class SoundRecording {
    private String specificMaterialDesignation = "u";
    private String undefined = "#";
    private String speed = "u";
    private String ChannelConfiguration = "u";
    private String groove = "u";
    private String dimensions = "u";
    private String tapeWidth = "u";
    private String tapeConfiguration = "u";
    private String disc = "u";
    private String material = "u";
    private String cutting = "u";
    private String playbackCharacteristics = "u";
    private String storageTechnique = "u";

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

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getChannelConfiguration() {
        return ChannelConfiguration;
    }

    public void setChannelConfiguration(String channelConfiguration) {
        ChannelConfiguration = channelConfiguration;
    }

    public String getGroove() {
        return groove;
    }

    public void setGroove(String groove) {
        this.groove = groove;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getTapeWidth() {
        return tapeWidth;
    }

    public void setTapeWidth(String tapeWidth) {
        this.tapeWidth = tapeWidth;
    }

    public String getTapeConfiguration() {
        return tapeConfiguration;
    }

    public void setTapeConfiguration(String tapeConfiguration) {
        this.tapeConfiguration = tapeConfiguration;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCutting() {
        return cutting;
    }

    public void setCutting(String cutting) {
        this.cutting = cutting;
    }

    public String getPlaybackCharacteristics() {
        return playbackCharacteristics;
    }

    public void setPlaybackCharacteristics(String playbackCharacteristics) {
        this.playbackCharacteristics = playbackCharacteristics;
    }

    public String getStorageTechnique() {
        return storageTechnique;
    }

    public void setStorageTechnique(String storageTechnique) {
        this.storageTechnique = storageTechnique;
    }
}
