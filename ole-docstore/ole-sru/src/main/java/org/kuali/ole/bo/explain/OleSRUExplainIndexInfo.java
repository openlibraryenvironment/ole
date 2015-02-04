package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainIndexInfo {

    private OleSRUExplainIndexSet set;
    private OleSRUExplainIndex index;


    public OleSRUExplainIndexSet getSet() {
        return set;
    }

    public void setSet(OleSRUExplainIndexSet set) {
        this.set = set;
    }

    public OleSRUExplainIndex getIndex() {
        return index;
    }

    public void setIndex(OleSRUExplainIndex index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Explain IndexInfo{" +
                "set=" + set +
                ", index=" + index +
                '}';
    }
}
