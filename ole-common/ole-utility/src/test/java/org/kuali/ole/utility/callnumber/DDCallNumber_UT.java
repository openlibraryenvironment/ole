package org.kuali.ole.utility.callnumber;

import junit.framework.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.solrmarc.callnum.CallNumber;


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
            String[] normalizedCallNumberForDD = {"3622.33 B11 B C 223", "3621.06 I59 PJM", "3621.06 I59 PK", "3621.06 I59 PKA", "3621.19 G293 S 15", "3621.2 D186 IE"};
            CallNumber callNumber = CallNumberFactory.getInstance().getCallNumber(callNumberType);

            for (int i = 0; i < callNumberArrayForDD.length; i++) {
                callNumber.parse(callNumberArrayForDD[i]);
                String normalisedCallNumber = callNumber.getShelfKey();
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
        callNumber.parse(number);
        boolean valid = callNumber.isValid();
        if (valid) {
            String sortableKey = callNumber.getShelfKey();
            System.out.println("Sortable key:" + sortableKey);
        }
        System.out.println("valid:" + valid);
    }
}
