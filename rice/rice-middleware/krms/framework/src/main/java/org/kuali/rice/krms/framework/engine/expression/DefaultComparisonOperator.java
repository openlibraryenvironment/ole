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
package org.kuali.rice.krms.framework.engine.expression;

import org.apache.commons.lang.ObjectUtils;
import org.kuali.rice.krms.api.engine.IncompatibleTypeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The default {@link ComparisonOperator}.  If no other {@link EngineComparatorExtension} have been configured to handle
 * a type, the DefaultComparisonOperator will be used.  At the moment the DefaultComparisonOperator is also the default
 * {@link StringCoercionExtension} for coercing types.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class DefaultComparisonOperator implements EngineComparatorExtension, StringCoercionExtension {

    @Override
    public int compare(Object lhs, Object rhs) {

        if (lhs == null && rhs == null) {
            return 0;
        } else if (lhs == null) {
            return -1;
        } else if (rhs == null) {
            return 1;
        }

        if (rhs instanceof String && !(lhs instanceof String)) {
            rhs = coerceStringOperand(lhs, rhs.toString());
        } else if (lhs instanceof String && !(rhs instanceof String)) {
            lhs = coerceStringOperand(rhs, lhs.toString());
        }


        if (ObjectUtils.equals(lhs, rhs)) {
            return 0;
        }

        if (lhs instanceof Comparable && rhs instanceof Comparable) {
            int result = ((Comparable)lhs).compareTo(rhs);
            return result;
        }
        else {
            throw new IncompatibleTypeException("DefaultComparisonOperator could not compare values", lhs, rhs.getClass());
        }
    }

    @Override
    public boolean canCompare(Object lhs, Object rhs) {
        try {
            compare(lhs, rhs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 
     * @param objectArg
     * @param stringArg
     * @return Object
     * @throws IncompatibleTypeException
     */
    private Object coerceStringOperand(Object objectArg, String stringArg) {
        Object result = stringArg;
        if (objectArg != null && stringArg != null) {
            if  (!(objectArg instanceof String)) {
                result = coerceHelper(objectArg, stringArg, Double.class, Float.class, Long.class, Integer.class, Boolean.class);

                if (result instanceof String) { // was coercion successful?
                    if (objectArg instanceof BigDecimal) {
                        try {
                            result = BigDecimal.valueOf(Double.valueOf(stringArg.toString()));
                        } catch (NumberFormatException e) {
                            throw new IncompatibleTypeException("Could not coerce String to BigDecimal" + this, stringArg, objectArg.getClass());
                        }
                    } else if (objectArg instanceof BigInteger) {
                        try {
                            result = BigInteger.valueOf(Long.valueOf(stringArg.toString()));
                        } catch (NumberFormatException e) {
                            throw new IncompatibleTypeException("Could not coerce String to BigInteger" + this, stringArg, objectArg.getClass());
                        }
                    } else {
                        throw new IncompatibleTypeException("Could not compare values for operator " + this, objectArg, stringArg.getClass());
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     * @param objectArg
     * @param stringArg
     * @param clazzes
     * @return The object of one of the given types, whose value is stringArg
     */
    private Object coerceHelper(Object objectArg, String stringArg, Class<?> ... clazzes) {
        for (Class clazz : clazzes) {
            if (clazz.isInstance(objectArg)) {
                try {
                    return clazz.getMethod("valueOf", String.class).invoke(null, stringArg);
                } catch (NumberFormatException e) {
                    throw new IncompatibleTypeException("Could not coerce String to " +
                            clazz.getSimpleName() + " " + this, stringArg, objectArg.getClass());
                } catch (NoSuchMethodException e) {
                    throw new IncompatibleTypeException("Could not coerce String to " +
                            clazz.getSimpleName() + " " + this, stringArg, objectArg.getClass());
                } catch (InvocationTargetException e) {
                    throw new IncompatibleTypeException("Could not coerce String to " +
                            clazz.getSimpleName() + " " + this, stringArg, objectArg.getClass());
                } catch (IllegalAccessException e) {
                    throw new IncompatibleTypeException("Could not coerce String to " +
                            clazz.getSimpleName() + " " + this, stringArg, objectArg.getClass());
                }
            }
        }
        return stringArg;
    }

    @Override
    public Object coerce(String type, String value) {
        try {
            Class clazz = Class.forName(type);
            // Constructor that takes string  a bit more generic than the coerceRhs
            Constructor constructor = clazz.getConstructor(new Class[]{String.class});
            Object propObject = constructor.newInstance(value);
            return propObject;
        } catch (Exception e) {
            return null; // TODO EGHM dev log?
        }
    }

    @Override
    public boolean canCoerce(String type, String value) {
        return coerce(type, value) != null;
    }
}
