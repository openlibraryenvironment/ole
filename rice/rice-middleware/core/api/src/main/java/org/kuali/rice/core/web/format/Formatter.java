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
// begin Kuali Foundation modification
package org.kuali.rice.core.web.format;
// end Kuali Foundation modification

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.type.AbstractKualiDecimal;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.core.api.util.type.KualiPercent;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;



// begin Kuali Foundation modification
/**
 * This is the base class for all other Formatters.
 */
/**
 * It provides default formatting and conversion behavior for most value types, including primitives, arrays, and instances of most
 * {@link Collection}types. <code>Formatter</code> and its subclasses were designed primarily to be used by web app framework
 * components, though they can also be used in other contexts.
 * <p>
 * During request processing, the {@link PojoActionForm}uses <code>Formatter</code> instances to convert inbound request values
 * to JavaBean property types. Whenever a given value cannot be converted to its target type, the conversion method
 * {@link PropertyUtils#getProperty(Object, String)}throws a {@link FormatException}to signal this condition to the
 * calling code.
 * <p>
 * During the response phase, Struts tags make calls to the {@link PojoRequestProcessor}in order to access bean property values.
 * The <code>PojoRequestProcessor</code> then uses <code>Formatter</code> instances to format the bean values for presentation
 * in the user interface.
 * <p>
 * In either case, <code>Formatter</code> instances are obtained by calling {@link #getFormatter(Class)}, which looks in an
 * internal registry to determine which <code>Formatter</code> class to instantiate, and returns a new instance. The StrutsLive
 * framework includes a number of <code>Formatter</code> classes that are registered statically; additional
 * <code>Formatter classes can be registered at compile
 * time or at run time. 
 * <p>
 * Subclasses of <code>Formatter</code> typically override the callback methods
 * {@link #convertToObject(String)} and {@link #formatObject(Object)}, which
 * otherwise provide default conversion and formmating behavior needed for
 * atomic values (i.e., an ordinary bean property such as a <code>String</code>
 * or <code>Integer</code>, or else an element of a property typed as
 * array or Collection).
 *
 */
// end Kuali Foundation modification
public class Formatter implements Serializable {

	// begin Kuali Foundation modification
	// removed serialVersionUID and logger members
	// end Kuali Foundation modification
	
    static final String CREATE_MSG = "Couldn't create an instance of class ";
    // begin Kuali Foundation modification
    // registry changed from AppLocal instance to a Map
    private static Map registry = Collections.synchronizedMap(new HashMap());
    // end Kuali Foundation modification
    
    protected Map settings;
    
    // begin Kuali Foundation modification
    // removed keypath and rootObject variables
    // end Kuali Foundation modification
    
    protected Class propertyType;

    static { 
    	// begin Kuali Foundation modification
        registerFormatter(String.class, Formatter.class);
        registerFormatter(String[].class, Formatter.class);
        registerFormatter(AbstractKualiDecimal.class, BigDecimalFormatter.class);
        registerFormatter(KualiDecimal.class, CurrencyFormatter.class); 
        registerFormatter(KualiInteger.class, KualiIntegerCurrencyFormatter.class);
        registerFormatter(KualiPercent.class, PercentageFormatter.class);
        registerFormatter(BigDecimal.class, BigDecimalFormatter.class);
        registerFormatter(Date.class, DateFormatter.class);
        registerFormatter(Integer.class, IntegerFormatter.class);
        registerFormatter(int.class, IntegerFormatter.class);
        registerFormatter(int[].class, IntegerFormatter.class);
        registerFormatter(Boolean.class, BooleanFormatter.class);
        registerFormatter(Boolean.TYPE, BooleanFormatter.class);
        registerFormatter(boolean[].class, BooleanFormatter.class);
        registerFormatter(Long.class, LongFormatter.class);
        registerFormatter(Timestamp.class, DateViewTimestampObjectFormatter.class);
        registerFormatter(boolean.class, LittleBooleanFormatter.class);
        registerFormatter(Collection.class, ArrayFormatter.class);
        // end Kuali Foundation modification
    }

    public static Formatter getFormatter(Class aType) {
        return getFormatter(aType, null);
    }

    // begin Kuali Foundation modification
    // param aType was valueType, comment changes, major code changes
    /**
     * Returns an instance of the Formatter class to be used to format the provided value type.
     * 
     * @param type the class of the value to be formatted
     * @param settings parameters used by subclasses to customize behavior
     * @return an instance of Formatter or one of its subclasses
     */
    public static Formatter getFormatter(Class aType, Map settings) {
    	// original code: return createFormatter(formatterForType(valueType), valueType, settings);
			
        Class type = formatterForType(aType);
        Formatter formatter = null;
        try {
            formatter = (Formatter) type.newInstance();
        }
        catch (InstantiationException e) {
            throw new FormatException(CREATE_MSG + type, e);
        }
        catch (IllegalAccessException e) {
            throw new FormatException(CREATE_MSG + type, e);
        }

        if (settings != null)
            formatter.setSettings(Collections.unmodifiableMap(settings));
        formatter.propertyType = aType;

        return formatter;
    }

    // removed getFormatterByName, formatterClassForName, createFormatter methods
    // end Kuali Foundation modification

    /**
     * Binds the provided value type to a Formatter type. Note that a single Formatter class can be associated with more than one
     * type.
     * 
     * @param type a value type
     * @param formatterType a Formatter type
     */
    public static void registerFormatter(Class type, Class formatterType) {
        registry.put(type, formatterType);
    }

    /**
     * Returns <code>true</code> if the provided class is an array type, implements either the {@link List}or {@link Set}
     * interfaces, or is one of the Formatter classes currently registered.
     * 
     * @see registerFormatter(Class, Class)
     */
    public static boolean isSupportedType(Class type) {
        // begin Kuali Foundation modification
        if (type == null)
            return false;
        // end Kuali Foundation modification
        if (List.class.isAssignableFrom(type))
            return true;
        if (Set.class.isAssignableFrom(type))
            return true;

        return findFormatter(type) != null;
    }

    /**
     * Return the Formatter associated with the given type, by consulting an internal registry. Additional associations can be made
     * by calling {@link registerFormatter(Class, Class)}.
     * 
     * @return a new Formatter instance
     */
    public static Class formatterForType(Class type) {
        if (type == null)
            throw new IllegalArgumentException("Type can not be null");

        Class formatterType = findFormatter(type);

        return formatterType == null ? Formatter.class : formatterType;
    }

	// Kuali Foundation modification: comment removed
    public static Class findFormatter(Class type) {
    	// begin Kuali Foundation modification
        if (type == null)
            return null;

        if (registry.containsKey(type)) {
            return (Class) registry.get(type);
        }

        Map<Class<?>, Class<?>> formatsToRegister = new HashMap<Class<?>, Class<?>>();
        Iterator typeIter = registry.keySet().iterator();
        while (typeIter.hasNext()) {
            Class currType = (Class) typeIter.next();
            if (currType.isAssignableFrom(type)) {
                Class currFormatter = (Class) registry.get(currType);
                formatsToRegister.put(type, currFormatter);
                return currFormatter;
            }
        }
        for(Entry<Class<?>, Class<?>> entry : formatsToRegister.entrySet()) {
            registerFormatter(entry.getKey(), entry.getValue());
        }
        return null;
        // end Kuali Foundation modification
    }

	// begin Kuali Foundation modification
    public String getImplementationClass() {
        return this.getClass().getName();
    }
    // end Kuali Foundation modification

    public Class getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Class propertyType) {
        this.propertyType = propertyType;
    }

    public Map getSettings() {
        return settings;
    }

    public void setSettings(Map settings) {
        this.settings = settings;
    }

	// begin Kuali Foundation modification
	// removed getKeypath, setKeyPath, getRootObject, setRootObject, hasSettingForKey, settingForKey, typeForKey, getErrorKey
	// end Kuali Foundation modification
	
    /**
     * begin Kuali Foundation modification
     * Returns a String representation of the given value. May be overridden by subclasses to provide customized behavior for
     * different types, though generally the callback method {@link #format(Object)}provides a better customization hook.
     * <p>
     * Provides default handling for properties typed as array or Collection. Subclass implementations of this method must invoke
     * <code>super.formatForPresentation()</code> to take advantage of this built-in behavior.
     * <p>
     * Delegates to callback method {@link formatObject}for all other types. This method in turn invokes the callback method
     * <code>format</code>, which serves as an extension point for subclasses; the default implementation simply returns its
     * argument. Overriding <code>format</code> allows subclasses to take advantage of all of the array, primitive type, and
     * Collection handling functionality provided by the base class.
     * 
     * @param value the object to be formatted
     * @return a formatted string representation of the given object
     * @see #formatObject(Object)
     * end Kuali Foundation modification
     */
    public Object formatForPresentation(Object value) {
        if (isNullValue(value))
            return formatNull();

		// begin Kuali Foundation modification
		// removed code
		/*
	    // TODO: add registry for non-navigable classes so there's a way to
        // disable formatting selectively for given types contained in arrays
        // or Collections.
        if (Collection.class.isAssignableFrom(value.getClass()))
            return formatCollection((Collection) value);
        
        if (propertyType != null && propertyType.isArray())
            return formatArray(value);
		*/
		// end Kuali Foundation modification
		
        return formatObject(value);
    }

    /**
     * May be overridden by subclasses to provide special handling for <code>null</code> values when formatting a bean property
     * value for presentation. The default implementation simply returns <code>null</code>
     */
    protected Object formatNull() {
        return null;
    }

    /**
     * May be overridden by subclasses to provide custom formatting behavior. Provides default formatting implementation for
     * primitive types. (Note that primitive types are will always be wrapped in an array in order to be passed as an argument of
     * type <code>Object</code>).
     */
    public Object formatObject(Object value) {
        if (value == null)
            return formatNull();

        // Collections and arrays have already been handled at this point, so
        // if value is an array, assume it's a wrapper for a primitive type.
        Class<?> type = value.getClass();
        if (type.isArray())
        	// begin Kuali Foundation modification
            return ArrayUtils.toString(value, type.getComponentType());
            // end begin Kuali Foundation modification

        if (!(isSupportedType(value.getClass())))
            // begin Kuali Foundation modification
            formatBean(value);
            // end Kuali Foundation modification

        return format(value);
    }

    /**
     * If an element of the Collection isn't a supported type, assume it's a JavaBean, and format each of its properties. Returns a
     * Map containing the formatted properties keyed by property name.
     */
    protected Object formatBean(Object bean) {
        Map properties = null;
        try {
        	// begin Kuali Foundation modification
            properties = PropertyUtils.describe(bean);
            // end Kuali Foundation modification
        }
        catch (Exception e) {
            throw new FormatException("Unable to format values for bean " + bean, e);
        }

        Map formattedVals = new HashMap();
        // begin Kuali Foundation modification
        Iterator propIter = properties.entrySet().iterator();

        while (propIter.hasNext()) {
            Map.Entry entry = (Map.Entry) propIter.next();
            Object value = entry.getValue();
            if (value != null && isSupportedType(value.getClass())) {
                Formatter formatter = getFormatter(value.getClass());
                formattedVals.put(entry.getKey(), formatter.formatForPresentation(value));
            }
        }
        // end Kuali Foundation modification
        return formattedVals;
    }

    public Object format(Object value) {
        return value;
    }

    public Object formatArray(Object value) {
    	// begin Kuali Foundation modification
        Class elementType = value.getClass().getComponentType();
        if (!isSupportedType(elementType))
            return value;

        int length = Array.getLength(value);
        Object[] formattedVals = new String[length];

        for (int i = 0; i < length; i++) {
            Object element = Array.get(value, i);
            Object objValue = ArrayUtils.toObject(element);
            Formatter elementFormatter = getFormatter(elementType);
            formattedVals[i] = elementFormatter.formatForPresentation(objValue);
        }

        return formattedVals;
        // end Kuali Foundation modification
    }

    public Object formatCollection(Collection value) {
        List stringVals = new ArrayList();
        Iterator iter = value.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            Formatter formatter = getFormatter(obj.getClass());
            // begin Kuali Foundation modification
            stringVals.add(formatter.formatForPresentation(obj));
            // end Kuali Foundation modification
        }
        return stringVals.toArray();
    }

    /**
     * Returns an object representation of the provided string after first removing any extraneous formatting characters. If the
     * argument is a native array wrapping the actual value, the value is removed (unwrapped) from the array prior to invoking the
     * callback method {@link #convertToObject(String)}, which performs the actual conversion.
     * <p>
     * If the provided object is <code>null</code>, a blank <code>String</code>, or a <code>String[]</code> of length <b>0
     * </b> or that has <code>null</code> or a blank <code>String</code> in the first position, returns <code>null</code>.
     * Otherwise, If the destination property is a <code>Collection</code>, returns an instance of that type containing the
     * string values of the array elements.
     * <p>
     * If the provided object is an array, uses a Formatter corresponding to the array's component type to convert each of its
     * elements, and returns a new array containing the converted values.
     * 
     * May be overidden by subclasses to customize conversion, though ordinarily {@link #convertToObject(String)}is a better choice
     * since it takes advantage of <code>convertFromPresentationFormat</code>'s built-in behavior.
     * 
     * @param value the string value to be converted
     * @return the object value corresponding to the provided string value
     * @see convertToObject(String)
     */
    public Object convertFromPresentationFormat(Object value) {
        if (isEmptyValue(value))
            return getNullObjectValue();

        Class type = value.getClass();
        boolean isArray = propertyType != null && propertyType.isArray();
        boolean isCollection = propertyType != null && Collection.class.isAssignableFrom(propertyType);

        if (!(isArray || isCollection)) {
            value = unwrapString(value);
            return convertToObject((String) value);
        }

        String[] strings = type.isArray() ? (String[]) value : new String[] { (String) value };

        return isArray ? convertToArray(strings) : convertToCollection(strings);
    }

    /**
     * May be overridden by subclasses to provide special handling for <code>null</code> values when converting from presentation
     * format to a bean property type. The default implementation simply returns <code>null</code>
     */
    protected Object getNullObjectValue() {
        return null;
    }

    /**
     * May be orverridden by subclasses to customize its behavior. The default implementation simply trims and returns the provided
     * string.
     */
    protected Object convertToObject(String string) {
        return string == null ? null : string.replace( "\r\n", "\n" ).trim();        
    }

    /**
     * Converts an array of strings to a Collection type corresponding to the value of <code>propertyType</code>. Since we don't
     * have type information for the elements of the collection, no attempt is made to convert the elements from <code>String</code>
     * to other types. However, subclasses can override this method if they need to provide the ability to convert the elements to a
     * given type.
     */
    protected Collection convertToCollection(String[] strings) {
        Collection collection = null;
        Class type = propertyType;

        if (propertyType.isAssignableFrom(List.class))
            type = ArrayList.class;
        else if (propertyType.isAssignableFrom(Set.class))
            type = HashSet.class;

        try {
            collection = (Collection) type.newInstance();
        }
        catch (Exception e) {
            throw new FormatException(CREATE_MSG + propertyType, e);
        }

        for (int i = 0; i < strings.length; i++)
            collection.add(strings[i]);

        return collection;
    }

    /**
     * Converts an array of strings to an array of objects by calling {@link #convertToObject(String)}on each element of the
     * provided array in turn, using instances of a Formatter class that corresponds to this Formatter's property type.
     * 
     * @see #propertyType
     */
    protected Object convertToArray(String[] strings) {
        Class type = propertyType.getComponentType();
        // begin Kuali Foundation modification
        Formatter formatter = getFormatter(type);
        // end Kuali Foundation modification
        Object array = null;
        try {
            array = Array.newInstance(type, strings.length);
        }
        catch (Exception e) {
            throw new FormatException(CREATE_MSG + type, e);
        }

        for (int i = 0; i < strings.length; i++) {
            Object value = formatter.convertToObject(strings[i]);
            // begin Kuali Foundation modification
            ArrayUtils.setArrayValue(array, type, value, i);
            // end Kuali Foundation modification
        }

        return array;
    }

    public static String unwrapString(Object target) {

        if (target.getClass().isArray()) {
            String wrapper[] = (String[]) target;
            return wrapper.length > 0 ? wrapper[0] : null;
        }
		// begin Kuali Foundation modification
        // if target object is null, return a null String
        else if (target == null) {
            return new String();
        }

        // otherwise, return the string value of the object, with the hope
        // that the toString() has been meaningfully overriden
        else {
            return target.toString();
        }
        // end Kuali Foundation modification
    }

    public static boolean isNullValue(Object obj) {
        if (obj == null)
            return true;

		// begin Kuali Foundation modification
        if ((obj instanceof String) && StringUtils.isEmpty((String) obj))
            return true;
        // end Kuali Foundation modification

        return false;
    }

    public static boolean isEmptyValue(Object obj) {
        if (obj == null)
            return true;
        // begin Kuali Foundation modification
        if ((obj instanceof String) && StringUtils.isBlank((String) obj))
            return true;
        // end Kuali Foundation modification
        Class type = obj.getClass();
        if (type.isArray()) {
            Class compType = type.getComponentType();
            if (compType.isPrimitive())
                return false;
            if (((Object[]) obj).length == 0)
                return true;
            if (((Object[]) obj)[0] == null)
                return true;
            if (String.class.isAssignableFrom(compType)) {
            	// begin Kuali Foundation modification
                return StringUtils.isBlank(((String[]) obj)[0]);
                // end Kuali Foundation modification
            }
        }
        return false;
    }

    /**
     * begin Kuali Foundation modification
     * This class sets the value of an element of an array of primitives at the supplied index.
     * end Kuali Foundation modification
     * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    protected static final class ArrayUtils {

        private ArrayUtils() {
            throw new UnsupportedOperationException("do not call");
        }

        /**
         * Sets the value of an element of an array of primitives at the supplied index.
         *
         * @param array An array.
         * @param type The component type of the array.
         * @param index An array index.
         */
        public static void setArrayValue(Object array, Class type, Object value, int index) {
            if (!type.isPrimitive())
                Array.set(array, index, value);
            else if (type.isAssignableFrom(Boolean.TYPE))
                Array.setBoolean(array, index, (Boolean) value);
            else if (type.isAssignableFrom(Character.TYPE))
                Array.setChar(array, index, (Character) value);
            else if (type.isAssignableFrom(Byte.TYPE))
                Array.setByte(array, index, (Byte) value);
            else if (type.isAssignableFrom(Integer.TYPE))
                Array.setInt(array, index, (Integer) value);
            else if (type.isAssignableFrom(Short.TYPE))
                Array.setShort(array, index, (Short) value);
            else if (type.isAssignableFrom(Long.TYPE))
                Array.setLong(array, index, (Long) value);
            else if (type.isAssignableFrom(Float.TYPE))
                Array.setFloat(array, index, (Float) value);
            else if (type.isAssignableFrom(Double.TYPE))
                Array.setDouble(array, index, (Double) value);
        }

        public static Object toObject(Object value) {
            if (!value.getClass().isArray())
                return value;

            Class type = value.getClass().getComponentType();
            if (Array.getLength(value) == 0)
                return null;
            if (!type.isPrimitive())
                return Array.get(value, 0);
            if (boolean.class.isAssignableFrom(type))
                return Array.getBoolean(value, 0);
            if (char.class.isAssignableFrom(type))
                return new Character(Array.getChar(value, 0));
            if (byte.class.isAssignableFrom(type))
                return new Byte(Array.getByte(value, 0));
            if (int.class.isAssignableFrom(type))
                return new Integer(Array.getInt(value, 0));
            if (long.class.isAssignableFrom(type))
                return new Long(Array.getLong(value, 0));
            if (short.class.isAssignableFrom(type))
                return new Short(Array.getShort(value, 0));
            if (double.class.isAssignableFrom(type))
                return new Double(Array.getDouble(value, 0));
            if (float.class.isAssignableFrom(type))
                return new Float(Array.getFloat(value, 0));

            return null;
        }

        public static Object toString(Object array, Class type) {
            if (boolean.class.isAssignableFrom(type))
                return Boolean.toString(((boolean[]) array)[0]);
            if (char.class.isAssignableFrom(type))
                return Character.toString(((char[]) array)[0]);
            if (byte.class.isAssignableFrom(type))
                return Byte.toString(((byte[]) array)[0]);
            if (int.class.isAssignableFrom(type))
                return Integer.toString(((int[]) array)[0]);
            if (long.class.isAssignableFrom(type))
                return Long.toString(((long[]) array)[0]);
            if (short.class.isAssignableFrom(type))
                return Short.toString(((short[]) array)[0]);
            if (double.class.isAssignableFrom(type))
                return Double.toString(((double[]) array)[0]);
            if (float.class.isAssignableFrom(type))
                return Float.toString(((float[]) array)[0]);

            return ((String[]) array)[0];
        }
    }
}