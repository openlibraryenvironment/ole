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

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ExpressionUtils;
import org.kuali.rice.krad.uif.util.KeyMessage;
import org.kuali.rice.krad.uif.util.UrlInfo;
import org.kuali.rice.krad.uif.util.UifKeyValueLocation;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for controls that accept/display multiple values
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class MultiValueControlBase extends ControlBase implements MultiValueControl {
    private static final long serialVersionUID = -8691367056245775455L;

    private List<KeyValue> options;
    private List<KeyMessage> richOptions;
    private List<Component> inlineComponents;

    private boolean locationSelect = false;

    public MultiValueControlBase() {
        super();
    }

    /**
     * Process rich message content that may be in the options, by creating and initializing the richOptions
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performApplyModel(org.kuali.rice.krad.uif.view.View,
     *      Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (options != null && richOptions == null) {
            richOptions = new ArrayList<KeyMessage>();

            for (KeyValue option : options) {
                Message message = ComponentFactory.getMessage();
                view.assignComponentIds(message);
                message.setMessageText(option.getValue());
                message.setInlineComponents(inlineComponents);
                message.setGenerateSpan(false);

                view.getViewHelperService().performComponentInitialization(view, model, message);
                richOptions.add(new KeyMessage(option.getKey(), option.getValue(), message));
            }
        }
    }

    /**
     * Adds appropriate parent data to inputs internal to the controls that may be in rich content of options
     *
     * @see org.kuali.rice.krad.uif.component.Component#performFinalize(org.kuali.rice.krad.uif.view.View, Object,
     *      org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        ExpressionEvaluator expressionEvaluator = view.getViewHelperService().getExpressionEvaluator();

        if (options != null && !options.isEmpty()) {
            for (KeyValue option : options) {
                if (option instanceof UifKeyValueLocation) {
                    locationSelect = true;

                    UrlInfo url = ((UifKeyValueLocation) option).getLocation();

                    ExpressionUtils.populatePropertyExpressionsFromGraph(url, false);
                    expressionEvaluator.evaluateExpressionsOnConfigurable(view, url, view.getContext());
                }
            }
        }

        if (richOptions == null || richOptions.isEmpty()) {
            return;
        }

        //Messages included in options which have have rich message content need to be aware of their parent for
        //validation purposes
        for (KeyMessage richOption : richOptions) {
            List<Component> components = richOption.getMessage().getMessageComponentStructure();

            if (components != null && !components.isEmpty()) {
                for (Component c : components) {
                    if (c instanceof Container || c instanceof InputField) {
                        c.addDataAttribute(UifConstants.DataAttributes.PARENT, parent.getId());
                    }
                }
            }
        }

    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        if (richOptions != null) {
            for (KeyMessage richOption : richOptions) {
                components.add(richOption.getMessage());
            }
        }

        return components;
    }

    /**
     * @see MultiValueControl#getOptions()
     */
    @BeanTagAttribute(name = "options", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<KeyValue> getOptions() {
        return this.options;
    }

    /**
     * @see MultiValueControl#setOptions(java.util.List<org.kuali.rice.core.api.util.KeyValue>)
     */
    public void setOptions(List<KeyValue> options) {
        this.options = options;
    }

    /**
     * Gets the inlineComponents which represent components that can be referenced in an option's value
     * by index
     *
     * @return the components that can be used in rich values of options
     */
    @BeanTagAttribute(name = "inlineComponents", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Component> getInlineComponents() {
        return inlineComponents;
    }

    /**
     * Sets the inlineComponents which represent components that can be referenced in an option's value
     * by index
     *
     * @param inlineComponents
     */
    public void setInlineComponents(List<Component> inlineComponents) {
        this.inlineComponents = inlineComponents;
    }

    /**
     * @see MultiValueControl#getRichOptions()
     */
    public List<KeyMessage> getRichOptions() {
        return richOptions;
    }

    /**
     * Sets the richOptions.  This will always override/ignore options if set.
     *
     * <p><b>Messages MUST be defined</b> when using this setter, do not use this setter for most cases
     * as setting options through setOptions, with a richMessage value, is appropriate in MOST cases.  This
     * setter is only available for full control.</p>
     *
     * @param richOptions with their messages predefined
     */
    public void setRichOptions(List<KeyMessage> richOptions) {
        this.richOptions = richOptions;
    }

    /**
     * If true, this select represents a location select (navigate on select of option)
     *
     * @return true if this is a location select
     */
    public boolean isLocationSelect() {
        return locationSelect;
    }

    /**
     * Sets the location select (navigate on select of option)
     *
     * @param locationSelect
     */
    protected void setLocationSelect(boolean locationSelect) {
        this.locationSelect = locationSelect;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        MultiValueControlBase multiValueControlBaseCopy = (MultiValueControlBase) component;

        try {
            if (options != null) {
                List<KeyValue> optionsCopy = new ArrayList<KeyValue>();
                for (KeyValue option : options) {

                    KeyValue keyValue = null;
                    if (option != null) {
                        Class<? extends KeyValue> optionClass = option.getClass();
                        keyValue = optionClass.getDeclaredConstructor(String.class, String.class).newInstance(
                                option.getKey(), option.getValue());
                        if (keyValue instanceof UifKeyValueLocation) {
                            ((UifKeyValueLocation) keyValue).setLocation(
                                    (UrlInfo) ((UifKeyValueLocation) option).getLocation().copy());
                        }
                    }

                    optionsCopy.add(keyValue);
                }
                multiValueControlBaseCopy.setOptions(optionsCopy);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy options in MultiValueControlBase", e);
        }

        if (richOptions != null) {
            List<KeyMessage> richOptionsCopy = new ArrayList<KeyMessage>();
            for (KeyMessage richOption : richOptions) {
                KeyMessage keyMessage = new KeyMessage(richOption.getKey(), richOption.getValue(),
                        richOption.getMessage());
                richOptionsCopy.add(keyMessage);
            }
            multiValueControlBaseCopy.setRichOptions(richOptionsCopy);
        }

        if (inlineComponents != null) {
            List<Component> inlineComponentsCopy = new ArrayList<Component>();
            for (Component inlineComponent : inlineComponents) {
                inlineComponentsCopy.add((Component) inlineComponent.copy());
            }
            multiValueControlBaseCopy.setInlineComponents(inlineComponentsCopy);
        }

        multiValueControlBaseCopy.setLocationSelect(this.locationSelect);
    }
}
