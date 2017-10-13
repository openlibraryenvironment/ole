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
package org.kuali.rice.scripts

import groovy.util.logging.Log
import groovy.xml.XmlUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Tests for the {@link DictionaryConverter} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class DictionaryConverterTest {
    static def testResourceDir = "./src/test/resources/"
    static def dictTestDir = testResourceDir + "DictionaryConverterTest/"

    DictionaryConverter dictionaryConverter
    def config

    @Before
    void setUp() {
        def config_file_path = testResourceDir + "test.config.properties"
        config = new ConfigSlurper().parse(new File(config_file_path).toURL())
        dictionaryConverter = new DictionaryConverter(config)
    }


    @Test
    void testFixNamespaceProperties() {
        def maint_def_file_path = dictTestDir + "AttributePropertySample.xml"
        def maint_def_file = new File(maint_def_file_path)
        def dd_root_node = new XmlParser().parse(maint_def_file)
        dd_root_node.bean.each { bean -> dictionaryConverter.fixNamespaceProperties(bean) }
        Assert.assertEquals("bean properties size does not match", 5, dd_root_node.bean.property.size())
    }

    @Test
    void testGetRelativePath() {
        checkRelativePath("/opt/rice/", "/opt/rice/org/kuali/rice/test.java", "org/kuali/rice/") // unix
        checkRelativePath("/opt/rice/", "/opt/rice/test.java", "") // unix with file dir matching base
        checkRelativePath("C:\\windows\\rice\\", "C:\\windows\\rice\\org\\kuali\\krad\\test.java", "org/kuali/krad/") // windows
    }

    void checkRelativePath(base_path, file_path, relative_path) {
        def result_path = ConversionUtils.getRelativePath(base_path, file_path)
        Assert.assertEquals("relative path does not match", relative_path, result_path)
    }

    @Test
    void testMapBusinessObjectsAndDefinitions() {
        log.finer 'start map bo definition test'
        log.finer 'loading inquiry definition sample'
        def inq_def_file_path = dictTestDir + "InquiryDefinitionSample.xml"
        def inq_def_file = new File(inq_def_file_path)
        def dd_root_node = new XmlParser().parse(inq_def_file)
        log.finer "Before " + dd_root_node.toString()
        try {
            dictionaryConverter.transformBusinessObjectsAndDefinitions(dd_root_node, "TravelDetails")
        } catch (Exception e) {
            e.printStackTrace()
            Assert.fail("exception occurred in testing")
        }
        // generate assertion tests to validate inquiry definition properly converted
        Assert.assertEquals("dictionary attribute count", 17, dd_root_node.bean.findAll { it.@parent == "AttributeDefinition" }.size())
    }

    @Test
    void testTransformInquiryDefinitions() {
        log.finer 'start map inquiry definition test'
        log.finer 'loading inquiry definition sample'
        def inq_def_file_path = dictTestDir + "InquiryDefinitionSample.xml"
        def inq_def_file = new File(inq_def_file_path)
        def dd_root_node = new XmlParser().parse(inq_def_file)
        log.finer "Before " + dd_root_node.toString()
        def bean_node
        try {
            bean_node = dd_root_node.bean.find { it.@parent == "InquiryDefinition" }
            dictionaryConverter.transformInquiryDefinition(dd_root_node, bean_node, "TravelerDetail", "org.kuali.rice.krad.demo.travel.authorization.dataobject.TravelerDetail")
        } catch (Exception e) {
            e.printStackTrace()
            Assert.fail("exception occurred in testing")
        }

        def writer = new StringWriter()
        // generate assertion tests to validate inquiry definition properly converted
        XmlUtil.serialize(dd_root_node, writer)
        log.finer "After  " + writer.toString()
        // confirm a uif inquiry view was generated and has the correct elements
        Assert.assertEquals("uif inquiry view count", 1, dd_root_node.bean.findAll { it.@parent == "Uif-InquiryView" }.size())
    }

    @Test
    void testMapControlField() {
        log.finer 'start map control field test'
        log.finer 'loading control field sample'
        def inq_def_file_path = dictTestDir + "ControlFieldSample.xml"
        def inq_def_file = new File(inq_def_file_path)
        def dd_root_node = new XmlParser().parse(inq_def_file)
        log.finer "Before " + dd_root_node.toString()
        try {
            dictionaryConverter.transformControlField(dd_root_node.bean, config.map.convert.dd_bean_control)
        } catch (Exception e) {
            e.printStackTrace()
            Assert.fail("exception occurred in testing")
        }
        def writer = new StringWriter()
        XmlUtil.serialize(dd_root_node, writer)
        log.finer "map control field - After  " + writer.toString()

        // validate a control field and options finder were generated
        Assert.assertEquals("control field count", 1, dd_root_node.bean.property.findAll { it.@name == "controlField" }.size())
        Assert.assertEquals("options finder count", 1, dd_root_node.bean.property.findAll { it.@name == "optionsFinder" }.size())

    }

    @Test
    void testTransformMaintenanceDocument() {
        log.finer 'start map bo definition test'
        log.finer 'loading inquiry definition sample'
        def inq_def_file_path = dictTestDir + "MaintenanceDefinitionSample.xml"
        def inq_def_file = new File(inq_def_file_path)
        def dd_root_node = new XmlParser().parse(inq_def_file)
        log.finer "Before " + dd_root_node.toString()
        try {
            dictionaryConverter.transformMaintenanceDocument(dd_root_node, "TravelDetails")
        } catch (Exception e) {
            e.printStackTrace()
            Assert.fail("exception occurred in testing")
        }

        def writer = new StringWriter()
        // generate assertion tests to validate maintenance definition properly converted
        XmlUtil.serialize(dd_root_node, writer)
        log.finer "After  " + writer.toString()
        Assert.assertEquals("dictionary attribute count", 1, dd_root_node.bean.findAll { it.@parent == "Uif-MaintenanceView" }.size())

    }

    @Test
    void testTransformLookupDefinition() {
        def inq_def_file_path = dictTestDir + "LookupDefinitionSample.xml"
        def inq_def_file = new File(inq_def_file_path)
        def dd_root_node = new XmlParser().parse(inq_def_file)
        log.finer "Before " + dd_root_node.toString()
        def bean_node
        try {
            bean_node = dd_root_node.bean.find { it.@parent == "LookupDefinition" }
            dictionaryConverter.transformLookupDefinition(dd_root_node, bean_node, "TravelerDetail", "org.kuali.rice.krad.demo.travel.authorization.dataobject.TravelerDetail")
        } catch (Exception e) {
            e.printStackTrace()
            Assert.fail("exception occurred in testing")
        }

        def writer = new StringWriter()
        // generate assertion tests to validate inquiry definition properly converted
        XmlUtil.serialize(dd_root_node, writer)
        log.finer "After  " + writer.toString()
        // confirm a uif inquiry view was generated and has the correct elements
        Assert.assertEquals("uif lookup view count", 1, dd_root_node.bean.findAll { it.@parent == "Uif-LookupView" }.size())
    }

}