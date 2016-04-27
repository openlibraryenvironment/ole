package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;

import java.util.List;

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
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String staffOnlyString = listFromJSONArray.get(0);
            BibRecord bibRecord = (BibRecord) exchange.get(OleNGConstants.BIB);
            boolean staffOnly = (StringUtils.isNotBlank(staffOnlyString) && StringUtils.equalsIgnoreCase(staffOnlyString,"Y") ? true : false);
            bibRecord.setStaffOnlyFlag(staffOnly);
            exchange.add(OleNGConstants.BIB_RECORD, bibRecord);
        }
    }
}
