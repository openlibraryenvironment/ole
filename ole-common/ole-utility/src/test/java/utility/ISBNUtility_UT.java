package utility;

import junit.framework.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.utility.ISBNUtil;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 4/6/12
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ISBNUtility_UT
        extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(ISBNUtility_UT.class);

    @Test
    public void testNormalization() throws OleException {
        ISBNUtil isbnUtil = new ISBNUtil();
        List<String> isbnList = new ArrayList<String>();
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

            LOG.info(normalizedIsbn);
            LOG.info(normalizedIsbn);
            Assert.assertEquals(13, normalizedIsbn.length());
        }

        //
        try {
            isbnUtil.normalizeISBN("098789678956462137123");
        } catch (Exception e) {
            LOG.error("Invalid input " + e.getMessage());
        }

    }


}
