package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
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

import java.util.*;

/**
 * Created by SheikS on 12/9/2015.
 */
public class BatchBibFileProcessor extends BatchFileProcessor {

    @Override
    public String processRecords(List<Record> records) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record marcRecord = iterator.next();
            List<VariableField> dataFields = marcRecord.getVariableFields("980");
            for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
                DataField dataField = (DataField) variableFieldIterator.next();
                List<Subfield> subFields = dataField.getSubfields("a");
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    String matchPoint1 = subfield.getData();
                    SolrRequestReponseHandler solrRequestReponseHandler = new SolrRequestReponseHandler();
                    List results = solrRequestReponseHandler.getSolrDocumentList("mdf_980a:" + "\"" + matchPoint1 + "\"");
                    if (null != results && results.size() == 1) {
                        JSONObject bib = new JSONObject();
                        SolrDocument solrDocument = (SolrDocument) results.get(0);
                        String updatedUserName = getUpdatedUserName();
                        String updatedDate = DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date());

                        //Bib data

                        Map bibData = new HashMap<>();
                        bibData.put("id", solrDocument.getFieldValue("LocalId_display"));
                        bibData.put("content", generateMARCXMLContent(marcRecord));
                        bibData.put("updatedBy", updatedUserName);
                        bibData.put("updatedDate", updatedDate);


                        bib.put("bib", bibData);


                        JSONObject holdings = new JSONObject();
                        Map holdingsData = new HashedMap();
                        holdingsData.put("locationLevel1", "UC");
                        holdingsData.put("locationLevel3", "UCX");
                        holdingsData.put("locationLevel5", "InProc");
                        holdingsData.put("callNumberType", "LCC - Library Of Congress classification");


                        holdings.put("holdings", holdingsData);

                        //Holdings data

                        jsonArray.put(bibData);
                        jsonArray.put(holdingsData);
                    }
                }
            }

        }
        return getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_BIB_HOLDING, jsonArray, OleDsNgRestClient.Format.JSON);
    }
}
