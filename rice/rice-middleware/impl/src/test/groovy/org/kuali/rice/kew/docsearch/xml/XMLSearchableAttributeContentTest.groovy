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

import javax.xml.namespace.QName
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import org.kuali.rice.core.api.resourceloader.ResourceLoader
import org.kuali.rice.coreservice.framework.parameter.ParameterService
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl
import org.w3c.dom.Element
import org.xml.sax.InputSource
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import javax.xml.transform.TransformerException
import static org.junit.Assert.assertNotNull
import org.kuali.rice.kew.api.KewApiConstants
import org.kuali.rice.kew.api.extension.ExtensionDefinition
import org.kuali.rice.kim.api.KimConstants
import org.apache.commons.lang.StringUtils
import org.kuali.rice.kim.api.group.GroupService

/**
 *
 */
class XMLSearchableAttributeContentTest {
    @Before
    void setupFakeEnv() {
        def config = new JAXBConfigImpl();
        config.putProperty(CoreConstants.Config.APPLICATION_ID, "APPID");
        config.putProperty("VAR1", "VALUE1")
        config.putProperty("VAR2", "VALUE2")

        ConfigContext.init(config);
        GlobalResourceLoader.stop();
        GlobalResourceLoader.addResourceLoader([
            getName: { -> new QName("Foo", "Bar") },
            getService: { QName name ->
                [
                    parameterService: [ getParameterValueAsString: { s0,s1,s2 -> null } ] as ParameterService,
                    kimGroupService: [ getGroupByNamespaceCodeAndName: {a,b -> null } ] as GroupService,

                ][name.getLocalPart()]
            },
            stop: {}
        ] as ResourceLoader)
    }

    @Test(expected=RuntimeException) // NPE
    void testXmlConfigMustBeDefined() {
        new XMLSearchableAttributeContent(null)
        fail("expected error")
    }

    @Test(expected=TransformerException) // SAX parse: Premature end of file
    void testXmlConfigMustBeNotBeEmpty() {
        new XMLSearchableAttributeContent("")
        fail("expected error")
    }

    @Test(expected=TransformerException) // SAX parse: Content not allowed in prologue
    void testXmlConfigMustBeWellFormedy() {
        new XMLSearchableAttributeContent("I'm not valid XML")
        fail("expected error")
    }

    @Test
    void testConstructWithED() {
        def config = """<searchingConfig/>"""
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, config)
        assertNotNull(new XMLSearchableAttributeContent(edb.build()).getSearchingConfig());
    }

    @Test(expected=RuntimeException)
    void testBadEDConfig() {
        def config = """ """
        def edb = ExtensionDefinition.Builder.create("test", KewApiConstants.SEARCHABLE_XML_ATTRIBUTE_TYPE, StandardGenericXMLSearchableAttribute.class.getName())
        edb.configuration.put(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA, config)
        assertNotNull(new XMLSearchableAttributeContent(edb.build()).getSearchingConfig());
    }
    
    /* tests that the <searchingConfig> element can be found anywhere, not just as the immediately
       child of the attribute config xml (which it really only ever should be in practice...)
     */
    @Test
    void testCanFindSearchingConfigAnywhere() {
        assertNotNull(new XMLSearchableAttributeContent("""
          <searchingConfig/>
        """).getSearchingConfig())
    }

    @Test void testDisplay() {
        def content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <display>
                  <type>text</type>
                  <meta>something</meta>
                  <values title="title a">value a</values>
                  <values title="title b" selected="true">value b</values>
                  <values title="title c"/>
                  <values title="title d" selected="true">value d</values>
                </display>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEquals(1, content.fieldDefs.size())
        assertEquals("text", content.fieldDefList[0].display.type)
        assertEquals("something", content.fieldDefList[0].display.meta)
        assertEquals(["value b", "value d"], new ArrayList(content.fieldDefList[0].display.selectedOptions))
        assertEquals(4, content.fieldDefList[0].display.options.size())
        assertEquals("", new ArrayList(content.fieldDefList[0].display.options)[2].key)
    }

    @Test void testFieldEvaluation() {
        def content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <fieldEvaluation>
                  <bogus>
                    <xpathexpression>0</xpathexpression>
                  </bogus>
                  <xpathexpression>a</xpathexpression>
                  <xpathexpression>b</xpathexpression>
                  <xpathexpression>c</xpathexpression>
                </fieldEvaluation>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEquals("a", content.fieldDefList[0].fieldEvaluationExpr)
    }

    @Test void testFieldSettings() {
        def content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef name="field1" title="Field 1">
                <value>default value</value>
                <resultColumn show="false"/>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEquals("default value", content.fieldDefList[0].defaultValue);
        assertEquals("field1", content.fieldDefList[0].name);
        assertEquals("Field 1", content.fieldDefList[0].title);
        assertEquals(false, content.fieldDefList[0].showResultColumn);
    }

    @Test void testValidation() {
        def content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <validation required="true">
                  <regex>abcd</regex>
                  <message>message</message>
                </validation>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEquals(true, content.fieldDefList[0].validation.required);
        assertEquals("abcd", content.fieldDefList[0].validation.regex);
        assertEquals("message", content.fieldDefList[0].validation.message);

        content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <validation/>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEquals(false, content.fieldDefList[0].validation.required);
        assertEquals(null, content.fieldDefList[0].validation.regex);
        assertEquals(null, content.fieldDefList[0].validation.message);
    }

    @Test void testVisibility() {
        def content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <visibility>
                  <column visible="true"/>
                </visibility>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEquals(Boolean.TRUE, content.fieldDefList[0].visibility.visible)
        assertEquals("column", content.fieldDefList[0].visibility.type)
        assertEquals(null, content.fieldDefList[0].visibility.groupName)
        assertEquals(null, content.fieldDefList[0].visibility.groupNamespace)

        content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <visibility>
                  <fieldAndColumn>
                    <isMemberOfWorkgroup>KR-\${VAR1}WKFLW:Test\${VAR2}Workgroup</isMemberOfWorkgroup>
                  </fieldAndColumn>
                </visibility>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEquals(null, content.fieldDefList[0].visibility.visible);
        assertEquals("fieldAndColumn", content.fieldDefList[0].visibility.type)
        assertEquals("TestVALUE2Workgroup", content.fieldDefList[0].visibility.groupName)
        assertEquals("KR-VALUE1WKFLW", content.fieldDefList[0].visibility.groupNamespace)

        content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <visibility>
                  <field>
                    <isMemberOfGroup namespace="KR-\${VAR1}NS">Test\${VAR2}Group</isMemberOfGroup>
                    <isMemberOfWorkgroup>KR-\${VAR1}WKFLW:Test\${VAR2}Workgroup</isMemberOfWorkgroup>
                  </field>
                </visibility>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEquals(null, content.fieldDefList[0].visibility.visible);
        assertEquals("field", content.fieldDefList[0].visibility.type)
        assertEquals("TestVALUE2Group", content.fieldDefList[0].visibility.groupName)
        assertEquals("KR-VALUE1NS", content.fieldDefList[0].visibility.groupNamespace)
    }

    @Test(expected=RuntimeException) void testInvalidGroupVisibility() {
        def content = new XMLSearchableAttributeContent("""
         <ruleAttribute>
           <searchingConfig>
             <fieldDef>
               <visibility>
                 <field>
                   <!-- NO NAMESPACE, THIS SHOULD FAIL -->
                   <isMemberOfGroup>TestGroup</isMemberOfGroup>
                 </field>
               </visibility>
             </fieldDef>
           </searchingConfig>
         </ruleAttribute>""")
        content.fieldDefList
    }

    @Test void testMissingWorkgroupVisibilityDefault() {
        def content = new XMLSearchableAttributeContent("""
         <ruleAttribute>
           <searchingConfig>
             <fieldDef>
               <visibility>
                 <field>
                   <!-- NO NAMESPACE, THIS SHOULD FAIL -->
                   <isMemberOfWorkgroup>TestGroup</isMemberOfWorkgroup>
                 </field>
               </visibility>
             </fieldDef>
           </searchingConfig>
         </ruleAttribute>""")
        // defaults to KIM group workflow namespace
        assertEquals(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, content.fieldDefList[0].visibility.groupNamespace)
    }

    @Test void testSearchContent() {
        String searchContent = """<putWhateverWordsIwantInsideThisTag>
          <givenname>
            <value>%givenname%</value>
          </givenname>
        </putWhateverWordsIwantInsideThisTag>"""
        def content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <xmlSearchContent>${searchContent}</xmlSearchContent>
            </searchingConfig>
          </ruleAttribute>
        """)
        assertEqualsDeleteWhitespace(searchContent, content.searchContent.trim())
    }

    @Test void testInclusivenessDefaults() {
        def searchDef = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <searchDefinition>
                  <rangeDefinition>
                    <lower/>
                    <upper/>
                  </rangeDefinition>
                </searchDefinition>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """).fieldDefList[0].searchDefinition

        assertTrue(searchDef.lowerBound.inclusive)
        assertFalse(searchDef.upperBound.inclusive)
    }

    @Test void testSearchDefinitionOptions() {
        def searchDef = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef>
                <searchDefinition caseSensitive="true" inclusive="true" dataType="datetime">
                  <rangeDefinition caseSensitive="false" datePicker="false">
                    <lower inclusive="false" datePicker="true"/>
                    <upper caseSensitive="true"/> <!-- caseSensitive ignored on bound -->
                  </rangeDefinition>
                </searchDefinition>
              </fieldDef>
            </searchingConfig>
          </ruleAttribute>
        """).fieldDefList[0].searchDefinition

        assertEquals("datetime", searchDef.dataType)
        assertTrue(searchDef.lowerBound.datePicker)
        assertFalse(searchDef.lowerBound.inclusive)
        assertTrue(searchDef.searchDef.caseSensitive)

        assertFalse(searchDef.upperBound.datePicker)
        assertTrue(searchDef.upperBound.inclusive)
        assertFalse(searchDef.rangeBoundOptions.caseSensitive)
    }

    @Test void testSearchDefinitionOptions2() {
        def searchDef = new XMLSearchableAttributeContent("""
        <ruleAttribute>
            <searchingConfig>
                <fieldDef name="textFieldWithCaseSensitivity" title="Text Field with Case Sensitivity">
                    <display>
                        <type>text</type>
                    </display>
                    <searchDefinition>
                        <rangeDefinition inclusive="true" datePicker="false" caseSensitive="true">
                            <lower/>
                            <upper/>
                        </rangeDefinition>
                    </searchDefinition>
                    <fieldEvaluation>
                        <xpathexpression>//putWhateverWordsIwantInsideThisTag/textFieldWithCaseSensitivity/value</xpathexpression>
                    </fieldEvaluation>
                </fieldDef>
                <xmlSearchContent>
                    <putWhateverWordsIwantInsideThisTag>
                        <textFieldWithCaseSensitivity>
                            <value>%textFieldWithCaseSensitivity%</value>
                        </textFieldWithCaseSensitivity>
                    </putWhateverWordsIwantInsideThisTag>
                </xmlSearchContent>
            </searchingConfig>
        </ruleAttribute>""").fieldDefList[0].searchDefinition
        assertTrue(searchDef.rangeDef.caseSensitive)
    }

    @Test void testDefaultSearchDataType() {
        def content = new XMLSearchableAttributeContent("""
          <ruleAttribute>
            <searchingConfig>
              <fieldDef/>
            </searchingConfig>
          </ruleAttribute>""")
        assertEquals(KewApiConstants.SearchableAttributeConstants.DEFAULT_SEARCHABLE_ATTRIBUTE_TYPE_NAME, content.fieldDefList[0].searchDefinition.dataType)
    }

    @Test
    void testDefaultXmlGenerationReturnsEmptyForNoFields() {
        assertEquals("", new XMLSearchableAttributeContent("<ruleAttribute><searchingConfig/></ruleAttribute>").generateSearchContent(null))
    }

    @Test
    void testDefaultXmlGenerationWithNoValues() {
        assertEqualsDeleteWhitespace("<xmlRouting></xmlRouting>", new XMLSearchableAttributeContent("""
        <ruleAttribute>
            <searchingConfig>
                <fieldDef name="def1"/>
                <fieldDef name="def2"/>
            </searchingConfig>
        </ruleAttribute>
        """).generateSearchContent(null))
    }

    @Test
    void testCustomXmlGenerationReturnsWithNoValues() {
        def content = """<myGeneratedContent>
                           <noVariableReplacement/>
                         </myGeneratedContent>"""
        assertEqualsDeleteWhitespace(content, new XMLSearchableAttributeContent("""
        <ruleAttribute>
          <searchingConfig>
            <xmlSearchContent>
              ${content}
            </xmlSearchContent>
            <fieldDef name="def1"/>
            <fieldDef name="def2"/>
          </searchingConfig>
        </ruleAttribute>
        """).generateSearchContent(null).trim())
    }

    @Test
    void testDefaultXmlGeneration() {
        assertEqualsDeleteWhitespace("""<xmlRouting><field name="def1"><value>val1</value></field></xmlRouting>""", new XMLSearchableAttributeContent("""
        <ruleAttribute>
          <searchingConfig>
            <fieldDef name="def1"/>
            <fieldDef name="def2"/>
          </searchingConfig>
        </ruleAttribute>
        """).generateSearchContent([ def1: "val1" ]))
    }

    @Test void testGenerateSearchContent() {
        def content = new XMLSearchableAttributeContent("""
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
        """)
        assertEqualsDeleteWhitespace("""
                <myGeneratedContent>
                  <version>whatever</version>
                  <anythingIWant>Once upon a val1...</anythingIWant>
                  <conclusion>Happily ever val2.</conclusion>
                  <epilogue>this var isn't set %undefined%</epilogue>
                </myGeneratedContent>
        """.trim(), content.generateSearchContent([ def1: "val1", def2: "val2", third: "doesn't matter", undefined: "not replaced" ]).trim())
    }

    private static Element dom(String content) {
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(content))).getDocumentElement()
    }

    private assertEqualsDeleteWhitespace(String expected, String actual) {
        assertEquals(StringUtils.deleteWhitespace(expected), StringUtils.deleteWhitespace(actual));
    }
}