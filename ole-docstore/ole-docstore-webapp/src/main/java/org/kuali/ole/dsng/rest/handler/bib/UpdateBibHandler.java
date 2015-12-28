package org.kuali.ole.dsng.rest.handler.bib;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class UpdateBibHandler extends Handler {
    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getOperationsList(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if(op.equals("112") || op.equals("212")){
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        try {
            String newBibContent = requestJsonObject.getString("modifiedContent");
            String updatedBy = requestJsonObject.getString("updatedBy");
            String updatedDateString = (String) requestJsonObject.get("updatedDate");

            if (requestJsonObject.has("id")) {
                String bibId = requestJsonObject.getString("id");
                BibRecord bibRecord = getBibDAO().retrieveBibById(bibId);
                bibRecord.setContent(newBibContent);
                bibRecord.setStatusUpdatedBy(updatedBy);
                bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC);

                Timestamp updatedDate = getDateTimeStamp(updatedDateString);

                bibRecord.setStatusUpdatedDate(updatedDate);
                BibRecord updatedBibRecord = getBibDAO().save(bibRecord);
                exchange.add("bib", updatedBibRecord);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
