package org.kuali.ole.utility.callnumber;

import java.text.DecimalFormat;

public abstract class AbstractCallNumber implements CallNumber {

    public String getSortableKey(String callNumber) {
        return null;
    }

/*@Override
public String normalize(String callNumber) throws Exception {
    String recid=null;
	String normalisedLCCallNumber=CallNumUtils.getLCShelfkey(callNumber, null);
    return normalisedLCCallNumber;
}*/

/*public String normalize(String callNumber){

        //     LCCallNumber lcCallNumber=LCCallNumber.getInstance();
    String normalisedCallNumber    =null;
           // =lcCallNumber.normalize(callNumber);
  *//* CallNumber callNumber1 = BeanLocator.getCallNumberFactory()
            .getCallNumber(callNumberType.getCode());*//*

    *//*if(callNumber1.equals(CallNumberType.LC.getDescription()))  {
         normalisedCallNumber=normalizedCallNumber(callNumber);
    }*//*

	return normalisedCallNumber;

}*/
    /*public String normalizedCallNumber(String callNumber)   {
        String normalizedCallNumber=null;
        try{
        LCCallNumber lcCallNumber=LCCallNumber.getInstance();
         normalizedCallNumber=  lcCallNumber.normalize(callNumber);
        }catch (Exception e){
            e.printStackTrace();
        }

        return normalizedCallNumber;
    }*/

    public static String normalizeFloat(String floatStr, int digitsB4, int digitsAfter) {
        double value = Double.valueOf(floatStr).doubleValue();

        String formatStr = getFormatString(digitsB4) + '.' + getFormatString(digitsAfter);

        DecimalFormat normFormat = new DecimalFormat(formatStr);
        String norm = normFormat.format(value);
        if (norm.endsWith("."))
            norm = norm.substring(0, norm.length() - 1);
        return norm;
    }

    private static String getFormatString(int numDigits) {
        StringBuilder b4 = new StringBuilder();
        if (numDigits < 0)
            b4.append("############");
        else if (numDigits > 0) {
            for (int i = 0; i < numDigits; i++) {
                b4.append('0');
            }
        }
        return b4.toString();
    }

    public static String normalizeSuffix(String suffix) {
        if (suffix != null && suffix.length() > 0) {
            StringBuilder resultBuf = new StringBuilder(suffix.length());
            // get digit substrings
            String[] digitStrs = suffix.split("[\\D]+");
            int len = digitStrs.length;
            if (digitStrs != null && len != 0) {
                int s = 0;
                for (int d = 0; d < len; d++) {
                    String digitStr = digitStrs[d];
                    int ix = suffix.indexOf(digitStr, s);
                    // add the non-digit chars before, if they exist
                    if (s < ix) {
                        String text = suffix.substring(s, ix);
                        resultBuf.append(text);
                    }
                    if (digitStr != null && digitStr.length() != 0) {
                        // add the normalized digit chars, if they exist
                        resultBuf.append(normalizeFloat(digitStr, 6, 0));
                        s = ix + digitStr.length();
                    }

                }
                // add any chars after the last digStr
                resultBuf.append(suffix.substring(s));
                return resultBuf.toString();
            }
        }

        return suffix;
    }
}
