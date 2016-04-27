package org.kuali.ole.batch.keyvalue;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OLEBatchProcessFundRecordProfileValueFinder extends KeyValuesBase {

    private static final Logger LOG = Logger.getLogger(OLEBatchProcessFundRecordProfileValueFinder.class);

    public static List<KeyValue> fundRecordKeyValues = null;

    public static long timeLastRefreshed;


    public static int refreshInterval = 300;     // in seconds


    private DocstoreUtil docstoreUtil;
    public DocstoreUtil getDocstoreUtil() {

        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);

        }
        return docstoreUtil;
    }
    @Override
    public List<KeyValue> getKeyValues() {

        List<KeyValue> options = initProfileDetailsForFundRecordImport();
        return options;
    }

    public static List<KeyValue> initProfileDetailsForFundRecordImport() {

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("batchProcessProfileType", "Fund Record Import");
        List<OLEBatchProcessProfileBo> oleBatchProcessProfileBo = (List<OLEBatchProcessProfileBo>) businessObjectService.findMatching(OLEBatchProcessProfileBo.class, parentCriteria1);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OLEBatchProcessProfileBo profileBo : oleBatchProcessProfileBo) {
            keyValues.add(new ConcreteKeyValue(profileBo.getBatchProcessProfileName(), profileBo.getBatchProcessProfileId()));
        }
        return keyValues;
    }

    public String getProfileName(String profileName){
        return profileName;
    }


    public static List<String> retrieveBatchProfileNamesForFundRecordImport(String fundRecordVal) {
        List<KeyValue> fundRecordKeyValues = retrieveProfileDetailsForFundRecordImport();
        List<String> fundRecordValues = new ArrayList<String>();
        for (KeyValue keyValue : fundRecordKeyValues) {
            fundRecordValues.add(keyValue.getKey());
        }
        Pattern pattern = Pattern.compile("[?$(){}\\[\\]\\^\\\\]");
        Matcher matcher = pattern.matcher(fundRecordVal);
        if (matcher.matches()) {
            return new ArrayList<String>();
        }

        if (!fundRecordVal.equalsIgnoreCase("*")) {
            fundRecordValues = Lists.newArrayList(Collections2.filter(fundRecordValues, Predicates.contains(Pattern.compile(fundRecordVal, Pattern.CASE_INSENSITIVE))));
        }
        Collections.sort(fundRecordValues);
        return fundRecordValues;
    }



    private static List<KeyValue> retrieveProfileDetailsForFundRecordImport() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (fundRecordKeyValues == null) {
            fundRecordKeyValues = initProfileDetailsForFundRecordImport();
            timeLastRefreshed = currentTime;
        } else {
            if (currentTime - timeLastRefreshed > refreshInterval) {
                fundRecordKeyValues = initProfileDetailsForFundRecordImport();
                timeLastRefreshed = currentTime;
            }
        }
        return fundRecordKeyValues;
    }

    public  static String getValue(String batchVal){
        String value="";
        fundRecordKeyValues=retrieveProfileDetailsForFundRecordImport();

        for (KeyValue keyValue : fundRecordKeyValues) {
            if(batchVal.equalsIgnoreCase(keyValue.getKey())){
                value =keyValue.getValue();
            }
        }
        return value;
    }


}
