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
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.ClassUtils

/**
 * Converts action bean and class data into relevant uif related components (i.e. controller and view)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class ActionConverter {

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

    public ActionConverter() {
        init(projectProps, inputDir, inputPaths, outputDir, outputPaths, tagMap, actionClassMap, actionFormMap)
    }

    public ActionConverter(config) {
        init(config.project, config.input.dir, config.input.path, config.output.dir, config.output.path, config.map.convert.jsp_to_tag, config.map.convert.kns_action_class, config.map.convert.kns_action_form)
    }

    public ActionConverter(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_) {
        init(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_)
    }

    def init(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_) {
        projectProps = projectProps_
        inputDir = inputDir_
        inputPaths = inputPaths_
        outputDir = outputDir_
        outputPaths = outputPaths_
        tagMap = tagMap_
        actionClassMap = actionClassMap_
        actionFormMap = actionFormMap_
    }

    /**
     * returns a map containing changed (form, controller, viewType) based on the action class
     *
     * */
    def getActionClassTransform(actionClassParentClass) {
        def actionClassElement = actionClassMap.get("default")
        if (actionClassMap != null && actionClassMap?.keySet().size() > 0) {
            actionClassMap.keySet().each {
                if (it =~ /${actionClassParentClass}$/) {
                    actionClassElement = actionClassMap.get(it)
                }
            }
        }
        return actionClassElement
    }

    /**
     * returns a binding map for use by the controller template
     *
     * */
    def buildControllerBinding(formBeanData, actionElement, actionClass) {
        // controller class binding map
        def controllerBinding = ["header": "",
                "author": "",
                "package": "",
                "imports": [],
                "uri": "",
                "className": "",
                "parentClassName": "",
                "formClassName": "",
                "actionMethods": [],
                "privateMethods": []]



        // fill in the controller class
        controllerBinding.header = "/** krad generated script **/"
        controllerBinding.author = "krad-generator";
        controllerBinding.package = actionElement.controller.package
        controllerBinding.imports = actionClass.imports
        // add form class to controller imports
        if (!controllerBinding.imports.find { it == formBeanData.class }) {
            controllerBinding.imports.add(formBeanData.class)
        }
        def actionTransform = getActionClassTransform(actionClass.parentClass)
        controllerBinding.imports.add(actionTransform.form)
        controllerBinding.uri = actionElement.uri;

        controllerBinding.className = ClassUtils.getShortClassName(actionElement.controller.name)
        controllerBinding.parentClassName = actionTransform.controller
        //actionElement.controller.parentClassName ?: "UifControllerBase"

        controllerBinding.formClassName = actionElement.formName ?: "UifFormBase"
        controllerBinding.actionMethods = actionClass.methods.findAll { it.returnType =~ /ActionForward/ } ?: []
        controllerBinding.privateMethods = actionClass.methods.findAll { it.accessModifier =~ /private/ } ?: []
        controllerBinding.oldActionClass = actionElement.class

        return controllerBinding
    }

    /**
     * build an action data map and related controller data from the action bean
     * @param actionBean
     * @return
     */
    def getActionBeanData(actionBean) {
        def actionBeanData = ["name": "", "formName": "", "class": "", "jspFile": "", "uri": "", "controller": ["name": "", "class": "", "package": "", "uri": ""]]
        actionBeanData.name = FilenameUtils.getName(actionBean.@type.toString())
        actionBeanData.formName = actionBean.@name
        actionBeanData.class = actionBean.@type.toString()
        actionBeanData.jspFile = actionBean.@input.toString()
        actionBeanData.package = actionBean.@type.toString().replaceFirst(~/\.[^\.]+$/, "")
        actionBeanData.uri = actionBean.@path ?: ""

        actionBeanData.controller = buildControllerDataElement(actionBeanData)

        log.finer "action element data: " + actionBeanData
        return actionBeanData
    }

    /**
     * converts action bean data into relevant controller data
     *
     * @param actionBeanData
     * @return
     */
    def buildControllerDataElement(actionBeanData) {
        // build controller elements
        def controllerElement = ["name": "", "class": "", "package": "", "uri": ""]
        controllerElement.name = actionBeanData.name.replaceFirst(~/Action/, 'Controller')
        controllerElement.package = actionBeanData.package.replaceAll(~/\bkns\b/, 'krad').replaceAll(~/\bstruts\b/, 'spring').replaceAll(~/\baction\b/, 'controller')
        controllerElement.path = ConversionUtils.getRelativePathFromPackage(controllerElement.package)

        return controllerElement
    }


    private void buildUifViewFile(LinkedHashMap<String, ArrayList> jspPageData, LinkedHashMap<String, String> formBeanData, LinkedHashMap<String, Serializable> actionClassData) {
        // build view data and related text
        def viewBinding = JstlConverter.buildViewBinding(jspPageData, formBeanData, actionClassData)
        viewBinding.viewType = getActionClassTransform(actionClassData.parentClass).view
        def viewText = JstlConverter.buildUifView(viewBinding)

        // build file
        def viewFilePath = outputDir + outputPaths.src.resources
        def viewFileName = actionClassData.className + "View.xml"
        log.info "building form file: " + viewFileName
        ConversionUtils.buildFile(viewFilePath, viewFileName, viewText)
    }

}
