package org.kuali.ole.utility.callnumber;

import junit.framework.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/14/13
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DDCallNumber_UT extends BaseTestCase {
    @Test
    public void testNormalize() {
        try {
            String callNumberType = "DDC";
            //String lcCallNumber="3.1 A12b C12 1981";
            String[] callNumberArrayForDD = {"622.33 B11b C23", "621.06 I59pjm", "621.06 I59pk", "621.06 I59pka", "621.19 G293s5", "621.2 D186iE"};
            String[] normalizedCallNumberForDD = {"622.33000000 B11 b C000023", "621.06000000 I59 pjm", "621.06000000 I59 pk", "621.06000000 I59 pka", "621.19000000 G293 s000005", "621.20000000 D186 iE"};
            CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);

            for (int i = 0; i < callNumberArrayForDD.length; i++) {
                String normalisedCallNumber = callNumber.getSortableKey(callNumberArrayForDD[i]);
                System.out.println(callNumberArrayForDD[i] + ":\tnormalized call number:\t" + normalisedCallNumber);
                Assert.assertEquals(normalizedCallNumberForDD[i], normalisedCallNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testValidCallNumber() throws Exception {
        String callNumberType = "DDC";
        //   String number = "1008 .E35 v.1 2006 no.1";
        String number = "70E35 v.1 2006 no.1";
        CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);
        boolean valid = callNumber.isValid(number);
        if (valid) {
            String sortableKey = callNumber.getSortableKey(number);
            System.out.println("Sortable key:" + sortableKey);
        }
        System.out.println("valid:" + valid);
    }
}
