package org.kuali.ole.describe.keyvalue;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.kuali.ole.deliver.bo.OleFeeType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hemalathas on 2/18/16.
 */
public class OLEFeeTypeValueBuilder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        return null;
    }

    public static List<String> retrieveFeeTypeForSuggest(String feeTypeCode) {
    List<OleFeeType> feeTypes = (List<OleFeeType>) SpringContext.getBean(KeyValuesService.class).findAll(OleFeeType.class);
        List<String> feeTypeCodes = new ArrayList<String>();
        for(OleFeeType oleFeeType : feeTypes){
            feeTypeCodes.add(oleFeeType.getFeeTypeCode());
        }
        Pattern pattern = Pattern.compile("[?$(){}\\[\\]\\^\\\\]");
        Matcher matcher = pattern.matcher(feeTypeCode);
        if (matcher.matches()) {
            return new ArrayList<String>();
        }
        if (!feeTypeCode.equalsIgnoreCase("*")) {
            feeTypeCodes = Lists.newArrayList(Collections2.filter(feeTypeCodes, Predicates.contains(Pattern.compile(feeTypeCode, Pattern.CASE_INSENSITIVE))));
        }

        Collections.sort(feeTypeCodes, new SortIgnoreCase());
        return feeTypeCodes;
    }


    public static class SortIgnoreCase implements Comparator<Object> {
        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        }
    }
}
