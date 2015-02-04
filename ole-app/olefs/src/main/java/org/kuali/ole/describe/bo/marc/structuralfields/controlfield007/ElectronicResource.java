package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'Electronic Resources'
 */
public class ElectronicResource {

    private String specificMaterialDesignation = "u";
    private String color = "|";
    private String dimensions = "|";
    private String sound = "#";
    private String imageBitDepth = "|||";
    private String fileFormats = "u";
    private String qualityAssuranceTargets = "u";
    private String source = "u";
    private String levelOfCompression = "u";
    private String reformattingQuality = "u";
    private String undefined= "#";

    public String getUndefined() {
        return undefined;
    }

    public void setUndefined(String undefined) {
        this.undefined = undefined;
    }

    public String getSpecificMaterialDesignation() {
        return specificMaterialDesignation;
    }

    public void setSpecificMaterialDesignation(String specificMaterialDesignation) {
        this.specificMaterialDesignation = specificMaterialDesignation;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getImageBitDepth() {
        return imageBitDepth;
    }

    public void setImageBitDepth(String imageBitDepth) {
        this.imageBitDepth = imageBitDepth;
    }

    public String getFileFormats() {
        return fileFormats;
    }

    public void setFileFormats(String fileFormats) {
        this.fileFormats = fileFormats;
    }

    public String getQualityAssuranceTargets() {
        return qualityAssuranceTargets;
    }

    public void setQualityAssuranceTargets(String qualityAssuranceTargets) {
        this.qualityAssuranceTargets = qualityAssuranceTargets;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLevelOfCompression() {
        return levelOfCompression;
    }

    public void setLevelOfCompression(String levelOfCompression) {
        this.levelOfCompression = levelOfCompression;
    }

    public String getReformattingQuality() {
        return reformattingQuality;
    }

    public void setReformattingQuality(String reformattingQuality) {
        this.reformattingQuality = reformattingQuality;
    }
}
