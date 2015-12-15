package org.kuali.ole.oleng.batch.profile.model;

/**
 * Created by SheikS on 11/25/2015.
 */
public class BatchProfileDataTransformer extends MarcDataField {

    private long dataTransformerId;
    private String dataType;
    private String transformer;
    private String expression;

    public long getDataTransformerId() {
        return dataTransformerId;
    }

    public void setDataTransformerId(long dataTransformerId) {
        this.dataTransformerId = dataTransformerId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getTransformer() {
        return transformer;
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
