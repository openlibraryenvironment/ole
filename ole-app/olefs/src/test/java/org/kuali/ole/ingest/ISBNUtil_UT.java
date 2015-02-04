package org.kuali.ole.ingest;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 4/6/12
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ISBNUtil_UT {

    @Test
    public void testNormalization() throws Exception {
        ISBNUtil isbnUtil = new ISBNUtil();
        List<String> isbnList = new ArrayList<String>();
        isbnList.add("0304935085");
        isbnList.add("0304935085847");
        isbnList.add("0714839337 (v. 3)");
        isbnList.add("0879514663 (set)");
        isbnList.add("087951468X (v. 2) :");
        isbnList.add("087951468X");
        isbnList.add("8086098044 :");
        isbnList.add("9781433113178 (hbk. : alk. paper)");
        isbnList.add("1433113171 (hbk. : alk. paper)");
        isbnList.add("978143311317X");
        for (String isbn : isbnList) {
            String normalizedIsbn = isbnUtil.normalizeISBN(isbn);
            System.out.println(normalizedIsbn);
            Assert.assertEquals(13, normalizedIsbn.length());
        }

    }
}
