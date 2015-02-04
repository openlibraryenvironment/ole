/*
 * Copyright 2012 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.ingest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static junit.framework.Assert.assertTrue;

public class OleLocationXMLSchemaValidation_UT {

    private static final Logger LOG = Logger.getLogger(OleLocationXMLSchemaValidation_UT.class);
    private static final String LOCATION_XML_FILE = "DefaultLibraryLocations.xml";

    @Test
    public void testLocationXMLSchemaValidation() throws Exception {
        byte[] locationByteArray;
        locationByteArray = IOUtils.toByteArray(getClass().getResourceAsStream(LOCATION_XML_FILE));
        ByteArrayInputStream locationSchemaByteArray = new ByteArrayInputStream(locationByteArray);
        OleLocationXMLSchemaValidator oleLocationXMLSchemaValidator = new OleLocationXMLSchemaValidator();
        boolean schemaFlag = oleLocationXMLSchemaValidator.validateContentsAgainstSchema(locationSchemaByteArray);
        assertTrue(schemaFlag);
    }

}
