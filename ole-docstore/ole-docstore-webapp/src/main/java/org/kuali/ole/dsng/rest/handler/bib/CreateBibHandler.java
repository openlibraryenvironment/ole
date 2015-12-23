package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.BibDAOImpl;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.service.OleDsNGDAOProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class CreateBibHandler extends Handler {
    @Override
    public Boolean isInterested(String operation) {
        try {
            List ops = new ObjectMapper().readValue(operation, new TypeReference<List<String>>() {
            });

            for (Iterator iterator = ops.iterator(); iterator.hasNext(); ) {
                String op = (String) iterator.next();
                return op.equals("111") || op.equals("211");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        try {
            String newBibContent = requestJsonObject.getString("unmodifiedContent");
            String updatedBy = requestJsonObject.getString("updatedBy");
            String updatedDateString = (String) requestJsonObject.get("updatedDate");

            BibRecord bibRecord = new BibRecord();
            bibRecord.setContent(newBibContent);
            bibRecord.setStatusUpdatedBy(updatedBy);

            Timestamp updatedDate = null;
            if (StringUtils.isNotBlank(updatedDateString)) {
                updatedDate = getDateTimeStamp(updatedDateString);
            }

            bibRecord.setStatusUpdatedDate(updatedDate);
            BibDAO bibDAO = new BibDAOImpl();
            BibRecord updatedBibRecord = bibDAO.save(bibRecord);
            exchange.add("bib", updatedBibRecord);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
