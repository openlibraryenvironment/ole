package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainSchemaInfo {

    private OleSRUExplainSchema schema;

    public OleSRUExplainSchema getSchema() {
        return schema;
    }

    public void setSchema(OleSRUExplainSchema schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "Explain SchemaInfo{" +
                "schema=" + schema +
                '}';
    }
}
