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
package org.kuali.rice.core.api.criteria;


import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElements
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType
import org.apache.commons.lang.builder.ToStringBuilder
import org.junit.Test
import static org.junit.Assert.*

/**
 * A test for the {@link SingleValuedPredicate} abstract base class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SimplePredicateTest {
    
    /**
     * Test method for {@link SingleValuedPredicate#getValue()}.
     * 
     * This empty constructor should only be invoked by JAXB.  We will invoke to ensure that it doesn't raise an exception.
     */
    @Test
    public void testSimpleExpression() {
        StringOnlyExpression expression1 = new StringOnlyExpression();
        assertNull(expression1.getPropertyPath());
        assertNull(expression1.getValue());
        
        new AllExpression();
    }

    /**
     * Test method for {@link SingleValuedPredicate#getValue()}.
     */
    @Test
    public void testSimpleExpressionStringCriteriaValueOfObject() {

        StringOnlyExpression expression2 = new StringOnlyExpression("path", new CriteriaStringValue("pathValue"));
        assertEquals("path", expression2.getPropertyPath());
        assertTrue(expression2.getValue() instanceof CriteriaStringValue);
        assertEquals("pathValue", expression2.getValue().getValue());
        
        // let's ensure that the "supports" method is being called, try to pass a CriteriaDateTimeValue which should trigger IllegalArgumentException
        try {
            new StringOnlyExpression("path", new CriteriaDateTimeValue(Calendar.getInstance()));
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
        }
        
        // check the failure cases
        try {
            new StringOnlyExpression(null, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
        }
        
        try {
            new StringOnlyExpression(null, new CriteriaStringValue("pathValue"));
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
        }
        
        // use a "blank" string for propertyPath, should not be allowed
        try {    
            new StringOnlyExpression(" ", new CriteriaStringValue("pathValue"));
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
        }
        
        try {
            new StringOnlyExpression("path", null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
        }
        
        AllExpression expression3 = new AllExpression("path", new CriteriaDecimalValue(BigDecimal.ZERO));
        assertEquals("path", expression3.getPropertyPath());
        assertTrue(expression3.getValue() instanceof CriteriaDecimalValue);
        assertEquals(BigDecimal.ZERO, expression3.getValue().getValue());
    }

    /**
     * Test method for {@link CriteriaSupportUtils#supportsCriteriaValue(Class<? extends org.kuali.rice.core.api.criteria.SingleValuedPredicate>, CriteriaValue<?>) }.
     */
    @Test
    public void testSupportsCriteriaValue() {
        
        // first test failure cases
        
        try {
            CriteriaSupportUtils.supportsCriteriaValue(null, null);
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        
        try {
        	CriteriaSupportUtils.supportsCriteriaValue(null, new CriteriaStringValue("value"));
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        
        try {
        	CriteriaSupportUtils.supportsCriteriaValue(SingleValuedPredicate.class, null);
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        
        // SimpleExpression supports string, decimal, integer and dateTime CriteriaValues
        assertTrue("Should support CriteriaStringValue", CriteriaSupportUtils.supportsCriteriaValue(AllExpression.class, new CriteriaStringValue("value")));
        assertTrue("Should support CriteriaDecimalValue", CriteriaSupportUtils.supportsCriteriaValue(AllExpression.class, new CriteriaDecimalValue(BigDecimal.ZERO)));
        assertTrue("Should support CriteriaIntegerValue", CriteriaSupportUtils.supportsCriteriaValue(AllExpression.class, new CriteriaIntegerValue(BigInteger.ZERO)));
        assertTrue("Should support CriteriaDateTimeValue", CriteriaSupportUtils.supportsCriteriaValue(AllExpression.class, new CriteriaDateTimeValue(Calendar.getInstance())));
        
        // test an expression which only supports string criteria
        assertTrue("Should support CriteriaStringValue", CriteriaSupportUtils.supportsCriteriaValue(StringOnlyExpression.class, new CriteriaStringValue("value")));
        assertFalse("Should NOT support CriteriaDecimalValue", CriteriaSupportUtils.supportsCriteriaValue(StringOnlyExpression.class, new CriteriaDecimalValue(BigDecimal.ZERO))); 
        
    }
    
    /**
     * A mock SimpleExpression for use in the unit test which allows all of the different {@link CriteriaValue}.
     * 
     * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    @XmlRootElement(name = "like")
    @XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
    @XmlType(name = "AllExpressionType")
    private static final class AllExpression extends AbstractPredicate implements SingleValuedPredicate {

    	private static final long serialVersionUID = -5606375770690671272L;
    	
		@XmlAttribute(name = "propertyPath")
    	private final String propertyPath;
    	@XmlElements(value = [
        		@XmlElement(name = "stringValue", type = CriteriaStringValue.class, required = true),
        		@XmlElement(name = "dateTimeValue", type = CriteriaDateTimeValue.class, required = true),
        		@XmlElement(name = "decimalValue", type = CriteriaDecimalValue.class, required = true),
        		@XmlElement(name = "integerValue", type = CriteriaIntegerValue.class, required = true)
        ])
    	private final CriteriaValue<?> value;
    	
        private AllExpression() {
            this.propertyPath = null;
            this.value = null;
        }
        
        public AllExpression(String propertyPath, CriteriaValue<?> value) {
        	CriteriaSupportUtils.validateValuedConstruction(getClass(), propertyPath, value);
    		this.propertyPath = propertyPath;
    		this.value = value;
        }
        
        @Override
        public String getPropertyPath() {
        	return propertyPath;
        }
        
    	@Override
    	public CriteriaValue<?> getValue() {
    		return value;
    	}

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
                
    }
    
    /**
     * A mock SimpleExpression for use in the unit test which only allows StringCriteriaValue.
     * 
     * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    @XmlRootElement(name = "like")
    @XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
    @XmlType(name = "StringOnlyExpressionType")
    private static final class StringOnlyExpression extends AbstractPredicate implements SingleValuedPredicate {

    	private static final long serialVersionUID = 5874946840884110187L;
    	
		@XmlAttribute(name = "propertyPath")
    	private final String propertyPath;
    	@XmlElements(value = [
        		@XmlElement(name = "stringValue", type = CriteriaStringValue.class, required = true)
        ])
    	private final CriteriaValue<?> value;
    	
        private StringOnlyExpression() {
            this.propertyPath = null;
            this.value = null;
        }
        
        public StringOnlyExpression(String propertyPath, CriteriaValue<?> value) {
        	CriteriaSupportUtils.validateValuedConstruction(getClass(), propertyPath, value);
    		this.propertyPath = propertyPath;
    		this.value = value;
        }
        
        @Override
        public String getPropertyPath() {
        	return propertyPath;
        }
        
    	@Override
    	public CriteriaValue<?> getValue() {
    		return value;
    	}

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
                
    }

}
