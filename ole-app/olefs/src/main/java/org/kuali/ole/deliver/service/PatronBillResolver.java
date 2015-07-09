package org.kuali.ole.deliver.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.OleFeeType;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Palanivelrajanb on 7/7/2015.
 */
public class PatronBillResolver {

    private static final Logger LOG = Logger.getLogger(PatronBillResolver.class);

    public void checkReplacementFineExistForPatron(OleLoanDocument oleLoanDocument,String patronId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.ITEM_BARCODE, oleLoanDocument.getItemId());
        map.put(OLEConstants.FEE_TYPE_FIELD, getOleFeeTypeCode(OLEConstants.FEE_TYPE_CODE_REPL_FEE));
        List<FeeType> feeTypes = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, map);
        if (feeTypes != null && feeTypes.size() > 0) {
            for(FeeType feeType:feeTypes){
                if(feeType.getBalFeeAmount().isGreaterThan(new KualiDecimal(0.00)) && feeType.getPatronBillPayment()!=null &&
                        feeType.getPatronBillPayment().getPatronId().equals(patronId)){
                    oleLoanDocument.setReplacementFeeExist(true);
                    oleLoanDocument.setBillName(OLEConstants.REPLACEMENT_FEE);
                    break;
                }
            }
        }
    }

    private String getOleFeeTypeCode(String typeCode){
        Map<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.FEE_TYPE_CODE, typeCode);
        List<OleFeeType> feeTypes = (List<OleFeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(OleFeeType.class, map);
        if(CollectionUtils.isNotEmpty(feeTypes)){
            return feeTypes.get(0).getFeeTypeId();
        }
        return "";
    }

    public void checkReplacementFineExist(OleLoanDocument oleLoanDocument) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.ITEM_BARCODE, oleLoanDocument.getItemId());
        map.put(OLEConstants.FEE_TYPE_FIELD, getOleFeeTypeCode(OLEConstants.FEE_TYPE_CODE_REPL_FEE));
        List<FeeType> feeTypes = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, map);
        oleLoanDocument.setFeeType(feeTypes);
        if (feeTypes != null && feeTypes.size() > 0) {
            for(FeeType feeType:feeTypes){
                if(feeType.getBalFeeAmount().isGreaterThan(new KualiDecimal(0.00))){
                    oleLoanDocument.setReplacementFeeExist(true);
                    break;
                }
            }
        }
    }

    public void checkOverdueExist(OleLoanDocument oleLoanDocument) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.ITEM_BARCODE, oleLoanDocument.getItemId());
        map.put(OLEConstants.FEE_TYPE_FIELD, getOleFeeTypeCode(OLEConstants.FEE_TYPE_CODE_OVERDUE));
        List<FeeType> feeTypes = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, map);
        if (feeTypes != null && feeTypes.size() > 0) {
            for (FeeType feeType : feeTypes) {
                if (feeType.getBalFeeAmount().compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) != 0) {
                    oleLoanDocument.setOverdueFineExist(true);
                    break;
                }
            }
        }
    }

}
