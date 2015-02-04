package utility;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Sreekanth
 * Date: 6/4/12
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class XMLUtility_UT
        extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(XMLUtility_UT.class);


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Test
    public void testAllTextUsingRegEx() throws Exception {
        String filePath = "/org/kuali/ole/utility/license/OLE-License-ONIXPL.xml";
        URL resource = getClass().getResource(filePath);
        File file = new File(resource.toURI());
        String sampleText = FileUtils.readFileToString(file);
        String[] content = sampleText.split("content>");
        for (int i = 0; i < content.length; i++) {
            if (i % 2 == 0) {
                if (content[i].contains("additionalAttributes")) {
                    LOG.debug("additionalAttributes");
                    LOG.debug("======================");
                    getText(content[i]);
                }
            } else {

                LOG.debug("content");
                LOG.debug("======================");
                getText(content[i]);
            }

        }

    }

    private void getText(String input) {
        String reg = "<.*>(.*)<\\/.*?>";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(input);

        while (m.find()) {
            String s1 = m.group(1);
            LOG.debug(s1);
        }
    }

}

