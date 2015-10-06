package org.kuali.ole.utility.callnumber;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.solrmarc.callnum.CallNumber;

public class LCCallNumber_UT extends BaseTestCase {


    @Test
    public void testSortableKey() {
        try {
            String callNumberType = "LCC";
            String[] callNumberArrayForLC = {"DK602 .P44 1901", "DK602.3 .P44 1996", "E461.C58 v.1 1955", "P145.6.T5 S321 1981 ", "PQ145.6.T5 S4 1976", "PQ145.62.T5 S4 1976", "PQ145.T51 S4 ", "PQ145.T511 S3", "PQ146.A31 1950", "PQ1024.J43 1999", "DK602.3.P44 H33 1997 no.1, pt.1", "G4124.R8 1880 .S7", "G4124.R8 1880 .S7 1995", "G5754.L7:2L6A35 1597 .N6 1919", "G5754.L7:2L6A35 1729 N6 1918"};
            String[] normalizedCallNumberForLC = {"DK 3602 P44 41901", "DK 3602.3 P44 41996", "E 3461 C58 V 11 41955",
                    "P 3145.6 T5 S321 41981", "PQ 3145.6 T5 S4 41976",
                    "PQ 3145.62 T5 S4 41976", "PQ 3145 T51 S4", "PQ 3145 T511 S3", "PQ 3146 A31 41950", "PQ 41024 J43 41999",
                    "DK 3602.3 P44 H33 41997 NO 11 PT 11", "G 44124 R8 41880 S7", "G 44124 R8 41880 S7 41995", "G 45754 L7 12 L6 A35 41597 N6 41919", "G 45754 L7 12 L6 A35 41729 N6 41918"};

            for (int i = 0; i < callNumberArrayForLC.length; i++) {
                CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
                callNumber.parse(callNumberArrayForLC[i]);
                String normalisedCallNumber = callNumber.getShelfKey();
                Assert.assertEquals(normalizedCallNumberForLC[i], normalisedCallNumber);
                System.out.println(callNumberArrayForLC[i] + ":::normalized call number:::" + normalisedCallNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValidCallNumber() throws Exception {
        String callNumberType = "LCC";
        String number = "DK602 .P44 1901";
        CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
        callNumber.parse(number);
        boolean valid = callNumber.isValid();
        System.out.println("valid:" + valid);
    }
}
