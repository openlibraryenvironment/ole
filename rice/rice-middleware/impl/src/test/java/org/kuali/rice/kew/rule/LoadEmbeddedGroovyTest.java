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
package org.kuali.rice.kew.rule;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Tests that Groovy can be loaded via Bean Scripting Framework

 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LoadEmbeddedGroovyTest {
    @Test public void testNativeGroovy() {
        Binding binding = new Binding();
        binding.setVariable("foo", new Integer(2));
        GroovyShell shell = new GroovyShell(binding);

        Object value = shell.evaluate("println 'Hello World!'; x = 123; return foo * 10");
        Assert.assertTrue(value.equals(new Integer(20)));
        Assert.assertTrue(binding.getVariable("x").equals(new Integer(123)));
    }

    @Test public void testJSR223Groovy() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        engine.eval("println 'hello embedded groovy world'");
    }

    @Test public void testJSR223Groovy2() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        engine.eval("var = 0\r\ndef foo() {\r\n var++\r\n }\r\n foo()");
    }
}
