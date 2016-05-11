package org.kuali.ole.utility;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.select.bo.OLEDonor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 5/10/2016.
 */
public class DonorUtil extends BusinessObjectServiceHelperUtil {
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
