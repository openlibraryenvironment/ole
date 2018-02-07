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
package org.kuali.rice.core.api.uif;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = RemotableAttributeError.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RemotableAttributeError.Constants.TYPE_NAME,
        propOrder = {RemotableAttributeError.Elements.ATTRIBUTE_NAME, RemotableAttributeError.Elements.ERRORS,
                CoreConstants.CommonElements.FUTURE_ELEMENTS})
public final class RemotableAttributeError extends AbstractDataTransferObject implements RemotableAttributeErrorContract {

    @XmlElement(name = Elements.ATTRIBUTE_NAME, required = false)
    private final String attributeName;
    @XmlElementWrapper(name = Elements.ERRORS, required = true )
    @XmlElement(name = Elements.ERROR, required = false)
    private final List<String> errors;
    @SuppressWarnings("unused") @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private RemotableAttributeError() {
        this.attributeName = null;
        this.errors = null;
    }

    private RemotableAttributeError(Builder builder) {
        this.attributeName = builder.getAttributeName();
        this.errors = builder.getErrors();
    }

    @Override
    public String getAttributeName() {
        return this.attributeName;
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }

    @Override
    public String getMessage() {
        return Joiner.on(", ").skipNulls().join(this.getErrors());
    }

    @Override
    public String toString() {
        return getAttributeName() + ": " + this.getMessage();
    }

    /**
     * Utility method to search a collection of attribute errors and check in the collection
     * contains a error for a give attribute name.
     *
     * @param attributeName the name of the attribute to search for.  Cannot be blank or null.
     * @param errors cannot be null.
     *
     * @return true if list contains an error class for attribute name.
     */
    public static boolean containsAttribute(String attributeName, Collection<RemotableAttributeError> errors) {
        if (StringUtils.isBlank(attributeName)) {
            throw new IllegalArgumentException("attributeName is blank");
        }

        if (errors == null) {
            throw new IllegalArgumentException("errors is null");
        }

        for (RemotableAttributeErrorContract error : errors) {
            if (attributeName.equals(error.getAttributeName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Utility method that iterates over a collection of AttributeErrors searching for multiple
     * AttributeError instances for the same attributeName.  If found those instances are combined.
     *
     * @param errors the errors.  cannot be null
     * @return a normalized list
     */
    public static List<RemotableAttributeError> normalizeRemotableAttributes(List<RemotableAttributeError> errors) {
        if (errors == null) {
            throw new IllegalArgumentException("errors is null");
        }

        final List<RemotableAttributeError> normalized = new ArrayList<RemotableAttributeError>();
        final Map<String, Integer> map = new HashMap<String, Integer>();

        for (final RemotableAttributeError error :  errors) {
            final Integer prevIndex = map.get(error.getAttributeName());
            if (prevIndex == null) {
                final int insertIdx = normalized.size();
                map.put(error.getAttributeName(), insertIdx);
                normalized.add(insertIdx, error);
            } else {
                final RemotableAttributeError prevError = normalized.get(prevIndex.intValue());
                final RemotableAttributeError.Builder b  = RemotableAttributeError.Builder.create(error.getAttributeName(), error.getErrors());
                b.addErrors(prevError.getErrors());
                normalized.set(prevIndex.intValue(), b.build());
            }
        }
        return normalized;
    }

    /**
     * A builder which can be used to construct {@link RemotableAttributeError} instances.  Enforces the constraints of
     * the {@link RemotableAttributeErrorContract}.
     */
    public static final class Builder implements Serializable, ModelBuilder, RemotableAttributeErrorContract {

        private String attributeName;
        private List<String> errors = new ArrayList<String>();

        private Builder(String attributeName) {
            if (StringUtils.isBlank(attributeName)) {
                throw new IllegalArgumentException("attributeName was null");
            }
            this.attributeName = attributeName;
        }

        public static Builder create(String attributeName) {
            return new Builder(attributeName);
        }

        public static Builder create(String attributeName, List<String> errors) {
            Builder b = new Builder(attributeName);
            b.setErrors(errors);
            return b;
        }

        public static Builder create(String attributeName, String... errors) {
            Builder b = new Builder(attributeName);
            b.addErrors(errors);
            return b;
        }

        public static Builder create(RemotableAttributeErrorContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getAttributeName());
            builder.setErrors(contract.getErrors());
            return builder;
        }

        public RemotableAttributeError build() {
            if (errors.isEmpty()) {
                throw new IllegalStateException("must contain at least one error");
            }

            for (String err : errors) {
                if (StringUtils.isBlank(err)) {
                    throw new IllegalStateException("contains a blank error");
                }
            }

            return new RemotableAttributeError(this);
        }

        @Override
        public String getAttributeName() {
            return this.attributeName;
        }

        @Override
        public List<String> getErrors() {
            return Collections.unmodifiableList(this.errors);
        }

        @Override
        public String getMessage() {
            return Joiner.on(", ").skipNulls().join(this.getErrors());
        }

        @Override
        public String toString() {
            return this.getAttributeName() + ": " + this.getMessage();
        }

        public void setAttributeName(String attributeName) {
            if (StringUtils.isBlank(attributeName)) {
                throw new IllegalArgumentException("attributeName is blank");
            }

            this.attributeName = attributeName;
        }

        public void setErrors(List<String> errors) {
            if (errors == null) {
                throw new IllegalArgumentException("errors is null");
            }

            this.errors = new ArrayList<String>(errors);
        }

        /**
         * Adds errors to the AttributeError.  The passed in errors cannot be null.
         *
         * @param errors any subsequent errors to add
         */
        public void addErrors(String... errors) {
            if (errors == null) {
                throw new IllegalArgumentException("errors is null");
            }

            this.errors.addAll(Arrays.asList(errors));
        }

        /**
         * Adds errors to the AttributeError.  The passed in errors cannot be null.
         *
         * @param errors any subsequent errors to add
         */
        public void addErrors(List<String> errors) {
            if (errors == null) {
                throw new IllegalArgumentException("errors is null");
            }

            this.errors.addAll(errors);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "attributeError";
        final static String TYPE_NAME = "attributeErrorType";

    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled
     * to XML.
     */
    static class Elements {

        final static String ATTRIBUTE_NAME = "attributeName";
        final static String ERROR = "error";
        final static String ERRORS = "errors";

    }

}