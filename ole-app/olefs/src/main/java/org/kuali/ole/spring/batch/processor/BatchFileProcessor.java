package org.kuali.ole.spring.batch.processor;

import org.apache.commons.io.FileUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 12/7/15.
 */
public class BatchFileProcessor extends BatchUtil {


    private static final Logger LOG = LoggerFactory.getLogger(BatchFileProcessor.class);

    public void processBatch(File file) {
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        try {
            String rawMarc = FileUtils.readFileToString(file);
            List<Record> records = marcXMLConverter.convertRawMarchToMarc(rawMarc);
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
                            JSONObject jsonObject = new JSONObject();
                            SolrDocument value = (SolrDocument) results.get(0);
                            jsonObject.put("solrDocument", value);
                            jsonArray.put(jsonObject);
                        }
                    }
                }

            }

            String responseData = getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_BIB, jsonArray, OleDsNgRestClient.Format.JSON);
            LOG.info("Response Data : " + responseData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
