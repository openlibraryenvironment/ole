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
package org.kuali.rice.kew.rule;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.validation.RuleValidationContext;
import org.kuali.rice.kew.framework.validation.RuleValidationAttributeExporterService;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.validation.RuleValidationAttributeResolver;
import org.kuali.rice.ksb.messaging.resourceloader.ServiceBusResourceLoader;

import javax.xml.namespace.QName;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

/**
 * Tests invoking the RuleValidationAttribute services.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleValidationAttributeRemotingTest extends KEWTestCase {

    private static RuleValidationAttributeResolver resolver;
    private static ServiceBusResourceLoader bus;
    private static final QName EXPORTER_SOAP_SERVICE_NAME = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0,  "ruleValidationAttributeExporterService");

    private static final ServiceBusResourceLoader getServiceBusResourceLoader(ResourceLoader loader) {
        if (loader instanceof ServiceBusResourceLoader) {
            return (ServiceBusResourceLoader) loader;
        }
        for (ResourceLoader rl: loader.getResourceLoaders()) {
            if (rl instanceof ServiceBusResourceLoader) {
                return (ServiceBusResourceLoader) rl;
            } else {
                ResourceLoader rl0 = getServiceBusResourceLoader(rl);
                if (rl0 != null) {
                    return (ServiceBusResourceLoader) rl0;
                }
            }
        }
        return null;
    }

    @Before
    public void obtainServices() {
        resolver = KEWServiceLocator.getRuleValidationAttributeResolver();
        bus = getServiceBusResourceLoader(GlobalResourceLoader.getResourceLoader());
        Assert.assertNotNull("could not find service bus resource loader", bus);
    }

    protected void loadTestData() throws Exception {
        loadXmlFile("RuleTemplateAttributeTestConfig.xml");
    }

    private static final Predicate RULE_VALIDATION_ATTRIB_PREDICATE = new Predicate() {
        @Override
        public boolean evaluate(Object attrib) {
            return KewApiConstants.RULE_VALIDATION_ATTRIBUTE_TYPE.equals(((RuleAttribute) attrib).getType());
        }
    };

    /**
     * Tests that the TestRuleValidationAttribute exposed by the exporter is invoked (via the resolver).
     * This test assumes that the only rule validation attribute present is the TestRuleValidationAttribute.
     */
    @Test
    public void test_resolver() throws Exception {
        TestRuleValidationAttribute.invocations = 0;
        int invocations = 0;
        for (RuleAttribute attrib: (Collection<RuleAttribute>) CollectionUtils.select(KEWServiceLocator.getRuleAttributeService().findAll(), RULE_VALIDATION_ATTRIB_PREDICATE)) {
            RuleValidationAttribute rva = resolver.resolveRuleValidationAttribute(attrib.getName(), null);
            assertNotNull("RuleValidationAttribute resolution failed", rva);
            RuleValidationContext ctx = RuleValidationContext.Builder.create(org.kuali.rice.kew.api.rule.Rule.Builder.create().build()).build();
            assertNotNull("RuleValidationAttribute returned null result", rva.validate(ctx));
            invocations++;
        }
        Assert.assertEquals("Actual TestRuleValidationAttribute invocations did not match expected invocations",
                invocations, TestRuleValidationAttribute.invocations);
    }

    /**
     * Tests invoking the exported SOAP RuleValidationAttributeExporterService.
     */
    @Test
    public void test_exporter() throws Exception {
        RuleValidationAttributeExporterService exporter = (RuleValidationAttributeExporterService) bus.getService(EXPORTER_SOAP_SERVICE_NAME);
        Assert.assertNotNull("Could not find the RuleValidationAttributeExporterService SOAP endpoint on the bus!", exporter);

        TestRuleValidationAttribute.invocations = 0;
        int invocations = 0;
        for (RuleAttribute attrib: (Collection<RuleAttribute>) CollectionUtils.select(KEWServiceLocator.getRuleAttributeService().findAll(), RULE_VALIDATION_ATTRIB_PREDICATE)) {
            RuleValidationContext ctx = RuleValidationContext.Builder.create(org.kuali.rice.kew.api.rule.Rule.Builder.create().build()).build();
            assertNotNull("RuleValidationAttributeExporterService return value was null", exporter.validate(attrib.getName(), ctx));
            invocations++;
        }
        Assert.assertEquals("Actual TestRuleValidationAttribute invocations did not match expected invocations",
                invocations, TestRuleValidationAttribute.invocations);
    }

}
