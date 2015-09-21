package org.kuali.ole.deliver.controller;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.util.ErrorMessage;

/**
 * Created by chenchulakshmig on 8/25/15.
 */
public class PatronLookupCircAPIController extends PatronLookupCircBaseController {

    @Override
    public OlePatronDocument getPatronDocument(DroolsExchange droolsExchange) {
        OlePatronDocument olePatronDocument = (OlePatronDocument) droolsExchange.getFromContext("olePatronDocument");
        if (olePatronDocument == null) {
            return new OlePatronDocument();
        }
        return olePatronDocument;
    }

    @Override
    public void setPatronDocument(DroolsExchange droolsExchange, OlePatronDocument patronDocument) {
        droolsExchange.addToContext("olePatronDocument", patronDocument);
    }

    @Override
    public String getPatronBarcode(DroolsExchange droolsExchange) {
        return (String) droolsExchange.getFromContext("patronBarcode");
    }

    @Override
    public void setErrorMessage(DroolsExchange droolsExchange, ErrorMessage errorMessage) {

    }

    @Override
    public void setProxyCheckDone(DroolsExchange droolsExchange, boolean proxyCheckDone) {

    }
}
