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
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.ClassUtils

/**
 *
 * Used to generate directory structure and related components for the generated output
 * Focuses on directory structure, pom file, web.xml, and extras (bean validation script)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class ScaffoldGenerator {
    // includes project name
    def projectProps

    // base directories
    def inputDir

    def outputDir

    // includes directory and relative paths
    def inputPaths

    // output directory and relative paths
    def outputPaths

    // servlet properties
    def servletProps


    public ScaffoldGenerator(ConfigObject config) {
        init(config.project, config.input.dir, config.input.path, config.output.dir, config.output.path, config.servlet)
    }

    public ScaffoldGenerator(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, servletProps_) {
        init(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, servletProps_)
    }

    def init(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, servletProps_) {
        projectProps = projectProps_
        inputDir = FilenameUtils.normalize(inputDir_,true)
        inputPaths = inputPaths_
        outputDir = FilenameUtils.normalize(outputDir_,true)
        outputPaths = outputPaths_
        servletProps = servletProps_
    }

    /**
     * generates a portal tag with links related to the newly generated uif views and controllers
     *
     * @param struts_config
     * @return
     */
    def buildPortalTag(strutsConfig) {
        def links = buildPortalLinks(StrutsConverter.getActionBeans(strutsConfig), servletProps.path)
        def outputWebappDirPath = outputDir + outputPaths.src.webapp
        log.finer "number of links produced - " + links.size
        def outputFile = new File(outputWebappDirPath + "/WEB-INF/tags/rice-portal/", projectProps.app + "KradTab.tag")
        def portalTagBinding = ["app": projectProps.app, "links": links]
        ConversionUtils.buildTemplateFile(outputFile.parent, outputFile.name, ConversionUtils.getTemplateDir(), "AppTab.tag.tmpl", portalTagBinding)

    }

    /**
     * generates links to the uif views based on the action bean data
     *
     * @param actionBeans
     * @param servletPath
     * @return
     */
    def buildPortalLinks(actionBeans, servletPath) {
        def links = []

        (0..<actionBeans.size()).each {
            def actionBean = actionBeans[it]
            def controllerPath = actionBean.@path
            def viewId = ClassUtils.getShortClassName(actionBean.@type).replaceFirst(/Action/, 'View')
            def viewTitle = viewId.replaceAll(/([A-Z])/, ' $1').replaceFirst(/^\s+/, '')

            def link = buildPortalLink(viewTitle, servletPath, controllerPath, viewId)
            links.add(link)
        }
        return links
    }

    static def buildPortalLink(linkTitle, appPath, controllerPath, viewId) {
        log.finer "building portal link for: " + linkTitle
        def portalUri = '${' + "ConfigProperties.application.url" + '}/' + appPath + controllerPath + "?viewId=" + viewId + "&methodToCall=start"
        def portalLink = "<portal:portalLink displayTitle=\"true\" title=\"" + linkTitle + "\" url=\"" + portalUri + "\" />"
        return portalLink
    }

    /**
     * @deprecated
     *
     * @param templateDir
     * @return
     */
    def buildWebFragmentFile(templateDir) {
        def webXmlBinding = ["servletapp": servletProps.app, "servletpath": servletProps.path, "count": servletProps.count]
        ConversionUtils.buildTemplateFile(outputDir + outputPaths.src.resources + "/META-INF/", "web-fragment.xml", ConversionUtils.getTemplateDir(), "web-fragment.xml.tmpl", webXmlBinding)
    }

    /**
     * build a spring beans validation test  (faster approach than using startup to test)
     *
     * @param targetPath
     * @param springBeanfiles
     */
    def buildSpringBeansValidationTest(targetDirPath, springXmlFilePathList) {
        targetDirPath = FilenameUtils.normalizeNoEndSeparator(targetDirPath, true)
        def testJavaDirPath = targetDirPath + FilenameUtils.normalizeNoEndSeparator(outputPaths.test.java) + "/org/kuali/rdv/"
        def testResourcesDirPath = targetDirPath + "/src/test/resources/"

        def validTestBinding = [:]
        def configBinding = buildValidationTestBinding(targetDirPath, springXmlFilePathList)

        ConversionUtils.buildTemplateFile(testJavaDirPath, "RiceSpringBeansValidationTest.java", ConversionUtils.getTemplateDir(), "RiceSpringBeansValidationTest.java.tmpl", validTestBinding)
        ConversionUtils.buildTemplateFile(testResourcesDirPath, "rdv-config.properties", ConversionUtils.getTemplateDir(), "rdv-config.properties.tmpl", configBinding)
    }

    def buildValidationTestBinding(targetPath, springXmlFilePathList) {
        def resourcePath = FilenameUtils.normalize(FilenameUtils.concat(targetPath, outputPaths.src.resources),true)
        def binding = ["springBeanFiles": []]
        def rdvSpringXmlPathList = []
        springXmlFilePathList.removeAll(springXmlFilePathList.findAll { path -> path =~ /META-INF/ })
        springXmlFilePathList.each { springFilePath ->
            if (springFilePath.find(~/${resourcePath}(.*?)$/)) {
                log.finer "processing rdv file: " + resourcePath + " " + springFilePath
                def rdvSpringXmlPath = "classpath\\:" + ConversionUtils.getRelativePath(resourcePath, springFilePath)
                rdvSpringXmlPathList.add(rdvSpringXmlPath)
            }
        }
        binding.springBeanFiles = rdvSpringXmlPathList
        return binding
    }


    /**
     * locate project web.xml file and copy to generated output directory
     *
     * @param srcPath
     * @param targetPath
     * @return
     */
    static def copyWebXml(srcPath, targetPath) {
        def webXmlFileList = ConversionUtils.findFilesByPattern(srcPath, ~/web\.xml/)
        webXmlFileList.each() {
            def inputFile = new File(it.path)
            def outputFile = new File(targetPath + "src/main/webapp/WEB-INF/web.xml")
            FileUtils.copyFile(inputFile, outputFile)
            // TODO: modify web.xml to add new servlet call
        }
    }

    /**
     * copy portal body tag and portal tab tags, adds generated app tab to list for use
     *
     * @param sourcePath
     * @param targetPath
     * @return
     */
    static def copyPortalTags(sourcePath, targetPath, projectApp) {
        def portalBodyList = ConversionUtils.findFilesByPattern(sourcePath, ~/portalBody\.tag$/)
        def tabBinding = ["app": projectApp]
        portalBodyList.each() { portalFile ->
            if (!(portalFile.path =~ /target/)) {
                def fileIn = new File(portalFile.path)
                def fileOut = new File(targetPath + "src/main/webapp/WEB-INF/tags/rice-portal/" + portalFile.name)
                FileUtils.copyFile(fileIn, fileOut)
                // appends new portal tab for generated code
                def input = ConversionUtils.buildTemplateToString(ConversionUtils.getTemplateDir(), "portalBody.fragment.tmpl", tabBinding)
                fileOut.write(fileOut.text.replaceFirst(~/(\s+\<c:when test=\'.\{selected)/, java.util.regex.Matcher.quoteReplacement(input) + "\$1"))
            }
        }

        def tabList = ConversionUtils.findFilesByPattern(sourcePath, ~/portalTabs\.tag$/)
        tabList.each() { tabFile ->
            if (!(tabFile.path =~ /target/)) {
                def fileIn = new File(tabFile.path)
                def fileOut = new File(targetPath + "src/main/webapp/WEB-INF/tags/rice-portal/", "portalTabs.tag")
                FileUtils.copyFile(fileIn, fileOut)
                // appends a new tab for all the links included
                def input = ConversionUtils.buildTemplateToString(ConversionUtils.getTemplateDir(), "portalTabs.fragment.tmpl", tabBinding)
                fileOut.write(fileOut.text.replaceFirst(~/(\s+\<c:if test=\'.\{selected)/, java.util.regex.Matcher.quoteReplacement(input) + "\$1"))
            }
        }
    }

    /**
     * build a war overlay pom for testing purposes
     *
     * @param targetPath
     * @param groupId
     * @param artifactId
     * @param version
     * @param systemlibs - for ant projects with no dependency management
     */
    static def buildWarOverlayPom(targetPath, app, groupId, artifactId, version, systemlibs) {
        def binding = ["app": app, "groupId": groupId, "artifactId": artifactId, "version": version, "systemlibs": systemlibs]
        ConversionUtils.buildTemplateFile(targetPath, "pom.xml", ConversionUtils.getTemplateDir(), "pom.xml.tmpl", binding)
    }

}