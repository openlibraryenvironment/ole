package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/18/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceReqSelComments extends PersistableBusinessObjectBase {

    private String oleReqSelCommentId;
    private String oleERSIdentifier;
    private String oleReqSelComments;

    public String getOleReqSelCommentId() {
        return oleReqSelCommentId;
    }

    public void setOleReqSelCommentId(String oleReqSelCommentId) {
        this.oleReqSelCommentId = oleReqSelCommentId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getOleReqSelComments() {
        return oleReqSelComments;
    }

    public void setOleReqSelComments(String oleReqSelComments) {
        this.oleReqSelComments = oleReqSelComments;
    }
}
