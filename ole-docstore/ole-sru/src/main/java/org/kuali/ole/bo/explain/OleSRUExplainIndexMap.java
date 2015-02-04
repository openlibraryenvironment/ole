package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainIndexMap {

    private OleSRUExplainIndexMapName name;

    public OleSRUExplainIndexMapName getName() {
        return name;
    }

    public void setName(OleSRUExplainIndexMapName name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Explain IndexMap{" +
                "name=" + name +
                '}';
    }
}
