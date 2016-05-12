package org.kuali.ole.oleng.common.service.impl;

import org.kuali.ole.cache.Memoize;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.oleng.common.service.OleNgCommonMemorizeService;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.utility.DonorUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SheikS on 5/10/2016.
 */
@Component
public class OleNgCommonMemorizeServiceImpl extends BusinessObjectServiceHelperUtil implements OleNgCommonMemorizeService {

    private DonorUtil donorUtil;

    @Memoize
    public OleLocation getLocationByCode(String locationCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("locationCode", locationCode);
        List<OleLocation> locations = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, map);
        if (locations.size() > 0) {
            return locations.get(0);
        }
        return null;
    }

    @Memoize
    public OLEDonor getDonorCode(String donorCode) {
        return getDonorUtil().getDonorCode(donorCode);
    }

    public DonorUtil getDonorUtil() {
        if(null == donorUtil) {
            donorUtil = new DonorUtil();
        }
        return donorUtil;
    }
}
