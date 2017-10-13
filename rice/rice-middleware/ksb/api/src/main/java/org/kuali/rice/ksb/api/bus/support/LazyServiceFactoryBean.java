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
package org.kuali.rice.ksb.api.bus.support;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoaderException;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.InitializingBean;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Loads a lazy proxy to a service from the {@link org.kuali.rice.ksb.api.bus.ServiceBus}.  This proxy is created based on either the
 * proxy interfaces that are injected into this bean, or derived from the objectType which is injected into this bean.
 * If neither of these are injected, then an exception will be through during bean initialization.
 *
 * The attempt to fetch the resource from the ServiceBus won't be attempted until a method on the resulting
 * proxy is invoked.  If it fails to locate the resource, it will throw a ResourceLoaderException indicating the service
 * could not be loaded.
 *
 * <p>This allows for referencing of a potentially remote service in the spring context during startup which won't get
 * used until after startup.  If the remote service gets used *during* startup, then it must be available from the GRL
 * during startup or else the ResourceLoaderException will be thrown.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class LazyServiceFactoryBean implements FactoryBean<Object>, InitializingBean {

    private String serviceNamespace;
	private String serviceName;
    private String applicationId;
    private Class<?> objectType;
    private Class<?>[] proxyInterfaces;
    private Object proxyObject;

	public LazyServiceFactoryBean() {
        this.objectType = Object.class;
        this.proxyInterfaces = null;
	}

    public void afterPropertiesSet() throws Exception {
        if (ArrayUtils.isEmpty(getProxyInterfaces())) {
            setProxyInterfaces(detectProxyInterfaces());
            if (ArrayUtils.isEmpty(getProxyInterfaces())) {
                throw new FactoryBeanNotInitializedException("Failed to initialize factory bean because " +
                        "proxyInterfaces were not injected or could not be derived from object type.");
            }
        }
        this.proxyObject = Proxy.newProxyInstance(getClass().getClassLoader(), getProxyInterfaces(),
                new LazyInvocationHandler());
	}

    protected Class<?>[] detectProxyInterfaces() {
        Class<?> type = getObjectType();
        if (type != null && type.isInterface()) {
            return new Class<?>[] {type};
        } else if (type != null) {
            return type.getInterfaces();
        } else {
            return null;
        }
    }

    @Override
    public Object getObject() throws Exception {
        return this.proxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        return this.objectType;
    }

    public void setObjectType(Class<?> objectType) {
        this.objectType = objectType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getServiceNamespace() {
        return serviceNamespace;
    }

    public void setServiceNamespace(String serviceNamespace) {
        this.serviceNamespace = serviceNamespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Class<?>[] getProxyInterfaces() {
        return proxyInterfaces;
    }

    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

    private class LazyInvocationHandler implements InvocationHandler {
        private volatile Object service = null;
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                if (service == null) {
                    QName name = new QName(getServiceNamespace(), getServiceName());
                    if (StringUtils.isNotBlank(getApplicationId())) {
                        service = KsbApiServiceLocator.getServiceBus().getService(name, getApplicationId());
                    } else {
                        service = KsbApiServiceLocator.getServiceBus().getService(name);
                    }
                    if (service == null) {
                        throw new ResourceLoaderException("Failed to locate resource with name: " + name);
                    }
                }
                return method.invoke(service, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

    }

}
