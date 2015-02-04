package org.kuali.ole.bo.serachRetrieve;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/17/12
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUInstanceDocument {

    public String typeOfRecord;
    public String encodingLevel;
    public String format;
    public String receiptAcqStatus;
    public String generalRetention;
    public String completeness;
    public String dateOfReport;
    public String nucCode;
    public String localLocation;
    public String shelvingLocation;
    public String callNumber;
    public String shelvingData;
    public String copyNumber;
    public String publicNote;
    public String reproductionNote;
    public String termsUseRepro;
    public String enumAndChron;
    public List<OleSRUInstanceVolume> volumes;
    public List<OleSRUCirculationDocument> circulations;

    public String getTypeOfRecord() {
        return typeOfRecord;
    }

    public void setTypeOfRecord(String typeOfRecord) {
        this.typeOfRecord = typeOfRecord;
    }

    public String getEncodingLevel() {
        return encodingLevel;
    }

    public void setEncodingLevel(String encodingLevel) {
        this.encodingLevel = encodingLevel;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getReceiptAcqStatus() {
        return receiptAcqStatus;
    }

    public void setReceiptAcqStatus(String receiptAcqStatus) {
        this.receiptAcqStatus = receiptAcqStatus;
    }

    public String getGeneralRetention() {
        return generalRetention;
    }

    public void setGeneralRetention(String generalRetention) {
        this.generalRetention = generalRetention;
    }

    public String getCompleteness() {
        return completeness;
    }

    public void setCompleteness(String completeness) {
        this.completeness = completeness;
    }

    public String getDateOfReport() {
        return dateOfReport;
    }

    public void setDateOfReport(String dateOfReport) {
        this.dateOfReport = dateOfReport;
    }

    public String getNucCode() {
        return nucCode;
    }

    public void setNucCode(String nucCode) {
        this.nucCode = nucCode;
    }

    public String getLocalLocation() {
        return localLocation;
    }

    public void setLocalLocation(String localLocation) {
        this.localLocation = localLocation;
    }

    public String getShelvingLocation() {
        return shelvingLocation;
    }

    public void setShelvingLocation(String shelvingLocation) {
        this.shelvingLocation = shelvingLocation;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getShelvingData() {
        return shelvingData;
    }

    public void setShelvingData(String shelvingData) {
        this.shelvingData = shelvingData;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getPublicNote() {
        return publicNote;
    }

    public void setPublicNote(String publicNote) {
        this.publicNote = publicNote;
    }

    public String getReproductionNote() {
        return reproductionNote;
    }

    public void setReproductionNote(String reproductionNote) {
        this.reproductionNote = reproductionNote;
    }

    public String getTermsUseRepro() {
        return termsUseRepro;
    }

    public void setTermsUseRepro(String termsUseRepro) {
        this.termsUseRepro = termsUseRepro;
    }

    public String getEnumAndChron() {
        return enumAndChron;
    }

    public void setEnumAndChron(String enumAndChron) {
        this.enumAndChron = enumAndChron;
    }

    public List<OleSRUInstanceVolume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<OleSRUInstanceVolume> volumes) {
        this.volumes = volumes;
    }

    public List<OleSRUCirculationDocument> getCirculations() {
        return circulations;
    }

    public void setCirculations(List<OleSRUCirculationDocument> circulations) {
        this.circulations = circulations;
    }

    @Override
    public String toString() {
        return "typeOfRecord='" + typeOfRecord + '\'' +
                ", encodingLevel='" + encodingLevel + '\'' +
                ", format='" + format + '\'' +
                ", receiptAcqStatus='" + receiptAcqStatus + '\'' +
                ", generalRetention='" + generalRetention + '\'' +
                ", completeness='" + completeness + '\'' +
                ", dateOfReport='" + dateOfReport + '\'' +
                ", nucCode='" + nucCode + '\'' +
                ", localLocation='" + localLocation + '\'' +
                ", shelvingLocation='" + shelvingLocation + '\'' +
                ", callNumber='" + callNumber + '\'' +
                ", shelvingData='" + shelvingData + '\'' +
                ", copyNumber='" + copyNumber + '\'' +
                ", publicNote='" + publicNote + '\'' +
                ", reproductionNote='" + reproductionNote + '\'' +
                ", termsUseRepro='" + termsUseRepro + '\'' +
                ", enumAndChron='" + enumAndChron + '\'' +
                ", volumes=" + volumes +
                ", circulations=" + circulations +
                '}';
    }
}
