package org.kuali.ole.utility.callnumber;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/15/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuDocCallNumber_UT extends BaseTestCase {

    @Test
    public void testNormalize() {
        try {
            String callNumberType = "SuDoc";
            //String lcCallNumber="D 5.354:TPC Q-28 D/992";

            String[] callNumberArrayForSuDoc = {"C 3.186:P-70/2/", "C 13.58:7564", "C 13.58:7611", "D 5.354:TPC Q-28 D/992", "D 5.354:TPC R-13 D/984",
                    "HE 20.4002:AD  9/2", "HE 20.4002:AD 9/5", "HE 20.4002:F 94", "L 36.202:F 15/2/980", "L 36.202:F 15/3", "Y 1.1/7:109-118", "Y 1.1/7:109-131", "Y 1.1/7:110-6",
                    "Y 4.EC 7:S.HRG.110-646", "Y 4.EC 7:C 73/10", "Y 3.L 52:1/2/", "I 19.102/5:38117-A 1-SP-500/981/NORTH"};
            String[] normalizedCallNumberForSuDoc = {"C     00003 00186 P     00070 00002", "C     00013 00058 07564", "C     00013 00058 07611", "D     00005 00354 TPC   Q     00028 D     00992",
                    "D     00005 00354 TPC   R     00013 D     00984", "HE    00020 04002 AD    00009 00002", "HE    00020 04002 AD    00009 00005", "HE    00020 04002 F     00094",
                    "L     00036 00202 F     00015 00002 00980", "L     00036 00202 F     00015 00003", "Y     00001 00001 00007 00109 00118", "Y     00001 00001 00007 00109 00131", "Y     00001 00001 00007 00110 00006",
                    "Y     00004 EC    00007 S     HRG   00110 00646", "Y     00004 EC    00007 C     00073 00010", "Y     00003 L     00052 00001 00002", "I     00019 00102 00005 38117 A     00001 SP    00500 00981 NORTH"};
            CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
            for (int i = 0; i < callNumberArrayForSuDoc.length; i++) {
                String normalisedCallNumber = callNumber.getSortableKey(callNumberArrayForSuDoc[i]);
                System.out.println(callNumberArrayForSuDoc[i] + ":\tnormalized call number:\t" + normalisedCallNumber);
                Assert.assertEquals(normalizedCallNumberForSuDoc[i], normalisedCallNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testValidCallNumber() throws Exception {
        String callNumberType = "SuDoc";
        String number = "DK602 .P44 1901";
        CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
        boolean valid = callNumber.isValid(number);
        System.out.println("valid:" + valid);
    }

}
