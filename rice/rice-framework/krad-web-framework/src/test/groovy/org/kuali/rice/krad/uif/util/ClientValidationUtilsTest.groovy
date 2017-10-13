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
package org.kuali.rice.krad.uif.util

import org.junit.Test
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint

import org.kuali.rice.krad.uif.component.BindingInfo
import org.junit.Before
import org.kuali.rice.core.api.util.RiceConstants
import javax.xml.namespace.QName
import org.kuali.rice.core.framework.resourceloader.BaseResourceLoader
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import groovy.mock.interceptor.MockFor
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.property.Config
import static org.junit.Assert.assertEquals
import org.kuali.rice.krad.uif.field.InputField

class ClientValidationUtilsTest {
    static final def MOCK_ATTR_NAME = "mock_attr_name"
    static final def MOCK_APP_ID = "mock_app_id"

    private static void mockTheConfig() {
        def mock_config = new MockFor(Config)
        mock_config.demand.getProperty(CoreConstants.Config.APPLICATION_ID) { app_id -> MOCK_APP_ID }
        ConfigContext.init(mock_config.proxyDelegateInstance())
    }

    private static void mockTheResourceLoader() {
        mockTheConfig()
        GlobalResourceLoader.addResourceLoader(new BaseResourceLoader(new QName(MOCK_APP_ID, RiceConstants.DEFAULT_ROOT_RESOURCE_LOADER_NAME)));
        GlobalResourceLoader.start();
    }

    // FIXME: ClientValidationUtils statically initializes a ConfigurationService singleton on class initialization
    // so we have to mock out the GRL so that code does not bomb
    @Before
    void init() {
        mockTheResourceLoader()
    }

    @Test
    void testGetRegexMethod() {
        // slash should be escaped to \/
        // and double backslash should be escaped to \\\\
        def value = 'abc/123\\\\'
        def expected_val = '''jQuery.validator.addMethod("validChar-bindingPath0", function(value, element) {
 return this.optional(element) || /abc\\/123\\\\\\\\/.test(value);}, "NO MESSAGE");'''

        def constraint = new ValidCharactersConstraint(value: value)
        def field = new InputField(bindingInfo: new BindingInfo(bindingPath: "bindingPath"))
        def javascript = ClientValidationUtils.getRegexMethod(field, constraint)
        assertEquals(expected_val, javascript.trim())
    }

    @Test
    void getRegexMethodWithBooleanCheck() {
        // slash should be escaped to \/
        // and double backslash should be escaped to \\\\
        def value = 'abc/123\\\\'
        def expected_val = '''jQuery.validator.addMethod("validChar-bindingPath0", function(value, element, doCheck) {
 if(doCheck === false){return true;}else{return this.optional(element) || /abc\\/123\\\\\\\\/.test(value);}}, "NO MESSAGE");'''

        def constraint = new ValidCharactersConstraint(value: value)
        def field = new InputField(bindingInfo: new BindingInfo(bindingPath: "bindingPath"))
        def javascript = ClientValidationUtils.getRegexMethodWithBooleanCheck(field, constraint)
        assertEquals(expected_val, javascript.trim())
    }

    @Test
    void testEmptyLabelKeyReturnsDefaultMessage() {
        def DEFAULT_MESSAGE = "NO MESSAGE"
        assertEquals(DEFAULT_MESSAGE, ClientValidationUtils.generateMessageText(null, null, null, null))
    }
}
