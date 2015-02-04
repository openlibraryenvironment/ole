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

public class ProfileXMLSchemaValidation_UT  {

    private static final Logger LOG = Logger.getLogger(ProfileXMLSchemaValidation_UT.class);
    private static final String PROFILE_XML_FILE = "profile.xml";

    @Test
    public void testProfileXMLSchemaValidation() throws Exception {
        byte[] profileByteArray;
        profileByteArray = IOUtils.toByteArray(getClass().getResourceAsStream(PROFILE_XML_FILE));
        ByteArrayInputStream profileSchemaByteArray = new ByteArrayInputStream(profileByteArray);
        ProfileXMLSchemaValidator profileXMLSchemaValidator = new ProfileXMLSchemaValidator();
        boolean schemaFlag=profileXMLSchemaValidator.validateContentsAgainstSchema(profileSchemaByteArray);
        assertTrue(schemaFlag);
    }
}
