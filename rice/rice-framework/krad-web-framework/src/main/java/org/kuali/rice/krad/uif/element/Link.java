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
package org.kuali.rice.krad.uif.element;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ErrorReport;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.LightBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Content element that renders a link
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "link-bean", parent="Uif-Link"), @BeanTag(name = "createNewLink-bean", parent = "Uif-CreateNewLink")})
public class Link extends ContentElementBase {
    private static final long serialVersionUID = 8989868231938336068L;

    private String linkText;
    private String target;
    private String href;

    private boolean openInLightbox;

    private LightBox lightBox;

    public Link() {
        super();
    }

    /**
     * The following updates are done here:
     *
     * <ul>
     * <li>Initialize the nested lightBox widget if open in lightbox is true</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.Component#performApplyModel(org.kuali.rice.krad.uif.view.View, java.lang.Object,
     *      org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (openInLightbox && (lightBox == null)) {
            lightBox = ComponentFactory.getLightBox();
        }
    }

    /**
     * Special handling for lightbox links to add and onclick data attribute to be handled by a global handler
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        if (lightBox != null && lightBox.isRender()){
            this.addDataAttribute(UifConstants.DataAttributes.ONCLICK, "handleLightboxOpen(jQuery(this), " +
                    lightBox.getTemplateOptionsJSString() + ", " + lightBox.isAddAppParms() + ", e);");
            lightBox.setRender(false);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(lightBox);

        return components;
    }

    /**
     * Returns the label of the link
     *
     * @return The link label
     */
    @BeanTagAttribute(name="linkText")
    public String getLinkText() {
        return linkText;
    }

    /**
     * Setter for the link label
     *
     * @param linkText
     */
    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    /**
     * Returns the target that will be used to specify where to open the href
     *
     * @return The target
     */
    @BeanTagAttribute(name="target")
    public String getTarget() {
        return target;
    }

    /**
     * Setter for the link target
     *
     * @param target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Returns the href text
     *
     * @return The href text
     */
    @BeanTagAttribute(name="href")
    public String getHref() {
        return href;
    }

    /**
     * Setter for the hrefText
     *
     * @param href
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * Indicates whether the link URL should be opened in a lightbox
     *
     * <p>
     * If set the target attribute is ignored and the URL is opened in a lightbox instead
     * </p>
     *
     * @return true to open link in a lightbox, false if not (follow standard target attribute)
     */
    public boolean isOpenInLightbox() {
        return openInLightbox;
    }

    /**
     * Setter that indicates whether the link should be opened in a lightbox
     *
     * @param openInLightbox
     */
    public void setOpenInLightbox(boolean openInLightbox) {
        this.openInLightbox = openInLightbox;
    }

    /**
     * Returns the <code>LightBox</code> used to open the link in
     *
     * @return The <code>LightBox</code>
     */
    @BeanTagAttribute(name="lightBox",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public LightBox getLightBox() {
        return lightBox;
    }

    /**
     * Setter for the lightBox
     *
     * @param lightBox
     */
    public void setLightBox(LightBox lightBox) {
        this.lightBox = lightBox;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer){
        ArrayList<ErrorReport> reports=new ArrayList<ErrorReport>();
        tracer.addBean(this);

        if(tracer.getValidationStage()== ValidationTrace.BUILD){

            // Checks that href is set
            if(getHref()==null){
                if(!Validator.checkExpressions(this, "href")){
                    String currentValues [] = {"href ="+getHref()};
                    tracer.createError("Href must be set",currentValues);
                }
            }

            // Checks that the text is set
            if(getLinkText()==null){
                if(!Validator.checkExpressions(this, "linkText")){
                    String currentValues [] = {"linkText = "+getLinkText()};
                    tracer.createError("LinkText must be set",currentValues);
                }
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
        Link linkCopy = (Link) component;
        linkCopy.setHref(this.href);

        if (this.lightBox != null) {
            linkCopy.setLightBox((LightBox)this.lightBox.copy());
        }

        linkCopy.setLinkText(this.linkText);
        linkCopy.setOpenInLightbox(this.openInLightbox);
        linkCopy.setTarget(this.target);
    }
}
