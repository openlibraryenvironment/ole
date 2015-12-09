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
    public String customProcess(List<Record> records) throws JSONException {
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


       /* for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record marcRecord = iterator.next();
            List<VariableField> dataFields = marcRecord.getVariableFields("980"); // Todo :  Need to find match point for holdings
            for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
                DataField dataField = (DataField) variableFieldIterator.next();
                List<Subfield> subFields = dataField.getSubfields("a");
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    String matchPoint1 = subfield.getData();
                    if (null != results && results.size() == 1) {
                        JSONObject jsonObject = new JSONObject();
                        SolrDocument solrDocument = (SolrDocument) results.get(0);
                        jsonObject.put("id", solrDocument.getFieldValue("LocalId_display"));
                        jsonObject.put("content", generateMARCXMLContent(marcRecord));
                        jsonObject.put("updatedBy", getUpdatedUserName());
                        jsonObject.put("updatedDate", DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
                        jsonArray.put(jsonObject);
                    }
                }
            }

        }
        return getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_HOLDING, jsonArray, OleDsNgRestClient.Format.JSON);*/
    }
}
