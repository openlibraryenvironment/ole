package org.kuali.ole.deliver.notice.valuesFinder;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 10/6/15.
 */
public class OleNoticeItemFieldLabelMappingKeyValuesFinder extends KeyValuesBase {


    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        String noticeType = null;
        if(GlobalVariables.getUserSession().getObjectMap()!=null){
          noticeType = (String)GlobalVariables.getUserSession().getObjectMap().get("noticeType");
        }

        keyValues.add(new ConcreteKeyValue(OLEConstants.TITLE_ITEM_INFORMATION,OLEConstants.TITLE_ITEM_INFORMATION));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_TITLE,OLEConstants.NOTICE_TITLE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_AUTHOR,OLEConstants.NOTICE_AUTHOR));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_COPY_NUMBER,OLEConstants.NOTICE_COPY_NUMBER));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_ENUMERATION,OLEConstants.NOTICE_ENUMERATION));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_CHRONOLOGY,OLEConstants.NOTICE_CHRONOLOGY));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_CALL_NUMBER,OLEConstants.NOTICE_CALL_NUMBER));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_ITEM_BARCODE,OLEConstants.NOTICE_ITEM_BARCODE));
        if(StringUtils.isNotEmpty(noticeType) && (noticeType.equals(OLEConstants.RECALL_NOTICE) || noticeType.equals(OLEConstants.ONHOLD_NOTICE) || noticeType.equals(OLEConstants.NOTICE_HOLD_COURTESY)  || noticeType.equals(OLEConstants.REQUEST_EXPIRATION_NOTICE))) {
            keyValues.add(new ConcreteKeyValue(OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME,OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME));
            keyValues.add(new ConcreteKeyValue(OLEConstants.CIRCULATION_REPLY_TO_EMAIL,OLEConstants.CIRCULATION_REPLY_TO_EMAIL));
            keyValues.add(new ConcreteKeyValue(OLEConstants.LIBRARY_SHELVING_LOCATION,OLEConstants.LIBRARY_SHELVING_LOCATION));
            keyValues.add(new ConcreteKeyValue(OLEConstants.LIBRARY_LOCATION,OLEConstants.LIBRARY_LOCATION));
         }
        if(StringUtils.isNotEmpty(noticeType) && noticeType.equals(OLEConstants.RECALL_NOTICE)){
        keyValues.add(new ConcreteKeyValue(OLEConstants.ORIGINAL_DUE_DATE,OLEConstants.ORIGINAL_DUE_DATE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NEW_DUE_DATE,OLEConstants.NEW_DUE_DATE));
        }
        if(StringUtils.isNotEmpty(noticeType) && noticeType.equals(OLEConstants.ONHOLD_NOTICE)){
            keyValues.add(new ConcreteKeyValue(OLEConstants.ITEM_WILL_BE_HELD_UNTIL,OLEConstants.ITEM_WILL_BE_HELD_UNTIL));
        }

        if(StringUtils.isNotEmpty(noticeType) && noticeType.equals(OLEConstants.OVERDUE_NOTICE)){
        keyValues.add(new ConcreteKeyValue(OLEConstants.ITEM_WAS_DUE,OLEConstants.ITEM_WAS_DUE));
        }
        if(StringUtils.isNotEmpty(noticeType) && noticeType.equals(OLEConstants.REQUEST_EXPIRATION_NOTICE)){
        keyValues.add(new ConcreteKeyValue(OLEConstants.HOLD_EXPIRATION_DATE,OLEConstants.HOLD_EXPIRATION_DATE));
        }
        if(StringUtils.isNotEmpty(noticeType) && noticeType.equals(OLEConstants.MISSING_PIECE_NOTICE)){
            keyValues.add(new ConcreteKeyValue(OLEConstants.MISSING_ITEM_CHECK_IN_DATE,OLEConstants.MISSING_ITEM_CHECK_IN_DATE));
            keyValues.add(new ConcreteKeyValue(OLEConstants.MISSING_ITEM_NOTE,OLEConstants.MISSING_ITEM_NOTE));
        }
        return keyValues;
    }
}
