/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.uif;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.control.RadioGroupControl;
import org.kuali.rice.krad.uif.element.Link;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.KeyMessage;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krad.test.KRADTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Test rich message generation and parsing functionality
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RichMessageTest extends KRADTestCase {

    /**
     * SimpleForm for testing purposes
     */
    public class SampleForm extends UifFormBase {
        public String field1;
        public String field2;
        public boolean renderField;
    }

    View view = new View();
    Message message;
    SampleForm model;

    /**
     * @see org.kuali.rice.test.BaselineTestCase#setUp()
     * @throws Exception
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        view.setViewHelperServiceClass(ViewHelperServiceImpl.class);
    }

    /**
     * Test html content generation in a message
     */
    @Test
    public void testHtmlContentGeneration() {
        model = new SampleForm();
        List<Component> components;

        //single tag
        generateAndSetMessage("[b]Message Content[/b]");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertFalse(((Message) components.get(0)).isGenerateSpan());
        Assert.assertEquals("<b>Message Content</b>", ((Message) components.get(0)).getMessageText());

        //nested tags
        generateAndSetMessage("[span][b]Message Content[/b][/span]");
        components = message.getMessageComponentStructure();
        Assert.assertEquals(1, components.size());
        Assert.assertEquals("<span><b>Message Content</b></span>", ((Message) components.get(0)).getMessageText());

        //multiple tags
        generateAndSetMessage("[div][b]Message Content[/b][/div][p]Message [i]Message[/i] Message[/p]");
        components = message.getMessageComponentStructure();
        Assert.assertEquals(1, components.size());
        Assert.assertEquals("<div><b>Message Content</b></div><p>Message&nbsp;<i>Message</i>&nbsp;Message</p>",
                ((Message) components.get(0)).getMessageText());

        //multiple tags with properties
        generateAndSetMessage(
                "[div class='cssClass'][b]Message Content[/b][/div][p]Message [a href='http://www.kuali.org']Message[/a] Message[/p]");
        components = message.getMessageComponentStructure();
        Assert.assertEquals(1, components.size());
        Assert.assertEquals(
                "<div class='cssClass'><b>Message Content</b></div><p>Message&nbsp;<a href='http://www.kuali.org'>Message</a>&nbsp;Message</p>",
                ((Message) components.get(0)).getMessageText());
    }

    /**
     * Test that the escape characters are properly replaced
     */
    @Test
    public void testEscapeCharacter() {
        model = new SampleForm();
        List<Component> components;

        //escape character
        generateAndSetMessage("\\[ \\] Message \\[content\\]\\[/content\\]");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        String messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals(KRADConstants.MessageParsing.LEFT_BRACKET
                + " "
                + KRADConstants.MessageParsing.RIGHT_BRACKET
                + " Message "
                + KRADConstants.MessageParsing.LEFT_BRACKET
                + "content"
                + KRADConstants.MessageParsing.RIGHT_BRACKET
                + ""
                + KRADConstants.MessageParsing.LEFT_BRACKET
                + "/content"
                + KRADConstants.MessageParsing.RIGHT_BRACKET, messageText);
        messageText = messageText.replace(KRADConstants.MessageParsing.LEFT_BRACKET, "[");
        messageText = messageText.replace(KRADConstants.MessageParsing.RIGHT_BRACKET, "]");
        Assert.assertEquals("[ ] Message [content][/content]", messageText);
    }

    /**
     * Test link tag generation in a message
     */
    @Test
    public void testLinkGeneration() {
        model = new SampleForm();
        List<Component> components;

        generateAndSetMessage("Link here [link='http://www.kuali.org']Link[/link] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        String messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals("Link here&nbsp;<a href='http://www.kuali.org' target='_blank'>Link</a>&nbsp;text", messageText);
    }

    /**
     * Test action link generation in a message
     */
    @Test
    public void testActionGeneration() {
        model = new SampleForm();
        List<Component> components;

        //methodToCall
        generateAndSetMessage("Action here [action=methodToCall]action[/action] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        String messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals(
                "Action here&nbsp;<a href=\"javascript:void(null)\" onclick=\"submitForm('methodToCall',null,true,true,null); return false;\">action</a>&nbsp;text",
                messageText);

        //Other options
        generateAndSetMessage("Action here [action=methodToCall,false]action[/action] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals(
                "Action here&nbsp;<a href=\"javascript:void(null)\" onclick=\"submitForm('methodToCall',null,false,true,null); return false;\">action</a>&nbsp;text",
                messageText);

        generateAndSetMessage("Action here [action=methodToCall,false,true]action[/action] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals(
                "Action here&nbsp;<a href=\"javascript:void(null)\" onclick=\"submitForm('methodToCall',null,false,true,null); return false;\">action</a>&nbsp;text",
                messageText);

        //ajax submit off
        generateAndSetMessage("Action here [action=methodToCall,true,false]action[/action] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals(
                "Action here&nbsp;<a href=\"javascript:void(null)\" onclick=\"submitForm('methodToCall',null,true,false,null); return false;\">action</a>&nbsp;text",
                messageText);

        //ajax callback defined
        generateAndSetMessage(
                "Action here [action=methodToCall,false,true,function(){console.log('success');}]action[/action] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals(
                "Action here&nbsp;<a href=\"javascript:void(null)\" onclick=\"submitForm('methodToCall',null,false,true,function(){console.log('success');}); return false;\">action</a>&nbsp;text",
                messageText);

        //data ajax
        generateAndSetMessage(
                "Action here [action=methodToCall data={something: 'value', something2: 'value2'}]action[/action] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals(
                "Action here&nbsp;<a href=\"javascript:void(null)\" onclick=\"submitForm('methodToCall',{something: 'value', something2: 'value2'},true,true,null); return false;\">action</a>&nbsp;text",
                messageText);

        //data non-ajax
        generateAndSetMessage(
                "Action here [action=methodToCall,true,false data={something: 'value', something2: 'value2'}]action[/action] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals(
                "Action here&nbsp;<a href=\"javascript:void(null)\" onclick=\"submitForm('methodToCall',{something: 'value', something2: 'value2'},true,false,null); return false;\">action</a>&nbsp;text",
                messageText);
    }

    /**
     * Test color tag generation in a message
     */
    @Test
    public void testColorGeneration() {
        model = new SampleForm();
        List<Component> components;

        generateAndSetMessage("color here [color='blue']Color[/color] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        String messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals("color here&nbsp;<span style='color: blue;'>Color</span>&nbsp;text", messageText);

        generateAndSetMessage("color here [color=#FFFFFF]Color[/color] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals("color here&nbsp;<span style='color: #FFFFFF;'>Color</span>&nbsp;text", messageText);
    }

    /**
     * Test css tag generation in a message
     */
    @Test
    public void testCssClassGeneration() {
        model = new SampleForm();
        List<Component> components;

        generateAndSetMessage("css here [css='c1']sample[/css] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        String messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals("css here&nbsp;<span class='c1'>sample</span>&nbsp;text", messageText);

        generateAndSetMessage("css here \\[[css='c1 c2']sample[/css]\\] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        messageText = ((Message) components.get(0)).getMessageText();
        Assert.assertEquals("css here " + KRADConstants.MessageParsing.LEFT_BRACKET +
                "<span class='c1 c2'>sample</span>" + KRADConstants.MessageParsing.RIGHT_BRACKET +
                " text", messageText);
    }

    /**
     * Test the ability to put components defined on the message inline by index number.
     */
    @Test
    public void testInlineComponentGeneration() {
        List<Component> components;

        //One inline component
        InputField inputField1 = ComponentFactory.getInputField();
        inputField1.setPropertyName("field1");
        List<Component> inline = new ArrayList<Component>();
        inline.add(inputField1);
        generateAndSetMessage("Message text [0] Message text", inline);
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(3, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertEquals("Message text&nbsp;", ((Message) components.get(0)).getMessageText());
        Assert.assertTrue(components.get(1) instanceof InputField);
        Assert.assertEquals("field1", ((InputField) components.get(1)).getPropertyName());
        Assert.assertTrue(components.get(2) instanceof Message);
        Assert.assertEquals("&nbsp;Message text", ((Message) components.get(2)).getMessageText());

        //Two inline components with html content
        inputField1 = ComponentFactory.getInputField();
        inputField1.setPropertyName("field1");
        InputField inputField2 = ComponentFactory.getInputField();
        inputField2.setPropertyName("field2");
        inline = new ArrayList<Component>();
        inline.add(inputField1);
        inline.add(inputField2);
        generateAndSetMessage("[p class='cssClass']Message text [0] Message [b]text [1] other[/b] text[/p]", inline);
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(5, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertEquals("<p class='cssClass'>Message text&nbsp;", ((Message) components.get(0)).getMessageText());
        Assert.assertTrue(components.get(1) instanceof InputField);
        Assert.assertEquals("field1", ((InputField) components.get(1)).getPropertyName());
        Assert.assertTrue(components.get(2) instanceof Message);
        Assert.assertEquals("&nbsp;Message&nbsp;<b>text&nbsp;", ((Message) components.get(2)).getMessageText());
        Assert.assertFalse(((Message) components.get(2)).isGenerateSpan());
        Assert.assertTrue(components.get(3) instanceof InputField);
        Assert.assertEquals("field2", ((InputField) components.get(3)).getPropertyName());
        Assert.assertTrue(components.get(4) instanceof Message);
        Assert.assertEquals("&nbsp;other</b>&nbsp;text</p>", ((Message) components.get(4)).getMessageText());

        //inline components with changed properties
        inputField1 = ComponentFactory.getInputField();
        inputField1.setPropertyName("field1");
        inputField2 = ComponentFactory.getInputField();
        inputField2.setPropertyName("field2");
        inline = new ArrayList<Component>();
        inline.add(inputField1);
        inline.add(inputField2);
        generateAndSetMessage(
                "[p class='cssClass']Message text [0 propertyName='field20'] Message [b]text [1 cssClasses='c1 c2' required=true] other[/b] text[/p]",
                inline);
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(5, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertEquals("<p class='cssClass'>Message text&nbsp;", ((Message) components.get(0)).getMessageText());
        Assert.assertTrue(components.get(1) instanceof InputField);
        Assert.assertEquals("field20", ((InputField) components.get(1)).getPropertyName());
        Assert.assertTrue(components.get(2) instanceof Message);
        Assert.assertEquals("&nbsp;Message&nbsp;<b>text&nbsp;", ((Message) components.get(2)).getMessageText());
        Assert.assertTrue(components.get(3) instanceof InputField);
        Assert.assertEquals("field2", ((InputField) components.get(3)).getPropertyName());
        Assert.assertTrue(((InputField) components.get(3)).getRequired());
        Assert.assertTrue(((InputField) components.get(3)).getCssClasses().contains("c1 c2"));
        Assert.assertTrue(components.get(4) instanceof Message);
        Assert.assertEquals("&nbsp;other</b>&nbsp;text</p>", ((Message) components.get(4)).getMessageText());
    }

    /**
     * Test the ability to put components inline by id
     */
    @Test
    public void testIdComponentGeneration() {
        List<Component> components;

        //One inline component and id component
        InputField inputField1 = ComponentFactory.getInputField();
        inputField1.setPropertyName("field1");
        List<Component> inline = new ArrayList<Component>();
        inline.add(inputField1);
        generateAndSetMessage("Message text [0] Message text [id=Uif-Link]", inline);
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(4, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertEquals("Message text&nbsp;", ((Message) components.get(0)).getMessageText());
        Assert.assertTrue(components.get(1) instanceof InputField);
        Assert.assertEquals("field1", ((InputField) components.get(1)).getPropertyName());
        Assert.assertTrue(components.get(2) instanceof Message);
        Assert.assertEquals("&nbsp;Message text&nbsp;", ((Message) components.get(2)).getMessageText());
        Assert.assertTrue(components.get(3) instanceof Link);

        //One inline component and id components
        inputField1 = ComponentFactory.getInputField();
        inputField1.setPropertyName("field1");
        inline = new ArrayList<Component>();
        inline.add(inputField1);
        generateAndSetMessage(
                "Message text [0] Message text [id=Uif-InputField propertyName=field2][id=Uif-InputField propertyName=field3]",
                inline);
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(5, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertEquals("Message text&nbsp;", ((Message) components.get(0)).getMessageText());
        Assert.assertTrue(components.get(1) instanceof InputField);
        Assert.assertEquals("field1", ((InputField) components.get(1)).getPropertyName());
        Assert.assertTrue(components.get(2) instanceof Message);
        Assert.assertEquals("&nbsp;Message text&nbsp;", ((Message) components.get(2)).getMessageText());
        Assert.assertTrue(components.get(3) instanceof InputField);
        Assert.assertEquals("field2", ((InputField) components.get(3)).getPropertyName());
        Assert.assertTrue(components.get(4) instanceof InputField);
        Assert.assertEquals("field3", ((InputField) components.get(4)).getPropertyName());

    }

    /**
     * Test a complex message which combines all the above tested functionality
     */
    @Test
    public void testComplexMessageGeneration() {
        List<Component> components;

        InputField inputField1 = ComponentFactory.getInputField();
        inputField1.setPropertyName("field1");
        List<Component> inline = new ArrayList<Component>();
        inline.add(inputField1);
        generateAndSetMessage("[p][css=class]Message [link=http://www.kuali.org]link[/link][/css] [0]"
                + " [action=methodToCall,false data={key: 'value'}]action text[/action]"
                + " [color=green]text [id=Uif-Link href='http://www.google.com' linkText=Linky]"
                + " [b]more text[/b][/color]\\[0\\][/p]", inline);
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(5, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertEquals(
                "<p><span class='class'>Message&nbsp;<a href='http://www.kuali.org' target='_blank'>link</a></span>&nbsp;",
                ((Message) components.get(0)).getMessageText());
        Assert.assertTrue(components.get(1) instanceof InputField);
        Assert.assertEquals("field1", ((InputField) components.get(1)).getPropertyName());
        Assert.assertTrue(components.get(2) instanceof Message);
        Assert.assertEquals("&nbsp;<a href=\"javascript:void(null)\" "
                + "onclick=\"submitForm('methodToCall',{key: 'value'},false,true,null); return false;\">"
                + "action text</a>&nbsp;<span style='color: green;'>text&nbsp;", ((Message) components.get(2)).getMessageText());
        Assert.assertTrue(components.get(3) instanceof Link);
        Assert.assertEquals("http://www.google.com", ((Link) components.get(3)).getHref());
        Assert.assertEquals("Linky", ((Link) components.get(3)).getLinkText());
        Assert.assertTrue(components.get(4) instanceof Message);
        Assert.assertEquals("&nbsp;<b>more text</b></span>"
                + KRADConstants.MessageParsing.LEFT_BRACKET
                + "0"
                + KRADConstants.MessageParsing.RIGHT_BRACKET
                + "</p>", ((Message) components.get(4)).getMessageText());

    }

    /**
     * Test rich message options available on MultiValueControls
     */
    @Test
    public void testRichMultiValueOptions() {

        List<KeyValue> options = new ArrayList<KeyValue>();
        options.add(new ConcreteKeyValue("1", "[color=green]Option [b]1[/b][/color]"));
        options.add(new ConcreteKeyValue("2", "Option 2 [link='http://www.kuali.org']link[/link]"));
        options.add(new ConcreteKeyValue("3", "Other: [id=Uif-InputField propertyName=field1]"));
        options.add(new ConcreteKeyValue("4", "Other 2: [0]"));
        RadioGroupControl radioGroupControl = ComponentFactory.getRadioGroupControl();

        List<Component> inline = new ArrayList<Component>();
        InputField field2 = ComponentFactory.getInputField();
        field2.setPropertyName("field2");
        inline.add(field2);
        radioGroupControl.setInlineComponents(inline);

        radioGroupControl.setOptions(options);
        performSimulatedLifecycle(radioGroupControl);
        for (Component component : radioGroupControl.getComponentsForLifecycle()) {
            performSimulatedLifecycle(component);
        }

        List<KeyMessage> richOptions = radioGroupControl.getRichOptions();
        Assert.assertEquals("<span style='color: green;'>Option&nbsp;<b>1</b></span>", ((Message) (richOptions.get(0)
                .getMessage().getMessageComponentStructure().get(0))).getMessageText());
        Assert.assertEquals("Option 2&nbsp;<a href='http://www.kuali.org' target='_blank'>link</a>",
                ((Message) (richOptions.get(1).getMessage().getMessageComponentStructure().get(0))).getMessageText());
        Assert.assertEquals("Other:&nbsp;", ((Message) (richOptions.get(2).getMessage().getMessageComponentStructure().get(
                0))).getMessageText());
        Assert.assertEquals("field1", ((InputField) (richOptions.get(2).getMessage().getMessageComponentStructure().get(
                1))).getPropertyName());
        Assert.assertEquals("Other 2:&nbsp;", ((Message) (richOptions.get(3).getMessage().getMessageComponentStructure().get(
                0))).getMessageText());
        Assert.assertEquals("field2", ((InputField) (richOptions.get(3).getMessage().getMessageComponentStructure().get(
                1))).getPropertyName());
    }

    /**
     * Test SPEL richMessages
     */
    @Test
    public void testSPELRichMessages() {
        /*  TODO Cannot figure out how to simulate correctly
        model = new SampleForm();
        view.setFormClass(SampleForm.class);
        List<Component> components;
        model.field1 = "value";
        model.field2 = "[link='http://www.kuali.org']value2[/link]";
        model.renderField = false;

        //simple
        generateAndSetMessage("Message @{field1} text");
        components = message.getMessageComponentStructure();
        Assert.assertNull(components);
        Assert.assertTrue(message.isGenerateSpan());
        Assert.assertEquals("Message value text", message.getMessageText());

        //rich message wrapping
        generateAndSetMessage("Message [b]@{field1}[/b] text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertFalse(((Message) components.get(0)).isGenerateSpan());
        Assert.assertEquals("Message <b>value</b> text", ((Message) components.get(0)).getMessageText());

        //spel value contains rich content
        generateAndSetMessage("Message @{field2} text");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(1, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertFalse(((Message) components.get(0)).isGenerateSpan());
        Assert.assertEquals("Message <a href='http://www.kuali.org' target='_blank'>value2</a> text",
                ((Message) components.get(0)).getMessageText());

        //spel value setting richMessage component value
        generateAndSetMessage("Message text [id=Uif-InputField render=@{renderField} propertyName=@{field1}]");
        components = message.getMessageComponentStructure();
        Assert.assertNotNull(components);
        Assert.assertEquals(2, components.size());
        Assert.assertTrue(components.get(0) instanceof Message);
        Assert.assertFalse(((Message) components.get(0)).isGenerateSpan());
        Assert.assertEquals("Message text ", ((Message) components.get(0)).getMessageText());
        Assert.assertTrue(components.get(1) instanceof InputField);
        Assert.assertFalse(((InputField) components.get(1)).isRender());
        Assert.assertEquals("value", ((InputField) components.get(1)).getPropertyName());*/
    }

    /**
     * Test basic message with no rich functionality
     */
    @Test
    public void testBasicMessage() {
        model = new SampleForm();
        List<Component> components;

        //single tag
        generateAndSetMessage("Message Content");
        components = message.getMessageComponentStructure();
        Assert.assertNull(components);
        Assert.assertTrue(message.isGenerateSpan());
        Assert.assertEquals("Message Content", message.getMessageText());
    }

    /**
     * Peform a simulated lifecycle on the component passed in
     *
     * @param component
     */
    private void performSimulatedLifecycle(Component component) {
        component.performInitialization(view, model);
        component.performApplyModel(view, model, view);
        component.performFinalize(view, model, view);
    }

    /**
     * Generate and setup a message to be used in testing
     *
     * @param messageText
     */
    private void generateAndSetMessage(String messageText) {
        message = new Message();
        message.setMessageText(messageText);
        performSimulatedLifecycle(message);
    }

    /**
     * Generate and setup a message to be used in testing which contains inline components
     *
     * @param messageText
     * @param inlineComponents
     */
    private void generateAndSetMessage(String messageText, List<Component> inlineComponents) {
        message = new Message();
        message.setInlineComponents(inlineComponents);
        message.setMessageText(messageText);
        performSimulatedLifecycle(message);
    }
}
