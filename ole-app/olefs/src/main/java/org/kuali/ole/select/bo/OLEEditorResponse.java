package org.kuali.ole.select.bo;

import org.kuali.ole.docstore.common.document.Bib;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 1/6/14
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEditorResponse {

    private Bib bib;
    private String linkedInstanceId;
    private String tokenId;
    private String linkToOrderOption;

    public String getLinkedInstanceId() {
        return linkedInstanceId;
    }

    public void setLinkedInstanceId(String linkedInstanceId) {
        this.linkedInstanceId = linkedInstanceId;
    }

    public Bib getBib() {
        return bib;
    }

    public void setBib(Bib bib) {
        this.bib = bib;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getLinkToOrderOption() {
        return linkToOrderOption;
    }

    public void setLinkToOrderOption(String linkToOrderOption) {
        this.linkToOrderOption = linkToOrderOption;
    }
}
