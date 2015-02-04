package org.kuali.ole.docstore.model.xmlpojo.ingest;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 2/9/12
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Content {
    private String content;
    private Object contentObject;

    public Content() {

    }

    /**
     * Method to get contentObject.
     *
     * @return the contentObject
     */
    public Object getContentObject() {
        return contentObject;
    }

    /**
     * Method to set contentObject.
     *
     * @param contentObject the contentObject to set
     */
    public void setContentObject(Object contentObject) {
        this.contentObject = contentObject;
    }

    public Content(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
