package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/27/13
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileRenameField extends PersistableBusinessObjectBase{
    private String id;
    private String batchProcessProfileId;
    private String originalTag;
    private String originalFirstIndicator;
    private String originalSecondIndicator;
    private String originalSubField;
    private String originalSubFieldContains;
    private String renamedTag;
    private String renamedFirstIndicator;
    private String renamedSecondIndicator;
    private String renamedSubField;
    private String renamedSubFieldContains;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }

    public String getOriginalTag() {
        return originalTag;
    }

    public void setOriginalTag(String originalTag) {
        this.originalTag = originalTag;
    }



    public String getOriginalSubField() {
        return originalSubField;
    }

    public void setOriginalSubField(String originalSubField) {
        this.originalSubField = originalSubField;
    }

    public String getOriginalSubFieldContains() {
        return originalSubFieldContains;
    }

    public void setOriginalSubFieldContains(String originalSubFieldContains) {
        this.originalSubFieldContains = originalSubFieldContains;
    }

    public String getRenamedTag() {
        return renamedTag;
    }

    public void setRenamedTag(String renamedTag) {
        this.renamedTag = renamedTag;
    }



    public String getRenamedSubField() {
        return renamedSubField;
    }

    public void setRenamedSubField(String renamedSubField) {
        this.renamedSubField = renamedSubField;
    }

    public String getRenamedSubFieldContains() {
        return renamedSubFieldContains;
    }

    public void setRenamedSubFieldContains(String renamedSubFieldContains) {
        this.renamedSubFieldContains = renamedSubFieldContains;
    }

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getOriginalFirstIndicator() {
        return originalFirstIndicator;
    }

    public void setOriginalFirstIndicator(String originalFirstIndicator) {
        this.originalFirstIndicator = originalFirstIndicator;
    }

    public String getOriginalSecondIndicator() {
        return originalSecondIndicator;
    }

    public void setOriginalSecondIndicator(String originalSecondIndicator) {
        this.originalSecondIndicator = originalSecondIndicator;
    }

    public String getRenamedFirstIndicator() {
        return renamedFirstIndicator;
    }

    public void setRenamedFirstIndicator(String renamedFirstIndicator) {
        this.renamedFirstIndicator = renamedFirstIndicator;
    }

    public String getRenamedSecondIndicator() {
        return renamedSecondIndicator;
    }

    public void setRenamedSecondIndicator(String renamedSecondIndicator) {
        this.renamedSecondIndicator = renamedSecondIndicator;
    }
}
