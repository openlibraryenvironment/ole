package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/9/2015.
 */
public class BatchHoldingFileProcessor extends BatchFileProcessor {
    @Override
    public String processRecords(List<Record> records,String profileName) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        List results = getSolrRequestReponseHandler().getSolrDocumentList("(DocType:holdings AND Level1Location_search:UC AND bibIdentifier:wbm-10000058)"); // Todo :  Need to form query
        if (null != results && results.size() == 1) {
            JSONObject jsonObject = new JSONObject();
            SolrDocument solrDocument = (SolrDocument) results.get(0);
            jsonObject.put("id", solrDocument.getFieldValue("LocalId_display"));
            jsonObject.put("updatedBy", getUpdatedUserName());
            jsonObject.put("updatedDate", DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
            jsonArray.put(jsonObject);
        }

        return getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_HOLDING, jsonArray, OleDsNgRestClient.Format.JSON);
    }
}
