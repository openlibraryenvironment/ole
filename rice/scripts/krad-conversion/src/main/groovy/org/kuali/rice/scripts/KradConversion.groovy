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
 *  uses properties file to load settings, builds scaffolding for project and runs related conversion
 *
 *  script pulls an input and target directory
 *  target directory is wiped and a structure is setup based on a web application maven project
 *  using the struts-config.xml the file is parsed and processed into creating a basic web-overlay project
 *  so the generated code can be tested without mixing with existing source
 */
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils
import org.kuali.rice.scripts.ConversionUtils
import org.kuali.rice.scripts.DictionaryConverter
import org.kuali.rice.scripts.ScaffoldGenerator
import org.kuali.rice.scripts.StrutsConverter

// load configuration file(s)
def configFilePath = "./src/main/resources/krad.conversion.properties"
def projectConfigFilePath = System.getProperty("alt.config.location")
def config = ConversionUtils.getConfig(configFilePath, projectConfigFilePath)


// load any config-specific members
def inputDir = FilenameUtils.normalize(config.input.dir, true)
def outputDir = FilenameUtils.normalize(config.output.dir, true)
def outputPathList = config.output.path.list

def outputResourceDir = outputDir + config.output.path.src.resources
def projectApp = config.project.app
def templateDir = config.script.path.template
def groupId = config.project.artifact.groupId
def artifactId = config.project.artifact.artifactId
def version = config.project.artifact.version

def strutsSearchDirPath = config.input.dir + config.input.path.src.webapp

// setup all necessary classes
ScaffoldGenerator scaffold = new ScaffoldGenerator(config)
StrutsConverter struts = new StrutsConverter(config)
DictionaryConverter dictionary = new DictionaryConverter(config)

if (StringUtils.isBlank(inputDir) || StringUtils.isBlank(outputDir)) {
    println "Error:\nplease configure your input and output directories before continuing\n\n"
}

// experimental - checkout svn directory instead of using input base dir.
if (config.project.use.svn == "true") {
    println "loading project from svn"
    def command = [config.project.svn.bin, "checkout", config.project.svn.path, config.project.src.dir]
    print command.toString()
    def proc = command.execute()
    proc.waitFor()
    println "finished loading project from svn"
}


def strutsConfigFiles = ConversionUtils.findFilesByName(strutsSearchDirPath, "struts-config.xml")
println "Load struts-config.xml files for processing - dir: " + strutsSearchDirPath + " " + strutsConfigFiles?.size()
if (strutsConfigFiles.size > 0) {
    ConversionUtils.buildDirectoryStructure(outputDir, outputPathList, true)
    ScaffoldGenerator.copyWebXml(inputDir, outputDir)
    ScaffoldGenerator.copyPortalTags(inputDir, outputDir, projectApp)
    ScaffoldGenerator.buildWarOverlayPom(outputDir, projectApp, groupId, artifactId, version, [])
    dictionary.convertDataDictionaryFiles()
}

// assuming there should only be one struts-config.xml
print "Generating all necessary spring components (controllers, forms, views) from struts information"
if (strutsConfigFiles != null && strutsConfigFiles.size() > 0) {
    def strutsConfig = StrutsConverter.parseStrutsConfig(strutsConfigFiles[0].path)
    struts.generateSpringComponents(strutsConfig)
    scaffold.buildPortalTag(strutsConfig)
}

// find all spring files and add to a rice validation test (good precursor test)
def springBeansFileList = ConversionUtils.findFilesByPattern(outputResourceDir, ~/\.xml$/)
def springBeansFilePathList = []
springBeansFileList.each { file -> springBeansFilePathList << file.path }

// includes a spring validation test to allow for testing before running the server application
print "Generating spring validation test based on resulting output from conversion"
scaffold.buildSpringBeansValidationTest(outputDir, springBeansFilePathList);

println " -- Script Complete"
println " -- open directory " + outputDir
println " -- prep project -- mvn eclipse:clean eclipse:eclipse generate-resources "
println " -- if using eclipse add target/generate-resources directory as a referenced library (Configure -> Build Path -> Library -> Add Class Folder "
// end of script
