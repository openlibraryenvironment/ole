package org.kuali.ole.describe.bo.marc.structuralfields.controlfield006;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 6/24/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class VisualMaterials {

    private String runningTime = "###";
    private String undefined = "#";
    private String undefinedPos5 = "#####";
    private String undefinedPos3 = "###";
    private String targetAudience = "#";
    private String governmentPublication = "#";
    private String formOfItem = "#";
    private String typeOfVisualMaterial = "|";
    private String technique = "n";

    public String getUndefinedPos3() {
        return undefinedPos3;
    }

    public void setUndefinedPos3(String undefinedPos3) {
        this.undefinedPos3 = undefinedPos3;
    }

    public String getUndefinedPos5() {
        return undefinedPos5;
    }

    public void setUndefinedPos5(String undefinedPos5) {
        this.undefinedPos5 = undefinedPos5;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    public String getUndefined() {
        return undefined;
    }

    public void setUndefined(String undefined) {
        this.undefined = undefined;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public String getGovernmentPublication() {
        return governmentPublication;
    }

    public void setGovernmentPublication(String governmentPublication) {
        this.governmentPublication = governmentPublication;
    }

    public String getFormOfItem() {
        return formOfItem;
    }

    public void setFormOfItem(String formOfItem) {
        this.formOfItem = formOfItem;
    }

    public String getTypeOfVisualMaterial() {
        return typeOfVisualMaterial;
    }

    public void setTypeOfVisualMaterial(String typeOfVisualMaterial) {
        this.typeOfVisualMaterial = typeOfVisualMaterial;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }
}
