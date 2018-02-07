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

import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.reflect.BaseTargetedInvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A Proxy that sets the thread Context ClassLoader before invocation of the
 * proxied object, and resets it back afterwards.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ContextClassLoaderProxy extends BaseTargetedInvocationHandler {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ContextClassLoaderProxy.class);

    /**
     * Convenience method that wraps a specified object with a ContextClassLoaderProxy, with a specified
     * handler classloader and proxy classloader.  If the specified object is null, or the object classloader
     * equals the proxy classloader, the object is returned unwrapped.
     * @param proxiedObject the object to proxy
     * @param proxyClassLoader the classloader OF THE PROXY INSTANCE
     * @param objectClassLoader the classloader to set as the context classloader prior to any invocations on the proxiedObject
     * @return a ContextClassLoaderProxy Proxy for the proxiedObject
     */
    public static Object wrap(Object proxiedObject, Class<?>[] classesToProxy, ClassLoader proxyClassLoader, ClassLoader objectClassLoader) {
        if (proxiedObject == null) {
        	return null;
        }
    	if (proxyClassLoader == null) {
            //proxyClassLoader = Thread.currentThread().getContextClassLoader();
        	proxyClassLoader = proxiedObject.getClass().getClassLoader();
        }
        if (objectClassLoader == null) {
            objectClassLoader = proxiedObject.getClass().getClassLoader();
        }
        if (classesToProxy == null) {
            classesToProxy = getInterfacesToProxy(proxyClassLoader, proxiedObject);
        }
        // this classloader comparison looks fishy
        // it is testing the classloader of the proxy against the intended *context* classloader of the proxiedObject
        // if these are the same the proxiedObject is not wrapped.  However, that implies that if the proxy
        // classloader may equal the intended context class loader, that the context class loader is actually not set,
        // and could theoretically NOT be the intended one in the future
        // somebody who understands this better than me should investigate this, and this method which I have
        // now applied to all prior uses of ContextClassLoaderProxy as a convenience
        //if (proxiedObject != null) { //&& !objectClassLoader.equals(proxyClassLoader)) {
        ContextClassLoaderProxy handler = new ContextClassLoaderProxy(objectClassLoader, proxiedObject);
        LOG.debug("Installed a ContextClassLoaderProxy on object: " + proxiedObject.getClass().getName());
        proxiedObject = Proxy.newProxyInstance(proxyClassLoader, classesToProxy, handler);
        //}

        return proxiedObject;
    }

    public static Object wrap(Object proxiedObject, ClassLoader proxyClassLoader, ClassLoader objectClassLoader) {
    	return wrap(proxiedObject, null, proxyClassLoader, objectClassLoader);
    }

    public static Object wrap(Object proxiedObject, ClassLoader classLoader) {
    	return wrap(proxiedObject, classLoader, classLoader);
    }

    public static Object wrap(Object proxiedObject, Class<?>[] classesToProxy) {
    	return wrap(proxiedObject, classesToProxy, null, null);
    }

    public static Object wrap(Object proxiedObject, Class<?>[] classesToProxy, ClassLoader classLoader) {
    	return wrap(proxiedObject, classesToProxy, classLoader, classLoader);
    }

    public static Object wrap(Object proxiedObject) {
    	return wrap(proxiedObject, null, null, null);
    }

    public static Class<?>[] getInterfacesToProxy(Object proxiedObject) {
	return getInterfacesToProxy(null, proxiedObject);
    }

    /**
     * Determines the interfaces which need to be proxied and are visable to the given proxy ClassLoader.
     */
    public static Class<?>[] getInterfacesToProxy(ClassLoader proxyClassLoader, Object proxiedObject) {
    	return ClassLoaderUtils.getInterfacesToProxy(proxiedObject, proxyClassLoader, null);
    }

    private ClassLoader classLoader;

    public ContextClassLoaderProxy(ClassLoader classLoader, Object target) {
    	super(target);
        this.classLoader = classLoader;
    }

    protected Object invokeInternal(Object proxy, Method m, Object[] args) throws Throwable {
        ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            return m.invoke(getTarget(), args);
        } catch (InvocationTargetException e) {
        	throw (e.getCause() != null ? e.getCause() : e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldCl);
        }
    }
}
