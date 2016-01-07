package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.Exchange;

/**
 * Created by SheikS on 1/5/2016.
 */
public class StaffOnlyHandler extends BibHandler {

    private final String TYPE = "Staff Only";

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
        String staffOnlyString = getStringValueFromJsonObject(requestJsonObject, TYPE);
        BibRecord bibRecord = (BibRecord) exchange.get("bib");
        boolean staffOnly = (StringUtils.isNotBlank(staffOnlyString) && StringUtils.equalsIgnoreCase(staffOnlyString,"Y") ? true : false);
        bibRecord.setStaffOnlyFlag(staffOnly);
        exchange.add("bibRecord", bibRecord);
    }
}
