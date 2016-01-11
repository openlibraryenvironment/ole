package org.kuali.ole.docstore.common.response;

import java.util.List;

/**
 * Created by SheikS on 1/11/2016.
 */
public class OleNGBibImportResponse {
    private List<BibResponse> bibResponses;

    public List<BibResponse> getBibResponses() {
        return bibResponses;
    }

    public void setBibResponses(List<BibResponse> bibResponses) {
        this.bibResponses = bibResponses;
    }
}
