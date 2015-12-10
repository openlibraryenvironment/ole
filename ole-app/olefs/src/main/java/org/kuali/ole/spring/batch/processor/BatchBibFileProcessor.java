package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
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

    private static final String FORWARD_SLASH = "/";

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

                        JSONObject bibData = new JSONObject();
                        bibData.put("id", solrDocument.getFieldValue("LocalId_display"));
                        bibData.put("content", generateMARCXMLContent(marcRecord));
                        bibData.put("updatedBy", updatedUserName);
                        bibData.put("updatedDate", updatedDate);


                        //Holdings data
                        String locationLevel1 = "UC";
                        String locationLevel2 = null;
                        String locationLevel3 = "UCX";
                        String locationLevel4 = null;
                        String locationLevel5 = "InProc";

                        String location = formLocation(locationLevel1, locationLevel2, locationLevel3,
                                locationLevel4, locationLevel5);


                        JSONObject holdingsData = new JSONObject();
                        holdingsData.put("location", location);
                        holdingsData.put("callNumberType", "LCC - Library Of Congress classification");
                        bibData.put("holdings", holdingsData);

                        jsonArray.put(bibData);
                    }
                }

            }
        }
        return getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_BIB_HOLDING, jsonArray, OleDsNgRestClient.Format.JSON);
    }

    public String formLocation(String locationLevel1, String locationLevel2, String locationLevel3,
                               String locationLevel4, String locationLevel5) {
        StringBuilder location = new StringBuilder();

        if (StringUtils.isNotBlank(locationLevel1)) {
            appendLocationToStrinBuilder(location, locationLevel1);
        }
        if (StringUtils.isNotBlank(locationLevel2)) {
            appendLocationToStrinBuilder(location, locationLevel2);
        }
        if (StringUtils.isNotBlank(locationLevel3)) {
            appendLocationToStrinBuilder(location, locationLevel3);
        }
        if (StringUtils.isNotBlank(locationLevel4)) {
            appendLocationToStrinBuilder(location, locationLevel4);
        }
        if (StringUtils.isNotBlank(locationLevel5)) {
            appendLocationToStrinBuilder(location, locationLevel5);
        }

        return location.toString();
    }

    private void appendLocationToStrinBuilder(StringBuilder stringBuilder, String location) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append(FORWARD_SLASH).append(location);
        } else {
            stringBuilder.append(location);
        }
    }
}
