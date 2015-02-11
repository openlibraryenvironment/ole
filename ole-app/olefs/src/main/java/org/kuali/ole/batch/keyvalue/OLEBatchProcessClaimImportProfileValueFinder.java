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

/**
 * Created by hemalathas on 12/31/14.
 */
public class OLEBatchProcessClaimImportProfileValueFinder extends KeyValuesBase {

    private static final Logger LOG = Logger.getLogger(OLEBatchProcessClaimImportProfileValueFinder.class);

    public static List<KeyValue> locationKeyValues = null;

    public static long timeLastRefreshed;


    public static int refreshInterval = 300;     // in seconds

    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments LevelCode and
     * LocationName.
     *
     * @return List<KeyValue>
     */

    private DocstoreUtil docstoreUtil;
    public DocstoreUtil getDocstoreUtil() {

        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);

        }
        return docstoreUtil;
    }
    @Override
    public List<KeyValue> getKeyValues() {

        List<KeyValue> options = initProfileDetailsForClaimReport();
        return options;
    }

    public static List<KeyValue> initProfileDetailsForClaimReport() {

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("batchProcessProfileType", "Claim Report");
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


    public static List<String> retrieveBatchProfileNamesForClaimReport(String locationVal) {
        List<KeyValue> locationKeyValues = retrieveProfileDetailsForClaimReport();
        List<String> locationValues = new ArrayList<String>();
        for (KeyValue keyValue : locationKeyValues) {
            locationValues.add(keyValue.getKey());
        }
        Pattern pattern = Pattern.compile("[?$(){}\\[\\]\\^\\\\]");
        Matcher matcher = pattern.matcher(locationVal);
        if (matcher.matches()) {
            return new ArrayList<String>();
        }

        if (!locationVal.equalsIgnoreCase("*")) {
            locationValues = Lists.newArrayList(Collections2.filter(locationValues, Predicates.contains(Pattern.compile(locationVal, Pattern.CASE_INSENSITIVE))));
        }
        Collections.sort(locationValues);
        return locationValues;
    }



    private static List<KeyValue> retrieveProfileDetailsForClaimReport() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (locationKeyValues == null) {
            locationKeyValues = initProfileDetailsForClaimReport();
            timeLastRefreshed = currentTime;
        } else {
            if (currentTime - timeLastRefreshed > refreshInterval) {
                locationKeyValues = initProfileDetailsForClaimReport();
                timeLastRefreshed = currentTime;
            }
        }
        return locationKeyValues;
    }

    public  static String getValue(String batchVal){
        String value="";
        locationKeyValues=retrieveProfileDetailsForClaimReport();

        for (KeyValue keyValue : locationKeyValues) {
            if(batchVal.equalsIgnoreCase(keyValue.getKey())){
                value =keyValue.getValue();
            }
        }
        return value;
    }


}
