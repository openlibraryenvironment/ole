package org.kuali.ole.describe.bo.marc.structuralfields.controlfield008;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 6/20/13
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class Music {

    private String formOfComposition = "uu";
    private String formatOfMusic = "u";
    private String musicParts = "#";
    private String accompanyingMatter = "#";
    private String literaryText = "#";
    private String transpositionAndArrangement = "#";
    private String targetAudience = "#";
    private String formOfItem = "#";
    private String undefinedPos1 = "#";
    private String undefinedPos2 = "#";


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

    public String getFormatOfMusic() {
        return formatOfMusic;
    }

    public void setFormatOfMusic(String formatOfMusic) {
        this.formatOfMusic = formatOfMusic;
    }

    public String getMusicParts() {
        return musicParts;
    }

    public void setMusicParts(String musicParts) {
        this.musicParts = musicParts;
    }

    public String getAccompanyingMatter() {
        return accompanyingMatter;
    }

    public void setAccompanyingMatter(String accompanyingMatter) {
        this.accompanyingMatter = accompanyingMatter;
    }

    public String getLiteraryText() {
        return literaryText;
    }

    public void setLiteraryText(String literaryText) {
        this.literaryText = literaryText;
    }

    public String getTranspositionAndArrangement() {
        return transpositionAndArrangement;
    }

    public void setTranspositionAndArrangement(String transpositionAndArrangement) {
        this.transpositionAndArrangement = transpositionAndArrangement;
    }

    public String getFormOfComposition() {

        return formOfComposition;
    }

    public void setFormOfComposition(String formOfComposition) {
        formOfComposition = formOfComposition;
    }

    public String getUndefinedPos1() {
        return undefinedPos1;
    }

    public void setUndefinedPos1(String undefinedPos1) {
        this.undefinedPos1 = undefinedPos1;
    }

    public String getUndefinedPos2() {
        return undefinedPos2;
    }

    public void setUndefinedPos2(String undefinedPos2) {
        this.undefinedPos2 = undefinedPos2;
    }
}
