package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.util.Languages008;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 9/13/13
 * Time: 7:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Langauge008KeyValueFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<>();
        return options;
    }
    public static List<String> retrieveControlField008Language(String languageVal) {
        List<String> reqlanguage  =  Languages008.getInstance().getLanguageDescription(languageVal);
        return reqlanguage;
    }
}
