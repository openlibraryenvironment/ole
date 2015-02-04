package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainDatabaseInfo {

    private OleSRUExplainDatabaseTitle title;

    public OleSRUExplainDatabaseTitle getTitle() {
        return title;
    }

    public void setTitle(OleSRUExplainDatabaseTitle title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Explain DatabaseInfo{" +
                "title=" + title +
                '}';
    }
}
