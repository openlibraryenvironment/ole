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
package org.kuali.rice.core.api.util.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An abstract base class for InvocationHanlders which can be used to implement
 * an InvocationHandler that delegates hashCode and equals methods to the proxied
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BaseInvocationHandler implements InvocationHandler {

	// preloaded Method objects for the methods in java.lang.Object
    private static Method hashCodeMethod;
    private static Method equalsMethod;
    private static Method toStringMethod;
    static {
	try {
	    hashCodeMethod = Object.class.getMethod("hashCode", (Class[])null);
	    equalsMethod = Object.class.getMethod("equals", new Class[] { Object.class });
        toStringMethod = Object.class.getMethod("toString", (Class[])null);
	    } catch (NoSuchMethodException e) {
	    	// this should never happen
	    	throw new NoSuchMethodError(e.getMessage());
	    }
    }

    @Override
	public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
		Class declaringClass = method.getDeclaringClass();
		if (declaringClass == Object.class) {
		    if (method.equals(hashCodeMethod)) {
		    	return proxyHashCode(proxy);
		    } else if (method.equals(equalsMethod)) {
		    	return proxyEquals(proxy, arguments[0]);
		    } else if (method.equals(toStringMethod)) {
			    return proxyToString(proxy);
		    }
		}
		try {
			return invokeInternal(proxy, method, arguments);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

	protected abstract Object invokeInternal(Object proxy, Method method, Object[] arguments) throws Throwable;

	protected Integer proxyHashCode(Object proxy) {
		return new Integer(System.identityHashCode(proxy));
	}

	protected Boolean proxyEquals(Object proxy, Object other) {
		return (proxy == other ? Boolean.TRUE : Boolean.FALSE);
	}

    protected String proxyToString(Object proxy) {
        return toString();
    }

}
