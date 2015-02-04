package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.document.BibMarc;

/**
 * Created with IntelliJ IDEA.
 * User: PP7788
 * Date: 12/6/12
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleWorkBibDocument extends BibMarc {

    private boolean select;
    private String tokenId;
    private String oleERSIdentifier;
    private String staffOnly;
    private String docFormat;

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }


    public String getStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(String staffOnly) {
        this.staffOnly = staffOnly;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
