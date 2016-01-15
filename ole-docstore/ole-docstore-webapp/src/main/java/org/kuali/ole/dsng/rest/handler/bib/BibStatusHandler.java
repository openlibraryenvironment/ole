package org.kuali.ole.dsng.rest.handler.bib;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.Exchange;

/**
 * Created by SheikS on 1/5/2016.
 */
public class BibStatusHandler extends BibHandler {

    private final String TYPE = "Bib Status";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }


    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        // Todo : Need to check match point.
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String bibStatus = getStringValueFromJsonObject(requestJsonObject, TYPE);
        BibRecord bibRecord = (BibRecord) exchange.get(OleNGConstants.BIB);
        bibRecord.setStatus(bibStatus);
        exchange.add(OleNGConstants.BIB_RECORD, bibRecord);
    }
}
