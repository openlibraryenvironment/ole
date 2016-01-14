package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.StatisticalSearchRecord;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SheikS on 1/6/2016.
 */
public class StatisticalSearchCodeUtil extends BusinessObjectServiceHelperUtil {

    public StatisticalSearchRecord fetchStatisticalSearchRecordByCode(String statisticalSearchCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", statisticalSearchCode);
        List<StatisticalSearchRecord> matching = (List<StatisticalSearchRecord>) getBusinessObjectService().findMatching(StatisticalSearchRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }
}
