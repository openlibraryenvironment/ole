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

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *  ScriptUtilsTest tests {@link ScriptUtils}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ScriptUtilsTest {

    @Test
    /**
     * tests {@link ScriptUtils#escapeHtml(String)}
     */
    public void testEscapeHtml() throws Exception {
        assertEquals("wasn&apos;t", ScriptUtils.escapeHtml("wasn't"));
        assertEquals("\\u0022wasn&apos;t\\u0022", ScriptUtils.escapeHtml("\"wasn't\""));
    }

    @Test
    /**
     * tests {@link ScriptUtils#escapeHtml(java.util.List)}
     */
    public void testEscapeHtmlStringList() {
        String[] escaped = {"wasn&apos;t", "\\u0022wasn&apos;t\\u0022"};
        String[] unEscaped = {"wasn't", "\"wasn't\""};
        assertEquals(Arrays.asList(escaped), ScriptUtils.escapeHtml(Arrays.asList(unEscaped)));

        List<String> nullList = null;
        assertNull(ScriptUtils.escapeHtml(nullList));

        List<String> emptyList = Collections.emptyList();
        assertEquals(emptyList, ScriptUtils.escapeHtml(emptyList));
    }

    @Test
    /**
     * tests {@link ScriptUtils#convertToJsValue(String)} for a function value
     */
    public void testConvertToJSValue_function() {
        // test for white space
        String jsFunction = "\n function () {alert('1 + 1 ' is 1 + 1);} \n\n";
        assertEquals("function was not converted to js value as expected", jsFunction, ScriptUtils.convertToJsValue(jsFunction));
    }

    @Test
    /**
     * tests {@link ScriptUtils#convertToJsValue(String)} for numeric values
     */
    public void testConvertToJSValue_numeric() {
        assertEquals("number was not converted to js value as expected", " -1 ", ScriptUtils.convertToJsValue(" -1 "));
        assertEquals("number was not converted to js value as expected", "1.01 ", ScriptUtils.convertToJsValue("1.01 "));
        assertEquals("string was not converted to js value as expected", "'1.o1 '", ScriptUtils.convertToJsValue("1.o1 "));
    }

    @Test
    /**
     * tests {@link ScriptUtils#convertToJsValue(String)} for  maps and arrays
     */
    public void testConvertToJSValue_mapAndArray() {
        assertEquals("array was not converted to js value as expected", " [-1, 4, 5] ", ScriptUtils.convertToJsValue(" [-1, 4, 5] "));
        String jsMap = " {'a':1, 'b':2} \n";
        assertEquals("map was not converted to js value as expected", jsMap, ScriptUtils.convertToJsValue(jsMap));
    }

    /**
     * Test building of event script matches the expected JavaScript
     */
    @Test
    public void testBuildEventHandlerScript() {
        String onClickScript = "alert('A click happened');";
        String onClickHandler = ScriptUtils.buildEventHandlerScript("u09", "click", onClickScript);

        String expectedHandler = "jQuery('#u09').on('click', function(e) {" + onClickScript + "}); ";

        assertEquals("generate event script is not correct", expectedHandler, onClickHandler);
    }

}
