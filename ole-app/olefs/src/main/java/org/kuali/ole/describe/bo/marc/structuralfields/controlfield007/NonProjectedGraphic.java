package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'Non Projected Graphic'
 */
public class NonProjectedGraphic {
    private String specificMaterialDesignation = "u";
    private String undefined = "#";
    private String color = "u";
    private String primarySupportMaterial = "u";
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

    public String getPrimarySupportMaterial() {
        return primarySupportMaterial;
    }

    public void setPrimarySupportMaterial(String primarySupportMaterial) {
        this.primarySupportMaterial = primarySupportMaterial;
    }

    public String getSecondarySupportMaterial() {
        return secondarySupportMaterial;
    }

    public void setSecondarySupportMaterial(String secondarySupportMaterial) {
        this.secondarySupportMaterial = secondarySupportMaterial;
    }
}
