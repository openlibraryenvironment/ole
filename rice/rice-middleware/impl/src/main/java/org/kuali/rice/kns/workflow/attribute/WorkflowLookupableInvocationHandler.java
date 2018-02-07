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
package org.kuali.rice.kns.workflow.attribute;

import java.lang.reflect.Method;
import java.util.HashMap;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import org.kuali.rice.core.web.format.BooleanFormatter;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * This class provides access to the properties of business objects returned as search results by the WorkflowLookupableImpl.
 * 
 * 
 * @see org.kuali.rice.kew.attribute.WorkflowLookupableImpl
 * @deprecated This will go away once workflow supports simple url integration for custom attribute lookups.
 */
public class WorkflowLookupableInvocationHandler implements InvocationHandler {
    private BusinessObject proxiedBusinessObject;
    private ClassLoader classLoader;

    private String returnUrl;

    /**
     * Constructs a WorkflowLookupableInvocationHandler.java.
     * 
     * @param proxiedBusinessObject The BusinessObject that this instance is providing access to.
     */
    public WorkflowLookupableInvocationHandler(BusinessObject proxiedBusinessObject, ClassLoader classLoader) {
        this.proxiedBusinessObject = proxiedBusinessObject;
        this.classLoader = classLoader;
    }

    /**
     * Constructs a WorkflowLookupableInvocationHandler.java.
     * 
     * @param proxiedBusinessObject The BusinessObject that this instance is providing access to.
     * @param returnUrl The returnUrl String for selection of a result from the UI
     */
    public WorkflowLookupableInvocationHandler(BusinessObject proxiedBusinessObject, String returnUrl, ClassLoader classLoader) {
        this.proxiedBusinessObject = proxiedBusinessObject;
        this.returnUrl = returnUrl;
        this.classLoader = classLoader;
    }

    /**
     * This method intercepts "getReturnUrl" and returns this objects returnUrl attribute. It proxies access to nested
     * BusinessObjects to ensure that the application plugin classloader is used to resolve OJB proxies. And, it translates booleans
     * for the UI, using the BooleanFormatter.
     * 
     * @see net.sf.cglib.proxy.InvocationHandler#invoke(java.lang.Object proxy, java.lang.reflect.Method method, java.lang.Object[]
     *      args)
     * 
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ClassLoader oldClassloader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            if ("getReturnUrl".equals(method.getName())) {
                return returnUrl;
            }
            else if ("getWorkflowLookupableResult".equals(method.getName())) {
                return Enhancer.create(HashMap.class, new Class[] { WorkflowLookupableResult.class }, this);
            }
            else if ("get".equals(method.getName())) {
                Object propertyValue = ObjectUtils.getNestedValue(proxiedBusinessObject, args[0].toString());
                if (propertyValue instanceof BusinessObject) {
                    return Enhancer.create(propertyValue.getClass(), new WorkflowLookupableInvocationHandler((BusinessObject) propertyValue, classLoader));
                }
                else {
                    if (propertyValue instanceof Boolean) {
                        return new BooleanFormatter().format(propertyValue);
                    }
                    return propertyValue;
                }
            }
            else {
                return method.invoke(proxiedBusinessObject, args);
            }
        }
        catch (Exception e) {
            throw (e.getCause() != null ? e.getCause() : e);
        }
        finally {
            Thread.currentThread().setContextClassLoader(oldClassloader);
        }
    }
}
