package org.kuali.ole.model.solr;

import org.apache.solr.common.SolrInputDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sheiks on 07/03/17.
 */
public class RecordCountAndSolrDocumentMap {

    private int numberOfBibFetched;
    private int numberOfBibProcessed;
    private int numberOfHoldingsFetched;
    private int numberOfHoldingsProcessed;
    private int numberOfItemsFetched;
    private int numberOfItemsProcessed;
    private Map<String, SolrInputDocument> solrInputDocumentMap = new HashMap<>();

    public int getNumberOfBibFetched() {
        return numberOfBibFetched;
    }

    public void setNumberOfBibFetched(int numberOfBibFetched) {
        this.numberOfBibFetched = numberOfBibFetched;
    }

    public int getNumberOfBibProcessed() {
        return numberOfBibProcessed;
    }

    public void setNumberOfBibProcessed(int numberOfBibProcessed) {
        this.numberOfBibProcessed = numberOfBibProcessed;
    }

    public int getNumberOfHoldingsFetched() {
        return numberOfHoldingsFetched;
    }

    public void setNumberOfHoldingsFetched(int numberOfHoldingsFetched) {
        this.numberOfHoldingsFetched = numberOfHoldingsFetched;
    }

    public int getNumberOfHoldingsProcessed() {
        return numberOfHoldingsProcessed;
    }

    public void setNumberOfHoldingsProcessed(int numberOfHoldingsProcessed) {
        this.numberOfHoldingsProcessed = numberOfHoldingsProcessed;
    }

    public int getNumberOfItemsFetched() {
        return numberOfItemsFetched;
    }

    public void setNumberOfItemsFetched(int numberOfItemsFetched) {
        this.numberOfItemsFetched = numberOfItemsFetched;
    }

    public int getNumberOfItemsProcessed() {
        return numberOfItemsProcessed;
    }

    public void setNumberOfItemsProcessed(int numberOfItemsProcessed) {
        this.numberOfItemsProcessed = numberOfItemsProcessed;
    }

    public Map<String, SolrInputDocument> getSolrInputDocumentMap() {
        return solrInputDocumentMap;
    }

    public void setSolrInputDocumentMap(Map<String, SolrInputDocument> solrInputDocumentMap) {
        this.solrInputDocumentMap = solrInputDocumentMap;
    }
}
