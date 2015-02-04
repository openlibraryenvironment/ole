package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainRecordData {

    private OleSRUExplainOperation explain;

    public OleSRUExplainOperation getExplain() {
        return explain;
    }

    public void setExplain(OleSRUExplainOperation explain) {
        this.explain = explain;
    }

    @Override
    public String toString() {
        return "Explain Data{" +
                "explain=" + explain +
                '}';
    }
}
