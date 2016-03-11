package org.kuali.ole.dsng.rest.handler;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by pvsubrah on 12/23/15.
 */
public abstract class Handler extends OleDsHelperUtil {

    protected List<Handler> metaDataHandlers;

    BibDAO bibDAO;

    HoldingDAO holdingDAO;

    ItemDAO itemDAO;

    BusinessObjectService businessObjectService;

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
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeStamp;
    }

    public BibDAO getBibDAO() {
        return bibDAO;
    }

    public void setBibDAO(BibDAO bibDAO) {
        this.bibDAO = bibDAO;
    }

    public HoldingDAO getHoldingDAO() {
        return holdingDAO;
    }

    public void setHoldingDAO(HoldingDAO holdingDAO) {
        this.holdingDAO = holdingDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
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
                    handler.processDataMappings(dataMapping, exchange);
                }
            }
        }
    }

    public List<Handler> getMetaDataHandlers() {
        return new ArrayList<Handler>();
    }

    public List<String> parseCommaSeperatedValues(String value){
        List values = new ArrayList();

        StringTokenizer stringTokenizer = new StringTokenizer(value, ",");
        while(stringTokenizer.hasMoreTokens()){
            values.add(stringTokenizer.nextToken());
        }
        return values;
    }
}
