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
package org.kuali.rice.core.api.util;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.reflect.TargetedInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;

/**
 * Provides common utility methods for dealing with Classloaders.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class ClassLoaderUtils {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ClassLoaderUtils.class);
    
	private ClassLoaderUtils() {
		throw new UnsupportedOperationException("do not call");
	}

	/**
	 * Returns the default class loader within the current context.  If there is a context classloader
	 * it is returned, otherwise the classloader which loaded the ClassLoaderUtil Class is returned.
	 *
	 * @return the appropriate default classloader which is guaranteed to be non-null
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoaderUtils.class.getClassLoader();
		}
		return classLoader;
	}

	/**
	 * Checks if the given object is an instance of the given class, unwrapping any proxies if
	 * necessary to get to the underlying object.
	 */
	public static boolean isInstanceOf(Object object, Class<?> instanceClass) {
		if (object == null) {
			return false;
		}
		if (instanceClass.isInstance(object)) {
			return true;
		}
		return isInstanceOf(unwrapFromProxyOnce(object), instanceClass);
	}

	public static Object unwrapFromProxy(Object proxy) {
		Object unwrapped = unwrapFromProxyOnce(proxy);
		if (unwrapped == null) {
			return proxy;
		}
		return unwrapFromProxy(unwrapped);
	}

	/**
	 * Unwraps the underlying object from the given proxy (which may itself be a proxy).  If the
	 * given object is not a valid proxy, then null is returned.
	 */
	private static Object unwrapFromProxyOnce(Object proxy) {
		if (proxy != null && Proxy.isProxyClass(proxy.getClass())) {
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
			if (invocationHandler instanceof TargetedInvocationHandler) {
				return ((TargetedInvocationHandler)invocationHandler).getTarget();
			}
		}
		return null;
	}

	/**
	 * Checks if the given Class is visible to the given ClassLoader.
	 */
	public static boolean isClassVisible(ClassLoader classLoader, Class<?> classToCheck) {
	    try {
		Class<?> classFound = classLoader.loadClass(classToCheck.getName());
		return classFound.equals(classToCheck);
	    } catch (ClassNotFoundException e) {
		return false;
	    }
	}

	/**
	 * Determines the interfaces which need to be proxied and are visible to the given proxy ClassLoader.
	 */
	public static Class[] getInterfacesToProxy(Object object, ClassLoader proxyClassLoader, String[] packageNamesToFilter) {
	    List interfaces = ClassUtils.getAllInterfaces(object.getClass());
	    outer:for (Iterator iterator = interfaces.iterator(); iterator.hasNext();) {
		Class objectInterface = (Class) iterator.next();
		// check package name filters
		if (packageNamesToFilter != null) {
		    for (String packageNames : packageNamesToFilter) {
			if (objectInterface.getName().startsWith(packageNames)) {
			    iterator.remove();
			    continue outer;
			}
		    }
		}
		// check that the interface is visible from the given classloader
		if (proxyClassLoader != null) {
		    if (!ClassLoaderUtils.isClassVisible(proxyClassLoader, objectInterface)) {
			if (LOG.isDebugEnabled()) {
			    LOG.debug("The interface " + objectInterface + " was not visible from the proxy ClassLoader when attempting to proxy: " + object);
			}
			iterator.remove();
			continue outer;
		    }
		}
	    }
	    Class[] interfaceArray = new Class[interfaces.size()];
	    return (Class[]) interfaces.toArray(interfaceArray);
	}
	
	// TODO why not make this so that it throws ClassNoteFoundException and doesn't return null?
	// this is more standard behavior...
	public static Class<?> getClass(String className) {
		if (StringUtils.isEmpty(className)) {
			return null;
		}
		try {
			return ClassUtils.getClass(getDefaultClassLoader(), className);
		} catch (ClassNotFoundException e) {
			throw new RiceRuntimeException(e);
		}
	}
	
	public static <T> Class<? extends T> getClass(String className, Class<T> type) throws ClassNotFoundException {
		Class<?> theClass = ClassUtils.getClass(getDefaultClassLoader(), className);
		return theClass.asSubclass(type);
	}
	
}
