package org.kuali.ole.describe.bo.marc.structuralfields.controlfield008;

/**
 * Class for handling control field 008 details
 */
public class ControlField008 {
    private String date008;
    private String rawText;
    private String headerText = "008";
    private String dateEnteredOnFile = "######";
    private String typeOfDate = "s";
    private String date1 = "####";
    private String date2 = "####";
    private String place="xxu";
    private String language="eng";
    private String modifiedRecord = "#";
    private String catalogingSource = "d";

    private Books books;
    private Music music;
    private Map map;
    private MixedMaterial mixedMaterial;
    private VisualMaterials visualMaterials;
    private ContinuingResources continuingResources;
    private ComputerFiles computerFiles;

    public String getDate008() {
        return date008;
    }

    public void setDate008(String date008) {
        this.date008 = date008;
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

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public MixedMaterial getMixedMaterial() {
        return mixedMaterial;
    }

    public void setMixedMaterial(MixedMaterial mixedMaterial) {
        this.mixedMaterial = mixedMaterial;
    }

    public VisualMaterials getVisualMaterials() {
        return visualMaterials;
    }

    public void setVisualMaterials(VisualMaterials visualMaterials) {
        this.visualMaterials = visualMaterials;
    }

    public ContinuingResources getContinuingResources() {
        return continuingResources;
    }

    public void setContinuingResources(ContinuingResources continuingResources) {
        this.continuingResources = continuingResources;
    }

    public ComputerFiles getComputerFiles() {
        return computerFiles;
    }

    public void setComputerFiles(ComputerFiles computerFiles) {
        this.computerFiles = computerFiles;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getDateEnteredOnFile() {
        return dateEnteredOnFile;
    }

    public void setDateEnteredOnFile(String dateEnteredOnFile) {
        this.dateEnteredOnFile = dateEnteredOnFile;
    }

    public String getTypeOfDate() {
        return typeOfDate;
    }

    public void setTypeOfDate(String typeOfDate) {
        this.typeOfDate = typeOfDate;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getModifiedRecord() {
        return modifiedRecord;
    }

    public void setModifiedRecord(String modifiedRecord) {
        this.modifiedRecord = modifiedRecord;
    }

    public String getCatalogingSource() {
        return catalogingSource;
    }

    public void setCatalogingSource(String catalogingSource) {
        this.catalogingSource = catalogingSource;
    }
}
