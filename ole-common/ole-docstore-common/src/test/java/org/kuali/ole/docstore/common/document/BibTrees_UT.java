package org.kuali.ole.docstore.common.document;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.docstore.common.util.ParseXml;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 1/23/14
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibTrees_UT {

    private static final Logger LOG = Logger.getLogger(BibTrees_UT.class);

    @Test
    public void deserializeAndSerialize() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibTrees1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        BibTrees.deserialize(input);

    }


}
