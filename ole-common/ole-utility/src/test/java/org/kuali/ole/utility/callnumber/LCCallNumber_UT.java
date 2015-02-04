package org.kuali.ole.utility.callnumber;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;

public class LCCallNumber_UT extends BaseTestCase {


    @Test
    public void testSortableKey() {
        try {
            String callNumberType = "LCC";
            String[] callNumberArrayForLC = {"DK602 .P44 1901", "DK602.3 .P44 1996", "E461.C58 v.1 1955", "P145.6.T5 S321 1981 ", "PQ145.6.T5 S4 1976", "PQ145.62.T5 S4 1976", "PQ145.T51 S4 ", "PQ145.T511 S3", "PQ146.A31 1950", "PQ1024.J43 1999", "DK602.3.P44 H33 1997 no.1, pt.1", "G4124.R8 1880 .S7", "G4124.R8 1880 .S7 1995", "G5754.L7:2L6A35 1597 .N6 1919", "G5754.L7:2L6A35 1729 N6 1918"};
            String[] normalizedCallNumberForLC = {"DK  0602.000000 P0.440000 001901", "DK  0602.300000 P0.440000 001996", "E   0461.000000 C0.580000 V.000001 001955",
                    "P   0145.600000 T0.500000 S0.321000 001981", "PQ  0145.600000 T0.500000 S0.400000 001976",
                    "PQ  0145.620000 T0.500000 S0.400000 001976", "PQ  0145.000000 T0.510000 S0.400000", "PQ  0145.000000 T0.511000 S0.300000", "PQ  0146.000000 A0.310000 001950", "PQ  1024.000000 J0.430000 001999",
                    "DK  0602.300000 P0.440000 H0.330000 001997 NO.000001, PT.000001", "G   4124.000000 R0.800000 001880 S0.700000", "G   4124.000000 R0.800000 001880 S0.700000 001995", "G   5754.000000 L0.700000 :000002L000006A000035 001597 N0.600000 001919", "G   5754.000000 L0.700000"};

            for (int i = 0; i < callNumberArrayForLC.length; i++) {
                CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
                String normalisedCallNumber = callNumber.getSortableKey(callNumberArrayForLC[i]);
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
        boolean valid = callNumber.isValid(number);
        System.out.println("valid:" + valid);
    }
}
