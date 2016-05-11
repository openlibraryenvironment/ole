package org.kuali.ole.oleng.common.service;

import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.select.bo.OLEDonor;

/**
 * Created by SheikS on 5/10/2016.
 */
public interface OleNgCommonMemorizeService {
    public OleLocation getLocationByCode(String locationCode);
    public OLEDonor getDonorCode(String donorCode);
}
