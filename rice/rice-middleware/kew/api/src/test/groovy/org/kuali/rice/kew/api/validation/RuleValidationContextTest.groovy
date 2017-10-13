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
package org.kuali.rice.kew.api.validation

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert
import org.kuali.rice.kew.api.rule.Rule
import org.kuali.rice.kew.api.rule.RuleDelegation
import org.junit.Ignore

/**
 * Unit test for RuleValidationContext object
 */
class RuleValidationContextTest {
    private static final String RULE = """
        <ns3:rule>
            <active>true</active>
            <forceAction>false</forceAction>
            <ruleExtensionMap/>
        </ns3:rule>
        """

    private static final String RVC = """
        <ns3:ruleValidationContext xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns:ns3="http://rice.kuali.org/kew/v2_0">
            ${RULE}
            <ns3:ruleDelegation/>
            <ns3:ruleAuthorPrincipalId>principalId</ns3:ruleAuthorPrincipalId>
 	 	</ns3:ruleValidationContext>
 	 	"""

    private static final String RVC_RULE_ONLY = """
        <ns3:ruleValidationContext xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns:ns3="http://rice.kuali.org/kew/v2_0">
            ${RULE}
 	 	</ns3:ruleValidationContext>
 	 	"""

    @Test(expected=IllegalArgumentException.class)
 	void test_Builder_create_fail_null_rule() {
 	    RuleValidationContext.Builder.create(null, null, null)
 	}

    @Test(expected=IllegalArgumentException.class)
 	void test_Builder_create_fail_null_source() {
 	    RuleValidationContext.Builder.create(null)
 	}

    @Test
    void test_Builder_create_success_optional_properties() {
        Rule rule = Rule.Builder.create().build()
 	    RuleValidationContext ctx = RuleValidationContext.Builder.create(rule, null, null).build()
 	    Assert.assertEquals(rule, ctx.getRule())
        Assert.assertNull(ctx.getRuleDelegation())
        Assert.assertNull(ctx.getRuleAuthorPrincipalId())
 	}

    @Test
    void test_Builder_create_success() {
        Rule rule = Rule.Builder.create().build()
        RuleDelegation ruleDelegation = RuleDelegation.Builder.create().build()
        String principalId = "principalId"
        RuleValidationContext ctx = RuleValidationContext.Builder.create(rule, ruleDelegation, principalId).build()
        Assert.assertEquals(rule, ctx.getRule())
        Assert.assertEquals(ruleDelegation, ctx.getRuleDelegation())
        Assert.assertEquals(principalId, ctx.getRuleAuthorPrincipalId())
 	}

    @Test
    void test_Builder_create_copy_success() {
        Rule rule = Rule.Builder.create().build()
        RuleDelegation ruleDelegation = RuleDelegation.Builder.create().build()
        String principalId = "principalId"
        def src = RuleValidationContext.Builder.create(rule, ruleDelegation, principalId).build()
        def ctx = RuleValidationContext.Builder.create(src).build()
 	    Assert.assertEquals(rule, ctx.getRule())
        Assert.assertEquals(ruleDelegation, ctx.getRuleDelegation())
        Assert.assertEquals(principalId, ctx.getRuleAuthorPrincipalId())
 	}

    @Test
    @Ignore
 	void test_Xml_Marshal_Unmarshal() {
        Rule rule = Rule.Builder.create("ruleName").build()
        RuleDelegation ruleDelegation = RuleDelegation.Builder.create().build()
        String principalId = "principalId"
        def ctx = RuleValidationContext.Builder.create(rule, ruleDelegation, principalId).build()
 	    JAXBAssert.assertEqualXmlMarshalUnmarshal(ctx, RVC, RuleValidationContext.class)
 	}

    @Test
    @Ignore
 	void test_Xml_Marshal_Unmarshal_just_rule() {
        Rule rule = Rule.Builder.create("ruleName").build()
        def ctx = RuleValidationContext.Builder.create(rule, null, null).build()
 	    JAXBAssert.assertEqualXmlMarshalUnmarshal(ctx, RVC_RULE_ONLY, RuleValidationContext.class)
 	}
}