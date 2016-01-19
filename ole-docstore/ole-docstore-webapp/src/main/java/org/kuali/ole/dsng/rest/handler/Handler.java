package org.kuali.ole.dsng.rest.handler;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONObject;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public abstract class Handler extends OleDsHelperUtil {

    public final static String LOCATION_LEVEL_1 = "Location Level1";
    public final static String LOCATION_LEVEL_2 = "Location Level2";
    public final static String LOCATION_LEVEL_3 = "Location Level3";
    public final static String LOCATION_LEVEL_4 = "Location Level4";
    public final static String LOCATION_LEVEL_5 = "Location Level5";

    public final static String HOLDINGS_LOCATION_LEVEL_1 = "Holdings Location Level1";
    public final static String HOLDINGS_LOCATION_LEVEL_2 = "Holdings Location Level2";
    public final static String HOLDINGS_LOCATION_LEVEL_3 = "Holdings Location Level3";
    public final static String HOLDINGS_LOCATION_LEVEL_4 = "Holdings Location Level4";
    public final static String HOLDINGS_LOCATION_LEVEL_5 = "Holdings Location Level5";

    public final static String CREATE = "Create";
    public final static String UPDATE = "Update";
    public final static String HOLDINGS_PROCESS_TYPE = "holdingsProcessType";
    public final static String ITEM_PROCESS_TYPE = "itemProcessType";

    BibDAO bibDAO;

    HoldingDAO holdingDAO;

    ItemDAO itemDAO;

    BusinessObjectService businessObjectService;

    public abstract Boolean isInterested(String operation);

    public abstract void process(JSONObject requestJsonObject, Exchange exchange);

    protected Timestamp getDateTimeStamp(String updatedDateString) {
        Timestamp timeStamp = null;
        try {
            Date parse = DocstoreConstants.DOCSTORE_DATE_FORMAT.parse(updatedDateString);
            if (null != parse) {
                timeStamp = new Timestamp(parse.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }


    public List<String> getOperationsList(String operation){
        List ops = new ArrayList();
        try {
            ops = new ObjectMapper().readValue(operation, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ops;

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
}