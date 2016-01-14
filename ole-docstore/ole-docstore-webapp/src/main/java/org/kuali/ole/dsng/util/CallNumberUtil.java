package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.utility.callnumber.CallNumberFactory;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pvsubrah on 1/4/16.
 */
public class CallNumberUtil extends BusinessObjectServiceHelperUtil {
    public String buildSortableCallNumber(String callNumber, String codeValue) {
        String shelvingOrder = "";
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                shelvingOrder = callNumberObj.getShelfKey();
            }
        }
        return shelvingOrder;
    }

    public CallNumberTypeRecord fetchCallNumberTypeRecordById(String callNumberTypeId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", callNumberTypeId);
        List<CallNumberTypeRecord> matching = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }
}
