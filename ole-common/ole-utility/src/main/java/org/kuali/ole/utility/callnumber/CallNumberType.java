package org.kuali.ole.utility.callnumber;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 20/2/13
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public enum CallNumberType {

    LCC("LCC", "LCC - Library of Congress classification"),
    DDC("DDC", "DDC - Dewey Decimal classification"),
    NLM("NLM", "NLM - National Library of Medicine classification"),
    SuDoc("SuDoc", "SuDoc - Superintendent of Documents classification"),
    FOUR("FOUR", "4 - Shelving control number"),
    FIVE("FIVE", "5 - Title"),
    SIX("SIX", "6 - Shelved separately"),
    SEVEN("SEVEN", "7 - Source specified in subfield $2"),
    EIGHT("EIGHT", "8 - Other scheme");

    private final String code;
    private final String description;
    public static Set<String> validCallNumberTypeCodeSet = getValidCallNumberTypeCodeSet();

    private CallNumberType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEqualTo(String input) {
        boolean result = false;
        if (null == input) {
            result = false;
        } else if (input.equalsIgnoreCase(getCode())) {
            result = true;
        } else if (input.equalsIgnoreCase(getDescription())) {
            result = true;
        }
        return result;
    }

    private static Set<String> getValidCallNumberTypeCodeSet() {
        validCallNumberTypeCodeSet = new HashSet<String>();
        validCallNumberTypeCodeSet.add(CallNumberType.LCC.getCode());
        validCallNumberTypeCodeSet.add(CallNumberType.DDC.getCode());
        validCallNumberTypeCodeSet.add(CallNumberType.SuDoc.getCode());
        validCallNumberTypeCodeSet.add(CallNumberType.NLM.getCode());
        validCallNumberTypeCodeSet.add(CallNumberType.FOUR.getCode());
        validCallNumberTypeCodeSet.add(CallNumberType.FIVE.getCode());
        validCallNumberTypeCodeSet.add(CallNumberType.SIX.getCode());
        validCallNumberTypeCodeSet.add(CallNumberType.SEVEN.getCode());
        validCallNumberTypeCodeSet.add(CallNumberType.EIGHT.getCode());
        return validCallNumberTypeCodeSet;
    }
}
