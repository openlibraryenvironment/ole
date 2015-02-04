package org.kuali.ole.docstore.metrics.reindex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/27/12
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReIndexingDocTypeStatus {

    private List<ReIndexingBatchStatus> reIndBatStatusList = new ArrayList<ReIndexingBatchStatus>();
    private String docCategory;
    private String docType;
    private String docFormat;
    private String status;

    public List<ReIndexingBatchStatus> getReIndBatStatusList() {
        return reIndBatStatusList;
    }

    public void setReIndBatStatusList(List<ReIndexingBatchStatus> reIndBatStatusList) {
        this.reIndBatStatusList = reIndBatStatusList;
    }

    public String getDocCategory() {
        return docCategory;
    }

    public void setDocCategory(String docCategory) {
        this.docCategory = docCategory;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
