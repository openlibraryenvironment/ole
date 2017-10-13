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
import groovy.xml.MarkupBuilder

/**
 * JstlConverter.groovy
 *
 * Utility class for parsing jsp/tag files and building krad view using kns data
 */
@Log
class JstlConverter {

    public JstlConverter(config_) {
        init();
    }

    def init() {}

    /**
     * identifies page element via (*page) tag and uses the page element to build out a uif version
     *
     * @param builder
     * @param root
     * @param jspMap
     * @return
     */
    public static def transformPage(root, jspMap) {
        int depth = 0;
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        // locate any page elements and convert into a page

        def bindingElements = ["pages": [], "beans": []]
        log.finer "parsing root tag " + depth + " : " + root.name() + "with children count" + root.children().size()
        if (root.children().size() > 0) {
            log.finer "locate it element for: " + root.name()
            def count = 0
            def pages = root.'**'.findAll { it.name() =~ /(?i)page/ }
            for (page in pages) {
                count++
                def pageData = page.attributes()
                log.finer "processing page element" + jspMap + " " + page
                builder.bean(id: getPageId(pageData.htmlformaction, count), parent: (jspMap[page.name()] ?: page.name())) {
                    log.finer "transform page attributes"
                    transformPageAttributes(delegate, bindingElements, page, jspMap)
                    log.finer "transform page elements"
                    property(name: "items") {
                        list("merge": "true") {
                            transformPageElements(delegate, bindingElements, page, jspMap, depth)
                        }
                    }
                }
                pageData.put("text", writer.toString())
                pageData.put("id", getPageId(pageData.htmlformaction, count))
                log.finer "page data" + pageData
                bindingElements.pages.add(pageData)
            }
        }
        return bindingElements
    }



    public static def transformPageAttributes(builder, bindingElements, page, jspMap) {
        // TODO: convert page attributes into properties
    }

    public static def getPageId(viewName, count) {
        return viewName + "-Page" + count
    }

    /**
     * recursively searches through page elements for kuali, html, and unknown tag element
     *
     * @param builder
     * @param bindingElements
     * @param page
     * @param jspMap
     * @param depth
     * @return
     */
    public static def transformPageElements(builder, bindingElements, page, jspMap, depth) {
        depth++
        log.info "processing page element children"
        def pageChildren = page.children()
        if (pageChildren.size() > 0) {
            log.finer "page children exists : " + pageChildren.size()
            (0..<pageChildren.size()).each {
                if (pageChildren[it].name() =~ /kul\:/) {
                    log.finer "kuali page child (" + it + ") - " + pageChildren[it].name()
                    transformKualiElement(builder, bindingElements, pageChildren[it], jspMap, depth)
                } else if (pageChildren[it].name() =~ /html\:/) {
                    log.finer "html (html namespace) page child (" + it + ") - " + pageChildren[it].name()
                    transformHtmlElement(builder, bindingElements, pageChildren[it], jspMap, depth)
                } else if (pageChildren[it].name().find(~/:/) == null) {
                    log.finer "html (no namespace) page child (" + it + ") - " + pageChildren[it].name()
                    transformHtmlElement(builder, bindingElements, pageChildren[it], jspMap, depth)
                } else {
                    log.finer "unknown page child (" + it + ") - " + pageChildren[it].name()
                    transformUnknownElement(builder, bindingElements, pageChildren[it], jspMap, depth)
                }
            }

        }
        depth--
    }

    /**
     * transforms into html elements counterparts and begins searching for additional page elements
     *
     * @param builder
     * @param bindingElements
     * @param elem
     * @param jspMap
     * @param depth
     * @return
     */
    public static def transformHtmlElement(builder, bindingElements, elem, jspMap, depth) {
        log.finer "parsing html elem tag " + depth + " : " + elem.name()
        def elemChildren = elem.children()
        if (elemChildren.size() > 0) {
            (0..<elemChildren.size()).each {
                transformPageElements(builder, bindingElements, elemChildren[it], jspMap, depth)
            }
        }
    }

    /**
     * transforms into uif counterparts and searches for additional page elements
     *
     *
     * @param builder
     * @param bindingElements
     * @param elem
     * @param jspMap
     * @param depth
     * @return
     */
    public static def transformKualiElement(builder, bindingElements, elem, jspMap, depth) {
        log.finer "parsing kuali elem tag " + depth + " : " + elem + bindingElements
        if (elem != null && jspMap[elem.name()] != null && jspMap[elem.name()] != "") {
            builder.bean(parent: jspMap[elem.name()]) {
                def elemChildren = elem.children()
                if (elemChildren.size() > 0) {
                    if (jspMap[elem.name()] =~ /Section/) {
                        property("name": "items") {
                            list("merge": "true") {
                                (0..<elemChildren.size()).each {
                                    transformPageElements(delegate, bindingElements, elemChildren[it], jspMap, depth)
                                }
                            }
                        }
                    } else {
                        (0..<elemChildren.size()).each {
                            transformPageElements(delegate, bindingElements, elemChildren[it], jspMap, depth)
                        }
                    }

                }
            }
        }
    }

    /**
     * unknown elements are generally application-specific tag elements, beans are generated and the
     * method continues to search for additional page elements to convert
     *
     * @param builder
     * @param bindingElements
     * @param elem
     * @param jspMap
     * @param depth
     * @return
     */
    public static def transformUnknownElement(builder, bindingElements, elem, jspMap, depth) {
        log.finer "parsing unknown elem tag " + depth + " : " + elem.name() + bindingElements
        def tagBean = transformTagToBeanId(elem.name())
        def tagParentBean = tagBean + "-parentBean"
        bindingElements.beans.add(tagParentBean)
        builder.bean(parent: tagParentBean)
        def elemChildren = elem.children()
        if (elemChildren.size() > 0) {
            (0..<elemChildren.size()).each {
                transformPageElements(builder, bindingElements, elemChildren[it], jspMap, depth)
            }
        }
    }

    public static def transformTagToBeanId(tagName) {
        def beanId = tagName.replaceAll(~/:/, "-")
    }


    /**
     * builds a uif view page from the following
     * form-bean element (contains the form name and class), form title and id comes from name
     * jspRoot: contains the pages and the beans
     *
     * @param jspRoot
     * @param formClass
     * @param actionClass
     * @return
     */
    public static def buildUifView(viewBinding) {
        def fileText = ConversionUtils.buildTemplateToString(ConversionUtils.getTemplateDir(), "UifView.xml.tmpl", viewBinding)
        return fileText
    }

    public static def buildViewBinding(jspData, formBeansData, actionClassData) {
        def uifViewBinding = [beanId: "", viewType: "Uif-FormView", viewId: "", viewName: "", headerText: "", entryPageId: "", items: [], formClass: "", beans: [], pages: []]
        log.finer "building view binding based on jsp, form bean, and action class data"
        uifViewBinding = buildViewBindingJspData(uifViewBinding, jspData)
        uifViewBinding = buildViewBindingFormData(uifViewBinding, formBeansData)
        uifViewBinding = buildViewBindingActionClassData(uifViewBinding, actionClassData)
        return uifViewBinding
    }

    /**
     *  extracts relevant data from jsp data for view binding
     * */
    public static def buildViewBindingJspData(uifViewBinding, jspData) {
        uifViewBinding.pages = jspData.pages
        uifViewBinding.entryPageId = jspData.pages[0].id
        uifViewBinding.beans = jspData.beans
        return uifViewBinding
    }

    /**
     *   extracts relevant data from action form data for view binding
     * */
    public static def buildViewBindingFormData(uifViewBinding, formBeansData) {
        uifViewBinding.formClass = formBeansData.class
        return uifViewBinding
    }

    /**
     *  extracts relevant data from action class data for view binding
     * */
    public static def buildViewBindingActionClassData(uifViewBinding, actionClassData) {
        log.finer "buildViewBindingActionClassData -  loading action class data into view binding"
        def viewId = getViewNameFromActionClass(actionClassData.className)
        def viewTitle = viewId.replaceAll(/([A-Z])/, ' $1').replaceAll(/(\d+)/, ' $1').replaceFirst(/^\s*/, "")
        uifViewBinding.viewId = viewId
        uifViewBinding.beanId = viewId
        uifViewBinding.viewTitle = viewTitle
        uifViewBinding.viewName = viewId
        uifViewBinding.entryPageId = viewId + "-Page1"
        return uifViewBinding
    }

    public static def getViewNameFromActionClass(actionClassName) {
        assert actionClassName != null, 'Action classname parameter must not be null'
        return actionClassName.replaceFirst(/Action/, 'View')
    }

}