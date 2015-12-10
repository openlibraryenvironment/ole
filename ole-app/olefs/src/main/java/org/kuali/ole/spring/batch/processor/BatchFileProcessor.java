package org.kuali.ole.spring.batch.processor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.codehaus.jettison.json.JSONException;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
            String responseData = processRecords(records);
            LOG.info("Response Data : " + responseData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract String processRecords(List<Record> records) throws JSONException;

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
