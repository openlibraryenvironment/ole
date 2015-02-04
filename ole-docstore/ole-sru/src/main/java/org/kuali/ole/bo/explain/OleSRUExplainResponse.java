package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/17/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainResponse {

    private String version;
    private OleSRUExplainRecord record;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public OleSRUExplainRecord getRecord() {
        return record;
    }

    public void setRecord(OleSRUExplainRecord record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "Explain Records{" +
                "version='" + version + '\'' +
                ", record=" + record +
                '}';
    }
}
