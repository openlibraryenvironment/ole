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
package org.kuali.rice.krad.uif.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Rich message structure utilities for parsing message content and converting it to components/content
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageStructureUtils {

    /**
     * Translate a message with special hooks described in MessageStructureUtils.parseMessage.  However, tags which
     * reference components will not be allowed/translated - only tags which can translate to string content will
     * be included for this translation.
     *
     * @param messageText messageText with only String translateable tags included (no id or component index tags)
     * @return html translation of rich messageText passed in
     * @see MessageStructureUtils#parseMessage
     */
    public static String translateStringMessage(String messageText) {
        if (!StringUtils.isEmpty(messageText)) {
            List<Component> components = MessageStructureUtils.parseMessage(null, messageText, null, null, false);

            if (!components.isEmpty()) {
                Component message = components.get(0);

                if (message instanceof Message) {
                    messageText = ((Message) message).getMessageText();
                }
            }
        }

        return messageText;
    }

    /**
     * Parses the message text passed in and returns the resulting rich message component structure.
     *
     * <p>If special characters [] are detected the message is split at that location.  The types of features supported
     * by the parse are (note that &lt;&gt; are not part of the content, they specify placeholders):
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
     * <li>[link=&lt;href src&gt;][/link] - an easier way to create an anchor that will open in a new page to the
     * href specified after =</li>
     * <li>[action=&lt;href src&gt;][/action] - create an action link inline without having to specify a component by
     * id or index.  The options for this are as follows and MUST be in a comma seperated list in the order specified
     * (specify 1-4 always in this order):
     * <ul>
     * <li>methodToCall(String)</li>
     * <li>validateClientSide(boolean) - true if not set</li>
     * <li>ajaxSubmit(boolean) - true if not set</li>
     * <li>successCallback(js function or function declaration) - this only works when ajaxSubmit is true</li>
     * </ul>
     * The tag would look something like this [action=methodToCall]Action[/action] in most common cases.  And in more
     * complex cases [action=methodToCall,true,true,functionName]Action[/action].  <p>In addition to these settings,
     * you can also specify data to send to the server in this fashion (space is required between settings and data):
     * </p>
     * [action=&lt;action settings&gt; data={key1: 'value 1', key2: value2}]
     * </li>
     * </ul>
     * If the [] characters are needed in message text, they need to be declared with an escape character: \\[ \\]
     * </p>
     *
     * @param messageId id of the message
     * @param messageText message text to be parsed
     * @param componentList the inlineComponent list
     * @param view the current view
     * @return list of components representing the parsed message structure
     */
    public static List<Component> parseMessage(String messageId, String messageText, List<Component> componentList,
            View view, boolean parseComponents) {
        messageText = messageText.replace("\\" + KRADConstants.MessageParsing.LEFT_TOKEN,
                KRADConstants.MessageParsing.LEFT_BRACKET);
        messageText = messageText.replace("\\" + KRADConstants.MessageParsing.RIGHT_TOKEN,
                KRADConstants.MessageParsing.RIGHT_BRACKET);
        messageText = messageText.replace(KRADConstants.MessageParsing.RIGHT_TOKEN,
                KRADConstants.MessageParsing.RIGHT_TOKEN_PLACEHOLDER);
        String[] messagePieces = messageText.split("[\\" + KRADConstants.MessageParsing.LEFT_TOKEN +
                "|\\" + KRADConstants.MessageParsing.RIGHT_TOKEN + "]");

        List<Component> messageComponentStructure = new ArrayList<Component>();

        //current message object to concatenate to after it is generated to prevent whitespace issues and
        //creation of multiple unneeded objects
        Message currentMessageComponent = null;

        for (String messagePiece : messagePieces) {
            if (messagePiece.endsWith(KRADConstants.MessageParsing.RIGHT_TOKEN_MARKER)) {
                messagePiece = StringUtils.removeEnd(messagePiece, KRADConstants.MessageParsing.RIGHT_TOKEN_MARKER);

                if (StringUtils.startsWithIgnoreCase(messagePiece, KRADConstants.MessageParsing.COMPONENT_BY_ID + "=")
                        && parseComponents) {
                    //Component by Id
                    currentMessageComponent = processIdComponentContent(messagePiece, messageComponentStructure,
                            currentMessageComponent, view);
                } else if (messagePiece.matches("^[0-9]+( .+=.+)*$") && parseComponents) {
                    //Component by index of inlineComponents
                    currentMessageComponent = processIndexComponentContent(messagePiece, componentList,
                            messageComponentStructure, currentMessageComponent, view, messageId);
                } else if (StringUtils.startsWithIgnoreCase(messagePiece, KRADConstants.MessageParsing.COLOR + "=")
                        || StringUtils.startsWithIgnoreCase(messagePiece, "/" + KRADConstants.MessageParsing.COLOR)) {
                    //Color span
                    currentMessageComponent = processColorContent(messagePiece, currentMessageComponent, view);
                } else if (StringUtils.startsWithIgnoreCase(messagePiece,
                        KRADConstants.MessageParsing.CSS_CLASSES + "=") || StringUtils.startsWithIgnoreCase(
                        messagePiece, "/" + KRADConstants.MessageParsing.CSS_CLASSES)) {
                    //css class span
                    currentMessageComponent = processCssClassContent(messagePiece, currentMessageComponent, view);
                } else if (StringUtils.startsWithIgnoreCase(messagePiece, KRADConstants.MessageParsing.LINK + "=")
                        || StringUtils.startsWithIgnoreCase(messagePiece, "/" + KRADConstants.MessageParsing.LINK)) {
                    //link (a tag)
                    currentMessageComponent = processLinkContent(messagePiece, currentMessageComponent, view);
                } else if (StringUtils.startsWithIgnoreCase(messagePiece,
                        KRADConstants.MessageParsing.ACTION_LINK + "=") || StringUtils.startsWithIgnoreCase(
                        messagePiece, "/" + KRADConstants.MessageParsing.ACTION_LINK)) {
                    //action link (a tag)
                    currentMessageComponent = processActionLinkContent(messagePiece, currentMessageComponent, view);
                } else if (messagePiece.equals("")) {
                    //do nothing    
                } else {
                    //raw html content
                    currentMessageComponent = processHtmlContent(messagePiece, currentMessageComponent, view);
                }
            } else {
                //raw string
                messagePiece = addBlanks(messagePiece);
                currentMessageComponent = concatenateStringMessageContent(currentMessageComponent, messagePiece, view);
            }
        }

        if (currentMessageComponent != null && StringUtils.isNotEmpty(currentMessageComponent.getMessageText())) {
            messageComponentStructure.add(currentMessageComponent);
            currentMessageComponent = null;
        }

        return messageComponentStructure;
    }

    /**
     * Concatenates string content onto the message passed in and passes it back.  If the message is null, creates
     * a new message object with the string content and passes that back.
     *
     * @param currentMessageComponent Message object
     * @param messagePiece string content to be concatenated
     * @param view the current view
     * @return resulting concatenated Message
     */
    private static Message concatenateStringMessageContent(Message currentMessageComponent, String messagePiece,
            View view) {
        //messagePiece = messagePiece.indexOf(" ");
/*        if (messagePiece.startsWith(" ")){
            messagePiece.replaceFirst(" ", "&nbsp;");
        }

        if (messagePiece.endsWith(" ")){

        }*/

        if (currentMessageComponent == null) {
            currentMessageComponent = ComponentFactory.getMessage();

            if (view != null) {
                view.assignComponentIds(currentMessageComponent);
            }

            currentMessageComponent.setMessageText(messagePiece);
            currentMessageComponent.setGenerateSpan(false);
        } else {
            currentMessageComponent.setMessageText(currentMessageComponent.getMessageText() + messagePiece);
        }

        return currentMessageComponent;
    }

    /**
     * Process the additional properties beyond index 0 of the tag (that was split into parts).
     *
     * <p>This will evaluate and set each of properties on the component passed in.  This only allows
     * setting of properties that can easily be converted to/from/are String type by Spring.</p>
     *
     * @param component component to have its properties set
     * @param tagParts the tag split into parts, index 0 is ignored
     * @return component with its properties set found in the tag's parts
     */
    private static Component processAdditionalProperties(Component component, String[] tagParts) {
        String componentString = tagParts[0];
        tagParts = (String[]) ArrayUtils.remove(tagParts, 0);

        for (String part : tagParts) {
            String[] propertyValue = part.split("=");

            if (propertyValue.length == 2) {
                String path = propertyValue[0];
                String value = propertyValue[1].trim();
                value = StringUtils.removeStart(value, "'");
                value = StringUtils.removeEnd(value, "'");
                ObjectPropertyUtils.setPropertyValue(component, path, value);
            } else {
                throw new RuntimeException(
                        "Invalid Message structure for component defined as " + componentString + " around " + part);
            }
        }

        return component;
    }

    /**
     * Inserts &amp;nbsp; into the string passed in, if spaces exist at the beginning and/or end,
     * so spacing is not lost in html translation.
     *
     * @param text string to insert  &amp;nbsp;
     * @return String with  &amp;nbsp; inserted, if applicable
     */
    public static String addBlanks(String text) {
        if (StringUtils.startsWithIgnoreCase(text, " ")) {
            text = "&nbsp;" + StringUtils.removeStart(text, " ");
        }

        if (text.endsWith(" ")) {
            text = StringUtils.removeEnd(text, " ") + "&nbsp;";
        }

        return text;
    }

    /**
     * Process a piece of the message that has id content to get a component by id and insert it in the structure
     *
     * @param messagePiece String piece with component by id content
     * @param messageComponentStructure the structure of the message being built
     * @param currentMessageComponent the state of the current text based message being built
     * @param view current View
     * @return null if currentMessageComponent had a value (it is now added to the messageComponentStructure passed in)
     */
    private static Message processIdComponentContent(String messagePiece, List<Component> messageComponentStructure,
            Message currentMessageComponent, View view) {
        //splits around spaces not included in single quotes
        String[] parts = messagePiece.trim().trim().split("([ ]+(?=([^']*'[^']*')*[^']*$))");
        messagePiece = parts[0];

        //if there is a currentMessageComponent add it to the structure and reset it to null
        //because component content is now interrupting the string content
        if (currentMessageComponent != null && StringUtils.isNotEmpty(currentMessageComponent.getMessageText())) {
            messageComponentStructure.add(currentMessageComponent);
            currentMessageComponent = null;
        }

        //match component by id from the view
        messagePiece = StringUtils.remove(messagePiece, "'");
        messagePiece = StringUtils.remove(messagePiece, "\"");
        Component component = ComponentFactory.getNewComponentInstance(StringUtils.removeStart(messagePiece,
                KRADConstants.MessageParsing.COMPONENT_BY_ID + "="));

        if (component != null) {
            view.assignComponentIds(component);
            component.addStyleClass(KRADConstants.MessageParsing.INLINE_COMP_CLASS);

            if (parts.length > 1) {
                component = processAdditionalProperties(component, parts);
            }
            messageComponentStructure.add(component);
        }

        return currentMessageComponent;
    }

    /**
     * Process a piece of the message that has index content to get a component by index in the componentList passed in
     * and insert it in the structure
     *
     * @param messagePiece String piece with component by index content
     * @param componentList list that contains the component referenced by index
     * @param messageComponentStructure the structure of the message being built
     * @param currentMessageComponent the state of the current text based message being built
     * @param view current View
     * @param messageId id of the message being parsed (for exception notification)
     * @return null if currentMessageComponent had a value (it is now added to the messageComponentStructure passed in)
     */
    private static Message processIndexComponentContent(String messagePiece, List<Component> componentList,
            List<Component> messageComponentStructure, Message currentMessageComponent, View view, String messageId) {
        //splits around spaces not included in single quotes
        String[] parts = messagePiece.trim().trim().split("([ ]+(?=([^']*'[^']*')*[^']*$))");
        messagePiece = parts[0];

        //if there is a currentMessageComponent add it to the structure and reset it to null
        //because component content is now interrupting the string content
        if (currentMessageComponent != null && StringUtils.isNotEmpty(currentMessageComponent.getMessageText())) {
            messageComponentStructure.add(currentMessageComponent);
            currentMessageComponent = null;
        }

        //match component by index from the componentList passed in
        int cIndex = Integer.parseInt(messagePiece);

        if (componentList != null && cIndex < componentList.size() && !componentList.isEmpty()) {
            Component component = componentList.get(cIndex);

            if (component != null) {
                if (component.getId() == null) {
                    view.assignComponentIds(component);
                }

                if (parts.length > 1) {
                    component = processAdditionalProperties(component, parts);
                }

                component.addStyleClass(KRADConstants.MessageParsing.INLINE_COMP_CLASS);
                messageComponentStructure.add(component);
            }
        } else {
            throw new RuntimeException("Component with index " + cIndex +
                    " does not exist in inlineComponents of the message component with id " + messageId);
        }

        return currentMessageComponent;
    }

    /**
     * Process a piece of the message that has color content by creating a span with that color style set
     *
     * @param messagePiece String piece with color content
     * @param currentMessageComponent the state of the current text based message being built
     * @param view current View
     * @return currentMessageComponent with the new textual content generated by this method appended to its
     *         messageText
     */
    private static Message processColorContent(String messagePiece, Message currentMessageComponent, View view) {
        if (!StringUtils.startsWithIgnoreCase(messagePiece, "/")) {
            messagePiece = StringUtils.remove(messagePiece, "'");
            messagePiece = StringUtils.remove(messagePiece, "\"");
            messagePiece = "<span style='color: " + StringUtils.removeStart(messagePiece,
                    KRADConstants.MessageParsing.COLOR + "=") + ";'>";
        } else {
            messagePiece = "</span>";
        }

        return concatenateStringMessageContent(currentMessageComponent, messagePiece, view);
    }

    /**
     * Process a piece of the message that has css content by creating a span with those css classes set
     *
     * @param messagePiece String piece with css class content
     * @param currentMessageComponent the state of the current text based message being built
     * @param view current View
     * @return currentMessageComponent with the new textual content generated by this method appended to its
     *         messageText
     */
    private static Message processCssClassContent(String messagePiece, Message currentMessageComponent, View view) {
        if (!StringUtils.startsWithIgnoreCase(messagePiece, "/")) {
            messagePiece = StringUtils.remove(messagePiece, "'");
            messagePiece = StringUtils.remove(messagePiece, "\"");
            messagePiece = "<span class='" + StringUtils.removeStart(messagePiece,
                    KRADConstants.MessageParsing.CSS_CLASSES + "=") + "'>";
        } else {
            messagePiece = "</span>";
        }

        return concatenateStringMessageContent(currentMessageComponent, messagePiece, view);
    }

    /**
     * Process a piece of the message that has link content by creating an anchor (a tag) with the href set
     *
     * @param messagePiece String piece with link content
     * @param currentMessageComponent the state of the current text based message being built
     * @param view current View
     * @return currentMessageComponent with the new textual content generated by this method appended to its
     *         messageText
     */
    private static Message processLinkContent(String messagePiece, Message currentMessageComponent, View view) {
        if (!StringUtils.startsWithIgnoreCase(messagePiece, "/")) {
            //clean up href
            messagePiece = StringUtils.removeStart(messagePiece, KRADConstants.MessageParsing.LINK + "=");
            messagePiece = StringUtils.removeStart(messagePiece, "'");
            messagePiece = StringUtils.removeEnd(messagePiece, "'");
            messagePiece = StringUtils.removeStart(messagePiece, "\"");
            messagePiece = StringUtils.removeEnd(messagePiece, "\"");

            messagePiece = "<a href='" + messagePiece + "' target='_blank'>";
        } else {
            messagePiece = "</a>";
        }

        return concatenateStringMessageContent(currentMessageComponent, messagePiece, view);
    }

    /**
     * Process a piece of the message that has action link content by creating an anchor (a tag) with the onClick set
     * to perform either ajaxSubmit or submit to the controller with a methodToCall
     *
     * @param messagePiece String piece with action link content
     * @param currentMessageComponent the state of the current text based message being built
     * @param view current View
     * @return currentMessageComponent with the new textual content generated by this method appended to its
     *         messageText
     */
    private static Message processActionLinkContent(String messagePiece, Message currentMessageComponent, View view) {
        if (!StringUtils.startsWithIgnoreCase(messagePiece, "/")) {
            messagePiece = StringUtils.removeStart(messagePiece, KRADConstants.MessageParsing.ACTION_LINK + "=");
            String[] splitData = messagePiece.split(KRADConstants.MessageParsing.ACTION_DATA + "=");

            String[] params = splitData[0].trim().split("([,]+(?=([^']*'[^']*')*[^']*$))");
            String methodToCall = ((params.length >= 1) ? params[0] : "");
            String validate = ((params.length >= 2) ? params[1] : "true");
            String ajaxSubmit = ((params.length >= 3) ? params[2] : "true");
            String successCallback = ((params.length >= 4) ? params[3] : "null");

            String submitData = "null";

            if (splitData.length > 1) {
                submitData = splitData[1].trim();
            }

            methodToCall = StringUtils.remove(methodToCall, "'");
            methodToCall = StringUtils.remove(methodToCall, "\"");

            messagePiece = "<a href=\"javascript:void(null)\" onclick=\"submitForm(" +
                    "'" +
                    methodToCall +
                    "'," +
                    submitData +
                    "," +
                    validate +
                    "," +
                    ajaxSubmit +
                    "," +
                    successCallback +
                    "); return false;\">";
        } else {
            messagePiece = "</a>";
        }

        return concatenateStringMessageContent(currentMessageComponent, messagePiece, view);
    }

    /**
     * Process a piece of the message that is assumed to have a valid html tag
     *
     * @param messagePiece String piece with html tag content
     * @param currentMessageComponent the state of the current text based message being built
     * @param view current View
     * @return currentMessageComponent with the new textual content generated by this method appended to its
     *         messageText
     */
    private static Message processHtmlContent(String messagePiece, Message currentMessageComponent, View view) {
        //raw html
        messagePiece = messagePiece.trim();

        if (StringUtils.startsWithAny(messagePiece, KRADConstants.MessageParsing.UNALLOWED_HTML) || StringUtils
                .endsWithAny(messagePiece, KRADConstants.MessageParsing.UNALLOWED_HTML)) {
            throw new RuntimeException("The following html is not allowed in Messages: " + Arrays.toString(
                    KRADConstants.MessageParsing.UNALLOWED_HTML));
        }

        messagePiece = "<" + messagePiece + ">";

        return concatenateStringMessageContent(currentMessageComponent, messagePiece, view);
    }
}
