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

public class OlePatronXMLSchemaValidation_UT  {

    private static final Logger LOG = Logger.getLogger(OlePatronXMLSchemaValidation_UT.class);
    private static final String PATRON_XML_FILE = "samplePatronRecord.xml";

    @Test
    public void testPatronXMLSchemaValidation() throws Exception {
        byte[] patronByteArray;
        patronByteArray = IOUtils.toByteArray(getClass().getResourceAsStream(PATRON_XML_FILE));
        ByteArrayInputStream patronSchemaByteArray = new ByteArrayInputStream(patronByteArray);
        OlePatronXMLSchemaValidator olePatronXMLSchemaValidator = new OlePatronXMLSchemaValidator();
        boolean schemaFlag=olePatronXMLSchemaValidator.validateContentsAgainstSchema(patronSchemaByteArray);

        assertTrue(schemaFlag);
    }

}
