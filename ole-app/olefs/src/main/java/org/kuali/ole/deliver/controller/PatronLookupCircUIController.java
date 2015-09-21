package org.kuali.ole.deliver.controller;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.util.ErrorMessage;

/**
 * Created by chenchulakshmig on 8/25/15.
 */
public class PatronLookupCircUIController extends PatronLookupCircBaseController {

    private CircForm getCircForm(DroolsExchange droolsExchange){
        return (CircForm) droolsExchange.getContext().get("circForm");
    }

    @Override
    public OlePatronDocument getPatronDocument(DroolsExchange droolsExchange){
        return getCircForm(droolsExchange).getPatronDocument();
    }

    @Override
    public void setPatronDocument(DroolsExchange droolsExchange, OlePatronDocument patronDocument) {
        getCircForm(droolsExchange).setPatronDocument(patronDocument);
    }

    @Override
    public String getPatronBarcode(DroolsExchange droolsExchange) {
        return getCircForm(droolsExchange).getPatronBarcode();
    }

    @Override
    public void setErrorMessage(DroolsExchange droolsExchange, ErrorMessage errorMessage) {
        getCircForm(droolsExchange).setErrorMessage(errorMessage);
    }

    @Override
    public void setProxyCheckDone(DroolsExchange droolsExchange, boolean proxyCheckDone) {
        getCircForm(droolsExchange).setProxyCheckDone(proxyCheckDone);
    }
}



