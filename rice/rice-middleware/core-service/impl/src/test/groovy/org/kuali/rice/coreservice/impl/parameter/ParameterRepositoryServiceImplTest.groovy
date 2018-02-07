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
package org.kuali.rice.coreservice.impl.parameter

import groovy.mock.interceptor.MockFor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.kuali.rice.coreservice.api.parameter.EvaluationOperator
import org.kuali.rice.coreservice.api.parameter.Parameter
import org.kuali.rice.coreservice.api.parameter.ParameterContract
import org.kuali.rice.coreservice.api.parameter.ParameterKey
import org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService
import org.kuali.rice.coreservice.api.parameter.ParameterType
import org.kuali.rice.coreservice.api.parameter.ParameterTypeContract
import org.kuali.rice.krad.service.BusinessObjectService
import static org.junit.Assert.assertNotNull

class ParameterRepositoryServiceImplTest {
    private MockFor mock

    //importing the should fail method since I don't want to extend
    //GroovyTestCase which is junit 3 style
    private final shouldFail = new GroovyTestCase().&shouldFail

    static final Parameter parameter = createParameter();
    static final ParameterKey key =
    ParameterKey.create("BORG_HUNT", "TNG", "C", "SHIELDS_ON");

    ParameterRepositoryService pservice;
    org.kuali.rice.coreservice.impl.parameter.ParameterRepositoryServiceImpl pserviceImpl;
    org.kuali.rice.coreservice.impl.parameter.ParameterBo bo = org.kuali.rice.coreservice.impl.parameter.ParameterBo.from(parameter);
    BusinessObjectService boService;

    @Before
    void setupServiceUnderTest() {
        pserviceImpl = new org.kuali.rice.coreservice.impl.parameter.ParameterRepositoryServiceImpl()
        pservice = pserviceImpl
    }

    @Before
    void setupBoServiceMockContext() {
        mock = new MockFor(BusinessObjectService)

    }

    void injectBusinessObjectServiceIntoParameterRepositoryService() {
        boService = mock.proxyDelegateInstance()
        pserviceImpl.setBusinessObjectService(boService)
    }

    @Test
    void test_create_parameter_null_parameter() {
        injectBusinessObjectServiceIntoParameterRepositoryService()

        shouldFail(IllegalArgumentException.class) {
            pservice.createParameter(null)
        }
        mock.verify(boService)
    }

    @Test
    void test_create_parameter_exists() {
        mock.demand.findByPrimaryKey(1..1) { clazz, map -> bo }

        injectBusinessObjectServiceIntoParameterRepositoryService()

        shouldFail(IllegalStateException.class) {
            pservice.createParameter(parameter)
        }
        mock.verify(boService)
    }

    @Test
    void test_create_parameter_does_not_exist() {
        mock.demand.findByPrimaryKey(2..2) { clazz, map -> null }
        mock.demand.save { bo -> bo}
        injectBusinessObjectServiceIntoParameterRepositoryService()
        def p = pservice.createParameter(parameter)
        assertNotNull p
        mock.verify(boService)

    }

    @Test
    void test_update_parameter_null_parameter() {
        injectBusinessObjectServiceIntoParameterRepositoryService()

        shouldFail(IllegalArgumentException.class) {
            pservice.updateParameter(null)
        }
        mock.verify(boService)
    }

    @Test
    void test_update_parameter_exists() {
        mock.demand.findByPrimaryKey(1..1) { clazz, map -> bo }
        mock.demand.save { bo -> bo }
        injectBusinessObjectServiceIntoParameterRepositoryService()
        def p = pservice.updateParameter(parameter)
        assertNotNull p
        mock.verify(boService)
    }

    @Test
    void test_update_parameter_does_not_exist() {
        mock.demand.findByPrimaryKey(2..2) { clazz, map -> null }

        injectBusinessObjectServiceIntoParameterRepositoryService()

        shouldFail(IllegalStateException.class) {
            pservice.updateParameter(parameter)
        }
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_null_key() {
        injectBusinessObjectServiceIntoParameterRepositoryService()

        shouldFail(IllegalArgumentException.class) {
            pservice.getParameter(null)
        }
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_exists() {
        mock.demand.findByPrimaryKey(1..1) { clazz, map -> bo }
        injectBusinessObjectServiceIntoParameterRepositoryService()
        Assert.assertEquals(parameter, pservice.getParameter(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_does_not_exist() {
        mock.demand.findByPrimaryKey(2..2) { clazz, map -> null }
        injectBusinessObjectServiceIntoParameterRepositoryService()
        Assert.assertNull(pservice.getParameter(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_value_as_string_not_null() {
        mock.demand.findByPrimaryKey(1..1) { clazz, map -> bo }
        injectBusinessObjectServiceIntoParameterRepositoryService()
        Assert.assertEquals(parameter.value, pservice.getParameterValueAsString(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_value_as_string_null() {
        mock.demand.findByPrimaryKey(2..2) { clazz, map -> null }
        injectBusinessObjectServiceIntoParameterRepositoryService()
        Assert.assertNull(pservice.getParameterValueAsString(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_value_as_boolean_null() {
        mock.demand.findByPrimaryKey(2..2) { clazz, map -> null }
        injectBusinessObjectServiceIntoParameterRepositoryService()
        Assert.assertNull(pservice.getParameterValueAsBoolean(key))
        mock.verify(boService)
    }

    private test_get_parameter_value_as_boolean_not_null(String value, boolean bValue) {
        org.kuali.rice.coreservice.impl.parameter.ParameterBo bo = org.kuali.rice.coreservice.impl.parameter.ParameterBo.from(parameter);
        bo.value = value

        mock.demand.findByPrimaryKey(1..1) { clazz, map -> bo }
        injectBusinessObjectServiceIntoParameterRepositoryService()
        Assert.assertEquals(bValue, pservice.getParameterValueAsBoolean(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_Y() {
        test_get_parameter_value_as_boolean_not_null("Y", true)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_true() {
        //mixed case
        test_get_parameter_value_as_boolean_not_null("tRue", true)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_N() {
        test_get_parameter_value_as_boolean_not_null("N", false)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_false() {
        //mixed case
        test_get_parameter_value_as_boolean_not_null("fAlse", false)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_not_boolean() {
        bo.value = "not boolean"

        mock.demand.findByPrimaryKey(1..1) { clazz, map ->  bo}
        injectBusinessObjectServiceIntoParameterRepositoryService()
        Assert.assertNull(pservice.getParameterValueAsBoolean(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_values_as_string_null() {
        mock.demand.findByPrimaryKey(2..2) { clazz, map ->  null}
        injectBusinessObjectServiceIntoParameterRepositoryService()
        def values = pservice.getParameterValuesAsString(key)
        Assert.assertTrue(values.isEmpty())

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_values_as_string_not_null_multiple_values() {
        //adding whitespace
        bo.value = "foo; bar ; baz "

        mock.demand.findByPrimaryKey(1..1) { clazz, map ->  bo}
        injectBusinessObjectServiceIntoParameterRepositoryService()
        def values = pservice.getParameterValuesAsString(key)
        Assert.assertTrue(values.size() == 3)
        Assert.assertTrue(values.contains("foo"))
        Assert.assertTrue(values.contains("bar"))
        Assert.assertTrue(values.contains("baz"))

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_values_as_string_not_null_single_values() {
        //adding whitespace
        bo.value = "a value "

        mock.demand.findByPrimaryKey(1..1) { clazz, map ->  bo}
        injectBusinessObjectServiceIntoParameterRepositoryService()
        def values = pservice.getParameterValuesAsString(key)
        Assert.assertTrue(values.size() == 1)
        Assert.assertTrue(values.contains("a value"))

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_value_as_string_null_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValueAsString(key, null)
        }
    }

    @Test
    void test_get_sub_parameter_value_as_string_empty_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValueAsString(key, "")
        }
    }

    @Test
    void test_get_sub_parameter_value_as_string_whitespace_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValueAsString(key, "  ")
        }
    }

    @Test
    void test_get_sub_parameter_value_as_string_null() {
        mock.demand.findByPrimaryKey(2..2) { clazz, map ->  null}
        injectBusinessObjectServiceIntoParameterRepositoryService()
        Assert.assertNull(pservice.getSubParameterValueAsString(key, "foo"))
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_value_as_string_single_match() {
        //adding whitespace
        bo.value = "foo= f1; bar=b1; baz=z1"

        mock.demand.findByPrimaryKey(1..1) { clazz, map ->  bo}
        injectBusinessObjectServiceIntoParameterRepositoryService()

        Assert.assertEquals("f1", pservice.getSubParameterValueAsString(key, "foo"))
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_value_as_string_multiple_match() {
        bo.value = "foo=f1; bar=b1; foo=f2"

        mock.demand.findByPrimaryKey(1..1) { clazz, map ->  bo}
        injectBusinessObjectServiceIntoParameterRepositoryService()

        //should return first match
        Assert.assertEquals("f1", pservice.getSubParameterValueAsString(key, "foo"))
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_values_as_string_null_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValuesAsString(key, null)
        }
    }

    @Test
    void test_get_sub_parameter_values_as_string_empty_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValuesAsString(key, "")
        }
    }

    @Test
    void test_get_sub_parameter_values_as_string_whitespace_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValuesAsString(key, "  ")
        }
    }

    @Test
    void test_get_sub_parameter_values_as_string_null() {
        mock.demand.findByPrimaryKey(2..2) { clazz, map ->  null}
        injectBusinessObjectServiceIntoParameterRepositoryService()
        def values = pservice.getSubParameterValuesAsString(key, "foo")
        Assert.assertTrue(values.isEmpty())

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_values_as_string_single_match() {
        //adding whitespace
        bo.value = "foo= f1, f2 , f3; bar=b1; baz=z1"

        mock.demand.findByPrimaryKey(1..1) { clazz, map ->  bo}
        injectBusinessObjectServiceIntoParameterRepositoryService()

        def values = pservice.getSubParameterValuesAsString(key, "foo")
        Assert.assertTrue(values.size() == 3)
        Assert.assertTrue(values.contains("f1"))
        Assert.assertTrue(values.contains("f2"))
        Assert.assertTrue(values.contains("f3"))

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_values_as_string_multiple_match() {
        //adding whitespace
        bo.value = "foo= f1, f2 , f3; bar=b1; foo=f4,f5"

        mock.demand.findByPrimaryKey(1..1) { clazz, map ->  bo}
        injectBusinessObjectServiceIntoParameterRepositoryService()

        def values = pservice.getSubParameterValuesAsString(key, "foo")
        Assert.assertTrue(values.size() == 3)
        Assert.assertTrue(values.contains("f1"))
        Assert.assertTrue(values.contains("f2"))
        Assert.assertTrue(values.contains("f3"))

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    private static createParameter() {
        return Parameter.Builder.create(new ParameterContract() {
            def String name = "SHIELDS_ON"

            def ParameterType getParameterType() {
                ParameterType.Builder.create(new ParameterTypeContract() {
                    def String code = "PC"
                    def String name = "Config"
                    def boolean active = true
                    def Long versionNumber = 1
                    def String objectId = UUID.randomUUID()
                }).build()
            }

            def String applicationId = "BORG_HUNT"
            def String namespaceCode = "TNG"
            def String componentCode = "C"
            def String value = "true"
            def String description = "turn the shields on"
            def EvaluationOperator evaluationOperator = EvaluationOperator.ALLOW
            def Long versionNumber = 1
            def String objectId = UUID.randomUUID()
        }).build()
    }
}
