package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
        getMarcRecordUtil().updateControlFieldValue(record,"001",bibId);
    }

    public void replaceOrganizationCodeTo003Tag(Record record) {
        String controlField003Value = getMarcRecordUtil().getControlFieldValue(record, "003");
        String organizationCode = ConfigContext.getCurrentContextConfig().getProperty("organization.marc.code");
        if(StringUtils.isBlank(controlField003Value)) {
            getMarcRecordUtil().updateControlFieldValue(record,"003",organizationCode);
        }
    }

    public BibRecord setDataMappingValues(BibRecord bibRecord, JSONObject requestJsonObject, Exchange exchange) {
        try {
            if (requestJsonObject.has("dataMapping")) {
                JSONObject dataMappings = requestJsonObject.getJSONObject("dataMapping");
                HashMap dataMappingsMap = new ObjectMapper().readValue(dataMappings.toString(), new TypeReference<Map<String, String>>() {});
                for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
                    String key1 = (String) iterator3.next();
                    for (Iterator<BibHandler> iterator4 = getBibMetaDetaHandler().iterator(); iterator4.hasNext(); ) {
                        BibHandler bibHandler = iterator4.next();
                        if (bibHandler.isInterested(key1)) {
                            bibHandler.setBusinessObjectService(getBusinessObjectService());
                            bibHandler.processDataMappings(dataMappings, exchange);
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
