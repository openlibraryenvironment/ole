package org.kuali.ole.dsng.rest.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.dsng.dao.BibValidationDao;
import org.kuali.ole.dsng.service.OleDsNGMemorizeService;
import org.kuali.ole.dsng.service.impl.OleDsNGMemorizeServiceImpl;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by pvsubrah on 12/23/15.
 */
public abstract class Handler extends OleDsHelperUtil {

    protected static final Logger LOG = Logger.getLogger(Handler.class);

    protected CopyOnWriteArrayList<Handler> metaDataHandlers;

    OleDsNGMemorizeService oleDsNGMemorizeService;

    BusinessObjectService businessObjectService;


    BibValidationDao bibValidationDao;

    public abstract Boolean isInterested(String operation);

    public abstract void process(JSONObject requestJsonObject, Exchange exchange);

    protected Timestamp getDateTimeStamp(String updatedDateString) {
        Timestamp timeStamp = null;
        if (StringUtils.isNotBlank(updatedDateString)) {
            try {
                Date parse = DocstoreConstants.DOCSTORE_DATE_FORMAT.parse(updatedDateString);
                if (null != parse) {
                    timeStamp = new Timestamp(parse.getTime());
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return timeStamp;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OleDsNGMemorizeService getOleDsNGMemorizeService() {
        if(null == oleDsNGMemorizeService) {
            oleDsNGMemorizeService = new OleDsNGMemorizeServiceImpl();
        }
        return oleDsNGMemorizeService;
    }

    public void setOleDsNGMemorizeService(OleDsNGMemorizeService oleDsNGMemorizeService) {
        this.oleDsNGMemorizeService = oleDsNGMemorizeService;
    }

    public void processDataMappings(JSONObject jsonObject, Exchange exchange) throws JSONException, IOException {
        JSONObject dataMapping = (JSONObject) exchange.get(OleNGConstants.DATAMAPPING);
        Map<String, Object> dataMappingsMap = new ObjectMapper().readValue(dataMapping.toString(), new TypeReference<Map<String, Object>>() {
        });

        for (Iterator dataMappingsIterator = dataMappingsMap.keySet().iterator(); dataMappingsIterator.hasNext(); ) {
            String key1 = (String) dataMappingsIterator.next();
            for (Iterator<Handler> itemMetatDataHandlerIterator = getMetaDataHandlers().iterator(); itemMetatDataHandlerIterator.hasNext(); ) {
                Handler handler = itemMetatDataHandlerIterator.next();
                if (handler.isInterested(key1)) {
                    handler.setBusinessObjectService(getBusinessObjectService());
                    handler.setOleDsNGMemorizeService(getOleDsNGMemorizeService());
                    handler.processDataMappings(dataMapping, exchange);
                }
            }
        }
    }

    public CopyOnWriteArrayList<Handler> getMetaDataHandlers() {
        return new CopyOnWriteArrayList<Handler>();
    }

    public List<String> parseCommaSeperatedValues(String value){
        List values = new ArrayList();

        StringTokenizer stringTokenizer = new StringTokenizer(value, ",");
        while(stringTokenizer.hasMoreTokens()){
            values.add(stringTokenizer.nextToken());
        }
        return values;
    }


    public boolean isDiscardedByAdditionalOverlayOps(Set<String> discardedIdsForAdditionalOverlayOps, String id) {
        return CollectionUtils.isNotEmpty(discardedIdsForAdditionalOverlayOps) ? discardedIdsForAdditionalOverlayOps.contains(id) : false;
    }

    public BibValidationDao getBibValidationDao() {
        if(null == bibValidationDao) {
            bibValidationDao = (BibValidationDao) org.kuali.ole.dsng.service.SpringContext.getBean("bibValidationDao");
        }

        return bibValidationDao;
    }

    public void setBibValidationDao(BibValidationDao bibValidationDao) {
        this.bibValidationDao = bibValidationDao;
    }
}
