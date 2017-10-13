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
import groovy.xml.QName
import groovy.xml.XmlUtil
import org.apache.commons.io.FilenameUtils

/**
 * DictionaryConverter.groovy
 *
 * A groovy class which can be used to updates KNS to KRAD. Splits the focus into
 * Business Objects, Attribute Definitions, Maintenance (and Transactional) Documents
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class DictionaryConverter {

    def config

    // directory and path structure
    def inputDir = ""
    def inputPaths = [:]

    def outputDir = ""
    def outputPaths = [:]

    // dictionary properties transform map
    def ddPropertiesMap

    // control definition transform map
    def ddBeanControlMap

    // bean property removal list
    def ddPropertiesRemoveList

    // namespace schema (p and xsi)
    def pNamespaceSchema
    def xsiNamespaceSchema


    public DictionaryConverter(config) {
        init(config)
    }

    def init(config) {
        inputDir = config.input.dir
        outputDir = config.output.dir

        inputPaths = config.input.path
        outputPaths = config.output.path

        ddPropertiesMap = config.map.convert.dd_prop
        ddBeanControlMap = config.map.convert.dd_bean_control
        ddPropertiesRemoveList = config.list.remove.dd_beans
        pNamespaceSchema = config.msg_bean_schema
        xsiNamespaceSchema = config.msg_xml_schema_legacy
    }


    /**
     * loads properties and runs through DataDictionary beans and related Maintenance Document beans
     * generating a new maintenance document
     */
    public void convertDataDictionaryFiles() {
        // Load Configurable Properties
        log.finer "finished loading config files"

        def inputResourceDir = inputDir + inputPaths.src.resources
        def outputResourceDir = outputDir + outputPaths.src.resources

        def xmlInclPattern = ~/.*\.xml$/
        def uifLibExclPattern = ~/uif\/library/

        // find all spring resource files
        def listBusObjFiles = locateSpringBeanFiles(inputResourceDir, xmlInclPattern, uifLibExclPattern, "", "businessObjectClass")

        def listTransDocFiles = locateSpringBeanFiles(inputResourceDir, xmlInclPattern, uifLibExclPattern, "TransactionalDocumentEntry", "")

        def listMaintDocFiles = locateSpringBeanFiles(inputResourceDir, xmlInclPattern, uifLibExclPattern, "MaintenanceDocumentEntry", "")
        log.finer "BO " + listBusObjFiles.size() + " TR " + listTransDocFiles.size() + " MN " + listMaintDocFiles.size()

        // load and parse data dictionary input
        processBusObjFiles(listBusObjFiles, outputResourceDir, inputResourceDir)

        // once complete with the business objects, proceed to the maintenance document
        processMaintDocFiles(listMaintDocFiles, outputResourceDir, inputResourceDir)

        // TODO: Get transactional documents working properly
        /**listTransDocFiles.each { transDocFile ->

         def tdocRootNode = null
         def objName = null
         def outputPath = null
         def outputFile = null
         outputFile = getFileName(transDocFile.path)
         objName = ConversionUtils.getObjectName(transDocFile.path, "TransactionalDocument")
         outputPath = outputResourceDir + ConversionUtils.getRelativePath(inputResourceDir, transDocFile.path)

         tdocRootNode = new XmlParser().parse(transDocFile.path)
         if (tdocRootNode != null) {transformTransactionalDocument(tdocRootNode, objName)
         generateSpringBeanFile(tdocRootNode, outputPath, outputFile)}}**/

    }

    /**
     * takes each of the business object related xml files in the input directory and generate a krad
     * compliant verison
     *
     * @param listBusObjFiles
     * @param outputResourceDir
     * @param inputResourceDir
     */
    private void processBusObjFiles(ArrayList listBusObjFiles, String outputResourceDir, String inputResourceDir) {
        listBusObjFiles.each { busObjFile ->

            try {
                // generates objName, output path, and output file from existing path and input information
                def ddRootNode = parseSpringXml(busObjFile.text)
                def objName = ConversionUtils.getObjectName(busObjFile.path, "BO")
                String path = outputResourceDir + ConversionUtils.getRelativePath(inputResourceDir, busObjFile.path)
                String extension = FilenameUtils.getExtension(busObjFile.path);
                String filename = objName + "KradBO." + extension;

                // process data dictionary beans for any business objects and related attributes
                if (ddRootNode != null) {
                    transformBusinessObjectsAndDefinitions(ddRootNode, objName)
                    generateSpringBeanFile(ddRootNode, path, filename)
                }
            } catch (FileNotFoundException ex) {
                log.error("Error - Missing Business Class File")
                errorText()
            }
        }
    }

    /**
     * processes a list of maintenance document files and generates the related krad files to an output directory
     *
     * @param listMaintDocFiles
     * @param outputResourceDir - location the fi
     * @param inputResourceDir - used to extract the relative directory of the xml files
     */
    private void processMaintDocFiles(ArrayList listMaintDocFiles, String outputResourceDir, String inputResourceDir) {
        listMaintDocFiles.each { maintDocFile ->
            log.info "\n---\nprocessing maintenance document file " + maintDocFile.path
            try {
                def objName = ConversionUtils.getObjectName(maintDocFile.path, "MaintenanceDocument")
                def path = outputResourceDir + ConversionUtils.getRelativePath(inputResourceDir, maintDocFile.path)
                def basename = FilenameUtils.getBaseName(maintDocFile.path);
                def extension = FilenameUtils.getExtension(maintDocFile.path);
                String filename = objName + "KradMaintenanceDocument." + extension;
                def mdocRootNode = parseSpringXml(maintDocFile.text)

                if (mdocRootNode != null) {
                    transformMaintenanceDocument(mdocRootNode, objName)
                    generateSpringBeanFile(mdocRootNode, path, filename)
                }

            } catch (FileNotFoundException ex) {
                log.error("Error - Missing Maintenance Document File")
                errorText()
            }
        }
    }

    /**
     * locate spring xml files and filters based on bean and property values inside the  file
     *
     * @param srcPath
     * @param inclPatterns
     * @param exclPatterns
     * @param inclBeanType
     * @param inclPropType
     * @return
     */
    def locateSpringBeanFiles(srcPath, inclPattern, exclPattern, inclBeanType, inclPropType) {
        def fileList = []
        log.finer "lookup path for " + srcPath
        //  def patternResultList = new FileNameFinder().getFileNames(searchDirPath, inclPatterns[0], exclPatterns[0]).collect { new File(it) }
        def patternResultList = ConversionUtils.findFilesByPattern(srcPath, inclPattern, exclPattern)
        patternResultList.each { resultFile ->
            try {
                def ddRootNode = parseSpringXml(resultFile.text)
                //def objectClass = ddRootNode.bean.find { it.@parent == inclBeanType }
                if (ddRootNode.bean.find { it.@parent == inclBeanType } || ddRootNode.bean.property.find { it.@name == inclPropType }) {
                    log.finer "processing file path " + resultFile.path + " for IBT " + inclBeanType + " or IPT " + inclPropType
                    fileList << resultFile
                }
            } catch (Exception e) { log.info "failed loading " + resultFile.path + "\n" + e.message + "\n---\n" + resultFile.text + "\n----\n" }
        }
        return fileList
    }

    /**
     * Simple spring xml parser
     *
     * @param inputText
     * @return
     */
    def parseSpringXml(inputText) {
        inputText = inputText.replaceFirst(/(?ms)^(\<\!.*?--\>\s*)/, "")
        def springBeanRootNode = new XmlParser().parseText(inputText)
        return springBeanRootNode
    }

    /**
     * traverses the spring beans and overwrites and replaces beans/properties with their krad equivalent
     *
     * @param ddRootNode
     * @param objName
     * @return
     */
    def transformBusinessObjectsAndDefinitions(ddRootNode, objName) {
        // modify groceries: quality items please
        def beans = ddRootNode.bean
        log.finer("Total bean : " + beans.size())

        // retrieve Business Object Class
        def objClass = ddRootNode.bean.property.find { it.@name == "businessObjectClass" }
        def objClassName
        if (objClass != null) {
            objClassName = objClass.@value
        } else {
            log.finer "no businessObjectClass beans found"
            errorText()
        }

        // Iterate through each bean
        (0..<beans.size()).each {
            def beanNode = beans[it]
            log.finer("bean id : " + beanNode.@id)

            // Changes specific to BusinessObjectEntry and AttributeDefinition
            log.finer "bean node parent is " + beanNode.@parent
            if (beanNode.@parent in ["BusinessObjectEntry", "AttributeDefinition", "AttributeReferenceDummy-genericSystemId"]) {
                log.finer "process each bean"
                def isBusObjEntry = false
                if (beanNode.@parent in ["BusinessObjectEntry"]) {
                    isBusObjEntry = true
                }
                //Verify Parent Change
                log.finer "process parent bean"
                if (ddPropertiesMap[beanNode.@parent.toString()] != null) {
                    beanNode.@parent = ddPropertiesMap[beanNode.@parent.toString()]
                }
                log.finer "process bean properties"
                def properties = beanNode.property
                transformBusinessObjectProperties(properties, ddPropertiesMap, ddPropertiesRemoveList)

                //Additional logic for Control field

                transformControlField(beanNode, ddBeanControlMap)
                if (isBusObjEntry) {
                    // handles mapping title attributes and primary keys
                    transformTitleAttribute(beanNode)
                }
                // Changes specific to Inquiry Definition
            } else if (beanNode.@parent in ["InquiryDefinition"]) {
                log.finer "mapping inquiry definition"
                transformInquiryDefinition(ddRootNode, beanNode, objName, objClassName)
                // Changes specific to Lookup Definition
            } else if (beanNode.@parent in ["LookupDefinition"]) {
                transformLookupDefinition(ddRootNode, beanNode, objName, objClassName)
            }
        }
    }

    private void transformTitleAttribute(beanNode) {
        def titleAttrBeanNode = beanNode.property.find { it.@name == "titleAttribute" }
        if (titleAttrBeanNode != null) {
            titleAttrBeanNode.replaceNode {
                property(name: "titleAttribute", value: titleAttrBeanNode.@value)
                property(name: "primaryKeys") {
                    list {
                        value(titleAttrBeanNode.@value)
                    }
                }
            }
        }
    }

    /**
     * Modifies control and controlField elements into Uif Control elements
     *
     * @param beanNode
     * @param beanControlMap
     * @return
     */
    def transformControlField(beanNode, beanControlMap) {

        def controlProp = beanNode.property.find { it.@name == "control" }
        def options = beanNode.property.find { it.@name == "optionsFinder" }
        def controlFieldProp = beanNode.property.find { it.@name == "controlField" }
        if (controlProp != null && controlFieldProp != null) {
            controlProp.replaceNode({})
        } else if (controlProp != null) {
            controlProp.@name = controlProp.@name + "Field"
            (0..<controlProp.bean.size()).each {
                def beanProp = controlProp.bean[it]
                if (beanControlMap[beanProp.@parent] != null && beanControlMap[beanProp.@parent] == "Uif-DropdownControl") {
                    controlProp.plus({ property(name: "optionsFinder") { bean(class: "org.kuali.rice.krad.keyvalues.PersistableBusinessObjectValuesFinder") } })
                    controlProp.bean.replaceNode({ bean(parent: "Uif-DropdownControl") })
                } else if (beanControlMap[beanProp.@parent] != null && beanControlMap[beanProp.@parent] == "Uif-TextAreaControl") {
                    controlProp.bean.replaceNode({ bean(parent: "Uif-TextAreaControl") })
                }
                if (beanControlMap[beanProp.@parent] != null) {
                    beanProp.@parent = beanControlMap[beanProp.@parent]
                } else {
                    beanProp.@parent = "Uif-" + beanProp.@parent.replace("Definition", "")
                }
            }
        }
    }

    // TODO: Determine if its necessary to handle the properties inner beans recursively
    private void transformBusinessObjectProperties(propertyNodes, Map<String, String> ddPropertiesMap, ddPropertiesRemoveList) {
        // review each of the property node elements
        (0..<propertyNodes.size()).each {
            def propertyNode = propertyNodes[it]
            // convert to new format if exists
            if (ddPropertiesMap[propertyNode.@name.toString()] != null) {
                def existingProperty = propertyNodes.find { it.@name == ddPropertiesMap[propertyNode.@name.toString()] }
                //log.info("   -->New Property name : "+ddPropertiesMap[propertyNode.@name.toString()])
                if (existingProperty != null) {
                    return // bypass loop if contains existing replacement
                }
                propertyNode.@name = ddPropertiesMap[propertyNode.@name.toString()]
            }

            // remove property element if necessary
            if (ddPropertiesRemoveList.contains(propertyNode.@name)) {
                //log.info("   -->Remove Property name : "+propertyNode.@name)
                propertyNode.replaceNode({})
            }

            //Iterate through property beans to replace any necessary bean properties
            // TODO: should this be split into a ddBeanMap?
            def propertyBeans = propertyNode.bean
            (0..<propertyBeans.size()).each {
                def propBeanNode = propertyBeans[it]
                if (ddPropertiesMap[propBeanNode.@parent.toString()] != null) {
                    propBeanNode.@parent = ddPropertiesMap[propBeanNode.@parent.toString()]
                }
            }
        }
    }

    /**
     * handles maintenance document files and transforms beans into krad compliant
     *
     * @param mdocRootNode
     * @param objName
     * @return
     */
    def transformMaintenanceDocument(mdocRootNode, objName) {
        def beans = mdocRootNode.bean
        log.finer("MD Total bean : " + beans.size())
        // orphans any beans with parents in file (TODO: check if this is necessary)
        (0..<beans.size()).each {
            def beanNode = beans[it]
            fixNamespaceProperties(beanNode)
            log.finer("bean id : " + beanNode.@id)
            def parentBeanNode = mdocRootNode.bean.find { it.@parent == beanNode.@id }
            if (parentBeanNode != null) {
                parentBeanNode.replaceNode({})
            }
            transformMaintenanceDocumentEntries(mdocRootNode, beanNode, objName)
        }
    }



    /**
     * formats spring root node into xml and saves to file
     *
     * @param rootBean
     * @param outputFile
     */
    private void generateSpringBeanFile(rootBean, path, filename) {
        try {
            def writer = new StringWriter()
            XmlUtil.serialize(rootBean, writer)
            def result = writer.toString()
            log.finer "Result for " + filename + ": " + result + "\n\n"
            //result = modifyBeanSchema(result)
            ConversionUtils.buildFile(path, filename, result)
        } catch (FileNotFoundException ex) {
            log.finer "unable to generate output for " + outputFile.name
            errorText()
        }
    }

    /**
     * reformats to handle excess p:namespace schemas in the xml
     *
     * @param fileText
     * @return
     */
    def modifyBeanSchema(fileText) {
        fileText = fileText.replace("xmlns:p=" + "\"$pNamespaceSchema\"", "")
        fileText = fileText.replace(xsiNamespaceSchema, "$xsiNamespaceSchema xmlns:p=" + "\"$pNamespaceSchema\"")
        return fileText
    }

    /**
     *
     *
     * @param ddRootNode
     * @param beanNode
     * @param busObjName
     * @param busObjClassQualName
     */
    private void transformInquiryDefinition(ddRootNode, beanNode, busObjName, busObjClassQualName) {
        // TODO: run through a duplicate analysis
        def inquiryBeanNode = ddRootNode.bean.find { it.@parent == beanNode.@id }
        if (inquiryBeanNode != null) {
            inquiryBeanNode.replaceNode({})
        }
        def inquiryParentBeanNode = beanNode
        def titlePropNode = inquiryParentBeanNode.property.find { it.@name == "title" }
        def inquirySectionsPropertyNode = inquiryParentBeanNode.property.find { it.@name == "inquirySections" }
        log.finer "transform bean node for inquiry"
        // TODO: Switch back to beanNode.replaceNode and find alt way to handle inq definition dependencies  (i.e. DataDictionaryOverrides)
        beanNode.plus {
            addComment(delegate, "Inquiry View")
            bean(id: inquiryBeanNode.@id, parent: inquiryBeanNode.@parent)
            bean(id: "$busObjName-InquiryView", parent: "Uif-InquiryView") {
                transformTitleProperty(delegate, titlePropNode)
                addViewNameProperty(delegate, titlePropNode.@value)
                log.finer "transform bean node for inquiry 3"
                property(name: "dataObjectClassName", value: busObjClassQualName)
                if (inquirySectionsPropertyNode != null) {
                    property(name: "Items") {
                        list {
                            (0..<inquirySectionsPropertyNode.list.bean.size()).each {
                                def innerbeanNode = inquirySectionsPropertyNode.list.bean[it]
                                // Checking whether the collection or not
                                def collectionField = innerbeanNode.property.list.bean.find { it.@parent == "InquiryCollectionDefinition" };
                                if (collectionField == null) {
                                    log.finer "transform bean node for inquiry 5a"
                                    def sectiontitlePropNode = innerbeanNode.property.find { it.@name == "title" }
                                    def columnsPropertyNode = innerbeanNode.property.find { it.@name == "numberOfColumns" }
                                    def inqFieldsPropNode = innerbeanNode.property.find { it.@name == "inquiryFields" }
                                    bean(parent: 'Uif-Disclosure-GridSection') {
                                        log.finer "transform bean node for inquiry 6"
                                        if (sectiontitlePropNode != null) {
                                            property(name: "title", value: sectiontitlePropNode.@value)
                                            property(name: "headerText", value: sectiontitlePropNode.@value)
                                        }
                                        log.finer "transform bean node for inquiry 7"
                                        if (columnsPropertyNode != null) {
                                            property(name: "layoutManager.numberOfColumns", value: columnsPropertyNode.@value)
                                        }
                                        if (inqFieldsPropNode != null) {
                                            log.finer "transform bean node for inquiry 8"
                                            property(name: "items") {
                                                list {
                                                    (0..<inqFieldsPropNode.list.bean.size()).each {
                                                        def innerInquiryFieldsbeanNode = inqFieldsPropNode.list.bean[it]
                                                        def attributeValue
                                                        innerInquiryFieldsbeanNode.attributes().each() { key, value ->
                                                            if (key.toString().endsWith("attributeName")) {
                                                                attributeValue = value
                                                            }
                                                        }
                                                        bean('xmlns:p': pNamespaceSchema, parent: 'Uif-InputField', 'p:propertyName': attributeValue)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    def sectiontitlePropNode = innerbeanNode.property.find { it.@name == "title" }
                                    def inqFieldsPropNode = innerbeanNode.property.list.bean.property.find { it.@name == "inquiryFields" }
                                    def businessObjectPropertyNode = innerbeanNode.property.list.bean.property.find { it.@name == "businessObjectClass" }
                                    def attributeNamePropertyNode = innerbeanNode.property.list.bean.property.find { it.@name == "attributeName" }
                                    def summarytitlePropNode = innerbeanNode.property.list.bean.property.find { it.@name == "summaryTitle" }
                                    def summaryFieldsPropertyNode = innerbeanNode.property.list.bean.property.find { it.@name == "summaryFields" }
                                    bean(parent: 'Uif-StackedCollectionSection') {
                                        if (sectiontitlePropNode != null) {
                                            property(name: "title", value: sectiontitlePropNode.@value)
                                        }
                                        if (businessObjectPropertyNode != null) {
                                            property(name: "collectionObjectClass", value: businessObjectPropertyNode.@value)
                                        }
                                        if (attributeNamePropertyNode != null) {
                                            property(name: "propertyName", value: attributeNamePropertyNode.@value)
                                        }
                                        if (inqFieldsPropNode != null) {
                                            property(name: "items") {
                                                list {
                                                    (0..<inqFieldsPropNode.list.bean.size()).each {
                                                        def innerInquiryFieldsbeanNode = inqFieldsPropNode.list.bean[it]
                                                        def attributeValue
                                                        innerInquiryFieldsbeanNode.attributes().each() { key, value ->
                                                            if (key.toString().endsWith("attributeName")) {
                                                                attributeValue = value
                                                            }
                                                        }
                                                        bean('xmlns:p': pNamespaceSchema, parent: 'Uif-InputField', 'p:propertyName': attributeValue)
                                                    }
                                                }
                                            }
                                        }
                                        if (summarytitlePropNode != null) {
                                            property(name: "layoutManager.summaryTitle", value: summarytitlePropNode.@value)
                                        }
                                        if (summaryFieldsPropertyNode != null) {
                                            property(name: "layoutManager.summaryFields") {
                                                list {
                                                    (0..<summaryFieldsPropertyNode.list.bean.size()).each {
                                                        def innerSummaryFieldsbeanNode = summaryFieldsPropertyNode.list.bean[it]
                                                        def attributeValue
                                                        innerSummaryFieldsbeanNode.attributes().each() { key, value ->
                                                            if (key.toString().endsWith("attributeName")) {
                                                                attributeValue = value
                                                            }
                                                        }
                                                        value(attributeValue)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                //log.info("Bean")
                            }
                            (0..<inquirySectionsPropertyNode.list.ref.size()).each {
                                log.finer "transform bean node for inquiry 9"
                                def referenceNode = inquirySectionsPropertyNode.list.ref[it]
                                def referencebeanNode = ddRootNode.bean.find { it.@id == referenceNode.@bean }
                                def innerbeanNode = referencebeanNode
                                if (referencebeanNode != null) {
                                    referencebeanNode.replaceNode({})
                                }
                                //log.info("Reference")
                                // Checking whether the collection or not
                                def collectionField = innerbeanNode?.property?.list?.bean?.find { it.@parent == "InquiryCollectionDefinition" };
                                if (collectionField == null) {
                                    def sectiontitlePropNode = innerbeanNode?.property?.find { it.@name == "title" }
                                    def columnsPropertyNode = innerbeanNode?.property?.find { it.@name == "numberOfColumns" }
                                    def inqFieldsPropNode = innerbeanNode?.property?.find { it.@name == "inquiryFields" }
                                    bean(parent: 'GroupSectionGridLayout') {
                                        if (sectiontitlePropNode != null) {
                                            property(name: "title", value: sectiontitlePropNode.@value)
                                        }
                                        if (columnsPropertyNode != null) {
                                            property(name: "layoutManager.numberOfColumns", value: columnsPropertyNode.@value)
                                        }
                                        if (inqFieldsPropNode != null) {
                                            property(name: "items") {
                                                list {
                                                    (0..<inqFieldsPropNode.list.bean.size()).each {
                                                        def innerInquiryFieldsbeanNode = inqFieldsPropNode.list.bean[it]
                                                        def attributeValue
                                                        innerInquiryFieldsbeanNode.attributes().each() { key, value ->
                                                            if (key.toString().endsWith("attributeName")) {
                                                                attributeValue = value
                                                            }
                                                        }
                                                        bean('xmlns:p': pNamespaceSchema, parent: 'Uif-InputField', 'p:propertyName': attributeValue)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    def sectiontitlePropNode = innerbeanNode.property.find { it.@name == "title" }
                                    def inqFieldsPropNode = innerbeanNode.property.list.bean.property.find { it.@name == "inquiryFields" }
                                    def businessObjectPropertyNode = innerbeanNode.property.list.bean.property.find { it.@name == "businessObjectClass" }
                                    def attributeNamePropertyNode = innerbeanNode.property.list.bean.property.find { it.@name == "attributeName" }
                                    def summarytitlePropNode = innerbeanNode.property.list.bean.property.find { it.@name == "summaryTitle" }
                                    def summaryFieldsPropertyNode = innerbeanNode.property.list.bean.property.find { it.@name == "summaryFields" }
                                    bean(parent: 'Uif-StackedCollectionSection') {
                                        if (sectiontitlePropNode != null) {
                                            property(name: "title", value: sectiontitlePropNode.@value)
                                        }
                                        if (businessObjectPropertyNode != null) {
                                            property(name: "collectionObjectClass", value: businessObjectPropertyNode.@value)
                                        }
                                        if (attributeNamePropertyNode != null) {
                                            property(name: "propertyName", value: attributeNamePropertyNode.@value)
                                        }
                                        if (inqFieldsPropNode != null) {
                                            property(name: "items") {
                                                list {
                                                    (0..<inqFieldsPropNode.list.bean.size()).each {
                                                        def innerInquiryFieldsbeanNode = inqFieldsPropNode.list.bean[it]
                                                        def attributeValue
                                                        innerInquiryFieldsbeanNode.attributes().each() { key, value ->
                                                            if (key.toString().endsWith("attributeName")) {
                                                                attributeValue = value
                                                            }
                                                        }
                                                        bean('xmlns:p': pNamespaceSchema, parent: 'Uif-InputField', 'p:propertyName': attributeValue)
                                                    }
                                                }
                                            }
                                        }
                                        if (summarytitlePropNode != null) {
                                            property(name: "layoutManager.summaryTitle", value: summarytitlePropNode.@value)
                                        }
                                        if (summaryFieldsPropertyNode != null) {
                                            property(name: "layoutManager.summaryFields") {
                                                list {
                                                    (0..<summaryFieldsPropertyNode.list.bean.size()).each {
                                                        def innerSummaryFieldsbeanNode = summaryFieldsPropertyNode.list.bean[it]
                                                        def attributeValue
                                                        innerSummaryFieldsbeanNode.attributes().each() { key, value ->
                                                            if (key.toString().endsWith("attributeName")) {
                                                                attributeValue = value
                                                            }
                                                        }
                                                        value(attributeValue)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * produces Uif-LookupView based on information in LookupDefinition
     *
     * @param ddRootNode
     * @param beanNode
     * @param objName
     * @param objClassName
     */
    protected void transformLookupDefinition(ddRootNode, beanNode, objName, objClassName) {
        // TODO: run through duplicate analysis
        def lookupBeanNode = ddRootNode.bean.find { it.@parent == beanNode.@id }
        if (lookupBeanNode != null) {
            lookupBeanNode.replaceNode({})
        }
        def lookupParentBeanNode = beanNode
        def titlePropNode = lookupParentBeanNode.property.find { it.@name == "title" }
        def menubarPropNode = lookupParentBeanNode.property.find { it.@name == "menubar"}
        def lookupFieldsPropertyNode = lookupParentBeanNode.property.find { it.@name == "lookupFields" }
        def resultFieldsPropertyNode = lookupParentBeanNode.property.find { it.@name == "resultFields" }
        // TODO: switch back to beanNode.replaceNode and find way to handle extra lookupDefinition definitions (i.e. DataDictionaryOverrides)
        beanNode.plus {
            addComment(delegate, "Lookup View")
            bean(id: lookupBeanNode.@id, parent: lookupBeanNode.@parent)
            bean(id: "$objName-LookupView", parent: "Uif-LookupView") {
                transformTitleProperty(delegate, titlePropNode)
                addViewNameProperty(delegate, titlePropNode.@value)
                //if (titlePropNode != null) {
                //property(name: "title", value: titlePropNode.@value)
                //property(name: "headerText", value: titlePropNode.@value)
                //}
                property(name: "dataObjectClassName", value: objClassName)
                transformMenubarProperty(delegate, menubarPropNode)
                if (lookupFieldsPropertyNode != null) {
                    property(name: "criteriaFields") {
                        list {
                            (0..<lookupFieldsPropertyNode.list.bean.size()).each {
                                def innerbeanNode = lookupFieldsPropertyNode.list.bean[it]
                                def attributeValue
                                innerbeanNode.attributes().each() { key, value ->
                                    if (key.toString().endsWith("attributeName")) {
                                        attributeValue = value
                                    }
                                }
                                bean('xmlns:p': pNamespaceSchema, parent: 'Uif-LookupCriteriaInputField', 'p:propertyName': attributeValue)
                            }
                        }
                    }
                }
                if (resultFieldsPropertyNode != null) {
                    property(name: "resultFields") {
                        list {
                            (0..<resultFieldsPropertyNode.list.bean.size()).each {
                                def innerbeanNode = resultFieldsPropertyNode.list.bean[it]
                                def attributeValue
                                innerbeanNode.attributes().each() { key, value ->
                                    if (key.toString().endsWith("attributeName")) {
                                        attributeValue = value
                                    }
                                }
                                bean('xmlns:p': pNamespaceSchema, parent: 'Uif-DataField', 'p:propertyName': attributeValue)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * replaces namespace properties (p:name) with a property tag
     * Allows transformation scripts to handle property tags properly
     *
     * @param beanNode
     */
    def fixNamespaceProperties(beanNode) {
        def count = 0;
        log.finer "loading " + beanNode.attributes()
        def remAttrs = []
        if (beanNode.attributes()) {
            def attrs = beanNode.attributes()

            attrs.keySet().each {
                count++
                log.finer "adding property: " + it + " " + it.class.name
                if (it instanceof QName) {
                    beanNode.appendNode("property", [name: it.getLocalPart(), value: attrs.get(it)])
                    remAttrs.add(it)
                }
            }
            remAttrs.each { beanNode.attributes().remove(it) }
        }
        log.finer "finishing fix properties: " + beanNode
    }

    private void transformMaintenanceDocumentEntries(mdocRootNode, beanNode, objName) {
        if (beanNode.@parent == "MaintenanceDocumentEntry") {
            def maintDocParentBeanNode = beanNode
            def titlePropNode = maintDocParentBeanNode.property.find { it.@name == "title" }
            def busObjPropNode = maintDocParentBeanNode.property.find { it.@name == "businessObjectClass" }
            def maintSectPropNode = maintDocParentBeanNode.property.find { it.@name == "maintainableSections" }
            def maintClassPropNode = maintDocParentBeanNode.property.find { it.@name == "maintainableClass" }
            def lockingKeysPropertyNode = maintDocParentBeanNode.property.find { it.@name == "lockingKeys" }
            def docTypePropertyNode = maintDocParentBeanNode.property.find { it.@name == "documentTypeName" }
            def docClassPropertyNode = maintDocParentBeanNode.property.find { it.@name == "documentAuthorizerClass" }
            beanNode.replaceNode {
                addComment(delegate, "Maintenance View")
                bean(id: "$objName-MaintenanceView", parent: "Uif-MaintenanceView") {
                    transformTitleProperty(delegate, titlePropNode)
                    addViewNameProperty(delegate, titlePropNode?.@value)

                    if (busObjPropNode != null) {
                        property(name: "dataObjectClassName", value: busObjPropNode.@value)
                    }
                    if (maintSectPropNode != null) {
                        transformMaintainableSection(delegate, mdocRootNode, maintSectPropNode)
                    }
                }
            }

            beanNode.replaceNode {
                bean(id: objName + "MaintenanceDocument", parent: "MaintenanceDocumentEntry") {
                    if (busObjPropNode != null) {
                        property(name: "businessObjectClass", value: busObjPropNode.@value)
                    }
                    if (maintClassPropNode != null) {
                        property(name: "maintainableClass", value: maintClassPropNode.@value)
                    }
                    if (docTypePropertyNode != null) {
                        property(name: "documentTypeName", value: docTypePropertyNode.@value)
                    }
                    if (docClassPropertyNode != null) {
                        property(name: "documentAuthorizerClass", value: docClassPropertyNode.@value)
                    }
                    if (lockingKeysPropertyNode != null) {
                        property(name: "lockingKeys") {
                            list {
                                (0..<lockingKeysPropertyNode.list.value.size()).each {
                                    value(lockingKeysPropertyNode.list.value[it].value())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    def transformMaintainableSection(rootNode, mdocRootNode, maintSectPropNode) {
        if (maintSectPropNode != null) {
            log.finer "bean size " + maintSectPropNode
            rootNode.property(name: "items") {
                list(merge: "true") {
                    (0..<maintSectPropNode.list.ref.size()).each {
                        def referenceNode = maintSectPropNode.list.ref[it]
                        log.finer "reference node  " + referenceNode

                        def referencebeanNode = mdocRootNode.bean.find { it.@id == referenceNode.@bean }
                        if (referencebeanNode != null && referencebeanNode.@parent != "MaintainableSectionDefinition") {
                            referencebeanNode = mdocRootNode.bean.find { it.@id == referencebeanNode.@parent }
                        }
                        def innerbeanNode = referencebeanNode
                        if (referencebeanNode != null) {
                            referencebeanNode.replaceNode({})
                        }
                        log.finer "innerbean " + innerbeanNode
                        // Checking whether the collection or not

                        if (innerbeanNode != null) {
                            def collectionField = innerbeanNode.property.list.bean.find { it.@parent == "MaintainableCollectionDefinition" };
                            if (collectionField == null) {
                                def sectiontitlePropNode = innerbeanNode.property.find { it.@name == "title" }
                                def maintainableItemsPropertyNode = innerbeanNode.property.find { it.@name == "maintainableItems" }
                                bean(parent: 'Uif-MaintenanceStackedCollectionSection') {
                                    if (sectiontitlePropNode != null) {
                                        property(name: "title", value: sectiontitlePropNode.@value)
                                        property(name: "headerText", value: sectiontitlePropNode.@value)
                                    }
                                    if (maintainableItemsPropertyNode != null) {
                                        property(name: "items") {
                                            list {
                                                (0..<maintainableItemsPropertyNode.list.bean.size()).each {
                                                    def innerMaintItemsbeanNode = maintainableItemsPropertyNode.list.bean[it]
                                                    def attributeValue
                                                    innerMaintItemsbeanNode.attributes().each() { key, value ->
                                                        if (key.toString().endsWith("name")) {
                                                            attributeValue = value
                                                        }
                                                    }
                                                    bean('xmlns:p': pNamespaceSchema, parent: 'Uif-InputField', 'p:propertyName': attributeValue)
                                                    //bean(parent: 'Uif-InputField') { property(name:'propertyName', 'value':attributeValue)  }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                def sectionTitlePropNode = innerbeanNode.property.find { it.@name == "title" }
                                def maintItems = innerbeanNode.property.list.bean.property.find { it.@name == "maintainableFields" }
                                def busObjPropNode = innerbeanNode.property.list.bean.property.find { it.@name == "businessObjectClass" }
                                def attrNamePropNode = innerbeanNode.property.list.bean.property.find { it.@name == "name" }
                                def summaryTitlePropNode = innerbeanNode.property.list.bean.property.find { it.@name == "summaryTitle" }
                                def summaryFieldsPropNode = innerbeanNode.property.list.bean.property.find { it.@name == "summaryFields" }
                                bean(parent: 'Uif-StackedCollectionSection') {
                                    if (sectionTitlePropNode != null) {
                                        property(name: "title", value: sectionTitlePropNode.@value)
                                    }
                                    if (busObjPropNode != null) {
                                        property(name: "collectionObjectClass", value: busObjPropNode.@value)
                                    }
                                    if (attrNamePropNode != null) {
                                        property(name: "propertyName", value: attrNamePropNode.@value)
                                    }
                                    if (maintItems != null) {
                                        property(name: "items") {
                                            list {
                                                (0..<maintItems.list.bean.size()).each {
                                                    def innerInquiryFieldsbeanNode = maintItems.list.bean[it]
                                                    def attributeValue
                                                    innerInquiryFieldsbeanNode.attributes().each() { key, value ->
                                                        if (key.toString().endsWith("name")) {
                                                            attributeValue = value
                                                        }
                                                    }
                                                    bean('xmlns:p': pNamespaceSchema, parent: 'Uif-InputField', 'p:propertyName': attributeValue)
                                                }
                                            }
                                        }
                                    }
                                    if (summaryTitlePropNode != null) {
                                        property(name: "layoutManager.summaryTitle", value: summaryTitlePropNode.@value)
                                    }
                                    if (summaryFieldsPropNode != null) {
                                        property(name: "layoutManager.summaryFields") {
                                            list {
                                                (0..<summaryFieldsPropNode.list.bean.size()).each {
                                                    def innerSummaryFieldsbeanNode = summaryFieldsPropNode.list.bean[it]
                                                    def attributeValue
                                                    innerSummaryFieldsbeanNode.attributes().each() { key, value ->
                                                        if (key.toString().endsWith("name")) {
                                                            attributeValue = value
                                                        }
                                                    }
                                                    value(attributeValue)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    def transformTitleProperty(builder, node) {
        if (node != null) {
            builder.property(name: "headerText", value: node.@value)
        }
    }

    def addViewNameProperty(builder, viewName) {
        if (viewName != null) {
            builder.property(name: "viewName", value: viewName?.replaceAll(~/\s/, '-'))
        }
    }

    def transformMenubarProperty(builder, node) {
        if (node != null) {
            builder.property(name: "page.header.lowerGroup.items") {
                list(merge: "true") {
                    bean(parent: "Uif-Message") {
                        property(name: "messageText", value: "[" + node.@value + "]")
                    }
                }
            }
        }
    }

    /**
     * used to add comments; current implementation uses meta tags in place of standard
     * comments (node.plus and the xml serialize did not handle xml comments well)
     *
     * @param builder
     * @param comment
     * @return
     */
    def addComment(builder, comment) {
        if (comment != null) {
            builder.meta(key: "comment", value: comment)
        }
    }

    /**
     * @deprecated
     */
    def errorText() {
        log.info("=====================\nFatal Error in Script\n=====================\n")
        System.exit(2)
    }

}