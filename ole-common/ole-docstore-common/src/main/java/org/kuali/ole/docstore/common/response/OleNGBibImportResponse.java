package org.kuali.ole.docstore.common.response;

import java.util.List;

/**
 * Created by SheikS on 1/11/2016.
 */
public class OleNGBibImportResponse {

    private int matchedBibsCount;
    private int unmatchedBibsCount;
    private int multipleMatchedBibsCount;

    private int matchedHoldingsCount;
    private int unmatchedHoldingsCount;
    private int multipleMatchedHoldingsCount;

    private int matchedItemsCount;
    private int unmatchedItemsCount;
    private int multipleMatchedItemsCount;

    private int matchedEHoldingsCount;
    private int unmatchedEHoldingsCount;
    private int multipleMatchedEHoldingsCount;

    private List<BibResponse> bibResponses;

    public List<BibResponse> getBibResponses() {
        return bibResponses;
    }

    public void setBibResponses(List<BibResponse> bibResponses) {
        this.bibResponses = bibResponses;
    }

    public int getMatchedBibsCount() {
        return matchedBibsCount;
    }

    public void setMatchedBibsCount(int matchedBibsCount) {
        this.matchedBibsCount = matchedBibsCount;
    }

    public int getUnmatchedBibsCount() {
        return unmatchedBibsCount;
    }

    public void setUnmatchedBibsCount(int unmatchedBibsCount) {
        this.unmatchedBibsCount = unmatchedBibsCount;
    }

    public int getMultipleMatchedBibsCount() {
        return multipleMatchedBibsCount;
    }

    public void setMultipleMatchedBibsCount(int multipleMatchedBibsCount) {
        this.multipleMatchedBibsCount = multipleMatchedBibsCount;
    }

    public int getMatchedHoldingsCount() {
        return matchedHoldingsCount;
    }

    public void setMatchedHoldingsCount(int matchedHoldingsCount) {
        this.matchedHoldingsCount = matchedHoldingsCount;
    }

    public int getUnmatchedHoldingsCount() {
        return unmatchedHoldingsCount;
    }

    public void setUnmatchedHoldingsCount(int unmatchedHoldingsCount) {
        this.unmatchedHoldingsCount = unmatchedHoldingsCount;
    }

    public int getMultipleMatchedHoldingsCount() {
        return multipleMatchedHoldingsCount;
    }

    public void setMultipleMatchedHoldingsCount(int multipleMatchedHoldingsCount) {
        this.multipleMatchedHoldingsCount = multipleMatchedHoldingsCount;
    }

    public int getMatchedItemsCount() {
        return matchedItemsCount;
    }

    public void setMatchedItemsCount(int matchedItemsCount) {
        this.matchedItemsCount = matchedItemsCount;
    }

    public int getUnmatchedItemsCount() {
        return unmatchedItemsCount;
    }

    public void setUnmatchedItemsCount(int unmatchedItemsCount) {
        this.unmatchedItemsCount = unmatchedItemsCount;
    }

    public int getMultipleMatchedItemsCount() {
        return multipleMatchedItemsCount;
    }

    public void setMultipleMatchedItemsCount(int multipleMatchedItemsCount) {
        this.multipleMatchedItemsCount = multipleMatchedItemsCount;
    }

    public int getMatchedEHoldingsCount() {
        return matchedEHoldingsCount;
    }

    public void setMatchedEHoldingsCount(int matchedEHoldingsCount) {
        this.matchedEHoldingsCount = matchedEHoldingsCount;
    }

    public int getUnmatchedEHoldingsCount() {
        return unmatchedEHoldingsCount;
    }

    public void setUnmatchedEHoldingsCount(int unmatchedEHoldingsCount) {
        this.unmatchedEHoldingsCount = unmatchedEHoldingsCount;
    }

    public int getMultipleMatchedEHoldingsCount() {
        return multipleMatchedEHoldingsCount;
    }

    public void setMultipleMatchedEHoldingsCount(int multipleMatchedEHoldingsCount) {
        this.multipleMatchedEHoldingsCount = multipleMatchedEHoldingsCount;
    }
}
