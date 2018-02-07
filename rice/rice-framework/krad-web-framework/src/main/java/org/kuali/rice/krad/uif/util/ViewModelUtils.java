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
package org.kuali.rice.krad.uif.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.view.View;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.TypedStringValue;

import java.util.Collection;
import java.util.Map;

/**
 * Provides methods for getting property values, types, and paths within the
 * context of a <code>View</code>
 *
 * <p>
 * The view provides a special map named 'abstractTypeClasses' that indicates
 * concrete classes that should be used in place of abstract property types that
 * are encountered on the object graph. This classes takes into account that map
 * while dealing with properties. e.g. suppose we have propertyPath
 * 'document.name' on the form, with the type of the document property set to
 * the interface Document. Using class introspection we would get back the
 * interface type for document and this would not be able to get the property
 * type for name. Using the view map, we can replace document with a concrete
 * class and then use it to get the name property
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewModelUtils {

    /**
     * Determines the associated type for the property within the View context
     *
     * <p>
     * Property path is full path to property from the View Form class. The abstract type classes
     * map configured on the View will be consulted for any entries that match the property path. If the
     * property path given contains a partial match to an abstract class (somewhere on path is an abstract
     * class), the property type will be retrieved based on the given concrete class to use and the part
     * of the path remaining. If no matching entry is found, standard reflection is used to get the type
     * </p>
     *
     * @param view view instance providing the context (abstract map)
     * @param propertyPath full path to property to retrieve type for (relative to the form class)
     * @return Class<?> type of property in model, or Null if type could not be determined
     * @see org.kuali.rice.krad.uif.view.View#getObjectPathToConcreteClassMapping()
     */
    public static Class<?> getPropertyTypeByClassAndView(View view, String propertyPath) {
        Class<?> propertyType = null;

        if (StringUtils.isBlank(propertyPath)) {
            return propertyType;
        }

        // in case of partial match, holds the class that matched and the
        // property so we can get by reflection
        Class<?> modelClass = view.getFormClass();
        String modelProperty = propertyPath;

        int bestMatchLength = 0;

        // removed collection indexes from path for matching
        String flattenedPropertyPath = propertyPath.replaceAll("\\[.+\\]", "");

        // check if property path matches one of the modelClass entries
        Map<String, Class<?>> modelClasses = view.getObjectPathToConcreteClassMapping();
        for (String path : modelClasses.keySet()) {
            // full match
            if (StringUtils.equals(path, flattenedPropertyPath)) {
                propertyType = modelClasses.get(path);
                break;
            }

            // partial match
            if (flattenedPropertyPath.startsWith(path) && (path.length() > bestMatchLength)) {
                bestMatchLength = path.length();

                modelClass = modelClasses.get(path);
                modelProperty = StringUtils.removeStart(flattenedPropertyPath, path);
                modelProperty = StringUtils.removeStart(modelProperty, ".");
            }
        }

        // if full match not found, get type based on reflection
        if (propertyType == null) {
            propertyType = ObjectPropertyUtils.getPropertyType(modelClass, modelProperty);
        }

        return propertyType;
    }

    /**
     * Gets the parent object path of the data field
     *
     * @param field
     * @return parent object path
     */
    public static String getParentObjectPath(DataField field) {
        String parentObjectPath = "";

        String objectPath = field.getBindingInfo().getBindingObjectPath();
        String propertyPrefix = field.getBindingInfo().getBindByNamePrefix();

        if (!field.getBindingInfo().isBindToForm() && StringUtils.isNotBlank(objectPath)) {
            parentObjectPath = objectPath;
        }

        if (StringUtils.isNotBlank(propertyPrefix)) {
            if (StringUtils.isNotBlank(parentObjectPath)) {
                parentObjectPath += ".";
            }

            parentObjectPath += propertyPrefix;
        }

        return parentObjectPath;
    }

    /**
     * Determines the associated type for the property within the View context
     *
     * <p>
     * The abstract type classes map configured on the View will be consulted for any entries that match
     * the property path. If the parent object path for the given field contains a partial match to an
     * abstract class (somewhere on path is an abstract class), the property type will be retrieved based
     * on the given concrete class to use and the part of the path remaining. If no matching entry is found,
     * standard reflection is used to get the type
     * </p>
     *
     * @param view view instance providing the context (abstract map)
     * @param field field to retrieve type for
     * @return Class<?> type of property in model, or Null if type could not be determined
     * @see org.kuali.rice.krad.uif.view.View#getObjectPathToConcreteClassMapping()
     */
    public static Class<?> getParentObjectClassForMetadata(View view, DataField field) {
        String parentObjectPath = getParentObjectPath(field);

        return getPropertyTypeByClassAndView(view, parentObjectPath);
    }

    /**
     * Determines the associated type for the property within the View context
     *
     * <p>
     * If the parent object instance is not null, get the class through it.  Otherwise, use the following logic:
     * The abstract type classes map configured on the View will be consulted for any entries that match
     * the property path. If the parent object path for the given field contains a partial match to an
     * abstract class (somewhere on path is an abstract class), the property type will be retrieved based
     * on the given concrete class to use and the part of the path remaining. If no matching entry is found,
     * standard reflection is used to get the type
     * </p>
     *
     * @param view view instance providing the context (abstract map)
     * @param model object model
     * @param field field to retrieve type for
     * @return the class of the object instance if not null or the type of property in model, or Null if type could not be determined
     * @see org.kuali.rice.krad.uif.view.View#getObjectPathToConcreteClassMapping()
     */
    public static Class<?> getParentObjectClassForMetadata(View view, Object model, DataField field) {
        String parentObjectPath = getParentObjectPath(field);

        return getObjectClassForMetadata(view, model, parentObjectPath);
    }

    /**
     * Determines the associated type for the property within the View context
     *
     * <p>
     * If the parent object instance is not null, get the class through it.  Otherwise, use the following logic:
     * The abstract type classes map configured on the View will be consulted for any entries that match
     * the property path. If the parent object path for the given field contains a partial match to an
     * abstract class (somewhere on path is an abstract class), the property type will be retrieved based
     * on the given concrete class to use and the part of the path remaining. If no matching entry is found,
     * standard reflection is used to get the type
     * </p>
     *
     * @param view view instance providing the context (abstract map)
     * @param model object model
     * @param propertyPath full path to property to retrieve type for (relative to the form class)
     * @return the class of the object instance if not null or the type of property in model, or Null if type could not be determined
     * @see org.kuali.rice.krad.uif.view.View#getObjectPathToConcreteClassMapping()
     */
    public static Class<?> getObjectClassForMetadata(View view, Object model, String propertyPath) {
        // get class by object instance if not null
        Object parentObject = ObjectPropertyUtils.getPropertyValue(model, propertyPath);
        if (parentObject != null) {
            return parentObject.getClass();
        }

        // get class by property type with abstract map check
        return getPropertyTypeByClassAndView(view, propertyPath);
    }

    /**
     * Retrieves the parent object if it exists or attempts to create a new instance
     *
     * @param view view instance providing the context (abstract map)
     * @param model object model
     * @param field field to retrieve type for
     * @return the class of the object instance if not null or the type of property in model, or Null if type could not be determined
     * @see org.kuali.rice.krad.uif.view.View#getObjectPathToConcreteClassMapping()
     */
    public static Object getParentObjectForMetadata(View view, Object model, DataField field) {
        // default to model as parent
        Object parentObject = model;

        String parentObjectPath = getParentObjectPath(field);
        if (StringUtils.isNotBlank(parentObjectPath)) {
            parentObject = ObjectPropertyUtils.getPropertyValue(model, parentObjectPath);

            // attempt to create new instance if parent is null or is a
            // collection or map
            if ((parentObject == null) || Collection.class.isAssignableFrom(parentObject.getClass()) ||
                    Map.class.isAssignableFrom(parentObject.getClass())) {
                try {
                    Class<?> parentObjectClass = getPropertyTypeByClassAndView(view, parentObjectPath);
                    if (parentObjectClass != null) {
                        parentObject = parentObjectClass.newInstance();
                    }
                } catch (InstantiationException e) {
                    // swallow exception and let null be returned
                } catch (IllegalAccessException e) {
                    // swallow exception and let null be returned
                }
            }
        }

        return parentObject;
    }

    /**
     * Helper method for getting the string value of a property from a {@link PropertyValues}
     *
     * @param propertyValues property values instance to pull from
     * @param propertyName name of property whose value should be retrieved
     * @return String value for property or null if property was not found
     */
    public static String getStringValFromPVs(PropertyValues propertyValues, String propertyName) {
        String propertyValue = null;

        if ((propertyValues != null) && propertyValues.contains(propertyName)) {
            Object pvValue = propertyValues.getPropertyValue(propertyName).getValue();
            if (pvValue instanceof TypedStringValue) {
                TypedStringValue typedStringValue = (TypedStringValue) pvValue;
                propertyValue = typedStringValue.getValue();
            } else if (pvValue instanceof String) {
                propertyValue = (String) pvValue;
            }
        }

        return propertyValue;
    }
}
