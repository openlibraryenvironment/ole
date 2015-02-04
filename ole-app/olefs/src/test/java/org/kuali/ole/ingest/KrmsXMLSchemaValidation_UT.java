package org.kuali.ole.ingest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 7/3/12
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class KrmsXMLSchemaValidation_UT  {

    private static final Logger LOG = Logger.getLogger(KrmsXMLSchemaValidation_UT.class);
    private static final String Krms_XML_FILE = "license.xml";

    @Test
    public void testProfileXMLSchemaValidation() throws Exception {
        byte[] profileByteArray;
        profileByteArray = IOUtils.toByteArray(getClass().getResourceAsStream(Krms_XML_FILE));
        ByteArrayInputStream profileSchemaByteArray = new ByteArrayInputStream(profileByteArray);
        KrmsXMLSchemaValidator krmsXMLSchemaValidator = new KrmsXMLSchemaValidator();
        boolean schemaFlag=krmsXMLSchemaValidator.validateContentsAgainstSchema(profileSchemaByteArray);
        assertTrue(schemaFlag);
    }
}
