package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;

import java.util.List;

/**
 * Created by SheikS on 1/5/2016.
 */
public class BibStatusHandler extends BibHandler {

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BIB_STATUS);
    }


    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        // Todo : Need to check match point.
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BIB_STATUS);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String bibStatus = listFromJSONArray.get(0);
            BibRecord bibRecord = (BibRecord) exchange.get(OleNGConstants.BIB);
            bibRecord.setStatus(bibStatus);
            exchange.add(OleNGConstants.BIB_STATUS_UPDATED,Boolean.TRUE);
            exchange.add(OleNGConstants.BIB_RECORD, bibRecord);
        }
    }
}
