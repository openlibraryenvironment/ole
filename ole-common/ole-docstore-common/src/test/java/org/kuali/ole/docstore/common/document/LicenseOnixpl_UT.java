package org.kuali.ole.docstore.common.document;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.docstore.common.util.ParseXml;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseOnixpl_UT {
    private static final Logger LOG = Logger.getLogger(LicenseOnixpl_UT.class);

    @Test
    public void deserializeAndSerialize() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/LicenseOnixpl1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        LicenseOnixpl licenseOnixpl = new LicenseOnixpl();
        licenseOnixpl = (LicenseOnixpl) licenseOnixpl.deserialize(input);
        licenseOnixpl.setContent("<onix:ONIXPublicationsLicenseMessage version=\"1.0\" datestamp=\"string\" sourcetype=\"00\" sourcename=\"string\" xmlns:onix=\"http://www.editeur.org/onix-pl\"></onix:ONIXPublicationsLicenseMessage>");
        String serializeXml = licenseOnixpl.serialize(licenseOnixpl);
        serializeXml = ParseXml.formatXml(serializeXml);
        System.out.print(serializeXml);
    }
}
