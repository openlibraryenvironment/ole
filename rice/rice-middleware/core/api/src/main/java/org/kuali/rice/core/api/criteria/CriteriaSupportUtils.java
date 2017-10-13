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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * A class which includes various utilities and constants for use within the criteria API.
 * This class is intended to be for internal use only within the criteria API.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
final class CriteriaSupportUtils {
	
	private CriteriaSupportUtils () {}
	
    /**
     * Defines various property constants for internal use within the criteria package.
     */
    static class PropertyConstants {
    	
    	/**
    	 * A constant representing the property name for {@link PropertyPathPredicate#getPropertyPath()}
    	 */
        final static String PROPERTY_PATH = "propertyPath";
        
    	/**
    	 * A constant representing the property name for {@link SingleValuedPredicate#getValue()}
    	 */
        final static String VALUE = "value";
        
        /**
         * A constant representing the method name for {@link SingleValuedPredicate#getValue()}
         */
        final static String GET_VALUE_METHOD_NAME = "getValue";

        /**
    	 * A constant representing the property name for {@link MultiValuedPredicate#getValues()}
    	 */
        final static String VALUES = "values";

        /**
         * A constant representing the method name for {@link MultiValuedPredicate#getValues()}
         */
        final static String GET_VALUES_METHOD_NAME = "getValues";
    }

    /**
     * Validates the various properties of a {@link SingleValuedPredicate}.
     * 
     * @param valuedPredicateClass the type of the predicate
     * @param propertyPath the propertyPath which is being configured on the predicate
     * @param value the value which is being configured on the predicate
     * 
     * @throws IllegalArgumentException if the propertPath is null or blank
     * @throws IllegalArgumentException if the value is null
     * @throws IllegalArgumentException if the given {@link SingleValuedPredicate} class does not support the {@link CriteriaValue}
     */
    static void validateValuedConstruction(Class<? extends SingleValuedPredicate> valuedPredicateClass, String propertyPath, CriteriaValue<?> value) {
    	if (StringUtils.isBlank(propertyPath)) {
			throw new IllegalArgumentException("Property path cannot be null or blank.");
		}
		if (value == null) {
		    throw new IllegalArgumentException("CriteriaValue cannot be null.");
		}
		if (!CriteriaSupportUtils.supportsCriteriaValue(valuedPredicateClass, value)) {
		    throw new IllegalArgumentException(valuedPredicateClass.getSimpleName() + " does not support the given CriteriaValue");
		}
    }

	static boolean supportsCriteriaValue(Class<? extends SingleValuedPredicate> simplePredicateClass, CriteriaValue<?> value) {
	    if (simplePredicateClass == null) {
	        throw new IllegalArgumentException("simplePredicateClass was null");
	    }
	    if (value == null) {
	        throw new IllegalArgumentException("valueClass was null");
	    }
	    XmlElements elementsAnnotation = CriteriaSupportUtils.findXmlElementsAnnotation(simplePredicateClass);
	    if (elementsAnnotation != null) {
	        XmlElement[] elements = elementsAnnotation.value();
	        for (XmlElement element : elements) {
	            if (value.getClass().equals(element.type())) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	private static XmlElements findXmlElementsAnnotation(Class<?> simplePredicateClass) {
		if (simplePredicateClass != null) {
			try{
				Field valueField = simplePredicateClass.getDeclaredField(PropertyConstants.VALUE);
				XmlElements elementsAnnotation = valueField.getAnnotation(XmlElements.class);
				if (elementsAnnotation != null) {
					return elementsAnnotation;
				}
			} catch (NoSuchFieldException e) {
				// ignore, try the method
			}
			try {
				Method valueMethod = simplePredicateClass.getDeclaredMethod(PropertyConstants.GET_VALUE_METHOD_NAME, (Class<?>[])null);
				XmlElements elementsAnnotation = valueMethod.getAnnotation(XmlElements.class);
				if (elementsAnnotation == null) {
					return CriteriaSupportUtils.findXmlElementsAnnotation(simplePredicateClass.getSuperclass());
				}
				return elementsAnnotation;
			} catch (NoSuchMethodException e) {
				return CriteriaSupportUtils.findXmlElementsAnnotation(simplePredicateClass.getSuperclass());
			}
		}
		return null;
	}
	
	static CriteriaValue<?> determineCriteriaValue(Object object) {
		if (object == null) {
			throw new IllegalArgumentException("Given criteria value cannot be null.");
		} else if (object instanceof CharSequence) {
			return new CriteriaStringValue((CharSequence)object);
		} else if (object instanceof DateTime) {
            return new CriteriaDateTimeValue((DateTime)object);
		} else if (object instanceof Calendar) {
			return new CriteriaDateTimeValue((Calendar)object);
		} else if (object instanceof Date) {
			return new CriteriaDateTimeValue((Date)object);
		} else if (object instanceof BigInteger) {
			return new CriteriaIntegerValue((BigInteger)object);
		} else if (object instanceof Short) {
			return new CriteriaIntegerValue((Short)object);
		} else if (object instanceof Integer) {
			return new CriteriaIntegerValue((Integer)object);
		} else if (object instanceof AtomicInteger) {
			return new CriteriaIntegerValue((AtomicInteger)object);
		} else if (object instanceof Long) {
			return new CriteriaIntegerValue((Long)object);
		} else if (object instanceof AtomicLong) {
			return new CriteriaIntegerValue((AtomicLong)object);
		} else if (object instanceof BigDecimal) {
			return new CriteriaDecimalValue((BigDecimal)object);
		} else if (object instanceof Float) {
			return new CriteriaDecimalValue((Float)object);
		} else if (object instanceof Double) {
			return new CriteriaDecimalValue((Double)object);
		}
		throw new IllegalArgumentException("Failed to translate the given object to a CriteriaValue: " + object);
	}
	
	static Set<CriteriaValue<?>> determineCriteriaValueList(Object[] values) {
		if (values == null) {
			return null;
		} else if (values.length == 0) {
			return Collections.emptySet();
		}
		Set<CriteriaValue<?>> criteriaValues = new HashSet<CriteriaValue<?>>();
		for (Object value : values) {
			if (value != null) {
                criteriaValues.add(determineCriteriaValue(value));
            }
		}
		return criteriaValues;
	}

    static Set<CriteriaStringValue> createCriteriaStringValueList(CharSequence[] values) {
		if (values == null) {
			return null;
		} else if (values.length == 0) {
			return Collections.emptySet();
		}
		Set<CriteriaStringValue> criteriaValues = new HashSet<CriteriaStringValue>();
		for (CharSequence value : values) {
			if (value != null) {
                criteriaValues.add(new CriteriaStringValue(value));
            }
		}
		return criteriaValues;
	}
	
	/**
     * Validates the incoming list of CriteriaValue to ensure they are valid for a
     * {@link MultiValuedPredicate}.  To be valid, the following must be true:
     * 
     * <ol>
     *   <li>The list of values must not be null.</li>
     *   <li>The list of values must not be empty.</li>
     *   <li>The list of values must all be of the same parameterized {@link CriteriaValue} type.</li>
     * </ol>
     */
    static void validateValuesForMultiValuedPredicate(Set<? extends CriteriaValue<?>> values) {
    	if (values == null) {
    		throw new IllegalArgumentException("Criteria values cannot be null.");
    	} else if (values.isEmpty()) {
    		throw new IllegalArgumentException("Criteria values cannot be empty.");
    	}
    	Class<?> previousType = null;
    	for (CriteriaValue<?> value : values) {
    		Class<?> currentType = value.getClass();
    		if (previousType != null) {
    			if (!currentType.equals(previousType)) {
    				throw new IllegalArgumentException("Encountered criteria values which do not match.  One was: " + previousType + " the other was: " + currentType);
    			}
    		}
    		previousType = currentType;
    	}
    }

    static String findDynName(String name) {
        String correctedName = StringUtils.uncapitalize(name).replace("Predicate", "");
        //null is a keyword therefore they are called isNull & isNotNull
        if (correctedName.equals("null")) {
            correctedName = "isNull";
        } else if (correctedName.equals("notNull")) {
            correctedName = "isNotNull";
        }
        return correctedName;
    }

    public static String toString(SingleValuedPredicate p) {
        return new StringBuilder(CriteriaSupportUtils.findDynName(p.getClass().getSimpleName())).append("(")
                .append(p.getPropertyPath()).append(", ").append(p.getValue().getValue()).append(")").toString();
    }

    public static String toString(MultiValuedPredicate p) {
        final List<String> values = new ArrayList<String>();
        for (CriteriaValue<?> value : p.getValues()) {
            values.add(value.getValue().toString());
        }

        return new StringBuilder(CriteriaSupportUtils.findDynName(p.getClass().getSimpleName())).append("(")
                .append(p.getPropertyPath()).append(", ").append("[").append(StringUtils.join(values, ", ")).append("]").append(")").toString();
    }
}
