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
package org.kuali.rice.kew.api.validation

import org.kuali.rice.core.test.JAXBAssert
import org.junit.Assert
import org.junit.Test

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller
import org.kuali.rice.kew.api.rule.RuleResponsibility
import org.kuali.rice.kew.api.rule.Rule;

/**
 * Unit test for ValidationResults object
 */
class ValidationResultsTest {
    private static final String vr_xml(i=0) {
        return vr_xml("field_${i}", "field_${i} error message")
    }

    private static final String vr_xml(fieldName, errorMessage) {
        return """<entry key="${fieldName}">${errorMessage}</entry>"""
    }

    private static final ValidationResults create_vrs(num=1) {
        def b = ValidationResults.Builder.create()
        if (num > 0) {
            0.upto(num-1) {
              b.addError("field_${it}", "field_${it} error message")
            }
        }
        return b.build()
    }

    private static final String vrs_xml(num=1) {
        def xml = """<ns2:validationResults xmlns:ns2="http://rice.kuali.org/kew/v2_0" xmlns="http://rice.kuali.org/core/v2_0">"""
        if (num > 0) {
            xml += "<ns2:errors>"
            xml += (0..num-1).toArray().inject('') { str, i-> str + vr_xml(i) }
            xml += "</ns2:errors>"
        }
        return xml + "</ns2:validationResults>"
    }

    @Test(expected=IllegalArgumentException.class)
 	void test_Builder_create_fail_null() {
 	    ValidationResults.Builder.create(null)
 	}

    @Test
    void test_Builder_create_single_success() {
        def vrs = create_vrs(1)
        def vr = vrs.getErrors();
        Assert.assertEquals(1, vr.size())
        Assert.assertEquals("field_0 error message", vr.get("field_0"));
 	}

    @Test
    void test_Builder_create_multiple_success() {
        def vrs = create_vrs(3)
        Assert.assertEquals(3, vrs.getErrors().size())
        0.upto(2) {
          Assert.assertEquals("field_" + it + " error message", vrs.getErrors().get("field_" + it));
        }
 	}

    @Test
    void test_Builder_create_none_success() {
        def vrs = create_vrs(0)
        Assert.assertEquals(0, vrs.getErrors().size())
    }

    @Test
    void test_Builder_create_copy_success() {
        def vrs = create_vrs(3)
        vrs = ValidationResults.Builder.create(vrs)
        Assert.assertEquals(3, vrs.getErrors().size())
        0.upto(2) {
          Assert.assertEquals("field_" + it + " error message", vrs.getErrors().get("field_" + it));
        }
 	}

    @Test
 	void test_Xml_Marshal_Unmarshal_single() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(create_vrs(1), vrs_xml(1), ValidationResults.class)
 	}

    @Test
 	void test_Xml_Marshal_Unmarshal_multiple() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(create_vrs(3), vrs_xml(3), ValidationResults.class)
 	}

    @Test
 	void test_Xml_Marshal_Unmarshal_none() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(create_vrs(0), vrs_xml(0), ValidationResults.class)
 	}
}