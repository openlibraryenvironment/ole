package org.kuali.ole.dsng.rest.handler;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.rest.Exchange;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public abstract class  Handler {

    BibDAO bibDAO;

    public abstract Boolean isInterested(String operation);

    public abstract void process(JSONObject requestJsonObject, Exchange exchange);

    protected Timestamp getDateTimeStamp(String updatedDateString) {
        Timestamp timeStamp = null;
        try {
            Date parse = DocstoreConstants.DOCSTORE_DATE_FORMAT.parse(updatedDateString);
            if (null != parse) {
                timeStamp = new Timestamp(parse.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public BibDAO getBibDAO() {
        return bibDAO;
    }

    public List<String> getOperationsList(String operation){
        List ops = new ArrayList();
        try {
            ops = new ObjectMapper().readValue(operation, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ops;

    }

    public void setBibDAO(BibDAO bibDAO) {
        this.bibDAO = bibDAO;
    }
}
