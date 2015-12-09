package org.kuali.ole.spring.batch.processor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 12/7/15.
 */
public abstract class BatchFileProcessor extends BatchUtil {


    private static final Logger LOG = LoggerFactory.getLogger(BatchFileProcessor.class);

    public void processBatch(File file) {
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        try {
            String rawMarc = FileUtils.readFileToString(file);
            List<Record> records = marcXMLConverter.convertRawMarchToMarc(rawMarc);
            String responseData = customProcess(records);
            LOG.info("Response Data : " + responseData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract String customProcess(List<Record> records) throws JSONException;

    public String generateMARCXMLContent(Record marcRecord){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MarcWriter writer = new MarcXmlWriter(out);
        writer.write(marcRecord);
        writer.close();
        return new String(out.toByteArray());
    }

    public String getUpdatedUserName() {
        UserSession userSession = GlobalVariables.getUserSession();
        if(null != userSession) {
            return userSession.getPrincipalName();
        }
        return null;
    }
}
