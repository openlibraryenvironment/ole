package org.kuali.ole.select.document.service;


import org.kuali.ole.select.bo.OLEDonor;

import java.util.List;
import java.util.Map;

/**
 * Created by premkb on 3/15/16.
 */
public interface DonorEncumberReportDAOService {

    //public List<Map<String, Object>> getDonorEncumberList(Map<String,Object> criteria);

    public List<OLEDonor> getDonorEncumberList(Map<String, Object> criteria);

}
