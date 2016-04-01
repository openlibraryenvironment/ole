package org.kuali.ole.gobi.request;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;

public class GobiRequestValidatorTest {

    @Test
    public void testValidate() throws Exception {
        URL resource = getClass().getResource("UnlistedPrintMonograph.xml");
        assertNotNull(resource);
        File inputFile = new File(resource.toURI());
        assertNotNull(inputFile);
        String gobiRequestXML = FileUtils.readFileToString(inputFile);
        GobiRequestValidator gobiRequestValidator = new GobiRequestValidator();
        boolean validate = gobiRequestValidator.validate(gobiRequestXML);
        assertTrue(validate);
    }
}