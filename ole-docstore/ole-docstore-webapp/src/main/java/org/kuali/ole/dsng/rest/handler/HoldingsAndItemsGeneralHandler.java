package org.kuali.ole.dsng.rest.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.select.bo.OLEDonor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 1/4/16.
 */
public abstract class HoldingsAndItemsGeneralHandler extends Handler {
    public HoldingsRecord getHoldingsRecordById(String holdingsId) {
        return getBusinessObjectService().findBySinglePrimaryKey(HoldingsRecord.class, holdingsId);
    }

    public OLEDonor getDonorCode(String donorCode) {
        if (StringUtils.isNotBlank(donorCode)) {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("donorCode", donorCode);
            List<OLEDonor> matching = (List<OLEDonor>) getBusinessObjectService().findMatching(OLEDonor.class, parameterMap);
            if(CollectionUtils.isNotEmpty(matching)) {
                return matching.get(0);
            }
        }
        return null;
    }
}
