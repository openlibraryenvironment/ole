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
package org.kuali.rice.kew.docsearch.xml

import org.apache.commons.lang.StringUtils
import org.junit.Test
import org.kuali.rice.kew.api.KewApiConstants
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria
import org.kuali.rice.kew.api.extension.ExtensionDefinition
import org.kuali.rice.kns.util.FieldUtils
import org.kuali.rice.kns.web.ui.Row
import org.kuali.rice.krad.util.KRADConstants
import org.w3c.dom.Element
import org.w3c.dom.Text
import static org.junit.Assert.*
import org.kuali.rice.kns.web.ui.Column

/**
 * Tests the StandardGenericXMLSearchableAttribute class in isolation
 */
class StandardGenericXMLSearchableAttributeUnitTest {
    private static class SGXSASubclass extends StandardGenericXMLSearchableAttribute {
        @Override
        protected Element getConfigXML(ExtensionDefinition extensionDefinition) {
            Element e = super.getConfigXML(extensionDefinition);
            Element xc = e.ownerDocument.createElement("xmlSearchContent")
            Text t = e.ownerDocument.createTextNode("dynamically added search content");
            xc.appendChild(t);
            e.appendChild(xc);
            e.setAttribute("customized", "true");
            return e;
        }
    }

    // this is sort of a crummy assertion, but describes the current behavior until we decide
    // to handle this error case more gracefully
    @Test(expected=RuntimeException) // NPE
    void testXmlConfigMustBeDefined() {
        testXmlConfigValidity(null)
        fail("expected error")
    }

    @Test(expected=RuntimeException) // SAX parse: Premature end of file
    void testXmlConfigMustBeNotBeEmpty() {
        testXmlConfigValidity("")
        fail("expected error")
    }

    @Test(expected=RuntimeException) // SAX parse: Content not allowed in prologue
    void testXmlConfigMustBeWellFormedy() {
        testXmlConfigValidity("I'm not valid XML")
        fail("expected error")
    }

    @Test
    void testDefaultXmlGenerationReturnsEmptyForNoFields() {
        testGenerateSearchContent("<validXml/>", "")
    }

    @Test
    void testDefaultXmlGenerationWithNoValues() {
        testGenerateSearchContent("""
        <ruleAttribute>
        <searchingConfig>
            <fieldDef name="def1"/>
            <fieldDef name="def2"/>
        </searchingConfig>
        </ruleAttribute>
        """, "<xmlRouting></xmlRouting>")
    }

    @Test
    void testCustomXmlGenerationWithNoValues() {
        def content = """<myGeneratedContent>
                           <noVariableReplacement/>
                         </myGeneratedContent>"""
        testGenerateSearchContent("""
        <ruleAttribute>
        <searchingConfig>
            <xmlSearchContent>
                ${content}
            </xmlSearchContent>
            <fieldDef name="def1"/>
            <fieldDef name="def2"/>
        </searchingConfig>
        </ruleAttribute>
        """, content)
    }

    @Test
    void testDefaultXmlGeneration() {
        testGenerateSearchContent("""
        <ruleAttribute>
        <searchingConfig>
            <fieldDef name="def1"/>
            <fieldDef name="def2"/>
        </searchingConfig>
        </ruleAttribute>
        """, """<xmlRouting><field name="def1"><value>val1</value></field></xmlRouting>""", [ def1: "val1" ])
    }

    @Test
    void testCustomXmlGeneration() {
        testGenerateSearchContent("""
        <ruleAttribute>
        <searchingConfig>
            <xmlSearchContent>
                <myGeneratedContent>
                    <version>whatever</version>
                    <anythingIWant>Once upon a %def1%...</anythingIWant>
                    <conclusion>Happily ever %def2%.</conclusion>
                    <epilogue>this var isn't set %undefined%</epilogue>
                </myGeneratedContent>
            </xmlSearchContent>
            <fieldDef name="def1"/>
            <fieldDef name="def2"/>
        </searchingConfig>
        </ruleAttribute>
        """, """
                <myGeneratedContent>
                    <version>whatever</version>
                    <anythingIWant>Once upon a val1...</anythingIWant>
                    <conclusion>Happily ever val2.</conclusion>
                    <epilogue>this var isn't set %undefined%</epilogue>
                </myGeneratedContent>
        """, [ def1: "val1", def2: "val2", third: "doesn't matter" ])
    }

    /* if no fields are defined, validation is moot */
    @Test void testDocumentAttributeCriteriaIsAlwaysValidWhenNoFieldsAreDefined() {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, "<validXml/>")
        def errors = new StandardGenericXMLSearchableAttribute().validateDocumentAttributeCriteria(edb.build(), DocumentSearchCriteria.Builder.create().build())
        assertEquals("unexpected validation errors", 0, errors.size())
    }

    private static final String STRING_FIELD_SEARCH_CONFIG = """
    <searchingConfig>
            <fieldDef name="givenname" title="First name">
                <display>
                    <type>text</type>
                </display>
                <visibility>
                    <column visible="true"/>
                </visibility>
                <validation required="true">
                    <regex>^[a-zA-Z ]+\$</regex>
                    <message>Invalid first name</message>
                </validation>
                <fieldEvaluation>
                    <xpathexpression>//putWhateverWordsIwantInsideThisTag/givenname/value</xpathexpression>
                </fieldEvaluation>
            </fieldDef>
            <xmlSearchContent>
                <putWhateverWordsIwantInsideThisTag>
                    <givenname>
                        <value>%givenname%</value>
                    </givenname>
                </putWhateverWordsIwantInsideThisTag>
            </xmlSearchContent>
        </searchingConfig>
    """

    @Test void testValidateDocumentAttributeCriteriaSimpleValue() {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, STRING_FIELD_SEARCH_CONFIG)
        def c = DocumentSearchCriteria.Builder.create();
        c.documentAttributeValues.put("givenname", [ "jack" ] as List<String>)
        def errors = new StandardGenericXMLSearchableAttribute().validateDocumentAttributeCriteria(edb.build(), c.build())
        assertEquals("unexpected validation errors", 0, errors.size())
    }

    @Test void testValidateBoundsInclusivityDiscrepancy() {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, RANGE_FIELD_SEARCH_CONFIG)
        def c = DocumentSearchCriteria.Builder.create();
        // ">= jack" will fail due to discrepancy between attrib and range upper bound inclusivity
        c.documentAttributeValues.put("givenname", [ "jack..jill" ] as List<String>)
        def errors = new StandardGenericXMLSearchableAttribute().validateDocumentAttributeCriteria(edb.build(), c.build())
        println errors
        assertEquals("expected range upper bound inclusivity discrepancy between Range default and Attribute configuration", 1, errors.size())
    }

    @Test void testValidateDocumentAttributeCriteriaExpression() {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, RANGE_FIELD_SEARCH_CONFIG)
        def c = DocumentSearchCriteria.Builder.create();
        // use "< jack", ">= jack" will fail due to discrepancy between attrib and range upper bound inclusivity
        c.documentAttributeValues.put("givenname", [ "< jack" ] as List<String>)
        def errors = new StandardGenericXMLSearchableAttribute().validateDocumentAttributeCriteria(edb.build(), c.build())
        println errors
        assertEquals("unexpected validation errors", 0, errors.size())
    }

    @Test void testGetSearchFields() {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, STRING_FIELD_SEARCH_CONFIG)
        def fields= new StandardGenericXMLSearchableAttribute().getSearchFields(edb.build(), "not used")
        println fields
        // TODO: test something more substantial
        assertEquals(1, fields.size())
        List<Row> rows = FieldUtils.convertRemotableAttributeFields(fields);
        assertEquals(1, rows.size());
    }

private static final String STRING_FIELD_SEARCH_FORMATTER_CONFIG = """
    <searchingConfig>
            <fieldDef name="givenname" title="First name">
                <display>
                    <type>text</type>
                    <formatter>org.kuali.rice.core.web.format.NoOpStringFormatter</formatter>
                </display>
                <visibility>
                    <column visible="true"/>
                </visibility>
                <validation required="true">
                    <regex>^[a-zA-Z ]+\$</regex>
                    <message>Invalid first name</message>
                </validation>
                <fieldEvaluation>
                    <xpathexpression>//putWhateverWordsIwantInsideThisTag/givenname/value</xpathexpression>
                </fieldEvaluation>
            </fieldDef>
            <xmlSearchContent>
                <putWhateverWordsIwantInsideThisTag>
                    <givenname>
                        <value>%givenname%</value>
                    </givenname>
                </putWhateverWordsIwantInsideThisTag>
            </xmlSearchContent>
        </searchingConfig>
    """
    @Test void testGetSearchFieldsFormatter() {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, STRING_FIELD_SEARCH_FORMATTER_CONFIG)
        def fields= new StandardGenericXMLSearchableAttribute().getSearchFields(edb.build(), "not used")
        println fields
        // TODO: test something more substantial
        assertEquals(1, fields.size())
        Column col = FieldUtils.constructColumnFromAttributeField(fields.get(0));
        println col.getFormatter();
        assertNotNull(col.getFormatter());
        assertEquals("org.kuali.rice.core.web.format.NoOpStringFormatter",col.getFormatter().getClass().getName());

    }


    private static final String RANGE_FIELD_SEARCH_CONFIG = """
    <searchingConfig>
        <fieldDef name="givenname" title="First name">
            <display>
                <type>text</type>
            </display>
            <searchDefinition rangeSearch="true"/>
            <validation required="true">
                <regex>^[a-zA-Z ]+\$</regex>
                <message>Invalid first name</message>
            </validation>
            <fieldEvaluation>
                <xpathexpression>//putWhateverWordsIwantInsideThisTag/givenname/value</xpathexpression>
            </fieldEvaluation>
        </fieldDef>
        <xmlSearchContent>
            <putWhateverWordsIwantInsideThisTag>
                <givenname>
                    <value>%givenname%</value>
                </givenname>
            </putWhateverWordsIwantInsideThisTag>
        </xmlSearchContent>
    </searchingConfig>
    """

    @Test void testGetRangeSearchFields() {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, RANGE_FIELD_SEARCH_CONFIG)
        def fields= new StandardGenericXMLSearchableAttribute().getSearchFields(edb.build(), "not used")
        println fields
        // TODO: test something more substantial
        assertEquals(1, fields.size())
        assertTrue(fields[0].attributeLookupSettings.lowerBoundInclusive);
        assertFalse(fields[0].attributeLookupSettings.upperBoundInclusive);

        // ranged search generates 2 fields, from and to
        List<Row> rows = FieldUtils.convertRemotableAttributeFields(fields);
        assertEquals(2, rows.size());
        assertEquals(KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + "givenname", rows[0].fields[0].propertyName);
        assertEquals("givenname", rows[1].fields[0].propertyName);
    }

    @Test void testSubclassCanOverrideConfigXML() {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA,
        """<searchingConfig>
             <fieldDef name="def1"/> <!-- add a single param so content is generated -->
           </searchingConfig>""")
        String content = new SGXSASubclass().generateSearchContent(edb.build(), "no document type", WorkflowAttributeDefinition.Builder.create("test_attr").build())
        // test subclass-generated content is found
        assertEquals("dynamically added search content", content);
    }

    protected void testXmlConfigValidity(String xmlConfig) {
        testGenerateSearchContent(xmlConfig)
    }

    protected void testGenerateSearchContent(String config, String expected = null, params = [:]) {
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, config)

        def ad = WorkflowAttributeDefinition.Builder.create("test_attr")
        params.each { k,v -> ad.addPropertyDefinition(k, v) }

        def generated = new StandardGenericXMLSearchableAttribute().generateSearchContent(edb.build(), "no document type", ad.build())

        if (expected != null) {
            // if we don't delete whitespace here we can get failures for some reason
            assertEquals(StringUtils.deleteWhitespace(expected), StringUtils.deleteWhitespace(generated));
        }
    }
}