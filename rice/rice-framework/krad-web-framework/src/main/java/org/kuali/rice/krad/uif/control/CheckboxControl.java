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
package org.kuali.rice.krad.uif.control;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.ErrorReport;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a HTML Checkbox control. Typically used for boolean attributes (where the
 * value is either on/off, true/false)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "checkboxControl-bean", parent = "Uif-CheckboxControl")
public class CheckboxControl extends ControlBase implements ValueConfiguredControl {
    private static final long serialVersionUID = -1397028958569144230L;

    private String value;
    private String checkboxLabel;

    private Message richLabelMessage;
    private List<Component> inlineComponents;

    public CheckboxControl() {
        super();
    }

    /**
     * Sets up rich message content for the label, if any exists
     *
     * @see Component#performApplyModel(org.kuali.rice.krad.uif.view.View, Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (richLabelMessage == null) {
            Message message = ComponentFactory.getMessage();
            view.assignComponentIds(message);
            message.setMessageText(checkboxLabel);
            message.setInlineComponents(inlineComponents);
            message.setGenerateSpan(false);
            view.getViewHelperService().performComponentInitialization(view, model, message);
            this.setRichLabelMessage(message);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(richLabelMessage);

        return components;
    }

    /**
     * The value that will be submitted when the checkbox control is checked
     *
     * <p>
     * Value can be left blank, in which case the checkbox will submit a boolean value that
     * will populate a boolean property. In cases where the checkbox needs to submit another value (for
     * instance possibly in the checkbox group) the value can be set which will override the default.
     * </p>
     *
     * @return value for checkbox
     */
    @BeanTagAttribute(name="value")
    public String getValue() {
        return value;
    }

    /**
     * Setter for the value that should be submitted when the checkbox is checked
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the label text for this checkbox
     *
     * @return the checkbox label text
     */
    @BeanTagAttribute(name="checkboxLabel")
    public String getCheckboxLabel() {
        return checkboxLabel;
    }

    /**
     * Sets the label text for this checkbox
     *
     * @param checkboxLabel the label text
     */
    public void setCheckboxLabel(String checkboxLabel) {
        this.checkboxLabel = checkboxLabel;
    }

    /**
     * Gets the Message that represents the rich message content of the label if labelText is using rich message tags.
     * <b>DO NOT set this
     * property directly unless you need full control over the message structure.</b>
     *
     * @return Message with rich message structure, null if no rich message structure
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
     * Gets the inlineComponents used by index in the checkboxLabel that has rich message component index tags
     *
     * @return the Label's inlineComponents
     */
    @BeanTagAttribute(name="inlineComponents",type= BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Component> getInlineComponents() {
        return inlineComponents;
    }

    /**
     * Sets the inlineComponents used by index in the checkboxLabel that has rich message component index tags
     *
     * @param inlineComponents
     */
    public void setInlineComponents(List<Component> inlineComponents) {
        this.inlineComponents = inlineComponents;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        CheckboxControl checkboxControlCopy = (CheckboxControl) component;
        checkboxControlCopy.setValue(this.value);
        checkboxControlCopy.setCheckboxLabel(this.checkboxLabel);

        if (this.richLabelMessage != null) {
            checkboxControlCopy.setRichLabelMessage((Message)this.richLabelMessage.copy());
        }

        if(inlineComponents != null) {
            List<Component> inlineComponentsCopy = new ArrayList<Component>();
            for(Component inlineComponent : inlineComponents)   {
                inlineComponentsCopy.add((Component)inlineComponent.copy());
            }
            checkboxControlCopy.setInlineComponents(inlineComponentsCopy);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer){
        tracer.addBean(this);

        super.completeValidation(tracer.getCopy());
    }
}
