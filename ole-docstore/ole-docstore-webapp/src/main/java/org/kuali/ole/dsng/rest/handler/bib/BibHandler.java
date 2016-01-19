package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.marc.Record;

import java.io.IOException;
import java.util.*;

/**
 * Created by pvsubrah on 1/4/16.
 */
public abstract class BibHandler extends Handler {

    private List<BibHandler> bibMetaDetaHandler;

    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange){

    }

    public String process001And003(String marcContent, String bibId) {
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(marcContent);
        if(CollectionUtils.isNotEmpty(records)) {
            Record record = records.get(0);
            replaceBibIdTo001Tag(record,bibId);
            replaceOrganizationCodeTo003Tag(record);
            return getMarcRecordUtil().convertMarcRecordToMarcContent(record);
        }
        return marcContent;
    }

    public void replaceBibIdTo001Tag(Record record,String bibId) {
        if(getMarcRecordUtil().hasField(record, OleNGConstants.TAG_001)) {
            getMarcRecordUtil().updateControlFieldValue(record,OleNGConstants.TAG_001,bibId);
        } else {
            // If 001 tag is not available creating tag
            getMarcRecordUtil().addControlField(record,OleNGConstants.TAG_001,bibId);
        }
    }

    public void replaceOrganizationCodeTo003Tag(Record record) {
        String organizationCode = ConfigContext.getCurrentContextConfig().getProperty("organization.marc.code");
        if(getMarcRecordUtil().hasField(record,OleNGConstants.TAG_003)) {
            String controlField003Value = getMarcRecordUtil().getControlFieldValue(record, OleNGConstants.TAG_003);
            if(StringUtils.isBlank(controlField003Value)) {
                getMarcRecordUtil().updateControlFieldValue(record,OleNGConstants.TAG_003,organizationCode);
            }
        } else {
            // If 003 tag is not available creating tag
            getMarcRecordUtil().addControlField(record,OleNGConstants.TAG_003,organizationCode);
        }
    }

    public BibRecord setDataMappingValues(BibRecord bibRecord, JSONObject requestJsonObject, Exchange exchange) {
        try {
            if (requestJsonObject.has(OleNGConstants.DATAMAPPING)) {
                JSONArray dataMappings = requestJsonObject.getJSONArray(OleNGConstants.DATAMAPPING);
                if(dataMappings.length() > 0) {
                    JSONObject dataMapping = (JSONObject) dataMappings.get(0);
                    Map<String, Object> dataMappingsMap = new ObjectMapper().readValue(dataMapping.toString(), new TypeReference<Map<String, Object>>() {});
                    for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
                        String key1 = (String) iterator3.next();
                        for (Iterator<BibHandler> iterator4 = getBibMetaDetaHandler().iterator(); iterator4.hasNext(); ) {
                            BibHandler bibHandler = iterator4.next();
                            if (bibHandler.isInterested(key1)) {
                                bibHandler.setBusinessObjectService(getBusinessObjectService());
                                bibHandler.processDataMappings(dataMapping, exchange);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bibRecord;
    }

    public List<BibHandler> getBibMetaDetaHandler() {
        if(null == bibMetaDetaHandler) {
            bibMetaDetaHandler = new ArrayList<BibHandler>();
            bibMetaDetaHandler.add(new BibStatusHandler());
            bibMetaDetaHandler.add(new StaffOnlyHandler());
        }
        return bibMetaDetaHandler;
    }
}
