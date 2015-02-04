package org.kuali.ole.docstore.common.document;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.docstore.common.util.ParseXml;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/27/14
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Licenses_UT {
    private static final Logger LOG = Logger.getLogger(Licenses_UT.class);

    @Test
    public void deserializeAndSerialize() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/Licenses1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        Licenses licenses = new Licenses();
        licenses = (Licenses) Licenses.deserialize(input);
        String serializeXml = licenses.serialize(licenses);
        serializeXml = ParseXml.formatXml(serializeXml);
        System.out.print(serializeXml);
    }
}
