package org.kuali.ole.dsng.rest.handler.bib;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.CreateHoldingsProcessor;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class CreateBibHandler extends BibHandler {

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("111") || op.equals("211")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        String newBibContent = null;
        try {
            newBibContent = requestJsonObject.getString(OleNGConstants.MODIFIED_CONTENT);
            String createdBy = requestJsonObject.getString(OleNGConstants.UPDATED_BY);
            String createdDateString = (String) requestJsonObject.get(OleNGConstants.UPDATED_DATE);

            BibRecord bibRecord = (BibRecord) exchange.get(OleNGConstants.BIB);

            bibRecord.setContent(newBibContent);
            bibRecord.setCreatedBy(createdBy);
            bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC);

            Timestamp createdDate = getDateTimeStamp(createdDateString);

            bibRecord.setDateCreated(createdDate);
            BibRecord createdBibRecord = getBibDAO().save(bibRecord);

            String modifiedcontent = process001And003(newBibContent, createdBibRecord.getBibId());
            bibRecord.setContent(modifiedcontent);

            setDataMappingValues(bibRecord, requestJsonObject, exchange);

            getBibDAO().save(bibRecord);

//            exchange.add(OleNGConstants.BIB, createdBibRecord);

//                createHoldings(requestJsonObject, exchange);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createHoldings(JSONObject requestJsonObject, Exchange exchange) {
        CreateHoldingsProcessor createHoldingsProcessor = new CreateHoldingsProcessor();
        createHoldingsProcessor.setHoldingDAO(getHoldingDAO());
        createHoldingsProcessor.setItemDAO(getItemDAO());
        createHoldingsProcessor.setBusinessObjectService(getBusinessObjectService());
        createHoldingsProcessor.processHoldings(requestJsonObject, exchange);
    }
}
