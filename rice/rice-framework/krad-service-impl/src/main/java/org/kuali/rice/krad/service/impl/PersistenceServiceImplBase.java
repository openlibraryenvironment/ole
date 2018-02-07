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
package org.kuali.rice.krad.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.kuali.rice.krad.exception.IntrospectionException;

public class PersistenceServiceImplBase extends PersistenceServiceStructureImplBase {

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataService#getPrimaryKeyFields(java.lang.Object)
	 */
	public Map getPrimaryKeyFieldValues(Object persistableObject) {
		return getPrimaryKeyFieldValues(persistableObject, false);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataService#getPrimaryKeyFields(java.lang.Object,
	 *      boolean)
	 */
	public Map getPrimaryKeyFieldValues(Object persistableObject, boolean sortFieldNames) {
		if (persistableObject == null) {
			throw new IllegalArgumentException("invalid (null) persistableObject");
		}

		Map keyValueMap = null;
		if (sortFieldNames) {
			keyValueMap = new TreeMap();
		} else {
			keyValueMap = new HashMap();
		}

		String className = null;
		String fieldName = null;
		try {
			List fields = listPrimaryKeyFieldNames(persistableObject.getClass());
			for (Iterator i = fields.iterator(); i.hasNext();) {
				fieldName = (String) i.next();
				className = persistableObject.getClass().getName();
				Object fieldValue = PropertyUtils.getSimpleProperty(persistableObject, fieldName);

				keyValueMap.put(fieldName, fieldValue);
			}
		} catch (IllegalAccessException e) {
			throw new IntrospectionException("problem accessing property '" + className + "." + fieldName + "'", e);
		} catch (NoSuchMethodException e) {
			throw new IntrospectionException("unable to invoke getter for property '" + className + "." + fieldName + "'", e);
		} catch (InvocationTargetException e) {
			throw new IntrospectionException("problem invoking getter for property '" + className + "." + fieldName + "'", e);
		}

		return keyValueMap;
	}

}
