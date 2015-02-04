package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainOperation {

    private OleSRUExplainServerInfo serverInfo;
    private OleSRUExplainDatabaseInfo databaseInfo;
    private OleSRUExplainIndexInfo indexInfo;
    private OleSRUExplainSchemaInfo schemaInfo;
    private OleSRUExplainConfigurationInfo configInfo;
    public String extraRequestData;

    public String getExtraRequestData() {
        return extraRequestData;
    }

    public void setExtraRequestData(String extraRequestData) {
        this.extraRequestData = extraRequestData;
    }

    public OleSRUExplainServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(OleSRUExplainServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public OleSRUExplainDatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    public void setDatabaseInfo(OleSRUExplainDatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public OleSRUExplainIndexInfo getIndexInfo() {
        return indexInfo;
    }

    public void setIndexInfo(OleSRUExplainIndexInfo indexInfo) {
        this.indexInfo = indexInfo;
    }

    public OleSRUExplainSchemaInfo getSchemaInfo() {
        return schemaInfo;
    }

    public void setSchemaInfo(OleSRUExplainSchemaInfo schemaInfo) {
        this.schemaInfo = schemaInfo;
    }

    public OleSRUExplainConfigurationInfo getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(OleSRUExplainConfigurationInfo configInfo) {
        this.configInfo = configInfo;
    }

    @Override
    public String toString() {
        return "Explain Operation{" +
                "serverInfo=" + serverInfo +
                ", databaseInfo=" + databaseInfo +
                ", indexInfo=" + indexInfo +
                ", schemaInfo=" + schemaInfo +
                ", configInfo=" + configInfo +
                ", extraRequestData=" + extraRequestData +
                '}';
    }
}
