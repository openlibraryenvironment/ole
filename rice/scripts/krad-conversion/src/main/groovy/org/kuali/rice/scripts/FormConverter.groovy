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
import org.apache.commons.lang.ClassUtils
import org.apache.commons.lang.StringUtils

/**
 * Converts kns form data into relevant uif form data
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Log
class FormConverter {

    def formSourceDir
    def formDestDir


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

    public FormConverter() {
        init(projectProps, inputDir, inputPaths, outputDir, outputPaths, tagMap, actionClassMap, actionFormMap)
    }

    public FormConverter(config) {
        init(config.project, config.input.dir, config.input.path, config.output.dir, config.output.path, config.map.convert.jsp_to_tag, config.map.convert.kns_action_class, config.map.convert.kns_action_form)
    }

    public FormConverter(projectProps_, inputDir_, inputPaths_, outputDir_, outputPaths_, tagMap_, actionClassMap_, actionFormMap_) {
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

    private void generateUifForms(formBeans, formSearchDir) {
        // for each form, build krad form
        log.finer "generating spring krad forms (" + formBeans.size() + ")"
        (0..<formBeans.size()).each {
            def formClassName = ClassUtils.getShortClassName(formBeans[it].@type)
            def formFileList = []
            if (!StringUtils.isBlank(formClassName)) {
                formFileList = ConversionUtils.findFilesByName(formSearchDir, formClassName + ".java")
            }

            if (formFileList.size() > 1) {
                log.warning("form list for " + formClassName + " returns more than 1 result [" + formFileList.size() + "]")
            }
            if (formFileList.size() > 0) {
                // gather relevant data  and build form file
                def formClassData = ClassParserUtils.parseClassFile(formFileList[0].text, true)
                log.finer "processed form data: " + formClassData
                buildUifFormFile(formClassData)
            }
        }
    }

    /**
     * helper function to pull form element data from form beans
     *
     * @param formName
     * @param formBeans - form beans from struts xml
     * @return
     */
    def getFormBeanData(formName, formBeans) {
        // default form element  contains a name class and path
        def formData = ["name": "UifFormBase", "class": "org.kuali.rice.krad.web.form.UifFormBase", "path": "org/kuali/rice/krad/web/form/"]
        def formBean = formBeans.find { it.@name == formName }
        if (formBean != null) {
            log.finer "found matching form element for " + formName
            formData.name = formBean.@name
            formData.class = formBean.@type
            formData.path = formData.path.replaceFirst(~/[^\.]*?$/, "").replaceAll(~/\./, "/")
        }
        return formData
    }

    private void buildUifFormFile(LinkedHashMap<String, Serializable> formData) {
        // build form
        def formBinding = buildFormBinding(formData)
        def formText = buildUifForm(formBinding)

        // build file
        def formFileName = formBinding.className + ".java"
        def formFilePath = outputDir + outputPaths.src.java + ConversionUtils.getRelativePathFromPackage(formBinding.package)
        ConversionUtils.buildFile(formFilePath, formFileName, formText)
        log.info "generating new form file: " + formFilePath + "/" + formFileName
    }

    /**
     * returns a map containing changed (form, controller, viewType) based on the action form parent class
     *
     * @param actionFormParentClass
     * @return
     */
    def getActionFormTransform(actionFormParentClass) {
        def actionFormElement = actionFormMap.get("default")
        if (actionFormMap != null && actionFormMap?.keySet().size() > 0) {
            actionFormMap.keySet().each {
                if (it =~ /${actionFormParentClass}$/) {
                    actionFormElement = actionFormMap.get(it)
                }
            }
        }
        return actionFormElement
    }

    /**
     * generates new uif form binding data from existing form class data
     *
     * @param formData
     * @return
     */
    def buildFormBinding(formData) {
        log.finer "form data imports are " + formData.imports
        def formBinding = formData
        def formTransform = getActionFormTransform(formData.parentClass)

        if (formTransform != null) {
            // replace existing parent class/import with new parent class/import
            if (formData.parentClass != null && formData.parentClass != "") {
                formBinding.imports.removeAll { it =~ /${formData.parentClass}$/ }
            }
            formBinding.parentClass = formTransform.form.replaceFirst(~/^.*?\.([^\.])$/, '$1')
            formBinding.imports.add(formTransform.form)

        }
        log.finer "form binding imports are " + formBinding.imports
        return formBinding
    }

    /**
     * builds a uif form based on form binding data
     *
     * @param formBinding
     * @return
     */
    static def buildUifForm(formBinding) {
        def fileText = ConversionUtils.buildTemplateToString(ConversionUtils.getTemplateDir(), "UifFormBase.java.tmpl" + "", formBinding)
        return fileText
    }

}
