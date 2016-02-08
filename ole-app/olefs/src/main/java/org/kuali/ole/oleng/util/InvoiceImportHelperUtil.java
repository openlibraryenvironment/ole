package org.kuali.ole.oleng.util;

import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 1/27/2016.
 */
public class InvoiceImportHelperUtil extends BusinessObjectServiceHelperUtil {

    public String getCurrencyTypeIdByCurrencyType(String currencyType){
        Map<String,String> currencyTypeMap = new HashMap<>();
        currencyTypeMap.put(org.kuali.ole.OLEConstants.OLEBatchProcess.CURRENCY_TYPE,currencyType);
        List<OleCurrencyType> currencyTypeList = (List) getBusinessObjectService().findMatching(OleCurrencyType.class, currencyTypeMap);
        return currencyTypeList.get(0).getCurrencyTypeId().toString();
    }

}
