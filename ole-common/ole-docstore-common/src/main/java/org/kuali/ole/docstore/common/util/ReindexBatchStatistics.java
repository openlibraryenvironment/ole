package org.kuali.ole.docstore.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/3/14
 * Time: 7:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReindexBatchStatistics extends BatchStatistics {

    private long indexTime;
    private long buildSolrDocsTime;
    private long recToAddInSolr;
    private long commitTime;

    public long getIndexTime() {
        return indexTime;
    }

    public void addIndexTime(long indexTime) {
        this.indexTime = this.indexTime + indexTime;
    }

    public long getBuildSolrDocsTime() {
        return buildSolrDocsTime;
    }

    public void setBuildSolrDocsTime(long buildSolrDocsTime) {
        this.buildSolrDocsTime = buildSolrDocsTime;
    }

    public long getRecToAddInSolr() {
        return recToAddInSolr;
    }

    public void setRecToAddInSolr(long recToAddInSolr) {
        this.recToAddInSolr = recToAddInSolr;
    }

    public long getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(long commitTime) {
        this.commitTime = commitTime;
    }

    public void addCommitTime(long commitTime) {
        this.commitTime = this.commitTime + commitTime;
    }

    public void addRecToAddInSolr(long recToAddInSolr) {
        this.recToAddInSolr = this.recToAddInSolr + recToAddInSolr;
    }


    public void addBuildSolrDocsTime(long buildSolrDocsTime) {
        this.buildSolrDocsTime = this.buildSolrDocsTime + buildSolrDocsTime;
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        if(super.getStartTime() != null) {
            stringBuilder.append("Index start Time : ");
            stringBuilder.append(super.getStartTime().toString());
            stringBuilder.append(",");
        }
        stringBuilder.append("\nIndexed ");
        stringBuilder.append(super.getBibCount());
        stringBuilder.append(" bibs, ");
        stringBuilder.append(super.getHoldingsCount());
        stringBuilder.append(" holdings, ");
        stringBuilder.append(super.getItemCount());
        stringBuilder.append( " items, ");
        stringBuilder.append((super.getBibCount() + super.getHoldingsCount() + super.getItemCount()));
        stringBuilder.append(" in total.");
        stringBuilder.append("\nbuildSolrDocsTime : ");
        stringBuilder.append(buildSolrDocsTime);
        stringBuilder.append(", add to solr : ");
        stringBuilder.append(recToAddInSolr);
        stringBuilder.append(", commit time : ");
        stringBuilder.append(commitTime);
        stringBuilder.append("\nTime taken - fetch : ");
        stringBuilder.append(super.getTimeTaken());
        stringBuilder.append(", index : ");
        stringBuilder.append(indexTime);
        stringBuilder.append(", Total : ");
        stringBuilder.append((super.getTimeTaken() + this.indexTime));
        stringBuilder.append(",\nIndex End Time : ");


        if(getEndTime() != null) {
            stringBuilder.append(super.getEndTime().toString());
        }

        return stringBuilder.toString();
    }


    public void getBatchStatus(ReindexBatchStatistics reindexBatchStatistics) {

        setBibCount(reindexBatchStatistics.getBibCount() - getBibCount());
        setHoldingsCount(reindexBatchStatistics.getHoldingsCount() - getHoldingsCount());
        setItemCount(reindexBatchStatistics.getItemCount() - getItemCount());
        setTimeTaken(reindexBatchStatistics.getTimeTaken() - getTimeTaken());
        indexTime = reindexBatchStatistics.getIndexTime() - indexTime;
        buildSolrDocsTime = reindexBatchStatistics.getBuildSolrDocsTime() - buildSolrDocsTime;
        recToAddInSolr = reindexBatchStatistics.getRecToAddInSolr() - recToAddInSolr;
        commitTime = reindexBatchStatistics.getCommitTime() - commitTime;
    }


    public void setData(ReindexBatchStatistics reindexBatchStatistics) {
        setBibCount(reindexBatchStatistics.getBibCount());
        setHoldingsCount(reindexBatchStatistics.getHoldingsCount());
        setItemCount(reindexBatchStatistics.getItemCount());
        setTimeTaken(reindexBatchStatistics.getTimeTaken());
        indexTime = reindexBatchStatistics.getIndexTime();
        buildSolrDocsTime = reindexBatchStatistics.getBuildSolrDocsTime();
        recToAddInSolr = reindexBatchStatistics.getRecToAddInSolr();
        commitTime = reindexBatchStatistics.getCommitTime();

    }

}
