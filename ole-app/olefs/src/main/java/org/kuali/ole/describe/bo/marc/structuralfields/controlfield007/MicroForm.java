package org.kuali.ole.describe.bo.marc.structuralfields.controlfield007;

/**
 * Class for handling control field 007 format 'MicroForm'
 */
public class MicroForm {
    private String specificMaterialDesignation = "u";
    private String undefined = "#";
    private String aspect = "u";
    private String dimensions = "u";
    private String reductionRatioRange = "u";
    private String reductionRatio = "|||";
    private String color = "u";
    private String emulsionOnFilm = "u";
    private String generation = "u";
    private String baseOfFilm = "u";

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

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getReductionRatioRange() {
        return reductionRatioRange;
    }

    public void setReductionRatioRange(String reductionRatioRange) {
        this.reductionRatioRange = reductionRatioRange;
    }

    public String getReductionRatio() {
        return reductionRatio;
    }

    public void setReductionRatio(String reductionRatio) {
        this.reductionRatio = reductionRatio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmulsionOnFilm() {
        return emulsionOnFilm;
    }

    public void setEmulsionOnFilm(String emulsionOnFilm) {
        this.emulsionOnFilm = emulsionOnFilm;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getBaseOfFilm() {
        return baseOfFilm;
    }

    public void setBaseOfFilm(String baseOfFilm) {
        this.baseOfFilm = baseOfFilm;
    }
}
