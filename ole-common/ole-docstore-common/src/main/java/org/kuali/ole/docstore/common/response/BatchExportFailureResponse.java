package org.kuali.ole.docstore.common.response;

/**
 * Created by rajeshbabuk on 4/20/16.
 */
public class BatchExportFailureResponse extends BatchResponse{

    private String bibLocalId;

    public String getBibLocalId() {
        return bibLocalId;
    }

    public void setBibLocalId(String bibLocalId) {
        this.bibLocalId = bibLocalId;
    }

}
