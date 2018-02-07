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
package org.kuali.rice.krad.uif.layout;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.PropertyReplacer;
import org.kuali.rice.krad.uif.component.ReferenceCopy;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for all layout managers
 *
 * <p>
 * Provides general properties of all layout managers, such as the unique id,
 * rendering template, and style settings
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class LayoutManagerBase extends UifDictionaryBeanBase implements LayoutManager {
    private static final long serialVersionUID = -2657663560459456814L;

    private String id;
    private String template;
    private String templateName;

    private String style;
    private List<String> libraryCssClasses;
    private List<String> cssClasses;
    private List<String> additionalCssClasses;

    @ReferenceCopy(newCollectionInstance = true)
    private Map<String, Object> context;
    private Map<String, Object> unmodifiableContext;

    private List<PropertyReplacer> propertyReplacers;

    public LayoutManagerBase() {
        super();
        unmodifiableContext = context = Collections.emptyMap();
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.Container)
     */
    @Override
    public void performInitialization(View view, Object model, Container container) {
        // set id of layout manager from container
        if (StringUtils.isBlank(id)) {
            id = container.getId() + "_layout";
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#performApplyModel(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.Container)
     */
    @Override
    public void performApplyModel(View view, Object model, Container container) {

    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.Container)
     */
    @Override
    public void performFinalize(View view, Object model, Container container) {
        // put together all css class names for this component, in order
        List<String> finalCssClasses = new ArrayList<String>();

        if (this.libraryCssClasses != null && view.isUseLibraryCssClasses()) {
            finalCssClasses.addAll(libraryCssClasses);
        }

        if (this.cssClasses != null) {
            finalCssClasses.addAll(cssClasses);
        }

        if (this.additionalCssClasses != null) {
            finalCssClasses.addAll(additionalCssClasses);
        }

        cssClasses = finalCssClasses;
    }

    /**
     * Default Impl
     *
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getSupportedContainer()
     */
    @Override
    public Class<? extends Container> getSupportedContainer() {
        return Container.class;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        return new ArrayList<Component>();
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getComponentPrototypes()
     */
    @Override
    public List<Component> getComponentPrototypes() {
        List<Component> components = new ArrayList<Component>();

        return components;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getId()
     */
    @Override
    @BeanTagAttribute(name = "id")
    public String getId() {
        return this.id;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getTemplate()
     */
    @Override
    @BeanTagAttribute(name = "template")
    public String getTemplate() {
        return this.template;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setTemplate(java.lang.String)
     */
    @Override
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getTemplateName()
     */
    @BeanTagAttribute(name = "tempateName")
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setTemplateName(java.lang.String)
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getStyle()
     */
    @Override
    @BeanTagAttribute(name = "Style")
    public String getStyle() {
        return this.style;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setStyle(java.lang.String)
     */
    @Override
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @see LayoutManager#getLibraryCssClasses()
     */
    @Override
    @BeanTagAttribute(name = "libraryCssClasses", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getLibraryCssClasses() {
        return libraryCssClasses;
    }

    /**
     * @see LayoutManager#setLibraryCssClasses(java.util.List)
     */
    @Override
    public void setLibraryCssClasses(List<String> libraryCssClasses) {
        this.libraryCssClasses = libraryCssClasses;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getCssClasses()
     */
    @Override
    @BeanTagAttribute(name = "cssClasses", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getCssClasses() {
        return this.cssClasses;
    }

    /**
     * @see LayoutManager#getAdditionalCssClasses()
     */
    @Override
    @BeanTagAttribute(name = "additionalCssClasses", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getAdditionalCssClasses() {
        return additionalCssClasses;
    }

    /**
     * @see LayoutManager#setAdditionalCssClasses(java.util.List)
     */
    @Override
    public void setAdditionalCssClasses(List<String> additionalCssClasses) {
        this.additionalCssClasses = additionalCssClasses;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setCssClasses(java.util.List)
     */
    @Override
    public void setCssClasses(List<String> cssClasses) {
        this.cssClasses = cssClasses;
    }

    /**
     * Builds the HTML class attribute string by combining the styleClasses list
     * with a space delimiter
     *
     * @return class attribute string
     */
    public String getStyleClassesAsString() {
        if (cssClasses != null) {
            return StringUtils.join(cssClasses, " ");
        }

        return "";
    }

    /**
     * Sets the styleClasses list from the given string that has the classes
     * delimited by space. This is a convenience for configuration. If a child
     * bean needs to inherit the classes from the parent, it should configure as
     * a list and use merge="true"
     *
     * @param styleClasses
     */
    public void setStyleClasses(String styleClasses) {
        String[] classes = StringUtils.split(styleClasses);
        this.cssClasses = Arrays.asList(classes);
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#addStyleClass(java.lang.String)
     */
    @Override
    public void addStyleClass(String styleClass) {
        if (cssClasses == null || cssClasses.isEmpty()) {
            cssClasses = new ArrayList<String>();
        }
        
        if (!cssClasses.contains(styleClass)) {
            cssClasses.add(styleClass);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#appendToStyle(java.lang.String)
     */
    @Override
    public void appendToStyle(String styleRules) {
        if (style == null) {
            style = "";
        }
        style = style + styleRules;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getContext()
     */
    @Override
    @BeanTagAttribute(name = "context", type = BeanTagAttribute.AttributeType.MAPBEAN)
    public Map<String, Object> getContext() {
        return this.unmodifiableContext;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setContext(java.util.Map)
     */
    @Override
    public void setContext(Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            this.unmodifiableContext = this.context = Collections.emptyMap();
        } else {
            this.context = context;
            this.unmodifiableContext = Collections.unmodifiableMap(this.context);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#pushObjectToContext(java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public void pushObjectToContext(String objectName, Object object) {
        if (this.context.isEmpty()) {
            this.context = new HashMap<String, Object>();
            this.unmodifiableContext = Collections.unmodifiableMap(this.context);
        }

        this.context.put(objectName, object);
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#pushAllToContext(java.util.Map)
     */
    @Override
    public void pushAllToContext(Map<String, Object> sourceContext) {
        if (sourceContext == null || sourceContext.isEmpty()) {
            return;
        }
        
        if (this.context.isEmpty()) {
            this.context = new HashMap<String, Object>();
            this.unmodifiableContext = Collections.unmodifiableMap(this.context);
        }

        this.context.putAll(sourceContext);
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getPropertyReplacers()
     */
    @Override
    @BeanTagAttribute(name = "propertyReplacers", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<PropertyReplacer> getPropertyReplacers() {
        return this.propertyReplacers;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setPropertyReplacers(java.util.List)
     */
    @Override
    public void setPropertyReplacers(List<PropertyReplacer> propertyReplacers) {
        this.propertyReplacers = propertyReplacers;
    }

    @Override
    public <T> T copy() {
        T copiedClass = null;
        try {
            copiedClass = (T) this.getClass().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException();
        }

        copyProperties(copiedClass);

        return copiedClass;
    }

    protected <T> void copyProperties(T layoutManager) {
        super.copyProperties(layoutManager);

        LayoutManagerBase layoutManagerBaseCopy = (LayoutManagerBase) layoutManager;

        layoutManagerBaseCopy.setId(this.id);
        layoutManagerBaseCopy.setTemplate(this.template);
        layoutManagerBaseCopy.setTemplateName(this.templateName);
        layoutManagerBaseCopy.setStyle(this.style);

        if (libraryCssClasses != null) {
            layoutManagerBaseCopy.setLibraryCssClasses(new ArrayList<String>(libraryCssClasses));
        }

        if (cssClasses != null && !cssClasses.isEmpty()) {
            layoutManagerBaseCopy.setCssClasses(new ArrayList<String>(cssClasses));
        }

        if (additionalCssClasses != null) {
            layoutManagerBaseCopy.setAdditionalCssClasses(new ArrayList<String>(additionalCssClasses));
        }

        if (propertyReplacers != null) {
            List<PropertyReplacer> propertyReplacersCopy = new ArrayList<PropertyReplacer>();
            for (PropertyReplacer propertyReplacer : propertyReplacers) {
                propertyReplacersCopy.add((PropertyReplacer) propertyReplacer.copy());
            }

            layoutManagerBaseCopy.setPropertyReplacers(propertyReplacersCopy);
        }
    }
}
