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
package org.kuali.rice.krad.uif.field;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.ErrorReport;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Link;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.LightBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Field that encloses a link element
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "linkField-bean", parent = "Uif-LinkField")
public class LinkField extends FieldBase {
    private static final long serialVersionUID = -1908504471910271148L;

    private Link link;

    public LinkField() {
        super();
    }

    /**
     * The following initialization is performed:
     *
     * <ul>
     * <li>Set the linkLabel if blank to the Field label</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        if (StringUtils.isBlank(getLinkText())) {
            setLinkText(this.getLabel());
        }
    }

    /**
     * PerformFinalize override - calls super, corrects the field's Label for attribute to point to this field's
     * content
     *
     * @param view the view
     * @param model the model
     * @param parent the parent component
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        //determine what id to use for the for attribute of the label, if present
        if (this.getFieldLabel() != null && this.getLink() != null && StringUtils.isNotBlank(this.getLink().getId())) {
            this.getFieldLabel().setLabelForComponentId(this.getLink().getId());
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(link);

        return components;
    }

    /**
     * Returns the <code>Link</code> field.
     *
     * @return The Link field
     */
    @BeanTagAttribute(name="link",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Link getLink() {
        return link;
    }

    /**
     * Setter for the <code>Link</code>  component.
     *
     * @param link
     */
    public void setLink(Link link) {
        this.link = link;
    }

    /**
     * Returns the label of the <code>Link</code> field that will be used to render the label of the link.
     *
     * @return The link label
     */
    @BeanTagAttribute(name="linkText")
    public String getLinkText() {
        return link.getLinkText();
    }

    /**
     * Setter for the link label. Sets the value on the <code>Link</code> field.
     *
     * @param linkLabel
     */
    public void setLinkText(String linkLabel) {
        link.setLinkText(linkLabel);
    }

    /**
     * Returns the target of the <code>Link</code> field that will be used to specify where to open the href.
     *
     * @return The target
     */
    @BeanTagAttribute(name="target")
    public String getTarget() {
        return link.getTarget();
    }

    /**
     * Setter for the link target. Sets the value on the <code>Link</code> field.
     *
     * @param target
     */
    public void setTarget(String target) {
        link.setTarget(target);
    }

    /**
     * Returns the href text of the <code>Link</code> field.
     *
     * @return The href text
     */
    @BeanTagAttribute(name="href")
    public String getHref() {
        return link.getHref();
    }

    /**
     * Setter for the hrefText. Sets the value on the <code>Link</code> field.
     *
     * @param hrefText
     */
    public void setHref(String hrefText) {
        link.setHref(hrefText);
    }

    /**
     * Setter for the lightBox
     *
     * @param lightBox
     */
    public void setLightBox(LightBox lightBox) {
        if (link != null) {
            link.setLightBox(lightBox);
        }
    }

    /**
     * Returns the <code>LightBox</code> used to open the link in
     *
     * @return The <code>LightBox</code>
     */
    @BeanTagAttribute(name="lightBox",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public LightBox getLightBox() {
        if (link != null) {
            return link.getLightBox();
        }

        return null;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer){
        tracer.addBean(this);

        // Checks that the link is set
        if(getLink()==null){
            if(Validator.checkExpressions(this, "link")){
                String currentValues [] = {"link = "+getLink()};
                tracer.createError("Link should be set",currentValues);
            }
        }

        // Checks that the label is set
        if(getLabel()==null){
            if(Validator.checkExpressions(this, "label")){
                String currentValues [] = {"label ="+getLabel(),"link ="+getLink()};
                tracer.createWarning("Label is null, link should be used instead",currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        LinkField linkFieldCopy = (LinkField) component;

        if (this.link != null) {
            linkFieldCopy.setLink((Link)this.link.copy());
        }
    }
}
