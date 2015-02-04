package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'Tactile Material'
 */
public class TactileMaterial {

    private String specificMaterialDesignation = "u";
    private String undefined = "#";
    private String brailleWriting = "u";
    private String levelOfContraction = "u";
    private String brailleMusicFormat = "u";
    private String specialPhysicalCharacteristics = "u";

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

    public String getBrailleWriting() {
        return brailleWriting;
    }

    public void setBrailleWriting(String brailleWriting) {
        this.brailleWriting = brailleWriting;
    }

    public String getLevelOfContraction() {
        return levelOfContraction;
    }

    public void setLevelOfContraction(String levelOfContraction) {
        this.levelOfContraction = levelOfContraction;
    }

    public String getBrailleMusicFormat() {
        return brailleMusicFormat;
    }

    public void setBrailleMusicFormat(String brailleMusicFormat) {
        this.brailleMusicFormat = brailleMusicFormat;
    }

    public String getSpecialPhysicalCharacteristics() {
        return specialPhysicalCharacteristics;
    }

    public void setSpecialPhysicalCharacteristics(String specialPhysicalCharacteristics) {
        this.specialPhysicalCharacteristics = specialPhysicalCharacteristics;
    }
}
