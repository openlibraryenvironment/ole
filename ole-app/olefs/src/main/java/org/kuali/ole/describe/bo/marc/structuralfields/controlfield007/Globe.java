package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'Globe'
 */
public class Globe {

    private String specificMaterialDesignation = "u";

    private String color = "c";
    private String physicalMedium = "u";
    private String typeOfReproduction = "n";
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

    public String getPhysicalMedium() {
        return physicalMedium;
    }

    public void setPhysicalMedium(String physicalMedium) {
        this.physicalMedium = physicalMedium;
    }

    public String getTypeOfReproduction() {
        return typeOfReproduction;
    }

    public void setTypeOfReproduction(String typeOfReproduction) {
        this.typeOfReproduction = typeOfReproduction;
    }
}
