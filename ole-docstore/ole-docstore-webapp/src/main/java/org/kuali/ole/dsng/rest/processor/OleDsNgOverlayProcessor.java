package org.kuali.ole.dsng.rest.processor;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgOverlayProcessor extends OleDsHelperUtil implements DocstoreConstants {


    @Autowired
    BibDAO bibDAO;

    @Autowired
    HoldingDAO holdingDAO;

    @Autowired
    ItemDAO itemDAO;

    public String processOverlayForBib(String jsonBody) throws JSONException, IOException {

        JSONArray requestJsonArray = new JSONArray(jsonBody);
        JSONArray responseJsonArray = new JSONArray();
        for(int index = 0 ; index < requestJsonArray.length() ; index++) {
            JSONObject jsonObject = requestJsonArray.getJSONObject(index);
            String bibId = jsonObject.getString(LOCALID_DISPLAY);
            BibRecord bibRecord = bibDAO.retrieveBibById(bibId);
            if(null != bibRecord) {
                //TODO : process bib record with overlay
                BibRecord savedBibRecord = bibDAO.save(bibRecord);
                JSONObject responseObject = new JSONObject();
                responseObject.put("bibId",savedBibRecord.getBibId());
                responseJsonArray.put(responseObject);
            } else {
                // TODO : need to handle if bib record is not found
            }
        }
        return responseJsonArray.toString();
    }
}
