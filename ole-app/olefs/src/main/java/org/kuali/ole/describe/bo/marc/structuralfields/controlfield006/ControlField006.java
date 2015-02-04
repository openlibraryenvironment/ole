package org.kuali.ole.describe.bo.marc.structuralfields.controlfield006;

/**
 * Class for handling control field 006 details
 */
public class ControlField006 {
    private String rawText;
    private String format;
    private Books books;
    private Music music;
    private Map map;
    private MixedMaterial mixedMaterial;
    private VisualMaterials visualMaterials;
    private ContinuingResources continuingResources;
    private ComputerFiles computerFiles;



    public VisualMaterials getVisualMaterials() {
        return visualMaterials;
    }

    public void setVisualMaterials(VisualMaterials visualMaterials) {
        this.visualMaterials = visualMaterials;
    }

    public MixedMaterial getMixedMaterial() {
        return mixedMaterial;
    }

    public void setMixedMaterial(MixedMaterial mixedMaterial) {
        this.mixedMaterial = mixedMaterial;
    }


    public ContinuingResources getContinuingResources() {
        return continuingResources;
    }

    public void setContinuingResources(ContinuingResources continuingResources) {
        this.continuingResources = continuingResources;
    }


    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }


    public ComputerFiles getComputerFiles() {
        return computerFiles;
    }

    public void setComputerFiles(ComputerFiles computerFiles) {
        this.computerFiles = computerFiles;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }


}
