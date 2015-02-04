package org.kuali.ole.describe.bo.marc.structuralfields.controlfield006;

/**
 * Class for handling format 'Books'
 */
public class Books {
    private String illustrations = "#";
    private String targetAudience = "#";
    private String formOfItem = "#";
    private String natureOfContents = "#";
    private String govtPublications = "#";
    private String confPublications = "0";
    private String festschrift = "0";
    private String index = "0";
    private String undefined = "#";
    private String literaryForm = "0";
    private String biography = "#";

    public String getIllustrations() {
        return illustrations;
    }

    public void setIllustrations(String illustrations) {
        this.illustrations = illustrations;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public String getFormOfItem() {
        return formOfItem;
    }

    public void setFormOfItem(String formOfItem) {
        this.formOfItem = formOfItem;
    }

    public String getNatureOfContents() {
        return natureOfContents;
    }

    public void setNatureOfContents(String natureOfContents) {
        this.natureOfContents = natureOfContents;
    }

    public String getGovtPublications() {
        return govtPublications;
    }

    public void setGovtPublications(String govtPublications) {
        this.govtPublications = govtPublications;
    }

    public String getConfPublications() {
        return confPublications;
    }

    public void setConfPublications(String confPublications) {
        this.confPublications = confPublications;
    }

    public String getFestschrift() {
        return festschrift;
    }

    public void setFestschrift(String festschrift) {
        this.festschrift = festschrift;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getUndefined() {
        return undefined;
    }

    public void setUndefined(String undefined) {
        this.undefined = undefined;
    }

    public String getLiteraryForm() {
        return literaryForm;
    }

    public void setLiteraryForm(String literaryForm) {
        this.literaryForm = literaryForm;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
