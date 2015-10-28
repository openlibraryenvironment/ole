package org.kuali.ole.batch.bo;

import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/2/14
 * Time: 10:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchBibImportStatistics {
    private static final Logger LOG = LoggerFactory.getLogger(OLEBatchBibImportStatistics.class);

    private int chunkCount = 0;
    private int totalCount = 0;
    private int successRecord = 0;
    private int noOfEinstanceAdded = 0;
    private int noOfEinstanceDeleted = 0;
    private int noOfEinstanceCreatedWithOutLink = 0;
    private int noOfbibsHaveMoreThanOneEinstance = 0;
    private int noOfEHoldingsCreatedWithOutPlatfom =0;
    private int noOfEHoldingsCreatedWithOutEResource =0;
    private StringBuffer misMatchMarcRecords = new StringBuffer("");
    private List<BibMarcRecord> mismatchRecordList = new ArrayList<BibMarcRecord>();
    private List<BibMarcRecord> recordsCreatedWithOutLink = new ArrayList<BibMarcRecord>();
    private List<BibMarcRecord> recordsCreatedWithMoreThanOneLink = new ArrayList<BibMarcRecord>();
    private List<Holdings> totalRecordsCreated = new ArrayList<Holdings>();
    private List<Holdings> totalRecordsDeleted = new ArrayList<Holdings>();
    private List<String> invalidLeaderField = new ArrayList<>();
    private List<BibMarcRecord> bibMarcRecordList = new ArrayList<BibMarcRecord>();
    private List bibImportChunkRecordsList = new ArrayList(0);
    private StringBuilder errorBuilder = new StringBuilder();
    private List<BibMarcRecord> matchedBibMarc = new ArrayList<>();
    private List<BibMarcRecord> nonMatchedBibMarc = new ArrayList<>();
    private List<String> matchedBibIds = new ArrayList<>();
    private List<String> noMatchFoundBibs = new ArrayList<>();
    private List<BibMarcRecord> matchedHoldingsMarc = new ArrayList<>();
    private List<BibMarcRecord> nonMatchedHoldingsMarc = new ArrayList<>();
    private List<String> matchedHoldingsIds = new ArrayList<>();
    private List<String> noMatchFoundHoldings = new ArrayList<>();
    private List<BibMarcRecord> matchedItemMarc = new ArrayList<>();
    private List<BibMarcRecord> nonMatchedItemMarc = new ArrayList<>();
    private List<String> matchedItemIds = new ArrayList<>();
    private List<String> noMatchFoundItem = new ArrayList<>();

    private List<BibMarcRecord> moreThanOneHoldingsMatched = new ArrayList<BibMarcRecord>();
    private List<BibMarcRecord> moreThanOneItemMatched = new ArrayList<BibMarcRecord>();

    public int getChunkCount() {
        return chunkCount;
    }

    public void setChunkCount(int chunkCount) {
        this.chunkCount = chunkCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSuccessRecord() {
        return successRecord;
    }

    public void setSuccessRecord(int successRecord) {
        this.successRecord = successRecord;
    }

    public int getNoOfEinstanceAdded() {
        return noOfEinstanceAdded;
    }

    public void setNoOfEinstanceAdded(int noOfEinstanceAdded) {
        this.noOfEinstanceAdded = noOfEinstanceAdded;
    }

    public int getNoOfEinstanceDeleted() {
        return noOfEinstanceDeleted;
    }

    public void setNoOfEinstanceDeleted(int noOfEinstanceDeleted) {
        this.noOfEinstanceDeleted = noOfEinstanceDeleted;
    }

    public int getNoOfEinstanceCreatedWithOutLink() {
        return noOfEinstanceCreatedWithOutLink;
    }

    public void setNoOfEinstanceCreatedWithOutLink(int noOfEinstanceCreatedWithOutLink) {
        this.noOfEinstanceCreatedWithOutLink = noOfEinstanceCreatedWithOutLink;
    }

    public int getNoOfbibsHaveMoreThanOneEinstance() {
        return noOfbibsHaveMoreThanOneEinstance;
    }

    public void setNoOfbibsHaveMoreThanOneEinstance(int noOfbibsHaveMoreThanOneEinstance) {
        this.noOfbibsHaveMoreThanOneEinstance = noOfbibsHaveMoreThanOneEinstance;
    }

    public void addChunkCount(int chunkCount) {
        this.chunkCount = this.chunkCount + chunkCount;
    }

    public void addTotalCount(int totalCount) {
        this.totalCount = this.totalCount + totalCount;
    }

    public void addSuccessRecord(int successRecord) {
        this.successRecord = this.successRecord + successRecord;
    }

    public void addNoOfEinstanceAdded(int noOfEinstanceAdded) {
        this.noOfEinstanceAdded = this.noOfEinstanceAdded + noOfEinstanceAdded;
    }

    public void addNoOfEinstanceDeleted(int noOfEinstanceDeleted) {
        this.noOfEinstanceDeleted = this.noOfEinstanceDeleted + noOfEinstanceDeleted;
    }

    public void addNoOfEinstanceCreatedWithOutLink(int noOfEinstanceCreatedWithOutLink) {
        this.noOfEinstanceCreatedWithOutLink = this.noOfEinstanceCreatedWithOutLink + noOfEinstanceCreatedWithOutLink;
    }

    public void addNoOfbibsHaveMoreThanOneEinstance(int noOfbibsHaveMoreThanOneEinstance) {
        this.noOfbibsHaveMoreThanOneEinstance = this.noOfbibsHaveMoreThanOneEinstance + noOfbibsHaveMoreThanOneEinstance;
    }

    public StringBuffer getMisMatchMarcRecords() {
        return misMatchMarcRecords;
    }

    public void setMisMatchMarcRecords(StringBuffer misMatchMarcRecords) {
        this.misMatchMarcRecords = misMatchMarcRecords;
    }

    public void appendMisMatchMarcRecords(String misMatchMarcRecords) {
        this.misMatchMarcRecords.append(misMatchMarcRecords);
    }

    public List<BibMarcRecord> getMismatchRecordList() {
        return mismatchRecordList;
    }

    public void setMismatchRecordList(List<BibMarcRecord> mismatchRecordList) {
        this.mismatchRecordList = mismatchRecordList;
    }

    public List<BibMarcRecord> getRecordsCreatedWithOutLink() {
        return recordsCreatedWithOutLink;
    }

    public void setRecordsCreatedWithOutLink(List<BibMarcRecord> recordsCreatedWithOutLink) {
        this.recordsCreatedWithOutLink = recordsCreatedWithOutLink;
    }

    public List<BibMarcRecord> getRecordsCreatedWithMoreThanOneLink() {
        return recordsCreatedWithMoreThanOneLink;
    }

    public void setRecordsCreatedWithMoreThanOneLink(List<BibMarcRecord> recordsCreatedWithMoreThanOneLink) {
        this.recordsCreatedWithMoreThanOneLink = recordsCreatedWithMoreThanOneLink;
    }

    public List<Holdings> getTotalRecordsCreated() {
        return totalRecordsCreated;
    }

    public void setTotalRecordsCreated(List<Holdings> totalRecordsCreated) {
        this.totalRecordsCreated = totalRecordsCreated;
    }

    public List<Holdings> getTotalRecordsDeleted() {
        return totalRecordsDeleted;
    }

    public void setTotalRecordsDeleted(List<Holdings> totalRecordsDeleted) {
        this.totalRecordsDeleted = totalRecordsDeleted;
    }

    public List<String> getInvalidLeaderField() {
        return invalidLeaderField;
    }

    public void setInvalidLeaderField(List<String> invalidLeaderField) {
        this.invalidLeaderField = invalidLeaderField;
    }

    public List<BibMarcRecord> getBibMarcRecordList() {
        return bibMarcRecordList;
    }

    public void setBibMarcRecordList(List<BibMarcRecord> bibMarcRecordList) {
        this.bibMarcRecordList = bibMarcRecordList;
    }

    public List getBibImportChunkRecordsList() {
        return bibImportChunkRecordsList;
    }

    public void setBibImportChunkRecordsList(List bibImportChunkRecordsList) {
        this.bibImportChunkRecordsList = bibImportChunkRecordsList;
    }

    public List<BibMarcRecord> getMoreThanOneHoldingsMatched() {
        return moreThanOneHoldingsMatched;
    }

    public void setMoreThanOneHoldingsMatched(List<BibMarcRecord> moreThanOneHoldingsMatched) {
        this.moreThanOneHoldingsMatched = moreThanOneHoldingsMatched;
    }

    public List<BibMarcRecord> getMoreThanOneItemMatched() {
        return moreThanOneItemMatched;
    }

    public void setMoreThanOneItemMatched(List<BibMarcRecord> moreThanOneItemMatched) {
        this.moreThanOneItemMatched = moreThanOneItemMatched;
    }

    public StringBuilder getErrorBuilder() {
        return errorBuilder;
    }

    public void setErrorBuilder(StringBuilder errorBuilder) {
        this.errorBuilder = errorBuilder;
    }

    public int getNoOfEHoldingsCreatedWithOutPlatfom() {
        return noOfEHoldingsCreatedWithOutPlatfom;
    }

    public void setNoOfEHoldingsCreatedWithOutPlatfom(int noOfEHoldingsCreatedWithOutPlatfom) {
        this.noOfEHoldingsCreatedWithOutPlatfom = noOfEHoldingsCreatedWithOutPlatfom;
    }

    public int getNoOfEHoldingsCreatedWithOutEResource() {
        return noOfEHoldingsCreatedWithOutEResource;
    }

    public void setNoOfEHoldingsCreatedWithOutEResource(int noOfEHoldingsCreatedWithOutEResource) {
        this.noOfEHoldingsCreatedWithOutEResource = noOfEHoldingsCreatedWithOutEResource;
    }

    public List<BibMarcRecord> getMatchedBibMarc() {
        return matchedBibMarc;
    }

    public void setMatchedBibMarc(List<BibMarcRecord> matchedBibMarc) {
        this.matchedBibMarc = matchedBibMarc;
    }

    public List<BibMarcRecord> getNonMatchedBibMarc() {
        return nonMatchedBibMarc;
    }

    public void setNonMatchedBibMarc(List<BibMarcRecord> nonMatchedBibMarc) {
        this.nonMatchedBibMarc = nonMatchedBibMarc;
    }


    public List<String> getMatchedBibIds() {
        return matchedBibIds;
    }

    public void setMatchedBibIds(List<String> matchedBibIds) {
        this.matchedBibIds = matchedBibIds;
    }

    public List<String> getNoMatchFoundBibs() {
        return noMatchFoundBibs;
    }

    public void setNoMatchFoundBibs(List<String> noMatchFoundBibs) {
        this.noMatchFoundBibs = noMatchFoundBibs;
    }

    public List<BibMarcRecord> getMatchedHoldingsMarc() {
        return matchedHoldingsMarc;
    }

    public void setMatchedHoldingsMarc(List<BibMarcRecord> matchedHoldingsMarc) {
        this.matchedHoldingsMarc = matchedHoldingsMarc;
    }

    public List<BibMarcRecord> getNonMatchedHoldingsMarc() {
        return nonMatchedHoldingsMarc;
    }

    public void setNonMatchedHoldingsMarc(List<BibMarcRecord> nonMatchedHoldingsMarc) {
        this.nonMatchedHoldingsMarc = nonMatchedHoldingsMarc;
    }

    public List<String> getMatchedHoldingsIds() {
        return matchedHoldingsIds;
    }

    public void setMatchedHoldingsIds(List<String> matchedHoldingsIds) {
        this.matchedHoldingsIds = matchedHoldingsIds;
    }

    public List<String> getNoMatchFoundHoldings() {
        return noMatchFoundHoldings;
    }

    public void setNoMatchFoundHoldings(List<String> noMatchFoundHoldings) {
        this.noMatchFoundHoldings = noMatchFoundHoldings;
    }

    public List<BibMarcRecord> getMatchedItemMarc() {
        return matchedItemMarc;
    }

    public void setMatchedItemMarc(List<BibMarcRecord> matchedItemMarc) {
        this.matchedItemMarc = matchedItemMarc;
    }

    public List<BibMarcRecord> getNonMatchedItemMarc() {
        return nonMatchedItemMarc;
    }

    public void setNonMatchedItemMarc(List<BibMarcRecord> nonMatchedItemMarc) {
        this.nonMatchedItemMarc = nonMatchedItemMarc;
    }

    public List<String> getMatchedItemIds() {
        return matchedItemIds;
    }

    public void setMatchedItemIds(List<String> matchedItemIds) {
        this.matchedItemIds = matchedItemIds;
    }

    public List<String> getNoMatchFoundItem() {
        return noMatchFoundItem;
    }

    public void setNoMatchFoundItem(List<String> noMatchFoundItem) {
        this.noMatchFoundItem = noMatchFoundItem;
    }

    @Override
    public String toString() {
        return "OLEBatchBibImportStatistics {" + "chunkCount" + chunkCount
                + "totalCount" + totalCount
                + "successRecord" + successRecord
                + "noOfEinstanceAdded" + noOfEinstanceAdded
                + "noOfEinstanceDeleted" + noOfEinstanceDeleted
                + "noOfEinstanceCreatedWithOutLink" + noOfEinstanceCreatedWithOutLink
                + "noOfbibsHaveMoreThanOneEinstance" + noOfbibsHaveMoreThanOneEinstance
                + "}";
    }

    public void setInstanceStatistics(List<BibMarcRecord> mismatchRecordList) {
        setMismatchRecordList(mismatchRecordList);
        setNoOfEinstanceCreatedWithOutLink(getRecordsCreatedWithOutLink().size());
        setNoOfbibsHaveMoreThanOneEinstance(getRecordsCreatedWithMoreThanOneLink().size());
        setNoOfEinstanceAdded(getTotalRecordsCreated().size());
        setNoOfEinstanceDeleted(getTotalRecordsDeleted().size());

    }
}
