package org.kuali.ole.select.keyvalue;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenchulakshmig on 3/18/15.
 */
public class OLEVendorValuesBuilder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        return null;
    }

    public static List<String> retrieveVendorDetailsForSuggest(String vendorVal) {
        List<VendorDetail> vendorDetails = (List<VendorDetail>) SpringContext.getBean(KeyValuesService.class).findAll(
                VendorDetail.class);
        List<String> vendorValues = new ArrayList<String>();
        for (VendorDetail vendorDetail : vendorDetails) {
            vendorValues.add(vendorDetail.getVendorName());
        }
        Pattern pattern = Pattern.compile("[?$(){}\\[\\]\\^\\\\]");
        Matcher matcher = pattern.matcher(vendorVal);
        if (matcher.matches()) {
            return new ArrayList<String>();
        }
        if (!vendorVal.equalsIgnoreCase("*")) {
            vendorValues = Lists.newArrayList(Collections2.filter(vendorValues, Predicates.contains(Pattern.compile(vendorVal, Pattern.CASE_INSENSITIVE))));
        }
        Collections.sort(vendorValues);
        return vendorValues;
    }
}
