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
package org.kuali.rice.core.api.mo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.util.collect.CollectionUtils;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * All model object's that are Jaxb annotated should extend this class.
 *
 * This class does several important things:
 * <ol>
 *     <li>Defines jaxb callback method to ensure that Collection and Map types are unmarshalled into immutable empty forms rather than null values</li>
 *     <li>Defines equals/hashcode/toString</li>
 *
 *     Note: the equals/hashCode implementation excludes {@value CoreConstants.CommonElements#FUTURE_ELEMENTS} field.
 *     This element should be present on all jaxb annotated classes.
 * </ol>
 *
 * <b>Important: all classes extending this class must be immutable</b>
 */
@XmlTransient // marked as @XmlTransient so that an AbstractDataTransferObjectType is not included in all WSDL schemas
public abstract class AbstractDataTransferObject implements ModelObjectComplete {

    private transient volatile Integer _hashCode;
    private transient volatile String _toString;

    protected AbstractDataTransferObject() {
        super();
    }

    /**
     * compute or return the memoized hashcode for this immutable class, excluding the named fields.
     * @param excludedFields the names of fields to exclude when computing the hashcode.
     * @return the hashcode value
     * @see #equalsExcludeFields(Object, java.util.Collection)
     * @see #getDefaultHashCodeEqualsExcludeFields()
     */
    protected final int hashCodeExcludeFields(Collection<String> excludedFields) {
        //using DCL idiom to cache hashCodes.  Hashcodes on immutable objects never change.  They can be safely cached.
        //see effective java 2nd ed. pg. 71
        Integer h = _hashCode;
        if (h == null) {
            synchronized (this) {
                h = _hashCode;
                if (h == null) {
                    _hashCode = h = Integer.valueOf(HashCodeBuilder.reflectionHashCode(this, excludedFields));
                }
            }
        }

        return h.intValue();
    }

    @Override
    public int hashCode() {
        return hashCodeExcludeFields(Constants.hashCodeEqualsExclude);
    }

    /**
     * Indicates whether the obj parameter is equal to this object.  Uses {@link org.apache.commons.lang.builder.EqualsBuilder#reflectionEquals(Object, Object, java.util.Collection)}
     * and takes the names of fields to exclude from comparison.
     * @param obj the other object to compare to
     * @param excludedFields the names of fields to exclude when computing the hashcode.
     * @return if equal
     * @see #hashCodeExcludeFields(java.util.Collection)
     * @see #getDefaultHashCodeEqualsExcludeFields()
     */
    protected final boolean equalsExcludeFields(Object obj, Collection<String> excludedFields) {
        return EqualsBuilder.reflectionEquals(obj, this, excludedFields);
    }

    @Override
    public boolean equals(Object obj) {
        return equalsExcludeFields(obj, Constants.hashCodeEqualsExclude);
    }

    @Override
    public String toString() {
        //using DCL idiom to cache toString.  toStrings on immutable objects never change.  They can be safely cached.
        //see effective java 2nd ed. pg. 71
        String t = _toString;
        if (t == null) {
            synchronized (this) {
                t = _toString;
                if (t == null) {
                    _toString = t = ToStringBuilder.reflectionToString(this);
                }
            }
        }

        return t;
    }

    @SuppressWarnings("unused")
    protected void beforeUnmarshal(Unmarshaller u, Object parent) throws Exception {
    }

    @SuppressWarnings("unused")
    protected void afterUnmarshal(Unmarshaller u, Object parent) throws Exception {
        CollectionUtils.makeUnmodifiableAndNullSafe(this);
    }

    private transient Object serializationMutex = new Object();

    private void writeObject(ObjectOutputStream out) throws IOException {
        synchronized (serializationMutex) {
            clearFutureElements();
            out.defaultWriteObject();
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        ois.defaultReadObject();
        serializationMutex = new Object();
    }

    /**
     * Looks for a field named "_futureElements" on the class and clears it's value if it exists.  This allows us to
     * prevent from storing these values during serialization.
     */
    private void clearFutureElements() {
        try {
            Field futureElementsField = getClass().getDeclaredField(CoreConstants.CommonElements.FUTURE_ELEMENTS);
            boolean originalAccessible = futureElementsField.isAccessible();
            futureElementsField.setAccessible(true);
            try {
                futureElementsField.set(this, null);
            } finally {
                futureElementsField.setAccessible(originalAccessible);
            }
        } catch (NoSuchFieldException e) {
            // if the field does not exist, don't do anything
        } catch (IllegalAccessException e) {
            // can't modify the field, ignore
        }
    }


    /**
     * Defines some internal constants used on this class.
     */
    protected static class Constants {
        final static Collection<String> hashCodeEqualsExclude = Collections.unmodifiableCollection(
                Arrays.asList(CoreConstants.CommonElements.FUTURE_ELEMENTS, "_hashCode", "_toString")
        );
    }

    protected static final Collection<String> getDefaultHashCodeEqualsExcludeFields() {
        return Constants.hashCodeEqualsExclude;
    }
}
