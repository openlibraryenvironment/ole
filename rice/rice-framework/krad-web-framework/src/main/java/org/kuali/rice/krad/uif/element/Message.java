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
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.util.MessageStructureUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.List;

/**
 * Encapsulates a text message to be displayed
 *
 * <p>
 * The <code>Message</code> is used to display static text in the user
 * interface
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "message-bean", parent = "Uif-Message"),
        @BeanTag(name = "instructionalMessage-bean", parent = "Uif-InstructionalMessage"),
        @BeanTag(name = "constraintMessage-bean", parent = "Uif-ConstraintMessage"),
        @BeanTag(name = "requiredMessage-bean", parent = "Uif-RequiredMessage"),
        @BeanTag(name = "requiredInstructionsMessage-bean", parent = "Uif-RequiredInstructionsMessage"),
        @BeanTag(name = "stateBased-requiredInstructionsMessage-bean",
                parent = "Uif-StateBased-RequiredInstructionsMessage"),
        @BeanTag(name = "dialogPrompt-bean", parent = "Uif-DialogPrompt"),
        @BeanTag(name = "imageCutineMessage-bean", parent = "Uif-ImageCutineMessage")})
public class Message extends ContentElementBase {
    private static final long serialVersionUID = 4090058533452450395L;

    private String messageText;
    private boolean generateSpan;

    private List<Component> inlineComponents;
    private List<Component> messageComponentStructure;

    private boolean parseComponents;

    public Message() {
        super();

        generateSpan = true;
        parseComponents = true;
    }

    /**
     * Message perfom apply model parses message text for rich text functionality if the messageText contains
     * [ or ] special characters
     *
     * @see Component#performApplyModel(org.kuali.rice.krad.uif.view.View, Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        //if messageText contains the special characters [] then parse and fill in the messageComponentStructure
        //but if messageComponentStructure has already been set it overrides messageText by default
        if (messageText != null && messageText.contains(KRADConstants.MessageParsing.LEFT_TOKEN) &&
                messageText.contains(KRADConstants.MessageParsing.RIGHT_TOKEN) &&
                (messageComponentStructure == null || messageComponentStructure.isEmpty())) {

            messageComponentStructure = MessageStructureUtils.parseMessage(this.getId(), this.getMessageText(),
                    this.getInlineComponents(), view, parseComponents);

            if (messageComponentStructure != null) {
                for (Component component : messageComponentStructure) {
                    view.getViewHelperService().performComponentInitialization(view, model, component);
                }
            }
        }
    }

    /**
     * @see Component#performFinalize(org.kuali.rice.krad.uif.view.View, Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        // message needs to be aware of its own parent because it now contains content that can have validation
        this.addDataAttribute(UifConstants.DataAttributes.PARENT, parent.getId());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        if (messageComponentStructure != null) {
            for (Component component : messageComponentStructure) {
                components.add(component);
            }
        }

        return components;
    }

    /**
     * Override to render only if the message text has been given or there is a conditional expression on the
     * message text
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#isRender()
     */
    @Override
    public boolean isRender() {
        boolean render = super.isRender();

        if (render) {
            render = getPropertyExpressions().containsKey("messageText") || (StringUtils.isNotBlank(messageText)
                    && !StringUtils.equals(messageText, "&nbsp;"));
        }

        return render;
    }

    /**
     * Text that makes up the message that will be displayed.
     *
     * <p>If special characters [] are detected the message inserts special content at that location.
     * The types of features supported are (note that &lt;&gt; are not part of the content below,
     * they specify placeholders):
     * <ul>
     * <li>[id=&lt;component id&gt;] - insert component with id specified at that location in the message</li>
     * <li>[n] - insert component at index n from the inlineComponent list</li>
     * <li>[&lt;html tag&gt;][/&lt;html tag&gt;] - insert html content directly into the message content at that
     * location,
     * without the need to escape the &lt;&gt; characters in xml</li>
     * <li>[color=&lt;html color code/name&gt;][/color] - wrap content in color tags to make text that color
     * in the message</li>
     * <li>[css=&lt;css classes&gt;][/css] - apply css classes specified to the wrapped content - same as wrapping
     * the content in span with class property set</li>
     * </ul>
     * If the [] characters are needed in message text, they need to be declared with an escape character: \\[ \\]
     * </p>
     *
     * @return message text
     */
    @BeanTagAttribute(name = "messageText")
    public String getMessageText() {
        return this.messageText;
    }

    /**
     * Setter for the message text
     *
     * @param messageText
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * If true, generate the span around this message (default).  When false, skip span generation for this
     * message - this has the additional effect the css classes/style classes will be lost for this message.
     *
     * @return true if generating a wrapping span, false otherwise
     */
    @BeanTagAttribute(name = "generateSpan")
    public boolean isGenerateSpan() {
        return generateSpan;
    }

    /**
     * Sets the generate span flag
     *
     * @param generateSpan
     */
    public void setGenerateSpan(boolean generateSpan) {
        this.generateSpan = generateSpan;
    }

    /**
     * The message component structure is a list of components which represent the components that make up a message
     * when using rich message functionality.
     *
     * <p>The structure represents the parsed messageText when not set. Normally this structure is setup by the Message
     * class and <b>SHOULD NOT BE SET</b> in xml, unless full control over the structure is needed.  </p>
     *
     * @return list of components which represent the message structure
     */
    public List<Component> getMessageComponentStructure() {
        return messageComponentStructure;
    }

    /**
     * Set the message component structure.  This will override/ignore messageText when set. Normally
     * this <b>SHOULD NOT BE SET</b> by the xml configuration.
     *
     * @param messageComponentStructure list of components which represent the message structure
     */
    public void setMessageComponentStructure(List<Component> messageComponentStructure) {
        this.messageComponentStructure = messageComponentStructure;
    }

    /**
     * The inlineComponents are a list of components in order by index.
     *
     * <p>inlineComponents is only used when the message is using rich message functionality.  A message
     * with [0] will reference component at index 0 of this list and insert it at that place in the message,
     * and likewise [1] will reference item 1, etc.  If the index referenced is out of bounds (or list doesnt exist),
     * an error will be thrown during message parse.</p>
     *
     * @return the inlineComponents to be filled in at indexes referenced by [n] in the message
     */
    @BeanTagAttribute(name = "inlineComponents", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Component> getInlineComponents() {
        return inlineComponents;
    }

    /**
     * Set the inlineComponents to be filled in at indexes referenced by [n] in the message
     *
     * @param inlineComponents the inlineComponents to be filled in at indexes referenced by [n] in the message
     */
    public void setInlineComponents(List<Component> inlineComponents) {
        this.inlineComponents = inlineComponents;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean(this);

        // Checks that text is set
        if (getMessageText() == null) {
            if (Validator.checkExpressions(this, "messageText")) {
                String currentValues[] = {"messageText  =" + getMessageText()};
                tracer.createWarning("MessageText should be set", currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    /**
     * Indicates if the inline components must be parsed for rich messages
     *
     * @return boolean
     */
    @BeanTagAttribute(name = "parseComponents")
    public boolean isParseComponents() {
        return parseComponents;
    }

    /**
     * Sets the parse components flag to indicate if inline components must be parsed for rich messages
     *
     * @param parseComponents
     */
    public void setParseComponents(boolean parseComponents) {
        this.parseComponents = parseComponents;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Message messageCopy = (Message) component;
        messageCopy.setGenerateSpan(this.generateSpan);

        if (this.inlineComponents != null) {
            List<Component> inlineComponents = Lists.newArrayListWithExpectedSize(this.inlineComponents.size());

            for (Component inlineComponent : this.inlineComponents) {
                inlineComponents.add((Component)inlineComponent.copy());
            }

            messageCopy.setInlineComponents(inlineComponents);
        }

        if (this.messageComponentStructure != null) {
            List<Component> messageComponentStructure = Lists.newArrayListWithExpectedSize(
                    this.messageComponentStructure.size());

            for (Component messageComponentStructureItem : this.messageComponentStructure) {
                messageComponentStructure.add((Component)messageComponentStructureItem.copy());
            }

            messageCopy.setMessageComponentStructure(messageComponentStructure);
        }

        messageCopy.setMessageText(this.messageText);
        messageCopy.setParseComponents(this.parseComponents);
    }
}
