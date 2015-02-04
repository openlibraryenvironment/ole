package org.kuali.ole.deliver.bo;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.List;


public interface OlePatronLoanDocumentsContract extends Versioned, Identifiable {

    public List<? extends OlePatronLoanDocumentContract> getOlePatronLoanDocuments();


}
