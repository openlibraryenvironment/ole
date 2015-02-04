package org.kuali.ole.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.service.OverlayOutputService;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 2/22/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayTransactionOutputServiceImpl implements OverlayOutputService {

    @Override
    public void setOutPutValue(String field, String value, Object object) {
        if(object!=null && object instanceof OleTxRecord){
            OleTxRecord oleTxRecord = (OleTxRecord) object;
            if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_VENDOR_LINEITEM_IDENTIFIER)){
                oleTxRecord.setVendorItemIdentifier(value);
            }else if(field.equalsIgnoreCase(OLEPropertyConstants.ACCOUNT_NUMBER)){
                oleTxRecord.setAccountNumber(value);
            }else if(field.equalsIgnoreCase(OLEPropertyConstants.CHART_CODE)){
                oleTxRecord.setItemChartCode(value);
            }else if(field.equalsIgnoreCase(OLEPropertyConstants.OBJECT_CODE)){
                oleTxRecord.setObjectCode(value);
            }
        }
    }
    @Override
    public void persist(Object object) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}