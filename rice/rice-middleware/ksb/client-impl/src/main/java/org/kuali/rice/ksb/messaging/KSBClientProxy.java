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
package org.kuali.rice.ksb.messaging;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * This class creates a proxy for services deployed on KSB. A 
 * reference to the service is obtained only upon the first method
 * invocation.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KSBClientProxy implements InvocationHandler {

private static final Logger LOG = Logger.getLogger(KSBClientProxy.class);
    
    private QName serviceName;
    private volatile Object service;


    public static <T> T newInstance(String serviceQName, Class<T> interfaceClass) throws InstantiationException, IllegalAccessException {
        if (StringUtils.isBlank(serviceQName)) {
            throw new IllegalArgumentException("the qname was blank");
        }

        if (interfaceClass == null) {
            throw new IllegalArgumentException("the interfaceClass was null");
        }

        @SuppressWarnings("unchecked")
        final T t = (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass }, new KSBClientProxy(serviceQName));
        return t;
    }

    public KSBClientProxy(String serviceQName){
        if (StringUtils.isBlank(serviceQName)) {
            throw new IllegalArgumentException("the qname was blank");
        }

        this.serviceName = QName.valueOf(serviceQName);
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //using DCL idiom
        //see effective java 2nd ed. pg. 71
        Object s = service;
        if (s == null) {
            synchronized (this) {
                s = service;
                if (s == null) {
                    service = s = GlobalResourceLoader.getService(serviceName);
                }
            }
        }

        if (s != null) {
            try {
                return method.invoke(s, args);
            } catch (InvocationTargetException e) {
                throw ExceptionUtils.getRootCause(e);
            }
        }

        LOG.warn("serviceName: " + serviceName + " was not found");
        return null;
    }
}
