package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.bo.OLECheckInItem;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.converter.OLECheckInItemConverter;
import org.kuali.ole.ncip.service.NCIPCheckInItemResponseBuilder;
import org.kuali.ole.ncip.service.OLECheckInItemService;
import org.kuali.ole.ncip.util.OLENCIPUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenchulakshmig on 8/21/15.
 */
public class OLENCIPCheckInItemServiceImpl extends NonSip2CheckinItemServiceImplImpl implements OLECheckInItemService {

    private static final Logger LOG = Logger.getLogger(OLENCIPCheckInItemServiceImpl.class);

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = (DocstoreClientLocator) SpringContext.getService("docstoreClientLocator");
        }
        return docstoreClientLocator;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
    }

    @Override
    public CheckInItemResponseData performService(CheckInItemInitiationData checkInItemInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException {
        LOG.info("Calling performService in OLENCIPCheckInItemServiceImpl");
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();

        OLENCIPUtil olencipUtil = new OLENCIPUtil();
        NCIPCheckInItemResponseBuilder ncipCheckInItemResponseBuilder = new NCIPCheckInItemResponseBuilder();
        CheckInItemResponseData checkInItemResponseData = new CheckInItemResponseData();

        AgencyId agencyId = olencipUtil.validateAgency(checkInItemInitiationData.getInitiationHeader(), checkInItemResponseData);
        if (null == agencyId) return checkInItemResponseData;

        String itemBarcode = checkInItemInitiationData.getItemId().getItemIdentifierValue();
        String operatorId = olencipUtil.agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
        String itemDeleteIndicator = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLENCIPConstants.TEMP_ITEM_DELETE_INDICATOR);

        LOG.info("itemBarcode >>>" + itemBarcode);
        LOG.info("operatorId >>>" + operatorId);
        Map checkinParameters = new HashMap();
        checkinParameters.put("operatorId", operatorId);
        checkinParameters.put("itemBarcode", itemBarcode);
        checkinParameters.put("responseFormatType", "XML");
        checkinParameters.put("deleteIndicator", itemDeleteIndicator);

        String responseString = checkinItem(checkinParameters);
        OLECheckInItem oleCheckInItem = (OLECheckInItem) new OLECheckInItemConverter().generateCheckInItemObject(responseString);

        if (oleCheckInItem != null && StringUtils.isNotBlank(oleCheckInItem.getMessage())) {
            if (oleCheckInItem.getMessage().equals(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_CHECKED_IN))) {
                ncipCheckInItemResponseBuilder.setItemId(checkInItemResponseData, itemBarcode, agencyId, oleCheckInItem.getItemType());
                ncipCheckInItemResponseBuilder.setUserId(checkInItemResponseData, agencyId, oleCheckInItem);
                ncipCheckInItemResponseBuilder.setItemOptionalFields(checkInItemResponseData, oleCheckInItem);
            } else {
                String problemElement = "";
                String problemValue = "";
                if( oleCheckInItem.getCode().equals("014")) {
                    problemElement = OLENCIPConstants.ITEM;
                    problemValue = itemBarcode;
                }
                else if (oleCheckInItem.getCode().equals("026")) {
                    problemValue = operatorId;
                }
                else if(oleCheckInItem.getCode().equals("500")) {
                    problemValue = "CheckIn Failed";
                }
                olencipUtil.processProblems(checkInItemResponseData, problemValue, oleCheckInItem.getMessage(), problemElement);
            }
        } else {
            olencipUtil.processProblems(checkInItemResponseData, itemBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CHECK_IN_FAILED), OLENCIPConstants.ITEM);
        }

        oleStopWatch.end();
        LOG.info("Time taken to perform Checkin item service : " + oleStopWatch.getTotalTime());
        return checkInItemResponseData;
    }

}
