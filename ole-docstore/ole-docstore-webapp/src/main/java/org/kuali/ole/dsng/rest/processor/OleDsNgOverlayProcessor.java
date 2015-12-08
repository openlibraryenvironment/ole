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

        JSONArray jsonArray = new JSONArray(jsonBody);
        for(int index = 0 ; index < jsonArray.length() ; index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            String bibId = jsonObject.getString(LOCALID_DISPLAY);
            BibRecord bibRecord = bibDAO.retrieveBibById(bibId);
            if(null != bibRecord) {
                //TODO : process bib record with overlay
                return getObjectMapper().writeValueAsString(bibDAO.save(bibRecord));
            } else {
                // TODO : need to handle if bib record is not found
            }
        }
        return null;
    }
}
