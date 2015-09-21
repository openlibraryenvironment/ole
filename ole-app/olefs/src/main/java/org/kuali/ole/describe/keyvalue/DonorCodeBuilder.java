package org.kuali.ole.describe.keyvalue;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srirams on 19/3/15.font-size:13px;
 */
public class DonorCodeBuilder extends KeyValuesBase {
    public static List<KeyValue> docorDoceKeyValues = null;
    public static long timeLastRefreshed;
    public static int refreshInterval = 300;     // in seconds

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> donorCodeList = new ArrayList<KeyValue>();
        Collection<OLEDonor> donorCodes =
                KRADServiceLocator.getBusinessObjectService().findAll(OLEDonor.class);
        for (OLEDonor donor : donorCodes) {
            donorCodeList.add(new ConcreteKeyValue(donor.getDonorCode(), donor.getDonorCode()));
        }
        return donorCodeList;
    }

    public static List<String> retrieveDonorCodeDetailsForSuggest(String donorVal){
        List<KeyValue> donorCodeKeyValues = retrieveDonorCodeDetails();
        List<String> donorCodeValues = new ArrayList<String>();
        for (KeyValue keyValue : donorCodeKeyValues) {
            donorCodeValues.add(keyValue.getKey());
        }
        Pattern pattern = Pattern.compile("[?$(){}\\[\\]\\^\\\\]");
        Matcher matcher = pattern.matcher(donorVal);
        if (matcher.matches()) {
            return new ArrayList<String>();
        }
        if (!donorVal.equalsIgnoreCase("*")) {
            donorCodeValues = Lists.newArrayList(Collections2.filter(donorCodeValues, Predicates.contains(Pattern.compile(donorVal, Pattern.CASE_INSENSITIVE))));
        }
        Collections.sort(donorCodeValues);
        return donorCodeValues;
    }

    private static List<KeyValue> retrieveDonorCodeDetails() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (docorDoceKeyValues == null) {
            docorDoceKeyValues = initDonorCodeDetails();
            timeLastRefreshed = currentTime;
        } else {
            if (currentTime - timeLastRefreshed > refreshInterval) {
                docorDoceKeyValues = initDonorCodeDetails();
                timeLastRefreshed = currentTime;
            }
        }
        return docorDoceKeyValues;
    }

    public static List<KeyValue> initDonorCodeDetails() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OLEDonor> donors =
                KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OLEDonor.class,"donorCode",true);
        for (OLEDonor donor : donors) {
            if (donor.isActive()) {
                options.add(new ConcreteKeyValue(donor.getDonorCode(), donor.getDonorCode()));
            }
        }
        return options;
    }


}
