package org.kuali.ole.dsng.rest.processor;

import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collections;

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

    public String processOverlayForBib(String jsonBody) {
        JSONArray responseJsonArray = null;
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for(int index = 0 ; index < requestJsonArray.length() ; index++) {
                JSONObject jsonObject = requestJsonArray.getJSONObject(index);

                String solrDocuemntContent = jsonObject.getString("solrDocument");
                SolrDocument deserializedDocument = getObjectMapper().readValue(solrDocuemntContent, SolrDocument.class);
                SolrInputDocument solrInputDocument = ClientUtils.toSolrInputDocument(deserializedDocument);
                String bibId = (String) solrInputDocument.getFieldValue("LocalId_Display");
                BibRecord bibRecord = bibDAO.retrieveBibById(bibId);
                if(null != bibRecord) {
                    //TODO : process bib record with overlay
                    BibRecord savedBibRecord = bibDAO.save(bibRecord);
                    getBibIndexer().commitDocumentToSolr(Collections.singletonList(solrInputDocument));
                    JSONObject responseObject = new JSONObject();
                    responseObject.put("bibId",savedBibRecord.getBibId());
                    responseJsonArray.put(responseObject);
                } else {
                    // TODO : need to handle if bib record is not found
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }
}
