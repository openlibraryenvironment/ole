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
package org.kuali.rice.kew.framework.validation

import groovy.mock.interceptor.MockFor
import javax.xml.namespace.QName
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.property.Config
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import org.kuali.rice.core.api.util.RiceConstants
import org.kuali.rice.core.framework.resourceloader.BaseResourceLoader
import org.kuali.rice.kew.api.rule.Rule
import org.kuali.rice.kew.api.validation.RuleValidationContext
import org.kuali.rice.kew.api.validation.ValidationResults
import org.kuali.rice.kew.impl.extension.ExtensionRepositoryServiceImpl
import org.kuali.rice.kew.rule.RuleValidationAttribute
import org.kuali.rice.kew.rule.bo.RuleAttribute
import org.kuali.rice.kew.rule.service.RuleAttributeService
import org.kuali.rice.kew.validation.RuleValidationAttributeExporterServiceImpl
import org.kuali.rice.kew.validation.RuleValidationAttributeResolver
import org.kuali.rice.kew.validation.RuleValidationAttributeResolverImpl

/**
 * Unit test for RuleValidationAttributeExporterService
 */
public class RuleValidationAttributeServicesTest {
    public static class TestRuleValidationAttribute implements RuleValidationAttribute {
        public ValidationResults validate(RuleValidationContext validationContext) {
            return ValidationResults.Builder.create().build();
        }
    }

    static final def MOCK_ATTR_NAME = "mock_attr_name"
    static final def MOCK_APP_ID = "mock_app_id"
    private RuleValidationAttributeExporterService exporter
    private RuleValidationAttributeResolver resolver= new RuleValidationAttributeResolverImpl() {
        protected RuleValidationAttributeExporterService findRuleValidationAttributeExporterService(String applicationId) {
            return exporter;
        }
    }

    private static void mockTheConfig() {
        def mock_config = new MockFor(Config)
        mock_config.demand.getProperty(CoreConstants.Config.APPLICATION_ID) { app_id -> MOCK_APP_ID }
        ConfigContext.init(mock_config.proxyDelegateInstance())
    }

    private static void mockTheResourceLoader() {
        mockTheConfig()
        GlobalResourceLoader.stop()
        GlobalResourceLoader.addResourceLoader(new BaseResourceLoader(new QName(MOCK_APP_ID, RiceConstants.DEFAULT_ROOT_RESOURCE_LOADER_NAME)))
        GlobalResourceLoader.start()
    }

    @Before
    void init() {
        mockTheResourceLoader()

        def mock_attr = new RuleAttribute()
        // ExtensionDefinition requires: name, type, classname
        mock_attr.name = MOCK_ATTR_NAME
        mock_attr.type = "RuleAttribute"
        mock_attr.resourceDescriptor = TestRuleValidationAttribute.class.name

        def attr_svc = new MockFor(RuleAttributeService)
        attr_svc.ignore.findByName { name -> mock_attr }

        def extension_repo = new ExtensionRepositoryServiceImpl()
        extension_repo.setRuleAttributeService(attr_svc.proxyDelegateInstance())

        exporter = new RuleValidationAttributeExporterServiceImpl()
        exporter.setExtensionRepositoryService(extension_repo)
    }

    @After
    void destroy() {
        GlobalResourceLoader.stop();
    }

    @Test(expected=IllegalArgumentException.class)
 	void test_exporter_validate_no_attribute_name() {
        exporter.validate(null, RuleValidationContext.Builder.create(Rule.Builder.create().build()).build())
 	}

    @Test
    void test_exporter_load_attribute() {
        def result = exporter.validate(MOCK_ATTR_NAME, RuleValidationContext.Builder.create(Rule.Builder.create().build()).build())
        Assert.assertNotNull(result)
 	}

    @Test
    void test_resolver() {
        def attrib = resolver.resolveRuleValidationAttribute(MOCK_ATTR_NAME, MOCK_APP_ID)
        Assert.assertNotNull(attrib)
        def result = attrib.validate(RuleValidationContext.Builder.create(Rule.Builder.create().build()).build())
        Assert.assertNotNull(result)
 	}
}