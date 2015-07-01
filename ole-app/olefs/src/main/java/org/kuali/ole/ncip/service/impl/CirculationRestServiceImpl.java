package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.ncip.bo.OLERenewItemList;
import org.kuali.ole.ncip.service.CirculationRestService;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
public class CirculationRestServiceImpl implements CirculationRestService {

    @Override
    public OLERenewItemList renewItems(String patronBarcode, String operator, List<String> itemBarcodes) {
        return new RenewItemsService().renewItems(patronBarcode,operator,itemBarcodes);
    }

}
