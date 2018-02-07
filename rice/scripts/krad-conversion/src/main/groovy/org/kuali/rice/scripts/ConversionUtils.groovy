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

import groovy.text.GStringTemplateEngine
import groovy.util.logging.Log
import org.apache.commons.io.FilenameUtils
import org.codehaus.plexus.util.StringUtils

/**
 * Utilities related to conversion script
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class ConversionUtils {

    static def baseResourcesDir = "./src/main/resources/"

    /**
     * converts java package name into a file path
     *
     * @param filePackage
     * @return string
     */
    public static def getRelativePathFromPackage(filePackage) {
        return filePackage.replaceAll(~/\./, "/")
    }

    /**
     * extracts the relative path of the file to its base directory
     *
     * @param parentDir
     * @param fullFilePath
     * @return
     */
    public static def getRelativePath(parentDir, fullFilePath) {
        def relativePath = ""

        // perform file path cleanup
        parentDir = FilenameUtils.normalize(parentDir, true)
        fullFilePath = FilenameUtils.normalize(fullFilePath, true)
        def filePath = FilenameUtils.getFullPath(fullFilePath)

        if (parentDir == filePath) {
            return ""
        }

        def relative_match = filePath =~ /(?:$parentDir)(.*?)$/
        if (relative_match != null && relative_match.size() > 0) {
            relativePath = relative_match[0][1]
        }
        return relativePath
    }

    /**
     * pulls (business) object name from file name
     * some file types have special suffixes such as MaintenanceDocument can be set in file type
     *
     * @param inputDir
     * @param resourcePath
     * @param fileType
     * @return
     */
    public static def getObjectName(fileName, fileType) {
        def objName = FilenameUtils.getBaseName(fileName)
        // remove filetype (MaintenanceDocument, etc..) or Document suffix from object name
        if (!StringUtils.isEmpty(fileType)) {
            if (objName.contains(fileType)) {
                objName = StringUtils.stripEnd(objName, fileType)
            } else if (objName.contains("Document")) {
                objName = StringUtils.stripEnd(objName, "Document")
            }
        }
        return objName
    }

    public static def findFilesByName(searchDirPath, fileName) {
        def filePattern = ~/${fileName}$/
        return findFilesByPattern(searchDirPath, filePattern)
    }

    /**
     *  recursively searches through file directory for matching patterns
     *
     * @param filePattern
     * @param searchDirPath
     * @return
     */
    public static def findFilesByPattern(searchDirPath, inclPattern) {
        return findFilesByPattern(searchDirPath, inclPattern, null)
    }

    /**
     *  recursively search directory for files matching patterns
     *
     * @param searchDirPath
     * @param inclPattern - pattern to include
     * @param exclPatterns - pattern that supercede the include pattern
     * @return
     */
    public static def findFilesByPattern(searchDirPath, inclPattern, exclPattern) {
        def dir = new File(searchDirPath)
        def files = []
        log.finer "searching " + searchDirPath + " for files matching pattern: " + inclPattern
        dir.eachFileRecurse { resultFile ->
            boolean isInclFile = inclPattern != null && resultFile.path.find(inclPattern)
            boolean isExclFile = exclPattern != null && resultFile.path.find(exclPattern)

            if (isInclFile && !isExclFile) {
                files << resultFile
            }
        }
        return files
    }

    public static def getConfig(defaultConfigFilePath) {
        return ConversionUtils.getConfig(defaultConfigFilePath, "")
    }

    /**
     * loads default config and project config, merging one on top of the other
     *
     * @param defaultConfigFilePath
     * @param projectConfigFilePath
     * @return
     */
    public static def getConfig(defaultConfigFilePath, projectConfigFilePath) {
        def defaultConfigFile = new File(defaultConfigFilePath)
        def config = new ConfigSlurper().parse(defaultConfigFile.toURL())
        if (projectConfigFilePath != null && projectConfigFilePath != "") {
            File altConfigFile = new File(projectConfigFilePath)
            if (altConfigFile.exists()) {
                def altConfig = new ConfigSlurper().parse(altConfigFile.toURL())
                config.merge(altConfig)
            }
        }
        return config
    }

    static def modifyFileWithText(file, replacePattern, text) {
        file.write(file.text.replaceFirst(replacePattern, java.util.regex.Matcher.quoteReplacement(text) + "\$1"))
    }

    /**
     *  used to build out base directory structure
     *
     * @param baseDirPath - directory to be used as a base directory for target project
     * @param relDirPathList - all paths to be created in new directory
     * @param safeToDelete - safe to delete existing base directory if it exists
     * @return
     */
    static def buildDirectoryStructure(baseDirPath, relDirPathList, safeToDelete) {
        def baseDir = new File(baseDirPath)
        if (baseDir.exists() && safeToDelete) {
            baseDir.deleteDir()
            log.info "deleted existing base directory: " + baseDir.path
        }
        baseDir.mkdirs()
        log.info "created new base directory: " + baseDir.path

        relDirPathList.each {
            def relPathDir = new File(baseDirPath + it)
            relPathDir.mkdirs()
            log.info "created new directory: " + relPathDir.path
        }
    }

    /**
     * Generates a template file (used for UifControllers, UifViews, pom.xml and customSpringBeans.xml)
     *
     * @param outputFileDir
     * @param outputFileName
     * @param templateFileDir
     * @param templateFileName
     * @param binding
     */
    static def buildTemplateFile(outputFileDir, outputFileName, templateFileDir, templateFileName, binding) {
        def fileText = buildTemplateToString(templateFileDir, templateFileName, binding)
        buildFile(outputFileDir, outputFileName, fileText)
    }

    static def getTemplateDir() {
        return baseResourcesDir + "templates/"
    }

    /**
     * writes a new file and creates the related directory paths if they do not exist
     *
     * @param outputFileDirPath
     * @param outputFileName
     * @param fileText
     * @return file
     */
    static def buildFile(outputFileDirPath, outputFileName, fileText) {
        outputFileDirPath = FilenameUtils.normalize(outputFileDirPath, true)

        def outputDir = new File(outputFileDirPath)
        outputDir.mkdirs()
        def outputFile = new File(outputDir, outputFileName)
        log.info "create a new file from template: " + outputFile.path
        outputFile.createNewFile()
        outputFile.write(fileText)
        return outputFile
    }

    /**
     * Generates a template for strings (usually used for fragments that need to included in other files)
     *
     * @param templateFileDir
     * @param templateFileName
     * @param binding
     * @return
     */
    static def buildTemplateToString(templateFileDir, templateFileName, binding) {
        // TODO: replace base resource dir with getResource
        def templateFile = new File(templateFileDir, templateFileName)
        def engine = new GStringTemplateEngine()
        def processedTemplate = engine.createTemplate(templateFile).make(binding)
        processedTemplate.toString()
    }
}