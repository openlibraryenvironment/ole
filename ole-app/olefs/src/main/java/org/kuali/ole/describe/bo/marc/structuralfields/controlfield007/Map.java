package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'Map'
 */
public class Map {
    private String materialDesignation = "u";
    private String undefined = "#";
    private String color = "a";
    private String physicalMedium = "u";
    private String typeOfReproduction = "n";
    private String productionDetails = "u";
    private String positiveOrNegativeAspect = "n";

    public String getMaterialDesignation() {
        return materialDesignation;
    }

    public void setMaterialDesignation(String materialDesignation) {
        this.materialDesignation = materialDesignation;
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

    public String getProductionDetails() {
        return productionDetails;
    }

    public void setProductionDetails(String productionDetails) {
        this.productionDetails = productionDetails;
    }

    public String getPositiveOrNegativeAspect() {
        return positiveOrNegativeAspect;
    }

    public void setPositiveOrNegativeAspect(String positiveOrNegativeAspect) {
        this.positiveOrNegativeAspect = positiveOrNegativeAspect;
    }
}
