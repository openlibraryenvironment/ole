package org.kuali.ole.dsng.rest.handler.bib;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.dao.BibValidationDao;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 1/21/16.
 */
public class DiscardBibHandler extends BibHandler{

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if(op.equals("113")){
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        if (requestJsonObject.has(OleNGConstants.ID)) {
            try {
                String bibId = requestJsonObject.getString(OleNGConstants.ID);
                BibRecord bibRecord = getOleDsNGMemorizeService().getBibDAO().retrieveBibById(bibId);
                BibValidationDao bibValidationDao = (BibValidationDao) org.kuali.ole.dsng.service.SpringContext.getBean("bibValidationDao");

                processIfDeleteAllExistOpsFound(bibRecord, requestJsonObject, exchange);

                exchange.add(OleNGConstants.BIB, bibRecord);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
