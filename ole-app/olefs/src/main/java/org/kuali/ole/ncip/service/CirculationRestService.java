package org.kuali.ole.ncip.service;

import org.kuali.ole.ncip.bo.OLERenewItemList;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
public interface CirculationRestService {
    public OLERenewItemList renewItems(String patronBarcode, String operator, List<String> itemBarcodes);
}
