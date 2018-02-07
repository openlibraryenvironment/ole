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

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.UifConstants.Position;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.List;

/**
 * Content element that renders a label
 *
 * <p>
 * Contains options for adding a colon to the label along with a required message
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "label-bean", parent = "Uif-Label")
public class Label extends ContentElementBase {
    private static final long serialVersionUID = -6491546893195180114L;

    private String labelText;
    private String labelForComponentId;

    private boolean renderColon;

    private Position requiredMessagePlacement;
    private Message requiredMessage;

    private Message richLabelMessage;
    private List<Component> inlineComponents;

    public Label() {
        renderColon = true;

        requiredMessagePlacement = Position.LEFT;
    }

    /**
     * Sets up rich message content for the label, if any exists
     *
     * @see Component#performApplyModel(org.kuali.rice.krad.uif.view.View, Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (richLabelMessage == null && labelText != null &&
                labelText.contains(KRADConstants.MessageParsing.LEFT_TOKEN) &&
                labelText.contains(KRADConstants.MessageParsing.RIGHT_TOKEN)) {
            Message message = ComponentFactory.getMessage();
            view.assignComponentIds(message);

            message.setMessageText(labelText);
            message.setInlineComponents(inlineComponents);
            message.setGenerateSpan(false);

            view.getViewHelperService().performComponentInitialization(view, model, message);

            this.setRichLabelMessage(message);
        }
    }

    /**
     * The following finalization is performed:
     *
     * <ul>
     * <li>If label text is blank, set render to false for field</li>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        if (StringUtils.isBlank(getLabelText())) {
            setRender(false);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(requiredMessage);
        components.add(richLabelMessage);

        return components;
    }

    /**
     * Indicates the id for the component the label applies to
     * <p>
     * Used for setting the labelFor attribute of the corresponding HTML
     * element. Note this gets set automatically by the framework during the
     * initialize phase
     * </p>
     *
     * @return component id
     */
    @BeanTagAttribute(name="labelForComponentId")
    public String getLabelForComponentId() {
        return this.labelForComponentId;
    }

    /**
     * Setter for the component id the label applies to
     *
     * @param labelForComponentId
     */
    public void setLabelForComponentId(String labelForComponentId) {
        this.labelForComponentId = labelForComponentId;
    }

    /**
     * Text that will display as the label
     *
     * @return label text
     */
    @BeanTagAttribute(name="labelText")
    public String getLabelText() {
        return this.labelText;
    }

    /**
     * Setter for the label text
     *
     * @param labelText
     */
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    /**
     * Indicates whether a colon should be rendered after the label text,
     * generally used when the label appears to the left of the field's control
     * or value
     *
     * @return true if a colon should be rendered, false if it should not be
     */
    @BeanTagAttribute(name="renderColon")
    public boolean isRenderColon() {
        return this.renderColon;
    }

    /**
     * Setter for the render colon indicator
     *
     * @param renderColon
     */
    public void setRenderColon(boolean renderColon) {
        this.renderColon = renderColon;
    }

    /**
     * <code>Message</code> instance that will display a required indicator
     *
     * <p>
     * To indicate a field must have a value (required input) the required
     * message field can be set to display an indicator or message along with
     * the label. The message field also dictates the styling of the required
     * message
     * </p>
     *
     * @return Message instance
     */
    @BeanTagAttribute(name="requiredMessage",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Message getRequiredMessage() {
        return this.requiredMessage;
    }

    /**
     * Setter for the required message field
     *
     * @param requiredMessage
     */
    public void setRequiredMessage(Message requiredMessage) {
        this.requiredMessage = requiredMessage;
    }

    /**
     * Indicates where the required message field should be placed in relation
     * to the label field, valid options are 'LEFT' and 'RIGHT'
     *
     * @return the requiredMessage placement
     */
    @BeanTagAttribute(name="requiredMessagePlacement",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Position getRequiredMessagePlacement() {
        return this.requiredMessagePlacement;
    }

    /**
     * Setter for the required message field placement
     *
     * @param requiredMessagePlacement
     */
    public void setRequiredMessagePlacement(Position requiredMessagePlacement) {
        this.requiredMessagePlacement = requiredMessagePlacement;
    }

    /**
     * Gets the Message that represents the rich message content of the label if labelText is using rich message tags.
     * <b>DO NOT set this
     * property directly unless you need full control over the message structure.</b>
     *
     * @return rich message structure, null if no rich message structure
     */
    @BeanTagAttribute(name="richLabelMessage",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Message getRichLabelMessage() {
        return richLabelMessage;
    }

    /**
     * Sets the Message that represents the rich message content of the label if it is using rich message tags.  <b>DO
     * NOT set this
     * property directly unless you need full control over the message structure.</b>
     *
     * @param richLabelMessage
     */
    public void setRichLabelMessage(Message richLabelMessage) {
        this.richLabelMessage = richLabelMessage;
    }

    /**
     * Gets the inlineComponents used by index in a Label that has rich message component index tags in its labelText
     *
     * @return the Label's inlineComponents
     */
    @BeanTagAttribute(name="inlineComponents",type= BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Component> getInlineComponents() {
        return inlineComponents;
    }

    /**
     * Sets the inlineComponents used by index in a Label that has rich message component index tags in its labelText
     *
     * @param inlineComponents
     */
    public void setInlineComponents(List<Component> inlineComponents) {
        this.inlineComponents = inlineComponents;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer){
        tracer.addBean(this);

        if(tracer.getValidationStage()== ValidationTrace.BUILD){
            // Checks that text is set if the component is rendered
            if(isRender() && getLabelText()==null){
                if(!Validator.checkExpressions(this, "labelText")) {
                    String currentValues [] = {"render = "+isRender(),"labelText ="+getLabelText()};
                    tracer.createError("LabelText should be set if render is true",currentValues);
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
        Label labelCopy = (Label) component;

        if (this.inlineComponents != null) {
            List<Component> inlineComponents = Lists.newArrayListWithExpectedSize(this.inlineComponents.size());

            for (Component inlineComponent : this.inlineComponents) {
                inlineComponents.add((Component) inlineComponent.copy());
            }

            labelCopy.setInlineComponents(inlineComponents);
        }

        labelCopy.setLabelForComponentId(this.labelForComponentId);
        labelCopy.setLabelText(this.labelText);
        labelCopy.setRenderColon(this.renderColon);

        if (this.requiredMessage != null) {
            labelCopy.setRequiredMessage((Message)this.requiredMessage.copy());
        }

        if (this.richLabelMessage != null) {
            labelCopy.setRichLabelMessage((Message)this.richLabelMessage.copy());
        }

        labelCopy.setRequiredMessagePlacement(this.requiredMessagePlacement);
    }
}
