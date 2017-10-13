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
/**
 * A groovy class which can be used to updates KNS to KRAD. Generate Attribute
 * definition, Inquiry and Lookup view definitions in one Xml file and Maintenance
 * view definitions in other Xml file.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
package org.kuali.rice.scripts

import groovy.util.logging.Log
import org.apache.commons.lang.ClassUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils

@Log
class StrutsConverter {

    def projectProps

    def servletProps

    // includes directory and relative paths
    def inputDir
    def inputPaths

    // output directory and relative paths
    def outputDir
    def outputPaths

    def beanMap

    def propertyMap

    def tagMap

    def actionClassMap

    def actionFormMap

    FormConverter formConverter

    ActionConverter actionConverter

    public StrutsConverter() {
        init(projectProps, inputDir, inputPaths, outputDir, outputPaths, tagMap, actionClassMap, actionFormMap)
    }

    public StrutsConverter(config) {
        init(config.project, config.input.dir, config.input.path, config.output.dir, config.output.path, config.map.convert.jsp_to_tag, config.map.convert.kns_action_class, config.map.convert.kns_action_form)
    }

    public StrutsConverter(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_) {
        init(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_)
    }

    def init(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_) {
        projectProps = projectProps_
        inputDir = FilenameUtils.normalize(inputDir_, true)
        inputPaths = inputPaths_
        outputDir = FilenameUtils.normalize(outputDir_, true)
        outputPaths = outputPaths_
        tagMap = tagMap_
        actionClassMap = actionClassMap_
        actionFormMap = actionFormMap_
        formConverter = new FormConverter(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_)
        actionConverter = new ActionConverter(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_)

    }

    /**
     * generates form and controller classes based on struts config data
     *
     * @param strutsConfigBean
     */
    def generateSpringComponents(strutsConfigBean) {
        // elements gathered and used in servlet binding
        def classpaths = []
        def componentClasspaths = []
        def prefixes = []
        def bundles = []
        def servletElements = ["classpaths": [], "componentClasspaths": [], "prefixes": [], "bundles": [], "dbRepoFilePath": ""]

        // generating uif forms
        def javaClassSearchDirPath = inputDir + inputPaths.src.java
        def javaClassOutputDirPath = outputDir + outputPaths.src.java
        def jspSearchDirPath = inputDir + inputPaths.src.webapp
        def formBeans = getFormBeans(strutsConfigBean)
        def actionBeans = getActionBeans(strutsConfigBean)
        formConverter.generateUifForms(formBeans, javaClassSearchDirPath)

        // for each action
        log.info "generating action related components (" + actionBeans.size() + ")"
        (0..<actionBeans.size()).each {
            // extracts relevant data from form bean
            def actionBean = actionBeans[it]
            def actionClassName = ClassUtils.getShortClassName(actionBean.@type)
            def actionClassFiles = []
            if (!StringUtils.isBlank(actionClassName)) {
                actionClassFiles = ConversionUtils.findFilesByName(javaClassSearchDirPath, actionClassName + ".java")
            }

            if (actionClassFiles.size() > 0) {
                // gather relevant data to build controller
                def actionClassData = ClassParserUtils.parseClassFile(actionClassFiles[0].text, true)

                // gathering form data for action form
                def actionBeanData = actionConverter.getActionBeanData(actionBean)
                def formBeanData = formConverter.getFormBeanData(actionBeanData.formName, formBeans)

                log.finer "build controller binding"
                def controllerBinding = actionConverter.buildControllerBinding(formBeanData, actionBeanData, actionClassData)
                prefixes.add(controllerBinding.package)
                def controllerText = buildController(controllerBinding)

                // build controller file
                def controllerFileName = ClassUtils.getShortClassName(controllerBinding.className) + ".java"
                def controllerFilePath = javaClassOutputDirPath + ConversionUtils.getRelativePathFromPackage(controllerBinding.package)
                ConversionUtils.buildFile(controllerFilePath, controllerFileName, controllerText)
                log.finer "generating new controller file: " + controllerFileName
                //   END - build controller

                // build view binding
                def jspFiles = ConversionUtils.findFilesByName(jspSearchDirPath, actionBeanData.jspFile)
                if (jspFiles.size() > 0) {
                    // gather relevant data
                    def jspRoot = JspParserUtils.parseJspFile(jspFiles[0].path)
                    log.finer "transforming jsp into view class"
                    def jspPageData = JstlConverter.transformPage(jspRoot, tagMap)
                    actionConverter.buildUifViewFile(jspPageData, formBeanData, actionClassData)
                }

            }
        }

        // loads any relevant spring files for module configuration
        classpaths = getClasspaths(outputDir + outputPaths.src.resources)

        // build module spring beans.xml
        buildModuleSpringBeansFiles(classpaths, prefixes, bundles)

    }

    /**
     *
     * @param filePath
     * @return
     */
    static def parseStrutsConfig(filePath) {
        def strutsConfig = new XmlParser().parse(filePath)
        return strutsConfig
    }

    private def getClasspaths(searchDir) {
        def classpaths = []
        def springXmlFiles = ConversionUtils.findFilesByPattern(searchDir, ~/\.xml$/, ~/META-INF/)
        (0..<springXmlFiles.size()).each {
            def relativePath = ConversionUtils.getRelativePath(searchDir, springXmlFiles[it].path)
            classpaths.add(relativePath + springXmlFiles[it].name)
        }
        return classpaths

    }

    private void buildModuleSpringBeansFiles(ArrayList classpaths, ArrayList prefixes, ArrayList bundles) {
        log.finer "generating spring beans and servlet xml"
        def outputResourcePath = outputDir + outputPaths.src.resources
        def outputWebappPath = outputDir + outputPaths.src.webapp

        def springBeanBinding = ["app": projectProps.app, "namespace": projectProps.namespace, "classpaths": classpaths.unique(), "prefixes": prefixes.unique(), "bundles": bundles.unique(), "databaseRepositoryFilePath": []]
        //"OJB-repository-" + app + ".xml"]
        // adding a replace krad servlet to wire in on top of current krad settings
        def kradServletFileName = "krad-" + projectProps.app + "-servlet.xml"
        def servletBinding = ["app": projectProps.app, "classpaths": prefixes.unique(), "resources": [kradServletFileName]]


        ConversionUtils.buildTemplateFile(outputResourcePath, kradServletFileName, ConversionUtils.getTemplateDir(), "ModuleSpringBeans.xml.tmpl", springBeanBinding)
        ConversionUtils.buildTemplateFile(outputWebappPath + "/WEB-INF/", kradServletFileName, ConversionUtils.getTemplateDir(), "ModuleSpringBeans.xml.tmpl", springBeanBinding)
        ConversionUtils.buildTemplateFile(outputWebappPath + "/WEB-INF/", "krad-servlet.xml", ConversionUtils.getTemplateDir(), "AppContext.xml.tmpl", servletBinding)
    }

    def buildControllerDataElement(actionBeanData) {
        // build controller elements
        def controllerElement = ["name": "", "class": "", "package": "", "uri": ""]
        controllerElement.name = actionBeanData.name.replaceFirst(~/Action/, 'Controller')
        controllerElement.package = actionBeanData.package.replaceAll(~/\bkns\b/, 'krad').replaceAll(~/\bstruts\b/, 'spring').replaceAll(~/\baction\b/, 'controller')
        controllerElement.path = controllerElement.package.replaceAll(/\./, '/')

        return controllerElement
    }

    static def buildController(controllerBinding) {
        def fileText = ConversionUtils.buildTemplateToString(ConversionUtils.getTemplateDir(), "UifControllerBase.java.tmpl", ["controller": controllerBinding])
        return fileText
    }

    static def getFormBeans(strutsConfigRoot) {
        def formBeans = strutsConfigRoot.'form-beans'.'form-bean'
        return formBeans
    }

    static def getActionBeans(strutsConfigRoot) {
        def actionBeans = strutsConfigRoot.'action-mappings'.'action'
        return actionBeans
    }

}