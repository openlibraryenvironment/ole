package org.kuali.ole.describe.bo.marc.structuralfields;

import java.io.Serializable;

/**
 * Class for handling leader and its structural fields
 */
public class LeaderField implements Serializable {

    private String length = "#####";
    private String recStatus = "n";
    private String type = "a";
    private String bibLevel = "m";
    private String typeOfControl = "#";
    private String coding = "a";
    private int indicatorCount = 2;
    private int subfieldCount = 2;
    private String baseAddress = "#####";
    private String encodingLevel = "#";
    private String descCatalogForm = "a";
    private String multipartRecordLevel = "#";
    private int lengthOfLengthOfField = 4;
    private int lengthOfStartingCharacterPosition = 5;
    private int lengthOfImplementationDefined = 0;
    private int undefinedLeader = 0;



    public String getRecStatus() {
        return recStatus;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setRecStatus(String recStatus) {
        this.recStatus = recStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBibLevel() {
        return bibLevel;
    }

    public void setBibLevel(String bibLevel) {
        this.bibLevel = bibLevel;
    }

    public String getTypeOfControl() {
        return typeOfControl;
    }

    public void setTypeOfControl(String typeOfControl) {
        this.typeOfControl = typeOfControl;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public int getIndicatorCount() {
        return indicatorCount;
    }

    public void setIndicatorCount(int indicatorCount) {
        this.indicatorCount = indicatorCount;
    }

    public int getSubfieldCount() {
        return subfieldCount;
    }

    public void setSubfieldCount(int subfieldCount) {
        this.subfieldCount = subfieldCount;
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

    public String getEncodingLevel() {
        return encodingLevel;
    }

    public void setEncodingLevel(String encodingLevel) {
        this.encodingLevel = encodingLevel;
    }

    public String getDescCatalogForm() {
        return descCatalogForm;
    }

    public void setDescCatalogForm(String descCatalogForm) {
        this.descCatalogForm = descCatalogForm;
    }

    public String getMultipartRecordLevel() {
        return multipartRecordLevel;
    }

    public void setMultipartRecordLevel(String multipartRecordLevel) {
        this.multipartRecordLevel = multipartRecordLevel;
    }

    public int getLengthOfLengthOfField() {
        return lengthOfLengthOfField;
    }

    public void setLengthOfLengthOfField(int lengthOfLengthOfField) {
        this.lengthOfLengthOfField = lengthOfLengthOfField;
    }

    public int getLengthOfStartingCharacterPosition() {
        return lengthOfStartingCharacterPosition;
    }

    public void setLengthOfStartingCharacterPosition(int lengthOfStartingCharacterPosition) {
        this.lengthOfStartingCharacterPosition = lengthOfStartingCharacterPosition;
    }

    public int getLengthOfImplementationDefined() {
        return lengthOfImplementationDefined;
    }

    public void setLengthOfImplementationDefined(int lengthOfImplementationDefined) {
        this.lengthOfImplementationDefined = lengthOfImplementationDefined;
    }


    public int getUndefinedLeader() {
        return undefinedLeader;
    }

    public void setUndefinedLeader(int undefinedLeader) {
        this.undefinedLeader = undefinedLeader;
    }

}
