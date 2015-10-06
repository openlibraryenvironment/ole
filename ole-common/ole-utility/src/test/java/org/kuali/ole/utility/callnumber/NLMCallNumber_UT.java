package org.kuali.ole.utility.callnumber;


import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.solrmarc.callnum.CallNumber;

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
            String[] normalizedCallNumberForNLM = {"QS 211 C815 A 41930", "QS 211 C815 A 41930A", "QS 211 C815 AT 41927", "QS 3532.5 A3 N532 41985", "QS 3532.5 A3 SL NO 11 41986", "WB 3110 C454 T 42002", "WO 3100 _S 49623 42000"};

            for (int i = 0; i < callNumberArrayForNLM.length; i++) {
                CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
                callNumber.parse(callNumberArrayForNLM[i]);
                String normalisedCallNumber = callNumber.getShelfKey();
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
        callNumber.parse(number);
        boolean valid = callNumber.isValid();
        System.out.println("valid:" + valid);
    }
}
