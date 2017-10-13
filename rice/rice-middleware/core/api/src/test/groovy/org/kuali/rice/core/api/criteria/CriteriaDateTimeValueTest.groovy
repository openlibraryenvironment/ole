/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.core.api.criteria

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.Assert
import org.junit.Test

class CriteriaDateTimeValueTest {

    @Test
    public void testXmlMarshalingAndUnMarshalling() {

        JAXBContext jc = JAXBContext.newInstance(CriteriaDateTimeValue.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()

        CriteriaDateTimeValue cdtv = new CriteriaDateTimeValue(Calendar.getInstance(TimeZone.getTimeZone("GMT")))
        marshaller.marshal(cdtv, sw)
        String xml = sw.toString()

        Unmarshaller unmarshaller = jc.createUnmarshaller()
        CriteriaDateTimeValue cdtvAfterXml = (CriteriaDateTimeValue) unmarshaller.unmarshal(new StringReader(xml))
        Assert.assertEquals(cdtv, cdtvAfterXml)
        Assert.assertEquals(cdtv.hashCode(), cdtvAfterXml.hashCode())
    }
}
