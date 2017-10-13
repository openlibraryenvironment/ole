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
package org.kuali.rice.core.framework.resourceloader;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.reflect.DataDefinition;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.reflect.PropertyDefinition;
import org.kuali.rice.core.api.resourceloader.ResourceLoaderException;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.ContextClassLoaderBinder;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Resolves object definitions into java Objects that are wrapped in a proxy whose classloader is the current
 * context classloader.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ObjectDefinitionResolver {

	private static final Logger LOG = Logger.getLogger(ObjectDefinitionResolver.class);

	/**
	 * Wraps the given object in a proxy which switches the context classloader appropriately.  The classloader
	 * of this resource loader is used.
	 */
	public static Object wrap(Object object) {
		return wrap(object, ClassLoaderUtils.getDefaultClassLoader());
	}

	/**
	 * Wraps the given object in a proxy which switches the context classloader appropriately.  The given classloader
	 * is used as the context classloader.
	 */
	public static Object wrap(Object object, ClassLoader classLoader) {
//		return ContextClassLoaderProxy.wrap(object, classLoader);
		return object;
	}

	public static Object createObject(String className, ClassLoader classLoader) {
		return createObject(new ObjectDefinition(className), classLoader, true);
	}

	public static Object createObject(final ObjectDefinition definition, final ClassLoader classLoader, final boolean wrap) {
		Object result = null;
		final String className = definition.getClassName();

        try {
            result = ContextClassLoaderBinder.doInContextClassLoader(classLoader, new Callable() {
                public Object call() throws Exception {
                    Object object = null;
                    Class<?>[] constructorParamTypes = buildConstructorParamTypes(definition.getConstructorParameters());
                    Object[] constructorParams = buildConstructorParams(definition.getConstructorParameters());
                    try {
                        Class<?> objectClass = Class.forName(className, true, classLoader);
                        object = loadObject(objectClass, constructorParams, constructorParamTypes);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                    invokeProperties(object, definition.getProperties());
                    if (wrap) {
                        return wrap(object, classLoader);
                    }
                    return object;

                }
            });
        } catch (Exception e) {
            handleException(className, e);
        }

		return result;
	}

	protected static Object loadObject(Class<?> objectClass, Object[] constructorParams, Class<?>[] constructorParamTypes) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return ConstructorUtils.invokeConstructor(objectClass, constructorParams, constructorParamTypes);
	}

	private static void handleException(String className, Exception e) {
		if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		}
		throw new ResourceLoaderException("Could not materialize object from definition, using classname: " + className, e);
	}

	protected static Class<?>[] buildConstructorParamTypes(List<DataDefinition> constructorParameters) {
		Class<?>[] params = new Class[constructorParameters.size()];
		int index = 0;
		for (Iterator iterator = constructorParameters.iterator(); iterator.hasNext();) {
			DataDefinition dataDef = (DataDefinition) iterator.next();
			params[index++] = dataDef.getType();
		}
		return params;
	}

	protected static Object[] buildConstructorParams(List<DataDefinition> constructorParameters) {
		Object[] params = new Object[constructorParameters.size()];
		int index = 0;
		for (Iterator iterator = constructorParameters.iterator(); iterator.hasNext();) {
			DataDefinition dataDef = (DataDefinition) iterator.next();
			params[index++] = dataDef.getValue();
		}
		return params;
	}

	protected static void invokeProperties(Object object, Collection<PropertyDefinition> properties) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			PropertyDefinition propertyDef = (PropertyDefinition) iterator.next();
			invokeProperty(object, propertyDef);
		}
	}

	protected static void invokeProperty(Object object, PropertyDefinition definition) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		PropertyUtils.setProperty(object, definition.getName(), definition.getData().getValue());
	}


}
