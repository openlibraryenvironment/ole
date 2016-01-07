package org.kuali.ole.dsng.rest.handler.bib;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.CreateHoldingsProcessor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class CreateBibHandler extends BibHandler {

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getOperationsList(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if(op.equals("111") || op.equals("211")){
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        if (!requestJsonObject.has("id")) {
            try {
                String newBibContent = requestJsonObject.getString("unmodifiedContent");
                String createdBy = requestJsonObject.getString("updatedBy");
                String createdDateString = (String) requestJsonObject.get("updatedDate");

                BibRecord bibRecord = new BibRecord();
                bibRecord.setContent(newBibContent);
                bibRecord.setCreatedBy(createdBy);
                bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC);

                Timestamp createdDate = getDateTimeStamp(createdDateString);

                bibRecord.setDateCreated(createdDate);
                BibRecord updatedBibRecord = getBibDAO().save(bibRecord);

                String modifiedcontent = process001And003(newBibContent, updatedBibRecord.getBibId());
                bibRecord.setContent(modifiedcontent);

                bibRecord = setDataMappingValues(bibRecord,requestJsonObject,exchange);

                updatedBibRecord = getBibDAO().save(bibRecord);

                exchange.add("bib", updatedBibRecord);

                List bibCrated = (List) exchange.get("bibCrated");
                if(null == bibCrated) {
                    bibCrated = new ArrayList();
                }
                bibCrated.add(bibRecord);

                exchange.add("bibCrated",bibCrated);

                createHoldings(requestJsonObject, exchange);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void createHoldings(JSONObject requestJsonObject, Exchange exchange) {
        CreateHoldingsProcessor createHoldingsProcessor = new CreateHoldingsProcessor();
        createHoldingsProcessor.setHoldingDAO(getHoldingDAO());
        createHoldingsProcessor.setItemDAO(getItemDAO());
        createHoldingsProcessor.setBusinessObjectService(getBusinessObjectService());
        createHoldingsProcessor.processHoldings(requestJsonObject,exchange);
    }
}
