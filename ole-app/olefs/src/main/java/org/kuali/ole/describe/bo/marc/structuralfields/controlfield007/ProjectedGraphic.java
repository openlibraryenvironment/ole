package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'Projected Graphic'
 */
public class ProjectedGraphic {
    private String specificMaterialDesignation = "u";
    private String undefined = "#";
    private String color = "u";
    private String baseOfEmulsion = "u";
    private String soundOnMedium = "u";
    private String mediumForSound = "u";
    private String dimensions = "u";
    private String secondarySupportMaterial = "u";

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

    public String getBaseOfEmulsion() {
        return baseOfEmulsion;
    }

    public void setBaseOfEmulsion(String baseOfEmulsion) {
        this.baseOfEmulsion = baseOfEmulsion;
    }

    public String getSoundOnMedium() {
        return soundOnMedium;
    }

    public void setSoundOnMedium(String soundOnMedium) {
        this.soundOnMedium = soundOnMedium;
    }

    public String getMediumForSound() {
        return mediumForSound;
    }

    public void setMediumForSound(String mediumForSound) {
        this.mediumForSound = mediumForSound;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getSecondarySupportMaterial() {
        return secondarySupportMaterial;
    }

    public void setSecondarySupportMaterial(String secondarySupportMaterial) {
        this.secondarySupportMaterial = secondarySupportMaterial;
    }
}
