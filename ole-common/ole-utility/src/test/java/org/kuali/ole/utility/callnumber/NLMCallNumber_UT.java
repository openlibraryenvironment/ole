package org.kuali.ole.utility.callnumber;


import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/15/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class NLMCallNumber_UT extends BaseTestCase {

    @Test
    public void testNormalize() {
        try {
            String callNumberType = "NLM";

            String[] callNumberArrayForNLM = {"QS 11 c815a 1930", "QS 11 c815a 1930a", "QS 11 c815at 1927", "QS 532.5.A3 N532 1985", "QS 532.5.A3 SL no.1 1986", "WB110 C454t 2002", "WO100 S 9623 2000"};
            String[] normalizedCallNumberForNLM = {"QS  0011.000000 C0.815000 A 001930", "QS  0011.000000 C0.815000 A 001930A", "QS  0011.000000 C0.815000 AT 001927", "QS  0532.500000 A0.300000 N0.532000 001985", "QS  0532.500000 A0.300000 SL NO.000001 001986", "WB  0110.000000 C0.454000 T 002002", "WO  0100.000000 S 009623 002000"};

            for (int i = 0; i < callNumberArrayForNLM.length; i++) {
                CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
                String normalisedCallNumber = callNumber.getSortableKey(callNumberArrayForNLM[i]);
                Assert.assertEquals(normalizedCallNumberForNLM[i], normalisedCallNumber);
                System.out.println(callNumberArrayForNLM[i] + ":::normalized call number:::" + normalisedCallNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValidCallNumber() throws Exception {
        String callNumberType = "NLM";
        String number = "DK602 .P44 1901";
        CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
        boolean valid = callNumber.isValid(number);
        System.out.println("valid:" + valid);
    }
}
