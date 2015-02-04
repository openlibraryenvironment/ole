package org.kuali.ole.bo.explain;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainIndex {

    private OleSRUExplainIndexMap indexMap;

    public OleSRUExplainIndexMap getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(OleSRUExplainIndexMap indexMap) {
        this.indexMap = indexMap;
    }

    @Override
    public String toString() {
        return "Explain Index{" +
                "indexMap=" + indexMap +
                '}';
    }
}
